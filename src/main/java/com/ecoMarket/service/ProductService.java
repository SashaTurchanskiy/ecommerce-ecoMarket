package com.ecoMarket.service;

import com.ecoMarket.dtos.request.ProductRequest;
import com.ecoMarket.dtos.response.ProductResponse;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Seller;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request, Seller seller) throws Exception;
    ProductResponse findProductById(Long productId) throws Exception;
    List<ProductResponse> searchProduct(String query);
    ProductResponse updateProduct(Long productId, ProductRequest request);
    Page<ProductResponse> getAllProduct(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );
    void deleteProduct(Long productId) throws Exception;
    List<ProductResponse> getProductBySellerId(Long sellerId);
}
