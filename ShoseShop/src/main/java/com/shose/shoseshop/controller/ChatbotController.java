package com.shose.shoseshop.controller;


import com.google.cloud.dialogflow.v2.*;
import com.shose.shoseshop.controller.request.ChatRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Value("${dialogflow.project-id}") // Lấy Project ID từ application.properties
    private String projectId;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody ChatRequest request) {
        try {
            String responseText = detectIntentText(request.getSessionId(), request.getMessage());
            return ResponseEntity.ok(Map.of("text", responseText));
        } catch (Exception e) {
            log.error("Lỗi khi gọi Dialogflow API: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("text", "Xin lỗi, hiện tại tôi đang gặp sự cố. Vui lòng thử lại sau."));
        }
    }

    private String detectIntentText(String sessionId, String message) throws Exception {
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            SessionName session = SessionName.of(projectId, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(message).setLanguageCode("vi-VN");
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(request);
            return response.getQueryResult().getFulfillmentText();
        }
    }
}
