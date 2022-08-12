package co.topper.domain.message;

import co.topper.configuration.RabbitConfiguration;
import co.topper.domain.message.data.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String routingKey;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate,
                           @Value("${exchange.name}") String exchange,
                           @Value("${routing.key.name}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void send(Message message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
