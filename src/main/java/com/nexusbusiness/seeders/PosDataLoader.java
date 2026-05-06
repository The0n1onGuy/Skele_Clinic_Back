package com.nexusbusiness.seeders;
import com.nexusbusiness.models.shoppingcart.*;
import com.nexusbusiness.repositories.*;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.repositories.system.IStatusRepository;
import com.nexuscore.security.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@Order(10) // Executes after HttpDataLoader and Status initial seeding
public class PosDataLoader implements CommandLineRunner {

    private final IPosCategoryRepository categoryRepository;
    private final IPosProductRepository productRepository;
    private final IPosInventoryRepository inventoryRepository;
    private final IStatusRepository statusRepository;

    public PosDataLoader(IPosCategoryRepository categoryRepository,
                         IPosProductRepository productRepository,
                         IPosInventoryRepository inventoryRepository,
                         IStatusRepository statusRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Target the master schema for this initial test phase
        TenantContext.setCurrentTenant("his_master");

        try {
            // 1. Ensure we have an "Active" status from your master status table
            StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                    .orElseThrow(() -> new RuntimeException("Initial 'Active' status not found in Master."));

            // 2. Seed Categories (Catalog level 0)
            if (categoryRepository.count() == 0) {
                seedCategories();
            }

            // 3. Seed Products and Inventory (Dependent levels)
            if (productRepository.count() == 0) {
                seedProductsAndInventory(activeStatus);
            }

            log.info(">>> POS Data: Initial test data loaded correctly into 'his_master'.");
        } catch (Exception e) {
            log.error(">>> POS Data Error: Failed to seed POS logic.", e);
        } finally {
            TenantContext.clear();
        }
    }

    private void seedCategories() {
        saveCategory("Groceries", "General food and household items.");
        saveCategory("Pharmacy", "Over-the-counter medication and first aid.");
        saveCategory("Beverages", "Soft drinks, water, and juices.");
        log.info("-> Categories seeded.");
    }

    private void seedProductsAndInventory(StatusModel status) {
        // Fetch categories to link them
        PosCategoryModel groceries = categoryRepository.findByNameIgnoreCase("Groceries").orElse(null);
        PosCategoryModel pharmacy = categoryRepository.findByNameIgnoreCase("Pharmacy").orElse(null);

        // Seed Products
        ProductModel chips = saveProduct("SKU-001", "Doritos Nachos 60g", new BigDecimal("15.50"), groceries, status);
        ProductModel aspirin = saveProduct("SKU-002", "Aspirina 500mg", new BigDecimal("45.00"), pharmacy, status);
        ProductModel water = saveProduct("SKU-003", "Agua Purificada 1L", new BigDecimal("12.00"), groceries, status);

        // Seed Inventory for these products[cite: 3]
        saveInventory(chips, 100, 10);
        saveInventory(aspirin, 50, 5);
        saveInventory(water, 200, 20);

        log.info("-> Products and Inventory seeded.");
    }

    // Helper methods for defensive saving
    private void saveCategory(String name, String desc) {
        PosCategoryModel cat = new PosCategoryModel();
        cat.setName(name);
        cat.setDescription(desc);
        categoryRepository.save(cat);
    }

    private ProductModel saveProduct(String sku, String name, BigDecimal price, PosCategoryModel cat, StatusModel status) {
        ProductModel product = new ProductModel();
        product.setUuid(UUID.randomUUID());
        product.setSku(sku);
        product.setName(name);
        product.setBase_price(price);
        product.setCategory_id(cat);
        product.setStatus_id(status);
        return productRepository.save(product);
    }

    private void saveInventory(ProductModel product, int current, int min) {
        InventoryModel inv = new InventoryModel();
        inv.setProduct(product);
        inv.setCurrent_stock(current);
        inv.setMin_stock(min);
        inventoryRepository.save(inv);
    }
}