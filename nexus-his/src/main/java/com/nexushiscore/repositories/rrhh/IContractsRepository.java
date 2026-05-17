package com.nexushiscore.repositories.rrhh;
import com.nexushiscore.models.rrhh.ContractsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContractsRepository extends JpaRepository<ContractsModel, Long> {
//    boolean existsByNameIgnoreCase(String name);
//
//    Optional<ContractsModel> findByNameIgnoreCase(String name);
}
