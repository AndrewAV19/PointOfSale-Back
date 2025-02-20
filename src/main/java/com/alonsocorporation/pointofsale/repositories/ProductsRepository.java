package com.alonsocorporation.pointofsale.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alonsocorporation.pointofsale.entities.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByName(String email);
}
