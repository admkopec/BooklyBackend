package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public record ReservationCarlyDto(UUID id, @NotEmpty String name, @FutureOrPresent long dateFrom, @Future long dateTo, @NotEmpty UUID carId) {
    public static Reservation convertToReservation(ReservationCarlyDto reservationDto, CompanionService service) {
        ReservationModificationDto reservation = new ReservationModificationDto(reservationDto.id(), reservationDto.name(), reservationDto.dateFrom(), reservationDto.dateTo(), reservationDto.carId(), null, null, null);
        return ReservationModificationDto.convertToReservation(reservation, service);
    }

    public static ReservationCarlyDto valueFrom(ReservationModificationDto reservationDto) {
        ReservationCarlyDto reservation = new ReservationCarlyDto(reservationDto.id(), reservationDto.name(), reservationDto.dateFrom(), reservationDto.dateTo(), reservationDto.offerId());
        return reservation;
    }
}
