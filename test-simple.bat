@echo off
echo 🧪 Testando registro com email...
echo.

powershell -Command "Invoke-RestMethod -Uri 'https://fathomless-cliffs-26716-38618d123f54.herokuapp.com/api/auth/register' -Method POST -Headers @{'Content-Type'='application/json'} -Body '{\"email\": \"teste-sendgrid-12345@gmail.com\", \"password\": \"123456\", \"name\": \"Teste SendGrid\", \"phone\": \"11999999999\", \"type\": \"pf\", \"birth_date\": \"1990-01-01\", \"gender\": \"M\"}'"

echo.
echo ✅ Teste concluído!
echo 📝 Verifique os logs do Heroku para ver se o email foi enviado!
pause