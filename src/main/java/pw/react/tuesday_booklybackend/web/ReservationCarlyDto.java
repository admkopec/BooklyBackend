package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public record ReservationCarlyDto(UUID id, String userId, @NotEmpty String name, @FutureOrPresent long startDate, @Future long endDate, @NotEmpty UUID carId, @NotEmpty String lastname) {
    public static Reservation convertToReservation(ReservationCarlyDto reservationDto, CompanionService service) {
        ReservationModificationDto reservation = new ReservationModificationDto(reservationDto.id(), reservationDto.name(), reservationDto.startDate(), reservationDto.endDate(), reservationDto.carId(), null, null, null);
        return ReservationModificationDto.convertToReservation(reservation, service);
    }

    public static ReservationCarlyDto valueFrom(ReservationModificationDto reservationDto, User user) {
        String[] names = user.getName().split(" ");
        String firstName;
        String lastName;
        if (names.length > 1) {
            firstName = names[0];
            lastName = names[1];
        } else {
            firstName = names[0];
            lastName = "";
        }
        ReservationCarlyDto reservation = new ReservationCarlyDto(reservationDto.id(), "80253CBB-455D-4C6D-ADF9-BF6B8B4ABEF7", firstName, reservationDto.dateFrom(), reservationDto.dateTo(), reservationDto.offerId(), lastName);
        return reservation;
    }
}
