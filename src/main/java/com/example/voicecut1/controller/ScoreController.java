package com.example.voicecut1.controller;

import com.example.voicecut1.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> checkScore(@RequestParam Long userId, @RequestParam int score, @RequestParam String fcmToken) {
        notificationService.checkScoreAndNotify(userId, score, fcmToken);
        return ResponseEntity.ok("Score checked and notification sent if applicable");
    }
}