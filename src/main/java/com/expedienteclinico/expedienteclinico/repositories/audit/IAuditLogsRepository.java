package com.expedienteclinico.expedienteclinico.repositories.audit;

import com.expedienteclinico.expedienteclinico.models.audit.AuditLogsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuditLogsRepository extends JpaRepository<AuditLogsModel, Long > {
}
