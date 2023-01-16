package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.models.Reservation;

import java.util.UUID;

public record ReservationAdminDto(UUID id, String name, String username, String type) {
    public static ReservationAdminDto valueFrom(Reservation reservation) {
        return new ReservationAdminDto(reservation.getId(), reservation.getName(),  reservation.getUser().getName(), reservation.getService().toString());
    }
}
