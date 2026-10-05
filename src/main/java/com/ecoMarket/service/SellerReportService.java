package com.ecoMarket.service;

import com.ecoMarket.model.Seller;
import com.ecoMarket.model.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller) throws Exception;

    SellerReport updateSellerReport(SellerReport sellerReport);
}
