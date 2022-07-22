package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SuccessVoteDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(SuccessVoteDto.class)
                .withNonnullFields("trackId", "trackName", "trackVotes")
                .verify();
    }

}
