package pw.react.tuesday_booklybackend.services;

import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.OfferDto;

import java.util.Collection;
import java.util.UUID;

public interface OfferService {
    Collection<OfferDto> fetchParklyOffers(String location, long dateFrom, long dateTo, int numberOfSpaces, int page);
    Collection<OfferDto> fetchCarlyOffers(String location, long dateFrom, long dateTo, String carType, int page, int itemsOnPage);
    Collection<OfferDto> fetchFlatlyOffers(String location, long dateFrom, long dateTo, int numberOfAdults, int numberOfKids, int page, int itemsOnPage);
    OfferDto fetchOffer(UUID offerId, CompanionService service);
}
