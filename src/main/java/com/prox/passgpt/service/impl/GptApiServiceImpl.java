package com.prox.passgpt.service.impl;

import com.prox.passgpt.model.FormRequest;
import com.prox.passgpt.service.ApiKeyService;
import com.prox.passgpt.service.GptApiService;
import com.prox.passgpt.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicBoolean;


@Service
public class GptApiServiceImpl implements GptApiService {
    @Value("${gpt.url}")
    private String url;
    @Value("${gpt.model}")
    private String model;
    @Value("${timeOut}")
    private long timeOut;
    @Autowired
    private ApiKeyService apiKeyService;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com")
            .build();

    @Override
    public Flux<byte[]> makeStreamRequest(String content) {

        return tryCall(content, apiKeyService.getApiKeys().size());
    }

    private Flux<byte[]> callGeminiStream(String content) {
        return webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKeyService.getApiKey())
                .body(BodyInserters.fromValue(FormRequest.createMessageStream(content, model)))
                .retrieve()
                .bodyToFlux(byte[].class);
    }

    private Flux<byte[]> tryCall(String content, int tryNumber) {
        AtomicBoolean timeOut = new AtomicBoolean(false);
        ThreadService.runAfterDelay(() -> timeOut.set(true), this.timeOut);
        return callGeminiStream(content)
                .onErrorResume(e -> tryNumber > 0 && !timeOut.get() ? tryCall(content, tryNumber - 1) : Flux.error(e));
    }


}
