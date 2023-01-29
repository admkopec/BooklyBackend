package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pw.react.tuesday_booklybackend.models.Reservation;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.OfferDto;
import pw.react.tuesday_booklybackend.web.PagingDto;
import pw.react.tuesday_booklybackend.web.ReservationDto;
import pw.react.tuesday_booklybackend.web.ReservationModificationDto;

import java.util.Collection;
import java.util.UUID;

public class OfferMainService implements OfferService {
    private static final Logger log = LoggerFactory.getLogger(OfferMainService.class);

    @Autowired
    private ServicesIntegrationService integrationService;

    // TODO: Add a price markup of 1.4% on each offer (may vary based on the user's membership level)

    @Override
    public Collection<OfferDto> fetchParklyOffers(String location, long dateFrom, long dateTo, int numberOfSpaces, int page) {
        // Call API endpoint
        log.info("Starting Parkly offers fetch");
        String serviceUrl = integrationService.getUrl(CompanionService.Parkly);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(CompanionService.Parkly);
        RestTemplate restTemplate = new RestTemplate();
        log.info("Headers: " + authorizedHeaders);
        ResponseEntity<PagingDto> response = restTemplate.exchange(serviceUrl + "/logic/api/offers?location="+location+
                "&dateFrom="+dateFrom+
                "&dateTo="+dateTo+
                "&numberOfSpaces="+numberOfSpaces+
                "&page="+(page-1),
                HttpMethod.GET, new HttpEntity<>(authorizedHeaders), PagingDto.class);
        log.info("Recieved a response from Parkly offers fetch");
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Recieved {} items", response.getBody().content().size());
            return response.getBody().content().stream().map(OfferDto::valueFrom).toList();
        }
        return null;
    }

    @Override
    public Collection fetchCarlyOffers(String location, long dateFrom, long dateTo, String carType, int page) {
        // Call API endpoint
        log.info("Starting Carly offers fetch");
        String serviceUrl = integrationService.getUrl(CompanionService.Carly);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(CompanionService.Carly);
        RestTemplate restTemplate = new RestTemplate();
        log.info("Headers: " + authorizedHeaders);
        ResponseEntity<Collection> response = restTemplate.exchange(serviceUrl + "/logic/api/offers?location="+location+
                        "&dateFrom="+dateFrom+
                        "&dateTo="+dateTo+
                        "&carType="+carType+
                        "&page="+page+
                        "&itemsOnPage="+30,
                HttpMethod.GET, new HttpEntity<>(authorizedHeaders), Collection.class);
        log.info("Recieved a response from Carly offers fetch");
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.info("Recieved {} items", response.getBody().size());
            return response.getBody();
        }
        return null;
    }

    @Override
    public Collection fetchFlatlyOffers(String location, long dateFrom, long dateTo, int numberOfAdults, int numberOfKids, int page) {
        // Call API endpoint
        String serviceUrl = integrationService.getUrl(CompanionService.Flatly);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(CompanionService.Flatly);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Collection> response = restTemplate.exchange(serviceUrl + "/logic/api/offers?location="+location+
                        "&dateFrom="+dateFrom+
                        "&dateTo="+dateTo+
                        "&numberOfAdults="+numberOfAdults+
                        "&numberOfKids="+numberOfKids+
                        "&page="+page
                ,
                HttpMethod.GET, new HttpEntity<>(authorizedHeaders), Collection.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public OfferDto fetchOffer(UUID offerId, CompanionService service) {
        // Call API endpoint
        String serviceUrl = integrationService.getOfferUrl(service);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(service);
        RestTemplate restTemplate = new RestTemplate();
        log.info("Headers: " + authorizedHeaders);
        ResponseEntity<OfferDto> response = restTemplate.exchange(serviceUrl + "/"+offerId,
                HttpMethod.GET, new HttpEntity<>(authorizedHeaders), OfferDto.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        return null;
    }
}
