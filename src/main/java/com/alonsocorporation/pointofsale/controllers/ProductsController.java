package com.alonsocorporation.pointofsale.controllers;


import com.alonsocorporation.pointofsale.entities.Products;
import com.alonsocorporation.pointofsale.services.ProductsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.alonsocorporation.pointofsale.dto.response.ProductDTO;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productsService.getAll();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productsService.getById(id);
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody Products product) {
        return productsService.create(product);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody Products productDetails) {
        return productsService.update(id, productDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productsService.delete(id);
    }
}
