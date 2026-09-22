package com.ecoMarket.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private int discountPercent;
    private int quantity;
    private String color;

    @Builder.Default
    private List<String> images = new ArrayList<>();

    private int numRatings;
    private Long categoryId;
    private Long sellerId;
    private LocalDateTime createdAt;
    private String sizes;
}
