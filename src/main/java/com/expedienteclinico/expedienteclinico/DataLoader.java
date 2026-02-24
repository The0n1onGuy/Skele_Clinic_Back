package com.expedienteclinico.expedienteclinico;

import com.expedienteclinico.expedienteclinico.models.Patients.*;
import com.expedienteclinico.expedienteclinico.repositories.Patients.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Profile("dev") // opcional: solo se ejecuta en perfil dev
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final IPatientsRepository patientsRepository;
    private final IAppointmentsRepository appointmentsRepository;
    private final ITriageRepository triageRepository;
    private final IClinicHistoryRepository clinicHistoryRepository;

    public DataLoader(IPatientsRepository patientsRepository,
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
            log.info("La base de datos ya contiene pacientes. Se omite el populado inicial.");
            return;
        }

        log.info("Iniciando populado del Módulo Clínico...");

        // 1. Paciente
        PatientsModel pacientePrueba = patientsRepository.save(getPatientsModel());

        // Reutilizamos un solo "ahora"
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioCita = ahora.plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finCita = inicioCita.plusHours(1);

        // 2. Cita
        AppointmentsModel cita = buildAppointment(pacientePrueba, inicioCita, finCita);
        appointmentsRepository.save(cita);

        // 3. Triaje
        TriageModel triaje = buildTriage(pacientePrueba, ahora);
        triageRepository.save(triaje);

        // 4. Historial clínico
        ClinicHistoryModel historial = buildClinicHistory(pacientePrueba, ahora);
        clinicHistoryRepository.save(historial);

        log.info("Módulo Clínico completado exitosamente.");
    }

    private static @NotNull PatientsModel getPatientsModel() {
        PatientsModel pacientePrueba = new PatientsModel();
        pacientePrueba.setNombre("Juan");
        pacientePrueba.setApellidos("Pérez Automático");
        pacientePrueba.setCurp("PERJ900101HDFXXX99");
        pacientePrueba.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        pacientePrueba.setGenero("M");
        pacientePrueba.setTelefono("9988776655");
        pacientePrueba.setEmail("juan.seeder@email.com");
        pacientePrueba.setDireccion("Villas Jotoch, Cancún");
        pacientePrueba.setTipoSangre("O+");
        return pacientePrueba;
    }

    private static AppointmentsModel buildAppointment(PatientsModel paciente,
                                                      LocalDateTime inicio,
                                                      LocalDateTime fin) {
        AppointmentsModel cita = new AppointmentsModel();
        cita.setPaciente(paciente);
        cita.setFechaHoraInicio(inicio);
        cita.setFechaHoraFin(fin);
        cita.setEstado(StatusAppointment.PENDIENTE);
        cita.setConsultorio("Consultorio 101");
        return cita;
    }

    private static TriageModel buildTriage(PatientsModel paciente, LocalDateTime fechaHora) {
        TriageModel triaje = new TriageModel();
        triaje.setPaciente(paciente);
        triaje.setFechaHora(fechaHora);
        triaje.setTemperatura(38.5);
        triaje.setPresionArterial("130/90");
        triaje.setFrecuenciaCardiaca(95);
        triaje.setSaturacionOxigeno(96);
        triaje.setNivel(UrgencyLevel.URGENCY);
        return triaje;
    }

    private static ClinicHistoryModel buildClinicHistory(PatientsModel paciente, LocalDateTime fechaHora) {
        ClinicHistoryModel historial = new ClinicHistoryModel();
        historial.setPaciente(paciente);
        historial.setFechaRegistro(fechaHora);
        historial.setMotivoConsulta("Fiebre persistente y dolor de cabeza.");
        historial.setEnfermedadActual("El paciente refiere dolor de cabeza punzante desde hace 48 horas.");
        historial.setDiagnosticoPreliminar("Infección viral a descartar dengue.");
        return historial;
    }
}
