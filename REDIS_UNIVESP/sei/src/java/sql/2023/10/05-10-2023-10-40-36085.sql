ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS codigo serial;
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS disciplina int;
alter table ofertadisciplina add constraint fk_ofertadisciplina_disciplina foreign key (disciplina) references disciplina(codigo);
update ofertadisciplina set disciplina = codigodisciplina where codigodisciplina is not null and disciplina is null;
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS created timestamp default now();
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS codigocreated int;
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS nomecreated varchar(255);
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS updated timestamp;
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS codigoupdated int;
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS nomeupdated varchar(255);
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS configuracaoacademico int;
alter table ofertadisciplina add constraint fk_ofertadisciplina_configuracaoacademico foreign key (configuracaoacademico) references configuracaoacademico(codigo);
ALTER TABLE IF EXISTS ofertadisciplina ADD COLUMN IF NOT EXISTS periodo varchar(30);
update ofertadisciplina set configuracaoacademico = 3 from disciplina where disciplina.codigo = ofertadisciplina.disciplina and disciplina.classificacaodisciplina = 'TCC' and ofertadisciplina.configuracaoacademico is null;
update ofertadisciplina set configuracaoacademico = 5 from disciplina where disciplina.codigo = ofertadisciplina.disciplina and disciplina.classificacaodisciplina = 'ESTAGIO' and ofertadisciplina.configuracaoacademico is null;
update ofertadisciplina set configuracaoacademico = 4 from disciplina where disciplina.codigo = ofertadisciplina.disciplina and disciplina.classificacaodisciplina = 'PROJETO_INTEGRADOR' and ofertadisciplina.configuracaoacademico is null;
update ofertadisciplina set configuracaoacademico = 2 where tipo =  'Regular' and configuracaoacademico is null;
update ofertadisciplina set configuracaoacademico = 2 where tipo =  'Regular DP' and configuracaoacademico is null;
update ofertadisciplina set configuracaoacademico = 2 where tipo is null and configuracaoacademico is null;
update ofertadisciplina set configuracaoacademico = 7 where tipo =  'Autoinstrucional' and configuracaoacademico is null;

update ofertadisciplina set periodo = 'SEMESTRE_1' where tipo =  'Autoinstrucional' and periodo is null;
update ofertadisciplina set periodo = 'BIMESTRE_03' where oferta ilike  '%b3' and periodo is null;
update ofertadisciplina set periodo = 'BIMESTRE_01' where oferta ilike  '%b1' and periodo is null;
update ofertadisciplina set periodo = 'BIMESTRE_04' where oferta ilike  '%b4' and periodo is null;
update ofertadisciplina set periodo = 'BIMESTRE_02' where oferta ilike  '%b2' and periodo is null;
update ofertadisciplina set periodo = 'SEMESTRE_1' where oferta ilike  '%s1' and periodo is null;
update ofertadisciplina set periodo = 'SEMESTRE_2' where oferta ilike  '%s2' and periodo is null;
update ofertadisciplina set periodo = 'SEMESTRE_2' where oferta ilike  'Inverno' and periodo is null;

alter table matriculaperiodoturmadisciplina add column if not exists bimestre  int;
alter table GradeCurricularGrupoOptativaDisciplina add column if not exists bimestre  int;

CREATE OR REPLACE FUNCTION public.consularsalageracaoblackboard(p_disciplina integer, p_ano character varying, p_semestre character varying, p_bimestre integer)
 RETURNS TABLE(disciplina_codigo integer, disciplina_abreviatura character varying, disciplina_nome character varying, disciplina_idconteudomasterblackboard text, classificacaodisciplina character varying, nrmaximoaulosporsala integer, nrminimoalunosporsala integer, ano character varying, semestre character varying, bimestre integer, programacaotutoriaonline integer, qtdealunos integer, qtdealunoensalado integer, qtdesalasexistentes integer, alunosensalados text, alunosnaoensalados text, salasexistentes text)
 LANGUAGE plpgsql
AS $function$
BEGIN
  RETURN QUERY 
SELECT 
  salas.p_disciplina_codigo as disciplina_codigo ,salas.p_disciplina_abreviatura as  disciplina_abreviatura , salas.p_disciplina_nome as disciplina_nome  , salas.p_disciplina_idConteudoMasterBlackboard as disciplina_idConteudoMasterBlackboard  , 
  salas.v_classificacaodisciplina as classificacaodisciplina , salas.p_nrmaximoaulosporsala as nrmaximoaulosporsala , salas.p_nrminimoalunosporsala as nrminimoalunosporsala , 
  salas.p_ano as ano , salas.p_semestre as semestre ,salas.v_bimestre as  bimestre , salas.v_programacaotutoriaonline as programacaotutoriaonline , salas.v_qtdeAlunos as qtdeAlunos  , 
  salas.v_qtdeAlunoEnsalado as qtdeAlunoEnsalado , salas.v_qtdeSalasExistentes as qtdeSalasExistentes, 
  salas.v_alunosEnsalados as alunosEnsalados, salas.v_alunosNaoEnsalados as alunosNaoEnsalados, salas.v_salasExistentes as salasExistentes  
  
