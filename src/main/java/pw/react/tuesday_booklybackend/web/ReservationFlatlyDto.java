package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public record ReservationFlatlyDto(UUID uuid, @NotEmpty String name, @FutureOrPresent long dateFrom, @Future long dateTo, @NotEmpty UUID offer_uuid, @NotEmpty String first_name, @NotEmpty String last_name, Integer admin_id, Integer numberOfKids, Integer numberOfAdults) {
    public static Reservation convertToReservation(ReservationFlatlyDto reservationDto, CompanionService service) {
        ReservationModificationDto reservation = new ReservationModificationDto(reservationDto.uuid(), reservationDto.name(), reservationDto.dateFrom(), reservationDto.dateTo(), reservationDto.offer_uuid(), null, reservationDto.numberOfAdults(), reservationDto.numberOfKids());
        return ReservationModificationDto.convertToReservation(reservation, service);
    }

    public static ReservationFlatlyDto valueFrom(ReservationModificationDto reservationDto, User user) {
        String[] names = user.getName().split(" ");
        String fistName;
        String lastName;
        if (names.length > 1) {
            fistName = names[0];
            lastName = names[1];
        } else {
            fistName = names[0];
            lastName = "";
        }
        ReservationFlatlyDto reservation = new ReservationFlatlyDto(reservationDto.id(), reservationDto.name(), reservationDto.dateFrom(), reservationDto.dateTo(), reservationDto.offerId(), fistName, lastName, 1, reservationDto.numberOfKids(), reservationDto.numberOfAdults());
        return reservation;
    }
}
