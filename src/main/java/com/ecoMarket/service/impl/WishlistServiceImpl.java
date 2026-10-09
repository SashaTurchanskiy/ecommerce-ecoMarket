package com.ecoMarket.service.impl;

import com.ecoMarket.model.Product;
import com.ecoMarket.model.User;
import com.ecoMarket.model.Wishlist;
import com.ecoMarket.repository.WishlistRepository;
import com.ecoMarket.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .build();
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId());

        if (wishlist == null){
            wishlist = createWishlist(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {
        Wishlist wishlist = getWishlistByUserId(user);

        if (wishlist.getProducts().contains(product)){
            wishlist.getProducts().remove(product);
        }
        else {
            wishlist.getProducts().add(product);
        }
        return wishlistRepository.save(wishlist);
    }
}
