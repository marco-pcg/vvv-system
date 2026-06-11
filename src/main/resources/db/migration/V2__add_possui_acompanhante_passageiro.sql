-- V2: Adiciona a coluna possui_acompanhante na tabela passageiro conforme o modelo lógico
ALTER TABLE passageiro ADD COLUMN possui_acompanhante BOOLEAN NOT NULL DEFAULT FALSE;
