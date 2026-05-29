package com.nexusbusiness.services;

import com.nexusbusiness.beans.CartCalculationResponseObject;
import com.nexusbusiness.beans.CartItemObject;
import com.nexusbusiness.beans.CartRequestObject;
import com.nexusbusiness.beans.ProductCatalogResponseObject;
import com.nexusbusiness.models.InventoryMovementModel;
import com.nexusbusiness.models.shoppingcart.*;
import com.nexusbusiness.models.system.*;
import com.nexusbusiness.repositories.shoppingcart.*;
import com.nexusbusiness.repositories.system.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PosCartService {
    @Autowired private IPosProductRepository productRepository;
    @Autowired private IPosInventoryRepository inventoryRepository;
    @Autowired private IPosSaleRepository saleRepository;
    @Autowired private IPosSaleDetailRepository detailRepository;
    @Autowired private IPosMovementRepository movementRepository;
    @Autowired private IPosOfferRepository offerRepository;

    @Autowired IStatusRepository statusRepo;


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

            // Extract just t|he name of the category to hide the object structure
            if (p.getCategory_id() != null) {
                dto.setCategory_name(p.getCategory_id().getName());
            }
            dto.setStatus_name(p.getStatus_id().getStatusName());
            return dto;

        }).toList();
    }

    public CartCalculationResponseObject calculateTotals(CartRequestObject request) {
        CartCalculationResponseObject response = new CartCalculationResponseObject();
        BigDecimal globalSubtotal = BigDecimal.ZERO;
        BigDecimal globalDiscount = BigDecimal.ZERO;
        List<CartCalculationResponseObject.CalculatedItem> details = new ArrayList<>();

        // We use the SKU from the new DTO we discussed
        for (CartRequestObject.CartItemRequest item : request.getItems()) {
            ProductModel product = productRepository.findBySku(item.getSku())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getSku()));

            BigDecimal qty = new BigDecimal(item.getQuantity());
            BigDecimal itemBaseSubtotal = product.getBase_price().multiply(qty);

            // 1. Fetch applicable offers
            List<OfferModel> applicableOffers = offerRepository.findActiveOffersForProduct(product, product.getCategory_id());

            // 2. Strategy: Find the Highest Value Discount per unit
            BigDecimal bestDiscountPerUnit = BigDecimal.ZERO;
            String appliedOfferName = "None";

            for (OfferModel offer : applicableOffers) {
                BigDecimal potentialDiscount = BigDecimal.ZERO;

                if ("PERCENTAGE".equalsIgnoreCase(offer.getDiscount_type())) {
                    // (base_price * discount_value) / 100
                    potentialDiscount = product.getBase_price()
                            .multiply(offer.getDiscount_value())
                            .divide(new BigDecimal("100"));
                } else if ("FIXED".equalsIgnoreCase(offer.getDiscount_type())) {
                    potentialDiscount = offer.getDiscount_value();
                }

                // If this offer saves more money than the previous best, replace it
                if (potentialDiscount.compareTo(bestDiscountPerUnit) > 0) {
                    bestDiscountPerUnit = potentialDiscount;
                    appliedOfferName = offer.getName();
                }
            }

            // 3. Apply the winning discount
            BigDecimal finalUnitPrice = product.getBase_price().subtract(bestDiscountPerUnit);
            BigDecimal totalItemDiscount = bestDiscountPerUnit.multiply(qty);

            // Prevent negative prices just in case a fixed discount is higher than the product price
            if (finalUnitPrice.compareTo(BigDecimal.ZERO) < 0) {
                finalUnitPrice = BigDecimal.ZERO;
                totalItemDiscount = product.getBase_price().multiply(qty);
            }

            // 4. Update Global Totals
            globalSubtotal = globalSubtotal.add(itemBaseSubtotal);
            globalDiscount = globalDiscount.add(totalItemDiscount);

            // 5. Build the detailed receipt for the frontend
            CartCalculationResponseObject.CalculatedItem detail = new CartCalculationResponseObject.CalculatedItem();
            detail.setSku(product.getSku());
            detail.setName(product.getName());
            detail.setQuantity(item.getQuantity());
            detail.setOriginalUnitPrice(product.getBase_price());
            detail.setFinalUnitPrice(finalUnitPrice);
            detail.setAppliedOfferName(appliedOfferName);
            details.add(detail);
        }

        response.setSubtotal(globalSubtotal);
        response.setTotalDiscount(globalDiscount);
        response.setFinalTotal(globalSubtotal.subtract(globalDiscount));
        response.setDetails(details);

        return response;
    }

    @Transactional
    public SaleModel processSale(CartRequestObject request) {
        // Run the exact same math the frontend just saw
        CartCalculationResponseObject calculatedCart = this.calculateTotals(request);

        SaleModel sale = new SaleModel();
        sale.setTicket_number("T-" + System.currentTimeMillis());
        sale.setPayment_method(request.getPaymentMethod());
        sale.setStatus_id(getStatusByName(Active));

        // Save the TRUE calculated totals!
        sale.setTotal_amount(calculatedCart.getFinalTotal());
        sale = saleRepository.save(sale);

        // Loop through the calculated details instead of the raw request
        for (CartCalculationResponseObject.CalculatedItem calcItem : calculatedCart.getDetails()) {
            ProductModel product = productRepository.findBySku(calcItem.getSku()).get();
            InventoryModel inventory = inventoryRepository.findByProduct(product).get();

            if (inventory.getCurrent_stock() < calcItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }

            // Create Sale Detail using the DISCOUNTED price
            SaleDetailModel detail = new SaleDetailModel();
            detail.setSale_id(sale);
            detail.setProduct_id(product);
            detail.setQuantity(calcItem.getQuantity());
            detail.setSold_price(calcItem.getFinalUnitPrice()); // Historical discounted price
            detail.setSubtotal(calcItem.getFinalUnitPrice().multiply(new BigDecimal(calcItem.getQuantity())));
            detailRepository.save(detail);

            // Deduct Inventory and Log Movement
            inventory.setCurrent_stock(inventory.getCurrent_stock() - calcItem.getQuantity());
            inventoryRepository.save(inventory);

            InventoryMovementModel movement = new InventoryMovementModel();
            movement.setProduct_id(product);
            movement.setType("SALE");
            movement.setQuantity(calcItem.getQuantity());
            movement.setReason("Checkout Ticket: " + sale.getTicket_number());
            movementRepository.save(movement);
        }

        return sale;
    }

    /*@Transactional // Critical: All steps succeed or all fail
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
            // Fetch Product and Inventory
            ProductModel product = productRepository.findByUuid(item.getProductUUID())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductUUID()));

            InventoryModel inventory = inventoryRepository.findByProduct(product)
                    .orElseThrow(() -> new RuntimeException("Inventory record missing for " + product.getName()));

            // FINAL INVENTORY CHECK (The "Bounce" logic)
            if (inventory.getCurrent_stock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName() +
                        ". Only " + inventory.getCurrent_stock() + " left.");
            }

            // Calculate financial data
            BigDecimal subtotal = product.getBase_price().multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            // Create Sale Detail (Historical Audit)
            SaleDetailModel detail = new SaleDetailModel();
            detail.setSale_id(sale);
            detail.setProduct_id(product);
            detail.setQuantity(item.getQuantity());
            detail.setSold_price(product.getBase_price()); //Save current price for log
            detail.setSubtotal(subtotal);
            detailRepository.save(detail);

            // Update Inventory and Create Audit Movement
            inventory.setCurrent_stock(inventory.getCurrent_stock() - item.getQuantity());
            inventoryRepository.save(inventory);

            InventoryMovementModel movement = new InventoryMovementModel();
            movement.setProduct_id(product);
            movement.setType("SALE"); // REMOVE
            movement.setQuantity(item.getQuantity());
            movement.setReason("Checkout Ticket: " + sale.getTicket_number());
            movementRepository.save(movement);
        }

        sale.setTotal_amount(totalAmount);
        return saleRepository.save(sale);
    }

    */
}

