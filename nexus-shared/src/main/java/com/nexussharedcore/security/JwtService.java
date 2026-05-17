package com.nexussharedcore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${JWT_SECRET_KEY:}")
    private String secretKey;

    @Value("${JWT_EXPIRATION_TIME:900000}")
    private long jwtExpiration;

    @PostConstruct
    public void validateSecretKey() {
        if (secretKey == null || secretKey.trim().isEmpty() || secretKey.length() < 32) {
            log.error("CRÍTICO: La clave secreta JWT no está configurada o es demasiado corta (< 32 caracteres).");
            log.warn("Se generará una clave efímera aleatoria para mantener el sistema operativo en modo DESARROLLO.");

            byte[] randomKeyBytes = new byte[32];
            new java.security.SecureRandom().nextBytes(randomKeyBytes);
            this.secretKey = java.util.Base64.getEncoder().encodeToString(randomKeyBytes);
        }
    }

    /**
     * Genera un token JWT inyectando obligatoriamente los roles del usuario.
     */
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTenant(String token) {
        return extractClaim(token, claims -> claims.get("tenant", String.class));
    }

    /**
     * NUEVO: Extrae los roles directamente del payload del token.
     * Evita ir a la base de datos en cada petición HTTP.
     */
    public List<GrantedAuthority> extractRoles(String token) {
        List<String> roles = extractClaim(token, claims -> claims.get("roles", List.class));
        if (roles == null) {
            return new ArrayList<>();
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                .collect(Collectors.toList());
    }

    /**
     * NUEVO: Validación puramente criptográfica.
     * Si el token fue alterado o expiró, extractAllClaims lanzará una excepción
     * y este método devolverá false, bloqueando la petición HTTP inmediatamente.
     */
    public boolean isTokenSignatureValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.warn("Intento de acceso con token JWT inválido, expirado o manipulado: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }
}