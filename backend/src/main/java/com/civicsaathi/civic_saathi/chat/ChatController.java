
package com.civicsaathi.civic_saathi.chat;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3001"})
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatResponse sendMessage(@RequestBody ChatRequest request) {

        if (request == null || request.message() == null
                || request.message().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please enter a message."
            );
        }

        if (request.message().length() > 4000) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please keep your message under 4000 characters."
            );
        }

        String reply = chatService.chat(
                request.message(),
                request.language(),
                request.history()
        );

        return new ChatResponse(reply, "CivicSaathi Local AI");
    }

    public record ChatRequest(
            String message,
            String language,
            List<ChatMessage> history
    ) {}

    public record ChatMessage(
            String role,
            String content
    ) {}

    public record ChatResponse(
            String reply,
            String assistant
    ) {}
}
