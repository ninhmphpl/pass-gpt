package com.prox.passgpt.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ResponseChat {
    public Status status;
    public String content;

    public enum Status {
        stream, stop
    }
}
