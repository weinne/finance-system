package com.weinne.finance_system.infrastructure.multitenancy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantClassEntity {
    String schemaNameField() default "schemaName";
    String tenantIdField() default "id";
}