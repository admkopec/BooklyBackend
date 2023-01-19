package pw.react.tuesday_booklybackend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.web.UserDto;

import java.util.Collection;
import java.util.UUID;

public interface UserService {
    User validateAndSave(User user);
    User updatePassword(User user, String password);
    User updateName(User user, String name);
    User updateEmail(User user, String email);
    void setPasswordEncoder(PasswordEncoder passwordEncoder);
    User fetchUser(UUID userId, User requester);
    void deleteUser(UUID userId, User requester);
    Collection<User> fetchAllUsers(User user);
}
