package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.util.UUID;

public record UserDto(UUID id, @NotEmpty String name, @Email String email, boolean isAdmin, int membershipLevel) {
    public static UserDto valueFrom(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getIsAdmin(), user.getMembershipLevel());
    }
}
