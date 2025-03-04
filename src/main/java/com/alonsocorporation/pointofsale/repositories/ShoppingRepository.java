package com.alonsocorporation.pointofsale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alonsocorporation.pointofsale.entities.Shopping;

public interface ShoppingRepository extends JpaRepository<Shopping, Long> {

}
