package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.Products;
import com.alonsocorporation.pointofsale.exceptions.ProductAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.ProductsRepository;
import com.alonsocorporation.pointofsale.services.ProductsService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Products update(Long id, Products products) {
        return productsRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(products.getName());
                    existingProduct.setDescription(products.getDescription());
                    existingProduct.setPrice(products.getPrice());
                    existingProduct.setStock(products.getStock());
                    existingProduct.setCategory(products.getCategory());
                    existingProduct.setSupplier(products.getSupplier());
                    existingProduct.setCostPrice(products.getCostPrice());
                    existingProduct.setDiscount(products.getDiscount());
                    existingProduct.setTaxRate(products.getTaxRate());
                    existingProduct.setImages(products.getImages());

                    return productsRepository.save(existingProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        if (!productsRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productsRepository.deleteById(id);
    }
}
