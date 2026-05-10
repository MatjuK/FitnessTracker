package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.fitnesstracker.user.api.SimpleUserDto;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserEmailDto;
import pl.wsb.fitnesstracker.user.api.UserService;
import pl.wsb.fitnesstracker.user.api.UserProvider;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for user-related operations.
 * Exposes CRUD endpoints under {@code /v1/users}.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;
    private final UserProvider userProvider;
    private final UserMapper userMapper;

    /**
     * Returns full details of all users.
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userProvider.findAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Returns only id + first/last name of all users.
     */
    @GetMapping("/simple")
    public List<SimpleUserDto> getAllSimpleUsers() {
        return userProvider.findAllUsers().stream()
                .map(userMapper::toSimpleDto)
                .toList();
    }

    /**
     * Returns full details of a single user by id.
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userProvider.getUser(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with id %d not found".formatted(id)));
    }

    /**
     * Searches users by email fragment (case-insensitive).
     * Returns only id + email per match.
     */
    @GetMapping("/email")
    public List<UserEmailDto> getUsersByEmail(@RequestParam String email) {
        return userProvider.findUsersByEmailFragment(email).stream()
                .map(userMapper::toEmailDto)
                .toList();
    }

    /**
     * Returns users older than the given date (born before {@code time}).
     */
    @GetMapping("/older/{time}")
    public List<UserDto> getUsersOlderThan(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time) {
        return userProvider.findUsersOlderThan(time).stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Creates a new user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        var saved = userService.createUser(userMapper.toEntity(userDto));
        return userMapper.toDto(saved);
    }

    /**
     * Updates an existing user.
     */
    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        var updated = userService.updateUser(userId, userMapper.toEntity(userDto));
        return userMapper.toDto(updated);
    }

    /**
     * Deletes a user by id.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}