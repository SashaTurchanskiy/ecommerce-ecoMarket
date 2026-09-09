package com.ecoMarket.service;

import com.ecoMarket.dtos.response.UserResponse;
import com.ecoMarket.model.User;

public interface UserService {

    UserResponse findUserByJwtToken(String jwt) throws Exception;

    UserResponse findUserByEmail(String email) throws Exception;
}
