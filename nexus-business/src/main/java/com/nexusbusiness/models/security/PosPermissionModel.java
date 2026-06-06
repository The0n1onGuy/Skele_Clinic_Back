package com.nexusbusiness.models.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "pos_permissions")
@Getter
@Setter
public class PosPermissionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permission", nullable = false)
    @Schema(description = "Identificador único incremental del permiso local", example = "1")
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
    @Schema(description = "UUID único global del permiso", example = "P1111111-1111-1111-1111-111111111111")
    private String uuid;

    @Column(name = "permission_name", nullable = false, unique = true, length = 100)
    @Schema(description = "Nombre clave del permiso de negocio", example = "SALES_CREATE")
    private String permissionName;

    @Column(name = "description", nullable = false, length = 255)
    @Schema(description = "Detalle descriptivo de las capacidades que otorga el permiso", example = "Registrar ventas y procesar cobros básicos")
    private String description;

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString().toUpperCase();
        }
    }
}
