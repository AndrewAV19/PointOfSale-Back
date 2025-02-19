package com.alonsocorporation.pointofsale.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alonsocorporation.pointofsale.entities.Clients;

public interface ClientsRepository extends JpaRepository<Clients, Long> {
    Optional<Clients> findByEmail(String email);
}
