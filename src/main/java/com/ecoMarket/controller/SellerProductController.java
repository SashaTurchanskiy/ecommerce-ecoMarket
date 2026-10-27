package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.ProductRequest;
import com.ecoMarket.dtos.response.ProductResponse;
import com.ecoMarket.model.Seller;
import com.ecoMarket.model.enums.Role;
import com.ecoMarket.repository.SellerRepository;
import com.ecoMarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final SellerRepository sellerRepository;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) throws Exception {
        Seller seller = getAuthenticatedSeller(authentication);
        ProductResponse createdProduct = productService.createProduct(request, seller);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponse>> getProductsBySeller(
            @PathVariable Long sellerId,
            Authentication authentication) {
        Seller seller = getAuthenticatedSeller(authentication);
        if (!seller.getId().equals(sellerId)) {
            throw new AccessDeniedException("Sellers can only view their own products");
        }
        return ResponseEntity.ok(productService.getProductBySellerId(sellerId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {
        Seller seller = getAuthenticatedSeller(authentication);
        return ResponseEntity.ok(productService.updateProduct(productId, request, seller));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId,
            Authentication authentication) throws Exception {
        Seller seller = getAuthenticatedSeller(authentication);
        productService.deleteProduct(productId, seller);
        return ResponseEntity.noContent().build();
    }

    private Seller getAuthenticatedSeller(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("Seller authentication is required");
        }
        boolean isSeller = authentication.getAuthorities().stream()
                .anyMatch(authority -> Role.ROLE_SELLER.name().equals(authority.getAuthority()));
        if (!isSeller) {
            throw new AccessDeniedException("Seller role is required");
        }

        String email = authentication.getName();
        if (email.startsWith("seller_")) {
            email = email.substring("seller_".length());
        }

        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new AccessDeniedException("Authenticated seller not found");
        }
        return seller;
    }
}
