package com.prox.passgpt.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
public class RequestBodyChat {
    public List<MessageContent> contents;
}
