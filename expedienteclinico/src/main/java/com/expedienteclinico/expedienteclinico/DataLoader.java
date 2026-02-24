package com.expedienteclinico.expedienteclinico;

import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.PositionsModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IPositionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.xml.soap.Text;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IDepartmentsRepository Drepository;

    @Autowired
    private IPositionsRepository Prepository;

    private void DepData() {
        // Usamos el orden que establecimos: UUID, luego el resto
        Drepository.save(new DepartmentsModel(null, UUID.randomUUID(), "Recursos Humanos"));
        Drepository.save(new DepartmentsModel(null,UUID.randomUUID(), "Sistemas"));
        Drepository.save(new DepartmentsModel(null,UUID.randomUUID(), "Urgencias"));
        Drepository.save(new DepartmentsModel(null,UUID.randomUUID(), "Quirófanos "));
        Drepository.save(new DepartmentsModel(null,UUID.randomUUID(), "Morgue "));
        System.out.println("Departamentos cargados exitosamente.");
    }

    private void PosData() {
        // Usamos el orden que establecimos: UUID, luego el resto
        Prepository.save(new PositionsModel(null, UUID.randomUUID(), "Gerente", "Encargado de funciones internas"));
        Prepository.save(new PositionsModel(null, UUID.randomUUID(), "Secretaria", "Gestor de juntas"));
        System.out.println("Posiciones cargados exitosamente.");
    }

    @Override
    public void run(String... args) throws Exception {
        if (Drepository.count() == 0) {
            DepData();
        }
        if (Prepository.count() == 0) {
            PosData();
        }
    }
}