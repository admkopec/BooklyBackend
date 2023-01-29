package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import pw.react.tuesday_booklybackend.dao.UserRepository;
import pw.react.tuesday_booklybackend.exceptions.UserValidationException;
import pw.react.tuesday_booklybackend.mail.services.MailService;
import pw.react.tuesday_booklybackend.models.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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
            } else {
                try {
                    mailService.sendWelcomeEmailTo(user);
                    log.info("Welcome email was send.");
                } catch (Exception e) {
                    log.error("There was an error while sending welcome email.");
                }
                if (passwordEncoder != null) {
                    log.debug("Encoding password.");
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }
            user = userRepository.save(user);
            log.info("User was saved.");
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
    public User updateName(User user, String name) {
        if (isValidUser(user) && isValid(name)) {
            log.debug("Updating user name.");
            user.setName(name);
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public User updateEmail(User user, String email) {
        if (isValidUser(user) && isValid(email)) {
            Optional<User> dbUser = userRepository.findByEmail(user.getUsername());
            if (!dbUser.isPresent() || dbUser.get().getId().equals(user.getId())) {
                log.debug("Updating user email.");
                user.setEmail(email);
                user = userRepository.save(user);
            }
        }
        return user;
    }


    @Override
    public User updatePassword(User user, String password) {
        if (isValidUser(user) && isValid(password)) {
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
    public Collection<User> fetchAllUsers(User user) {
        if (user.getIsAdmin()) {
            throw new AccessDeniedException("User doesn't have necessary privileges");
        }
        return userRepository.findAll();
    }

    @Override
    public User fetchUser(UUID userId, User user) {
        if (user.getIsAdmin()) {
            throw new AccessDeniedException("User doesn't have necessary privileges");
        }
        Optional<User> requestedUser = userRepository.findById(userId);
        if (!requestedUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user was not found!");
        }
        return requestedUser.get();
    }

    @Override
    public void deleteUser(UUID userId, User user) {
        if (user.getIsAdmin()) {
            throw new AccessDeniedException("User doesn't have necessary privileges");
        }
        Optional<User> requestedUser = userRepository.findById(userId);
        if (requestedUser.isPresent()) {
            userRepository.delete(requestedUser.get());
        }
    }
}

