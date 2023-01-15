package pw.react.tuesday_booklybackend.services;

import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;

public interface OfferService {
    // TODO: Support passing parameters to fetching functions
    Collection<OfferDto> fetchParklyOffers();
    Collection<OfferDto> fetchCarlyOffers();
    Collection<OfferDto> fetchFlatlyOffers();

}
