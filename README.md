# Bank Project - Sistema Bancário com CQRS

Este é um projeto de sistema bancário implementado usando o padrão CQRS (Command Query Responsibility Segregation) com Spring Boot.

## 🏗️ Arquitetura

- **Comandos**: Utiliza H2 (banco em memória) para operações de escrita
- **Consultas**: Utiliza MongoDB para operações de leitura otimizadas
- **Autenticação**: JWT (JSON Web Tokens)
- **Validação**: Inclui validação de CPF brasileiro
- **Padrão**: CQRS com eventos assíncronos

## 🛠️ Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.6
- Spring Security
- Spring Data JPA (H2)
- Spring Data MongoDB
- JWT
- Docker & Docker Compose
- Maven

## 📋 Pré-requisitos

- Docker e Docker Compose
- Java 21 (para desenvolvimento local)
- Maven (para desenvolvimento local)

## 🚀 Como Executar a Aplicação

### Opção 1: Usando Docker Compose (Recomendado)

1. **Clone o repositório e navegue até a pasta:**
   ```bash
   cd /caminho/para/cqrs-bank-project
   ```

2. **Configure a variável de ambiente JWT:**
   ```bash
   export JWT_SECRET_KEY="a-very-long-and-secure-secret-key-for-your-jwt-tokens-that-is-at-least-256-bits"
   ```

3. **Execute com Docker Compose:**
   ```bash
   docker-compose up --build
   ```

### Opção 2: Executando Localmente

1. **Inicie o MongoDB (usando Docker):**
   ```bash
   docker run -d --name mongodb -p 27017:27017 mongo:6.0
   ```

2. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```

## 🧪 Testando a Aplicação

A aplicação estará disponível em `http://localhost:8080`

### 1. Registrar um Novo Usuário

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "João Silva",
    "document": "11144477735",
    "login": "joao.silva",
    "password": "password123"
  }'
```

**Resposta esperada:** `Usuário criado com sucesso!`

### 2. Fazer Login (Obter Token JWT)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "password123"
  }'
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**⚠️ Importante:** Copie o token retornado para usar nas próximas requisições.

### 3. Realizar Depósito

```bash
curl -X POST http://localhost:8080/api/accounts/deposit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "amount": 1000.50
  }'
```

### 4. Pagar uma Conta (Saque)

```bash
curl -X POST http://localhost:8080/api/accounts/pay \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "amount": 250.75
  }'
```

### 5. Consultar Extrato

```bash
curl -X GET http://localhost:8080/api/statement \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta esperada:**
```json
{
  "userId": "abc123...",
  "totalBalance": 749.75,
  "history": [
    {
      "type": "saque",
      "value": 250.75,
      "date": "07-10-2025 14:30:15"
    },
    {
      "type": "deposito",
      "value": 1000.50,
      "date": "07-10-2025 14:25:10"
    }
  ]
}
```

## 🔧 Funcionalidades Disponíveis

### Endpoints Públicos
- `POST /api/users/register` - Registro de usuários
- `POST /api/auth/login` - Autenticação

### Endpoints Protegidos (Requerem Autenticação)
- `POST /api/accounts/deposit` - Realizar depósito
- `POST /api/accounts/pay` - Pagar conta (permite saldo negativo)
- `GET /api/statement` - Consultar extrato

### Console H2 (Desenvolvimento)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:bankdb`
- Username: `sa`
- Password: `password`

## 🧪 Executando Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes específicos
./mvnw test -Dtest=UserCommandServiceTest
./mvnw test -Dtest=AccountCommandServiceTest
```

## 📊 Regras de Negócio

1. **Depósitos**: 
   - Se a conta estiver negativa, aplica-se juros de 2% sobre o valor em débito antes do depósito

2. **Saques/Pagamentos**: 
   - Permite saldo negativo (cheque especial)

3. **CPF**: 
   - Validação automática de CPF brasileiro

4. **Extrato**: 
   - Consulta otimizada via MongoDB
   - Histórico em ordem cronológica decrescente

## 🐳 Docker

### Construir apenas a imagem da aplicação:
```bash
docker build -t bankproject .
```

### Executar com compose:
```bash
docker-compose up -d
```

### Parar os serviços:
```bash
docker-compose down
```

## 📝 Exemplo Completo de Teste

Aqui está um script completo para testar todas as funcionalidades:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080"

# 1. Registrar usuário
echo "1. Registrando usuário..."
curl -X POST $BASE_URL/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Maria Santos",
    "document": "11144477735",
    "login": "maria.santos",
    "password": "password123"
  }'
echo -e "\n"

# 2. Fazer login e capturar token
echo "2. Fazendo login..."
RESPONSE=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "maria.santos",
    "password": "password123"
  }')

TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "Token obtido: $TOKEN"
echo ""

# 3. Fazer depósito
echo "3. Realizando depósito de R$ 1500,00..."
curl -X POST $BASE_URL/api/accounts/deposit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "amount": 1500.00
  }'
echo -e "\n"

# 4. Pagar uma conta
echo "4. Pagando conta de R$ 300,00..."
curl -X POST $BASE_URL/api/accounts/pay \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "amount": 300.00
  }'
echo -e "\n"

# 5. Consultar extrato
echo "5. Consultando extrato..."
curl -X GET $BASE_URL/api/statement \
  -H "Authorization: Bearer $TOKEN" | jq .
echo -e "\n"
```

## 🚨 Troubleshooting

### Problema: "maven:3.8.5-openjdk-21: not found"
**Solução:** O Dockerfile já foi corrigido para usar `maven:3.9.5-eclipse-temurin-21`

### Problema: Erro de conexão MongoDB
**Solução:** Certifique-se de que o MongoDB está rodando:
```bash
docker ps | grep mongo
```

### Problema: Token JWT inválido
**Solução:** Verifique se o token não expirou (1 hora) e se está sendo passado corretamente no header Authorization.

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a Licença Apache 2.0 - veja o arquivo [LICENSE](LICENSE) para mais detalhes.