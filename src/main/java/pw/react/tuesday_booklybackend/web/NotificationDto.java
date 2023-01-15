package pw.react.tuesday_booklybackend.web;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.UUID;

public record NotificationDto(@NotEmpty String service, @NotEmpty Collection<UUID> bookings) {
}
