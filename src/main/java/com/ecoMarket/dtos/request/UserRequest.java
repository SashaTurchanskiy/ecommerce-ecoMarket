package com.ecoMarket.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private Long id;
    private String email;
    private String fullName;
    private String mobile;
    private String password; // тільки для створення/оновлення
}
