package com.chidoscode.green_bank.service.impl;

import com.chidoscode.green_bank.dto.*;
import com.chidoscode.green_bank.entity.Transaction;
import com.chidoscode.green_bank.entity.User;
import com.chidoscode.green_bank.repository.UserRepository;
import com.chidoscode.green_bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * create an account - saving a new user to  db
         * Check if user already exist
         */

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .emailStatus("Email did not send")
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations, Your account was created successfully.\nHere are your account details: \nAccount number: " + savedUser.getAccountNumber() +
                        "\nAccount Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .emailStatus("Email Sent Successfully")
                .build();
    }

    @Override
    public BankResponse balaceEnquiry(EnquiryRequest request) {
        //check if provided account number Exists in db
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //find user and provide details
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        //check if provided account number Exists in db
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        //checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        //save transaction to db
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("CREDIT")
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        //check if user exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //check if withdraw amount is more than existing balance
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            //save transaction to db
            TransactionDto transactionDto = TransactionDto.builder()
                    .transactionType("DEBIT")
                    .accountNumber(userToDebit.getAccountNumber())
                    .amount(request.getAmount())
                    .status("SUCCESS")
                    .build();

            transactionService.saveTransaction(transactionDto);
        }
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        //check if reciever's account exist
        boolean isRecieverAccountExist = userRepository.existsByAccountNumber(request.getReceiverAccountNumber());
        if (!isRecieverAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.RECIEVER_ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.RECIEVER_ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //check if the sender's account balance is more than the amount being sent
        User userToDebit = userRepository.findByAccountNumber(request.getSenderAccountNumber());
        User userToCredit = userRepository.findByAccountNumber(request.getReceiverAccountNumber());

        String senderName = userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName();
        String receiverName = userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName();

        if (userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .recipient(userToDebit.getEmail())
                    .subject("DEBIT ALERT")
                    .messageBody("Dear " + senderName + "\nAmount : " + request.getAmount() + "\nAccount Balance : "
                            + userToDebit.getAccountBalance() + "\nSent to : " + receiverName  )
                    .build();
            emailService.sendEmailAlert(debitAlert);
        }
        //credit reciever's Account
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        //Credit Alert sent as Email
        EmailDetails creditAlert = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("CREDIT ALERT")
                .messageBody("Dear " + receiverName + "\nAmount : " + request.getAmount() + "\nAccount Balance : "
                        + userToCredit.getAccountBalance() + "\nRecieved from : " + senderName)
                .build();
        emailService.sendEmailAlert(creditAlert);

        //save transaction to db
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("TRANSFER")
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }
}