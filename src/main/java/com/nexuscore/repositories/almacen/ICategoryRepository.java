<<<<<<<< HEAD:src/main/java/com/expedienteclinico/expedienteclinico/repositories/warehouse/ICategoryRepository.java
package com.expedienteclinico.expedienteclinico.repositories.warehouse;

import com.expedienteclinico.expedienteclinico.models.warehouse.CategoryModel;
========
package com.nexuscore.repositories.almacen;

import com.nexuscore.models.almacen.CategoryModel;
>>>>>>>> origin/LC:src/main/java/com/nexuscore/repositories/almacen/ICategoryRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, Long> {
    List<CategoryModel> findByNameContainingIgnoreCase(String name);
}