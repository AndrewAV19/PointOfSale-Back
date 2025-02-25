package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.*;
import com.alonsocorporation.pointofsale.repositories.*;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
            List<Role> roles = (List<Role>) roleRepository.findAllById(user.getRoleIds());
            user.setRoles(roles);
        }
    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    
        return repository.save(user);
    }
    

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = repository.findByEmail(email);
        return userOptional.orElse(null);
    }

}