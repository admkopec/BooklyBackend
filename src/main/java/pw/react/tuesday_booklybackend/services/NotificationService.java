package pw.react.tuesday_booklybackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import pw.react.tuesday_booklybackend.web.NotificationDto;

import java.util.UUID;

public class NotificationService {
    @Autowired
    private ReservationService reservationService;

    public void handleNotification(NotificationDto notificationDto) {
        // Handle notification by performing update on each changed reservation
        for (UUID reservationId: notificationDto.bookings()) {
            reservationService.updateReservation(reservationId);
        }
    }
}
