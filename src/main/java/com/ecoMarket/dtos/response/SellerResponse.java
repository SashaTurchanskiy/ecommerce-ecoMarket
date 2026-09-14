package com.ecoMarket.dtos.response;

import com.ecoMarket.model.Address;
import com.ecoMarket.model.BankDetails;
import com.ecoMarket.model.BusinessDetails;
import com.ecoMarket.model.enums.AccountStatus;
import com.ecoMarket.model.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerResponse {

    private Long id;
    private String sellerName;
    private String mobile;
    private String email;

    private BusinessDetails businessDetails;
    private BankDetails bankDetails;
    private Address pickupAddress;

    private String SGTIN;
    private Role role;
    private boolean isEmailVerified;
    private AccountStatus accountStatus;

}
