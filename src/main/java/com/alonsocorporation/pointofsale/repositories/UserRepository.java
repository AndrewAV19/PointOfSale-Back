package com.alonsocorporation.pointofsale.repositories;

import com.alonsocorporation.pointofsale.entities.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
    
    boolean existsByEmail(String username);

    Optional<User> findByEmail(String email);
}
