package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.dto.response.ShoppingDTO;
import com.alonsocorporation.pointofsale.entities.*;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.*;
import com.alonsocorporation.pointofsale.services.ShoppingService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {

    private final ShoppingRepository shoppingRepository;
    private final ProductsRepository productsRepository;
    private final SuppliersRepository suppliersRepository;
    private final ShoppingProductRepository shoppingProductRepository;

    public ShoppingServiceImpl(ShoppingRepository shoppingRepository, ProductsRepository productsRepository,
            SuppliersRepository suppliersRepository, ShoppingProductRepository shoppingProductRepository) {
        this.shoppingRepository = shoppingRepository;
        this.productsRepository = productsRepository;
        this.suppliersRepository = suppliersRepository;
        this.shoppingProductRepository = shoppingProductRepository;
    }

    @Override
    public List<ShoppingDTO> getAll() {
        return shoppingRepository.findAll()
                .stream()
                .map(ShoppingDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingDTO getById(Long id) {
        Shopping shopping = shoppingRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return new ShoppingDTO(shopping);
    }

    @Override
    public ShoppingDTO create(Shopping shopping) {
        if (shopping.getShoppingProducts() == null) {
            shopping.setShoppingProducts(new ArrayList<>());
        }

        for (ShoppingProduct shoppingProduct : shopping.getShoppingProducts()) {
            Products product = productsRepository.findById(shoppingProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id " + shoppingProduct.getProduct().getId()));

            product.setStock(product.getStock() + shoppingProduct.getQuantity());
            productsRepository.save(product);

            // Establece la relación bidireccional
            shoppingProduct.setShopping(shopping);
            shoppingProduct.setProduct(product);
        }

        if (shopping.getSupplier() != null && shopping.getSupplier().getId() != 0) {
            Suppliers supplier = suppliersRepository.findById(shopping.getSupplier().getId())
                    .orElseThrow(
                            () -> new RuntimeException("Supplier not found with id " + shopping.getSupplier().getId()));
            shopping.setSupplier(supplier);
        } else {
            shopping.setSupplier(null);
        }

        // Guarda la venta y los productos asociados
        Shopping savedShopping = shoppingRepository.save(shopping);
        return new ShoppingDTO(savedShopping);
    }

    @Override
    public ShoppingDTO update(Long id, Shopping salesDetails) {
        Optional<Shopping> shoppingOptional = shoppingRepository.findById(id);
        if (shoppingOptional.isPresent()) {
            Shopping shopping = shoppingOptional.get();

            // Devolver el stock de los productos antiguos antes de eliminarlos
            for (ShoppingProduct oldProduct : shopping.getShoppingProducts()) {
                Products product = productsRepository.findById(oldProduct.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Product not found with id " + oldProduct.getProduct().getId()));
                product.setStock(product.getStock() - oldProduct.getQuantity());
                productsRepository.save(product);
            }

            // Limpiar productos anteriores
            shopping.getShoppingProducts().clear();

            // Agregar nuevos productos y actualizar stock
            if (salesDetails.getShoppingProducts() != null) {
                for (ShoppingProduct shoppingProduct : salesDetails.getShoppingProducts()) {
                    Products product = productsRepository.findById(shoppingProduct.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Product not found with id " + shoppingProduct.getProduct().getId()));

                    product.setStock(product.getStock() + shoppingProduct.getQuantity());
                    productsRepository.save(product);

                    shoppingProduct.setShopping(shopping);
                    shopping.getShoppingProducts().add(shoppingProduct);
                }
            }

            if (salesDetails.getSupplier() != null) {
                shopping.setSupplier(salesDetails.getSupplier());
            }

            if (salesDetails.getAmount() != null && salesDetails.getAmount() >= 0) {
                shopping.setAmount(salesDetails.getAmount());
            }
            if (salesDetails.getTotal() != null && salesDetails.getTotal() >= 0) {
                shopping.setTotal(salesDetails.getTotal());
            }

            return new ShoppingDTO(shoppingRepository.save(shopping));
        } else {
            throw new RuntimeException("Sale not found with id " + id);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Shopping> shoppingOptional = shoppingRepository.findById(id);
        if (shoppingOptional.isPresent()) {
            Shopping shopping = shoppingOptional.get();

            for (ShoppingProduct shoppingProduct : shopping.getShoppingProducts()) {
                Products product = shoppingProduct.getProduct();
                product.setStock(product.getStock() + shoppingProduct.getQuantity());
                productsRepository.save(product);
            }

            shoppingProductRepository.deleteAll(shopping.getShoppingProducts());
            shoppingRepository.delete(shopping);
        } else {
            throw new ProductNotFoundException(id);
        }
    }
}