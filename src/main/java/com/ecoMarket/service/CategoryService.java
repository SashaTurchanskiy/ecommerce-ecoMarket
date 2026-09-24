package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CategoryRequest;
import com.ecoMarket.dtos.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request) throws Exception;

    CategoryResponse getByCategoryId(String categoryId) throws Exception;

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Long categoryId) throws Exception;
}
