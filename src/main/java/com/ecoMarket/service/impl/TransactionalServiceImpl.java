package com.ecoMarket.service.impl;

import com.ecoMarket.model.Order;
import com.ecoMarket.model.Seller;
import com.ecoMarket.model.Transaction;
import com.ecoMarket.repository.SellerRepository;
import com.ecoMarket.repository.TransactionalRepository;
import com.ecoMarket.service.TransactionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionalServiceImpl implements TransactionalService {

    private final TransactionalRepository transactionalRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());

        return transactionalRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionBySellerId(Seller seller) {
        return transactionalRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionalRepository.findAll();
    }
}
