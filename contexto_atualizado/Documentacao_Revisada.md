# CEFET/RJ – Centro Federal de Educação Tecnológica Celso Suckow da Fonseca 
SI – Sistemas de Informação 
Análise e Modelagem de Sistemas 
          
**Estudo de Caso: VVV**

Rio de Janeiro – RJ  
Outubro - 2025 

**Equipe / Grupo:** André de Martin Guiot, João Victor Tavares Froes, Marco Antonio Lira Barros, Miguel Rodrigues Rios da Silva e Romario Ferreira Euzebio. 

---

## Sumário 
1. Introdução
   1.1. Sobre o trabalho
   1.2. Sobre as atividades desenvolvidas
   1.3. Sobre a interação do grupo
   1.4. Sobre a divisão das tarefas
   1.5. Sobre as fases de desenvolvimento
2. Sobre a Análise - Apresentação teórica e prática
   2.1. Especificação de Requisitos
      2.1.1. Regras de Negócio
      2.1.2. Definição de Prioridade
      2.1.3. Pós-Condições e Pré-condições de Requisitos
   2.2. Requisitos Funcionais
   2.3. Requisitos Não-Funcionais
   2.4. Requisitos Inversos
3. Análise e Modelagem
   3.1. Casos de Uso
   3.2. Diagrama de Classes
   3.3. Diagrama de Sequência
   3.4. Diagrama de Atividade
4. Considerações Finais
5. Referências Bibliográficas

---

## 1. INTRODUÇÃO 
Este documento apresenta a análise e especificação detalhada dos requisitos para o novo Sistema de Controle e Vendas de Passagens (SCVP) da Vai&Volta Viagens (VVV), com base no estudo de caso fornecido. 
Os requisitos compreendem as especificações que o Sistema VVV deverá atender para suprir as necessidades do cliente e atender aos padrões de qualidade e previsibilidade dos desenvolvedores, a fim de facilitar sua manutenção. 

### 1.1. Sobre o trabalho 
Este estudo não aborda senão a análise do novo sistema da VVV, contemplando regras de negócio e especificação de requisitos (funcionais, não-funcionais e inversos). Ao final, são descritos os pontos de foco do trabalho em grupo, especificando as atividades dos membros integrantes, a organização adotada e os resultados obtidos. 

### 1.2. Sobre as atividades desenvolvidas 
As atividades realizadas pelo grupo, além daquelas especificadas pela análise, a elaboração mesma desta documentação foi fundamental. Nesse sentido, alguns tiveram a função específica de documentar os processos feitos e de escrevê-los. 
Como mais da metade do grupo lidou com conceitos semelhantes da análise solicitada, foram criados documentos de análise repetidos (um para cada membro), sendo que estes, ao compor a documentação, foram refinados e deles se extraiu a análise considerada mais correta. 

### 1.3. Sobre a interação do grupo 
Os meios de interação mais propícios para a interação do grupo foram os programas Whatsapp e o Discord. 
Sobre a interação no fluxo de produção, o grupo utilizou um repositório compartilhado no Google Drive, onde cada membro podia visualizar os arquivos e pastas em comum. 

### 1.4. Sobre a divisão das tarefas 
A divisão de tarefas abarcou atividades como: 
- organização de pastas e arquivos do repositório; 
- elaboração de itens sobre o grupo na documentação; 
- elaboração dos documentos de análise solicitados; 
- integração dos documentos em sua versão final. 

A equipe dividiu-se em: 
- dois integrantes responsáveis pela documentação; 
- dois integrantes responsáveis pela análise; 
- um integrante responsável pela organização da equipe. 

### 1.5. Sobre as fases de desenvolvimento 
A fase inicial ocorreu nos Meetups no Discord. Seguindo da primeira reunião, os integrantes responsáveis por elaborar a análise produziram seus documentos e, passado uma semana, foi feita outra reunião para comparar e estudar a viabilidade. Feito o desenvolvimento, o processo de conclusão e refinamento seguiu na última semana. 
  
---

