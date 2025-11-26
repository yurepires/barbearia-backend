# 💈 Barbearia Backend API

API REST desenvolvida em Spring Boot para gerenciamento de uma barbearia, incluindo clientes, barbeiros, serviços e autenticação.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **PostgreSQL** (produção)
- **H2 Database** (desenvolvimento)
- **Lombok**
- **SendGrid** (envio de emails)
- **Maven**

## 🌐 URL da API em Produção

A API está disponível em:

**Base URL:** `https://barbearia-backend-x0st.onrender.com`

Todos os endpoints documentados abaixo devem ser acessados usando esta URL base.

## 📚 Endpoints da API

### 🔐 Autenticação de Administrador

**Base URL:** `/api/admin/auth`

#### Login de Administrador
```http
POST /api/admin/auth/login
Content-Type: application/json

{
  "username": "login",
  "password": "password"
}
```

**Resposta de Sucesso (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Resposta de Erro (401):**
```json
"Usuário ou senha inválidos"
```

---

### 🔐 Autenticação de Cliente (OTP)

**Base URL:** `/api/cliente/auth`

#### Gerar OTP
Envia um código OTP para o email do cliente.

```http
POST /api/cliente/auth/gerarOtp
Content-Type: application/json

{
  "email": "cliente@email.com"
}
```

**Resposta de Sucesso (200):**
```json
"OTP enviado para o email."
```

#### Validar OTP
Valida o código OTP e retorna um token de autenticação.

```http
POST /api/cliente/auth/validarOtp
Content-Type: application/json

{
  "email": "cliente@email.com",
  "otp": "123456"
}
```

**Resposta de Sucesso (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "valid": true
}
```

---

### 👤 Gestão de Clientes

**Base URL:** `/api/cliente`

#### Criar Cliente
```http
POST /api/cliente/criarCliente
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(99) 99999-9999"
}
```

**Resposta de Sucesso (201):**
```json
{
  "statusCode": "201",
  "statusMsg": "Cliente criado com sucesso"
}
```

#### Buscar Cliente por Email
```http
GET /api/cliente/buscarCliente?email=joao@email.com
```

**Resposta de Sucesso (200):**
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(99) 99999-9999"
}
```

#### Buscar Todos os Clientes
```http
GET /api/cliente/buscarTodosClientes
```

**Resposta de Sucesso (200):**
```json
[
  {
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "(99) 99999-9999"
  },
  {
    "nome": "Maria Santos",
    "email": "maria@email.com",
    "telefone": "(99) 88888-8888"
  }
]
```

#### Atualizar Cliente
```http
PUT /api/cliente/atualizarCliente
Content-Type: application/json

{
  "nome": "João Silva Atualizado",
  "email": "joao@email.com",
  "telefone": "(99) 99999-9999"
}
```

**Resposta de Sucesso (200):**
```json
{
  "statusCode": "200",
  "statusMsg": "Requisição processada com sucesso"
}
```

**Resposta de Erro (417):**
```json
{
  "statusCode": "417",
  "statusMsg": "Falha na atualização. Por favor, tente novamente ou contate o suporte"
}
```

#### Deletar Cliente
```http
DELETE /api/cliente/deletarCliente?email=joao@email.com
```

**Resposta de Sucesso (200):**
```json
{
  "statusCode": "200",
  "statusMsg": "Requisição processada com sucesso"
}
```

**Resposta de Erro (417):**
```json
{
  "statusCode": "417",
  "statusMsg": "Falha ao deletar. Por favor, tente novamente ou contate o suporte"
}
```

---

### ✂️ Gestão de Barbeiros

**Base URL:** `/api/barbeiro`

#### Criar Barbeiro
```http
POST /api/barbeiro/criarBarbeiro
Content-Type: application/json

{
  "nome": "Carlos Barbeiro",
  "email": "carlos@barbearia.com",
  "telefone": "(99) 77777-7777",
  "especialidade": "Corte e Barba"
}
```

**Resposta de Sucesso (201):**
```json
{
  "statusCode": "201",
  "statusMsg": "Barbeiro criado com sucesso"
}
```

#### Buscar Barbeiro por Email
```http
GET /api/barbeiro/buscarBarbeiro?email=carlos@barbearia.com
```

**Resposta de Sucesso (200):**
```json
{
  "nome": "Carlos Barbeiro",
  "email": "carlos@barbearia.com",
  "telefone": "(99) 77777-7777",
  "especialidade": "Corte e Barba"
}
```

#### Buscar Todos os Barbeiros
```http
GET /api/barbeiro/buscarTodosBarbeiros
```

**Resposta de Sucesso (200):**
```json
[
  {
    "nome": "Carlos Barbeiro",
    "email": "carlos@barbearia.com",
    "telefone": "(99) 77777-7777",
    "especialidade": "Corte e Barba"
  }
]
```

#### Atualizar Barbeiro
```http
PUT /api/barbeiro/atualizarBarbeiro
Content-Type: application/json

{
  "nome": "Carlos Barbeiro Atualizado",
  "email": "carlos@barbearia.com",
  "telefone": "(99) 77777-7777",
  "especialidade": "Corte, Barba e Pigmentação"
}
```

