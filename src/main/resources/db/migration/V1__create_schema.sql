-- ============================================================
-- VVV System — V1 Schema Migration
-- PostgreSQL 16+
-- ============================================================

-- ============================================================
-- TABELAS INDEPENDENTES (sem FK)
-- ============================================================

CREATE TABLE transportadora (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL,
    cnpj       VARCHAR(14)  NOT NULL,

    CONSTRAINT uk_transportadora_cnpj UNIQUE (cnpj)
);

-- -------------------------------------------------------

CREATE TABLE cidade (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    uf   VARCHAR(2)   NOT NULL,

    CONSTRAINT uk_cidade_nome_uf UNIQUE (nome, uf),
    CONSTRAINT ck_cidade_uf CHECK (
        uf IN ('AC','AL','AP','AM','BA','CE','DF','ES','GO',
               'MA','MT','MS','MG','PA','PB','PR','PE','PI',
               'RJ','RN','RS','RO','RR','SC','SP','SE','TO')
    )
);

-- -------------------------------------------------------

CREATE TABLE passageiro (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cpf             VARCHAR(11)  NOT NULL,
    nome            VARCHAR(255) NOT NULL,
    cep             VARCHAR(8),
    data_nascimento DATE,
    email           VARCHAR(255) NOT NULL,
    telefone        VARCHAR(11),
    possui_acompanhante BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uk_passageiro_cpf   UNIQUE (cpf),
    CONSTRAINT uk_passageiro_email UNIQUE (email)
);

-- ============================================================
-- RBAC — TABELAS DE AUTENTICAÇÃO / AUTORIZAÇÃO
-- ============================================================

CREATE TABLE permission (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),

    CONSTRAINT uk_permission_name UNIQUE (name)
);

-- -------------------------------------------------------

CREATE TABLE role (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255),

    CONSTRAINT uk_role_name UNIQUE (name)
);

-- -------------------------------------------------------

CREATE TABLE users (
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username                VARCHAR(100) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    enabled                 BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_expired     BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked      BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_users_username UNIQUE (username)
);

-- -------------------------------------------------------
-- RBAC — tabelas de junção
-- -------------------------------------------------------

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE role_permission (
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    PRIMARY KEY (role_id, permission_id),

    CONSTRAINT fk_role_perm_role       FOREIGN KEY (role_id)       REFERENCES role (id),
    CONSTRAINT fk_role_perm_permission FOREIGN KEY (permission_id) REFERENCES permission (id)
);

-- ============================================================
-- TABELAS COM DEPENDÊNCIAS DE 1º NÍVEL
-- ============================================================

CREATE TABLE modal (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_transportadora   BIGINT      NOT NULL,
    codigo              VARCHAR(20) NOT NULL,
    tipo                VARCHAR(20) NOT NULL,
    capacidade          INTEGER     NOT NULL,
    status_operacional  VARCHAR(20) NOT NULL,

    CONSTRAINT uk_modal_codigo         UNIQUE (codigo),
    CONSTRAINT fk_modal_transportadora FOREIGN KEY (id_transportadora) REFERENCES transportadora (id),
    CONSTRAINT ck_modal_capacidade     CHECK (capacidade > 0),
    CONSTRAINT ck_modal_tipo           CHECK (tipo IN ('ONIBUS','AVIAO','TREM','NAVIO')),
    CONSTRAINT ck_modal_status         CHECK (status_operacional IN ('OPERACIONAL','EM_MANUTENCAO','INATIVO'))
);

-- -------------------------------------------------------

