package com.ecoMarket.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {

    private String email;
    private String fullName;
    private String password;
    private String otp;
}
