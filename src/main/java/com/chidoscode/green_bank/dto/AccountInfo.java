package com.chidoscode.green_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {

    @Schema(name = "User Account name")
    private String accountName;

    @Schema(name = "User Account balance")
    private BigDecimal accountBalance;

    @Schema(name = "User Account number")
    private String accountNumber;
}
