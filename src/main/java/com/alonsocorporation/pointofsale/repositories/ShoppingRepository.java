package com.alonsocorporation.pointofsale.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alonsocorporation.pointofsale.entities.Shopping;

public interface ShoppingRepository extends JpaRepository<Shopping, Long> {
    List<Shopping> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
