package pw.react.tuesday_booklybackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.tuesday_booklybackend.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
