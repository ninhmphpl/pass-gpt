package com.prox.passgpt.controller;

import com.prox.passgpt.service.GeminiApiService;
import com.prox.passgpt.service.SecurityService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/gemini")
public class GeminiApi {
    @Autowired
    private GeminiApiService apiService;
    @Autowired
    private SecurityService securityService;
    @Value("${password}")
    private String password;


    @PostMapping
    public ResponseEntity<Flux<byte[]>> callApiGptStream(@RequestBody Request request,
                                                         @RequestHeader("Key") Optional<String> key,
                                                         @RequestHeader("TimeStamps") Optional<Long> timeStamps
    ) {
        securityService.parseKey(
                timeStamps.orElseThrow(() -> new JwtException("TimeStamps header required")),
                key.orElseThrow(() -> new JwtException("Key header required")));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(apiService.makeStreamRequest(request.content));
    }

    @GetMapping
    public ResponseEntity<Flux<byte[]>> callApiGptStream(@RequestParam("content") String content,
                                                         @RequestHeader("Key") Optional<String> key,
                                                         @RequestHeader("TimeStamps") Optional<Long> timeStamps) {
        securityService.parseKey(
                timeStamps.orElseThrow(() -> new JwtException("TimeStamps header required")),
                key.orElseThrow(() -> new JwtException("Key header required")));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(apiService.makeStreamRequest(content));
    }

    @GetMapping("/token")
    public ResponseEntity<GptApiV2Controller.Data> getToken(@RequestHeader("Authorization") String pass) {
        if (!pass.equals(password)) throw new JwtException("Password wrong");
        long timeStamps = System.currentTimeMillis();
        String key = securityService.getKey(timeStamps);
        return ResponseEntity.ok(new GptApiV2Controller.Data(key, timeStamps));
    }
    public record Request(String content){}



}
