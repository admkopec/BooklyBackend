package pw.react.tuesday_booklybackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.tuesday_booklybackend.models.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
