package com.alonsocorporation.pointofsale.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alonsocorporation.pointofsale.entities.Suppliers;

public interface SuppliersRepository extends JpaRepository<Suppliers, Long> {
    Optional<Suppliers> findByEmail(String email);
}
