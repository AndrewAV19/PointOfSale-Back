package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.Products;
import com.alonsocorporation.pointofsale.exceptions.ProductAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.ProductsRepository;
import com.alonsocorporation.pointofsale.services.ProductsService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public List<Products> getAll() {
        return productsRepository.findAll();
    }

    @Override
    public Products getById(Long id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Products create(Products products) {
        // Verificar si ya existe un producto con el mismo name
        productsRepository.findByName(products.getName()).ifPresent(existingClient -> {
            throw new ProductAlreadyExistsException(products.getName());
        });

        return productsRepository.save(products);
    }

    @Override
    public Products update(Long id, Products productDetails) {
        Optional<Products> productOptional = productsRepository.findById(id);
        if (productOptional.isPresent()) {
            Products product = productOptional.get();

            // Actualizar solo los campos que no son null
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
            if (productDetails.getSupplier() != null) {
                product.setSupplier(productDetails.getSupplier());
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

            return productsRepository.save(product);
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
