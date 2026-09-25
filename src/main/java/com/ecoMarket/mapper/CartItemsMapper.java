package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.response.CartItemsResponse;
import com.ecoMarket.model.CartItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "mrpPrice", ignore = true)
    @Mapping(target = "sellingPrice", ignore = true)
    @Mapping(target = "userId", ignore = true)
    CartItems toEntity(CartItemsRequest request);

    @Mapping(target = "cartId", source = "cart.id")
    @Mapping(target = "productId", source = "product.id")
    CartItemsResponse toResponse(CartItems cartItems);
}
