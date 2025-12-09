package negocio.facade.jdbc.blackboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.BlackboardFechamentoNotaOperacaoVO;
import negocio.comuns.blackboard.BlackboardGestaoFechamentoNotaVO;
import negocio.comuns.blackboard.BlackboardGestaoFechamentoNotaVO.ConfiguracaoAcademicaFechamentoNotaVO;
import negocio.comuns.blackboard.BlackboardGestaoFechamentoNotaVO.NotaFechamentoNotaVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.blackboard.BlackboardFechamentoNotaOperacaoInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@SuppressWarnings("serial")
@Repository
@Scope("singleton")
@Lazy
public class BlackboardOperacaoFechamentoNota extends ControleAcesso implements BlackboardFechamentoNotaOperacaoInterfaceFacade {

	@Override
	public Boolean validarExisteOperacaoFechamentoNotaPendente() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM fechamentonotaoperacao WHERE executado = FALSE LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return tabelaResultado.next();
	}
	
	@Override
	public BlackboardGestaoFechamentoNotaVO consultarConfguracaoAcademicaParaFechamentoNota(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO, DataModelo controleConsulta, String nivelEducacional) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT configuracaoacademico.codigo, configuracaoacademico.nome, ");
		for (int i = 1; i < 41; i++) {
			if (i <= 40) {
				sql.append("configuracaoacademico.titulonotaapresentar" + i + ", ");
				sql.append("configuracaoacademico.utilizarnota" + i + ", ");
				sql.append("configuracaoacademico.formulacalculonota" + i);
				sql.append(i <= 39 ? ", " : " ");
			}
		}
		sql.append("FROM configuracaoacademico ");
		sql.append("WHERE 1 = 1 ");
		sql.append("AND EXISTS (SELECT FROM historico ");
		sql.append("INNER JOIN matricula ON matricula.matricula = historico.matricula ");
		sql.append("INNER JOIN curso ON curso.codigo = matricula.curso ");
		sql.append("WHERE historico.configuracaoacademico = configuracaoacademico.codigo ");
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sql.append("AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append("AND historico.matricula = '").append(matricula).append("' ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append("and matricula.curso = ").append(curso).append(" ");
		}
		if (Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
			sql.append("AND historico.anohistorico ='").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sql.append("AND historico.anohistorico ='").append(ano).append("' and historico.semestrehistorico ='").append(semestre).append("' ");
		}
		if (!filtroRelatorioAcademicoVO.isTodasSituacoesMatriculaDesmarcadas()) {
			sql.append("AND ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
			sql.append("AND matricula.unidadeEnsino IN (").append(unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append("AND historico.disciplina = ").append(disciplina).append(" ");
		}
		if (Uteis.isAtributoPreenchido(idSalaAulaBlackboard) || Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum) || Uteis.isAtributoPreenchido(bimestre)) {
			sql.append("AND EXISTS (SELECT FROM salaaulablackboardpessoa INNER JOIN salaaulablackboard ON salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
			sql.append("WHERE salaaulablackboardpessoa.matricula = matricula.matricula AND salaaulablackboardpessoa.matriculaperiodoturmadisciplina = historico.matriculaperiodoturmadisciplina ");
			if (Uteis.isAtributoPreenchido(idSalaAulaBlackboard)) {
				sql.append("AND salaaulablackboard.idSalaAulaBlackboard ilike('%").append(idSalaAulaBlackboard).append("%')");
			}
			if (Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum)) {
				sql.append(" and salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum).append("'");
			}
			if (Uteis.isAtributoPreenchido(bimestre)) {
				sql.append(" and salaaulablackboard.bimestre =").append(bimestre).append(" ");
			}
			sql.append(")");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append("AND EXISTS (SELECT FROM matriculaperiodoturmadisciplina m ");
			sql.append(" WHERE m.codigo = historico.matriculaperiodoturmadisciplina ");
			sql.append(" AND m.turma = ").append(turma).append(")");
		}
		sql.append(") ");
		sql.append("GROUP BY configuracaoacademico.codigo ");
		sql.append("order by nome ");
//		System.out.println(sql.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		BlackboardGestaoFechamentoNotaVO blackboardGestaoFechamentoNotaVO = new BlackboardGestaoFechamentoNotaVO();
		while (tabelaResultado.next()) {
			ConfiguracaoAcademicaFechamentoNotaVO configuracaoAcademicaFechamentoNotaVO = new ConfiguracaoAcademicaFechamentoNotaVO();
			configuracaoAcademicaFechamentoNotaVO.getConfiguracaoAcademico().setCodigo(tabelaResultado.getInt("codigo"));
			configuracaoAcademicaFechamentoNotaVO.getConfiguracaoAcademico().setNome(tabelaResultado.getString("nome"));
			for (int i = 1; i < 41; i++) {
				Boolean utilizarNota = tabelaResultado.getBoolean("utilizarnota" + i);
				String formulaCalculoNota = tabelaResultado.getString("formulacalculonota" + i);
				String tituloNotaApresentar = tabelaResultado.getString("titulonotaapresentar" + i);
				if (utilizarNota && !Uteis.isAtributoPreenchido(formulaCalculoNota) && Uteis.isAtributoPreenchido(tituloNotaApresentar)) {
					NotaFechamentoNotaVO nota = new NotaFechamentoNotaVO();
					nota.setConfiguracaoAcademico(configuracaoAcademicaFechamentoNotaVO.getConfiguracaoAcademico().getCodigo());
					nota.setNota(i);
					nota.setTituloApresentar(tituloNotaApresentar);
					configuracaoAcademicaFechamentoNotaVO.getListaNotaFechamentoNotaVO().add(nota);
				}
			}
			if (Uteis.isAtributoPreenchido(configuracaoAcademicaFechamentoNotaVO.getListaNotaFechamentoNotaVO())) {
				blackboardGestaoFechamentoNotaVO.getConfiguracaoAcademicaFechamentoNotaVOs().add(configuracaoAcademicaFechamentoNotaVO);
			}
		}
		return blackboardGestaoFechamentoNotaVO;
	}

	private List<BlackboardFechamentoNotaOperacaoVO> consultarHistoricosFechamentoNota(Integer codigoConfiguracaoAcademica, String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT configuracaoacademico, disciplina, array_to_string(array_agg(t.codigo), ', ') historicos ");
		sql.append("FROM (SELECT historico.configuracaoacademico, historico.disciplina, ROW_NUMBER() OVER(PARTITION BY historico.configuracaoacademico, historico.disciplina) - 1 ROW_NUMBER, historico.codigo ");
		sql.append("FROM historico ");
		sql.append("INNER JOIN matricula ON matricula.matricula = historico.matricula ");
		sql.append("INNER JOIN curso ON curso.codigo = matricula.curso ");
		sql.append("WHERE historico.configuracaoacademico = ").append(codigoConfiguracaoAcademica).append(" ");
		sql.append("AND curso.nivelEducacional = 'SU' ");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append("AND historico.matricula = '").append(matricula).append("' ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append("AND matricula.curso = ").append(curso).append(" ");
		}
		if (Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre)) {
			sql.append("AND historico.anohistorico ='").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sql.append("AND historico.anohistorico ='").append(ano).append("' and historico.semestrehistorico ='").append(semestre).append("' ");
		}
		if (!filtroRelatorioAcademicoVO.isTodasSituacoesMatriculaDesmarcadas()) {
			sql.append("AND ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
			sql.append("AND matricula.unidadeEnsino IN (").append(unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append("AND historico.disciplina = ").append(disciplina).append(" ");
		}
		if (Uteis.isAtributoPreenchido(idSalaAulaBlackboard) || Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum) || Uteis.isAtributoPreenchido(bimestre)) {
			sql.append("AND EXISTS (SELECT FROM salaaulablackboardpessoa INNER JOIN salaaulablackboard ON salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
			sql.append("WHERE salaaulablackboardpessoa.matricula = matricula.matricula AND salaaulablackboardpessoa.matriculaperiodoturmadisciplina = historico.matriculaperiodoturmadisciplina ");
			if (Uteis.isAtributoPreenchido(idSalaAulaBlackboard)) {
				sql.append("AND salaaulablackboard.idSalaAulaBlackboard ilike('%").append(idSalaAulaBlackboard).append("%')");
			}
			if (Uteis.isAtributoPreenchido(tipoSalaAulaBlackboardEnum)) {
				sql.append(" AND salaaulablackboard.tipoSalaAulaBlackboardEnum = '").append(tipoSalaAulaBlackboardEnum).append("'");
			}
			if (Uteis.isAtributoPreenchido(bimestre)) {
				sql.append(" AND salaaulablackboard.bimestre =").append(bimestre).append(" ");
			}
			sql.append(")");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append("AND EXISTS (SELECT FROM matriculaperiodoturmadisciplina m ");
			sql.append(" WHERE m.codigo = historico.matriculaperiodoturmadisciplina ");
			sql.append(" AND m.turma = ").append(turma).append(")");
		}
		sql.append(") t GROUP BY configuracaoacademico, disciplina, t.row_number / 1000 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<BlackboardFechamentoNotaOperacaoVO> blackboardFechamentoNotaOperacaoVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			BlackboardFechamentoNotaOperacaoVO blackboardFechamentoNotaOperacaoVO = new BlackboardFechamentoNotaOperacaoVO();
			blackboardFechamentoNotaOperacaoVO.getConfiguracaoAcademica().setCodigo(tabelaResultado.getInt("configuracaoacademico"));
			blackboardFechamentoNotaOperacaoVO.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("historicos"))) {
				blackboardFechamentoNotaOperacaoVO.setListaHistorico(new ArrayList<>(Arrays.asList(tabelaResultado.getString("historicos").split(", "))));
				blackboardFechamentoNotaOperacaoVOs.add(blackboardFechamentoNotaOperacaoVO);
			}
		}
		return blackboardFechamentoNotaOperacaoVOs;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarSeExisteProcessamentoFechamentoNotaPendente() {
		StringBuilder sql = new StringBuilder("select count(fechamentonotaoperacao.codigo) as QTDE from fechamentonotaoperacao where executado = false ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Integer qtde = (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		Uteis.checkState(Uteis.isAtributoPreenchido(qtde), "Não foi possível realizar essa operação, pois ainda existe " + qtde + " processamentos pendentes para Fechamento de Notas Operação.");
	}

	private Boolean verificarPossivelRealizarOperacaoFechamentoNota(BlackboardGestaoFechamentoNotaVO blackboardGestaoFechamentoNotaVO) {
		return blackboardGestaoFechamentoNotaVO != null && Uteis.isAtributoPreenchido(blackboardGestaoFechamentoNotaVO.getConfiguracaoAcademicaFechamentoNotaVOs()) && blackboardGestaoFechamentoNotaVO.getConfiguracaoAcademicaFechamentoNotaVOs().stream().anyMatch(c -> Uteis.isAtributoPreenchido(c.getListaNotaFechamentoNotaVO())) && blackboardGestaoFechamentoNotaVO.getConfiguracaoAcademicaFechamentoNotaVOs().stream().map(ConfiguracaoAcademicaFechamentoNotaVO::getListaNotaFechamentoNotaVO).flatMap(Collection::stream).anyMatch(NotaFechamentoNotaVO::getSelecionado);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void incluirFechamentoNotaOperacao(BlackboardFechamentoNotaOperacaoVO obj, UsuarioVO usuario) throws Exception {
		obj.validarDados();
		incluir(obj, "fechamentoNotaOperacao", new AtributoPersistencia().add("configuracaoacademica", obj.getConfiguracaoAcademica().getCodigo()).add("disciplina", obj.getDisciplina().getCodigo()).add("listaHistorico", obj.getListaHistorico().stream().map(h -> h).collect(Collectors.joining(", "))).add("notas", obj.getNotas()).add("calcularmedia", obj.getCalcularMedia()).add("aprovado", obj.getAprovado()).add("cursando", obj.getCursando()).add("reprovado", obj.getReprovado()), usuario);
	}

	private void realizarCriacaoFechamentoNotaPorHistoricosUltrapassandoLimite(Integer configuracaoAcademica, Integer disciplina, String notas, List<String> listaHistorico, List<BlackboardFechamentoNotaOperacaoVO> listaOperacoes) {
		BlackboardFechamentoNotaOperacaoVO operacaoFechamentoNota = new BlackboardFechamentoNotaOperacaoVO();
		operacaoFechamentoNota.getConfiguracaoAcademica().setCodigo(configuracaoAcademica);
		operacaoFechamentoNota.getDisciplina().setCodigo(disciplina);
		operacaoFechamentoNota.setNotas(notas);
		List<String> historicosRestantes = new ArrayList<>();
		Integer qtd = 0;
		for (String his : listaHistorico) {
			qtd++;
			if (qtd <= (1000)) {
				operacaoFechamentoNota.getListaHistorico().add(his);
			} else {
				historicosRestantes.add(his);
			}
		}
		listaOperacoes.add(operacaoFechamentoNota);
		if (Uteis.isAtributoPreenchido(historicosRestantes)) {
			realizarCriacaoFechamentoNotaPorHistoricosUltrapassandoLimite(configuracaoAcademica, disciplina, notas, historicosRestantes, listaOperacoes);
		}
	}

	private List<BlackboardFechamentoNotaOperacaoVO> realizarMontagemListaFechamentoNotaOperacao(BlackboardGestaoFechamentoNotaVO blackboardGestaoFechamentoNotaVO, String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioLogado) throws Exception {
		List<BlackboardFechamentoNotaOperacaoVO> listaOperacoes = new ArrayList<>();
		for (ConfiguracaoAcademicaFechamentoNotaVO configuracaoAcademica : blackboardGestaoFechamentoNotaVO.getConfiguracaoAcademicaFechamentoNotaVOs()) {
			if (Uteis.isAtributoPreenchido(configuracaoAcademica.getConfiguracaoAcademico()) && Uteis.isAtributoPreenchido(configuracaoAcademica.getListaNotaFechamentoNotaVO()) && configuracaoAcademica.getListaNotaFechamentoNotaVO().stream().anyMatch(NotaFechamentoNotaVO::getSelecionado)) {
				List<BlackboardFechamentoNotaOperacaoVO> operacoes = consultarHistoricosFechamentoNota(configuracaoAcademica.getConfiguracaoAcademico().getCodigo(), idSalaAulaBlackboard, unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, bimestre, curso, turma, disciplina, matricula, filtroRelatorioAcademicoVO, usuarioLogado);
				for (BlackboardFechamentoNotaOperacaoVO operacao : operacoes) {
					operacao.setNotas(configuracaoAcademica.getListaNotaFechamentoNotaVO().stream().filter(NotaFechamentoNotaVO::getSelecionado).map(n -> n.getNota().toString()).collect(Collectors.joining(", ")));
					if (Uteis.isAtributoPreenchido(operacao.getConfiguracaoAcademica()) && Uteis.isAtributoPreenchido(operacao.getDisciplina()) && Uteis.isAtributoPreenchido(operacao.getListaHistorico())) {
						if (operacao.getListaHistorico().size() > 1000) {
							realizarCriacaoFechamentoNotaPorHistoricosUltrapassandoLimite(operacao.getConfiguracaoAcademica().getCodigo(), operacao.getDisciplina().getCodigo(), operacao.getNotas(), operacao.getListaHistorico(), listaOperacoes);
						} else {
							listaOperacoes.add(operacao);
						}
					}
				}
			}
		}
		return listaOperacoes;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarOperacaoDeFechamentoNota(BlackboardGestaoFechamentoNotaVO blackboardGestaoFechamentoNotaVO, String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioLogado) throws Exception {
		consultarSeExisteProcessamentoFechamentoNotaPendente();
		Uteis.checkState(!verificarPossivelRealizarOperacaoFechamentoNota(blackboardGestaoFechamentoNotaVO), "Para ser realizada a operação, é necessário ao menos selecionar uma nota das configurações acadêmicas");
		if (blackboardGestaoFechamentoNotaVO != null && Uteis.isAtributoPreenchido(blackboardGestaoFechamentoNotaVO.getConfiguracaoAcademicaFechamentoNotaVOs())) {
			List<BlackboardFechamentoNotaOperacaoVO> listaOperacoes = realizarMontagemListaFechamentoNotaOperacao(blackboardGestaoFechamentoNotaVO, idSalaAulaBlackboard, unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, bimestre, curso, turma, disciplina, matricula, filtroRelatorioAcademicoVO, usuarioLogado);
			Uteis.checkState(!Uteis.isAtributoPreenchido(listaOperacoes), "Não foi possível realizar a criação das operações de fechamento de nota");
			if (Uteis.isAtributoPreenchido(listaOperacoes)) {
				for (BlackboardFechamentoNotaOperacaoVO operacaoFechamentoNota : listaOperacoes) {
					try {
						if (blackboardGestaoFechamentoNotaVO.getRealizarCalculoNota()) {
							operacaoFechamentoNota.setCalcularMedia(blackboardGestaoFechamentoNotaVO.getRealizarCalculoNota());
							operacaoFechamentoNota.setAprovado(filtroRelatorioAcademicoVO.getAprovado());
							operacaoFechamentoNota.setCursando(filtroRelatorioAcademicoVO.getCursando());
							operacaoFechamentoNota.setReprovado(filtroRelatorioAcademicoVO.getReprovado());
						}
						incluirFechamentoNotaOperacao(operacaoFechamentoNota, usuarioLogado);
					} catch (Exception e) {
						try {
							String log = "fechamento de nota " + "Configuração: " + operacaoFechamentoNota.getConfiguracaoAcademica().getCodigo() + " Discplina: " + operacaoFechamentoNota.getDisciplina().getCodigo() + " erro" + e.getMessage();
							RegistroExecucaoJobVO registroExecucaoJob = new RegistroExecucaoJobVO(JobsEnum.JOB_FECHAMENTO_NOTAS.getName(), log, 0);
							getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJob, usuarioLogado);
						} catch (Exception e2) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@Override
	public List<BlackboardFechamentoNotaOperacaoVO> consultarFechamentoNotaOperacaoNaoExecutado() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo, configuracaoacademica, disciplina, listahistorico, notas, executado,	calcularmedia, aprovado, cursando, reprovado ");
		sql.append("FROM fechamentonotaoperacao ");
		sql.append("WHERE executado = FALSE ");
		sql.append("ORDER BY codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<BlackboardFechamentoNotaOperacaoVO> fechamentoNotaOperacaoVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			BlackboardFechamentoNotaOperacaoVO fechamentoNotaOperacaoVO = new BlackboardFechamentoNotaOperacaoVO();
			fechamentoNotaOperacaoVO.setCodigo(tabelaResultado.getInt("codigo"));
			fechamentoNotaOperacaoVO.getConfiguracaoAcademica().setCodigo(tabelaResultado.getInt("configuracaoacademica"));
			fechamentoNotaOperacaoVO.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("listahistorico"))) {
				fechamentoNotaOperacaoVO.setListaHistorico(new ArrayList<>(Arrays.asList(tabelaResultado.getString("listahistorico").split(", "))));
			}
			fechamentoNotaOperacaoVO.setNotas(tabelaResultado.getString("notas"));
			fechamentoNotaOperacaoVO.setExecutado(tabelaResultado.getBoolean("executado"));
			fechamentoNotaOperacaoVO.setCalcularMedia(tabelaResultado.getBoolean("calcularmedia"));
			fechamentoNotaOperacaoVO.setAprovado(tabelaResultado.getBoolean("aprovado"));
			fechamentoNotaOperacaoVO.setCursando(tabelaResultado.getBoolean("cursando"));
			fechamentoNotaOperacaoVO.setReprovado(tabelaResultado.getBoolean("reprovado"));
			fechamentoNotaOperacaoVOs.add(fechamentoNotaOperacaoVO);
		}
		return fechamentoNotaOperacaoVOs;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void alterarFechamentoNotaOperacaoExecutado(BlackboardFechamentoNotaOperacaoVO obj, UsuarioVO usuario) {
		AtributoPersistencia atributoPersistencia = new AtributoPersistencia();
		if (Uteis.isAtributoPreenchido(obj.getErro())) {
			atributoPersistencia.add("erro", obj.getErro());
			atributoPersistencia.add("executado", Boolean.FALSE);
		} else {
			atributoPersistencia.add("erro", null);
			atributoPersistencia.add("executado", Boolean.TRUE);
		}
		alterar(obj, "fechamentoNotaOperacao", atributoPersistencia, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void atualizarHistoricoNotaNulla(List<String> historicos, List<String> notas, UsuarioVO usuario) {
		if (Uteis.isAtributoPreenchido(historicos) && Uteis.isAtributoPreenchido(notas)) {
			StringBuilder update = new StringBuilder();
			update.append("UPDATE historico ");
			update.append("SET ").append(notas.stream().map(n -> "nota" + n + " = CASE WHEN nota" + n + " IS NULL THEN 0.0 ELSE nota" + n + " END ").collect(Collectors.joining(", "))).append(" ");
			update.append("WHERE codigo IN (").append(historicos.stream().map(h -> h).collect(Collectors.joining(", "))).append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(update.toString());
		}
	}

	private void realizarFechamentoNotasSelecionados(BlackboardFechamentoNotaOperacaoVO obj, ProgressBarVO progressBarFechamentoNota) {
		if (Uteis.isAtributoPreenchido(obj.getListaHistorico()) && Uteis.isAtributoPreenchido(obj.getNotas())) {
			List<String> listaHistorico = obj.getListaHistorico();
			List<String> notas = new ArrayList<>(Arrays.asList(obj.getNotas().split(", ")));
			atualizarHistoricoNotaNulla(listaHistorico, notas, progressBarFechamentoNota.getUsuarioVO());
		}
	}

	private void realizarFechamentoNotaCalculandoMedia(BlackboardFechamentoNotaOperacaoVO obj, ProgressBarVO progressBarFechamentoNota) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			List<String> notas = new ArrayList<>(Arrays.asList(obj.getNotas().split(", ")));
			FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
			filtroRelatorioAcademicoVO.setAprovado(obj.getAprovado());
			filtroRelatorioAcademicoVO.setCursando(obj.getCursando());
			filtroRelatorioAcademicoVO.setReprovado(obj.getReprovado());
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaHistoricoParaFechamentoNotasPorBlackboard(obj, filtroRelatorioAcademicoVO, progressBarFechamentoNota.getUsuarioVO());
			if (Uteis.isAtributoPreenchido(historicoVOs)) {
				historicoVOs.stream().forEach(historicoVO -> {
					if (Uteis.isAtributoPreenchido(historicoVO)) {
						for (int i = 0; i < notas.size(); i++) {
							if (UtilReflexao.invocarMetodoGet(historicoVO, "nota" + notas.get(i)) != null) {
								continue;
							} else {
								UtilReflexao.invocarMetodoSet1Parametro(historicoVO, "nota" + notas.get(i), new Double(0.0).getClass(), 0.0);
							}
						}
					}
				});
				getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(historicoVOs, Boolean.FALSE, Boolean.TRUE, progressBarFechamentoNota.getUsuarioVO());
			}
		}
	}
	
	private String getStatusFechamentoNota(BlackboardFechamentoNotaOperacaoVO obj) {
		return "Operação de código: " + obj.getCodigo() + ", Configuração Acadêmica: " + obj.getConfiguracaoAcademica().getCodigo() + ", Disciplina: " + obj.getDisciplina().getCodigo() + ", Quantidade de Históricos: " + obj.getListaHistorico().size();
	}

	@Override
	public void executarOperacaoFechamentoNotaBlackboard(List<BlackboardFechamentoNotaOperacaoVO> operacoes, ProgressBarVO progressBarFechamentoNota, SuperControle superControle) throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(operacoes) && progressBarFechamentoNota != null && Uteis.isAtributoPreenchido(progressBarFechamentoNota.getUsuarioVO())) {
				for (int i = 0; i < operacoes.size(); i++) {
					progressBarFechamentoNota.setProgresso(Long.valueOf(i + 1));
					BlackboardFechamentoNotaOperacaoVO obj = operacoes.get(i);
					if (progressBarFechamentoNota.getForcarEncerramento()) {
						break;
					}
					try {
						if (Uteis.isAtributoPreenchido(obj) && !obj.getExecutado()) {
							progressBarFechamentoNota.setStatus(progressBarFechamentoNota.getProgresso() + " á " + progressBarFechamentoNota.getMaxValue() + " - " + getStatusFechamentoNota(obj));
							obj.validarDados();
							if (obj.getCalcularMedia()) {
								realizarFechamentoNotaCalculandoMedia(obj, progressBarFechamentoNota);
							} else {
								realizarFechamentoNotasSelecionados(obj, progressBarFechamentoNota);
							}
						}
					} catch (Exception exe) {
						obj.setErro(exe.getMessage());
					} finally {
						alterarFechamentoNotaOperacaoExecutado(obj, progressBarFechamentoNota.getUsuarioVO());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			progressBarFechamentoNota.setForcarEncerramento(Boolean.TRUE);
		}
	}
	
	@Override
	public List<BlackboardFechamentoNotaOperacaoVO> consultarFechamentoNotaErro() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT fechamentonotaoperacao.codigo, configuracaoacademico.codigo AS codigoConfiguracaoAcademica, configuracaoacademico.nome AS nomeConfiguracaoAcademica,	disciplina.codigo AS codigoDiscplina, disciplina.nome AS nomeDiscina, fechamentonotaoperacao.erro ");
		sql.append("FROM fechamentonotaoperacao ");
		sql.append("INNER JOIN configuracaoacademico ON	configuracaoacademico.codigo = fechamentonotaoperacao.configuracaoacademica ");
		sql.append("INNER JOIN disciplina ON disciplina.codigo = fechamentonotaoperacao.disciplina ");
		sql.append("WHERE fechamentonotaoperacao.erro IS NOT NULL AND fechamentonotaoperacao.erro != '' ");
		sql.append("ORDER BY configuracaoacademico.codigo, disciplina.codigo DESC ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<BlackboardFechamentoNotaOperacaoVO> fechamentoNotaOperacaoVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			BlackboardFechamentoNotaOperacaoVO fechamentoNotaOperacaoVO = new BlackboardFechamentoNotaOperacaoVO();
			fechamentoNotaOperacaoVO.setCodigo(tabelaResultado.getInt("codigo"));
			fechamentoNotaOperacaoVO.setErro(tabelaResultado.getString("erro"));
			fechamentoNotaOperacaoVO.getConfiguracaoAcademica().setCodigo(tabelaResultado.getInt("codigoConfiguracaoAcademica"));
			fechamentoNotaOperacaoVO.getConfiguracaoAcademica().setNome(tabelaResultado.getString("nomeConfiguracaoAcademica"));
			fechamentoNotaOperacaoVO.getDisciplina().setCodigo(tabelaResultado.getInt("codigoDiscplina"));
			fechamentoNotaOperacaoVO.getDisciplina().setNome(tabelaResultado.getString("nomeDiscina"));
			fechamentoNotaOperacaoVOs.add(fechamentoNotaOperacaoVO);
		}
		return fechamentoNotaOperacaoVOs;
	}
}