**Resposta de Sucesso (200):**
```json
{
  "statusCode": "200",
  "statusMsg": "Requisição processada com sucesso"
}
```

**Resposta de Erro (417):**
```json
{
  "statusCode": "417",
  "statusMsg": "Falha na atualização. Por favor, tente novamente ou contate o suporte"
}
```

#### Deletar Barbeiro
```http
DELETE /api/barbeiro/deletarBarbeiro?email=carlos@barbearia.com
```

**Resposta de Sucesso (200):**
```json
{
  "statusCode": "200",
  "statusMsg": "Requisição processada com sucesso"
}
```

**Resposta de Erro (417):**
```json
{
  "statusCode": "417",
  "statusMsg": "Falha ao deletar. Por favor, tente novamente ou contate o suporte"
}
```

---

### 🛠️ Gestão de Serviços

**Base URL:** `/api/servico`

#### Criar Serviço
```http
POST /api/servico/criarServico
Content-Type: application/json

{
  "nome": "Corte Masculino",
  "descricao": "Corte de cabelo masculino tradicional",
  "preco": 35.00,
  "duracao": 30
}
```

**Resposta de Sucesso (201):**
```json
{
  "statusCode": "201",
  "statusMsg": "Serviço criado com sucesso"
}
```

#### Buscar Serviço por ID
```http
GET /api/servico/buscarServico?servicoId=1
```

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "nome": "Corte Masculino",
  "descricao": "Corte de cabelo masculino tradicional",
  "preco": 35.00,
  "duracao": 30
}
```

#### Buscar Todos os Serviços
```http
GET /api/servico/buscarTodosServicos
```

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "nome": "Corte Masculino",
    "descricao": "Corte de cabelo masculino tradicional",
    "preco": 35.00,
    "duracao": 30
  },
  {
    "id": 2,
    "nome": "Barba",
    "descricao": "Aparar e modelar barba",
    "preco": 25.00,
    "duracao": 20
  }
]
```

#### Atualizar Serviço
```http
PUT /api/servico/atualizarServico
Content-Type: application/json

{
  "id": 1,
  "nome": "Corte Masculino Premium",
  "descricao": "Corte de cabelo masculino com acabamento premium",
  "preco": 45.00,
  "duracao": 40
}
```

**Resposta de Sucesso (200):**
```json
{
  "statusCode": "200",
  "statusMsg": "Requisição processada com sucesso"
}
```

**Resposta de Erro (417):**
```json
{
  "statusCode": "417",
  "statusMsg": "Falha na atualização. Por favor, tente novamente ou contate o suporte"
}
```

#### Deletar Serviço
```http
DELETE /api/servico/deletarServico?servicoId=1
```

**Resposta de Sucesso (200):**
```json
{
  "statusCode": "200",
  "statusMsg": "Requisição processada com sucesso"
}
```

**Resposta de Erro (417):**
```json
{
  "statusCode": "417",
  "statusMsg": "Falha ao deletar. Por favor, tente novamente ou contate o suporte"
}
```

---

## 🔒 Autenticação e Autorização

### Tipos de Autenticação

1. **Administrador (JWT)**: Login tradicional com username e senha
   - Endpoint: `/api/admin/auth/login`
   - Retorna um token JWT
   - Role: `ADM`

2. **Cliente (OTP)**: Autenticação via código enviado por email
   - Endpoint: `/api/cliente/auth/gerarOtp` (gera código)
   - Endpoint: `/api/cliente/auth/validarOtp` (valida código e retorna token)
   - Role: `CLIENTE`

### Uso do Token

Após autenticação bem-sucedida, inclua o token no header das requisições:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 📝 Validações

- **Email**: Todos os endpoints que recebem email validam o formato
- **Campos obrigatórios**: Validação automática via `@Valid`
- **Respostas padronizadas**: Uso de `ResponseDto` para mensagens consistentes

## 🗄️ Estrutura do Projeto

```
src/main/java/com/ifma/barbearia/
├── controller/          # Controladores REST
├── DTOs/               # Data Transfer Objects
├── entities/           # Entidades JPA
├── services/           # Lógica de negócio
├── repositories/       # Repositórios JPA
├── security/           # Configurações de segurança e JWT
└── constants/          # Constantes da aplicação
```

## 🤝 Guia para o Frontend

### Fluxo de Autenticação - Administrador

1. Fazer POST para `/api/admin/auth/login` com credenciais
2. Armazenar o token retornado
3. Incluir o token no header `Authorization: Bearer {token}` em todas as requisições

### Fluxo de Autenticação - Cliente

1. Fazer POST para `/api/cliente/auth/gerarOtp` com o email
2. Cliente recebe código por email
3. Fazer POST para `/api/cliente/auth/validarOtp` com email e código
4. Armazenar o token retornado
5. Incluir o token no header `Authorization: Bearer {token}` em todas as requisições

### Tratamento de Erros

- **200**: Sucesso
- **201**: Criado com sucesso
- **401**: Não autorizado (credenciais inválidas)
- **417**: Expectativa falhou (erro na operação)
- **Validação**: Retorna detalhes dos campos inválidos

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).

---

**Desenvolvido para IFMA - 2025.2**
