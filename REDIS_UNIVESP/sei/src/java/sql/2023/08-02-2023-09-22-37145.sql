drop function if exists  public.matriculaIntegralizacaoCurricular(matriculaAluno varchar);

create or replace
function public.matriculaIntegralizacaoCurricular(matriculaAluno varchar) 
returns table(matricula varchar,
cargaHorariaTotal integer,
cargaHorariaDisciplinaObrigatorioExigida integer,
cargaHorariaDisciplinaObrigatorioCumprida integer, 
cargaHorariaDisciplinaOptativaExigida integer,
quantidadeMinimaDisciplinaOptativaExigida integer,
cargaHorariaDisciplinaOptativaCumprida integer,
quantidadeDisciplinaOptativaCumprida integer,
cargaHorariaEstagioExigido integer,
cargaHorariaEstagioCumprido integer,
cargaHorariaAtividadeComplementarExigido integer,
cargaHorariaAtividadeComplementarCumprido integer,
cargaHorariaCumprido integer,
cargaHorariaPendente integer,
percentualIntegralizado numeric(20,
2),
percentualNaoIntegralizado numeric(20,
2), 
percentualPermitirIniciarEstagio numeric(20,
2),
cargaHorariaExigidaLiberarEstagio integer,
cargaHorariaCumpridaLiberarEstagio integer,
percentualCumpridoLiberarEstagio numeric(20,
2),
percentualPendenteLiberarEstagio numeric(20,
2),
percentualPermitirIniciarTcc numeric(20,
2),
cargaHorariaExigidaLiberarTcc integer,
cargaHorariaCumpridaLiberarTcc integer,
percentualCumpridoLiberarTcc numeric(20,
2),
percentualPendenteLiberarTcc numeric(20,
2))
 language plpgsql
as $function$
begin
  return QUERY
