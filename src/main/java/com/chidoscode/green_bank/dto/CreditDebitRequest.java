package com.chidoscode.green_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDebitRequest {

    @Schema(name = "User Account Number")
    private String accountNumber;

    @Schema(name = "Amount")
    private BigDecimal amount;
}
