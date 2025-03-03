package com.alonsocorporation.pointofsale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alonsocorporation.pointofsale.entities.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long> {

}
