package pl.wsb.fitnesstracker.user.api;

/**
 * DTO containing only basic identification of a user (id and full name).
 * Used for endpoints that should not expose full user details.
 */
public record SimpleUserDto(Long id, String firstName, String lastName) {
}