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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/core/v1/2fa")
@RequiredArgsConstructor
public class TotpController {

    private final ISystemUsersRepository usersRepository;
    private final JwtService jwtService;
    private final CodeVerifier totpVerifier; // Inyectado por la librería

    // 1. Endpoint para generar el Secreto y el QR (Enrollment)
    @GetMapping("/generate-qr")
    public ResponseEntity<Map<String, Object>> generateQr(HttpServletRequest httpRequest) throws QrGenerationException {
        // Asumimos que validaste el token y extrajiste el UUID o username del usuario temporal
        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        SystemUsersModel user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getIs2faEnabled()) {
            return ResponseFactory.badRequest("El usuario ya tiene el 2FA habilitado.");
        }

        // Generamos un nuevo secreto de 32 caracteres en Base32
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        // IMPORTANTE: Guardamos el secreto pero NO habilitamos la bandera aún
        user.setTotpSecret(secret);
        usersRepository.save(user);

        // Generamos la data para la app (Google Auth, Authy)
        QrData data = new QrData.Builder()
                .label(user.getUserName())
                .secret(secret)
                .issuer("Nexus Core System")
                .algorithm(dev.samstevens.totp.code.HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        // Convertimos a imagen PNG en Base64 para que el FrontEnd la dibuje
        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);
        String mimeType = generator.getImageMimeType();
        String base64Image = dev.samstevens.totp.util.Utils.getDataUriForImage(imageData, mimeType);

        return ResponseFactory.successMessage("Escanee este código en su aplicación de autenticación.", Map.of(
                "qrImage", base64Image,
                "manualSecret", secret // Por si no puede escanear la cámara
        ));
    }

    @PostMapping("/verify-enrollment")
    public ResponseEntity<Map<String, Object>> verifyEnrollment(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        String code = request.get("code");

        SystemUsersModel user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validación criptográfica del primer código
        if (totpVerifier.isValidCode(user.getTotpSecret(), code)) {
            user.setIs2faEnabled(true); // SE ACTIVA EL CANDADO DEFINITIVAMENTE
            usersRepository.save(user);

            // Ahora sí, entregamos el Token Maestro con sus roles reales
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

    @PostMapping("/verify-login")
    public ResponseEntity<Map<String, Object>> verifyLogin(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);

        // Extraemos la identidad del token efímero
        String username = jwtService.extractUsername(token);
        String code = request.get("code");

        if (code == null || code.trim().isEmpty()) {
            return ResponseFactory.badRequest("El código 2FA es obligatorio.");
        }

        SystemUsersModel user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validamos criptográficamente el código de 6 dígitos
        if (totpVerifier.isValidCode(user.getTotpSecret(), code)) {

            // Éxito: Destruimos (lógicamente) el estado efímero y emitimos el Token Maestro
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