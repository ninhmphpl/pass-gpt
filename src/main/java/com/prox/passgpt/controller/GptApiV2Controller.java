package com.prox.passgpt.controller;

import com.prox.passgpt.service.GptApiService;
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
@RequestMapping("/api/v2/gpt")
public class GptApiV2Controller {

    @Autowired
    private GptApiService apiService;

    @Autowired
    private SecurityService securityService;
    @Value("${password}")
    private String password;

    public record DataBody(String content){}
    @PostMapping
    public ResponseEntity<Flux<byte[]>> callApiGptStream(@RequestBody DataBody dataBody,
                                                         @RequestHeader("Key") Optional<String> key,
                                                         @RequestHeader("TimeStamps") Optional<Long> timeStamps
    ){
        securityService.parseKey(
                timeStamps.orElseThrow(() -> new JwtException("TimeStamps header required")),
                key.orElseThrow(() -> new JwtException("Key header required")));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(apiService.makeStreamRequest(dataBody.content));
    }
    @GetMapping
    public ResponseEntity<Flux<byte[]>> callApiGptStream(@RequestParam("content") String content,
                                                         @RequestHeader("Key") Optional<String> key,
                                                         @RequestHeader("TimeStamps") Optional<Long> timeStamps){
        securityService.parseKey(
                timeStamps.orElseThrow(() -> new JwtException("TimeStamps header required")),
                key.orElseThrow(() -> new JwtException("Key header required")));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(apiService.makeStreamRequest(content));
    }

    public record Data(String key, long timeStamps){}
    @GetMapping("/token")
    public ResponseEntity<Data> getToken(@RequestHeader("Authorization") String pass){
        if(!pass.equals(password)) throw new JwtException("Password wrong");
        long timeStamps = System.currentTimeMillis();
        String key = securityService.getKey(timeStamps);
        return ResponseEntity.ok(new Data(key, timeStamps));
    }



}