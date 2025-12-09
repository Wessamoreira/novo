alter table gradedisciplina add column if not exists bimestre int default  0;

drop function if exists consularSalaGeracaoBlackboard(p_disciplina integer, p_ano varchar, p_semestre varchar, p_bimestre integer);
create or replace function consularSalaGeracaoBlackboard(p_disciplina integer, p_ano varchar, p_semestre varchar, p_bimestre integer)
RETURNS table (disciplina_codigo int, disciplina_abreviatura varchar, disciplina_nome  varchar, classificacaodisciplina varchar, 
nrmaximoaulosporsala int, nrminimoalunosporsala int, ano varchar, semestre varchar, bimestre int, programacaotutoriaonline int, qtdeAlunos int , qtdeAlunoEnsalado int, qtdeSalasExistentes int, alunosEnsalados text, alunosNaoEnsalados text, salasExistentes text)
LANGUAGE plpgsql AS $function$
BEGIN
  RETURN QUERY 
SELECT 
  salas.p_disciplina_codigo as disciplina_codigo ,salas.p_disciplina_abreviatura as  disciplina_abreviatura , salas.p_disciplina_nome as disciplina_nome  , 
  salas.v_classificacaodisciplina as classificacaodisciplina , salas.p_nrmaximoaulosporsala as nrmaximoaulosporsala , salas.p_nrminimoalunosporsala as nrminimoalunosporsala , 
  salas.p_ano as ano , salas.p_semestre as semestre ,salas.v_bimestre as  bimestre , salas.v_programacaotutoriaonline as programacaotutoriaonline , salas.v_qtdeAlunos as qtdeAlunos  , 
  salas.v_qtdeAlunoEnsalado as qtdeAlunoEnsalado , salas.v_qtdeSalasExistentes as qtdeSalasExistentes, 
  salas.v_alunosEnsalados as alunosEnsalados, salas.v_alunosNaoEnsalados as alunosNaoEnsalados, salas.v_salasExistentes as salasExistentes  
  
