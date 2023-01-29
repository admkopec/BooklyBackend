package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public record ReservationParklyDto(UUID id, @NotEmpty String name, @FutureOrPresent long startDate, @Future long endDate, @NotEmpty UUID offerId, Integer numberOfSpots) {
    public static ReservationParklyDto valueFrom(ReservationModificationDto reservationDto) {
        ReservationParklyDto reservation = new ReservationParklyDto(reservationDto.id(), reservationDto.name(), reservationDto.dateFrom(), reservationDto.dateTo(), reservationDto.offerId(), reservationDto.numberOfSpaces());
        return reservation;
    }
}

