package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.Categories;
import com.alonsocorporation.pointofsale.entities.Products;
import com.alonsocorporation.pointofsale.entities.Suppliers;
import com.alonsocorporation.pointofsale.exceptions.ProductAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.ProductsRepository;
import com.alonsocorporation.pointofsale.repositories.SuppliersRepository;
import com.alonsocorporation.pointofsale.services.ProductsService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.alonsocorporation.pointofsale.dto.response.ProductDTO;
import com.alonsocorporation.pointofsale.repositories.CategoriesRepository;

@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final SuppliersRepository suppliersRepository;
    private final CategoriesRepository categoriesRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository, SuppliersRepository suppliersRepository,
            CategoriesRepository categoriesRepository) {
        this.productsRepository = productsRepository;
        this.suppliersRepository = suppliersRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<ProductDTO> getAll() {
        return productsRepository.findAll()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getById(Long id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return new ProductDTO(product);
    }

    @Override
public ProductDTO create(Products product) {
    productsRepository.findByName(product.getName()).ifPresent(existingProduct -> {
        throw new ProductAlreadyExistsException(product.getName());
    });

    // Si no se proporcionan proveedores, inicializa una lista vacía
    if (product.getSuppliers() == null) {
        product.setSuppliers(new ArrayList<>());
    }

    // Si se proporcionan proveedores, verifica que existan en la base de datos
    for (Suppliers supplier : product.getSuppliers()) {
        Suppliers existingSupplier = suppliersRepository.findById(supplier.getId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + supplier.getId()));

        existingSupplier.getProducts().add(product);
    }

    // Si se proporciona una categoría y su ID no es 0, verifica que exista en la base de datos
    if (product.getCategory() != null && product.getCategory().getId() != 0) {
        Categories category = categoriesRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id " + product.getCategory().getId()));
        product.setCategory(category);
    } else {
        // Si no se proporciona una categoría o el ID es 0, no la asignes
        product.setCategory(null);
    }

    Products savedProduct = productsRepository.save(product);

    return new ProductDTO(savedProduct);
}

    @Override
    public ProductDTO update(Long id, Products productDetails) {
        Optional<Products> productOptional = productsRepository.findById(id);
        if (productOptional.isPresent()) {
            Products product = productOptional.get();

            if (productDetails.getName() != null) {
                product.setName(productDetails.getName());
            }
            if (productDetails.getDescription() != null) {
                product.setDescription(productDetails.getDescription());
            }
            if (productDetails.getPrice() != null && productDetails.getPrice() >= 0) {
                product.setPrice(productDetails.getPrice());
            }
            if (productDetails.getStock() != null && productDetails.getStock() >= 0) {
                product.setStock(productDetails.getStock());
            }
            if (productDetails.getCategory() != null) {
                product.setCategory(productDetails.getCategory());
            }
            if (productDetails.getSuppliers() != null) {
                product.setSuppliers(productDetails.getSuppliers());
            }
            if (productDetails.getCostPrice() != null && productDetails.getCostPrice() >= 0) {
                product.setCostPrice(productDetails.getCostPrice());
            }
            if (productDetails.getDiscount() != null && productDetails.getDiscount() >= 0) {
                product.setDiscount(productDetails.getDiscount());
            }
            if (productDetails.getTaxRate() != null && productDetails.getTaxRate() >= 0) {
                product.setTaxRate(productDetails.getTaxRate());
            }
            if (productDetails.getImages() != null && !productDetails.getImages().isEmpty()) {
                product.setImages(productDetails.getImages());
            }

            return new ProductDTO(productsRepository.save(product));
        } else {
            throw new RuntimeException("Product not found with id " + id);
        }
    }

    @Override
    public void delete(Long id) {
        if (!productsRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productsRepository.deleteById(id);
    }
}
