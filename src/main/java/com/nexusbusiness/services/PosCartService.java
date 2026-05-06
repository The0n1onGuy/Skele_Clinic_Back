package com.nexusbusiness.services;

import com.nexusbusiness.beans.CartItem;
import com.nexusbusiness.beans.CartRequest;
import com.nexusbusiness.models.InventoryMovementModel;
import com.nexusbusiness.models.shoppingcart.InventoryModel;
import com.nexusbusiness.models.shoppingcart.ProductModel;
import com.nexusbusiness.models.shoppingcart.SaleDetailModel;
import com.nexusbusiness.models.shoppingcart.SaleModel;
import com.nexusbusiness.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PosCartService {
    @Autowired private IPosProductRepository productRepository;
    @Autowired private IPosInventoryRepository inventoryRepository;
    @Autowired private IPosSaleRepository saleRepository;
    @Autowired private IPosSaleDetailRepository detailRepository;
    @Autowired private IPosMovementRepository movementRepository;

    @Transactional // Critical: All steps succeed or all fail
    public SaleModel processPurchase(CartRequest request) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        SaleModel sale = new SaleModel();
        sale.setTicket_number("T-" + System.currentTimeMillis());
        sale.setPayment_method(request.getPaymentMethod());
        // Initial save to get an ID for details
        sale = saleRepository.save(sale);

        for (CartItem item : request.getItems()) {
            // 1. Fetch Product and Inventory
            ProductModel product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

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
            movement.setType("SALE");
            movement.setQuantity(-item.getQuantity());
            movement.setReason("Checkout Ticket: " + sale.getTicket_number());
            movementRepository.save(movement);
        }

        sale.setTotal_amount(totalAmount);
        return saleRepository.save(sale);
    }
}
