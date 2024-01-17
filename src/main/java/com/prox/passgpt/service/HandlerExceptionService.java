package com.prox.passgpt.service;

import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerExceptionService {
    public record ErrorResponse(String detail){}
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> jwtException(JwtException e){
        return ResponseEntity.status(403).body(new ErrorResponse(e.getMessage()));
    }
}
