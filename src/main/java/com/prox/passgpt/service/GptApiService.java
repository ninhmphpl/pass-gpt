package com.prox.passgpt.service;

import com.prox.passgpt.model.ModelChat;
import com.prox.passgpt.model.RequestBodyChat;
import com.prox.passgpt.model.ResponseChat;
import reactor.core.publisher.Flux;

public interface GptApiService {
    Flux<byte[]> makeStreamRequest(String content);
    Flux<ResponseChat> makeStreamRequest(RequestBodyChat request, ModelChat model);
}
