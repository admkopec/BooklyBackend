package pw.react.tuesday_booklybackend.services;

import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;
import java.util.UUID;

public interface OfferService {
    Collection<OfferDto> fetchParklyOffers(String location, long dateFrom, long dateTo, int numberOfSpaces, int page);
    Collection fetchCarlyOffers(String location, long dateFrom, long dateTo, String carType, int page);
    Collection fetchFlatlyOffers(String location, long dateFrom, long dateTo, int numberOfAdults, int numberOfKids, int page);
    OfferDto fetchOffer(UUID offerId, CompanionService service);
}
