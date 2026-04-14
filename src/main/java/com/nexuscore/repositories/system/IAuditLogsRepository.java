package com.nexuscore.repositories.system;

import com.nexuscore.models.system.AuditLogsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuditLogsRepository extends JpaRepository<AuditLogsModel, Long > {
}
