package com.expedienteclinico.expedienteclinico.repositories;

import com.expedienteclinico.expedienteclinico.models.Morgue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMorgueRepository extends JpaRepository<Morgue, Long> {
}