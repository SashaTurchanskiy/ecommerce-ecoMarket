package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.CategoryRequest;
import com.ecoMarket.dtos.response.CategoryResponse;
import com.ecoMarket.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) throws Exception {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable String categoryId) throws Exception {
        return ResponseEntity.ok(categoryService.getByCategoryId(categoryId));
    }
}
