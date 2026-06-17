# Issue T20 - Emissão de Ticket (RF07/RI08)

## Objetivo

Gerar automaticamente o ticket após a confirmação do pagamento.

## Dependências

* [x] T19 - Processamento de Pagamento

## Checklist

* [ ] Gerar número único do ticket
* [ ] Associar ticket à reserva
* [ ] Registrar data e informações da viagem
* [ ] Persistir ticket no banco
* [ ] Criar endpoint de consulta
* [ ] Criar testes

## Critério de aceite

* [ ] O ticket é emitido automaticamente após pagamento aprovado
* [ ] O ticket pode ser consultado pelo sistema