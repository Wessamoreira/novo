drop function if exists public.fn_alterarSituacaoMatriculaIntegralizada_finalizada(integer);

CREATE OR REPLACE FUNCTION public.fn_alterarSituacaoMatriculaIntegralizada_finalizada( usuarioalteracao integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
 DECLARE
  matriculas                  record; 
  query_update_matricula      text := '';
  query_update_matriculap     text := ''; 
begin	
   for matriculas in    select  m.matricula from  matricula m 
      /**
	 * inicio regra consulta
	 */      
      where m.situacao ='AT' 
      /**
	 * Valida se existe alguma disciplina obrigatório não cumprida --inicio---
	 */
      and not  exists  ( select gradedisciplina.codigo from gradedisciplina
				          inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo
				          where 	gradecurricular = m.gradecurricularatual and gradedisciplina.tipodisciplina in ('OB', 'LG')	 
				          and gradedisciplina.codigo not in (
					            select gradedisciplina from historico  
					            inner join gradedisciplina gd on gd.codigo = historico.gradedisciplina 
					            where matricula = m.matricula and matrizcurricular = m.gradecurricularatual	and gradedisciplina is not null
									  and gd.tipodisciplina in ('OB', 'LG') and situacao in ('AP', 'AA', 'AE', 'IS', 'CC', 'CH', 'AD', 'AB') 
						 ) 
	  )
	  /**
	 * Valida se existe alguma disciplina obrigatório não cumprida --- fim ----
	 */
						
	/**
	 * Valida se o aluno cumpriu a carga horária optativa minima obrigatória . ---inicio ---
	 */					
	and (
	select 
		case
			when resultado.quantidadeDisciplinasOptativasMatrizCurricular > 0 then ( resultado.quantidadeDisciplinaOptativaCumprida >= resultado.quantidadeDisciplinasOptativasMatrizCurricular)
			else
			case
				when resultado.quantidadeDisciplinasOptativasExpedicaoDiploma > 0 then ( resultado.quantidadeDisciplinaOptativaCumprida >= resultado.quantidadeDisciplinasOptativasExpedicaoDiploma)
				else coalesce(resultado.cargaHorariaOptativaCumprida, 0) >= ( coalesce( resultado.cargaHoraria, 0) - coalesce( resultado.totalcargahorariaestagio, 0) - coalesce( resultado.totalcargahorariaatividadecomplementar, 0) - coalesce( resultado.cargahorariaobrigatoria, 0) )
			end
		end as integralizado
	from
		(
		 select 	curso.quantidadeDisciplinasOptativasExpedicaoDiploma as quantidadeDisciplinasOptativasExpedicaoDiploma , gradecurricular.quantidadeDisciplinasOptativasMatrizCurricular as quantidadeDisciplinasOptativasMatrizCurricular, cargahoraria, totalcargahorariaestagio, totalcargahorariaatividadecomplementar, (
			select 	sum(cargahoraria) from gradedisciplina
			inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo
			where periodoletivo.gradecurricular = gradecurricular.codigo and gradedisciplina.tipodisciplina not in ('OP', 'LO') )
			as cargahorariaobrigatoria,
				(select	sum(cargahorariadisciplina) from ( 	select historico.cargahorariadisciplina from historico
				 inner join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina
				where 
					matricula = m.matricula
					and matrizcurricular = gradecurricular.codigo
					and gradedisciplina.tipodisciplina in ('OP', 'LO')
					and (historico.historicoequivalente = false
					or historico.historicoequivalente is null)
					and situacao in ('AA', 'IS', 'CC', 'CH', 'AP', 'AE', 'CO', 'AD', 'AB')
			  union all
				select historico.cargahorariadisciplina from historico
				inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina
				where
					matricula = m.matricula
					and historico.matrizcurricular = gradecurricular.codigo
					and (historico.historicoequivalente = false
					or historico.historicoequivalente is null)
					and situacao in ('AA', 'IS', 'CC', 'CH', 'AP', 'AE', 'CO', 'AD', 'AB')) as t) 
			as cargaHorariaOptativaCumprida,
			  (select count(disciplina) from 	(select	historico.disciplina from historico	
			  inner join gradedisciplina on	gradedisciplina.codigo = historico.gradedisciplina
				where
					matricula = m.matricula
					and matrizcurricular = gradecurricular.codigo
					and gradedisciplina.tipodisciplina in ('OP', 'LO')
					and (historico.historicoequivalente = false
					or historico.historicoequivalente is null)
					and situacao in ('AA', 'IS', 'CC', 'CH', 'AP', 'AE', 'CO', 'AD', 'AB')
			  union all
				select historico.disciplina	from historico 
				     inner join gradecurriculargrupooptativadisciplina on	gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina
				where
					matricula = m.matricula
					and historico.matrizcurricular = gradecurricular.codigo
					and (historico.historicoequivalente = false
					or historico.historicoequivalente is null)
					and situacao in ('AA', 'IS', 'CC', 'CH', 'AP', 'AE', 'CO', 'AD', 'AB')) as t)
			as quantidadeDisciplinaOptativaCumprida
		from gradecurricular
		inner join curso on curso.codigo = gradecurricular.curso 
		 where gradecurricular.codigo = m.gradecurricularatual 
	 ) as resultado 
	)
	/**
	 * Valida se o aluno cumpriu a carga horária optativa minima obrigatória ---fim ---
	 */
	
	
	/**
	 * Valida se na grade exige atividade complementar e se o aluno cumpriu a carga horária do mesmo. ---inicio ---
	 */
	and (case
		   when (select coalesce(totalCargaHorariaAtividadeComplementar, 0 ) from gradecurricular
		   where  codigo = m.gradecurricularatual ) > 0 
		   then ( select * from (select
						case
							when coalesce(sum(cargahorariapendente-cargahorariaexcedida), 0 ) < 0 then true
							else (sum(cargahorariapendente-cargahorariaexcedida) <= 0
							or sum(cargahorariapendente-cargahorariaexcedida) <= 0.0 )
						end as cargahorariaIntegralizada
					from
						(select 
							case
								when sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) < cargahorariaexigida then cargahorariaexigida - sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end )
								else 0.0
							end as cargahorariapendente,
							0 as cargahorariaexcedida
						from
							(
							select 	coalesce(sum(racm.cargahorariaconsiderada), 0) as cargaHorariaRealizadaAtividadeComplementar, gctac.tipoatividadecomplementar as tipoAtividadeComplementar, gctac.cargahoraria, gradecurricular.totalcargahorariaatividadecomplementar - coalesce ((
								select sum(horasminimasexigida) from gradecurriculartipoatividadecomplementar
								where
									gradecurriculartipoatividadecomplementar.gradecurricular = gradecurricular.codigo
									and gradecurriculartipoatividadecomplementar.horasminimasexigida > 0.0 ), 0) as cargahorariaexigida
							from gradecurriculartipoatividadecomplementar gctac
							inner join gradecurricular on gradecurricular.codigo = gctac.gradecurricular
							left join registroatividadecomplementarmatricula racm on gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar and matricula = m.matricula
							where
								gctac.gradecurricular = m.gradecurricularatual
								and gctac.horasminimasexigida = 0.0
								and racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO'
								and gctac.horasminimasexigida = 0.0
							group by gctac.tipoAtividadeComplementar, gctac.cargahoraria , gradecurricular.totalcargahorariaatividadecomplementar, gradecurricular.codigo ) as atividade
						group by
							cargahorariaexigida
					union all
						select
							case
								when coalesce(sum(racm.cargahorariaconsiderada), 0) < gctac.horasminimasexigida then gctac.horasminimasexigida - coalesce(sum(racm.cargahorariaconsiderada), 0)
								else 0
							end as cargahorariapendente,
							case
								when
								(case
									when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria
									else coalesce(sum(racm.cargahorariaconsiderada), 0)
								end) > gctac.horasminimasexigida then
								(case
									when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria
									else coalesce(sum(racm.cargahorariaconsiderada), 0)
								end) - gctac.horasminimasexigida
								else 0
							end as cargahorariaexcedida
						from gradecurriculartipoatividadecomplementar gctac
						inner join gradecurricular on gradecurricular.codigo = gctac.gradecurricular
						inner join tipoAtividadeComplementar on tipoAtividadeComplementar.codigo = gctac.tipoAtividadeComplementar
						left join registroatividadecomplementarmatricula racm on gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar and matricula = m.matricula
						where
							gctac.gradecurricular = m.gradecurricularatual
							and gctac.horasminimasexigida > 0.0
							and racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO'
						group by gctac.tipoAtividadeComplementar, gctac.cargahoraria , gradecurricular.totalcargahorariaatividadecomplementar, gradecurricular.codigo, gctac.horasminimasexigida, tipoAtividadeComplementar.nome ) as atividade ) as resultado
				where (resultado.cargahorariaIntegralizada is null ) = false 
		    )
		    else true end 
	)
	/**
	 * Valida se na grade exige atividade complementar e se o aluno cumpriu a carga horária do mesmo  --- fim --- 
	 */
	
	/**
	 * Valida se na grade exige estágio e se o aluno cumpriu a carga horária do estágio --- inicio ---
	 */
	and (
	select 	case when coalesce(sum(cargahorarioobrigatorio), 0) <= 0 then true 	else (
				case when ( ( select coalesce(sum(cargaHorariaDeferida)) as cargaHorariaDeferida from estagio
					where
						matricula = m.matricula and estagio.situacaoEstagioEnum = 'DEFERIDO' ) < coalesce(sum(cargahorarioobrigatorio), 0) ) 
						then false 	else true end ) end as total
		from gradecurricularestagio
		where 	gradecurricular = m.gradecurricularatual 
	)
	/**
	 * Valida se na grade exige estágio e se o aluno cumpriu a carga horária do estágio --- fim ---
	 */
	
	 /**
	 * fim regra consulta
	 */
          loop          
           raise notice 'matricula  : %',matriculas.matricula;
            begin
	           query_update_matricula := ' update matricula set situacao = ''FI''  where matricula = ''' || matriculas.matricula::text ||''' ;-- EXECUTADO PELA FUNCÃƒO fn_alterarSituacaoMatriculaIntegralizada_finalizada -- ul:' || usuarioAlteracao;
	           EXECUTE(query_update_matricula);
	         
	          query_update_matriculap := ' update matriculaperiodo  set situacaoMatriculaPeriodo = ''FI''  where matriculaperiodo.codigo  = 	(select mp.codigo  from matriculaperiodo mp
			               where mp.matricula = '''|| matriculas.matricula::text ||'''  and  mp.situacaoMatriculaPeriodo != ''PC'' and mp.situacaoMatriculaPeriodo  !=''FI''  order by (mp.ano || ''/'' || mp.semestre) desc,
	                      case when mp.situacaoMatriculaPeriodo in (''AT'', ''PR'', ''FI'', ''FO'') then 1 else 2 end, mp.codigo desc  limit 1 );-- EXECUTADO PELA FUNCÃƒO fn_alterarSituacaoMatriculaIntegralizada_finalizada -- ul:' || usuarioAlteracao;
	          EXECUTE(query_update_matriculap);
            exception when others then 
            raise notice 'matricula com erro ao atualizar situação . possui calendario de matricula para matricula periodo ';
           end ;
        END LOOP;
        
  RETURN true;
END;
$function$
;

