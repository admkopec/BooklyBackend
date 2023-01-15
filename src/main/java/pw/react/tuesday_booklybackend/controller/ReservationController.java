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
import pw.react.tuesday_booklybackend.web.UserDto;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping(path = "/logic/api/bookings")
public class ReservationController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Create new reservation")
    @PutMapping(path = "")
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) {
        log.info("Received create a reservation request.");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Make an API request to appropriate service.");
        // Use a reservationService for that
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationDto, user));
    }

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

    @Operation(summary = "Update reservation info")
    @PostMapping(path = "/{reservationId}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable UUID reservationId, @RequestBody ReservationDto reservationDto) {
        log.info("Received update reservation info request.");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Make an API request to appropriate service.");
        // Use a reservationService for that
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.updateReservation(reservationId, reservationDto, user));
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

    @Operation(summary = "Fetch all reservations info")
    @GetMapping(path = "")
    public ResponseEntity<Collection<ReservationDto>> fetchUsers(@RequestParam(defaultValue = "name") String sortBy,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<ReservationDto> allReservations = reservationService.fetchAllReservations(user);
        // Implementation of paging by filtering `allReservations`
        // TODO: Implement sorting of `allReservations`
        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allReservations.size());
        return ResponseEntity.status(HttpStatus.OK).body(allReservations.stream().toList().subList(startIndex, endIndex));
    }
}
