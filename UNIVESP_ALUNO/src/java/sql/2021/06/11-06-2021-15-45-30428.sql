CREATE TABLE public.perguntachecklistorigem (
	codigo serial NOT NULL,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(255),
	updated timestamp,
	codigoUpdated int,
	nomeUpdated varchar(255),
	perguntarespostaorigem int NOT NULL,
	perguntachecklist int NOT NULL,
	checklist boolean default false,
	
	CONSTRAINT pkey_perguntachecklistorigem_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_perguntachecklistorigem_perguntachecklist FOREIGN KEY (perguntachecklist) REFERENCES perguntachecklist(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_perguntachecklistorigem_perguntarespostaorigem FOREIGN KEY (perguntarespostaorigem) REFERENCES perguntarespostaorigem(codigo) ON UPDATE CASCADE ON DELETE CASCADE
);