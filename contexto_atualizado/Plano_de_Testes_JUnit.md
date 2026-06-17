# Documentação de Testes - Sistema Vai&Volta Viagens (VVV)

## 1. Plano de Teste

### 1.1. Estratégia de Testes (Tipo de Teste Adotado)
O tipo de teste adotado para esta fase do projeto é o **Teste Unitário**. A estratégia consiste em testar o comportamento das funções essenciais das principais classes de domínio do sistema (regras de negócio) de forma isolada, sem dependência de banco de dados, rede ou interface gráfica. Foi utilizado o framework **JUnit** (linguagem Java) para a criação e execução automatizada dos casos de teste.

### 1.2. Classes de Teste e Classes Testadas
Abaixo estão listadas as classes principais de domínio que foram submetidas aos testes unitários, e suas respectivas classes de teste:
*   **Classe Testada:** `Reserva` -> **Classe de Teste:** `ReservaTest`
*   **Classe Testada:** `Ticket` -> **Classe de Teste:** `TicketTest`
*   **Classe Testada:** `Pagamento` (e suas extensões, como `CreditoPagamento`) -> **Classe de Teste:** `PagamentoTest`
*   **Classe Testada:** `Viagem` -> **Classe de Teste:** `ViagemTest`
*   **Classe Testada:** `Funcionario` -> **Classe de Teste:** `FuncionarioTest`

### 1.3. Mapa de Testes (Operações e Resultados Esperados)
A tabela a seguir apresenta o planejamento dos testes, indicando a operação a ser testada, o cenário e o resultado esperado, com base nas Regras de Negócio (RN) levantadas na análise de requisitos.

| Classe Testada | Operação Testada | Cenário | Resultado Esperado |
| :--- | :--- | :--- | :--- |
| `Reserva` | `calcularValorFinal()` | Passageiro tem entre 2 e 10 anos e possui acompanhante legal (RN14). | O valor retornado deve ser o valor original com **40% de desconto**. |
| `Reserva` | `confirmarPagamento()` | Processamento de um pagamento válido (RN12). | O status da reserva deve ser alterado de "Pendente" para **"Confirmada"**. |
| `Reserva` | `instanciarTicket()` | Tentativa de gerar ticket para reserva com status "Pendente" (RN12). | Deve lançar erro/exceção, pois o pagamento ainda não ocorreu. |
| `Ticket` | `emitirTicket()` | Instanciar um novo ticket válido (RN13). | O ticket deve ser gerado contendo código, assento e id da reserva obrigatoriamente. |
| `CreditoPagamento` | `processarTransacao()` | Pagamento parcelado em até 4 vezes (RN15). | O valor cobrado deve ser **sem juros** (exatamente igual ao valor original). |
| `CreditoPagamento` | `processarTransacao()` | Pagamento parcelado em 5 vezes ou mais (RN15). | O valor cobrado deve ter um **acréscimo de 5%**. |
| `Viagem` | `verificarDisponibilidadeCapacidade()` | Adicionar reserva quando a capacidade do modal já foi atingida (RN09 - Lei do Overbooking). | Deve retornar **falso** ou lançar exceção de limite de assentos atingido. |
| `Funcionario` | `registrarVinculoPonto()` | Adicionar um 3º ponto de venda físico a um funcionário (RN08). | Deve lançar exceção informando que o limite de 2 PDVs foi excedido. |

---

## 2. Relatório de Testes

### 2.1. Resultados Obtidos vs. Resultados Esperados
*A tabela abaixo reflete a execução real dos testes mapeados no JUnit.*

| Classe Testada | Operação Testada | Resultado Esperado | Resultado Obtido (JUnit) | Status |
| :--- | :--- | :--- | :--- | :--- |
| `Reserva` | `calcularValorFinal()` | Desconto de 40% aplicado corretamente. | Desconto de 40% aplicado e valor calculado corretamente. | ✅ Passou |
| `Reserva` | `confirmarPagamento()` | Status alterado para "Confirmada". | Status alterado para "Confirmada". | ✅ Passou |
| `Reserva` | `instanciarTicket()` | Exceção lançada ao tentar emitir sem pagamento. | Exceção controlada lançada conforme esperado. | ✅ Passou |
| `Ticket` | `emitirTicket()` | Atributos obrigatórios preenchidos. | Atributos validados e preenchidos no objeto. | ✅ Passou |
| `CreditoPagamento` | `processarTransacao()` | Valor sem juros (até 4x). | Valor cobrado sem juros com sucesso. | ✅ Passou |
| `CreditoPagamento` | `processarTransacao()` | Valor com 5% juros (5x ou mais). | Valor cobrado com acréscimo exato de 5%. | ✅ Passou |
| `Viagem` | `verificarDisponibilidadeCapacidade()`| Exceção/Falso por capacidade máxima. | Comportamento bloqueado corretamente por Overbooking. | ✅ Passou |
| `Funcionario` | `registrarVinculoPonto()` | Erro ao exceder 2 PDVs. | Erro tratado corretamente ao adicionar o 3º PDV. | ✅ Passou |

### 2.2. Reporte de Erros e Tratativas
Durante o desenvolvimento do código e a execução inicial dos testes no JUnit, alguns comportamentos anormais (bugs) foram encontrados e devidamente corrigidos na lógica de domínio. Abaixo listamos os principais erros reportados e como foram tratados:

1. **Erro no Cálculo de Desconto de Idade (Falha Lógica)**
   * **Sintoma do Erro:** No teste `calcularValorFinal()`, o JUnit acusou uma falha de asserção (*Assertion Error*). O desconto de 40% estava sendo aplicado mesmo para crianças sem acompanhante legal, o que viola a Regra de Negócio RN14.
   * **Foi tratado?** Sim.
   * **Tratativa:** Refatoramos o método na classe `Reserva` adicionando a verificação `if (passageiro.idade <= 10 && passageiro.isAcompanhado())` antes de aplicar o cálculo de desconto. O teste foi re-executado no JUnit e passou com sucesso.

2. **Erro de NullPointerException na Geração de Ticket**
   * **Sintoma do Erro:** Ao rodar o teste de `instanciarTicket()`, o console exibiu uma exceção `NullPointerException`. O problema ocorria porque o método tentava ler os dados de uma Reserva nula quando o Ticket era criado avulso.
   * **Foi tratado?** Sim.
   * **Tratativa:** Foi implementada uma validação no construtor do `Ticket` para verificar se os dados essenciais existem. Caso estejam vazios, lançamos uma `IllegalArgumentException` controlada. Isso corrigiu o problema em tempo de execução e fez o teste ficar verde.
