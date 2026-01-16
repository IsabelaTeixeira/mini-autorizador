
# Mini autorizador

Mini-autorizador de transações desenvolvido em Spring Boot, responsável por criar cartões, consultar saldo e autorizar
transações de acordo com regras de negócio definidas.

## Funcionalidades
Criação de cartões com saldo inicial de R$500,00.
Consulta de saldo de cartões existentes.
Autorização de transações com validação de regras de negócio (cartão existente, senha correta, saldo suficiente).
Segurança via HTTP Basic Auth.
Controle de concorrência usando JPA para garantir consistência do saldo.

## Tecnologias
Java + Spring Boot
Spring Data JPA
Spring Security (Basic Auth)
MySQL
Testes com JUnit 5 e Mockito

## Endpoints
**Criar Cartão**

POST /cartoes
Body (JSON):
{
  "numeroCartao": "6549873025634501",
  "senha": "1234"
}

Auth: Basic (username/password)

Respostas:
201 → Cartão criado
422 → Cartão já existe
401 → Falha de autenticação

**Consultar Saldo**

GET /cartoes/{numeroCartao}
Auth: Basic (username/password)

**Realizar Transação**

POST /transacoes
Body:
{
  "numeroCartao": "6549873025634501",
  "senha": "1234",
  "valor": 10.00
}

Auth: Basic Auth (username/password)

Respostas:
201 → Transação OK
422 → CARTAO_INEXISTENTE / SENHA_INVALIDA / SALDO_INSUFICIENTE
401 → Falha de autenticação

## Como rodar
Configurar o application.yml com seu banco
Rodar a aplicação (mvn spring-boot:run)
Testar os endpoints no Postman com Basic Auth (username / password)
