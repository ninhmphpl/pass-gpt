package com.prox.passgpt.service.impl;

import com.prox.passgpt.service.GeminiApiKeyService;
import com.prox.passgpt.service.GeminiApiService;
import com.prox.passgpt.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class GeminiApiServiceImpl implements GeminiApiService {
    @Autowired
    private GeminiApiKeyService apiKeyService;
    @Value("${timeOut}")
    private long timeOut;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();

    @Override
    public Flux<byte[]> makeStreamRequest(String content) {
        return tryCall(content, apiKeyService.getApiKeys().size());
//                .timeout(java.time.Duration.ofSeconds(3), Flux.error(new RuntimeException("Timeout")));
    }

    private Flux<byte[]> callGeminiStream(String content) {
        return webClient.post()
                .uri("/v1/models/gemini-pro:streamGenerateContent?alt=sse&key=" + apiKeyService.getApiKey())
                .body(BodyInserters.fromValue(makeRequest(content)))
                .retrieve()
                .bodyToFlux(byte[].class);
    }

    private Flux<byte[]> tryCall(String content, int tryNumber) {
        AtomicBoolean timeOut = new AtomicBoolean(false);
        ThreadService.runAfterDelay(() -> timeOut.set(true), this.timeOut);
        return callGeminiStream(content)
                .onErrorResume(e -> tryNumber > 0 && !timeOut.get() ? tryCall(content, tryNumber - 1) : Flux.error(e));
    }

    private Request makeRequest(String content) {
        return new Request(List.of(new Content(List.of(new Part(content)))));
    }

    public record Part(String text) {
    }

    public record Content(List<Part> parts) {
    }

    public record Request(List<Content> contents) {
    }


}
