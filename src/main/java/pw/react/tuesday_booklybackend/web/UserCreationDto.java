package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public record UserCreationDto(@NotEmpty String name, @Email String email, @NotEmpty String password) {
    public static User convertToUser(UserCreationDto userDto) {
        User user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setIsAdmin(false);
        return user;
    }
}
