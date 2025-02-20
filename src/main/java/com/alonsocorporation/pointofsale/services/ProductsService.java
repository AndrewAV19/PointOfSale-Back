package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Products;
import java.util.List;

public interface ProductsService {
    List<Products> getAll();
    Products getById(Long id);
    Products create(Products products);
    Products update(Long id, Products products);
    void delete(Long id);
}