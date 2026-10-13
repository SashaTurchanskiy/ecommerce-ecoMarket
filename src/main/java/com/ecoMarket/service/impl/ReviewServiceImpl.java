package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.CreateReviewRequest;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Review;
import com.ecoMarket.model.User;
import com.ecoMarket.repository.ReviewRepository;
import com.ecoMarket.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest request, User user, Product product) {
        Review review = Review.builder()
                .user(user)
                .product(product)
                .reviewText(request.getReviewText())
                .rating(request.getReviewRating())
                .productImage(request.getProductImages())
                .build();

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        Review review = getReviewById(reviewId);

        if (review.getUser().getId().equals(userId)){
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepository.save(review);
        }
        throw new Exception("You are not authorized to update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);

        if (review.getUser().getId().equals(userId)){
            throw new Exception("You are not authorized to delete this review");
        }
        reviewRepository.delete(review);


    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId)
                .orElseThrow(()-> new Exception("Review not found with id: " + reviewId));
    }
}
