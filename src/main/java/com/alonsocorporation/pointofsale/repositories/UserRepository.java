package com.alonsocorporation.pointofsale.repositories;

import com.alonsocorporation.pointofsale.entities.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
    
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
