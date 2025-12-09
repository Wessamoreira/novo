package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jakarta.faces. model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaUnidadeVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.DadosComerciaisVO;

import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MatriculaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>MatriculaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MatriculaVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
@SuppressWarnings("unchecked")
public class TransferenciaUnidade extends ControleAcesso  {

	protected static String idEntidade;
	public static final long serialVersionUID = 1L;

	public TransferenciaUnidade() throws Exception {
		super();
		setIdEntidade("TransferenciaUnidade");
	}

//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public TransferenciaUnidadeVO persistir(MatriculaPeriodoVO matriculaPeriodoOrigem, MatriculaPeriodoVO matriculaPeriodoDestino, MatriculaVO matriculaVoNova, Boolean transferirDescontos, List listaSelectItemReconhecimentoCurso , ConfiguracaoGeralSistemaVO configuracaoGeralSistema, MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO, UsuarioVO usuario) throws Exception {
//		try {
//			if (!getFacadeFactory().getContaReceberFacade().consultarAlunoPossuiContasRenegociadas(matriculaPeriodoOrigem.getMatriculaVO().getMatricula(), "AR", usuario)) {
//				if (matriculaVoNova.isNovoObj().booleanValue()) {
//					if (configuracaoFinanceiroPadraoSistema.getUtilizarIntegracaoFinanceira() && matriculaVoNova.getCodigoFinanceiroMatricula().trim().equals("")) {
//						throw new ConsistirException("Deve ser informado o código de integração financeira da matrícula!");						
//					}
//					criarDocumentacaoNovaMatricula(matriculaPeriodoDestino, matriculaVoNova, matriculaPeriodoOrigem, listaSelectItemReconhecimentoCurso, usuario);
//					carregarDisciplinasCurso(matriculaPeriodoDestino, matriculaVoNova, usuario);
//					carregarPlanoFinanceiroAluno(matriculaPeriodoOrigem, matriculaPeriodoDestino, matriculaVoNova, transferirDescontos, configuracaoFinanceiroPadraoSistema, usuario);
//					gravarMatricula(matriculaPeriodoDestino, matriculaVoNova, configuracaoFinanceiroPadraoSistema, configuracaoGeralSistema, usuario);
//					isentarContasReceberMatriculaDestino(matriculaPeriodoOrigem, matriculaPeriodoDestino, matriculaVoNova, configuracaoFinanceiroPadraoSistema, usuario);
//					excluirContasReceberMatriculaOrigem(matriculaPeriodoOrigem, matriculaPeriodoDestino, matriculaVoNova, usuario);
//					aproveitarDisciplinasDaOutraUnidade(matriculaPeriodoOrigem, matriculaVoNova, usuario);
//					TransferenciaUnidadeVO transferenciaUnidadeVO = incluirTransferenciaUnidade(matriculaPeriodoOrigem, matriculaVoNova, usuario);
//					alterarSituacaoAcademicaMatriculaParaTransferenciaInterna(matriculaPeriodoOrigem, transferenciaUnidadeVO, configuracaoFinanceiroPadraoSistema, usuario);
//					return transferenciaUnidadeVO;
//				}
//			} else {
//				throw new ConsistirException("A transferência de unidade não pode ser feita porque a MATRÍCULA " + matriculaPeriodoOrigem.getMatriculaVO().getMatricula() + " possui contas renegociadas em aberto.");
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//		return null;
//	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoAcademicaMatriculaParaTransferenciaInterna(MatriculaPeriodoVO matriculaPeriodoOrigem, TransferenciaUnidadeVO transferenciaUnidadeVO,  UsuarioVO usuario) throws Exception {
		matriculaPeriodoOrigem.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor());
		getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(matriculaPeriodoOrigem.getMatriculaVO().getMatricula(), matriculaPeriodoOrigem.getSituacaoMatriculaPeriodo(), usuario);
		matriculaPeriodoOrigem.setAlunoTransferidoUnidade(true);
		String[] situacaoHistoricoDesconsiderar = {"AA", "CC", "CH", "IS"};
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoGradeCurricularAtual(matriculaPeriodoOrigem.getCodigo(), matriculaPeriodoOrigem.getMatriculaVO().getGradeCurricularAtual().getCodigo(), situacaoHistoricoDesconsiderar, false, usuario);
		for (HistoricoVO obj : historicoVOs) {
			obj.setMatriculaPeriodo(matriculaPeriodoOrigem);
//			if (obj.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor())) {
				obj.setRealizarAlteracaoSituacaoHistorico(true);
				obj.setEditavel(false);
//			} else {
//				obj.setEditavel(true);
//			}
		}
		getFacadeFactory().getTrancamentoFacade().alterarSituacaoAcademicaMatriculaPeriodo(matriculaPeriodoOrigem, historicoVOs, new Date(), OrigemFechamentoMatriculaPeriodoEnum.TRANSFERENCIA_INTERNA, transferenciaUnidadeVO.getCodigo(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarDocumentacaoNovaMatricula(MatriculaPeriodoVO matriculaPeriodoDestino, MatriculaVO matriculaVoNova, MatriculaPeriodoVO matriculaPeriodoOrigem, List listaSelectItemReconhecimentoCurso, UsuarioVO usuario) throws Exception {
		try {
			if (matriculaPeriodoDestino.getNovoObj()) {
				getFacadeFactory().getMatriculaPeriodoFacade().validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(matriculaVoNova, matriculaPeriodoDestino, usuario, null);
			}
			matriculaPeriodoDestino.setResponsavelRenovacaoMatricula(usuario);
//			getFacadeFactory().getMatriculaFacade().inicializarTextoContratoPlanoFinanceiroAluno(matriculaVoNova, matriculaPeriodoDestino, true, usuario);
			if (!matriculaVoNova.getSituacao().equals("PL")) {
				matriculaVoNova.setUsuario(usuario);
			}
			matriculaVoNova.setAluno(matriculaPeriodoOrigem.getMatriculaVO().getAluno());
			matriculaPeriodoDestino.setSituacao("PF");
			matriculaPeriodoDestino.setCodigo(0);
			getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerRealizadaSemValidarDataMatriculaCalendario(matriculaVoNova, matriculaPeriodoDestino, usuario);
			getFacadeFactory().getMatriculaFacade().gerenciarEntregaDocumentoMatricula(matriculaVoNova, usuario);

			if (!listaSelectItemReconhecimentoCurso.isEmpty() && listaSelectItemReconhecimentoCurso.size() == 2) {
				Integer valor = (Integer) ((SelectItem) listaSelectItemReconhecimentoCurso.get(1)).getValue();
				matriculaVoNova.getAutorizacaoCurso().setCodigo(valor);
			}
			matriculaVoNova.setDocumetacaoMatriculaVOs(getFacadeFactory().getDocumetacaoMatriculaFacade().consultaRapidaPorMatricula(matriculaPeriodoOrigem.getMatriculaVO().getMatricula(), false, usuario));
			matriculaVoNova.setFormaIngresso(matriculaPeriodoOrigem.getMatriculaVO().getFormaIngresso());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void carregarDisciplinasCurso(MatriculaPeriodoVO matriculaPeriodoVoDestino, MatriculaVO matriculaVoNova, UsuarioVO usuario) throws Exception {
		try {
			limparDisciplinasPorPeriodoLetivo(matriculaPeriodoVoDestino);
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(matriculaVoNova, matriculaPeriodoVoDestino, usuario, null, false, false);
		} catch (Exception e) {
			throw e;
		}
	}

	public void limparDisciplinasPorPeriodoLetivo(MatriculaPeriodoVO matriculaPeriodoVoDestino) {
		matriculaPeriodoVoDestino.getMatriculaPeriodoTumaDisciplinaVOs().clear();
		matriculaPeriodoVoDestino.getTurma().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
	}

//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public void carregarPlanoFinanceiroAluno(MatriculaPeriodoVO matriculaPeriodoOrigem, MatriculaPeriodoVO matriculaPeriodoDestino, MatriculaVO matriculaVoNova, Boolean transferirDescontos , UsuarioVO usuario) throws Exception {
//		try {
//			List ordemDesconto = null;
//			getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(matriculaVoNova, matriculaPeriodoDestino, true, usuario);
//			if (transferirDescontos) {
//				carregarPlanoFinanceiroMatriculaPeriodoOrigemParaNovaMatricula(matriculaPeriodoOrigem, matriculaVoNova, usuario);
//			}
//			ordemDesconto = matriculaVoNova.getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual();
//			getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(matriculaVoNova, matriculaPeriodoDestino, usuario);
//			getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(matriculaVoNova, matriculaPeriodoDestino, ordemDesconto, usuario);
//		} catch (Exception e) {
//			throw e;
//		}
//	}



	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarMatricula(MatriculaPeriodoVO matriculaPeriodoVoDestino, MatriculaVO matriculaVoNova , ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		try {
			if (matriculaVoNova.getIsAtiva()) {
				getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVoNova, usuario);
				matriculaPeriodoVoDestino.setResponsavelRenovacaoMatricula(usuario);
				adicionarMatriculaPeriodo(matriculaVoNova, matriculaPeriodoVoDestino);
				if (!matriculaVoNova.getMatriculaJaRegistrada()) {
					getFacadeFactory().getMatriculaFacade().incluir(matriculaVoNova, matriculaPeriodoVoDestino, matriculaPeriodoVoDestino.getProcessoMatriculaCalendarioVO(),  configuracaoGeralSistema, true, usuario);
				}
			} else {
				throw new ConsistirException("Esta renovação não pode ser gravada pois refere-se a uma matrícula: " + matriculaVoNova.getSituacao_Apresentar().toUpperCase());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void adicionarMatriculaPeriodo(MatriculaVO matriculaVoNova, MatriculaPeriodoVO matriculaPeriodoVoDestino) throws Exception {
		if (!matriculaVoNova.getMatricula().equals("")) {
			matriculaPeriodoVoDestino.setMatricula(matriculaVoNova.getMatricula());
		}
		matriculaVoNova.adicionarObjMatriculaPeriodoVOs(matriculaPeriodoVoDestino);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void isentarContasReceberMatriculaDestino(MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaPeriodoVO matriculaPeriodoVoDestino, MatriculaVO matriculaVoNova , UsuarioVO usuario) throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().liberarContasTransferenciaUnidade(matriculaVoNova, matriculaPeriodoVoOrigem, matriculaPeriodoVoDestino, matriculaPeriodoVoDestino.getProcessoMatriculaCalendarioVO(), usuario, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContasReceberMatriculaOrigem(MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaPeriodoVO matriculaPeriodoVoDestino, MatriculaVO matriculaVoNova, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getMatriculaPeriodoVencimentoFacade().excluirComBaseNaContaReceberDataVencimentoSituacaoMatricula(matriculaPeriodoVoOrigem.getMatriculaVO().getMatricula(), "AR", usuario);
//		getFacadeFactory().getContaReceberFacade().excluirComBaseNaDataVencimentoSituacaoMatricula(matriculaPeriodoVoOrigem.getMatriculaVO().getMatricula(), "AR", usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void aproveitarDisciplinasDaOutraUnidade(MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaVO matriculaVoNova, UsuarioVO usuario) throws Exception {
		List<HistoricoVO> listaHistoricosMatriculaOrigem = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaSituacaoAprovadoAproveitamentoMedia(matriculaPeriodoVoOrigem.getMatriculaVO().getMatricula(), true, true, false, usuario);
		if (!listaHistoricosMatriculaOrigem.isEmpty()) {
			HashMap<Integer, HistoricoVO> hashHistoricosMatriculaDestino = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaDisciplinasHistoricoSemDadosSubordinados(matriculaVoNova.getMatricula(), listaHistoricosMatriculaOrigem, false, usuario);
			if (!hashHistoricosMatriculaDestino.isEmpty()) {
				setarDadosHistoricoOrigemParaHistoricoDestino(listaHistoricosMatriculaOrigem, hashHistoricosMatriculaDestino, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void setarDadosHistoricoOrigemParaHistoricoDestino(List<HistoricoVO> listaHistoricosMatriculaOrigem, HashMap<Integer, HistoricoVO> hashHistoricosMatriculaDestino, UsuarioVO usuario) throws Exception {
		for (HistoricoVO historicoMatriculaOrigem : listaHistoricosMatriculaOrigem) {
			if (hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()) == null) {
				continue;
			}
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota1(historicoMatriculaOrigem.getNota1());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota2(historicoMatriculaOrigem.getNota2());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota3(historicoMatriculaOrigem.getNota3());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota4(historicoMatriculaOrigem.getNota4());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota5(historicoMatriculaOrigem.getNota5());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota6(historicoMatriculaOrigem.getNota6());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota7(historicoMatriculaOrigem.getNota7());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota8(historicoMatriculaOrigem.getNota8());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota9(historicoMatriculaOrigem.getNota9());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota10(historicoMatriculaOrigem.getNota10());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota11(historicoMatriculaOrigem.getNota11());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota12(historicoMatriculaOrigem.getNota12());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota13(historicoMatriculaOrigem.getNota13());

			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setFreguencia(historicoMatriculaOrigem.getFreguencia());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setSituacao("AA");
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setMediaFinal(historicoMatriculaOrigem.getMediaFinal());

			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota1Lancada(historicoMatriculaOrigem.getNota1Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota2Lancada(historicoMatriculaOrigem.getNota2Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota3Lancada(historicoMatriculaOrigem.getNota3Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota4Lancada(historicoMatriculaOrigem.getNota4Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota5Lancada(historicoMatriculaOrigem.getNota5Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota6Lancada(historicoMatriculaOrigem.getNota6Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota7Lancada(historicoMatriculaOrigem.getNota7Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota8Lancada(historicoMatriculaOrigem.getNota8Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota9Lancada(historicoMatriculaOrigem.getNota9Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota10Lancada(historicoMatriculaOrigem.getNota10Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota11Lancada(historicoMatriculaOrigem.getNota11Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota12Lancada(historicoMatriculaOrigem.getNota12Lancada());
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).setNota13Lancada(historicoMatriculaOrigem.getNota13Lancada());
			
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).getMatrizCurricular().setCodigo((historicoMatriculaOrigem.getMatrizCurricular().getCodigo()));
			hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()).getGradeDisciplinaVO().setCodigo((historicoMatriculaOrigem.getGradeDisciplinaVO().getCodigo()));
			
			getFacadeFactory().getHistoricoFacade().alterar(hashHistoricosMatriculaDestino.get(historicoMatriculaOrigem.getDisciplina().getCodigo()), usuario);
		}
	}

	public void gerarDataMatriculaDestino(MatriculaVO matriculaVoNova, MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaPeriodoVO matriculaPeriodoVoDestino) throws Exception {
		Integer qtdeContasMatriculaOrigem = null;
		Date dataMatriculaDestino = null;
		try {
//			qtdeContasMatriculaOrigem = getFacadeFactory().getContaReceberFacade().consultarQtdeContasComBaseNaDataVencimento(matriculaPeriodoVoOrigem.getMatriculaVO().getMatricula(), "AL", "Matrícula", true);
//			if (!matriculaPeriodoVoDestino.getCondicaoPagamentoPlanoFinanceiroCurso().getNaoControlarMatricula() || matriculaPeriodoVoDestino.getProcessoMatriculaCalendarioVO().getMesSubsequenteMatricula()) {
//				qtdeContasMatriculaOrigem++;
//			}
			dataMatriculaDestino = Uteis.getObterDataComMesesAMenos(new Date(), qtdeContasMatriculaOrigem);
			dataMatriculaDestino = Uteis.getDataPrimeiroDiaMes(dataMatriculaDestino);
			matriculaVoNova.setData(dataMatriculaDestino);
			matriculaPeriodoVoDestino.setData(dataMatriculaDestino);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TransferenciaUnidadeVO incluirTransferenciaUnidade(MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaVO matriculaVoNova, UsuarioVO usuario) throws Exception {
		TransferenciaUnidadeVO transferenciaUnidadeVO = new TransferenciaUnidadeVO();
		transferenciaUnidadeVO.setMatriculaVoOrigem(matriculaPeriodoVoOrigem.getMatriculaVO());
		transferenciaUnidadeVO.setMatriculaVoDestino(matriculaVoNova);
		transferenciaUnidadeVO.setData(new Date());
		transferenciaUnidadeVO.setResponsavel(usuario);
		incluir(transferenciaUnidadeVO, usuario);
		return transferenciaUnidadeVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TransferenciaUnidadeVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO transferenciaUnidade( matriculaOrigem, unidadeEnsinoOrigem, matriculaDestino, unidadeEnsinoDestino, responsavel, data) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, obj.getMatriculaVoOrigem().getMatricula());
					sqlInserir.setInt(2, obj.getMatriculaVoOrigem().getUnidadeEnsino().getCodigo());
					sqlInserir.setString(3, obj.getMatriculaVoDestino().getMatricula());
					sqlInserir.setInt(4, obj.getMatriculaVoDestino().getUnidadeEnsino().getCodigo());
					sqlInserir.setInt(5, obj.getResponsavel().getCodigo());
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getData()));
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void validarDados(TransferenciaUnidadeVO obj) throws ConsistirException {
		if (obj.getMatriculaVoOrigem().getMatricula().equals("")) {
			throw new ConsistirException("O campo MATRICULA ORIGEM (TransferenciaUnidade) deve ser informado.");
		}
		if (obj.getMatriculaVoDestino().getMatricula().equals("")) {
			throw new ConsistirException("O campo MATRICULA DESTINO (TransferenciaUnidade) deve ser informado.");
		}
		if (obj.getMatriculaVoOrigem().getUnidadeEnsino().getCodigo().equals(0)) {
			throw new ConsistirException("O campo UNIDADE ENSINO ORIGEM (TransferenciaUnidade) deve ser informado.");
		}
		if (obj.getMatriculaVoDestino().getUnidadeEnsino().getCodigo().equals(0)) {
			throw new ConsistirException("O campo UNIDADE ENSINO DESTINO (TransferenciaUnidade) deve ser informado.");
		}
		if (obj.getResponsavel().getCodigo().equals(0)) {
			throw new ConsistirException("O campo RESPONSAVEL (TransferenciaUnidade) deve ser informado.");
		}
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA (TransferenciaUnidade) deve ser informado.");
		}
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("select transferenciaunidade.codigo AS \"transferenciaunidade.codigo\", transferenciaunidade.matriculaOrigem AS \"transferenciaunidade.matriculaOrigem\", ");
		str.append("transferenciaunidade.unidadeEnsinoOrigem AS \"transferenciaunidade.unidadeEnsinoOrigem\", transferenciaunidade.matriculaDestino AS \"transferenciaunidade.matriculaDestino\", ");
		str.append("transferenciaunidade.unidadeEnsinoDestino AS \"transferenciaunidade.unidadeEnsinoDestino\", transferenciaunidade.responsavel AS \"transferenciaunidade.responsavel\", ");
		str.append("transferenciaunidade.data AS \"transferenciaunidade.data\", pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ueorigem.nome AS \"ueorigem.nome\", ");
		str.append("uedestino.nome AS \"uedestino.nome\", usuario.nome AS \"usuario.nome\", curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\" ");
		str.append("from transferenciaunidade ");
		str.append("LEFT JOIN matricula ON matricula.matricula = transferenciaunidade.matriculaOrigem ");
		str.append("LEFT JOIN curso ON curso.codigo = matricula.curso ");
		str.append("LEFT JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		str.append("LEFT JOIN unidadeEnsino AS ueorigem ON ueorigem.codigo = transferenciaunidade.unidadeEnsinoOrigem ");
		str.append("LEFT JOIN unidadeEnsino AS uedestino ON uedestino.codigo = transferenciaunidade.unidadeEnsinoDestino ");
		str.append("LEFT JOIN usuario ON usuario.codigo = transferenciaunidade.responsavel ");
		return str;
	}

	public List<TransferenciaUnidadeVO> consultaRapidaPorMatriculaOrigem(String matriculaOrigem, Integer unidadeEnsinoOrigem, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(transferenciaUnidade.matriculaOrigem) like('");
		sqlStr.append(matriculaOrigem.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsinoOrigem.intValue() != 0 && unidadeEnsinoOrigem != null) {
			sqlStr.append(" AND transferenciaUnidade.unidadeEnsinoOrigem = ");
			sqlStr.append(unidadeEnsinoOrigem.intValue());
		}
		sqlStr.append(" ORDER BY transferenciaUnidade.matriculaOrigem");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<TransferenciaUnidadeVO> consultaRapidaPorMatriculaDestino(String matriculaDestino, Integer unidadeEnsinoDestino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(transferenciaUnidade.matriculaDestino) like('");
		sqlStr.append(matriculaDestino.toLowerCase());
		sqlStr.append("%')");
		if (unidadeEnsinoDestino.intValue() != 0 && unidadeEnsinoDestino != null) {
			sqlStr.append(" AND transferenciaUnidade.unidadeEnsinoDestino = ");
			sqlStr.append(unidadeEnsinoDestino.intValue());
		}
		sqlStr.append(" ORDER BY transferenciaUnidade.matriculaDestino");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<TransferenciaUnidadeVO> consultaRapidaPorNomeAluno(String nomeAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(pessoa.nome) like('");
		sqlStr.append(nomeAluno.toLowerCase());
		sqlStr.append("%')");
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<TransferenciaUnidadeVO> consultaRapidaPorNomeCurso(String nomeCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(curso.nome) like('");
		sqlStr.append(nomeCurso.toLowerCase());
		sqlStr.append("%')");
		sqlStr.append(" ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<TransferenciaUnidadeVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsinoOrigem, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (transferenciaUnidade.data >= '").append(Uteis.getDataJDBC(prmIni)).append(" 00:00:00' AND transferenciaUnidade.data <= '").append(Uteis.getDataJDBC(prmFim)).append(" 23:59:59')");
		if (unidadeEnsinoOrigem.intValue() != 0 && unidadeEnsinoOrigem != null) {
			sqlStr.append(" AND (transferenciaUnidade.unidadeEnsinoOrigem = ").append(unidadeEnsinoOrigem.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY transferenciaUnidade.data, Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<TransferenciaUnidadeVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<TransferenciaUnidadeVO> vetResultado = new ArrayList<TransferenciaUnidadeVO>(0);
		while (tabelaResultado.next()) {
			TransferenciaUnidadeVO obj = new TransferenciaUnidadeVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(TransferenciaUnidadeVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Transferencia Unidade
		obj.setCodigo(new Integer(dadosSQL.getInt("transferenciaUnidade.codigo")));
		obj.getMatriculaVoOrigem().setMatricula(dadosSQL.getString("transferenciaUnidade.matriculaOrigem"));
		obj.getMatriculaVoOrigem().getUnidadeEnsino().setCodigo(dadosSQL.getInt("transferenciaUnidade.unidadeEnsinoOrigem"));
		obj.getMatriculaVoDestino().setMatricula(dadosSQL.getString("transferenciaUnidade.matriculaDestino"));
		obj.getMatriculaVoDestino().getUnidadeEnsino().setCodigo(dadosSQL.getInt("transferenciaUnidade.unidadeEnsinoDestino"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("transferenciaUnidade.responsavel"));
		obj.setData(dadosSQL.getTimestamp("transferenciaUnidade.data"));
		obj.getMatriculaVoOrigem().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatriculaVoOrigem().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatriculaVoOrigem().getUnidadeEnsino().setNome(dadosSQL.getString("ueorigem.nome"));
		obj.getMatriculaVoDestino().getUnidadeEnsino().setNome(dadosSQL.getString("uedestino.nome"));
		obj.getMatriculaVoOrigem().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getMatriculaVoOrigem().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificador é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TransferenciaUnidade.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		TransferenciaUnidade.idEntidade = idEntidade;
	}

	
	public Boolean imprimirDeclaracaoTransferenciaUnidade(Integer transferenciaUnidade, Integer textoPadraoDeclaracao, UsuarioVO usuario) throws Exception {
		try {
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			TransferenciaUnidadeVO transferenciaUnidadeVO = consultarPorChavePrimaria(transferenciaUnidade, false, usuario);
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setMatriculaOrigem(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(transferenciaUnidadeVO.getMatriculaVoOrigem().getMatricula(), transferenciaUnidadeVO.getMatriculaVoOrigem().getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, usuario));
			impressaoContratoVO.setMatriculaDestino(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(transferenciaUnidadeVO.getMatriculaVoDestino().getMatricula(), transferenciaUnidadeVO.getMatriculaVoDestino().getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, usuario));
			if (impressaoContratoVO.getMatriculaDestino().getCurso().getCodigo().equals(0)) {
				impressaoContratoVO.getMatriculaDestino().setCurso(impressaoContratoVO.getMatriculaOrigem().getCurso());
			}
			impressaoContratoVO.setTextoPadraoDeclaracao(textoPadraoDeclaracaoVO.getCodigo());
			textoPadraoDeclaracaoVO.substituirTag(impressaoContratoVO, usuario);
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textoPadraoDeclaracaoVO.getTexto());
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
//	@Override
//	public Boolean imprimirContratoTransferenciaUnidade(Integer transferenciaUnidade, Integer codigoTexto,  UsuarioVO usuarioLogado) throws Exception {
//		try {
//			TransferenciaUnidadeVO transferenciaUnidadeVO = consultarPorChavePrimaria(transferenciaUnidade, false, usuarioLogado);
//			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(transferenciaUnidadeVO.getMatriculaVoDestino().getMatricula(), transferenciaUnidadeVO.getMatriculaVoDestino().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,  usuarioLogado);
//			DadosComerciaisVO dc = getFacadeFactory().getDadosComerciaisFacade().consultarEmpregoAtualPorCodigoPessoa(obj.getAluno().getCodigo(), usuarioLogado);
//// 			PlanoFinanceiroAlunoVO plano = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(obj.getMatriculaPeriodoVOAtiva().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
////			plano.setCondicaoPagamentoPlanoFinanceiroCursoVO(obj.getMatriculaPeriodoVOAtiva().getCondicaoPagamentoPlanoFinanceiroCurso());
//// 			List<PlanoDescontoVO> listaPlanoDescontoVO = getFacadeFactory().getPlanoDescontoFacade().consultarPorPlanoFinanceiroAluno(plano.getCodigo(), false, usuarioLogado);
//			TextoPadraoVO texto = getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(codigoTexto, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
//			String contratoPronto  = texto.substituirTagsTextoPadraoContratoMatricula( obj,  obj.getMatriculaPeriodoVOAtiva(), obj.getMatriculaPeriodoVOAtiva(),  dc, usuarioLogado);
//			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
//	        request.getSession().setAttribute("textoRelatorio", contratoPronto);
//	        return Boolean.TRUE;
//		} catch (Exception e) {
//			throw e;
//		}
//
//	}

	public TransferenciaUnidadeVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE transferenciaUnidade.codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		TransferenciaUnidadeVO obj = new TransferenciaUnidadeVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);
		}
		return obj;
	}
	
	public Boolean verificarExistenciaTransferenciaInterna(String matriculaorigem) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT count(transferenciaunidade.codigo) AS existetransferenciainterna  ");
		str.append(" FROM transferenciaunidade INNER JOIN matricula ON  transferenciaunidade.matriculaorigem = matricula.matricula  ");
		str.append(" WHERE matricula.situacao = 'TI' AND transferenciaunidade.matriculaorigem = '");
		str.append(matriculaorigem.toLowerCase()).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (tabelaResultado.next()) {
			if (tabelaResultado.getInt("existetransferenciainterna") > 0) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Método responsável por gerar a data de vencimento da parcela de matrícula matrícula
	 */
	public void gerarDataMatriculaDestinoDeAcordoProcessoMatricula(MatriculaVO matriculaVoNova, MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaPeriodoVO matriculaPeriodoVoDestino) throws Exception {
		Date dataMatriculaDestino = new Date();
//		if (!matriculaPeriodoVoDestino.getCondicaoPagamentoPlanoFinanceiroCurso().getNaoControlarMatricula()) {
//			if(Uteis.isAtributoPreenchido(matriculaPeriodoVoDestino.getDataVencimentoMatriculaEspecifico())) {
//				dataMatriculaDestino = matriculaPeriodoVoDestino.getDataVencimentoMatriculaEspecifico();
//			}else if (matriculaPeriodoVoDestino.getProcessoMatriculaCalendarioVO().getUsarDataVencimentoDataMatricula()) {
//				if (matriculaPeriodoVoDestino.getProcessoMatriculaCalendarioVO().getQtdeDiasAvancarDataVencimentoMatricula() > 0) {
//					dataMatriculaDestino = (Uteis.obterDataAvancada(matriculaPeriodoVoDestino.getData(), matriculaPeriodoVoDestino.getProcessoMatriculaCalendarioVO().getQtdeDiasAvancarDataVencimentoMatricula()));
//				} 
//			} else {
//				dataMatriculaDestino = (matriculaPeriodoVoDestino.getProcessoMatriculaCalendarioVO().getDataVencimentoMatricula());
//			}
//		}
		matriculaVoNova.setData(dataMatriculaDestino);
		matriculaPeriodoVoDestino.setData(dataMatriculaDestino);
	}

}
