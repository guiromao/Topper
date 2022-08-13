package co.topper.domain.message.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MessageTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(Message.class)
                .withNonnullFields("emailId", "username", "registerDate")
                .verify();
    }

}
