# Contexto de Domínio para Agentes de IA (Desenvolvimento)

**[SYSTEM INSTRUCTION]**
Sempre que for gerar código, arquitetar soluções ou implementar *issues* para o projeto "Sistema VVV" (Vai&Volta Viagens), você DEVE basear suas respostas rigorosamente neste contexto. Não alucine lógicas de negócio que fujam das regras abaixo e siga estritamente os artefatos de modelagem já validados.

---

## 🤖 1. Glossário e Restrições de Entidades (Linguagem Ubíqua)
- **Cliente:** Aquele que faz login e realiza o pagamento.
- **Passageiro:** Aquele que viaja de fato. Ao aplicar a regra de desconto (ex: menor de idade), o sistema deve avaliar os dados do *Passageiro*, não do Cliente.
- **Viagem e Roteiro:** Relação `1 -> N`. A viagem não tem variáveis simples de "origem" e "destino", ela possui relacionamentos com múltiplas `Cidades` para suportar Escalas.
- **Reserva e Ticket:** A `Reserva` é a intenção (carrinho). O `Ticket` só pode ser instanciado e agregado à Reserva **após** a confirmação bem-sucedida do `Pagamento`.
- **Modal:** O veículo da transportadora. Se o `StatusOperacional` for "Em Manutenção", viagens que o utilizem devem disparar alertas/bloqueios.

---

## ⚙️ 2. Regras de Negócio (RN) Inegociáveis no Código
1. **RN09 (Overbooking):** O método `verificarDisponibilidadeCapacidade()` na classe `Viagem` deve conter a lógica da "lei do overbooking" (matemática de capacidade do Modal).
2. **RN08 (Limite de PDV):** Um `Funcionario` só atua em, no máximo, 2 Pontos de Venda (PDV). A validação `contarPontosAutorizadosAtuais() <= 2` é obrigatória na rotina de vinculação feita pelo Gerente.
3. **Padrão Arquitetural DDD:** É expressamente proibido criar *Anemic Domain Models*. A lógica matemática (ex: `calcularValorFinal()`, `aplicarDesconto()`) deve ser encapsulada em métodos dentro das Entidades de Domínio, e não em Services genéricos ou Controladores de API.

---

## 📂 3. Fontes da Verdade (Onde buscar a estrutura do código)
Antes de escrever código SQL ou de Classes, utilize as seguintes fontes como gabarito:

*   **Para Modelagem Relacional:** Leia o arquivo `ModelagemFISBDFeitaPeloMeuAmigo.md`. Respeite os tipos (`VARCHAR`, `INT`) e, principalmente, as FKs (ex: `Reserva` carrega o `id_viagem`).
*   **Para Estrutura Orientada a Objetos:** Leia o arquivo `Diagrama_de_Classes.txt`. Ele dita o polimorfismo de pagamentos e a herança das Pessoas (`Cliente` e `Passageiro` lado a lado herdando de `Pessoa`).
*   **Para Chamadas de API e Fluxo Lógico:** Leia os arquivos na pasta `Diagramas De Sequência/`. As chamadas síncronas/assíncronas lá descritas são a documentação exata de quais métodos a interface deve chamar e quais entidades vão interagir.
