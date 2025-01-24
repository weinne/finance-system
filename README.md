# Sistema de Gestão Financeira para Igrejas

## Visão Geral
Sistema SaaS multitenant para gestão financeira de igrejas, com foco em transparência e integração com fluxos religiosos. Permite o gerenciamento de doações, gastos e relatórios auditáveis, com dashboard público para congregação.

---

## Requisitos Funcionais

### 1. **Multitenancy (Igrejas como Tenants)**
- Cadastro de igreja com CNPJ, endereço e configurações básicas
- Isolamento completo de dados entre igrejas
- Configuração de categorias personalizadas (doações/gastos)

### 2. **Gestão de Doações**
- Registro manual (dinheiro, transferência, Pix)
- Importação de extratos bancários (CSV/PDF)
- Anexo de comprovantes digitalizados
- Reconciliação bancária automática

### 3. **Gestão de Gastos**
- Lançamento de despesas com notas fiscais
- Fluxo de aprovação por comissão
- Categorização por projeto (ex.: "Reforma do telhado")
- Limite orçamentário por categoria

### 4. **Dashboard de Transparência**
- Visualização pública via link único
- Gráficos comparativos (arrecadação vs. gastos)
- Detalhamento por período/projeto
- Exportação para redes sociais

### 5. **Controle de Acesso**
| Função            | Permissões                                  |
|--------------------|--------------------------------------------|
| Administrador      | Gerenciar usuários/configurações da igreja |
| Tesoureiro         | Registrar doações/gastos                   |
| Pastor             | Visualizar relatórios completos            |
| Comissão de Contas | Aprovar/rejeitar gastos                    |
| Doador             | Acesso ao dashboard público                |

### 6. **Relatórios e Exportação**
- Relatório mensal automático (PDF/XLSX)
- Histórico de alterações (log de auditoria)
- Dados prontos para contabilidade (SPED)

---

## Requisitos Não-Funcionais

### 1. **Segurança**
- Autenticação com JWT + Spring Security
- Criptografia de dados sensíveis (AES-256)
- Mascaramento de dados em logs

### 2. **Performance**
- Tempo de resposta < 2s para 95% das requisições
- Suporte a 100 igrejas simultâneas (escalabilidade horizontal)

### 3. **Disponibilidade**
- SLA 99.9% (uptime mensal)
- Backup diário automático

### 4. **Deploy**
- Empacotamento em containers Docker
- Implantação em cloud (AWS/Azure)
- CI/CD com GitHub Actions

---

## Stack Tecnológica

### Backend
- Java 17 + Spring Boot 3.3
- PostgreSQL (Multitenancy por schema)
- Flyway (migrações de banco)
- Spring Security + Keycloak

### Frontend
- React.js (dashboard administrativo)
- Thymeleaf (dashboard público)
- Chart.js (visualizações)

### Infra
- Docker + Docker Compose
- NGINX (reverse proxy)
- Prometheus + Grafana (monitoramento)

---

## Roadmap

### Fase 1 (MVP)
- [ ] Cadastro básico de igrejas
- [ ] Registro manual de doações/gastos
- [ ] Dashboard público simplificado

### Fase 2
- [ ] Integração com OpenFinance/Pix
- [ ] Fluxo de aprovação de gastos
- [ ] Relatórios personalizáveis

### Fase 3
- [ ] App móvel para doadores
- [ ] Pagamento de dizimo via PIX QR Code
- [ ] IA para categorização automática

---

## Glossário
| Termo               | Definição                                  |
|---------------------|-------------------------------------------|
| Tenant              | Instância isolada de uma igreja no sistema|
| OpenFinance         | Sistema de integração bancária do BACEN   |
| Reconciliação       | Conferência entre registros e extratos    |