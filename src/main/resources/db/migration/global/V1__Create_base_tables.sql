-- Para o schema público (gerenciamento de tenants)
CREATE TABLE igrejas (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    address TEXT,
    schema_name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Cada tenant terá seu próprio schema com estas tabelas:
CREATE SCHEMA IF NOT EXISTS tenant_template;

CREATE TABLE tenant_template.doacoes (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(10,2) NOT NULL,
    date DATE NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    category VARCHAR(100),
    church_id BIGINT NOT NULL REFERENCES igrejas(id)
);

CREATE TABLE tenant_template.gastos (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(10,2) NOT NULL,
    date DATE NOT NULL,
    supplier VARCHAR(255),
    category VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    invoice_url TEXT,
    church_id BIGINT NOT NULL REFERENCES igrejas(id)
);