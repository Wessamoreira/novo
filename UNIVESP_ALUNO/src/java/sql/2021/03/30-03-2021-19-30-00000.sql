CREATE TABLE public.configuracaoseiblackboard(
  codigo SERIAL NOT NULL,
  usernameseiblackboard varchar(255),
  senhaseiblackboard varchar(255),
  clienteseiblackboard varchar(255),
  tokenseiblackboard varchar(255),
  urlexternaseiblackboard varchar(255),
  idblackboard varchar(255),  
  clientblackboard varchar(255),
  secretblackboard varchar(255),
  urlexternablackboard varchar(255),
  ativaroperacoesdoblackboard boolean default false,
  CONSTRAINT configuracaoseiblackboard_pkey PRIMARY KEY (codigo)  
);

CREATE TABLE public.configuracaoseiblackboarddominio (
	codigo serial NOT NULL,
	configuracaoseiblackboard int4 NULL,
	dominioemail varchar(255) NULL,
	permissaosistema varchar(255) NULL,
	permissaoinstitucional varchar(255) NULL,	
	tipopessoaenum varchar(100) NULL DEFAULT 'NENHUM'::character varying,
	CONSTRAINT configuracaoseiblackboarddominio_pkey PRIMARY KEY (codigo),
	CONSTRAINT dominioemail_unique_configuracaoseiblackboarddominio UNIQUE (configuracaoseiblackboard, dominioemail),
	CONSTRAINT fk_configuracaoseiblackboarddominio_configuracaoseiblackboard FOREIGN KEY (configuracaoseiblackboard) REFERENCES configuracaoseiblackboard(codigo) ON UPDATE CASCADE ON DELETE CASCADE
);


 CREATE TABLE salaaulablackboard(
	codigo serial NOT NULL,
	turma int4 NULL,
	disciplina int4 NULL,
	professoread int4 NULL,
	ano varchar(4) NULL,
	semestre varchar(1) NULL,
	linksalaaulablackboard varchar(255) NULL,
	idsalaaulablackboard varchar(255) NULL,
	id varchar(255)NULL,
	CONSTRAINT salaaulablackboard_unique_idsalaaulablackboard UNIQUE (idsalaaulablackboard),
	CONSTRAINT pk_salaaulablackboard_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_salaaulablackboard_disciplina FOREIGN KEY (disciplina) REFERENCES disciplina(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_salaaulablackboard_professoread FOREIGN KEY (professoread) REFERENCES pessoa(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_salaaulablackboard_turma FOREIGN KEY (turma) REFERENCES turma(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT  
);

CREATE TABLE public.salaaulablackboardoperacao (
	codigo serial NOT NULL,
	turma int4 NULL,
	turmaold int4 NULL,
	turmapratica int4 NULL,
	turmapraticaold int4 NULL,
	turmateorica int4 NULL,
	turmateoricaold int4 NULL,
	disciplina int4 NULL,
	disciplinaold int4 NULL,
	professor int4 NULL,
	ano varchar(4) NULL,
	anoold varchar(4) NULL,
	semestre varchar(1) NULL,
	semestreold varchar(1) NULL,
	email varchar(100) NULL,
	operacao varchar(100) NULL,
	executada bool NULL DEFAULT false,
	codigoorigem int4 NULL,
	tipoorigem varchar(250) NULL,
	erro text NULL,
	CONSTRAINT pk_salaaulablackboardoperacao_codigo PRIMARY KEY (codigo)
);

CREATE OR REPLACE FUNCTION public.fn_persistirsalaaulablackboardoperacao()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
DECLARE
   v_email TEXT;
   v_codigomatriculaperiodo INTEGER;   
begin
	IF (select ativaroperacoesdoblackboard from configuracaoseiblackboard) then
		IF (TG_OP = 'DELETE') THEN	
			v_codigomatriculaperiodo := old.matriculaperiodo;
		ELSE
			v_codigomatriculaperiodo := new.matriculaperiodo;
		END IF;
		 
		select pessoaemailinstitucional.email INTO v_email from pessoaemailinstitucional
		inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa
		inner join matricula on matricula.aluno = pessoa.codigo 
		inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = v_codigomatriculaperiodo;
		IF (v_email is not null and TG_OP = 'INSERT') THEN 
				INSERT INTO public.salaaulablackboardoperacao (turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
				VALUES(new.turma, new.turmapratica, new.turmateorica, new.disciplina, new.professor, new.ano, new.semestre, v_email, TG_OP, false, new.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
						 
		ELSEIF (v_email is not null and TG_OP = 'DELETE') then
				INSERT INTO public.salaaulablackboardoperacao (turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
				VALUES(old.turma, old.turmapratica, old.turmateorica, old.disciplina, old.professor, old.ano, old.semestre, v_email, TG_OP, false, old.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
			
		ELSEIF (v_email is not null and  TG_OP = 'UPDATE' and ((new.turma !=  old.turma) 
					or ((new.turmapratica is not null and  old.turmapratica is not null and new.turmapratica !=  old.turmapratica) or (new.turmapratica is null and  old.turmapratica is not null ) or new.turmapratica is not null and  old.turmapratica is null ) 
					or ((new.turmateorica is not null and  old.turmateorica is not null and new.turmateorica !=  old.turmateorica) or (new.turmateorica is null and  old.turmateorica is not null ) or new.turmateorica is not null and  old.turmateorica is null ) 
					or (new.disciplina !=  old.disciplina) or (new.ano !=  old.ano) or (new.semestre !=  old.semestre))) THEN 
			 	INSERT INTO public.salaaulablackboardoperacao (turma, turmaold, turmapratica, turmapraticaold, turmateorica, turmateoricaold, disciplina, disciplinaold, professor, ano, anoold, semestre, semestreold, email, operacao, executada, codigoorigem, tipoorigem) 
			    VALUES(new.turma, old.turma,new.turmapratica, old.turmapratica,new.turmateorica, old.turmateorica, new.disciplina, old.disciplina, new.professor, new.ano, old.ano, new.semestre, old.semestre, v_email, 'UPDATE', false, new.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
			
		ELSEIF (v_email is not null and  TG_OP = 'UPDATE' and ((new.professor is not null and old.professor is null) or (new.professor is null and old.professor is not null) or (new.professor != old.professor))
				    and (new.turma =  old.turma) and (new.disciplina =  old.disciplina) and (new.ano =  old.ano) and (new.semestre =  old.semestre)) THEN
			IF ((new.professor is not null and old.professor is null) or (new.professor != old.professor) )THEN
					INSERT INTO public.salaaulablackboardoperacao (turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
					VALUES(new.turma, new.turmapratica, new.turmateorica, new.disciplina, new.professor, new.ano, new.semestre, v_email, 'INSERT', false, new.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
			END IF;
		 	IF ((old.professor is not null and new.professor is null) or (new.professor != old.professor) ) THEN
			 		INSERT INTO public.salaaulablackboardoperacao (turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
					VALUES(old.turma, old.turmapratica, old.turmateorica, old.disciplina, old.professor, old.ano, old.semestre, v_email, 'DELETE', false, old.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
		 	END IF;
		END IF;		
	 END IF;
	 RETURN NEW;
END;
$function$
;



create trigger tg_persistirsalaaulablackboardoperacao after insert or delete or update on public.matriculaperiodoturmadisciplina for each row execute function fn_persistirsalaaulablackboardoperacao();


alter table if exists ProgramacaoTutoriaOnline  add column if not exists executarsalaaulablackboardautomatico boolean default false;
