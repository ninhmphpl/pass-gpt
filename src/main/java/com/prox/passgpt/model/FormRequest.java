package com.prox.passgpt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FormRequest {
    private String model;
    private boolean stream;
    private List<MessageContent> messages;

    public static FormRequest createMessageStream(String content, String model) {
        FormRequest formRequest = new FormRequest();
        formRequest.model = model;
        formRequest.stream = true;
        MessageContent messageContent = new MessageContent();
        messageContent.setRole(Role.user);
        messageContent.setContent(content);
        formRequest.messages = List.of(messageContent);
        return formRequest;
    }
}
