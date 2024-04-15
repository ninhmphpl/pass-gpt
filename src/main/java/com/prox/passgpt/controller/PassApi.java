package com.prox.passgpt.controller;

import com.prox.passgpt.model.RequestBodyChat;
import com.prox.passgpt.model.ResponseChat;
import com.prox.passgpt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/pass")
public class PassApi {

    @Autowired
    private ChatService chatService;
    @PostMapping
    public ResponseEntity<Flux<ResponseChat>> callApiGptStream(@RequestBody RequestBodyChat request){
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(chatService.callApi(request));
    }
}
