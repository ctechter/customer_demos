package io.developer.confluent.spring.controller;

import io.confluent.developer.User;

import io.developer.confluent.spring.service.ccKafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final ccKafkaProducerService producerService;

    @PostMapping("/async")
    public ResponseEntity<String> sendMessageAsync(@RequestBody User user) {
        producerService.sendMessage(user);
        return ResponseEntity.accepted().body("Message send initiated");
    }

    @PostMapping("/sync")
    public ResponseEntity<String> sendMessageSync(@RequestBody User user) {
        try {
            producerService.sendMessageWithResponse(user).get(); // Wait for the result
            return ResponseEntity.ok("Message sent successfully");
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError()
                .body("Failed to send message: " + e.getMessage());
        }
    }
}
