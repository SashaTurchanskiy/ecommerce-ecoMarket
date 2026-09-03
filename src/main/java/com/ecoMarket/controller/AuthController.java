package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.dtos.response.AuthResponse;
import com.ecoMarket.model.User;
import com.ecoMarket.model.enums.Role;
import com.ecoMarket.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request){
        String jwt = authService.createUser(request);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setRole(Role.ROLE_CUSTOMER);

        return ResponseEntity.ok(authResponse);
    }
}
