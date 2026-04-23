package com.nexuscore.repositories.rrhh;
import com.nexuscore.models.rrhh.DepartmentsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository

public interface IDepartmentsRepository extends JpaRepository<DepartmentsModel, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<DepartmentsModel> findByNameIgnoreCase(String name);
}
