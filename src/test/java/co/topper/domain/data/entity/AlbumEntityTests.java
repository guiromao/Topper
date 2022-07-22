package co.topper.domain.data.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AlbumEntityTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(AlbumEntity.class)
                .verify();
    }

}
