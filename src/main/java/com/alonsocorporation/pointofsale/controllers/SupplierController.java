package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.entities.Suppliers;
import com.alonsocorporation.pointofsale.services.SuppliersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SuppliersService suppliersService;

    public SupplierController(SuppliersService suppliersService) {
        this.suppliersService = suppliersService;
    }

    @GetMapping
    public ResponseEntity<List<Suppliers>> getAllSuppliers() {
        List<Suppliers> suppliers = suppliersService.getAll();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Suppliers> getSupplierById(@PathVariable Long id) {
        Suppliers suppliers = suppliersService.getById(id);
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping
    public ResponseEntity<Suppliers> createSupplier(@RequestBody Suppliers suppliers) {
        Suppliers newSupplier = suppliersService.create(suppliers);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSupplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Suppliers> updateSupplier(@PathVariable Long id, @RequestBody Suppliers suppliers) {
        Suppliers updatedSupplier = suppliersService.update(id, suppliers);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        suppliersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
