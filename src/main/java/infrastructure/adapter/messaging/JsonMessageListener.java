package infrastructure.adapter.messaging;

import application.usecase.ProcessJsonUseCase;
import domain.model.JsonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class JsonMessageListener {

    private final ProcessJsonUseCase processJsonUseCase;

    @RabbitListener(queues = "json-queue")
    public Mono<Void> onMessage(String message) {
        JsonMessage jsonMessage = new JsonMessage("1", message);
        JsonMessage secondMessage = new JsonMessage("2", "{}");

        return processJsonUseCase.processAndStore(jsonMessage, secondMessage);
    }
}