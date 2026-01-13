@echo off
echo Testando endpoint de verificacao...
echo.

curl -X POST https://fathomless-cliffs-26716-38618d123f54.herokuapp.com/api/verification/send-code ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"novo-usuario-202601130829@example.com\"}" ^
  -w "\nStatus: %{http_code}\n"

echo.
echo Teste concluido!
pause