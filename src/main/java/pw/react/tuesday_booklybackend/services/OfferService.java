package pw.react.tuesday_booklybackend.services;

import pw.react.tuesday_booklybackend.utils.CompanionService;

import java.util.Collection;
import java.util.UUID;

public interface OfferService {
    Collection fetchParklyOffers(String location, long dateFrom, long dateTo, int numberOfSpaces, int page);
    Collection fetchCarlyOffers(String location, long dateFrom, long dateTo, String carType, int page);
    Collection fetchFlatlyOffers(String location, long dateFrom, long dateTo, int numberOfAdults, int numberOfKids, int page);
    String fetchOffer(UUID offerId, CompanionService service);
}
