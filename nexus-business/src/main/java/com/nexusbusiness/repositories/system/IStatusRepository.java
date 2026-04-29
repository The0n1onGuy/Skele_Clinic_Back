package com.nexusbusiness.repositories.system;

import com.nexusbusiness.models.system.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStatusRepository extends JpaRepository<StatusModel, Long> {
    Optional<StatusModel> findByStatusNameIgnoreCase(String statusName);

}