FROM (
select salas.p_disciplina_codigo ,salas.p_disciplina_abreviatura  , salas.p_disciplina_nome    , salas.p_disciplina_idConteudoMasterBlackboard    , 
  salas.v_classificacaodisciplina , salas.p_nrmaximoaulosporsala , salas.p_nrminimoalunosporsala  , 
  salas.p_ano , salas.p_semestre  ,salas.v_bimestre , programacaotutoriaonline.codigo as v_programacaotutoriaonline , salas.v_qtdeAlunos   , 
  salas.v_qtdeAlunoEnsalado , count(distinct case when salaaulablackboard.codigo is not null then salaaulablackboard.codigo  end )::INT as v_qtdeSalasExistentes, 
  salas.v_alunosEnsalados, salas.v_alunosNaoEnsalados, array_to_string(array_agg(distinct case when salaaulablackboard.codigo is not null then salaaulablackboard.codigo  end), ',')  as v_salasExistentes   from (
  
select  distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome, disciplina.idConteudoMasterBlackboard as p_disciplina_idConteudoMasterBlackboard,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoaulosporsala as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, case when matriculaperiodoturmadisciplina.bimestre is not null then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end  as v_bimestre, 
count(distinct matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end)::INT as v_qtdeAlunoEnsalado,
array_to_string( array_agg(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string( array_agg(distinct case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados
from matriculaperiodoturmadisciplina 
inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
inner join historico  on matriculaperiodo.codigo = historico.matriculaperiodo and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
left join gradedisciplina  on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina
left join gradecurriculargrupooptativadisciplina  on gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina
--inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina 
--and turmadisciplina.definicoestutoriaonline = 'DINAMICA' and turmadisciplina.modalidadedisciplina = 'ON_LINE' 
left join lateral (
select salaaulablackboardpessoa.codigo, salaaulablackboardpessoa.matriculaperiodoturmadisciplina from salaaulablackboardpessoa  
inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard
and salaaulablackboard.tiposalaaulablackboardenum = 'DISCIPLINA'
and salaaulablackboard.ano = p_ano 
and salaaulablackboard.semestre = p_semestre
and (coalesce(p_disciplina,0) = 0 or salaaulablackboard.disciplina = p_disciplina)

) as salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI')
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (p_bimestre is null or case when matriculaperiodoturmadisciplina.bimestre is not null then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end = p_bimestre)
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS') 
and disciplina.classificacaodisciplina = 'NENHUMA'
group by disciplina.codigo , disciplina.nome,  disciplina.idconteudomasterblackboard,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala ,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre, gradecurriculargrupooptativadisciplina.bimestre, matriculaperiodoturmadisciplina.bimestre, gradedisciplina.codigo

union all 
select distinct disciplina.codigo  as p_disciplina_codigo, disciplina.abreviatura as p_disciplina_abreviatura, disciplina.nome as p_disciplina_nome, disciplina.idConteudoMasterBlackboard as p_disciplina_idConteudoMasterBlackboard,  disciplina.classificacaodisciplina as v_classificacaodisciplina, 
disciplina.nrmaximoalunosporambientacao  as p_nrmaximoaulosporsala, disciplina.nrminimoalunosporsala as p_nrminimoalunosporsala, 
matriculaperiodoturmadisciplina.ano as p_ano, matriculaperiodoturmadisciplina.semestre as p_semestre, case when matriculaperiodoturmadisciplina.bimestre is not null then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end as v_bimestre, 
count(distinct matriculaperiodoturmadisciplina.codigo)::INT  as v_qtdeAlunos,
count(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end)::INT as v_qtdeAlunoEnsalado,
array_to_string(array_agg(distinct case when salaaulablackboardpessoa.codigo is not null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosEnsalados,
array_to_string(array_agg(distinct case when salaaulablackboardpessoa.codigo is null then matriculaperiodoturmadisciplina.codigo end), ',')  as v_alunosNaoEnsalados
from matriculaperiodoturmadisciplina 
inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo
inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula
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

) as salaaulablackboardpessoa on salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo 
where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI')
and (coalesce(p_disciplina,0) = 0 or disciplina.codigo = p_disciplina)
and (p_bimestre is null or case when matriculaperiodoturmadisciplina.bimestre is not null then matriculaperiodoturmadisciplina.bimestre::INT else case when gradedisciplina.codigo is not null then coalesce(gradedisciplina.bimestre, 0)::INT else coalesce(gradecurriculargrupooptativadisciplina.bimestre, 0)::INT end end = p_bimestre)
and matriculaperiodoturmadisciplina.ano = p_ano 
and matriculaperiodoturmadisciplina.semestre = p_semestre
and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS')
and disciplina.classificacaodisciplina = 'PROJETO_INTEGRADOR'
group by disciplina.codigo , disciplina.nome,  disciplina.idconteudomasterblackboard,  disciplina.classificacaodisciplina, disciplina.nrmaximoaulosporsala, disciplina.nrminimoalunosporsala,
matriculaperiodoturmadisciplina.ano, disciplina.abreviatura, matriculaperiodoturmadisciplina.semestre, gradedisciplina.bimestre, gradecurriculargrupooptativadisciplina.bimestre, matriculaperiodoturmadisciplina.bimestre, gradedisciplina.codigo

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
  salas.p_ano  , salas.p_semestre  ,salas.v_bimestre  ,  programacaotutoriaonline.codigo , salas.v_qtdeAlunos  , 
  salas.v_qtdeAlunoEnsalado , 
  salas.v_alunosEnsalados , salas.v_alunosNaoEnsalados 
  order by salas.p_disciplina_abreviatura
) as salas;
END; $function$
;
