-- Verificar todas as tabelas no banco
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;

-- Verificar estrutura da tabela verification_codes se existir
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'verification_codes' 
ORDER BY ordinal_position;