package com.prox.passgpt.controller;

import com.prox.passgpt.service.ApiKeyService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/apikey")
public class ApiKeysController {
    @Autowired
    private ApiKeyService apiKeyService;
    @Value("${password}")
    private String password;

    @GetMapping
    public ResponseEntity<?> getListKey(@RequestHeader("Authorization") String pass) {
        if (!pass.equals(password)) throw new JwtException("Password wrong");
        return ResponseEntity.ok(apiKeyService.getApiKeys());
    }

    @PostMapping
    public ResponseEntity<?> setListKey(@RequestHeader("Authorization") String pass,
                                        @RequestBody List<String> apiKeys) throws IOException {
        if (!pass.equals(password)) throw new JwtException("Password wrong");
        return ResponseEntity.ok(apiKeyService.saveApiKey(apiKeys));
    }

}
