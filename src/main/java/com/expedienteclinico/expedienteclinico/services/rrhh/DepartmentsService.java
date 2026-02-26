package com.expedienteclinico.expedienteclinico.services.rrhh;
import com.expedienteclinico.expedienteclinico.models.StatusModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.repositories.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
import com.expedienteclinico.expedienteclinico.services.audit.AuditLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class DepartmentsService {
    @Autowired
    IDepartmentsRepository departmentsRepo;

    @Autowired
    IStatusRepository statusRepo;

    @Autowired
    AuditLogsService auditService; // Inyección del auditor

    @Value("${STATUS1:Activo}") private String Active;
    @Value("${STATUS2:Inactivo}") private String Inactive;
    @Value("${STATUS3:Editado}") private String Edited;
    @Value("${STATUS4:Eliminado}") private String Deleted;
    @Value("${STATUS5:Agregado}") private String Added;

    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }

    public List<DepartmentsModel> getAll() {
        return departmentsRepo.findAll();
    }

    public DepartmentsModel saveInfo(DepartmentsModel depto) {
        if (depto.getId_status() == null) {
            StatusModel statusActive = getStatusByName(Active);
            depto.setId_status(statusActive);
        }
        DepartmentsModel saved =departmentsRepo.save(depto);
        auditService.registrarAccion("CREACION: " + saved.getName(), Added);
        return saved;
    }

    public DepartmentsModel updateInfo(Long id, DepartmentsModel deptoUpdate) {
        return departmentsRepo.findById(id).map(depto -> {
            depto.setName(deptoUpdate.getName());
            depto.setId_status(deptoUpdate.getId_status());
            DepartmentsModel updated = departmentsRepo.save(depto);
            auditService.registrarAccion("CAMBIOS: " + id, Edited);
            return updated;
        }).orElse(null);
    }

    public boolean deleteInfo(Long id) {
        return departmentsRepo.findById(id).map(depto -> {
            StatusModel statusInactive = getStatusByName(Inactive);
            depto.setId_status(statusInactive);
            departmentsRepo.save(depto);
            auditService.registrarAccion("ELIMINACION: " + id, Deleted);
            return true;
        }).orElse(false);
    }
}
