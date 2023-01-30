package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import pw.react.tuesday_booklybackend.dao.ReservationRepository;
import pw.react.tuesday_booklybackend.mail.services.MailService;
import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.*;

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
        //return user.getIsAdmin() || reservation.getUser().getId() == user.getId();
        return user.getIsAdmin() || reservation.getUser().getId().equals(user.getId());
    }

    @Override
    public ReservationDto createReservation(ReservationModificationDto reservationDto, User user, CompanionService service) {
        if (!ReservationModificationDto.isValid(reservationDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided Reservation was bad");
        }
        switch (service) {
            case Parkly:
                return createReservationParkly(reservationDto, user);
            case Carly:
                return createReservationCarly(reservationDto, user);
            case Flatly:
                return createReservationFlatly(reservationDto, user);
            default:
                return null;
        }
    }


    private ReservationDto createReservationParkly(ReservationModificationDto reservationDto, User user) {
        // Call API endpoint, if successful create an entry in our database
        String serviceUrl = integrationService.getUrl(CompanionService.Parkly);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(CompanionService.Parkly);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ReservationParklyResponseDto> response = restTemplate.exchange(serviceUrl + "/logic/api/bookings/", HttpMethod.POST, new HttpEntity<>(ReservationParklyDto.valueFrom(reservationDto), authorizedHeaders), ReservationParklyResponseDto.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Reservation reservation = ReservationParklyResponseDto.convertToReservation(response.getBody(), CompanionService.Parkly);
            reservation.setName(reservationDto.name());
            // Validate that we don't have id collisions
            if (reservationRepository.findById(reservation.getId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The companion service returned a bad response");
            }
            reservation.setUser(user);
            reservationRepository.save(reservation);
            return ReservationDto.valueFrom(reservation);
        }
        throw new ResponseStatusException(response.getStatusCode(), "The companion service encountered an error");
    }

    private ReservationDto createReservationCarly(ReservationModificationDto reservationDto, User user) {
        // Call API endpoint, if successful create an entry in our database
        String serviceUrl = integrationService.getUrl(CompanionService.Carly);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(CompanionService.Carly);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ReservationCarlyDto> response = restTemplate.exchange(serviceUrl + "/logic/api/bookings/", HttpMethod.POST, new HttpEntity<>(ReservationCarlyDto.valueFrom(reservationDto, user), authorizedHeaders), ReservationCarlyDto.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Reservation reservation = ReservationCarlyDto.convertToReservation(response.getBody(), CompanionService.Carly);
            reservation.setName(reservationDto.name());
            // Validate that we don't have id collisions
            if (reservationRepository.findById(reservation.getId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The companion service returned a bad response");
            }
            reservation.setUser(user);
            reservationRepository.save(reservation);
            return ReservationDto.valueFrom(reservation);
        }
        throw new ResponseStatusException(response.getStatusCode(), "The companion service encountered an error");
    }

    private ReservationDto createReservationFlatly(ReservationModificationDto reservationDto, User user) {
        // Call API endpoint, if successful create an entry in our database
        String serviceUrl = integrationService.getUrl(CompanionService.Flatly);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(CompanionService.Flatly);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ReservationFlatlyDto> response = restTemplate.exchange(serviceUrl + "/logic/api/bookings/", HttpMethod.POST, new HttpEntity<>(ReservationFlatlyDto.valueFrom(reservationDto, user), authorizedHeaders), ReservationFlatlyDto.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Reservation reservation = ReservationFlatlyDto.convertToReservation(response.getBody(), CompanionService.Flatly);
            reservation.setName(reservationDto.name());
            // Validate that we don't have id collisions
            if (reservationRepository.findById(reservation.getId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The companion service returned a bad response");
            }
            reservation.setUser(user);
            reservationRepository.save(reservation);
            return ReservationDto.valueFrom(reservation);
        }
        throw new ResponseStatusException(response.getStatusCode(), "The companion service encountered an error");
    }

    @Override
    public ReservationDto updateReservation(UUID reservationId, ReservationModificationDto reservationDto, User user) {
        Optional<Reservation> dbReservation = reservationRepository.findById(reservationId);
        if (!dbReservation.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        if (!userHasAccess(user, dbReservation.get())) {
            throw new AccessDeniedException("User doesn't have required privileges");
        }
        // Make sure the supplied reservation is valid
        if (ReservationModificationDto.isValid(reservationDto)) {
            // Call API endpoint, return the results
            String serviceUrl = integrationService.getUrl(dbReservation.get().getService());
            HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(dbReservation.get().getService());
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ReservationModificationDto> response = restTemplate.exchange(serviceUrl + "/logic/api/bookings/" + reservationId, HttpMethod.PUT, new HttpEntity<>(reservationDto, authorizedHeaders), ReservationModificationDto.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                reservationRepository.save(ReservationModificationDto.updateReservation(dbReservation.get(), response.getBody()));
            }
        }
        return ReservationDto.valueFrom(dbReservation.get());
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
        ResponseEntity<ReservationModificationDto> response = restTemplate.exchange(serviceUrl + "/logic/api/bookings/"+reservationId, HttpMethod.GET, new HttpEntity<Void>(authorizedHeaders), ReservationModificationDto.class);

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
        } else if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // Update the name of the reservation, dateFrom and dateTo as well as offerId
            reservationRepository.save(ReservationModificationDto.updateReservation(dbReservation.get(), response.getBody()));
        }
    }

    @Override
    public ReservationDto fetchReservation(UUID reservationId, User user) {
        Optional<Reservation> dbReservation = reservationRepository.findById(reservationId);
        if (!dbReservation.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        if (!userHasAccess(user, dbReservation.get())) {
            throw new AccessDeniedException("User doesn't have required privileges");
        }
        // TODO: Optionally: Call API endpoint, return the results
        return ReservationDto.valueFrom(dbReservation.get());
    }

    @Override
    public Collection<ReservationDto> fetchReservations(User user) {
        Collection<Reservation> dbReservations = reservationRepository.findAllByUser(user);
        // TODO: Optionally: Call API endpoint, return the results
        return dbReservations.stream().map(ReservationDto::valueFrom).toList();
    }

    @Override
    public void deleteReservation(UUID reservationId, User user) {
        Optional<Reservation> dbReservation = reservationRepository.findById(reservationId);
        if (!dbReservation.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        if (!userHasAccess(user, dbReservation.get())) {
            throw new AccessDeniedException("User doesn't have required privileges");
        }
        // Call API endpoint, if success remove `dbReservation` from our database
        String serviceUrl = integrationService.getUrl(dbReservation.get().getService());
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(dbReservation.get().getService());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(serviceUrl + "/logic/api/bookings/"+reservationId, HttpMethod.DELETE, new HttpEntity<>("", authorizedHeaders), String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            reservationRepository.delete(dbReservation.get());
        }
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
