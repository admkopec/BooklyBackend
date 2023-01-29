package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public record ReservationParklyResponseDto(UUID id, @NotEmpty String name, @FutureOrPresent long startDate, @Future long endDate, @NotEmpty UUID parkingId) {
    public static Reservation convertToReservation(ReservationParklyResponseDto reservationDto, CompanionService service) {
        ReservationModificationDto reservation = new ReservationModificationDto(reservationDto.id(), reservationDto.name(), reservationDto.startDate(), reservationDto.endDate(), reservationDto.parkingId(), null, null, null);
        return ReservationModificationDto.convertToReservation(reservation, service);
    }
}
