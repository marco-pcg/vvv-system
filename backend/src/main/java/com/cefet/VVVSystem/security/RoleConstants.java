package com.cefet.VVVSystem.security;

public final class RoleConstants {

    private RoleConstants() {
        // Construtor privado para evitar instanciação
    }

    // ==========================================
    // PERFIS (ROLES)
    // ==========================================
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_GERENTE = "ROLE_GERENTE";
    public static final String ROLE_FUNCIONARIO = "ROLE_FUNCIONARIO";
    public static final String ROLE_TRANSPORTADORA = "ROLE_TRANSPORTADORA";
    public static final String ROLE_CLIENTE = "ROLE_CLIENTE";

    // ==========================================
    // PERMISSÕES GERAIS / ADMIN
    // ==========================================
    public static final String PERM_USER_MANAGE = "user.manage";
    public static final String PERM_ROLE_MANAGE = "role.manage";
    public static final String PERM_PERMISSION_MANAGE = "permission.manage";
    public static final String PERM_FUNCIONARIO_MANAGE = "funcionario.manage";
    public static final String PERM_TRANSPORTADORA_MANAGE = "transportadora.manage";
    public static final String PERM_MODAL_MANAGE = "modal.manage";
    public static final String PERM_VIAGEM_MANAGE = "viagem.manage";
    public static final String PERM_CIDADE_MANAGE = "cidade.manage";
    public static final String PERM_PDV_MANAGE = "pdv.manage";
    public static final String PERM_LOGS_READ = "logs.read";
    public static final String PERM_SYSTEM_CONFIG = "system.config";
    public static final String PERM_RESERVA_MANAGE_ALL = "reserva.manage_all";
    public static final String PERM_TICKET_EMIT_MANUAL = "ticket.emit_manual";

    // ==========================================
    // PERMISSÕES GERENTE
    // ==========================================
    public static final String PERM_RESERVA_ONLINE_READ = "reserva.online.read";
    public static final String PERM_RESERVA_ONLINE_APPROVE = "reserva.online.approve";
    public static final String PERM_RESERVA_ONLINE_REJECT = "reserva.online.reject";
    public static final String PERM_INTEGRACAO_RETRY = "integracao.retry";
    public static final String PERM_RELATORIO_EMIT = "relatorio.emit";
    public static final String PERM_FUNCIONARIO_ASSIGN = "funcionario.assign";
    public static final String PERM_FUNCIONARIO_UNASSIGN = "funcionario.unassign";
    public static final String PERM_PDV_READ = "pdv.read";
    public static final String PERM_VENDAS_READ = "vendas.read";

    // ==========================================
    // PERMISSÕES FUNCIONÁRIO
    // ==========================================
    public static final String PERM_RESERVA_CREATE = "reserva.create";
    public static final String PERM_RESERVA_READ = "reserva.read";
    public static final String PERM_PAGAMENTO_PROCESS = "pagamento.process";
    public static final String PERM_TICKET_EMIT = "ticket.emit";
    public static final String PERM_PASSAGEIRO_CREATE = "passageiro.create";

    // ==========================================
    // PERMISSÕES TRANSPORTADORA
    // ==========================================
    public static final String PERM_MODAL_READ = "modal.read";
    public static final String PERM_MODAL_MAINTENANCE = "modal.maintenance";
    public static final String PERM_INTEGRACAO_RECEIVE = "integracao.receive";

    // ==========================================
    // PERMISSÕES CLIENTE
    // ==========================================
    public static final String PERM_RESERVA_SELF_MANAGE = "reserva.self_manage";
    public static final String PERM_USER_SELF_UPDATE = "user.self_update";
    public static final String PERM_VIAGEM_READ = "viagem.read";
}
