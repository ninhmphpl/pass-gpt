package com.prox.passgpt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.lang.NonNull;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFluxGlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Class<? extends Throwable>, Integer> exceptionHttpStatusMap = new HashMap<>();

    public AbstractFluxGlobalExceptionHandler() {
        this.addException(this.exceptionHttpStatusMap);
    }

    public @NonNull Mono<Void> handle(@NonNull  ServerWebExchange exchange,@NonNull Throwable ex) {
        try {
            Integer statusCode = this.exceptionHttpStatusMap.get(ex.getClass());
            if (statusCode == null) {
                statusCode = 500;
            }
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(statusCode));
            String body = this.objectMapper.writeValueAsString(this.makeBodyString(ex));
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes())));
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }

    protected abstract Object makeBodyString(Throwable ex);

    protected abstract void addException(Map<Class<? extends Throwable>, Integer> exceptionHttpStatusMap);
}