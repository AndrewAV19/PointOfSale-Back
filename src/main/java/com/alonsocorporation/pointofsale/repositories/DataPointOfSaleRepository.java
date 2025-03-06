package com.alonsocorporation.pointofsale.repositories;

import com.alonsocorporation.pointofsale.entities.DataPointOfSale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataPointOfSaleRepository extends JpaRepository<DataPointOfSale, Long> {
}