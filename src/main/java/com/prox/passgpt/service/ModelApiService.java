package com.prox.passgpt.service;

import com.prox.passgpt.model.ModelChat;

import java.util.Map;

public interface ModelApiService {
    Map<ModelChat, Integer> getModelPercent();
    Map<ModelChat, Integer> setModelPercent(Map<ModelChat, Integer> modelPercent);
}
