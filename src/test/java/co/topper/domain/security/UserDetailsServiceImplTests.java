package co.topper.domain.security;

import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.UserEmailNotFoundException;
import co.topper.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
class UserDetailsServiceImplTests {

    @Mock
    UserRepository userRepository;

    UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setup() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void testUnknownEmailId() {
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                UserEmailNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown-email-id@mail.com")
        );
    }

    @Test
    void testLoadByUsername() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userExample()));

        UserDetails test = userDetailsService.loadUserByUsername("mail@email.com");

        Assertions.assertNotNull(test);
        Assertions.assertEquals("mail@email.com", test.getUsername());
        Assertions.assertEquals("encrypted-password", test.getPassword());
        Assertions.assertEquals(Set.of(Role.USER, Role.ADMIN), test.getAuthorities());
    }

    private UserEntity userExample() {
        return new UserEntity(
                "mail@email.com",
                "username",
                "encrypted-password",
                Set.of("1", "2", "3"),
                Set.of("4", "5", "6", "7"),
                Map.of("track-1", 5000L, "track-2", 10000L),
                3000L,
                Instant.now().minus(3, ChronoUnit.HOURS),
                Set.of(Role.USER, Role.ADMIN)
        );
    }

}
