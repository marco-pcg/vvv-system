### 👤 Dev 1 — Arquitetura, Autenticação e Entidades "nome do responsável por esta parte"

**Responsabilidades:**
- Configuração do projeto e infraestrutura (Spring Boot, PostgreSQL, Docker)
- Todas as entidades JPA, enums e migrations do banco
- Módulo de Autenticação/Autorização (JWT, Spring Security, Roles/Permissions)
- Sistema de Logs transversal (AOP / interceptors)
- Tratamento global de exceções
- DTOs base e mappers

**Ordem das tarefas:**
1. Setup do projeto + estrutura de pacotes
2. Entidades JPA (todas) + Enums + Migrations
3. Módulo Auth (Login, JWT, Spring Security, Roles)
4. Sistema de Logs (AOP)
5. Exception handling global
6. Suporte aos demais devs (contratos e interfaces)

**Dependências:** Nenhuma (é o ponto de partida)

**Entregáveis:**
- Projeto configurado e funcional
- Todas as entidades mapeadas
- Endpoint POST `/auth/login` funcional com JWT
- Middleware de autorização por role
- Logs automáticos por AOP
- Migrations do banco de dados

---

### 👤 Dev 2 — Cadastros Base (Transportadora, Modal, Cidade) "nome do responsável por esta parte"

**Responsabilidades:**
- CRUD de Transportadoras (Controller → Service → Repository)
- CRUD de Modais (RF01/CU03)
- CRUD de Cidades e Aeroportos (RF04)
- Validações de negócio (código único, transportadora existente, código aeroporto para aéreas)

**Ordem das tarefas:**
1. Aguardar entidades JPA do Dev 1
2. CRUD Transportadoras (Repository + Service + Controller + DTOs)
3. CRUD Cidades/Aeroportos
4. CRUD Modais (com validação de transportadora)
5. Testes unitários e de integração

**Dependências:** Dev 1 (entidades + auth)

**Entregáveis:**
- Endpoints REST para Transportadoras, Cidades e Modais
- Validações de RN01, RN02, RN06, RN07
- Testes automatizados

---

### 👤 Dev 3 — Viagens, Manutenção e Gestão de PDV/Funcionários "nome do responsável por esta parte"

**Responsabilidades:**
- CRUD de Viagens (RF03/CU04)
- Registro de Manutenção de Modal (RF02/CU07)
- CRUD de Funcionários (RF13)
- CRUD de Pontos de Venda (RF10)
- Atribuição de Gerente a PDV (RF11)
- Alocação de Funcionário em PDV (RF14/CU06)

**Ordem das tarefas:**
1. Aguardar entidades JPA do Dev 1 e CRUDs do Dev 2 (Modais, Cidades)
2. CRUD Funcionários + CRUD PDV (podem ser feitos em paralelo às Viagens)
3. Manutenção de Modal (status + bloqueio de reservas)
4. Cadastro de Viagens (validação de modal operacional + cidades)
5. Atribuição de Gerente a PDV
6. Alocação de Funcionário (com validação de limite 2 PDVs)
7. Testes unitários e de integração

**Dependências:** Dev 1 (entidades + auth), Dev 2 (Modais, Cidades)

**Entregáveis:**
- Endpoints REST para Viagens, Manutenção, Funcionários, PDV
- Validações de RN03, RN04, RN05, RN17, RN18, RN22
- Testes automatizados

---

### 👤 Dev 4 — Módulo de Vendas (Reserva, Pagamento, Ticket) "nome do responsável por esta parte"

**Responsabilidades:**
- Cadastro de Passageiros (RF08)
- Cadastro de Reserva (RF05)
- Controle de Capacidade / Anti-overbooking (RF09/RI04)
- Cálculo de Desconto (RF15/RI03)
- Processamento de Pagamento — Strategy Pattern (RF06/RI02/RI05)
- Emissão de Ticket (RF07/RI08)
- Fluxo completo do CU02 (Processo de Compra)

**Ordem das tarefas:**
1. Aguardar entidades do Dev 1 e Viagens do Dev 3
2. CRUD Passageiros
3. Serviço Anti-overbooking (RI04)
4. Cálculo de desconto para menores (RI03)
5. Serviço de Reserva (com anti-overbooking + desconto integrados)
6. Strategy de Pagamento (PagamentoCredito, PagamentoDebito, PagamentoDinheiro)
7. Cálculo de juros no crédito (RI02)
8. Emissão de Ticket (com validação RI08)
9. Fluxo orquestrado do CU02
10. Testes unitários e de integração

**Dependências:** Dev 1 (entidades + auth), Dev 3 (Viagens)

**Entregáveis:**
- Endpoint POST `/reservas` com fluxo completo de compra
- Strategy Pattern para pagamentos
- Validações de RN08–RN16, RN23
- Testes automatizados

---

### 👤 Dev 5 — Vendas Online, Integrações e Segurança "nome do responsável por esta parte"

**Responsabilidades:**
- Fluxo de Venda Online (variante do CU02 para canal web)
- Aprovação de Venda Online pelo Gerente Virtual (RF12/CU05)
- Integração com Transportadoras — API de transferência de dados (RI06)
- Processamento de scripts de vendas online (RI07)
- Confirmação automática de pagamento pela operadora (RI05/RN16)
- Segurança e conformidade (RNF01, RNF07)
- Backup e recuperação (RNF05)

**Ordem das tarefas:**
1. Aguardar Módulo de Vendas do Dev 4 (pelo menos Reserva + Pagamento)
2. Fluxo de venda online (status "Aguardando Aprovação Gerencial")
3. Interface de aprovação gerencial (CU05)
4. Serviço de integração com Transportadoras (RI06)
5. Processamento de scripts de vendas online (RI07)
6. Confirmação automática de pagamento (RI05)
7. Implementação de segurança (criptografia, LGPD, auditoria)
8. Configuração de backup automático
9. Testes unitários e de integração

**Dependências:** Dev 1 (auth), Dev 4 (Reserva + Pagamento)

**Entregáveis:**
- Endpoint GET/POST para aprovação de vendas online
- Serviço de integração com transportadoras
- Processamento de scripts de vendas
- Configurações de segurança e LGPD
- Testes automatizados