package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.dto.response.ClientDebtDTO;
import com.alonsocorporation.pointofsale.dto.response.SalesDTO;
import com.alonsocorporation.pointofsale.entities.Sales;
import com.alonsocorporation.pointofsale.services.SalesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public List<SalesDTO> getAllSales() {
        return salesService.getAll();
    }

    @GetMapping("/{id}")
    public SalesDTO getSaleById(@PathVariable Long id) {
        return salesService.getById(id);
    }

    @PostMapping
    public SalesDTO createSale(@RequestBody Sales sale) {
        return salesService.create(sale);
    }

    @PutMapping("/{id}")
    public SalesDTO updateSale(@PathVariable Long id, @RequestBody Sales saleDetails) {
        return salesService.update(id, saleDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteSale(@PathVariable Long id) {
        salesService.delete(id);
    }

    @GetMapping("/client-debts/{clientId}")
    public List<ClientDebtDTO> getClientDebts(@PathVariable Long clientId) {
        return salesService.getClientDebts(clientId);
    }
}