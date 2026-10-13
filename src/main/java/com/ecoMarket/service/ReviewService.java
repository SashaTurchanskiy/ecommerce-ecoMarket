package com.ecoMarket.service;

import com.ecoMarket.dtos.request.CreateReviewRequest;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Review;
import com.ecoMarket.model.User;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest request, User user, Product product);

    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;

}
