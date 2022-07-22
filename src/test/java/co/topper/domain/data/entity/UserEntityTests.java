package co.topper.domain.data.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

class UserEntityTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(UserEntity.class)
                .withNonnullFields("id", "username", "password",
                        "email", "friendsListIds", "requestsReceivedIds",
                        "trackVotes", "availableVotes", "lastVoteAttempt")
                .verify();
    }

    @Test
    void testCreateUser() {
        UserEntity test = UserEntity.create("username", "password", "user@mail.com");

        Assertions.assertNotNull(UUID.fromString(test.getId()));
        Assertions.assertTrue(CollectionUtils.isEmpty(test.getFriendsListIds()));
        Assertions.assertTrue(CollectionUtils.isEmpty(test.getRequestsReceivedIds()));
        Assertions.assertTrue(CollectionUtils.isEmpty(test.getTrackVotes()));
        Assertions.assertEquals(1000L, test.getAvailableVotes());
        Assertions.assertTrue(test.getLastVoteAttempt().isBefore(Instant.now().plus(1, ChronoUnit.MINUTES)));
        Assertions.assertEquals(Set.of(Role.USER), test.getRoles());
    }

}
