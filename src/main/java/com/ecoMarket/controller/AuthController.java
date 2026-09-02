package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.model.User;
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

    @PostMapping("/login")
    public ResponseEntity<User> signup(@RequestBody SignupRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        return ResponseEntity.ok(user);
    }
}
