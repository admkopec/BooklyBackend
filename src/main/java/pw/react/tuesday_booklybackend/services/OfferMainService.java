package pw.react.tuesday_booklybackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;

public class OfferMainService implements OfferService {
    private static final Logger log = LoggerFactory.getLogger(OfferMainService.class);

    @Autowired
    private ServicesIntegrationService integrationService;

    // TODO: Implement Offer API calls

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
}
