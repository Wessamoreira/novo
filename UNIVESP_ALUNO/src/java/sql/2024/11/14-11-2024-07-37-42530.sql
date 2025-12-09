CREATE TABLE IF NOT EXISTS public.operacaomoodle (
	codigo serial4 NOT NULL,
	jsonmoodle text NULL,
	processado bool DEFAULT false NULL,
	tipooperacao varchar(10) NULL,
	erro text NULL,
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,
	nomeupdated varchar(255) NULL
);