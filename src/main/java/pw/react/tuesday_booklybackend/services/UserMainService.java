package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.tuesday_booklybackend.dao.UserRepository;
import pw.react.tuesday_booklybackend.exceptions.UserValidationException;
import pw.react.tuesday_booklybackend.mail.services.MailService;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.web.UserDto;

import java.util.Collection;
import java.util.Optional;

public class UserMainService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserMainService.class);

    private final UserRepository userRepository;
    @Autowired
    private MailService mailService;
    private PasswordEncoder passwordEncoder;

    public UserMainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User validateAndSave(User user) {
        if (isValidUser(user)) {
            log.info("User is valid");
            Optional<User> dbUser = userRepository.findByEmail(user.getUsername());
            if (dbUser.isPresent()) {
                log.info("User already exists. Updating it.");
                user.setId(dbUser.get().getId());
            }
            user = userRepository.save(user);
            log.info("User was saved.");
            try {
                mailService.sendWelcomeEmailTo(user);
                log.info("Welcome email was send.");
            } catch (Exception e) {
                log.error("There was an error while sending welcome email.");
            }
        }
        return user;
    }

    private boolean isValidUser(User user) {
        if (user != null) {
            if (!isValid(user.getUsername())) {
                log.error("Empty username.");
                throw new UserValidationException("Empty username.");
            }
            if (!isValid(user.getPassword())) {
                log.error("Empty user password.");
                throw new UserValidationException("Empty user password.");
            }
            if (!isValid(user.getEmail())) {
                log.error("UEmpty email.");
                throw new UserValidationException("Empty email.");
            }
            return true;
        }
        log.error("User is null.");
        throw new UserValidationException("User is null.");
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public User updatePassword(User user, String password) {
        if (isValidUser(user)) {
            if (passwordEncoder != null) {
                log.debug("Encoding password.");
                user.setPassword(passwordEncoder.encode(password));
            } else {
                log.debug("Password in plain text.");
                user.setPassword(password);
            }
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public Collection<UserDto> fetchAllUsers(User user) {
        if (user.getIsAdmin()) {
            throw new AccessDeniedException("User doesn't have necessary privileges");
        }
        return  userRepository.findAll().stream().map(UserDto::valueFrom).toList();
    }
}

