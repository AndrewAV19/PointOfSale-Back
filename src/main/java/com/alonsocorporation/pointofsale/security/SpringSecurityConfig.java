package com.alonsocorporation.pointofsale.security;

import com.alonsocorporation.pointofsale.security.filter.JwtAuthenticationFilter;
import com.alonsocorporation.pointofsale.security.filter.JwtValidationFilter;
import com.alonsocorporation.pointofsale.services.LicenciaService;
import com.alonsocorporation.pointofsale.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final LicenciaService licenciaService;
    private final UserService userService;

    public SpringSecurityConfig(
            AuthenticationConfiguration authenticationConfiguration,
            UserService userService, LicenciaService licenciaService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userService = userService;
        this.licenciaService = licenciaService;
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers( "/licencia/**").permitAll()
                .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userService,licenciaService))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