## 2. SOBRE A ANÁLISE - APRESENTAÇÃO TEÓRICA E PRÁTICA 
Os requisitos compreendem as especificações que o Sistema VVV deverá atender para suprir as necessidades da agência VVV. 

### 2.1. Especificação de Requisitos 
Os requisitos abrangem categorias diversas: 
- **Funcionais:** Aqueles sem os quais o sistema não funciona.  
- **Não-Funcionais:** Impõem restrições quanto à tecnologia, banco de dados, legislação, etc. 
- **Inversos:** Descrevem rotinas de suporte e mecanismos técnicos internos.

#### 2.1.1. Regras de Negócio 
As regras de negócio são as normas, políticas e procedimentos que guiam as operações da empresa. 
*Definição: RN significa “Regra de Negócio”.*

*   **RN01.** Cada modal pertence a uma única companhia transportadora. 
*   **RN02.** Cada modal tem um tipo, capacidade, modelo e ano de fabricação. 
*   **RN03.** Modais em manutenção não podem ser utilizados em viagens. 
*   **RN04.** Uma viagem pode utilizar um ou mais modais. 
*   **RN05.** A viagem pode ter escalas em uma ou várias cidades. 
*   **RN06.** Cada cidade tem um código e um identificador de 3 letras. 
*   **RN07.** Para viagens aéreas, é obrigatório o código do aeroporto. 
*   **RN08.** Não é permitido overbooking – as passagens só podem ser vendidas até a capacidade total. 
*   **RN09.** Uma reserva é obrigatória antes da venda. 
*   **RN10.** Uma reserva é de um único passageiro apenas. 
*   **RN11.** Um passageiro pode ter várias reservas diferentes. 
*   **RN12.** A reserva só é confirmada após pagamento (O sistema não deve emitir ticket sem pagamento). 
*   **RN13.** Após pagamento confirmado, o ticket (bilhete) é gerado com informações obrigatórias. 
*   **RN14.** Crianças entre 2 e 10 anos têm 40% de desconto (se acompanhadas de um adulto > 21 anos). 
*   **RN15.** Pagamento com crédito: até 4x sem juros, acima de 4x com 5% de juros. 
*   **RN16.** A confirmação do pagamento online é feita automaticamente pela operadora. 
*   **RN17.** Funcionários podem atuar em até dois pontos de venda diferentes com autorização. 
*   **RN18.** Cada ponto de venda possui um gerente responsável. 
*   **RN19.** As vendas online são aprovadas e gerenciadas pelo gerente de negócios virtuais. 
*   **RN20.** As vendas online devem ser processadas e os dados transferidos às transportadoras. 
*   **RN21.** O sistema deve emitir logs sempre que realizar uma funcionalidade crítica. 
*   **RN22.** Um funcionário é cadastrado por um administrador. 
*   **RN23.** Os métodos de pagamento suportados são: débito, crédito e dinheiro-vivo. 

#### 2.1.2. Definição de Prioridade 
A prioridade dos requisitos foi definida utilizando o método MoSCoW (Precisa ter, Deveria ter, Poderia ter, Não vai ter).

#### 2.1.3. Pós-Condições e Pré-condições de Requisitos 
Pré-condição é o que precisa ser validado antes; Pós-condição é o estado final do sistema após a execução da funcionalidade.

### 2.2. Requisitos Funcionais 

**RF01 - Cadastrar Modal** 
*   **Pré-condições:** Usuário logado como "Administrador". Companhia já cadastrada. 
*   **Descrição:** Permitir o cadastro de modais (aviões, trens, etc). 
*   **Pós-condições:** Modal salvo na base e disponível. 
*   **Prioridade:** Precisa ter. 

**RF02 - Registrar Manutenção** 
*   **Pré-condições:** O modal deve existir. Usuário com permissão. 
*   **Descrição:** O sistema deve registrar e gerenciar o período de manutenção de modais. 
*   **Pós-condições:** Status alterado para "Em Manutenção" e indisponível para vendas. 
*   **Prioridade:** Precisa ter. 

