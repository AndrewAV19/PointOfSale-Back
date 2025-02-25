package com.alonsocorporation.pointofsale.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alonsocorporation.pointofsale.entities.Role;
import com.alonsocorporation.pointofsale.entities.User;
import com.alonsocorporation.pointofsale.repositories.RoleRepository;
import com.alonsocorporation.pointofsale.services.UserService;

@Configuration
public class AppConfig {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Value("${user.passwordInitial}")
    private String passwordInitial;

     private final PasswordEncoder passwordEncoder;


    public AppConfig(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            if (adminRoleOptional.isEmpty()) {
                // Crear el rol ROLE_ADMIN
                Role adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);
                System.out.println("Rol ROLE_ADMIN creado exitosamente");
            }

            Optional<Role> userRoleOptional = roleRepository.findByName("ROLE_USER");
            if (userRoleOptional.isEmpty()) {
                // Crear el rol ROLE_USER
                Role userRole = new Role("ROLE_USER");
                roleRepository.save(userRole);
                System.out.println("Rol ROLE_USER creado exitosamente");
            }


            if (userService.findByEmail("admin@admin.com") == null) {
                // Crear el usuario Admin
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@admin.com"); 
                admin.setPassword(("123")); 
                admin.setPhone("12345");
                admin.setAddress("Calle 123");
                admin.setCity("Ciudad Ejemplo");
                admin.setState("Estado Ejemplo");
                admin.setZipCode(12345);
                admin.setCountry("México");
                admin.setEnabled(true);
    
 
                Iterable<Role> rolesIterable = roleRepository.findAllById(Arrays.asList(1L, 2L));
                List<Role> roles = (List<Role>) rolesIterable; 
                
                admin.setRoles(roles);
    
                // Guardar el usuario admin
                userService.save(admin);
                System.out.println("Usuario admin creado exitosamente");
            } 
        };
    }
    
}

