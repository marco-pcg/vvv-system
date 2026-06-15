-- ============================================================
-- VVV System — V3 Schema Migration
-- ============================================================
-- Adiciona a coluna numero_cartao ausente no script V1 original

ALTER TABLE pagamento_credito 
ADD COLUMN numero_cartao VARCHAR(20) NOT NULL DEFAULT '0000000000000000';

ALTER TABLE pagamento_debito 
ADD COLUMN numero_cartao VARCHAR(20) NOT NULL DEFAULT '0000000000000000';
