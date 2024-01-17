package com.prox.passgpt.service;

import java.io.IOException;
import java.util.List;

public interface ApiKeyService {
    List<String> saveApiKey(List <String> apiKeys) throws IOException;
    List<String> getApiKeys();
    String getApiKey();
}
