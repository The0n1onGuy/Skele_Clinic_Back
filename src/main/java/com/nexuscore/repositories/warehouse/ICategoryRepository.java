package com.nexuscore.repositories.warehouse;

import com.nexuscore.models.warehouse.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, Long> {
    List<CategoryModel> findByNameContainingIgnoreCase(String name);
}