package com.chidoscode.green_bank.controller;

import com.chidoscode.green_bank.dto.*;
import com.chidoscode.green_bank.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary = "Create a new User account",
            description = "Creating a new user and assigning an account ID"
    )
@ApiResponse(
        responseCode = "20",
        description = "HTTP header 200 Account Created"
)
    @PostMapping("/createUser")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "input a valid account number to retrieve the balance of the corresponding account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP header 200 Successful"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
        return userService.balaceEnquiry(request);
    }

    @Operation(
            summary = "Name Enquiry",
            description = "input a valid account number to retrieve the Name of the account user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP header 200 Successful"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);
    }

    @Operation(
            summary = "Credits an amount to a specified account.",
            description = "This endpoint allows users to add funds to a specific bank account." +
                    " The user provides the account number and the amount to be credited. " +
                    "Upon successful execution, the balance of the account is updated. " +
                    "This operation is typically used for deposits or receiving payments."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP header 200 Successful Credit"
    )
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }

    @Operation(
            summary = "Debits an amount from a specified account.",
            description = "This endpoint facilitates the withdrawal of funds from a specific bank account." +
                    " The user must specify the account number and the amount to be debited. " +
                    "The transaction will only succeed if the account has sufficient funds. It is commonly used for withdrawals or payments."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP header 200 Debit Successful"
    )
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }

    @Operation(
            summary = "Transfers funds between two accounts.",
            description = "This endpoint enables the transfer of funds from one account to another. " +
                    "The user must provide the sender's account number, the recipient's account number, and the transfer amount. " +
                    " The system ensures that the sender has sufficient funds before completing the transaction. " +
                    "It is typically used for peer-to-peer transactions or business payments."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP header 200 Transfer Successful"
    )
    @PostMapping("/transfer")
    public BankResponse tranfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }


}
