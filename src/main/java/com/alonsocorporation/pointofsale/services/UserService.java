package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    
    List<User> findAll();

    User save(User user);

    Optional<User> findById(Long id);

    User update(Long id, User userDetails);

    void delete(Long id);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    boolean changePassword(Long id, String currentPassword, String newPassword);
}