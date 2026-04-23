package com.nexuscore.models.warehouse;

import com.nexuscore.models.warehouse.CategoryModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryModelTest {

    @Test
    @DisplayName("Debería crear una instancia de CategoryModel y verificar sus atributos")
    void testCategoryCreation() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setName("Farmacia");
        category.setDescription("Artículos de farmacia y medicamentos");

        assertEquals(1L, category.getId());
        assertEquals("Farmacia", category.getName());
        assertEquals("Artículos de farmacia y medicamentos", category.getDescription());
    }

    @Test
    @DisplayName("Debería funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        CategoryModel category = new CategoryModel(2L, "Equipo Médico", "Maquinaria especializada");

        assertAll("Verificación de campos",
                () -> assertEquals(2L, category.getId()),
                () -> assertEquals("Equipo Médico", category.getName()),
                () -> assertEquals("Maquinaria especializada", category.getDescription())
        );
    }
}