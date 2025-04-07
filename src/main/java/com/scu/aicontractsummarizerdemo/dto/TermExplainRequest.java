package com.scu.aicontractsummarizerdemo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TermExplainRequest {
    private String key;
    private String value;
    private String fullJsonString;
}