**RF03 - Cadastrar Viagem** 
*   **Pré-condições:** Cidades e modais já cadastrados e operacionais. 
*   **Descrição:** Permitir o cadastro de viagens com rota definida. 
*   **Pós-condições:** Viagem criada. 
*   **Prioridade:** Precisa ter. 

**RF04 - Cadastrar Cidades** 
*   **Pré-condições:** Administrador logado. 
*   **Descrição:** Permitir o cadastro de cidades e aeroportos. 
*   **Pós-condições:** Cidade registrada. 
*   **Prioridade:** Precisa ter. 

**RF05 - Cadastrar Reserva** 
*   **Pré-condições:** Viagem com capacidade disponível. 
*   **Descrição:** Permitir o cadastro de reservas temporárias. 
*   **Pós-condições:** Reserva "Pendente de Pagamento". Assento temporário retido. 
*   **Prioridade:** Precisa ter. 

**RF06 - Processar Pagamento** 
*   **Pré-condições:** Reserva pendente. 
*   **Descrição:** Processar pagamentos via cartão ou dinheiro aplicando taxas. 
*   **Pós-condições:** Reserva alterada para "Confirmada". 
*   **Prioridade:** Precisa ter. 

**RF07 - Emitir Ticket** 
*   **Pré-condições:** Reserva com status "Confirmada". 
*   **Descrição:** Emitir os tickets contendo identificador único. 
*   **Pós-condições:** Ticket disponibilizado ao passageiro. 
*   **Prioridade:** Precisa ter. 

**RF08 - Cadastrar Passageiro** 
*   **Pré-condições:** Dados pessoais validados. 
*   **Descrição:** Permitir o cadastro de clientes e passageiros dependentes (menores). 
*   **Pós-condições:** Passageiro apto a receber reservas. 
*   **Prioridade:** Precisa ter. 

**RF09 - Cadastrar Ponto de Venda** 
*   **Pré-condições:** CNPJ não existente na base. 
*   **Descrição:** Permitir o cadastro das lojas físicas (PDV). 
*   **Pós-condições:** PDV registrado. 
*   **Prioridade:** Precisa ter. 

**RF10 - Atribuir Gerente a PDV** 
*   **Pré-condições:** PDV já cadastrado. 
*   **Descrição:** Associar um gerente como responsável técnico pelo Ponto de Venda. 
*   **Pós-condições:** PDV com gerente atribuído. 
*   **Prioridade:** Precisa ter. 

**RF11 - Aprovar Venda Online** 
*   **Pré-condições:** Reservas com status "Aguardando Aprovação". 
*   **Descrição:** Interface para que o gerente de negócios virtuais aprove as vendas feitas pelo site. 
*   **Pós-condições:** Venda efetivada e dados repassados à transportadora. 
*   **Prioridade:** Precisa ter. 

**RF12 - Cadastrar Funcionário** 
*   **Pré-condições:** Administrador logado. 
*   **Descrição:** Permitir o cadastro dos dados de um funcionário na base. 
*   **Pós-condições:** Novo funcionário criado. 
*   **Prioridade:** Precisa ter. 

**RF13 - Alocar Funcionário em PDV** 
*   **Pré-condições:** Funcionário já cadastrado no sistema. 
*   **Descrição:** Atribuir um funcionário a postos de trabalho (limite de 2 PDVs). 
*   **Pós-condições:** Funcionário habilitado a operar naquele PDV. 
*   **Prioridade:** Precisa ter. 

**RF14 - Aplicar Desconto** 
*   **Pré-condições:** Passageiro entre 2 e 10 anos + Acompanhante legal. 
*   **Descrição:** O sistema deve calcular automaticamente a dedução no valor da passagem. 
*   **Pós-condições:** Reserva precificada com 40% de desconto. 
*   **Prioridade:** Deveria ter. 

