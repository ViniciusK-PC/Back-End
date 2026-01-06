#!/bin/bash
# Script para gerar uma chave JWT segura de 256 bits
# Use esta chave na variável JWT_SECRET no Render

echo "==================================================="
echo "   Gerador de Chave JWT Segura para Produção"
echo "==================================================="
echo ""

# Gerar uma string aleatória base64 de 256 bits (32 bytes)
JWT_SECRET=$(openssl rand -base64 32)

echo "✅ Chave JWT Segura Gerada:"
echo ""
echo "$JWT_SECRET"
echo ""
echo "==================================================="
echo "⚠️  IMPORTANTE:"
echo "   1. COPIE esta chave agora"
echo "   2. Cole no Render em: Environment Variables"
echo "   3. Nome da variável: JWT_SECRET"
echo "   4. Valor: a chave acima"
echo "   5. NÃO compartilhe esta chave publicamente"
echo "   6. NÃO faça commit desta chave no Git"
echo "==================================================="
echo ""
echo "📋 Tamanho: 256 bits (seguro para HS256)"
echo "🔐 Algoritmo recomendado: HS256"
echo ""
