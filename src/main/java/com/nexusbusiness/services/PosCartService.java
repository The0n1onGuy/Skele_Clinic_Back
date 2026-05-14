package com.nexusbusiness.services;

import com.nexusbusiness.beans.CartItemObject;
import com.nexusbusiness.beans.CartRequestObject;
import com.nexusbusiness.beans.ProductCatalogResponseObject;
import com.nexusbusiness.models.InventoryMovementModel;
import com.nexusbusiness.models.shoppingcart.InventoryModel;
import com.nexusbusiness.models.shoppingcart.ProductModel;
import com.nexusbusiness.models.shoppingcart.SaleDetailModel;
import com.nexusbusiness.models.shoppingcart.SaleModel;
import com.nexusbusiness.repositories.*;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.repositories.system.IStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PosCartService {
    @Autowired private IPosProductRepository productRepository;
    @Autowired private IPosInventoryRepository inventoryRepository;
    @Autowired private IPosSaleRepository saleRepository;
    @Autowired private IPosSaleDetailRepository detailRepository;
    @Autowired private IPosMovementRepository movementRepository;

    @Autowired
    IStatusRepository statusRepo;


    @Value("${STATUS1:Active}") private String Active;
    @Value("${STATUS2:Inactive}") private String Inactive;

    //Filtro por nombre del estado
    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }
    public List<ProductCatalogResponseObject> getCatalogProducts() {
        return productRepository.findAll().stream().map(p -> {
            ProductCatalogResponseObject dto = new ProductCatalogResponseObject();
            dto.setUuid(p.getUuid());
            dto.setProduct_name(p.getName());
            dto.setProduct_sku(p.getSku());
            dto.setNormal_price(p.getBase_price());

            // Extract just the name of the category to hide the object structure
            if (p.getCategory_id() != null) {
                dto.setCategory_name(p.getCategory_id().getName());
            }
            dto.setStatus_name(p.getStatus_id().getStatusName());
            return dto;

        }).toList();
    }
    @Transactional // Critical: All steps succeed or all fail
    public SaleModel processPurchase(CartRequestObject request) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        SaleModel sale = new SaleModel();
        sale.setTicket_number("T-" + System.currentTimeMillis());
        //TODO Multiple payments MORE CHANGES

        sale.setPayment_method(request.getPaymentMethod());
        StatusModel activeStatus = getStatusByName(Active);
        sale.setStatus_id(activeStatus);
        sale.setTotal_amount(BigDecimal.ZERO);
        // Initial save to get an ID for details
        sale = saleRepository.save(sale);

        for (CartItemObject item : request.getItems()) {
            // 1. Fetch Product and Inventory
            ProductModel product = productRepository.findByUuid(item.getProductUUID())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductUUID()));

            InventoryModel inventory = inventoryRepository.findByProduct(product)
                    .orElseThrow(() -> new RuntimeException("Inventory record missing for " + product.getName()));

            // 2. FINAL INVENTORY CHECK (The "Bounce" logic)
            if (inventory.getCurrent_stock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName() +
                        ". Only " + inventory.getCurrent_stock() + " left.");
            }

            // 3. Calculate financial data
            BigDecimal subtotal = product.getBase_price().multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            // 4. Create Sale Detail (Historical Audit)
            SaleDetailModel detail = new SaleDetailModel();
            detail.setSale_id(sale);
            detail.setProduct_id(product);
            detail.setQuantity(item.getQuantity());
            detail.setSold_price(product.getBase_price()); // Protection: Save current price
            detail.setSubtotal(subtotal);
            detailRepository.save(detail);

            // 5. Update Inventory and Create Audit Movement
            inventory.setCurrent_stock(inventory.getCurrent_stock() - item.getQuantity());
            inventoryRepository.save(inventory);

            InventoryMovementModel movement = new InventoryMovementModel();
            movement.setProduct_id(product);
            movement.setType("SALE"); // REMOVE
            movement.setQuantity(-item.getQuantity());
            movement.setReason("Checkout Ticket: " + sale.getTicket_number());
            movementRepository.save(movement);
        }

        sale.setTotal_amount(totalAmount);
        return saleRepository.save(sale);
    }
}
