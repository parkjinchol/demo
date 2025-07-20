package com.demo.dto;

import lombok.Builder;

@Builder
public class EmailDto {
    private Long id;
    private String emailMasterId;
    private String email;
    private String subject;
    private String content;

}
