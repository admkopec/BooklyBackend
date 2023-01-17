package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;
import pw.react.tuesday_booklybackend.dao.ReservationRepository;
import pw.react.tuesday_booklybackend.mail.services.MailService;
import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.ReservationAdminDto;
import pw.react.tuesday_booklybackend.web.ReservationDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ReservationMainService implements ReservationService {
    private static final Logger log = LoggerFactory.getLogger(ReservationMainService.class);

    private final ReservationRepository reservationRepository;
    @Autowired
    private ServicesIntegrationService integrationService;
    @Autowired
    private MailService mailService;

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
    public ReservationDto updateReservation(UUID reservationId, ReservationDto reservationDto, User user) {
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
    public void updateReservation(UUID reservationId) {
        Optional<Reservation> dbReservation = reservationRepository.findById(reservationId);
        if (!dbReservation.isPresent()) {
            return;
        }
        // Get proper URL and Headers from Services Integration Service
        String serviceUrl = integrationService.getUrl(dbReservation.get().getService());
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(dbReservation.get().getService());

        // Call API endpoint
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(serviceUrl + "/logic/api/bookings/"+reservationId, HttpMethod.GET, new HttpEntity<String>(authorizedHeaders), String.class);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Inform the user about cancellation of their reservation
            User user = dbReservation.get().getUser();
            try {
                mailService.sendReservationCancelledEmailTo(user);
            } catch (Exception e) {
                log.error("There was an error while sending cancellation email!");
            }
            // If error is 404, delete the record from our database
            reservationRepository.deleteById(reservationId);
        } else {
            // TODO: Update the name of the reservation
        }
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
    public Collection<ReservationDto> fetchReservations(User user) {
        Collection<Reservation> dbReservations = user.getReservations();
        // TODO: Call API endpoint, return the results
        return new ArrayList<>();
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
    public Collection<ReservationAdminDto> fetchAllReservations(User user, Optional<CompanionService> service) {
        if (!user.getIsAdmin()) {
            throw new AccessDeniedException("User doesn't have required privileges");
        }
        Collection<Reservation> dbReservations;
        if (service.isPresent()) {
            dbReservations = reservationRepository.findAllByService(service.get());
        } else {
            dbReservations = reservationRepository.findAll();
        }
        // Return the results
        return dbReservations.stream().map(ReservationAdminDto::valueFrom).toList();
    }
}
