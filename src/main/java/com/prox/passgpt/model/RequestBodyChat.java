package com.prox.passgpt.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
public class RequestBodyChat {

    public List<MessageContent> contents;

    public String getQuestion() {
        StringBuilder content = new StringBuilder();
        for(var c : contents) {
            if(c.role == Role.user){
                content.append(c.content).append("\n");
            }
        }
        return content.toString();
    }
}
