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
root/
 ├── crm-service/
 ├── sign-service/
 └── utils/
     ├── postman-collections
     └── scripts-bd
```
---

# Execução no Ambiente Local

## CRM

```bash
cd crm-service
mvn spring-boot:run
```

## SIGN

```bash
cd sign-service
mvn spring-boot:run
```



## Via Docker:

```bash
docker-compose up -d
```