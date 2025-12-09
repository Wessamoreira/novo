CREATE OR REPLACE FUNCTION public.aulaaluno_disciplinaead(historico integer, trazerdisciplinacomposta boolean, professor integer)
 RETURNS TABLE(professor_codigo integer, professor_nome text, professores_codigo integer[], disciplina_codigo integer, disciplina_nome text, datainicio date, datatermino date, modalidadedisciplina text)
 LANGUAGE plpgsql
AS $function$
BEGIN
  RETURN QUERY
SELECT DISTINCT 
		 pessoa.codigo as professor_codigo , pessoa.nome::text as professor_nome, array_agg(pessoa.codigo) as professores_codigo, disciplina.codigo as disciplina_codigo, disciplina.nome::text as disciplina_nome,
   	     case when curso.periodicidade = 'SE' then
		 case when gradedisciplina.bimestre in (0, 1) then periodoletivoativounidadeensinocurso.datainicioperiodoletivo::date else periodoletivoativounidadeensinocurso.datainicioperiodoletivosegundobimestre end else null end as datainicio,
         case when curso.periodicidade = 'SE' then
         case when gradedisciplina.bimestre = 1 then periodoletivoativounidadeensinocurso.datafimperiodoletivoprimeirobimestre else periodoletivoativounidadeensinocurso.datafimperiodoletivosegundobimestre end else null end as datatermino, 
          'ON_LINE'::text as modalidadeDisciplina
		FROM matriculaperiodoturmadisciplina 
		inner join turmadisciplina on turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina and turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.definicoestutoriaonline = 'DINAMICA'
		inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina 
		inner join historico on historico.codigo = $1 and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina 
		inner join matriculaperiodo on matriculaperiodo.codigo =  historico.matriculaperiodo 
		inner join gradedisciplina on gradedisciplina.codigo =  historico.gradedisciplina
		inner join unidadeensinocurso on matriculaperiodo.unidadeensinocurso =  unidadeensinocurso.codigo 
		inner join curso on curso.codigo =  unidadeensinocurso.curso
		inner join processomatriculacalendario on matriculaperiodo.processomatricula =  processomatriculacalendario.processomatricula 
		and unidadeensinocurso.curso = processomatriculacalendario.curso  and unidadeensinocurso.turno = processomatriculacalendario.turno
		inner join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo =  processomatriculacalendario.periodoletivoativounidadeensinocurso
		left join programacaotutoriaonline on programacaotutoriaonline.disciplina = matriculaperiodoturmadisciplina.disciplina and programacaotutoriaonline.situacao = 'ATIVO'
		left join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo and programacaotutoriaonlineprofessor.situacaoprogramacaotutoriaonline = 'ATIVO'
		left join pessoa on programacaotutoriaonlineprofessor.professor = pessoa.codigo 		
		group by pessoa.codigo , pessoa.nome, disciplina.codigo, disciplina.nome, curso.periodicidade,gradedisciplina.bimestre, periodoletivoativounidadeensinocurso.datainicioperiodoletivo, periodoletivoativounidadeensinocurso.datainicioperiodoletivosegundobimestre, periodoletivoativounidadeensinocurso.datafimperiodoletivosegundobimestre,  periodoletivoativounidadeensinocurso.datafimperiodoletivoprimeirobimestre limit 1;
END; $function$
;
