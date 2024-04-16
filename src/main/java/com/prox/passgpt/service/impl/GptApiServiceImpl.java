package com.prox.passgpt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prox.passgpt.model.*;
import com.prox.passgpt.service.ApiKeyService;
import com.prox.passgpt.service.GptApiService;
import com.prox.passgpt.service.ThreadService;
import com.prox.passgpt.service.TokenGptService;
import com.prox.passgpt.service.errorchat.GptErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


@Service
public class GptApiServiceImpl implements GptApiService, GptErrorService {
    @Value("${gpt.url}")
    private String url;
    @Value("${gpt.model}")
    private String model;
    @Value("${timeOut}")
    private long timeOut;
    @Autowired
    private ApiKeyService apiKeyService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenGptService tokenGptService;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com")
            .build();

    @Override
    public Flux<byte[]> makeStreamRequest(String content) {

        return tryCall(content, apiKeyService.getApiKeys().size());
    }

    @Override
    public Flux<ResponseChat> makeStreamRequest(RequestBodyChat request, ModelChat model) {
        return tryCall(request, model, apiKeyService.getApiKeys().size());
    }

    public Flux<ResponseChat> callStreamRequest(RequestBodyChat request, ModelChat model) {
        String question = request.getQuestion();
        StringBuilder answare = new StringBuilder();
        AtomicBoolean set = new AtomicBoolean(true);
        return webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKeyService.getApiKey())
                .body(BodyInserters.fromValue(RequestGpt.createMessageStream(request, model)))
                .retrieve()
                .bodyToFlux(String.class)
                .map(s -> {
                    ResponseChat responseChat = parseResponse(s);
                    if (responseChat.status == ResponseChat.Status.stream) {
                        answare.append(responseChat.content);
                    } else {
                        if (set.get()) {
                            if (model == ModelChat.gpt35) {
                                tokenGptService.questionToken35(tokenGptService.calculateToken(question));
                                tokenGptService.answerToken35(tokenGptService.calculateToken(answare.toString()));
                            } else if (model == ModelChat.gpt35_shot) {
                                tokenGptService.questionToken35Short(tokenGptService.calculateToken(question));
                                tokenGptService.answerToken35Short(tokenGptService.calculateToken(answare.toString()));
                            } else if (model == ModelChat.gpt35_100word) {
                                tokenGptService.questionToken35Word100(tokenGptService.calculateToken(question));
                                tokenGptService.answerToken35Word100(tokenGptService.calculateToken(answare.toString()));
                            }
                        }
                        set.set(false);
                    }
                    return responseChat;
                });
    }

    private ResponseChat parseResponse(String response) {
        try {
            ResponseGptStream responseGptStream;
            if (response == null || response.isEmpty()) {
                return new ResponseChat(ResponseChat.Status.stream, "");
            } else if (response.equals("[DONE]")) {
                return new ResponseChat(ResponseChat.Status.stop, "");
            } else if ((responseGptStream = parseResponseStream(response)) != null) {
                if (!responseGptStream.choices.isEmpty()) {
                    if (responseGptStream.choices.get(0).finish_reason != null && responseGptStream.choices.get(0).finish_reason.equals("stop")) {
                        return new ResponseChat(ResponseChat.Status.stop, "");
                    } else if (responseGptStream.choices.get(0).delta != null && responseGptStream.choices.get(0).delta.content != null) {
                        return new ResponseChat(ResponseChat.Status.stream, responseGptStream.choices.get(0).delta.content);
                    } else {
                        return new ResponseChat(ResponseChat.Status.stream, "");
                    }
                }
            }
            return new ResponseChat(ResponseChat.Status.stream, "");
        } catch (Exception e) {
            return new ResponseChat(ResponseChat.Status.stream, "[ERROR] : " + e.getMessage());
        }
    }

    private ResponseGptStream parseResponseStream(String response) {
        try {
            return objectMapper.readValue(response, ResponseGptStream.class);
        } catch (Exception e) {
            return null;
        }
    }


    private Flux<byte[]> callGptStream(String content) {
        return webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKeyService.getApiKey())
                .body(BodyInserters.fromValue(RequestGpt.createMessageStream(content, model)))
                .retrieve()
                .bodyToFlux(byte[].class)
                .onErrorResume(throwable -> Flux.error(new RuntimeException("Error call Gemini", throwable)));
    }

    private Flux<byte[]> tryCall(String content, int tryNumber) {
        AtomicBoolean timeOut = new AtomicBoolean(false);
        ThreadService.runAfterDelay(() -> timeOut.set(true), this.timeOut);
        return callGptStream(content)
                .onErrorResume(e -> tryNumber > 0 && !timeOut.get() ? tryCall(content, tryNumber - 1) : Flux.error(e));
    }

    private Flux<ResponseChat> tryCall(RequestBodyChat request, ModelChat model, int tryNumber) {
        AtomicBoolean timeOut = new AtomicBoolean(false);
        ThreadService.runAfterDelay(() -> timeOut.set(true), this.timeOut);
        return callStreamRequest(request, model)
                .onErrorResume(e -> tryNumber > 0 && !timeOut.get() ? tryCall(request, model, tryNumber - 1) : Flux.error(e));
    }


    // =========================== Error ===========================
    @Override
    public void error403(Consumer<String> token403) {

    }

    @Override
    public void error400(Consumer<String> token404) {

    }

    @Override
    public void error429(Consumer<String> token404) {

    }

    @Override
    public void error(Consumer<String> token404) {

    }

    @Override
    public void errorTimeOut(Runnable errorTimeOut) {

    }
}
