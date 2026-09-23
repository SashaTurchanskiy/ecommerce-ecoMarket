package com.ecoMarket.controller;

import com.ecoMarket.dtos.response.ProductResponse;
import com.ecoMarket.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long productId) throws Exception {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam(required = false) String query){
        return ResponseEntity.ok(productService.searchProduct(query));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProduct(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false) String sizes,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        return new ResponseEntity<>(
                productService.getAllProduct(
                        category, brand, colors, sizes,
                        minPrice, maxPrice, minDiscount,
                        sort, stock, pageNumber),
                HttpStatus.OK
        );

    }


}
