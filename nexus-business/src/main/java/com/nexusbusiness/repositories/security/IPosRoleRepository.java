package com.nexusbusiness.repositories.security;

import com.nexusbusiness.models.security.PosRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPosRoleRepository extends JpaRepository<PosRoleModel, Long> {
    Optional<PosRoleModel> findByRoleNameIgnoreCase(String roleName);
    Optional<PosRoleModel> findByUuid(String uuid);
}
