alter table if exists concedente
add column if not exists situacao varchar(20) NOT NULL DEFAULT 'ATIVO'::character varying;