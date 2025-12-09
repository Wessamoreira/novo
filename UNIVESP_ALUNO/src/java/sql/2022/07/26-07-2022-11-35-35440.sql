create table if not exists  logoperacaoensalamentoblackboard(
 codigo serial,
 ano varchar(4),
 semestre varchar(1),
 codigodisciplina int,
 nomedisciplina varchar(300),
 abreviaturadisciplina varchar(150),
 matricula varchar(150),
 tipoSalaAulaBlackboardPessoa  varchar(50),
 pessoa  varchar(350),
 emailInstitucional  varchar(150),
 codigoSalaAulaBlackBoard int,
 idSalaAulaBlackBoard varchar(350),
 salaAulaBlackBoard varchar(350),
 tipoSalaAulaBlackboard varchar(50),
 idGrupoBlackBoard varchar(350),
 grupoBlackBoard varchar(350),
 operacaoEnsalacaoBlackboard varchar(30),
 observacao text,
 created timestamp,
 codigoCreated int,
 nomeCreated varchar(350),
 
 constraint pk_logoperacaoensalamentoblackboard primary key (codigo)
);

CREATE OR REPLACE FUNCTION public.fn_persistirsalaaulablackboardoperacao()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
declare 
  pos int;
  ul int;
  comentario text;
  query text;
   
begin
	IF ((select codigo from configuracaoseiblackboard) is not null) then
			   
	query := current_query();
	 pos := strpos(query, '--ul:');
	 if pos > 0
	  then 
	   comentario := substr(query, pos + 5, length(query));
	   pos := strpos(comentario, '--');
	   if(pos > 0) then
    		ul := substr(comentario, 0, pos);
	   else
    		ul := trim(comentario);
	   end if;
	  else ul := 0;
	 end if;
	
				INSERT INTO public.salaaulablackboardoperacao (operacao, executada, codigoorigem, tipoorigem,  created, tipoSalaAulaBlackboardPessoaEnum, pessoa, idsalaaulablackboard, email, codigocreated) 
				(select 'DELETE', false, salaaulablackboardpessoa.salaaulablackboard, 'SALA_AULA_BLACKBOARD_PESSOA', now(), 'ALUNO', pessoaemailinstitucional.pessoa, salaaulablackboard.idsalaaulablackboard, pessoaemailinstitucional.email, ul 
				from salaaulablackboardpessoa 
				inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard
				inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional
			    where salaaulablackboardpessoa.matriculaperiodoturmadisciplina = OLD.codigo
			    and salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum = 'ALUNO');
			
				
		
	 END IF;
 	 update salaaulablackboardpessoa set matriculaperiodoturmadisciplina = null where matriculaperiodoturmadisciplina = OLD.codigo;
	 RETURN OLD;
END;
$function$
;



 create or replace function inserirLogOperacaoEnsalamentoBlack() returns trigger 
 LANGUAGE plpgsql
AS $function$
 DECLARE
   pos int;
   ul int;
   comentario text;
  query text;
 begin 	 
	 if(TG_OP != 'INSERT') then
	 query := current_query();
	 pos := strpos(query, '--ul:');
	 if pos > 0
	  then 
	   comentario := substr(query, pos + 5, length(query));
	   pos := strpos(comentario, '--');
	   if(pos > 0) then
    		ul := substr(comentario, 0, pos);
	   else
    		ul := trim(comentario);
	   end if;
	  else ul := 0;
	 end if;
	else 
	 ul := new.codigoCreated;
	end if;
	 
	 insert into logOperacaoEnsalamentoBlackboard (codigoDisciplina, nomeDisciplina, abreviaturaDisciplina, matricula, tipoSalaAulaBlackboardPessoa, pessoa, emailInstitucional, 
	 codigoSalaAulaBlackBoard, idSalaAulaBlackBoard, tipoSalaAulaBlackboard, 
	 salaAulaBlackBoard, idGrupoBlackBoard, grupoBlackBoard, operacaoEnsalacaoBlackboard, ano, semestre, created, codigoCreated, nomeCreated) (
	  select disciplina.codigo, disciplina.nome, disciplina.abreviatura,  salaaulablackboardpessoa.matricula,
	  salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum,  pessoa.nome, pessoaemailinstitucional.email, 
	  salaaulablackboard.codigo, salaaulablackboard.id, salaaulablackboard.tiposalaaulablackboardenum,  
	  salaaulablackboard.idsalaaulablackboard, salaaulablackboard.idgrupo,
	  salaaulablackboard.nomegrupo, case when TG_OP = 'INSERT' then 'INCLUIR' else 'EXCLUIR' end,   
	  salaaulablackboard.ano, salaaulablackboard.semestre, now(), usuario.codigo, case when usuario.nome is null or usuario.nome =  '' then usuario.username else usuario.nome  end 
	  from salaaulablackboard
	  inner join salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo
	  inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional
  	  inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa
	  left join disciplina on disciplina.codigo  = salaaulablackboard.disciplina 
	  left join usuario on usuario.codigo = ul
	  where salaaulablackboardpessoa.codigo = case when TG_OP = 'INSERT' then new.codigo else OLD.codigo end 
	 );
	 return case when TG_OP = 'INSERT' then new else OLD end ;
 end;
$function$;

CREATE trigger logensalamentoblackboard_insert AFTER INSERT ON salaaulablackboardpessoa FOR EACH ROW EXECUTE PROCEDURE inserirLogOperacaoEnsalamentoBlack();
CREATE trigger logensalamentoblackboard_delete BEFORE DELETE ON salaaulablackboardpessoa FOR EACH ROW EXECUTE PROCEDURE inserirLogOperacaoEnsalamentoBlack();

