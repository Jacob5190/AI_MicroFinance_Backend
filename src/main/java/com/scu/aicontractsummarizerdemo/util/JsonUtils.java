package com.scu.aicontractsummarizerdemo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String cleanAndExtractJson(String aiResponse) {
        if (aiResponse == null) return "";
        // Trim and find where the JSON starts
        aiResponse = aiResponse.trim();
        int start = Math.min(
                aiResponse.indexOf('{') >= 0 ? aiResponse.indexOf('{') : Integer.MAX_VALUE,
                aiResponse.indexOf('[') >= 0 ? aiResponse.indexOf('[') : Integer.MAX_VALUE
        );
        if (start == Integer.MAX_VALUE) return "";
        String jsonPart = aiResponse.substring(start).trim();
        // Basic cleanup
        jsonPart = fixCommonIssues(jsonPart);
        // Validate and return pretty-printed JSON
        try {
            JsonNode parsed = mapper.readTree(jsonPart);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parsed);
        } catch (Exception e) {
            System.err.println("Could not parse JSON after cleaning: " + e.getMessage());
            return jsonPart; // Return best effort
        }
    }

    private static String fixCommonIssues(String json) {
        // Remove trailing commas
        json = json.replaceAll(",\\s*([}\\]])", "$1");
        // Unescaped inner quotes in values (very simple heuristic, not bulletproof)
        json = json.replaceAll(":(\\s*)\"([^\"]*?)\"(\\s*[,}\\]])", ":\"$2\"$3");
        // Convert smart quotes to normal quotes
        json = json.replaceAll("[“”]", "\"");
        // Add closing braces if obviously missing
        int openBraces = json.length() - json.replace("{", "").length();
        int closeBraces = json.length() - json.replace("}", "").length();
        while (closeBraces < openBraces) {
            json += "}";
            closeBraces++;
        }
        return json.trim();
    }
}
