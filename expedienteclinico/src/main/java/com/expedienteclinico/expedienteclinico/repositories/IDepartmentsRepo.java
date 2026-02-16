package com.expedienteclinico.expedienteclinico.repositories;
import com.expedienteclinico.expedienteclinico.models.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartmentsRepo extends JpaRepository<Departments, Integer> {
}
