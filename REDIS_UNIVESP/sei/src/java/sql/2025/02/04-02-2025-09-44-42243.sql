DROP FUNCTION IF EXISTS public.consularsalageracaoblackboard(p_disciplina integer, p_ano character varying, p_semestre character varying, p_bimestre integer);

CREATE OR REPLACE FUNCTION public.consultarsalageracaoblackboard(p_disciplina integer, p_ano character varying, p_semestre character varying, p_bimestre integer, p_disciplina_niveleducacional character varying)
 RETURNS TABLE(disciplina_codigo integer, disciplina_abreviatura character varying, disciplina_nome character varying, disciplina_idconteudomasterblackboard text, classificacaodisciplina character varying, nrmaximoaulosporsala integer, nrminimoalunosporsala integer, niveleducacional character varying, ano character varying, semestre character varying, bimestre integer, programacaotutoriaonline integer, qtdealunos integer, qtdealunoensalado integer, qtdesalasexistentes integer, alunosensalados text, alunosnaoensalados text, salasexistentes text)
 LANGUAGE plpgsql
AS $function$
BEGIN
  RETURN QUERY 
SELECT 
  salas.p_disciplina_codigo as disciplina_codigo ,salas.p_disciplina_abreviatura as  disciplina_abreviatura , salas.p_disciplina_nome as disciplina_nome  , salas.p_disciplina_idConteudoMasterBlackboard as disciplina_idConteudoMasterBlackboard  , 
  salas.v_classificacaodisciplina as classificacaodisciplina , salas.p_nrmaximoaulosporsala as nrmaximoaulosporsala , salas.p_nrminimoalunosporsala as nrminimoalunosporsala , 
  salas.p_niveleducacional as niveleducacional, salas.p_ano as ano , salas.p_semestre as semestre ,salas.v_bimestre as  bimestre , salas.v_programacaotutoriaonline as programacaotutoriaonline , salas.v_qtdeAlunos as qtdeAlunos  , 
  salas.v_qtdeAlunoEnsalado as qtdeAlunoEnsalado , salas.v_qtdeSalasExistentes as qtdeSalasExistentes,
  salas.v_alunosEnsalados as alunosEnsalados, salas.v_alunosNaoEnsalados as alunosNaoEnsalados, salas.v_salasExistentes as salasExistentes  
  
