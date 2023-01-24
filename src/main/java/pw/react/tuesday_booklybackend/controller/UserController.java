package pw.react.tuesday_booklybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.services.UserService;
import pw.react.tuesday_booklybackend.web.UserCreationDto;
import pw.react.tuesday_booklybackend.web.UserDto;
import pw.react.tuesday_booklybackend.web.UserUpdateDto;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.UUID;

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

    @Operation(summary = "Update info about user")
    @PutMapping(path = "/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDto userDto) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(userId)) {
            log.error("Ids don't match {} != {}", user.getId(), userId);
            throw new AccessDeniedException("There's been an error");
        }
        log.info("Values are going to be updated.");
        userService.updateName(user, userDto.name());
        userService.updateEmail(user, userDto.email());
        if (userDto.password().isPresent()) {
            log.info("Password is going to be encoded.");
            userService.updatePassword(user, userDto.password().get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserDto.valueFrom(user));
    }

    @Operation(summary = "Fetch current user info")
    @GetMapping(path = "")
    public ResponseEntity<UserDto> fetchCurrentUser() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(UserDto.valueFrom(user));
    }

    @Operation(summary = "Fetch user info")
    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserDto> fetchUser(@PathVariable UUID userId) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User requestedUser = userService.fetchUser(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body(UserDto.valueFrom(requestedUser));
    }

    @Operation(summary = "Remove user")
    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteUser(userId, user);
    }

    @Operation(summary = "Fetch all users info")
    @GetMapping(path = "/all")
    public ResponseEntity<Collection<UserDto>> fetchUsers(@RequestParam(defaultValue = "name") String sortBy,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<User> allUsers = userService.fetchAllUsers(user);
        // Implementation of paging by filtering `allUsers`
        switch (sortBy) {
            case "name":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user1.getName(), user2.getName())).toList();
                break;
            case "-name":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user2.getName(), user1.getName())).toList();
                break;
            case "email":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user1.getEmail(), user2.getEmail())).toList();
                break;
            case "-email":
                allUsers = allUsers.stream().sorted((user1, user2) -> String.CASE_INSENSITIVE_ORDER.compare(user2.getEmail(), user1.getEmail())).toList();
                break;
        }
        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allUsers.size());
        if (startIndex > endIndex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        return ResponseEntity.status(HttpStatus.OK).body(allUsers.stream().toList().subList(startIndex, endIndex).stream().map(UserDto::valueFrom).toList());
    }
}

