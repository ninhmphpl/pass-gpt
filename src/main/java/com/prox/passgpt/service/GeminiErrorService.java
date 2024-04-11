package com.prox.passgpt.service;

import java.util.function.Consumer;

public interface GeminiErrorService {
    void error403(Consumer<String> token403);
    void error400(Consumer<String> token404);
    void error429(Consumer<String> token404);
}
