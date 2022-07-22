package co.topper.domain.data.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AlbumDtoTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(AlbumDto.class)
                .withNonnullFields("id", "name")
                .verify();
    }

}
