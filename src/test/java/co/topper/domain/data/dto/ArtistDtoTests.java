package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ArtistDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(ArtistDto.class)
                .verify();
    }

}
