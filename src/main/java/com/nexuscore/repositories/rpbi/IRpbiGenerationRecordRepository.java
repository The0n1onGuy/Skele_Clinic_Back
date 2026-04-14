package com.nexuscore.repositories.rpbi;

import com.nexuscore.models.rpbi.RpbiGenerationRecordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IRpbiGenerationRecordRepository extends JpaRepository<RpbiGenerationRecordModel, Long> {

    // Para poder "Traducir" de UUID a ID en las operaciones internas
    Optional<RpbiGenerationRecordModel> findByUuid(String uuid);

}
