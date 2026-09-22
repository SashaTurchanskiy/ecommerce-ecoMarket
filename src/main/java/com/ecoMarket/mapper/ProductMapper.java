package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.ProductRequest;
import com.ecoMarket.dtos.response.ProductResponse;
import com.ecoMarket.model.Category;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "discountPercent", ignore = true) // розраховується окремо
    Product toEntity(ProductRequest request, Seller seller, Category category);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discountPercent", ignore = true)
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}
