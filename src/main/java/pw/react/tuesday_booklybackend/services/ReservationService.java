package pw.react.tuesday_booklybackend.services;

import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.ReservationAdminDto;
import pw.react.tuesday_booklybackend.web.ReservationDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ReservationService {
    ReservationDto createReservation(ReservationDto reservationDto, User user);
    void updateReservation(UUID reservationId);
    ReservationDto updateReservation(UUID reservationId, ReservationDto reservationDto, User user);
    ReservationDto fetchReservation(UUID reservationId, User user);
    Collection<ReservationDto> fetchReservations(User user);
    void deleteReservation(UUID reservationId, User user);
    Collection<ReservationAdminDto> fetchAllReservations(User user, Optional<CompanionService> service);
}
