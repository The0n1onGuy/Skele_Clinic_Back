/*
package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.PositionsModel;
import com.expedienteclinico.expedienteclinico.repositories.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IPositionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.UUID;



@Component
public class RRHHDataLoader implements CommandLineRunner {

    StatusModel Active;
    StatusModel Inactive;
    @Value("${STATUS1:Activo}")
//    private String Active;
//    @Value("${STATUS2:Inactivo}")
//    private String Inactive;
//    @Value("${STATUS3:Editado}")
//    private String Edited;
//    @Value("${STATUS4:Eliminado}")
//    private String Deleted;
//    @Value("${STATUS5:Agregado}")
//    private String Added;
    @Autowired
    private IStatusRepository Srepository;

    @Autowired
    private IDepartmentsRepository Drepository;

    @Autowired
    private IPositionsRepository Prepository;


    private void StaData() {
        // 1. Instanciamos vacío (Hibernate se encargará del ID y la clase generará el UUID)
        StatusModel statusActivo = new StatusModel();
        // 2. Seteamos únicamente el nombre
        statusActivo.setStatusName("Activo");
        // 3. Guardamos
        Srepository.save(statusActivo);

        StatusModel statusInactivo = new StatusModel();
        statusInactivo.setStatusName("Inactivo");
        Srepository.save(statusInactivo);

        System.out.println("Estados cargados");
    }

    private void DepData() {
        DepartmentsModel deptoRH = new DepartmentsModel();

        deptoRH.setName("Recursos Humanos");
        deptoRH.setStatus(Active);
        deptoRH.setName("Sistemas");
        deptoRH.setStatus(Active);
        deptoRH.setName("Urgencias");
        deptoRH.setStatus(Active);
        deptoRH.setName("Quirófanos");
        deptoRH.setStatus(Active);
        deptoRH.setName("Morgue");
        deptoRH.setStatus(Active);

        Drepository.save(deptoRH);
        System.out.println("Departamentos cargados exitosamente.");
    }

    private void PosData() {

        PositionsModel Posis = new PositionsModel();
        Posis.setName("Gerente");
        Posis.setDescription("Encargado de funciones internas");
        Posis.setStatus(Active);
        Posis.setName("Secretaria");
        Posis.setDescription("Gestor de juntas");
        Posis.setStatus(Active);
        Prepository.save(Posis);
        System.out.println("Posiciones cargados exitosamente.");
    }

    @Override
    public void run(String... args) throws Exception {
        if (Srepository.count() == 0) {
            StaData();
        }
        Active = Srepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Error: Estado 'Activo' no encontrado"));

        if (Drepository.count() == 0) {
            DepData();
        }
        if (Prepository.count() == 0) {
            PosData();
        }
    }
}

*/


//package com.expedienteclinico.expedienteclinico;
//
//import com.expedienteclinico.expedienteclinico.models.StatusModel;
//import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
//import com.expedienteclinico.expedienteclinico.models.rrhh.PositionsModel;
//import com.expedienteclinico.expedienteclinico.repositories.IStatusRepository;
//import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
//import com.expedienteclinico.expedienteclinico.repositories.rrhh.IPositionsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import java.util.UUID;
//
//
//
//@Component
//public class DataLoader implements CommandLineRunner {
//
//    @Value("${STATUS1:Activo}")
//    private String Active;
//    @Value("${STATUS2:Inactivo}")
//    private String Inactive;
//    @Value("${STATUS3:Editado}")
//    private String Edited;
//    @Value("${STATUS4:Eliminado}")
//    private String Deleted;
//    @Value("${STATUS5:Agregado}")
//    private String Added;
//
//    @Autowired
//    private IStatusRepository Srepository;
//
//    @Autowired
//    private IDepartmentsRepository Drepository;
//
//    @Autowired
//    private IPositionsRepository Prepository;
//
//    //private final IStatusRepository Srepository;
//
//    private StatusModel resolveStatus(String name) {
//        return Srepository.findAll().stream()
//                .filter(s -> s.getStatusName().equalsIgnoreCase(name))
//                .findFirst()
//                .orElseGet(() -> {
//                    StatusModel nuevo = new StatusModel();
//                    nuevo.setStatusName(name);
//                    return Srepository.save(nuevo);
//                });
//    }
//
//    private void DepData(StatusModel status) {
//        Drepository.save(new DepartmentsModel( UUID.randomUUID(), null,"Recursos Humanos", status));
//        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Sistemas", status));
//        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Urgencias", status));
//        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Quirófanos ", status));
//        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Morgue ", status));
//        System.out.println("Departamentos cargados exitosamente.");
//    }
//
//    private void PosData(StatusModel status) {
//        Prepository.save(new PositionsModel(UUID.randomUUID(), null,"Gerente", "Encargado de funciones internas", status));
//        Prepository.save(new PositionsModel(UUID.randomUUID(),null, "Secretaria", "Gestor de juntas", status));
//        System.out.println("Posiciones cargados exitosamente.");
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        StatusModel activeStatus = resolveStatus(Active);
//        StatusModel inactiveStatus = resolveStatus(Inactive);
//        StatusModel editedStatus = resolveStatus(Edited);
//        StatusModel deletedStatus = resolveStatus(Deleted);
//        StatusModel addedStatus = resolveStatus(Added);
//        if (Drepository.count() == 0) {
//            DepData(activeStatus);
//        }
//        if (Prepository.count() == 0) {
//            PosData(activeStatus);
//        }
//    }
//}