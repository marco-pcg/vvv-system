-- ============================================================
-- VVV System — V4 Schema Migration (Roles e Permissions)
-- ============================================================

-- 1. Criar Perfis (Roles)
INSERT INTO role (name, description) VALUES
('ROLE_ADMIN', 'Super Usuário'),
('ROLE_GERENTE', 'Gerente Unificado: PDV + Negócios Virtuais'),
('ROLE_FUNCIONARIO', 'Funcionário de Ponto de Venda'),
('ROLE_TRANSPORTADORA', 'Transportadora / Parceiro Externo'),
('ROLE_CLIENTE', 'Cliente Final (Auto-atendimento)');

-- 2. Criar Permissões (Permissions)
INSERT INTO permission (name, description) VALUES
-- Admin
('user.manage', 'CRUD Usuários'),
('role.manage', 'CRUD Roles'),
('permission.manage', 'CRUD Permissões'),
('funcionario.manage', 'CRUD Funcionários e Gerentes'),
('transportadora.manage', 'CRUD Transportadoras'),
('modal.manage', 'CRUD Modais'),
('viagem.manage', 'CRUD Viagens'),
('cidade.manage', 'CRUD Cidades'),
('pdv.manage', 'CRUD PDVs'),
('logs.read', 'Visualizar Logs'),
('system.config', 'Configurações do sistema'),
('reserva.manage_all', 'Consultar e cancelar qualquer reserva'),
('ticket.emit_manual', 'Emitir tickets manualmente em casos excepcionais'),

-- Gerente
('reserva.online.read', 'Visualizar vendas/reservas online'),
('reserva.online.approve', 'Aprovar venda online'),
('reserva.online.reject', 'Recusar venda online'),
('integracao.retry', 'Reprocessar integração'),
('relatorio.emit', 'Emitir relatórios'),
('funcionario.assign', 'Autorizar funcionário a atuar no PDV'),
('funcionario.unassign', 'Remover autorização de funcionário do PDV'),
('pdv.read', 'Consultar equipe e dados do seu PDV'),
('vendas.read', 'Consultar vendas do PDV e fechar caixa'),

-- Funcionário
('reserva.create', 'Criar reserva'),
('reserva.read', 'Consultar reservas / pesquisar viagens'),
('pagamento.process', 'Receber pagamento presencial'),
('ticket.emit', 'Emitir ticket'),
('passageiro.create', 'Cadastrar passageiro'),

-- Transportadora
('modal.read', 'Consultar seus próprios modais'),
('modal.maintenance', 'Informar manutenção e atualizar status operacional'),
('integracao.receive', 'Receber vendas integradas e confirmar recebimento'),

-- Cliente
('reserva.self_manage', 'Criar, visualizar e cancelar apenas as suas próprias reservas'),
('user.self_update', 'Atualizar seu próprio cadastro'),
('viagem.read', 'Consultar viagens disponíveis');

-- 3. Vincular Permissões aos Perfis (Role <-> Permission)

-- ROLE_ADMIN: Todas as de admin (e também pode ter as básicas, mas para simplificar, listamos as mapeadas)
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p 
WHERE r.name = 'ROLE_ADMIN' 
  AND p.name IN (
    'user.manage', 'role.manage', 'permission.manage', 'funcionario.manage',
    'transportadora.manage', 'modal.manage', 'viagem.manage', 'cidade.manage',
    'pdv.manage', 'logs.read', 'system.config', 'reserva.manage_all', 'ticket.emit_manual'
  );

-- ROLE_GERENTE
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p 
WHERE r.name = 'ROLE_GERENTE' 
  AND p.name IN (
    'reserva.online.read', 'reserva.online.approve', 'reserva.online.reject',
    'integracao.retry', 'relatorio.emit', 'funcionario.assign',
    'funcionario.unassign', 'pdv.read', 'vendas.read', 'viagem.read'
  );

-- ROLE_FUNCIONARIO
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p 
WHERE r.name = 'ROLE_FUNCIONARIO' 
  AND p.name IN (
    'reserva.create', 'reserva.read', 'pagamento.process',
    'ticket.emit', 'passageiro.create', 'viagem.read'
  );

-- ROLE_TRANSPORTADORA
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p 
WHERE r.name = 'ROLE_TRANSPORTADORA' 
  AND p.name IN (
    'modal.read', 'modal.maintenance', 'integracao.receive'
  );

-- ROLE_CLIENTE
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM role r, permission p 
WHERE r.name = 'ROLE_CLIENTE' 
  AND p.name IN (
    'reserva.self_manage', 'user.self_update', 'viagem.read'
  );

-- 4. Criar Usuários para Cada Perfil (com senha 'password' em BCrypt)
-- Hash BCrypt para a senha 'password'
INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
('admin', '$2a$10$VIpHhMwfV8safyr5A7FtDuWb.WoA8FWz6.bLOLFNn6mmwI8aaL68O', true, true, true, true),
('gerente', '$2a$10$VIpHhMwfV8safyr5A7FtDuWb.WoA8FWz6.bLOLFNn6mmwI8aaL68O', true, true, true, true),
('funcionario', '$2a$10$VIpHhMwfV8safyr5A7FtDuWb.WoA8FWz6.bLOLFNn6mmwI8aaL68O', true, true, true, true),
('transportadora', '$2a$10$VIpHhMwfV8safyr5A7FtDuWb.WoA8FWz6.bLOLFNn6mmwI8aaL68O', true, true, true, true),
('cliente', '$2a$10$VIpHhMwfV8safyr5A7FtDuWb.WoA8FWz6.bLOLFNn6mmwI8aaL68O', true, true, true, true);

-- 5. Vincular Usuários aos Perfis
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.username = 'gerente' AND r.name = 'ROLE_GERENTE';

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.username = 'funcionario' AND r.name = 'ROLE_FUNCIONARIO';

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.username = 'transportadora' AND r.name = 'ROLE_TRANSPORTADORA';

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.username = 'cliente' AND r.name = 'ROLE_CLIENTE';
