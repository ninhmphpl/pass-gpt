package com.prox.passgpt.controller;

import com.prox.passgpt.model.TokenErrorDetail;
import com.prox.passgpt.service.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/checking")
public class CheckingApi {
    @Autowired
    private CheckingService checkingService;

    @GetMapping("/token")
    public ResponseEntity<?> getChecking(@RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(checkingService.getCheckingTokenCount(page, size));
    }

    @GetMapping("/error")
    public ResponseEntity<?> getCheckingError(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "10") int size){


        return ResponseEntity.ok(checkingService.getCheckingError(page, size));
    }

    @GetMapping("/error/detail")
    public ResponseEntity<TokenErrorDetail[]> getErrorMapDetail(){
        return ResponseEntity.ok(TokenErrorDetail.of(checkingService.getErrorMapDetail()));
    }
}
