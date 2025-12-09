CREATE TABLE IF NOT EXISTS public.fechamentonotaoperacao (
	codigo serial4 NOT NULL,
	configuracaoacademica int NOT NULL,
	disciplina int NOT NULL,
	listaHistorico TEXT NOT NULL,
	notas varchar,
	executado boolean DEFAULT FALSE,
	erro TEXT,
	calcularMedia boolean DEFAULT FALSE,
	aprovado boolean DEFAULT FALSE,
	cursando boolean DEFAULT FALSE,
	reprovado boolean DEFAULT FALSE,
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,
	nomeupdated varchar(255) NULL
);