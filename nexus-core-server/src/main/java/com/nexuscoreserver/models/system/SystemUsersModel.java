package com.nexuscoreserver.models.system;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "system_users")
@Getter
@Setter
public class SystemUsersModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false)
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
    private String uuid = UUID.randomUUID().toString();

    // Longitud ajustada a 100 para coincidir con system_tenants.tenant_key
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    // Homologado a 'password' para consistencia con el Seeder y PasswordEncoder
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private SystemRolesModel role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusModel status;

    // ==========================================
    // INYECCIÓN ESTRUCTURAL FASE 2: TOTP
    // ==========================================
    @Column(name = "totp_secret", length = 64)
    private String totpSecret;

    @Column(name = "is_2fa_enabled")
    private Boolean is2faEnabled = false;
}