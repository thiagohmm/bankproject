##Como testar a Aplicação

Para testar a aplicação, você pode seguir os seguintes passos:

Para registrar um novo usuário, você pode usar o seguinte comando curl:

    curl -X POST http://localhost:8080/api/users/register \
    -H "Content-Type: application/json" \
    -d '{
        "fullName": "Thiago Henrique",
        "document": "SEU_CPF_VALIDO_AQUI",
        "login": "thiago",
        "password": "password123"
    }'
    
Substitua "SEU_CPF_VALIDO_AQUI" por um CPF válido.

Para autenticar um usuário e obter um token JWT, use o seguinte comando curl:

    curl -X POST http://localhost:8080/api/users/authenticate \
    -H "Content-Type: application/json" \
    -d '{
        "login": "thiago",
        "password": "password123"
    }'

Isso retornará um token JWT que você deve usar para autenticar as próximas requisições.
Para criar uma nova conta bancária, use o seguinte comando curl, substituindo "SEU_TOKEN_JWT_AQUI" pelo token obtido na etapa anterior:
    curl -X POST http://localhost:8080/api/accounts \
    -H "Content-Type: application/json" \
    -H "Authorization

Copie o token retornado.c. Depositar dinheiro (endpoint protegido): Substitua SEU_TOKEN_AQUI pelo token obtido no passo anterior.

    curl -X POST http://localhost:8080/api/accounts/deposit \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer SEU_TOKEN_AQUI" \
    -d '{
        "amount": 500.75
    }'


. Pagar uma conta (endpoint protegido):

    curl -X POST http://localhost:8080/api/accounts/deposit \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer SEU_TOKEN_AQUI" \
    -d '{
        "amount": 500.75
    }'

. Consultar o extrato (endpoint protegido): Este endpoint usa o lado da consulta (MongoDB) e será muito rápido.

    curl -X GET http://localhost:8080/api/statement \
    -H "Authorization: Bearer SEU_TOKEN_AQUI"
    
