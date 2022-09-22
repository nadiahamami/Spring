package com.vermeg.controllers;

import com.vermeg.payload.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class WelcomeController {

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public ApiResponse<Object> welcome(){
        String messageResponse = messageSource.getMessage("common.welcome", null, LocaleContextHolder.getLocale());
        return new ApiResponse<>(200, messageResponse ,null);

    }
}
