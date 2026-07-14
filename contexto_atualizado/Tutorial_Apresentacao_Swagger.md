# Roteiro de Apresentação: Demonstrando o Fluxo de Compra no Swagger

Como o sistema backend não possui telas (frontend) prontas, o Swagger UI será a interface gráfica pela qual vocês apresentarão o funcionamento do código para o professor. 

Este roteiro detalhado possui todos os *Payloads* (JSONs) que você deve copiar e colar no Swagger para a apresentação rodar lisa, sem erros de digitação na hora.

> [!IMPORTANT]
> A variável `IGNORE_PERMISSION` no arquivo `.env` já está definida como `false`. Isso significa que todos os endpoints exigirão autenticação (Token JWT), exatamente como num sistema real em produção.

---

## 🚀 Passo 1: Iniciando a Aplicação e o Swagger
1. Certifique-se de que o **Docker** com o banco de dados PostgreSQL está rodando.
2. Inicie a aplicação Spring Boot (pela IDE).
3. Abra o navegador e acesse a interface do Swagger:
   * **URL:** `http://localhost:8080/swagger-ui/index.html`

---

## 🔐 Passo 2: Demonstrando a Autenticação (Login)
*Aqui mostramos a Segurança (Spring Security) em ação.*

1. No Swagger, localize a aba do **AuthController** e expanda-a.
2. Clique no endpoint azul **`POST /auth/login`**.
3. Clique no botão **"Try it out"**.
4. No campo de *Request body*, cole o JSON abaixo *(assumindo que "admin" e "123" são suas credenciais pré-cadastradas)*:
   ```json
   {
     "username": "admin", 
     "password": "123"
   }
   ```
5. Clique em **Execute**.
6. No **Response body** retornado (código 200), **copie a string inteira do campo `token`**.
7. Suba a página até o topo do Swagger e clique no botão verde com cadeado **"Authorize"**.
8. Cole o token copiado escrevendo a palavra `Bearer ` antes (Ex: `Bearer eyJhbGciOiJIUz...`). Clique em **Authorize** e feche.
*O cadeado em todos os endpoints ficará fechado, indicando que você tem acesso livre.*

---

## 🛠️ Passo 3: Cadastrando Dados Base (Opcional, mas impressiona)
*Se o banco de dados estiver zerado, mostre o MVC funcionando ao cadastrar dados em diferentes controllers.*

**A. Cadastrar Cliente / Passageiro (`POST /api/passageiros`)**
```json
{
  "nome": "João Silva",
  "cpf": "111.222.333-44",
  "email": "joao@email.com",
  "dataNascimento": "1990-01-01",
  "isAcompanhado": true
}
```

**B. Cadastrar Modal (`POST /api/modais`)**
```json
{
  "modelo": "Ônibus Leito Marcopolo",
  "capacidade": 40,
  "statusModal": "DISPONIVEL"
}
```

**C. Cadastrar Viagem (`POST /api/viagens`)**
```json
{
  "origemId": 1,
  "destinoId": 2,
  "modalId": 1,
  "valorOriginal": 100.0,
  "dataSaida": "2026-10-10T08:00:00"
}
```

---

## 🛒 Passo 4: O Coração do Sistema - Venda Online (Regras de Pagamento)
Vá na aba do **VendaOnlineController**, clique em **`POST /api/vendas-online/solicitar`** e em **"Try it out"**. 

### Cenário 1: Pagamento no Cartão de Crédito com Juros (RN15)
Se o pagamento em crédito exceder 4 parcelas, há acréscimo de 5%.

**JSON Body:**
```json
{
  "reserva": {
    "idViagem": 1,
    "idCliente": 1,
    "idPassageiro": 1
  },
  "tipoPagamento": "CREDITO",
  "numeroCartao": "1234567890123456",
  "parcelas": 5
}
```
**Apresentando o Resultado:**
Ao clicar em **Execute**, aponte: *"Como compramos parcelado em 5 vezes, o Service aplicou 5% de juros em cima do valor, a reserva confirmou e os tickets já foram instanciados."*

### Cenário 2: Pagamento à Vista no Dinheiro (Desconto de 10%)
**JSON Body:**
```json
{
  "reserva": {
    "idViagem": 1,
    "idCliente": 1,
    "idPassageiro": 2
  },
  "tipoPagamento": "DINHEIRO",
  "numeroCartao": "",
  "parcelas": 1
}
```
**Apresentando o Resultado:**
*"O MVC acionou a Strategy correta para dinheiro, tirando 10% do valor final sem acoplamentos"* e exiba novamente a emissão instantânea do Ticket da Reserva.

---

## 🚫 Passo 5: Demonstrando as Regras de Exceção e Bloqueios
Para mostrar que o seu sistema é seguro contra falhas, simule tentativas ilegais.

### Cenário 3: Regra do Desconto para Criança Acompanhada (RN14)
Mostre que o valor final cai absurdamente (40%) se o passageiro for criança.
Para isso, vá rapidamente no **PassageiroController** (`POST /api/passageiros`) e crie uma criança:
```json
{
  "nome": "Enzo Menino",
  "cpf": "999.888.777-66",
  "email": "enzo@email.com",
  "dataNascimento": "2020-01-01",
  "isAcompanhado": true
}
```
Volte ao VendaOnlineController e faça a compra com esse ID novo (ex: idPassageiro: 3) em dinheiro. O valor despencará devido aos **10% do Dinheiro + 40% da Idade**.

### Cenário 4: Lei do Overbooking (RN09)
Mostre que o seu sistema impede a reserva se não houver vagas. 
*(Para simular rápido: Cadastre um `Modal` com Capacidade = 1, vincule a uma Viagem e tente vender para dois passageiros diferentes na mesma viagem. A segunda requisição deverá ser barrada com Erro 400 ou 409!)*.

### Cenário 5: Tentativa de Emissão de Ticket sem Pagamento
Se o professor perguntar: *"E se alguém for lá no controller do Ticket e tentar criar o ticket diretamente burlando o pagamento?"*
Vá no **TicketController** (`POST /api/tickets`), aponte para um ID de uma Reserva que não está Paga/Confirmada e mostre o sistema devolvendo **ERRO** exigindo a confirmação prévia!

---

### Dica de Ouro:
Deixe a IDE aberta na classe `VendaOnlineService` ou em `CreditoPagamento`. Enquanto o Swagger processa, mostre o código responsável. Isso valida o trabalho que foi desenvolvido no Backend.
