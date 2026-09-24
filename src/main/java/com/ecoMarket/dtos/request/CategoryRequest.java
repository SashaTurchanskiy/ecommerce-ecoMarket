package com.ecoMarket.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank
    private String name;

    /** Optional public identifier; if null the service may generate one */
    private String categoryId;

    /** Parent category public id (categoryId). Nullable for root categories */
    private String parentCategoryId;

    /** Optional level; service will compute if absent */
    private Integer level;
}
