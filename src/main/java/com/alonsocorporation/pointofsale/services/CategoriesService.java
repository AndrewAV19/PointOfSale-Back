package com.alonsocorporation.pointofsale.services;

import java.util.List;
import com.alonsocorporation.pointofsale.entities.Categories;

public interface  CategoriesService {
     List<Categories> getAll();
     Categories getById(Long id);
     Categories create(Categories products);
     Categories update(Long id, Categories products);
    void delete(Long id);
}

