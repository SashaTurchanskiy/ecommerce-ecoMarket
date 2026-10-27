package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.CartItemsRequest;
import com.ecoMarket.dtos.response.ApiResponse;
import com.ecoMarket.dtos.response.CartItemsResponse;
import com.ecoMarket.dtos.response.CartResponse;
import com.ecoMarket.dtos.response.UserResponse;
import com.ecoMarket.model.Cart;
import com.ecoMarket.model.User;
import com.ecoMarket.service.CartItemService;
import com.ecoMarket.service.CartService;
import com.ecoMarket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final CartItemService cartItemService;


    @GetMapping("/find")
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemsResponse> addCartItem(
            @Valid @RequestBody CartItemsRequest cartItemsRequest,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        return ResponseEntity.ok(
                cartService.addCartItem(user.getId(), cartItemsRequest)
        );
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long cartItemId) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse api = new ApiResponse();
        api.setMessage("Item deleted successfully");

        return new ResponseEntity<>(api, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItemsResponse> updateCartItem(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CartItemsRequest request,
            @PathVariable Long cartItemId) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        return ResponseEntity.ok(cartItemService.updateCartItem(user.getId(),cartItemId, request));


    }

}
