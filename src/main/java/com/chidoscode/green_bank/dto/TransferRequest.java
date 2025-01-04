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
public class TransferRequest {

    @Schema(name = "Sender's Account number")
    private String senderAccountNumber;

    @Schema(name = "Reciever's Account number")
    private String receiverAccountNumber;

    @Schema(name = "Amount")
    private BigDecimal amount;
}
