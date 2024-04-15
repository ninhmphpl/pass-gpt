package com.prox.passgpt.service;

import com.prox.passgpt.service.errorchat.GeminiErrorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ErrorService {
    @Autowired
    private GeminiErrorService geminiErrorService;
    @Autowired
    private GeminiApiKeyService keyService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${url.webhook}")
    private String urlWebhook;

    @PostConstruct
    private void init() {
        geminiErrorService.error403(token403 -> {
                    keyService.deleteApiKey(token403);
                    restTemplate.postForLocation(urlWebhook, new BodyWebhook("Error 403: " + token403));
                }
        );
        geminiErrorService.error400(s ->
                restTemplate.postForLocation(urlWebhook, new BodyWebhook("Error 400: " + s))
        );
        geminiErrorService.error429(s ->
                restTemplate.postForLocation(urlWebhook, new BodyWebhook("Error 429: " + s))
        );
        geminiErrorService.error(System.out::println);
        geminiErrorService.errorTimeOut(() -> System.out.println("Error TimeOut"));
    }


    public static class BodyWebhook {
        public String content;

        public BodyWebhook(String content) {
            this.content = content;
        }
    }
}