select
	t.matricula,
	t.cargaHorariaTotal::int,
	t.cargaHorariaDisciplinaObrigatorioExigida::int,
	t.cargaHorariaDisciplinaObrigatorioCumprida::int,
	t.cargaHorariaDisciplinaOptativaExigida::int,
	t.quantidadeMinimaDisciplinaOptativaExigida::int,
	t.cargaHorariaDisciplinaOptativaCumprida::int,
	case
		when t.cargaHorariaDisciplinaOptativaExigida > 0 then t.quantidadeDisciplinaOptativaCumprida::int
		else 0::int
	end quantidadeDisciplinaOptativaCumprida,
	t.cargaHorariaEstagioExigido::int,
	t.cargaHorariaEstagioCumprido::int,
	t.cargaHorariaAtividadeComplementarExigido::int,
	t.cargaHorariaAtividadeComplementarCumprido::int,
	(
	(case when t.cargaHorariaDisciplinaObrigatorioCumprida > t.cargaHorariaDisciplinaObrigatorioExigida then t.cargaHorariaDisciplinaObrigatorioExigida
	else t.cargaHorariaDisciplinaObrigatorioCumprida
end ) 
+
	(case
		when t.cargaHorariaDisciplinaOptativaExigida > 0 then
		case
			when t.quantidadeMinimaDisciplinaOptativaExigida > 0
			and t.quantidadeDisciplinaOptativaCumprida = t.quantidadeMinimaDisciplinaOptativaExigida then t.cargaHorariaDisciplinaOptativaExigida
			else t.cargaHorariaDisciplinaOptativaCumprida
		end
		else 0
	end)
+
	(case
		when t.cargaHorariaEstagioCumprido > t.cargaHorariaEstagioExigido then t.cargaHorariaEstagioExigido
		else t.cargaHorariaEstagioCumprido
	end )
+
	(case
		when t.cargaHorariaAtividadeComplementarCumprido > t.cargaHorariaAtividadeComplementarExigido then t.cargaHorariaAtividadeComplementarExigido
		else t.cargaHorariaAtividadeComplementarCumprido
	end ))::INT as cargaHorariaCumprido,
	(t.cargaHorariaTotal 
-
	(case
		when t.cargaHorariaDisciplinaObrigatorioCumprida > t.cargaHorariaDisciplinaObrigatorioExigida then t.cargaHorariaDisciplinaObrigatorioExigida
		else t.cargaHorariaDisciplinaObrigatorioCumprida
	end ) 
-
	(case
		when t.cargaHorariaDisciplinaOptativaExigida > 0 then
		case
			when t.quantidadeMinimaDisciplinaOptativaExigida > 0
			and t.quantidadeDisciplinaOptativaCumprida = t.quantidadeMinimaDisciplinaOptativaExigida then t.cargaHorariaDisciplinaOptativaExigida
			else t.cargaHorariaDisciplinaOptativaCumprida
		end
		else 0
	end)
-
	(case
		when t.cargaHorariaEstagioCumprido > t.cargaHorariaEstagioExigido then t.cargaHorariaEstagioExigido
		else t.cargaHorariaEstagioCumprido
	end )
-
	(case
		when t.cargaHorariaAtividadeComplementarCumprido > t.cargaHorariaAtividadeComplementarExigido then t.cargaHorariaAtividadeComplementarExigido
		else t.cargaHorariaAtividadeComplementarCumprido
	end ))::int as cargaHorariaPendente,
	(((
	(case when t.cargaHorariaDisciplinaObrigatorioCumprida > t.cargaHorariaDisciplinaObrigatorioExigida then t.cargaHorariaDisciplinaObrigatorioExigida
	else t.cargaHorariaDisciplinaObrigatorioCumprida
end ) 
+
	(case
		when t.cargaHorariaDisciplinaOptativaExigida > 0 then
		case
			when t.quantidadeMinimaDisciplinaOptativaExigida > 0
			and t.quantidadeDisciplinaOptativaCumprida = t.quantidadeMinimaDisciplinaOptativaExigida then t.cargaHorariaDisciplinaOptativaExigida
			else t.cargaHorariaDisciplinaOptativaCumprida
		end
		else 0
	end)
+
	(case
		when t.cargaHorariaEstagioCumprido > t.cargaHorariaEstagioExigido then t.cargaHorariaEstagioExigido
		else t.cargaHorariaEstagioCumprido
	end )
+
	(case
		when t.cargaHorariaAtividadeComplementarCumprido > t.cargaHorariaAtividadeComplementarExigido then t.cargaHorariaAtividadeComplementarExigido
		else t.cargaHorariaAtividadeComplementarCumprido
	end ))::numeric(20,
	2) * 100) / t.cargaHorariaTotal)::numeric(20,
	2) as percentualIntegralizado,
	(100 - (((
	(case when t.cargaHorariaDisciplinaObrigatorioCumprida > t.cargaHorariaDisciplinaObrigatorioExigida then t.cargaHorariaDisciplinaObrigatorioExigida
	else t.cargaHorariaDisciplinaObrigatorioCumprida
end ) 
+
	(case
		when t.cargaHorariaDisciplinaOptativaExigida > 0 then
		case
			when t.quantidadeMinimaDisciplinaOptativaExigida > 0
			and t.quantidadeDisciplinaOptativaCumprida = t.quantidadeMinimaDisciplinaOptativaExigida then t.cargaHorariaDisciplinaOptativaExigida
			else t.cargaHorariaDisciplinaOptativaCumprida
		end
		else 0
	end)
+
	(case
		when t.cargaHorariaEstagioCumprido > t.cargaHorariaEstagioExigido then t.cargaHorariaEstagioExigido
		else t.cargaHorariaEstagioCumprido
	end )
+
	(case
		when t.cargaHorariaAtividadeComplementarCumprido > t.cargaHorariaAtividadeComplementarExigido then t.cargaHorariaAtividadeComplementarExigido
		else t.cargaHorariaAtividadeComplementarCumprido
	end ))::numeric(20,
	2) * 100) / t.cargaHorariaTotal))::numeric(20,
	2) as percentualNaoIntegralizado,
	t.percentualPermitirIniciarEstagio::numeric(20,
	2) as percentualPermitirIniciarEstagio,
	case
		when t.percentualPermitirIniciarEstagio > 0 then (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarEstagio)/ 100)::INTEGER
		else 0::INTEGER
	end as cargaHorariaExigidaLiberarEstagio,
	case
		when t.percentualPermitirIniciarEstagio > 0 then (t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::INTEGER
		else 0::INTEGER
	end as cargaHorariaCumpridaLiberarEstagio,
	case
		when t.percentualPermitirIniciarEstagio > 0 then
		case
			when (t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) > (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarEstagio)::numeric(20,
			2)/ 100.0::numeric(20,
			2)) then 100.0::numeric(20,
			2)
			else 
	 ((t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) / (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarEstagio)::numeric(20,
			2)/ 100.0::numeric(20,
			2))) * 100.0::numeric(20,
			2)
		end
		else 0.0::numeric(20,
		2)
	end as percentualCumpridoLiberarEstagio,
	case
		when t.percentualPermitirIniciarEstagio > 0 then
		case
			when (t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) > (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarEstagio)::numeric(20,
			2)/ 100.0::numeric(20,
			2)) then 0.0::numeric(20,
			2)
			else 
	100.0::numeric(20,
			2) - (((t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) / (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarEstagio)::numeric(20,
			2)/ 100.0::numeric(20,
			2))) * 100.0::numeric(20,
			2))
		end
		else 0.0::numeric(20,
		2)
	end as percentualPendenteLiberarEstagio,
	t.percentualPermitirIniciarTcc::numeric(20,
	2) as percentualPermitirIniciarTcc,
	case
		when t.percentualPermitirIniciarTcc > 0 then (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarTcc)/ 100)::INTEGER
		else 0::INTEGER
	end as cargaHorariaExigidaLiberarTcc,
	case
		when t.percentualPermitirIniciarTcc > 0 then (t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::INTEGER
		else 0::INTEGER
	end as cargaHorariaCumpridaLiberarTcc,
	case
		when t.percentualPermitirIniciarTcc > 0 then
		case
			when (t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) > (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarTcc)::numeric(20,
			2)/ 100.0::numeric(20,
			2)) then 100.0::numeric(20,
			2)
			else 
	 ((t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) / (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarTcc)::numeric(20,
			2)/ 100.0::numeric(20,
			2))) * 100.0::numeric(20,
			2)
		end
		else 0.0::numeric(20,
		2)
	end as percentualCumpridoLiberarTcc,
	case
		when t.percentualPermitirIniciarTcc > 0 then
		case
			when (t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) > (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarTcc)::numeric(20,
			2)/ 100.0::numeric(20,
			2)) then 0.0::numeric(20,
			2)
			else 
	100.0::numeric(20,
			2) - (((t.cargaHorariaDisciplinaObrigatorioCumprida + t.cargaHorariaAtividadeComplementarCumprido - t.cargaHorariaCumpridaTCC + t.cargaHorariaDisciplinaOptativaCumprida)::numeric(20,
			2) / (((t.cargaHorariaTotal - t.cargaHorariaEstagioExigido - t.cargaHorariaExigidaTCC) * t.percentualPermitirIniciarTcc)::numeric(20,
			2)/ 100.0::numeric(20,
			2))) * 100.0::numeric(20,
			2))
		end
		else 0.0::numeric(20,
		2)
	end as percentualPendenteLiberarTcc
