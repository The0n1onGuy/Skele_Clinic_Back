package com.expedienteclinico.expedienteclinico.services.warehouse;

import com.expedienteclinico.expedienteclinico.models.warehouse.CategoryModel;
import com.expedienteclinico.expedienteclinico.repositories.warehouse.ICategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryModel category;

    @BeforeEach
    void setUp() {
        category = new CategoryModel(1L, "Farmacia", "Descripción de prueba");
    }

    @Test
    @DisplayName("Debería retornar todas las categorías")
    void testGetAllCategories() {
        // Given
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        // When
        List<CategoryModel> result = categoryService.getAllCategories();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Farmacia", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería guardar una categoría con éxito")
    void testSaveCategory() {
        // Given
        when(categoryRepository.save(any(CategoryModel.class))).thenReturn(category);

        // When
        CategoryModel savedCategory = categoryService.saveCategory(category);

        // Then
        assertNotNull(savedCategory);
        assertEquals("Farmacia", savedCategory.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Debería encontrar una categoría por ID")
    void testGetCategoryById() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When
        Optional<CategoryModel> found = categoryService.getCategoryById(1L);

        // Then
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    @DisplayName("Debería buscar categorías por nombre conteniendo el texto")
    void testFindByNameContaining() {
        // Given
        String search = "Far";
        when(categoryRepository.findByNameContainingIgnoreCase(search))
                .thenReturn(Arrays.asList(category));

        // When
        List<CategoryModel> result = categoryService.findByNameContaining(search);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains(search));
    }
}