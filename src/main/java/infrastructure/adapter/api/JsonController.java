package infrastructure.adapter.api;


import application.usecase.ProcessJsonUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/json")
@RequiredArgsConstructor
public class JsonController {

    private final ProcessJsonUseCase processJsonUseCase;

    @PostMapping("/processFromUrls")
    public Mono<ResponseEntity<String>> processJsonFromUrls() {
        return processJsonUseCase.processAndStoreFromUrls()
                .then(Mono.just(ResponseEntity.ok("Processing completed from URLs!")))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).body("Error: " + error.getMessage())));
    }
}