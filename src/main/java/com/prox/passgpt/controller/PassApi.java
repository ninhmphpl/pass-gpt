package com.prox.passgpt.controller;

import com.prox.passgpt.model.RequestBodyChat;
import com.prox.passgpt.model.ResponseChat;
import com.prox.passgpt.service.ChatService;
import com.prox.passgpt.service.SecurityService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/pass")
public class PassApi {

    @Autowired
    private ChatService chatService;
    @Autowired
    private SecurityService securityService;
    @PostMapping
    public ResponseEntity<Flux<ResponseChat>> callApiGptStream(@RequestBody RequestBodyChat request,
                                                               @RequestHeader("Key") Optional<String> key,
                                                               @RequestHeader("TimeStamps") Optional<Long> timeStamps
    ) {
        securityService.parseKey(
                timeStamps.orElseThrow(() -> new JwtException("TimeStamps header required")),
                key.orElseThrow(() -> new JwtException("Key header required")));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(chatService.callApi(request));
    }
}
