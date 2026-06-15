package com.nexusbusiness.models.security;

import com.nexusbusiness.models.system.StatusModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "pos_roles")
@Getter
@Setter
public class PosRoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role", nullable = false)
    @Schema(description = "Identificador único incremental del rol", example = "1")
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
    @Schema(description = "UUID único global del rol", example = "R1111111-1111-1111-1111-111111111111")
    private String uuid;

    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    @Schema(description = "Nombre único de Spring Security para el rol local del POS", example = "ROLE_POS_CAJERO")
    private String roleName;

    @Column(name = "description", nullable = false, length = 255)
    @Schema(description = "Explicación de las responsabilidades asociadas al rol", example = "Cajero de Punto de Venta (Registro y cobro)")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    @Schema(description = "Estado de activación del rol")
    private StatusModel status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "pos_role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Schema(description = "Conjunto de permisos asociados al rol local")
    private Set<PosPermissionModel> permissions;

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString().toUpperCase();
        }
    }
}
