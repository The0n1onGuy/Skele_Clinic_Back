package com.expedienteclinico.expedienteclinico;

import com.expedienteclinico.expedienteclinico.models.Patients.*;
import com.expedienteclinico.expedienteclinico.repositories.Patients.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Profile("dev")
public class PatientsDataLoader  implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PatientsDataLoader.class);

    private final IPatientsRepository patientsRepository;
    private final IAppointmentsRepository appointmentsRepository;
    private final ITriageRepository triageRepository;
    private final IClinicHistoryRepository clinicHistoryRepository;

    public PatientsDataLoader (IPatientsRepository patientsRepository,
                              IAppointmentsRepository appointmentsRepository,
                              ITriageRepository triageRepository,
                              IClinicHistoryRepository clinicHistoryRepository) {
        this.patientsRepository = patientsRepository;
        this.appointmentsRepository = appointmentsRepository;
        this.triageRepository = triageRepository;
        this.clinicHistoryRepository = clinicHistoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (patientsRepository.count() > 0) {
            log.info("Módulo clínico ya poblado. Se omite el seeding.");
            return;
        }

        log.info("🎬 Iniciando populado del Módulo Clínico...");

        // 1. PACIENTE JUAN
        PatientsModel juan = new PatientsModel();
        juan.setNombre("Juan");
        juan.setApellidos("Pérez Automático");
        juan.setCurp("PERJ900101HDFXXX99");
        juan.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        juan.setGenero("M");
        juan.setTelefono("9988776655");
        juan.setEmail("juan.seeder@email.com");
        juan.setDireccion("Villas Jotoch, Cancún");
        juan.setTipoSangre("O+");
        juan = patientsRepository.save(juan);
        log.info("✅ Paciente Juan creado (ID: {})", juan.getId());

        // Timestamp fijo para consistencia
        LocalDateTime ahora = LocalDateTime.now();

        // 2. CITA DE JUAN
        AppointmentsModel cita = new AppointmentsModel();
        cita.setPaciente(juan);
        cita.setFechaHoraInicio(ahora.plusDays(1).withHour(10).withMinute(0));
        cita.setFechaHoraFin(ahora.plusDays(1).withHour(11).withMinute(0));
        cita.setEstado(StatusAppointment.PENDIENTE);
        cita.setConsultorio("Consultorio 101");
        appointmentsRepository.save(cita);
        log.info("✅ Cita de Juan creada");

        // 3. TRIAJE DE JUAN
        TriageModel triaje = new TriageModel();
        triaje.setPaciente(juan);
        triaje.setFechaHora(ahora);
        triaje.setTemperatura(38.5);
        triaje.setPresionArterial("130/90");
        triaje.setFrecuenciaCardiaca(95);
        triaje.setSaturacionOxigeno(96);
        triaje.setNivel(UrgencyLevel.URGENCY);
        triageRepository.save(triaje);
        log.info("✅ Triaje de Juan creado");

        // 4. HISTORIAL DE JUAN
        ClinicHistoryModel historial = new ClinicHistoryModel();
        historial.setPaciente(juan);
        historial.setFechaRegistro(ahora);
        historial.setMotivoConsulta("Fiebre persistente y dolor de cabeza");
        historial.setEnfermedadActual("Dolor de cabeza punzante desde hace 48 horas");
        historial.setDiagnosticoPreliminar("Infección viral a descartar dengue");
        clinicHistoryRepository.save(historial);
        log.info("✅ Historial clínico de Juan creado");

        log.info("🎉 Módulo Clínico completado exitosamente!");
    }
}
