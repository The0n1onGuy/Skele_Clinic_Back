package com.expedienteclinico.expedienteclinico.controllers.warehouse;

import com.expedienteclinico.expedienteclinico.models.warehouse.CategoryModel;
import com.expedienteclinico.expedienteclinico.services.warehouse.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse/categories")
@Tag(name = "Warehouse: Categorías", description = "Category management of articles for Pharmacy, Clinic and Equipment")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories", description = "Returns a list of all warehouse categories defined in the system.")
    @GetMapping
    public List<CategoryModel> getAll() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Create a new category", description = "Save a new category such as 'Pharmacy' or 'Medical Equipment' in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Category created successfully")
    })
    @PostMapping
    public CategoryModel create(@RequestBody CategoryModel category) {
        return categoryService.saveCategory(category);
    }

    @Operation(summary = "Get category by ID", description = "Searches for a specific category using its unique integer ID. Returns 404 if not found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found and successfully returned"),
            @ApiResponse(responseCode = "404", description = "The provided category ID does not exist in the database")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getById(
            @Parameter(description = "ID of the category to be retrieved", example = "1")
            @PathVariable Long id) {

        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search categories by name", description = "Filter categories by a partial name string (case-insensitive).")
    @GetMapping("/search")
    public List<CategoryModel> searchByName(
            @Parameter(description = "Partial name of the category to search", example = "Pharmacy")
            @RequestParam String name) {

        return categoryService.findByNameContaining(name);
    }
}