@echo off
echo 🧪 Testando SendGrid...
echo.

REM Testar status do SendGrid
echo 📊 Verificando status do SendGrid...
curl -X GET http://localhost:8080/api/test/sendgrid-status
echo.
echo.

REM Testar envio de email (substitua pelo seu email)
echo 📧 Testando envio de email...
curl -X POST http://localhost:8080/api/test/sendgrid-test ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"tecmau@gmail.com\"}"
echo.
echo.

echo ✅ Teste concluído!
echo.
echo 📝 Verifique os logs do console para ver o código gerado!
echo 🌐 Acesse: https://front-end-five-sable.vercel.app/debug-email para ver os códigos
pause