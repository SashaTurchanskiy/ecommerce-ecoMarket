package com.ecoMarket.mapper;

import com.ecoMarket.dtos.response.OrderItemResponse;
import com.ecoMarket.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse toResponse(OrderItem orderItem);
}
