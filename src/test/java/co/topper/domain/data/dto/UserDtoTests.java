package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UserDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(UserDto.class)
                .withNonnullFields("username", "password", "emailId",
                        "availableVotes", "lastVoteDate")
                .verify();
    }

}
