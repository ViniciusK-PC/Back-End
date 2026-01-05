-- Script para adicionar perfil DONO à tabela usuarios
-- Passo 1: Criar nova tabela com constraint atualizado
CREATE TABLE usuarios_new (
    id blob not null,
    ativo boolean not null,
    atualizado_em timestamp,
    criado_em timestamp not null,
    email varchar(160) not null unique,
    nome varchar(120) not null,
    perfil varchar(32) not null check ((perfil in ('RECEPCAO','TECNICO','GERENTE','DONO'))),
    senha_hash varchar(255) not null,
    primary key (id)
);

-- Passo 2: Copiar dados existentes
INSERT INTO usuarios_new SELECT * FROM usuarios;

-- Passo 3: Remover tabela antiga
DROP TABLE usuarios;

-- Passo 4: Renomear nova tabela
ALTER TABLE usuarios_new RENAME TO usuarios;

-- Passo 5: Inserir os 2 donos
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

-- Verificar
SELECT id, nome, email, perfil, ativo FROM usuarios WHERE perfil = 'DONO';
