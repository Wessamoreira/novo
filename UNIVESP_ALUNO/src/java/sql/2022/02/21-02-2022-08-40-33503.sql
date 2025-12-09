CREATE OR REPLACE FUNCTION public.consularsalageracaoblackboard(p_disciplina integer, p_ano character varying, p_semestre character varying, p_bimestre integer)
 RETURNS TABLE(disciplina_codigo integer, disciplina_abreviatura character varying, disciplina_nome character varying, classificacaodisciplina character varying, nrmaximoaulosporsala integer, nrminimoalunosporsala integer, ano character varying, semestre character varying, bimestre integer, programacaotutoriaonline integer, qtdealunos integer, qtdealunoensalado integer, qtdesalasexistentes integer, alunosensalados text, alunosnaoensalados text, salasexistentes text)
 LANGUAGE plpgsql
AS $function$
BEGIN
  RETURN QUERY 
SELECT 
  salas.p_disciplina_codigo as disciplina_codigo ,salas.p_disciplina_abreviatura as  disciplina_abreviatura , salas.p_disciplina_nome as disciplina_nome  , 
  salas.v_classificacaodisciplina as classificacaodisciplina , salas.p_nrmaximoaulosporsala as nrmaximoaulosporsala , salas.p_nrminimoalunosporsala as nrminimoalunosporsala , 
  salas.p_ano as ano , salas.p_semestre as semestre ,salas.v_bimestre as  bimestre , salas.v_programacaotutoriaonline as programacaotutoriaonline , salas.v_qtdeAlunos as qtdeAlunos  , 
  salas.v_qtdeAlunoEnsalado as qtdeAlunoEnsalado , salas.v_qtdeSalasExistentes as qtdeSalasExistentes, 
  salas.v_alunosEnsalados as alunosEnsalados, salas.v_alunosNaoEnsalados as alunosNaoEnsalados, salas.v_salasExistentes as salasExistentes  
  
FROM (
select salas.p_disciplina_codigo ,salas.p_disciplina_abreviatura  , salas.p_disciplina_nome    , 
  salas.v_classificacaodisciplina , salas.p_nrmaximoaulosporsala , salas.p_nrminimoalunosporsala  , 
  salas.p_ano , salas.p_semestre  ,salas.v_bimestre , programacaotutoriaonline.codigo as v_programacaotutoriaonline , salas.v_qtdeAlunos   , 
  salas.v_qtdeAlunoEnsalado , count(distinct case when salaaulablackboard.codigo is not null then salaaulablackboard.codigo  end )::INT as v_qtdeSalasExistentes, 
  salas.v_alunosEnsalados, salas.v_alunosNaoEnsalados, array_to_string(array_agg(distinct case when salaaulablackboard.codigo is not null then salaaulablackboard.codigo  end), ',')  as v_salasExistentes   from (
  
select  distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
count(matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(salaaulablackboardpessoa.codigo)::INT as v_qtdeAlunoEnsalado,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados
from matriculaperiodoturmadisciplina 
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
--inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
--and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and turmadisciplina.modalidadedisciplina = 'ON_LINE'
left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo = 'AT' 
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS') 
and disciplina.classificacaodisciplina = 'NENHUMA'
group by disciplina.codigo , disciplina.nome,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala ,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre 

union all 
select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoalunosporambientacao  as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
count(matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(salaaulablackboardpessoa.codigo)::INT as v_qtdeAlunoEnsalado,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados
from matriculaperiodoturmadisciplina 
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo = 'AT' 
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
and disciplina.classificacaodisciplina = 'PROJETO_INTEGRADOR'
group by disciplina.codigo , disciplina.nome,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre

--union all 
--select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
--disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
--matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
--count(matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
--count(salaaulablackboardpessoa.codigo)::INT as v_qtdeAlunoEnsalado,
--array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
--array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados
--from matriculaperiodoturmadisciplina 
--inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
--inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
--inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
--inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
--inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
--inner join periodoletivo  on periodoletivo.codigo = gradedisciplina.periodoletivo
--inner join gradecurricular  on periodoletivo.gradecurricular = gradecurricular.codigo
--inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
--and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
--left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
--where matriculaperiodo.situacaomatriculaperiodo = 'AT' 
--and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
--and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
--and matriculaperiodoturmadisciplina.ano = p_ano 
--and matriculaperiodoturmadisciplina.semestre = p_semestre
--and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
--and (disciplina.classificacaodisciplina = 'TCC' and gradecurricular.disciplinapadraotcc is null)
--group by disciplina.codigo , disciplina.nome,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
--matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre

--union all
--select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura,  disciplina.nome as p_disciplina_nome,  'TCC'::varchar as v_classificacaodisciplina, 
--disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
--matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
--count(matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
--count(salaaulablackboardpessoa.codigo)::INT as v_qtdeAlunoEnsalado,
--array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
--array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados
--from matriculaperiodoturmadisciplina 
--inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
--inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
--inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
--inner join disciplina as d  on d.codigo = matriculaperiodoturmadisciplina.disciplina
--inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
--inner join periodoletivo  on periodoletivo.codigo = gradedisciplina.periodoletivo
--inner join gradecurricular  on periodoletivo.gradecurricular = gradecurricular.codigo
--inner join disciplina  on disciplina.codigo = gradecurricular.disciplinapadraotcc
--inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
--and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
--left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo
--where matriculaperiodo.situacaomatriculaperiodo = 'AT'
--and (coalesce(p_disciplina,0) = 0 or  disciplina.codigo = p_disciplina)
--and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
--and matriculaperiodoturmadisciplina.ano = p_ano 
--and matriculaperiodoturmadisciplina.semestre = p_semestre
--and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
--and d.classificacaodisciplina = 'TCC'
--group by disciplina.codigo , disciplina.nome, disciplina.abreviatura, disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
--matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre

) as salas
left join salaaulablackboard on 
salaaulablackboard.disciplina = salas.p_disciplina_codigo
and salaaulablackboard.ano = salas.p_ano
and salaaulablackboard.semestre =  salas.p_semestre
and salaaulablackboard.bimestre =  salas.v_bimestre
and salaaulablackboard.tiposalaaulablackboardenum in ('DISCIPLINA','PROJETO_INTEGRADOR_AMBIENTACAO')
left join programacaotutoriaonline on programacaotutoriaonline.disciplina = salas.p_disciplina_codigo
and programacaotutoriaonline.situacao = 'ATIVO' and programacaotutoriaonline.tiponivelprogramacaotutoria = 'DISCIPLINA'

group by salas.p_disciplina_codigo  ,salas.p_disciplina_abreviatura , salas.p_disciplina_nome   , 
  salas.v_classificacaodisciplina  , salas.p_nrmaximoaulosporsala  , salas.p_nrminimoalunosporsala , 
  salas.p_ano  , salas.p_semestre  ,salas.v_bimestre  ,  programacaotutoriaonline.codigo , salas.v_qtdeAlunos  , 
  salas.v_qtdeAlunoEnsalado , 
  salas.v_alunosEnsalados , salas.v_alunosNaoEnsalados 
  
) as salas;
END; $function$
;
