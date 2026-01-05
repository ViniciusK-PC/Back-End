-- Atualizar senha dos usuários DONOS para 'admin123'
-- Hash BCrypt válido: $2a$10$18zd4d.YBlelc.JsfTsjI.chtnE1gNGoqYWcWZ5CWPJvBxMm4DHky

UPDATE usuarios 
SET senha_hash = '$2a$10$18zd4d.YBlelc.JsfTsjI.chtnE1gNGoqYWcWZ5CWPJvBxMm4DHky'
WHERE email = 'Mauricio@oficina.com';

UPDATE usuarios 
SET senha_hash = '$2a$10$18zd4d.YBlelc.JsfTsjI.chtnE1gNGoqYWcWZ5CWPJvBxMm4DHky'
WHERE email = 'Adriana@oficina.com';

-- Verificar
SELECT email, perfil FROM usuarios WHERE perfil = 'DONO';
