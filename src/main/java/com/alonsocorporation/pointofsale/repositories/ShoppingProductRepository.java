package com.alonsocorporation.pointofsale.repositories;

import com.alonsocorporation.pointofsale.entities.ShoppingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingProductRepository extends JpaRepository<ShoppingProduct, Long> {
}