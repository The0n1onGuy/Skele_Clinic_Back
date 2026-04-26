package com.nexuscore.security;

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
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final SecurityExceptionHandler securityExceptionHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // todo: requestmatchers en base  de datos, cliente para swagger

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(securityExceptionHandler) // Captura 401 sin token
                        .accessDeniedHandler(securityExceptionHandler)      // Captura 403 roles inválidos
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/error").permitAll() // Login abierto
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Swagger abierto
                        // RBAC estricto:
                        .requestMatchers("/api/master/tenants/**").hasAnyRole("MASTER")
                        .requestMatchers("/api/his/v1/rpbi/**").hasAnyRole("ADMIN", "RPBI")
                        .requestMatchers("/api/his/v1/rrhh/**").hasAnyRole("ADMIN", "RRHH")
                        .requestMatchers("/api/his/v1/patients/**").hasAnyRole("ADMIN", "PATIENTS")
                        .requestMatchers("/api/his/v1/emergencias/**").hasAnyRole("ADMIN", "EMERGENCIAS")
                        .requestMatchers("/api/his/v1/morgue/**").hasAnyRole("ADMIN", "MORGUE")
                        .requestMatchers("/api/his/v1/lyr/**").hasAnyRole("ADMIN", "LYR")
                        .requestMatchers("/api/his/v1/appointments/**").hasAnyRole("ADMIN", "APPOINTMENTS")
                        .requestMatchers("/api/his/v1/almacen/**").hasAnyRole("ADMIN", "ALMACEN")
                        // ROL DE GESTOR DE LOGS
                        .requestMatchers("/api/system/logs/*").hasAnyRole("MASTER")
                        .anyRequest().denyAll()
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
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // PELIGRO
        // En producción cambiar por: Arrays.asList("https://midominio.com")
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}