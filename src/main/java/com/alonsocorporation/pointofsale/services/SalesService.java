package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Sales;
import java.util.List;
import com.alonsocorporation.pointofsale.dto.response.SalesDTO;

public interface SalesService {
    List<SalesDTO> getAll();
    SalesDTO getById(Long id);
    SalesDTO create(Sales sales);
    SalesDTO update(Long id, Sales sales);
    void delete(Long id);
}