package com.ecoMarket.service.impl;

import com.ecoMarket.model.Order;
import com.ecoMarket.model.Seller;
import com.ecoMarket.model.Transaction;
import com.ecoMarket.service.TransactionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionalServiceImpl implements TransactionalService {
    @Override
    public Transaction createTransaction(Order order) {
        return null;
    }

    @Override
    public List<Transaction> getTransactionBySellerId(Seller seller) {
        return List.of();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return List.of();
    }
}