FROM (
select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
programacaotutoriaonline.codigo as v_programacaotutoriaonline, 
count(matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(salaaulablackboardpessoa.codigo)::INT as v_qtdeAlunoEnsalado,
count(distinct salaaulablackboard.id)::INT as v_qtdeSalasExistentes,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados,
array_to_string(array_agg(distinct case when salaaulablackboard.codigo is null then salaaulablackboard.codigo  end), ',')  as v_salasExistentes
from matriculaperiodoturmadisciplina 
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
and historico.anohistorico = p_ano and historico.semestrehistorico = p_semestre 
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
left join programacaotutoriaonline on programacaotutoriaonline.disciplina = disciplina.codigo 
and programacaotutoriaonline.situacao = 'ATIVO'
left join salaaulablackboard on salaaulablackboard.disciplina =  disciplina.codigo
and salaaulablackboard.ano =  matriculaperiodoturmadisciplina.ano
and salaaulablackboard.semestre =  matriculaperiodoturmadisciplina.semestre
and salaaulablackboard.bimestre =  coalesce(gradedisciplina.bimestre, 0)
left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo = 'AT' 
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
and matriculaperiodo.ano = p_ano 
and matriculaperiodo.semestre = p_semestre
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and disciplina.classificacaodisciplina = 'NENHUMA'
group by disciplina.codigo , disciplina.nome,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala ,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre,
programacaotutoriaonline.codigo
union all 
select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
programacaotutoriaonline.codigo as v_programacaotutoriaonline, 
count(matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(salaaulablackboardpessoa.codigo)::INT as v_qtdeAlunoEnsalado,
count(distinct salaaulablackboard.id)::INT as v_qtdeSalasExistentes,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados,
array_to_string(array_agg(distinct case when salaaulablackboard.codigo is null then salaaulablackboard.codigo  end), ',')  as v_salasExistentes
from matriculaperiodoturmadisciplina 
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
and historico.anohistorico = p_ano and historico.semestrehistorico = p_semestre
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
left join programacaotutoriaonline on programacaotutoriaonline.disciplina = disciplina.codigo 
and programacaotutoriaonline.situacao = 'ATIVO'
left join salaaulablackboard on salaaulablackboard.disciplina =  disciplina.codigo
and salaaulablackboard.ano =  matriculaperiodoturmadisciplina.ano
and salaaulablackboard.semestre =  matriculaperiodoturmadisciplina.semestre
and salaaulablackboard.bimestre =  coalesce(gradedisciplina.bimestre, 0)
left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo = 'AT' 
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
and matriculaperiodo.ano = p_ano 
and matriculaperiodo.semestre = p_semestre
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and disciplina.classificacaodisciplina = 'PROJETO_INTEGRADOR'
group by disciplina.codigo , disciplina.nome,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre,
programacaotutoriaonline.codigo
union all 
select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
programacaotutoriaonline.codigo as v_programacaotutoriaonline, 
count(matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(salaaulablackboardpessoa.codigo)::INT as qtdeAlunoEnsalado,
count(distinct salaaulablackboard.id)::INT as v_qtdeSalasExistentes,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados,
array_to_string(array_agg(distinct case when salaaulablackboard.codigo is null then salaaulablackboard.codigo  end), ',')  as v_salasExistentes
from matriculaperiodoturmadisciplina 
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
and historico.anohistorico = p_ano and historico.semestrehistorico = p_semestre
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
inner join periodoletivo  on periodoletivo.codigo = gradedisciplina.periodoletivo
inner join gradecurricular  on periodoletivo.gradecurricular = gradecurricular.codigo
inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
left join programacaotutoriaonline on programacaotutoriaonline.disciplina = disciplina.codigo 
and programacaotutoriaonline.situacao = 'ATIVO'
left join salaaulablackboard on salaaulablackboard.disciplina =  disciplina.codigo
and salaaulablackboard.ano =  matriculaperiodoturmadisciplina.ano
and salaaulablackboard.semestre =  matriculaperiodoturmadisciplina.semestre
and salaaulablackboard.bimestre =  coalesce(gradedisciplina.bimestre, 0)
left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo = 'AT' 
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
and matriculaperiodo.ano = p_ano 
and matriculaperiodo.semestre = p_semestre
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and (disciplina.classificacaodisciplina = 'TCC' and gradecurricular.disciplinapadraotcc is null)
group by disciplina.codigo , disciplina.nome,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre,
programacaotutoriaonline.codigo
union all
select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura,  disciplina.nome as p_disciplina_nome,  'TCC'::varchar as v_classificacaodisciplina, 
disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala,
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, coalesce(gradedisciplina.bimestre, 0)::INT as v_bimestre, 
programacaotutoriaonline.codigo as v_programacaotutoriaonline, 
count(matriculaperiodoturmadisciplina.disciplina)::INT  as v_qtdeAlunos,
count(salaaulablackboardpessoa.codigo)::INT as qtdeAlunoEnsalado,
count(distinct salaaulablackboard.id)::INT as v_qtdeSalasExistentes,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados,
array_to_string(array_agg(distinct case when salaaulablackboard.codigo is null then salaaulablackboard.codigo  end), ',')  as v_salasExistentes
from matriculaperiodoturmadisciplina 
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
and historico.anohistorico = p_ano and historico.semestrehistorico = p_semestre
inner join disciplina as d  on d.codigo = matriculaperiodoturmadisciplina.disciplina
inner join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
inner join periodoletivo  on periodoletivo.codigo = gradedisciplina.periodoletivo
inner join gradecurricular  on periodoletivo.gradecurricular = gradecurricular.codigo
inner join disciplina  on disciplina.codigo = gradecurricular.disciplinapadraotcc
inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
left join programacaotutoriaonline on programacaotutoriaonline.disciplina = disciplina.codigo 
and programacaotutoriaonline.situacao = 'ATIVO'
left join salaaulablackboard on salaaulablackboard.disciplina =  disciplina.codigo
and salaaulablackboard.ano =  matriculaperiodoturmadisciplina.ano
and salaaulablackboard.semestre =  matriculaperiodoturmadisciplina.semestre
and salaaulablackboard.bimestre =  coalesce(gradedisciplina.bimestre, 0)
left join salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo
where matriculaperiodo.situacaomatriculaperiodo = 'AT'
and (coalesce(p_disciplina,0) = 0 or  disciplina.codigo = p_disciplina)
and (p_bimestre is null or gradedisciplina.bimestre = p_bimestre)
and matriculaperiodo.ano = p_ano 
and matriculaperiodo.semestre = p_semestre
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and d.classificacaodisciplina = 'TCC'
group by disciplina.codigo , disciplina.nome, disciplina.abreviatura, disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre,
programacaotutoriaonline.codigo
) as salas;
END; $function$;