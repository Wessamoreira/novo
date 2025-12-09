package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.ControleVagaRelVO;
import relatorio.negocio.interfaces.academico.ControleVagaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ControleVagaRel extends SuperRelatorio implements ControleVagaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public ControleVagaRel() throws Exception {
	}

	public void validarDados(TurmaVO turmaVO, List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs, ControleVagaRelVO controleVagaVO, String filtrarPor, String periodicidade, String ano, String semestre) throws Exception {
		if (filtrarPor.equals("curso")) {
			if (unidadeEnsinoCursoVOs == null || unidadeEnsinoCursoVOs != null && unidadeEnsinoCursoVOs.isEmpty()) {
				throw new ConsistirException("Por Favor informe um curso para a geração do relatório.");
			}
			if (periodicidade.equals("SE")) {
				if (ano.equals("")) {
					throw new ConsistirException("Por Favor informe um ano para a geração do relatório.");
				}
				if (semestre.equals("")) {
					throw new ConsistirException("Por Favor informe um semestre para a geração do relatório.");
				}
			} else if (periodicidade.equals("AN")) {
				if (ano.equals("")) {
					throw new ConsistirException("Por Favor informe um ano para a geração do relatório.");
				}
			}
		}
		if (filtrarPor.equals("turma")) {
			if (turmaVO == null || turmaVO.getCodigo().equals(0)) {
				throw new ConsistirException("Por Favor informe uma turma para a geração do relatório.");
			}
			if (turmaVO.getCurso().getPeriodicidade().equals("SE")) {
				if (ano.equals("")) {
					throw new ConsistirException("Por Favor informe um ano para a geração do relatório.");
				}
				if (semestre.equals("")) {
					throw new ConsistirException("Por Favor informe um semestre para a geração do relatório.");
				}
			} else if (turmaVO.getCurso().getPeriodicidade().equals("AN")) {
				if (ano.equals("")) {
					throw new ConsistirException("Por Favor informe um ano para a geração do relatório.");
				}
			}
		}
	}

	public List<ControleVagaRelVO> criarObjeto(ControleVagaRelVO controleVagaRelVO, TurmaVO turmaVO, List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs,
												Boolean unificarTurmas, Boolean detalharCalouroVeterano, String filtrarPor, String tipoRelatorio, String ano, String semestre) throws Exception {
		List<ControleVagaRelVO> listaControleVagaRelVO = new ArrayList<ControleVagaRelVO>(0);
		SqlRowSet dadosSQL;
		dadosSQL = executarConsultaParametrizada(controleVagaRelVO, turmaVO, unidadeEnsinoCursoVOs, unificarTurmas, detalharCalouroVeterano, filtrarPor, tipoRelatorio, ano, semestre);
		while (dadosSQL != null && dadosSQL.next()) {
			listaControleVagaRelVO.add(montarDados(dadosSQL, tipoRelatorio));
		}
		return listaControleVagaRelVO;
	}
	
	public SqlRowSet executarConsultaParametrizada(ControleVagaRelVO ControleVagaRelVO, TurmaVO turmaVO, List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs,
			Boolean unificarTurmas, Boolean detalharCalouroVeterano, String filtrarPor, String tipoRelatorio, String ano, String semestre) throws Exception {
		if (tipoRelatorio.equals("AN")) {
			return executarConsultaParametrizadaAnalitica(ControleVagaRelVO, turmaVO, unidadeEnsinoCursoVOs, unificarTurmas, filtrarPor, detalharCalouroVeterano, ano, semestre);
		} else if (tipoRelatorio.equals("SI")) {
			return executarConsultaParametrizadaSintetica(ControleVagaRelVO, turmaVO, unidadeEnsinoCursoVOs, unificarTurmas, filtrarPor, detalharCalouroVeterano, ano, semestre);
		} else {
			return null;
		}
	}

	public ControleVagaRelVO montarDados(SqlRowSet dadosSQL, String tipoRelatorio) throws Exception {
		ControleVagaRelVO controleVagaRelVO = new ControleVagaRelVO();
		controleVagaRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino"));
		controleVagaRelVO.setIdentificadorTurma(dadosSQL.getString("turma"));
		controleVagaRelVO.setNomeCurso(dadosSQL.getString("curso"));
		controleVagaRelVO.setNomeGradeCurricular(dadosSQL.getString("gradecurricular"));
		controleVagaRelVO.setDescricaoPeriodoLetivo(dadosSQL.getString("periodoletivo"));
		controleVagaRelVO.setAbreviaturaDisciplina(dadosSQL.getString("disciplina"));
		controleVagaRelVO.setNrmaximomatricula(dadosSQL.getInt("nrmaximomatricula"));
		controleVagaRelVO.setNrMaximoVagasReposicao(dadosSQL.getInt("nrmaximovagasreposicao"));
		controleVagaRelVO.setNrVagas(dadosSQL.getInt("nrVagas"));
		controleVagaRelVO.setTipohistorico(dadosSQL.getString("tipohistorico"));
		if (tipoRelatorio.equals("AN")) {
			controleVagaRelVO.setMatricula(dadosSQL.getString("matricula"));
			controleVagaRelVO.setNomeAluno(dadosSQL.getString("aluno"));
			controleVagaRelVO.setSituacao(dadosSQL.getString("situacao"));
			controleVagaRelVO.setVeterano(dadosSQL.getInt("veterano"));
		} else if (tipoRelatorio.equals("SI")) {
			controleVagaRelVO.setQtdeMatricula(dadosSQL.getInt("matriculas"));
			controleVagaRelVO.setQtdeMatriculaAtiva(dadosSQL.getInt("ativas"));
			controleVagaRelVO.setQtdeMatriculaAtivaReposicao(dadosSQL.getInt("ativasReposicao"));
			controleVagaRelVO.setVeterano(dadosSQL.getInt("veteranos"));
			controleVagaRelVO.setQtdPreMatriculas(dadosSQL.getInt("preMatriculas"));
			controleVagaRelVO.setQtdPreMatriculasReposicao(dadosSQL.getInt("preMatriculasReposicao"));
		}
		return controleVagaRelVO;
	}

	@SuppressWarnings("rawtypes")
	public SqlRowSet executarConsultaParametrizadaAnalitica(ControleVagaRelVO controleVagaRelVO, TurmaVO turmaVO, List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs,
			Boolean unificarTurmas, String filtrarPor, Boolean detalharCalouroVeterano, String ano, String semestre) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		
		if (unificarTurmas) {
			sqlStr.append("select distinct y.unidadeensino, '' as turma, y.disciplina, '' as curso, '' as gradecurricular, '' as periodoletivo, ");
			sqlStr.append("		y.matricula, y.aluno, y.situacao, y.veterano, y.nrmaximomatricula, y.nrmaximovagasreposicao, y.tipohistorico, y.nrVagas ");
			sqlStr.append("from ( ");
		}
		sqlStr.append("select x.unidadeensino, x.turma, x.disciplina, x.curso, x.gradecurricular, x.periodoletivo, x.matricula, x.aluno, x.situacao, x.veterano, ");
		sqlStr.append("		case when x.vtd_nrmaximomatricula is not null then x.vtd_nrmaximomatricula else x.tu_nrmaximomatricula end as nrmaximomatricula, ");
		sqlStr.append(" CASE WHEN x.vtd_nrvagasmatriculareposicao IS NOT NULL THEN vtd_nrvagasmatriculareposicao ELSE x.tu_nrvagasinclusaoreposicao END AS nrmaximovagasreposicao, x.h_tipohistorico AS tipohistorico, ");
		sqlStr.append(" CASE WHEN x.vtd_nrvagas IS NOT NULL THEN x.vtd_nrvagas ELSE x.tu_nrvagas END AS nrVagas ");
		sqlStr.append("from ( ");
		sqlStr.append("		select ue.nome as unidadeensino, tu.identificadorturma as turma, ");
		sqlStr.append("			array_to_string(array_agg(distinct c.nome order by c.nome),',') as curso, ");
		sqlStr.append("			array_to_string(array_agg(distinct gc.nome order by gc.nome),',') as gradecurricular, ");
		sqlStr.append("			array_to_string(array_agg(distinct pl.descricao order by pl.descricao),',') as periodoletivo, ");
		sqlStr.append("			d.nome as disciplina, ");
		sqlStr.append("			m.matricula, p.nome as aluno, ");
		sqlStr.append("			case when mp.situacaomatriculaperiodo = 'AT' then 'Ativa' when mp.situacaomatriculaperiodo = 'PR' then 'Pré-matrícula' else mp.situacaomatriculaperiodo end as situacao, ");
		if (detalharCalouroVeterano) {
		sqlStr.append("			( ");
		sqlStr.append("				(select count(mpi.codigo) from matriculaperiodo mpi ");
		sqlStr.append("				 where mpi.matricula = m.matricula ");
		sqlStr.append("				 and mpi.situacaomatriculaperiodo != 'PC' ");
		sqlStr.append("				 and case when c.periodicidade = 'IN' then mpi.data < mp.data else (mpi.ano||'/'||mpi.semestre) < (mp.ano||'/'||mp.semestre) end) ");
		sqlStr.append("			) as veterano, ");
		} else {
		sqlStr.append("			0 as veterano, ");
		}
		sqlStr.append("			tu.nrmaximomatricula as tu_nrmaximomatricula, vtd.nrmaximomatricula as vtd_nrmaximomatricula, tu.nrvagasinclusaoreposicao AS tu_nrvagasinclusaoreposicao, vtd.nrvagasmatriculareposicao AS vtd_nrvagasmatriculareposicao, h.tipohistorico AS h_tipohistorico, tu.nrvagas AS tu_nrvagas, vtd.vagaturma AS vtd_nrvagas ");
		sqlStr.append("		from matricula as m ");
		if (filtrarPor.equals("turma")) {
		sqlStr.append("		inner join pessoa as p on p.codigo = m.aluno ");
		sqlStr.append("		inner join curso as c on c.codigo = m.curso ");
		sqlStr.append("		inner join matriculaperiodo as mp on mp.matricula = m.matricula and mp.ano = '").append(ano).append("' and mp.semestre = '").append(semestre).append("' and mp.situacaomatriculaperiodo in ('AT','PR') ");
		sqlStr.append("		inner join periodoletivo as pl on pl.codigo = mp.periodoletivomatricula ");
		sqlStr.append("		inner join gradecurricular as gc on gc.codigo = pl.gradecurricular ");
		sqlStr.append("		inner join matriculaperiodoturmadisciplina as mptd on mptd.matricula = m.matricula and mptd.ano = '").append(ano).append("' and mptd.semestre = '").append(semestre).append("' ");
		sqlStr.append(" 	INNER JOIN historico h ON mptd.codigo = h.matriculaperiodoturmadisciplina ");
		sqlStr.append("		inner join turma as tu on tu.codigo = ").append(turmaVO.getCodigo());
		sqlStr.append("		inner join unidadeensino as ue on ue.codigo = tu.unidadeensino ");
		sqlStr.append("		inner join turmadisciplina as td on td.turma = tu.codigo ");
		sqlStr.append("		inner join disciplina as d on d.codigo = td.disciplina ");
		sqlStr.append("		left join vagaturma as vt on vt.turma = tu.codigo ");
		sqlStr.append("		left join vagaturmadisciplina as vtd on vtd.vagaturma = vt.codigo and vtd.disciplina = d.codigo ");
		sqlStr.append("		left join turma as t on t.codigo = mptd.turma ");
		sqlStr.append("		left join turma as tt on tt.codigo = mptd.turmateorica ");
		sqlStr.append("		left join turma as tp on tp.codigo = mptd.turmapratica ");
		sqlStr.append("		left join turmaagrupada as ta on ta.turma = mptd.turma ");
		sqlStr.append("		left join turma as tm on tm.codigo = ta.turmaorigem and tm.turmaagrupada and tm.subturma is not true ");
		sqlStr.append("		where td.disciplina = mptd.disciplina and tu.codigo in (t.codigo, tt.codigo, tp.codigo, ta.turmaorigem) ");
		sqlStr.append("		and (ta.turmaorigem is null or (ta.turmaorigem is not null and tm.codigo is not null ");
		sqlStr.append("     and not exists (select 1 from turma where turmaprincipal = tm.codigo))) "); // excluindo turma agrupada que tenha subturma
		sqlStr.append("     and (t.codigo is null or (t.codigo is not null and tt.codigo is null and tp.codigo is null)) "); // excluindo turma se tiver turmateorica ou turmapratica
		} else if (filtrarPor.equals("curso")) {
		sqlStr.append("		inner join pessoa as p on p.codigo = m.aluno ");
		sqlStr.append("		inner join curso as c on c.codigo = m.curso ");
		sqlStr.append("		inner join unidadeensinocurso as uec on uec.curso = c.codigo ");
		if (!unidadeEnsinoCursoVOs.isEmpty()) {
		sqlStr.append("			and uec.codigo in (");
		Iterator i = unidadeEnsinoCursoVOs.iterator();
		while (i.hasNext()) {
			UnidadeEnsinoCursoVO uec = (UnidadeEnsinoCursoVO)i.next();
			sqlStr.append(uec.getCodigo()).append(",");
		}
		sqlStr.append("0)");
		}
		sqlStr.append("		inner join unidadeensino as ue on ue.codigo = uec.unidadeensino ");
		sqlStr.append("		inner join matriculaperiodo as mp on mp.matricula = m.matricula and mp.ano = '").append(ano).append("' and mp.semestre = '").append(semestre).append("' and mp.situacaomatriculaperiodo in ('AT','PR') ");
		sqlStr.append("		inner join periodoletivo as pl on pl.codigo = mp.periodoletivomatricula ");
		sqlStr.append("		inner join gradecurricular as gc on gc.codigo = pl.gradecurricular ");
		sqlStr.append("		inner join matriculaperiodoturmadisciplina as mptd on mptd.matricula = m.matricula and mptd.ano = '").append(ano).append("' and mptd.semestre = '").append(semestre).append("' ");
		sqlStr.append(" 	INNER JOIN historico h ON mptd.codigo = h.matriculaperiodoturmadisciplina ");
		sqlStr.append("		left join turma as t on t.codigo = mptd.turma ");
		sqlStr.append("		left join turma as tt on tt.codigo = mptd.turmateorica ");
		sqlStr.append("		left join turma as tp on tp.codigo = mptd.turmapratica ");
		sqlStr.append("		left join turmaagrupada as ta on ta.turma = mptd.turma ");
		sqlStr.append("		left join turma as tm on tm.codigo = ta.turmaorigem and tm.turmaagrupada and tm.subturma is not true ");
		sqlStr.append("		inner join turma as tu on tu.codigo in (t.codigo, tt.codigo, tp.codigo, ta.turmaorigem) ");
		sqlStr.append("		inner join turmadisciplina as td on td.turma = tu.codigo ");
		sqlStr.append("		inner join disciplina as d on d.codigo = td.disciplina ");
		sqlStr.append("		left join vagaturma as vt on vt.turma = tu.codigo ");
		sqlStr.append("		left join vagaturmadisciplina as vtd on vtd.vagaturma = vt.codigo and vtd.disciplina = d.codigo ");
		sqlStr.append("		where td.disciplina = mptd.disciplina and (ta.turmaorigem is null or (ta.turmaorigem is not null and tm.codigo is not null ");
		sqlStr.append("     and not exists (select 1 from turma where turmaprincipal = tm.codigo))) "); // excluindo turma agrupada que tenha subturma
		sqlStr.append("     and (tu.codigo <> t.codigo or (tt.codigo is null and tp.codigo is null)) "); // excluindo turma se tiver turmateorica ou turmapratica
		}
		sqlStr.append("		group by 1,2,6,7,8,9,10,11,12,13,14,15,16,17 ");
		sqlStr.append(") as x ");
		if (unificarTurmas) {
			sqlStr.append(") as y ");
		}
		sqlStr.append("order by 1,2,3,4,5,6,7 ");
				
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}
	
	@SuppressWarnings("rawtypes")
	public SqlRowSet executarConsultaParametrizadaSintetica(ControleVagaRelVO controleVagaRelVO, TurmaVO turmaVO, List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs,
			Boolean unificarTurmas, String filtrarPor, Boolean detalharCalouroVeterano, String ano, String semestre) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		
		if (unificarTurmas) {
			sqlStr.append("select y.unidadeensino, '' as turma, y.disciplina, '' as curso, '' as gradecurricular, '' as periodoletivo, ");
		sqlStr.append("		sum(y.matriculas) as matriculas, sum(y.ativas) as ativas, sum(y.veteranos) as veteranos, sum(y.nrmaximomatricula) as nrmaximomatricula, y.nrmaximovagasreposicao, y.tipohistorico, y.ativasReposicao, y.preMatriculas, y.preMatriculasReposicao, y.nrVagas ");
		sqlStr.append("from ( ");
		}
		sqlStr.append("	select x.unidadeensino,	x.turma, x.disciplina, ");
		if (unificarTurmas) {
		sqlStr.append("		x.curso, x.gradecurricular, x.periodoletivo, ");
		} else {
		sqlStr.append("		array_to_string(array_agg(distinct x.curso order by x.curso),',') as curso, ");
		sqlStr.append("		array_to_string(array_agg(distinct x.gradecurricular order by x.gradecurricular),',') as gradecurricular, ");
		sqlStr.append("		array_to_string(array_agg(distinct x.periodoletivo order by x.periodoletivo),',') as periodoletivo, ");
		}
		sqlStr.append("		count(distinct x.matricula) as matriculas, ");
		sqlStr.append("		sum(CASE WHEN x.situacaomatriculaperiodo = 'AT' AND x.h_tipohistorico NOT IN ('DE') THEN 1 ELSE 0 END) AS ativas, ");
		if (detalharCalouroVeterano) {
		sqlStr.append("		sum(case when ( ");
		sqlStr.append("			(select count(mpi.codigo) from matriculaperiodo mpi ");
		sqlStr.append("			 where mpi.matricula = x.matricula ");
		sqlStr.append("			 and mpi.situacaomatriculaperiodo != 'PC' ");
		sqlStr.append("			 and case when x.periodicidade = 'IN' then mpi.data < x.data else (mpi.ano||'/'||mpi.semestre) < (x.ano||'/'||x.semestre) end) > 0 ");
		sqlStr.append("		) then 1 else 0 end) as veteranos, ");
		} else {
		sqlStr.append("		0 as veteranos, ");
		}
		sqlStr.append("		case when x.vtd_nrmaximomatricula is not null then x.vtd_nrmaximomatricula else x.tu_nrmaximomatricula end as nrmaximomatricula, ");
		sqlStr.append(" 	CASE WHEN x.vtd_nrvagasmatriculareposicao IS NOT NULL THEN vtd_nrvagasmatriculareposicao ELSE x.tu_nrvagasinclusaoreposicao END AS nrmaximovagasreposicao, x.h_tipohistorico AS tipohistorico, ");
		sqlStr.append(" sum(CASE WHEN x.situacaomatriculaperiodo = 'AT' AND x.h_tipohistorico IN ('DE') THEN 1 ELSE 0 END) AS ativasReposicao, ");
		sqlStr.append(" sum(CASE WHEN x.situacaomatriculaperiodo = 'PR' AND x.h_tipohistorico NOT IN ('DE') THEN 1 ELSE 0 END) AS preMatriculas, sum(CASE WHEN x.situacaomatriculaperiodo = 'PR' AND x.h_tipohistorico IN ('DE') THEN 1 ELSE 0 END) AS preMatriculasReposicao, ");
		sqlStr.append(" CASE WHEN x.vtd_nrvagas IS NOT NULL THEN x.vtd_nrvagas ELSE x.tu_nrvagas END AS nrVagas ");
		sqlStr.append("	from ( ");
		sqlStr.append("		select distinct ue.nome as unidadeensino, ");
		sqlStr.append("			tu.identificadorturma as turma, ");
		sqlStr.append("			c.nome as curso, ");
		sqlStr.append("			gc.nome as gradecurricular, ");
		sqlStr.append("			pl.descricao as periodoletivo, ");
		sqlStr.append("			d.nome as disciplina, ");
		sqlStr.append("			mp.situacaomatriculaperiodo, m.matricula,	 c.periodicidade, mp.data, mp.ano, mp.semestre, ");
		sqlStr.append("			tu.nrmaximomatricula as tu_nrmaximomatricula, vtd.nrmaximomatricula as vtd_nrmaximomatricula, tu.nrvagasinclusaoreposicao AS tu_nrvagasinclusaoreposicao, vtd.nrvagasmatriculareposicao AS vtd_nrvagasmatriculareposicao, h.tipohistorico AS h_tipohistorico, tu.nrvagas AS tu_nrvagas, vtd.vagaturma AS vtd_nrvagas ");
		sqlStr.append("		from matricula as m ");
		if (filtrarPor.equals("turma")) {
		sqlStr.append("		inner join curso as c on c.codigo = m.curso ");
		sqlStr.append("		inner join matriculaperiodo as mp on mp.matricula = m.matricula and mp.ano = '").append(ano).append("' and mp.semestre = '").append(semestre).append("' and mp.situacaomatriculaperiodo in ('AT','PR') ");
		sqlStr.append("		inner join periodoletivo as pl on pl.codigo = mp.periodoletivomatricula ");
		sqlStr.append("		inner join gradecurricular as gc on gc.codigo = pl.gradecurricular ");
		sqlStr.append("		inner join matriculaperiodoturmadisciplina as mptd on mptd.matricula = m.matricula and mptd.ano = '").append(ano).append("' and mptd.semestre = '").append(semestre).append("' ");
		sqlStr.append(" 	INNER JOIN historico h ON mptd.codigo = h.matriculaperiodoturmadisciplina ");
		sqlStr.append("		inner join turma as tu on tu.codigo = ").append(turmaVO.getCodigo());
		sqlStr.append("		inner join unidadeensino as ue on ue.codigo = tu.unidadeensino ");
		sqlStr.append("		inner join turmadisciplina as td on td.turma = tu.codigo ");
		sqlStr.append("		inner join disciplina as d on d.codigo = td.disciplina ");
		sqlStr.append("		left join vagaturma as vt on vt.turma = tu.codigo ");
		sqlStr.append("		left join vagaturmadisciplina as vtd on vtd.vagaturma = vt.codigo and vtd.disciplina = d.codigo ");
		sqlStr.append("		left join turma as t on t.codigo = mptd.turma ");
		sqlStr.append("		left join turma as tt on tt.codigo = mptd.turmateorica ");
		sqlStr.append("		left join turma as tp on tp.codigo = mptd.turmapratica ");
		sqlStr.append("		left join turmaagrupada as ta on ta.turma = mptd.turma ");
		sqlStr.append("		left join turma as tm on tm.codigo = ta.turmaorigem and tm.turmaagrupada and tm.subturma is not true ");
		sqlStr.append("		where td.disciplina = mptd.disciplina and tu.codigo in (t.codigo, tt.codigo, tp.codigo, ta.turmaorigem) ");
		sqlStr.append("		and (ta.turmaorigem is null or (ta.turmaorigem is not null and tm.codigo is not null ");
		sqlStr.append("     and not exists (select 1 from turma where turmaprincipal = tm.codigo))) "); // excluindo turma agrupada que tenha subturma
		sqlStr.append("     and (t.codigo is null or (t.codigo is not null and tt.codigo is null and tp.codigo is null)) "); // excluindo turma se tiver turmateorica ou turmapratica
		} else if (filtrarPor.equals("curso")) {
		sqlStr.append("		inner join curso as c on c.codigo = m.curso ");
		sqlStr.append("		inner join unidadeensinocurso as uec on uec.curso = c.codigo ");
		if (!unidadeEnsinoCursoVOs.isEmpty()) {
		sqlStr.append("			and uec.codigo in (");
		Iterator i = unidadeEnsinoCursoVOs.iterator();
		while (i.hasNext()) {
			UnidadeEnsinoCursoVO uec = (UnidadeEnsinoCursoVO)i.next();
			sqlStr.append(uec.getCodigo()).append(",");
		}
		sqlStr.append("0)");
		}
		sqlStr.append("		inner join unidadeensino as ue on ue.codigo = uec.unidadeensino ");
		sqlStr.append("		inner join matriculaperiodo as mp on mp.matricula = m.matricula and mp.ano = '").append(ano).append("' and mp.semestre = '").append(semestre).append("' and mp.situacaomatriculaperiodo in ('AT','PR') ");
		sqlStr.append("		inner join periodoletivo as pl on pl.codigo = mp.periodoletivomatricula ");
		sqlStr.append("		inner join gradecurricular as gc on gc.codigo = pl.gradecurricular ");
		sqlStr.append("		inner join matriculaperiodoturmadisciplina as mptd on mptd.matricula = m.matricula and mptd.ano = '").append(ano).append("' and mptd.semestre = '").append(semestre).append("' ");
		sqlStr.append(" 	INNER JOIN historico h ON mptd.codigo = h.matriculaperiodoturmadisciplina ");
		sqlStr.append("		left join turma as t on t.codigo = mptd.turma ");
		sqlStr.append("		left join turma as tt on tt.codigo = mptd.turmateorica ");
		sqlStr.append("		left join turma as tp on tp.codigo = mptd.turmapratica ");
		sqlStr.append("		left join turmaagrupada as ta on ta.turma = mptd.turma ");
		sqlStr.append("		left join turma as tm on tm.codigo = ta.turmaorigem and tm.turmaagrupada and tm.subturma is not true ");
		sqlStr.append("		inner join turma as tu on tu.codigo in (t.codigo, tt.codigo, tp.codigo, ta.turmaorigem) ");
		sqlStr.append("		inner join turmadisciplina as td on td.turma = tu.codigo ");
		sqlStr.append("		inner join disciplina as d on d.codigo = td.disciplina ");
		sqlStr.append("		left join vagaturma as vt on vt.turma = tu.codigo ");
		sqlStr.append("		left join vagaturmadisciplina as vtd on vtd.vagaturma = vt.codigo and vtd.disciplina = d.codigo ");
		sqlStr.append("		where td.disciplina = mptd.disciplina and (ta.turmaorigem is null or (ta.turmaorigem is not null and tm.codigo is not null ");
		sqlStr.append("     and not exists (select 1 from turma where turmaprincipal = tm.codigo))) "); // excluindo turma agrupada que tenha subturma
		sqlStr.append("     and (tu.codigo <> t.codigo or (tt.codigo is null and tp.codigo is null)) "); // excluindo turma se tiver turmateorica ou turmapratica
		}
		sqlStr.append(") as x ");
		if (unificarTurmas) {
			sqlStr.append("group by 1,2,3,4,5,6,10,11,12,16 ");
			sqlStr.append(") as y ");
			sqlStr.append("group by 1,3,11,12,13,14,15,16 ");
		} else {
			sqlStr.append("group by 1,2,3,10,11,12,16 ");
		}
		sqlStr.append("order by 1,2,3,4,5,6 ");
				
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	public static String getDesignIReportRelatorio(String tipoRelatorio) throws Exception {
		if (tipoRelatorio.equals("SI")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Sintetico" + ".jrxml");
		} else if (tipoRelatorio.equals("AN")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Analitico" + ".jrxml");
		} else {
			throw new Exception("Favor informe o tipo do relatório!");
		}
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ControleVagaRel");
	}
}