package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.User;
import java.util.List;

public interface UserService {
    
    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);
}
