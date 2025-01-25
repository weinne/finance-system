package com.weinne.finance_system.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weinne.finance_system.model.SchemaChangeLog;


public interface SchemaChangeLogRepository extends JpaRepository<SchemaChangeLog, Long> {

}