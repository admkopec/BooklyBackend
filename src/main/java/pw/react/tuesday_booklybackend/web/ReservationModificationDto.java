package pw.react.tuesday_booklybackend.web;

import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

public record ReservationModificationDto(UUID id, @NotEmpty String name, @FutureOrPresent long dateFrom, @Future long dateTo, @NotEmpty UUID offerId, Integer numberOfSpaces) {
    public static boolean isValid(ReservationModificationDto reservationDto) {
        if (reservationDto.name() == null || reservationDto.name().isEmpty()) {
            return false;
        }
        if (reservationDto.dateFrom() > reservationDto.dateTo() || reservationDto.dateFrom() < (new Date().getTime())/1000) {
            return false;
        }
        if (reservationDto.offerId() == null) {
            return false;
        }
        return true;
    }
    public static Reservation convertToReservation(ReservationModificationDto reservationDto, CompanionService service) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.id());
        reservation.setName(reservationDto.name());
        reservation.setDateFrom(new Date(reservationDto.dateFrom()*1000));
        reservation.setDateTo(new Date(reservationDto.dateTo()*1000));
        reservation.setOfferId(reservationDto.offerId());
        reservation.setService(service);
        return reservation;
    }

    public static Reservation updateReservation(Reservation reservation, ReservationModificationDto reservationDto) {
        reservation.setName(reservationDto.name());
        reservation.setDateFrom(new Date(reservationDto.dateFrom()));
        reservation.setDateTo(new Date(reservationDto.dateTo()));
        reservation.setOfferId(reservationDto.offerId());
        return reservation;
    }

}
