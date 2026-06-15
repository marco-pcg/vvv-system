@startuml
hide methods
hide stereotypes
skinparam linetype ortho
skinparam roundcorner 10
skinparam shadowing false
skinparam classAttributeIconSize 0

title Modelo Lógico (MER / DER) - Sistema de Reservas de Viagens

entity CLIENTE {
  * id_cliente
  --
  cpf {unique}
  nome
  cep
  data_nascimento
  email
}

entity PASSAGEIRO {
  * id_passageiro
  --
  cpf {unique}
  nome
  cep
  data_nascimento
  email
  possui_acompanhante
}

entity PONTO_DE_VENDA {
  * id_pdv
  --
  cnpj {unique}
  endereco
  gerente_responsavel_id
}

entity FUNCIONARIO {
  * id_funcionario
  --
  cpf {unique}
  nome
  cep
  data_nascimento
  email
  matricula {unique}
}

entity FUNCIONARIO_PDV {
  * id_funcionario_pdv
  --
  funcionario_id
  pdv_id
}

' --- Entidades RBAC ---
entity USER {
  * id_user
  --
  username {unique}
  password_hash
  ativo
  ultimo_login
}

entity ROLE {
  * id_role
  --
  nome {unique}
  descricao
}

entity PERMISSION {
  * id_permission
  --
  nome {unique}
  descricao
}

entity USER_ROLE {
  * id_user_role
  --
  user_id
  role_id
}

entity ROLE_PERMISSION {
  * id_role_permission
  --
  role_id
  permission_id
}

entity VIAGEM {
  * id_viagem
  --
  partida
  chegada
  status
  preco
}

entity RESERVA {
  * codigo {unique}
  --
  data_criacao
  status
  valor_final
}

entity TICKET {
  * numero {unique}
  --
  assento
}

entity CIDADE {
  * id_cidade
  --
  nome
  uf
}

entity TRANSPORTADORA {
  * id_transportadora
  --
  nome
  cnpj {unique}
}

entity MODAL {
  * id_modal
  --
  codigo {unique}
  tipo
  capacidade
  status_operacional
}

entity PAGAMENTO {
  * id_pagamento
  --
  valor
  data
  tipo
  status
}

entity PAGAMENTO_CREDITO {
  * id_pagamento
  --
  numero_cartao
  parcelas
}

entity PAGAMENTO_DEBITO {
  * id_pagamento
  --
  numero_cartao
}

entity PAGAMENTO_DINHEIRO {
  * id_pagamento
  --
  valor_recebido
  troco
}

' --- Relacionamentos do MER ---
CLIENTE ||--o{ RESERVA
PASSAGEIRO ||--o{ RESERVA
VIAGEM ||--o{ RESERVA
RESERVA ||--o| TICKET
RESERVA ||--o| PAGAMENTO

CIDADE ||--o{ VIAGEM : origem
CIDADE ||--o{ VIAGEM : destino

TRANSPORTADORA ||--o{ MODAL
MODAL ||--o{ VIAGEM

PAGAMENTO ||--|| PAGAMENTO_CREDITO
PAGAMENTO ||--|| PAGAMENTO_DEBITO
PAGAMENTO ||--|| PAGAMENTO_DINHEIRO

PONTO_DE_VENDA ||--o{ FUNCIONARIO_PDV
FUNCIONARIO ||--o{ FUNCIONARIO_PDV
FUNCIONARIO ||--o{ PONTO_DE_VENDA : gerenteResponsavel

' --- Relacionamentos RBAC ---
CLIENTE |o--o| USER
FUNCIONARIO ||--|| USER
USER ||--o{ USER_ROLE
ROLE ||--o{ USER_ROLE
ROLE ||--o{ ROLE_PERMISSION
PERMISSION ||--o{ ROLE_PERMISSION

@enduml