package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.entities.Clients;
import com.alonsocorporation.pointofsale.entities.Products;
import com.alonsocorporation.pointofsale.services.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = productsService.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable Long id) {
        Products products = productsService.getById(id);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Products> createProduct(@RequestBody Products products) {
        Products newProduct = productsService.create(products);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Products product, BindingResult result) {
        try {
            Products updatedProduct = productsService.update(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
