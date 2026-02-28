package com.expedienteclinico.expedienteclinico;

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
public class DataLoader implements CommandLineRunner {

    StatusModel Active;
    StatusModel Inactive;

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
        Drepository.save(new DepartmentsModel( UUID.randomUUID(), null,"Recursos Humanos", Active));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Sistemas", Active));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Urgencias", Active));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Quirófanos ", Active));
        Drepository.save(new DepartmentsModel(UUID.randomUUID(),null, "Morgue ", Active));
        System.out.println("Departamentos cargados exitosamente.");
    }

    private void PosData() {
        Prepository.save(new PositionsModel(UUID.randomUUID(), null,"Gerente", "Encargado de funciones internas", Active));
        Prepository.save(new PositionsModel(UUID.randomUUID(),null, "Secretaria", "Gestor de juntas", Active));
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