CREATE OR REPLACE FUNCTION public.consultarplanoensino(unidadeensino integer, curso integer, ano character varying, semestre character varying, disciplina integer, turno integer, professor integer, situacao character varying)
 RETURNS TABLE(codigo integer)
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
 
     select codigo from (  
select row_number() over(partition by planoensino.unidadeensino, planoensino.curso, planoensino.disciplina order by planoensino.unidadeensino, planoensino.curso, planoensino.disciplina,						 
						 case when planoensino.professorresponsavel is not null then 1 else 2 end, 						 
						 case when planoensino.turno is not null then 1 else 2 end, 
		planoensino.ano  desc,
		planoensino.semestre desc) as ordem,
planoensino.codigo, planoensino.unidadeensino, planoensino.curso, planoensino.turno, planoensino.disciplina, planoensino.professorresponsavel
from planoensino
left join curso on curso.codigo = planoensino.curso
where 
		 (((curso.periodicidade = 'SE' or planoensino.periodicidade = 'SE') and (planoensino.ano||planoensino.semestre) <= ($3||$4))
		 or ((curso.periodicidade = 'AN' or planoensino.periodicidade = 'AN') and planoensino.ano <= $3)
		 or (curso.periodicidade = 'IN' or planoensino.periodicidade = 'IN'))
and (planoensino.unidadeensino = $1 or planoensino.unidadeensino is null)
and (planoensino.curso = $2 or planoensino.curso is null)
and planoensino.disciplina = $5  
and (planoensino.professorresponsavel = $7  or professorresponsavel is null)
and (planoensino.turno = $6  or turno is null)
and ($8 is null or $8 = '' or situacao = $8)
order by ordem limit 1 
  ) as t
$function$
;
