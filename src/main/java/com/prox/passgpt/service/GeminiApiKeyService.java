package com.prox.passgpt.service;

import java.io.IOException;
import java.util.List;

public interface GeminiApiKeyService {
    List<String> saveApiKey(List <String> apiKeys) throws IOException;
    List<String> getApiKeys();
    String getApiKey();
    void deleteApiKey(String apiKey);
}
