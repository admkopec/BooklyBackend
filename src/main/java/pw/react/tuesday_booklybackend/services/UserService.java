package pw.react.tuesday_booklybackend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.tuesday_booklybackend.models.User;

public interface UserService {
    User validateAndSave(User user);
    User updatePassword(User user, String password);
    void setPasswordEncoder(PasswordEncoder passwordEncoder);
}
