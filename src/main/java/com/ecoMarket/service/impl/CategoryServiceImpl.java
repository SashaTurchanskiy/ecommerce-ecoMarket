package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.CategoryRequest;
import com.ecoMarket.dtos.response.CategoryResponse;
import com.ecoMarket.mapper.CategoryMapper;
import com.ecoMarket.model.Category;
import com.ecoMarket.repository.CategoryRepository;
import com.ecoMarket.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) throws Exception {
//        Category checkCategory = categoryRepository.findByCategoryId(request.getCategoryId());
//        if (checkCategory != null){
//            throw new Exception("cannot find category with id");
//        }
        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getByCategoryId(String categoryId) throws Exception {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if (categoryId == null){
            throw new Exception("cannot find categoryId");
        }
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteCategory(Long categoryId) throws Exception {
        return;

    }
}
