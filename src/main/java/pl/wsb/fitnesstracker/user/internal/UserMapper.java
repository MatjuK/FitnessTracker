package pl.wsb.fitnesstracker.user.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.user.api.SimpleUserDto;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserEmailDto;

/**
 * Maps {@link User} entity to/from various DTOs.
 */
@Component
class UserMapper {

    /**
     * Converts {@link User} entity to full {@link UserDto}.
     *
     * @param user user entity
     * @return DTO with all user fields
     */
    UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate(),
                user.getEmail()
        );
    }

    /**
     * Converts {@link UserDto} to {@link User} entity (without ID — for creation).
     *
     * @param userDto incoming DTO
     * @return new {@link User} entity ready to be persisted
     */
    User toEntity(UserDto userDto) {
        return new User(
                userDto.firstName(),
                userDto.lastName(),
                userDto.birthdate(),
                userDto.email()
        );
    }

    /**
     * Converts {@link User} to lightweight {@link SimpleUserDto} (id + names only).
     */
    SimpleUserDto toSimpleDto(User user) {
        return new SimpleUserDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    /**
     * Converts {@link User} to {@link UserEmailDto} (id + email only).
     */
    UserEmailDto toEmailDto(User user) {
        return new UserEmailDto(user.getId(), user.getEmail());
    }
}