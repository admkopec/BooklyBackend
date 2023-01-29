package pw.react.tuesday_booklybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import pw.react.tuesday_booklybackend.services.OfferService;
import pw.react.tuesday_booklybackend.models.User;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;
import java.util.UUID;

@Tag(name = "Offers")
@RestController
@RequestMapping(path = "/logic/api/offers")
public class OfferController {
    private static final Logger log = LoggerFactory.getLogger(OfferController.class);
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @Operation(summary = "Fetch offer info")
    @GetMapping(path = "/{service}/{offerId}")
    public ResponseEntity<OfferDto> fetchOffer(@PathVariable String service, @PathVariable UUID offerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: Maybe add some kind of special offer treatment for particular user, such as membership benefits
        CompanionService companionService = switch (service){
            case "parkly" -> CompanionService.Parkly;
            case "carly" -> CompanionService.Carly;
            case "flatly" -> CompanionService.Flatly;
            default -> null;
        };
        if (companionService == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        return ResponseEntity.status(HttpStatus.OK).body(offerService.fetchOffer(offerId, companionService));
    }

    @Operation(summary = "Fetch offers from Parkly")
    @GetMapping(path = "/parkly")
    public ResponseEntity<Collection<OfferDto>> fetchParkly(@RequestParam String location,
                                                  @RequestParam long dateFrom,
                                                  @RequestParam long dateTo,
                                                  @RequestParam int numberOfSpaces,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: Maybe add some kind of special offer treatment for particular user, such as membership benefits
        Collection<OfferDto> allOffers = offerService.fetchParklyOffers(location, dateFrom, dateTo, numberOfSpaces, page);
        return ResponseEntity.status(HttpStatus.OK).body(allOffers);
    }

    @Operation(summary = "Fetch offers from Carly")
    @GetMapping(path = "/carly")
    public ResponseEntity<Collection<OfferDto>> fetchCarly(@RequestParam String location,
                                                 @RequestParam long dateFrom,
                                                 @RequestParam long dateTo,
                                                 @RequestParam String carType,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: Maybe add some kind of special offer treatment for particular user, such as membership benefits
        Collection<OfferDto> allOffers = offerService.fetchCarlyOffers(location, dateFrom, dateTo, carType, page, itemsOnPage);
        return ResponseEntity.status(HttpStatus.OK).body(allOffers);
    }

    @Operation(summary = "Fetch offers from Flatly")
    @GetMapping(path = "/flatly")
    public ResponseEntity<Collection<OfferDto>> fetchFlatly(@RequestParam String location,
                                                  @RequestParam long dateFrom,
                                                  @RequestParam long dateTo,
                                                  @RequestParam int numberOfAdults,
                                                  @RequestParam(defaultValue = "0") int numberOfKids,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "30") int itemsOnPage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: Maybe add some kind of special offer treatment for particular user, such as membership benefits
        Collection<OfferDto> allOffers = offerService.fetchFlatlyOffers(location, dateFrom, dateTo, numberOfAdults, numberOfKids, page, itemsOnPage);
        return ResponseEntity.status(HttpStatus.OK).body(allOffers);
    }
}
