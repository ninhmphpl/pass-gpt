package com.prox.passgpt.service.impl;

import com.prox.passgpt.model.FormRequest;
import com.prox.passgpt.service.ApiKeyService;
import com.prox.passgpt.service.GptApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


@Service
public class GptApiServiceImpl implements GptApiService {
    @Value("${gpt.url}")
    private String url;
    @Value("${gpt.model}")
    private String model;
    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    public Flux<byte[]> makeStreamRequest(String content) {
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .build();
        return webClient.post()
                .header("Authorization", "Bearer " + apiKeyService.getApiKey())
                .body(BodyInserters.fromValue(FormRequest.createMessageStream(content, model)))
                .retrieve()
                .bodyToFlux(byte[].class);
    }




}
