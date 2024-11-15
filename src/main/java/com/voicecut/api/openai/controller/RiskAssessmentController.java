package com.voicecut.api.openai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.voicecut.api.openai.dto.RiskAssessmentRequest;
import com.voicecut.api.openai.service.RiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    @Autowired
    public RiskAssessmentController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @PostMapping
    public ResponseEntity<String> gptRisk(@RequestBody RiskAssessmentRequest request) throws JsonProcessingException {
        return riskAssessmentService.assessRisk(request);
    }
}