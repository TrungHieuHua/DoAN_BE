package com.shose.shoseshop.controller.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private String message;
    private String sessionId;
}
