package com.prox.passgpt.model;

import java.util.List;

public class RequestGpt {
    public String model;
    public boolean stream;
    public List<MessageContent> messages;

    public static RequestGpt createMessageStream(String content, String model) {
        RequestGpt formRequest = new RequestGpt();
        formRequest.model = model;
        formRequest.stream = true;
        MessageContent messageContent = new MessageContent();
        messageContent.role = Role.user;
        messageContent.content = content;
        formRequest.messages = List.of(messageContent);
        return formRequest;
    }

    public static RequestGpt createMessageStream(RequestBodyChat requestBodyChat, ModelChat model) {
        RequestGpt formRequest = new RequestGpt();
        formRequest.model = (model != ModelChat.gpt4) ? "gpt-3.5-turbo" : "gpt-4";
        formRequest.stream = true;
        formRequest.messages = requestBodyChat.contents;
        fixBody(model, formRequest);
        return formRequest;
    }

    private static void fixBody(ModelChat model, RequestGpt formRequest) {
        if (model == ModelChat.gpt35_shot) {
            for(var content : formRequest.messages){
                if(content.role == Role.user){
                    content.content += " (Short answer)";
                }
            }
        } else if (model == ModelChat.gpt35_100word) {
            for(var content : formRequest.messages){
                if(content.role == Role.user){
                    content.content += " (Max answer is 100 words)";
                }
            }
        }
    }

}
