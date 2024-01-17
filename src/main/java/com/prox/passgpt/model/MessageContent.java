package com.prox.passgpt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageContent {
    private String content;
    private Role role;
}
