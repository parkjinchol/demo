package com.demo.dto;

import lombok.Getter;

@Getter
public class EmailRecipient {
    private Long id;
    private String emailMasterId;
    private String email;
    private String subject;
    private String content;

}
