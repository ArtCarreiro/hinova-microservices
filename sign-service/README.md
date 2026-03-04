# SIGN Service

O **SIGN Service** é uma API especializada na gestão do ciclo completo de assinatura digital de documentos.

**Ele é responsável por:**

- Geração e preparação de documentos para assinatura.
- Validação e verificação de integridade dos documentos assinados.
- Acompanhamento e atualização do status das assinaturas em tempo real.
- Persistência e rastreabilidade de eventos relacionados ao processo de assinatura.

## Endpoints Públicos

```text
POST /contracts
GET  /contracts/{uuid}
POST /contracts/{uuid}/sign
```

## Estrutura
```text
src/main/java/com/example/sign_service/
├── config
├── controllers
├── domains
├── dto
├── messaging
├── repositories
└── services
```