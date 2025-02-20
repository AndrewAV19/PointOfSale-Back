package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Suppliers;
import java.util.List;

public interface SuppliersService {
    List<Suppliers> getAll();
    Suppliers getById(Long id);
    Suppliers create(Suppliers suppliers);
    Suppliers update(Long id, Suppliers suppliers);
    void delete(Long id);
}