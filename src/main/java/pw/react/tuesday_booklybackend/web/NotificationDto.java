package pw.react.tuesday_booklybackend.web;

import java.util.Collection;
import java.util.UUID;

public record NotificationDto(String service, Collection<UUID> bookings) {

}
