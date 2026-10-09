package com.ecoMarket.service;

import com.ecoMarket.model.Product;
import com.ecoMarket.model.User;
import com.ecoMarket.model.Wishlist;

public interface WishlistService {

    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);


}
