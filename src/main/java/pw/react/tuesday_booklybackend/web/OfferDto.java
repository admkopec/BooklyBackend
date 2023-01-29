package pw.react.tuesday_booklybackend.web;

import java.util.UUID;

public record OfferDto(UUID id, String name, String imageUrl, String description, Double pricePerDay) {
    public static OfferDto valueFrom(OfferParklyDto offerDto) {
        return new OfferDto(offerDto.id(), offerDto.name(), offerDto.imageUrl(), offerDto.address(), offerDto.pricePerHour() * 24);
    }
}
