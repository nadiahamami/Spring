package com.vermeg.controllers;

import com.vermeg.entities.User;
import com.vermeg.exceptions.EmailAlreadyUsedException;
import com.vermeg.payload.responses.ApiResponse;
import com.vermeg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ApiResponse<Object> getProfile(Authentication authentication){
        User userProfile = userService.getProfile(authentication);
        String messageResponse = messageSource.getMessage("common.getProfile",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse<>(200, messageResponse, userProfile);
    }

    @RequestMapping(value = "profile", method = RequestMethod.PUT)
    public ApiResponse<Object> updateProfile(Principal principal,
         @RequestBody User updatedUser) throws EmailAlreadyUsedException, MessagingException {
        userService.updateProfile(principal, updatedUser);
        String messageResponse = messageSource.getMessage("common.updateProfile",
                null, LocaleContextHolder.getLocale());
        return new ApiResponse<>(200, messageResponse,null);
    }
}
