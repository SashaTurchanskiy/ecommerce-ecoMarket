package com.ecoMarket.service;

import com.ecoMarket.dtos.request.LoginRequest;
import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.dtos.response.AuthResponse;
import com.ecoMarket.model.enums.Role;

public interface AuthService {

    void sendLoginOpt(String email) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    //add method signIn
    AuthResponse signIn(LoginRequest request);

}
