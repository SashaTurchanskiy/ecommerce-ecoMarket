package com.ecoMarket.controller;

import com.ecoMarket.model.Seller;
import com.ecoMarket.model.Transaction;
import com.ecoMarket.service.SellerService;
import com.ecoMarket.service.TransactionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionalController {

    private final TransactionalService transactionalService;
    private final SellerService sellerService;

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(
            @RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);

        List<Transaction> transactions = transactionalService.getTransactionBySellerId(seller);
        return ResponseEntity.ok(transactions);
    }
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionalService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
