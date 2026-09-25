package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.CartRequest;
import com.ecoMarket.dtos.response.CartResponse;
import com.ecoMarket.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemsMapper.class)
public interface CartMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "totalSellingPrice", ignore = true)
    @Mapping(target = "totalItem", ignore = true)
    @Mapping(target = "totalMrpPrice", ignore = true)
    @Mapping(target = "discount", ignore = true)
    Cart toEntity(CartRequest request);

    @Mapping(target = "userId", source = "user.id")
    CartResponse toResponse(Cart cart);
}
