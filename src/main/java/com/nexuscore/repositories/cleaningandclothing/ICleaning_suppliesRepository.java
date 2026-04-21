package com.nexuscore.repositories.cleaningandclothing;

import com.nexuscore.models.cleaningandclothing.Cleaning_suppliesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICleaning_suppliesRepository extends JpaRepository<Cleaning_suppliesModel, Long> {

    Optional<Cleaning_suppliesModel> findByUuid(String uuid);

}