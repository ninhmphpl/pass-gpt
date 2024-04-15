package com.prox.passgpt.model;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
public class RequestGemini{
    public List<Content> contents;

    public static RequestGemini makeRequest(String content) {
        return new RequestGemini(List.of(new Content(List.of(new Part(content)))));
    }


    public record Part(String text) {
    }

    public record Content(List<Part> parts) {
    }
}
