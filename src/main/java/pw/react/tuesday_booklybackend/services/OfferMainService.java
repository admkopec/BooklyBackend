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
import pw.react.tuesday_booklybackend.web.ReservationDto;
import pw.react.tuesday_booklybackend.web.ReservationModificationDto;

import java.util.Collection;
import java.util.UUID;

public class OfferMainService implements OfferService {
    private static final Logger log = LoggerFactory.getLogger(OfferMainService.class);

    @Autowired
    private ServicesIntegrationService integrationService;

    // TODO: Implement Offer API calls
    // TODO: Add a price markup of 1.4% on each offer (may vary based on the user's membership level)

    @Override
    public Collection fetchParklyOffers(String location, long dateFrom, long dateTo, int numberOfSpaces, int page) {
        // Call API endpoint, if successful create an entry in our database
        String serviceUrl = integrationService.getUrl(CompanionService.Parkly);
        HttpHeaders authorizedHeaders = integrationService.getAuthorizationHeaders(CompanionService.Parkly);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Collection> response = restTemplate.exchange(serviceUrl + "/logic/api/offers?location="+location+
                "&dateFrom="+dateFrom+
                "&dateTo="+dateTo+
                "&numberOfSpaces="+numberOfSpaces+
                "&page="+page, HttpMethod.GET, new HttpEntity<>(authorizedHeaders), Collection.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public Collection fetchCarlyOffers() {
        return null;
    }

    @Override
    public Collection fetchFlatlyOffers() {
        return null;
    }

    @Override
    public OfferDto fetchOffer(UUID offerId, CompanionService service) {
        return null;
    }
}
