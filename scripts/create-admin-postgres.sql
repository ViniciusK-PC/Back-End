-- Script para criar usuário admin no PostgreSQL (Render)
-- Execute este script no Shell do Render usando: psql $DATABASE_URL

-- Criar usuário admin
-- IMPORTANTE: Mude o email e senha conforme necessário!
INSERT INTO usuarios (id, nome, email, senha, papel, ativo, created_at, updated_at) 
VALUES (
  gen_random_uuid(),
  'Mauricio',
  'Mauricio@oficina.com',
  '$2a$10$N9qo8uLOickgx2ZMRZoMye5POV7SMOBFUisKe7.JQPfgWg6lKZF0C', -- senha: admin123
  'ROLE_ADMIN',
  true,
  NOW(),
  NOW()
);

-- Verificar se o usuário foi criado
SELECT id, nome, email, papel, ativo FROM usuarios WHERE email = 'Mauricio@oficina.com';
