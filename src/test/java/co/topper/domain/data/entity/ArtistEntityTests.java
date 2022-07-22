package co.topper.domain.data.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ArtistEntityTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(ArtistEntity.class)
                .withNonnullFields("id", "name")
                .verify();
    }

}
