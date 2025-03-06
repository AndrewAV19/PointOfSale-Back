package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.entities.DataPointOfSale;
import com.alonsocorporation.pointofsale.services.DataPointOfSaleService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataPointOfSaleController {

    private final DataPointOfSaleService dataPointOfSaleService;

     public DataPointOfSaleController(DataPointOfSaleService dataPointOfSaleService) {
        this.dataPointOfSaleService = dataPointOfSaleService;
    }

    @GetMapping
    public ResponseEntity<List<DataPointOfSale>> getAll() {
        List<DataPointOfSale> dataPointOfSales = dataPointOfSaleService.getAll();
        return ResponseEntity.ok(dataPointOfSales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataPointOfSale> getById(@PathVariable Long id) {
        DataPointOfSale dataPointOfSale = dataPointOfSaleService.getById(id);
        return ResponseEntity.ok(dataPointOfSale);
    }

    @PutMapping("/{id}")
    public DataPointOfSale updateData(@PathVariable Long id, @RequestBody DataPointOfSale dataPointOfSale) {
        return dataPointOfSaleService.update(id, dataPointOfSale);
    }
}