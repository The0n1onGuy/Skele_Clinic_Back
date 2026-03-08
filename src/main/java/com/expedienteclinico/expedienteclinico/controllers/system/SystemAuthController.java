package com.expedienteclinico.expedienteclinico.controllers.system;

import com.expedienteclinico.expedienteclinico.beans.system.SystemUsersRequestObject;
import com.expedienteclinico.expedienteclinico.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SystemAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody SystemUsersRequestObject request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Autenticar credenciales contra la base de datos
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getUserPassword())
            );

            // 2. Extraer roles para inyectarlos en el token
            String role = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", role);

            // 3. Generar JWT
            String jwtToken = jwtService.generateToken(extraClaims, request.getUserName());

            response.put("message", "Autenticación exitosa");
            response.put("token", jwtToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Seguridad: No exponer detalles del por qué falló (si fue usuario o contraseña)
            response.put("message", "Credenciales inválidas");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}