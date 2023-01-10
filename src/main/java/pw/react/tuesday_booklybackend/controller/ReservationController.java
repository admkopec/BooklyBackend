package pw.react.tuesday_booklybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.services.ReservationService;
import pw.react.tuesday_booklybackend.web.ReservationDto;

import java.util.UUID;

@RestController
@RequestMapping(path = "/reservation")
public class ReservationController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Create new reservation")
    @PostMapping(path = "")
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) {
        log.info("Received create a reservation request.");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Make an API request to appropriate service.");
        // Use a reservationService for that
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationDto, user));
    }

    // TODO: Add an endpoint for fetching all reservations of a user

    @Operation(summary = "Fetch reservation info")
    @GetMapping(path = "/{reservationId}")
    public ResponseEntity<ReservationDto> fetchReservation(@PathVariable UUID reservationId) {
        log.info("Received fetch reservation info request.");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Make an API request to appropriate service.");
        // Use a reservationService for that
        ReservationDto reservationDto = reservationService.fetchReservation(reservationId, user);
        return ResponseEntity.status(HttpStatus.OK).body(reservationDto);
    }

    @Operation(summary = "Remove reservation")
    @DeleteMapping(path = "/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable UUID reservationId) {
        log.info("Received delete reservation info request.");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Make an API request to appropriate service.");
        // Use a reservationService for that
        reservationService.deleteReservation(reservationId, user);
    }
}
