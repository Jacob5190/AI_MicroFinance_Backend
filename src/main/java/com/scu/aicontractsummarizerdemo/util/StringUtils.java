package com.scu.aicontractsummarizerdemo.util;

public class StringUtils {
    public static String stripCodeFences(String input) {
        if (input == null) return "";
        return input
                .replaceAll("(?s)```\\w*\\s*", "")   // remove ```json, ```markdown, etc.
                .replaceAll("(?s)```", "")           // remove closing ```
                .trim();
    }
}
