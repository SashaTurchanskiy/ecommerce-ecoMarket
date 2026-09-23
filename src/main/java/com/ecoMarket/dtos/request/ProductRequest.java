package com.ecoMarket.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    private Integer mrpPrice;

    @NotNull
    @Min(0)
    private Integer sellingPrice;

    @NotNull
    @Min(0)
    private Integer discountPercent;

    @NotNull
    @Min(0)
    private Integer quantity;

    private String color;

    @Builder.Default
    private List<String> images = new ArrayList<>();


    private String categoryId;

    @NotNull
    private Long sellerId;

    private String sizes;
}
