package com.ecoMarket.mapper;

import com.ecoMarket.dtos.request.UserRequest;
import com.ecoMarket.dtos.response.UserResponse;
import com.ecoMarket.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", constant = "ROLE_CUSTOMER")
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
}
