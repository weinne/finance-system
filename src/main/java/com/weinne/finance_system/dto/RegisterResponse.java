package com.weinne.finance_system.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Long churchId;
    private String churchName;
    private String adminEmail;
    private String schemaName;
}