CREATE OR REPLACE FUNCTION elastic.agendar_alunos() RETURNS void LANGUAGE plpgsql AS $function$
	DECLARE
		r record;
	BEGIN
		FOR r IN select p.codigo, to_json(p) conteudo from (
			select a.codigo, ar.cpfrequerimento||'/'||ar.nome foto, trim(a.nome) nome, array_to_string(array_agg(m.matricula),', ') matriculas, trim(elastic.filtrar_aphanumeric(a.cpf)) cpf, trim(a.email) email,
			coalesce((select array_agg(p2) from (
				select trim(pf.nome) nome, trim(elastic.filtrar_aphanumeric(pf.cpf)) cpf, trim(pf.email) email, f.tipo, f.responsavelfinanceiro from filiacao f
				inner join pessoa pf on pf.codigo = f.pais
				where f.aluno = a.codigo
			) p2),'{}'::record[]) filiacao
			from pessoa a
			left join matricula m on m.aluno = a.codigo
			left join arquivo ar on ar.codigo = a.arquivoimagem
			where a.aluno
			group by a.codigo, ar.cpfrequerimento, ar.nome
		) p
		LOOP
			perform elastic.agendar_sync_pg_es('post', (select elastic.consultar_indice('aluno')||r.codigo), r.conteudo);
		END LOOP;
		return;
	END;
$function$;

CREATE OR REPLACE FUNCTION elastic.montar_aluno(integer) RETURNS json LANGUAGE sql AS $function$
select to_json(p) from (
  select t.codigo, t.foto, t.nome, t.matriculas, t.cpf, t.email, coalesce(t.filiacao, '[]'::json) filiacao from (
   select a.codigo, ar.cpfrequerimento||'/'||ar.nome foto, trim(a.nome) nome, array_to_string(array_agg(m.matricula),', ') matriculas, trim(elastic.filtrar_aphanumeric(a.cpf)) cpf, trim(a.email) email,
   (select array_to_json(array_agg(p2)) from (
    select trim(pf.nome) nome, trim(elastic.filtrar_aphanumeric(pf.cpf)) cpf, trim(pf.email) email, f.tipo, f.responsavelfinanceiro from filiacao f
    inner join pessoa pf on pf.codigo = f.pais
    where f.aluno = a.codigo
   ) p2) filiacao
   from pessoa a
   left join matricula m on m.aluno = a.codigo
   left join arquivo ar on ar.codigo = a.arquivoimagem
   where a.codigo = $1
   group by a.codigo, ar.cpfrequerimento, ar.nome
  ) t
 ) p
$function$;