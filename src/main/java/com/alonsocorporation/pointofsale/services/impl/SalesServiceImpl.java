package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.dto.response.ClientDTO;
import com.alonsocorporation.pointofsale.dto.response.ClientDebtDTO;
import com.alonsocorporation.pointofsale.dto.response.DailyIncomeDTO;
import com.alonsocorporation.pointofsale.dto.response.ProductsDTO;
import com.alonsocorporation.pointofsale.dto.response.SalesDTO;
import com.alonsocorporation.pointofsale.entities.*;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.*;
import com.alonsocorporation.pointofsale.services.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<SalesDTO> getSalesByState(String state) {
        return salesRepository.findByState(state)
                .stream()
                .map(SalesDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientDebtDTO> getClientDebts(Long clientId) {
        // Obtener todas las ventas pendientes del cliente específico
        List<Sales> pendingSales = salesRepository.findByClientIdAndState(clientId, "pendiente");

        // Si no hay ventas pendientes, retornar una lista vacía
        if (pendingSales.isEmpty()) {
            return new ArrayList<>();
        }

        // Consolidar productos y montos
        Map<Long, ProductsDTO> productMap = new HashMap<>();
        double totalAmount = 0.0;
        double paidAmount = 0.0;
        double remainingBalance = 0.0;

        for (Sales sale : pendingSales) {
            totalAmount += sale.getTotal();
            paidAmount += sale.getAmount();
            remainingBalance += (sale.getTotal() - sale.getAmount());

            for (SaleProduct saleProduct : sale.getSaleProducts()) {
                ProductsDTO productDTO = new ProductsDTO(saleProduct.getProduct(), saleProduct.getQuantity());

                if (productMap.containsKey(productDTO.getId())) {
                    ProductsDTO existingProduct = productMap.get(productDTO.getId());
                    existingProduct.setQuantity(existingProduct.getQuantity() + productDTO.getQuantity());
                } else {
                    productMap.put(productDTO.getId(), productDTO);
                }
            }
        }

        // Convertir el mapa de productos a una lista
        List<ProductsDTO> products = new ArrayList<>(productMap.values());

        // Obtener el cliente
        Clients client = pendingSales.get(0).getClient();
        ClientDTO clientDTO = new ClientDTO(client);

        // Crear el ClientDebtDTO consolidado
        ClientDebtDTO clientDebt = new ClientDebtDTO(
                clientDTO,
                products,
                totalAmount,
                paidAmount,
                remainingBalance,
                "pendiente");

        return List.of(clientDebt);
    }

    @Override
    public DailyIncomeDTO getDailyIncome() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Sales> dailySales = salesRepository.findByCreatedAtBetween(startOfDay, endOfDay);

        // Calcular ingresos totales
        Double totalIncome = dailySales.stream()
                .mapToDouble(sale -> {
                    if ("pendiente".equalsIgnoreCase(sale.getState())) {
                        return sale.getAmount() != null ? sale.getAmount() : 0.0;
                    } else {
                        return sale.getTotal() != null ? sale.getTotal() : 0.0;
                    }
                })
                .sum();

        // Número de transacciones
        Integer numberOfTransactions = dailySales.size();

        // Ticket promedio
        Double averageTicket = numberOfTransactions > 0
                ? BigDecimal.valueOf(totalIncome / numberOfTransactions)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        // Ingresos por hora
        Map<Integer, Double> incomeByHour = dailySales.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getCreatedAt().getHour(),
                        Collectors.summingDouble(sale -> {
                            if ("pendiente".equalsIgnoreCase(sale.getState())) {
                                return sale.getAmount() != null ? sale.getAmount() : 0.0;
                            } else {
                                return sale.getTotal() != null ? sale.getTotal() : 0.0;
                            }
                        })));

        // Últimas 5 transacciones
        List<SalesDTO> lastFiveTransactions = dailySales.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .limit(5)
                .map(SalesDTO::new)
                .collect(Collectors.toList());

        return new DailyIncomeDTO(totalIncome, numberOfTransactions, averageTicket, incomeByHour, lastFiveTransactions);
    }
}