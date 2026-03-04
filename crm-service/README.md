# CRM Service

O **CRM Service** é uma API responsável por centralizar e orquestrar as operações relacionadas ao ciclo comercial.

**Ela permite:**

- Criação e gerenciamento de propostas comerciais.
- Consulta e acompanhamento do status das propostas, com controle de versionamento.
- Integração com provedores de assinatura eletrônica, automatizando o envio e acompanhamento do processo de formalização.


## Requisitos Técnicos Atendidos

- Java 17+
- Spring Boot
- Princípios SOLID
- Arquitetura em camadas
- Testes unitários e de integração
- Docker
- Persistência em MySQL
- Documentação via Swagger

## Endpoints Públicos

```text
POST /proposals
GET  /proposals/{uuid}
POST /proposals/{uuid}/send-to-signature
```
```text
POST /sign/callbacks/contract-signed
```

## Estrutura

```text
src/main/java/com/example/crm_service/
├── config
├── controllers
├── domain
├── dto
├── integration
├── repositories
└── services
```
