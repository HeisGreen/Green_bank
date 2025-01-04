package com.chidoscode.green_bank.repository;

import com.chidoscode.green_bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
