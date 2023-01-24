package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.UUID;

public record ReservationDto(UUID id, @NotEmpty String name, @FutureOrPresent Date fromDate, @Future Date toDate, @NotEmpty String service, @NotEmpty UUID offerId) {
}