package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.ProductRequest;
import com.ecoMarket.dtos.response.ProductResponse;
import com.ecoMarket.mapper.ProductMapper;
import com.ecoMarket.model.Category;
import com.ecoMarket.model.Product;
import com.ecoMarket.model.Seller;
import com.ecoMarket.repository.CategoryRepository;
import com.ecoMarket.repository.ProductRepository;
import com.ecoMarket.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;




@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request, Seller seller) throws Exception {
//        Category category = categoryRepository.findByCategoryId(request.getCategoryId());
//        if (category == null){
//            throw new Exception("category not found");
//        }
        Product product = productMapper.toEntity(request, seller, null);

        int discount = calculateDiscountPercentage(product.getMrpPrice(), product.getSellingPrice());
        product.setDiscountPercent(discount);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse findProductById(Long productId) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new Exception("cannot find product with " + productId));
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> searchProduct(String query) {
        return productMapper.toResponseList(productRepository.searchProduct(query));
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Оновлюємо поля через мапер
        productMapper.updateProductFromRequest(request, product);

        // Перерахунок знижки
        int discount = calculateDiscountPercentage(product.getMrpPrice(), product.getSellingPrice());
        product.setDiscountPercent(discount);

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    public Page<ProductResponse> getAllProduct(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null){
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (colors != null && !colors.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            if (sizes != null && !sizes.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("sizes"), sizes));
            }
            if (minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null){
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        if(sort != null && !sort.isEmpty()){

            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        }
        else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        return productPage.map(productMapper::toResponse);
    }

    @Override
    public void deleteProduct(Long productId) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new Exception("cannot find product with " +  productId));
        productRepository.delete(product);

    }

    @Override
    public List<ProductResponse> getProductBySellerId(Long sellerId) {
        List<Product> product = productRepository.findSellerById(sellerId);
        return productMapper.toResponseList(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice){
        if (mrpPrice <= 0) throw new IllegalArgumentException("MRP must be > 0");
        double discount = mrpPrice - sellingPrice;
        return (int) (discount / mrpPrice * 100);
    }
}
