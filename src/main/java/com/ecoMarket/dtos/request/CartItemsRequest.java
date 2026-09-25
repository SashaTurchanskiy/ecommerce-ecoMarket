package com.ecoMarket.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemsRequest {

    @NotNull
    private Long productId;

    private String size;

    @NotNull
    @Min(1)
    @Builder.Default
    private Integer quantity = 1;
}
