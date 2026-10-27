package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.ProductRequest;
import com.ecoMarket.mapper.ProductMapper;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Seller;
import com.ecoMarket.repository.CategoryRepository;
import com.ecoMarket.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productMapper = mock(ProductMapper.class);
        productService = new ProductServiceImpl(
                productRepository,
                productMapper,
                mock(CategoryRepository.class)
        );
    }

    @Test
    void updateProductRejectsAnotherSellersProduct() {
        Seller owner = Seller.builder().id(1L).build();
        Seller otherSeller = Seller.builder().id(2L).build();
        Product product = Product.builder().id(10L).seller(owner).mrpPrice(100).sellingPrice(80).build();
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        assertThrows(AccessDeniedException.class,
                () -> productService.updateProduct(10L, new ProductRequest(), otherSeller));

        verifyNoInteractions(productMapper);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProductRejectsAnotherSellersProduct() {
        Seller owner = Seller.builder().id(1L).build();
        Seller otherSeller = Seller.builder().id(2L).build();
        Product product = Product.builder().id(10L).seller(owner).build();
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        assertThrows(AccessDeniedException.class,
                () -> productService.deleteProduct(10L, otherSeller));

        verify(productRepository, never()).delete(any(Product.class));
    }
}
