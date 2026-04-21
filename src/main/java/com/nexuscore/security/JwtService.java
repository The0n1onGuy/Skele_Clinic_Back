package com.nexuscore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
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
            log.warn("ADVERTENCIA: Todos los tokens se invalidarán si el servidor se reinicia.");

            // 2. SOLUCIÓN A LA VERSIÓN 0.9.1: Generación de 256 bits con Java Nativo
            byte[] randomKeyBytes = new byte[32];
            new java.security.SecureRandom().nextBytes(randomKeyBytes);
            this.secretKey = java.util.Base64.getEncoder().encodeToString(randomKeyBytes);
        }
    }

    /**
     * Genera un token JWT para un usuario específico.
     */
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    /**
     * Sobrecarga para inyectar Claims (cargas útiles) adicionales, como roles o permisos.
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

    /**
     * Extrae el nombre de usuario (Subject) del payload del token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Obtener el "identificador" de la persona que inició sesión
    public String extractTenant(String token) {
        return extractClaim(token, claims -> claims.get("tenant", String.class));
    }
    /**
     * Valida la integridad de la firma y que el token pertenezca al usuario esperado.
     */
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un Claim específico utilizando una función de mapeo.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parsea el token. Si la firma es inválida, está expirado o fue alterado,
     * lanzará excepciones criptográficas (SignatureException, ExpiredJwtException).
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Convierte la cadena secreta HEX/Base64 en un objeto SecretKey criptográficamente válido.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }
}