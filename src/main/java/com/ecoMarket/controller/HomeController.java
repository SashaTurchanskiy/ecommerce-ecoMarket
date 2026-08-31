package com.ecoMarket.controller;

import com.ecoMarket.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping
    public ApiResponse HomeControllerHandle(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Hello world");
        return apiResponse;
    }
}
