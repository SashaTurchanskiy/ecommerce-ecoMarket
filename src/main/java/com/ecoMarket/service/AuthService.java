package com.ecoMarket.service;

import com.ecoMarket.dtos.request.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest request);
}
