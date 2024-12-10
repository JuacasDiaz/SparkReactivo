package application.usecase;


import domain.model.JsonMessage;
import infrastructure.adapter.processing.SparkProcessor;
import infrastructure.adapter.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProcessJsonUseCase {

    private final SparkProcessor sparkProcessor;
    private final RedisRepository redisRepository;
    private final WebClient webClient;

    // Método existente: Procesa y almacena JSON desde URLs
    public Mono<Void> processAndStoreFromUrls() {
        // URLs de los JSON
        String url1 = "https://jsonplaceholder.typicode.com/todos/2";
        
        String url2 = "https://jsonplaceholder.typicode.com/todos/1";

        Mono<JsonMessage> json1 = webClient.get().uri(url1).retrieve()
                .bodyToMono(String.class)
                .map(content -> new JsonMessage("1", content));

        Mono<JsonMessage> json2 = webClient.get().uri(url2).retrieve()
                .bodyToMono(String.class)
                .map(content -> new JsonMessage("2", content));

        return Mono.zip(json1, json2)
                .flatMap(tuple -> sparkProcessor.processJson(tuple.getT1(), tuple.getT2()))
                .flatMap(result -> redisRepository.saveData("processed:result", result))
                .then();
    }

    // Nuevo método: Procesa y almacena JSON proporcionado directamente
    public Mono<Void> processAndStore(JsonMessage json1, JsonMessage json2) {
        return sparkProcessor.processJson(json1, json2) // Llama a Spark para procesar los JSON
                .flatMap(result -> redisRepository.saveData("processed:result", result)) // Guarda en Redis
                .then();
    }
}
