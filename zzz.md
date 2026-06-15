# Adequação do Sistema VVV às Regras de Negócio Oficiais

O objetivo deste plano é limpar o código de todas as lógicas e entidades criadas fora dos parâmetros estipulados pelos Requisitos Funcionais e Regras de Negócio (`Documentacao_Revisada.md`).

## User Review Required

> [!WARNING]
> A remoção do sistema RBAC (Role, Permission, e suas tabelas associativas) exigirá uma refatoração da camada de Autenticação/Segurança (`SecurityConfig`, `TokenService`, `AuthController`). Será implementada apenas a autenticação básica com os papéis de `Administrador`, `Gerente` e `Funcionario`.

> [!IMPORTANT]
> A refatoração da classe `Viagem` para suportar Múltiplos Modais (RN04) e Escalas (RN05) representará uma mudança estrutural no banco de dados e nos relacionamentos ORM do JPA.

## Open Questions

> [!CAUTION]
> 1. **Banco de Dados**: Ao substituir o meio de pagamento **PIX** pelo correto **Dinheiro Vivo** (RN23), e ao remover a estrutura excessiva de permissões RBAC, você prefere que seja criado um novo arquivo de migração (SQL) e dropar as tabelas antigas, ou posso reescrever os scripts de SQL de criação originais (já que o sistema ainda está em desenvolvimento inicial)?
> 2. **Validação do Acompanhante**: A RN14 exige que a criança esteja acompanhada de um adulto *maior de 21 anos*. Atualmente o passageiro só tem um boolean `possuiAcompanhante`. Gostaria que adicionássemos o ID do acompanhante real na reserva para conferir a idade, ou podemos assumir na validação do Controller que o fato de o boolean estar 'true' já indica presença de um adulto > 21 anos?

## Proposed Changes

### 1. Refatoração de Regras de Desconto e Pagamento

#### [MODIFY] [DescontoService.java](file:///c:/Workspace/VVV-System/vvv-system/src/main/java/com/cefet/VVVSystem/service/DescontoService.java)
- Remover todas as regras de 100%, 50% e 25% para faixas etárias variadas.
- Implementar **exclusivamente a RN14**: desconto único de 40% apenas para idades entre 2 e 10 anos, caso o passageiro esteja acompanhado.

#### [DELETE] [PagamentoPix.java](file:///c:/Workspace/VVV-System/vvv-system/src/main/java/com/cefet/VVVSystem/domain/entity/PagamentoPix.java)
#### [DELETE] [PagamentoPixStrategy.java](file:///c:/Workspace/VVV-System/vvv-system/src/main/java/com/cefet/VVVSystem/strategy/PagamentoPixStrategy.java)
- Exclusão do método de pagamento inventado que não consta na RN23.

#### [NEW] `PagamentoDinheiroVivo.java`
#### [NEW] `PagamentoDinheiroVivoStrategy.java`
- Criação do método de pagamento em espécie exigido pela **RN23**.

### 2. Correções de Atributos Faltantes nas Entidades

#### [MODIFY] [Modal.java](file:///c:/Workspace/VVV-System/vvv-system/src/main/java/com/cefet/VVVSystem/domain/entity/Modal.java)
- Adicionar os atributos exigidos pela **RN02**: `String modelo` e `Integer anoFabricacao`.

#### [MODIFY] [Cidade.java](file:///c:/Workspace/VVV-System/vvv-system/src/main/java/com/cefet/VVVSystem/domain/entity/Cidade.java)
- Adicionar os atributos exigidos pela **RN06/RN07**: `String codigo`, `String identificador` (validação de 3 letras), e `String codigoAeroporto` (para modais aéreos).

### 3. Correção Estrutural de Viagem (Escalas e Modais Múltiplos)

#### [MODIFY] [Viagem.java](file:///c:/Workspace/VVV-System/vvv-system/src/main/java/com/cefet/VVVSystem/domain/entity/Viagem.java)
- Alterar a associação de Modal de único (`@ManyToOne`) para múltiplo: `@ManyToMany private Set<Modal> modais;` (RN04).
- Alterar o mapeamento de destino/origem para suportar uma lista ordenada de cidades/escalas (RN05). 

### 4. Limpeza da Arquitetura Extrapolada (RBAC Excessivo)

#### [DELETE] Entidades associadas às permissões
- Excluir classes e repositórios de `Role`, `Permission`, `RolePermission`.
- Simplificar o `User` para usar um `Enum` básico de Perfil.

## Verification Plan

### Automated Tests
- Executar `mvn test` após as remoções estruturais do PIX e das permissões extras.
- Adaptar/atualizar os testes unitários afetados no [DescontoServiceTest.java](file:///c:/Workspace/VVV-System/vvv-system/src/test/java/com/cefet/VVVSystem/service/DescontoServiceTest.java) para validarem estritamente a faixa de desconto de 40% (RN14).

### Manual Verification
- Validar se a geração do schema JPA no console obedece às alterações estruturais nas tabelas (criação correta do DinheiroVivo, das novas colunas na Cidade/Modal e das escalas de Viagem).