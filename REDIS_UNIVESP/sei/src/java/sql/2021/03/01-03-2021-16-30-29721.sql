CREATE TABLE public.pessoaemailinstitucional (
	codigo serial NOT NULL,
	pessoa int4 NULL,
	email varchar(255) NULL,
	statusativoinativoenum varchar(100) NULL DEFAULT 'ATIVO'::character varying,
	CONSTRAINT pkey_pessoaemailinstitucional_codigo PRIMARY KEY (codigo),
	CONSTRAINT pessoaemailinstitucional_email_unique UNIQUE (email),
	CONSTRAINT fk_pessoaemailinstitucional_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa(codigo) ON UPDATE CASCADE ON DELETE CASCADE
);