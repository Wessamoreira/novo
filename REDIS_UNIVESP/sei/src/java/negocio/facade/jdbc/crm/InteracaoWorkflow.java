package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.FormacaoAcademicaProspectEnum;
import negocio.comuns.crm.enumerador.RendaProspectEnum;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoContatoEnum;
import negocio.comuns.crm.enumerador.TipoEmpresaProspectEnum;
import negocio.comuns.crm.enumerador.TipoInteracaoEnum;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboInteracaoWorkflowEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.facade.jdbc.academico.Curso;
import negocio.facade.jdbc.administrativo.Campanha;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.interfaces.crm.InteracaoWorkflowInterfaceFacade;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>InteracaoWorkflowVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>InteracaoWorkflowVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see InteracaoWorkflowVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class InteracaoWorkflow extends ControleAcesso implements InteracaoWorkflowInterfaceFacade {

	protected static String idEntidade;

	public InteracaoWorkflow() throws Exception {
		super();
		setIdEntidade("InteracaoWorkflow");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>InteracaoWorkflowVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InteracaoWorkflowVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			incluirSemValidarDados(obj, usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemValidarDados(final InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// InteracaoWorkflow.incluir(getIdEntidade());
			realizarUpperCaseDados(obj);
			final String sql = "INSERT INTO InteracaoWorkflow( prospect, campanha, unidadeEnsino, curso, workflow, compromissoAgendaPessoaHorario, motivoInsucesso, observacao, tipoInteracao,  etapaWorkflow, responsavel, horaInicio, horaTermino, dataInicio, dataTermino, tempodecorrido, tipomidia, turno, motivo, tipoOrigemInteracao, identificadorOrigem, codigoEntidadeOrigem, matricula) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getProspect().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getProspect().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getCampanha().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getCampanha().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getCurso().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getWorkflow().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getWorkflow().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getMotivoInsucesso().getCodigo() != 0) {
						sqlInserir.setInt(7, obj.getMotivoInsucesso().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getObservacao());
					sqlInserir.setString(9, obj.getTipoInteracao().toString());
					if (obj.getEtapaWorkflow().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getEtapaWorkflow().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					if (obj.getHoraInicio().equals("")) {
						obj.setHoraInicio(Uteis.getHoraAtual());
					}
					sqlInserir.setString(12, obj.getHoraInicio());
					sqlInserir.setString(13, obj.getHoraTermino());
					if (obj.getDataInicio() == null) {
						obj.setDataInicio(new Date());
					}

					sqlInserir.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataInicio()));
					sqlInserir.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataTermino()));
					sqlInserir.setString(16, obj.getTempoDecorrido());
					if (obj.getTipoMidia().getCodigo() != 0) {
						sqlInserir.setInt(17, obj.getTipoMidia().getCodigo());
					} else {
						sqlInserir.setNull(17, 0);
					}
					if (obj.getTurno().getCodigo() != 0) {
						sqlInserir.setInt(18, obj.getTurno().getCodigo());
					} else {
						sqlInserir.setNull(18, 0);
					}
					sqlInserir.setString(19, obj.getMotivo());
					sqlInserir.setString(20, obj.getTipoOrigemInteracao().toString());
					sqlInserir.setString(21, obj.getIdentificadorOrigem());
					if(obj.getCodigoEntidadeOrigem() != null) {
						sqlInserir.setInt(22, obj.getCodigoEntidadeOrigem());
					}else {
						sqlInserir.setNull(22, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getMatriculaVO().getMatricula())) {
						sqlInserir.setString(23, obj.getMatriculaVO().getMatricula());
					} else {
						sqlInserir.setNull(23, 0);
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
			if (obj.getCompromissoAgendaPessoaHorario().getDataCompromisso().compareTo(new Date()) > 0) {

			}
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>InteracaoWorkflowVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados ( <code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InteracaoWorkflowVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			alterarSemValidarDados(obj, usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSemValidarDados(final InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// InteracaoWorkflow.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			final String sql = "UPDATE InteracaoWorkflow set prospect=?, campanha=?, unidadeEnsino=?, curso=?, workflow=?, compromissoAgendaPessoaHorario=?, motivoInsucesso=?, observacao=?, tipoInteracao=?, etapaWorkflow=?, responsavel=?, horaInicio=?, horaTermino=?, dataInicio=?, dataTermino=?, tempodecorrido=?, tipomidia=?, turno=?, motivo = ?, tipoOrigemInteracao=?, identificadorOrigem=?, codigoEntidadeOrigem=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getProspect().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getProspect().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getCampanha().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getCampanha().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getCurso().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (obj.getWorkflow().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getWorkflow().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getMotivoInsucesso().getCodigo() != 0) {
						sqlAlterar.setInt(7, obj.getMotivoInsucesso().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getObservacao());
					sqlAlterar.setString(9, obj.getTipoInteracao().toString());
					if (obj.getEtapaWorkflow().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getEtapaWorkflow().getCodigo());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					if (obj.getHoraInicio().equals("")) {
						obj.setHoraInicio(Uteis.getHoraAtual());
					}
					if (obj.getDataInicio() == null) {
						obj.setDataInicio(new Date());
					}
					sqlAlterar.setString(12, obj.getHoraInicio());
					sqlAlterar.setString(13, obj.getHoraTermino());
					sqlAlterar.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataInicio()));
					sqlAlterar.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataTermino()));
					sqlAlterar.setString(16, obj.getTempoDecorrido());
					if (obj.getTipoMidia().getCodigo() != 0) {
						sqlAlterar.setInt(17, obj.getTipoMidia().getCodigo());
					} else {
						sqlAlterar.setNull(17, 0);
					}
					if (obj.getTurno().getCodigo() != 0) {
						sqlAlterar.setInt(18, obj.getTurno().getCodigo());
					} else {
						sqlAlterar.setNull(18, 0);
					}
					sqlAlterar.setString(19, obj.getMotivo());
					sqlAlterar.setString(20, obj.getTipoOrigemInteracao().toString());
					sqlAlterar.setString(21, obj.getIdentificadorOrigem());
					if(Uteis.isAtributoPreenchido(obj.getCodigoEntidadeOrigem())) {
						sqlAlterar.setInt(22, obj.getCodigoEntidadeOrigem());
					}else {
						sqlAlterar.setNull(22, 0);
					}				
					
					sqlAlterar.setInt(23, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarObservacao(final InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception {
		try {
			InteracaoWorkflow.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			final String sql = "UPDATE InteracaoWorkflow set observacao=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getObservacao());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alteratProspectInteracao(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE InteracaoWorkflow set prospect=? WHERE ((prospect = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, codProspectManter);
					sqlAlterar.setInt(2, codProspectRemover);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>InteracaoWorkflowVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InteracaoWorkflowVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// InteracaoWorkflow.excluir(getIdEntidade());
			String sql = "DELETE FROM InteracaoWorkflow WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirInteracaoProspects(Integer codProspect, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM InteracaoWorkflow WHERE ((prospect = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { codProspect });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param InteracaoWorkflowVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuario);
		} else {
			alterar(obj, usuario);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirFollowUpContaReceber(BoletoBancarioRelVO boleto, String codigoUsuario, String origemRotina, String nomePDF) throws Exception {
		InteracaoWorkflowVO interacao = new InteracaoWorkflowVO();
		interacao.getResponsavel().setCodigo(Uteis.isAtributoPreenchido(codigoUsuario) ? Integer.parseInt(codigoUsuario) : 0);
		interacao.setDataInicio(new Date());
		interacao.setHoraInicio(Uteis.getHoraAtualComSegundos());
		interacao.setDataTermino(new Date());
		interacao.setHoraTermino(Uteis.getHoraAtualComSegundos());
		interacao.setTipoOrigemInteracao(TipoOrigemInteracaoEnum.IMPRESSAO_BOLETO);
		interacao.setIdentificadorOrigem(boleto.getContareceber_parcela());
		interacao.setCodigoEntidadeOrigem(boleto.getContareceber_codigo());
		interacao.setObservacao("Impressão de Boleto - " + nomePDF+"- origem Rotina "+origemRotina);
		incluirSemValidarDados(interacao, new UsuarioVO());

	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param InteracaoWorkflowVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRemarcacaoCompromissoPorInteracaoWorkflow(List<InteracaoWorkflowVO> listaInteracaoWorkflowVO, InteracaoWorkflowVO interacaoWorkflowVO, Date dataCompromissoAdiado, String horaCompromissoAdiado, String horaFimCompromissoAdiado, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		realizarGravacaoInteracaoWorkflow(listaInteracaoWorkflowVO, false, usuarioLogado, configuracaoGeralSistema);
		if(!Uteis.isAtributoPreenchido(interacaoWorkflowVO.getReagendarCompromisso())){
			interacaoWorkflowVO.setReagendarCompromisso(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().clone());	
		}		
		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarRemarcacaoCompromisso(interacaoWorkflowVO.getReagendarCompromisso(), dataCompromissoAdiado, horaCompromissoAdiado, horaFimCompromissoAdiado, true, true,"","","",0, usuarioLogado);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoVinculoProspectComConsultorInteracaoComoResponsavel(InteracaoWorkflowVO interacaoWorkflowVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (!interacaoWorkflowVO.getProspect().getConsultorPadrao().getCodigo().equals(0)) {
			// Se já existe um responsavel entao nao ha o que fazer
			return;
		}
		if (!configuracaoGeralSistema.getAssociarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir()) {
			return;
		}
		try {
			FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(usuarioLogado.getPessoa().getCodigo(), Boolean.FALSE, usuarioLogado);
			interacaoWorkflowVO.getProspect().setConsultorPadrao(funcionario);
			getFacadeFactory().getProspectsFacade().alterarConsultorProspect(interacaoWorkflowVO.getProspect(), usuarioLogado);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGravacaoInteracaoWorkflow(List<InteracaoWorkflowVO> listaInteracaoWorkflowVO, Boolean validarDados, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (!listaInteracaoWorkflowVO.isEmpty()) {
			for (int i = 0; i < listaInteracaoWorkflowVO.size(); i++) {
				InteracaoWorkflowVO interacaoWorkflowVO = listaInteracaoWorkflowVO.get(i);
				if (i == 0) {
					realizarValidacaoSeProspectInteracaoLigacaoReceptiva(interacaoWorkflowVO, usuarioLogado, configuracaoGeralSistema);
					realizarValidacaoSeCompromissoAgendaPessoaHorarioInteracaoLigacaoReceptivaGravarInteracao(interacaoWorkflowVO, usuarioLogado);
					realizarGeracaoVinculoProspectComConsultorInteracaoComoResponsavel(interacaoWorkflowVO, usuarioLogado, configuracaoGeralSistema);
				}
				interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setCodigo(listaInteracaoWorkflowVO.get(0).getCompromissoAgendaPessoaHorario().getCodigo());
				interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getCampanha().getWorkflow().setTempoMedioGerarAgenda(interacaoWorkflowVO.getWorkflow().getTempoMedioGerarAgenda());
				interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDataCompromisso(listaInteracaoWorkflowVO.get(0).getCompromissoAgendaPessoaHorario().getDataCompromisso());
				interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setHora(listaInteracaoWorkflowVO.get(0).getCompromissoAgendaPessoaHorario().getHora());
				interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(listaInteracaoWorkflowVO.get(0).getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario());
				interacaoWorkflowVO.getProspect().setCodigo(listaInteracaoWorkflowVO.get(0).getProspect().getCodigo());
				interacaoWorkflowVO.setResponsavel(usuarioLogado);
				realizarGravacaoInteracaoWorkflowwPorEtapa(interacaoWorkflowVO, validarDados, usuarioLogado);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGravacaoInteracaoWorkflowwPorEtapa(InteracaoWorkflowVO obj, Boolean validarDados, UsuarioVO usuarioLogado) throws Exception {
		if (validarDados) {
			if (obj.getCodigo().equals(0)) {
				incluir(obj, usuarioLogado);
			} else {
				alterar(obj, usuarioLogado);
			}
		} else {
			if (obj.getCodigo().equals(0)) {
				incluirSemValidarDados(obj, usuarioLogado);
			} else {
				alterarSemValidarDados(obj, usuarioLogado);
			}
		}

		if (obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO) || obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gravarCompromissoRealizadoComEtapa(obj.getCompromissoAgendaPessoaHorario().getCodigo(), obj.getEtapaWorkflow().getCodigo(), usuarioLogado);
		}
	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param InteracaoWorkflowVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarNavegacaoProximaEtapa(List<InteracaoWorkflowVO> listaInteracoesPercorridasNovas, List<InteracaoWorkflowVO> listaInteracoesPercorridasGravadas, InteracaoWorkflowVO interacaoWorkflowVO, EtapaWorkflowAntecedenteVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		// realizarValidacaoSeProspectInteracaoLigacaoReceptiva(interacaoWorkflowVO,
		// usuarioLogado, configuracaoGeralSistema);
		// realizarValidacaoSeCompromissoAgendaPessoaHorarioInteracaoLigacaoReceptiva(interacaoWorkflowVO,
		// usuarioLogado);
		InteracaoWorkflowVO objListaInteracoesPercorridasNovas = novaInteracao(listaInteracoesPercorridasNovas, obj);
		InteracaoWorkflowVO objListaInteracoesPercorridasGravadas = novaInteracao(listaInteracoesPercorridasGravadas, obj);
		if (objListaInteracoesPercorridasNovas == null && objListaInteracoesPercorridasGravadas == null) {
			interacaoWorkflowVO.setEtapaWorkflow(obj.getEtapaWorkflow());
			interacaoWorkflowVO.setCodigo(0);
			interacaoWorkflowVO.setNovoObj(true);
			interacaoWorkflowVO.setHoraInicio(null);
			interacaoWorkflowVO.setDataInicio(null);
			interacaoWorkflowVO.setDataTermino(null);
			interacaoWorkflowVO.setHoraTermino(null);
			interacaoWorkflowVO.setTempoDecorrido("");
			interacaoWorkflowVO.setObservacao("");
			if (interacaoWorkflowVO.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().isEmpty()) {
				interacaoWorkflowVO.getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(interacaoWorkflowVO.getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, false, usuarioLogado));
			}
			getFacadeFactory().getEtapaWorkflowFacade().carregarArquivosEtapaWorkflowVO(interacaoWorkflowVO.getEtapaWorkflow(), Boolean.FALSE, usuarioLogado);
			getFacadeFactory().getEtapaWorkflowFacade().carregarCursosEtapaWorkflowVO(interacaoWorkflowVO.getEtapaWorkflow(), Boolean.FALSE, usuarioLogado);
		} else if (objListaInteracoesPercorridasNovas == null) {
			preencherInteracaoWorkflowSemReferenciaMemoriaDadosExclusivosInteracaoEtapa(interacaoWorkflowVO, objListaInteracoesPercorridasGravadas, usuarioLogado);
		} else {
			preencherInteracaoWorkflowSemReferenciaMemoriaDadosExclusivosInteracaoEtapa(interacaoWorkflowVO, objListaInteracoesPercorridasNovas, usuarioLogado);
		}
		// interacaoWorkflowVO.getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(interacaoWorkflowVO.getEtapaWorkflow().getCodigo(),
		// false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
	}

	private InteracaoWorkflowVO novaInteracao(List<InteracaoWorkflowVO> listaInteracoesPercorridas, EtapaWorkflowAntecedenteVO obj) {
		for (InteracaoWorkflowVO interacaoWorkflowVO : listaInteracoesPercorridas) {
			if (interacaoWorkflowVO.getEtapaWorkflow().getCodigo().equals(obj.getEtapaWorkflow().getCodigo())) {
				return interacaoWorkflowVO;
			}
		}
		return null;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarValidacaoSeProspectInteracaoLigacaoReceptiva(InteracaoWorkflowVO interacaoWorkflowVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (interacaoWorkflowVO.getProspect().getCodigo().intValue() == 0) {
			getFacadeFactory().getProspectsFacade().incluirRapidaPorLigacaoReceptia(interacaoWorkflowVO.getProspect(), usuarioLogado, configuracaoGeralSistema);
		} else {
			getFacadeFactory().getProspectsFacade().alterarSemValidarDados(interacaoWorkflowVO.getProspect(), true, usuarioLogado, configuracaoGeralSistema, false);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarValidacaoSeCompromissoAgendaPessoaHorarioInteracaoLigacaoReceptiva(InteracaoWorkflowVO interacaoWorkflowVO, UsuarioVO usuarioLogado) throws Exception {
		if (interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getCodigo().intValue() == 0) {
			AgendaPessoaVO agendaPessoa = getFacadeFactory().getAgendaPessoaFacade().consultarPorCodigoPessoa(usuarioLogado.getPessoa().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
			AgendaPessoaHorarioVO agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agendaPessoa, Uteis.getDiaMesData(new Date()), Uteis.getMesData(new Date()), Uteis.getAnoData(new Date()), Uteis.getDiaSemanaEnum(new Date()), true);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(agendaPessoaHorarioVO);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setCampanha(interacaoWorkflowVO.getCampanha());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDataCadastro(new Date());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDataCompromisso(new Date());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDescricao("Atendimento a ligação receptiva");
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setHora(Uteis.getHoraMinutoComMascara(new Date()));
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setObservacao("");
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setOrigem("");
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setProspect(interacaoWorkflowVO.getProspect());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setTipoCompromisso(TipoCompromissoEnum.CONTATO);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setTipoContato(TipoContatoEnum.TELEFONE);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.PARALIZADO);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setUrgente(Boolean.FALSE);
			// getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario(),
			// usuarioLogado);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarValidacaoSeCompromissoAgendaPessoaHorarioInteracaoLigacaoReceptivaGravarInteracao(InteracaoWorkflowVO interacaoWorkflowVO, UsuarioVO usuarioLogado) throws Exception {
		if (interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getCodigo().intValue() == 0) {
			AgendaPessoaVO agendaPessoa = getFacadeFactory().getAgendaPessoaFacade().consultarPorCodigoPessoa(usuarioLogado.getPessoa().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
			if (agendaPessoa.getCodigo().intValue() == 0) {
				agendaPessoa.setPessoa(usuarioLogado.getPessoa());
				getFacadeFactory().getAgendaPessoaFacade().persistir(agendaPessoa, usuarioLogado);
				getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(agendaPessoa, new Date(), usuarioLogado);
			}
			AgendaPessoaHorarioVO agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agendaPessoa, Uteis.getDiaMesData(new Date()), Uteis.getMesData(new Date()), Uteis.getAnoData(new Date()), Uteis.getDiaSemanaEnum(new Date()), true);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(agendaPessoaHorarioVO);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setCampanha(interacaoWorkflowVO.getCampanha());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDataCadastro(new Date());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDataCompromisso(new Date());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDataInicialCompromisso(new Date());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDescricao("Atendimento a ligação receptiva");
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setHora(Uteis.getHoraMinutoComMascara(new Date()));
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setObservacao("");
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setOrigem("");
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setProspect(interacaoWorkflowVO.getProspect());
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setTipoCompromisso(TipoCompromissoEnum.CONTATO);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setTipoContato(TipoContatoEnum.TELEFONE);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.PARALIZADO);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setUrgente(Boolean.FALSE);
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario(), usuarioLogado);

		} else if (interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getCodigo().intValue() > 0 && interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getDataCompromisso().compareTo(new Date()) > 0) {
			AgendaPessoaVO agendaPessoa = getFacadeFactory().getAgendaPessoaFacade().consultarPorCodigoPessoa(usuarioLogado.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
			if (agendaPessoa.getCodigo().intValue() == 0) {
				agendaPessoa.setPessoa(usuarioLogado.getPessoa());
				getFacadeFactory().getAgendaPessoaFacade().persistir(agendaPessoa, usuarioLogado);
			}
			Date dataBase = new Date();
			AgendaPessoaHorarioVO agendaPessoaHorarioVO = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(agendaPessoa.getCodigo(), dataBase, Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioLogado);
			if (agendaPessoaHorarioVO.getCodigo() == 0) {
				agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agendaPessoa, Uteis.getDiaMesData(dataBase), Uteis.getMesData(dataBase), Uteis.getAnoData(dataBase), Uteis.getDiaSemanaEnum(dataBase), true);
				getFacadeFactory().getAgendaPessoaHorarioFacade().incluir(agendaPessoaHorarioVO, usuarioLogado);
				interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(agendaPessoaHorarioVO);
			}
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(agendaPessoaHorarioVO);
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().alterarDataCompromissoRealizado(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getCodigo(), dataBase, Uteis.getHoraMinutoComMascara(dataBase), interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getCodigo(), usuarioLogado);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setDataCompromisso(dataBase);
			interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setHora(Uteis.getHoraMinutoComMascara(dataBase));
		} else {
			if (interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getHora() != null 
					&& !interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getHora().trim().isEmpty()
					&& usuarioLogado.getPessoa().getCodigo() != 0) {
				AgendaPessoaVO agendaPessoa = getFacadeFactory().getAgendaPessoaFacade().consultarPorCodigoPessoa(usuarioLogado.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
				if (agendaPessoa.getCodigo().intValue() == 0) {
					agendaPessoa.setPessoa(usuarioLogado.getPessoa());
					getFacadeFactory().getAgendaPessoaFacade().persistir(agendaPessoa, usuarioLogado);
				}
				AgendaPessoaHorarioVO agendaPessoaHorarioVO = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(agendaPessoa.getCodigo(), interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getDataCompromisso(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, usuarioLogado);
				if (agendaPessoaHorarioVO.getCodigo() == 0) {
					agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agendaPessoa, Uteis.getDiaMesData(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getDataCompromisso()), Uteis.getMesData(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getDataCompromisso()), Uteis.getAnoData(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getDataCompromisso()), Uteis.getDiaSemanaEnum(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getDataCompromisso()), true);
					getFacadeFactory().getAgendaPessoaHorarioFacade().incluir(agendaPessoaHorarioVO, usuarioLogado);
					interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(agendaPessoaHorarioVO);
				}
				interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(agendaPessoaHorarioVO);							
				getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().alterarDataCompromissoRealizado(interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getCodigo(), interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getDataCompromisso(), interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getHora(), interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getCodigo(), usuarioLogado);
			}
		}

	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param InteracaoWorkflowVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InteracaoWorkflowVO realizarNavegacaoEtapaAnterior(InteracaoWorkflowVO interacaoWorkflowVO, UsuarioVO usuarioLogado) throws Exception {
		// interacaoWorkflowVO =
		// (consultarInteracaoWorkflowAnterior(interacaoWorkflowVO.getCodigo(),
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		if (interacaoWorkflowVO.getEtapaWorkflow().getCodigo() == null || interacaoWorkflowVO.getEtapaWorkflow().getCodigo().intValue() == 0) {
			throw new Exception("Não foi possivel voltar etapa pois, não foi encontrada nenhuma etapa para o código : " + interacaoWorkflowVO.getCodigo());
		}
		interacaoWorkflowVO.setDataTermino(null);
		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().executarAtualizacaoEtapaAtualCompromisso(interacaoWorkflowVO.getEtapaWorkflow().getCodigo(), interacaoWorkflowVO.getCompromissoAgendaPessoaHorario().getCodigo(), usuarioLogado);
		// interacaoWorkflowVO.getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(interacaoWorkflowVO.getEtapaWorkflow().getCodigo(),
		// false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		return interacaoWorkflowVO;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>InteracaoWorkflowVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(InteracaoWorkflowVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}

		if ((obj.getProspect() == null) || (obj.getProspect().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_prospect"));
		}
		if ((obj.getCampanha() == null) || (obj.getCampanha().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_campanha"));
		}
		// if ((obj.getUnidadeEnsino() == null)
		// || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_unidadeEnsino"));
		// }
		if ((obj.getWorkflow() == null) || (obj.getWorkflow().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_workflow"));
		}
		if ((obj.getCompromissoAgendaPessoaHorario() == null) || (obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_compromissoAgendaPessoaHorario"));
		}
		if (obj.getEtapaWorkflow().getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_etapaWorkflow"));
		}
		if (obj.getEtapaWorkflow().getObrigatorioInformarObservacao() && obj.getObservacao().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_observacao"));
		}
		if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InteracaoWorkflow_responsavel"));
		}

	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>InteracaoWorkflowVO</code>.
	 */
	public void validarUnicidade(List<InteracaoWorkflowVO> lista, InteracaoWorkflowVO obj) throws ConsistirException {
		for (InteracaoWorkflowVO repetido : lista) {
		}
	}

	public InteracaoWorkflowVO executarPreenchimnetoNovaInteracaoWorkflowPorCompromissoPorEtapaAtual(Integer compromisso, Integer etapaAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT cap.codigo as cap_codigo, cap.dataCompromisso as cap_dataCompromisso, cap.dataInicialCompromisso as cap_dataInicialCompromisso, cap.hora as cap_hora, cap.duvida AS cap_duvida, cap.tipoCompromisso AS cap_tipoCompromisso, ");
		sqlStr.append("  aph.codigo as aph_codigo, ap.codigo as ap_codigo, ap.pessoa as ap_pessoa, ");
		sqlStr.append(" prospects.arquivofoto AS prospects_arquivofoto,  ");
		sqlStr.append(" prospects.codigo AS prospects_codigo,  ");
		sqlStr.append(" prospects.nome AS prospects_nome,  ");
		sqlStr.append(" prospects.consultorPadrao AS prospects_consultorPadrao, prospects.responsavelFinanceiro AS prospects_responsavelFinanceiro, ");
		sqlStr.append(" prospects.dataNascimento AS prospects_dataNascimento,  ");
		sqlStr.append(" prospects.sexo AS prospects_sexo, ");
		sqlStr.append(" prospects.emailprincipal AS prospects_email,  ");
		sqlStr.append(" prospects.telefoneresidencial AS prospects_telefoneresidencial ,  ");
		sqlStr.append(" prospects.celular AS prospects_celular, ");
		sqlStr.append(" prospects.telefonecomercial AS prospects_telefonecomercial,  ");
		sqlStr.append(" prospects.telefonerecado AS prospects_telefonerecado,  ");
		sqlStr.append(" prospects.razaosocial as prospects_razaosocial, prospects.cpf as prospects_cpf , prospects.cnpj as prospects_cnpj , prospects.rg as prospects_rg, prospects.orgaoemissor as prospects_orgaoemissor, prospects.estadoemissor as prospects_estadoemissor,  ");
		sqlStr.append(" prospects.inscricaoEstadual as prospects_inscricaoEstadual , prospects.endereco as prospects_endereco,  prospects.emailSecundario as prospects_emailSecundario , prospects.renda as prospects_renda , ");
		sqlStr.append(" prospects.skype as prospects_skype , prospects.setor as prospects_setor , prospects.complemento as prospects_complemento, ");
		sqlStr.append(" prospects.formacaoAcademica as prospects_formacaoAcademica , prospects.tipoProspect as prospects_tipoProspect , prospects.cep as prospects_cep , prospects.tipoEmpresa as prospects_tipoEmpresa , ");
		sqlStr.append(" prospects.pessoa as prospects_pessoa , prospects.fornecedor as prospects_fornecedor, prospects.parceiro as prospects_parceiro, prospects.unidadeensinoprospect as prospects_unidadeensinoprospect, prospects.unidadeensino as prospects_unidadeensino, ");
		sqlStr.append(" arquivo.nome AS prospects_NomeArquivo, ");
		sqlStr.append(" arquivo.pastabasearquivo AS prospects_PastaBase, ");
		sqlStr.append(" cidade.codigo AS prospects_cidade, cidade.nome as prospects_cidadenome, ");
		sqlStr.append(" cursoInteresse.codigo AS cursoInteresse_codigo , cursoInteresse.dataCadastro AS cursoInteresse_dataCadastro, ");
		sqlStr.append(" cci.codigo AS cursoInteresse_curso , cci.nome AS cursoInteresse_nome, ");
		sqlStr.append(" campanha.codigo AS campanha_codigo, ");
		sqlStr.append(" campanha.descricao AS campanha_descricao, campanha.tipoCampanha AS campanha_tipoCampanha, ");
		sqlStr.append(" unidadeensino.codigo AS unidadeensino_codigo, ");
		sqlStr.append(" unidadeensino.nome AS unidadeensino_nome,");
		sqlStr.append(" curso.codigo AS curso_codigo, ");
		sqlStr.append(" curso.nome AS curso_nome,  ");
		sqlStr.append(" 0 AS turno_codigo, ");
		sqlStr.append(" '' AS turno_nome,  ");
		sqlStr.append(" wf.codigo AS wf_codigo, ");
		sqlStr.append(" wf.nome AS wf_nome,  ");
		sqlStr.append(" wf.tempomediogeraragenda AS wf_tempomediogeraragenda,  ");
		sqlStr.append(" wf.numeroSegundosAlertarUsuarioTempoMaximoInteracao AS wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao,  ");
		sqlStr.append(" ewf.codigo AS ewf_codigo, ");
		sqlStr.append(" ewf.nome AS ewf_nome, ");
		sqlStr.append(" ewf.cor AS ewf_cor,  ");
		sqlStr.append(" ewf.nivelApresentacao AS ewf_nivelApresentacao,  ");
		sqlStr.append(" ewf.permitirfinalizardessaetapa AS ewf_permitirfinalizardessaetapa, ");
		sqlStr.append(" ewf.script AS ewf_script,  ");
		sqlStr.append(" ewf.motivo AS ewf_motivo,  ");
		sqlStr.append(" ewf.tempomaximo AS ewf_tempomaximo, ");
		sqlStr.append(" ewf.tempominimo AS ewf_tempominimo, ");
		sqlStr.append(" ewf.obrigatorioInformarObservacao AS ewf_obrigatorioInformarObservacao, ");
		sqlStr.append(" situacaoprospectworkflow.codigo as situacaoprospectworkflow_codigo, ");
		sqlStr.append(" situacaoprospectworkflow.efetivacaoVendaHistorica as situacaoprospectworkflow_efetivacaoVendaHistorica, ");
		sqlStr.append(" situacaoprospectpipeline.codigo as situacaoprospectpipeline_codigo, ");
		sqlStr.append(" situacaoprospectpipeline.nome as situacaoprospectpipeline_nome, ");
		sqlStr.append(" situacaoprospectpipeline.controle as situacaoprospectpipeline_controle, ");
		sqlStr.append(" aewf.codigo AS aewf_codigo, ");
		sqlStr.append(" arquivoEWF.codigo AS arquivoEWF_codigo,  ");
		sqlStr.append(" arquivoEWF.nome AS arquivoEWF_nome,  ");
		sqlStr.append(" arquivoEWF.descricao AS arquivoEWF_descricao,  ");
		sqlStr.append(" cursoEWF.codigo AS cursoEWF_codigo, ");
		sqlStr.append(" cewf.codigo AS cewf_codigo,  ");
		sqlStr.append(" cewf.script AS cewf_script,  ");
		sqlStr.append(" cursoEWF.nome AS cursoEWF_nome, ");
		sqlStr.append(" (select case when matricula.matricula is not null then true else false end from matricula where matricula.aluno = prospects.pessoa limit 1) AS pessoa_aluno ");
		sqlStr.append(" from compromissoagendapessoahorario as cap  ");
		sqlStr.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario  ");
		sqlStr.append(" inner join agendapessoa AS ap ON ap.codigo = aph.agendapessoa  ");
		sqlStr.append(" inner join prospects ON cap.prospect = prospects.codigo  ");
		sqlStr.append(" left join arquivo AS arquivo ON prospects.arquivofoto = arquivo.codigo  ");
		sqlStr.append(" LEFT JOIN cursoInteresse ON cursoInteresse.prospects = prospects.codigo ");
		sqlStr.append(" LEFT JOIN curso as cci ON cursoInteresse.curso =  cci.codigo ");
		sqlStr.append(" LEFT JOIN cidade ON cidade.codigo =  prospects.cidade ");
		sqlStr.append(" inner join campanha ON campanha.codigo = cap.campanha  ");
		sqlStr.append(" inner join unidadeensino ON campanha.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" left join curso  ON curso.codigo = campanha.curso ");
		sqlStr.append(" inner join workflow AS wf ON campanha.workflow = wf.codigo ");
		sqlStr.append(" left join etapaworkflow AS ewf ON ewf.codigo = cap.etapaworkflow ");
		sqlStr.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = ewf.situacaodefinirprospectfinal ");
		sqlStr.append(" left join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline  ");
		sqlStr.append(" left join arquivoEtapaWorkflow AS aewf ON  aewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join arquivo AS arquivoEWF ON arquivoEWF.codigo = aewf.arquivo ");
		sqlStr.append(" left join cursoEtapaWorkFlow AS cewf ON cewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join curso AS cursoEWF ON cursoEWF.codigo = cewf.curso ");
		sqlStr.append("WHERE  cap.codigo = ").append(compromisso);
		if (!etapaAtual.equals(0)) {
			sqlStr.append(" and cap.etapaworkflow = ").append(etapaAtual);
		}
		// sqlStr.append(" and situacaoprospectpipeline.controle = 'INICIAL' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosInteracaoWorkflowNovo(tabelaResultado, nivelMontarDados, usuario, true);
	}

	public InteracaoWorkflowVO consultarInteracaoWorkflowAnterior(Integer codigoEtapa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT cap.codigo as cap_codigo, cap.dataCompromisso as cap_dataCompromisso, cap.dataInicialCompromisso as cap_dataInicialCompromisso, cap.hora as cap_hora, ");
		sqlStr.append("  aph.codigo as aph_codigo, ap.codigo as ap_codigo, ap.pessoa as ap_pessoa, ");
		sqlStr.append(" prospects.arquivofoto AS prospects_arquivofoto,  ");
		sqlStr.append(" prospects.codigo AS prospects_codigo,  ");
		sqlStr.append(" prospects.nome AS prospects_nome,  ");
		sqlStr.append(" prospects.dataNascimento AS prospects_dataNascimento,  ");
		sqlStr.append(" prospects.sexo AS prospects_sexo, ");
		sqlStr.append(" prospects.emailprincipal AS prospects_email,  ");
		sqlStr.append(" prospects.telefoneresidencial AS prospects_telefoneresidencial ,  ");
		sqlStr.append(" prospects.celular AS prospects_celular, ");
		sqlStr.append(" prospects.telefonecomercial AS prospects_telefonecomercial,  ");
		sqlStr.append(" prospects.telefonerecado AS prospects_telefonerecado,  ");
		sqlStr.append(" prospects.razaosocial as prospects_razaosocial, prospects.cpf as prospects_cpf , prospects.cnpj as prospects_cnpj , prospects.rg as prospects_rg, prospects.orgaoemissor as prospects_orgaoemissor, prospects.estadoemissor as prospects_estadoemissor, ");
		sqlStr.append(" prospects.inscricaoEstadual as prospects_inscricaoEstadual , prospects.endereco as prospects_endereco,  prospects.emailSecundario as prospects_emailSecundario , prospects.renda as prospects_renda , ");
		sqlStr.append(" prospects.skype as prospects_skype , prospects.setor as prospects_setor , prospects.complemento as prospects_complemento, ");
		sqlStr.append(" prospects.formacaoAcademica as prospects_formacaoAcademica , prospects.tipoProspect as prospects_tipoProspect , prospects.cep as prospects_cep , prospects.tipoEmpresa as prospects_tipoEmpresa , ");
		sqlStr.append(" prospects.pessoa as prospects_pessoa , prospects.fornecedor as prospects_fornecedor, prospects.parceiro as prospects_parceiro, prospects.unidadeensinoprospect as prospects_unidadeensinoprospect, prospects.unidadeensino as prospects_unidadeensino, ");
		sqlStr.append(" arquivo.nome AS prospects_NomeArquivo, ");
		sqlStr.append(" arquivo.pastabasearquivo AS prospects_PastaBase, ");
		sqlStr.append(" cidade.codigo AS prospects_cidade, cidade.nome as prospects_cidadenome, ");
		sqlStr.append(" cursoInteresse.codigo AS cursoInteresse_codigo , cursoInteresse.dataCadastro AS cursoInteresse_dataCadastro, ");
		sqlStr.append(" cci.codigo AS cursoInteresse_curso , cci.nome AS cursoInteresse_nome, ");
		sqlStr.append(" campanha.codigo AS campanha_codigo, ");
		sqlStr.append(" campanha.descricao AS campanha_descricao,  ");
		sqlStr.append(" unidadeensino.codigo AS unidadeensino_codigo, ");
		sqlStr.append(" unidadeensino.nome AS unidadeensino_nome,");
		sqlStr.append(" curso.codigo AS curso_codigo, ");
		sqlStr.append(" curso.nome AS curso_nome,  ");
		sqlStr.append(" wf.codigo AS wf_codigo, ");
		sqlStr.append(" wf.nome AS wf_nome,  ");
		sqlStr.append(" wf.numeroSegundosAlertarUsuarioTempoMaximoInteracao AS wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao,  ");
		sqlStr.append(" ewf.codigo AS ewf_codigo, ");
		sqlStr.append(" ewf.nome AS ewf_nome, ");
		sqlStr.append(" ewf.cor AS ewf_cor,  ");
		sqlStr.append(" ewf.motivo AS ewf_motivo,  ");
		sqlStr.append(" ewf.nivelApresentacao AS ewf_nivelApresentacao,  ");
		sqlStr.append(" ewf.permitirfinalizardessaetapa AS ewf_permitirfinalizardessaetapa, ");
		sqlStr.append(" ewf.script AS ewf_script,  ");
		sqlStr.append(" ewf.tempomaximo AS ewf_tempomaximo, ");
		sqlStr.append(" ewf.tempominimo AS ewf_tempominimo, ");
		sqlStr.append(" ewf.obrigatorioInformarObservacao AS ewf_obrigatorioInformarObservacao, ");
		sqlStr.append(" situacaoprospectworkflow.codigo as situacaoprospectworkflow_codigo, ");
		sqlStr.append(" situacaoprospectworkflow.efetivacaoVendaHistorica as situacaoprospectworkflow_efetivacaoVendaHistorica, ");
		sqlStr.append(" situacaoprospectpipeline.codigo as situacaoprospectpipeline_codigo, ");
		sqlStr.append(" situacaoprospectpipeline.nome as situacaoprospectpipeline_nome, ");
		sqlStr.append(" situacaoprospectpipeline.controle as situacaoprospectpipeline_controle, ");
		sqlStr.append(" aewf.codigo AS aewf_codigo, ");
		sqlStr.append(" interacaoWorkflow.codigo AS interacaoWorkflow_codigo, ");
		sqlStr.append(" interacaoWorkflow.datainicio AS interacaoWorkflow_datainicio, ");
		sqlStr.append(" interacaoWorkflow.datatermino AS interacaoWorkflow_datatermino, ");
		sqlStr.append(" interacaoWorkflow.tipoMidia AS interacaoWorkflow_tipoMidia, ");
		sqlStr.append(" interacaoWorkflow.horainicio AS interacaoWorkflow_horainicio, ");
		sqlStr.append(" interacaoWorkflow.horatermino AS interacaoWorkflow_horatermino, ");
		sqlStr.append(" interacaoWorkflow.observacao AS interacaoWorkflow_observacao, ");
		sqlStr.append(" interacaoWorkflow.motivo AS interacaoWorkflow_motivo, ");
		sqlStr.append(" interacaoWorkflow.tipointeracao AS interacaoWorkflow_tipointeracao, ");
		sqlStr.append(" interacaoWorkflow.tempodecorrido AS interacaoWorkflow_tempodecorrido, ");
		
        sqlStr.append(" interacaoWorkflow.tipoOrigemInteracao AS interacaoWorkflow_tipoOrigemInteracao,");
        sqlStr.append(" interacaoWorkflow.identificadorOrigem AS interacaoWorkflow_identificadorOrigem,");
        sqlStr.append(" interacaoWorkflow.codigoEntidadeOrigem AS interacaoWorkflow_codigoEntidadeOrigem,");		
		
		sqlStr.append(" motivoinsucesso.codigo AS motivoinsucesso_codigo, ");
		sqlStr.append(" arquivoEWF.codigo AS arquivoEWF_codigo,  ");
		sqlStr.append(" arquivoEWF.nome AS arquivoEWF_nome,  ");
		sqlStr.append(" arquivoEWF.descricao AS arquivoEWF_descricao,  ");
		sqlStr.append(" cursoEWF.codigo AS cursoEWF_codigo, ");
		sqlStr.append(" cewf.codigo AS cewf_codigo,  ");
		sqlStr.append(" cewf.script AS cewf_script,  ");
		sqlStr.append(" cursoEWF.nome AS cursoEWF_nome ");
		sqlStr.append(" from interacaoWorkflow  ");
		sqlStr.append(" inner join compromissoagendapessoahorario AS cap ON cap.codigo = interacaoWorkflow.compromissoagendapessoahorario  ");
		sqlStr.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario  ");
		sqlStr.append(" inner join agendapessoa AS ap ON ap.codigo = aph.agendapessoa  ");
		sqlStr.append(" left join motivoInsucesso on interacaoWorkflow.motivoInsucesso = motivoInsucesso.codigo ");
		sqlStr.append(" inner join prospects ON interacaoWorkflow.prospect = prospects.codigo  ");
		sqlStr.append(" left join arquivo AS arquivo ON prospects.arquivofoto = arquivo.codigo  ");
		sqlStr.append(" LEFT JOIN cursoInteresse ON cursoInteresse.prospects = prospects.codigo ");
		sqlStr.append(" LEFT JOIN curso as cci ON cursoInteresse.curso =  cci.codigo ");
		sqlStr.append(" LEFT JOIN cidade ON cidade.codigo =  prospects.cidade ");
		sqlStr.append(" inner join campanha ON campanha.codigo = interacaoWorkflow.campanha  ");
		sqlStr.append(" left join unidadeensino ON interacaoWorkflow.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" left join curso  ON curso.codigo = campanha.curso ");
		sqlStr.append(" inner join workflow AS wf ON interacaoWorkflow.workflow = wf.codigo ");
		sqlStr.append(" inner join etapaworkflow AS ewf ON ewf.codigo = interacaoWorkflow.etapaworkflow ");
		sqlStr.append(" inner join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = ewf.situacaodefinirprospectfinal ");
		sqlStr.append(" inner join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline  ");
		sqlStr.append(" left join arquivoEtapaWorkflow AS aewf ON  aewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join arquivo AS arquivoEWF ON arquivoEWF.codigo = aewf.arquivo ");
		sqlStr.append(" left join cursoEtapaWorkFlow AS cewf ON cewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join curso AS cursoEWF ON cursoEWF.codigo = cewf.curso ");
		sqlStr.append(" WHERE  interacaoWorkflow.codigo = ").append(codigoEtapa);
		sqlStr.append(" ORDER BY interacaoWorkflow.codigo desc ");
		sqlStr.append(" LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosInteracaoWorkflowNovo(tabelaResultado, nivelMontarDados, usuario, false);
	}

	public InteracaoWorkflowVO consultarInteracaoWorkflowExistentePorCodigoCompromissoPorEtapaAtual(Integer compromisso, Integer etapaAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT cap.codigo as cap_codigo, cap.dataCompromisso as cap_dataCompromisso, cap.dataInicialCompromisso as cap_dataInicialCompromisso, cap.hora as cap_hora, ");
		sqlStr.append("  aph.codigo as aph_codigo, ap.codigo as ap_codigo, ap.pessoa as ap_pessoa, ");
		sqlStr.append(" prospects.arquivofoto AS prospects_arquivofoto,  ");
		sqlStr.append(" prospects.codigo AS prospects_codigo,  ");
		sqlStr.append(" prospects.nome AS prospects_nome,  ");
		sqlStr.append(" prospects.consultorPadrao AS prospects_consultorPadrao,  ");
		sqlStr.append(" prospects.dataNascimento AS prospects_dataNascimento,  ");
		sqlStr.append(" prospects.sexo AS prospects_sexo, ");
		sqlStr.append(" prospects.emailprincipal AS prospects_email,  ");
		sqlStr.append(" prospects.telefoneresidencial AS prospects_telefoneresidencial ,  ");
		sqlStr.append(" prospects.celular AS prospects_celular, ");
		sqlStr.append(" prospects.telefonecomercial AS prospects_telefonecomercial,  ");
		sqlStr.append(" prospects.telefonerecado AS prospects_telefonerecado,  ");
		sqlStr.append(" prospects.razaosocial as prospects_razaosocial, prospects.cpf as prospects_cpf , prospects.cnpj as prospects_cnpj , prospects.rg as prospects_rg, prospects.orgaoemissor as prospects_orgaoemissor, prospects.estadoemissor as prospects_estadoemissor, ");
		sqlStr.append(" prospects.inscricaoEstadual as prospects_inscricaoEstadual , prospects.endereco as prospects_endereco,  prospects.emailSecundario as prospects_emailSecundario , prospects.renda as prospects_renda , ");
		sqlStr.append(" prospects.skype as prospects_skype , prospects.setor as prospects_setor , prospects.complemento as prospects_complemento, ");
		sqlStr.append(" prospects.formacaoAcademica as prospects_formacaoAcademica , prospects.tipoProspect as prospects_tipoProspect , prospects.cep as prospects_cep , prospects.tipoEmpresa as prospects_tipoEmpresa , ");
		sqlStr.append(" prospects.pessoa as prospects_pessoa , prospects.fornecedor as prospects_fornecedor, prospects.parceiro as prospects_parceiro, prospects.unidadeensinoprospect as prospects_unidadeensinoprospect, prospects.unidadeensino as prospects_unidadeensino, ");
		sqlStr.append(" arquivo.nome AS prospects_NomeArquivo, ");
		sqlStr.append(" arquivo.pastabasearquivo AS prospects_PastaBase, ");
		sqlStr.append(" cidade.codigo AS prospects_cidade, cidade.nome as prospects_cidadenome, ");
		sqlStr.append(" cursoInteresse.codigo AS cursoInteresse_codigo , cursoInteresse.dataCadastro AS cursoInteresse_dataCadastro, ");
		sqlStr.append(" cci.codigo AS cursoInteresse_curso , cci.nome AS cursoInteresse_nome, ");
		sqlStr.append(" campanha.codigo AS campanha_codigo, ");
		sqlStr.append(" campanha.descricao AS campanha_descricao,  ");
		sqlStr.append(" unidadeensino.codigo AS unidadeensino_codigo, ");
		sqlStr.append(" unidadeensino.nome AS unidadeensino_nome,");
		sqlStr.append(" curso.codigo AS curso_codigo, ");
		sqlStr.append(" curso.nome AS curso_nome,  ");
		sqlStr.append(" turno.codigo AS turno_codigo, ");
		sqlStr.append(" turno.nome AS turno_nome,  ");
		sqlStr.append(" wf.codigo AS wf_codigo, ");
		sqlStr.append(" wf.nome AS wf_nome,  ");
		sqlStr.append(" wf.tempomediogeraragenda AS wf_tempomediogeraragenda,  ");
		sqlStr.append(" wf.numeroSegundosAlertarUsuarioTempoMaximoInteracao AS wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao,  ");
		sqlStr.append(" ewf.codigo AS ewf_codigo, ");
		sqlStr.append(" ewf.nome AS ewf_nome, ");
		sqlStr.append(" ewf.cor AS ewf_cor,  ");
		sqlStr.append(" ewf.nivelApresentacao AS ewf_nivelApresentacao,  ");
		sqlStr.append(" ewf.permitirfinalizardessaetapa AS ewf_permitirfinalizardessaetapa, ");
		sqlStr.append(" ewf.script AS ewf_script,  ");
		sqlStr.append(" ewf.motivo AS ewf_motivo,  ");
		sqlStr.append(" ewf.tempomaximo AS ewf_tempomaximo, ");
		sqlStr.append(" ewf.tempominimo AS ewf_tempominimo, ");
		sqlStr.append(" ewf.obrigatorioInformarObservacao AS ewf_obrigatorioInformarObservacao, ");
		sqlStr.append(" situacaoprospectworkflow.codigo as situacaoprospectworkflow_codigo, ");
		sqlStr.append(" situacaoprospectworkflow.efetivacaoVendaHistorica as situacaoprospectworkflow_efetivacaoVendaHistorica, ");
		sqlStr.append(" situacaoprospectpipeline.codigo as situacaoprospectpipeline_codigo, ");
		sqlStr.append(" situacaoprospectpipeline.nome as situacaoprospectpipeline_nome, ");
		sqlStr.append(" situacaoprospectpipeline.controle as situacaoprospectpipeline_controle, ");
		sqlStr.append(" aewf.codigo AS aewf_codigo, ");
		sqlStr.append(" interacaoWorkflow.codigo AS interacaoWorkflow_codigo, ");
		sqlStr.append(" interacaoWorkflow.datainicio AS interacaoWorkflow_datainicio, ");
		sqlStr.append(" interacaoWorkflow.datatermino AS interacaoWorkflow_datatermino, ");
		sqlStr.append(" interacaoWorkflow.tipoMidia AS interacaoWorkflow_tipoMidia, ");
		sqlStr.append(" interacaoWorkflow.horainicio AS interacaoWorkflow_horainicio, ");
		sqlStr.append(" interacaoWorkflow.horatermino AS interacaoWorkflow_horatermino, ");
		sqlStr.append(" interacaoWorkflow.observacao AS interacaoWorkflow_observacao, ");
		sqlStr.append(" interacaoWorkflow.motivo AS interacaoWorkflow_motivo, ");
		sqlStr.append(" interacaoWorkflow.tipointeracao AS interacaoWorkflow_tipointeracao, ");
		sqlStr.append(" interacaoWorkflow.tempodecorrido AS interacaoWorkflow_tempodecorrido, ");
        sqlStr.append(" interacaoWorkflow.tipoOrigemInteracao AS interacaoWorkflow_tipoOrigemInteracao,");
        sqlStr.append(" interacaoWorkflow.identificadorOrigem AS interacaoWorkflow_identificadorOrigem,");
        sqlStr.append(" interacaoWorkflow.codigoEntidadeOrigem AS interacaoWorkflow_codigoEntidadeOrigem,");
		sqlStr.append(" motivoinsucesso.codigo AS motivoinsucesso_codigo, ");
		sqlStr.append(" arquivoEWF.codigo AS arquivoEWF_codigo,  ");
		sqlStr.append(" arquivoEWF.nome AS arquivoEWF_nome,  ");
		sqlStr.append(" arquivoEWF.descricao AS arquivoEWF_descricao,  ");
		sqlStr.append(" cursoEWF.codigo AS cursoEWF_codigo, ");
		sqlStr.append(" cewf.codigo AS cewf_codigo,  ");
		sqlStr.append(" cewf.script AS cewf_script,  ");
		sqlStr.append(" cursoEWF.nome AS cursoEWF_nome ");
		sqlStr.append(" from interacaoWorkflow  ");
		sqlStr.append(" inner join compromissoagendapessoahorario AS cap ON cap.codigo = interacaoWorkflow.compromissoagendapessoahorario  ");
		sqlStr.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario  ");
		sqlStr.append(" inner join agendapessoa AS ap ON ap.codigo = aph.agendapessoa  ");
		sqlStr.append(" left join motivoInsucesso on interacaoWorkflow.motivoInsucesso = motivoInsucesso.codigo ");
		sqlStr.append(" inner join prospects ON interacaoWorkflow.prospect = prospects.codigo  ");
		sqlStr.append(" left join arquivo AS arquivo ON prospects.arquivofoto = arquivo.codigo  ");
		sqlStr.append(" LEFT JOIN cursoInteresse ON cursoInteresse.prospects = prospects.codigo ");
		sqlStr.append(" LEFT JOIN curso as cci ON cursoInteresse.curso =  cci.codigo ");
		sqlStr.append(" LEFT JOIN cidade ON cidade.codigo =  prospects.cidade ");
		sqlStr.append(" inner join campanha ON campanha.codigo = interacaoWorkflow.campanha  ");
		sqlStr.append(" left join unidadeensino ON interacaoWorkflow.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" left join curso  ON interacaoWorkflow.curso = curso.codigo ");
		sqlStr.append(" left join turno  ON interacaoWorkflow.turno = turno.codigo ");
		sqlStr.append(" inner join workflow AS wf ON interacaoWorkflow.workflow = wf.codigo ");
		sqlStr.append(" inner join etapaworkflow AS ewf ON ewf.codigo = interacaoWorkflow.etapaworkflow ");
		sqlStr.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = ewf.situacaodefinirprospectfinal ");
		sqlStr.append(" left join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline  ");
		sqlStr.append(" left join arquivoEtapaWorkflow AS aewf ON  aewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join arquivo AS arquivoEWF ON arquivoEWF.codigo = aewf.arquivo ");
		sqlStr.append(" left join cursoEtapaWorkFlow AS cewf ON cewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join curso AS cursoEWF ON cursoEWF.codigo = cewf.curso ");
		sqlStr.append(" WHERE  cap.codigo = ").append(compromisso);
		sqlStr.append(" and interacaoWorkflow.etapaworkflow = ").append(etapaAtual);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosInteracaoWorkflowNovo(tabelaResultado, nivelMontarDados, usuario, false);
	}

	public List<InteracaoWorkflowVO> consultarInteracoesWorkflowExistentesPorCodigoCompromisso(InteracaoWorkflowVO interacaoWorkflow, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT cap.codigo as cap_codigo, ");
		sqlStr.append("  aph.codigo as aph_codigo, ap.codigo as ap_codigo, ap.pessoa as ap_pessoa, ");
		sqlStr.append(" prospects.arquivofoto AS prospects_arquivofoto,  ");
		sqlStr.append(" prospects.codigo AS prospects_codigo,  ");
		sqlStr.append(" prospects.nome AS prospects_nome,  ");
		sqlStr.append(" prospects.dataNascimento AS prospects_dataNascimento,  ");
		sqlStr.append(" prospects.sexo AS prospects_sexo, ");
		sqlStr.append(" prospects.emailprincipal AS prospects_email,  ");
		sqlStr.append(" prospects.telefoneresidencial AS prospects_telefoneresidencial ,  ");
		sqlStr.append(" prospects.celular AS prospects_celular, ");
		sqlStr.append(" prospects.telefonecomercial AS prospects_telefonecomercial,  ");
		sqlStr.append(" prospects.telefonerecado AS prospects_telefonerecado,  ");
		sqlStr.append(" prospects.razaosocial as prospects_razaosocial, prospects.cpf as prospects_cpf , prospects.cnpj as prospects_cnpj , prospects.rg as prospects_rg, prospects.orgaoemissor as prospects_orgaoemissor, prospects.estadoemissor as prospects_estadoemissor, ");
		sqlStr.append(" prospects.inscricaoEstadual as prospects_inscricaoEstadual , prospects.endereco as prospects_endereco,  prospects.emailSecundario as prospects_emailSecundario , prospects.renda as prospects_renda , ");
		sqlStr.append(" prospects.skype as prospects_skype , prospects.setor as prospects_setor , prospects.complemento as prospects_complemento, ");
		sqlStr.append(" prospects.formacaoAcademica as prospects_formacaoAcademica , prospects.tipoProspect as prospects_tipoProspect , prospects.cep as prospects_cep , prospects.tipoEmpresa as prospects_tipoEmpresa , ");
		sqlStr.append(" prospects.pessoa as prospects_pessoa , prospects.fornecedor as prospects_fornecedor, prospects.parceiro as prospects_parceiro, prospects.unidadeensinoprospect as prospects_unidadeensinoprospect, prospects.unidadeensino as prospects_unidadeensino, ");
		sqlStr.append(" arquivo.nome AS prospects_NomeArquivo, ");
		sqlStr.append(" arquivo.pastabasearquivo AS prospects_PastaBase, ");
		sqlStr.append(" cidade.codigo AS prospects_cidade, cidade.nome as prospects_cidadenome, ");
		sqlStr.append(" cursoInteresse.codigo AS cursoInteresse_codigo , cursoInteresse.dataCadastro AS cursoInteresse_dataCadastro, ");
		sqlStr.append(" cci.codigo AS cursoInteresse_curso , cci.nome AS cursoInteresse_nome, ");
		sqlStr.append(" campanha.codigo AS campanha_codigo, ");
		sqlStr.append(" campanha.descricao AS campanha_descricao,  ");
		sqlStr.append(" unidadeensino.codigo AS unidadeensino_codigo, ");
		sqlStr.append(" unidadeensino.nome AS unidadeensino_nome,");
		sqlStr.append(" curso.codigo AS curso_codigo, ");
		sqlStr.append(" curso.nome AS curso_nome,  ");
		sqlStr.append(" turno.codigo AS turno_codigo, ");
		sqlStr.append(" turno.nome AS turno_nome,  ");
		sqlStr.append(" wf.codigo AS wf_codigo, ");
		sqlStr.append(" wf.nome AS wf_nome,  ");
		sqlStr.append(" wf.numeroSegundosAlertarUsuarioTempoMaximoInteracao AS wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao,  ");
		sqlStr.append(" ewf.codigo AS ewf_codigo, ");
		sqlStr.append(" ewf.nome AS ewf_nome, ");
		sqlStr.append(" ewf.cor AS ewf_cor,  ");
		sqlStr.append(" ewf.motivo AS ewf_motivo,  ");
		sqlStr.append(" ewf.nivelApresentacao AS ewf_nivelApresentacao,  ");
		sqlStr.append(" ewf.permitirfinalizardessaetapa AS ewf_permitirfinalizardessaetapa, ");
		sqlStr.append(" ewf.script AS ewf_script,  ");
		sqlStr.append(" ewf.tempomaximo AS ewf_tempomaximo, ");
		sqlStr.append(" ewf.tempominimo AS ewf_tempominimo, ");
		sqlStr.append(" ewf.obrigatorioInformarObservacao AS ewf_obrigatorioInformarObservacao, ");
		sqlStr.append(" situacaoprospectworkflow.codigo as situacaoprospectworkflow_codigo, ");
		sqlStr.append(" situacaoprospectworkflow.efetivacaoVendaHistorica as situacaoprospectworkflow_efetivacaoVendaHistorica, ");
		sqlStr.append(" situacaoprospectpipeline.codigo as situacaoprospectpipeline_codigo, ");
		sqlStr.append(" situacaoprospectpipeline.nome as situacaoprospectpipeline_nome, ");
		sqlStr.append(" situacaoprospectpipeline.controle as situacaoprospectpipeline_controle, ");
		sqlStr.append(" aewf.codigo AS aewf_codigo, ");
		sqlStr.append(" interacaoWorkflow.codigo AS interacaoWorkflow_codigo, ");
		sqlStr.append(" interacaoWorkflow.datainicio AS interacaoWorkflow_datainicio, ");
		sqlStr.append(" interacaoWorkflow.datatermino AS interacaoWorkflow_datatermino, ");
		sqlStr.append(" interacaoWorkflow.tipoMidia AS interacaoWorkflow_tipoMidia, ");
		sqlStr.append(" interacaoWorkflow.horainicio AS interacaoWorkflow_horainicio, ");
		sqlStr.append(" interacaoWorkflow.horatermino AS interacaoWorkflow_horatermino, ");
		sqlStr.append(" interacaoWorkflow.observacao AS interacaoWorkflow_observacao, ");
		sqlStr.append(" interacaoWorkflow.motivo AS interacaoWorkflow_motivo, ");
		sqlStr.append(" interacaoWorkflow.tipointeracao AS interacaoWorkflow_tipointeracao, ");
		sqlStr.append(" interacaoWorkflow.tempodecorrido AS interacaoWorkflow_tempodecorrido, ");
		sqlStr.append(" motivoinsucesso.codigo AS motivoinsucesso_codigo, ");
		sqlStr.append(" arquivoEWF.codigo AS arquivoEWF_codigo,  ");
		sqlStr.append(" arquivoEWF.nome AS arquivoEWF_nome,  ");
		sqlStr.append(" arquivoEWF.descricao AS arquivoEWF_descricao,  ");
		sqlStr.append(" cursoEWF.codigo AS cursoEWF_codigo, ");
		sqlStr.append(" cewf.codigo AS cewf_codigo,  ");
		sqlStr.append(" cewf.script AS cewf_script,  ");
		sqlStr.append(" cursoEWF.nome AS cursoEWF_nome ");
		sqlStr.append(" from interacaoWorkflow  ");
		sqlStr.append(" inner join compromissoagendapessoahorario AS cap ON cap.codigo = interacaoWorkflow.compromissoagendapessoahorario  ");
		sqlStr.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario  ");
		sqlStr.append(" inner join agendapessoa AS ap ON ap.codigo = aph.agendapessoa  ");
		sqlStr.append(" left join motivoInsucesso on interacaoWorkflow.motivoInsucesso = motivoInsucesso.codigo ");
		sqlStr.append(" inner join prospects ON interacaoWorkflow.prospect = prospects.codigo  ");
		sqlStr.append(" left join arquivo AS arquivo ON prospects.arquivofoto = arquivo.codigo  ");
		sqlStr.append(" LEFT JOIN cursoInteresse ON cursoInteresse.prospects = prospects.codigo ");
		sqlStr.append(" LEFT JOIN curso as cci ON cursoInteresse.curso =  cci.codigo ");
		sqlStr.append(" LEFT JOIN cidade ON cidade.codigo =  prospects.cidade ");
		sqlStr.append(" inner join campanha ON campanha.codigo = interacaoWorkflow.campanha  ");
		sqlStr.append(" left join unidadeensino ON interacaoWorkflow.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" left join curso  ON curso.codigo = campanha.curso ");
		sqlStr.append(" left join turno  ON interacaoWorkflow.turno = turno.codigo ");
		sqlStr.append(" inner join workflow AS wf ON interacaoWorkflow.workflow = wf.codigo ");
		sqlStr.append(" inner join etapaworkflow AS ewf ON ewf.codigo = interacaoWorkflow.etapaworkflow ");
		sqlStr.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = ewf.situacaodefinirprospectfinal ");
		sqlStr.append(" left join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline  ");
		sqlStr.append(" left join arquivoEtapaWorkflow AS aewf ON  aewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join arquivo AS arquivoEWF ON arquivoEWF.codigo = aewf.arquivo ");
		sqlStr.append(" left join cursoEtapaWorkFlow AS cewf ON cewf.etapaworkflow = ewf.codigo ");
		sqlStr.append(" left join curso AS cursoEWF ON cursoEWF.codigo = cewf.curso ");
		sqlStr.append(" WHERE  cap.codigo = ").append(interacaoWorkflow.getCompromissoAgendaPessoaHorario().getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosInteracaoWorkflowAnteriores(interacaoWorkflow, tabelaResultado, nivelMontarDados, usuario, false);
	}

	public List<InteracaoWorkflowVO> consultarInteracaoWorkflowAnteriorPorCodigoCompromissoPorEtapaAtual(Integer nivelApresentacao, Integer compromisso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT cap.codigo as cap_codigo, ");
		sqlStr.append(" ewf.codigo AS ewf_codigo, ");
		sqlStr.append(" ewf.nome AS ewf_nome, ");
		sqlStr.append(" ewf.cor AS ewf_cor,  ");
		sqlStr.append(" interacaoWorkflow.codigo AS interacaoWorkflow_codigo ");
		sqlStr.append(" from interacaoWorkflow  ");
		sqlStr.append(" inner join compromissoagendapessoahorario AS cap ON cap.codigo = interacaoWorkflow.compromissoagendapessoahorario  ");
		sqlStr.append(" inner join etapaworkflow AS ewf ON ewf.codigo = interacaoWorkflow.etapaworkflow ");
		sqlStr.append(" WHERE  cap.codigo = ").append(compromisso).append(" AND ewf.nivelApresentacao < ").append(nivelApresentacao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosInteracaoWorkflowAnterioresBasico(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<String> consultarObservacoesEtapasAnteriores(Integer nivelApresentacao, Integer compromisso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT ewf.nome AS ewf_nome, ");
		sqlStr.append(" interacaoWorkflow.observacao AS interacaoWorkflow_observacao ");
		sqlStr.append(" from interacaoWorkflow  ");
		sqlStr.append(" inner join compromissoagendapessoahorario AS cap ON cap.codigo = interacaoWorkflow.compromissoagendapessoahorario  ");
		sqlStr.append(" inner join etapaworkflow AS ewf ON ewf.codigo = interacaoWorkflow.etapaworkflow ");
		sqlStr.append(" WHERE  cap.codigo = ").append(compromisso).append(" AND ewf.nivelApresentacao < ").append(nivelApresentacao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<String> listaString = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			String observacao = tabelaResultado.getString("ewf_nome").toUpperCase() + ": " + tabelaResultado.getString("interacaoWorkflow_observacao");
			if (!tabelaResultado.getString("interacaoWorkflow_observacao").equals("")) {
				listaString.add(observacao);
			}

		}
		return listaString;
	}

	public static List<InteracaoWorkflowVO> montarDadosInteracaoWorkflowAnterioresBasico(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<InteracaoWorkflowVO> listaObjs = new ArrayList<InteracaoWorkflowVO>(0);

		while (dadosSQL.next()) {
			InteracaoWorkflowVO obj = new InteracaoWorkflowVO();
			// Dados InteracaoWorkflow
			obj.setCodigo(dadosSQL.getInt("interacaoWorkflow_codigo"));

			// Dados Etapa
			obj.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewf_codigo"));
			obj.getEtapaWorkflow().setNome(dadosSQL.getString("ewf_nome"));
			obj.getEtapaWorkflow().setCor(dadosSQL.getString("ewf_cor"));
			listaObjs.add(obj);
		}
		return listaObjs;
	}

	public static InteracaoWorkflowVO montarDadosInteracaoWorkflowNovo(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario, Boolean novaInteracao) throws Exception {
		InteracaoWorkflowVO obj = new InteracaoWorkflowVO();
		while (dadosSQL.next()) {
			if (obj.getProspect().getCodigo() == 0) {
				// Dados SituacaoFinalEtapa

				// Dados Etapa
				obj.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewf_codigo"));
				obj.getEtapaWorkflow().setNome(dadosSQL.getString("ewf_nome"));
				obj.getEtapaWorkflow().setTempoMinimo(dadosSQL.getLong("ewf_tempominimo"));
				obj.getEtapaWorkflow().setTempoMaximo(dadosSQL.getLong("ewf_tempomaximo"));
				obj.getEtapaWorkflow().setScript(dadosSQL.getString("ewf_script"));
				obj.getEtapaWorkflow().setMotivo(dadosSQL.getString("ewf_motivo"));
				obj.getEtapaWorkflow().setNivelApresentacao(dadosSQL.getInt("ewf_nivelApresentacao"));
				obj.getEtapaWorkflow().setCor(dadosSQL.getString("ewf_cor"));
				obj.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(dadosSQL.getBoolean("ewf_permitirfinalizardessaetapa"));
				obj.getEtapaWorkflow().setObrigatorioInformarObservacao(dadosSQL.getBoolean("ewf_obrigatorioInformarObservacao"));
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(dadosSQL.getInt("situacaoprospectworkflow_codigo"));
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(dadosSQL.getDouble("situacaoprospectworkflow_efetivacaoVendaHistorica"));
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(dadosSQL.getInt("situacaoprospectpipeline_codigo"));
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(dadosSQL.getString("situacaoprospectpipeline_nome"));
				if (dadosSQL.getString("situacaoprospectpipeline_controle") != null && !dadosSQL.getString("situacaoprospectpipeline_controle").isEmpty()) {
					obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("situacaoprospectpipeline_controle")));
				}

				if (!novaInteracao) {
					obj.getMotivoInsucesso().setCodigo(dadosSQL.getInt("motivoinsucesso_codigo"));
					obj.getTipoMidia().setCodigo(dadosSQL.getInt("interacaoWorkflow_tipoMidia"));
					obj.setCodigo(dadosSQL.getInt("interacaoWorkflow_codigo"));
					if (dadosSQL.getString("interacaoWorkflow_tipoInteracao") != null) {
						obj.setTipoInteracao(TipoInteracaoEnum.valueOf(dadosSQL.getString("interacaoWorkflow_tipoInteracao")));
					}
					obj.setDataInicio(dadosSQL.getTimestamp("interacaoWorkflow_datainicio"));
					obj.setDataTermino(dadosSQL.getTimestamp("interacaoWorkflow_datatermino"));
					obj.setHoraInicio(dadosSQL.getString("interacaoWorkflow_horainicio"));
					obj.setHoraTermino(dadosSQL.getString("interacaoWorkflow_horatermino"));
					obj.setObservacao(dadosSQL.getString("interacaoWorkflow_observacao"));
					obj.setMotivo(dadosSQL.getString("interacaoWorkflow_motivo"));
					obj.setTempoDecorrido(dadosSQL.getString("interacaoWorkflow_tempodecorrido"));
					if (dadosSQL.getString("interacaoWorkflow_tipoOrigemInteracao") != null) {
						obj.setTipoOrigemInteracao(TipoOrigemInteracaoEnum.valueOf(dadosSQL.getString("interacaoWorkflow_tipoOrigemInteracao")));
					} else {
						obj.setTipoOrigemInteracao(TipoOrigemInteracaoEnum.NENHUM);
					}
		            obj.setIdentificadorOrigem(dadosSQL.getString("interacaoWorkflow_identificadorOrigem"));
		            if(dadosSQL.getObject("interacaoWorkflow_codigoEntidadeOrigem") != null) {
		            	obj.setCodigoEntidadeOrigem(dadosSQL.getInt("interacaoWorkflow_codigoEntidadeOrigem"));
		            }
				}

				// Dados campanha
				obj.getCampanha().setCodigo(dadosSQL.getInt("campanha_codigo"));
				obj.getCampanha().setDescricao(dadosSQL.getString("campanha_descricao"));
				if (dadosSQL.getString("campanha_tipoCAmpanha") != null) {
					obj.getCampanha().setTipoCampanha(TipoCampanhaEnum.valueOf(dadosSQL.getString("campanha_tipoCAmpanha")));
				}
				// Dados unidade Ensino
				obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino_codigo"));
				obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino_nome"));
				// Dados curso
				obj.getCurso().setCodigo(dadosSQL.getInt("curso_codigo"));
				obj.getCurso().setNome(dadosSQL.getString("curso_nome"));
				// Dados turno
				obj.getTurno().setCodigo(dadosSQL.getInt("turno_codigo"));
				obj.getTurno().setNome(dadosSQL.getString("turno_nome"));
				// Dados Workflow
				obj.getWorkflow().setCodigo(dadosSQL.getInt("wf_codigo"));
				obj.getWorkflow().setNome(dadosSQL.getString("wf_nome"));
				obj.getWorkflow().setTempoMedioGerarAgenda(dadosSQL.getInt("wf_tempomediogeraragenda"));
				obj.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(dadosSQL.getInt("wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao"));
				// Dados Prospect
				obj.getProspect().getArquivoFoto().setCodigo(dadosSQL.getInt("prospects_arquivofoto"));
				obj.getProspect().getArquivoFoto().setNome(dadosSQL.getString("prospects_NomeArquivo"));
				obj.getProspect().getArquivoFoto().setPastaBaseArquivo(dadosSQL.getString("prospects_PastaBase"));
				obj.getProspect().setCodigo(dadosSQL.getInt("prospects_codigo"));
				obj.getProspect().setNome(dadosSQL.getString("prospects_nome"));
				obj.getProspect().setDataNascimento(dadosSQL.getDate("prospects_dataNascimento"));
				obj.getProspect().setSexo(dadosSQL.getString("prospects_sexo"));
				obj.getProspect().setTelefoneResidencial(dadosSQL.getString("prospects_telefoneresidencial"));
				obj.getProspect().setTelefoneComercial(dadosSQL.getString("prospects_telefonecomercial"));
				obj.getProspect().setTelefoneRecado(dadosSQL.getString("prospects_telefonerecado"));
				obj.getProspect().setCelular(dadosSQL.getString("prospects_celular"));
				obj.getProspect().setEmailPrincipal(dadosSQL.getString("prospects_email"));
				obj.getProspect().setRazaoSocial(dadosSQL.getString("prospects_razaosocial"));
				obj.getProspect().setCpf(dadosSQL.getString("prospects_cpf"));
				obj.getProspect().setCnpj(dadosSQL.getString("prospects_cnpj"));
				obj.getProspect().setRg(dadosSQL.getString("prospects_rg"));
				obj.getProspect().setEstadoEmissor(dadosSQL.getString("prospects_estadoemissor"));
				obj.getProspect().setOrgaoEmissor(dadosSQL.getString("prospects_orgaoemissor"));
				obj.getProspect().setInscricaoEstadual(dadosSQL.getString("prospects_inscricaoEstadual"));
				obj.getProspect().setEndereco(dadosSQL.getString("prospects_endereco"));
				obj.getProspect().setEmailSecundario(dadosSQL.getString("prospects_emailSecundario"));
				obj.getProspect().setSkype(dadosSQL.getString("prospects_skype"));
				obj.getProspect().setSetor(dadosSQL.getString("prospects_setor"));
				obj.getProspect().setComplemento(dadosSQL.getString("prospects_complemento"));
				obj.getProspect().setCEP(dadosSQL.getString("prospects_cep"));
				obj.getProspect().getPessoa().setCodigo(dadosSQL.getInt("prospects_pessoa"));
				obj.getProspect().getFornecedor().setCodigo(dadosSQL.getInt("prospects_fornecedor"));
				obj.getProspect().getParceiro().setCodigo(dadosSQL.getInt("prospects_parceiro"));
				obj.getProspect().getUnidadeEnsino().setCodigo(dadosSQL.getInt("prospects_unidadeensino"));
				obj.getProspect().getUnidadeEnsinoProspect().setCodigo(dadosSQL.getInt("prospects_unidadeensinoprospect"));
				obj.getProspect().getCidade().setCodigo(dadosSQL.getInt("prospects_cidade"));
				obj.getProspect().getCidade().setNome(dadosSQL.getString("prospects_cidadenome"));
				obj.getProspect().getConsultorPadrao().setCodigo(dadosSQL.getInt("prospects_consultorPadrao"));
				obj.getProspect().getPessoa().setAluno(dadosSQL.getBoolean("pessoa_aluno"));
				obj.getProspect().setResponsavelFinanceiro(dadosSQL.getBoolean("prospects_responsavelFinanceiro"));
				if (dadosSQL.getString("prospects_renda") != null && !dadosSQL.getString("prospects_renda").isEmpty()) {
					obj.getProspect().setRenda(RendaProspectEnum.valueOf(dadosSQL.getString("prospects_renda")));
				}
				if (dadosSQL.getString("prospects_formacaoAcademica") != null && !dadosSQL.getString("prospects_formacaoAcademica").isEmpty()) {
					obj.getProspect().setFormacaoAcademica(FormacaoAcademicaProspectEnum.valueOf(dadosSQL.getString("prospects_formacaoAcademica")));
				}
				if (dadosSQL.getString("prospects_tipoProspect") != null && !dadosSQL.getString("prospects_tipoProspect").isEmpty()) {
					obj.getProspect().setTipoProspect(TipoProspectEnum.valueOf(dadosSQL.getString("prospects_tipoProspect")));
				}
				if (dadosSQL.getString("prospects_tipoEmpresa") != null && !dadosSQL.getString("prospects_tipoEmpresa").isEmpty()) {
					obj.getProspect().setTipoEmpresa(TipoEmpresaProspectEnum.valueOf(dadosSQL.getString("prospects_tipoEmpresa")));
				}
				obj.getCompromissoAgendaPessoaHorario().setCodigo(dadosSQL.getInt("cap_codigo"));
				obj.getCompromissoAgendaPessoaHorario().setDataCompromisso(dadosSQL.getDate("cap_dataCompromisso"));
				obj.getCompromissoAgendaPessoaHorario().setDataInicialCompromisso(dadosSQL.getDate("cap_dataInicialCompromisso"));
				obj.getCompromissoAgendaPessoaHorario().setHora(dadosSQL.getString("cap_hora"));
				obj.getCompromissoAgendaPessoaHorario().setProspect(obj.getProspect());
				obj.getCompromissoAgendaPessoaHorario().setCampanha(obj.getCampanha());
				obj.getCompromissoAgendaPessoaHorario().setEtapaWorkflowVO(obj.getEtapaWorkflow());
				obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().setCodigo(dadosSQL.getInt("aph_codigo"));
				obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().setCodigo(dadosSQL.getInt("ap_codigo"));
				obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(dadosSQL.getInt("ap_pessoa"));
				obj.getCompromissoAgendaPessoaHorario().setDuvida(dadosSQL.getString("cap_duvida"));
				if (dadosSQL.getString("cap_tipoCompromisso") != null) {
					obj.getCompromissoAgendaPessoaHorario().setTipoCompromisso(TipoCompromissoEnum.valueOf(dadosSQL.getString("cap_tipoCompromisso")));
				}
				obj.setListaFilhosResponsavelFinanceiroVOs(getFacadeFactory().getPessoaFacade().consultarFilhosPorResponsavelFinanceiro(obj.getProspect().getPessoa().getCodigo(), usuario));
			}
			obj.setNovoObj(false);
			// Dados cursos Interesse
			getFacadeFactory().getProspectsFacade().montarCursoInteresseVO(dadosSQL, obj.getProspect());
			// Dados cursos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarCursosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
			// Dados arquivos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarArquivosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
		}
		return obj;
	}

	public static List<InteracaoWorkflowVO> montarDadosInteracaoWorkflowAnteriores(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario, Boolean novaInteracao) throws Exception {
		List<InteracaoWorkflowVO> listaObjs = new ArrayList<InteracaoWorkflowVO>(0);
		while (dadosSQL.next()) {
			InteracaoWorkflowVO obj = new InteracaoWorkflowVO();

			// Dados Etapa
			obj.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewf_codigo"));
			obj.getEtapaWorkflow().setNome(dadosSQL.getString("ewf_nome"));
			obj.getEtapaWorkflow().setTempoMinimo(dadosSQL.getLong("ewf_tempominimo"));
			obj.getEtapaWorkflow().setTempoMaximo(dadosSQL.getLong("ewf_tempomaximo"));
			obj.getEtapaWorkflow().setScript(dadosSQL.getString("ewf_script"));
			obj.getEtapaWorkflow().setMotivo(dadosSQL.getString("ewf_motivo"));
			obj.getEtapaWorkflow().setNivelApresentacao(dadosSQL.getInt("ewf_nivelApresentacao"));
			obj.getEtapaWorkflow().setCor(dadosSQL.getString("ewf_cor"));
			obj.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(dadosSQL.getBoolean("ewf_permitirfinalizardessaetapa"));
			obj.getEtapaWorkflow().setObrigatorioInformarObservacao(dadosSQL.getBoolean("ewf_obrigatorioInformarObservacao"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(dadosSQL.getInt("situacaoprospectworkflow_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(dadosSQL.getDouble("situacaoprospectworkflow_efetivacaoVendaHistorica"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(dadosSQL.getInt("situacaoprospectpipeline_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(dadosSQL.getString("situacaoprospectpipeline_nome"));
			if (dadosSQL.getString("situacaoprospectpipeline_controle") != null && !dadosSQL.getString("situacaoprospectpipeline_controle").isEmpty()) {
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("situacaoprospectpipeline_controle")));
			}

			if (!novaInteracao) {
				obj.getMotivoInsucesso().setCodigo(dadosSQL.getInt("motivoinsucesso_codigo"));
				obj.getTipoMidia().setCodigo(dadosSQL.getInt("interacaoWorkflow_tipoMidia"));
				obj.setCodigo(dadosSQL.getInt("interacaoWorkflow_codigo"));
				if (dadosSQL.getString("interacaoWorkflow_tipoInteracao") != null) {
					obj.setTipoInteracao(TipoInteracaoEnum.valueOf(dadosSQL.getString("interacaoWorkflow_tipoInteracao")));
				}
				obj.setDataInicio(dadosSQL.getTimestamp("interacaoWorkflow_datainicio"));
				obj.setDataTermino(dadosSQL.getTimestamp("interacaoWorkflow_datatermino"));
				obj.setHoraInicio(dadosSQL.getString("interacaoWorkflow_horainicio"));
				obj.setHoraTermino(dadosSQL.getString("interacaoWorkflow_horatermino"));
				obj.setObservacao(dadosSQL.getString("interacaoWorkflow_observacao"));
				obj.setTempoDecorrido(dadosSQL.getString("interacaoWorkflow_tempodecorrido"));
			}

			// Dados campanha
			obj.getCampanha().setCodigo(dadosSQL.getInt("campanha_codigo"));
			obj.getCampanha().setDescricao(dadosSQL.getString("campanha_descricao"));
			// Dados unidade Ensino
			obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino_codigo"));
			obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino_nome"));
			// Dados curso
			obj.getCurso().setCodigo(dadosSQL.getInt("curso_codigo"));
			obj.getCurso().setNome(dadosSQL.getString("curso_nome"));
			// Dados turno
			obj.getTurno().setCodigo(dadosSQL.getInt("turno_codigo"));
			obj.getTurno().setNome(dadosSQL.getString("turno_nome"));
			// Dados Workflow
			obj.getWorkflow().setCodigo(dadosSQL.getInt("wf_codigo"));
			obj.getWorkflow().setNome(dadosSQL.getString("wf_nome"));
			obj.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(dadosSQL.getInt("wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao"));
			// Dados Prospect
			obj.getProspect().getArquivoFoto().setCodigo(dadosSQL.getInt("prospects_arquivofoto"));
			obj.getProspect().getArquivoFoto().setNome(dadosSQL.getString("prospects_NomeArquivo"));
			obj.getProspect().getArquivoFoto().setPastaBaseArquivo(dadosSQL.getString("prospects_PastaBase"));
			obj.getProspect().setCodigo(dadosSQL.getInt("prospects_codigo"));
			obj.getProspect().setNome(dadosSQL.getString("prospects_nome"));
			obj.getProspect().setDataNascimento(dadosSQL.getDate("prospects_dataNascimento"));
			obj.getProspect().setSexo(dadosSQL.getString("prospects_sexo"));
			obj.getProspect().setTelefoneResidencial(dadosSQL.getString("prospects_telefoneresidencial"));
			obj.getProspect().setTelefoneComercial(dadosSQL.getString("prospects_telefonecomercial"));
			obj.getProspect().setTelefoneRecado(dadosSQL.getString("prospects_telefonerecado"));
			obj.getProspect().setCelular(dadosSQL.getString("prospects_celular"));
			obj.getProspect().setEmailPrincipal(dadosSQL.getString("prospects_email"));
			obj.getProspect().setRazaoSocial(dadosSQL.getString("prospects_razaosocial"));
			obj.getProspect().setCpf(dadosSQL.getString("prospects_cpf"));
			obj.getProspect().setCnpj(dadosSQL.getString("prospects_cnpj"));
			obj.getProspect().setRg(dadosSQL.getString("prospects_rg"));
			obj.getProspect().setEstadoEmissor(dadosSQL.getString("prospects_estadoemissor"));
			obj.getProspect().setOrgaoEmissor(dadosSQL.getString("prospects_orgaoemissor"));
			obj.getProspect().setInscricaoEstadual(dadosSQL.getString("prospects_inscricaoEstadual"));
			obj.getProspect().setEndereco(dadosSQL.getString("prospects_endereco"));
			obj.getProspect().setEmailSecundario(dadosSQL.getString("prospects_emailSecundario"));
			obj.getProspect().setSkype(dadosSQL.getString("prospects_skype"));
			obj.getProspect().setSetor(dadosSQL.getString("prospects_setor"));
			obj.getProspect().setComplemento(dadosSQL.getString("prospects_complemento"));
			obj.getProspect().setCEP(dadosSQL.getString("prospects_cep"));
			obj.getProspect().getPessoa().setCodigo(dadosSQL.getInt("prospects_pessoa"));
			obj.getProspect().getFornecedor().setCodigo(dadosSQL.getInt("prospects_fornecedor"));
			obj.getProspect().getParceiro().setCodigo(dadosSQL.getInt("prospects_parceiro"));
			obj.getProspect().getUnidadeEnsino().setCodigo(dadosSQL.getInt("prospects_unidadeensino"));
			obj.getProspect().getUnidadeEnsinoProspect().setCodigo(dadosSQL.getInt("prospects_unidadeensinoprospect"));
			obj.getProspect().getCidade().setCodigo(dadosSQL.getInt("prospects_cidade"));
			obj.getProspect().getCidade().setNome(dadosSQL.getString("prospects_cidadenome"));
			if (dadosSQL.getString("prospects_renda") != null && !dadosSQL.getString("prospects_renda").isEmpty()) {
				obj.getProspect().setRenda(RendaProspectEnum.valueOf(dadosSQL.getString("prospects_renda")));
			}
			if (dadosSQL.getString("prospects_formacaoAcademica") != null && !dadosSQL.getString("prospects_formacaoAcademica").isEmpty()) {
				obj.getProspect().setFormacaoAcademica(FormacaoAcademicaProspectEnum.valueOf(dadosSQL.getString("prospects_formacaoAcademica")));
			}
			if (dadosSQL.getString("prospects_tipoProspect") != null && !dadosSQL.getString("prospects_tipoProspect").isEmpty()) {
				obj.getProspect().setTipoProspect(TipoProspectEnum.valueOf(dadosSQL.getString("prospects_tipoProspect")));
			}
			if (dadosSQL.getString("prospects_tipoEmpresa") != null && !dadosSQL.getString("prospects_tipoEmpresa").isEmpty()) {
				obj.getProspect().setTipoEmpresa(TipoEmpresaProspectEnum.valueOf(dadosSQL.getString("prospects_tipoEmpresa")));
			}
			obj.getCompromissoAgendaPessoaHorario().setCodigo(dadosSQL.getInt("cap_codigo"));
			obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().setCodigo(dadosSQL.getInt("aph_codigo"));
			obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().setCodigo(dadosSQL.getInt("ap_codigo"));
			obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(dadosSQL.getInt("ap_pessoa"));

			obj.setNovoObj(false);
			// Dados cursos Interesse
			getFacadeFactory().getProspectsFacade().montarCursoInteresseVO(dadosSQL, obj.getProspect());
			// Dados cursos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarCursosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
			// Dados arquivos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarArquivosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);

			listaObjs.add(obj);

		}
		return listaObjs;
	}

	public static List<InteracaoWorkflowVO> montarDadosInteracaoWorkflowAnteriores(InteracaoWorkflowVO interacaoWorkflow, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario, Boolean novaInteracao) throws Exception {
		List<InteracaoWorkflowVO> listaObjs = new ArrayList<InteracaoWorkflowVO>(0);
		while (dadosSQL.next()) {
			InteracaoWorkflowVO obj = new InteracaoWorkflowVO();

			// Dados Etapa
			obj.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewf_codigo"));
			obj.getEtapaWorkflow().setNome(dadosSQL.getString("ewf_nome"));
			obj.getEtapaWorkflow().setTempoMinimo(dadosSQL.getLong("ewf_tempominimo"));
			obj.getEtapaWorkflow().setTempoMaximo(dadosSQL.getLong("ewf_tempomaximo"));
			obj.getEtapaWorkflow().setScript(dadosSQL.getString("ewf_script"));
			obj.getEtapaWorkflow().setMotivo(dadosSQL.getString("ewf_motivo"));
			obj.getEtapaWorkflow().setNivelApresentacao(dadosSQL.getInt("ewf_nivelApresentacao"));
			obj.getEtapaWorkflow().setCor(dadosSQL.getString("ewf_cor"));
			obj.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(dadosSQL.getBoolean("ewf_permitirfinalizardessaetapa"));
			obj.getEtapaWorkflow().setObrigatorioInformarObservacao(dadosSQL.getBoolean("ewf_obrigatorioInformarObservacao"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(dadosSQL.getInt("situacaoprospectworkflow_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(dadosSQL.getDouble("situacaoprospectworkflow_efetivacaoVendaHistorica"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(dadosSQL.getInt("situacaoprospectpipeline_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(dadosSQL.getString("situacaoprospectpipeline_nome"));
			if (dadosSQL.getString("situacaoprospectpipeline_controle") != null && !dadosSQL.getString("situacaoprospectpipeline_controle").isEmpty()) {
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("situacaoprospectpipeline_controle")));
			}
			obj.getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(obj.getEtapaWorkflow().getCodigo(), false, nivelMontarDados, false, usuario));

			if (!novaInteracao) {
				obj.getMotivoInsucesso().setCodigo(dadosSQL.getInt("motivoinsucesso_codigo"));
				obj.getTipoMidia().setCodigo(dadosSQL.getInt("interacaoWorkflow_tipoMidia"));
				obj.setCodigo(dadosSQL.getInt("interacaoWorkflow_codigo"));
				if (dadosSQL.getString("interacaoWorkflow_tipoInteracao") != null) {
					obj.setTipoInteracao(TipoInteracaoEnum.valueOf(dadosSQL.getString("interacaoWorkflow_tipoInteracao")));
				}
				obj.setDataInicio(dadosSQL.getTimestamp("interacaoWorkflow_datainicio"));
				obj.setDataTermino(dadosSQL.getTimestamp("interacaoWorkflow_datatermino"));
				obj.setHoraInicio(dadosSQL.getString("interacaoWorkflow_horainicio"));
				obj.setHoraTermino(dadosSQL.getString("interacaoWorkflow_horatermino"));
				obj.setObservacao(dadosSQL.getString("interacaoWorkflow_observacao"));
				obj.setMotivo(dadosSQL.getString("interacaoWorkflow_motivo"));
				obj.setTempoDecorrido(dadosSQL.getString("interacaoWorkflow_tempodecorrido"));
				obj.setGravada(Boolean.TRUE);
			}

			// Dados campanha
			obj.setCampanha(interacaoWorkflow.getCampanha());
			// Dados unidade Ensino
			obj.setUnidadeEnsino(interacaoWorkflow.getUnidadeEnsino());
			// Dados curso
			obj.setCurso(interacaoWorkflow.getCurso());
			// Dados turno
			obj.setTurno(interacaoWorkflow.getTurno());
			// Dados Workflow
			obj.setWorkflow(interacaoWorkflow.getWorkflow());
			// Dados Prospect
			obj.setProspect(interacaoWorkflow.getProspect());
			// Dados Compromisso
			obj.setCompromissoAgendaPessoaHorario(interacaoWorkflow.getCompromissoAgendaPessoaHorario());
			// Dados cursos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarCursosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
			// Dados arquivos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarArquivosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
			obj.setNovoObj(false);
			listaObjs.add(obj);

		}
		return listaObjs;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InteracaoWorkflowVO realizarPreenchimentoInteracaoNovoProspect(Integer campanha, UsuarioVO usuario, UnidadeEnsinoVO unidadeEnsinoLogado) throws Exception {
		InteracaoWorkflowVO obj = new InteracaoWorkflowVO();
		CampanhaVO objCampanha = new CampanhaVO();

		// Dados Prospect
		obj.getProspect().setCodigo(0);
		obj.getProspect().setTipoProspect(TipoProspectEnum.FISICO);
		if (usuario.getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())) {
			objCampanha = getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(campanha, false,
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			obj.getProspect().getUnidadeEnsino().setCodigo(objCampanha.getUnidadeEnsino().getCodigo());
		} else {
			obj.getProspect().getUnidadeEnsino().setCodigo(unidadeEnsinoLogado.getCodigo());
		}

		// Dados unidade Ensino
		if (usuario.getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())) {
			obj.getUnidadeEnsino().setCodigo(objCampanha.getUnidadeEnsino().getCodigo());
			obj.getUnidadeEnsino().setNome(objCampanha.getUnidadeEnsino().getNome());
		} else {
			obj.getUnidadeEnsino().setCodigo(unidadeEnsinoLogado.getCodigo());
			obj.getUnidadeEnsino().setNome(unidadeEnsinoLogado.getNome());
		}
		objCampanha = null;

		SqlRowSet dadosSQL = consultarInteracaoWorkflowPorInclusaoRapidaProspectLigacaReceptiva(usuario, campanha);

		// Dados Etapa
		while (dadosSQL.next()) {
			obj.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewf_codigo"));
			obj.getEtapaWorkflow().setNome(dadosSQL.getString("ewf_nome"));
			obj.getEtapaWorkflow().setTempoMinimo(dadosSQL.getLong("ewf_tempominimo"));
			obj.getEtapaWorkflow().setTempoMaximo(dadosSQL.getLong("ewf_tempomaximo"));
			obj.getEtapaWorkflow().setNivelApresentacao(dadosSQL.getInt("ewf_nivelApresentacao"));
			obj.getEtapaWorkflow().setScript(dadosSQL.getString("ewf_script"));
			obj.getEtapaWorkflow().setMotivo(dadosSQL.getString("ewf_motivo"));
			obj.getEtapaWorkflow().setCor(dadosSQL.getString("ewf_cor"));
			obj.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(dadosSQL.getBoolean("ewf_permitirfinalizardessaetapa"));
			obj.getEtapaWorkflow().setObrigatorioInformarObservacao(dadosSQL.getBoolean("ewf_obrigatorioInformarObservacao"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(dadosSQL.getInt("situacaoprospectworkflow_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(dadosSQL.getDouble("situacaoprospectworkflow_efetivacaoVendaHistorica"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(dadosSQL.getInt("situacaoprospectpipeline_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(dadosSQL.getString("situacaoprospectpipeline_nome"));
			if (dadosSQL.getString("situacaoprospectpipeline_controle") != null && !dadosSQL.getString("situacaoprospectpipeline_controle").isEmpty()) {
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("situacaoprospectpipeline_controle")));
			}
			// Dados campanha
			obj.getCampanha().setCodigo(dadosSQL.getInt("campanha_codigo"));
			obj.getCampanha().setDescricao(dadosSQL.getString("campanha_descricao"));

			// Dados Workflow
			obj.getWorkflow().setCodigo(dadosSQL.getInt("wf_codigo"));
			obj.getWorkflow().setNome(dadosSQL.getString("wf_nome"));
			obj.getWorkflow().setTempoMedioGerarAgenda(dadosSQL.getInt("wf_tempomediogeraragenda"));
			obj.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(dadosSQL.getInt("wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao"));

			// Dados cursos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarCursosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
			// Dados arquivos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarArquivosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
		}

		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public InteracaoWorkflowVO realizarPreenchimentoInteracaoLigacaoAtivaSemAgenda(ProspectsVO prospect, Integer campanha, UsuarioVO usuario, UnidadeEnsinoVO unidadeEnsinoLogado) throws Exception {
		InteracaoWorkflowVO obj = new InteracaoWorkflowVO();

		// Dados Prospect
		obj.setProspect(prospect);
		// Dados unidade Ensino
		obj.getUnidadeEnsino().setCodigo(unidadeEnsinoLogado.getCodigo());
		obj.getUnidadeEnsino().setNome(unidadeEnsinoLogado.getNome());

		SqlRowSet dadosSQL = consultarInteracaoWorkflowPorInclusaoRapidaProspectLigacaReceptiva(usuario, campanha);

		// Dados Etapa
		while (dadosSQL.next()) {
			obj.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewf_codigo"));
			obj.getEtapaWorkflow().setNome(dadosSQL.getString("ewf_nome"));
			obj.getEtapaWorkflow().setTempoMinimo(dadosSQL.getLong("ewf_tempominimo"));
			obj.getEtapaWorkflow().setTempoMaximo(dadosSQL.getLong("ewf_tempomaximo"));
			obj.getEtapaWorkflow().setNivelApresentacao(dadosSQL.getInt("ewf_nivelApresentacao"));
			obj.getEtapaWorkflow().setScript(dadosSQL.getString("ewf_script"));
			obj.getEtapaWorkflow().setMotivo(dadosSQL.getString("ewf_motivo"));
			obj.getEtapaWorkflow().setCor(dadosSQL.getString("ewf_cor"));
			obj.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(dadosSQL.getBoolean("ewf_permitirfinalizardessaetapa"));
			obj.getEtapaWorkflow().setObrigatorioInformarObservacao(dadosSQL.getBoolean("ewf_obrigatorioInformarObservacao"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(dadosSQL.getInt("situacaoprospectworkflow_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(dadosSQL.getDouble("situacaoprospectworkflow_efetivacaoVendaHistorica"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(dadosSQL.getInt("situacaoprospectpipeline_codigo"));
			obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(dadosSQL.getString("situacaoprospectpipeline_nome"));
			if (dadosSQL.getString("situacaoprospectpipeline_controle") != null && !dadosSQL.getString("situacaoprospectpipeline_controle").isEmpty()) {
				obj.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("situacaoprospectpipeline_controle")));
			}
			// Dados campanha
			obj.getCampanha().setCodigo(dadosSQL.getInt("campanha_codigo"));
			obj.getCampanha().setDescricao(dadosSQL.getString("campanha_descricao"));

			// Dados Workflow
			obj.getWorkflow().setCodigo(dadosSQL.getInt("wf_codigo"));
			obj.getWorkflow().setNome(dadosSQL.getString("wf_nome"));
			obj.getWorkflow().setTempoMedioGerarAgenda(dadosSQL.getInt("wf_tempomediogeraragenda"));
			obj.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(dadosSQL.getInt("wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao"));

			// Dados cursos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarCursosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
			// Dados arquivos etapa workflow
			getFacadeFactory().getEtapaWorkflowFacade().montarArquivosEtapaWorkflowInteracaoWorkflow(dadosSQL, obj);
		}

		return obj;
	}

	public SqlRowSet consultarInteracaoWorkflowPorInclusaoRapidaProspectLigacaReceptiva(UsuarioVO usuario, Integer campanha) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT  ");
		sqlStr.append(" distinct etapaworkflow.codigo  as ewf_codigo,  ");
		sqlStr.append(" etapaworkflow.nome as ewf_nome, ");
		sqlStr.append(" etapaworkflow.tempominimo as ewf_tempominimo, ");
		sqlStr.append(" etapaworkflow.tempomaximo as ewf_tempomaximo, ");
		sqlStr.append(" etapaworkflow.script as ewf_script, ");
		sqlStr.append(" etapaworkflow.motivo as ewf_motivo, ");
		sqlStr.append(" etapaworkflow.cor as ewf_cor, ");
		sqlStr.append(" etapaworkflow.nivelApresentacao AS ewf_nivelApresentacao,  ");
		sqlStr.append(" etapaworkflow.permitirfinalizardessaetapa as ewf_permitirfinalizardessaetapa, ");
		sqlStr.append(" etapaworkflow.obrigatorioInformarObservacao as ewf_obrigatorioInformarObservacao, ");
		sqlStr.append(" aewf.codigo AS aewf_codigo, ");
		sqlStr.append(" arquivoEWF.codigo AS arquivoEWF_codigo,  ");
		sqlStr.append(" arquivoEWF.nome AS arquivoEWF_nome,  ");
		sqlStr.append(" arquivoEWF.descricao AS arquivoEWF_descricao,  ");
		sqlStr.append(" cursoEWF.codigo AS cursoEWF_codigo, ");
		sqlStr.append(" cewf.codigo AS cewf_codigo,  ");
		sqlStr.append(" cewf.script AS cewf_script,  ");
		sqlStr.append(" cursoEWF.nome AS cursoEWF_nome, ");
		sqlStr.append(" spw.codigo as situacaoprospectworkflow_codigo, ");
		sqlStr.append(" spw.efetivacaoVendaHistorica as situacaoprospectworkflow_efetivacaoVendaHistorica, ");
		sqlStr.append(" spp.codigo as situacaoprospectpipeline_codigo, ");
		sqlStr.append(" spp.nome as situacaoprospectpipeline_nome, ");
		sqlStr.append(" spp.controle as situacaoprospectpipeline_controle, ");
		sqlStr.append(" workflow.codigo as wf_codigo, ");
		sqlStr.append(" workflow.tempomediogeraragenda as wf_tempomediogeraragenda, ");
		sqlStr.append(" workflow.nome as wf_nome, ");
		sqlStr.append(" workflow.numeroSegundosAlertarUsuarioTempoMaximoInteracao as wf_numeroSegundosAlertarUsuarioTempoMaximoInteracao, ");
		sqlStr.append(" campanha.codigo as campanha_codigo, ");
		sqlStr.append(" campanha.descricao as campanha_descricao ");
		sqlStr.append(" from campanha   ");
		sqlStr.append(" inner join campanhacolaborador on campanhacolaborador.campanha = campanha.codigo ");
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo ");
		sqlStr.append(" inner join  funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sqlStr.append(" inner join workflow on workflow.codigo = campanha.workflow ");
		sqlStr.append(" inner join etapaworkflow on workflow.codigo = etapaworkflow.workflow ");
		sqlStr.append(" left join arquivoEtapaWorkflow AS aewf ON  aewf.etapaworkflow = etapaworkflow.codigo ");
		sqlStr.append(" left join arquivo AS arquivoEWF ON arquivoEWF.codigo = aewf.arquivo ");
		sqlStr.append(" left join cursoEtapaWorkFlow AS cewf ON cewf.etapaworkflow = etapaworkflow.codigo ");
		sqlStr.append(" left join curso AS cursoEWF ON cursoEWF.codigo = cewf.curso ");
		sqlStr.append(" inner join situacaoprospectworkflow as spw on spw.codigo = etapaworkflow.situacaodefinirprospectfinal ");
		sqlStr.append(" inner join situacaoprospectpipeline as spp on spp.codigo = spw.situacaoprospectpipeline and spp.controle = '").append(SituacaoProspectPipelineControleEnum.INICIAL).append("'  ");
		sqlStr.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		if (campanha.intValue() != 0) {
			sqlStr.append(" and campanha.codigo = ").append(campanha);
		}
		sqlStr.append(" order by etapaworkflow.nivelapresentacao ");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	public void preencherInteracaoWorkflowSemReferenciaMemoriaBasico(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception {

		// Dados Prospect
		objDestino.getProspect().setCodigo(0);
		objDestino.getProspect().setTipoProspect(TipoProspectEnum.FISICO);
		objDestino.getProspect().getUnidadeEnsino().setCodigo(objOrigem.getProspect().getUnidadeEnsino().getCodigo());
		// Dados unidade Ensino
		objDestino.getUnidadeEnsino().setCodigo(objOrigem.getProspect().getUnidadeEnsino().getCodigo());
		objDestino.getUnidadeEnsino().setNome(objOrigem.getProspect().getUnidadeEnsino().getNome());

		// Dados Etapa

		objDestino.getEtapaWorkflow().setCodigo(objOrigem.getEtapaWorkflow().getCodigo());
		objDestino.getEtapaWorkflow().setNome(objOrigem.getEtapaWorkflow().getNome());
		objDestino.getEtapaWorkflow().setTempoMinimo(objOrigem.getEtapaWorkflow().getTempoMinimo());
		objDestino.getEtapaWorkflow().setTempoMaximo(objOrigem.getEtapaWorkflow().getTempoMaximo());
		objDestino.getEtapaWorkflow().setNivelApresentacao(objOrigem.getEtapaWorkflow().getNivelApresentacao());
		objDestino.getEtapaWorkflow().setScript(objOrigem.getEtapaWorkflow().getScript());
		objDestino.getEtapaWorkflow().setMotivo(objOrigem.getEtapaWorkflow().getMotivo());
		objDestino.getEtapaWorkflow().setCor(objOrigem.getEtapaWorkflow().getCor());
		objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().clear();
		for (EtapaWorkflowAntecedenteVO etapaWorkflowAntecedente : objOrigem.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs()) {
			EtapaWorkflowAntecedenteVO obj = new EtapaWorkflowAntecedenteVO();
			getFacadeFactory().getEtapaWorkflowAntecedenteFacade().preencherEtapaWorkflowAntecedenteSemReferenciaMemoria(obj, etapaWorkflowAntecedente);
			objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().add(obj);
		}
		objDestino.getEtapaWorkflow().setArquivoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getArquivoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setCursoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getCursoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(objOrigem.getEtapaWorkflow().getPermitirFinalizarDessaEtapa());
		objDestino.getEtapaWorkflow().setObrigatorioInformarObservacao(objOrigem.getEtapaWorkflow().getObrigatorioInformarObservacao());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getEfetivacaoVendaHistorica());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getNome());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle());

		// Dados campanha
		objDestino.getCampanha().setCodigo(objOrigem.getCampanha().getCodigo());
		objDestino.getCampanha().setDescricao(objOrigem.getCampanha().getDescricao());

		// Dados Workflow
		objDestino.getWorkflow().setCodigo(objOrigem.getWorkflow().getCodigo());
		objDestino.getWorkflow().setNome(objOrigem.getWorkflow().getNome());
		objDestino.getWorkflow().setTempoMedioGerarAgenda(objOrigem.getWorkflow().getTempoMedioGerarAgenda());
		objDestino.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(objOrigem.getWorkflow().getNumeroSegundosAlertarUsuarioTempoMaximoInteracao());

	}

	public void preencherInteracaoWorkflowSemReferenciaMemoriaBasicoLigacaoAtivaSemAgenda(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception {

		// Dados unidade Ensino
		objDestino.getUnidadeEnsino().setCodigo(objOrigem.getProspect().getUnidadeEnsino().getCodigo());
		objDestino.getUnidadeEnsino().setNome(objOrigem.getProspect().getUnidadeEnsino().getNome());

		// Dados Etapa

		objDestino.getEtapaWorkflow().setCodigo(objOrigem.getEtapaWorkflow().getCodigo());
		objDestino.getEtapaWorkflow().setNome(objOrigem.getEtapaWorkflow().getNome());
		objDestino.getEtapaWorkflow().setTempoMinimo(objOrigem.getEtapaWorkflow().getTempoMinimo());
		objDestino.getEtapaWorkflow().setTempoMaximo(objOrigem.getEtapaWorkflow().getTempoMaximo());
		objDestino.getEtapaWorkflow().setNivelApresentacao(objOrigem.getEtapaWorkflow().getNivelApresentacao());
		objDestino.getEtapaWorkflow().setScript(objOrigem.getEtapaWorkflow().getScript());
		objDestino.getEtapaWorkflow().setMotivo(objOrigem.getEtapaWorkflow().getMotivo());
		objDestino.getEtapaWorkflow().setCor(objOrigem.getEtapaWorkflow().getCor());
		objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().clear();
		for (EtapaWorkflowAntecedenteVO etapaWorkflowAntecedente : objOrigem.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs()) {
			EtapaWorkflowAntecedenteVO obj = new EtapaWorkflowAntecedenteVO();
			getFacadeFactory().getEtapaWorkflowAntecedenteFacade().preencherEtapaWorkflowAntecedenteSemReferenciaMemoria(obj, etapaWorkflowAntecedente);
			objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().add(obj);
		}
		objDestino.getEtapaWorkflow().setArquivoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getArquivoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setCursoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getCursoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(objOrigem.getEtapaWorkflow().getPermitirFinalizarDessaEtapa());
		objDestino.getEtapaWorkflow().setObrigatorioInformarObservacao(objOrigem.getEtapaWorkflow().getObrigatorioInformarObservacao());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getEfetivacaoVendaHistorica());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getNome());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle());

		// Dados campanha
		objDestino.getCampanha().setCodigo(objOrigem.getCampanha().getCodigo());
		objDestino.getCampanha().setDescricao(objOrigem.getCampanha().getDescricao());

		// Dados Workflow
		objDestino.getWorkflow().setCodigo(objOrigem.getWorkflow().getCodigo());
		objDestino.getWorkflow().setNome(objOrigem.getWorkflow().getNome());
		objDestino.getWorkflow().setTempoMedioGerarAgenda(objOrigem.getWorkflow().getTempoMedioGerarAgenda());
		objDestino.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(objOrigem.getWorkflow().getNumeroSegundosAlertarUsuarioTempoMaximoInteracao());

	}

	public List<InteracaoWorkflowVO> preencherListaTodasEtapasWorkflow(List<EtapaWorkflowVO> listaEtapas, UsuarioVO usuario) throws Exception {
		List<InteracaoWorkflowVO> listaInteracoes = new ArrayList<InteracaoWorkflowVO>(0);
		for (EtapaWorkflowVO etapaWorkflowVO : listaEtapas) {
			InteracaoWorkflowVO interacaoWorkflowVO = new InteracaoWorkflowVO();
			etapaWorkflowVO.setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(etapaWorkflowVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, Boolean.FALSE, usuario));
			interacaoWorkflowVO.setEtapaWorkflow(etapaWorkflowVO);
			interacaoWorkflowVO.setNovoObj(Boolean.FALSE);
			listaInteracoes.add(interacaoWorkflowVO);
		}
		return listaInteracoes;
	}

	public void preencherInteracaoWorkflowSemReferenciaMemoriaCompleto(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception {
		// Dados unidade Ensino
		objDestino.getUnidadeEnsino().setCodigo(objOrigem.getProspect().getUnidadeEnsino().getCodigo());
		objDestino.getUnidadeEnsino().setNome(objOrigem.getProspect().getUnidadeEnsino().getNome());

		// Dados Etapa

		objDestino.getEtapaWorkflow().setCodigo(objOrigem.getEtapaWorkflow().getCodigo());
		objDestino.getEtapaWorkflow().setNome(objOrigem.getEtapaWorkflow().getNome());
		objDestino.getEtapaWorkflow().setTempoMinimo(objOrigem.getEtapaWorkflow().getTempoMinimo());
		objDestino.getEtapaWorkflow().setTempoMaximo(objOrigem.getEtapaWorkflow().getTempoMaximo());
		objDestino.getEtapaWorkflow().setNivelApresentacao(objOrigem.getEtapaWorkflow().getNivelApresentacao());
		objDestino.getEtapaWorkflow().setScript(objOrigem.getEtapaWorkflow().getScript());
		objDestino.getEtapaWorkflow().setMotivo(objOrigem.getEtapaWorkflow().getMotivo());
		objDestino.getEtapaWorkflow().setCor(objOrigem.getEtapaWorkflow().getCor());
		objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().clear();
		for (EtapaWorkflowAntecedenteVO etapaWorkflowAntecedente : objOrigem.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs()) {
			EtapaWorkflowAntecedenteVO obj = new EtapaWorkflowAntecedenteVO();
			getFacadeFactory().getEtapaWorkflowAntecedenteFacade().preencherEtapaWorkflowAntecedenteSemReferenciaMemoria(obj, etapaWorkflowAntecedente);
			objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().add(obj);
		}
		objDestino.getEtapaWorkflow().setArquivoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getArquivoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setCursoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getCursoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(objOrigem.getEtapaWorkflow().getPermitirFinalizarDessaEtapa());
		objDestino.getEtapaWorkflow().setObrigatorioInformarObservacao(objOrigem.getEtapaWorkflow().getObrigatorioInformarObservacao());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getEfetivacaoVendaHistorica());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getNome());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle());

		// Dados campanha
		objDestino.getCampanha().setCodigo(objOrigem.getCampanha().getCodigo());
		objDestino.getCampanha().setDescricao(objOrigem.getCampanha().getDescricao());

		// Dados Workflow
		objDestino.getWorkflow().setCodigo(objOrigem.getWorkflow().getCodigo());
		objDestino.getWorkflow().setNome(objOrigem.getWorkflow().getNome());
		objDestino.getWorkflow().setTempoMedioGerarAgenda(objOrigem.getWorkflow().getTempoMedioGerarAgenda());
		objDestino.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(objOrigem.getWorkflow().getNumeroSegundosAlertarUsuarioTempoMaximoInteracao());

		// Dados Compromisso
		objDestino.setCompromissoAgendaPessoaHorario(objOrigem.getCompromissoAgendaPessoaHorario());

		// Dados Responsável
		objDestino.getResponsavel().setCodigo(objOrigem.getResponsavel().getCodigo());

		// Dados Prospect
		objDestino.setProspect(objOrigem.getProspect());

		// Dados Curso
		objDestino.setCurso(objOrigem.getCurso());

		// Dados turno
		objDestino.setTurno(objOrigem.getTurno());

		// Dados Interacao
		objDestino.setCodigo(objOrigem.getCodigo());
		objDestino.setDataInicio(objOrigem.getDataInicio());
		objDestino.setDataTermino(objOrigem.getDataTermino());
		objDestino.setHoraTermino(objOrigem.getHoraTermino());
		objDestino.setHoraInicio(objOrigem.getHoraInicio());
		objDestino.setObservacao(objOrigem.getObservacao());
		objDestino.setMotivo(objOrigem.getMotivo());
		objDestino.setTempoDecorrido(objOrigem.getTempoDecorrido());
		objDestino.getMotivoInsucesso().setCodigo(objOrigem.getMotivoInsucesso().getCodigo());

	}

	public void preencherInteracaoWorkflowSemReferenciaMemoriaSemEtapaSemInteracao(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception {
		// Dados unidade Ensino
		objDestino.getUnidadeEnsino().setCodigo(objOrigem.getProspect().getUnidadeEnsino().getCodigo());
		objDestino.getUnidadeEnsino().setNome(objOrigem.getProspect().getUnidadeEnsino().getNome());

		// Dados campanha
		objDestino.getCampanha().setCodigo(objOrigem.getCampanha().getCodigo());
		objDestino.getCampanha().setDescricao(objOrigem.getCampanha().getDescricao());

		// Dados Workflow
		objDestino.getWorkflow().setCodigo(objOrigem.getWorkflow().getCodigo());
		objDestino.getWorkflow().setNome(objOrigem.getWorkflow().getNome());
		objDestino.getWorkflow().setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(objOrigem.getWorkflow().getNumeroSegundosAlertarUsuarioTempoMaximoInteracao());

		// Dados Compromisso
		objDestino.setCompromissoAgendaPessoaHorario(objOrigem.getCompromissoAgendaPessoaHorario());

		// Dados Responsável
		objDestino.getResponsavel().setCodigo(objOrigem.getResponsavel().getCodigo());

		// Dados Prospect
		objDestino.setProspect(objOrigem.getProspect());

		// Dados Curso
		objDestino.setCurso(objOrigem.getCurso());

		// Dados Turno
		objDestino.setTurno(objOrigem.getTurno());

	}

	public void preencherInteracaoWorkflowSemReferenciaMemoriaDadosExclusivosInteracaoEtapa(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem, UsuarioVO usuario) throws Exception {

		// Dados Etapa
		objDestino.getEtapaWorkflow().setCodigo(objOrigem.getEtapaWorkflow().getCodigo());
		objDestino.getEtapaWorkflow().setNome(objOrigem.getEtapaWorkflow().getNome());
		objDestino.getEtapaWorkflow().setTempoMinimo(objOrigem.getEtapaWorkflow().getTempoMinimo());
		objDestino.getEtapaWorkflow().setTempoMaximo(objOrigem.getEtapaWorkflow().getTempoMaximo());
		objDestino.getEtapaWorkflow().setNivelApresentacao(objOrigem.getEtapaWorkflow().getNivelApresentacao());
		objDestino.getEtapaWorkflow().setScript(objOrigem.getEtapaWorkflow().getScript());
		objDestino.getEtapaWorkflow().setMotivo(objOrigem.getEtapaWorkflow().getMotivo());
		objDestino.getEtapaWorkflow().setCor(objOrigem.getEtapaWorkflow().getCor());
		objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().clear();
		for (EtapaWorkflowAntecedenteVO etapaWorkflowAntecedente : objOrigem.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs()) {
			EtapaWorkflowAntecedenteVO obj = new EtapaWorkflowAntecedenteVO();
			getFacadeFactory().getEtapaWorkflowAntecedenteFacade().preencherEtapaWorkflowAntecedenteSemReferenciaMemoria(obj, etapaWorkflowAntecedente);
			objDestino.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().add(obj);
		}
		objDestino.getEtapaWorkflow().setArquivoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getArquivoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setCursoEtapaWorkflowVOs(objOrigem.getEtapaWorkflow().getCursoEtapaWorkflowVOs());
		objDestino.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(objOrigem.getEtapaWorkflow().getPermitirFinalizarDessaEtapa());
		objDestino.getEtapaWorkflow().setObrigatorioInformarObservacao(objOrigem.getEtapaWorkflow().getObrigatorioInformarObservacao());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getEfetivacaoVendaHistorica());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getCodigo());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getNome());
		objDestino.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(objOrigem.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle());

		// Dados Interacao
		objDestino.setCodigo(objOrigem.getCodigo());
		objDestino.setDataInicio(objOrigem.getDataInicio());
		objDestino.setDataTermino(objOrigem.getDataTermino());
		objDestino.setHoraInicio(objOrigem.getHoraInicio());
		objDestino.setObservacao(objOrigem.getObservacao());
		objDestino.setMotivo(objOrigem.getMotivo());
		objDestino.setTempoDecorrido(objOrigem.getTempoDecorrido());
		objDestino.getMotivoInsucesso().setCodigo(objOrigem.getMotivoInsucesso().getCodigo());

	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(InteracaoWorkflowVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		obj.setObservacao(obj.getObservacao().toUpperCase());
		obj.setHoraInicio(obj.getHoraInicio().toUpperCase());
		obj.setHoraTermino(obj.getHoraTermino().toUpperCase());
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis na Tela
	 * InteracaoWorkflowCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public List<InteracaoWorkflowVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.CODIGO.toString())) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.NOME_PROSPECTS.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorNomeProspects(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.DESCRICAO_CAMPANHA.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorDescricaoCampanha(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.NOME_UNIDADE_ENSINO.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorNomeUnidadeEnsino(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.NOME_CURSO.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorNomeCurso(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.NOME_WORKFLOW.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorNomeWorkflow(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.DESCRICAO_COMPROMISSO_AGENDA_PESSOA_HORARIO.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorDescricaoCompromissoAgendaPessoaHorario(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.NOME_SITUACAO_PROSPECT_PIPELINE.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorNomeSituacaoProspectPipeline(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.ETAPA_WORKFLOW.toString())) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorEtapaWorkflow(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboInteracaoWorkflowEnum.MATRICULA_FUNCIONARIO.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getInteracaoWorkflowFacade().consultarPorMatriculaFuncionario(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return new ArrayList(0);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>matricula</code> da classe
	 * <code>Funcionario</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorMatriculaFuncionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, Funcionario WHERE InteracaoWorkflow.responsavel = Funcionario.codigo and upper( Funcionario.matricula ) like(?) ORDER BY Funcionario.matricula";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>Integer etapaWorkflow</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorEtapaWorkflow(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		String sqlStr = "SELECT * FROM InteracaoWorkflow WHERE etapaWorkflow >= ?  ORDER BY etapaWorkflow";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>SituacaoProspectPipeline</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeSituacaoProspectPipeline(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, SituacaoProspectPipeline WHERE InteracaoWorkflow.situacaoProspecFinalEtapa = SituacaoProspectPipeline.codigo and upper( SituacaoProspectPipeline.nome ) like(?) ORDER BY SituacaoProspectPipeline.nome";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>descricao</code> da classe
	 * <code>CompromissoAgendaPessoaHorario</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoCompromissoAgendaPessoaHorario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, CompromissoAgendaPessoaHorario WHERE InteracaoWorkflow.compromissoAgendaPessoaHorario = CompromissoAgendaPessoaHorario.codigo and upper( CompromissoAgendaPessoaHorario.descricao ) like(?) ORDER BY CompromissoAgendaPessoaHorario.descricao";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Workflow</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeWorkflow(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, Workflow WHERE InteracaoWorkflow.workflow = Workflow.codigo and upper( Workflow.nome ) like(?) ORDER BY Workflow.nome";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Curso</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, Curso WHERE InteracaoWorkflow.curso = Curso.codigo and upper( Curso.nome ) like(?) ORDER BY Curso.nome";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>UnidadeEnsino</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, UnidadeEnsino WHERE InteracaoWorkflow.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like(?) ORDER BY UnidadeEnsino.nome";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>descricao</code> da classe
	 * <code>Campanha</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoCampanha(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, Campanha WHERE InteracaoWorkflow.campanha = Campanha.codigo and upper( Campanha.descricao ) like(?) ORDER BY Campanha.descricao";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Prospects</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		valorConsulta += "%";
		String sqlStr = "SELECT InteracaoWorkflow.* FROM InteracaoWorkflow, Prospects WHERE InteracaoWorkflow.prospect = Prospects.codigo and upper( Prospects.nome ) like(?) ORDER BY Prospects.nome";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>InteracaoWorkflow</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		String sqlStr = "SELECT * FROM InteracaoWorkflow WHERE codigo >= ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>InteracaoWorkflowVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados ( <code>ResultSet</code>) em um objeto da classe
	 * <code>InteracaoWorkflowVO</code>.
	 * 
	 * @return O objeto da classe <code>InteracaoWorkflowVO</code> com os dados
	 *         devidamente montados.
	 */
	public static InteracaoWorkflowVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		InteracaoWorkflowVO obj = new InteracaoWorkflowVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getProspect().setCodigo(new Integer(dadosSQL.getInt("prospect")));
		obj.getCampanha().setCodigo(new Integer(dadosSQL.getInt("campanha")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
		obj.getWorkflow().setCodigo(new Integer(dadosSQL.getInt("workflow")));
		obj.getCompromissoAgendaPessoaHorario().setCodigo(new Integer(dadosSQL.getInt("compromissoAgendaPessoaHorario")));
		obj.getMotivoInsucesso().setCodigo(dadosSQL.getInt("motivoInsucesso"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setMotivo(dadosSQL.getString("motivo"));
		obj.setTipoInteracao(TipoInteracaoEnum.valueOf(dadosSQL.getString("tipoInteracao")));
		obj.getEtapaWorkflow().setCodigo(new Integer(dadosSQL.getInt("etapaWorkflow")));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.setHoraInicio(dadosSQL.getString("horaInicio"));
		obj.setHoraTermino(dadosSQL.getString("horaTermino"));
		obj.setDataInicio(dadosSQL.getDate("dataInicio"));
		obj.setDataTermino(dadosSQL.getDate("dataTermino"));
		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			getFacadeFactory().getProspectsFacade().carregarDados(obj.getProspect(), usuario);
		} else {
			montarDadosProspect(obj, nivelMontarDados, usuario);
		}
		montarDadosCampanha(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosWorkflow(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCompromissoAgendaPessoaHorario(obj, nivelMontarDados, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>FuncionarioVO</code> relacionado ao objeto
	 * <code>InteracaoWorkflowVO</code>. Faz uso da chave primária da classe
	 * <code>FuncionarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(InteracaoWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(new Usuario().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code> relacionado ao objeto
	 * <code>InteracaoWorkflowVO</code>. Faz uso da chave primária da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCompromissoAgendaPessoaHorario(InteracaoWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue() == 0) {
			obj.setCompromissoAgendaPessoaHorario(new CompromissoAgendaPessoaHorarioVO());
			return;
		}
		obj.setCompromissoAgendaPessoaHorario(new CompromissoAgendaPessoaHorario().consultarPorChavePrimaria(obj.getCompromissoAgendaPessoaHorario().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>WorkflowVO</code> relacionado ao objeto
	 * <code>InteracaoWorkflowVO</code>. Faz uso da chave primária da classe
	 * <code>WorkflowVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosWorkflow(InteracaoWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getWorkflow().getCodigo().intValue() == 0) {
			obj.setWorkflow(new WorkflowVO());
			return;
		}
		obj.setWorkflow(new Workflow().consultarPorChavePrimaria(obj.getWorkflow().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CursoVO</code> relacionado ao objeto
	 * <code>InteracaoWorkflowVO</code>. Faz uso da chave primária da classe
	 * <code>CursoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCurso(InteracaoWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		obj.setCurso(new Curso().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UnidadeEnsinoVO</code> relacionado ao objeto
	 * <code>InteracaoWorkflowVO</code>. Faz uso da chave primária da classe
	 * <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(InteracaoWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(new UnidadeEnsino().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CampanhaVO</code> relacionado ao objeto
	 * <code>InteracaoWorkflowVO</code>. Faz uso da chave primária da classe
	 * <code>CampanhaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCampanha(InteracaoWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCampanha().getCodigo().intValue() == 0) {
			obj.setCampanha(new CampanhaVO());
			return;
		}
		obj.setCampanha(new Campanha().consultarPorChavePrimaria(obj.getCampanha().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ProspectsVO</code> relacionado ao objeto
	 * <code>InteracaoWorkflowVO</code>. Faz uso da chave primária da classe
	 * <code>ProspectsVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosProspect(InteracaoWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProspect().getCodigo().intValue() == 0) {
			obj.setProspect(new ProspectsVO());
			return;
		}
		obj.setProspect(new Prospects().consultarPorChavePrimaria(obj.getProspect().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>InteracaoWorkflowVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public InteracaoWorkflowVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		String sql = "SELECT * FROM InteracaoWorkflow WHERE codigo = " + codigoPrm;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( InteracaoWorkflow ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<InteracaoWorkflowVO> consultarUnicidade(InteracaoWorkflowVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
		super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
		return new ArrayList(0);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return InteracaoWorkflow.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		InteracaoWorkflow.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirListaInteracaoWorkflow(List<InteracaoWorkflowVO> listaInteracaoWorkflowVO, Boolean validarDados, UsuarioVO usuarioLogado) throws Exception {
		if (!listaInteracaoWorkflowVO.isEmpty()) {
			for (int i = 0; i < listaInteracaoWorkflowVO.size(); i++) {
				InteracaoWorkflowVO interacaoWorkflowVO = (InteracaoWorkflowVO)listaInteracaoWorkflowVO.get(i);
				if (interacaoWorkflowVO.getCodigo().equals(0)) {
					if (validarDados) {
						incluir(interacaoWorkflowVO, usuarioLogado);
					} else {
						incluirSemValidarDados(interacaoWorkflowVO, usuarioLogado);
					}
				} else {
					if (validarDados) {
						alterar(interacaoWorkflowVO, usuarioLogado);
					} else {
						alterarSemValidarDados(interacaoWorkflowVO, usuarioLogado);
					}
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirListaInteracaoWorkflowEntidadeOrigem(TipoOrigemInteracaoEnum tipoOrigemInteracao, 
			String identificadorOrigem, Integer codigoEntidadeOrigem, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM InteracaoWorkflow WHERE (1 = 1) ");
			if (tipoOrigemInteracao != null) {
				sql.append("AND tipoOrigemInteracao = '").append(tipoOrigemInteracao.toString()).append("' ");
			}
			if ((identificadorOrigem != null) && (!identificadorOrigem.equals(""))) {
				sql.append("AND identificadorOrigem = '").append(identificadorOrigem.toString()).append("' ");
			}			
			if ((codigoEntidadeOrigem != null) && (codigoEntidadeOrigem.intValue() != 0)) {
				sql.append("AND codigoEntidadeOrigem = ").append(codigoEntidadeOrigem.toString()).append(" ");
			}
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}
	
	public InteracaoWorkflowVO consultarPorRenovacaoMatriculaPeloPortalAluno(String matricula, String semestreAno, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade());
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT i.* FROM InteracaoWorkflow i INNER JOIN usuario u on i.responsavel = u.codigo ");
		sql.append(" INNER JOIN pessoa p on u.pessoa = p.codigo INNER JOIN matricula m on p.codigo = m.aluno ");
		sql.append(" WHERE i.tipoorigeminteracao = '" + TipoOrigemInteracaoEnum.RENOVACAO_MATRICULA.toString()).append("' ");
		sql.append(" AND i.tipointeracao = '").append(TipoInteracaoEnum.PORTALALUNO.toString()).append("' ");
		sql.append(" AND i.observacao ilike ? AND m.matricula = ? ORDER BY datatermino DESC LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), "%" + semestreAno + "%", matricula);
		if (!tabelaResultado.next()) {
			return new InteracaoWorkflowVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
}
