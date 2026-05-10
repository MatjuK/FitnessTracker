package pl.wsb.fitnesstracker.user.api;

/**
 * Service interface for write operations on User entities (commands).
 * Read operations live in {@link UserProvider}.
 */
public interface UserService {

    /**
     * Creates a new user in the system.
     *
     * @param user user to create (without DB id)
     * @return persisted user with generated id
     */
    User createUser(User user);

    /**
     * Updates an existing user in the system.
     *
     * @param userId id of the user to update
     * @param user   user data with new values
     * @return updated user
     */
    User updateUser(Long userId, User user);

    /**
     * Deletes a user with the given id.
     *
     * @param userId id of the user to delete
     */
    void deleteUser(Long userId);
}