package com.prox.passgpt.service;

import reactor.core.publisher.Flux;

public interface GeminiApiService {
    Flux<byte[]> makeStreamRequest(String content) ;
}
