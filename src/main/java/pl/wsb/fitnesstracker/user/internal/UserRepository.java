package pl.wsb.fitnesstracker.user.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
    default Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findFirst();
    }

    /**
     * Search users whose email contains the given fragment (case-insensitive).
     *
     * @param fragment fragment of email
     * @return list of matching users
     */
    default List<User> findByEmailContainingIgnoreCase(String fragment) {
        String lower = fragment.toLowerCase();
        return findAll().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().toLowerCase().contains(lower))
                .toList();
    }

    /**
     * Returns users with birthdate before the given date (i.e. older than the date).
     *
     * @param date threshold date
     * @return list of users born before {@code date}
     */
    default List<User> findByBirthdateBefore(LocalDate date) {
        return findAll().stream()
                .filter(u -> u.getBirthdate() != null && u.getBirthdate().isBefore(date))
                .toList();
    }
}