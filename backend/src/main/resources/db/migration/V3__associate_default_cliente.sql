-- ============================================================
-- VVV System — V3 Schema Migration (Link default cliente user)
-- ============================================================

UPDATE cliente 
SET user_id = (SELECT id FROM users WHERE username = 'cliente') 
WHERE user_id IS NULL;
