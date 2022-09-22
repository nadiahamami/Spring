package com.vermeg.service.impl;

import com.vermeg.entities.PasswordResetToken;
import com.vermeg.entities.User;
import com.vermeg.exceptions.BadRequestException;
import com.vermeg.payload.requests.ResetPasswordRequest;
import com.vermeg.repositories.PasswordResetTokenRepository;
import com.vermeg.repositories.UserRepository;
import com.vermeg.service.ForgotPassword;
import com.vermeg.utils.Email;
import com.vermeg.utils.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ForgotPasswordServiceImpl implements ForgotPassword {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    MessageSource messageSource;

    @Autowired
    EmailSenderService emailSender;

    public void forgotPassword(String userEmail) throws MessagingException, BadRequestException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            String message = messageSource.getMessage("accountNotFound",
                    null, LocaleContextHolder.getLocale());
            throw new BadRequestException(message);
        }
        // delete old token
        if(passwordResetTokenRepository.existsByUser(user)){
            PasswordResetToken oldToken = passwordResetTokenRepository.getByUser(user);
            passwordResetTokenRepository.deleteById(oldToken.getId());
        }
        // Create a new token in the database
        String token = UUID.randomUUID().toString();
        PasswordResetToken createdToken = new PasswordResetToken();
        createdToken.setToken(token);
        createdToken.setUser(user);
        passwordResetTokenRepository.save(createdToken);
        // send mail
        Email email = new Email();
        email.setTo(userEmail);
        email.setFrom("svermeg@gmail.com");
        String message = messageSource.getMessage("resetPasswordMailHeader",
                null, LocaleContextHolder.getLocale());
        email.setSubject(message);
        email.setTemplate("forgot-password.html");
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", user.getFirstName() + " " + user.getLastName());
        properties.put("resetPasswordLink", "http://localhost:4200/auth/reset-password/"+ token);
        email.setProperties(properties);
        emailSender.sendHtmlMessage(email);
    }


    public void resetPassword(ResetPasswordRequest resetPasswordRequest) throws BadRequestException {
        PasswordResetToken token = passwordResetTokenRepository.getByToken(resetPasswordRequest.getToken());
        String message = messageSource.getMessage("invalidToken",
                null, LocaleContextHolder.getLocale());
        if(token == null){
            throw new BadRequestException(message);
        }else{
            if(token.isTokenExpired()){
                // Delete the expired token
                passwordResetTokenRepository.deleteById(token.getId());
                throw new BadRequestException(message);
            }else{
                // Update the password
                User user = token.getUser();
                user.setPassword(bcryptEncoder.encode(resetPasswordRequest.getPassword()));
                userRepository.save(user);
                // Delete the token
                passwordResetTokenRepository.deleteById(token.getId());
            }
        }

    }
}
