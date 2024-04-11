package com.prox.passgpt.service.impl;

import com.prox.passgpt.service.GeminiApiKeyService;
import com.prox.passgpt.service.GeminiApiService;
import com.prox.passgpt.service.GeminiErrorService;
import com.prox.passgpt.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Service
public class GeminiApiServiceImpl implements GeminiApiService, GeminiErrorService {
    @Autowired
    private GeminiApiKeyService apiKeyService;
    @Value("${timeOut}")
    private long timeOut;
    private Consumer<String> token403;
    private Consumer<String> token400;
    private Consumer<String> token429;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();

    @Override
    public Flux<byte[]> makeStreamRequest(String content) {
        return tryCall(content, apiKeyService.getApiKeys().size());
//                .timeout(java.time.Duration.ofSeconds(3), Flux.error(new RuntimeException("Timeout")));
    }

    private Flux<byte[]> callGeminiStream(String content, String apiKey) {
        return webClient.post()
                .uri("/v1/models/gemini-pro:streamGenerateContent?alt=sse&key=" + apiKey)
                .body(BodyInserters.fromValue(makeRequest(content)))
                .retrieve()
                .bodyToFlux(byte[].class);
    }

    private Flux<byte[]> tryCall(String content, int tryNumber) {
        AtomicBoolean timeOut = new AtomicBoolean(false);
        ThreadService.runAfterDelay(() -> timeOut.set(true), this.timeOut);
        String apiKey;
        return callGeminiStream(content, apiKey = apiKeyService.getApiKey())
                .onErrorResume(e -> {
                    if (e.getMessage().contains("403") && token403 != null) token403.accept(apiKey);
                    else if (e.getMessage().contains("400") && token400 != null) token400.accept(apiKey);
                    else if (e.getMessage().contains("429") && token429 != null) token429.accept(apiKey);
                    return tryNumber > 0 && !timeOut.get() ? tryCall(content, tryNumber - 1) : Flux.error(e);
                });
    }

    private Request makeRequest(String content) {
        return new Request(List.of(new Content(List.of(new Part(content)))));
    }

    @Override
    public void error403(Consumer<String> token403) {
        this.token403 = token403;
    }

    @Override
    public void error400(Consumer<String> token400) {
        this.token400 = token400;
    }

    @Override
    public void error429(Consumer<String> token429) {
        this.token429 = token429;
    }

    public record Part(String text) {
    }

    public record Content(List<Part> parts) {
    }

    public record Request(List<Content> contents) {
    }


}
