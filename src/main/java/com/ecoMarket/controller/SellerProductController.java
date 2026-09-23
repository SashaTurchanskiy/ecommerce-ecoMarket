package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.ProductRequest;
import com.ecoMarket.dtos.response.ProductResponse;
import com.ecoMarket.model.Seller;
import com.ecoMarket.repository.SellerRepository;
import com.ecoMarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) throws Exception {
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new Exception("Seller not found with id: " + request.getSellerId()));

        ProductResponse createdProduct = productService.createProduct(request, seller);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponse>> getProductsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(productService.getProductBySellerId(sellerId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(productService.updateProduct(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws Exception {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
