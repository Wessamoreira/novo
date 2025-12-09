drop function aulaaluno_disciplinanormal(integer,boolean,integer,boolean,integer[]);

CREATE OR REPLACE FUNCTION public.aulaaluno_disciplinanormal(historico integer, trazerdisciplinacomposta boolean, professor integer, apenasturmaorigem boolean, listaturma integer[])
 RETURNS TABLE(professor_codigo integer, professor_nome text, professores_codigo integer[], disciplina_codigo integer, disciplina_nome text, datainicio date, datatermino date, modalidadedisciplina text, turmabaseprogramado integer)
 LANGUAGE plpgsql
AS $function$
BEGIN
  RETURN QUERY
	SELECT DISTINCT 
	  max(pessoa.codigo) as professor_codigo, array_to_string(array_agg(distinct pessoa.nome), ', ')::TEXT as professor_nome,   array_agg(distinct pessoa.codigo) as professores_codigo,
	 disciplina.codigo as disciplina_codigo, disciplina.nome::TEXT as disciplina_nome , min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL'::TEXT as modalidadeDisciplina, 
	 case when matriculaperiodoturmadisciplina.turmateorica is not null and matriculaperiodoturmadisciplina.turmateorica = professortitulardisciplinaturma.turma then 0 
	 else case when matriculaperiodoturmadisciplina.turmapratica is not null and matriculaperiodoturmadisciplina.turmapratica = professortitulardisciplinaturma.turma then 1 
	 else case when matriculaperiodoturmadisciplina.turma = professortitulardisciplinaturma.turma then 2 
	 else 3 end end end as turmabaseprogramado  
	 FROM horarioturma  
	 INNER JOIN horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
	 inner join turma on turma.codigo = horarioturma.turma
	 INNER JOIN horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
	 and horarioturma.turma = ANY(listaturma)
	 INNER JOIN disciplina on disciplina.codigo = horarioturmadiaitem.disciplina
	 INNER JOIN historico on historico.codigo = $1
	 INNER JOIN matricula on matricula.matricula = historico.matricula
	 INNER JOIN curso on matricula.curso = curso.codigo
	 INNER JOIN matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
	 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
	 left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = disciplina.codigo
		and professortitulardisciplinaturma.turma = horarioturma.turma -- turma.codigo 
		and ((curso.periodicidade = 'AN' and professortitulardisciplinaturma.ano = matriculaperiodo.ano)
		or (curso.periodicidade = 'SE' and professortitulardisciplinaturma.ano = matriculaperiodo.ano and professortitulardisciplinaturma.semestre = matriculaperiodo.semestre)
		or (curso.periodicidade = 'IN')
		) and professortitulardisciplinaturma.titular and ($3 is null or $3 = 0)
	INNER JOIN pessoa on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = pessoa.codigo) or (professortitulardisciplinaturma.codigo is null and pessoa.codigo = horarioturmadiaitem.professor))
	 where ((turma.turmaagrupada 
	 and (disciplina.codigo = historico.disciplina or exists(
	 	select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina 
	 	and disciplinaequivalente.equivalente  = historico.disciplina 
	 ) or exists(
	 	select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina 
	 	and disciplinaequivalente.disciplina  = historico.disciplina 
	 ))) or (turma.turmaagrupada = false and horarioturmadiaitem.disciplina  =  historico.disciplina ))
	 and (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null))
	 and ((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
	   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
	   or (curso.periodicidade = 'IN')
	  ) 
	
	 GROUP BY disciplina.codigo, disciplina.nome, turmabaseprogramado, 'PRESENCIAL'::text
	 order by turmabaseprogramado;
END; $function$
;
