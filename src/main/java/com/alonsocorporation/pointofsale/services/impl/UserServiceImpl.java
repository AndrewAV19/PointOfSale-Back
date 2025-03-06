package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.*;
import com.alonsocorporation.pointofsale.repositories.*;
import com.alonsocorporation.pointofsale.services.UserService;

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
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User update(Long id, User userDetails) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
    
            // Actualizar solo los campos que no son null
            if (userDetails.getName() != null) {
                user.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPhone() != null) {
                user.setPhone(userDetails.getPhone());
            }
            if (userDetails.getAddress() != null) {
                user.setAddress(userDetails.getAddress());
            }
            if (userDetails.getCity() != null) {
                user.setCity(userDetails.getCity());
            }
            if (userDetails.getState() != null) {
                user.setState(userDetails.getState());
            }
            if (userDetails.getZipCode() != null) {
                user.setZipCode(userDetails.getZipCode());
            }
            if (userDetails.getCountry() != null) {
                user.setCountry(userDetails.getCountry());
            }
    
            // Actualizar roles si se proporcionan nuevos
            if (userDetails.getRoleIds() != null && !userDetails.getRoleIds().isEmpty()) {
                List<Role> roles = (List<Role>) roleRepository.findAllById(userDetails.getRoleIds());
                user.setRoles(roles);
            }
    
            return repository.save(user);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
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

    @Override
    @Transactional
    public boolean changePassword(Long id, String currentPassword, String newPassword) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                repository.save(user);
                return true;
            } else {
                throw new RuntimeException("Current password is incorrect");
            }
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }
}