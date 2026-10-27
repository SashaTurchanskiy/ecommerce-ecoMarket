package com.ecoMarket.service;

import com.ecoMarket.model.Deal;

import java.util.List;

public interface DealService {

    List<Deal> getDeals();

    Deal createDeal(Deal deal);

    Deal updateDeal(Deal deal, Long dealId) throws Exception;

    void deleteDeal(Long dealId) throws Exception;
}
