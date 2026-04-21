<<<<<<<< HEAD:src/main/java/com/expedienteclinico/expedienteclinico/services/warehouse/CategoryService.java
package com.expedienteclinico.expedienteclinico.services.warehouse;

import com.expedienteclinico.expedienteclinico.models.warehouse.CategoryModel;
import com.expedienteclinico.expedienteclinico.repositories.warehouse.ICategoryRepository;
========
package com.nexuscore.services.almacen;

import com.nexuscore.models.almacen.CategoryModel;
import com.nexuscore.repositories.almacen.ICategoryRepository;
>>>>>>>> origin/LC:src/main/java/com/nexuscore/services/almacen/CategoryService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private ICategoryRepository iCategoryRepository;

    public List<CategoryModel> getAllCategories() {
        return iCategoryRepository.findAll();
    }

    public CategoryModel saveCategory(CategoryModel category) {
        return iCategoryRepository.save(category);
    }

    public Optional<CategoryModel> getCategoryById(Long id) {
        return iCategoryRepository.findById(id);
    }

//    public void cambiarstatus(Long id) {
//
//    }

    public List<CategoryModel> findByNameContaining(String name) {
        return iCategoryRepository.findByNameContainingIgnoreCase(name);
    }
}
