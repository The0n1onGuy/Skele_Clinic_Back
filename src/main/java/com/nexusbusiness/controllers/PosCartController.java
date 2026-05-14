package com.nexusbusiness.controllers;

import com.nexusbusiness.beans.CartRequestObject;
import com.nexusbusiness.models.shoppingcart.SaleModel;
import com.nexusbusiness.repositories.IPosProductRepository;
import com.nexusbusiness.services.PosCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pos/cart")
public class PosCartController {
    @Autowired
    private PosCartService cartService;
    @Autowired private IPosProductRepository productRepository;

    // STEP 1: Consult products with their availability
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(cartService.getCatalogProducts());
    }

    // STEP 2 & 4: Final Checkout with inventory lock
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CartRequestObject request) {
        try {
            SaleModel receipt = cartService.processPurchase(request);
            // Here you would trigger your PDF generation service
            return ResponseEntity.ok(receipt);
        } catch (RuntimeException e) {
            // This is where we "bounce" the user if stock ran out
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }
}