from
	(
	select
		matricula.matricula,
		gradecurricularatual.cargahoraria as cargaHorariaTotal,
		(
		select
			sum(gd.cargahoraria)
		from
			gradedisciplina as gd
		inner join periodoletivo pl on
			pl.codigo = gd.periodoletivo
		inner join disciplina on
			disciplina.codigo = gd.disciplina
		where
			pl.gradecurricular = matricula.gradecurricularatual
			and ((disciplina.classificacaodisciplina != 'ESTAGIO')
				or (disciplina.classificacaodisciplina = 'ESTAGIO'
					and not exists(
					select
						gce.codigo
					from
						gradecurricularestagio gce
					where
						gce.gradecurricular = gradecurricularatual.codigo )))
			and gd.tipodisciplina not in ('OP', 'LO')) as cargaHorariaDisciplinaObrigatorioExigida,
		(
		select
			sum(gd.cargahoraria)
		from
			gradedisciplina as gd
		inner join periodoletivo pl on
			pl.codigo = gd.periodoletivo
		inner join disciplina on
			disciplina.codigo = gd.disciplina
		where
			pl.gradecurricular = matricula.gradecurricularatual
			and ((disciplina.classificacaodisciplina != 'ESTAGIO')
				or (disciplina.classificacaodisciplina = 'ESTAGIO'
					and not exists(
					select
						gce.codigo
					from
						gradecurricularestagio gce
					where
						gce.gradecurricular = gradecurricularatual.codigo )))
			and gd.tipodisciplina not in ('OP', 'LO')
				and exists (
				select
					his.codigo
				from
					historico his
				where
					his.matricula = matricula.matricula
					and his.matrizcurricular = matricula.gradecurricularatual
					and his.gradedisciplina = gd.codigo
					and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))) as cargaHorariaDisciplinaObrigatorioCumprida,
		(gradecurricularatual.cargahoraria - 
			(
		select
			sum(gd.cargahoraria)
		from
			gradedisciplina as gd
		inner join periodoletivo pl on
			pl.codigo = gd.periodoletivo
		inner join disciplina on
			disciplina.codigo = gd.disciplina
		where
			pl.gradecurricular = matricula.gradecurricularatual
			and ((disciplina.classificacaodisciplina != 'ESTAGIO')
				or (disciplina.classificacaodisciplina = 'ESTAGIO'
					and not exists(
					select
						gce.codigo
					from
						gradecurricularestagio gce
					where
						gce.gradecurricular = gradecurricularatual.codigo )))
			and gd.tipodisciplina not in ('OP', 'LO')) - 
			 case
			when gradecurricularatual.totalcargahorariaestagio is null then 0
			else gradecurricularatual.totalcargahorariaestagio
		end - 
			 case
			when totalcargahorariaatividadecomplementar is null then 0
			else totalcargahorariaatividadecomplementar
		end ) as cargaHorariaDisciplinaOptativaExigida,
		case
			when gradecurricularatual.quantidadedisciplinasoptativasmatrizcurricular > 0 then gradecurricularatual.quantidadedisciplinasoptativasmatrizcurricular
			else curso.quantidadedisciplinasoptativasexpedicaodiploma
		end as quantidadeMinimaDisciplinaOptativaExigida,
		(
		select
			case
				when sum(cargahoraria) is null then 0
				else sum(cargahoraria)
			end
		from
			(
			select
				gd.cargahoraria
			from
				historico his
			inner join gradedisciplina gd on
				gd.codigo = his.gradedisciplina
			inner join disciplina on
				disciplina.codigo = gd.disciplina
			inner join periodoletivo pl on
				pl.codigo = gd.periodoletivo
			where
				his.matricula = matricula.matricula
				and his.matrizcurricular = matricula.gradecurricularatual
				and ((disciplina.classificacaodisciplina != 'ESTAGIO')
					or (disciplina.classificacaodisciplina = 'ESTAGIO'
						and not exists(
						select
							gce.codigo
						from
							gradecurricularestagio gce
						where
							gce.gradecurricular = gradecurricularatual.codigo )))
				and pl.gradecurricular = matricula.gradecurricularatual
				and gd.tipodisciplina in ('OP', 'LO')
					and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')
			union all
				select
					gcgod.cargahoraria
				from
					historico his
				inner join gradecurriculargrupooptativadisciplina gcgod on
					gcgod.codigo = his.gradecurriculargrupooptativadisciplina
				inner join gradecurriculargrupooptativa as gcgo on
					gcgod.gradecurriculargrupooptativa = gcgo.codigo
				where
					his.matricula = matricula.matricula
					and his.matrizcurricular = matricula.gradecurricularatual
					and gcgo.gradecurricular = matricula.gradecurricularatual
					and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')
     ) as t) as cargaHorariaDisciplinaOptativaCumprida,
		(
		select
			count(distinct disciplina)
		from
			(
			select
				his.disciplina
			from
				historico his
			inner join gradedisciplina gd on
				gd.codigo = his.gradedisciplina
			inner join periodoletivo pl on
				pl.codigo = gd.periodoletivo
			where
				his.matricula = matricula.matricula
				and his.matrizcurricular = matricula.gradecurricularatual
				and pl.gradecurricular = matricula.gradecurricularatual
				and gd.tipodisciplina in ('OP', 'LO')
					and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')
			union all
				select
					his.disciplina
				from
					historico his
				inner join gradecurriculargrupooptativadisciplina gcgod on
					gcgod.codigo = his.gradecurriculargrupooptativadisciplina
				inner join gradecurriculargrupooptativa as gcgo on
					gcgod.gradecurriculargrupooptativa = gcgo.codigo
				where
					his.matricula = matricula.matricula
					and his.matrizcurricular = matricula.gradecurricularatual
					and gcgo.gradecurricular = matricula.gradecurricularatual
					and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE')
     ) as t) as quantidadeDisciplinaOptativaCumprida,
		coalesce(gradecurricularatual.totalcargahorariaestagio, 0) as cargaHorariaEstagioExigido,
		coalesce(( (select sum(est.cargaHoraria) as cargaHoraria from estagio est where est.matricula = matricula.matricula and est.situacaoestagioenum = 'DEFERIDO' ) ), 0) as cargaHorariaEstagioCumprido,
		coalesce(gradecurricularatual.totalcargahorariaatividadecomplementar, 0) as cargaHorariaAtividadeComplementarExigido,
		coalesce((select case when cargahorariaconsiderada > cargahorariaexigida then cargahorariaexigida else cargahorariaconsiderada end cargaHorariaAtividadeComplementar from 
	(select distinct gradecurricularatual.totalCargaHorariaAtividadeComplementar as cargahorariaexigida, (select sum (case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end )	 
	as cargaHorariaRealizadaAtividadeComplementar from ( select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar,
	 registroatividadecomplementarmatricula.tipoAtividadeComplementar, gradecurriculartipoatividadecomplementar.cargahoraria 
		 from registroatividadecomplementarmatricula 
		  inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar 
		 where registroatividadecomplementarmatricula.matricula = matricula.matricula and gradecurriculartipoatividadecomplementar.gradecurricular = gradecurricularatual.codigo 
		 and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = 'DEFERIDO' 
		group by registroatividadecomplementarmatricula.tipoAtividadeComplementar, gradecurriculartipoatividadecomplementar.cargahoraria ) as t) as cargahorariaconsiderada ) as t), 0) as cargaHorariaAtividadeComplementarCumprido, 
		coalesce(gradecurricularatual.percentualpermitiriniciarestagio, 0) as percentualPermitirIniciarEstagio,
		coalesce(gradecurricularatual.percentualpermitiriniciartcc, 0) as percentualPermitirIniciarTcc,
		coalesce((select sum(gd.cargahoraria) from gradedisciplina as gd 
			inner join periodoletivo pl on pl.codigo = gd.periodoletivo 
			inner join disciplina on disciplina.codigo = gd.disciplina 
			where pl.gradecurricular = matricula.gradecurricularatual 
			and disciplina.classificacaodisciplina in ('TCC')), 0) as cargaHorariaExigidaTCC,
		coalesce((select sum(gd.cargahoraria) from gradedisciplina as gd 
			inner join periodoletivo pl on pl.codigo = gd.periodoletivo 
			inner join disciplina on disciplina.codigo = gd.disciplina 
			where pl.gradecurricular = matricula.gradecurricularatual 
			and disciplina.classificacaodisciplina in ('TCC')
			and exists (
			select his.codigo from historico his where his.matricula = matricula.matricula and his.matrizcurricular = matricula.gradecurricularatual 
			 and his.gradedisciplina = gd.codigo and his.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))), 0) as cargaHorariaCumpridaTCC
	from
		matricula
	inner join curso on
		curso.codigo = matricula.curso
	inner join gradecurricular gradecurricularatual on
		gradecurricularatual.codigo = matricula.gradecurricularatual
	where
		matricula.matricula = $1
) as t;
end;

$function$
;
