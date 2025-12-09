package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jobs.JobExecutarSincronismoComLdapAoCancelarTransferirMatricula;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;

import negocio.comuns.protocolo.RequerimentoVO;
//import negocio.comuns.sad.MatriculaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.TrancamentoInterfaceFacade;
/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TrancamentoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TrancamentoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 *
 * @see TrancamentoVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
public class Trancamento extends ControleAcesso implements TrancamentoInterfaceFacade {

	protected static String idEntidade;
	public static final long serialVersionUID = 1L;

	public Trancamento() throws Exception {
		super();
		setIdEntidade("Trancamento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>TrancamentoVO</code>.
	 */
	public TrancamentoVO novo() throws Exception {
		Trancamento.incluir(getIdEntidade());
		TrancamentoVO obj = new TrancamentoVO();
		return obj;
	}

	/**
	 * Responsável por executar a alteração da situação do requerimento para FD (Finalizado Deferido), realizar a alteração da situação da matrícula e
	 * por fim realizar a alteração da situação acadêmica da matrícula período.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaPeriodoVO
	 * @param historicoVOs
	 * @param requerimentoVO
	 * @param data
	 * @param origem
	 * @param codigoOrigemFechamentoMatriculaPeriodo
	 * @param alunoTransferidoUnidade
	 * @param relativoAbandonoDeCurso
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoAcademicaMatricula(MatriculaPeriodoVO matriculaPeriodoVO, List<HistoricoVO> historicoVOs, RequerimentoVO requerimentoVO, Date data, OrigemFechamentoMatriculaPeriodoEnum origem, Integer codigoOrigemFechamentoMatriculaPeriodo, boolean alunoTransferidoUnidade, boolean relativoAbandonoDeCurso, Boolean inativarUsuarioLdap , Boolean inativarUsuarioBlackBoard  , UsuarioVO usuario) throws Exception {
		getFacadeFactory().getRequerimentoFacade().alterarSituacao(requerimentoVO.getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), "", requerimentoVO.getMotivoDeferimento(),requerimentoVO, usuario);
		getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), matriculaPeriodoVO.getSituacaoMatriculaPeriodo(), usuario);
		matriculaPeriodoVO.setAlunoTransferidoUnidade(alunoTransferidoUnidade);
		if (relativoAbandonoDeCurso) {
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaParaIndicarAbandonoCurso(matriculaPeriodoVO.getMatriculaVO().getMatricula(), true);
		}
		alterarSituacaoAcademicaMatriculaPeriodo(matriculaPeriodoVO, historicoVOs, data, origem, codigoOrigemFechamentoMatriculaPeriodo, usuario);		
		realizarInativacaoUsuarioLdap(matriculaPeriodoVO, inativarUsuarioLdap, usuario);
		realizarInativacaoBlackboardPessoaEmailInstitucional(matriculaPeriodoVO, inativarUsuarioBlackBoard, usuario);
	}

	private void realizarInativacaoUsuarioLdap(MatriculaPeriodoVO matriculaPeriodoVO, Boolean inativarUsuarioLdap, UsuarioVO usuario) throws Exception {
		if (inativarUsuarioLdap && Uteis.isAtributoPreenchido(matriculaPeriodoVO) &&
				(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()) 	
				|| SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo())
				|| SituacaoMatriculaPeriodoEnum.JUBILADO.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()))) {
			if(!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getCurso().getConfiguracaoLdapVO())) {
				matriculaPeriodoVO.getMatriculaVO().getCurso().setConfiguracaoLdapVO(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo()));
			}
			if (!getFacadeFactory().getMatriculaFacade().verificarExisteMultiplasMatriculasAtivasDominioLDAP(matriculaPeriodoVO.getMatriculaVO(), usuario)) {
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			if(Uteis.isAtributoPreenchido(usuarioVO)) {
					getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( usuarioVO, Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(usuarioVO.getPessoa().getCPF()), Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(usuarioVO.getPessoa().getCPF()), false, usuario);
					getFacadeFactory().getUsuarioFacade().alterarBooleanoResetouSenhaPrimeiroAcesso(false, usuarioVO, false, usuario);
				JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuarioVO, matriculaPeriodoVO.getMatriculaVO(), false,usuario);
				Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
				jobSincronizarCancelamento.start();
			}
			}
			
		}
	}

	private void realizarInativacaoBlackboardPessoaEmailInstitucional(MatriculaPeriodoVO matriculaPeriodoVO, Boolean inativarUsuarioBlackBoard, UsuarioVO usuario) throws Exception {
		if (inativarUsuarioBlackBoard && Uteis.isAtributoPreenchido(matriculaPeriodoVO) && Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO()) &&
				(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo())
						|| SituacaoMatriculaPeriodoEnum.TRANCADA.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo())
						|| SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo())
						|| SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo())
						|| SituacaoMatriculaPeriodoEnum.JUBILADO.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()))) {
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
			if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
				if(!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getCurso().getConfiguracaoLdapVO())) {
					matriculaPeriodoVO.getMatriculaVO().getCurso().setConfiguracaoLdapVO(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo()));
				}
				if (!getFacadeFactory().getMatriculaFacade().verificarExisteMultiplasMatriculasAtivasDominioLDAP(matriculaPeriodoVO.getMatriculaVO(), usuario)) {
					getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarInativacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuario);
					if(!SituacaoMatriculaPeriodoEnum.TRANCADA.getValor().equals(matriculaPeriodoVO.getSituacaoMatriculaPeriodo())) {
						pessoaEmailInstitucionalVO.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
						getFacadeFactory().getPessoaEmailInstitucionalFacade().alterarSituacaoStatusAtivoInativoEnum(pessoaEmailInstitucionalVO, usuario);
					}
				}
				if(!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getSalaAulaBlackboardVO().getCodigo())) {
					matriculaPeriodoVO.getMatriculaVO().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(matriculaPeriodoVO.getMatriculaVO().getMatricula(), TipoSalaAulaBlackboardEnum.ESTAGIO, usuario));
				}
				if(!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getSalaAulaBlackboardTcc().getCodigo())) {
					matriculaPeriodoVO.getMatriculaVO().setSalaAulaBlackboardTcc(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(matriculaPeriodoVO.getMatriculaVO().getMatricula(), TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO, usuario));
				}
				if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getSalaAulaBlackboardVO().getCodigo())) {
					getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(matriculaPeriodoVO.getMatriculaVO().getSalaAulaBlackboardVO().getCodigo(), matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, pessoaEmailInstitucionalVO.getEmail(), usuario);
				}
				if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getSalaAulaBlackboardTcc().getCodigo())) {
					getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(matriculaPeriodoVO.getMatriculaVO().getSalaAulaBlackboardTcc().getCodigo(), matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, pessoaEmailInstitucionalVO.getEmail(), usuario);
				}
			}
		}
	}

	/**
	 * Responsável por executar a alteração da situação da matrícula período, alteração de todos os históricos, retirar a reserva da turma e realizar
	 * criação da MatriculaDW.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaPeriodoVO
	 * @param historicoVOs
	 * @param data
	 * @param origem
	 * @param codigoOrigemFechamentoMatriculaPeriodo
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoAcademicaMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO, List<HistoricoVO> historicoVOs, Date data, OrigemFechamentoMatriculaPeriodoEnum origem, Integer codigoOrigemFechamentoMatriculaPeriodo,  UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoPorCodigo(matriculaPeriodoVO.getCodigo(), matriculaPeriodoVO.getSituacaoMatriculaPeriodo(), origem, codigoOrigemFechamentoMatriculaPeriodo, matriculaPeriodoVO.getAlunoTransferidoUnidade(), data);
			if (matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor()) || matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor())) {
				alterarSituacaoHistoricoPorMatriculaPeriodo(historicoVOs, SituacaoHistorico.TRANSFERIDO.getValor(), matriculaPeriodoVO,  usuario);
			} else if (Objects.nonNull(matriculaPeriodoVO.getSituacaoMatriculaPeriodoEnum()) && matriculaPeriodoVO.getSituacaoMatriculaPeriodoEnum().isSituacaoMatriculaPeriodoPresenteHistorico()) {
				alterarSituacaoHistoricoPorMatriculaPeriodo(historicoVOs, matriculaPeriodoVO.getSituacaoMatriculaPeriodo(), matriculaPeriodoVO, usuario);
			}
			if(!matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
					&& !matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())
					&& !matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
					) {
				List<DocumentoAssinadoVO> documentoAssinadoVOs = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaAlunoContratoPendenteAssinatura(matriculaPeriodoVO.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
				for (Iterator<DocumentoAssinadoVO> iterator = documentoAssinadoVOs.iterator(); iterator.hasNext();) {
					DocumentoAssinadoVO documentoAssinadoVO = (DocumentoAssinadoVO) iterator.next();
					getFacadeFactory().getDocumentoAssinadoFacade().excluir(documentoAssinadoVO, false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuario));					
				}
			}
//			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().retirarReservaTurmaDisciplina(matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), usuario);
//			matriculaPeriodoVO.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, usuario));
//			MatriculaDWVO matriculaDWVO = criarMatriculaDW(matriculaPeriodoVO.getProcessoMatricula(), matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO.getSituacaoMatriculaPeriodo(), data, 1);
//			getFacadeFactory().getMatriculaDWFacade().incluir(matriculaDWVO, usuario);
		}
	}

	/**
	 * Responsável por executar a alteração dos históricos que estejam selecionados , levando em consideração que por default os históricos cuja
	 * situação seja cursado (CS) já deverão vir marcado, ou seja, sempre serão alterados suas situações.
	 * 
	 * @author Wellington Rodrigues - 27/03/2015
	 * @param trancamento
	 * @param situacaoMatriculaPeriodo
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoHistoricoPorMatriculaPeriodo(List<HistoricoVO> historicoVOs, String situacaoMatriculaPeriodo, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		for (HistoricoVO obj : historicoVOs) {
			if (obj.getRealizarAlteracaoSituacaoHistorico()) {
				if (obj.getHistoricoPorEquivalencia()) {
					alterarSituacaoHistoricoEquivalentePorMatriculaPeriodo(obj, situacaoMatriculaPeriodo, usuario);
				}
				if (obj.getHistoricoDisciplinaComposta()) {
					alterarSituacaoHistoricoCompostoPorMatriculaPeriodo(obj, situacaoMatriculaPeriodo, matriculaPeriodoVO, usuario);
				}
				getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(obj.getCodigo(), situacaoMatriculaPeriodo, usuario);
			}
		}
	}

	/**
	 * Responsável por executar a alteração da situação dos históricos que fazem parte da equivalência.
	 * 
	 * @author Wellington Rodrigues - 27/03/2015
	 * @param historicoVO
	 * @param situacaoMatriculaPeriodo
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoHistoricoEquivalentePorMatriculaPeriodo(HistoricoVO historicoVO, String situacaoMatriculaPeriodo, UsuarioVO usuario) throws Exception {
		List<HistoricoVO> historicoEquivalenteVOs = getFacadeFactory().getHistoricoFacade().consultarHistoricoEquivalentePorMatriculaMapaEquivalenciaDisciplina(historicoVO.getMatricula().getMatricula(), historicoVO.getMapaEquivalenciaDisciplina().getCodigo(), historicoVO.getNumeroAgrupamentoEquivalenciaDisciplina(), false, usuario);
		for (HistoricoVO historicoEquivalenteVO : historicoEquivalenteVOs) {
			getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(historicoEquivalenteVO.getCodigo(), situacaoMatriculaPeriodo, usuario);
		}
	}

	/**
	 * Responsável por executar a alteração da situação dos históricos que fazem parte da composição.
	 * 
	 * @author Wellington Rodrigues - 27/03/2015
	 * @param historicoVO
	 * @param situacaoMatriculaPeriodo
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoHistoricoCompostoPorMatriculaPeriodo(HistoricoVO historicoVO, String situacaoMatriculaPeriodo, MatriculaPeriodoVO matriculaPeriodoVO , UsuarioVO usuario) throws Exception {
		List<HistoricoVO> historicoDisciplinaFazParteComposicaoVOs = getFacadeFactory().getHistoricoFacade().consultarHistoricoDisciplinaFazParteComposicao(historicoVO.getMatricula().getMatricula(), historicoVO.getGradeDisciplinaVO().getCodigo(), null, false, matriculaPeriodoVO, usuario);
		for (HistoricoVO historicoFazParteComposicao : historicoDisciplinaFazParteComposicaoVOs) {
			getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(historicoFazParteComposicao.getCodigo(), situacaoMatriculaPeriodo, usuario);
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TrancamentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>TrancamentoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO, Boolean realizandoTrancamentoAutomatico) throws Exception {
		try {
			validarDados(obj, configuracaoGeralSistemaVO);
			// TrancamentoVO.validarTrancamento(obj);
			// alterarSituacaoRequerimento(obj);
			// alterarSituacaoAcademicaMatricula(obj);
			if (Uteis.isAtributoPreenchido(usuarioVO)) {
				incluir(getIdEntidade(), realizandoTrancamentoAutomatico ? false : true, usuarioVO);
			}
			final String sql = "INSERT INTO Trancamento( data, descricao, matricula, codigoRequerimento, justificativa, responsavelAutorizacao, situacao, dataPossivelRetorno, motivoCancelamentoTrancamento, ano, semestre, matriculaPeriodo, tipotrancamento) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(++i, obj.getDescricao());
					// sqlInserir.setString( 3, obj.getSituacao() );
					sqlInserir.setString(++i, obj.getMatricula().getMatricula());
					if ((obj.getCodigoRequerimento().getCodigo() != null) && (obj.getCodigoRequerimento().getCodigo() != 0)) {
						sqlInserir.setInt(++i, obj.getCodigoRequerimento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getJustificativa());
					if (obj.getResponsavelAutorizacao() != null && obj.getResponsavelAutorizacao().getCodigo() != null && obj.getResponsavelAutorizacao().getCodigo() > 0) {
						sqlInserir.setInt(++i, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
//					sqlInserir.setBoolean(++i, obj.getTrancamentoRelativoAbondonoCurso().booleanValue());
					sqlInserir.setString(++i, obj.getSituacao());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPossivelRetorno()));
					if (obj.getMotivoCancelamentoTrancamento() != null && obj.getMotivoCancelamentoTrancamento().getCodigo() != null && obj.getMotivoCancelamentoTrancamento().getCodigo() > 0) {
						sqlInserir.setInt(++i, obj.getMotivoCancelamentoTrancamento().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getAno());
					sqlInserir.setString(++i, obj.getSemestre());
					if (Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoVO().getCodigo())) {
						sqlInserir.setInt(++i, obj.getMatriculaPeriodoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTipoTrancamento());
					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			alterarSituacaoAcademicaMatriculaTrancamento(obj, configuracaoGeralSistemaVO, usuarioVO, realizandoTrancamentoAutomatico);
//			if (obj.getMatricula().getSituacao().equals("AC")) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemRegistroAbandonoCurso(obj.getMatriculaPeriodoVO());
//			} else if (obj.getMatricula().getSituacao().equals("TR")) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTrancamentoMatricula(obj.getMatricula().getAluno(), obj.getMatricula(), usuarioVO);
//			} else if (obj.getMatricula().getSituacao().equals("JU")) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemJubilamentoMatricula(obj.getMatricula().getAluno(), obj.getMatricula(), usuarioVO);
//			}
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>TrancamentoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code>
	 * da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>TrancamentoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj, configuracaoGeralSistemaVO);
			// alterarSituacaoRequerimento(obj);
			alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE Trancamento set data=?, descricao=?, matricula=?, justificativa=?, responsavelAutorizacao=?, codigoRequerimento=?, situacao=?, dataPossivelRetorno=?, motivoCancelamentoTrancamento=?, ano=?, semestre=?, matriculaperiodo=?, tipotrancamento=?  WHERE ((codigo = ?))";
			// Matricula observador = (Matricula) ((Advised)
			// getFacadeFactory().getMatriculaFacade()).getTargetSource().getTarget();
			// obj.getMatricula().addObserver(observador);
			// obj.getMatricula().setChanged();
			// obj.getMatricula().notifyObservers();
			// getBloqueio().lock();
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(++i, obj.getDescricao());
					// sqlAlterar.setString( 3, obj.getSituacao() );
					sqlAlterar.setString(++i, obj.getMatricula().getMatricula());
					sqlAlterar.setString(++i, obj.getJustificativa());
					sqlAlterar.setInt(++i, obj.getResponsavelAutorizacao().getCodigo().intValue());
//					sqlAlterar.setBoolean(++i, obj.getTrancamentoRelativoAbondonoCurso());
					if ((obj.getCodigoRequerimento().getCodigo() != null) && (obj.getCodigoRequerimento().getCodigo() != 0)) {
						sqlAlterar.setInt(++i, obj.getCodigoRequerimento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getSituacao());
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPossivelRetorno()));
					sqlAlterar.setInt(++i, obj.getMotivoCancelamentoTrancamento().getCodigo());
					sqlAlterar.setString(++i, obj.getAno());
					sqlAlterar.setString(++i, obj.getSemestre());
					if (Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoVO().getCodigo())) {
						sqlAlterar.setInt(++i, obj.getMatriculaPeriodoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getTipoTrancamento());
					sqlAlterar.setInt(++i, obj.getCodigo().intValue());					
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TrancamentoVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>TrancamentoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TrancamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM Trancamento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por executar a alteração da situação acadêmica da matrícula no ato da inclusão.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param trancamentoVO
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoAcademicaMatriculaTrancamento(TrancamentoVO trancamentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuario, Boolean realizandoTrancamentoAutomatico) throws Exception {
		trancamentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
		alterarSituacao(trancamentoVO.getCodigo(), trancamentoVO.getSituacao());
		if(!Uteis.isAtributoPreenchido(trancamentoVO.getMatriculaPeriodoVO().getCodigo())){			
			StringBuilder sql = new StringBuilder("select codigo, ano, semestre  from matriculaperiodo where matricula = '");
			sql.append(trancamentoVO.getMatricula().getMatricula()).append("' and ano||semestre <= '").append(trancamentoVO.getAno()+trancamentoVO.getSemestre()).append("' order by ano||semestre desc limit 1 ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			Integer mp = 0;
			if(rs.next()){
				mp = rs.getInt("codigo");
			}else{
				sql = new StringBuilder("select codigo, ano, semestre  from matriculaperiodo where matricula = '");
				sql.append(trancamentoVO.getMatricula().getMatricula()).append("' and ano||semestre > '").append(trancamentoVO.getAno()+trancamentoVO.getSemestre()).append("' order by ano||semestre asc limit 1 ");
				rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				if(rs.next()){
					mp = rs.getInt("codigo");
				}
			}
			MatriculaPeriodoVO mpBase = new MatriculaPeriodoVO();
			mpBase.setCodigo(mp);
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(mpBase, NivelMontarDados.BASICO, usuario);			
			executarVerificacaoEGeracaoMatriculaPeriodoSemestrePosteriorMapaRegistroAbandonoCursoTrancamento(trancamentoVO, mpBase, usuario);
			alterar(trancamentoVO, configuracaoGeralSistemaVO, usuario);
		}
		OrigemFechamentoMatriculaPeriodoEnum origem = null;
		if (trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.ABANDONO_DE_CURSO.getValor())) {
			trancamentoVO.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getValor());
			origem = OrigemFechamentoMatriculaPeriodoEnum.ABANDONO;
		} else if (trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.TRANCAMENTO.getValor())
				|| trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.RENOVACAO_AUTOMATICA.getValor())){
			trancamentoVO.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor());
			origem = OrigemFechamentoMatriculaPeriodoEnum.TRANCAMENTO;
		} else if (trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.JUBILAMENTO.getValor())){
			trancamentoVO.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.JUBILADO.getValor());
			origem = OrigemFechamentoMatriculaPeriodoEnum.JUBILAMENTO;
		}
		
		if((Uteis.isAtributoPreenchido(trancamentoVO.getMatriculaPeriodoVO().getCodigo()) 
				&& Uteis.isAtributoPreenchido(trancamentoVO.getUltimaMatriculaPeriodoVO().getCodigo())
				&& trancamentoVO.getMatriculaPeriodoVO().getCodigo().equals(trancamentoVO.getUltimaMatriculaPeriodoVO().getCodigo()))
				|| (Uteis.isAtributoPreenchido(trancamentoVO.getMatriculaPeriodoVO().getCodigo()) && 
					(trancamentoVO.getAno()+trancamentoVO.getSemestre()).compareTo(trancamentoVO.getUltimaMatriculaPeriodoVO().getAno()+trancamentoVO.getUltimaMatriculaPeriodoVO().getSemestre()) > 0)){
			trancamentoVO.getMatricula().setSituacao(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo());
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(trancamentoVO.getMatricula().getMatricula(), trancamentoVO.getMatricula().getSituacao(), usuario);			
			
			if (trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.ABANDONO_DE_CURSO.getValor())) {
				getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaParaIndicarAbandonoCurso(trancamentoVO.getMatricula().getMatricula(), true);
			}
		}
		if(Uteis.isAtributoPreenchido(trancamentoVO.getCodigoRequerimento().getCodigo())){
			getFacadeFactory().getRequerimentoFacade().alterarSituacao(trancamentoVO.getCodigoRequerimento().getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), "", trancamentoVO.getCodigoRequerimento().getMotivoDeferimento(),trancamentoVO.getCodigoRequerimento(), usuario);
		}
		alterarSituacaoAcademicaMatriculaPeriodo(trancamentoVO.getMatriculaPeriodoVO(), trancamentoVO.getHistoricoVOs(), trancamentoVO.getData(), origem, trancamentoVO.getCodigo(), usuario);
		getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluirTrancamentoOrJubilamentotoAluno(trancamentoVO, usuario, configuracaoGeralSistemaVO);
		if(!realizandoTrancamentoAutomatico && trancamentoVO.getHistoricoVOs().isEmpty()){
			StringBuilder sql  = new StringBuilder("UPDATE historico set situacao = '");
			sql.append(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo()).append("' ");
			sql.append(" where matriculaperiodo = ").append(trancamentoVO.getMatriculaPeriodoVO().getCodigo());
			sql.append(" and situacao in ('TR', 'AC') ");
			getConexao().getJdbcTemplate().execute(sql.toString());			
		} else if (realizandoTrancamentoAutomatico) {
			StringBuilder sql  = new StringBuilder("UPDATE historico set situacao = '");
			sql.append(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo()).append("' ");
			sql.append(" where matriculaperiodo = ").append(trancamentoVO.getMatriculaPeriodoVO().getCodigo());
			sql.append(" and situacao in ('CS', 'CE') ");
			getConexao().getJdbcTemplate().execute(sql.toString());
		}
		
		if(trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.JUBILAMENTO.getValor())) {
			realizarInativacaoUsuarioLdap(trancamentoVO.getMatriculaPeriodoVO(), true, usuario);	
		}
		realizarInativacaoBlackboardPessoaEmailInstitucional(trancamentoVO.getMatriculaPeriodoVO(), true, usuario);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirEDeferirRequerimento(final TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO, Boolean realizandoTrancamentoAutomatico) throws Exception {
		incluir(obj, configuracaoGeralSistemaVO, usuarioVO, realizandoTrancamentoAutomatico);
//		alterarSituacaoAcademicaMatriculaTrancamento(obj, configuracaoGeralSistemaVO, usuarioVO, );
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(final Integer trancamento, final String situacao) throws Exception {
		final String sql = "UPDATE Trancamento set situacao=? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, situacao);
				sqlAlterar.setInt(2, trancamento.intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Responsável por realizar uma consulta de <code>Trancamento</code> através do valor do atributo <code>nome</code> da classe <code>Pessoa</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
//	public List<TrancamentoVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT Trancamento.* FROM Trancamento, Usuario WHERE Trancamento.responsavelAutorizacao = Usuario.codigo and lower (Usuario.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Usuario.nome";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
//	}

//	public List<TrancamentoVO> consultarPorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuilder sqlStr = new StringBuilder("select distinct on (tr.matricula) tr.* from trancamento tr ");
//		sqlStr.append(" inner join matriculaperiodo mp on tr.matricula = mp.matricula ");
//		sqlStr.append(" inner join turma tu on mp.turma = tu.codigo ");
//		sqlStr.append(" where lower(tu.identificadorturma) like '").append(valorConsulta.toLowerCase()).append("%' ");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
//	}

	/**
	 * Responsável por realizar uma consulta de <code>Trancamento</code> através do valor do atributo <code>String tipoJustificativa</code>. Retorna
	 * os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
//	public List<TrancamentoVO> consultarPorTipoJustificativa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM Trancamento inner join motivoCancelamentoTrancamento on motivoCancelamentoTrancamento.codigo = trancamento.motivoCancelamentoTrancamento WHERE motivoCancelamentoTrancamento.nome ilike('" + valorConsulta + "%') ORDER BY motivoCancelamentoTrancamento.nome";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
//	}

	/**
	 * Responsável por realizar uma consulta de <code>Trancamento</code> através do valor do atributo <code>Integer codigoRequerimento</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
//	public List<TrancamentoVO> consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM Trancamento WHERE codigoRequerimento >= " + valorConsulta.intValue() + " ORDER BY codigoRequerimento";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
//	}

	/**
	 * Responsável por realizar uma consulta de <code>Trancamento</code> através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
//	public List<TrancamentoVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT Trancamento.* FROM Trancamento, Matricula WHERE Trancamento.matricula = Matricula.matricula and upper(Matricula.matricula) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Matricula.matricula";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
//	}

//	public List<TrancamentoVO> consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT trancamento.* FROM trancamento inner join matricula on matricula.matricula = trancamento.matricula inner join pessoa on pessoa.codigo = matricula.aluno WHERE upper (pessoa.nome) like('" + valorConsulta.toUpperCase() + "%') ORDER BY pessoa.nome";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
//	}

//	public List<TrancamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE sem_acentos(p.nome) iLIKE sem_acentos(?) ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), "%"+valorConsulta+"%");
//		return montarDadosConsultaBasica(resultado);
//	}
	
//	@Override
//	public List<TrancamentoVO> consultaRapidaPorRegistroAcademicoAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE sem_acentos(p.registroAcademico) iLIKE sem_acentos(?) ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), "%"+valorConsulta+"%");
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<TrancamentoVO> consultaRapidaPorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE UPPER(tu.identificadorturma) LIKE UPPER('" + valorConsulta.toUpperCase() + "%') ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<TrancamentoVO> consultaRapidaPorTipoJustificativa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE UPPER(motivoCancelamentoTrancamento.tipojustificativa) LIKE UPPER('" + valorConsulta.toUpperCase() + "%') ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<TrancamentoVO> consultaRapidaPorCodigoRequerimento(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE req.codigo >= " + valorConsulta + " ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<TrancamentoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE t.codigo >= " + valorConsulta + " ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

//	public TrancamentoVO consultaRapidaPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO, ) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
//		StringBuilder sqlStr = new StringBuilder("");
//		sqlStr.append("SELECT * FROM Trancamento ");
//		sqlStr.append("WHERE matricula = '").append(valorConsulta).append("' ");
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		return montarDados(resultado, nivelMontarDados, usuarioVO);
//	}

	public List<TrancamentoVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(t.matricula) LIKE '" + valorConsulta.toUpperCase() + "%' ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		sql.append("order by t.codigo");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

//	public List<TrancamentoVO> consultaRapidaPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE UPPER(t.situacao) LIKE '" + valorConsulta.toUpperCase() + "%' ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}
//
//	public List<TrancamentoVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE t.data >= '" + Uteis.getDataJDBC(prmIni) + "' AND t.data <= " + Uteis.getDataJDBC(prmFim) + "' ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

	public List<TrancamentoVO> consultarPorMatriculaSituacaoFinanceira(String matricula, String situacaoFinanceira, Integer limite, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT trancamento.* FROM trancamento");
		sqlStr.append(" INNER JOIN requerimento ON requerimento.codigo = trancamento.codigorequerimento");
		sqlStr.append(" WHERE trancamento.matricula = '" + matricula + "' AND requerimento.situacaofinanceira = '" + situacaoFinanceira + "'");
		sqlStr.append(" ORDER BY trancamento.data DESC");
		if (limite.intValue() > 0) {
			sqlStr.append(" LIMIT " + limite);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Trancamento</code> através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
//	public List<TrancamentoVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM Trancamento, Requerimento WHERE Trancamento.codigoRequerimento  = Requerimento.codigo and upper (Requerimento.situacao) = '" + valorConsulta.toUpperCase() + "' ORDER BY Requerimento.situacao";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
//	}

	/**
	 * Responsável por realizar uma consulta de <code>Trancamento</code> através do valor do atributo <code>Date data</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
//	public List<TrancamentoVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM Trancamento WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
//	}

//	public List<TrancamentoVO> consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM Trancamento " + "LEFT JOIN matricula ON trancamento.matricula = matricula.matricula " + "LEFT JOIN unidadeEnsino ON matricula.unidadeEnsino = unidadeEnsino.codigo " + "WHERE ((Trancamento.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (Trancamento.data <= '" + Uteis.getDataJDBC(prmFim) + "')) AND unidadeEnsino.nome = '" + unidadeEnsino + "' ORDER BY Trancamento.data";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
//	}

	public TrancamentoVO consultarPorCodigoRequerimento(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Trancamento WHERE codigoRequerimento = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return new TrancamentoVO();
			// throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Trancamento</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos
	 * com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
//	public List<TrancamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM Trancamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
//	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>TrancamentoVO</code> resultantes da consulta.
	 */
	public  List<TrancamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TrancamentoVO> vetResultado = new ArrayList<TrancamentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TrancamentoVO</code>.
	 *
	 * @return O objeto da classe <code>TrancamentoVO</code> com os dados devidamente montados.
	 */
	public  TrancamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TrancamentoVO obj = new TrancamentoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getCodigoRequerimento().setCodigo(new Integer(dadosSQL.getInt("codigoRequerimento")));
		obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
		obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
//		obj.setTrancamentoRelativoAbondonoCurso(dadosSQL.getBoolean("trancamentoRelativoAbondonoCurso"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaperiodo"));
		obj.setDataPossivelRetorno(dadosSQL.getDate("dataPossivelRetorno"));
		obj.setDataEstorno(dadosSQL.getDate("dataestorno"));
		obj.getResponsavelEstorno().setCodigo(new Integer(dadosSQL.getInt("responsavelestorno")));
		obj.setTipoTrancamento(dadosSQL.getString("tipotrancamento"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		
		montarDadosRequerimento(obj, nivelMontarDados, usuario);
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelEstorno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setJustificativa(dadosSQL.getString("justificativa"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		
		return obj;
	}

	public  void montarDadosRequerimento(TrancamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCodigoRequerimento().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>TrancamentoVO</code>. Faz uso
	 * da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosResponsavelAutorizacao(TrancamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>TrancamentoVO</code> . Faz
	 * uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosMatricula(TrancamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>TrancamentoVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public TrancamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Trancamento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>TrancamentoVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public void validarDados(TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getData())) {
			throw new ConsistirException("O campo DATA (Trancamento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			throw new ConsistirException("O campo MATRÍCULA (Trancamento) deve ser informado.");
		}
		if (!configuracaoGeralSistemaVO.getPermiteTrancamentoSemRequerimento()) {
			if (!Uteis.isAtributoPreenchido(obj.getCodigoRequerimento())) {
				throw new ConsistirException("O campo CÓDIGO REQUERIMENTO (Trancamento) deve ser informado.");
			}
		}
		if (!obj.getMatricula().getCurso().getPeriodicidade().equals("IN") && (obj.getAno().trim().isEmpty() || obj.getAno().trim().length() != 4)) {
			throw new ConsistirException("O campo ANO (Trancamento) deve ser informado (Ex: "+Uteis.getAnoDataAtual4Digitos()+").");
		}
		if (obj.getMatricula().getCurso().getPeriodicidade().equals("SE") 
				&& (obj.getSemestre().trim().isEmpty() || (!obj.getSemestre().trim().equals("1") && !obj.getSemestre().trim().equals("2")))) {
			throw new ConsistirException("O campo SEMESTRE (Trancamento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getJustificativa())) {
			throw new ConsistirException("O campo JUSTIFICATIVA (Trancamento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento()) && !Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getMotivoPadraoAbandonoCurso())) {
			throw new ConsistirException("Para que seja realizado a operação, o campo Motivo Cancelamento/Trancamento deve ser informado, Caso o contrário é necessário que seja informado o Motivo Padrão de Evasão/Trancamento de curso, dentro da configuração geral do sistema.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento())) {
			throw new ConsistirException("O campo MOTIVO TRANCAMENTO (Trancamento) deve ser informado.");
		}
		if (obj.getTipoTrancamento() == null || obj.getTipoTrancamento().equals("")) {
			throw new ConsistirException("O campo TIPO TRANCAMENTO deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return Trancamento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Trancamento.idEntidade = idEntidade;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT count(*) over() qtdeRegistro, t.codigo, t.motivoCancelamentoTrancamento, t.situacao, t.data, m.matricula, t.tipotrancamento, p.nome as nomealuno, p.registroAcademico as registroAcademico , tu.identificadorturma, resp.codigo as codigoresponsavel, resp.nome AS nomeresponsavel, mp.ano, mp.semestre, motivoCancelamentoTrancamento.nome as motivoCancelamentoTrancamento_nome ");
		sql.append("FROM trancamento t ");
		sql.append("LEFT JOIN matricula m ON m.matricula = t.matricula ");
		sql.append("LEFT JOIN motivoCancelamentoTrancamento ON motivoCancelamentoTrancamento.codigo = t.motivoCancelamentoTrancamento ");
		sql.append("LEFT JOIN pessoa p ON p.codigo = m.aluno ");
		sql.append("LEFT JOIN matriculaperiodo mp ON mp.matricula = t.matricula and mp.codigo = t.matriculaperiodo ");
		sql.append("LEFT JOIN turma tu ON tu.codigo = mp.turma ");
		sql.append("LEFT JOIN usuario resp ON resp.codigo = t.responsavelautorizacao ");
		sql.append("LEFT JOIN requerimento req ON req.codigo = t.codigorequerimento ");
		return sql;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.*, m.matricula AS matriculaAluno, p.codigo AS codigoPessoa, p.nome AS nomePessoa, req.codigo AS codigoRequerimento, resp.codigo AS codigoResponsavel, resp.nome AS nomeResponsavel, mp.situacaomatriculaperiodo ");
		sql.append("FROM trancamento t ");
		sql.append("LEFT JOIN matricula m ON m.matricula = t.matricula ");
		sql.append("LEFT JOIN matriculaperiodo mp ON mp.codigo = t.matriculaperiodo ");
		sql.append("LEFT JOIN pessoa p ON p.codigo = m.aluno ");
		sql.append("LEFT JOIN usuario resp ON resp.codigo = t.responsavelAutorizacao ");
		sql.append("LEFT JOIN requerimento req ON req.codigo = t.codigorequerimento ");
		return sql;
	}

	public void carregarDados(TrancamentoVO obj,  UsuarioVO usuario) throws Exception {
		carregarDados(obj, NivelMontarDados.TODOS, usuario);
	}

	public void carregarDados(TrancamentoVO obj, NivelMontarDados nivelMontarDados,  UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), Boolean.FALSE, usuario);
			montarDadosBasico(obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), Boolean.FALSE, usuario);
			montarDadosCompleto(obj, resultado, usuario);
		}
	}

	private void montarDadosBasico(TrancamentoVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
		obj.getMotivoCancelamentoTrancamento().setNome(dadosSQL.getString("motivoCancelamentoTrancamento_nome"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setTipoTrancamento(dadosSQL.getString("tipotrancamento"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomealuno"));
		obj.getMatricula().getAluno().setRegistroAcademico(dadosSQL.getString("registroAcademico"));
		obj.setTurma(dadosSQL.getString("identificadorturma"));
		obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("nomeresponsavel"));
		obj.getMatriculaPeriodoVO().setAno(dadosSQL.getString("ano"));
		obj.getMatriculaPeriodoVO().setSemestre(dadosSQL.getString("semestre"));
	}

	private void montarDadosCompleto(TrancamentoVO obj, SqlRowSet dadosSQL,  UsuarioVO usuario) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getCodigoRequerimento().setCodigo(dadosSQL.getInt("codigoRequerimento"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
//		obj.setTrancamentoRelativoAbondonoCurso(dadosSQL.getBoolean("trancamentorelativoabandonocurso"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("codigoPessoa"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomePessoa"));
		obj.getCodigoRequerimento().setCodigo(dadosSQL.getInt("codigoRequerimento"));
		obj.getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("codigoResponsavel"));
		obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("nomeResponsavel"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaperiodo"));
		obj.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(dadosSQL.getString("situacaomatriculaperiodo"));
		obj.setTipoTrancamento(dadosSQL.getString("tipotrancamento"));
	}

	public List<TrancamentoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<TrancamentoVO> vetResultado = new ArrayList<TrancamentoVO>(0);
		while (tabelaResultado.next()) {
			TrancamentoVO obj = new TrancamentoVO();
			montarDadosBasico(obj, tabelaResultado); 
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TrancamentoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado,  UsuarioVO usuario) throws Exception {
		List<TrancamentoVO> vetResultado = new ArrayList<TrancamentoVO>(0);
		while (tabelaResultado.next()) {
			TrancamentoVO obj = new TrancamentoVO();
			montarDadosCompleto(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (t.codigo = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Trancamento ).");
		}
		tabelaResultado.beforeFirst();
		return tabelaResultado;
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaCompleta();
		sql.append(" WHERE (t.codigo = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Trancamento ).");
		}
		tabelaResultado.beforeFirst();
		return tabelaResultado;
	}
	
	@Override
	public void validarDadosAntesImpressao(TrancamentoVO obj, Integer textoPadrao) throws Exception {		
		if (obj.getMatricula() == null || !Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			throw new Exception("O Aluno deve ser informado para geração do relatório.");
		}
		if(!Uteis.isAtributoPreenchido(textoPadrao)){
			throw new Exception("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}

	@Override
	public String imprimirDeclaracaoTrancamento(TrancamentoVO trancamentoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO config,  UsuarioVO usuario) throws Exception {
		String caminhoRelatorio = "";
		try {			
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContratoVO.setGerarNovoArquivoAssinado(true);
			impressaoContratoVO.setMatriculaVO(trancamentoVO.getMatricula());
			impressaoContratoVO.getMatriculaVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(trancamentoVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(impressaoContratoVO.getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
			impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(trancamentoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.setTurmaVO(impressaoContratoVO.getMatriculaPeriodoVO().getTurma());
			impressaoContratoVO.setTextoPadraoDeclaracao(textoPadraoDeclaracaoVO.getCodigo());
			impressaoContratoVO.setTrancamentoVO(trancamentoVO);
			String textoStr = "";
			textoPadraoDeclaracaoVO.substituirTag(impressaoContratoVO, usuario);
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(textoPadraoDeclaracaoVO.getTexto());
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(textoStr, textoPadraoDeclaracaoVO.getOrientacaoDaPagina());
			if (textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isHtml()){
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoStr);
			}
//			else {
//				caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracaoVO, "", true, config, usuario);
//				getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
//			}
			return caminhoRelatorio;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Date consultarDataTrancamentoPorMatricula(String matricula, UsuarioVO usuarioVO, Boolean filtrarTipoRetorno) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT data FROM Trancamento where matricula = '").append(matricula).append("' order by data desc limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getDate("data");
		}
		if(filtrarTipoRetorno) {
			return null;
		}
		return new Date();
	}

	/**
	 * Responsável por executar a persistência dos dados pertinentes a TrancamentoVO.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @param configuracaoGeralSistemaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception {
		if (obj.isNovoObj()) {
			incluir(obj, configuracaoGeralSistemaVO, usuarioVO, false);
		} else {
			alterar(obj, configuracaoGeralSistemaVO, usuarioVO);
		}		
	}

	/**
	 * Responsável por executar a montagem dos históricos para realizar alteração da situação de acordo com a ultima matrícula período cuja situação
	 * seja AT ou PR.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<HistoricoVO> executarMontagemHistoricosParaRealizarAlteracaoSituacao(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception {
		if(matriculaPeriodoVO == null || !Uteis.isAtributoPreenchido(matriculaPeriodoVO.getCodigo())){
			matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(matriculaVO.getMatricula(), "'AT', 'PR'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		}
		String[] situacaoHistoricoDesconsiderar = { "AA", "CC", "CH", "IS" };
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoGradeCurricularAtual(matriculaPeriodoVO.getCodigo(), matriculaVO.getGradeCurricularAtual().getCodigo(), situacaoHistoricoDesconsiderar, false, usuarioVO);
		for (HistoricoVO obj : historicoVOs) {
			obj.setRealizarAlteracaoSituacaoHistorico(obj.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor()) || obj.getSituacao().equals(SituacaoHistorico.TRANCAMENTO.getValor())  || obj.getSituacao().equals(SituacaoHistorico.ABANDONO_CURSO.getValor()));
			obj.setEditavel(!obj.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor()));
		}
		return historicoVOs;
	}
	
	/**
	 * Responsável por realizar a validação da situação do requerimento.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @throws ConsistirException
	 */
	@Override
	public void validarSituacaoRequerimento(RequerimentoVO obj) throws ConsistirException {
		if (obj.getSituacao().equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor())) {
			throw new ConsistirException("Requerimento especificado está aguardando pagamento.");
		}
		if (!obj.getMatricula().getSituacao().equals("AT") && !obj.getMatricula().getSituacao().equals("CF")) {
			throw new ConsistirException("Matrícula especificada não está ativa.");
		}
		if (obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor()) || obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
			throw new ConsistirException("Requerimento especificado já está finalizado.");
		}
	}

	/**
	 * Responsável por executar a criação da MatriculaDW.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param processoMatricula
	 * @param matriculaVO
	 * @param situacaoMatricula
	 * @param data
	 * @param peso
	 * @return
	 */
//	private MatriculaDWVO criarMatriculaDW(Integer processoMatricula, MatriculaVO matriculaVO, String situacaoMatricula, Date data, Integer peso) {
//		MatriculaDWVO obj = new MatriculaDWVO();
//		obj.setAno(Uteis.getAnoData(data));
//		obj.setMes(Uteis.getMesData(data));
//		obj.getCurso().setCodigo(matriculaVO.getCurso().getCodigo());
//		obj.getAreaConhecimento().setCodigo(matriculaVO.getCurso().getAreaConhecimento().getCodigo());
//		obj.setData(data);
//		obj.setNivelEducacional(matriculaVO.getCurso().getNivelEducacional());
//		obj.getProcessoMatricula().setCodigo(processoMatricula);
//		obj.setSituacao(situacaoMatricula);
//		obj.setPeso(peso);
//		obj.getTurno().setCodigo(matriculaVO.getTurno().getCodigo());
//		obj.getUnidadeEnsino().setCodigo(matriculaVO.getUnidadeEnsino().getCodigo());
//		return obj;
//	}	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarVerificacaoEGeracaoMatriculaPeriodoSemestrePosteriorMapaRegistroAbandonoCursoTrancamento(TrancamentoVO trancamentoVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception {
		if (!getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoExisteMatriculaPeriodoRenovadaPorAnoSemestreEvasao(matriculaPeriodoVO.getCodigo(), trancamentoVO.getAno(), trancamentoVO.getSemestre(), false, usuarioVO)) {
			realizarMontagemMatriculaPeriodoEvasao(trancamentoVO, matriculaPeriodoVO, usuarioVO);
		} else {
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaSemestreAno(matriculaPeriodoVO.getMatricula(), trancamentoVO.getSemestre(), trancamentoVO.getAno(), false, NivelMontarDados.BASICO.getValor(), usuarioVO);
			matriculaPeriodo.setFinanceiroManual(true);
			matriculaPeriodo.setDataFechamentoMatriculaPeriodo(new Date());
			matriculaPeriodo.setOrigemFechamentoMatriculaPeriodo(OrigemFechamentoMatriculaPeriodoEnum.ABANDONO);
			matriculaPeriodo.setOrigemMapaRegistroTrancamentoAbandono(true);
			trancamentoVO.setMatriculaPeriodoVO(matriculaPeriodo);
		}
	}
	
	private void realizarMontagemMatriculaPeriodoEvasao(TrancamentoVO trancamentoVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception {
		MatriculaPeriodoVO obj = new MatriculaPeriodoVO();
		obj.setNovoObj(true);
		obj.setTurma(matriculaPeriodoVO.getTurma());
		obj.setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarProximoPeriodoLetivoCurso(matriculaPeriodoVO.getMatriculaVO().getGradeCurricularAtual().getCodigo(), matriculaPeriodoVO.getMatriculaVO().getCurso().getCodigo(), matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo().intValue() + 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		if (!Uteis.isAtributoPreenchido(obj.getPeridoLetivo())) {
			obj.setPeridoLetivo(matriculaPeriodoVO.getPeriodoLetivo());
		}
		obj.setGradeCurricular(matriculaPeriodoVO.getMatriculaVO().getGradeCurricularAtual());
		obj.setMatricula(matriculaPeriodoVO.getMatricula());
		obj.setMatriculaVO(matriculaPeriodoVO.getMatriculaVO());
		obj.setSituacao("CO");
		obj.setData(new Date());
		obj.setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCodigo());
		obj.setUnidadeEnsinoCursoVO(matriculaPeriodoVO.getUnidadeEnsinoCursoVO());
		obj.setAno(trancamentoVO.getAno());
		obj.setSemestre(trancamentoVO.getSemestre());
		obj.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.ATIVA.getValor());
		obj.setFinanceiroManual(true);
		obj.setDataFechamentoMatriculaPeriodo(new Date());
		obj.setOrigemFechamentoMatriculaPeriodo(OrigemFechamentoMatriculaPeriodoEnum.ABANDONO);
		obj.setOrigemMapaRegistroTrancamentoAbandono(true);
//		ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorUnidadeEnsinoCursoAnoSemestre(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCodigo(), obj.getAno(), obj.getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, false, usuarioVO, TipoAlunoCalendarioMatriculaEnum.VETERANO);
//		Uteis.checkState(!Uteis.isAtributoPreenchido(processoMatriculaCalendarioVO.getProcessoMatricula()), "Nenhum Calendário de Matrícula Curso (Calendário de Matrícula) definido para o ano de " + obj.getAno() + " no semestre " + obj.getSemestre() + " do trancamento.");
//		obj.setProcessoMatriculaCalendarioVO(processoMatriculaCalendarioVO);
		obj.setProcessoMatricula(obj.getProcessoMatriculaCalendarioVO().getProcessoMatricula());
		obj.getProcessoMatriculaVO().setCodigo(obj.getProcessoMatriculaCalendarioVO().getProcessoMatricula());
		getFacadeFactory().getMatriculaPeriodoFacade().incluir(obj, obj.getMatriculaVO(), obj.getProcessoMatriculaCalendarioVO(), usuarioVO);
		trancamentoVO.setMatriculaPeriodoVO(obj);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTrancamentoMapaRegistroAbandonoCursoTrancamento(List<MatriculaPeriodoVO> matriculaPeriodoVOs, String tipoTrancamento, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception {
		Map<Integer, ConfiguracaoGeralSistemaVO> configuracaoGeralSistemaVOs = new HashMap<Integer, ConfiguracaoGeralSistemaVO>(0);
//		Map<Integer> configuracaoFinanceiroVOs = new HashMap<Integer>(0);
		for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
			if (matriculaPeriodoVO.getMatriculaVO().getAlunoSelecionado()) {
				progressBarVO.setProgresso(progressBarVO.getProgresso() + 1);
				progressBarVO.setStatus("Gerando Abandono Curso/Trancamento da Matrícula n° " + matriculaPeriodoVO.getMatriculaVO().getMatricula() + " (" + progressBarVO.getProgresso() + " de " + progressBarVO.getMaxValue() + ")");
				if (!configuracaoGeralSistemaVOs.containsKey(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo())) {
					configuracaoGeralSistemaVOs.put(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
				}
//				if (!configuracaoFinanceiroVOs.containsKey(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo())) {
//					configuracaoFinanceiroVOs.put(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
//				}
				//persistirTrancamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(matriculaPeriodoVO, tipoTrancamento, configuracaoGeralSistemaVOs.get(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo())s.get(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()), usuarioVO);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTrancamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception {
		mrecmp.setTrancamentoVO(new TrancamentoVO());
		mrecmp.getTrancamentoVO().setNovoObj(true);
		mrecmp.getTrancamentoVO().setResponsavelAutorizacao(usuarioVO);
		mrecmp.getTrancamentoVO().setJustificativa("Originado pelo Mapa Registro Evasão Curso - "+mrecmp.getMapaRegistroEvasaoCursoVO().getJustificativa());
		mrecmp.getTrancamentoVO().setCodigoRequerimento(null);
		mrecmp.getTrancamentoVO().setMatricula(mrecmp.getMatriculaPeriodoVO().getMatriculaVO());
		mrecmp.getTrancamentoVO().setData(new Date());
		mrecmp.getTrancamentoVO().setDescricao("Originado pelo Mapa Registro Evasão Curso");
		mrecmp.getTrancamentoVO().setTipoTrancamento(mrecmp.getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().getValor());		
		mrecmp.getTrancamentoVO().setSituacao("FD");
		mrecmp.getTrancamentoVO().setDataPossivelRetorno(new Date());
		mrecmp.getTrancamentoVO().setMotivoCancelamentoTrancamento(mrecmp.getMapaRegistroEvasaoCursoVO().getMotivoCancelamentoTrancamento());
		mrecmp.getTrancamentoVO().setAno(mrecmp.getMapaRegistroEvasaoCursoVO().getAnoRegistroEvasao());
		mrecmp.getTrancamentoVO().setSemestre(mrecmp.getMapaRegistroEvasaoCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor()) ? mrecmp.getMapaRegistroEvasaoCursoVO().getSemestreRegistroEvasao() : Constantes.EMPTY);
		executarVerificacaoEGeracaoMatriculaPeriodoSemestrePosteriorMapaRegistroAbandonoCursoTrancamento(mrecmp.getTrancamentoVO(), mrecmp.getMatriculaPeriodoVO(), usuarioVO);		
		configuracaoGeralSistemaVO.setPermiteTrancamentoSemRequerimento(true);
		mrecmp.getTrancamentoVO().setUltimaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(mrecmp.getTrancamentoVO().getMatricula().getMatricula(), "'AT', 'PR', 'PC', 'AC', 'TR', 'ER'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		persistir(mrecmp.getTrancamentoVO(), configuracaoGeralSistemaVO, usuarioVO);
		
	}
	
	/**
	 * Responsável por executar o estorno do trancamento de matrícula
	 * 
	 * @author Alessandro - 22/01/2016
	 * @param trancamentoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEstorno(TrancamentoVO trancamentoVO, UsuarioVO usuarioVO) throws Exception {
		realizarEstornoTrancamento(trancamentoVO, usuarioVO);
		Integer ultimaMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaCodigoUltimaMatriculaPeriodoPorMatricula(trancamentoVO.getMatricula().getMatricula(), false, usuarioVO);
		if(ultimaMatriculaPeriodo.equals(trancamentoVO.getMatriculaPeriodoVO().getCodigo())) {
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaEstornoTrancamento(trancamentoVO.getMatricula().getMatricula(), usuarioVO);
		}
		getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoEstornoTrancamento(trancamentoVO.getMatriculaPeriodoVO().getCodigo(), usuarioVO);
		List <HistoricoVO> historicos = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaTrancadaAbandonadaJubilada(trancamentoVO.getMatriculaPeriodoVO().getCodigo(), usuarioVO);
		if (!historicos.isEmpty()) {
			for(HistoricoVO historicoVO: historicos){
				if(historicoVO.getSituacao().equals(SituacaoHistorico.TRANCAMENTO.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.ABANDONO_CURSO.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.JUBILADO.getValor())){
					historicoVO.setSituacao("CS");
				}
			}
			getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(historicos, false, true, usuarioVO);
		}
		if(trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.JUBILAMENTO.getValor())) {
			realizarAtivacaoUsuarioLdap(trancamentoVO, usuarioVO);
		}
		realizarAtivacaoBlackboardPessoaEmailInstitucional(trancamentoVO, usuarioVO);
		getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluirEstornarTrancamentoOrJubilamentotoAluno(trancamentoVO, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO));
	}

	private void realizarAtivacaoBlackboardPessoaEmailInstitucional(TrancamentoVO trancamentoVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO())) {
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorMatricula(trancamentoVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtivacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuarioVO);
				if(pessoaEmailInstitucionalVO.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.INATIVO)) {
					pessoaEmailInstitucionalVO.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
					getFacadeFactory().getPessoaEmailInstitucionalFacade().alterarSituacaoStatusAtivoInativoEnum(pessoaEmailInstitucionalVO, usuarioVO);
				}
			}		
		}
	}

	private void realizarAtivacaoUsuarioLdap(TrancamentoVO trancamentoVO, UsuarioVO usuarioVO) throws Exception {
		UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(trancamentoVO.getMatricula().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(usuario)) {
			if(trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.JUBILAMENTO.getValor()) && Uteis.isAtributoPreenchido(usuario.getPessoa().getRegistroAcademico())) {
				getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( usuario, usuario.getPessoa().getRegistroAcademico(),usuario.getPessoa().getRegistroAcademico(),false, usuarioVO);
				usuario.setUsername(usuario.getPessoa().getRegistroAcademico());
				getFacadeFactory().getUsuarioFacade().alterarBooleanoResetouSenhaPrimeiroAcesso(false, usuario, false, usuario);	
			}
			JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuario, trancamentoVO.getMatricula(), true,usuarioVO);
			Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
			jobSincronizarCancelamento.start();
		}
	}
	
	private void realizarEstornoTrancamento(final TrancamentoVO trancamentoVO, final UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sqlStr = new StringBuilder("update trancamento as ta set situacao = 'ES', responsavelEstorno = ?, dataEstorno = ? from (");
		sqlStr.append("select t.codigo from trancamento as t ");
		sqlStr.append("inner join matriculaperiodo as mp on mp.codigo = t.matriculaperiodo and mp.situacaomatriculaperiodo in ('TR','AC', 'JU') ");
		sqlStr.append("where t.matricula = ? and t.situacao = 'FD' and mp.codigo = ? ");
		sqlStr.append(" and not exists (select mpa.codigo from matriculaperiodo mpa where mpa.ano = mp.ano and mpa.semestre = mp.semestre and mpa.matricula = mp.matricula and mpa.codigo != mp.codigo and mpa.situacaomatriculaperiodo not in ('AT', 'FO', 'FI', 'PR', 'CO')) ");
		sqlStr.append("order by t.data desc limit 1");
		sqlStr.append(") as x where ta.codigo = x.codigo");
		int alterados = getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				sqlAlterar.setInt(1, trancamentoVO.getResponsavelEstorno().getCodigo());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(trancamentoVO.getDataEstorno()));
				sqlAlterar.setString(3, trancamentoVO.getMatricula().getMatricula());
				sqlAlterar.setInt(4, trancamentoVO.getMatriculaPeriodoVO().getCodigo());
				return sqlAlterar;
			}
		});
		if (alterados < 1) {
			throw new Exception("Matrícula do trancamento não atende condições para estorno, verifique se o trancamento não tenha sido estornado ou se não existe outra matricula periodo com a situação Ativa, Formada ou Finalizada neste mesmo ano/semestre!");
		}
	}
	
	
	@Override
	public void realizarDefinicaoMatriculaPeriodoRealizarTrancamento(TrancamentoVO trancamentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception{
		validarDados(trancamentoVO, configuracaoGeralSistemaVO);
		trancamentoVO.setMensagemConfirmacao("");
		trancamentoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(trancamentoVO.getMatricula().getMatricula(), trancamentoVO.getSemestre(), trancamentoVO.getAno(),false, false, Optional.ofNullable(null), Optional.ofNullable(null), usuarioVO));
		String anoSemestre = trancamentoVO.getMatricula().getCurso().getPeriodicidade().equals("IN") ? "" : trancamentoVO.getAno() + (trancamentoVO.getMatricula().getCurso().getPeriodicidade().equals("SE")?"/"+trancamentoVO.getSemestre():"");
		if(Uteis.isAtributoPreenchido(trancamentoVO.getMatriculaPeriodoVO().getCodigo())){
			if(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor()) 
					|| trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor())
					|| trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor())					
					|| trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor())){
				throw new Exception(UteisJSF.internacionalizar("msg_Trancamento_matriculaPeriodoSituacaoInapta").replace("{0}", anoSemestre).replace("{1}", SituacaoMatriculaPeriodoEnum.getEnumPorValor(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo()).getDescricao()));
			}else  if(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor())){
				if(trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.ABANDONO_DE_CURSO.getValor())){
					trancamentoVO.setMensagemConfirmacao(UteisJSF.internacionalizar("msg_Trancamento_matriculaPeriodoTrancamentoMudarAbandono").replace("{0}", anoSemestre));
				}else{
					throw new Exception(UteisJSF.internacionalizar("msg_Trancamento_matriculaPeriodoJaTrancada").replace("{0}", anoSemestre));
				}
			}else if(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getValor())){
				if(!trancamentoVO.getTipoTrancamento().equals(TipoTrancamentoEnum.ABANDONO_DE_CURSO.getValor())){
					trancamentoVO.setMensagemConfirmacao(UteisJSF.internacionalizar("msg_Trancamento_matriculaPeriodoAbandonoMudarTrancamento").replace("{0}", anoSemestre));
				}else{
					throw new Exception(UteisJSF.internacionalizar("msg_Trancamento_matriculaPeriodoJaAbandonda").replace("{0}", anoSemestre));
				}
			}else if(trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
					|| trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())					
					|| trancamentoVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA_CANCELADA.getValor())){
			}	
			
		}else{
			trancamentoVO.setMensagemConfirmacao(UteisJSF.internacionalizar("msg_Trancamento_matriculaPeriodoInexistente").replace("{0}", anoSemestre));
		}
		
		trancamentoVO.setUltimaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(trancamentoVO.getMatricula().getMatricula(), "'AT', 'PR', 'PC', 'AC', 'TR', 'ER', 'FI', 'TS', 'CA', 'TI'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		if((trancamentoVO.getAno()+trancamentoVO.getSemestre()).compareTo(trancamentoVO.getUltimaMatriculaPeriodoVO().getAno()+trancamentoVO.getUltimaMatriculaPeriodoVO().getSemestre()) > 0 
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoAtiva() && !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoPreMatricula()
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA_CANCELADA.getValor())
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor())
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.INDEFINIDA.getValor())
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor())
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())	
				&& !trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getValor())){
			throw new Exception(UteisJSF.internacionalizar("msg_Trancamento_matriculaPeriodoSituacaoInapta").replace("{0}", trancamentoVO.getUltimaMatriculaPeriodoVO().getAno()+"/"+trancamentoVO.getUltimaMatriculaPeriodoVO().getSemestre()).replace("{1}", SituacaoMatriculaPeriodoEnum.getEnumPorValor(trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoMatriculaPeriodo()).getDescricao()));
		}
		if(((trancamentoVO.getUltimaMatriculaPeriodoVO().getCodigo().equals(trancamentoVO.getMatriculaPeriodoVO().getCodigo())) || 
				(!Uteis.isAtributoPreenchido(trancamentoVO.getMatriculaPeriodoVO().getCodigo()) 
			&& (trancamentoVO.getAno()+trancamentoVO.getSemestre()).compareTo(trancamentoVO.getUltimaMatriculaPeriodoVO().getAno()+trancamentoVO.getUltimaMatriculaPeriodoVO().getSemestre()) > 0 )) && 
			(Uteis.isAtributoPreenchido(trancamentoVO.getUltimaMatriculaPeriodoVO().getCodigo()) && (trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoAtiva() || trancamentoVO.getUltimaMatriculaPeriodoVO().getSituacaoPreMatricula()))){
			boolean existePendenciaFinanceira = false; //getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(trancamentoVO.getMatricula().getMatricula(), usuarioVO);
			if (existePendenciaFinanceira && !trancamentoVO.getMatricula().getCanceladoFinanceiro()) {
				throw new Exception(UteisJSF.internacionalizar("msg_Trancamento_matriculaPendenteFinanceira"));
			}
//			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().validarDadosPendenciaEmprestimoBiblioteca(trancamentoVO.getMatricula(), true, false, false, false, false, false, configuracaoGeralSistemaVO, usuarioVO);
		}
		if(Uteis.isAtributoPreenchido(trancamentoVO.getMatriculaPeriodoVO().getCodigo())){
			trancamentoVO.setHistoricoVOs(executarMontagemHistoricosParaRealizarAlteracaoSituacao(trancamentoVO.getMatricula(), trancamentoVO.getMatriculaPeriodoVO(), usuarioVO));
		}else if((trancamentoVO.getAno()+trancamentoVO.getSemestre()).compareTo(trancamentoVO.getUltimaMatriculaPeriodoVO().getAno()+trancamentoVO.getUltimaMatriculaPeriodoVO().getSemestre()) == 0){
			trancamentoVO.setHistoricoVOs(executarMontagemHistoricosParaRealizarAlteracaoSituacao(trancamentoVO.getMatricula(), trancamentoVO.getUltimaMatriculaPeriodoVO(), usuarioVO));
		}
		
	}	
	
	public  void montarDadosResponsavelEstorno(TrancamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelEstorno().getCodigo() == null || obj.getResponsavelEstorno().getCodigo() == 0) {
			return;
		}
		obj.setResponsavelEstorno(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelEstorno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}
	
	@Override
	public void consultaOtimizada(DataModelo controleConsulta, String  tipoJustificativa, String situacao, String tipo, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		List<Object> filtros =  new ArrayList<Object>(0);
		if (controleConsulta.getCampoConsulta().equals("matriculaMatricula")) {			
			sql.append(" WHERE sem_acentos(m.matricula) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("registroAcademico")) {			
			sql.append(" WHERE sem_acentos(p.registroAcademico) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("nomeAluno")) {			
			sql.append(" WHERE sem_acentos(p.nome) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("turma")) {			
			sql.append(" WHERE sem_acentos(tu.identificadorTurma) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("descricao")) {			
			sql.append(" WHERE sem_acentos(t.descricao) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("nomePessoa")) {			
			sql.append(" WHERE sem_acentos(resp.nome) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("codigoRequerimento")) {
			if(!Uteis.getIsValorNumerico(controleConsulta.getValorConsulta())) {
				throw new Exception("Deve ser informado apenas valores numéricos para o filtro REQUERIMENTO.");
			}
			sql.append(" WHERE req.codigo = ? ");
			filtros.add(Integer.valueOf(controleConsulta.getValorConsulta()));
		}
		if (controleConsulta.getCampoConsulta().equals("codigo")) {
			if(!Uteis.getIsValorNumerico(controleConsulta.getValorConsulta())) {
				throw new Exception("Deve ser informado apenas valores numéricos para o filtro CÓDIGO.");
			}
			sql.append(" WHERE t.codigo >= ? ");
			filtros.add(Integer.valueOf(controleConsulta.getValorConsulta()));
		}
		
		if (Uteis.isAtributoPreenchido(tipoJustificativa)) {			
			sql.append(" AND  sem_acentos(motivoCancelamentoTrancamento.nome) ilike sem_acentos(?) ");
			filtros.add(PERCENT+tipoJustificativa+PERCENT);
		}
		if (Uteis.isAtributoPreenchido(situacao)) {			
			sql.append(" AND  (t.situacao) = (?) ");
			filtros.add(situacao);
		}
		if (Uteis.isAtributoPreenchido(tipo) && !tipo.equals("NE")) {			
			sql.append(" AND  (t.tipotrancamento) = (?) ");
			filtros.add(tipo);
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		sql.append(" and ").append(realizarGeracaoWhereDataInicioDataTermino(controleConsulta.getDataIni(), controleConsulta.getDataFim(), "t.data", "t.data", false));
		
		if (controleConsulta.getCampoConsulta().equals("matriculaMatricula")) {
			sql.append(" order by m.matricula, p.nome, t.data desc ");			
		}
		if (controleConsulta.getCampoConsulta().equals("registroAcademico")) {
			sql.append(" order by p.registroAcademico, p.nome, t.data desc ");			
		}
		if (controleConsulta.getCampoConsulta().equals("nomeAluno")) {						
			sql.append(" order by p.nome, t.data desc ");
		}
		if (controleConsulta.getCampoConsulta().equals("descricao")) {						
			sql.append(" order by c.descricao, p.nome, t.data desc ");
		}
		if (controleConsulta.getCampoConsulta().equals("codigoRequerimento")) {						
			sql.append(" order by p.nome, t.data desc ");
		}
		if (controleConsulta.getCampoConsulta().equals("codigo")) {						
			sql.append(" order by t.codigo ");
		}
		if (controleConsulta.getCampoConsulta().equals("turma")) {						
			sql.append(" order by tu.identificadorTurma, p.nome, t.data desc ");
		}
		if (controleConsulta.getCampoConsulta().equals("nomePessoa")) {						
			sql.append(" order by resp.nome, p.nome, t.data desc ");
		}
		sql.append(" limit ").append(controleConsulta.getLimitePorPagina());
		sql.append(" offset ").append(controleConsulta.getOffset());
		SqlRowSet resultado =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		if(resultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(resultado.getInt("qtdeRegistro"));
			resultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaBasica(resultado));
		}else {
			controleConsulta.setTotalRegistrosEncontrados(0);
			controleConsulta.setListaConsulta(new ArrayList());
		}
	}

}
