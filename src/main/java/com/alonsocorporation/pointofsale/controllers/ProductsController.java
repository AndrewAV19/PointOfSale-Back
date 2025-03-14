package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.entities.Products;
import com.alonsocorporation.pointofsale.services.ProductsService;
import com.alonsocorporation.pointofsale.services.QRCodeLabelService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.alonsocorporation.pointofsale.dto.response.ProductDTO;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productsService;
    private final QRCodeLabelService qrCodeLabelService;

    public ProductsController(ProductsService productsService, QRCodeLabelService qrCodeLabelService) {
        this.productsService = productsService;
        this.qrCodeLabelService = qrCodeLabelService;
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

    @GetMapping("/{id}/label")
    public ResponseEntity<byte[]> generateProductLabel(@PathVariable Long id) {
        ProductDTO product = productsService.getById(id);
        return qrCodeLabelService.generateLabel(product);
    }

    @PutMapping("/{id}/generate-qr")
    public ResponseEntity<ProductDTO> generateQrCode(@PathVariable Long id) {
        ProductDTO updatedProduct = productsService.generateQrCode(id);
        return ResponseEntity.ok(updatedProduct);
    }
}
