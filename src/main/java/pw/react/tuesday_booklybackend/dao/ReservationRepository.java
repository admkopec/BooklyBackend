package pw.react.tuesday_booklybackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.tuesday_booklybackend.models.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findById(UUID id);
}
