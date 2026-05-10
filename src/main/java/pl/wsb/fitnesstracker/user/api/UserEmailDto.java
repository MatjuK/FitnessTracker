package pl.wsb.fitnesstracker.user.api;

/**
 * DTO containing only id and email of a user.
 * Used for email-fragment search endpoint.
 */
public record UserEmailDto(Long id, String email) {
}