package com.expedienteclinico.expedienteclinico.models.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "system_roles")
@Getter
@Setter
public class SystemRolesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_role", nullable = false)
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
    private String uuid = java.util.UUID.randomUUID().toString();

    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    @Schema(description = "Nombre del rol (Prefijo ROLE_ es estándar en Spring Security)", example = "ROLE_MEDICO")
    private String roleName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusModel status;
}