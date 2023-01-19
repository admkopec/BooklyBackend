package pw.react.tuesday_booklybackend.web;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public record UserUpdateDto(@NotEmpty String name, @Email String email, Optional<String> password) {
}
