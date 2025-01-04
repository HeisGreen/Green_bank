package com.chidoscode.green_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @Schema(name = "User's first name")
    private String firstName;

    @Schema(name = "User's last name")
    private String lastName;

    @Schema(name = "User's other name")
    private String otherName;

    @Schema(name = "User's gender")
    private String gender;

    @Schema(name = "User's address")
    private String address;

    @Schema(name = "User's State of Origin")
    private String stateOfOrigin;

    @Schema(name = "User's email address")
    private String email;

    @Schema(name = "User's password")
    private String password;

    @Schema(name = "User's phone number")
    private String phoneNumber;

    @Schema(name = "User's Alternate phone number")
    private String alternatePhoneNumber;

}
