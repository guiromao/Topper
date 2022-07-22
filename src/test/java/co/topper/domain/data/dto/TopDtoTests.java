package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TopDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(TopDto.class)
                .withNonnullFields("id", "name", "totalVotes")
                .verify();
    }

}
