package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.react.tuesday_booklybackend.dao.ReservationRepository;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.web.ReservationDto;

import java.util.UUID;

public class ReservationMainService implements ReservationService {
    private static final Logger log = LoggerFactory.getLogger(UserMainService.class);

    private final ReservationRepository reservationRepository;

    public ReservationMainService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Override
    public ReservationDto createReservation(ReservationDto reservationDto, User user) {
        // TODO: Implement
        return null;
    }

    @Override
    public ReservationDto fetchReservation(UUID reservationId, User user) {
        // TODO: Implement
        return null;
    }

    @Override
    public void deleteReservation(UUID reservationId, User user) {
        // TODO: Implement
    }
}
