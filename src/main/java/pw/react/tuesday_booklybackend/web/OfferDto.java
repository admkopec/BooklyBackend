package pw.react.tuesday_booklybackend.web;

import java.util.UUID;

public record OfferDto(UUID id, String name, String imageUrl, String description, Double pricePerDay) {
    public static OfferDto valueFrom(OfferParklyDto offerDto) {
        return new OfferDto(offerDto.id(), offerDto.name(), offerDto.imageUrl(), offerDto.address(), offerDto.pricePerHour() * 24);
    }

    public static OfferDto valueFrom(OfferCarlyDto offerDto) {
        return new OfferDto(offerDto.id(), offerDto.brand(), null, offerDto.city(), offerDto.pricePerDay());
    }

    public static OfferDto valueFrom(OfferFlatlyDto offerDto) {
        return new OfferDto(offerDto.uuid(), offerDto.name(), null, offerDto.location(), offerDto.price());
    }
}
