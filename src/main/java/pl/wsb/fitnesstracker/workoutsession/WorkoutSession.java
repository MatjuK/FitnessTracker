package pl.wsb.fitnesstracker.workoutsession;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.wsb.fitnesstracker.training.api.Training;

import java.time.LocalDateTime;

@Entity
@Table(name = "workout_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacja ManyToOne: wiele sesji może należeć do jednego treningu
    // (np. użytkownik zrobił przerwę i wznowił trening)
    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    // Zmienione z String na LocalDateTime — przechowuje datę i godzinę sesji
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "startLatitude")
    private double startLatitude;

    @Column(name = "startLongitude")
    private double startLongitude;

    @Column(name = "endLatitude")
    private double endLatitude;

    @Column(name = "endLongitude")
    private double endLongitude;

    @Column(name = "altitude")
    private double altitude;

    public WorkoutSession(Training training, LocalDateTime timestamp,
                          double startLatitude, double startLongitude,
                          double endLatitude, double endLongitude,
                          double altitude) {
        this.training = training;
        this.timestamp = timestamp;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.altitude = altitude;
    }
}
