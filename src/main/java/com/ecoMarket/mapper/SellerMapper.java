package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.SellerRequest;
import com.ecoMarket.dtos.response.SellerResponse;
import com.ecoMarket.model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "ROLE_SELLER")
    @Mapping(target = "isEmailVerified", constant = "false")
    @Mapping(target = "accountStatus", constant = "PENDING_VERIFICATION")
    Seller toEntity(SellerRequest request);

    SellerResponse toResponse(Seller seller);

    List<SellerResponse> toResponseList(List<Seller> sellers);
}
