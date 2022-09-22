package com.vermeg.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    @NotBlank
    String token;
    @NotBlank
    String password;
}
