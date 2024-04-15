package com.prox.passgpt.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
public class RequestGeminiV2 {
    public List<Content> contents;

    public static RequestGeminiV2 makeRequest(RequestBodyChat bodyChat) {
        List<MessageContent> contents = bodyChat.contents;
        RequestGeminiV2 request = new RequestGeminiV2();
        request.contents = new ArrayList<>();
        for(MessageContent content : contents){
            Content newContent = new Content();
            newContent.role = content.role == Role.user ? "user" : "model";
            List<Part> parts = newContent.parts = new ArrayList<>();
            parts.add(new Part(content.content));
            request.contents.add(newContent);
        }
        return request;
    }

    public static class Content {
        public String role;
        public List<Part> parts;

    }
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Part {
        public String text;

    }
}
