package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.patients.*;
import com.expedienteclinico.expedienteclinico.models.rrhh.*;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.Patients.*;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.*;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TenantSeederService {

    private final IPatientsRepository patientsRepository;
    private final IPatientsAppointmentsRepository appointmentsRepository;
    private final ITriageRepository triageRepository;
    private final IClinicHistoryRepository clinicHistoryRepository;
    private final IEmployeesRepository employeesRepository;
    private final IStatusRepository statusRepository;
    private final IDepartmentsRepository departmentsRepository;
    private final IPositionsRepository positionsRepository;
    private final EntityManager entityManager; // 2. DECLARACIÓN DE LA VARIABLE

    // 3. INYECCIÓN EN EL CONSTRUCTOR
    public TenantSeederService(IPatientsRepository patientsRepository,
                               IPatientsAppointmentsRepository appointmentsRepository,
                               ITriageRepository triageRepository,
                               IClinicHistoryRepository clinicHistoryRepository,
                               IEmployeesRepository employeesRepository,
                               IStatusRepository statusRepository,
                               IDepartmentsRepository departmentsRepository,
                               IPositionsRepository positionsRepository,
                               EntityManager entityManager) { // Añadido aquí
        this.patientsRepository = patientsRepository;
        this.appointmentsRepository = appointmentsRepository;
        this.triageRepository = triageRepository;
        this.clinicHistoryRepository = clinicHistoryRepository;
        this.employeesRepository = employeesRepository;
        this.statusRepository = statusRepository;
        this.departmentsRepository = departmentsRepository;
        this.positionsRepository = positionsRepository;
        this.entityManager = entityManager; // Asignación final
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeSeed(String tenant) {
        // ACCIÓN DE GRADO SENIOR: Forzar físicamente el esquema en la sesión de MySQL
        // Esto garantiza que cualquier consulta siguiente (repositorio) use este esquema.
        entityManager.createNativeQuery("USE " + tenant).executeUpdate();
        entityManager.flush(); // Asegura que el comando se ejecute de inmediato

        // Ahora Hibernate ya está "parado" sobre el hospital correcto.
        StatusModel activeStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Estatus no encontrado"));

        // Al usar findAll(), Hibernate ahora buscará en hospital_aurora.rrhh_departments
        DepartmentsModel dept = departmentsRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        PositionsModel pos = positionsRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Posición no encontrada"));
        // 1. EMPLEADO DE PRUEBA
        if (employeesRepository.count() == 0) {
            EmployeesModel emp = new EmployeesModel();
            emp.setName("Manuel");
            emp.setPatname("Pérez");
            emp.setMatname("López");
            emp.setCurp("PELJ900101HDFRRN01");
            emp.setRfc("PELJ900101123");
            emp.setDatebirth(LocalDate.parse("1990-01-01"));
            emp.setDatereg(LocalDate.parse("2026-03-11"));
            emp.setGender("M");
            emp.setId_position(pos);
            emp.setId_department(dept);
            emp.setId_status(activeStatus);
            employeesRepository.save(emp);
        }

        // 2. PACIENTE DE PRUEBA
        if (patientsRepository.count() == 0) {
            PatientsModel juan = new PatientsModel();
            juan.setNombre("Juan");
            juan.setApellidos("Pérez Gómez");
            juan.setCurp("PELJ900101HDFRRN01");
            juan.setFechaNacimiento(LocalDate.of(1985, 5, 20));
            juan.setGenero("Masculino");
            juan.setTelefono("555-123-4567");
            juan.setEmail("juan.perez@example.com");
            juan.setDireccion("Av. Siempre Viva 742");
            juan.setTipoSangre("O+");
            juan.setContactoEmergencia("Maria Pérez");
            juan = patientsRepository.save(juan);

            LocalDateTime ahora = LocalDateTime.now();

            // Cita, Triaje e Historial (se mantienen igual que en tu código original)
            PatientsAppointmentsModel cita = new PatientsAppointmentsModel();
            cita.setPaciente(juan);
            cita.setFechaHoraInicio(ahora.plusDays(1).withHour(10));
            cita.setFechaHoraFin(ahora.plusDays(1).withHour(11));
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
}