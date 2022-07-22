package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class VoteDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(VoteDto.class)
                .withNonnullFields("votes", "id", "name")
                .verify();
    }

}
