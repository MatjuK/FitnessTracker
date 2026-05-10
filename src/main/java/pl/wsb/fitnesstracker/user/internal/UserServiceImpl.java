package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    // === UserService (write) ===

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(final Long userId, final User user) {
        log.info("Updating User with id={} -> {}", userId, user);
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setBirthdate(user.getBirthdate());
        existing.setEmail(user.getEmail());

        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(final Long userId) {
        log.info("Deleting User with id={}", userId);
        userRepository.deleteById(userId);
    }

    // === UserProvider (read) ===

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUsersByEmailFragment(final String emailFragment) {
        return userRepository.findByEmailContainingIgnoreCase(emailFragment);
    }

    @Override
    public List<User> findUsersOlderThan(final LocalDate date) {
        return userRepository.findByBirthdateBefore(date);
    }
}