package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.patients.*;
import com.expedienteclinico.expedienteclinico.models.rrhh.*;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.Patients.*;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.*;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import com.expedienteclinico.expedienteclinico.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@Order(5) // Prioridad menor para ejecutar al final
public class PatientsDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PatientsDataLoader.class);

    private final DataSource dataSource;
    private final IPatientsRepository patientsRepository;
    private final IPatientsAppointmentsRepository appointmentsRepository;
    private final ITriageRepository triageRepository;
    private final IClinicHistoryRepository clinicHistoryRepository;
    private final IEmployeesRepository employeesRepository;
    private final IStatusRepository statusRepository;
    private final IDepartmentsRepository departmentsRepository;
    private final IPositionsRepository positionsRepository;

    public PatientsDataLoader(DataSource dataSource, IPatientsRepository patientsRepository, IPatientsAppointmentsRepository appointmentsRepository, ITriageRepository triageRepository, IClinicHistoryRepository clinicHistoryRepository, IEmployeesRepository employeesRepository, IStatusRepository statusRepository, IDepartmentsRepository departmentsRepository, IPositionsRepository positionsRepository) {
        this.dataSource = dataSource;
        this.patientsRepository = patientsRepository;
        this.appointmentsRepository = appointmentsRepository;
        this.triageRepository = triageRepository;
        this.clinicHistoryRepository = clinicHistoryRepository;
        this.employeesRepository = employeesRepository;
        this.statusRepository = statusRepository;
        this.departmentsRepository = departmentsRepository;
        this.positionsRepository = positionsRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.warn("ATENCIÓN: Seeder de Pruebas (DEV) inicializado.");
        List<String> tenants = getTenants();

        if (tenants.isEmpty()) {
            log.info("No hay esquemas clínicos registrados. Omitiendo inyección de datos de prueba.");
            return;
        }

        for (String tenant : tenants) {
            TenantContext.setCurrentTenant(tenant);
            try {
                log.info("Inyectando datos ficticios en el esquema: {}", tenant);
                injectDummyData();
            } catch (Exception e) {
                log.error("Fallo al inyectar datos de prueba en {}", tenant, e);
            } finally {
                TenantContext.clear();
            }
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void injectDummyData() {
        StatusModel activeStatus = statusRepository.findById(1L).orElseThrow(() -> new RuntimeException("Estatus no encontrado"));
        DepartmentsModel dept = departmentsRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
        PositionsModel pos = positionsRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Posición no encontrada"));

        // 1. EMPLEADO DE PRUEBA (Alineado al modelo)
        if (employeesRepository.count() == 0) {
            EmployeesModel emp = new EmployeesModel();
            emp.setName("Manuel");
            emp.setPatname("Pérez");
            emp.setMatname("López");
            emp.setCurp("PELJ900101HDFRRN01");
            emp.setRfc("PELJ900101123");
            emp.setDatebirth(LocalDate.parse("1990-01-01")); // Ahora es LocalDate
            emp.setDatereg(LocalDate.parse("2026-03-11"));   // Ahora es LocalDate
            emp.setGender("M");
            emp.setId_position(pos);
            emp.setId_department(dept);
            emp.setId_status(activeStatus);
            employeesRepository.save(emp);
        }

        // 2. PACIENTE DE PRUEBA (Alineado al modelo)
        if (patientsRepository.count() == 0) {
            PatientsModel juan = new PatientsModel();
            juan.setNombre("Juan");
            juan.setApellidos("Pérez Gómez"); // El modelo no divide los apellidos
            juan.setFechaNacimiento(LocalDate.of(1985, 5, 20));
            juan.setGenero("Masculino");
            juan.setTelefono("555-123-4567");
            juan.setEmail("juan.perez@example.com");
            juan.setDireccion("Av. Siempre Viva 742, Springfield");
            juan.setTipoSangre("O+");
            juan.setContactoEmergencia("Maria Pérez (555-987-6543)");
            juan.setAlergias("Penicilina");
            juan.setEnfermedadesCronicas("Hipertensión");
            juan = patientsRepository.save(juan);

            LocalDateTime ahora = LocalDateTime.now();

            PatientsAppointmentsModel cita = new PatientsAppointmentsModel();
            cita.setPaciente(juan);
            cita.setFechaHoraInicio(ahora.plusDays(1).withHour(10).withMinute(0));
            cita.setFechaHoraFin(ahora.plusDays(1).withHour(11).withMinute(0));
            cita.setEstado(StatusAppointment.PENDIENTE);
            cita.setConsultorio("Consultorio 101");
            appointmentsRepository.save(cita);

            TriageModel triaje = new TriageModel();
            triaje.setPaciente(juan);
            triaje.setFechaHora(ahora);
            triaje.setTemperatura(38.5);
            triaje.setPresionArterial("130/90");
            triaje.setFrecuenciaCardiaca(95);
            triaje.setSaturacionOxigeno(96);
            triaje.setNivel(UrgencyLevel.URGENCY);
            triageRepository.save(triaje);

            ClinicHistoryModel historial = new ClinicHistoryModel();
            historial.setPaciente(juan);
            historial.setFechaRegistro(ahora);
            historial.setMotivoConsulta("Fiebre persistente y dolor de cabeza");
            historial.setPadecimientoActual("Dolor de cabeza punzante desde hace 48 horas");
            clinicHistoryRepository.save(historial);
        }
    }

    private List<String> getTenants() {
        List<String> tenants = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT tenant_id FROM dbo.system_users WHERE tenant_id != 'dbo' AND tenant_id IS NOT NULL")) {
            while (rs.next()) {
                tenants.add(rs.getString(1));
            }
        } catch (Exception e) {
            log.error("Fallo al obtener directorio de inquilinos. ¿Base de datos limpia?", e);
        }
        return tenants;
    }
}