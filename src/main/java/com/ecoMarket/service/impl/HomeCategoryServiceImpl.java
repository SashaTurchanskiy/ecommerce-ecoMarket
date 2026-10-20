package com.ecoMarket.service.impl;

import com.ecoMarket.model.HomeCategory;
import com.ecoMarket.repository.HomeCategoryRepository;
import com.ecoMarket.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return null;
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategory) {
        return List.of();
    }

    @Override
    public HomeCategory updateHomeCategory(Long id, HomeCategory homeCategory) {
        return null;
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}
