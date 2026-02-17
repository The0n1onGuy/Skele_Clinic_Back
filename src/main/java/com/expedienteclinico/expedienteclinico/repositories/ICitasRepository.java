package com.expedienteclinico.expedienteclinico.repositories;

import com.expedienteclinico.expedienteclinico.models.CitasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICitasRepository extends JpaRepository<CitasModel, Long> {
}