package com.nexusbusiness.repositories.security;

import com.nexusbusiness.models.security.PosPermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPosPermissionRepository extends JpaRepository<PosPermissionModel, Long> {
    Optional<PosPermissionModel> findByPermissionNameIgnoreCase(String permissionName);
    Optional<PosPermissionModel> findByUuid(String uuid);
}
