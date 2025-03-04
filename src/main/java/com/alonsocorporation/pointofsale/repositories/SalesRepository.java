package com.alonsocorporation.pointofsale.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alonsocorporation.pointofsale.entities.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByState(String state);
    List<Sales> findByClientIdAndState(Long clientId, String state);
}
