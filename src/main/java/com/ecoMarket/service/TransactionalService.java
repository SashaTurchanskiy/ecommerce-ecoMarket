package com.ecoMarket.service;

import com.ecoMarket.model.Order;
import com.ecoMarket.model.Seller;
import com.ecoMarket.model.Transaction;

import java.util.List;

public interface TransactionalService {

    Transaction createTransaction(Order order);

    List<Transaction> getTransactionBySellerId(Seller seller);

    List<Transaction> getAllTransactions();
}
