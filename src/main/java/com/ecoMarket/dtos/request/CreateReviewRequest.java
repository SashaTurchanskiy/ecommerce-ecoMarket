package com.ecoMarket.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReviewRequest {

    private String reviewText;
    private double reviewRating;
    private List<String> productImages;
}
