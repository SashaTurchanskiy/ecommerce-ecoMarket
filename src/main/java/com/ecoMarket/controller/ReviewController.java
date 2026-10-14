package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.CreateReviewRequest;
import com.ecoMarket.dtos.response.ApiResponse;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Review;
import com.ecoMarket.model.User;
import com.ecoMarket.service.ProductService;
import com.ecoMarket.service.ReviewService;
import com.ecoMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<List<Review>> getReviewById(@PathVariable Long productId){
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/review/{productId}")
    public ResponseEntity<Review> writeReview(
            @RequestBody CreateReviewRequest reviewRequest,
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(reviewRequest, user, product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/update/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest request,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Review updateReview = reviewService.updateReview(
                reviewId,
                request.getReviewText(),
                request.getReviewRating(),
                user.getId()
        );
        return ResponseEntity.ok(updateReview);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        reviewService.deleteReview(reviewId, user.getId());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Review deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }


}
