package com.nexussharedcore.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/core/v1/auth/**", "/error").permitAll()

                        // Solo quien tenga un JWT válido con el rol de SETUP puede pedir un QR o enrolarse
                        .requestMatchers("/api/core/v1/2fa/generate-qr").hasRole("PRE_AUTH_SETUP")
                        .requestMatchers("/api/core/v1/2fa/verify-enrollment").hasRole("PRE_AUTH_SETUP")

                        // Solo quien ya esté enrolado y necesite verificar su código para entrar
                        .requestMatchers("/api/core/v1/2fa/verify-login").hasRole("PRE_AUTH_VERIFY")

                        .requestMatchers("/api/core/v1/system-roles/**").hasAnyRole("MASTER")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").hasRole("DEVELOPMENT")
                        .requestMatchers("/api/master/tenants/**").hasAnyRole("MASTER")
                        .requestMatchers("/api/core/v1/system-users/**").permitAll()
                        .requestMatchers("/api/his/v1/rpbi/**").hasAnyRole("RPBI", "ADMIN")
                        .requestMatchers("/api/his/v1/rrhh/**").hasAnyRole("RRHH", "ADMIN")
                        .requestMatchers("/api/his/v1/patients/**").hasAnyRole("PATIENTS", "ADMIN")
                        .requestMatchers("/api/his/v1/emergencias/**").hasAnyRole("EMERGENCIAS", "ADMIN")
                        .requestMatchers("/api/his/v1/morgue/**").hasAnyRole("MORGUE", "ADMIN")
                        .requestMatchers("/api/his/v1/lyr/**").hasAnyRole("LYR", "ADMIN")
                        .requestMatchers("/api/his/v1/appointments/**").hasAnyRole("APPOINTMENTS", "ADMIN")
                        .requestMatchers("/api/his/v1/almacen/**").hasAnyRole("ALMACEN", "ADMIN")
                        .requestMatchers("/api/system/logs/*").hasAnyRole("MASTER")
                        .anyRequest().denyAll()
                )
                // Se eliminó .authenticationProvider(). La librería no hace Logins.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}