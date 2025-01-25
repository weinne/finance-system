package com.weinne.finance_system.service;

import com.weinne.finance_system.dto.RegisterRequest;
import com.weinne.finance_system.dto.RegisterResponse;
import com.weinne.finance_system.infrastructure.multitenancy.context.TenantContext;
import com.weinne.finance_system.infrastructure.multitenancy.schema.TenantSchemaService;
import com.weinne.finance_system.model.Role;
import com.weinne.finance_system.model.User;
import com.weinne.finance_system.model.Church;
import com.weinne.finance_system.repos.ChurchRepository;
import com.weinne.finance_system.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ChurchRepository churchRepository;
    private final TenantSchemaService tenantMigrationService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // 1. Criar tenant no schema público
        Church church = new Church();
        church.setName(request.getName());
        church.setCnpj(request.getCnpj());
        church.setAddress(request.getAddress());
        church.setSchemaName(request.getSchemaName());
        church = churchRepository.save(church);

        User user = new User();
        user.setEmail(request.getAdminEmail());
        user.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        user.setRole(Role.ADMIN);
        user.setChurch(church);
        user = userRepository.save(user);

        // 2. Criar schema do tenant
        tenantMigrationService.createTenantSchema(church);

        // 3. Setar contexto do tenant para criar igreja e usuário
        TenantContext.setCurrentTenant(church.getSchemaName());

        return RegisterResponse.builder()
            .churchId(church.getId())
            .churchName(church.getName())
            .adminEmail(user.getEmail())
            .schemaName(church.getSchemaName())
            .build();
    }

}
