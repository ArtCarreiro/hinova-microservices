# SIGN Service

O **SIGN Service** é uma API especializada na gestão do ciclo completo de assinatura digital de documentos.

**Ele é responsável por:**

- Geração e preparação de documentos para assinatura.
- Validação e verificação de integridade dos documentos assinados.
- Acompanhamento e atualização do status das assinaturas em tempo real.
- Persistência e rastreabilidade de eventos relacionados ao processo de assinatura.

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
POST /contracts
GET  /contracts/{uuid}
PUT  /contracts/{uuid}/sign
```

## Estrutura
```text
src/main/java/com/example/sign_service/
├── config
├── controllers
├── domains
├── dto
├── integration
├── repositories
└── services
```
