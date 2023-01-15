package pw.react.tuesday_booklybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.react.tuesday_booklybackend.services.OfferService;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/logic/api/offers")
public class OfferController {
    private static final Logger log = LoggerFactory.getLogger(OfferController.class);
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }


    @Operation(summary = "Fetch offers from Parkly")
    @GetMapping(path = "parkly")
    public ResponseEntity<Collection<OfferDto>> fetchParkly(@RequestParam String location,
                                                            @RequestParam String dateFrom,
                                                            @RequestParam String dateTo,
                                                            @RequestParam int numberOfSpaces,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<OfferDto> allOffers = offerService.fetchParklyOffers();
        // Implementation of paging by filtering `allReservations`
        // TODO: Implement sorting of `allOffers`
        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allOffers.size());
        return ResponseEntity.status(HttpStatus.OK).body(allOffers.stream().toList().subList(startIndex, endIndex));
    }

    @Operation(summary = "Fetch offers from Carly")
    @GetMapping(path = "carly")
    public ResponseEntity<Collection<OfferDto>> fetchCarly(@RequestParam String location,
                                                           @RequestParam String dateFrom,
                                                           @RequestParam String dateTo,
                                                           @RequestParam String carType,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<OfferDto> allOffers = offerService.fetchCarlyOffers();
        // Implementation of paging by filtering `allReservations`
        // TODO: Implement sorting of `allOffers`
        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allOffers.size());
        return ResponseEntity.status(HttpStatus.OK).body(allOffers.stream().toList().subList(startIndex, endIndex));
    }

    @Operation(summary = "Fetch offers from Flatly")
    @GetMapping(path = "flatly")
    public ResponseEntity<Collection<OfferDto>> fetchFlatly(@RequestParam String location,
                                                            @RequestParam String dateFrom,
                                                            @RequestParam String dateTo,
                                                            @RequestParam int numberOfAdults,
                                                            @RequestParam(defaultValue = "0") int numberOfKids,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<OfferDto> allOffers = offerService.fetchFlatlyOffers();
        // Implementation of paging by filtering `allReservations`
        // TODO: Implement sorting of `allOffers`
        int startIndex = (page - 1)*itemsOnPage;
        int endIndex = Math.min(page * itemsOnPage, allOffers.size());
        return ResponseEntity.status(HttpStatus.OK).body(allOffers.stream().toList().subList(startIndex, endIndex));
    }
}
