package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Products;

import java.util.List;

import com.alonsocorporation.pointofsale.dto.response.ProductDTO;

public interface ProductsService {
    List<ProductDTO> getAll();
    ProductDTO getById(Long id);
    ProductDTO create(Products products);
    ProductDTO update(Long id, Products products);
    void delete(Long id);
}