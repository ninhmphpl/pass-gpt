package com.prox.passgpt.service;

import com.prox.passgpt.service.impl.AbstractFluxGlobalExceptionHandler;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
public class FluxGlobalExceptionHandler extends AbstractFluxGlobalExceptionHandler {

    @Override
    protected Object makeBodyString(Throwable ex) {
        return new ErrorBody(ex.getMessage());
    }

    @Override
    protected void addException(Map<Class<? extends Throwable>, Integer> exceptionHttpStatusMap) {
        exceptionHttpStatusMap.put(NullPointerException.class, 400);
        exceptionHttpStatusMap.put(IllegalArgumentException.class, 400);
        exceptionHttpStatusMap.put(ArithmeticException.class, 500);
        exceptionHttpStatusMap.put(ArrayIndexOutOfBoundsException.class, 400);
        exceptionHttpStatusMap.put(UnsupportedOperationException.class, 501);
        exceptionHttpStatusMap.put(ClassCastException.class, 400);
        exceptionHttpStatusMap.put(RuntimeException.class, 500);
        exceptionHttpStatusMap.put(JwtException.class, 403);
        exceptionHttpStatusMap.put(TooManyRequest.class, 429);
        exceptionHttpStatusMap.put(SignatureException.class, 403);
    }

    public static class TooManyRequest extends Throwable{
        public TooManyRequest(String message) {
            super(message);
        }
    }
    public  record ErrorBody(String detail){}

}
