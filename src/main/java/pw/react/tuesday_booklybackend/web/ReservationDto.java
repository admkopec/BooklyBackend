package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;

import java.util.Date;
import java.util.UUID;

public record ReservationDto(UUID id, @FutureOrPresent Date fromDate, @Future Date toDate, UUID offerId) {

    public static ReservationDto valueFrom(Reservation reservation) {
        return new ReservationDto(reservation.getId(), null, null, null);
    }

    public static Reservation convertToReservation(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.id());
        return reservation;
    }
}