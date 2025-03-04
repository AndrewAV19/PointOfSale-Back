package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Shopping;
import java.util.List;
import com.alonsocorporation.pointofsale.dto.response.ShoppingDTO;

public interface ShoppingService {
    List<ShoppingDTO> getAll();
    ShoppingDTO getById(Long id);
    ShoppingDTO create(Shopping shopping);
    ShoppingDTO update(Long id, Shopping shopping);
    void delete(Long id);
}