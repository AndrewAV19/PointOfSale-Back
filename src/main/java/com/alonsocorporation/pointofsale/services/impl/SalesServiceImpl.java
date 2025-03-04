package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.dto.response.SalesDTO;
import com.alonsocorporation.pointofsale.entities.*;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.*;
import com.alonsocorporation.pointofsale.services.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final ProductsRepository productsRepository;
    private final ClientsRepository clientsRepository;
    private final SaleProductRepository saleProductRepository;

    public SalesServiceImpl(SalesRepository salesRepository, ProductsRepository productsRepository,
            ClientsRepository clientsRepository, SaleProductRepository saleProductRepository) {
        this.salesRepository = salesRepository;
        this.productsRepository = productsRepository;
        this.clientsRepository = clientsRepository;
        this.saleProductRepository = saleProductRepository;
    }

    @Override
    public List<SalesDTO> getAll() {
        return salesRepository.findAll()
                .stream()
                .map(SalesDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public SalesDTO getById(Long id) {
        Sales sales = salesRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return new SalesDTO(sales);
    }

    @Override
    public SalesDTO create(Sales sale) {
        if (sale.getSaleProducts() == null) {
            sale.setSaleProducts(new ArrayList<>());
        }

        for (SaleProduct saleProduct : sale.getSaleProducts()) {
            Products product = productsRepository.findById(saleProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id " + saleProduct.getProduct().getId()));

            if (product.getStock() < saleProduct.getQuantity()) {
                throw new RuntimeException("Not enough stock for product " + product.getName());
            }

            product.setStock(product.getStock() - saleProduct.getQuantity());
            productsRepository.save(product);

            // Establece la relación bidireccional
            saleProduct.setSale(sale);
            saleProduct.setProduct(product);
        }

        if (sale.getClient() != null && sale.getClient().getId() != 0) {
            Clients client = clientsRepository.findById(sale.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client not found with id " + sale.getClient().getId()));
            sale.setClient(client);
        } else {
            sale.setClient(null);
        }

        // Guarda la venta y los productos asociados
        Sales savedSale = salesRepository.save(sale);
        return new SalesDTO(savedSale);
    }

    @Override
    public SalesDTO update(Long id, Sales salesDetails) {
        Optional<Sales> saleOptional = salesRepository.findById(id);
        if (saleOptional.isPresent()) {
            Sales sale = saleOptional.get();

            // Devolver stock de los productos antiguos antes de eliminarlos
            for (SaleProduct oldProduct : sale.getSaleProducts()) {
                Products product = productsRepository.findById(oldProduct.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Product not found with id " + oldProduct.getProduct().getId()));
                product.setStock(product.getStock() + oldProduct.getQuantity());
                productsRepository.save(product);
            }

            // Limpiar productos anteriores
            sale.getSaleProducts().clear();

            // Agregar nuevos productos y RESTAR stock
            if (salesDetails.getSaleProducts() != null) {
                for (SaleProduct saleProduct : salesDetails.getSaleProducts()) {
                    Products product = productsRepository.findById(saleProduct.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Product not found with id " + saleProduct.getProduct().getId()));

                    if (product.getStock() < saleProduct.getQuantity()) {
                        throw new RuntimeException("Not enough stock for product " + product.getName());
                    }

                    product.setStock(product.getStock() - saleProduct.getQuantity());
                    productsRepository.save(product);

                    saleProduct.setSale(sale);
                    sale.getSaleProducts().add(saleProduct);
                }
            }

            if (salesDetails.getClient() != null) {
                sale.setClient(salesDetails.getClient());
            }

            if (salesDetails.getAmount() != null && salesDetails.getAmount() >= 0) {
                sale.setAmount(salesDetails.getAmount());
            }
            if (salesDetails.getState() != null) {
                sale.setState(salesDetails.getState());
            }
            if (salesDetails.getTotal() != null && salesDetails.getTotal() >= 0) {
                sale.setTotal(salesDetails.getTotal());
            }

            return new SalesDTO(salesRepository.save(sale));
        } else {
            throw new RuntimeException("Sale not found with id " + id);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Sales> saleOptional = salesRepository.findById(id);
        if (saleOptional.isPresent()) {
            Sales sale = saleOptional.get();

            for (SaleProduct saleProduct : sale.getSaleProducts()) {
                Products product = saleProduct.getProduct();
                product.setStock(product.getStock() + saleProduct.getQuantity());
                productsRepository.save(product);
            }

            saleProductRepository.deleteAll(sale.getSaleProducts());
            salesRepository.delete(sale);
        } else {
            throw new ProductNotFoundException(id);
        }
    }
}