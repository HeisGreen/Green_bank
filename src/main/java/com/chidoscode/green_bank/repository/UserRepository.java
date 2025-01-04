package com.chidoscode.green_bank.repository;

import com.chidoscode.green_bank.dto.EnquiryRequest;
import com.chidoscode.green_bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);
}
