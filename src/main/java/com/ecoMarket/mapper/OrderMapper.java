package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.OrderRequest;
import com.ecoMarket.dtos.response.OrderResponse;
import com.ecoMarket.model.Order;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    Order toEntity(OrderRequest request);

    List<OrderResponse> toOrderList(List<Order> orderList);
}
