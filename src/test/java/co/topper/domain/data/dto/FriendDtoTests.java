package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class FriendDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(FriendDto.class)
                .withNonnullFields("friendId", "username", "likedTracks")
                .verify();
    }

}
