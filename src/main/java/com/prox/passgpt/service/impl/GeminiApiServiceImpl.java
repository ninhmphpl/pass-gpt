package com.prox.passgpt.service.impl;

import com.prox.passgpt.service.GeminiApiKeyService;
import com.prox.passgpt.service.GeminiApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class GeminiApiServiceImpl implements GeminiApiService {
    @Autowired
    private GeminiApiKeyService apiKeyService;

    @Override
    public Flux<byte[]> makeStreamRequest(String content) {
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:streamGenerateContent?alt=sse&key=" + apiKeyService.getApiKey();
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .build();
        return webClient.post()
                .body(BodyInserters.fromValue(makeRequest(content)))
                .retrieve()
                .bodyToFlux(byte[].class);
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
