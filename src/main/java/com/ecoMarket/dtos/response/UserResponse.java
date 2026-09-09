package com.ecoMarket.dtos.response;

import com.ecoMarket.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String password;
    private String email;
    private String fullName;
    private String mobile;
    private Role roles;
}
