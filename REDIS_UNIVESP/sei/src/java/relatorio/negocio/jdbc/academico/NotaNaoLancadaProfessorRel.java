package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.NotaNaoLancadaProfessorRelVO;
import relatorio.negocio.interfaces.academico.NotaNaoLancadaProfessorRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class NotaNaoLancadaProfessorRel extends SuperRelatorio implements NotaNaoLancadaProfessorRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public NotaNaoLancadaProfessorRel() throws Exception {
	}

	public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, ConfiguracaoAcademicoVO configuracaoAcademica, List<CursoVO> cursoVOs, TurmaVO turmaVO,
			String periodicidade, String ano, String semestre, Date periodoAulaInicial, Date periodoAulaFinal, String filtrarPor) throws Exception {
		boolean possuiUnidadeEnsinoSelecionada = false;
		for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
    		if (ue.getFiltrarUnidadeEnsino()) {
    			possuiUnidadeEnsinoSelecionada = true;
    			break;
    		}
    	}
		if (!possuiUnidadeEnsinoSelecionada) {
			throw new ConsistirException("Por Favor informe pelo menos uma unidade de ensino para a geração do relatório.");
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
		} else if (periodicidade.equals("IN")) {
			if (periodoAulaInicial == null && periodoAulaFinal == null) {
				throw new ConsistirException("Por Favor informe o Filtro por Período de Aula do relatório.");
			}
		}
		if (filtrarPor.equals("curso")) {
			boolean possuiCursoSelecionado = false;
			for (CursoVO cursoVO : cursoVOs) {
				if (cursoVO.getFiltrarCursoVO()) {
					possuiCursoSelecionado = true;
					break;
				}
			}
			if (!possuiCursoSelecionado) {
				throw new ConsistirException("Por Favor informe pelo menos um curso para a geração do relatório.");
			}
		}
		if (filtrarPor.equals("turma")) {
			if (turmaVO == null || turmaVO.getCodigo() == 0) {
				throw new ConsistirException("Por Favor informe uma turma para a geração do relatório.");
			}
		}
		if (configuracaoAcademica.getCodigo() == 0) {
			throw new ConsistirException("Por Favor informe uma configuração acadêmica para a geração do relatório.");
		}
		if (!(configuracaoAcademica.isFiltrarNota1() || configuracaoAcademica.isFiltrarNota2() || configuracaoAcademica.isFiltrarNota3() ||
				configuracaoAcademica.isFiltrarNota4() || configuracaoAcademica.isFiltrarNota5() || configuracaoAcademica.isFiltrarNota6() ||
				configuracaoAcademica.isFiltrarNota7() || configuracaoAcademica.isFiltrarNota8() || configuracaoAcademica.isFiltrarNota9() ||
				configuracaoAcademica.isFiltrarNota10() || configuracaoAcademica.isFiltrarNota11() || configuracaoAcademica.isFiltrarNota12() ||
				configuracaoAcademica.isFiltrarNota13() || configuracaoAcademica.isFiltrarNota14() || configuracaoAcademica.isFiltrarNota15() ||
				configuracaoAcademica.isFiltrarNota16() || configuracaoAcademica.isFiltrarNota17() || configuracaoAcademica.isFiltrarNota18() ||
				configuracaoAcademica.isFiltrarNota19() || configuracaoAcademica.isFiltrarNota20() || configuracaoAcademica.isFiltrarNota21() ||
				configuracaoAcademica.isFiltrarNota22() || configuracaoAcademica.isFiltrarNota23() || configuracaoAcademica.isFiltrarNota24() ||
				configuracaoAcademica.isFiltrarNota25() || configuracaoAcademica.isFiltrarNota26() || configuracaoAcademica.isFiltrarNota27() ||
				configuracaoAcademica.isFiltrarNota28() || configuracaoAcademica.isFiltrarNota29() || configuracaoAcademica.isFiltrarNota30() ||
				configuracaoAcademica.isFiltrarNota31() || configuracaoAcademica.isFiltrarNota32() || configuracaoAcademica.isFiltrarNota33() ||
				configuracaoAcademica.isFiltrarNota34() || configuracaoAcademica.isFiltrarNota35() || configuracaoAcademica.isFiltrarNota36() ||
				configuracaoAcademica.isFiltrarNota37() || configuracaoAcademica.isFiltrarNota38() || configuracaoAcademica.isFiltrarNota39() ||
				configuracaoAcademica.isFiltrarNota40())) {
			throw new ConsistirException("Por Favor selecione pelo menos uma nota para a geração do relatório.");
		}
	}

	public List<NotaNaoLancadaProfessorRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String periodicidade, String ano, String semestre, Date periodoMatriculaInicial,
			Date periodoMatriculaFinal,	String campoFiltroPor, TurmaVO turma, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, DisciplinaVO disciplina, FuncionarioVO professor,
			ConfiguracaoAcademicoVO configuracaoAcademica, String filtrarNotas, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String layout, String ordenacao, Date periodoAulaInicial, Date periodoAulaFinal) throws Exception {
		List<NotaNaoLancadaProfessorRelVO> lista = new ArrayList<NotaNaoLancadaProfessorRelVO>(0);
		SqlRowSet dadosSQL;
		dadosSQL = executarConsultaParametrizada(unidadeEnsinoVOs, periodicidade, ano, semestre, periodoMatriculaInicial, periodoMatriculaFinal, campoFiltroPor, turma,
				cursoVOs, turnoVOs,	disciplina, professor, configuracaoAcademica, filtrarNotas, filtroRelatorioAcademicoVO, layout, ordenacao, periodoAulaInicial, periodoAulaFinal);
		while (dadosSQL != null && dadosSQL.next()) {
			lista.add(montarDados(dadosSQL));
		}
		return lista;
	}

	public NotaNaoLancadaProfessorRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
		NotaNaoLancadaProfessorRelVO notaNaoLancadaProfessorRelVO = new NotaNaoLancadaProfessorRelVO();
		notaNaoLancadaProfessorRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino"));
		notaNaoLancadaProfessorRelVO.setIdentificadorTurma(dadosSQL.getString("turma"));
		notaNaoLancadaProfessorRelVO.setNomeCurso(dadosSQL.getString("curso"));
		notaNaoLancadaProfessorRelVO.setNomeProfessor(dadosSQL.getString("professor"));
		notaNaoLancadaProfessorRelVO.setAbreviaturaDisciplina(dadosSQL.getString("disciplina"));
		notaNaoLancadaProfessorRelVO.setQtdeAlunosTurmaDisciplina(dadosSQL.getInt("qtdeAluno"));
		notaNaoLancadaProfessorRelVO.setQtdeNotasNaoLancadas(dadosSQL.getInt("qtdefalta"));
		return notaNaoLancadaProfessorRelVO;
	}
	
	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String periodicidade, String ano, String semestre, Date periodoMatriculaInicial,
			Date periodoMatriculaFinal,	String campoFiltroPor, TurmaVO turma, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, DisciplinaVO disciplina, FuncionarioVO professor,
			ConfiguracaoAcademicoVO configuracaoAcademica, String filtrarNotas, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String layout, String ordenacao, Date periodoAulaInicial, Date periodoAulaFinal) throws Exception {
		String condicaoNotas = getCondicaoNotas(filtrarNotas, configuracaoAcademica);
		String condicaoWhereFiltros = getCondicaoWhereFiltros(unidadeEnsinoVOs, periodicidade, ano, semestre, periodoMatriculaInicial, periodoMatriculaFinal, campoFiltroPor, turma, cursoVOs, turnoVOs, disciplina, configuracaoAcademica, filtroRelatorioAcademicoVO, periodoAulaInicial, periodoAulaFinal);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select mptd.unidadeensino, mptd.curso, turma.identificadorturma as turma, ");
		sqlStr.append(" disciplina.nome as disciplina, pessoa.nome as professor, qtdealuno, qtdefalta ");
		sqlStr.append(" from ( ");
		sqlStr.append(" 	select unidadeensino.nome as unidadeensino,  curso.nome as curso, mptd.turma, mptd.disciplina, false as turmaagrupada, false as disciplinaOnline, 0 as professor, ");
		sqlStr.append(" 	count(distinct mptd.codigo) as qtdealuno, ");
		sqlStr.append(" 	sum(case when ").append(condicaoNotas).append(" then 1 else 0 end) as qtdefalta ");
		sqlStr.append(" 	from  matriculaperiodoturmadisciplina mptd ");
		sqlStr.append(" 	inner join matricula on matricula.matricula = mptd.matricula ");
		if (periodicidade.equals("IN") ||
				filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() ||
				filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getConcluido() ||
				filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getFormado() ||
				filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado() ||
				filtroRelatorioAcademicoVO.getJubilado() ) {
			sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = mptd.matriculaperiodo ");
		}
		sqlStr.append(" 	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" 	inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" 	inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" 	inner join horarioturma on horarioturma.turma = mptd.turma and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");
		sqlStr.append(" 	where historico.situacao not in ('AA', 'CC', 'CH','IS', 'AB') and mptd.turmateorica is null and mptd.turmapratica is null ");
		sqlStr.append(condicaoWhereFiltros);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" 	group by unidadeensino.nome,  curso.nome, mptd.turma, mptd.disciplina ");
		sqlStr.append(" 	union all ");
		sqlStr.append(" 	select unidadeensino.nome as unidadeensino,  curso.nome as curso, turmaagrupada.turmaorigem as turma, mptd.disciplina, true as turmaagrupada, false as disciplinaOnline, 0 as professor, ");
		sqlStr.append(" 	count(distinct mptd.codigo) as qtdealuno, ");
		sqlStr.append(" 	sum(case when ").append(condicaoNotas).append(" then 1 else 0 end) as qtdefalta ");
		sqlStr.append(" 	from  matriculaperiodoturmadisciplina mptd ");
		sqlStr.append(" 	inner join matricula on matricula.matricula = mptd.matricula ");
		if (periodicidade.equals("IN") ||
				filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() ||
				filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getConcluido() ||
				filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getFormado() ||
				filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado() || 
				filtroRelatorioAcademicoVO.getJubilado()) {
			sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = mptd.matriculaperiodo ");
		}
		sqlStr.append(" 	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" 	inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" 	inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turma = mptd.turma ");
		sqlStr.append(" 	inner join horarioturma on horarioturma.turma = turmaagrupada.turmaorigem and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");
		sqlStr.append(" 	where historico.situacao not in ('AA', 'CC', 'CH','IS', 'AB') and mptd.turmateorica is null and mptd.turmapratica is null ");
		sqlStr.append(condicaoWhereFiltros);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" 	group by unidadeensino.nome,  curso.nome, turmaagrupada.turmaorigem, mptd.disciplina ");
		sqlStr.append(" 	union all ");
		sqlStr.append(" 	select unidadeensino.nome as unidadeensino,  curso.nome as curso, mptd.turmateorica, mptd.disciplina, false as turmaagrupada, false as disciplinaOnline, 0 as professor, ");
		sqlStr.append(" 	count(distinct mptd.codigo) as qtdealuno, ");
		sqlStr.append(" 	sum(case when ").append(condicaoNotas).append(" then 1 else 0 end) as qtdefalta ");
		sqlStr.append(" 	from  matriculaperiodoturmadisciplina mptd ");
		sqlStr.append(" 	inner join matricula on matricula.matricula = mptd.matricula ");
		if (periodicidade.equals("IN") ||
				filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() ||
				filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getConcluido() ||
				filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getFormado() ||
				filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado() || 
				filtroRelatorioAcademicoVO.getJubilado()) {
			sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = mptd.matriculaperiodo ");
		}
		sqlStr.append(" 	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" 	inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" 	inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" 	inner join horarioturma on horarioturma.turma = mptd.turmateorica and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");
		sqlStr.append(" 	where historico.situacao not in ('AA', 'CC', 'CH','IS', 'AB') ");
		sqlStr.append(condicaoWhereFiltros);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" 	group by unidadeensino.nome,  curso.nome, mptd.turmateorica, mptd.disciplina ");
		sqlStr.append(" 	union all ");
		sqlStr.append(" 	select unidadeensino.nome as unidadeensino, curso.nome as curso, turmaagrupada.turmaorigem as turma, mptd.disciplina, true as turmaagrupada, false as disciplinaOnline, 0 as professor, ");
		sqlStr.append(" 	count(distinct mptd.codigo) as qtdealuno, ");
		sqlStr.append(" 	sum(case when ").append(condicaoNotas).append(" then 1 else 0 end) as qtdefalta ");
		sqlStr.append(" 	from  matriculaperiodoturmadisciplina mptd ");
		sqlStr.append(" 	inner join matricula on matricula.matricula = mptd.matricula ");
		if (periodicidade.equals("IN") ||
				filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() ||
				filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getConcluido() ||
				filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getFormado() ||
				filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado() ||
				filtroRelatorioAcademicoVO.getJubilado()) {
			sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = mptd.matriculaperiodo ");
		}
		sqlStr.append(" 	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" 	inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" 	inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turma = mptd.turmateorica ");
		sqlStr.append(" 	inner join horarioturma on horarioturma.turma = turmaagrupada.turmaorigem and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");
		sqlStr.append(" 	where historico.situacao not in ('AA', 'CC', 'CH','IS', 'AB') ");
		sqlStr.append(condicaoWhereFiltros);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" 	group by unidadeensino.nome, curso.nome, turmaagrupada.turmaorigem, mptd.disciplina ");
		sqlStr.append(" 	union all ");
		sqlStr.append(" 	select unidadeensino.nome as unidadeensino,  curso.nome as curso, mptd.turmapratica, mptd.disciplina, false as turmaagrupada, false as disciplinaOnline, 0 as professor, ");
		sqlStr.append(" 	count(distinct mptd.codigo) as qtdealuno, ");
		sqlStr.append(" 	sum(case when ").append(condicaoNotas).append(" then 1 else 0 end) as qtdefalta ");
		sqlStr.append(" 	from  matriculaperiodoturmadisciplina mptd ");
		sqlStr.append(" 	inner join matricula on matricula.matricula = mptd.matricula ");
		if (periodicidade.equals("IN") ||
				filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() ||
				filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getConcluido() ||
				filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getFormado() ||
				filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado() || 
				filtroRelatorioAcademicoVO.getJubilado()) {
			sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = mptd.matriculaperiodo ");
		}
		sqlStr.append(" 	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" 	inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" 	inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" 	inner join horarioturma on horarioturma.turma = mptd.turmapratica and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");
		sqlStr.append(" 	where historico.situacao not in ('AA', 'CC', 'CH','IS', 'AB') ");
		sqlStr.append(condicaoWhereFiltros);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" 	group by unidadeensino.nome, curso.nome, mptd.turmapratica, mptd.disciplina ");
		sqlStr.append(" 	union all ");
		sqlStr.append(" 	select unidadeensino.nome as unidadeensino,  curso.nome as curso, turmaagrupada.turmaorigem as turma, mptd.disciplina, true as turmaagrupada, false as disciplinaOnline, 0 as professor, ");
		sqlStr.append(" 	count(distinct mptd.codigo) as qtdealuno, ");
		sqlStr.append(" 	sum(case when ").append(condicaoNotas).append(" then 1 else 0 end) as qtdefalta ");
		sqlStr.append(" 	from  matriculaperiodoturmadisciplina mptd ");
		sqlStr.append(" 	inner join matricula on matricula.matricula = mptd.matricula ");
		if (periodicidade.equals("IN") ||
				filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() ||
				filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getConcluido() ||
				filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getFormado() ||
				filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado() || 
				filtroRelatorioAcademicoVO.getJubilado()) {
			sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = mptd.matriculaperiodo ");
		}
		sqlStr.append(" 	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" 	inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" 	inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turma = mptd.turmapratica ");
		sqlStr.append(" 	inner join horarioturma on horarioturma.turma = turmaagrupada.turmaorigem and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("' ");
		sqlStr.append(" 	where historico.situacao not in ('AA', 'CC', 'CH','IS', 'AB') ");
		sqlStr.append(condicaoWhereFiltros);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" 	group by unidadeensino.nome, curso.nome, turmaagrupada.turmaorigem, mptd.disciplina ");

		sqlStr.append(" 	union all ");
		sqlStr.append(" 	select unidadeensino.nome as unidadeensino,  curso.nome as curso, mptd.turma as turma, mptd.disciplina, false as turmaagrupada, true as disciplinaOnline, mptd.professor, ");
		sqlStr.append(" 	count(distinct mptd.codigo) as qtdealuno, ");
		sqlStr.append(" 	sum(case when ").append(condicaoNotas).append(" then 1 else 0 end) as qtdefalta ");
		sqlStr.append(" 	from  matriculaperiodoturmadisciplina mptd ");
		sqlStr.append(" 	inner join matricula on matricula.matricula = mptd.matricula ");
		if (periodicidade.equals("IN") ||
				filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() ||
				filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getConcluido() ||
				filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getFormado() ||
				filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado() || 
				filtroRelatorioAcademicoVO.getJubilado()) {
			sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = mptd.matriculaperiodo ");
		}
		sqlStr.append(" 	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" 	inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" 	inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sqlStr.append(" 	inner join turmadisciplina on turmadisciplina.turma = mptd.turma and turmadisciplina.disciplina = mptd.disciplina and turmadisciplina.definicoestutoriaonline = 'DINAMICA' ");
		sqlStr.append(" 	where historico.situacao not in ('AA', 'CC', 'CH','IS', 'AB') ");
		sqlStr.append(condicaoWhereFiltros);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" 	group by unidadeensino.nome, curso.nome, mptd.turma, mptd.disciplina, mptd.professor ");		
				
		sqlStr.append(" ) as mptd ");
		sqlStr.append(" left join horarioturma on horarioturma.turma = mptd.turma and mptd.disciplinaOnline =  false "); 
		sqlStr.append(" left join turma on (mptd.disciplinaOnline =  false and horarioturma.turma = turma.codigo)  or (mptd.disciplinaOnline =  true and turma.codigo = mptd.turma) ");
		sqlStr.append(" left join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo and mptd.disciplinaOnline =  false ");
		sqlStr.append(" left join disciplinaequivalente disEquivalente on disEquivalente.disciplina = mptd.disciplina and mptd.turmaagrupada and mptd.disciplinaOnline =  false ");
		sqlStr.append(" left join disciplinaequivalente disEquivale on disEquivale.equivalente = mptd.disciplina and mptd.turmaagrupada and mptd.disciplinaOnline =  false ");
		sqlStr.append(" left join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo and mptd.disciplinaOnline =  false ");
		sqlStr.append(" and ((horarioturmadiaitem.disciplina = mptd.disciplina) or (mptd.turmaagrupada and (horarioturmadiaitem.disciplina = disEquivalente.equivalente or  horarioturmadiaitem.disciplina = disEquivale.disciplina )) ) ");
		sqlStr.append(" inner join pessoa on (mptd.disciplinaOnline =  false and horarioturmadiaitem.professor = pessoa.codigo) or (mptd.disciplinaOnline =  true and mptd.professor = pessoa.codigo)  ");
		sqlStr.append(" inner join disciplina on (mptd.disciplinaOnline =  false and horarioturmadiaitem.disciplina = disciplina.codigo) or (mptd.disciplinaOnline =  true and mptd.disciplina = disciplina.codigo) ");
		sqlStr.append(" where qtdefalta > 0 ");
		sqlStr.append(" and ((mptd.disciplinaOnline =  false and ").append(realizarGeracaoWherePeriodo(periodoAulaInicial, periodoAulaFinal, "horarioturmadiaitem.data", false));
		if(!periodicidade.equals("IN")){
			sqlStr.append(" and horarioturma.anovigente = '").append(ano).append("' ");
		}
		if(periodicidade.equals("SE")){
			sqlStr.append(" and horarioturma.semestrevigente = '").append(semestre).append("' ");
		}		
		sqlStr.append(" ) or (mptd.disciplinaOnline =  true)) ");
		
		if (campoFiltroPor.equals("turma")) {
			sqlStr.append(" and turma.codigo = ").append(turma.getCodigo());
		}
		if (professor.getPessoa().getCodigo() != 0) {
			sqlStr.append(" and pessoa.codigo = ").append(professor.getPessoa().getCodigo());
		}
		sqlStr.append(" group by mptd.unidadeensino, mptd.curso, disciplina.nome, turma.identificadorturma, pessoa.nome, qtdealuno, qtdefalta ");
		if (layout.equals("curso")) {
			if (ordenacao.equals("professor")) {
				sqlStr.append(" order by unidadeensino, curso, professor ");
			} else if (ordenacao.equals("turma")) {
				sqlStr.append(" order by unidadeensino, curso, turma ");
			} else if (ordenacao.equals("disciplina")) {
				sqlStr.append(" order by unidadeensino, curso, disciplina ");
			}
		} else if (layout.equals("professor")) {
			if (ordenacao.equals("curso")) {
				sqlStr.append(" order by professor, curso ");
			} else if (ordenacao.equals("turma")) {
				sqlStr.append(" order by professor, turma ");
			} else if (ordenacao.equals("disciplina")) {
				sqlStr.append(" order by professor, disciplina ");
			}
		}		

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		condicaoNotas = null;
		condicaoWhereFiltros = null;
		return tabelaResultado;
	}
	
	private String getCondicaoWhereFiltros(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String periodicidade, String ano, String semestre, Date periodoMatriculaInicial,
			Date periodoMatriculaFinal,	String campoFiltroPor, TurmaVO turma, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, DisciplinaVO disciplina,
    		ConfiguracaoAcademicoVO configuracaoAcademica, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Date periodoAulaInicial, Date periodoAulaFinal) {
		StringBuilder sqlStr = new StringBuilder();
		StringBuilder sqlUnidade = new StringBuilder(" and unidadeensino.codigo in (");
		String auxU = "";
		for (UnidadeEnsinoVO unidadeEnsino : unidadeEnsinoVOs) {
			if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
				sqlUnidade.append(auxU).append(unidadeEnsino.getCodigo());
				auxU = ",";
			}
		}
		sqlUnidade.append(")");
		sqlStr.append(sqlUnidade);
		if (periodicidade.equals("AN") || periodicidade.equals("SE")) {
			if (Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" and historico.anohistorico = '").append(ano).append("' and mptd.ano = '").append(ano).append("'");
			}
		}
		if (periodicidade.equals("SE")) {
			if (Uteis.isAtributoPreenchido(semestre)) {
				sqlStr.append(" and historico.semestrehistorico = '").append(semestre).append("' and mptd.semestre = '").append(semestre).append("'");
			}
		}
		if (periodicidade.equals("IN")) {
			if (periodoMatriculaInicial != null) {
				sqlStr.append(" and matriculaperiodo.data >= '").append(Uteis.getDataJDBCTimestamp(periodoMatriculaInicial)).append("'");
			}
			if (periodoMatriculaFinal != null) {
				sqlStr.append(" and matriculaperiodo.data <= '").append(Uteis.getDataJDBCTimestamp(periodoMatriculaFinal)).append("'");
			}
		}
				
		if (campoFiltroPor.equals("curso")) {
			StringBuilder sqlCurso = new StringBuilder(" and curso.codigo in (");
			String auxC = "";
			for (CursoVO curso : cursoVOs) {
				if (curso.getFiltrarCursoVO()) {
					sqlCurso.append(auxC).append(curso.getCodigo());
					auxC = ",";
				}
			}
			sqlCurso.append(")");
			sqlStr.append(sqlCurso);
			StringBuilder sqlTurno = new StringBuilder(" and matricula.turno in (");
			String auxT = "";
			for (TurnoVO turno : turnoVOs) {
				if (turno.getFiltrarTurnoVO()) {
					sqlTurno.append(auxT).append(turno.getCodigo());
					auxT = ",";
				}
			}
			sqlTurno.append(")");
			if (!auxT.isEmpty()) {
				sqlStr.append(sqlTurno);
			}
		}
		if (Uteis.isAtributoPreenchido(disciplina.getCodigo())) {
			sqlStr.append(" and historico.disciplina = ").append(disciplina.getCodigo());
		}
		
		sqlStr.append(" and historico.configuracaoAcademico = ").append(configuracaoAcademica.getCodigo());
		
		StringBuilder situacaoAcademica = new StringBuilder(" and matriculaperiodo.situacaomatriculaperiodo in (");
		String auxSit = "";
		if (filtroRelatorioAcademicoVO.getAtivo()) {
			situacaoAcademica.append(auxSit).append("'AT'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
			situacaoAcademica.append(auxSit).append("'PR'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
			situacaoAcademica.append(auxSit).append("'PC'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getTrancado()) {
			situacaoAcademica.append(auxSit).append("'TR'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getCancelado()) {
			situacaoAcademica.append(auxSit).append("'CA'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getConcluido()) {
			situacaoAcademica.append(auxSit).append("'FI'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
			situacaoAcademica.append(auxSit).append("'TI'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
			situacaoAcademica.append(auxSit).append("'TS'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getFormado()) {
			situacaoAcademica.append(auxSit).append("'FO'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getAbandonado()) {
			situacaoAcademica.append(auxSit).append("'AC'");
			auxSit = ",";
		}
		if (filtroRelatorioAcademicoVO.getJubilado()) {
			situacaoAcademica.append(auxSit).append("'JU'");
			auxSit = ",";
		}
		situacaoAcademica.append(")");
		if (!auxSit.isEmpty()) {
			sqlStr.append(situacaoAcademica.toString());
		}
		
		StringBuilder situacaoFinanceira = new StringBuilder(" and matriculaperiodo.situacao in (");
		String auxF = "";
		if (filtroRelatorioAcademicoVO.getPendenteFinanceiro()) {
			situacaoFinanceira.append(auxF).append("'PF'");
			auxF = ",";
		}
		if (filtroRelatorioAcademicoVO.getConfirmado()) {
			situacaoFinanceira.append(auxF).append("'CO','AT'");
			auxF = ",";
		}
		situacaoFinanceira.append(")");
		if (!auxF.isEmpty()) {
			sqlStr.append(situacaoFinanceira.toString());
		}
		return sqlStr.toString();
	}
	
	public String getCondicaoNotas(String filtrarNotas, ConfiguracaoAcademicoVO configuracaoAcademica) {
		StringBuilder sqlStrNota = new StringBuilder();
		sqlStrNota.append("(");
		String auxNota = "";
		if (configuracaoAcademica.isFiltrarNota1()) {
			sqlStrNota.append(auxNota).append(" historico.nota1 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota2()) {
			sqlStrNota.append(auxNota).append(" historico.nota2 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota3()) {
			sqlStrNota.append(auxNota).append(" historico.nota3 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota4()) {
			sqlStrNota.append(auxNota).append(" historico.nota4 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota5()) {
			sqlStrNota.append(auxNota).append(" historico.nota5 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota6()) {
			sqlStrNota.append(auxNota).append(" historico.nota6 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota7()) {
			sqlStrNota.append(auxNota).append(" historico.nota7 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota8()) {
			sqlStrNota.append(auxNota).append(" historico.nota8 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota9()) {
			sqlStrNota.append(auxNota).append(" historico.nota9 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota10()) {
			sqlStrNota.append(auxNota).append(" historico.nota10 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota11()) {
			sqlStrNota.append(auxNota).append(" historico.nota11 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota12()) {
			sqlStrNota.append(auxNota).append(" historico.nota12 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota13()) {
			sqlStrNota.append(auxNota).append(" historico.nota13 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota14()) {
			sqlStrNota.append(auxNota).append(" historico.nota14 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota15()) {
			sqlStrNota.append(auxNota).append(" historico.nota15 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota16()) {
			sqlStrNota.append(auxNota).append(" historico.nota16 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota17()) {
			sqlStrNota.append(auxNota).append(" historico.nota17 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota18()) {
			sqlStrNota.append(auxNota).append(" historico.nota18 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota19()) {
			sqlStrNota.append(auxNota).append(" historico.nota19 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota20()) {
			sqlStrNota.append(auxNota).append(" historico.nota20 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota21()) {
			sqlStrNota.append(auxNota).append(" historico.nota21 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota22()) {
			sqlStrNota.append(auxNota).append(" historico.nota22 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota23()) {
			sqlStrNota.append(auxNota).append(" historico.nota23 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota24()) {
			sqlStrNota.append(auxNota).append(" historico.nota24 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota25()) {
			sqlStrNota.append(auxNota).append(" historico.nota25 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota26()) {
			sqlStrNota.append(auxNota).append(" historico.nota26 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota27()) {
			sqlStrNota.append(auxNota).append(" historico.nota27 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota28()) {
			sqlStrNota.append(auxNota).append(" historico.nota28 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota29()) {
			sqlStrNota.append(auxNota).append(" historico.nota29 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota30()) {
			sqlStrNota.append(auxNota).append(" historico.nota30 is null ");
			auxNota = filtrarNotas;
		}
		
		if (configuracaoAcademica.isFiltrarNota31()) {
			sqlStrNota.append(auxNota).append(" historico.nota31 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota32()) {
			sqlStrNota.append(auxNota).append(" historico.nota32 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota33()) {
			sqlStrNota.append(auxNota).append(" historico.nota33 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota34()) {
			sqlStrNota.append(auxNota).append(" historico.nota34 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota35()) {
			sqlStrNota.append(auxNota).append(" historico.nota35 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota36()) {
			sqlStrNota.append(auxNota).append(" historico.nota36 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota37()) {
			sqlStrNota.append(auxNota).append(" historico.nota37 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota38()) {
			sqlStrNota.append(auxNota).append(" historico.nota38 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota39()) {
			sqlStrNota.append(auxNota).append(" historico.nota39 is null ");
			auxNota = filtrarNotas;
		}
		if (configuracaoAcademica.isFiltrarNota40()) {
			sqlStrNota.append(auxNota).append(" historico.nota40 is null ");
			auxNota = filtrarNotas;
		}
		
		sqlStrNota.append(")");
		return sqlStrNota.toString();
	}

	public static String getDesignIReportRelatorio(String layout) throws Exception {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade(layout) + ".jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade(String layout) {
		if (layout.equals("curso")) {
			return "NotaNaoLancadaProfessorPorCursoRel";
		} else if (layout.equals("professor")) {
			return "NotaNaoLancadaProfessorPorProfessorRel";
		} else {
			return "";
		}
	}
}