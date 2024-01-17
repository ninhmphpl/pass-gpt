package com.prox.passgpt.service;

import io.jsonwebtoken.JwtException;

public interface SecurityService {
    String getToken();
    void parseToken(String token) throws JwtException;
    String getKey(long timeStamps);
    void parseKey(long timeStamps, String key) throws JwtException;
}
