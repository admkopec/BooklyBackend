package pw.react.tuesday_booklybackend.models;

import javax.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pw.react.tuesday_booklybackend.utils.CompanionService;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @Column
    private CompanionService service;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CompanionService getService() {
        return service;
    }

    public void setService(CompanionService service) {
        this.service = service;
    }
}
