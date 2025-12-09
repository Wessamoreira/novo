alter table if exists pergunta  add column if not exists extensaotiporesposta varchar(255);

CREATE TABLE public.perguntachecklist (
	codigo serial NOT NULL,
	pergunta int NOT NULL,
	descricao text NOT NULL,
	statusativoinativoenum varchar(20) NOT NULL DEFAULT 'ATIVO',
	CONSTRAINT pkey_perguntachecklist_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_perguntachecklist_pergunta FOREIGN KEY (pergunta) REFERENCES pergunta(codigo) ON UPDATE CASCADE ON DELETE CASCADE
);