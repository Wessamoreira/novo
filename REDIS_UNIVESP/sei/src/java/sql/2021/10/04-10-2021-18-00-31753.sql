CREATE OR REPLACE FUNCTION elastic.agendar_alunos() RETURNS void LANGUAGE plpgsql AS $function$
	DECLARE
		r record;
	BEGIN
		FOR r IN select p.codigo, to_json(p) conteudo from (
			select a.codigo, ar.cpfrequerimento||'/'||ar.nome foto, trim(replace(a.nome, '''', '')) nome, array_to_string(array_agg(elastic.filtrar_aphanumeric(m.matricula)),' / ') matriculas, a.registroacademico, trim(elastic.filtrar_aphanumeric(a.cpf)) cpf, trim(a.email) email,
			coalesce((select array_agg(p2) from (
				select trim(replace(pf.nome, '''', '')) nome, trim(elastic.filtrar_aphanumeric(pf.cpf)) cpf, trim(pf.email) email, f.tipo, f.responsavelfinanceiro from filiacao f
				inner join pessoa pf on pf.codigo = f.pais
				where f.aluno = a.codigo
			) p2),'{}'::record[]) filiacao
			from pessoa a
			left join matricula m on m.aluno = a.codigo
			left join arquivo ar on ar.codigo = a.arquivoimagem
			where a.aluno
			group by a.codigo, a.registroacademico, ar.cpfrequerimento, ar.nome
		) p
		LOOP
			perform elastic.agendar_sync_pg_es('post', (select elastic.consultar_indice('aluno')||r.codigo), r.conteudo);
		END LOOP;
		return;
	END;
$function$;

CREATE OR REPLACE FUNCTION elastic.montar_query_aluno(text, integer) RETURNS json LANGUAGE sql AS $function$
	select ('{
        "query":{
            "bool":{
                "should":[
                    {"match":{"nome":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":4,"fuzziness":"AUTO"}}},
                    {"match":{"matriculas":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":3,"fuzziness":"AUTO"}}},
                    {"match":{"registroacademico":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":3,"fuzziness":"AUTO"}}},
                    {"match":{"cpf":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":2,"fuzziness":"AUTO"}}},
                    {"match":{"email":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":2,"fuzziness":"AUTO"}}},
                    {"match":{"filiacao.nome":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":1.5,"fuzziness":"AUTO"}}},
                    {"match":{"filiacao.cpf":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":1.5,"fuzziness":"AUTO"}}},
                    {"match":{"filiacao.email":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":1.5,"fuzziness":"AUTO"}}}]
            }
        },
        "size":"'||$2||'",
        "highlight":{
            "pre_tags":["<b>"],
            "post_tags":["</b>"],
            "fields":{
                "nome":{},
                "matriculas":{},
                "registroacademico":{},
                "cpf":{},
                "email":{},
                "filiacao.nome":{},
                "filiacao.cpf":{},
                "filiacao.email":{}
            }
        }
    }')::json;
$function$;

CREATE OR REPLACE FUNCTION elastic.montar_aluno(integer) RETURNS json LANGUAGE sql AS $function$
    select to_json(p) from (
		select t.codigo, t.foto, t.nome, t.matriculas, t.registroacademico, t.cpf, t.email, coalesce(t.filiacao, '[]'::json) filiacao from (
			select a.codigo, ar.cpfrequerimento||'/'||ar.nome foto, trim(replace(a.nome, '''', '')) nome, array_to_string(array_agg(elastic.filtrar_aphanumeric(m.matricula)),' / ') matriculas, a.registroacademico, trim(elastic.filtrar_aphanumeric(a.cpf)) cpf, trim(a.email) email,
			(select array_to_json(array_agg(p2)) from (
				select trim(replace(pf.nome, '''', '')) nome, trim(elastic.filtrar_aphanumeric(pf.cpf)) cpf, trim(pf.email) email, f.tipo, f.responsavelfinanceiro from filiacao f
				inner join pessoa pf on pf.codigo = f.pais
				where f.aluno = a.codigo
			) p2) filiacao
			from pessoa a
			left join matricula m on m.aluno = a.codigo
			left join arquivo ar on ar.codigo = a.arquivoimagem
			where a.codigo = $1
			group by a.codigo, a.registroacademico, ar.cpfrequerimento, ar.nome
		) t
	) p
$function$;

drop trigger alterar_aluno on public.pessoa;
create trigger alterar_aluno after update on public.pessoa for each row when ((
    ((old.nome)::text is distinct       from    (new.nome)::text)
    or ((old.cpf)::text is distinct     from    (new.cpf)::text)
    or ((old.email)::text is distinct   from    (new.email)::text)
    or ((old.registroacademico)::text is distinct   from    (new.registroacademico)::text)
    or (old.aluno is distinct           from    new.aluno)
    or (old.arquivoimagem is distinct   from    new.arquivoimagem)
)) execute procedure elastic.reindexar_aluno_alterando_aluno();