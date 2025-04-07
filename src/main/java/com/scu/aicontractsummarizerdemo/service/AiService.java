package com.scu.aicontractsummarizerdemo.service;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AiService {

    private final OpenAiChatModel openAiChatModel;

    @Autowired
    public AiService(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    public String analyzeContract(String contractText) {
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("""
                        You are a legal AI assistant. Analyze the following loan contract.
                        Extract key terms, parties, repayment details, and potential risks.
                        If you cannot extract this information, return a JSON object with `"error": "<your explanation here>"` explaining what is missing.
                        Format the response in json format and do not include any non-json data including titles.
                        
                        """),
                new UserMessage(contractText)
        ));

        ChatResponse response = openAiChatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }

    public String explainTerm(String key, String term, String fullJsonString) {
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("""
                You are a legal AI assistant. The user clicked on a specific field in a JSON summary of a contract.
                Please explain the meaning and legal/financial significance of this field in the context of the overall contract, and relate it to common contract clauses if possible.
                If this field is unclear, ambiguous, or its meaning depends on another part of the contract, explain that as well.
                If you cannot explain this information, return a JSON object with `"error": "<your explanation here>"` explaining what is missing as a last resort.
                Do not show text that allow for further question or allow user to communicate further.
                The result should be in markdown styling if possible.
                
                """),
                new UserMessage(String.format("""
                Key: %s
                Term: %s
                FullJsonSummary: %s
                """, key, term, fullJsonString))
        ));
        ChatResponse response = openAiChatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }
}
