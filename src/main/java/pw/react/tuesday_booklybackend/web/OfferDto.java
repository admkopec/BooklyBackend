package pw.react.tuesday_booklybackend.web;

import java.util.UUID;

public record OfferDto(UUID id, String name, String imageUrl, String address, Double pricePerDay) {
}
