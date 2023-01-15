package pw.react.tuesday_booklybackend.services;

import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.web.ReservationDto;

import java.util.Collection;
import java.util.UUID;

public interface ReservationService {
    ReservationDto createReservation(ReservationDto reservationDto, User user);
    ReservationDto updateReservation(UUID reservationId, ReservationDto reservationDto, User user);
    ReservationDto fetchReservation(UUID reservationId, User user);
    void deleteReservation(UUID reservationId, User user);
    Collection<ReservationDto> fetchAllReservations(User user);
}
