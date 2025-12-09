alter table disciplina add column classificacaoDisciplina varchar(60) default 'NENHUMA';
alter table disciplina add column modeloGeracaoSalaBlackboard varchar(50) default 'DISCIPLINA';
alter table disciplina add column dividirSalaEmGrupo boolean default false;
alter table disciplina add column nrMaximoAulosPorSala int default 1000;
alter table disciplina add column nrMaximoAulosPorGrupo int default  0;
alter table salaaulablackboardpessoa add column memberId varchar(100);
alter table salaaulablackboardpessoa add column userId varchar(100);

alter table salaaulablackboardoperacao add column curso int;
alter table salaaulablackboardoperacao add constraint fk_salaaulablackboardoperacao_curso foreign key (curso) references curso(codigo) on delete cascade on update cascade;

alter table configuracaoseiblackboarddominio add column tipoUsuarioBlackboard varchar(50) default 'Student';
update configuracaoseiblackboarddominio set tipoUsuarioBlackboard =  'Instructor' where tipopessoaenum = 'PROFESSOR' and tipoUsuarioBlackboard = 'Student';
update configuracaoseiblackboarddominio set tipoUsuarioBlackboard =  'TeachingAssistant' where tipopessoaenum = 'COORDENADOR' and tipoUsuarioBlackboard = 'Student';
alter table configuracaoseiblackboarddominio drop column tipopessoaenum;



CREATE OR REPLACE FUNCTION public.fn_persistirsalaaulablackboardoperacao()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
DECLARE
   v_email TEXT;
   v_codigomatriculaperiodo INTEGER;   
   v_modeloGeracaoSalaBlackboard text;
   v_curso INTEGER;
begin
	IF (select ativaroperacoesdoblackboard from configuracaoseiblackboard) then
		IF (TG_OP = 'DELETE') THEN	
			v_codigomatriculaperiodo := old.matriculaperiodo;
		ELSE
			v_codigomatriculaperiodo := new.matriculaperiodo;
		END IF;
		 
		select pessoaemailinstitucional.email, matricula.curso,  disciplina.modeloGeracaoSalaBlackboard INTO v_email, v_curso,  v_modeloGeracaoSalaBlackboard  from matriculaperiodo		
		inner join matricula on matriculaperiodo.matricula = matricula.matricula 	
	    inner join disciplina on disciplina.codigo = new.disciplina 
	    inner join pessoaemailinstitucional on matricula.aluno = pessoaemailinstitucional.pessoa and pessoaemailinstitucional.statusativoinativoenum = 'ATIVO'
	    and pessoaemailinstitucional.codigo = (select pei.codigo from pessoaemailinstitucional pei where pei.pessoa = matricula.aluno and pei.statusativoinativoenum = 'ATIVO' order by pei.codigo desc limit 1 )
	    where  matriculaperiodo.codigo = v_codigomatriculaperiodo  ;
	
		IF (v_email is not null and TG_OP = 'INSERT') THEN 
				INSERT INTO public.salaaulablackboardoperacao (curso, turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
				VALUES(case when v_modeloGeracaoSalaBlackboard = 'CURSO' then  v_curso else null end, case when v_modeloGeracaoSalaBlackboard = 'TURMA' then  new.turma else null end, case when v_modeloGeracaoSalaBlackboard = 'TURMA' then  new.turmapratica else null end, case when v_modeloGeracaoSalaBlackboard = 'TURMA' then  new.turmateorica else null end, new.disciplina, null, new.ano, new.semestre, v_email, TG_OP, false, new.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
						 
		ELSEIF (v_email is not null and TG_OP = 'DELETE') then
				INSERT INTO public.salaaulablackboardoperacao (curso, turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
				VALUES(case when v_modeloGeracaoSalaBlackboard = 'CURSO' then  v_curso else null end, case when v_modeloGeracaoSalaBlackboard = 'TURMA' then  new.turma else null end, case when v_modeloGeracaoSalaBlackboard = 'TURMA' then  new.turmapratica else null end, case when v_modeloGeracaoSalaBlackboard = 'TURMA' then  new.turmateorica else null end, old.disciplina, null, old.ano, old.semestre, v_email, TG_OP, false, old.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
			
		ELSEIF (v_modeloGeracaoSalaBlackboard = 'TURMA' and v_email is not null and  TG_OP = 'UPDATE' and ((new.turma !=  old.turma) 
					or ((new.turmapratica is not null and  old.turmapratica is not null and new.turmapratica !=  old.turmapratica) or (new.turmapratica is null and  old.turmapratica is not null ) or new.turmapratica is not null and  old.turmapratica is null ) 
					or ((new.turmateorica is not null and  old.turmateorica is not null and new.turmateorica !=  old.turmateorica) or (new.turmateorica is null and  old.turmateorica is not null ) or new.turmateorica is not null and  old.turmateorica is null ) 
					or (new.disciplina !=  old.disciplina) or (new.ano !=  old.ano) or (new.semestre !=  old.semestre))) THEN 
			 	INSERT INTO public.salaaulablackboardoperacao (turma, turmaold, turmapratica, turmapraticaold, turmateorica, turmateoricaold, disciplina, disciplinaold, professor, ano, anoold, semestre, semestreold, email, operacao, executada, codigoorigem, tipoorigem) 
			    VALUES(new.turma, old.turma,new.turmapratica, old.turmapratica,new.turmateorica, old.turmateorica, new.disciplina, old.disciplina, new.professor, new.ano, old.ano, new.semestre, old.semestre, v_email, 'UPDATE', false, new.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
			   
			   ELSEIF (v_modeloGeracaoSalaBlackboard = 'DISCIPLINA' and v_email is not null and  TG_OP = 'UPDATE' and ((new.disciplina !=  old.disciplina) or (new.ano !=  old.ano) or (new.semestre !=  old.semestre))) THEN 
			 	INSERT INTO public.salaaulablackboardoperacao (disciplina, disciplinaold, ano, anoold, semestre, semestreold, email, operacao, executada, codigoorigem, tipoorigem) 
			    VALUES(new.disciplina, old.disciplina, new.ano, old.ano, new.semestre, old.semestre, v_email, 'UPDATE', false, new.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
					
		END IF;		
	 END IF;
	 RETURN NEW;
END;
$function$
;
