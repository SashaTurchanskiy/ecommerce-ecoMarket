package com.ecoMarket.dtos.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequest {

    @Valid
    @Builder.Default
    private Set<CartItemsRequest> cartItems = new HashSet<>();

    private String couponCode;
}
