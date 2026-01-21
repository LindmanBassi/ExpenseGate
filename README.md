# 💼 ExpenseGate API – Spring Boot & Spring Security

O **ExpenseGate** é uma **API REST para gerenciamento de despesas corporativas**, desenvolvida com **Spring Boot** e **Spring Security**, criada com o objetivo de **estudar e aplicar conceitos avançados de autenticação e autorização**.

O projeto explora **RBAC (Role-Based Access Control)**, **authorities granulares**, **wildcards**, **segurança a nível de método com SpEL** e **regras de acesso baseadas em contexto**, como dono da despesa e departamento do usuário.

---

## 🔐 Funcionalidades

- Criação de despesas por usuários autenticados  
- Consulta de despesas individuais com regras de acesso dinâmicas  
- Listagem de despesas conforme permissões do usuário  
- Aprovação ou reprovação de despesas  
- Controle de acesso baseado em:
  - Roles (EMPLOYEE, MANAGER, ADMIN)
  - Authorities (`expense:create`, `expense:read`, etc.)
  - Wildcards (`expense:*`)
  - Contexto da requisição (dono da despesa / mesmo departamento)
- Seed automático de usuários, roles e authorities ao iniciar a aplicação  

---

## 🧠 Modelo de Autorização

O projeto implementa um **modelo híbrido RBAC + ABAC**, combinando controle por papel e regras contextuais.

### Roles
- **EMPLOYEE**
- **MANAGER**
- **ADMIN**

### Authorities
- `expense:create`
- `expense:read`
- `expense:read:any`
- `expense:approve`
- `expense:approve:any`
- `expense:*` (wildcard)

### Regras de acesso
- Usuários podem acessar **suas próprias despesas**
- Managers podem acessar despesas do **mesmo departamento**
- Admins possuem acesso total via wildcard
- Regras aplicadas com `@PreAuthorize` e SpEL customizado

---

## 📐 Modelagem e Recursos JPA

Relacionamentos utilizados:
- `@ManyToOne` (Expense → User)
- `@ManyToMany` (User ↔ Role, Role ↔ Authority)

Outros recursos:
- Uso de **Enums** para status e departamentos
- Mapeamento explícito de entidades com JPA
- Queries derivadas para validação de regras de negócio
- Separação clara entre entidades, DTOs, serviços e controladores

---

## 🛡️ Segurança

- Autenticação via **HTTP Basic**
- Aplicação **stateless** (sem sessão HTTP)
- Senhas criptografadas com **BCrypt**
- Autorização a nível de método com `@EnableMethodSecurity`
- Beans SpEL customizados:
  - `@authz` → verificação de authorities com suporte a wildcard
  - `@expenseSec` → regras contextuais (owner / department)
- RBAC seed automático com `CommandLineRunner`

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL 8.4
- Docker & Docker Compose
- Lombok
- Maven
- BCrypt Password Encoder

---

## ⚙️ Requisitos para rodar o projeto

### Opção 1 – Usando Docker (recomendado)

O projeto já possui um `docker-compose.yml` para subir o banco MySQL:

```bash
docker compose up -d
```
### Opção 2 – Banco local
Ter o MySQL instalado

Criar um banco chamado expensegate

Ajustar o application.properties conforme seu ambiente

## 🚀 Como rodar

### 1️⃣ Subir o banco de dados com Docker

O projeto utiliza MySQL via Docker. Antes de iniciar a aplicação, suba o container:

```bash
docker compose up -d
```
### 2️⃣ Rodar a aplicação Spring Boot
Com o Maven instalado:

```bash
./mvnw spring-boot:run
```

Ou rode diretamente pela sua IDE (ex: IntelliJ IDEA).

---

Desenvolvido por Henrique Lindman ✨
