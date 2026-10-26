package com.ecoMarket.controller;

import com.ecoMarket.model.Home;
import com.ecoMarket.model.HomeCategory;
import com.ecoMarket.service.HomeCategoryService;
import com.ecoMarket.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homeCategories")
@RequiredArgsConstructor
public class HomeCategoryController {

    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/create")
    public ResponseEntity<Home> createHomeCategories(
            @RequestBody List<HomeCategory> homeCategories){

        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.CREATED);
    }

    @GetMapping("/admin/home/category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory(){
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        return  ResponseEntity.ok(categories);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
            @PathVariable Long id,
            @RequestBody HomeCategory homeCategory) throws Exception {
        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(id, homeCategory);
        return ResponseEntity.ok(updatedCategory);

    }
}
