package com.prox.passgpt.service;

import com.prox.passgpt.model.CheckingError;
import com.prox.passgpt.model.CheckingTokenCount;

import java.util.List;
import java.util.Map;

public interface CheckingService {
    List<CheckingTokenCount> getCheckingTokenCount(int page, int size);
    List<CheckingError> getCheckingError(int page, int size);
    Map<String, Map<String, Integer>> getErrorMapDetail();
}
