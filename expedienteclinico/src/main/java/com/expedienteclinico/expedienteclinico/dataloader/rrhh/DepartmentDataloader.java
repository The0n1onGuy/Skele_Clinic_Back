/*
package com.expedienteclinico.expedienteclinico.dataloader.rrhh;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component // Esto lo convierte en un "DataLoader" manejado por Spring
public class DepartmentDataloader implements CommandLineRunner {
    @Autowired
    private IDepartmentsRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            loadData();
        }
    }

    private void loadData() {
        // Usamos el orden que establecimos: UUID, luego el resto
        repository.save(new DepartmentsModel(null, UUID.randomUUID(), "Recursos Humanos"));
        repository.save(new DepartmentsModel(null,UUID.randomUUID(), "Sistemas"));
        repository.save(new DepartmentsModel(null,UUID.randomUUID(), "Urgencias"));
        repository.save(new DepartmentsModel(null,UUID.randomUUID(), "Quirófanos "));
        repository.save(new DepartmentsModel(null,UUID.randomUUID(), "Morgue "));

        System.out.println(">> DataLoader: Departamentos cargados exitosamente.");
    }
}





*/