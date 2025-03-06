package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.entities.DataPointOfSale;
import com.alonsocorporation.pointofsale.services.DataPointOfSaleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataPointOfSaleController {

    private final DataPointOfSaleService dataPointOfSaleService;

     public DataPointOfSaleController(DataPointOfSaleService dataPointOfSaleService) {
        this.dataPointOfSaleService = dataPointOfSaleService;
    }


    @PutMapping("/{id}")
    public DataPointOfSale updateData(@PathVariable Long id, @RequestBody DataPointOfSale dataPointOfSale) {
        return dataPointOfSaleService.update(id, dataPointOfSale);
    }
}