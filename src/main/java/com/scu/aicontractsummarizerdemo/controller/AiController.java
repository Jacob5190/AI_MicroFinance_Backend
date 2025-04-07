package com.scu.aicontractsummarizerdemo.controller;

import com.scu.aicontractsummarizerdemo.dto.TermExplainRequest;
import com.scu.aicontractsummarizerdemo.service.AiService;
import com.scu.aicontractsummarizerdemo.util.JsonUtils;
import com.scu.aicontractsummarizerdemo.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/contract")
    public ResponseEntity<String> summarizeContract(@RequestBody String contract) {
        String contractText = contract.replaceAll("\\r\\n?", "\n")
                .replaceAll("\\n{2,}", "\n\n")
                .trim();
        contractText = contractText.replaceAll("(?i)Lender:\\s+(.*?)\\n", "LENDER_NAME: [$1]\n");
        contractText = contractText.replaceAll("(?i)Borrower:\\s+(.*?)\\n", "BORROWER_NAME: [$1]\n");
        contractText = contractText.replaceAll("(\\bDate:?\\b)\\s+(\\w+ \\d{1,2}, \\d{4})", "$1 [$2]");
        contractText = contractText.replaceAll("(?m)^Signature: _+\\s*$", ""); // Remove signature lines
        String aiResponse = aiService.analyzeContract(contractText);
        try {
            aiResponse = JsonUtils.cleanAndExtractJson(aiResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON response from AI");
        }
        return ResponseEntity.ok(StringUtils.stripCodeFences(aiResponse));
    }

    @PostMapping("/explain")
    public ResponseEntity<String> explainTerm(@RequestBody TermExplainRequest termInfo) {
        String aiResponse = aiService.explainTerm(termInfo.getKey(), termInfo.getValue(), termInfo.getFullJsonString());
        return ResponseEntity.ok(StringUtils.stripCodeFences(aiResponse));
    }
}
