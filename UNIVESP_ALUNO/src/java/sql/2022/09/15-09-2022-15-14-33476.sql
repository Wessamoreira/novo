create table if not exists public.maparegistroevasaocurso (
	codigo serial4 NOT NULL,
	dataregistro timestamp not NULL,
	tipotrancamentoenum varchar(100) default 'NENHUM',	
	ano varchar(4) NULL,	
	semestre varchar(1) NULL,
	periodicidade varchar(2) NULL,
	usuarioresponsavel int4 not NULL,
	trazeralunoprematriculadorenovouanosemestreseguinte boolean default false,
	trazeralunotrancadoabandonadoanosemestrebase boolean default false,
	alunosreprovadostodasdisciplinasprimeirosemestre boolean default false,	
	qtddiasalunossemacessoava int4 NULL,
	qtdtrancamentoemexcesso int4 NULL,
	qtdmesalunosrenovacaosemacessoava int4 NULL,
	unidadeensinos integer[] NULL,
	cursos integer[] NULL,
	turnos integer[] NULL,
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,	
	nomeupdated varchar(255) NULL,
	CONSTRAINT pkey_maparegistroevasaocurso_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_maparegistroevasaocurso_usuarioresponsavel FOREIGN KEY (usuarioresponsavel) REFERENCES public.usuario(codigo) ON DELETE RESTRICT ON UPDATE restrict
);

CREATE INDEX idx_maparegistroevasaocurso_unidadeEnsinos on maparegistroevasaocurso USING GIN (unidadeEnsinos gin__int_ops);
CREATE INDEX idx_maparegistroevasaocurso_cursos on maparegistroevasaocurso USING GIN (cursos gin__int_ops);
CREATE INDEX idx_maparegistroevasaocurso_turnos on maparegistroevasaocurso USING GIN (turnos gin__int_ops);
   

create table if not exists public.maparegistroevasaocursomatriculaperiodo (
	codigo serial4 NOT NULL,
	maparegistroevasaocurso int4 NOT NULL,
	matriculaperiodo int4 NOT NULL,
	trancamento int4 NULL,
	cancelamento int4 NULL,
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,
	nomeupdated varchar(255) NULL,
	CONSTRAINT pkey_maparegistroevasaocursomatriculaperiodo_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_maparegistroevasaocursomatriculaperiodo_maparegistroevasaocurso FOREIGN KEY (maparegistroevasaocurso) REFERENCES public.maparegistroevasaocurso(codigo) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_maparegistroevasaocursomatriculaperiodo_trancamento FOREIGN KEY (trancamento) REFERENCES public.trancamento(codigo) ON DELETE RESTRICT ON UPDATE restrict,
	CONSTRAINT fk_maparegistroevasaocursomatriculaperiodo_cancelamento FOREIGN KEY (cancelamento) REFERENCES public.cancelamento(codigo) ON DELETE RESTRICT ON UPDATE restrict
);
