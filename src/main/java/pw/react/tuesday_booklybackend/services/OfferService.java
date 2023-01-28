package pw.react.tuesday_booklybackend.services;

import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;
import java.util.UUID;

public interface OfferService {
    // TODO: Support passing parameters to fetching functions
    Collection fetchParklyOffers(String location, long dateFrom, long dateTo, int numberOfSpaces, int page);
    Collection fetchCarlyOffers();
    Collection fetchFlatlyOffers();
    OfferDto fetchOffer(UUID offerId, CompanionService service);
}
