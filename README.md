# Plataforma de Propostas e Assinaturas

## Arquitetura

A solução foi dividida em dois serviços independentes para simular um cenário de arquitetura distribuída e separar responsabilidades:

- **crm-service**
    - Responsável por gerenciar propostas comerciais
    - Inicia o fluxo de assinatura ao enviar a proposta para o serviço de assinatura

- **sign-service**
    - Responsável por gerenciar contratos e assinaturas
    - Processa a assinatura e notifica o CRM via callback quando o contrato é assinado

A comunicação entre os serviços ocorre via **HTTP REST**, com **callback** do SIGN para o CRM ao final do fluxo.
***

## Decisões Técnicas

### Separação em dois serviços (CRM e SIGN)
A separação simula uma arquitetura de microserviços, garantindo isolamento de responsabilidades e permitindo que cada serviço evolua de forma independente (ex.: escalabilidade, deployment, regras de negócio).

### Spring Boot + Maven
Escolhidos por serem amplamente utilizados no ecossistema Java, com suporte robusto para APIs REST, validações, testes e padronização de build.

### Idempotency-Key na criação de propostas
Utilizado no endpoint `POST /proposals` para evitar duplicidade em cenários comuns de retry (ex.: falhas de rede, timeouts, reenvio de requisição).

### Callback do SIGN para o CRM
Após a assinatura, o serviço SIGN notifica o CRM via callback (`POST /sign/callbacks/contract-signed`) para atualização do status da proposta e consistência do fluxo.

## Requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose (opcional, para execução via Compose)
- MySQL (caso execute local sem Docker)


***

## Como rodar o projeto:

### Opção 1: Docker Compose

Na raiz do projeto (api):

```bash
docker compose build
docker compose up -d
```

### Opção 2: Execução local

1. Suba um MySQL local e crie os bancos:

```bash
cd utils/scripts
./create-databases.sh
```

2. Execute o CRM:

```bash
cd crm-service
mvn spring-boot:run
```

3. Execute o SIGN:

```bash
cd sign-service
mvn spring-boot:run
```
***

## Swagger

- CRM Swagger UI: `http://localhost:8080/swagger-ui.html`
- SIGN Swagger UI: `http://localhost:8081/swagger-ui.html`

## Fluxo Principal

1. Criar proposta no CRM (`POST /proposals`) com header `Idempotency-Key`.
2. Enviar proposta para assinatura (`POST /proposals/{uuid}/send-to-signature`).
3. Consultar contrato no SIGN (`GET /contracts/{uuid}`).
4. Assinar contrato no SIGN (`POST /contracts/{uuid}/sign`).
5. SIGN envia callback para CRM (`POST /sign/callbacks/contract-signed`).
6. Consultar proposta assinada no CRM (`GET /proposals/{uuid}`).

***

## Testes

Rodar todos os testes:

```bash
mvn test
```

Rodar por módulo:

```bash
mvn -pl crm-service test
mvn -pl sign-service test
```

Os testes atuais são unitários (JUnit 5 + Mockito).


## Collection Postman para testes:

- `utils/postman/api_collection.json`
