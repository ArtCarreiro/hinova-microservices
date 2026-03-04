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
- A separação simula uma arquitetura de microserviços, garantindo isolamento de responsabilidades e permitindo que cada serviço evolua de forma independente (ex.: escalabilidade, deployment, regras de negócio).

### Spring Boot + Maven
-  Escolhidos por serem amplamente utilizados no ecossistema Java, com suporte robusto para APIs REST, validações, testes e padronização de build.

### Idempotency-Key na criação de propostas
- Utilizado no endpoint `POST /proposals` para evitar duplicidade em cenários comuns de retry (ex.: falhas de rede, timeouts, reenvio de requisição).

### Callback do SIGN para o CRM via REST
- Após a assinatura, o serviço SIGN notifica o CRM via callback (`POST /sign/callbacks/contract-signed`) para atualização do status da proposta e consistência do fluxo. 
Essa abordagem é simples e eficaz para comunicação entre serviços, garantindo que o CRM seja informado sobre o resultado da assinatura.

### Docker Compose
- Utilizado para facilitar a execução dos serviços e do banco de dados MySQL em um ambiente isolado, garantindo consistência entre ambientes de desenvolvimento e testes.

### JUnit 5 + Mockito para testes
- Escolhidos por serem padrões de mercado para testes unitários em Java, permitindo fácil criação de mocks e validação de comportamentos em isolamento.

### SOLID e boas práticas de design
- O código foi estruturado seguindo os princípios SOLID para garantir alta coesão, baixo acoplamento e facilidade de manutenção. 
Utilização de camadas (Controller, Service, Repository) para organizar responsabilidades e facilitar testes.

### Arquitetura RESTful
- Os endpoints foram projetados seguindo as convenções REST, utilizando verbos HTTP adequados (POST para criação, GET para consulta, PATCH para atualização) e recursos bem definidos (proposals, contracts).

## Resalvas:

- Há uma branch chamada `origin/Kafka` onde foi implementada uma versão utilizando Kafka para comunicação assíncrona entre os serviços. 
A branch principal (main) utiliza comunicação síncrona via REST para simplicidade e clareza do fluxo.

- O foco do projeto foi na implementação do fluxo principal e na demonstração de boas práticas, portanto, algumas funcionalidades adicionais 
(ex.: autenticação, autorização, monitoramento) foram deixadas de fora para manter o escopo gerenciável.

- Uso de cache para otimizações de desempenho foi considerado fora do escopo para esta implementação, visto que se trata de um projeto de demonstração focado no fluxo de assinatura e comunicação entre serviços.


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
