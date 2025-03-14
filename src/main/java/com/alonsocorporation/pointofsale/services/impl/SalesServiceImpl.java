package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.dto.response.ClientDTO;
import com.alonsocorporation.pointofsale.dto.response.ClientDebtDTO;
import com.alonsocorporation.pointofsale.dto.response.DailyIncomeDTO;
import com.alonsocorporation.pointofsale.dto.response.MonthlyIncomeDTO;
import com.alonsocorporation.pointofsale.dto.response.ProductsDTO;
import com.alonsocorporation.pointofsale.dto.response.SalesDTO;
import com.alonsocorporation.pointofsale.dto.response.YearlyIncomeDTO;
import com.alonsocorporation.pointofsale.entities.*;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.*;
import com.alonsocorporation.pointofsale.services.SalesService;
import com.alonsocorporation.pointofsale.services.TicketService;

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
    private final UserRepository userRepository;
    private final SaleProductRepository saleProductRepository;
    private final TicketService ticketService;

    public SalesServiceImpl(SalesRepository salesRepository, ProductsRepository productsRepository,
            ClientsRepository clientsRepository, UserRepository userRepository,
            SaleProductRepository saleProductRepository, TicketService ticketService) {
        this.salesRepository = salesRepository;
        this.productsRepository = productsRepository;
        this.clientsRepository = clientsRepository;
        this.userRepository = userRepository;
        this.saleProductRepository = saleProductRepository;
        this.ticketService = ticketService;
    }

    @Override
    public List<SalesDTO> getAll() {
        return salesRepository.findAll()
                .stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
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

        double totalSale = 0.0;

        for (SaleProduct saleProduct : sale.getSaleProducts()) {
            Products product = productsRepository.findById(saleProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id " + saleProduct.getProduct().getId()));

            if (product.getStock() < saleProduct.getQuantity()) {
                throw new RuntimeException("Not enough stock for product " + product.getName());
            }

            // Asignar el descuento del producto al SaleProduct
            saleProduct.setDiscount(product.getDiscount());

            // Aplicar descuento al precio del producto
            double productPrice = product.getPrice();
            double discount = saleProduct.getDiscount() != null ? saleProduct.getDiscount() : 0.0;
            double discountedPrice = productPrice * (1 - discount / 100);

            // Calcular el subtotal con el descuento aplicado
            double subtotal = discountedPrice * saleProduct.getQuantity();
            totalSale += subtotal;

            // Actualizar el stock del producto
            product.setStock(product.getStock() - saleProduct.getQuantity());
            productsRepository.save(product);

            // Establece la relación bidireccional
            saleProduct.setSale(sale);
            saleProduct.setProduct(product);
        }

        // Establecer el total de la venta
        sale.setTotal(totalSale);

        if (sale.getClient() != null && sale.getClient().getId() != 0) {
            Clients client = clientsRepository.findById(sale.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client not found with id " + sale.getClient().getId()));
            sale.setClient(client);
        } else {
            sale.setClient(null);
        }

        if (sale.getUser() != null && sale.getUser().getId() != 0) {
            User user = userRepository.findById(sale.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + sale.getUser().getId()));
            sale.setUser(user);
        } else {
            sale.setClient(null);
        }

        // Guarda la venta y los productos asociados
        Sales savedSale = salesRepository.save(sale);
        SalesDTO salesDTO = new SalesDTO(savedSale);

        // Imprimir el ticket
        ticketService.printTicket(salesDTO);

        return salesDTO;
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

            double totalSale = 0.0; 

            if (salesDetails.getSaleProducts() != null) {
                for (SaleProduct saleProduct : salesDetails.getSaleProducts()) {
                    Products product = productsRepository.findById(saleProduct.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Product not found with id " + saleProduct.getProduct().getId()));

                    if (product.getStock() < saleProduct.getQuantity()) {
                        throw new RuntimeException("Not enough stock for product " + product.getName());
                    }

                    // Asignar el descuento del producto al SaleProduct
                    saleProduct.setDiscount(product.getDiscount());

                    // Aplicar descuento al precio del producto
                    double productPrice = product.getPrice();
                    double discount = saleProduct.getDiscount() != null ? saleProduct.getDiscount() : 0.0;
                    double discountedPrice = productPrice * (1 - discount / 100);

                    // Calcular el subtotal con el descuento aplicado
                    double subtotal = discountedPrice * saleProduct.getQuantity();
                    totalSale += subtotal;

                    // Actualizar el stock del producto
                    product.setStock(product.getStock() - saleProduct.getQuantity());
                    productsRepository.save(product);

                    saleProduct.setSale(sale);
                    sale.getSaleProducts().add(saleProduct);
                }
            }

            sale.setTotal(totalSale);

            if (salesDetails.getClient() != null) {
                sale.setClient(salesDetails.getClient());
            }

            if (salesDetails.getUser() != null) {
                sale.setUser(salesDetails.getUser());
            }

            if (salesDetails.getAmount() != null && salesDetails.getAmount() >= 0) {
                sale.setAmount(salesDetails.getAmount());
            }
            if (salesDetails.getState() != null) {
                sale.setState(salesDetails.getState());
            }

            // Guardar la venta actualizada
            Sales updatedSale = salesRepository.save(sale);
            return new SalesDTO(updatedSale);
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
    public void cancel(Long id) {
        Optional<Sales> saleOptional = salesRepository.findById(id);
        if (saleOptional.isPresent()) {
            Sales sale = saleOptional.get();

            sale.setState("cancelada");

            // Devolver el stock de los productos asociados a la venta
            for (SaleProduct saleProduct : sale.getSaleProducts()) {
                Products product = saleProduct.getProduct();
                product.setStock(product.getStock() + saleProduct.getQuantity());
                productsRepository.save(product);
            }

            // Guardar la venta con el estado actualizado
            salesRepository.save(sale);
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
    public DailyIncomeDTO getDailyIncome(int year, int month, int day) {
        LocalDateTime startOfDay = LocalDateTime.of(year, month, day, 0, 0, 0);
        LocalDateTime endOfDay = LocalDateTime.of(year, month, day, 23, 59, 59);

        // Obtener ventas del día y excluir las canceladas
        List<Sales> dailySales = salesRepository.findByCreatedAtBetween(startOfDay, endOfDay)
                .stream()
                .filter(sale -> !"cancelada".equalsIgnoreCase(sale.getState()))
                .collect(Collectors.toList());

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

    @Override
    public MonthlyIncomeDTO getMonthlyIncome(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, startOfMonth.toLocalDate().lengthOfMonth(), 23, 59,
                59);

        // Obtener ventas del mes y excluir las canceladas
        List<Sales> monthlySales = salesRepository.findByCreatedAtBetween(startOfMonth, endOfMonth)
                .stream()
                .filter(sale -> !"cancelada".equalsIgnoreCase(sale.getState()))
                .collect(Collectors.toList());

        // Calcular ingresos totales
        Double totalIncome = monthlySales.stream()
                .mapToDouble(sale -> {
                    if ("pendiente".equalsIgnoreCase(sale.getState())) {
                        return sale.getAmount() != null ? sale.getAmount() : 0.0;
                    } else {
                        return sale.getTotal() != null ? sale.getTotal() : 0.0;
                    }
                })
                .sum();

        // Número de transacciones
        Integer numberOfTransactions = monthlySales.size();

        // Ticket promedio
        Double averageTicket = numberOfTransactions > 0
                ? BigDecimal.valueOf(totalIncome / numberOfTransactions)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        // Ingresos por día
        Map<Integer, Double> incomeByDay = monthlySales.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getCreatedAt().getDayOfMonth(),
                        Collectors.summingDouble(sale -> {
                            if ("pendiente".equalsIgnoreCase(sale.getState())) {
                                return sale.getAmount() != null ? sale.getAmount() : 0.0;
                            } else {
                                return sale.getTotal() != null ? sale.getTotal() : 0.0;
                            }
                        })));

        // Últimas 5 transacciones
        List<SalesDTO> lastFiveTransactions = monthlySales.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .limit(5)
                .map(SalesDTO::new)
                .collect(Collectors.toList());

        return new MonthlyIncomeDTO(totalIncome, numberOfTransactions, averageTicket, incomeByDay,
                lastFiveTransactions);
    }

    @Override
    public YearlyIncomeDTO getYearlyIncome(int year) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        // Obtener ventas del año y excluir las canceladas
        List<Sales> yearlySales = salesRepository.findByCreatedAtBetween(startOfYear, endOfYear)
                .stream()
                .filter(sale -> !"cancelada".equalsIgnoreCase(sale.getState()))
                .collect(Collectors.toList());

        // Calcular ingresos totales
        Double totalIncome = yearlySales.stream()
                .mapToDouble(sale -> {
                    if ("pendiente".equalsIgnoreCase(sale.getState())) {
                        return sale.getAmount() != null ? sale.getAmount() : 0.0;
                    } else {
                        return sale.getTotal() != null ? sale.getTotal() : 0.0;
                    }
                })
                .sum();

        // Número de transacciones
        Integer numberOfTransactions = yearlySales.size();

        // Ticket promedio
        Double averageTicket = numberOfTransactions > 0
                ? BigDecimal.valueOf(totalIncome / numberOfTransactions)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        // Ingresos por mes
        Map<Integer, Double> incomeByMonth = yearlySales.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getCreatedAt().getMonthValue(),
                        Collectors.summingDouble(sale -> {
                            if ("pendiente".equalsIgnoreCase(sale.getState())) {
                                return sale.getAmount() != null ? sale.getAmount() : 0.0;
                            } else {
                                return sale.getTotal() != null ? sale.getTotal() : 0.0;
                            }
                        })));

        // Últimas 5 transacciones
        List<SalesDTO> lastFiveTransactions = yearlySales.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .limit(5)
                .map(SalesDTO::new)
                .collect(Collectors.toList());

        return new YearlyIncomeDTO(totalIncome, numberOfTransactions, averageTicket, incomeByMonth,
                lastFiveTransactions);
    }
}