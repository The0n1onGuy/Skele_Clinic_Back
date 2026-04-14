package com.nexuscore.seeders;

import com.nexuscore.models.rrhh.EmployeesModel;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.models.rrhh.DepartmentsModel;
import com.nexuscore.models.rrhh.PositionsModel;
import com.nexuscore.repositories.rrhh.IEmployeesRepository;
import com.nexuscore.repositories.system.IStatusRepository;
import com.nexuscore.repositories.rrhh.IDepartmentsRepository;
import com.nexuscore.repositories.rrhh.IPositionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    @Autowired
    private IEmployeesRepository Erepository;
////    EL CREADOR DE LOS STATUS
//
//
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

//  VERSION DE SOLO TRANSFERENCIA.

    private StatusModel resolveStatus(String name) {
        return Srepository.findByStatusNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException(
                        "ERROR CRÍTICO: El estado de sistema '" + name + "' no ha sido precargado. " +
                                "Asegúrese de que SystemDataLoader se ejecute primero."
                ));
    }

    private void DepData(StatusModel status) {
        Drepository.save(new DepartmentsModel(  null, UUID.randomUUID(),"Recursos Humanos", status));
        Drepository.save(new DepartmentsModel(null, UUID.randomUUID(), "Quirófanos ", status));
        Drepository.save(new DepartmentsModel(null, UUID.randomUUID(),"Urgencias", status));
        Drepository.save(new DepartmentsModel(null, UUID.randomUUID(),"Morgue ", status));
        Drepository.save(new DepartmentsModel(null, UUID.randomUUID(), "Sistemas", status));
        System.out.println("Departamentos cargados exitosamente.");
    }

    private void PosData(StatusModel status) {
        Prepository.save(new PositionsModel( null, UUID.randomUUID(),"Gerente", "Encargado de funciones internas", status));
        Prepository.save(new PositionsModel(null,UUID.randomUUID(), "Secretaria", "Gestor de juntas", status));
        System.out.println("Posiciones cargados exitosamente.");
    }

    private void EmpData(StatusModel status, DepartmentsModel dept, PositionsModel pos) {
        Erepository.save(new EmployeesModel(
                null,UUID.randomUUID(), "Juan", "Pérez", "López", "PELJ900101HDFRRN01"
                , "PELJ900101123"
                , LocalDate.parse( "1990-01-01" )
                , LocalDate.parse( "2026-03-11" )
                ,"M"
                , pos
                , dept
                , status
        ));
        System.out.println("Empleados base cargados exitosamente.");
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
        if (Erepository.count() == 0) {
            DepartmentsModel defaultDept = Drepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No hay departamentos para asignar al empleado."));
            PositionsModel defaultPos = Prepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No hay posiciones para asignar al empleado."));

            EmpData(activeStatus, defaultDept, defaultPos);
        }
    }
}