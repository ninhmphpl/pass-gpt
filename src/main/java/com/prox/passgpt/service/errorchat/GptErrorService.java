package com.prox.passgpt.service.errorchat;

import java.util.function.Consumer;

public interface GptErrorService {
    void error403(Consumer<String> token403);
    void error400(Consumer<String> token404);
    void error429(Consumer<String> token404);
    void error(Consumer<String> token404);
    void errorTimeOut(Runnable errorTimeOut);
}
