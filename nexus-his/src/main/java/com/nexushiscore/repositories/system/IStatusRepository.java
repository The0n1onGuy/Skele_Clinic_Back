package com.nexushiscore.repositories.system;

import com.nexushiscore.models.system.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStatusRepository extends JpaRepository<StatusModel, Long> {
    Optional<StatusModel> findByStatusNameIgnoreCase(String statusName);

}
