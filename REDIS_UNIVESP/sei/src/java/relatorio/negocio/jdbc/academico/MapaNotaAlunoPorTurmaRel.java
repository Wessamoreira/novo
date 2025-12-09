package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.OrdenadorVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.academico.HorarioTurmaDiaItem;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import relatorio.negocio.comuns.academico.MapaNotaAlunoPorTurmaAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaNotaAlunoPorTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.interfaces.academico.MapaNotaAlunoPorTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class MapaNotaAlunoPorTurmaRel extends SuperRelatorio implements MapaNotaAlunoPorTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	private enum TipoRegraTurmaAgrupada {
		NORMAL(" INNER JOIN turmaagrupada ta ON ta.turma = matriculaperiodoturmadisciplina.turma INNER JOIN turma turmaorigem ON turmaorigem.codigo = ta.turmaorigem "), 
		PRATICA(" INNER JOIN turmaagrupada ta ON ta.turma = MatriculaPeriodoTurmaDisciplina.turmaPratica INNER JOIN turma turmaorigem ON turmaorigem.codigo = ta.turmaorigem "),
		TEORICA(" INNER JOIN turmaagrupada ta ON ta.turma = MatriculaPeriodoTurmaDisciplina.turmaTeorica INNER JOIN turma turmaorigem ON turmaorigem.codigo = ta.turmaorigem "), 
		NENHUMA("");
		TipoRegraTurmaAgrupada(final String joinTurmaAgrupada) {
			this.joinTurmaAgrupada = joinTurmaAgrupada;
		}

		private final String joinTurmaAgrupada;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.MapaNotaAlunoPorTurmaRelInterfaceFacade
	 * #montarListaAlunos(java.lang.Integer)
	 */
	public List<MapaNotaAlunoPorTurmaAlunoRelVO> montarListaAlunos(TurmaVO turma, String ano, String semestre, String tipoNota, ConfiguracaoAcademicoVO conAcademicoVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoDisciplina, String tipoAluno, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, DisciplinaVO disciplinaVO, SalaLocalAulaVO salaLocalAulaVO, FuncionarioVO funcionarioVO, String periodicidade, String tipoLayout, UsuarioVO usuarioVO, List<String> listaNotas, String ordenacao) throws Exception {
		String filtroCursoIN = "";
		String filtroUnidadeIN = "";
		String filtroTurnoIN = "";
		if (!Uteis.isAtributoPreenchido(turma)) {
			filtroCursoIN = cursoVOs.stream().filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
			filtroUnidadeIN = unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
			filtroTurnoIN = turnoVOs.stream().filter(TurnoVO::getFiltrarTurnoVO).map(TurnoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
		}
		if (tipoNota == null || tipoNota.trim().isEmpty()) {
			tipoNota = "mediaFinal";
		}
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM ( ");
		String regraTurma = Uteis.isAtributoPreenchido(turma) ? " AND Turma.codigo = " + turma.getCodigo() : "";
		getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, conAcademicoVO, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplinaVO, salaLocalAulaVO, 
				funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenacao, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, regraTurma, TipoRegraTurmaAgrupada.NENHUMA);
		if (Uteis.isAtributoPreenchido(turma) && turma.getTurmaAgrupada()) {
			sqlStr.append(" UNION ");
			getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, conAcademicoVO, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplinaVO, salaLocalAulaVO, 
					funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenacao, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, " AND turmaorigem.codigo = " + turma.getCodigo(), TipoRegraTurmaAgrupada.NORMAL);
			sqlStr.append(" UNION ");
			getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, conAcademicoVO, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplinaVO, salaLocalAulaVO, 
					funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenacao, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, " AND turmaorigem.codigo = " + turma.getCodigo(), TipoRegraTurmaAgrupada.PRATICA);
			sqlStr.append(" UNION ");
			getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, conAcademicoVO, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplinaVO, salaLocalAulaVO, 
					funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenacao, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, " AND turmaorigem.codigo = " + turma.getCodigo(), TipoRegraTurmaAgrupada.TEORICA);
		}
		sqlStr.append(") AS t order by ");
		if(periodicidade.equals("IN")) {
			sqlStr.append(" \"aula.datainicio\" asc, ");
		}
		if (ordenacao.equals("disciplina")) {
			sqlStr.append(" ordemDisciplina, nomealuno ");			
		}else {
			sqlStr.append(" \"aula.datainicio\" asc, ordemDisciplina, nomealuno ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, conAcademicoVO, tipoLayout, listaNotas);
	}

	private void getSQLPadraoMontarListaAlunos(TurmaVO turma, String ano, String semestre, String tipoNota,	ConfiguracaoAcademicoVO conAcademicoVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,
			String tipoDisciplina, String tipoAluno, DisciplinaVO disciplinaVO, SalaLocalAulaVO salaLocalAulaVO,FuncionarioVO funcionarioVO, String periodicidade, String tipoLayout, List<String> listaNotas,
			String ordenacao, StringBuilder sqlStr, String filtroCursoIN, String filtroUnidadeIN, String filtroTurnoIN, String regraTurma, TipoRegraTurmaAgrupada tipoRegraTurmaAgrupada) {
		sqlStr.append("SELECT distinct m.matricula AS matricula, p.nome AS nomeAluno, horarioturma.datainicio as \"aula.datainicio\", d.codigo AS codDisciplina, d.nome as nomeDisciplina, h.").append(tipoNota).append(" AS nota, mfc.abreviaturaConceitoNota as conceito, h.freguencia AS falta, ");
		sqlStr.append(" turma.codigo as codTurma, turma.identificadorturma as turma, u.nome as unidadeensino, t.nome as turno, c.nome as curso, h.situacao as situacao, h.cargahorariadisciplina as cargahorariadisciplina ,");
		sqlStr.append(" horarioturma.local_nome||' - '||horarioturma.sala_nome as sala, horarioturma.professor_nome as professor, h.configuracaoAcademico, ");
		sqlStr.append(" dense_rank() over (order by m.unidadeensino, turma.codigo, d.codigo ) AS grupo ");
		if (tipoLayout.equals("MapaNotasBoletimAlunosPorTurmaRel")) {
			if(Uteis.isAtributoPreenchido(conAcademicoVO) && !listaNotas.isEmpty()) {				
				for(String nota: listaNotas) {
					sqlStr.append(", h." + nota + " ");
					sqlStr.append(", h." + nota + "Conceito ");
				}
			}else {
				for (int contador = 1; contador <= 40; contador++) {
					sqlStr.append(", h.nota" + contador + " ");
					sqlStr.append(", h.nota" + contador + "Conceito ");
				}
			}
		}
		sqlStr.append(", case when h.gradecurriculargrupooptativadisciplina is not null then 10000 else case when gdc.codigo is not null and gd.codigo is not null then gd.ordem else gradedisciplina.ordem end end as ordemDisciplina ,");
		sqlStr.append(" periodoletivo.descricao as \"periodoletivo.descricao\", periodoletivo.nomeCertificacao as \"periodoletivo.nomeCertificacao\", configuracaoacademico.quantidadeCasasDecimaisPermitirAposVirgula ");
		sqlStr.append("FROM historico h ");
		sqlStr.append("INNER JOIN matricula m ON h.matricula = m.matricula ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = m.matricula  and matriculaperiodoturmadisciplina.codigo = h.matriculaperiodoturmadisciplina  ");
		sqlStr.append("INNER JOIN matriculaperiodo mp ON mp.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		if (tipoAluno.equals("normal")) {
			sqlStr.append("INNER JOIN turma on turma.codigo = mp.turma and turma.codigo = matriculaperiodoturmadisciplina.turma ");
		} else {
			if (turma.getSubturma()) {
				if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
				} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
				} else {
					sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			} else {
				sqlStr.append("INNER JOIN Turma ON (turma.codigo = matriculaPeriodoTurmaDisciplina.turma and matriculaPeriodoTurmaDisciplina.turmapratica is null and matriculaPeriodoTurmaDisciplina.turmateorica is null) ");
				sqlStr.append(" or (turma.codigo = matriculaPeriodoTurmaDisciplina.turmapratica) ");
				sqlStr.append(" or (turma.codigo = matriculaPeriodoTurmaDisciplina.turmateorica) ");
			}
		}
		sqlStr.append(tipoRegraTurmaAgrupada.joinTurmaAgrupada);
		sqlStr.append("INNER JOIN unidadeensino u ON u.codigo = m.unidadeensino ");
		sqlStr.append("INNER JOIN turno t ON t.codigo = m.turno ");
		sqlStr.append("INNER JOIN curso c ON c.codigo = m.curso ");
		sqlStr.append("INNER JOIN pessoa p ON m.aluno = p.codigo ");
		sqlStr.append("INNER JOIN disciplina d ON h.disciplina = d.codigo ");
		sqlStr.append("left JOIN gradedisciplina ON h.gradedisciplina = gradedisciplina.codigo ");
		sqlStr.append("left JOIN gradedisciplinacomposta gdc ON h.gradedisciplinacomposta = gdc.codigo ");
		sqlStr.append("left JOIN gradedisciplina gd ON gd.codigo = gdc.gradedisciplina ");
		sqlStr.append("left join periodoauladisciplinaaluno(turma.codigo, d.codigo, c.periodicidade, matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre) as horarioturma on horarioturma.professor_codigo is not null ");
		
		sqlStr.append("LEFT JOIN configuracaoacademiconotaconceito mfc ON mfc.codigo = h." + tipoNota + "Conceito ");
		sqlStr.append("LEFT JOIN configuracaoacademico on configuracaoacademico.codigo = h.configuracaoacademico ");
		sqlStr.append("LEFT JOIN periodoletivo ON periodoletivo.codigo = turma.periodoletivo ");

		if (tipoAluno.equals("reposicao")) {
			sqlStr.append("WHERE matriculaperiodoturmadisciplina.turma <> mp.turma ");
		}else if (tipoAluno.equals("normal")) {
				sqlStr.append("WHERE matriculaperiodoturmadisciplina.turma = mp.turma ");
		} else {
			sqlStr.append("WHERE 1 = 1 ");
		}
		
		if(Uteis.isAtributoPreenchido(conAcademicoVO)){
			sqlStr.append(" and h.configuracaoAcademico =").append(conAcademicoVO.getCodigo());
		}
		
		if (filtroCursoIN.length() > 0) {
			sqlStr.append(" and c.codigo in (").append(filtroCursoIN).append(")");
		}

		if (filtroUnidadeIN.length() > 0) {
			sqlStr.append(" and u.codigo in (").append(filtroUnidadeIN).append(") ");
		}

		if (filtroTurnoIN.length() > 0) {
			sqlStr.append(" and t.codigo in (").append(filtroTurnoIN).append(") ");
		}

		if (Uteis.isAtributoPreenchido(ano) && !periodicidade.equals("IN")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.ano ='").append(ano).append("' ");
			sqlStr.append(" and h.anohistorico = '").append(ano).append("' ");
			sqlStr.append(" AND mp.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre) && periodicidade.equals("SE")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.semestre ='").append(semestre).append("' ");
			sqlStr.append(" and h.semestrehistorico = '").append(semestre).append("' ");
			sqlStr.append(" AND mp.semestre = '").append(semestre).append("' ");
		}
		
		if(filtroRelatorioAcademicoVO.getDataInicioPeriodoAula() != null) {
			sqlStr.append(" and horarioturma.datainicio >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
		}
		if(filtroRelatorioAcademicoVO.getDataTerminoPeriodoAula() != null) {
			sqlStr.append(" and horarioturma.datainicio <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTerminoPeriodoAula())).append("' ");
		}

		sqlStr.append(" and c.periodicidade = '").append(periodicidade).append("' ");

		if (Uteis.isAtributoPreenchido(disciplinaVO)) {
			sqlStr.append(" AND d.codigo = ").append(disciplinaVO.getCodigo());
		}
		sqlStr.append(regraTurma);
		if (periodicidade.equals("IN") && filtroRelatorioAcademicoVO.getDataInicio() != null && filtroRelatorioAcademicoVO.getDataTermino() != null) {
			sqlStr.append(" and " + realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "mp.data", false));
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		if (tipoDisciplina.equals("composta")) {
			sqlStr.append(" and (h.historicodisciplinacomposta or h.gradedisciplina is not null)");
		} else if (tipoDisciplina.equals("composicao")) {
			sqlStr.append(" and (h.historicodisciplinafazpartecomposicao or h.gradedisciplina is not null)");
			sqlStr.append(" and h.historicodisciplinacomposta = false");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(" and ((m.gradecurricularatual = h.matrizcurricular");
		sqlStr.append(" and (h.historicocursandoporcorrespondenciaapostransferencia is null or");
		sqlStr.append(" h.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and (h.transferenciamatrizcurricularmatricula IS NULL OR (h.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and h.disciplina not in (select disciplina from historico his");
		sqlStr.append(" where his.matricula = h.matricula");
		sqlStr.append(" and his.anohistorico = h.anohistorico");
		sqlStr.append(" and his.semestrehistorico = h.semestrehistorico");
		sqlStr.append(" and his.disciplina = h.disciplina");
		sqlStr.append(" and his.historicocursandoporcorrespondenciaapostransferencia");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = h.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and his.matrizcurricular != m.gradecurricularatual limit 1");
		sqlStr.append(" ) and h.historicoporequivalencia = false))) or (m.gradecurricularatual != h.matrizcurricular");
		sqlStr.append(" and h.historicocursandoporcorrespondenciaapostransferencia ");
		sqlStr.append(" and h.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and h.disciplina = (select disciplina from historico his");
		sqlStr.append(" where his.matricula = h.matricula ");
		sqlStr.append(" and his.anohistorico = h.anohistorico");
		sqlStr.append(" and his.semestrehistorico = h.semestrehistorico");
		sqlStr.append(" and his.disciplina = h.disciplina");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = h.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and (his.historicocursandoporcorrespondenciaapostransferencia is null or ");
		sqlStr.append(" his.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and his.matrizcurricular = m.gradecurricularatual limit 1");
		sqlStr.append(" )) ");
		sqlStr.append(" OR ( ");
		sqlStr.append(" m.gradecurricularatual != h.matrizcurricular ");
		sqlStr.append(" AND h.historicoequivalente =  true                 ");
		sqlStr.append(" AND exists ( ");
		sqlStr.append(" select hist.codigo from historico as hist ");
		sqlStr.append(" WHERE h.matricula = hist.matricula ");
		sqlStr.append(" and h.mapaequivalenciadisciplina = hist.mapaequivalenciadisciplina ");
		sqlStr.append(" and hist.historicoporequivalencia = true ");
		sqlStr.append(" and hist.numeroagrupamentoequivalenciadisciplina = h.numeroagrupamentoequivalenciadisciplina ");
		sqlStr.append(" and exists ( ");
		sqlStr.append(" SELECT his.disciplina FROM historico his ");
		sqlStr.append(" WHERE his.matricula = hist.matricula ");
		sqlStr.append(" AND his.anohistorico = hist.anohistorico ");
		sqlStr.append(" AND his.semestrehistorico = hist.semestrehistorico ");
		sqlStr.append(" AND his.disciplina = hist.disciplina ");
		sqlStr.append(" AND his.transferenciamatrizcurricularmatricula = hist.transferenciamatrizcurricularmatricula ");
		sqlStr.append(" AND (his.historicocursandoporcorrespondenciaapostransferencia IS NULL OR his.historicocursandoporcorrespondenciaapostransferencia = FALSE ) ");
		sqlStr.append(" AND his.matrizcurricular = m.gradecurricularatual ");
		sqlStr.append(" LIMIT 1)) ");
		sqlStr.append(" ) ");
		// Essa condição OR é responsável por trazer as disciplinas que fazem
		// parte da composição após realizar transferencia
		// de matriz curricular. Isso porque após a transferência o aluno irá
		// cursar a disciplina na grade antiga mesmo estando
		// já realizada a transferência para nova grade. O sistema então irá
		// trazer o histórico da matriz antiga caso não possua a mesma
		// disciplina na nova grade.
		sqlStr.append(" or (h.matrizcurricular = mp.gradecurricular ");
		sqlStr.append(" and m.gradecurricularatual != h.matrizcurricular ");
		sqlStr.append(" and h.historicodisciplinafazpartecomposicao ");
		sqlStr.append(" and h.disciplina not in (");
		sqlStr.append(" select his.disciplina from historico his ");
		sqlStr.append(" where his.matriculaperiodo = h.matriculaperiodo ");
		sqlStr.append(" and his.disciplina = h.disciplina ");
		sqlStr.append(" and m.gradecurricularatual = his.matrizcurricular))	");
		if(filtroRelatorioAcademicoVO.getTrazerAlunosComTransferenciaMatriz()){
			sqlStr.append(" or (m.gradecurricularatual != h.matrizcurricular ");
			sqlStr.append(" AND (h.historicocursandoporcorrespondenciaapostransferencia IS NULL ");
			sqlStr.append(" OR h.historicocursandoporcorrespondenciaapostransferencia = FALSE) ");
			sqlStr.append(" AND h.transferenciamatrizcurricularmatricula IS NULL ");
			sqlStr.append(" and not exists ( ");
			sqlStr.append(" 				select his.codigo from historico his ");
			sqlStr.append(" 				inner join matriculaperiodoturmadisciplina mptd on mptd.codigo = his.matriculaperiodoturmadisciplina ");
			sqlStr.append("					where his.matricula = m.matricula ");
			sqlStr.append(" 				and his.matrizcurricular = m.gradecurricularatual ");
			sqlStr.append(" 				and his.disciplina = h.disciplina ");
			sqlStr.append(" 				and his.anohistorico = h.anohistorico ");
			sqlStr.append(" 				and his.semestrehistorico = h.semestrehistorico ");
			sqlStr.append(" 				and (((his.historicocursandoporcorrespondenciaapostransferencia IS NULL ");
			sqlStr.append("                 OR his.historicocursandoporcorrespondenciaapostransferencia = FALSE) and his.transferenciamatrizcurricularmatricula is null) ");
			sqlStr.append(" 				OR (his.historicocursandoporcorrespondenciaapostransferencia IS NOT NULL ");
			sqlStr.append("                 and his.historicocursandoporcorrespondenciaapostransferencia = TRUE and his.transferenciamatrizcurricularmatricula is not null) "); 
			sqlStr.append(" 				or (his.matriculaperiodoturmadisciplina is not null and mptd.turma != matriculaperiodoturmadisciplina.turma))");
			sqlStr.append(" 			)");
			sqlStr.append(" )");
		}
		sqlStr.append(") ");
		if (Uteis.isAtributoPreenchido(funcionarioVO)) {
			sqlStr.append(" and horarioturma.professor_codigo = ").append(funcionarioVO.getPessoa().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(salaLocalAulaVO)) {
			sqlStr.append(" and horarioturma.sala_codigo = ").append(salaLocalAulaVO.getCodigo());
		}
	}

	private List<MapaNotaAlunoPorTurmaAlunoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, ConfiguracaoAcademicoVO conAcademicoVO, String layout, List<String> listaNotas) throws Exception {
		List<MapaNotaAlunoPorTurmaAlunoRelVO> vetResultado = new ArrayList<MapaNotaAlunoPorTurmaAlunoRelVO>(0);
		int i = 0;
		Map<String, Integer> hashOrdemMatricula = new HashMap<String, Integer>(0);
		Map<Integer, List<MapaNotaAlunoPorTurmaAlunoRelVO>> mapGrupoUnidadeTurmaDisciplinaSalaVOs = new HashMap<Integer, List<MapaNotaAlunoPorTurmaAlunoRelVO>>(0);
		Map<String, Integer> mapOrdemDisciplina = new HashMap<String, Integer>();
		while (tabelaResultado.next()) {
			Integer codigoConfAcademico = tabelaResultado.getInt("configuracaoAcademico");
			String hashOrdem = tabelaResultado.getString("unidadeensino")+tabelaResultado.getString("turma")+tabelaResultado.getString("nomeDisciplina");
			Integer ordemDisciplina = tabelaResultado.getInt("ordemDisciplina");
			if(mapOrdemDisciplina.containsKey(hashOrdem)) {
				ordemDisciplina = mapOrdemDisciplina.get(hashOrdem);
			}else {
				mapOrdemDisciplina.put(hashOrdem, ordemDisciplina);
			}
			List<MapaNotaAlunoPorTurmaAlunoRelVO> objs = montarDados(tabelaResultado, mapGrupoUnidadeTurmaDisciplinaSalaVOs, getAplicacaoControle().carregarDadosConfiguracaoAcademica(codigoConfAcademico), layout, listaNotas, ordemDisciplina);
			for(MapaNotaAlunoPorTurmaAlunoRelVO obj: objs) {
				if (!hashOrdemMatricula.containsKey(obj.getMatricula())) {
					obj.setOrdemLinha(i);
					hashOrdemMatricula.put(objs.get(0).getMatricula(), obj.getOrdemLinha());
					i++;
				} else {
					obj.setOrdemLinha(hashOrdemMatricula.get(obj.getMatricula()));
				}
			}
			vetResultado.addAll(objs);
		}
		realizarCalculoMedia(mapGrupoUnidadeTurmaDisciplinaSalaVOs);
		return vetResultado;
	}

	public void realizarCalculoMedia(Map<Integer, List<MapaNotaAlunoPorTurmaAlunoRelVO>> mapGrupoUnidadeTurmaDisciplinaSalaVOs) {

		for (List<MapaNotaAlunoPorTurmaAlunoRelVO> lista : mapGrupoUnidadeTurmaDisciplinaSalaVOs.values()) {
			Double notaParcial = 0.0;
			for (MapaNotaAlunoPorTurmaAlunoRelVO mapaNotaAlunoPorTurmaAlunoRelVO : lista) {
				notaParcial = notaParcial + mapaNotaAlunoPorTurmaAlunoRelVO.getNotaDouble();
			}
			for (MapaNotaAlunoPorTurmaAlunoRelVO mapaNotaAlunoPorTurmaAlunoRelVO : lista) {
				mapaNotaAlunoPorTurmaAlunoRelVO.setMediaParcial(Uteis.truncar((notaParcial / lista.size()), 2));
			}

		}
	}

	private List<MapaNotaAlunoPorTurmaAlunoRelVO> montarDados(SqlRowSet dadosSQL, Map<Integer, List<MapaNotaAlunoPorTurmaAlunoRelVO>> mapGrupoUnidadeTurmaDisciplinaSalaVOs, ConfiguracaoAcademicoVO conAcademicoVO, String layout, List<String> listaNotas, Integer ordemDisciplina) throws Exception {
		MapaNotaAlunoPorTurmaAlunoRelVO obj = new MapaNotaAlunoPorTurmaAlunoRelVO();
		obj.setCodigoDisciplina(dadosSQL.getInt("codDisciplina"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setNome(dadosSQL.getString("nomeAluno"));
		obj.setConceito(dadosSQL.getString("conceito"));
		obj.setFalta(dadosSQL.getDouble("falta"));

		if (dadosSQL.getObject("nota") != null) {
			obj.setNotaDouble(dadosSQL.getDouble("nota"));
			obj.setNota(Uteis.isAtributoPreenchido(dadosSQL.getString("conceito")) ? dadosSQL.getString("conceito") : Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("nota"), conAcademicoVO.getQuantidadeCasasDecimaisPermitirAposVirgula()));
		} else {
			obj.setNotaDouble(0.0);
			obj.setNota("--");
		}
		obj.setNomeDisciplina(dadosSQL.getString("nomeDisciplina"));
		obj.setTurma(dadosSQL.getString("turma"));
		obj.setSala(dadosSQL.getString("sala"));
		obj.setUnidadeensino(dadosSQL.getString("unidadeensino"));
		obj.setTurno(dadosSQL.getString("turno"));
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoApresentar(SituacaoHistorico.getDescricao(obj.getSituacao()));
		obj.setCargaHoraria(dadosSQL.getInt("cargahorariadisciplina"));
		
		obj.setOrdemColuna(ordemDisciplina);
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("periodoletivo.nomeCertificacao"))) {
			obj.setPeriodoLetivo(dadosSQL.getString("periodoletivo.nomeCertificacao"));	
		}else {
			obj.setPeriodoLetivo(dadosSQL.getString("periodoletivo.descricao"));
		}
		
		List<MapaNotaAlunoPorTurmaAlunoRelVO> mapaNotaAlunoPorTurmaAlunoRelVOs= new ArrayList<MapaNotaAlunoPorTurmaAlunoRelVO>(0);
		
		if(layout.equals("MapaNotasBoletimAlunosPorTurmaRel") && Uteis.isAtributoPreenchido(conAcademicoVO)) {
			if(Uteis.isAtributoPreenchido(listaNotas)) {
				for (String nota: listaNotas) {
					Integer contador = Integer.valueOf(nota.replace("nota", ""));
					if((boolean) UtilReflexao.invocarMetodoGet(conAcademicoVO, "utilizarNota"+ contador)) {
						MapaNotaAlunoPorTurmaAlunoRelVO objClone = obj.clone();
						objClone.setNotaDouble(dadosSQL.getDouble("nota"+contador));
						objClone.setConceito(dadosSQL.getString("nota"+contador+"Conceito"));
						objClone.setNota(Uteis.isAtributoPreenchido(dadosSQL.getString("nota"+contador+"Conceito")) ? dadosSQL.getString("nota"+contador+"Conceito") : Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("nota"+contador),conAcademicoVO.getQuantidadeCasasDecimaisPermitirAposVirgula()));		
						objClone.setOrdemColunaNota(contador);
						ConfiguracaoAcademicaNotaVO conf = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(conAcademicoVO, "configuracaoAcademicaNota"+contador+"VO");
						objClone.setTituloNota( (String) UtilReflexao.invocarMetodoGet(conf, "titulo") );
						adicionarMapaNotaAlunoPorTurmaAlunoRelVO(objClone, dadosSQL.getInt("grupo"), mapGrupoUnidadeTurmaDisciplinaSalaVOs);
						mapaNotaAlunoPorTurmaAlunoRelVOs.add(objClone);
					}
				}
			}else {
				for (int contador = 1; contador <= 40; contador++) {
					if((boolean) UtilReflexao.invocarMetodoGet(conAcademicoVO, "utilizarNota"+ contador)) {
						MapaNotaAlunoPorTurmaAlunoRelVO objClone = obj.clone();
						objClone.setNotaDouble(dadosSQL.getDouble("nota"+contador));
						objClone.setConceito(dadosSQL.getString("nota"+contador+"Conceito"));
						objClone.setNota(Uteis.isAtributoPreenchido(dadosSQL.getString("nota"+contador+"Conceito")) ? dadosSQL.getString("nota"+contador+"Conceito") : Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("nota"+contador), conAcademicoVO.getQuantidadeCasasDecimaisPermitirAposVirgula()));		
						objClone.setOrdemColunaNota(contador);
						ConfiguracaoAcademicaNotaVO conf = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(conAcademicoVO, "configuracaoAcademicaNota"+contador+"VO");
						objClone.setTituloNota( (String) UtilReflexao.invocarMetodoGet(conf, "titulo") );
						//objClone.setTituloNota( (String) UtilReflexao.invocarMetodoGet(conAcademicoVO, "tituloNota"+ contador) );
						adicionarMapaNotaAlunoPorTurmaAlunoRelVO(objClone, dadosSQL.getInt("grupo"), mapGrupoUnidadeTurmaDisciplinaSalaVOs);
						mapaNotaAlunoPorTurmaAlunoRelVOs.add(objClone);
					}
				}
			}
		}else {
			adicionarMapaNotaAlunoPorTurmaAlunoRelVO(obj, dadosSQL.getInt("grupo"), mapGrupoUnidadeTurmaDisciplinaSalaVOs);
			mapaNotaAlunoPorTurmaAlunoRelVOs.add(obj);
		}

		return mapaNotaAlunoPorTurmaAlunoRelVOs;
	}
	
	public void adicionarMapaNotaAlunoPorTurmaAlunoRelVO(MapaNotaAlunoPorTurmaAlunoRelVO obj, Integer agrupador, Map<Integer, List<MapaNotaAlunoPorTurmaAlunoRelVO>> mapGrupoUnidadeTurmaDisciplinaSalaVOs) {
		if (!mapGrupoUnidadeTurmaDisciplinaSalaVOs.containsKey(agrupador)) {
			List<MapaNotaAlunoPorTurmaAlunoRelVO> listaAlunoPorTurmaAlunoRelVOs = new ArrayList<MapaNotaAlunoPorTurmaAlunoRelVO>(0);
			listaAlunoPorTurmaAlunoRelVOs.add(obj);
			mapGrupoUnidadeTurmaDisciplinaSalaVOs.put(agrupador, listaAlunoPorTurmaAlunoRelVOs);
		} else {
			List<MapaNotaAlunoPorTurmaAlunoRelVO> lista = mapGrupoUnidadeTurmaDisciplinaSalaVOs.get(agrupador);
			lista.add(obj);
			mapGrupoUnidadeTurmaDisciplinaSalaVOs.put(agrupador, lista);
		}
	}

	public List<MapaNotaAlunoPorTurmaRelVO> executarConsultaMapaNotaTurmaPosGraduacao(TurmaVO turma, String ano, String semestre, String periodicidade, String tipoNota, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoDisciplina, String tipoAluno , String ordenador, String tipoLayout,  DisciplinaVO disciplina, ConfiguracaoAcademicoVO configuracaoAcademica, FuncionarioVO funcionarioVO, List<UnidadeEnsinoVO> unidadesEnsinoVOs, List<CursoVO> cursosVOs, List<TurnoVO> turnosVOs, SalaLocalAulaVO salaLocalAulaVO, List<String> listaNotas) throws Exception {
		String filtroCursoIN = null;
		String filtroUnidadeIN = null;
		String filtroTurnoIN = null;
		
		if (!Uteis.isAtributoPreenchido(turma)) {
			filtroCursoIN = cursosVOs.stream().filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
			filtroUnidadeIN = unidadesEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
			filtroTurnoIN = turnosVOs.stream().filter(TurnoVO::getFiltrarTurnoVO).map(TurnoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
		}		

		
		if (tipoNota == null || tipoNota.trim().isEmpty()) {
			tipoNota = "mediaFinal";
		}
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM ( ");
		String regraTurma = Uteis.isAtributoPreenchido(turma) ? " AND Turma.codigo = " + turma.getCodigo() : "";
		getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, configuracaoAcademica, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplina, salaLocalAulaVO, 
				funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenador, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, regraTurma, TipoRegraTurmaAgrupada.NENHUMA);
		if (Uteis.isAtributoPreenchido(turma) && turma.getTurmaAgrupada()) {
			sqlStr.append(" UNION ");
			getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, configuracaoAcademica, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplina, salaLocalAulaVO, 
					funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenador, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, " AND turmaorigem.codigo = " + turma.getCodigo(), TipoRegraTurmaAgrupada.NORMAL);
			sqlStr.append(" UNION ");
			getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, configuracaoAcademica, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplina, salaLocalAulaVO, 
					funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenador, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, " AND turmaorigem.codigo = " + turma.getCodigo(), TipoRegraTurmaAgrupada.PRATICA);
			sqlStr.append(" UNION ");
			getSQLPadraoMontarListaAlunos(turma, ano, semestre, tipoNota, configuracaoAcademica, filtroRelatorioAcademicoVO, tipoDisciplina, tipoAluno, disciplina, salaLocalAulaVO, 
					funcionarioVO, periodicidade, tipoLayout, listaNotas, ordenador, sqlStr, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, " AND turmaorigem.codigo = " + turma.getCodigo(), TipoRegraTurmaAgrupada.TEORICA);
		}
		sqlStr.append(") AS t order by ");
		if(periodicidade.equals("IN")) {
			sqlStr.append(" \"aula.datainicio\" asc, ");
		}
		if (ordenador.equals("disciplina")) {
			sqlStr.append(" ordemDisciplina, nomealuno ");			
		}else {
			sqlStr.append(" \"aula.datainicio\" asc, turma, ordemDisciplina, nomealuno ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaTurmaPosGraduacao(tabelaResultado, turma, disciplina);
	}

	private List<MapaNotaAlunoPorTurmaRelVO> montarDadosConsultaTurmaPosGraduacao(SqlRowSet tabelaResultado, TurmaVO turma,  DisciplinaVO disciplina) throws Exception {

		Integer ordenador = 0;
		List<MapaNotaAlunoPorTurmaRelVO> mapaNotaAlunoPorTurmaRelVOs = new ArrayList<MapaNotaAlunoPorTurmaRelVO>(0);
		Map<String, Integer> mapDisciplinaOrdem = new HashMap<String, Integer>(0);
		MapaNotaAlunoPorTurmaRelVO  mapaNotaAlunoPorTurmaRelVO =  null;
		while (tabelaResultado.next()) {
			if(mapaNotaAlunoPorTurmaRelVO == null || !mapaNotaAlunoPorTurmaRelVOs.stream().anyMatch(m -> m.getTurma().equals(tabelaResultado.getString("turma")))) {
				mapaNotaAlunoPorTurmaRelVO =  new MapaNotaAlunoPorTurmaRelVO();
				mapaNotaAlunoPorTurmaRelVO.setTurma(tabelaResultado.getString("turma"));
				mapaNotaAlunoPorTurmaRelVO.setNomeUnidadeEnsino(tabelaResultado.getString("unidadeEnsino"));
				mapaNotaAlunoPorTurmaRelVO.setCurso(tabelaResultado.getString("curso"));
				mapaNotaAlunoPorTurmaRelVO.setTurno(tabelaResultado.getString("turno"));
					
			
				mapaNotaAlunoPorTurmaRelVO.setPeriodoLetivo(tabelaResultado.getString("periodoLetivo.descricao"));
				mapaNotaAlunoPorTurmaRelVOs.add(mapaNotaAlunoPorTurmaRelVO);
				mapDisciplinaOrdem.clear();
				
			}
			if(mapDisciplinaOrdem.isEmpty() || !mapDisciplinaOrdem.containsKey(tabelaResultado.getString("nomedisciplina"))){
				mapDisciplinaOrdem.put(tabelaResultado.getString("nomedisciplina"), ordenador++);				
			}
			CrosstabVO crosstabVO = new CrosstabVO();
			crosstabVO.setOrdemColuna(mapDisciplinaOrdem.get(tabelaResultado.getString("nomedisciplina")));			
			crosstabVO.setLabelLinha(tabelaResultado.getString("nomealuno"));
			crosstabVO.setLabelColuna(Uteis.getData(tabelaResultado.getDate("aula.datainicio")));
			crosstabVO.setLabelColuna2(tabelaResultado.getString("nomedisciplina"));
			if (tabelaResultado.getObject("nota") == null) {
				crosstabVO.setValorString("--");				
			} else {
				crosstabVO.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(tabelaResultado.getDouble("nota"), tabelaResultado.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
				
			}
			if (tabelaResultado.getString("conceito") != null && !tabelaResultado.getString("conceito").trim().isEmpty()) {
				crosstabVO.setValorString(tabelaResultado.getString("conceito"));
			} 			
			if (tabelaResultado.getObject("falta") == null || (tabelaResultado.getDouble("falta") == 0 && tabelaResultado.getString("situacao").equals("CS"))) {
				crosstabVO.setLabelLinha2("--");										
			} else {
				crosstabVO.setLabelLinha2(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(tabelaResultado.getDouble("falta"), 2));				
			}	
			
			
			
			mapaNotaAlunoPorTurmaRelVO.getCrosstabVOs().add(crosstabVO);			
		}
		return mapaNotaAlunoPorTurmaRelVOs;
	}

	public static String getDesignIReportRelatorioMapaNotaAlunoPorTurma() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeMapaNotaAlunoPorTurmaPosGraduacaoRel() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorioMapaNotaAlunoPorTurma() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidadeMapaNotaAlunoPorTurmaPosGraduacaoRel() {
		return ("MapaNotaAlunoPorTurmaPosGraduacaoRel");
	}

	@Override
	public List<MapaNotaAlunoPorTurmaRelVO> criarObjetoLayout3e4(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, FuncionarioVO funcionarioVO, SalaLocalAulaVO salaLocalAulaVO, String tipoNota, String tipoAluno, String periodicidade, String tipoDisciplina, OrientacaoPaginaEnum orientacaoPaginaEnum, UsuarioVO usuarioVO) throws Exception {
		List<MapaNotaAlunoPorTurmaRelVO> listaMapaNotaAlunoPorTurmaRelVOs = executarConsultaParametrizada(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, cursoVOs, turnoVOs, turmaVO, disciplinaVO, ano, semestre, funcionarioVO, salaLocalAulaVO, tipoNota, tipoAluno, periodicidade, tipoDisciplina, usuarioVO);
		return montarDadosMapaNotasAlunosDisciplina(listaMapaNotaAlunoPorTurmaRelVOs, orientacaoPaginaEnum);
	}

	private List<MapaNotaAlunoPorTurmaRelVO> montarDadosMapaNotasAlunosDisciplina(List<MapaNotaAlunoPorTurmaRelVO> listaMapaNotaAlunoPorTurmaRelVOs, OrientacaoPaginaEnum orientacaoPaginaEnum) throws Exception {
		for (MapaNotaAlunoPorTurmaRelVO mapaNotaAlunoPorTurmaRelVO : listaMapaNotaAlunoPorTurmaRelVOs) {
			int ordemLinha = 1;
			for (HistoricoVO historicoVO : mapaNotaAlunoPorTurmaRelVO.getHistoricoVOs()) {
				int ordemColuna = 1;
				mapaNotaAlunoPorTurmaRelVO.getCrosstabVOs().add(montarDadosFrequenciaHistoricoCrosstab(historicoVO, ordemLinha, ordemColuna));
				ordemColuna++;
				mapaNotaAlunoPorTurmaRelVO.getCrosstabVOs().add(montarDadosSituacaoHistoricoCrosstab(historicoVO, ordemLinha, ordemColuna));
				List<CrosstabVO> crosstabVOs = montarDadosMapaNotasAlunosDisciplinaCrosstab(historicoVO, ordemLinha, ordemColuna);
				mapaNotaAlunoPorTurmaRelVO.getCrosstabVOs().addAll(crosstabVOs);
				mapaNotaAlunoPorTurmaRelVO.getCrosstabVOs().addAll(preencherColunasFinalPagina(historicoVO, ordemLinha, ordemColuna + crosstabVOs.size(), orientacaoPaginaEnum));
				ordemLinha++;
			}
		}
		return listaMapaNotaAlunoPorTurmaRelVOs;
	}

	public CrosstabVO montarDadosFrequenciaHistoricoCrosstab(HistoricoVO historicoVO, int ordemLinha, int ordemColuna) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setLabelColuna("Freq.(%)");
		crosstab.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(historicoVO.getFreguencia(),2));
		return crosstab;
	}

	public CrosstabVO montarDadosSituacaoHistoricoCrosstab(HistoricoVO historicoVO, int ordemLinha, int ordemColuna) throws Exception {
		CrosstabVO crosstab = new CrosstabVO();
		crosstab.setOrdemLinha(ordemLinha);
		crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstab.setOrdemColuna(ordemColuna);
		crosstab.setLabelColuna("Situação");
		crosstab.setValorString(historicoVO.getSituacao());
		return crosstab;

	}

	public List<CrosstabVO> montarDadosMapaNotasAlunosDisciplinaCrosstab(HistoricoVO historicoVO, int ordemLinha, int ordemColuna) throws Exception {
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		int cont = 1;
		for (int x = 1; x <= 40; x++) {
			boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "utilizarNota" + x);
			boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "apresentarNota" + x);
			boolean notaMediaFinal = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "nota" + x + "MediaFinal");
			ConfiguracaoAcademicoNotaConceitoVO notaConceito = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "nota" + x + "Conceito");
			if ((utilizarNota && apresentarNota) || (utilizarNota && notaMediaFinal)) {
				CrosstabVO crosstabVO = new CrosstabVO();
				crosstabVO.setOrdemLinha(ordemLinha);
				crosstabVO.setLabelLinha(historicoVO.getMatricula().getMatricula());
				crosstabVO.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
				crosstabVO.setOrdemColuna(cont + ordemColuna);
				crosstabVO.setLabelColuna((String) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "tituloNotaApresentar" + x));
				if (Uteis.isAtributoPreenchido(notaConceito)) {
					crosstabVO.setValorString(notaConceito.getAbreviaturaConceitoNota());
				} else {
					if (UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x) != null) {
						crosstabVO.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula((Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
					} else {
						crosstabVO.setValorString("--");
					}
				}
				crosstabVOs.add(crosstabVO);
				cont++;
			}
		}
		CrosstabVO crosstabVO = new CrosstabVO();
		crosstabVO.setOrdemLinha(ordemLinha);
		crosstabVO.setLabelLinha(historicoVO.getMatricula().getMatricula());
		crosstabVO.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
		crosstabVO.setOrdemColuna(cont + ordemColuna);
		crosstabVO.setLabelColuna("Média Final");
		if(Uteis.isAtributoPreenchido(historicoVO.getMediaFinalConceito())){
			crosstabVO.setValorString(historicoVO.getMediaFinalConceito().getAbreviaturaConceitoNota());
		}else if(Uteis.isAtributoPreenchido(historicoVO.getNotaFinalConceito())){
			crosstabVO.setValorString(historicoVO.getNotaFinalConceito());
		}else if(Uteis.isAtributoPreenchido(historicoVO.getMediaFinal())){
			crosstabVO.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(historicoVO.getMediaFinal(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
		}else{
			crosstabVO.setValorString("--");
		}
		crosstabVOs.add(crosstabVO);
		return crosstabVOs;
	}

	public List<CrosstabVO> preencherColunasFinalPagina(HistoricoVO historicoVO, int ordemLinha, int ordemColuna, OrientacaoPaginaEnum orientacaoPaginaEnum) {
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		int tam = orientacaoPaginaEnum.equals(OrientacaoPaginaEnum.PAISAGEM) ? 26 : 10;
		if ((tam - (ordemColuna % tam)) > 0) {
			for (int x = 1; x < (tam - (ordemColuna % tam)); x++) {
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setLabelLinha(historicoVO.getMatricula().getMatricula());
				crosstab.setLabelLinha2(historicoVO.getMatricula().getAluno().getNome());
				crosstab.setOrdemColuna(ordemColuna + x);
				crosstab.setOrdemLinha(ordemLinha);
				crosstab.setLabelColuna(" ");
				crosstabVOs.add(crosstab);

			}
		}
		return crosstabVOs;
	}

	public List<MapaNotaAlunoPorTurmaRelVO> executarConsultaParametrizada(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, FuncionarioVO funcionarioVO, SalaLocalAulaVO salaLocalAulaVO, String tipoNota, String tipoAluno, String periodicidade, String tipoDisciplina, UsuarioVO usuarioVO) throws Exception {
		String filtroCursoIN = "";
		String filtroUnidadeIN = "";
		String filtroTurnoIN = "";
		if (!Uteis.isAtributoPreenchido(turmaVO)) {
			filtroCursoIN = cursoVOs.stream().filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
			filtroUnidadeIN = unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
			filtroTurnoIN = turnoVOs.stream().filter(TurnoVO::getFiltrarTurnoVO).map(TurnoVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", "));
		}
		StringBuilder sb = new StringBuilder("SELECT * FROM ( ");
		String regraTurma = Uteis.isAtributoPreenchido(turmaVO) ? " AND Turma.codigo = " + turmaVO.getCodigo() : "";
		getSQLPadraoConsultaParametrizada(filtroRelatorioAcademicoVO, turmaVO, disciplinaVO, ano, semestre, funcionarioVO, salaLocalAulaVO,
				tipoNota, tipoAluno, periodicidade, tipoDisciplina, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, sb, regraTurma, TipoRegraTurmaAgrupada.NENHUMA);
		if (Uteis.isAtributoPreenchido(turmaVO) && turmaVO.getTurmaAgrupada()) {
			sb.append(" UNION ");
			getSQLPadraoConsultaParametrizada(filtroRelatorioAcademicoVO, turmaVO, disciplinaVO, ano, semestre, funcionarioVO, salaLocalAulaVO,
					tipoNota, tipoAluno, periodicidade, tipoDisciplina, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, sb, " AND turmaorigem.codigo = " + turmaVO.getCodigo(), TipoRegraTurmaAgrupada.NORMAL);
			sb.append(" UNION ");
			getSQLPadraoConsultaParametrizada(filtroRelatorioAcademicoVO, turmaVO, disciplinaVO, ano, semestre, funcionarioVO, salaLocalAulaVO,
					tipoNota, tipoAluno, periodicidade, tipoDisciplina, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, sb, " AND turmaorigem.codigo = " + turmaVO.getCodigo(), TipoRegraTurmaAgrupada.PRATICA);
			sb.append(" UNION ");
			getSQLPadraoConsultaParametrizada(filtroRelatorioAcademicoVO, turmaVO, disciplinaVO, ano, semestre, funcionarioVO, salaLocalAulaVO,
					tipoNota, tipoAluno, periodicidade, tipoDisciplina, filtroCursoIN, filtroUnidadeIN, filtroTurnoIN, sb, " AND turmaorigem.codigo = " + turmaVO.getCodigo(), TipoRegraTurmaAgrupada.TEORICA);
		}
		sb.append(") AS t order by ");
		if(periodicidade.equals("IN")) {
			sb.append("  \"aula.datainicio\" asc, ");
		}
		sb.append(" nomeUnidadeEnsino, nomeCurso, identificadorTurma, nomeDisciplina, nomePessoa ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosListaLayout3(tabelaResultado, usuarioVO);
	}

	private void getSQLPadraoConsultaParametrizada(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, FuncionarioVO funcionarioVO,
			SalaLocalAulaVO salaLocalAulaVO, String tipoNota, String tipoAluno, String periodicidade, String tipoDisciplina, String filtroCursoIN, String filtroUnidadeIN,
			String filtroTurnoIN, StringBuilder sb, String regraTurma, TipoRegraTurmaAgrupada tipoRegraTurmaAgrupada) {
		sb.append("select historico.codigo, horarioturma.datainicio as \"aula.datainicio\" , historico.matricula, historico.freguencia, historico.situacao, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS nomePessoa, historico.mediaFinal, ");
		sb.append(" historico.configuracaoAcademico, historico.mediaFinalConceito, ");
		for(int x = 1; x<=40;x++) {
			sb.append(" historico.nota").append(x).append(", ");
			sb.append(" historico.nota").append(x).append("conceito, ");
		}
		sb.append(" unidadeEnsino.codigo AS codUnidadeEnsino, unidadeEnsino.nome AS nomeUnidadeEnsino, curso.codigo AS codCurso, curso.nome AS nomeCurso, ");
		sb.append(" turma.codigo AS codTurma, turma.identificadorTurma AS identificadorTurma, disciplina.codigo AS codDisciplina, disciplina.nome AS nomeDisciplina, ");
		sb.append(" turno.codigo AS codTurno, turno.nome AS nomeTurno, ");
		sb.append(" (select distinct gradeCurricular.nome from gradeCurricular where gradeCurricular.codigo = turma.gradeCurricular) AS nomeMatrizCurricular, ");
		sb.append(" horarioturma.professor_nome as professor,  horarioturma.local_nome||' - '||horarioturma.sala_nome as sala,  ");
		sb.append(" Periodoletivo.descricao as \"Periodoletivo.descricao\", Periodoletivo.nomeCertificacao as \"Periodoletivo.nomeCertificacao\" ");
		
		sb.append(" from historico ");
		sb.append(" inner join matricula on matricula.matricula = historico.matricula ");
		sb.append(" inner JOIN matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.codigo = historico.matriculaPeriodoTurmaDisciplina ");
		sb.append(" INNER JOIN matriculaPeriodo on matricula.matricula = matriculaPeriodo.matricula and matriculaPeriodo.codigo = historico.matriculaPeriodo ");
		if (tipoAluno.equals("normal")) {
			sb.append("INNER JOIN turma on turma.codigo = matriculaPeriodo.turma and turma.codigo = matriculaperiodoturmadisciplina.turma ");
		} else {
			if (turmaVO.getSubturma()) {
				if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					sb.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
				} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sb.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
				} else {
					sb.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			} else {
				sb.append("INNER JOIN Turma ON (turma.codigo = matriculaPeriodoTurmaDisciplina.turma and matriculaPeriodoTurmaDisciplina.turmapratica is null and matriculaPeriodoTurmaDisciplina.turmateorica is null) ");
				sb.append(" or (turma.codigo = matriculaPeriodoTurmaDisciplina.turmapratica) ");
				sb.append(" or (turma.codigo = matriculaPeriodoTurmaDisciplina.turmateorica) ");
			}
		}
		sb.append(tipoRegraTurmaAgrupada.joinTurmaAgrupada);
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sb.append(" INNER JOIN disciplina on disciplina.codigo = historico.disciplina ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
		sb.append(" left join periodoauladisciplinaaluno(turma.codigo, matriculaPeriodoTurmaDisciplina.disciplina, curso.periodicidade, matriculaPeriodoTurmaDisciplina.ano, matriculaPeriodoTurmaDisciplina.semestre) as horarioturma on horarioturma.professor_codigo is not null ");		
		
		sb.append(" where 1=1 ");
		
		if(filtroRelatorioAcademicoVO.getDataInicioPeriodoAula() != null) {
			sb.append(" and historico.matriculaperiodoturmadisciplina is not null and horarioturma.datainicio >= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' ");
		}
		if(filtroRelatorioAcademicoVO.getDataTerminoPeriodoAula() != null) {
			sb.append(" and horarioturma.datainicio <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTerminoPeriodoAula())).append("' ");
		}

		if (tipoNota == null || tipoNota.trim().isEmpty()) {
			tipoNota = "mediaFinal";
		}
		
		if (tipoAluno.equals("reposicao")) {
			sb.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma ");
		}else if (tipoAluno.equals("normal")) {
			sb.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma ");
		} 

		if (Uteis.isAtributoPreenchido(ano) && !periodicidade.equals("IN")) {
			sb.append(" and matriculaperiodoturmadisciplina.ano ='").append(ano).append("' ");
			sb.append(" and historico.anohistorico = '").append(ano).append("' ");
			sb.append(" AND matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre) && periodicidade.equals("SE")) {
			sb.append(" and matriculaperiodoturmadisciplina.semestre ='").append(semestre).append("' ");
			sb.append(" and historico.semestrehistorico = '").append(semestre).append("' ");
			sb.append(" AND matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}

		sb.append(" and curso.periodicidade = '").append(periodicidade).append("' ");

		if (Uteis.isAtributoPreenchido(disciplinaVO)) {
			sb.append(" AND disciplina.codigo = ").append(disciplinaVO.getCodigo());
		}

		if (filtroCursoIN.length() > 0) {
			sb.append(" and curso.codigo in (").append(filtroCursoIN).append(")");
		}

		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and unidadeEnsino.codigo in (").append(filtroUnidadeIN).append(") ");
		}

		if (filtroTurnoIN.length() > 0) {
			sb.append(" and turno.codigo in (").append(filtroTurnoIN).append(") ");
		}

		sb.append(regraTurma);
		if (periodicidade.equals("IN") && filtroRelatorioAcademicoVO.getDataInicio() != null && filtroRelatorioAcademicoVO.getDataTermino() != null) {
			sb.append(" and " + realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "matriculaPeriodo.data", false));
		}
		sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaPeriodo"));
		sb.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaPeriodo"));
		if (tipoDisciplina.equals("composta")) {
			sb.append(" and (historico.historicodisciplinacomposta or historico.gradedisciplina is not null)");
		} else if (tipoDisciplina.equals("composicao")) {
			sb.append(" and (historico.historicodisciplinafazpartecomposicao or historico.gradedisciplina is not null)");
			sb.append(" and historico.historicodisciplinacomposta = false");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sb.append(" and ((matricula.gradecurricularatual = historico.matrizcurricular");
		sb.append(" and (historico.historicocursandoporcorrespondenciaapostransferencia is null or");
		sb.append(" historico.historicocursandoporcorrespondenciaapostransferencia = false)");
		sb.append(" and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sb.append(" and historico.disciplina not in (select disciplina from historico his");
		sb.append(" where his.matricula = historico.matricula");
		sb.append(" and his.anohistorico = historico.anohistorico");
		sb.append(" and his.semestrehistorico = historico.semestrehistorico");
		sb.append(" and his.disciplina = historico.disciplina");
		sb.append(" and his.historicocursandoporcorrespondenciaapostransferencia");
		sb.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sb.append(" and his.matrizcurricular != matricula.gradecurricularatual limit 1");
		sb.append(" ) and historico.historicoporequivalencia = false))) or (matricula.gradecurricularatual != historico.matrizcurricular");
		sb.append(" and historico.historicocursandoporcorrespondenciaapostransferencia ");
		sb.append(" and historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sb.append(" and historico.disciplina = (select disciplina from historico his");
		sb.append(" where his.matricula = historico.matricula ");
		sb.append(" and his.anohistorico = historico.anohistorico");
		sb.append(" and his.semestrehistorico = historico.semestrehistorico");
		sb.append(" and his.disciplina = historico.disciplina");
		sb.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sb.append(" and (his.historicocursandoporcorrespondenciaapostransferencia is null or ");
		sb.append(" his.historicocursandoporcorrespondenciaapostransferencia = false)");
		sb.append(" and his.matrizcurricular = matricula.gradecurricularatual limit 1");
		sb.append(" )) ");
		sb.append(" OR ( ");
		sb.append(" matricula.gradecurricularatual != historico.matrizcurricular ");
		sb.append(" AND historico.historicoequivalente =  true                 ");
		sb.append(" AND exists ( ");
		sb.append(" select hist.codigo from historico as hist ");
		sb.append(" WHERE historico.matricula = hist.matricula ");
		sb.append(" and historico.mapaequivalenciadisciplina = hist.mapaequivalenciadisciplina ");
		sb.append(" and hist.historicoporequivalencia = true ");
		sb.append(" and hist.numeroagrupamentoequivalenciadisciplina = historico.numeroagrupamentoequivalenciadisciplina ");
		sb.append(" and exists ( ");
		sb.append(" SELECT his.disciplina FROM historico his ");
		sb.append(" WHERE his.matricula = hist.matricula ");
		sb.append(" AND his.anohistorico = hist.anohistorico ");
		sb.append(" AND his.semestrehistorico = hist.semestrehistorico ");
		sb.append(" AND his.disciplina = hist.disciplina ");
		sb.append(" AND his.transferenciamatrizcurricularmatricula = hist.transferenciamatrizcurricularmatricula ");
		sb.append(" AND (his.historicocursandoporcorrespondenciaapostransferencia IS NULL OR his.historicocursandoporcorrespondenciaapostransferencia = FALSE ) ");
		sb.append(" AND his.matrizcurricular = matricula.gradecurricularatual ");
		sb.append(" LIMIT 1)) ");
		sb.append(" ) ");
		// Essa condição OR é responsável por trazer as disciplinas que fazem
		// parte da composição após realizar transferencia
		// de matriz curricular. Isso porque após a transferência o aluno irá
		// cursar a disciplina na grade antiga mesmo estando
		// já realizada a transferência para nova grade. O sistema então irá
		// trazer o histórico da matriz antiga caso não possua a mesma
		// disciplina na nova grade.
		sb.append(" or (historico.matrizcurricular = matriculaPeriodo.gradecurricular ");
		sb.append(" and matricula.gradecurricularatual != historico.matrizcurricular ");
		sb.append(" and historico.historicodisciplinafazpartecomposicao ");
		sb.append(" and historico.disciplina not in (");
		sb.append(" select his.disciplina from historico his ");
		sb.append(" where his.matriculaperiodo = historico.matriculaperiodo ");
		sb.append(" and his.disciplina = historico.disciplina ");
		sb.append(" and matricula.gradecurricularatual = his.matrizcurricular))	");
		if(filtroRelatorioAcademicoVO.getTrazerAlunosComTransferenciaMatriz()){
			sb.append(" or (matricula.gradecurricularatual != historico.matrizcurricular ");
			sb.append(" AND (historico.historicocursandoporcorrespondenciaapostransferencia IS NULL ");
			sb.append(" OR historico.historicocursandoporcorrespondenciaapostransferencia = FALSE) ");
			sb.append(" AND historico.transferenciamatrizcurricularmatricula IS NULL ");
			sb.append(" and not exists ( ");
			sb.append(" 				select his.codigo from historico his ");
			sb.append(" 				left join matriculaperiodoturmadisciplina mptd on mptd.codigo = his.matriculaperiodoturmadisciplina ");
			sb.append("					where his.matricula = matricula.matricula ");
			sb.append(" 				and his.matrizcurricular = matricula.gradecurricularatual ");
			sb.append(" 				and his.disciplina = historico.disciplina ");
			sb.append(" 				and his.anohistorico = historico.anohistorico ");
			sb.append(" 				and his.semestrehistorico = historico.semestrehistorico ");
			sb.append(" 				and (((his.historicocursandoporcorrespondenciaapostransferencia IS NULL ");
			sb.append("                 OR his.historicocursandoporcorrespondenciaapostransferencia = FALSE)  and his.transferenciamatrizcurricularmatricula is null) ");
			sb.append(" 				OR (his.historicocursandoporcorrespondenciaapostransferencia IS NOT NULL ");
			sb.append("                 and his.historicocursandoporcorrespondenciaapostransferencia = TRUE and his.transferenciamatrizcurricularmatricula is not null) ");
			sb.append(" 				or (his.matriculaperiodoturmadisciplina is not null and mptd.turma != matriculaperiodoturmadisciplina.turma))");
			sb.append(" 			)");
			sb.append(" )");
		}
		sb.append(") ");

		if (Uteis.isAtributoPreenchido(funcionarioVO)) {
			sb.append(" and horarioturma.professor_codigo = ").append(funcionarioVO.getPessoa().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(salaLocalAulaVO)) {
			sb.append(" and horarioturma.sala_codigo = ").append(salaLocalAulaVO.getCodigo());
		}
	}

	public List<MapaNotaAlunoPorTurmaRelVO> montarDadosListaLayout3(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		Map<String, MapaNotaAlunoPorTurmaRelVO> mapTurmaDisciplinaVOs = new HashMap<String, MapaNotaAlunoPorTurmaRelVO>(0);
		List<MapaNotaAlunoPorTurmaRelVO> mapaNotaAlunoPorTurmaRelVOs = new ArrayList<MapaNotaAlunoPorTurmaRelVO>(0);
		
		while (dadosSQL.next()) {
			if (mapTurmaDisciplinaVOs.containsKey(dadosSQL.getInt("codTurma") + "-" +dadosSQL.getInt("codDisciplina"))) {
				MapaNotaAlunoPorTurmaRelVO obj = mapTurmaDisciplinaVOs.get(dadosSQL.getInt("codTurma") + "-" +dadosSQL.getInt("codDisciplina"));
				HistoricoVO historicoVO = new HistoricoVO();
				montarDadosLayout3(historicoVO, dadosSQL, usuarioVO);
				obj.getHistoricoVOs().add(historicoVO);
			} else {
				MapaNotaAlunoPorTurmaRelVO obj = new MapaNotaAlunoPorTurmaRelVO();
				obj.setNomeUnidadeEnsino(dadosSQL.getString("nomeUnidadeEnsino"));
				obj.setCurso(dadosSQL.getString("nomeCurso"));
				obj.setTurma(dadosSQL.getString("identificadorTurma"));
				obj.setDisciplina(dadosSQL.getString("nomeDisciplina"));
				obj.setTurno(dadosSQL.getString("nomeTurno"));
				obj.setSala(dadosSQL.getString("sala"));
				obj.setDataModulo(Uteis.getData(dadosSQL.getDate("aula.datainicio")));
				obj.setProfessor(dadosSQL.getString("professor"));
				obj.setNomeMatrizCurricular(dadosSQL.getString("nomeMatrizCurricular"));
				if(Uteis.isAtributoPreenchido(dadosSQL.getString("Periodoletivo.nomeCertificacao"))) {
					obj.setPeriodoLetivo(dadosSQL.getString("Periodoletivo.nomeCertificacao"));	
				}else {
					obj.setPeriodoLetivo(dadosSQL.getString("Periodoletivo.descricao"));
				}
				
				
				HistoricoVO historicoVO = new HistoricoVO();						
				montarDadosLayout3(historicoVO, dadosSQL, usuarioVO);
				obj.getHistoricoVOs().add(historicoVO);
				mapTurmaDisciplinaVOs.put(dadosSQL.getInt("codTurma") + "-" +dadosSQL.getInt("codDisciplina"), obj);
				mapaNotaAlunoPorTurmaRelVOs.add(obj);
			}
		}
		return mapaNotaAlunoPorTurmaRelVOs;
	}

	public void montarDadosLayout3(HistoricoVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoAcademico"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.setFreguencia(dadosSQL.getDouble("freguencia"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setMediaFinal(dadosSQL.getDouble("mediaFinal"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomePessoa"));

		if (dadosSQL.getObject("nota1") == null) {
			obj.setNota1((Double) dadosSQL.getObject("nota1"));
		} else {
			obj.setNota1(dadosSQL.getDouble("nota1"));
		}
		if (dadosSQL.getObject("nota2") == null) {
			obj.setNota2((Double) dadosSQL.getObject("nota2"));
		} else {
			obj.setNota2(dadosSQL.getDouble("nota2"));
		}
		if (dadosSQL.getObject("nota3") == null) {
			obj.setNota3((Double) dadosSQL.getObject("nota3"));
		} else {
			obj.setNota3(dadosSQL.getDouble("nota3"));
		}
		if (dadosSQL.getObject("nota4") == null) {
			obj.setNota4((Double) dadosSQL.getObject("nota4"));
		} else {
			obj.setNota4(dadosSQL.getDouble("nota4"));
		}
		if (dadosSQL.getObject("nota5") == null) {
			obj.setNota5((Double) dadosSQL.getObject("nota5"));
		} else {
			obj.setNota5(dadosSQL.getDouble("nota5"));
		}
		if (dadosSQL.getObject("nota6") == null) {
			obj.setNota6((Double) dadosSQL.getObject("nota6"));
		} else {
			obj.setNota6(dadosSQL.getDouble("nota6"));
		}
		if (dadosSQL.getObject("nota7") == null) {
			obj.setNota7((Double) dadosSQL.getObject("nota7"));
		} else {
			obj.setNota7(dadosSQL.getDouble("nota7"));
		}
		if (dadosSQL.getObject("nota8") == null) {
			obj.setNota8((Double) dadosSQL.getObject("nota8"));
		} else {
			obj.setNota8(dadosSQL.getDouble("nota8"));
		}
		if (dadosSQL.getObject("nota9") == null) {
			obj.setNota9((Double) dadosSQL.getObject("nota9"));
		} else {
			obj.setNota9(dadosSQL.getDouble("nota9"));
		}
		if (dadosSQL.getObject("nota10") == null) {
			obj.setNota10((Double) dadosSQL.getObject("nota10"));
		} else {
			obj.setNota10(dadosSQL.getDouble("nota10"));
		}
		if (dadosSQL.getObject("nota11") == null) {
			obj.setNota11((Double) dadosSQL.getObject("nota11"));
		} else {
			obj.setNota11(dadosSQL.getDouble("nota11"));
		}
		if (dadosSQL.getObject("nota12") == null) {
			obj.setNota12((Double) dadosSQL.getObject("nota12"));
		} else {
			obj.setNota12(dadosSQL.getDouble("nota12"));
		}
		if (dadosSQL.getObject("nota13") == null) {
			obj.setNota13((Double) dadosSQL.getObject("nota13"));
		} else {
			obj.setNota13(dadosSQL.getDouble("nota13"));
		}
		if (dadosSQL.getObject("nota14") == null) {
			obj.setNota14((Double) dadosSQL.getObject("nota14"));
		} else {
			obj.setNota14(dadosSQL.getDouble("nota14"));
		}

		if (dadosSQL.getObject("nota15") == null) {
			obj.setNota15((Double) dadosSQL.getObject("nota15"));
		} else {
			obj.setNota15(dadosSQL.getDouble("nota15"));
		}

		if (dadosSQL.getObject("nota16") == null) {
			obj.setNota16((Double) dadosSQL.getObject("nota16"));
		} else {
			obj.setNota16(dadosSQL.getDouble("nota16"));
		}

		if (dadosSQL.getObject("nota17") == null) {
			obj.setNota17((Double) dadosSQL.getObject("nota17"));
		} else {
			obj.setNota17(dadosSQL.getDouble("nota17"));
		}

		if (dadosSQL.getObject("nota18") == null) {
			obj.setNota18((Double) dadosSQL.getObject("nota18"));
		} else {
			obj.setNota18(dadosSQL.getDouble("nota18"));
		}

		if (dadosSQL.getObject("nota19") == null) {
			obj.setNota19((Double) dadosSQL.getObject("nota19"));
		} else {
			obj.setNota19(dadosSQL.getDouble("nota19"));
		}
		if (dadosSQL.getObject("nota20") == null) {
			obj.setNota20((Double) dadosSQL.getObject("nota20"));
		} else {
			obj.setNota20(dadosSQL.getDouble("nota20"));
		}
		if (dadosSQL.getObject("nota21") == null) {
			obj.setNota21((Double) dadosSQL.getObject("nota21"));
		} else {
			obj.setNota21(dadosSQL.getDouble("nota21"));
		}
		if (dadosSQL.getObject("nota22") == null) {
			obj.setNota22((Double) dadosSQL.getObject("nota22"));
		} else {
			obj.setNota22(dadosSQL.getDouble("nota22"));
		}
		if (dadosSQL.getObject("nota23") == null) {
			obj.setNota23((Double) dadosSQL.getObject("nota23"));
		} else {
			obj.setNota23(dadosSQL.getDouble("nota23"));
		}
		if (dadosSQL.getObject("nota24") == null) {
			obj.setNota24((Double) dadosSQL.getObject("nota24"));
		} else {
			obj.setNota24(dadosSQL.getDouble("nota24"));
		}
		if (dadosSQL.getObject("nota25") == null) {
			obj.setNota25((Double) dadosSQL.getObject("nota25"));
		} else {
			obj.setNota25(dadosSQL.getDouble("nota25"));
		}
		if (dadosSQL.getObject("nota26") == null) {
			obj.setNota26((Double) dadosSQL.getObject("nota26"));
		} else {
			obj.setNota26(dadosSQL.getDouble("nota26"));
		}
		if (dadosSQL.getObject("nota27") == null) {
			obj.setNota27((Double) dadosSQL.getObject("nota27"));
		} else {
			obj.setNota27(dadosSQL.getDouble("nota27"));
		}
		if (dadosSQL.getObject("nota28") == null) {
			obj.setNota28((Double) dadosSQL.getObject("nota28"));
		} else {
			obj.setNota28(dadosSQL.getDouble("nota28"));
		}
		if (dadosSQL.getObject("nota29") == null) {
			obj.setNota29((Double) dadosSQL.getObject("nota29"));
		} else {
			obj.setNota29(dadosSQL.getDouble("nota29"));
		}
		if (dadosSQL.getObject("nota30") == null) {
			obj.setNota30((Double) dadosSQL.getObject("nota30"));
		} else {
			obj.setNota30(dadosSQL.getDouble("nota30"));
		}
		if (dadosSQL.getObject("nota31") == null) {
			obj.setNota31((Double) dadosSQL.getObject("nota31"));
		} else {
			obj.setNota31(dadosSQL.getDouble("nota31"));
		}
		if (dadosSQL.getObject("nota32") == null) {
			obj.setNota32((Double) dadosSQL.getObject("nota32"));
		} else {
			obj.setNota32(dadosSQL.getDouble("nota32"));
		}
		if (dadosSQL.getObject("nota33") == null) {
			obj.setNota33((Double) dadosSQL.getObject("nota33"));
		} else {
			obj.setNota33(dadosSQL.getDouble("nota33"));
		}
		if (dadosSQL.getObject("nota34") == null) {
			obj.setNota34((Double) dadosSQL.getObject("nota34"));
		} else {
			obj.setNota34(dadosSQL.getDouble("nota34"));
		}
		if (dadosSQL.getObject("nota35") == null) {
			obj.setNota35((Double) dadosSQL.getObject("nota35"));
		} else {
			obj.setNota35(dadosSQL.getDouble("nota35"));
		}
		if (dadosSQL.getObject("nota36") == null) {
			obj.setNota36((Double) dadosSQL.getObject("nota36"));
		} else {
			obj.setNota36(dadosSQL.getDouble("nota36"));
		}
		if (dadosSQL.getObject("nota37") == null) {
			obj.setNota37((Double) dadosSQL.getObject("nota37"));
		} else {
			obj.setNota37(dadosSQL.getDouble("nota37"));
		}
		if (dadosSQL.getObject("nota38") == null) {
			obj.setNota38((Double) dadosSQL.getObject("nota38"));
		} else {
			obj.setNota38(dadosSQL.getDouble("nota38"));
		}
		if (dadosSQL.getObject("nota39") == null) {
			obj.setNota39((Double) dadosSQL.getObject("nota39"));
		} else {
			obj.setNota39(dadosSQL.getDouble("nota39"));
		}
		if (dadosSQL.getObject("nota40") == null) {
			obj.setNota40((Double) dadosSQL.getObject("nota40"));
		} else {
			obj.setNota40(dadosSQL.getDouble("nota40"));
		}
		obj.getMediaFinalConceito().setCodigo(dadosSQL.getInt("mediaFinalConceito"));
		obj.getNota1Conceito().setCodigo(dadosSQL.getInt("nota1Conceito"));
		obj.getNota2Conceito().setCodigo(dadosSQL.getInt("nota2Conceito"));
		obj.getNota3Conceito().setCodigo(dadosSQL.getInt("nota3Conceito"));
		obj.getNota4Conceito().setCodigo(dadosSQL.getInt("nota4Conceito"));
		obj.getNota5Conceito().setCodigo(dadosSQL.getInt("nota5Conceito"));
		obj.getNota6Conceito().setCodigo(dadosSQL.getInt("nota6Conceito"));
		obj.getNota7Conceito().setCodigo(dadosSQL.getInt("nota7Conceito"));
		obj.getNota8Conceito().setCodigo(dadosSQL.getInt("nota8Conceito"));
		obj.getNota9Conceito().setCodigo(dadosSQL.getInt("nota9Conceito"));
		obj.getNota10Conceito().setCodigo(dadosSQL.getInt("nota10Conceito"));
		obj.getNota11Conceito().setCodigo(dadosSQL.getInt("nota11Conceito"));
		obj.getNota12Conceito().setCodigo(dadosSQL.getInt("nota12Conceito"));
		obj.getNota13Conceito().setCodigo(dadosSQL.getInt("nota13Conceito"));
		obj.getNota14Conceito().setCodigo(dadosSQL.getInt("nota14Conceito"));
		obj.getNota15Conceito().setCodigo(dadosSQL.getInt("nota15Conceito"));
		obj.getNota16Conceito().setCodigo(dadosSQL.getInt("nota16Conceito"));
		obj.getNota17Conceito().setCodigo(dadosSQL.getInt("nota17Conceito"));
		obj.getNota18Conceito().setCodigo(dadosSQL.getInt("nota18Conceito"));
		obj.getNota19Conceito().setCodigo(dadosSQL.getInt("nota19Conceito"));
		obj.getNota20Conceito().setCodigo(dadosSQL.getInt("nota20Conceito"));
		obj.getNota21Conceito().setCodigo(dadosSQL.getInt("nota21Conceito"));
		obj.getNota22Conceito().setCodigo(dadosSQL.getInt("nota22Conceito"));
		obj.getNota23Conceito().setCodigo(dadosSQL.getInt("nota23Conceito"));
		obj.getNota24Conceito().setCodigo(dadosSQL.getInt("nota24Conceito"));
		obj.getNota25Conceito().setCodigo(dadosSQL.getInt("nota25Conceito"));
		obj.getNota26Conceito().setCodigo(dadosSQL.getInt("nota26Conceito"));
		obj.getNota27Conceito().setCodigo(dadosSQL.getInt("nota27Conceito"));
		obj.getNota28Conceito().setCodigo(dadosSQL.getInt("nota28Conceito"));
		obj.getNota29Conceito().setCodigo(dadosSQL.getInt("nota29Conceito"));
		obj.getNota30Conceito().setCodigo(dadosSQL.getInt("nota30Conceito"));
		obj.getNota31Conceito().setCodigo(dadosSQL.getInt("nota31Conceito"));
		obj.getNota32Conceito().setCodigo(dadosSQL.getInt("nota32Conceito"));
		obj.getNota33Conceito().setCodigo(dadosSQL.getInt("nota33Conceito"));
		obj.getNota34Conceito().setCodigo(dadosSQL.getInt("nota34Conceito"));
		obj.getNota35Conceito().setCodigo(dadosSQL.getInt("nota35Conceito"));
		obj.getNota36Conceito().setCodigo(dadosSQL.getInt("nota36Conceito"));
		obj.getNota37Conceito().setCodigo(dadosSQL.getInt("nota37Conceito"));
		obj.getNota38Conceito().setCodigo(dadosSQL.getInt("nota38Conceito"));
		obj.getNota39Conceito().setCodigo(dadosSQL.getInt("nota39Conceito"));
		obj.getNota40Conceito().setCodigo(dadosSQL.getInt("nota40Conceito"));
		getFacadeFactory().getHistoricoFacade().carregarDadosNotaConceitoHistorico(obj);				
		
	}

}
