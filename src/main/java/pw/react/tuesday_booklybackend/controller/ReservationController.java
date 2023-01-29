package pw.react.tuesday_booklybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.services.ReservationService;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.ReservationAdminDto;
import pw.react.tuesday_booklybackend.web.ReservationDto;
import pw.react.tuesday_booklybackend.web.ReservationModificationDto;
import pw.react.tuesday_booklybackend.web.UserDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/logic/api/bookings")
public class ReservationController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Create new reservation")
    @PostMapping(path = "")
    public ResponseEntity<ReservationDto> createReservation(@RequestParam String service,
                                                            @RequestBody ReservationModificationDto reservationDto) {
        log.info("Received create a reservation request.");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Make an API request to appropriate service.");
        // Use a reservationService for that
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationDto, user, CompanionService.valueOf(service)));
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

    @Operation(summary = "Fetch all reservations info made by the particular user")
    @GetMapping(path = "")
    public ResponseEntity<Collection<ReservationDto>> fetchReservations(@RequestParam(defaultValue = "-date") String sortBy,
                                                                        @RequestParam(required = false) String search,
                                                                        @RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<ReservationDto> allReservations = reservationService.fetchReservations(user);
        // TODO: Implement searching of `allReservations`
        // Implementation of paging by filtering `allReservations`
        // TODO: Implement sorting of `allReservations`
        switch (sortBy) {
            case "name":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation1.name(), reservation2.name())).toList();
                break;
            case "-name":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation2.name(), reservation1.name())).toList();
                break;
            case "type":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation1.service(), reservation2.service())).toList();
                break;
            case "-type":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation2.service(), reservation1.service())).toList();
                break;
            case "date":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> Long.compare(reservation1.dateFrom(), reservation2.dateFrom())).toList();
                break;
            case "-date":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> Long.compare(reservation2.dateFrom(), reservation1.dateFrom())).toList();
                break;
        }

        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allReservations.size());
        if (startIndex > endIndex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        return ResponseEntity.status(HttpStatus.OK).body(allReservations.stream().toList().subList(startIndex, endIndex));
    }

    @Operation(summary = "Fetch all reservations info")
    @GetMapping(path = "/all")
    public ResponseEntity<Collection<ReservationAdminDto>> fetchAllReservations(@RequestParam(defaultValue = "name") String sortBy,
                                                                                @RequestParam(defaultValue = "1") int page,
                                                                                @RequestParam(defaultValue = "30") int itemsOnPage,
                                                                                @RequestParam(defaultValue = "all") String filterCategory,
                                                                                @RequestParam(defaultValue = "") String searchCriteria) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<ReservationAdminDto> allReservations = reservationService.fetchAllReservations(user, Optional.empty());
        // Implementation of paging by filtering and sorting `allReservations`
        Collection<ReservationAdminDto> allReservationsCopy = allReservations;

        switch (filterCategory){
            case "all":
                break;
            case "Parkly":
                allReservations = allReservationsCopy.stream().filter((reservation) -> reservation.type()==CompanionService.Parkly.name()).toList();
                break;
            case "Flatly":
                allReservations = allReservationsCopy.stream().filter((reservation) -> reservation.type()==CompanionService.Flatly.name()).toList();
                break;
            case "Carly":
                allReservations = allReservationsCopy.stream().filter((reservation) -> reservation.type()==CompanionService.Carly.name()).toList();
                break;
        }

        if(searchCriteria != "")
        {
            allReservations = allReservationsCopy.stream().filter((reservation) -> reservation.name().toLowerCase().contains(searchCriteria.toLowerCase())).toList();
        }


        switch (sortBy) {
            case "user":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation1.username(), reservation2.username())).toList();
                break;
            case "-user":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation2.username(), reservation1.username())).toList();
                break;
            case "name":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation1.name(), reservation2.name())).toList();
                break;
            case "-name":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation2.name(), reservation1.name())).toList();
                break;
            case "type":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation1.type(), reservation2.type())).toList();
                break;
            case "-type":
                allReservations = allReservations.stream().sorted((reservation1, reservation2) -> String.CASE_INSENSITIVE_ORDER.compare(reservation2.type(), reservation1.type())).toList();
                break;
        }
        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allReservations.size());
        if (startIndex > endIndex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        return ResponseEntity.status(HttpStatus.OK).body(allReservations.stream().toList().subList(startIndex, endIndex));
    }
}
