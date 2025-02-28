package com.alonsocorporation.pointofsale.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alonsocorporation.pointofsale.entities.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findByName(String name);
}