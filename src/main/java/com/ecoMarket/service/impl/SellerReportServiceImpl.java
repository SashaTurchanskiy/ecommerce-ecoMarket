package com.ecoMarket.service.impl;

import com.ecoMarket.model.SellerReport;
import com.ecoMarket.repository.SellerReportRepository;
import com.ecoMarket.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(String sellerId) {
        return null;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return null;
    }
}
