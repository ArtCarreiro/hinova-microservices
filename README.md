# Plataforma de Propostas e Assinaturas (CRM + SIGN)

Implementação do desafio técnico de uma plataforma SaaS com dois módulos independentes:

- `crm-service`: cria propostas comerciais, consulta propostas e envia para assinatura.
- `sign-service`: cria contratos, consulta contrato, assina contrato e notifica o CRM por callback.

## Requisitos Técnicos Atendidos

- Java 17+
- Spring Boot
- Persistência em MySQL
- Documentação via Swagger

## Estrutura do Repositório

```text
api/
├── crm-service/
├── sign-service/
├── utils/
│   ├── postman/
│   └── scripts/
└── docs/
    └── Decisões-Técnicas.md
```

## Como Rodar

### Opção 1: Docker Compose

Na raiz do projeto (api):

```bash
docker compose build
docker compose up -d
```

Serviços:

- CRM: `http://localhost:8080`
- SIGN: `http://localhost:8081`

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

Os testes atuais são unitários (JUnit 5 + Mockito), sem dependência de banco MySQL externo.


## Collection Postman para testes:

- `utils/postman/api_collection.json`
