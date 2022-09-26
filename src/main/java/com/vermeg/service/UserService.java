package com.vermeg.service;

import com.vermeg.entities.Role;
import com.vermeg.entities.User;
import com.vermeg.exceptions.EmailAlreadyUsedException;
import org.springframework.security.core.Authentication;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.List;

public interface UserService {
    User findUserByEmail(String username);
    List<User> findAll();
    User save(User user) throws EmailAlreadyUsedException, MessagingException;
    User findById(long id);
    User update(long id, User updatedUser) throws EmailAlreadyUsedException;
    void delete(long id);
    // Profile section
    User getProfile(Authentication authentication);
        void updateProfile(Principal principal, User updatedUser) throws EmailAlreadyUsedException, MessagingException;
    List<Role> getAllRole();
}
