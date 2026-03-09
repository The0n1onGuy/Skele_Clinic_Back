package com.expedienteclinico.expedienteclinico.services.rrhh;
import com.expedienteclinico.expedienteclinico.beans.rrhh.DepartmentObject;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
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

    //@Autowired
//    AuditLogsService auditService; // Inyección del auditor

    @Value("${STATUS1:Activo}") private String Active;
    @Value("${STATUS2:Inactivo}") private String Inactive;
    @Value("${STATUS3:Editado}") private String Edited;
    @Value("${STATUS4:Eliminado}") private String Deleted;
    @Value("${STATUS5:Agregado}") private String Added;

    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }

    public DepartmentObject convertToDTO(DepartmentsModel model) {
        DepartmentObject dto = new DepartmentObject();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setUuid(model.getUuid());
        dto.setStatusName(model.getId_status().getStatusName());
        return dto;
    }

    public List<DepartmentObject> getAll() {
        return departmentsRepo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

//    public DepartmentsModel saveInfo(DepartmentsModel depto) {
//        if (depto.getId_status() == null) {
//            StatusModel statusActive = getStatusByName(Active);
//            depto.setId_status(statusActive);
//        }
    ////        DepartmentsModel saved =departmentsRepo.save(depto);
    ////        auditService.registrarAccion("CREACION: " + saved.getName(), Added);
    ////        return saved;
//
//
//        return departmentsRepo.save(depto);
//    }
    public DepartmentObject saveInfo(DepartmentObject dto) {
        DepartmentsModel model = new DepartmentsModel();
        model.setName(dto.getName());

        // Buscamos el estado por defecto (Activo)
        StatusModel status = statusRepo.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        model.setId_status(status);

        // Hibernate genera el UUID automáticamente gracias a @UuidGenerator
        DepartmentsModel saved = departmentsRepo.save(model);
        return convertToDTO(saved);
    }

    public DepartmentObject updateInfo(Long id, DepartmentObject dto) {
        return departmentsRepo.findById(id).map(model -> {
            model.setName(dto.getName());
            // El estatus usualmente no cambia en un update de nombre,
            // pero si fuera necesario, se buscaría aquí.
            DepartmentsModel updated = departmentsRepo.save(model);
            return convertToDTO(updated);
        }).orElse(null);
    }

    public boolean deleteInfo(Long id) {
        return departmentsRepo.findById(id).map(depto -> {
            StatusModel statusInactive = getStatusByName(Inactive);
            depto.setId_status(statusInactive);
//            departmentsRepo.save(depto);
//            auditService.registrarAccion("ELIMINACION: " + id, Deleted);
//            return true;

            departmentsRepo.save(depto);
            return true;
        }).orElse(false);
    }
}
