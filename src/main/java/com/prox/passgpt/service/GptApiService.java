package com.prox.passgpt.service;

import reactor.core.publisher.Flux;

public interface GptApiService {
    Flux<byte[]> makeStreamRequest(String content);
}
