package com.ecoMarket.controller;

import com.ecoMarket.model.Seller;
import com.ecoMarket.model.enums.AccountStatus;
import com.ecoMarket.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
public class AdminController {

    private final SellerService sellerService;

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long id,
            @PathVariable AccountStatus status){

        Seller sellerUpdate = sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(sellerUpdate);
    }

}
