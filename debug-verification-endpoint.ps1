# Debug do endpoint de verificação
Write-Host "🐛 Debugando endpoint /api/verification/send-code" -ForegroundColor Yellow
Write-Host ""

$email = "novo-usuario-202601130829@example.com"
Write-Host "📧 Testando com email: $email" -ForegroundColor Cyan
Write-Host ""

$body = @{
    email = $email
} | ConvertTo-Json

try {
    Write-Host "📤 Enviando requisição para /api/verification/send-code..." -ForegroundColor Green
    
    $response = Invoke-RestMethod -Uri "https://fathomless-cliffs-26716-38618d123f54.herokuapp.com/api/verification/send-code" `
        -Method Post `
        -Body $body `
        -ContentType "application/json" `
        -ErrorAction Stop
    
    Write-Host "✅ Requisição bem-sucedida!" -ForegroundColor Green
    Write-Host "📋 Resposta:" -ForegroundColor Green
    Write-Host ($response | ConvertTo-Json -Depth 10)
    
} catch {
    Write-Host "❌ Erro na requisição:" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "Mensagem: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        $responseBody = $reader.ReadToEnd()
        Write-Host "📄 Corpo do erro:" -ForegroundColor Red
        Write-Host $responseBody -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "🔍 Verifique os logs do Heroku para mais detalhes:" -ForegroundColor Yellow
Write-Host "heroku logs --tail --app fathomless-cliffs-26716" -ForegroundColor Gray
Write-Host ""
Write-Host "Script concluído." -ForegroundColor Yellow