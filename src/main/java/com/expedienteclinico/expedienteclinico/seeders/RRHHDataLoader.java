package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.PositionsModel;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IPositionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.UUID;



@Component
@Order(2)
public class RRHHDataLoader implements CommandLineRunner {

    @Value("${STATUS1:Active}")
    private String Active;
    @Value("${STATUS2:Inactive}")
    private String Inactive;
    @Value("${STATUS3:Edited}")
    private String Edited;
    @Value("${STATUS4:Deleted}")
    private String Deleted;
    @Value("${STATUS5:Added}")
    private String Added;

    @Autowired
    private IStatusRepository Srepository;

    @Autowired
    private IDepartmentsRepository Drepository;

    @Autowired
    private IPositionsRepository Prepository;

    //EL CREADOR DE LOS STATUS
//    private StatusModel resolveStatus(String name) {
//        return Srepository.findAll().stream()
//                .filter(s -> s.getStatusName().equalsIgnoreCase(name))
//                .findFirst()
//                .orElseGet(() -> {
//                    StatusModel nuevo = new StatusModel();
//                    nuevo.setStatusName(name);po
//                    return Srepository.save(nuevo);
//                });
//    }


    private StatusModel resolveStatus(String name) {
        return Srepository.findByStatusNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException(
                        "ERROR CRÍTICO: El estado de sistema '" + name + "' no ha sido precargado. " +
                                "Asegúrese de que SystemDataLoader se ejecute primero."
                ));
    }

    private void DepData(StatusModel status) {
        Drepository.save(new DepartmentsModel( UUID.randomUUID(), null,"Recursos Humanos", status));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Sistemas", status));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Urgencias", status));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Quirófanos ", status));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Morgue ", status));
        System.out.println("Departamentos cargados exitosamente.");
    }

    private void PosData(StatusModel status) {
        Prepository.save(new PositionsModel(UUID.randomUUID(), null,"Gerente", "Encargado de funciones internas", status));
        Prepository.save(new PositionsModel(UUID.randomUUID(),null, "Secretaria", "Gestor de juntas", status));
        System.out.println("Posiciones cargados exitosamente.");
    }

    @Override
    public void run(String... args) throws Exception {
        // LINEAS QUE EJECUTAN LOS ESTATUS
        StatusModel activeStatus = resolveStatus(Active);
        StatusModel inactiveStatus = resolveStatus(Inactive);

        if (Drepository.count() == 0) {
            DepData(activeStatus);
        }
        if (Prepository.count() == 0) {
            PosData(activeStatus);
        }
    }
}