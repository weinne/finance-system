package com.weinne.finance_system.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weinne.finance_system.model.Church;

@Repository
public interface ChurchRepository extends JpaRepository<Church, Long> {
    Optional<Church> findByCnpj(String cnpj);
    boolean existsBySchemaName(String schemaName);
}
