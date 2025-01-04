package com.chidoscode.green_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {

    @Schema(name = "Email Recipient")
    private String recipient;

    @Schema(name = "Body of message to be sent")
    private String messageBody;

    @Schema(name = "Subject of mail")
    private String subject;

    @Schema(name = "Attachment")
    private String attachment;
}
