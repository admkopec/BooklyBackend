package pw.react.tuesday_booklybackend.web;

import java.util.UUID;
public record OfferCarlyDto(UUID id, String brand, String model, Double year, Double pricePerDay, String city) {
}
