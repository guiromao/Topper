package co.topper.domain.service;

import co.topper.domain.data.converter.UserConverter;
import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.ResourceNotFoundException;
import co.topper.domain.message.MessageProducer;
import co.topper.domain.message.data.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTests {

    static final String USER_EMAIL_ID = "user_id@mail.com";
    static final String USER_NAME = "username";
    static final String PASSWORD = "password";
    static final Map<String, Long> TRACK_VOTES = Map.of("track-1", 1000L, "track-2", 2000L);

    final UserDto userDto = userDto();

    @Mock
    UserRepository userRepository;

    @Mock
    UserConverter userConverter;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    MessageProducer messageProducer;

    UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, userConverter,
                bCryptPasswordEncoder, messageProducer);

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encrypted-password");
    }

    @Test
    void testSaveNewUser() {
        when(userRepository.existsById(anyString())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity());
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto test = userService.saveUser(userDto);

        verify(userRepository, times(0)).findById(anyString());
        verify(messageProducer, times(1)).send(any(Message.class));
        Assertions.assertEquals(userDto, test);
    }

    @Test
    void testSaveExistingUser() {
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity()));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity());
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto test = userService.saveUser(userDto);

        Assertions.assertEquals(userDto, test);
        verifyNoInteractions(messageProducer);
    }

    @Test
    void testGetNonExistingUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUser("non-existing-user-id@mail.com")
        );
    }

    @Test
    void testGetExistingUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity()));
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto test = userService.getUser("existing-user-id@mail.com");

        Assertions.assertEquals(userDto, test);
    }

    private UserEntity userEntity() {
        return new UserEntity(
                USER_EMAIL_ID,
                USER_NAME,
                PASSWORD,
                Set.of("user-1", "user-2", "user-3"),
                Set.of("user-4"),
                TRACK_VOTES,
                5000L,
                Instant.now(),
                Set.of(Role.USER, Role.ADMIN)
        );
    }

    private UserDto userDto() {
        return new UserDto(
                USER_EMAIL_ID, USER_NAME, PASSWORD,
                TRACK_VOTES, 5000L,
                Instant.now()
        );
    }

}
