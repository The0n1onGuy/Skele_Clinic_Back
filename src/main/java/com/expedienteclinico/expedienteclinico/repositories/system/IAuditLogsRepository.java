package com.expedienteclinico.expedienteclinico.repositories.system;

import com.expedienteclinico.expedienteclinico.models.system.AuditLogsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuditLogsRepository extends JpaRepository<AuditLogsModel, Long > {
}
