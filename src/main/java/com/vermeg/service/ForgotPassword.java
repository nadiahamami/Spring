package com.vermeg.service;

import com.vermeg.exceptions.BadRequestException;
import com.vermeg.payload.requests.ResetPasswordRequest;

import javax.mail.MessagingException;

public interface ForgotPassword {
    public void forgotPassword(String userEmail) throws MessagingException, BadRequestException;
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws BadRequestException;
}
