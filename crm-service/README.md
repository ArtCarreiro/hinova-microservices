# CRM Service

O **CRM Service** é uma API responsável por centralizar e orquestrar as operações relacionadas ao ciclo comercial.

**Ela permite:**

- Criação e gerenciamento de propostas comerciais.
- Consulta e acompanhamento do status das propostas, com controle de versionamento.
- Integração com provedores de assinatura eletrônica, automatizando o envio e acompanhamento do processo de formalização.


## Endpoints Públicos

```text
POST /proposals
GET  /proposals/{uuid}
POST /proposals/{uuid}/send-to-signature
```

## Estrutura

```text
src/main/java/com/example/crm_service/
├── config
├── controllers
├── domain
├── dto
├── integration
├── messaging
├── repositories
└── services
```
