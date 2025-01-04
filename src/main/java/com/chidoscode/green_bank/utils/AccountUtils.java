package com.chidoscode.green_bank.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "010";
    public static final String RECIEVER_ACCOUNT_NOT_EXISTS_CODE = "020";
    public static final String TRANSFER_SUCCESSFUL_CODE = "030";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_CREDITED_SUCCESSFULLY_CODE = "007";
    public static final String ACCOUNT_DEBITED_SUCCESSFULLY_CODE = "008";
    public static final String INSUFFICIENT_FUNDS_CODE = "009";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account was found";
    public static final String RECIEVER_ACCOUNT_NOT_EXISTS_MESSAGE = "Reciever's account not exist.";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "This transfer was successful";
    public static final String ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE = "Account was debited successfully!";
    public static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds to complete this transaction.";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "This account does not exist.";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account!";
    public static final String ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE = "This account has been credited successfully!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account created successfully!";

    public static String generateAccountNumber() {
        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;

        int randNum = (int) Math.floor (Math.random() * ((max - min) + 1) + min);

        //convert year and randNum to strings and concatenate
        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNum);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();
    }
}
