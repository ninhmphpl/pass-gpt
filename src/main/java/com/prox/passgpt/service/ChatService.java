package com.prox.passgpt.service;

import com.prox.passgpt.model.RequestBodyChat;
import com.prox.passgpt.model.ResponseChat;
import reactor.core.publisher.Flux;

public interface ChatService {
    Flux<ResponseChat> callApi(RequestBodyChat requestBodyChat);
}
