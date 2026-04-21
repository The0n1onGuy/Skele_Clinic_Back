package com.nexuscore.services.rrhh;

import com.nexuscore.beans.rrhh.EmployeeOnboardingRequestObject;
import com.nexuscore.models.rrhh.DepartmentsModel;
import com.nexuscore.models.rrhh.EmployeesModel;
import com.nexuscore.models.rrhh.PositionsModel;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.repositories.rrhh.IDepartmentsRepository;
import com.nexuscore.repositories.rrhh.IEmployeesRepository;
import com.nexuscore.repositories.rrhh.IPositionsRepository;
import com.nexuscore.repositories.system.IStatusRepository;
import com.nexuscore.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class EmployeeOnboardingService {

    @Autowired
    private DataSource dataSource; // CORRECCIÓN: camelCase

    @Autowired
    private IStatusRepository statusRepository;

    @Autowired
    private IDepartmentsRepository departmentsRepository;

    @Autowired
    private IPositionsRepository positionsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IEmployeesRepository employeesRepository;

    public void onboardNewEmployee(EmployeeOnboardingRequestObject request, String tenantId) {
        String sharedUuid = UUID.randomUUID().toString();
        JdbcTemplate masterJdbcTemplate = new JdbcTemplate(dataSource);

        // BIFURCACIÓN ESTRATÉGICA
        if (request.getRequiresSystemAccess() != null && request.getRequiresSystemAccess()) {
            // RUTA A: CON CREDENCIALES (Transacción Distribuida)
            insertIntoMaster(request, sharedUuid, tenantId, masterJdbcTemplate);

            try {
                TenantContext.setCurrentTenant(tenantId);
                insertIntoTenantJPA(request, sharedUuid);
            } catch (Exception e) {
                rollbackMasterInsertion(sharedUuid, masterJdbcTemplate);
                e.printStackTrace();
                throw new RuntimeException("Fallo al guardar el perfil clínico. Credenciales revertidas.", e);
            } finally {
                TenantContext.clear();
            }
        } else {
            // RUTA B: SIN CREDENCIALES (Operación Exclusiva de Tenant)
            try {
                TenantContext.setCurrentTenant(tenantId);
                insertIntoTenantJPA(request, sharedUuid);
            } finally {
                TenantContext.clear();
            }
        }
    }

    private void insertIntoMaster(EmployeeOnboardingRequestObject request, String uuid, String tenantId, JdbcTemplate masterJdbc) {
        if (request.getUsername() == null || request.getPassword() == null || request.getRoleName() == null) {
            throw new IllegalArgumentException("Usuario, contraseña y rol son obligatorios si requiresSystemAccess es true");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        String sqlUser = "INSERT INTO system_users (role_id, status_id, uuid, tenant_id, user_name, password) " +
                "VALUES (" +
                "(SELECT id_role FROM system_roles WHERE role_name = ? LIMIT 1), " +
                "(SELECT id_status FROM status WHERE name = 'Active' LIMIT 1), " +
                "?, ?, ?, ?)";

        masterJdbc.update(sqlUser,
                request.getRoleName(),
                uuid,
                tenantId,
                request.getUsername(),
                hashedPassword
        );
    }

    private void rollbackMasterInsertion(String uuid, JdbcTemplate masterJdbc) {
        String sqlDelete = "DELETE FROM system_users WHERE uuid = ?";
        masterJdbc.update(sqlDelete, uuid);
    }

    private void insertIntoTenantJPA(EmployeeOnboardingRequestObject request, String uuidString) {
        EmployeesModel employee = new EmployeesModel();
        employee.setUuid(UUID.fromString(uuidString));
        employee.setName(request.getName());
        employee.setPatname(request.getPatname());
        employee.setMatname(request.getMatname() != null ? request.getMatname() : "");
        employee.setCurp(request.getCurp());
        employee.setRfc(request.getRfc());
        employee.setGender(request.getGender());
        employee.setDatebirth(LocalDate.parse(request.getDatebirth()));
        employee.setDatereg(LocalDate.now());

        PositionsModel position = positionsRepository.findById(request.getIdPosition())
                .orElseThrow(() -> new RuntimeException("Posición no encontrada: " + request.getIdPosition()));
        employee.setId_position(position);

        DepartmentsModel department = departmentsRepository.findById(request.getIdDepartment())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado: " + request.getIdDepartment()));
        employee.setId_department(department);

        // CORRECCIÓN: Búsqueda dinámica robusta en lugar de Hardcoding (1L)
        // Asegúrate de tener un Optional<StatusModel> findByName(String name); en IStatusRepository
        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new RuntimeException("Estado 'Active' no encontrado en el diccionario."));
        employee.setId_status(activeStatus);

        employeesRepository.save(employee);
    }
}