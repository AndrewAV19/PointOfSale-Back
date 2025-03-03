package com.alonsocorporation.pointofsale.repositories;

import com.alonsocorporation.pointofsale.entities.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {
}