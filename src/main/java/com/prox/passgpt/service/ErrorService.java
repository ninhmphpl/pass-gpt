package com.prox.passgpt.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {
    @Autowired
    private GeminiErrorService geminiErrorService;


    @PostConstruct
    private void init() {
        geminiErrorService.error403(token403 -> {
            System.out.println("Error 403: " + token403);
        });
    }
}
