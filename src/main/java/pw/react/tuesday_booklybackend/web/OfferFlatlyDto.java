package pw.react.tuesday_booklybackend.web;

import java.util.UUID;

public record OfferFlatlyDto(UUID uuid, String name, String description, Double price, String location) {
}
