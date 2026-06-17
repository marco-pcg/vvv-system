-- ============================================================
-- VVV System — V5 Schema Migration
-- ============================================================

ALTER TABLE reserva ADD COLUMN integrado_transportadora BOOLEAN DEFAULT FALSE NOT NULL;
