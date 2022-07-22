package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TrackDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(TrackDto.class)
                .withNonnullFields("id", "name")
                .verify();
    }

}
