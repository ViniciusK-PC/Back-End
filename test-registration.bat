@echo off
echo 🧪 Testando fluxo completo de registro com email...
echo.

REM Testar registro com novo email
echo 📧 Testando registro com email de teste...
curl -X POST https://fathomless-cliffs-26716-38618d123f54.herokuapp.com/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"teste-sendgrid-"%RANDOM%"@gmail.com\", \"password\": \"123456\", \"name\": \"Teste SendGrid\", \"phone\": \"11999999999\", \"type\": \"pf\", \"birth_date\": \"1990-01-01\", \"gender\": \"M\"}"
echo.
echo.

echo ✅ Teste de registro concluído!
echo 📝 Verifique os logs do Heroku para ver se o email foi enviado!
echo 🌐 Acesse: https://dashboard.heroku.com/apps/fathomless-cliffs-26716/logs
echo.
echo 🔍 Você também pode verificar os códigos em: https://front-end-five-sable.vercel.app/debug-email
pause