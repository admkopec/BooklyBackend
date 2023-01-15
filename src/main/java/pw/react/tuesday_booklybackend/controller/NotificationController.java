package pw.react.tuesday_booklybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.react.tuesday_booklybackend.services.NotificationService;
import pw.react.tuesday_booklybackend.services.OfferService;
import pw.react.tuesday_booklybackend.services.ReservationService;
import pw.react.tuesday_booklybackend.web.NotificationDto;

import java.util.UUID;

@RestController
@RequestMapping(path = "/logic/api/notifications")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Inform about changes to any bookings made by Bookly")
    @PostMapping(path = "")
    public void notification(@RequestBody NotificationDto notificationDto) {
        log.info("Received a new notification.");
        // Handle notification
        notificationService.handleNotification(notificationDto);
    }
}
