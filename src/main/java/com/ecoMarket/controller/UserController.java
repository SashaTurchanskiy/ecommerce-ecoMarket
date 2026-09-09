package com.ecoMarket.controller;

import com.ecoMarket.dtos.response.UserResponse;
import com.ecoMarket.model.User;
import com.ecoMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/profile")
    public ResponseEntity<UserResponse> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        return ResponseEntity.ok(userService.findUserByJwtToken(jwt));
    }
}
