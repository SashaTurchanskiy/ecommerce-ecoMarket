package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.CategoryRequest;
import com.ecoMarket.dtos.response.CategoryResponse;
import com.ecoMarket.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);
}
