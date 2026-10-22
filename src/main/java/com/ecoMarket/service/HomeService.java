package com.ecoMarket.service;

import com.ecoMarket.model.Home;
import com.ecoMarket.model.HomeCategory;
import com.ecoMarket.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HomeService {

    Home createHomePageData(List<HomeCategory> allCategories);
}
