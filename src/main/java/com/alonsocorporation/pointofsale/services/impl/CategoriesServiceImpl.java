package com.alonsocorporation.pointofsale.services.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import com.alonsocorporation.pointofsale.entities.Categories;
import com.alonsocorporation.pointofsale.exceptions.ProductAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.ProductNotFoundException;
import com.alonsocorporation.pointofsale.repositories.CategoriesRepository;
import com.alonsocorporation.pointofsale.services.CategoriesService;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<Categories> getAll() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories getById(Long id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Categories create(Categories categories) {
        // Verificar si ya existe una categoria con el mismo name
        categoriesRepository.findByName(categories.getName()).ifPresent(existingClient -> {
            throw new ProductAlreadyExistsException(categories.getName());
        });

        return categoriesRepository.save(categories);
    }

    @Override
    public Categories update(Long id, Categories categoriesDetails) {
        Optional<Categories> categoryOptional = categoriesRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Categories category = categoryOptional.get();

            // Actualizar solo los campos que no son null
            if (categoriesDetails.getName() != null) {
                category.setName(categoriesDetails.getName());
            }
            if (categoriesDetails.getDescription() != null) {
                category.setDescription(categoriesDetails.getDescription());
            }
           
            return categoriesRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with id " + id);
        }
    }

    @Override
    public void delete(Long id) {
        if (!categoriesRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        categoriesRepository.deleteById(id);
    }
}
