package com.weinne.finance_system.dto;

import com.weinne.finance_system.model.Role;
import lombok.Data;

@Data
public class ChurchRegistrationDTO {
    // Dados da igreja
    private String churchName;
    private String cnpj;
    private String address;
    
    // Dados do usuário administrador
    private String email;
    private String password;
    private Role role;
}
