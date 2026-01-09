# 🚀 Como Rodar o Backend

Este documento explica como executar o backend do projeto **Cadastro de Clientes**.

## 📋 Pré-requisitos

- **Java 17** ou superior instalado
- **Maven** (opcional, o projeto usa Maven Wrapper)
- **Banco de Dados**: H2 (Padrão) ou PostgreSQL configurado

### Configuração Local (H2)
O projeto vem configurado para usar o banco H2 em memória por padrão para testes locais. Não é necessária nenhuma configuração adicional.

### Configuração PostgreSQL (Opcional)
Se desejar usar PostgreSQL, configure as variáveis no seu ambiente ou no `application.properties`.

## ▶️ Formas de Executar

### 1️⃣ Modo Simples (Recomendado)

Duplo clique no arquivo ou execute no terminal:

```cmd
run-backend-simples.bat
```

### 2️⃣ Modo com Verificações

Execute o script PowerShell com verificações de ambiente:

```powershell
.\run-backend.ps1
```

### 3️⃣ Modo Desenvolvimento (Hot Reload)

Para desenvolvimento com recarga automática:

```cmd
run-backend-dev.bat
```

### 4️⃣ Modo Manual

Se preferir executar manualmente:

```cmd
.\mvnw.cmd spring-boot:run
```

## 🌐 Acessando o Backend

Após iniciar, o backend estará disponível em:

- **URL**: `http://localhost:8080`
- **API Docs**: `http://localhost:8080/swagger-ui.html` (se configurado)

## 🗄️ Banco de Dados

- **Desenvolvimento**: H2 em memória (limpa ao reiniciar)
- **H2 Console**: Disponível em `/h2-console` quando rodando com o perfil `local`

## 🛑 Parando o Servidor

Pressione `Ctrl+C` no terminal onde o servidor está rodando.

## 🐛 Problemas Comuns

### Erro: "Java não encontrado"
- Instale o Java 17: https://adoptium.net/
- Verifique: `java -version`

### Erro: "Porta 8080 já está em uso"
- Pare outros servidores rodando na porta 8080
- Ou altere a porta em `application.properties`:
  ```properties
  server.port=8081
  ```

### Erro de Permissão no PowerShell
Execute uma vez:
```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Erro de Conexão com Banco de Dados
- Verifique as credenciais no `application.properties`
- Confirme se o banco de dados está acessível

## 📝 Logs

Os logs serão exibidos no terminal. Para salvar em arquivo:

```cmd
.\mvnw.cmd spring-boot:run > logs.txt 2>&1
```

## ✅ Testando se está funcionando

```powershell
# Teste simples
curl http://localhost:8080
```

ou abra no navegador: `http://localhost:8080`

---

**Dúvidas?** Entre em contato com a equipe de desenvolvimento.
