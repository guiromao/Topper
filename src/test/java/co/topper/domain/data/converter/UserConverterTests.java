package co.topper.domain.data.converter;

import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class UserConverterTests {

    private static final Instant NOW = Instant.now();

    @Autowired
    UserConverter userConverter;

    @Test
    void testConvertUserLastVoteToday() {
        UserEntity userVotedToday = userWithLastVoteAt(NOW);

        UserDto test = userConverter.toDto(userVotedToday);

        Assertions.assertEquals("username@mail.com", test.getEmailId());
        Assertions.assertEquals("username", test.getUsername());
        Assertions.assertNull(test.getPassword());
        Assertions.assertEquals(Map.of("artist-1", 800L), test.getTrackVotes());
        Assertions.assertEquals(1000L, test.getAvailableVotes());
        Assertions.assertEquals(NOW, test.getLastVoteDate());
    }

    @Test
    void testConvertUserLastVoteBeforeToday() {
        Instant beforeDate = NOW.minus(2, ChronoUnit.DAYS);
        UserEntity user = userWithLastVoteAt(beforeDate);

        UserDto test = userConverter.toDto(user);

        Assertions.assertEquals("username@mail.com", test.getEmailId());
        Assertions.assertEquals("username", test.getUsername());
        Assertions.assertNull(test.getPassword());
        Assertions.assertEquals(Map.of("artist-1", 800L), test.getTrackVotes());
        Assertions.assertEquals(3000L, test.getAvailableVotes());
        Assertions.assertEquals(beforeDate, test.getLastVoteDate());
    }

    private UserEntity userWithLastVoteAt(Instant instant) {
        return new UserEntity(
            "username@mail.com",
            "username",
            "pass",
            Set.of("friendId"),
            Set.of("requestId-1", "requestId-2"),
            Map.of("artist-1", 800L),
            1000L,
            instant,
            Set.of(Role.USER)
        );
    }

}
