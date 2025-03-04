package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.dto.response.ShoppingDTO;
import com.alonsocorporation.pointofsale.entities.Shopping;
import com.alonsocorporation.pointofsale.services.ShoppingService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;

    public ShoppingController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @GetMapping
    public List<ShoppingDTO> getAllShopping() {
        return shoppingService.getAll();
    }

    @GetMapping("/{id}")
    public ShoppingDTO getShoppingById(@PathVariable Long id) {
        return shoppingService.getById(id);
    }

    @PostMapping
    public ShoppingDTO createShopping(@RequestBody Shopping shopping) {
        return shoppingService.create(shopping);
    }

    @PutMapping("/{id}")
    public ShoppingDTO updateShopping(@PathVariable Long id, @RequestBody Shopping shoppingDetails) {
        return shoppingService.update(id, shoppingDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteShopping(@PathVariable Long id) {
        shoppingService.delete(id);
    }
}