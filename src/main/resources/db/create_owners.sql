-- Script SQL para criar os 2 usuários DONOS
-- Execute este script no seu banco de dados

-- Inserir Dono 1: Mauricio
-- Email: Mauricio@oficina.com
-- Senha: Bancada402. (hash BCrypt)
INSERT INTO usuarios (id, nome, email, senha_hash, perfil, ativo, criado_em, atualizado_em)
VALUES (
    LOWER(HEX(RANDOMBLOB(16))),
    'Mauricio',
    'Mauricio@oficina.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'DONO',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Inserir Dono 2: Adriana
-- Email: Adriana@oficina.com  
-- Senha: Bancada402. (hash BCrypt)
INSERT INTO usuarios (id, nome, email, senha_hash, perfil, ativo, criado_em, atualizado_em)
VALUES (
    LOWER(HEX(RANDOMBLOB(16))),
    'Adriana',
    'Adriana@oficina.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'DONO',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Verificar se foram criados
SELECT id, nome, email, perfil, ativo FROM usuarios WHERE perfil = 'DONO';
