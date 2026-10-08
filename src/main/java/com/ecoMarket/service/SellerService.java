package com.ecoMarket.service;

import com.ecoMarket.dtos.request.SellerRequest;
import com.ecoMarket.dtos.response.SellerResponse;
import com.ecoMarket.model.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws Exception;
    SellerResponse createSeller(SellerRequest request) throws Exception;
    Seller getSellerById(Long id) throws Exception;
    Seller getSellerByEmail(String email) throws Exception;
    List<SellerResponse> getAllSellers();
    SellerResponse updateSellerProfile(Long id, SellerRequest request);
    void deleteSeller(Long id) throws Exception;
    SellerResponse verifyEmail(String email, String otp) throws Exception;
}
