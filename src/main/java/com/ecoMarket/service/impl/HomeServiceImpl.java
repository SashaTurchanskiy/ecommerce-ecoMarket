package com.ecoMarket.service.impl;

import com.ecoMarket.model.Deal;
import com.ecoMarket.model.Home;
import com.ecoMarket.model.HomeCategory;
import com.ecoMarket.model.enums.HomeCategorySection;
import com.ecoMarket.repository.DealRepository;
import com.ecoMarket.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategorySection.GRID)
                .toList();

        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES)
                .toList();

        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES)
                .toList();

        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(cateogry ->
                        cateogry.getSection() == HomeCategorySection.DEALS)
                .toList();

        List<Deal> createDeals = new ArrayList<>();

        if (dealRepository.findAll().isEmpty()){
            List<Deal> daels = allCategories.stream()
                    .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .toList();
            createDeals = dealRepository.saveAll(daels);
        }else createDeals = dealRepository.findAll();

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectricCategories(electricCategories);
        home.setDeals(createDeals);
        home.setDealCategories(dealCategories);

        return home;
    }
}
