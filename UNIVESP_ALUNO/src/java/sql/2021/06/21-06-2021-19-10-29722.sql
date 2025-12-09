alter table salaaulablackboard add column programacaotutoriaonline int;
alter table salaaulablackboard add constraint fk_salaaulablackboard_programacaotutoriaonline foreign key (programacaotutoriaonline) references programacaotutoriaonline(codigo);

create index idx_pessoaemailinstitucional_pessoa_situacao on pessoaemailinstitucional(pessoa, statusativoinativoenum);
create index idx_pessoaemailinstitucional_email on pessoaemailinstitucional(email);
create index idx_salaaulablackboardpessoa_mptd on salaaulablackboardpessoa(matriculaperiodoturmadisciplina);
create index idx_salaaulablackboardpessoa_salaaulablackboard on salaaulablackboardpessoa(salaaulablackboard);

ALTER TABLE public.salaaulablackboardpessoa ADD CONSTRAINT unq_salaaulablackboardpessoa UNIQUE (salaaulablackboard, tiposalaaulablackboardpessoaenum, pessoaemailinstitucional);

CREATE OR REPLACE FUNCTION public.fn_persistirsalaaulablackboardoperacao()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
DECLARE
   v_email TEXT;
   v_codigomatriculaperiodoturmadisciplina INTEGER; 
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
		IF (TG_OP = 'DELETE') THEN	
			v_codigomatriculaperiodoturmadisciplina := old.codigo;
		ELSE
			v_codigomatriculaperiodoturmadisciplina := new.codigo;
		END IF;
		 
		select pessoaemailinstitucional.email, matricula.curso, case when salaaulablackboard.turma is not null then 'TURMA' else 
		case when salaaulablackboard.curso is not null then 'CURSO' else case when salaaulablackboard.disciplina is not null then 'DISCIPLINA' else '' end end end  INTO v_email, v_curso, v_modeloGeracaoSalaBlackboard  from matriculaperiodo		
		inner join matricula on matriculaperiodo.matricula = matricula.matricula 		    
	    inner join pessoaemailinstitucional on matricula.aluno = pessoaemailinstitucional.pessoa and pessoaemailinstitucional.statusativoinativoenum = 'ATIVO'	    
	    and pessoaemailinstitucional.codigo = (select pei.codigo from pessoaemailinstitucional pei where pei.pessoa = matricula.aluno and pei.statusativoinativoenum = 'ATIVO' order by pei.codigo desc limit 1 )
	    left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = v_codigomatriculaperiodoturmadisciplina
	    left join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard
	    where  matriculaperiodo.codigo = v_codigomatriculaperiodo  ;
	
		IF (v_email is not null and TG_OP = 'INSERT') THEN 
				INSERT INTO public.salaaulablackboardoperacao (curso, turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
				VALUES(v_curso, new.turma, new.turmapratica, new.turmateorica, new.disciplina, null, new.ano, new.semestre, v_email, TG_OP, false, new.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
						 
		ELSEIF (v_email is not null and TG_OP = 'DELETE') then
				INSERT INTO public.salaaulablackboardoperacao (curso, turma, turmapratica, turmateorica, disciplina, professor, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem) 
				VALUES(v_curso, new.turma, new.turmapratica, new.turmateorica, old.disciplina, null, old.ano, old.semestre, v_email, TG_OP, false, old.codigo, 'MATRICULA_PERIODO_TURMA_DISCIPLINA');
			
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
