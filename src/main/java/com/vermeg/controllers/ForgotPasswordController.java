package com.vermeg.controllers;

import com.vermeg.exceptions.BadRequestException;
import com.vermeg.payload.requests.ForgotPasswordRequest;
import com.vermeg.payload.requests.ResetPasswordRequest;
import com.vermeg.payload.responses.ApiResponse;
import com.vermeg.payload.responses.TokenResponse;
import com.vermeg.service.impl.ForgotPasswordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ForgotPasswordController {

    @Autowired
    ForgotPasswordServiceImpl forgotPasswordService;

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "forgot-password", method = RequestMethod.POST)
    public ApiResponse<TokenResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException, BadRequestException {
        forgotPasswordService.forgotPassword(forgotPasswordRequest.getEmail());
        String messageResponse = messageSource.getMessage("common.forgotPassword",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse<>(200, messageResponse,null);
    }

    @RequestMapping(value = "reset-password", method = RequestMethod.POST)
    public ApiResponse<TokenResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) throws BadRequestException {
        forgotPasswordService.resetPassword(resetPasswordRequest);
        String messageResponse = messageSource.getMessage("common.resetPassword",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse<>(200, messageResponse,null);
    }
}
