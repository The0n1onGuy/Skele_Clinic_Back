package com.expedienteclinico.expedienteclinico.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // .cors(...) -> Mantén tu configuración CORS original aquí
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/warehouse/**", "/error").permitAll() // Login abierto
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()// Swagger abierto
                        // RBAC estricto:
                        .requestMatchers("/api/rpbi/**").hasAnyRole("ADMIN", "RPBI")
                        .requestMatchers("/api/rrhh/**").hasAnyRole("ADMIN", "RRHH")
                        .requestMatchers("/api/patients/**").hasAnyRole("ADMIN", "PATIENTS")
                        .requestMatchers("/api/emergencias/**").hasAnyRole("ADMIN", "EMERGENCIAS")
                        .requestMatchers("/api/morgue/**").hasAnyRole("ADMIN", "MORGUE")
                        .requestMatchers("/api/lyr/**").hasAnyRole("ADMIN", "LYR")
                        .requestMatchers("/api/appointments/**").hasAnyRole("ADMIN", "APPOINTMENTS")
                        .requestMatchers("/api/warehouse/**").hasAnyRole("ADMIN", "WAREHOUSE")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                // Inyectar nuestro filtro ANTES del filtro estándar de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 1. Se inyecta el servicio directamente en el constructor
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        // 2. Se mantiene la asignación del encriptador de contraseñas
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Hashing seguro estándar de la industria
    }
}