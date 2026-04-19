package pl.wsb.fitnesstracker.user.api;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.wsb.fitnesstracker.event.Event;

import java.time.LocalDate;

@Entity
@Table(name = "user_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    // Relacja ManyToOne: wielu użytkowników może być zapisanych na wiele eventów
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relacja ManyToOne: na jeden event może być zapisanych wielu użytkowników
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "status")
    private String status;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    public UserEvent(User user, Event event, String status, LocalDate registrationDate) {
        this.user = user;
        this.event = event;
        this.status = status;
        this.registrationDate = registrationDate;
    }
}
