package com.nexusbusiness.seeders;
import com.nexusbusiness.models.shoppingcart.*;
import com.nexusbusiness.models.system.StatusModel;
import com.nexusbusiness.repositories.shoppingcart.*;
import com.nexusbusiness.repositories.system.*;
import com.nexussharedcore.security.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Order(10) // Executes after HttpDataLoader and Status initial seeding
public class PosDataLoader implements CommandLineRunner {

    private final IPosCategoryRepository categoryRepository;
    private final IPosProductRepository productRepository;
    private final IPosInventoryRepository inventoryRepository;
    private final IStatusRepository statusRepository;
    private final IPosOfferRepository offerRepository;

    @Value("${NEXUS_SEED_TARGET_TENANT:pos_prueba_2026}")
    private String targetTenant;

    public PosDataLoader(IPosCategoryRepository categoryRepository
                         ,IPosProductRepository productRepository
                         ,IPosInventoryRepository inventoryRepository
                         ,IStatusRepository statusRepository
                         ,IPosOfferRepository offerRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.statusRepository = statusRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    //@Transactional
    public void run(String... args) {
        // Target the master schema for this initial test phase
        if (targetTenant == null || targetTenant.trim().isEmpty()) {
            log.info(">>> POS DataLoader: No target tenant specified in environment variables (NEXUS_SEED_TARGET_TENANT). Skipping seed.");
            return;
        }
        //TenantContext.setCurrentTenant("pos_prueba_2026");

        try {
            // Ensure we have an "Active" status
            StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                    .orElseThrow(() -> new RuntimeException("Initial 'Active' status not found."));
            log.info(">>> POS DataLoader: Switching context to target tenant: {}", targetTenant);
            TenantContext.setCurrentTenant(targetTenant);
            // Seed Categories (Catalog level 0)
            if (categoryRepository.count() == 0) {
                seedCategories();
            }

            // Seed Products and Inventory (Dependent levels)
            if (productRepository.count() == 0) {
                seedProductsAndInventory(activeStatus);
            }

            if (offerRepository.count() == 0) {
                seedOffers();
            }

            log.info(">>> POS Data: Initial test data loaded correctly into ''." + TenantContext.getCurrentTenant());
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

    //product1[2] = {"qamous", "sus"};

    private void seedProductsAndInventory(StatusModel status) {
        // Fetch categories to link them
        PosCategoryModel groceries = categoryRepository.findByNameIgnoreCase("Groceries").orElse(null);
        PosCategoryModel pharmacy = categoryRepository.findByNameIgnoreCase("Pharmacy").orElse(null);

        // Seed Products
        ProductModel chips = saveProduct("SKU-001", "Doritos Nachos 60g", "Sabritas famosas", new BigDecimal("15.50"), groceries, status);
        ProductModel aspirin = saveProduct("SKU-002", "Aspirina 500mg", "Medicamente para dolor, fiebre, inflamaciones y problemas cardiacos", new BigDecimal("45.00"), pharmacy, status);
        ProductModel water = saveProduct("SKU-003", "Agua Purificada 1L", "Agua potable bebible",  new BigDecimal("12.00"), groceries, status);

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

    private ProductModel saveProduct(String sku, String name,String description, BigDecimal price,PosCategoryModel cat, StatusModel status) {
        ProductModel product = new ProductModel();
        product.setUuid(UUID.randomUUID());
        product.setSku(sku);
        product.setName(name);
        product.setBase_price(price);
        product.setBase_description(description);
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

    private void seedOffers() {
        // 1. Recuperamos los objetos de la base de datos
        ProductModel aspirin = productRepository.findBySku("SKU-002").orElse(null);
        PosCategoryModel groceries = categoryRepository.findByNameIgnoreCase("Groceries").orElse(null);

        // 2. Creamos la oferta para Farmacia (Aspirina)
        if (aspirin != null) {
            OfferModel healthOffer = new OfferModel();
            healthOffer.setName("Descuento Salud 15%");
            healthOffer.setDiscount_type("PERCENTAGE");
            healthOffer.setDiscount_value(new BigDecimal("15.00"));
            healthOffer.setActive(true);
            healthOffer.setProducts(List.of(aspirin));
            offerRepository.save(healthOffer);
        }

        // 3. Creamos la oferta para Abarrotes (Categoría entera)
        if (groceries != null) {
            OfferModel groceryOffer = new OfferModel();
            groceryOffer.setName("Abarrotes 10% Off");
            groceryOffer.setDiscount_type("PERCENTAGE");
            groceryOffer.setDiscount_value(new BigDecimal("10.00"));
            groceryOffer.setActive(true);
            groceryOffer.setCategories(List.of(groceries));
            offerRepository.save(groceryOffer);
        }

        log.info("-> Promotion logic and discounts seeded.");
    }

    /*
    private void seedOffers(ProductModel aspirin, PosCategoryModel groceries) {
        // Create the Offer for Pharmacy (15% off Aspirin specifically)
        OfferModel healthOffer = new OfferModel();
        healthOffer.setName("Descuento Salud 15%");
        healthOffer.setDiscount_type("PERCENTAGE");
        healthOffer.setDiscount_value(new BigDecimal("15.00"));
        healthOffer.setActive(true);
        // Link the product directly to the offer list
        healthOffer.setProducts(List.of(aspirin));

        offerRepository.save(healthOffer); // Hibernate automatically writes to pos_offer_products
        // Create the Offer for Groceries (10% off entire Category)
        OfferModel groceryOffer = new OfferModel();
        groceryOffer.setName("Abarrotes 10% Off");
        groceryOffer.setDiscount_type("PERCENTAGE");
        groceryOffer.setDiscount_value(new BigDecimal("10.00"));
        groceryOffer.setActive(true);
        // Link the category directly to the offer list
        groceryOffer.setCategories(List.of(groceries));
        offerRepository.save(groceryOffer); // Hibernate automatically writes to pos_offer_categories

        log.info("-> Promotion logic and discounts seeded.");
    }
    */

}