package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.ProductRequest;
import com.ecoMarket.dtos.response.ProductResponse;
import com.ecoMarket.model.Category;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "discountPercent", ignore = true)
    Product toEntity(ProductRequest request, Seller seller, Category category);

    default ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .mrpPrice(product.getMrpPrice())
                .sellingPrice(product.getSellingPrice())
                .discountPercent(product.getDiscountPercent())
                .quantity(product.getQuantity())
                .color(product.getColor())
                .images(product.getImages() == null ? new ArrayList<>() : product.getImages())
                .numRatings(product.getNumRatings())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .sellerId(product.getSeller() != null ? product.getSeller().getId() : null)
                .createdAt(product.getCreatedAt())
                .sizes(product.getSizes())
                .build();
    }

    default List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null) {
            return null;
        }

        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "discountPercent", ignore = true)
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}
