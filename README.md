# Plataforma CRM + SIGN

Este repositório contém dois microsserviços principais:

* **CRM Service** – Gerenciamento de clientes e regras de negócio
* **SIGN Service** – Gerenciamento de assinaturas digitais

Fluxo:

1. Cliente é criado no CRM
2. Documento é criado no SIGN
3. Assinatura é solicitada
4. Status da assinatura retorna para o CRM
---

# Organização do Repositório

```
api/
 ├── crm-service/
 ├── sign-service/
 └── utils/
     ├── postman-collections
     └── scripts (banco de dados)
```
---


## Configuração e Execução

### Clonar o repositório

```bash
git clone git@github.com:ArtCarreiro/hinova-microservices.git

cd crm-service
```

### Banco de Dados

```bash
cd utils/scripts/

./create-databases.sh
```

### CRM

```bash
cd crm-service

mvn spring-boot:run
```

### SIGN

```bash
cd sign-service

mvn spring-boot:run
```

### Docker:

```bash
docker-compose up -d
```