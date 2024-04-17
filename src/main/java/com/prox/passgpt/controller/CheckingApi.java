package com.prox.passgpt.controller;

import com.prox.passgpt.service.CheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/checking")
public class CheckingApi {
    @Autowired
    private CheckingService checkingService;

    @GetMapping("/token")
    public ResponseEntity<?> getChecking(){
        return ResponseEntity.ok(checkingService.getCheckingTokenCount());
    }

    @GetMapping("/error")
    public ResponseEntity<?> getCheckingError(){
        return ResponseEntity.ok(checkingService.getCheckingError());
    }

    @GetMapping("/error/detail")
    public ResponseEntity<?> getErrorMapDetail(){
        return ResponseEntity.ok(checkingService.getErrorMapDetail());
    }
}