CREATE TABLE cliente (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cpf             VARCHAR(11)  NOT NULL,
    nome            VARCHAR(255) NOT NULL,
    cep             VARCHAR(8),
    data_nascimento DATE,
    email           VARCHAR(255) NOT NULL,
    telefone        VARCHAR(11),
    user_id         BIGINT,

    CONSTRAINT uk_cliente_cpf     UNIQUE (cpf),
    CONSTRAINT uk_cliente_email   UNIQUE (email),
    CONSTRAINT uk_cliente_user_id UNIQUE (user_id),
    CONSTRAINT fk_cliente_user    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- -------------------------------------------------------

CREATE TABLE funcionario (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cpf             VARCHAR(11)  NOT NULL,
    nome            VARCHAR(255) NOT NULL,
    cep             VARCHAR(8),
    data_nascimento DATE,
    matricula       VARCHAR(20)  NOT NULL,
    email           VARCHAR(255) NOT NULL,
    telefone        VARCHAR(11),
    user_id         BIGINT,

    CONSTRAINT uk_funcionario_cpf       UNIQUE (cpf),
    CONSTRAINT uk_funcionario_matricula UNIQUE (matricula),
    CONSTRAINT uk_funcionario_email     UNIQUE (email),
    CONSTRAINT uk_funcionario_user_id   UNIQUE (user_id),
    CONSTRAINT fk_funcionario_user      FOREIGN KEY (user_id) REFERENCES users (id)
);

-- ============================================================
-- TABELAS COM DEPENDÊNCIAS DE 2º NÍVEL
-- ============================================================

CREATE TABLE viagem (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cidade_origem   BIGINT       NOT NULL,
    cidade_destino  BIGINT       NOT NULL,
    partida         TIMESTAMP    NOT NULL,
    chegada         TIMESTAMP    NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    preco           NUMERIC(10,2) NOT NULL,

    CONSTRAINT fk_viagem_cidade_origem  FOREIGN KEY (cidade_origem)  REFERENCES cidade (id),
    CONSTRAINT fk_viagem_cidade_destino FOREIGN KEY (cidade_destino) REFERENCES cidade (id),
    CONSTRAINT ck_viagem_preco          CHECK (preco >= 0),
    CONSTRAINT ck_viagem_datas          CHECK (chegada > partida),
    CONSTRAINT ck_viagem_status         CHECK (status IN ('AGENDADA','EM_ANDAMENTO','CONCLUIDA','CANCELADA'))
);

-- -------------------------------------------------------

CREATE TABLE viagem_modal (
    viagem_id BIGINT NOT NULL,
    modal_id BIGINT NOT NULL,

    PRIMARY KEY (viagem_id, modal_id),

    CONSTRAINT fk_vm_viagem FOREIGN KEY (viagem_id) REFERENCES viagem (id),
    CONSTRAINT fk_vm_modal FOREIGN KEY (modal_id) REFERENCES modal (id)
);

-- -------------------------------------------------------

CREATE TABLE viagem_escala (
    viagem_id BIGINT NOT NULL,
    cidade_id BIGINT NOT NULL,

    PRIMARY KEY (viagem_id, cidade_id),

    CONSTRAINT fk_ve_viagem FOREIGN KEY (viagem_id) REFERENCES viagem (id),
    CONSTRAINT fk_ve_cidade FOREIGN KEY (cidade_id) REFERENCES cidade (id)
);

-- -------------------------------------------------------

CREATE TABLE ponto_de_venda (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cnpj       VARCHAR(14)  NOT NULL,
    endereco   VARCHAR(255) NOT NULL,
    gerente_id BIGINT       NOT NULL,

    CONSTRAINT uk_pdv_cnpj    UNIQUE (cnpj),
    CONSTRAINT fk_pdv_gerente FOREIGN KEY (gerente_id) REFERENCES funcionario (id)
);

-- -------------------------------------------------------

CREATE TABLE funcionario_pdv (
    funcionario_id BIGINT NOT NULL,
    pdv_id         BIGINT NOT NULL,

    PRIMARY KEY (funcionario_id, pdv_id),

    CONSTRAINT fk_func_pdv_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario (id),
    CONSTRAINT fk_func_pdv_pdv         FOREIGN KEY (pdv_id)         REFERENCES ponto_de_venda (id)
);

-- ============================================================
-- TABELAS COM DEPENDÊNCIAS DE 3º NÍVEL
-- ============================================================

CREATE TABLE reserva (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo         VARCHAR(20)   NOT NULL,
    id_viagem      BIGINT        NOT NULL,
    id_cliente     BIGINT        NOT NULL,
    id_passageiro  BIGINT        NOT NULL,
    data_criacao   TIMESTAMP     NOT NULL,
    status         VARCHAR(20)   NOT NULL,
    valor_total    NUMERIC(10,2) NOT NULL,

    CONSTRAINT uk_reserva_codigo     UNIQUE (codigo),
    CONSTRAINT fk_reserva_viagem     FOREIGN KEY (id_viagem)     REFERENCES viagem (id),
    CONSTRAINT fk_reserva_cliente    FOREIGN KEY (id_cliente)    REFERENCES cliente (id),
    CONSTRAINT fk_reserva_passageiro FOREIGN KEY (id_passageiro) REFERENCES passageiro (id),
    CONSTRAINT ck_reserva_valor      CHECK (valor_total >= 0),
    CONSTRAINT ck_reserva_status     CHECK (status IN ('PENDENTE','CONFIRMADA','CANCELADA','EXPIRADA'))
);

-- -------------------------------------------------------

CREATE TABLE ticket (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero     VARCHAR(20) NOT NULL,
    id_reserva BIGINT      NOT NULL,
    assento    VARCHAR(10) NOT NULL,

    CONSTRAINT uk_ticket_numero  UNIQUE (numero),
    CONSTRAINT uk_ticket_reserva UNIQUE (id_reserva),
    CONSTRAINT fk_ticket_reserva FOREIGN KEY (id_reserva) REFERENCES reserva (id)
);

-- -------------------------------------------------------

CREATE TABLE pagamento (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_reserva BIGINT      NOT NULL,
    tipo       VARCHAR(20) NOT NULL,
    status     VARCHAR(20) NOT NULL,

    CONSTRAINT uk_pagamento_reserva UNIQUE (id_reserva),
    CONSTRAINT fk_pagamento_reserva FOREIGN KEY (id_reserva) REFERENCES reserva (id),
    CONSTRAINT ck_pagamento_tipo    CHECK (tipo IN ('CREDITO','DEBITO','PIX')),
    CONSTRAINT ck_pagamento_status  CHECK (status IN ('PENDENTE','APROVADO','RECUSADO','ESTORNADO'))
);

-- -------------------------------------------------------

CREATE TABLE pagamento_credito (
    id            BIGINT  NOT NULL PRIMARY KEY,
    parcelas      INTEGER NOT NULL,
    numero_cartao INTEGER NOT NULL,

    CONSTRAINT fk_pgto_credito          FOREIGN KEY (id) REFERENCES pagamento (id),
    CONSTRAINT ck_pgto_credito_parcelas CHECK (parcelas > 0)
);

-- -------------------------------------------------------

CREATE TABLE pagamento_debito (
    id            BIGINT  NOT NULL PRIMARY KEY,
    parcelas      INTEGER NOT NULL,
    numero_cartao INTEGER NOT NULL,

    CONSTRAINT fk_pgto_debito FOREIGN KEY (id) REFERENCES pagamento (id)
);

-- -------------------------------------------------------

CREATE TABLE pagamento_pix (
    id        BIGINT        NOT NULL PRIMARY KEY,
    chave_pix VARCHAR(60)   NOT NULL,
    valor     DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_pgto_pix FOREIGN KEY (id) REFERENCES pagamento (id)
);

-- ============================================================
-- ÍNDICES
-- ============================================================
-- Nota: colunas com UNIQUE constraint já possuem índice implícito
-- no PostgreSQL (cpf, email, username, matricula, codigo, cnpj).
-- Os índices abaixo são para colunas não-únicas usadas em queries.
-- ============================================================

-- FK indices (PostgreSQL NÃO cria índices automaticamente para FKs)
CREATE INDEX idx_modal_transportadora   ON modal (id_transportadora);
-- Removed idx_viagem_modal since id_modal was extracted
CREATE INDEX idx_viagem_cidade_origem   ON viagem (cidade_origem);
CREATE INDEX idx_viagem_cidade_destino  ON viagem (cidade_destino);
CREATE INDEX idx_reserva_viagem         ON reserva (id_viagem);
CREATE INDEX idx_reserva_cliente        ON reserva (id_cliente);
CREATE INDEX idx_reserva_passageiro     ON reserva (id_passageiro);
CREATE INDEX idx_pdv_gerente            ON ponto_de_venda (gerente_id);

-- Status indices (queries frequentes por status)
CREATE INDEX idx_reserva_status ON reserva (status);
CREATE INDEX idx_viagem_status  ON viagem (status);

-- Otimização de consultas por data
CREATE INDEX idx_viagem_partida ON viagem (partida);
