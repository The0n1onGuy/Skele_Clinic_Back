package com.nexuscore.controllers.warehouse;

import com.nexuscore.models.warehouse.CategoryModel;
import com.nexuscore.services.warehouse.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 1. Cambiamos las anotaciones de Spring por Mockito puro
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // 2. Configuramos MockMvc manualmente apuntando al controlador
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    @DisplayName("GET /api/warehouse/categories - Debería retornar lista completa")
    void testGetAll() throws Exception {
        // Given
        CategoryModel cat = new CategoryModel(1L, "Pharmacy", "Desc");
        when(categoryService.getAllCategories()).thenReturn(List.of(cat));

        // When & Then
        mockMvc.perform(get("/api/warehouse/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pharmacy"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("POST /api/warehouse/categories - Debería crear categoría con éxito")
    void testCreate() throws Exception {
        // Given
        CategoryModel input = new CategoryModel(null, "Medical Equipment", "Tools");
        CategoryModel output = new CategoryModel(1L, "Medical Equipment", "Tools");

        when(categoryService.saveCategory(any(CategoryModel.class))).thenReturn(output);

        // When & Then
        mockMvc.perform(post("/api/warehouse/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Medical Equipment"))
                .andExpect((jsonPath("$.description").value("Tools")));
    }

    @Test
    @DisplayName("GET /search - Debería buscar por nombre")
    void testSearchByName() throws Exception {
        // Given
        CategoryModel cat = new CategoryModel(1L, "Pharmacy", "Desc");
        when(categoryService.findByNameContaining("Phar")).thenReturn(List.of(cat));

        // When & Then
        mockMvc.perform(get("/api/warehouse/categories/search")
                        .param("name", "Phar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pharmacy"));
    }
}