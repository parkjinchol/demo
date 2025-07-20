package com.demo.dto;

import lombok.Builder;

@Builder
public class Test2Dto {
    private Long id;
    private String emailMasterId;
    private String email;
    private String subject;
    private String content;

}
