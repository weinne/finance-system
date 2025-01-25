package com.weinne.finance_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "Nome da igreja é obrigatório")
    private String name;
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", 
            message = "CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
    private String cnpj;
    
    @NotBlank(message = "Endereço é obrigatório")
    private String address;
    
    @NotBlank(message = "Email do administrador é obrigatório")
    @Email(message = "Email inválido")
    private String adminEmail;
    
    @NotBlank(message = "Senha do administrador é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String adminPassword;

    @NotBlank(message = "Nome do schema é obrigatório")
    private String schemaName;
}
