package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;
import java.util.UUID;

public class OfferMainService implements OfferService {
    private static final Logger log = LoggerFactory.getLogger(OfferMainService.class);

    @Autowired
    private ServicesIntegrationService integrationService;

    // TODO: Implement Offer API calls
    // TODO: Add a price markup of 1.4% on each offer (may vary based on the user's membership level)

    @Override
    public Collection<OfferDto> fetchParklyOffers() {
        return null;
    }

    @Override
    public Collection<OfferDto> fetchCarlyOffers() {
        return null;
    }

    @Override
    public Collection<OfferDto> fetchFlatlyOffers() {
        return null;
    }

    @Override
    public OfferDto fetchOffer(UUID offerId, CompanionService service) {
        return null;
    }
}
