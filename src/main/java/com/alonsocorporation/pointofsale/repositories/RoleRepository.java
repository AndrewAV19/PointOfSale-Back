package com.alonsocorporation.pointofsale.repositories;

import com.alonsocorporation.pointofsale.entities.Role;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

    List<Role> findByIdIn(List<Long> ids); 
    
}