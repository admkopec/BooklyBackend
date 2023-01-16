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
import pw.react.tuesday_booklybackend.web.UserCreationDto;
import pw.react.tuesday_booklybackend.web.UserDto;

import javax.annotation.PostConstruct;
import java.util.Collection;

@RestController
@RequestMapping(path = "/logic/api/users")
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
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreationDto userDto) {
        User user = UserCreationDto.convertToUser(userDto);
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

    @Operation(summary = "Fetch all users info")
    @GetMapping(path = "/all")
    public ResponseEntity<Collection<UserDto>> fetchUsers(@RequestParam(defaultValue = "name") String sortBy,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<UserDto> allUsers = userService.fetchAllUsers(user);
        // Implementation of paging by filtering `allUsers`
        switch (sortBy) {
            case "name":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user1.name(), user2.name())).toList();
                break;
            case "-name":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user2.name(), user1.name())).toList();
                break;
            case "email":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user1.email(), user2.email())).toList();
                break;
            case "-email":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user2.email(), user1.email())).toList();
                break;
        }
        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allUsers.size());
        return ResponseEntity.status(HttpStatus.OK).body(allUsers.stream().toList().subList(startIndex, endIndex));
    }
}

