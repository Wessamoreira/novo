CREATE TABLE public.TipoRequerimentoCursoTransferenciaCurso (
	codigo serial NOT NULL,
	tiporequerimentocurso int4 NOT NULL,
	curso int4 NOT NULL,
	CONSTRAINT tiporequerimentocursotransferenciacurso_pkey PRIMARY KEY (codigo),
	CONSTRAINT fk_tiporequerimentocursotrasferenciacurso_tiporequerimentocurso FOREIGN KEY (tiporequerimentocurso) REFERENCES tiporequerimentocurso(codigo) ON DELETE RESTRICT,
	CONSTRAINT fk_tiporequerimentocursotransferenciacurso_curso FOREIGN KEY (curso) REFERENCES curso(codigo) ON DELETE RESTRICT
);