package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import controle.academico.RenovarMatriculaControle;
//import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.SituacaoTransferenciaEntrada;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.TransferenciaEntradaInterfaceFacade;
import webservice.servicos.TurnoRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TransferenciaEntradaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>TransferenciaEntradaVO</code>. Encapsula toda a interação com
 * o banco de dados.
 * 
 * @see TransferenciaEntradaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TransferenciaEntrada extends ControleAcesso implements TransferenciaEntradaInterfaceFacade {

	protected static String idEntidade;
	protected static String idEntidadeTransferenciaInterna;

	@SuppressWarnings("OverridableMethodCallInConstructor")
	public TransferenciaEntrada() throws Exception {
		super();
		setIdEntidade("TransferenciaEntrada");
		setIdEntidadeTransferenciaInterna("TransferenciaInterna");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>TransferenciaEntradaVO</code>.
	 */
	public TransferenciaEntradaVO novo() throws Exception {
		TransferenciaEntrada.incluir(getIdEntidade());
		TransferenciaEntradaVO obj = new TransferenciaEntradaVO();
		return obj;
	}

	public void alterarSituacaoRequerimento(TransferenciaEntradaVO obj, UsuarioVO usuarioVO) throws Exception {
		if (!obj.getCodigoRequerimento().getCodigo().equals(0)) {
			switch (SituacaoTransferenciaEntrada.getEnum(obj.getSituacao())) {
			case EFETIVADO:
				getFacadeFactory().getRequerimentoFacade().alterarSituacao(obj.getCodigoRequerimento().getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), "", obj.getCodigoRequerimento().getMotivoDeferimento(),obj.getCodigoRequerimento(), usuarioVO);
				break;
			case EM_AVALIACAO:
				getFacadeFactory().getRequerimentoFacade().alterarSituacao(obj.getCodigoRequerimento().getCodigo(), SituacaoRequerimento.EM_EXECUCAO.getValor(), "", "",obj.getCodigoRequerimento(), usuarioVO);
				break;
			case INDEFERIDO:
				getFacadeFactory().getRequerimentoFacade().alterarSituacao(obj.getCodigoRequerimento().getCodigo(), SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor(), obj.getCodigoRequerimento().getMotivoIndeferimento(), "",obj.getCodigoRequerimento(), usuarioVO);
				break;
			}
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>TransferenciaEntradaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TransferenciaEntradaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TransferenciaEntradaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,  Boolean criarHistoricoTransferenciaEntrada, Boolean controleAcesso, UsuarioVO usuario) throws Exception {
		try {
			TransferenciaEntradaVO.validarDados(obj, configuracaoGeralSistema);
			alterarSituacaoRequerimento(obj, usuario);
			if(obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.INTERNA.getValor())){
				incluir(getIdEntidadeTransferenciaInterna(), controleAcesso, usuario);
			}else {
				incluir(getIdEntidade(), controleAcesso, usuario);
			}
			final String sql = "INSERT INTO TransferenciaEntrada( data, descricao, situacao, matricula, codigoRequerimento, cursoOrigem, instituicaoOrigem, parecerLegalInstituicaoOrigem, responsavelAutorizacao, justificativa, tipoJustificativa, tipoMidiaCaptacao, unidadeensino, curso, turno, gradecurricular, periodoletivo, turma, pessoa, unidadeEnsinoCurso, tipoTransferenciaEntrada, cidade ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			// Obtém o bloqueio para a Transferência.
			// getBloqueio().lock();
			// Matricula observador = (Matricula) ((Advised)
			// getFacadeFactory().getMatriculaFacade()).getTargetSource().getTarget();
			Date updated = null;
			// Obtém o bloqueio para a matricula.
			// observador.getBloqueio().lock();
			updated = new Date();
			try {
				// if (obj.getIsPossuiRequerimento()) {
				// obj.getCodigoRequerimento().getMatricula().addObserver(observador);
				// obj.getCodigoRequerimento().getMatricula().setChanged();
				// obj.getCodigoRequerimento().getMatricula().notifyObservers();
				// } else {
				// obj.getMatricula().addObserver(observador);
				// obj.getMatricula().setChanged();
				// obj.getMatricula().notifyObservers();
				// }

				obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlInserir = arg0.prepareStatement(sql);
						sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
						sqlInserir.setString(2, obj.getDescricao());
						sqlInserir.setString(3, obj.getSituacao());
						if (!obj.getMatricula().getMatricula().equals("")) {
							sqlInserir.setString(4, obj.getMatricula().getMatricula());
						} else {
							sqlInserir.setNull(4, 0);
						}
						if (obj.getCodigoRequerimento().getCodigo().intValue() != 0) {
							sqlInserir.setInt(5, obj.getCodigoRequerimento().getCodigo().intValue());
						} else {
							sqlInserir.setNull(5, 0);
						}
						sqlInserir.setString(6, obj.getCursoOrigem());
						sqlInserir.setString(7, obj.getInstituicaoOrigem());
						sqlInserir.setString(8, obj.getParecerLegalInstituicaoOrigem());
						if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
							sqlInserir.setInt(9, obj.getResponsavelAutorizacao().getCodigo().intValue());
						} else {
							sqlInserir.setNull(9, 0);
						}
						sqlInserir.setString(10, obj.getJustificativa());
						sqlInserir.setString(11, obj.getTipoJustificativa());
						if (obj.getTipoMidiaCaptacao().getCodigo().intValue() != 0) {
							sqlInserir.setInt(12, obj.getTipoMidiaCaptacao().getCodigo().intValue());
						} else {
							sqlInserir.setNull(12, 0);
						}
						if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
							sqlInserir.setInt(13, obj.getUnidadeEnsino().getCodigo().intValue());
						} else {
							sqlInserir.setNull(13, 0);
						}
						if (obj.getCurso().getCodigo().intValue() != 0) {
							sqlInserir.setInt(14, obj.getCurso().getCodigo());
						} else {
							sqlInserir.setNull(14, 0);
						}
						if (obj.getTurno().getCodigo().intValue() != 0) {
							sqlInserir.setInt(15, obj.getTurno().getCodigo().intValue());
						} else {
							sqlInserir.setNull(15, 0);
						}
						if (obj.getGradeCurricular().getCodigo().intValue() != 0) {
							sqlInserir.setInt(16, obj.getGradeCurricular().getCodigo().intValue());
						} else {
							sqlInserir.setNull(16, 0);
						}
						if (obj.getPeridoLetivo().getCodigo().intValue() != 0) {
							sqlInserir.setInt(17, obj.getPeridoLetivo().getCodigo().intValue());
						} else {
							sqlInserir.setNull(17, 0);
						}
						if (obj.getTurma().getCodigo().intValue() != 0) {
							sqlInserir.setInt(18, obj.getTurma().getCodigo().intValue());
						} else {
							sqlInserir.setNull(18, 0);
						}
						if (obj.getPessoa().getCodigo().intValue() != 0) {
							sqlInserir.setInt(19, obj.getPessoa().getCodigo().intValue());
						} else {
							sqlInserir.setNull(19, 0);
						}
						if (obj.getUnidadeEnsinoCurso().intValue() != 0) {
							sqlInserir.setInt(20, obj.getUnidadeEnsinoCurso().intValue());
						} else {
							sqlInserir.setNull(20, 0);
						}
						
						sqlInserir.setString(21, obj.getTipoTransferenciaEntrada());
						if (obj.getCidade().getCodigo().intValue() != 0) {
							sqlInserir.setInt(22, obj.getCidade().getCodigo().intValue());
						} else {
							sqlInserir.setNull(22, 0);
						}
						return sqlInserir;
					}
				}, new ResultSetExtractor<Integer>() {

					public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
						if (arg0.next()) {
							obj.setNovoObj(Boolean.FALSE);
							return arg0.getInt("codigo");
						}
						return null;
					}
				}));
				// getFacadeFactory().getTransferenciaEntradaDisciplinasAproveitadasFacade().incluirTransferenciaEntradaDisciplinasAproveitadass(obj.getCodigo(),
				// obj.getCurso().getPeriodicidade(),
				// obj.getTransferenciaEntradaDisciplinasAproveitadasVOs());
				if(criarHistoricoTransferenciaEntrada) {
					
					criarHistoricoTransferenciaEntrada(obj, usuario);
					obj.setNovoObj(Boolean.FALSE);
					if (obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.INTERNA.getValor())) {
						obj.getCodigoRequerimento().setSituacao(SituacaoRequerimento.EM_EXECUCAO.getValor());
						// realizarCancelamentoMatricula(obj);
						if (obj.getCodigoRequerimento().getCodigo().intValue() != 0) {
							getFacadeFactory().getRequerimentoFacade().alterarSituacao(obj.getCodigoRequerimento().getCodigo(), SituacaoRequerimento.EM_EXECUCAO.getValor(), "", "",obj.getCodigoRequerimento(), usuario);
						}
					}
				}
				// Atualiza tabela de matrícula.
				if (obj.getIsPossuiRequerimento()) {
					realizarAtualizacaoDoCampoUpdated(MatriculaVO.class, "(matricula = ?)", updated, obj.getCodigoRequerimento().getMatricula().getMatricula());
				} else {
					realizarAtualizacaoDoCampoUpdated(MatriculaVO.class, "(matricula = ?)", updated, obj.getMatricula().getMatricula());
				}
				getFacadeFactory().getTransferenciaEntradaRegistroAulaFrequenciaFacade().incluirTransferenciaEntradaRegistroAulaFrequenciaVOs(obj.getCodigo(), obj.getTransferenciaEntradaRegistroAulaFrequenciaVOs(), usuario);
				obj.getMatricula().setUpdated(updated);
			} finally {
				// if (obj.getIsPossuiRequerimento()) {
				// obj.getCodigoRequerimento().getMatricula().deleteObserver(observador);
				// } else {
				// obj.getMatricula().deleteObserver(observador);
				// }
				// Libera o bloqueio para a matrícula.
				// observador.getBloqueio().unlock();
				// Libera o bloqueio da transferência.
				// getBloqueio().unlock();
			}
		} catch (Exception e) {
			obj.getCodigoRequerimento().setSituacao(SituacaoRequerimento.EM_EXECUCAO.getValor());
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCancelamentoMatricula(TransferenciaEntradaVO obj,  UsuarioVO usuario) throws Exception {
		// CancelamentoVO cancelamentoVO = new CancelamentoVO();
		// cancelamentoVO.setCodigoRequerimento(obj.getCodigoRequerimento());
		// cancelamentoVO.setData(new Date());
		// if (obj.getCodigoRequerimento().getMatricula().getMatricula() != null
		// &&
		// !obj.getCodigoRequerimento().getMatricula().getMatricula().equals(""))
		// {
		// cancelamentoVO.setMatricula(obj.getCodigoRequerimento().getMatricula());
		// } else if (obj.getMatricula().getMatricula() != null &&
		// !obj.getMatricula().getMatricula().equals("")) {
		// cancelamentoVO.setMatricula(obj.getMatricula());
		// }
		// cancelamentoVO.setResponsavelAutorizacao(obj.getResponsavelAutorizacao());
		// cancelamentoVO.setMotivoCancelamentoTrancamento(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorTipoJustificativa("TI"));
		// cancelamentoVO.setJustificativa("Transferência Interna");
		// cancelamentoVO.setSituacao("FD");
		// getFacadeFactory().getCancelamentoFacade().incluir(cancelamentoVO,
		// usuario);
		/**
		 * Caso a Transferencia Entrada seja do tipo Transferencia Interna, a
		 * matricula é obtida atravez do requerimento, caso seja do tipo
		 * Transferencia Externa a matricula e obtida atravez da propria
		 * Transferencia Entrada
		 */
		if (!obj.getCodigoRequerimento().getCodigo().equals(0)) {
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaEMatriculaPeriodoTransferenciaInterna(obj.getCodigoRequerimento().getMatricula(), OrigemFechamentoMatriculaPeriodoEnum.TRANSFERENCIA_INTERNA, obj.getCodigo(), obj.getData(), usuario);
		} else {
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaEMatriculaPeriodoTransferenciaInterna(obj.getMatricula(), OrigemFechamentoMatriculaPeriodoEnum.TRANSFERENCIA_INTERNA, obj.getCodigo(), obj.getData(), usuario);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarHistoricoTransferenciaEntrada(TransferenciaEntradaVO obj,  UsuarioVO usuario) throws Exception {
		if (obj.getMatriculado() && obj.getSituacao().equals("EF")) {
			criarHistoricoTransferenciaEntrada(obj, usuario, true);			
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<HistoricoVO> criarHistoricoTransferenciaEntrada(TransferenciaEntradaVO obj, UsuarioVO usuarioVO, boolean persistirHistorico) throws Exception {
		List<HistoricoVO> historicoVOs = new ArrayList<HistoricoVO>(0);
        Iterator<TransferenciaEntradaDisciplinasAproveitadasVO> i = obj.getTransferenciaEntradaDisciplinasAproveitadasVOs().iterator();
        while (i.hasNext()) {
            TransferenciaEntradaDisciplinasAproveitadasVO objItem = (TransferenciaEntradaDisciplinasAproveitadasVO) i.next();
            HistoricoVO historicoVO = new HistoricoVO();
            historicoVO.setDataRegistro(new Date());
            historicoVO.getDisciplina().setCodigo(objItem.getDisciplina().getCodigo());
            historicoVO.getDisciplina().setNome(objItem.getDisciplina().getNome());
            historicoVO.setFreguencia(objItem.getFrequencia());
            historicoVO.getMatricula().setMatricula(obj.getMatriculaPeriodo().getMatricula());
            historicoVO.getMatriculaPeriodo().setCodigo(obj.getMatriculaPeriodo().getCodigo());
            historicoVO.setMediaFinal(objItem.getNota());
            historicoVO.setSituacao("AA");
            historicoVO.setAnoHistorico(objItem.getAnoConclusaoDisciplina());
            historicoVO.setSemestreHistorico(objItem.getSemestreConclusaoDisciplina());
            historicoVO.setInstituicao(obj.getInstituicaoOrigem());
            historicoVO.setCidadeVO(obj.getCidade());
            historicoVO.setCargaHorariaAproveitamentoDisciplina(objItem.getCargaHoraria().intValue());
            historicoVO.setTipoHistorico("NO");
            historicoVO.setResponsavel(obj.getResponsavelAutorizacao());
            historicoVO.setTransferenciaEntradaDisciplinasAproveitadas(objItem.getCodigo());
            historicoVO.setConfiguracaoAcademico(objItem.getConfiguracaoAcademico());
            historicoVO.setMatrizCurricular(obj.getMatriculaPeriodo().getMatriculaVO().getGradeCurricularAtual());
            historicoVO.setPeriodoLetivoCursada(obj.getMatriculaPeriodo().getPeriodoLetivo());
            historicoVO.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeCurricularEDisciplina(historicoVO.getMatrizCurricular().getCodigo(), historicoVO.getDisciplina().getCodigo(), usuarioVO, null));
			if (!Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO()) ) {
				historicoVO.setGradeCurricularGrupoOptativaDisciplinaVO(getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorDisciplinaMatrizCurricularPeriodoLetivo(historicoVO.getDisciplina().getCodigo(), historicoVO.getMatrizCurricular().getCodigo(), usuarioVO));
				if(Uteis.isAtributoPreenchido(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
					historicoVO.setCargaHorariaDisciplina(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria());
					historicoVO.setPeriodoLetivoMatrizCurricular(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivoPorGradeCurricularGrupoOptativaDisciplina(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuarioVO));
				}
			}else if(Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO())) {
				historicoVO.setCargaHorariaDisciplina(historicoVO.getGradeDisciplinaVO().getCargaHoraria());
				historicoVO.setPeriodoLetivoMatrizCurricular(historicoVO.getGradeDisciplinaVO().getPeriodoLetivoVO());				
			}else {
				historicoVO.setHistoricoDisciplinaForaGrade(true);
				historicoVO.setGradeCurricularGrupoOptativaDisciplinaVO(objItem.getGradeCurricularGrupoOptativaDisciplinaVO());
				historicoVO.setGradeDisciplinaVO(objItem.getGradeDisciplinaVO());
				historicoVO.setPeriodoLetivoMatrizCurricular(obj.getMatriculaPeriodo().getPeriodoLetivo());
			}
			
			if(Uteis.isAtributoPreenchido(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
				historicoVO.setDisciplinaReferenteAUmGrupoOptativa(true);				
			}else if(Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO())){
				historicoVO.setHistoricoDisciplinaOptativa(historicoVO.getGradeDisciplinaVO().getTipoDisciplina().equals("LO")|| historicoVO.getGradeDisciplinaVO().getTipoDisciplina().equals("OP"));
			}
			
			if (objItem.getUtilizanotafinalconceito()) {
				historicoVO.getMediaFinalConceito().setCodigo(objItem.getConfiguracaoAcademicoNotaConceitoVO().getCodigo());
				historicoVO.setUtilizaNotaFinalConceito(objItem.getUtilizanotafinalconceito());
				historicoVO.setNotaFinalConceito(objItem.getNotafinalconceito());
			}
//			verificarAlunoAprovadoDisciplina(historicoVO.getDisciplina().getCodigo(), obj.getMatricula().getMatricula(), obj.getMatriculaPeriodo().getCodigo(), usuario);
			historicoVOs.add(historicoVO);
			if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO()) || Uteis.isAtributoPreenchido(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
				if(persistirHistorico) {
					getFacadeFactory().getHistoricoFacade().incluir(historicoVO, usuarioVO);
				}
			}
        }
        return historicoVOs;
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEfetivacaoSituacaoTransferenciaEntrada(TransferenciaEntradaVO transferenciaEntradaVO,  UsuarioVO usuario) throws Exception {
		transferenciaEntradaVO.setSituacao(SituacaoTransferenciaEntrada.EFETIVADO.getValor());
		alterar(transferenciaEntradaVO, usuario);
	}

	public void verificarAlunoAprovadoDisciplina(Integer disciplina, String matricula, Integer matriculaPeriodo,  UsuarioVO usuario) throws Exception {
		List obj = consultarHistoricoAlunoDisciplina(matricula, disciplina, false, usuario);
		Iterator i = obj.iterator();
		while (i.hasNext()) {
			HistoricoVO historicoVO = (HistoricoVO) i.next();
			if (historicoVO.getAprovado()) {
				throw new Exception("Não é possivel fazer o aproveitamento da disciplina " + historicoVO.getDisciplina().getNome() + " pois o aluno já está aprovado nesta disciplina.");
			}
			if (historicoVO.getMatriculaPeriodo().getCodigo().intValue() == matriculaPeriodo.intValue()) {
				throw new Exception("Não é possivel fazer o aproveitamento da disciplina " + historicoVO.getDisciplina().getNome() + " para este periodo pois o aluno já esta cursando esta disciplina.");
			}

		}

	}

	public List consultarHistoricoAlunoDisciplina(String matricula, Integer disciplina, boolean controlarAcesso,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		List hist = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacao(matricula, disciplina, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return hist;
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>TransferenciaEntradaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TransferenciaEntradaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TransferenciaEntradaVO obj,  UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO c = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, null);
			TransferenciaEntradaVO.validarDados(obj, c);
			if(obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.INTERNA.getValor())){
				alterar(getIdEntidadeTransferenciaInterna(), true, usuario);
			}else {
				alterar(getIdEntidade(), true, usuario);
			}
			final String sql = "UPDATE TransferenciaEntrada set data=?, descricao=?, situacao=?, matricula=?, codigoRequerimento=?, cursoOrigem=?, instituicaoOrigem=?, parecerLegalInstituicaoOrigem=?, responsavelAutorizacao=?, justificativa=?, tipoJustificativa=?, tipoMidiaCaptacao=?, unidadeensino=?, curso=?, turno=?, gradecurricular=?, periodoletivo=?, turma=?, pessoa=?, unidadeEnsinoCurso=?, tipoTransferenciaEntrada = ?, cidade = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			// Obtém o bloqueio para a Transferência.
			// getBloqueio().lock();
			// Matricula observador = (Matricula) ((Advised)
			// getFacadeFactory().getMatriculaFacade()).getTargetSource().getTarget();
			Date updated = null;
			// Obtém o bloqueio para a matricula.
			// observador.getBloqueio().lock();
			updated = new Date();
			try {
				// if (obj.getIsPossuiRequerimento()) {
				// obj.getCodigoRequerimento().getMatricula().addObserver(observador);
				// obj.getCodigoRequerimento().getMatricula().setChanged();
				// obj.getCodigoRequerimento().getMatricula().notifyObservers();
				// } else {
				// obj.getMatricula().addObserver(observador);
				// obj.getMatricula().setChanged();
				// obj.getMatricula().notifyObservers();
				// }

				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
						sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
						sqlAlterar.setString(2, obj.getDescricao());
						sqlAlterar.setString(3, obj.getSituacao());
						if (!obj.getMatricula().getMatricula().equals("")) {
							sqlAlterar.setString(4, obj.getMatricula().getMatricula());
						} else {
							sqlAlterar.setNull(4, 0);
						}
						if (obj.getCodigoRequerimento().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(5, obj.getCodigoRequerimento().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(5, 0);
						}
						sqlAlterar.setString(6, obj.getCursoOrigem());
						sqlAlterar.setString(7, obj.getInstituicaoOrigem());
						sqlAlterar.setString(8, obj.getParecerLegalInstituicaoOrigem());
						if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(9, obj.getResponsavelAutorizacao().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(9, 0);
						}
						sqlAlterar.setString(10, obj.getJustificativa());
						sqlAlterar.setString(11, obj.getTipoJustificativa());
						if (obj.getTipoMidiaCaptacao().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(12, obj.getTipoMidiaCaptacao().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(12, 0);
						}
						if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(13, obj.getUnidadeEnsino().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(13, 0);
						}
						if (obj.getCurso().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(14, obj.getCurso().getCodigo());
						} else {
							sqlAlterar.setNull(14, 0);
						}
						if (obj.getTurno().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(15, obj.getTurno().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(15, 0);
						}
						if (obj.getGradeCurricular().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(16, obj.getGradeCurricular().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(16, 0);
						}
						if (obj.getPeridoLetivo().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(17, obj.getPeridoLetivo().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(17, 0);
						}
						if (obj.getTurma().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(18, obj.getTurma().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(18, 0);
						}
						if (obj.getPessoa().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(19, obj.getPessoa().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(19, 0);
						}
						if (obj.getUnidadeEnsinoCurso().intValue() != 0) {
							sqlAlterar.setInt(20, obj.getUnidadeEnsinoCurso().intValue());
						} else {
							sqlAlterar.setNull(20, 0);
						}
						
						sqlAlterar.setString(21, obj.getTipoTransferenciaEntrada());
						if (obj.getCidade().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(22, obj.getCidade().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(22, 0);
						}
						sqlAlterar.setInt(23, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				});
				// getFacadeFactory().getTransferenciaEntradaDisciplinasAproveitadasFacade().alterarTransferenciaEntradaDisciplinasAproveitadass(obj.getCodigo(),
				// obj.getCurso().getPeriodicidade(),
				// obj.getTransferenciaEntradaDisciplinasAproveitadasVOs());
				criarHistoricoTransferenciaEntrada(obj, usuario);
				// Atualiza tabela de matrícula.
				if (obj.getIsPossuiRequerimento()) {
					realizarAtualizacaoDoCampoUpdated(MatriculaVO.class, "(matricula = ?)", updated, obj.getCodigoRequerimento().getMatricula().getMatricula());
				} else {
					realizarAtualizacaoDoCampoUpdated(MatriculaVO.class, "(matricula = ?)", updated, obj.getMatricula().getMatricula());
				}
				obj.getMatricula().setUpdated(updated);
			} finally {
				// if (obj.getIsPossuiRequerimento()) {
				// obj.getCodigoRequerimento().getMatricula().deleteObserver(observador);
				// } else {
				// obj.getMatricula().deleteObserver(observador);
				// }
				// Libera o bloqueio para a matrícula.
				// observador.getBloqueio().unlock();
				// Libera o bloqueio da transferência.
				// getBloqueio().unlock();
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Operação responsável por alterar no BD apenas alguns dados de um objeto da classe
	 * <code>TransferenciaEntradaVO</code> que esteja com a situação Efetivado ou Indeferido.
	 * Os campos que podem ser alterados são: Curso de Origem, Instituição de Origem, Cidade e Parecer legal Instituição de Origem.
	 * 
	 * @param obj
	 *        Objeto da classe <code>TransferenciaEntradaVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *            Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTransferenciaEntradaEfetivada(final TransferenciaEntradaVO obj,  UsuarioVO usuario) throws Exception {
		try {
			TransferenciaEntradaVO.validarDadosParaTransferenciaEntradaNaoPodeSerAlterada(obj);
			if(obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.INTERNA.getValor())){
				alterar(getIdEntidadeTransferenciaInterna(), true, usuario);
			}else {
				alterar(getIdEntidade(), true, usuario);
			}
			final String sql = "UPDATE TransferenciaEntrada set cursoOrigem=?, instituicaoOrigem=?, parecerLegalInstituicaoOrigem=?, cidade = ?, data=?  WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			try {
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
						sqlAlterar.setString(1, obj.getCursoOrigem());
						sqlAlterar.setString(2, obj.getInstituicaoOrigem());
						sqlAlterar.setString(3, obj.getParecerLegalInstituicaoOrigem());
						if (obj.getCidade().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(4, obj.getCidade().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(4, 0);
						}
						sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getData()));
						sqlAlterar.setInt(6, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				});
			} finally {
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(Integer codigo, String situacao) throws Exception {
		String sql = "UPDATE TransferenciaEntrada set  situacao=?  WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { situacao, codigo });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatricula(Integer codigo, String matricula) throws Exception {
		String sql = "UPDATE TransferenciaEntrada set  matricula=?  WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { matricula, codigo });
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TransferenciaEntradaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TransferenciaEntradaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TransferenciaEntradaVO obj, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {			
			if(obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.INTERNA.getValor())){
				excluir(getIdEntidadeTransferenciaInterna(), true, usuarioVO);
			}else {
				excluir(getIdEntidade(), true, usuarioVO);
			}
			String sql = "DELETE FROM TransferenciaEntrada WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getTransferenciaEntradaDisciplinasAproveitadasFacade().excluirTransferenciaEntradaDisciplinasAproveitadass(obj.getCodigo());
			getFacadeFactory().getTransferenciaEntradaRegistroAulaFrequenciaFacade().excluirTransferenciaEntradaRegistroAulaFrequenciaVOs(obj.getCodigo(), null);
		} catch (Exception e) {
			throw e;
		}
	}

	public TransferenciaEntradaVO montarDadosTransferenciaInternaPeloCodigoRequerimento(Integer requerimento, Integer unidadeEnsino,  Boolean possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral, UsuarioVO usuario) throws Exception {
		RequerimentoVO requerimentoVO = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(requerimento, "TI", unidadeEnsino, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if (!configuracaoGeralSistemaVO.getCodigo().equals(0)) {
			if (!requerimentoVO.getIsRequerimentoPago() && !configuracaoGeralSistemaVO.getPermiteTransferenciaInternaSemRequerimento()) {
				throw new Exception("O requerimento está PENDENTE FINANCEIRAMENTE por isso não é possível realizar esta operação.");
			}
		} else {
			if (!requerimentoVO.getIsRequerimentoPago()) {
				throw new Exception("O requerimento está PENDENTE FINANCEIRAMENTE por isso não é possível realizar esta operação.");
			}
		}
//		if (requerimentoVO.getSituacao().equals(SituacaoRequerimento.EM_EXECUCAO.getValor())) {
//			throw new Exception("O requerimento já está em " + SituacaoRequerimento.EM_EXECUCAO.getDescricao() + " por isso não é possível realizar esta operação.");
//		}
		if (requerimentoVO.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
			throw new Exception("O requerimento já está " + SituacaoRequerimento.FINALIZADO_DEFERIDO.getDescricao() + " por isso não é possível realizar esta operação.");
		}
		if (requerimentoVO.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor())) {
			throw new Exception("O requerimento já está " + SituacaoRequerimento.FINALIZADO_INDEFERIDO.getDescricao() + " por isso não é possível realizar esta operação.");
		}
//		if (!requerimentoVO.getMatricula().getMatriculaCanceladaFinanceiramente() && getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(requerimentoVO.getMatricula().getMatricula(), usuario)) {
//			throw new Exception("A Transferencia Interna da Matrícula não pode ser realizada. Realize a liberação financeira da matrícula (" + requerimentoVO.getMatricula().getMatricula() + ") e tente novamente!");
//		}
		TransferenciaEntradaVO transferenciaEntradaVO = new TransferenciaEntradaVO();
		transferenciaEntradaVO.setCodigoRequerimento(requerimentoVO);
		transferenciaEntradaVO.setCursoOrigem(requerimentoVO.getMatricula().getCurso().getNome());
		transferenciaEntradaVO.setInstituicaoOrigem(requerimentoVO.getMatricula().getUnidadeEnsino().getNome());
		transferenciaEntradaVO.setMatriculado(false);
		transferenciaEntradaVO.setPessoa(requerimentoVO.getPessoa());
		// transferenciaEntradaVO.getUnidadeEnsino().setCodigo(requerimentoVO.getUnidadeEnsino().getCodigo());
		// transferenciaEntradaVO.getUnidadeEnsino().setNome(requerimentoVO.getUnidadeEnsino().getNome());
		transferenciaEntradaVO.setInstituicaoOrigem(requerimentoVO.getUnidadeEnsino().getNome());
		transferenciaEntradaVO.setTipoTransferenciaEntrada(TipoTransferenciaEntrada.INTERNA.getValor());
		transferenciaEntradaVO.setSituacao(SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor());
		transferenciaEntradaVO.setMatricula(requerimentoVO.getMatricula());
		transferenciaEntradaVO.getUnidadeEnsino().setCodigo(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo());
		if(Uteis.isAtributoPreenchido(requerimentoVO.getCursoTransferenciaInternaVO().getCodigo()) 
				&& Uteis.isAtributoPreenchido(requerimentoVO.getTurnoTransferenciaInternaVO()) && Uteis.isAtributoPreenchido(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO())){
			UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(requerimentoVO.getCursoTransferenciaInternaVO().getCodigo(), requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo(), requerimentoVO.getTurnoTransferenciaInternaVO().getCodigo(), usuario);
			adicionarCursoTransferenciaInterna(transferenciaEntradaVO, unidadeEnsinoCursoVO, possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral);
		}		
		return transferenciaEntradaVO;
	}

	public void adicionarCursoTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO, UnidadeEnsinoCursoVO obj, Boolean possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral) throws Exception {
		if (!possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral) {
			if (transferenciaEntradaVO.getCodigoRequerimento().getMatricula().getCurso().getCodigo().intValue() == obj.getCurso().getCodigo().intValue()
					&& transferenciaEntradaVO.getCodigoRequerimento().getMatricula().getUnidadeEnsino().getCodigo().intValue() == obj.getUnidadeEnsino().intValue()) {
				throw new Exception("Não é possivel fazer uma TRANSFERÊNCIA INTERNA para o curso que já está cursando.");
			}
		}
		transferenciaEntradaVO.setCurso(obj.getCurso());
		transferenciaEntradaVO.setTurno(obj.getTurno());
		// GradeCurricularVO gradeCurricularVO =
		// getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCurso(transferenciaEntradaVO.getCurso().getCodigo(),
		// "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		// if (gradeCurricularVO.getCodigo() == 0) {
		// throw new
		// Exception("Não existe uma Grade Curricular Ativa para este curso.");
		// }
		// transferenciaEntradaVO.setGradeCurricular(gradeCurricularVO);
		transferenciaEntradaVO.setUnidadeEnsinoCurso(obj.getCodigo());
		return;
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>TransferenciaEntrada</code> através do valor do atributo
	 * <code>String tipoJustificativa</code>. Retorna os objetos, com início do
	 * valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaEntradaVO> consultarPorTipoJustificativa(String valorConsulta, String situacaoMatriculado, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntrada WHERE tipoJustificativa like(?) ORDER BY tipoJustificativa";
		if (situacaoMatriculado.equals("matriculado")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE tipoJustificativa like(?) and matricula is not null and ORDER BY tipoJustificativa";
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE tipoJustificativa like(?) and matricula is null and ORDER BY tipoJustificativa";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>TransferenciaEntrada</code> através do valor do atributo
	 * <code>String instituicaoOrigem</code>. Retorna os objetos, com início do
	 * valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaEntradaVO> consultarPorInstituicaoOrigem(String valorConsulta, String situacaoMatriculado, boolean controlarAcesso, int nivelMontarDados,  TipoTransferenciaEntrada tipoTransferenciaEntrada, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntrada WHERE instituicaoOrigem like(?) and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY instituicaoOrigem";
		if (situacaoMatriculado.equals("matriculado")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE instituicaoOrigem like(?) and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY instituicaoOrigem";
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE instituicaoOrigem like(?) and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY instituicaoOrigem";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>TransferenciaEntrada</code> através do valor do atributo
	 * <code>Integer codigoRequerimento</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaEntradaVO> consultarPorCodigoRequerimento(Integer valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigoRequerimento >= " + valorConsulta.intValue() + " and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigoRequerimento";
		if (situacaoMatriculado.equals("matriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigoRequerimento >= " + valorConsulta.intValue() + " and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigoRequerimento";
			}else{
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigoRequerimento >= " + valorConsulta.intValue() + " and situacao = 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigoRequerimento";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigoRequerimento >= " + valorConsulta.intValue() + " and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigoRequerimento";
			}else{
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigoRequerimento >= " + valorConsulta.intValue() + " and situacao != 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigoRequerimento";
			}
		}
		
		if (situacaoMatriculado.equals("ambas")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigoRequerimento >= " + valorConsulta.intValue() + " and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigoRequerimento";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public TransferenciaEntradaVO consultarPorCodigoRequerimento(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		
		String sql = "SELECT * FROM TransferenciaEntrada WHERE codigoRequerimento = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return new TransferenciaEntradaVO();
			// throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>TransferenciaEntrada</code> através do valor do atributo
	 * <code>matricula</code> da classe <code>Matricula</code> Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaEntradaVO> consultarPorMatriculaMatricula(String valorConsulta, String situacaoMatriculado, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like(?) and tipoTransferenciaEntrada= 'EX'  ORDER BY Matricula.matricula";
		if (situacaoMatriculado.equals("matriculado")) {
			sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like(?) and tipoTransferenciaEntrada= 'EX' and TransferenciaEntrada.matricula is not null ORDER BY Matricula.matricula";
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like(?) and tipoTransferenciaEntrada= 'EX' and TransferenciaEntrada.matricula is null ORDER BY Matricula.matricula";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<TransferenciaEntradaVO> consultarPorMatriculaETipo(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY Matricula.matricula";
		if (situacaoMatriculado.equals("matriculado")) {
			if (tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA)) {
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') and TransferenciaEntrada.situacao = 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY Matricula.matricula";
			} else {
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') and TransferenciaEntrada.matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY Matricula.matricula";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') and TransferenciaEntrada.situacao != 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY Matricula.matricula";
		}
		
		if (situacaoMatriculado.equals("ambas")) {
			sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Matricula WHERE TransferenciaEntrada.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY Matricula.matricula";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>TransferenciaEntrada</code> através do valor do atributo
	 * <code>String situacao</code>. Retorna os objetos, com início do valor do
	 * atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaEntradaVO> consultarPorSituacao(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntrada WHERE situacao like('" + valorConsulta + "%') and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
		if (situacaoMatriculado.equals("matriculado")) {
			if (tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)) {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE situacao like('" + valorConsulta + "%') and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			} else {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE situacao like('" + valorConsulta + "%') and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			if (tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)) {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE situacao like('" + valorConsulta + "%') and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			} else {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE situacao like('" + valorConsulta + "%') and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		
		if (situacaoMatriculado.equals("ambas")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE situacao like('" + valorConsulta + "%') and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<TransferenciaEntradaVO> consultarPorCurso(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Curso WHERE TransferenciaEntrada.curso = curso.codigo and upper (sem_acentos(curso.nome)) like upper(sem_acentos(?)) and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
		if (situacaoMatriculado.equals("matriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Curso WHERE TransferenciaEntrada.curso = curso.codigo and upper (sem_acentos(curso.nome)) like upper(sem_acentos(?)) and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}else{
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Curso WHERE TransferenciaEntrada.curso = curso.codigo and upper (sem_acentos(curso.nome)) like upper(sem_acentos(?)) and situacao = 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
			sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Curso WHERE TransferenciaEntrada.curso = curso.codigo and upper (sem_acentos(curso.nome)) like upper(sem_acentos(?)) and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}else{
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Curso WHERE TransferenciaEntrada.curso = curso.codigo and upper (sem_acentos(curso.nome)) like upper(sem_acentos(?)) and situacao != 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<TransferenciaEntradaVO> consultarPorAluno(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.nome)) like upper(sem_acentos(?)) and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
		if (situacaoMatriculado.equals("matriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.nome)) like upper(sem_acentos(?)) and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}else{
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.nome)) like upper(sem_acentos(?)) and situacao = 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.nome)) like upper(sem_acentos(?)) and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}else{
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.nome)) like upper(sem_acentos(?)) and situacao != 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	@Override
	public List<TransferenciaEntradaVO> consultarPorRegistroAcademicoAluno(String valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.registroAcademico)) like upper(sem_acentos(?)) and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
		if (situacaoMatriculado.equals("matriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.registroAcademico)) like upper(sem_acentos(?)) and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}else{
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.registroAcademico)) like upper(sem_acentos(?)) and situacao = 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.registroAcademico)) like upper(sem_acentos(?)) and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}else{
				sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and upper (sem_acentos(pessoa.registroAcademico)) like upper(sem_acentos(?)) and situacao != 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>TransferenciaEntrada</code> através do valor do atributo
	 * <code>Date data</code>. Retorna os objetos com valores pertecentes ao
	 * período informado por parâmetro. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaEntradaVO> consultarPorData(Date prmIni, Date prmFim, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntrada WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY data";
		if (situacaoMatriculado.equals("matriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY data";
			}else{
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and situacao ='EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY data";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			if(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)){
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY data";
			}else{
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and situacao != 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY data";
			}
		}
		if (situacaoMatriculado.equals("ambas")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY data";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean matriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";
		if (matriculado) {
			sqlStr = "SELECT * FROM TransferenciaEntrada " + "LEFT JOIN unidadeEnsino ON TransferenciaEntrada.unidadeEnsino = unidadeEnsino.codigo " + "WHERE ((TransferenciaEntrada.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (TransferenciaEntrada.data <= '" + Uteis.getDataJDBC(prmFim) + "')) and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' " + "AND unidadeEnsino.nome = '" + unidadeEnsino + "' ORDER BY TransferenciaEntrada.data";
		}
		if (!matriculado) {
			sqlStr = "SELECT * FROM TransferenciaEntrada " + "LEFT JOIN unidadeEnsino ON TransferenciaEntrada.unidadeEnsino = unidadeEnsino.codigo " + "WHERE ((TransferenciaEntrada.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (TransferenciaEntrada.data <= '" + Uteis.getDataJDBC(prmFim) + "')) and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' " + "AND unidadeEnsino.nome = '" + unidadeEnsino + "' ORDER BY TransferenciaEntrada.data";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarTodosTiposPorData(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaEntrada " + "LEFT JOIN unidadeEnsino ON TransferenciaEntrada.unidadeEnsino = unidadeEnsino.codigo " + "WHERE ((TransferenciaEntrada.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (TransferenciaEntrada.data <= '" + Uteis.getDataJDBC(prmFim) + "')) " + "AND unidadeEnsino.nome = '" + unidadeEnsino + "' ORDER BY TransferenciaEntrada.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>TransferenciaEntrada</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaEntradaVO> consultarPorCodigo(Integer valorConsulta, String situacaoMatriculado, TipoTransferenciaEntrada tipoTransferenciaEntrada, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.INTERNA) ? getIdEntidadeTransferenciaInterna() : getIdEntidade(), controlarAcesso, usuario);
		
		String sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigo >= " + valorConsulta.intValue() + " and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigo";
		if (situacaoMatriculado.equals("matriculado")) {
			if (tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)) {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigo >= " + valorConsulta.intValue() + " and matricula is not null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigo";
			} else {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigo >= " + valorConsulta.intValue() + " and situacao ='EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigo";
			}
		}
		if (situacaoMatriculado.equals("naoMatriculado")) {
			if (tipoTransferenciaEntrada.equals(TipoTransferenciaEntrada.EXTERNA)) {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigo >= " + valorConsulta.intValue() + " and matricula is null and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigo";
			} else {
				sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigo >= " + valorConsulta.intValue() + " and situacao != 'EF' and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigo";
			}
		}
		if (situacaoMatriculado.equals("ambas")) {
			sqlStr = "SELECT * FROM TransferenciaEntrada WHERE codigo >= " + valorConsulta.intValue() + " and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<TransferenciaEntradaDisciplinasAproveitadasVO> buscarDisciplinaSeremAproveitadaTransferenciaInterna(Integer aluno, Integer curso, Integer gradeCurricular,  UsuarioVO usuario) throws Exception {
		List<TransferenciaEntradaDisciplinasAproveitadasVO> transferenciaEntradaDisciplinasAproveitadasVOs = new ArrayList<>(0);
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarHistoricoParaAproveitamentoNaTransferenciaInternaPorCursoGrade(aluno, curso, gradeCurricular, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, null);
		adicionarDisciplinaSeremAproveitadaTransferenciaInterna(historicoVOs, transferenciaEntradaDisciplinasAproveitadasVOs, curso, gradeCurricular, usuario);
		verificarDisciplinasAproveitadasRepetidas(transferenciaEntradaDisciplinasAproveitadasVOs);
		return transferenciaEntradaDisciplinasAproveitadasVOs;
	}

	public void verificarDisciplinasAproveitadasRepetidas(List<TransferenciaEntradaDisciplinasAproveitadasVO> transferenciaEntradaDisciplinasAproveitadasVOs) {
		List<TransferenciaEntradaDisciplinasAproveitadasVO> listaDisciplinasAproveitadas = transferenciaEntradaDisciplinasAproveitadasVOs;
		List<TransferenciaEntradaDisciplinasAproveitadasVO> listaDisciplinasAproveitadasRemover = new ArrayList<TransferenciaEntradaDisciplinasAproveitadasVO>(0);
		for (TransferenciaEntradaDisciplinasAproveitadasVO disciplinaAproveitada : listaDisciplinasAproveitadas) {
			for (TransferenciaEntradaDisciplinasAproveitadasVO disciplinaAproveitadaVerificacao : transferenciaEntradaDisciplinasAproveitadasVOs) {
				if (!disciplinaAproveitada.equals(disciplinaAproveitadaVerificacao)) {
					if (disciplinaAproveitada.getDisciplina().getCodigo().equals(disciplinaAproveitadaVerificacao.getDisciplina().getCodigo())) {
						if (disciplinaAproveitada.getNota() != null && disciplinaAproveitadaVerificacao.getNota() != null) {
							if (disciplinaAproveitada.getNota().doubleValue() >= disciplinaAproveitadaVerificacao.getNota().doubleValue()) {
								if (!listaDisciplinasAproveitadasRemover.contains(disciplinaAproveitadaVerificacao) && !listaDisciplinasAproveitadasRemover.contains(disciplinaAproveitada)) {
									listaDisciplinasAproveitadasRemover.add(disciplinaAproveitadaVerificacao);
								}
							} else {
								if (!listaDisciplinasAproveitadasRemover.contains(disciplinaAproveitada) && !listaDisciplinasAproveitadasRemover.contains(disciplinaAproveitadaVerificacao)) {
									listaDisciplinasAproveitadasRemover.add(disciplinaAproveitada);
								}
							}
						}
					}
				}
			}
		}
		transferenciaEntradaDisciplinasAproveitadasVOs.removeAll(listaDisciplinasAproveitadasRemover);
	}

	public void adicionarDisciplinaSeremAproveitadaTransferenciaInterna(List<HistoricoVO> historicoVOs, List<TransferenciaEntradaDisciplinasAproveitadasVO> transferenciaEntradaDisciplinasAproveitadasVOs, Integer curso, Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		for (HistoricoVO historicoVO : historicoVOs) {
			TransferenciaEntradaDisciplinasAproveitadasVO transferenciaEntradaDisciplinasAproveitadasVO = new TransferenciaEntradaDisciplinasAproveitadasVO();
			transferenciaEntradaDisciplinasAproveitadasVO.setCargaHoraria(historicoVO.getGradeDisciplinaVO().getCargaHoraria().doubleValue());
			//verificarGradeContemDisciplinaAproveitada(historicoVO, curso, gradeCurricular, usuario);
			transferenciaEntradaDisciplinasAproveitadasVO.setDisciplina(historicoVO.getDisciplina());
			transferenciaEntradaDisciplinasAproveitadasVO.setFrequencia(historicoVO.getFreguencia());
			transferenciaEntradaDisciplinasAproveitadasVO.setNota(historicoVO.getMediaFinal());
			transferenciaEntradaDisciplinasAproveitadasVO.setValidarAnoSemestre(Boolean.FALSE);
			transferenciaEntradaDisciplinasAproveitadasVO.setConfiguracaoAcademico(historicoVO.getConfiguracaoAcademico());
			if (historicoVO.getUtilizaNotaFinalConceito()) {
				transferenciaEntradaDisciplinasAproveitadasVO.getConfiguracaoAcademicoNotaConceitoVO().setCodigo(historicoVO.getMediaFinalConceito().getCodigo());
				transferenciaEntradaDisciplinasAproveitadasVO.setUtilizanotafinalconceito(historicoVO.getUtilizaNotaFinalConceito());
				transferenciaEntradaDisciplinasAproveitadasVO.setNotafinalconceito(historicoVO.getNotaFinalConceito());
			}
			if (Uteis.isAtributoPreenchido(historicoVO.getAnoHistorico())) {
				transferenciaEntradaDisciplinasAproveitadasVO.setAnoConclusaoDisciplina(historicoVO.getAnoHistorico());	
			}
			if (Uteis.isAtributoPreenchido(historicoVO.getSemestreHistorico())) {
				transferenciaEntradaDisciplinasAproveitadasVO.setSemestreConclusaoDisciplina(historicoVO.getSemestreHistorico());	
			}
			transferenciaEntradaDisciplinasAproveitadasVOs.add(transferenciaEntradaDisciplinasAproveitadasVO);
		}
	}

	public void verificarGradeContemDisciplinaAproveitada(HistoricoVO historicoVO, Integer curso, Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		DisciplinaVO disciplinaAproveitada = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPelaDisciplinaEquivalente(curso, gradeCurricular, historicoVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (disciplinaAproveitada.getCodigo() != null && disciplinaAproveitada.getCodigo() != 0) {
			historicoVO.setDisciplina(disciplinaAproveitada);
		}
	}

	public void montarListaDisciplinasAproveitadas(TransferenciaEntradaVO transferenciaEntrada, UsuarioVO usuario) throws Exception {
		DisciplinaVO disciplina = new DisciplinaVO();
		for (TransferenciaEntradaDisciplinasAproveitadasVO transferenciaEntradaDisciplinasAproveitadasVO : transferenciaEntrada.getTransferenciaEntradaDisciplinasAproveitadasVOs()) {
			disciplina = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaEquivalenteAproveitada(transferenciaEntrada.getCurso().getCodigo(), transferenciaEntrada.getGradeCurricular().getCodigo(), transferenciaEntradaDisciplinasAproveitadasVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			transferenciaEntradaDisciplinasAproveitadasVO.setDisciplina(disciplina);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarTransferenciaEntrada(TransferenciaEntradaVO transferenciaEntrada, String situacao,  UsuarioVO usuario) throws Exception {
		if (transferenciaEntrada.getCodigo() != null && transferenciaEntrada.getCodigo() != 0) {
			getFacadeFactory().getTransferenciaEntradaFacade().alterarSituacao(transferenciaEntrada.getCodigo(), situacao);
		}
		// A Partir da versão 5.0, foi retirada a validação abaixo. Pois, o
		// requerimento, mesmo após a matrícula do
		// aluno, precisa ser encaminhado para outros departamentos, para que
		// processos posteriores sejam realizados.
		// Como por exemplo, conferencia do aproveitamento e notificação do
		// aluno pelo telemarkting.
		// if (transferenciaEntrada.getCodigoRequerimento().getCodigo() != null
		// && transferenciaEntrada.getCodigoRequerimento().getCodigo() != 0) {
		// getFacadeFactory().getTransferenciaEntradaFacade().alterarSituacaoRequerimento(transferenciaEntrada,
		// usuario);
		// }
		if ((transferenciaEntrada.getCodigo() != null && transferenciaEntrada.getCodigo() != 0) && situacao.equals("EF") && !transferenciaEntrada.getTipoTransferenciaEntrada().equals("EX")) {
			realizarCancelamentoMatricula(transferenciaEntrada, usuario);
		}
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaEntradaVO</code> resultantes da consulta.
	 */
	public  List<TransferenciaEntradaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		List<TransferenciaEntradaVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TransferenciaEntradaVO</code>.
	 * 
	 * @return O objeto da classe <code>TransferenciaEntradaVO</code> com os
	 *         dados devidamente montados.
	 */
	public  TransferenciaEntradaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		TransferenciaEntradaVO obj = new TransferenciaEntradaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getCodigoRequerimento().setCodigo(new Integer(dadosSQL.getInt("codigoRequerimento")));
		obj.setCursoOrigem(dadosSQL.getString("cursoOrigem"));
		obj.setInstituicaoOrigem(dadosSQL.getString("instituicaoOrigem"));
		obj.setParecerLegalInstituicaoOrigem(dadosSQL.getString("parecerLegalInstituicaoOrigem"));
		obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.setTipoJustificativa(dadosSQL.getString("tipoJustificativa"));
		obj.getTipoMidiaCaptacao().setCodigo(new Integer(dadosSQL.getInt("tipoMidiaCaptacao")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeensino")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getGradeCurricular().setCodigo(new Integer(dadosSQL.getInt("gradecurricular")));
		obj.getPeridoLetivo().setCodigo(new Integer(dadosSQL.getInt("periodoLetivo")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade")));
		obj.setUnidadeEnsinoCurso(new Integer(dadosSQL.getInt("unidadeEnsinoCurso")));
		obj.setTipoTransferenciaEntrada(dadosSQL.getString("tipoTransferenciaEntrada"));
		obj.setDataEstorno(dadosSQL.getDate("dataEstorno"));
		obj.setMotivoEstorno(dadosSQL.getString("motivoEstorno"));
		obj.getResponsavelEstorno().setCodigo(dadosSQL.getInt("responsavelEstorno"));
		if (!obj.getMatricula().getMatricula().equals("")) {
			obj.setMatriculado(Boolean.TRUE);
		}
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosRequerimento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			// montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			return obj;
		}
		obj.setTransferenciaEntradaDisciplinasAproveitadasVOs(getFacadeFactory().getTransferenciaEntradaDisciplinasAproveitadasFacade().consultarTransferenciaEntradaDisciplinasAproveitadass(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setTransferenciaEntradaRegistroAulaFrequenciaVOs(getFacadeFactory().getTransferenciaEntradaRegistroAulaFrequenciaFacade().consultarPorTransferenciaEntrada(obj.getCodigo(), usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosCidade(obj, nivelMontarDados, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosGradeCurricular(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosPeriodoLetivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelEstorno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosTipoMidiaCaptacao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosRequerimento(obj, nivelMontarDados, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>TransferenciaEntradaVO</code>. Faz uso da chave primária da classe
	 * <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosPessoa(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public  void montarDadosResponsavelAutorizacao(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}
	
	public  void montarDadosResponsavelEstorno(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj.getResponsavelEstorno().getCodigo())) {
			obj.setResponsavelEstorno(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelEstorno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
	}
	
	public  void montarDadosTipoMidiaCaptacao(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTipoMidiaCaptacao().getCodigo().intValue() == 0) {
			return;
		}
		obj.setTipoMidiaCaptacao(getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorChavePrimaria(obj.getTipoMidiaCaptacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public  void montarDadosRequerimento(TransferenciaEntradaVO obj, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		if (obj.getCodigoRequerimento().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>TransferenciaEntradaVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosMatricula(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			return;
		}
		getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatricula(), usuario);
		// obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(),
		// 0, nivelMontarDados));
	}

	public  void montarDadosCidade(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getCidade() == null) || (obj.getCidade().getCodigo() == null) || obj.getCidade().getCodigo() == 0) {
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}

	public  void montarDadosCurso(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getCurso().getCodigo() == null) || (obj.getCurso().getCodigo() == 0)) {
			return;
		}
		getFacadeFactory().getCursoFacade().carregarDados(obj.getCurso(), NivelMontarDados.BASICO, usuario);
		// obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(.getCodigo(),
		// nivelMontarDados));
	}

	public  void montarDadosTurno(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getTurno().getCodigo() == null) || (obj.getTurno().getCodigo() == 0)) {
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public  void montarDadosTurma(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getTurma().getCodigo() == null) || (obj.getTurma().getCodigo() == 0)) {
			return;
		}
		getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), usuario);
		// obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(.getCodigo(),
		// nivelMontarDados));
	}

	public  void montarDadosUnidadeEnsino(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getUnidadeEnsino().getCodigo() == null) || (obj.getUnidadeEnsino().getCodigo() == 0)) {
			return;
		}		
		 obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public  void montarDadosGradeCurricular(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getGradeCurricular().getCodigo() == null) || (obj.getGradeCurricular().getCodigo() == 0)) {
			return;
		}
		obj.setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(obj.getGradeCurricular().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosPeriodoLetivo(TransferenciaEntradaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getPeridoLetivo().getCodigo() == null) || (obj.getPeridoLetivo().getCodigo() == 0)) {
			return;
		}
		obj.setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(obj.getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>TransferenciaEntradaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public TransferenciaEntradaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		
		String sql = "SELECT * FROM TransferenciaEntrada WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public TransferenciaEntradaVO consultarPorCodigoParaHistoricoAluno(Integer codigo) throws Exception {
		String sql = "SELECT te.codigo, te.data, te.unidadeensino, ue.nome as nomeUnidadeEnsino, te.curso, c.nome as nomeCurso FROM TransferenciaEntrada as te inner join curso as c on c.codigo = te.curso inner join unidadeensino as ue on ue.codigo = te.unidadeensino WHERE te.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigo });
		TransferenciaEntradaVO obj = new TransferenciaEntradaVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setData(tabelaResultado.getDate("data"));
			obj.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeensino"));
			obj.getUnidadeEnsino().setNome(tabelaResultado.getString("nomeUnidadeensino"));
			obj.getCurso().setCodigo(tabelaResultado.getInt("curso"));
			obj.getCurso().setNome(tabelaResultado.getString("nomeCurso"));
		}
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TransferenciaEntrada.idEntidade;
	}
	
	public static String getIdEntidadeTransferenciaInterna() {
		return TransferenciaEntrada.idEntidadeTransferenciaInterna;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TransferenciaEntrada.idEntidade = idEntidade;
	}
	
	public void setIdEntidadeTransferenciaInterna(String idEntidadeTransferenciaInterna) {
		TransferenciaEntrada.idEntidadeTransferenciaInterna = idEntidadeTransferenciaInterna;
	}

	public String consultarInstituicaoOrigemCursoOrigemPorMatricula(String matricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select te.instituicaoorigem, te.cursoorigem from matricula m ");
		sqlStr.append("inner join transferenciaentrada te on m.tranferenciaentrada = te.codigo ");
		sqlStr.append("where m.matricula = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matricula });
		if (!tabelaResultado.next()) {
			return "IES não encontrada na base.";
		}
		return tabelaResultado.getString("instituicaoorigem") + " - " + tabelaResultado.getString("cursoorigem");
	}
	
	public static void validarDadosAntesImpressao(TransferenciaEntradaVO transferenciaEntradaVO, Integer textoPadrao) throws Exception {		
		if (transferenciaEntradaVO.getMatricula() == null || !Uteis.isAtributoPreenchido(transferenciaEntradaVO.getMatricula().getMatricula())) {
			throw new Exception("O Aluno deve ser informado para geração do relatório.");
		}
		if(!Uteis.isAtributoPreenchido(textoPadrao)){
			throw new Exception("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}

	@Override
	public String imprimirDeclaracaoTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		String caminhoRelatorio = "";
		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
		impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
		impressaoContratoVO.setGerarNovoArquivoAssinado(true);
		String textoStr = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(matriculaVO, impressaoContratoVO, textoPadraoDeclaracao, config, usuario);
		if (textoPadraoDeclaracao.getTipoDesigneTextoEnum().isHtml()) {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textoStr);
		} 
//		else {
//			caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracao, "", true, config, usuario);
//			getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
//		}
		return caminhoRelatorio;

	}

	@Override
	public void vincularMatriculaTransferenciaEntrada(TransferenciaEntradaVO obj, UsuarioVO usuario) throws Exception {
		try {
			boolean transferenciaEntradaVinculadaMatricula = this.executarVerificarTransferenciaEntradaVinculadaMatricula(obj.getCodigo(), usuario);
			if (!transferenciaEntradaVinculadaMatricula) {
				this.alterarMatricula(obj.getCodigo(), obj.getMatricula().getMatricula());
				this.alterarSituacao(obj.getCodigo(), SituacaoTransferenciaEntrada.EFETIVADO.getValor());
				getFacadeFactory().getMatriculaFacade().alterarTransferenciaEntrada(obj.getMatricula().getMatricula(), obj.getCodigo(), usuario);
				obj.setSituacao(SituacaoTransferenciaEntrada.EFETIVADO.getValor());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean executarVerificarTransferenciaEntradaVinculadaMatricula(Integer transferenciaEntrada, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT matricula from matricula ");
		sqlStr.append("WHERE tranferenciaentrada = ").append(transferenciaEntrada);
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoTransferenciaEntradaMatricula(Integer transferenciaEntrada, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		String sql = "UPDATE TransferenciaEntrada set matricula = null  WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { transferenciaEntrada });
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEstornoTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO,  UsuarioVO usuarioVO) throws Exception{
		verificarPermissaoFuncionalidadeUsuario("PermiteEstornarTransferenciaInterna", usuarioVO);		
		try{ 
			String matricula = consultarNumeroMatriculaVinculadoTransferenciaInterna(transferenciaEntradaVO.getCodigo(), usuarioVO);
			if(matricula != null && !matricula.trim().isEmpty()){
				throw new Exception("Para estornar esta TRANFERÊNCIA INTERNA é necessário realizar a exclusão da matrícula ("+matricula+").");
			}
			if(transferenciaEntradaVO.getSituacaoEstornado()){
				throw new Exception("Esta TRANFERÊNCIA INTERNA já esta estornada.");
			}
			if(transferenciaEntradaVO.getMotivoEstorno().trim().isEmpty()){
				throw new Exception("O campos MOTIVO ESTORNO (TRANFERÊNCIA INTERNA) deve ser informado.");
			}	
			if(!transferenciaEntradaVO.getMatricula().getSituacao().equals("TI")){
				throw new Exception("Só é possível realizar o estorno da transferência de interna se a situação da ultima matrícula período for transferência interna.");
			}
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(transferenciaEntradaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if(!matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor())){
				throw new Exception("Só é possível realizar o estorno da transferência de interna se a situação da ultima matrícula período for transferência interna.");
			}
			transferenciaEntradaVO.setDataEstorno(new Date());
			transferenciaEntradaVO.getResponsavelEstorno().setCodigo(usuarioVO.getCodigo());
			transferenciaEntradaVO.getResponsavelEstorno().setNome(usuarioVO.getNome());			
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(transferenciaEntradaVO.getMatricula().getMatricula(), "AT", usuarioVO);
			matriculaPeriodoVO.setSituacaoMatriculaPeriodo("AT");
			matriculaPeriodoVO.setAlunoTransferidoUnidade(false);
			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, null, null, null);
			List<HistoricoVO> historicos = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoSituacoes(matriculaPeriodoVO.getCodigo(),"'"+SituacaoHistorico.TRANSFERIDO.getValor()+"'", usuarioVO);
			if (!historicos.isEmpty()) {
				for(HistoricoVO historicoVO: historicos){
					if(historicoVO.getSituacao().equals(SituacaoHistorico.TRANSFERIDO.getValor())){
						historicoVO.setSituacao("CS");
					}
				}
				getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(historicos, false, true, usuarioVO);
			}					
			if(Uteis.isAtributoPreenchido(transferenciaEntradaVO.getCodigoRequerimento())){
				getFacadeFactory().getRequerimentoFacade().alterarSituacaoRequerimento(transferenciaEntradaVO.getCodigoRequerimento().getCodigo(), SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor(), usuarioVO);
			}
			alterarSituacaoEstorno(transferenciaEntradaVO, usuarioVO);
			transferenciaEntradaVO.setSituacao(SituacaoTransferenciaEntrada.ESTORNADO.getValor());
		}catch(Exception e){			
			throw e;
		}		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoEstorno(final TransferenciaEntradaVO obj, UsuarioVO usuario) throws Exception {
			alterar(obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.INTERNA.getValor()) ? getIdEntidadeTransferenciaInterna() : getIdEntidade() , true, usuario);
			final String sql = "UPDATE TransferenciaEntrada set situacao = ?, dataEstorno=?, motivoEstorno=?, responsavelEstorno=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
						int i = 0;
						sqlAlterar.setString(++i, SituacaoTransferenciaEntrada.ESTORNADO.getValor());
						sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataEstorno()));
						sqlAlterar.setString(++i, obj.getMotivoEstorno());
						sqlAlterar.setInt(++i, obj.getResponsavelEstorno().getCodigo().intValue());
						sqlAlterar.setInt(++i, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				});

	}
	


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarVinculoTransferenciaInternaMatriculaExistente(TransferenciaEntradaVO transferenciaEntrada, MatriculaVO matriculaVO,  UsuarioVO usuarioVO) throws Exception {		
		getFacadeFactory().getMatriculaFacade().alterarTransferenciaEntrada(matriculaVO.getMatricula(), transferenciaEntrada.getCodigo(), usuarioVO);
		verificarTransferenciaEntrada(transferenciaEntrada, SituacaoTransferenciaEntrada.EFETIVADO.getValor(), usuarioVO);
		if (!matriculaVO.getTransferenciaEntrada().getCodigo().equals(0)) {
			getFacadeFactory().getRegistroAulaFacade().realizarCriacaoRegistroAulaAPartirTransferenciaEntrada(matriculaVO.getMatricula(), matriculaVO.getTransferenciaEntrada().getTransferenciaEntradaRegistroAulaFrequenciaVOs(), usuarioVO);
		}
		transferenciaEntrada.setSituacao(SituacaoTransferenciaEntrada.EFETIVADO.getValor());
	}
	
	public String consultarNumeroMatriculaVinculadoTransferenciaInterna(Integer transferenciaEntrada, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT matricula from matricula ");
		sqlStr.append("WHERE tranferenciaentrada = ").append(transferenciaEntrada);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getString("matricula"); 
		}
		return null;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alteraData(TransferenciaEntradaVO transferenciaEntradaVO, UsuarioVO usuarioVO) throws Exception{
		if(transferenciaEntradaVO.getData() == null){
			throw new Exception("O campo DATA deve ser informado.");
		}
		getConexao().getJdbcTemplate().update("update transferenciaentrada set data = ? where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), Uteis.getDataJDBC(transferenciaEntradaVO.getData()), transferenciaEntradaVO.getCodigo());
	}
	
	public TransferenciaEntradaVO consultarPorMatriculaESituacao(String matricula, String situacao, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		String sql = "SELECT * FROM TransferenciaEntrada WHERE matricula = ? and situacao = ? order by codigo desc limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula, situacao } );
		TransferenciaEntradaVO obj = new TransferenciaEntradaVO();
		if (tabelaResultado.next()) {			
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return obj;
	}
	
	@Override
	public void validarDadosEnturmacaoAlunoTransferencia(TransferenciaEntradaVO transferenciaEntradaVO, UsuarioVO usuarioVO) throws Exception {
		TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(transferenciaEntradaVO.getMatricula().getMatricula(), usuarioVO);
		if (!turmaVO.getCodigo().equals(0)) {
			if (turmaVO.getCodigo().equals(transferenciaEntradaVO.getTurma().getCodigo())) {
				throw new Exception("Não é possível realizar a transferência para a Turma ( "+turmaVO.getIdentificadorTurma()+" ). Aluno já se encontrada matrículado na Turma em questão.");
			}
		}
	}
	
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoTranferenciaInternaGerandoNovaMatriculaAproveitandoDisciplinasAprovadasProximoPeriodoLetivoPorRequerimento(RequerimentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,   Boolean apresentarVisaoAluno , UsuarioVO usuario) throws Exception {
    	
			if (Uteis.isAtributoPreenchido(obj.getCodigo())) {	
				MatriculaPeriodoVO ultimaMP = null;
				try {
		    	TransferenciaEntradaVO transferenciaEntradaVO = new TransferenciaEntradaVO();
				transferenciaEntradaVO.setCodigoRequerimento(obj);
				if(!Uteis.isAtributoPreenchido(obj.getMatricula().getUnidadeEnsino().getCodigo())) {
					getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatricula(), NivelMontarDados.TODOS, usuario);
				}
				transferenciaEntradaVO.setInstituicaoOrigem(getAplicacaoControle().getUnidadeEnsinoVO(obj.getMatricula().getUnidadeEnsino().getCodigo(), null).getNome());
				transferenciaEntradaVO.setCursoOrigem(obj.getMatricula().getCurso().getNome());
				transferenciaEntradaVO.setMatriculado(false);
				transferenciaEntradaVO.setPessoa(obj.getPessoa());				
				transferenciaEntradaVO.setTipoTransferenciaEntrada(TipoTransferenciaEntrada.INTERNA.getValor());
				transferenciaEntradaVO.setSituacao(SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor());
				transferenciaEntradaVO.setMatricula(obj.getMatricula());
				transferenciaEntradaVO.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsinoTransferenciaInternaVO().getCodigo());
				transferenciaEntradaVO.getResponsavelAutorizacao().setCodigo(usuario.getCodigo());
			    transferenciaEntradaVO.getResponsavelAutorizacao().setNome(usuario.getNome());				
				UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(obj.getCursoTransferenciaInternaVO().getCodigo(), obj.getUnidadeEnsinoTransferenciaInternaVO().getCodigo(), obj.getTurnoTransferenciaInternaVO().getCodigo(), usuario);
				transferenciaEntradaVO.setCurso(unidadeEnsinoCursoVO.getCurso());
				transferenciaEntradaVO.setTurno(unidadeEnsinoCursoVO.getTurno());				
				transferenciaEntradaVO.setUnidadeEnsinoCurso(unidadeEnsinoCursoVO.getCodigo());
				if (Uteis.isAtributoPreenchido(obj.getTipoRequerimento().getAno())) {
					ultimaMP = getFacadeFactory().getMatriculaPeriodoFacade()
							.consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(transferenciaEntradaVO.getMatricula().getMatricula(), obj.getTipoRequerimento().getAno(), obj.getTipoRequerimento().getSemestre(), 
									0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, "");
				}
				if (!Uteis.isAtributoPreenchido(ultimaMP)) {
					ultimaMP = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(transferenciaEntradaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				}
				if(!Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoVO())) {
					obj.setMatriculaPeriodoVO(ultimaMP);
				}
				obj.getMatricula().getMatriculaPeriodoVOs().clear();
				obj.getMatricula().getMatriculaPeriodoVOs().add(ultimaMP);
				if(!obj.getCursoTransferenciaInternaVO().getCodigo().equals(obj.getMatricula().getCurso().getCodigo())) {
					transferenciaEntradaVO.setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularDataAtivacaoAtualPorSituacaoGradeCurso(obj.getUnidadeEnsinoTransferenciaInternaVO().getCodigo() ,transferenciaEntradaVO.getCurso().getCodigo(),transferenciaEntradaVO.getTurno().getCodigo() , 0,"AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				}else {
					transferenciaEntradaVO.getGradeCurricular().setCodigo(obj.getMatricula().getGradeCurricularAtual().getCodigo());
				}
				List<PeriodoLetivoVO> periodoLetivos = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(transferenciaEntradaVO.getGradeCurricular().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	            if(!Uteis.isAtributoPreenchido(periodoLetivos)) {
	            	throw new Exception("Não foi encontrada nenhum Periodo Letivo habilitado para a realização da matrícula do requerimento "+obj.getCodigo()+".");
	            }	            
	            List<ProcessoMatriculaVO> processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculaOnline(apresentarVisaoAluno , false,  
	            		obj.getUnidadeEnsinoTransferenciaInternaVO().getCodigo(), 
	            		obj.getTipoRequerimento().getRegistrarTransferenciaProximoSemestre() ? obj.getTipoRequerimento().getAno() : ultimaMP.getAno(),
	            		obj.getTipoRequerimento().getRegistrarTransferenciaProximoSemestre() ? obj.getTipoRequerimento().getSemestre() : ultimaMP.getSemestre(), 
	            		obj.getCursoTransferenciaInternaVO().getCodigo(), obj.getTurnoTransferenciaInternaVO().getCodigo(), usuario, 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, true);
	            
				if(processoMatriculaVOs.isEmpty()){
					throw new Exception("Não foi encontrado nenhum CALENDÁRIO DE MATRÍCULA habilitada para a realização da matrícula do requerimento "+obj.getCodigo()+".");
				}
				Integer numeroPeriodoLetivo = ultimaMP.getPeriodoLetivo().getPeriodoLetivo().intValue();
				if(obj.getTipoRequerimento().getRegistrarTransferenciaProximoSemestre() && numeroPeriodoLetivo < periodoLetivos.get(periodoLetivos.size()-1).getPeriodoLetivo()) {
					numeroPeriodoLetivo++;
				}
				
				if((ultimaMP.getAno().equals(processoMatriculaVOs.get(0).getAno()) &&  ultimaMP.getSemestre().equals(processoMatriculaVOs.get(0).getSemestre()))
					|| 
					(processoMatriculaVOs.get(0).getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals("AN") && 
    						ultimaMP.getAno().equals(processoMatriculaVOs.get(0).getAno()))) {					
					 numeroPeriodoLetivo = ultimaMP.getPeriodoLetivo().getPeriodoLetivo().intValue();
					 
				}
				
				if(ultimaMP.getAno().equals(processoMatriculaVOs.get(0).getAno()) &&  ultimaMP.getSemestre().equals(processoMatriculaVOs.get(0).getSemestre()) && Uteis.isAtributoPreenchido(ultimaMP.getTranferenciaEntrada()) ) {
					if(getFacadeFactory().getTransferenciaEntradaFacade().verificarCursoOrigemTransferenciaInternaCorrespondeNovoCursoTransferenciaInterna(transferenciaEntradaVO.getCurso().getCodigo() ,transferenciaEntradaVO.getPessoa().getCodigo(),processoMatriculaVOs.get(0).getAno(), processoMatriculaVOs.get(0).getSemestre() )) {
						throw new Exception("Não é possível realizar a transferência por Requerimento. A matrícula ("+transferenciaEntradaVO.getMatricula().getMatricula()+") possui uma transferência realizada do Curso de "+transferenciaEntradaVO.getCurso().getNome()+" no ano/semestre  "+ultimaMP.getApresentarAnoSemestre() );
					}				
				}	           
	            Integer utimoPeriodoLetivoLista = 0;
	            for(PeriodoLetivoVO periodoLetivo : periodoLetivos) {	            	
	            	if(periodoLetivo.getPeriodoLetivo().equals(numeroPeriodoLetivo)) {
	            		transferenciaEntradaVO.setPeridoLetivo(periodoLetivo);
	            		break;
	            	}else {
	            		if(utimoPeriodoLetivoLista <  periodoLetivo.getPeriodoLetivo()) {
	            			utimoPeriodoLetivoLista = periodoLetivo.getPeriodoLetivo();
	            			transferenciaEntradaVO.setPeridoLetivo(periodoLetivo);
	            		}
	            	}          	
	            }
	            
				if(!ultimaMP.getAno().equals(processoMatriculaVOs.get(0).getAno()) || !ultimaMP.getSemestre().equals(processoMatriculaVOs.get(0).getSemestre())) {	
					// adicionado a regra porque caso tenha que realizar uma renovação de matricula e nao tenha um periodo letivo superior ou seja o aluno ta no ultimo periodo o sistema precisa somente renovar no mesmo periodo letivo do anterior .
					PeriodoLetivoVO periodoLetivoCursar =  ultimaMP.getPeriodoLetivo().getPeriodoLetivo().equals(transferenciaEntradaVO.getPeridoLetivo().getPeriodoLetivo()) ? ultimaMP.getPeriodoLetivo() : transferenciaEntradaVO.getPeridoLetivo();
					transferenciaEntradaVO.setProcessoMatriculaVO(processoMatriculaVOs.get(0));
					getFacadeFactory().getMatriculaFacade().realizarRenovacaoMatriculaAutomaticaAtravesTransferenciaInterna(transferenciaEntradaVO, periodoLetivoCursar ,apresentarVisaoAluno, configuracaoGeralSistema, usuario);
				}
				List<TurmaVO> listaResultado = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(transferenciaEntradaVO.getPeridoLetivo().getPeriodoLetivo(), transferenciaEntradaVO.getUnidadeEnsino().getCodigo(), transferenciaEntradaVO.getCurso().getCodigo(), transferenciaEntradaVO.getTurno().getCodigo(), transferenciaEntradaVO.getGradeCurricular().getCodigo(), true, false,false, false, "",  "",   false, usuario, true);
				if(listaResultado.isEmpty()) {
					transferenciaEntradaVO.setMatriculaPeriodo(null);
					if(!Uteis.isAtributoPreenchido(transferenciaEntradaVO.getGradeCurricular().getNome()) && Uteis.isAtributoPreenchido(transferenciaEntradaVO.getGradeCurricular().getCodigo())) {
						transferenciaEntradaVO.setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(transferenciaEntradaVO.getGradeCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
					}
					throw new Exception("Não foi encontrada nenhuma TURMA habilitada para a realização da matrícula do Requerimento ("+obj.getCodigo()+") no POLO "+obj.getUnidadeEnsinoTransferenciaInternaVO().getNome()+", CURSO "+obj.getCursoTransferenciaInternaVO().getNome()+", MATRIZ "+transferenciaEntradaVO.getGradeCurricular().getNome()+" e PERÍODO LETIVO "+transferenciaEntradaVO.getPeridoLetivo().getPeriodoLetivo()+".");					
				}
				transferenciaEntradaVO.setTurma(listaResultado.get(0));
				getFacadeFactory().getTransferenciaEntradaFacade().validarDadosEnturmacaoAlunoTransferencia(transferenciaEntradaVO, usuario);
				transferenciaEntradaVO.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(transferenciaEntradaVO.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario));
				getFacadeFactory().getTransferenciaEntradaFacade().incluir(transferenciaEntradaVO,configuracaoGeralSistema, false, false ,usuario );				
				getFacadeFactory().getMatriculaFacade().realizarCriacaoMatriculaPorTransferenciaInternaOrigemRequerimento(transferenciaEntradaVO,configuracaoGeralSistema, apresentarVisaoAluno, processoMatriculaVOs.get(0), usuario);
				}catch (Exception e) {
					if(ultimaMP != null) {
						obj.setMatriculaPeriodoVO(null);
					}
					throw e;
				}
			}else {		
				
				throw new Exception("Não foi encontrado nenhum requerimento para criação da transferencia/Matricula por Requerimento.");
				
			}	
			
    
    }

	@Override
	public Boolean verificarCursoOrigemTransferenciaInternaCorrespondeNovoCursoTransferenciaInterna(Integer curso, Integer aluno , String ano , String semestre ) throws Exception {
		StringBuilder sqlStr = new StringBuilder();		
		sqlStr.append(" select tse.codigo from transferenciaentrada tse	");	
		sqlStr.append("  inner join matricula as matricula_origem on matricula_origem.matricula = tse.matricula ");
		sqlStr.append("  inner join matriculaperiodo mp_origem on mp_origem.matricula = matricula_origem.matricula  ");
		
		sqlStr.append("  inner join matricula as matricula_destino on matricula_destino.tranferenciaentrada =  tse.codigo ");
		sqlStr.append("  inner join matriculaperiodo mp_destino on mp_destino.matricula = matricula_destino.matricula  and mp_destino.transferenciaentrada = tse.codigo  ");
		
		sqlStr.append(" where ( matricula_origem.curso = " ).append(curso).append("  and matricula_origem.aluno = ").append(aluno);
				if (!ano.equals("")) {
					sqlStr.append(" and mp_origem.ano ='").append(ano).append("' ");
				}
				if (!semestre.equals("")) {
					sqlStr.append(" and mp_origem.semestre = '").append(semestre).append("' ");
				}		
				sqlStr.append(" ) or   ( matricula_destino.curso = " ).append(curso).append(" and matricula_destino.aluno = ").append(aluno);
				if (!ano.equals("")) {
					sqlStr.append(" and mp_destino.ano = '").append(ano).append("' ");
				}
				if (!semestre.equals("")) {
					sqlStr.append("  and mp_destino.semestre = '").append(semestre).append("' ");
				}	
				sqlStr.append(" ) limit 1  ");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
		
	}
	
	
	@Override
	public void removerVinculoTransferenciaEntradaRequerimento( Integer requerimento , UsuarioVO usuarioVO) throws Exception {			
			final String sql = "update transferenciaentrada  set codigorequerimento  = null  where  codigorequerimento = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);					
					sqlAlterar.setInt(1, requerimento.intValue());
					return sqlAlterar;
				}
			});
		
		
	}
	
	@Override
	public Date consultarDataTransferenciaExternaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT data FROM transferenciaentrada where matricula = '").append(matricula).append("' order by data desc limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getDate("data");
		}
		return null;
	}

	public Boolean consultarExisteTransferencia(Integer valorConsulta,  TipoTransferenciaEntrada tipoTransferenciaEntrada, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT TransferenciaEntrada.* FROM TransferenciaEntrada, Pessoa WHERE TransferenciaEntrada.pessoa = pessoa.codigo and pessoa.codigo = " + valorConsulta + " and tipoTransferenciaEntrada = '" + tipoTransferenciaEntrada.getValor() + "' ORDER BY situacao";
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}
}
