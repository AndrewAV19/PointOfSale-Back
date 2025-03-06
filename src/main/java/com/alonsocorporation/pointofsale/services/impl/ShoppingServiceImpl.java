package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.dto.response.DailyExpenseDTO;
import com.alonsocorporation.pointofsale.dto.response.MonthlyExpenseDTO;
import com.alonsocorporation.pointofsale.dto.response.ShoppingDTO;
import com.alonsocorporation.pointofsale.dto.response.YearlyExpenseDTO;
import com.alonsocorporation.pointofsale.entities.*;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.*;
import com.alonsocorporation.pointofsale.services.ShoppingService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {

    private final ShoppingRepository shoppingRepository;
    private final ProductsRepository productsRepository;
    private final SuppliersRepository suppliersRepository;
    private final UserRepository userRepository;
    private final ShoppingProductRepository shoppingProductRepository;

    public ShoppingServiceImpl(ShoppingRepository shoppingRepository, ProductsRepository productsRepository,
            SuppliersRepository suppliersRepository, UserRepository userRepository,
            ShoppingProductRepository shoppingProductRepository) {
        this.shoppingRepository = shoppingRepository;
        this.productsRepository = productsRepository;
        this.suppliersRepository = suppliersRepository;
        this.userRepository = userRepository;
        this.shoppingProductRepository = shoppingProductRepository;
    }

    @Override
    public List<ShoppingDTO> getAll() {
        return shoppingRepository.findAll()
                .stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
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

        if (shopping.getUser() != null && shopping.getUser().getId() != 0) {
            User user = userRepository.findById(shopping.getUser().getId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id " + shopping.getUser().getId()));
            shopping.setUser(user);
        } else {
            shopping.setUser(null);
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

            if (salesDetails.getUser() != null) {
                shopping.setUser(salesDetails.getUser());
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

                if (product.getStock() - shoppingProduct.getQuantity() < 0) {
                    throw new RuntimeException("El stock no puede ser negativo para el producto: " + product.getName());
                }

                product.setStock(product.getStock() - shoppingProduct.getQuantity());
                productsRepository.save(product);
            }

            shoppingProductRepository.deleteAll(shopping.getShoppingProducts());
            shoppingRepository.delete(shopping);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public DailyExpenseDTO getDailyExpense(int year, int month, int day) {
        LocalDateTime startOfDay = LocalDateTime.of(year, month, day, 0, 0, 0);
        LocalDateTime endOfDay = LocalDateTime.of(year, month, day, 23, 59, 59);

        List<Shopping> dailyShoppings = shoppingRepository.findByCreatedAtBetween(startOfDay, endOfDay);

        // Calcular egresos totales
        Double totalExpense = dailyShoppings.stream()
                .mapToDouble(Shopping::getTotal)
                .sum();

        // Número de transacciones
        Integer numberOfTransactions = dailyShoppings.size();

        // Ticket promedio
        Double averageTicket = numberOfTransactions > 0
                ? BigDecimal.valueOf(totalExpense / numberOfTransactions)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        // Egresos por hora
        Map<Integer, Double> expenseByHour = dailyShoppings.stream()
                .collect(Collectors.groupingBy(
                        shopping -> shopping.getCreatedAt().getHour(),
                        Collectors.summingDouble(Shopping::getTotal)));

        // Últimas 5 transacciones
        List<ShoppingDTO> lastFiveTransactions = dailyShoppings.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .limit(5)
                .map(ShoppingDTO::new)
                .collect(Collectors.toList());

        return new DailyExpenseDTO(totalExpense, numberOfTransactions, averageTicket, expenseByHour,
                lastFiveTransactions);
    }

    @Override
    public MonthlyExpenseDTO getMonthlyExpense(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, startOfMonth.toLocalDate().lengthOfMonth(), 23, 59,
                59);

        List<Shopping> monthlyShoppings = shoppingRepository.findByCreatedAtBetween(startOfMonth, endOfMonth);

        // Calcular egresos totales
        Double totalExpense = monthlyShoppings.stream()
                .mapToDouble(Shopping::getTotal)
                .sum();

        // Número de transacciones
        Integer numberOfTransactions = monthlyShoppings.size();

        // Ticket promedio
        Double averageTicket = numberOfTransactions > 0
                ? BigDecimal.valueOf(totalExpense / numberOfTransactions)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        // Egresos por día
        Map<Integer, Double> expenseByDay = monthlyShoppings.stream()
                .collect(Collectors.groupingBy(
                        shopping -> shopping.getCreatedAt().getDayOfMonth(),
                        Collectors.summingDouble(Shopping::getTotal)));

        // Últimas 5 transacciones
        List<ShoppingDTO> lastFiveTransactions = monthlyShoppings.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .limit(5)
                .map(ShoppingDTO::new)
                .collect(Collectors.toList());

        return new MonthlyExpenseDTO(totalExpense, numberOfTransactions, averageTicket, expenseByDay,
                lastFiveTransactions);
    }

    @Override
    public YearlyExpenseDTO getYearlyExpense(int year) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        List<Shopping> yearlyShoppings = shoppingRepository.findByCreatedAtBetween(startOfYear, endOfYear);

        // Calcular egresos totales
        Double totalExpense = yearlyShoppings.stream()
                .mapToDouble(Shopping::getTotal)
                .sum();

        // Número de transacciones
        Integer numberOfTransactions = yearlyShoppings.size();

        // Ticket promedio
        Double averageTicket = numberOfTransactions > 0
                ? BigDecimal.valueOf(totalExpense / numberOfTransactions)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()
                : 0.0;

        // Egresos por mes
        Map<Integer, Double> expenseByMonth = yearlyShoppings.stream()
                .collect(Collectors.groupingBy(
                        shopping -> shopping.getCreatedAt().getMonthValue(),
                        Collectors.summingDouble(Shopping::getTotal)));

        // Últimas 5 transacciones
        List<ShoppingDTO> lastFiveTransactions = yearlyShoppings.stream()
                .sorted((s1, s2) -> s2.getCreatedAt().compareTo(s1.getCreatedAt()))
                .limit(5)
                .map(ShoppingDTO::new)
                .collect(Collectors.toList());

        return new YearlyExpenseDTO(totalExpense, numberOfTransactions, averageTicket, expenseByMonth,
                lastFiveTransactions);
    }
}