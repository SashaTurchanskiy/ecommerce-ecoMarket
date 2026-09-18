package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.LoginRequest;
import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.dtos.response.ApiResponse;
import com.ecoMarket.dtos.response.AuthResponse;
import com.ecoMarket.model.User;
import com.ecoMarket.model.VerificationCode;
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

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerHandler(@RequestBody SignupRequest request) throws Exception {
        AuthResponse authResponse = authService.createUser(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.signIn(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signingHandler(@RequestBody LoginRequest request) {
        return loginHandler(request);
    }

    @PostMapping({"/send-otp", "/sent/login-signup-otp"})
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode request) throws Exception {
        authService.sendLoginOpt(request.getEmail());

        ApiResponse res = new ApiResponse();
        res.setMessage("otp sent successfully");

        return ResponseEntity.ok(res);
    }

}