### 2.3. Requisitos Não-Funcionais 
*   **RNF1 - Segurança:** O sistema deve garantir a segurança dos dados com criptografia. 
*   **RNF2 - Usabilidade:** A interface deve ser amigável e acessível em dispositivos móveis. 
*   **RNF3 - Escalabilidade:** Suportar o crescimento de usuários da VVV. 
*   **RNF4 - Integração:** Integração via API com operadoras de cartões e transportadoras. 
*   **RNF5 - Backup:** Backup automático com tempo de recuperação de até 4 horas. 
*   **RNF6 - Compatibilidade:** Compatível com os principais navegadores. 
*   **RNF7 - Conformidade Legal (LGPD):** Estar em conformidade com as regulamentações de proteção de dados. 
*   **RNF8 - Documentação:** Documentação técnica completa. 
*   **RNF9 - Desempenho:** Consultas em menos de 3s; suportar 500 usuários simultâneos. 
*   **RNF10 - Disponibilidade:** 24/7 (uptime mínimo). 
*   **RNF11 - Manutenibilidade:** Permitir atualizações sem downtime brusco. 
*   **RNF12 - Legislação Anti-overbooking:** O sistema é terminantemente obrigado por lei a impedir vendas acima da capaVcidade física registrada no Modal (Regra de Negócio de caráter legal).

### 2.4. Requisitos Inversos 
*   **RI01:** Rotina técnica em background para verificação de lock de assento. 
*   **RI02:** Rotina de cálculo de juros dinâmico em caso de parcelamento. 
*   **RI03:** Rotina algorítmica para cruzamento de idades na validação de menores viajando. 
*   **RI04:** Processo em lotes (batch) de confirmação automática de pagamentos da operadora. 
*   **RI05:** Job de transferência agendada de dados de vendas às companhias transportadoras via webservice. 

---

## 3. Análise e Modelagem 

### 3.1. Casos de Uso 
O sistema VVV modelado contempla atores como Cliente, Passageiro, Funcionário, Gerente, Administrador e Transportadora. 
*Observação:* No fluxo principal corrigido, o ator "Passageiro" age distintamente do "Cliente" (que efetua o pagamento). Além disso, dependências lógicas foram reajustadas (ex: a função de "Calcular Desconto" age como um `<<extend>>` opcional do caso de uso de "Processar Pagamento").

### 3.2. Diagrama de Classes 
O Diagrama de Classes detalha o Domínio da agência VVV. 
Nele encontram-se as abstrações de `Pessoa` (generalizando Cliente, Funcionário e Gerente), e a cadeia de serviços estruturais com as classes lógicas independentes (como a interface `Pagamento` orientada por Polimorfismo, `Reserva`, `Ticket`, e `Viagem`). A classe Passageiro relaciona-se com a Reserva garantindo a atribuição da cobrança.

### 3.3. Diagrama de Sequência 
Os Diagramas de Sequência foram inteiramente remodelados sob a perspectiva da **Lógica de Domínio**. O foco puramente processual (Classes Controladoras genéricas e Views estáticas) foi substituído para evidenciar as trocas de mensagens diretas entre os objetos reais do negócio (Ex: a classe `Viagem` questionando o estado da classe `Modal`; a entidade `Reserva` emitindo `Tickets`). 

### 3.4. Diagrama de Atividade 
O Diagrama de Atividade detalha o fluxo percorrido visualmente pelo usuário (visão do Cliente no site e Visão da Gerência/Física).

---

## 4. Considerações Finais 
A especificação de requisitos é etapa viva da construção de um projeto, ou seja, é feita antes, durante e depois. Porque os requisitos mudam com o tempo, o código programado também muda; daí resulta que manutenção e escalonamento do sistema variam. Sem especificação de requisitos, não tem produto, nem diálogo com o cliente.

## 5. Referências Bibliográficas 
- DEVMEDIA. Trabalhando com Engenharia de Requisitos. Rio de Janeiro: 2025. 
- IBM. What Are Business Rules? Armonk: IBM, 2025.
- RIBEIRO, Sildenir Alves. Material didático AMS. CEFET/RJ, 2025.
