package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.util.UUID;

public record UserDto(UUID id, @NotEmpty String name, @Email String email, @NotEmpty String password) {

    public static UserDto valueFrom(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User convertToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setIsAdmin(false);
        return user;
    }
}
