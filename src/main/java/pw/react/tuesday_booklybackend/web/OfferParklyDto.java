package pw.react.tuesday_booklybackend.web;

import java.util.UUID;

public record OfferParklyDto(UUID id, String name, String imageUrl, String address, Double pricePerHour, String openingHours) {
}
