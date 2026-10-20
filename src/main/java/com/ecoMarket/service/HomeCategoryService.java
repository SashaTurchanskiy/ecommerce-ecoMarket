package com.ecoMarket.service;

import com.ecoMarket.model.Home;
import com.ecoMarket.model.HomeCategory;

import java.util.List;


public interface HomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory);

    List<HomeCategory> createCategories(List<HomeCategory> homeCategory);

    HomeCategory updateHomeCategory(Long id, HomeCategory homeCategory);

    List<HomeCategory> getAllHomeCategories();
}
