create table if not exists   processoMatriculaUnidadeEnsino (
	codigo serial,
	unidadeEnsino int not null,
	processoMatricula int not null,
	created timestamp default now(),
	codigoCreated int,
	nomeCreated varchar(200),
	updated timestamp default now(),
	codigoUpdated int,
	nomeUpdated  varchar(200),
	
	constraint pk_processoMatriculaUnidadeEnsino primary key (codigo),
	constraint unq_pmue_unidadeEnsino_processoMatricula unique (unidadeEnsino, processoMatricula),
	constraint fk_pmue_unidadeEnsino foreign key (unidadeEnsino) references unidadeEnsino (codigo) on update restrict on delete restrict,
	constraint fk_pmue_processoMatricula foreign key (processoMatricula) references processoMatricula (codigo) on update cascade on delete cascade
);




alter table processomatriculacalendario add column curso int;
alter table processomatriculacalendario add column turno int;
alter table processomatriculacalendario add constraint fk_processomatriculacalendario_curso foreign key (curso) references curso (codigo) on update restrict on delete restrict;
alter table processomatriculacalendario add constraint fk_processomatriculacalendario_turno foreign key (turno) references turno (codigo) on update restrict on delete restrict;

alter table periodoletivoativounidadeensinocurso add column curso int;
alter table periodoletivoativounidadeensinocurso add column turno int;
alter table periodoletivoativounidadeensinocurso add constraint fk_periodoletivoativounidadeensinocurso_curso foreign key (curso) references curso (codigo) on update restrict on delete restrict;
alter table periodoletivoativounidadeensinocurso add constraint fk_periodoletivoativounidadeensinocurso_turno foreign key (turno) references turno (codigo) on update restrict on delete restrict;

alter table PeriodoLetivoAtivoUnidadeEnsinoCursoLog add column curso int;
alter table PeriodoLetivoAtivoUnidadeEnsinoCursoLog add column turno int;
alter table PeriodoLetivoAtivoUnidadeEnsinoCursoLog add constraint fk_periodoletivoativounidadeensinocursolog_curso foreign key (curso) references curso (codigo) on update restrict on delete cascade;
alter table PeriodoLetivoAtivoUnidadeEnsinoCursoLog add constraint fk_periodoletivoativounidadeensinocursolog_turno foreign key (turno) references turno (codigo) on update restrict on delete cascade;

update processomatriculacalendario set curso = unidadeensinocurso.curso, turno =  unidadeensinocurso.turno from unidadeensinocurso where unidadeensinocurso.codigo = processomatriculacalendario.unidadeensinocurso;
update periodoletivoativounidadeensinocurso set curso = unidadeensinocurso.curso, turno =  unidadeensinocurso.turno from unidadeensinocurso where unidadeensinocurso.codigo = periodoletivoativounidadeensinocurso.unidadeensinocurso;
update periodoletivoativounidadeensinocursolog set curso = unidadeensinocurso.curso, turno =  unidadeensinocurso.turno from unidadeensinocurso where unidadeensinocurso.codigo = periodoletivoativounidadeensinocursolog.unidadeensinocurso;

insert into processomatriculaunidadeensino (processomatricula, unidadeensino) (
	select processomatricula.codigo, processomatricula.unidadeensino from processomatricula
	where not exists (
	select processomatriculaunidadeensino.codigo from processomatriculaunidadeensino where processomatriculaunidadeensino.processomatricula = processomatricula.codigo
	)
);


CREATE OR REPLACE FUNCTION public.fn_validarexistenciaprocessomatriculacalendario(codigo_processomatricula integer, codigo_unidadeensinocurso integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
       quantidadeRegistro integer;
BEGIN
 --Efetua a consulta para verificar se o processoMatricula inserido contém processomatriculacalendario criado.
  SELECT INTO quantidadeRegistro COUNT(processomatricula.codigo) 
    FROM processomatricula  
    INNER JOIN processomatriculacalendario ON processomatricula.codigo = processomatriculacalendario.processomatricula
    INNER JOIN processomatriculaunidadeensino ON processomatricula.codigo = processomatriculaunidadeensino.processomatricula
    INNER JOIN unidadeensinocurso ON unidadeensinocurso.codigo = codigo_unidadeEnsinoCurso
    WHERE processomatriculacalendario.processomatricula  = codigo_processoMatricula
     AND  processomatriculacalendario.curso =  unidadeensinocurso.curso 
     AND  processomatriculacalendario.turno =  unidadeensinocurso.turno 
     and processomatriculaunidadeensino.unidadeensino = unidadeensinocurso.unidadeensino
     AND  processomatriculacalendario.periodoletivoativounidadeensinocurso IS NOT NULL;
--Verifica se há registro retornado da consulta.
      IF quantidadeRegistro > 0 THEN
            RETURN   TRUE;
      ELSE
            RETURN   FALSE;
      END IF;                                                                    
END;
$function$
;

alter table processomatriculacalendario drop column unidadeensinocurso;
alter table periodoletivoativounidadeensinocurso drop column unidadeensinocurso;
alter table processomatricula drop column unidadeensino;


CREATE OR REPLACE FUNCTION public.fn_validaruematriculaigualueprocessomatricula(codigo_processomatricula integer, codigo_unidadeensinocurso integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
       quantidadeRegistro integer;
BEGIN
 --Efetua a consulta para verificar se a unidade de ensino presente no processoMatricula é igual a unidade de ensino da matrícula.
SELECT INTO quantidadeRegistro count(processomatricula.codigo) AS quantidadeRegistro
FROM unidadeensinocurso  
INNER JOIN processomatricula ON processomatricula.codigo = codigo_processoMatricula
inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo
and unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino
WHERE unidadeensinocurso.codigo         = codigo_unidadeensinocurso ;
--Verifica se há registro retornado da consulta.
      IF quantidadeRegistro > 0 THEN
            RETURN   TRUE;
      ELSE
            RETURN   FALSE;
      END IF;                                                                    
END;
$function$
;
