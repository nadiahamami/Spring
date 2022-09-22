package com.vermeg.service.impl;

import com.vermeg.entities.ERole;
import com.vermeg.entities.Role;
import com.vermeg.exceptions.EmailAlreadyUsedException;
import com.vermeg.exceptions.ResourceNotFoundException;
import com.vermeg.repositories.RoleRepository;
import com.vermeg.repositories.UserRepository;
import com.vermeg.entities.User;
import com.vermeg.service.UserService;
import com.vermeg.utils.Email;
import com.vermeg.utils.EmailSenderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    EmailSenderService emailSender;

    @Autowired
    RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            String message = messageSource.getMessage("common.usernameNotFound",
                    null, LocaleContextHolder.getLocale());
            throw new UsernameNotFoundException(message);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getAuthority(user.getRole()));
    }

    private List<SimpleGrantedAuthority> getAuthority(Role role) {
        return Arrays.asList(new SimpleGrantedAuthority(role.getName()));
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    public void delete(long id) {
        Optional<User> userData = this.userRepository.findById(id);
        if (userData.isPresent()) {
            this.userRepository.deleteById(id);
        } else {
            String modelName = messageSource.getMessage("models.user", null, LocaleContextHolder.getLocale());
            String message = messageSource.getMessage("common.notFound",
                    new Object[]{modelName}, LocaleContextHolder.getLocale());
            throw new ResourceNotFoundException(message);
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        String modelName = messageSource.getMessage("models.user", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("common.notFound",
                new Object[]{modelName}, LocaleContextHolder.getLocale());
        return optionalUser.orElseThrow(() -> new ResourceNotFoundException(message));
    }

    public User update(long id, User updatedUser) throws EmailAlreadyUsedException {
        User user = findById(id);
        if (user != null) {
            updatedUser.setId(user.getId());
            return userRepository.save(updatedUser);
        }
        return  null ;
    }

    public User save(User user) throws EmailAlreadyUsedException, MessagingException {
        // test if email already used
        if (this.userRepository.existsByEmail(user.getEmail())) {
            String message = messageSource.getMessage("common.emailAlreadyUsed",
                    null, LocaleContextHolder.getLocale());
            throw new EmailAlreadyUsedException(message);
        }

        String password = user.getPassword();
        // Create new user account
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setAvatar(user.getAvatar());
        newUser.setRole(user.getRole());
        newUser.setPost(user.getPost());
        newUser.setProjects(user.getProjects());
        newUser.setResponsable(user.getResponsable());

        newUser.setResponsable(user.getResponsable());


        User createdUser = userRepository.save(user);
         //Send welcome email
		Email email = new Email();
		email.setTo(user.getEmail());
		email.setFrom("vermegentreprise@gmail.com");
		String message = messageSource.getMessage("welcomeMailHeader",
				null, LocaleContextHolder.getLocale());
		email.setSubject(message);
		email.setTemplate("welcome-email.html");
		Map<String, Object> properties = new HashMap<>();
		properties.put("name", user.getFirstName() + " " + user.getLastName());
		properties.put("email", user.getEmail());
		properties.put("password", password);
		properties.put("platformLink", "http://localhost:4200/auth/login/");
		email.setProperties(properties);
		emailSender.sendHtmlMessage(email);
        //System.out.println(password);
        return createdUser;
    }

    // Profile section
    // You can use Authentication or Principal
    // Link: https://www.baeldung.com/get-user-in-spring-security
    public User getProfile(Authentication authentication) {
        if (!userRepository.existsByEmail(authentication.getName())) {
            String modelName = messageSource.getMessage("models.user", null, LocaleContextHolder.getLocale());
            String message = messageSource.getMessage("common.notFound",
                    new Object[]{modelName}, LocaleContextHolder.getLocale());
            throw new ResourceNotFoundException(message);
        }
        System.out.println(authentication.getAuthorities());
        return userRepository.findByEmail(authentication.getName());
    }

    public void updateProfile(Principal principal, User updatedUser) throws EmailAlreadyUsedException, MessagingException {
        if (!userRepository.existsByEmail(principal.getName())) {
            String modelName = messageSource.getMessage("models.user", null, LocaleContextHolder.getLocale());
            String message = messageSource.getMessage("common.notFound",
                    new Object[]{modelName}, LocaleContextHolder.getLocale());
            throw new ResourceNotFoundException(message);
        }
        User user = userRepository.findByEmail(principal.getName());
        if (userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), user.getId())) {
            String message = messageSource.getMessage("common.emailAlreadyUsed",
                    null, LocaleContextHolder.getLocale());
            throw new EmailAlreadyUsedException(message);
        }
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setAvatar(updatedUser.getAvatar());
        String password = updatedUser.getPassword();
        if (updatedUser.getPassword() != null) {
            user.setPassword(bcryptEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(user);
        Email email = new Email();
        email.setTo(user.getEmail());
        email.setFrom("vermegentreprise@gmail.com");
        String message = messageSource.getMessage("welcomeMailHeader",
                null, LocaleContextHolder.getLocale());
        email.setSubject("Update Profile");
        email.setTemplate("welcome-email.html");
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", user.getFirstName() + " " + user.getLastName());
        properties.put("email", user.getEmail());
        properties.put("password", password);
        properties.put("platformLink", "http://localhost:4200/auth/login/");
        email.setProperties(properties);
        emailSender.sendHtmlMessage(email);
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll().stream()
                .filter( role -> !role.getName().equals("Role_super_Admin"))
                .collect(Collectors.toList());
    }
}
