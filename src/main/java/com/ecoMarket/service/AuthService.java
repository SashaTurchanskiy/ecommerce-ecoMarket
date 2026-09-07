package com.ecoMarket.service;

import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.model.enums.Role;

public interface AuthService {

    void sendLoginOpt(String email) throws Exception;
    String createUser(SignupRequest request) throws Exception;
}
