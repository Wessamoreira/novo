CREATE TABLE IF NOT EXISTS public.motivoindeferimentodocumentoaluno (
    codigo        SERIAL       NOT NULL,
    nome          VARCHAR(255) NOT NULL UNIQUE,
    situacao      varchar(20) DEFAULT 'NENHUM',
    created       timestamp,
    codigoCreated int,
    nomeCreated   varchar(200),
    updated       timestamp,
    codigoupdated int,
    nomeupdated   varchar(200),
    CONSTRAINT pkey_motivoindeferimentodocumentoaluno_codigo PRIMARY KEY (codigo)
);