package pl.wsb.fitnesstracker.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Zapytanie JPQL — zwraca nadchodzące eventy (startDate w przyszłości).
     * JPQL używa nazw KLAS i PÓL Javy (Event, startDate),
     * a nie nazw tabel i kolumn bazy (event, start_date).
     */
    @Query("SELECT e FROM Event e WHERE e.startDate > :now ORDER BY e.startDate")
    List<Event> findUpcoming(@Param("now") LocalDate now);

    /**
     * Zapytanie natywne SQL — zwraca eventy w podanym mieście.
     * nativeQuery = true oznacza że używamy prawdziwego SQL z nazwami TABEL i KOLUMN,
     * a nie nazw klas Javy.
     */
    @Query(
            value = "SELECT * FROM event WHERE city = :city",
            nativeQuery = true
    )
    List<Event> findByCity(@Param("city") String city);

}
