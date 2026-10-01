package com.ecoMarket.service;

import com.ecoMarket.model.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(String sellerId);

    SellerReport updateSellerReport(SellerReport sellerReport);
}
