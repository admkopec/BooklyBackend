package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.webjars.NotFoundException;
import pw.react.tuesday_booklybackend.dao.ReservationRepository;
import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.web.ReservationDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ReservationMainService implements ReservationService {
    private static final Logger log = LoggerFactory.getLogger(UserMainService.class);

    private final ReservationRepository reservationRepository;

    public ReservationMainService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    private boolean userHasAccess(User user, Reservation reservation) {
        return user.getIsAdmin() || reservation.getUser().getId() == user.getId();
    }


    @Override
    public ReservationDto createReservation(ReservationDto reservationDto, User user) {
        // TODO: Call API endpoint, if successful create an entry in our database
        return null;
    }

    @Override
    public ReservationDto fetchReservation(UUID reservationId, User user) {
        Optional<Reservation> dbReservation = reservationRepository.findById(reservationId);
        if (!dbReservation.isPresent()) {
            throw new NotFoundException("Reservation not found");
        }
        if (!userHasAccess(user, dbReservation.get())) {
            throw new AccessDeniedException("User doesn't have required privileges");
        }
        // TODO: Call API endpoint, return the results
        return null;
    }

    @Override
    public void deleteReservation(UUID reservationId, User user) {
        Optional<Reservation> dbReservation = reservationRepository.findById(reservationId);
        if (!dbReservation.isPresent()) {
            throw new NotFoundException("Reservation not found");
        }
        if (!userHasAccess(user, dbReservation.get())) {
            throw new AccessDeniedException("User doesn't have required privileges");
        }
        // TODO: Call API endpoint, if success remove `dbReservation` from our database
    }

    @Override
    public Collection<ReservationDto> fetchAllReservations(User user) {
        if (!user.getIsAdmin()) {
            throw new AccessDeniedException("User doesn't have required privileges");
        }
        Collection<Reservation> dbReservations = reservationRepository.findAll();
        // TODO: Call API endpoint, return the results
        return null;
    }
}
