package pl.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Provider interface for read-only operations on User entities (queries).
 */
public interface UserProvider {

    /**
     * Retrieves a user by their id.
     *
     * @param userId id of the user
     * @return {@link Optional} with the user or empty if not found
     */
    Optional<User> getUser(Long userId);

    /**
     * Retrieves a user by their exact email.
     *
     * @param email exact email
     * @return {@link Optional} with the user or empty if not found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Returns all users from the system.
     *
     * @return list of all users
     */
    List<User> findAllUsers();

    /**
     * Searches users by email fragment (case-insensitive).
     *
     * @param emailFragment fragment of email to search for
     * @return list of users whose email contains the fragment
     */
    List<User> findUsersByEmailFragment(String emailFragment);

    /**
     * Returns users older than a given date (i.e. born before that date).
     *
     * @param date threshold date
     * @return list of users with birthdate before {@code date}
     */
    List<User> findUsersOlderThan(LocalDate date);
}