package com.ecoMarket.dtos.request;

import com.ecoMarket.model.Address;
import com.ecoMarket.model.BankDetails;
import com.ecoMarket.model.BusinessDetails;
import com.ecoMarket.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerRequest {

    @NotBlank
    private String sellerName;

    @NotBlank
    private String mobile;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private BusinessDetails businessDetails;
    private BankDetails bankDetails;
    private Address pickupAddress;

    private String SGTIN;
}
