package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import com.expedienteclinico.expedienteclinico.models.rpbi.*;
import com.expedienteclinico.expedienteclinico.repositories.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2) // DE MANERA ESTRICTA: Se ejecuta DESPUÉS de SystemDataLoader
public class RpbiDataLoader implements CommandLineRunner {

    private final IStatusRepository statusRepository;
    private final IRpbiClasificationRepository classificationRepository;
    private final IRpbiPhysicalStateRepository physicalStateRepository;
    private final IRpbiContainerRepository containerRepository;
    private final IRpbiComplianceMatrixRepository matrixRepository;

    @Value("${STATUS1:Active}")
    private String statusActiveName;

    @Value("${STATUS2:Inactive}")
    private String statusInactiveName;

    @Value("${RPBI_CLASIF_NAME:Punzocortantes}")
    private String clasifName;

    @Value("${RPBI_CLASIF_DESC:Agujas de jeringas, bisturíes, estiletes}")
    private String clasifDesc;

    @Value("${RPBI_CLASIF_COLOR:Rojo}")
    private String clasifColor;

    @Value("${RPBI_PHYS_STATE_NAME:Solido}")
    private String physStateName;

    @Value("${RPBI_PHYS_STATE_UNIT:kg}")
    private String physStateUnit;

    @Value("${RPBI_CONT_NAME:Recipiente Rígido}")
    private String contName;

    @Value("${RPBI_CONT_DESC:Polipropileno resistente a fracturas}")
    private String contDesc;

    public RpbiDataLoader(IStatusRepository statusRepository,
                          IRpbiClasificationRepository classificationRepository,
                          IRpbiPhysicalStateRepository physicalStateRepository,
                          IRpbiContainerRepository containerRepository,
                          IRpbiComplianceMatrixRepository matrixRepository) {
        this.statusRepository = statusRepository;
        this.classificationRepository = classificationRepository;
        this.physicalStateRepository = physicalStateRepository;
        this.containerRepository = containerRepository;
        this.matrixRepository = matrixRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Fallará automáticamente si SystemDataLoader no hizo su trabajo previo
        StatusModel activeStatus = resolveStatus(statusActiveName);
        StatusModel inactiveStatus = resolveStatus(statusInactiveName);

        if (classificationRepository.count() == 0) {
            RpbiClasificationModel cVar = saveClasif(clasifName, clasifDesc, clasifColor, activeStatus);
            RpbiPhysicalStateModel sVar = saveState(physStateName, physStateUnit, activeStatus);
            RpbiContainerModel nVar = saveCont(contName, contDesc, activeStatus);
            saveMatrix(cVar, sVar, nVar, activeStatus);

            seedNom087Normative(activeStatus);

            System.out.println(">>> Módulo RPBI: Sembrado defensivo y normativo completado con éxito.");
        }
    }

    /**
     * Búsqueda estricta. Si no existe, detiene el despliegue para evitar inconsistencias de BD.
     */
    private StatusModel resolveStatus(String name) {
        return statusRepository.findAll().stream()
                .filter(s -> s.getStatusName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "ERROR CRÍTICO: El estado de sistema '" + name + "' no ha sido precargado. " +
                                "Asegúrese de que SystemDataLoader se ejecute primero."
                ));
    }

    private void seedNom087Normative(StatusModel status) {
        RpbiClasificationModel sangre = saveClasif("Sangre", "Sangre líquida y sus componentes", "Rojo", status);
        RpbiPhysicalStateModel liquido = saveState("Líquido", "L", status);
        RpbiContainerModel hermeticoRojo = saveCont("Recipiente Hermético", "Polipropileno tapa hermética", status);
        saveMatrix(sangre, liquido, hermeticoRojo, status);

        RpbiClasificationModel pato = saveClasif("Patológicos", "Tejidos y órganos no en formol", "Amarillo", status);
        RpbiPhysicalStateModel solido = saveState("Sólido", "kg", status);
        RpbiContainerModel bolsaAma = saveCont("Bolsa Polietileno Amarilla", "Calibre mínimo 300", status);
        saveMatrix(pato, solido, bolsaAma, status);
    }

    private RpbiClasificationModel saveClasif(String n, String d, String c, StatusModel s) {
        return classificationRepository.findAll().stream()
                .filter(clasif -> clasif.getName().equalsIgnoreCase(n))
                .findFirst()
                .orElseGet(() -> {
                    RpbiClasificationModel m = new RpbiClasificationModel();
                    m.setName(n);
                    m.setDescription(d);
                    m.setColorCode(c);
                    m.setStatus(s);
                    return classificationRepository.save(m);
                });
    }

    private RpbiPhysicalStateModel saveState(String n, String u, StatusModel s) {
        return physicalStateRepository.findAll().stream()
                .filter(state -> state.getName().equalsIgnoreCase(n))
                .findFirst()
                .orElseGet(() -> {
                    RpbiPhysicalStateModel m = new RpbiPhysicalStateModel();
                    m.setName(n);
                    m.setMeasureUnit(u);
                    m.setStatus(s);
                    return physicalStateRepository.save(m);
                });
    }

    private RpbiContainerModel saveCont(String n, String d, StatusModel s) {
        return containerRepository.findAll().stream()
                .filter(cont -> cont.getName().equalsIgnoreCase(n))
                .findFirst()
                .orElseGet(() -> {
                    RpbiContainerModel m = new RpbiContainerModel();
                    m.setName(n);
                    m.setDescription(d);
                    m.setStatus(s);
                    return containerRepository.save(m);
                });
    }

    private void saveMatrix(RpbiClasificationModel c, RpbiPhysicalStateModel p, RpbiContainerModel e, StatusModel s) {
        boolean exists = matrixRepository.findAll().stream()
                .anyMatch(matrix -> matrix.getClassification().getId().equals(c.getId()) &&
                        matrix.getPhysicalState().getId().equals(p.getId()) &&
                        matrix.getContainer().getId().equals(e.getId()));

        if (!exists) {
            RpbiComplianceMatrixModel m = new RpbiComplianceMatrixModel();
            m.setClassification(c);
            m.setPhysicalState(p);
            m.setContainer(e);
            m.setStatus(s);
            matrixRepository.save(m);
        }
    }
}