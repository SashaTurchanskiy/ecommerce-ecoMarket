package com.ecoMarket.repository;

import com.ecoMarket.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionalRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySellerId(Long sellerId);
}
