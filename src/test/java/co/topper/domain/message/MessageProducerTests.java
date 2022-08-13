package co.topper.domain.message;

import co.topper.domain.message.data.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class MessageProducerTests {

    @Mock
    RabbitTemplate rabbitTemplate;

    MessageProducer messageProducer;

    @BeforeEach
    void setup() {
        messageProducer = new MessageProducer(rabbitTemplate,
                "exchange-name", "routing-key");
    }

    @Test
    void testSendMessage() {
        Message message = new Message("emailId", "username", Instant.now());

        messageProducer.send(message);

        Mockito.verify(rabbitTemplate, Mockito.times(1))
                .convertAndSend(anyString(), anyString(), any(Message.class));
    }

}
