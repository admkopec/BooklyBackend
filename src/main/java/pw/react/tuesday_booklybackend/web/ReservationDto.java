package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.UUID;

public record ReservationDto(UUID id, @NotEmpty String name, @FutureOrPresent long dateFrom, @Future long dateTo, @NotEmpty UUID offerId, @NotEmpty String service) {
    public static ReservationDto valueFrom(Reservation reservation) {
        return new ReservationDto(reservation.getId(), reservation.getName(), reservation.getDateFrom().getTime(), reservation.getDateTo().getTime(), reservation.getOfferId(), reservation.getService().toString());
    }
}