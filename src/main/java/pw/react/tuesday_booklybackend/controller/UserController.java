package pw.react.tuesday_booklybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.services.UserService;
import pw.react.tuesday_booklybackend.web.UserDto;

import javax.annotation.PostConstruct;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        userService.setPasswordEncoder(passwordEncoder);
    }

    @Operation(summary = "Create new user")
    @PostMapping(path = "")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = UserDto.convertToUser(userDto);
        user = userService.validateAndSave(user);
        log.info("Password is going to be encoded.");
        userService.updatePassword(user, user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.valueFrom(user));
    }

    @Operation(summary = "Fetch user info")
    @GetMapping(path = "")
    public ResponseEntity<UserDto> fetchUser() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(UserDto.valueFrom(user));
    }

    @Operation(summary = "Fetch user info")
    @GetMapping(path = "/all")
    public ResponseEntity<Collection<UserDto>> fetchUsers() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<UserDto> allUsers = userService.fetchAllUsers(user);
        // TODO: Add paging by filtering `allUsers`
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }
}

