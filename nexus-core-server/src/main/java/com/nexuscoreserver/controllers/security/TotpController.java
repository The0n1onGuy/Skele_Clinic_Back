package com.nexuscoreserver.controllers.security;

import com.nexuscoreserver.models.system.SystemUsersModel;
import com.nexuscoreserver.repositories.system.ISystemUsersRepository;
import com.nexussharedcore.payload.response.ResponseFactory;
import com.nexussharedcore.security.JwtService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión del ciclo de vida del factor de autenticación TOTP (MFA 2FA).
 * Robustecido contra vectores de sobreescritura de secreto y excepciones no controladas de desbordamiento de cadenas.
 */
@RestController
@RequestMapping("/api/core/v1/2fa")
@RequiredArgsConstructor
public class TotpController {

    private final ISystemUsersRepository usersRepository;
    private final JwtService jwtService;
    private final CodeVerifier totpVerifier;

    // 1. Endpoint para generar el Secreto y el código QR (Enrollment)
    @GetMapping("/generate-qr")
    public ResponseEntity<Map<String, Object>> generateQr(HttpServletRequest httpRequest) throws QrGenerationException {
        
        // Validación de Robustez: Evita StringIndexOutOfBoundsException si la cabecera está ausente o mal formateada
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() < 8) {
            return ResponseFactory.unauthorized("Token de autorización ausente o inválido.");
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        SystemUsersModel user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Si ya tiene el candado activo, bloqueamos la generación de nuevos códigos
        if (user.getIs2faEnabled()) {
            return ResponseFactory.badRequest("El usuario ya tiene el 2FA habilitado.");
        }

        // Generamos un nuevo secreto seguro de 32 caracteres en Base32
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        // IMPORTANTE: Guardamos el secreto en estado 'en proceso', pero NO activamos el flag de habilitado aún
        user.setTotpSecret(secret);
        usersRepository.save(user);

        // Generamos la data necesaria para la app autenticadora (Google Auth, Authy, Microsoft Authenticator)
        QrData data = new QrData.Builder()
                .label(user.getUserName())
                .secret(secret)
                .issuer("Nexus Core System")
                .algorithm(dev.samstevens.totp.code.HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        // Convertimos a imagen física en Base64 para visualización directa en el Frontend
        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);
        String mimeType = generator.getImageMimeType();
        String base64Image = dev.samstevens.totp.util.Utils.getDataUriForImage(imageData, mimeType);

        return ResponseFactory.successMessage("Escanee este código en su aplicación de autenticación.", Map.of(
                "qrImage", base64Image,
                "manualSecret", secret
        ));
    }

    // 2. Endpoint para verificar el primer código de enrolamiento y activar el 2FA definitivamente
    @PostMapping("/verify-enrollment")
    public ResponseEntity<Map<String, Object>> verifyEnrollment(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        // Validación de Robustez de Cabecera
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() < 8) {
            return ResponseFactory.unauthorized("Token de autorización ausente o inválido.");
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        String code = request.get("code");

        SystemUsersModel user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // MITIGACIÓN DE SOBREESCRITURA: Evitamos que una cuenta enrolada pueda ser re-configurada
        if (user.getIs2faEnabled()) {
            return ResponseFactory.badRequest("El usuario ya tiene el 2FA habilitado.");
        }

        // Validación criptográfica del código de 6 dígitos ingresado por el usuario
        if (totpVerifier.isValidCode(user.getTotpSecret(), code)) {
            user.setIs2faEnabled(true); // SE ACTIVA EL CANDADO DEFINITIVAMENTE
            user.setLast2faVerifiedAt(LocalDateTime.now()); // Inicializamos la marca de tiempo de validación exitosa de la sesión
            usersRepository.save(user);

            // Generamos y emitimos el Token Maestro final con sus roles y reclamos reales
            String finalToken = jwtService.generateToken(
                    Map.of("roles", List.of(user.getRole().getRoleName()), "tenant", user.getTenantId()),
                    user.getUserName()
            );

            return ResponseFactory.successMessage("2FA activado exitosamente.", Map.of(
                    "authStatus", "SUCCESS",
                    "accessToken", finalToken
            ));
        }

        return ResponseFactory.badRequest("Código de verificación inválido. Intente de nuevo.");
    }

    // 3. Endpoint para validar el código de 6 dígitos en inicios de sesión subsecuentes
    @PostMapping("/verify-login")
    public ResponseEntity<Map<String, Object>> verifyLogin(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        // Validación de Cabecera
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() < 8) {
            return ResponseFactory.unauthorized("Token de autorización ausente o inválido.");
        }
        String token = authHeader.substring(7);

        // Extraemos la identidad del token efímero de pre-autenticación
        String username = jwtService.extractUsername(token);
        String code = request.get("code");

        if (code == null || code.trim().isEmpty()) {
            return ResponseFactory.badRequest("El código 2FA es obligatorio.");
        }

        SystemUsersModel user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validación criptográfica
        if (totpVerifier.isValidCode(user.getTotpSecret(), code)) {
            
            // Éxito: Actualizamos la marca de tiempo de la sesión 2FA para el control de la ventana deslizante
            user.setLast2faVerifiedAt(LocalDateTime.now());
            usersRepository.save(user);

            // Emitimos el Token Maestro final con sus roles y credenciales reales
            String finalToken = jwtService.generateToken(
                    Map.of("roles", List.of(user.getRole().getRoleName()), "tenant", user.getTenantId()),
                    user.getUserName()
            );

            return ResponseFactory.successMessage("Autenticación completada con éxito.", Map.of(
                    "authStatus", "SUCCESS",
                    "accessToken", finalToken,
                    "tenantDestination", user.getTenantId()
            ));
        }

        return ResponseFactory.unauthorized("El código 2FA ingresado es incorrecto o ha expirado.");
    }
}