FROM (
select salas.p_disciplina_codigo ,salas.p_disciplina_abreviatura  , salas.p_disciplina_nome    , salas.p_disciplina_idConteudoMasterBlackboard    , 
  salas.v_classificacaodisciplina , salas.p_nrmaximoaulosporsala , salas.p_nrminimoalunosporsala  ,  salas.p_niveleducacional,
  salas.p_ano , salas.p_semestre  ,salas.v_bimestre , programacaotutoriaonline.codigo as v_programacaotutoriaonline , salas.v_qtdeAlunos   , 
  salas.v_qtdeAlunoEnsalado , count(distinct case when salaaulablackboard.codigo is not null then salaaulablackboard.codigo  end )::INT as v_qtdeSalasExistentes, 
  salas.v_alunosEnsalados, salas.v_alunosNaoEnsalados, array_to_string(array_agg(distinct case when salaaulablackboard.codigo is not null then salaaulablackboard.codigo  end), ',')  as v_salasExistentes   from (
  
select  distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome, disciplina.idConteudoMasterBlackboard as p_disciplina_idConteudoMasterBlackboard,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, case when matriculaperiodoturmadisciplina.bimestre is not null and configuracaoacademico.matricularapenasdisciplinaaulaprogramada then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end  as v_bimestre, 
count(distinct matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end)::INT as v_qtdeAlunoEnsalado,
array_to_string( array_agg(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string( array_agg(distinct case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados,
curso.niveleducacional as p_niveleducacional
from matriculaperiodoturmadisciplina 
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
inner join curso on curso.codigo = matricula.curso
inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico  
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
left join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
left join gradecurriculargrupooptativadisciplina  on gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina
--inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
--and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and turmadisciplina.modalidadedisciplina = 'ON_LINE' 
left join lateral (
select salaaulablackboardpessoa.codigo, salaaulablackboardpessoa.matriculaperiodoturmadisciplina from salaaulablackboardpessoa  
inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard
inner join disciplina on salaaulablackboard.disciplina = disciplina.codigo 
and salaaulablackboard.tiposalaaulablackboardenum = 'DISCIPLINA'
and salaaulablackboard.ano = p_ano 
and salaaulablackboard.semestre = p_semestre
and (coalesce(p_disciplina,0) = 0 or salaaulablackboard.disciplina = p_disciplina)
and (coalesce(p_disciplina_niveleducacional,' ') = ' ' or disciplina.niveleducacional = p_disciplina_niveleducacional)

) as salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI')
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (coalesce(p_disciplina_niveleducacional,' ') = ' ' or curso.niveleducacional = p_disciplina_niveleducacional)
and (p_bimestre is null or case when matriculaperiodoturmadisciplina.bimestre is not null then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end = p_bimestre)
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS') 
and disciplina.classificacaodisciplina = 'NENHUMA'
group by disciplina.codigo , disciplina.nome,  disciplina.idconteudomasterblackboard,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala ,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, curso.niveleducacional,
case when matriculaperiodoturmadisciplina.bimestre is not null and configuracaoacademico.matricularapenasdisciplinaaulaprogramada then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end

union all 
select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome, disciplina.idConteudoMasterBlackboard as p_disciplina_idConteudoMasterBlackboard,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoalunosporambientacao  as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, case when matriculaperiodoturmadisciplina.bimestre is not null  and configuracaoacademico.matricularapenasdisciplinaaulaprogramada then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end as v_bimestre, 
count(distinct matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end)::INT as v_qtdeAlunoEnsalado,
array_to_string(array_agg(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(distinct case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados,
curso.niveleducacional as p_niveleducacional
from matriculaperiodoturmadisciplina 
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
inner join curso on curso.codigo = matricula.curso
inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
left join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
left join gradecurriculargrupooptativadisciplina  on gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina
inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE'
left join lateral (
select salaaulablackboardpessoa.codigo, salaaulablackboardpessoa.matriculaperiodoturmadisciplina from salaaulablackboardpessoa  
inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard
and salaaulablackboard.tiposalaaulablackboardenum = 'PROJETO_INTEGRADOR_AMBIENTACAO'
and salaaulablackboard.ano = p_ano 
and salaaulablackboard.semestre = p_semestre
and (coalesce(p_disciplina,0) = 0 or salaaulablackboard.disciplina = p_disciplina)
and (p_bimestre is null or salaaulablackboard.bimestre = p_bimestre)
and (coalesce(p_disciplina_niveleducacional,' ') = ' ' or disciplina.niveleducacional = p_disciplina_niveleducacional)

) as salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI')
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (coalesce(p_disciplina_niveleducacional,' ') = ' ' or curso.niveleducacional = p_disciplina_niveleducacional)
and (p_bimestre is null or case when matriculaperiodoturmadisciplina.bimestre is not null then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end = p_bimestre)
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
and disciplina.classificacaodisciplina = 'PROJETO_INTEGRADOR'
group by disciplina.codigo , disciplina.nome,  disciplina.idconteudomasterblackboard,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, curso.niveleducacional, 
case when matriculaperiodoturmadisciplina.bimestre is not null and configuracaoacademico.matricularapenasdisciplinaaulaprogramada then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end

) as salas
left join salaaulablackboard on 
salaaulablackboard.disciplina = salas.p_disciplina_codigo
and salaaulablackboard.ano = salas.p_ano
and salaaulablackboard.semestre =  salas.p_semestre
and salaaulablackboard.bimestre =  salas.v_bimestre
and salaaulablackboard.tiposalaaulablackboardenum in ('DISCIPLINA','PROJETO_INTEGRADOR_AMBIENTACAO')
left join programacaotutoriaonline on programacaotutoriaonline.disciplina = salas.p_disciplina_codigo
and programacaotutoriaonline.situacao = 'ATIVO' and programacaotutoriaonline.tiponivelprogramacaotutoria = 'DISCIPLINA'

group by salas.p_disciplina_codigo  ,salas.p_disciplina_abreviatura , salas.p_disciplina_nome   , salas.p_disciplina_idConteudoMasterBlackboard   , 
  salas.v_classificacaodisciplina  , salas.p_nrmaximoaulosporsala  , salas.p_nrminimoalunosporsala , 
  salas.p_ano  , salas.p_semestre  ,salas.v_bimestre  ,  salas.p_niveleducacional,  programacaotutoriaonline.codigo , salas.v_qtdeAlunos  , 
  salas.v_qtdeAlunoEnsalado , 
  salas.v_alunosEnsalados , salas.v_alunosNaoEnsalados 
  order by salas.p_disciplina_abreviatura
) as salas;
END; $function$
;



