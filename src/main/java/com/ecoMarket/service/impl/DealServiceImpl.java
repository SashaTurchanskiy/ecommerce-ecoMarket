package com.ecoMarket.service.impl;

import com.ecoMarket.model.Deal;
import com.ecoMarket.model.HomeCategory;
import com.ecoMarket.repository.DealRepository;
import com.ecoMarket.repository.HomeCategoryRepository;
import com.ecoMarket.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;


    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        Deal createDeal = Deal.builder()
                .category(category)
                .discount(deal.getDiscount())
                .build();
        return dealRepository.save(createDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long dealId) throws Exception {
        Deal updateDeal = dealRepository.findById(dealId).orElse(null);
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        if (updateDeal != null){
            if (deal.getCategory() != null){
                updateDeal.setDiscount(deal.getDiscount());
            }
            if (category != null){
                updateDeal.setCategory(category);
            }
            return dealRepository.save(updateDeal);
        }
        throw new Exception("Deal not found");
    }

    @Override
    public void deleteDeal(Long dealId) throws Exception {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(()-> new Exception("cannot find deal with " + dealId));
        dealRepository.delete(deal);
    }
}
