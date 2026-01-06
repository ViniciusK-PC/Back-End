# Script PowerShell para gerar uma chave JWT segura de 256 bits
# Use esta chave na variável JWT_SECRET no Render

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "   Gerador de Chave JWT Segura para Produção" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

# Gerar 32 bytes aleatórios (256 bits)
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$JWT_SECRET = [Convert]::ToBase64String($bytes)

Write-Host "✅ Chave JWT Segura Gerada:" -ForegroundColor Green
Write-Host ""
Write-Host $JWT_SECRET -ForegroundColor Yellow
Write-Host ""
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "⚠️  IMPORTANTE:" -ForegroundColor Red
Write-Host "   1. COPIE esta chave agora"
Write-Host "   2. Cole no Render em: Environment Variables"
Write-Host "   3. Nome da variável: JWT_SECRET"
Write-Host "   4. Valor: a chave acima"
Write-Host "   5. NÃO compartilhe esta chave publicamente"
Write-Host "   6. NÃO faça commit desta chave no Git"
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Tamanho: 256 bits (seguro para HS256)" -ForegroundColor Gray
Write-Host "🔐 Algoritmo recomendado: HS256" -ForegroundColor Gray
Write-Host ""

# Copiar para a área de transferência (opcional)
try {
    Set-Clipboard -Value $JWT_SECRET
    Write-Host "✨ Chave copiada para a área de transferência!" -ForegroundColor Green
} catch {
    Write-Host "⚠️  Não foi possível copiar para a área de transferência" -ForegroundColor Yellow
}
