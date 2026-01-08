# 🚀 Como Rodar o Backend

Este documento explica como executar o backend do projeto **Cadastro de Clientes**.

## 📋 Pré-requisitos

- **Java 17** ou superior instalado
- **Maven** (opcional, o projeto usa Maven Wrapper)
- **Conta Supabase** com um projeto PostgreSQL configurado

## ⚙️ Configuração do Banco de Dados

### Supabase PostgreSQL

O projeto usa **Supabase** como banco de dados. Configure suas credenciais no arquivo `.env`:

```env
DATABASE_PASSWORD=sua_senha_do_supabase
JWT_SECRET=sua_chave_secreta_jwt
```

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

O projeto usa **Supabase PostgreSQL**:

- **Host**: Configurado em `application.properties`
- **Porta**: 5432
- **Senha**: Deve estar no arquivo `.env`

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
- Verifique se o arquivo `.env` existe e contém `DATABASE_PASSWORD`
- Verifique se o projeto Supabase está ativo
- Confirme a URL do banco em `application.properties`

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
