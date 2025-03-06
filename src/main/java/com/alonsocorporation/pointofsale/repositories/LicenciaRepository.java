package com.alonsocorporation.pointofsale.repositories;

import com.alonsocorporation.pointofsale.entities.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
    Licencia findByClaveLicencia(String claveLicencia);
}
