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
@RequestMapping("/api/v1/gpt")
public class GptApiController {

    @Autowired
    private GptApiService gptApiService;

    @Autowired
    private SecurityService securityService;
    @Value("${password}")
    private String password;

    public record DataBody(String content){}
    @PostMapping
    public ResponseEntity<Flux<byte[]>> callApiGptStream(@RequestBody DataBody dataBody, @RequestHeader("Authorization") Optional<String> token){
        securityService.parseToken(token.orElseThrow(() -> new JwtException("Token required")));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(gptApiService.makeStreamRequest(dataBody.content));
    }
    @GetMapping
    public ResponseEntity<Flux<byte[]>> callApiGptStream(@RequestParam("content") String content, @RequestHeader("Authorization") Optional<String> token){
        securityService.parseToken(token.orElseThrow(() -> new JwtException("Token required")));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(gptApiService.makeStreamRequest(content));
    }

    public record Data(String data){}
    @GetMapping("/token")
    public ResponseEntity<Data> getToken(@RequestHeader("Authorization") String pass){
        if(!pass.equals(password)) throw new JwtException("Password wrong");
        return ResponseEntity.ok(new Data(securityService.getToken()));
    }



}