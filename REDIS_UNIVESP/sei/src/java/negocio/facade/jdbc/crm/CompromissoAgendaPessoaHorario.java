/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TipoDistribuicaoProspectCampanhaPublicoAlvoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoCampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.ReagendamentoCompromissoVO;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoContatoEnum;
import negocio.comuns.crm.enumerador.TipoOrigemCadastroProspectEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboCompromissoAgendaPessoaHorarioEnum;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.crm.CompromissoAgendaPessoaHorarioInterfaceFacade;

/**
 * 
 * @author edigarjr
 */
@Repository
@Scope("singleton")
@Lazy
public class CompromissoAgendaPessoaHorario extends ControleAcesso implements CompromissoAgendaPessoaHorarioInterfaceFacade {

	protected static String idEntidade;

	public CompromissoAgendaPessoaHorario() throws Exception {
		super();
		setIdEntidade("CompromissoAgendaPessoaHorario");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Primeiramente valida os
	 * dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>
	 *            que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception {
		CompromissoAgendaPessoaHorarioVO.validarDados(obj);
		incluirSemValidarDados(obj, usuarioVO);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Primeiramente valida os
	 * dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>
	 *            que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemValidarDados(final CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// CompromissoAgendaPessoaHorario.incluir(getIdEntidade());
			realizarUpperCaseDados(obj);
			final String sql = "INSERT INTO CompromissoAgendaPessoaHorario( descricao, agendaPessoaHorario, hora, tipoCompromisso, prospect, campanha, observacao, origem, " + "urgente, dataCadastro, tipoContato, dataCompromisso, etapaworkflow, tipoSituacaoCompromissoEnum, horaFim, preInscricao, dataInicialCompromisso, historicoReagendamentoCompromisso, duvida ) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setInt(2, obj.getAgendaPessoaHorario().getCodigo().intValue());
					sqlInserir.setString(3, Uteis.getFormatoHoraMinuto(obj.getHora()));
					sqlInserir.setString(4, obj.getTipoCompromisso().toString());
					if (obj.getProspect().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getProspect().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getCampanha().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getCampanha().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, obj.getObservacao());
					sqlInserir.setString(8, obj.getOrigem());
					sqlInserir.setBoolean(9, obj.isUrgente().booleanValue());
					sqlInserir.setTimestamp(10, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlInserir.setString(11, obj.getTipoContato().toString());
                    sqlInserir.setTimestamp(12, Uteis.getDataJDBCTimestamp(obj.getDataCompromisso()));
					if (obj.getEtapaWorkflowVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getEtapaWorkflowVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setString(14, obj.getTipoSituacaoCompromissoEnum().toString());
					sqlInserir.setString(15, Uteis.getFormatoHoraMinuto(obj.getHoraFim()));
					if (obj.getPreInscricao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(16, obj.getPreInscricao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(16, 0);
					}
					// Registra a data de inicio do compromisso para que a mesma seja utilizada como
					// referencia para saber se um compromisso já foi reagendado ou não.
					sqlInserir.setTimestamp(17, Uteis.getDataJDBCTimestamp(obj.getDataInicialCompromisso()));
					sqlInserir.setString(18, obj.getHistoricoReagendamentoCompromisso());
					sqlInserir.setString(19, obj.getDuvida());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados ( <code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>
	 *            que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception {
		CompromissoAgendaPessoaHorarioVO.validarDados(obj);
		alterarSemValidarDados(obj, usuarioVO);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados ( <code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>
	 *            que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSemValidarDados(final CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// CompromissoAgendaPessoaHorario.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			final String sql = "UPDATE CompromissoAgendaPessoaHorario set descricao=?, agendaPessoaHorario=?, hora=?, tipoCompromisso=?, prospect=?, campanha=?, " + "observacao=?, origem=?, urgente=?, dataCompromisso=? , tipoContato=?, etapaworkflow=?, " + "tipoSituacaoCompromissoEnum=?, horaFim=?, preInscricao=?, historicoReagendamentoCompromisso=?, duvida=? WHERE ((codigo = ?)) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			if (!(Boolean) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setInt(2, obj.getAgendaPessoaHorario().getCodigo().intValue());
					sqlAlterar.setString(3, Uteis.getFormatoHoraMinuto(obj.getHora()));
					sqlAlterar.setString(4, obj.getTipoCompromisso().toString());
					if (obj.getProspect().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getProspect().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getCampanha().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getCampanha().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, obj.getObservacao());
					sqlAlterar.setString(8, obj.getOrigem());
					sqlAlterar.setBoolean(9, obj.isUrgente().booleanValue());
					sqlAlterar.setTimestamp(10, Uteis.getDataJDBCTimestamp(obj.getDataCompromisso()));
					sqlAlterar.setString(11, obj.getTipoContato().toString());
					if (obj.getEtapaWorkflowVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(12, obj.getEtapaWorkflowVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					sqlAlterar.setString(13, obj.getTipoSituacaoCompromissoEnum().toString());
					sqlAlterar.setString(14, Uteis.getFormatoHoraMinuto(obj.getHoraFim()));

					if (obj.getPreInscricao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(15, obj.getPreInscricao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(15, 0);
					}
					sqlAlterar.setString(16, obj.getHistoricoReagendamentoCompromisso());
					sqlAlterar.setString(17, obj.getDuvida());
					sqlAlterar.setInt(18, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					return arg0.next();
				}
			})) {
				incluirSemValidarDados(obj, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarCompromissoRealizado(final Integer codigoCompromisso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE CompromissoAgendaPessoaHorario set tipoSituacaoCompromissoEnum=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, TipoSituacaoCompromissoEnum.REALIZADO.toString());
					sqlAlterar.setInt(2, codigoCompromisso);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataCompromissoRealizado(final Integer codigoCompromisso, final Date dataCompromisso, final String horaCompromisso, final Integer agendapessoahorario, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE CompromissoAgendaPessoaHorario set dataCompromisso=?, hora=?, agendapessoahorario = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(dataCompromisso));
					sqlAlterar.setString(2, Uteis.getFormatoHoraMinuto(horaCompromisso));
					sqlAlterar.setInt(3, agendapessoahorario);
					sqlAlterar.setInt(4, codigoCompromisso);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alteratProspectCompromisso(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE CompromissoAgendaPessoaHorario set prospect=? WHERE (prospect = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarCompromissoRealizadoComEtapa(final Integer codigoCompromisso, final Integer codigoEtapa, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE CompromissoAgendaPessoaHorario set tipoSituacaoCompromissoEnum=?, etapaworkflow = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, TipoSituacaoCompromissoEnum.REALIZADO.toString());
					if (codigoEtapa != 0) {
						sqlAlterar.setInt(2, codigoEtapa);
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, codigoCompromisso);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAtualizacaoEtapaAtualCompromisso(final Integer codigoEtapa, final Integer codigoCompromisso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE CompromissoAgendaPessoaHorario set etapaworkflow = ?, tipoSituacaoCompromissoEnum = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, codigoEtapa);
					sqlAlterar.setString(2, TipoSituacaoCompromissoEnum.PARALIZADO.toString());
					sqlAlterar.setInt(3, codigoCompromisso);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAtualizacaoTipoSituacaoCompromissoEnum(final TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum, final Integer codigoCompromisso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE CompromissoAgendaPessoaHorario set tipoSituacaoCompromissoEnum = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, tipoSituacaoCompromissoEnum.toString());
					sqlAlterar.setInt(2, codigoCompromisso);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Sempre localiza o registro
	 * a ser excluído através da chave primária da entidade. Primeiramente
	 * verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>
	 *            que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// CompromissoAgendaPessoaHorario.excluir(getIdEntidade());
			// getFacadeFactory().getInteracaoWorkflowFacade().excluir();
			if(obj.getCodigo() > 0){
				String sql = "UPDATE interacaoworkflow set compromissoagendapessoahorario =  null where compromissoagendapessoahorario = "+obj.getCodigo()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				getConexao().getJdbcTemplate().update(sql);
			}
			String sql = "DELETE FROM CompromissoAgendaPessoaHorario WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCompromissoAgendaPessoaHorarioNaoInicializadoProspect(Integer prospect, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "";
			if(prospect != null && prospect > 0){
				sql = "UPDATE interacaoworkflow set compromissoagendapessoahorario =  null where compromissoagendapessoahorario in ( select codigo from compromissoagendaPessoaHorario where tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' and prospect = ? )"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				getConexao().getJdbcTemplate().update(sql, new Object[] { prospect });
			}			
			
			sql = " delete from compromissoagendaPessoaHorario where tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' and prospect = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { prospect });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCompromissoAgendaPessoaHorario(Integer prospect, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "";			
			sql = " delete from compromissoagendaPessoaHorario where prospect = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { prospect });
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param CompromissoAgendaPessoaHorarioVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCompromissoPorAgendaHorarioPessoa(CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getAgendaPessoaHorario().getCodigo().equals(0)) {
			AgendaPessoaHorarioVO agendaPessoaHorarioVO = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(obj.getAgendaPessoaHorario().getAgendaPessoa().getCodigo(), obj.getDataCompromisso(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario);
			if (agendaPessoaHorarioVO.getCodigo() == 0) {
				getFacadeFactory().getAgendaPessoaHorarioFacade().incluir(obj.getAgendaPessoaHorario(), usuario);
			} else {
				obj.setAgendaPessoaHorario(agendaPessoaHorarioVO);
			}
		}
		if (obj.getIsTipoCompromissoContato() && obj.getEtapaWorkflowVO().getCodigo() == 0 && obj.getCampanha().getCodigo() != 0) {
			obj.setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorCodigoCampanhaEtapaInicial(obj.getCampanha().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		// if (obj.getAgendaPessoaHorario().getCodigo().intValue() == 0) {
		// obj.setAgendaPessoaHorario(agendaPessoaHorarioVO);
		// }
		if (obj.getCodigo().equals(0)) {
			incluirSemValidarDados(obj, usuario);
		} else {
			alterarSemValidarDados(obj, usuario);
		}
	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param CompromissoAgendaPessoaHorarioVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
	}

	// /**
	// * Operação responsável por validar os dados de um objeto da classe
	// <code>CompromissoAgendaPessoaHorarioVO</code>.
	// * Todos os tipos de consistência de dados são e devem ser implementadas
	// neste método.
	// * São validações típicas: verificação de campos obrigatórios, verificação
	// de valores válidos para os atributos.
	// * @exception ConsistirExecption Se uma inconsistência for encontrada
	// aumaticamente é gerada uma exceção descrevendo
	// * o atributo e o erro ocorrido.
	// */
	// public void validarDados(CompromissoAgendaPessoaHorarioVO obj) throws
	// ConsistirException {
	// if (!obj.isValidarDados().booleanValue()) {
	// return;
	// }
	// ConsistirException consistir = new ConsistirException();
	// if (obj.getDescricao().equals("")) {
	// consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_descricao"));
	// }
	// if (obj.getAgendaPessoaHorario().getCodigo().intValue() == 0) {
	// consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_agendaPessoaHorario"));
	// }
	// if (obj.getHora().equals("")) {
	// consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_hora"));
	// }
	// if ((obj.getProspect() == null)
	// || (obj.getProspect().getCodigo().intValue() == 0)) {
	// consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_prospect"));
	// }
	// if ((obj.getCampanha() == null)
	// || (obj.getCampanha().getCodigo().intValue() == 0)) {
	// consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CompromissoAgendaPessoaHorario_campanha"));
	// }
	// if (consistir.existeErroListaMensagemErro()) {
	// throw consistir;
	// }
	//
	// }
	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>CompromissoAgendaPessoaHorarioVO</code>.
	 */
	public void validarUnicidade(List<CompromissoAgendaPessoaHorarioVO> lista, CompromissoAgendaPessoaHorarioVO obj) throws ConsistirException {
		for (CompromissoAgendaPessoaHorarioVO repetido : lista) {
		}
	}

	public void validarDiaMesAnoCompromisso(CompromissoAgendaPessoaHorarioVO obj) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT compromissoagendapessoahorario.codigo FROM compromissoagendapessoahorario ");
		sql.append("INNER JOIN agendapessoahorario ON agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
		sql.append("INNER JOIN agendapessoa ON agendapessoa.codigo = agendapessoahorario.agendapessoa ");
		sql.append("WHERE compromissoagendapessoahorario.hora = '").append(obj.getHora()).append("' ");
		sql.append(" AND agendapessoahorario.dia = ").append(obj.getAgendaPessoaHorario().getDia());
		sql.append(" AND agendapessoahorario.mes = ").append(obj.getAgendaPessoaHorario().getMes());
		sql.append(" AND agendapessoahorario.ano = ").append(obj.getAgendaPessoaHorario().getAno());
		sql.append(" AND agendapessoa.codigo = ").append(obj.getAgendaPessoaHorario().getAgendaPessoa().getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			throw new Exception("Já existe um compromisso com essa data,hora e resposável.");
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(CompromissoAgendaPessoaHorarioVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		obj.setDescricao(obj.getDescricao().toUpperCase());
		obj.setHora(obj.getHora().toUpperCase());
		obj.setObservacao(obj.getObservacao().toUpperCase());
		obj.setOrigem(obj.getOrigem().toUpperCase());
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis na Tela
	 * CompromissoAgendaPessoaHorarioCons.jsp. Define o tipo de consulta a ser
	 * executada, por meio de ComboBox denominado campoConsulta, disponivel
	 * neste mesmo JSP. Como resultado, disponibiliza um List com os objetos
	 * selecionados na sessao da pagina.
	 */
	public List<CompromissoAgendaPessoaHorarioVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		if (campoConsulta.equals(TipoConsultaComboCompromissoAgendaPessoaHorarioEnum.CODIGO.toString())) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		if (campoConsulta.equals(TipoConsultaComboCompromissoAgendaPessoaHorarioEnum.DESCRICAO.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarPorDescricao(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		if (campoConsulta.equals(TipoConsultaComboCompromissoAgendaPessoaHorarioEnum.AGENDA_PESSOA_HORARIO.toString())) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarPorAgendaPessoaHorario(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		if (campoConsulta.equals(TipoConsultaComboCompromissoAgendaPessoaHorarioEnum.HORA.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarPorHora(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		if (campoConsulta.equals(TipoConsultaComboCompromissoAgendaPessoaHorarioEnum.NOME_PROSPECTS.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarPorNomeProspects(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		if (campoConsulta.equals(TipoConsultaComboCompromissoAgendaPessoaHorarioEnum.DESCRICAO_CAMPANHA.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarPorDescricaoCampanha(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		return new ArrayList(0);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CompromissoAgendaPessoaHorario</code> através do valor do atributo
	 * <code>descricao</code> da classe <code>Campanha</code> Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoAgendaPessoaHorarioVO</code> resultantes da
	 *         consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoCampanha(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		valorConsulta += "%";
		String sqlStr = "SELECT CompromissoAgendaPessoaHorario.* FROM CompromissoAgendaPessoaHorario, Campanha WHERE CompromissoAgendaPessoaHorario.campanha = Campanha.codigo and upper( Campanha.descricao ) like(?) ORDER BY Campanha.descricao";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CompromissoAgendaPessoaHorario</code> através do valor do atributo
	 * <code>nome</code> da classe <code>Prospects</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoAgendaPessoaHorarioVO</code> resultantes da
	 *         consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		valorConsulta += "%";
		String sqlStr = "SELECT CompromissoAgendaPessoaHorario.* FROM CompromissoAgendaPessoaHorario, Prospects WHERE CompromissoAgendaPessoaHorario.prospect = Prospects.codigo and upper( Prospects.nome ) like(?) ORDER BY Prospects.nome";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado);
	}

	public List consultarCompromissoFuturoProspect(Integer codigoProspect, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT CompromissoAgendaPessoaHorario.* FROM CompromissoAgendaPessoaHorario, Prospects WHERE CompromissoAgendaPessoaHorario.prospect = Prospects.codigo and Prospects.codigo = ? and dataCompromisso > ? ORDER BY Prospects.nome";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, codigoProspect, new Date()), nivelMontarDados, usuarioLogado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CompromissoAgendaPessoaHorario</code> através do valor do atributo
	 * <code>String hora</code>. Retorna os objetos, com início do valor do
	 * atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoAgendaPessoaHorarioVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorHora(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		valorConsulta += "%";
		String sqlStr = "SELECT * FROM CompromissoAgendaPessoaHorario WHERE upper( hora ) like(?) ORDER BY hora";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CompromissoAgendaPessoaHorario</code> através do valor do atributo
	 * <code>Integer agendaPessoaHorario</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoAgendaPessoaHorarioVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorAgendaPessoaHorario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM CompromissoAgendaPessoaHorario WHERE agendaPessoaHorario >= ?  ORDER BY agendaPessoaHorario";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado));
	}

	public Boolean consultarSeCompromissoRealizadoDiaAtual(Integer valorConsulta, Integer ano, Integer mes, Integer dia) throws Exception {
		String sqlStr = "SELECT interacaoworkflow.dataInicio AS dataIni, compromissoagendapessoahorario.hora AS hora FROM interacaoworkflow INNER JOIN compromissoagendapessoahorario ON compromissoagendapessoahorario.codigo = interacaoworkflow.compromissoagendapessoahorario WHERE compromissoagendapessoahorario = " + valorConsulta +";";// " and datainicio >= '" + ano + "-" + mes + "-" + dia + " 00:00:00' and datainicio <= '" + ano + "-" + mes + "-" + dia + " 23:59:59'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
//			if (Uteis.getHoraMinutoSegundo(tabelaResultado.getDate("dataIni"), "hora") < Integer.parseInt(tabelaResultado.getString("hora").substring(0, 2))) {
//				return Boolean.FALSE;
//			} else if (Uteis.getHoraMinutoSegundo(tabelaResultado.getDate("dataIni"), "hora") == Integer.parseInt(tabelaResultado.getString("hora").substring(0, 2))) {
//				if (Uteis.getHoraMinutoSegundo(tabelaResultado.getDate("dataIni"), "minutos") < Integer.parseInt(tabelaResultado.getString("hora").substring(3, 5))) {
//					return Boolean.FALSE;
//				}
//			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CompromissoAgendaPessoaHorario</code> através do valor do atributo
	 * <code>String descricao</code>. Retorna os objetos, com início do valor do
	 * atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoAgendaPessoaHorarioVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		valorConsulta += "%";
		String sqlStr = "SELECT * FROM CompromissoAgendaPessoaHorario WHERE upper( descricao ) like(?) ORDER BY descricao";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CompromissoAgendaPessoaHorario</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoAgendaPessoaHorarioVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM CompromissoAgendaPessoaHorario WHERE codigo >= ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado));
	}

	// public List consultaMontarComboboxCampanha(Integer codigoPessoa) throws
	// Exception {
	// StringBuilder sqlStr = new StringBuilder();
	// sqlStr.append("SELECT DISTINCT campanha.codigo AS campanha_codigo,");
	// sqlStr.append("campanha.descricao AS campanha_descricao ");
	// sqlStr.append("FROM CompromissoAgendaPessoaHorario ");
	// sqlStr.append("INNER JOIN  agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
	// sqlStr.append("INNER JOIN agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa ");
	// sqlStr.append("INNER JOIN campanha ON CompromissoAgendaPessoaHorario.campanha = campanha.codigo ");
	// sqlStr.append("WHERE agendapessoa.pessoa = ").append(codigoPessoa);
	// return
	// montarCampanhaCombobox(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
	// }
	//
	// public List montarCampanhaCombobox(SqlRowSet dadosSQL) throws Exception {
	// List objs = new ArrayList(0);
	// while (dadosSQL.next()) {
	// CampanhaVO campanhaVO = new CampanhaVO();
	// campanhaVO.setCodigo(dadosSQL.getInt("campanha_codigo"));
	// campanhaVO.setDescricao(dadosSQL.getString("campanha_descricao"));
	// objs.add(campanhaVO);
	// }
	// return objs;
	// }
	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoAgendaPessoaHorarioVO</code> resultantes da
	 *         consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados ( <code>ResultSet</code>) em um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code>.
	 * 
	 * @return O objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>
	 *         com os dados devidamente montados.
	 */
	public static CompromissoAgendaPessoaHorarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		CompromissoAgendaPessoaHorarioVO obj = new CompromissoAgendaPessoaHorarioVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getAgendaPessoaHorario().setCodigo(new Integer(dadosSQL.getInt("agendaPessoaHorario")));
		obj.setHora(dadosSQL.getString("hora"));
		obj.setTipoCompromisso(TipoCompromissoEnum.valueOf(dadosSQL.getString("tipoCompromisso")));
		obj.getProspect().setCodigo(new Integer(dadosSQL.getInt("prospect")));
		obj.getCampanha().setCodigo(new Integer(dadosSQL.getInt("campanha")));
		obj.getEtapaWorkflowVO().setCodigo(new Integer(dadosSQL.getInt("etapaworkflow")));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setOrigem(dadosSQL.getString("origem"));
		obj.setUrgente(new Boolean(dadosSQL.getBoolean("urgente")));
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.setDataCompromisso(dadosSQL.getDate("dataCompromisso"));
		// atributo transient montado somente para sabermos se o compromisso foi reagendado ou nao 
		obj.setDataCompromissoAnterior(dadosSQL.getDate("dataCompromisso"));
		obj.setDataInicialCompromisso(dadosSQL.getDate("dataInicialCompromisso"));
		obj.setHistoricoReagendamentoCompromisso(dadosSQL.getString("historicoReagendamentoCompromisso"));
		
		obj.setTipoContato(TipoContatoEnum.valueOf(dadosSQL.getString("tipoContato")));
		if (dadosSQL.getString("tipoSituacaoCompromissoEnum") != null && !dadosSQL.getString("tipoSituacaoCompromissoEnum").isEmpty()) {
			obj.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.valueOf(dadosSQL.getString("tipoSituacaoCompromissoEnum")));
		}
		obj.getPreInscricao().setCodigo(new Integer(dadosSQL.getInt("preinscricao")));
		obj.setNovoObj(new Boolean(false));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		montarDadosProspect(obj, nivelMontarDados, usuarioLogado);
		montarDadosEtapaWorkflow(obj, nivelMontarDados, usuarioLogado);
		montarDadosCampanha(obj, nivelMontarDados, usuarioLogado);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CampanhaVO</code> relacionado ao objeto
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Faz uso da chave primária
	 * da classe <code>CampanhaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosEtapaWorkflow(CompromissoAgendaPessoaHorarioVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getEtapaWorkflowVO().getCodigo().intValue() == 0) {
			obj.setEtapaWorkflowVO(new EtapaWorkflowVO());
			return;
		}
		obj.setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorChavePrimaria(obj.getEtapaWorkflowVO().getCodigo(), nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CampanhaVO</code> relacionado ao objeto
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Faz uso da chave primária
	 * da classe <code>CampanhaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCampanha(CompromissoAgendaPessoaHorarioVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getCampanha().getCodigo().intValue() == 0) {
			obj.setCampanha(new CampanhaVO());
			return;
		}
		obj.setCampanha(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCampanha().getCodigo(), false, nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ProspectsVO</code> relacionado ao objeto
	 * <code>CompromissoAgendaPessoaHorarioVO</code>. Faz uso da chave primária
	 * da classe <code>ProspectsVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosProspect(CompromissoAgendaPessoaHorarioVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getProspect().getCodigo().intValue() == 0) {
			obj.setProspect(new ProspectsVO());
			return;
		}
		obj.setProspect(getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(obj.getProspect().getCodigo(), nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>CompromissoAgendaPessoaHorarioVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public CompromissoAgendaPessoaHorarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		String sql = "SELECT * FROM CompromissoAgendaPessoaHorario WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}

	public List<CompromissoAgendaPessoaHorarioVO> consultarUnicidade(CompromissoAgendaPessoaHorarioVO obj, boolean alteracao, UsuarioVO usuarioLogado) throws Exception {
		super.verificarPermissaoConsultar(getIdEntidade(), false, usuarioLogado);
		return new ArrayList(0);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CompromissoAgendaPessoaHorario.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CompromissoAgendaPessoaHorario.idEntidade = idEntidade;
	}

	/**
	 * Este método gera um compromisso para um consultor, na data e hora
	 * previstas Vale destacar que exista um objeto de AgendaPessoaHorario para
	 * cada dia portanto, a data da geracao do compromisso é por padrao a
	 * dataCompromisso registrar em AgendaPessoaHorario
	 */
	public CompromissoAgendaPessoaHorarioVO executarGeracaoCompromissoRotacionamento(AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, UsuarioVO usuario, ProspectsVO prospect, String horaGeracaoProximaAgenda) throws Exception {
		CompromissoAgendaPessoaHorarioVO novoCompromisso = new CompromissoAgendaPessoaHorarioVO();
		novoCompromisso.setAgendaPessoaHorario(agendaPessoaHorario);
		novoCompromisso.setDataCompromisso(agendaPessoaHorario.getDataCompromisso());
		novoCompromisso.setDataInicialCompromisso(agendaPessoaHorario.getDataCompromisso());
		novoCompromisso.setHora(horaGeracaoProximaAgenda);
		return gerarHoraCompromisso(novoCompromisso, novoCompromisso.getAgendaPessoaHorario(), campanha, prospect, true, usuario);
	}

	public CompromissoAgendaPessoaHorarioVO executarGeracaoCompromisso(AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, UsuarioVO usuario, ProspectsVO prospect, Integer prospectsAgendados, String ultimaHoraRegistrada, Boolean considerarSabado, Boolean considerarFeriados, String horaFinalGerarAgenda) throws Exception {
		CompromissoAgendaPessoaHorarioVO novoCompromisso = new CompromissoAgendaPessoaHorarioVO();
		novoCompromisso.setAgendaPessoaHorario(agendaPessoaHorario);
		if (ultimaHoraRegistrada.equals("")) {
			if (!consultarHoraCompromissoAgendaPessoaHora(agendaPessoaHorario, campanha.getHoraInicial(), agendaPessoaHorario.getDia())) {
				novoCompromisso.setHora(campanha.getHoraInicial());
			} else {
				novoCompromisso.setHora(atualizarCompromissoHoraInicial(novoCompromisso, agendaPessoaHorario, campanha, campanha.getHoraInicial(), horaFinalGerarAgenda));
			}
		} else {
			novoCompromisso.setHora(atualizarCompromissoHoraInicial(novoCompromisso, agendaPessoaHorario, campanha, ultimaHoraRegistrada, horaFinalGerarAgenda));
		}
		return gerarHoraCompromisso( novoCompromisso, novoCompromisso.getAgendaPessoaHorario(), campanha, prospect, false, usuario);
	}

	public String atualizarCompromissoHoraInicial(CompromissoAgendaPessoaHorarioVO novoCompromisso, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, String horarioInicialTentativa, String horaFinalGerarAgenda) throws Exception {
		String horarioInicialTentativaInsercao = horarioInicialTentativa;
		Integer tempoMedioGerarAgenda = campanha.getWorkflow().getTempoMedioGerarAgenda();
		while (consultarHoraCompromissoAgendaPessoaHora(agendaPessoaHorario, horarioInicialTentativaInsercao, agendaPessoaHorario.getDia())) {
			if (horarioInicialTentativaInsercao.compareTo(Uteis.obterHoraAvancada(horarioInicialTentativaInsercao, campanha.getWorkflow().getTempoMedioGerarAgenda())) > 0
				|| 
				(
					Uteis.obterHoraAvancada(horarioInicialTentativaInsercao, campanha.getWorkflow().getTempoMedioGerarAgenda()).compareTo(horaFinalGerarAgenda) >= 0)
				) {
				Date dataNovocompromisso = Uteis.obterDataAvancadaPorDiaPorMesPorAno(agendaPessoaHorario.getDia(), agendaPessoaHorario.getMes(), agendaPessoaHorario.getAno(), 0);
				novoCompromisso.setDataCompromisso(dataNovocompromisso);
				agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(novoCompromisso.getAgendaPessoaHorario().getAgendaPessoa().getCodigo(), Uteis.obterDataAvancada(novoCompromisso.getDataCompromisso(), 1), Uteis.NIVELMONTARDADOS_COMBOBOX, true, null);
				if (agendaPessoaHorario.getCodigo() == 0) {
					agendaPessoaHorario = new AgendaPessoaHorarioVO(novoCompromisso.getAgendaPessoaHorario().getAgendaPessoa(), Uteis.getDiaMesData(Uteis.obterDataAvancada(novoCompromisso.getDataCompromisso(), 1)), Uteis.getMesData(Uteis.obterDataAvancada(novoCompromisso.getDataCompromisso(), 1)), Uteis.getAnoData(Uteis.obterDataAvancada(novoCompromisso.getDataCompromisso(), 1)), Uteis.getDiaSemanaEnum(Uteis.obterDataAvancada(novoCompromisso.getDataCompromisso(), 1)), true);
				}
				novoCompromisso.setAgendaPessoaHorario(agendaPessoaHorario);
				dataNovocompromisso = Uteis.obterDataAvancadaPorDiaPorMesPorAno(agendaPessoaHorario.getDia(), agendaPessoaHorario.getMes(), agendaPessoaHorario.getAno(), 0);
				novoCompromisso.setDataCompromisso(dataNovocompromisso);
			}
			if (!campanha.getWorkflow().getTempoMedioGerarAgenda().equals(0)) {
				horarioInicialTentativaInsercao = Uteis.obterHoraAvancada(horarioInicialTentativaInsercao, campanha.getWorkflow().getTempoMedioGerarAgenda());
			} else {
				horarioInicialTentativaInsercao = Uteis.obterHoraAvancada(horarioInicialTentativaInsercao, 1);
			}
		}
		return horarioInicialTentativaInsercao;
	}

	public CompromissoAgendaPessoaHorarioVO gerarHoraCompromisso(CompromissoAgendaPessoaHorarioVO novoCompromisso, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, ProspectsVO prospect, boolean validarProspectCampanhaDataCompromissoHora, UsuarioVO usuario) throws Exception {
		novoCompromisso.setDescricao("Agendamento gerado pela campanha " + campanha.getDescricao());
		novoCompromisso.setTipoContato(TipoContatoEnum.TELEFONE);
		novoCompromisso.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
		novoCompromisso.setProspect(prospect);
		novoCompromisso.setAgendaPessoaHorario(agendaPessoaHorario);
		novoCompromisso.setCampanha(campanha);
		if (novoCompromisso.getIsTipoCompromissoContato() && novoCompromisso.getEtapaWorkflowVO().getCodigo() == 0) {
			novoCompromisso.setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorCodigoCampanhaEtapaInicial(campanha.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		if(!Uteis.isAtributoPreenchido(novoCompromisso)){
			novoCompromisso.reagendarCompromissoParaDataFutura(novoCompromisso.getDataCompromisso(), usuario);	
		}
		agendaPessoaHorario.getListaCompromissoAgendaPessoaHorarioVOs().add(novoCompromisso);
		//Date dataNovocompromisso = Uteis.obterDataAvancadaPorDiaPorMesPorAno(agendaPessoaHorario.getDia(), agendaPessoaHorario.getMes(), agendaPessoaHorario.getAno(), 0);
		//novoCompromisso.setDataCompromisso(dataNovocompromisso);
		if (!validarProspectCampanhaDataCompromissoHora) {
			incluirCompromissoPorAgendaHorarioPessoa(novoCompromisso, usuario);
		} else if (!verificarExistenciaCompromissoAgendaPessoaHorarioPorProspectCampanhaDataCompromissoHora(novoCompromisso.getProspect().getCodigo(), novoCompromisso.getCampanha().getCodigo(), novoCompromisso.getDataCompromisso(), novoCompromisso.getHora())) {
			incluirCompromissoPorAgendaHorarioPessoa(novoCompromisso, usuario);
		}
		// // System.out.println("Data Compromisso: " +
		// Uteis.getData(novoCompromisso.getDataCompromisso()) +
		// " Hora Compromisso: " + novoCompromisso.getHora() +
		// " Prospect: " + novoCompromisso.getProspect().getNome() +
		// " Consultor: " +
		// novoCompromisso.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getNome());
		return novoCompromisso;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRemarcacoesCompromissos(List<CompromissoAgendaPessoaHorarioVO> lista, Date dataCompromissoAdiado, String horaAdiado, Boolean considerarSabado, Boolean considerarFeriados,String horaFimCompromissoAdiado, String horaIntevaloInicioCompromissoAdiado,String horaIntevaloFimCompromissoAdiado,Integer intervaloAgendaCompromisso, UsuarioVO usuarioLogado) throws Exception {
		validarDadosRemarcacaoAgenda(dataCompromissoAdiado,horaAdiado, horaFimCompromissoAdiado,  horaIntevaloInicioCompromissoAdiado, horaIntevaloFimCompromissoAdiado, intervaloAgendaCompromisso);
		boolean nenhumCompromissoSelecionado = true;
		HashMap<String, Object> mapaResultados = new HashMap<String, Object>();
		mapaResultados.put("dataCompromissoAdiado", dataCompromissoAdiado);
		mapaResultados.put("horaAdiado", horaAdiado);
		if (Uteis.isAtributoPreenchido(horaFimCompromissoAdiado)) {
			mapaResultados.put("horaFimAdiado", horaFimCompromissoAdiado);	
		}
		mapaResultados.put("ultimaHoraRegistrada", "");

		for (CompromissoAgendaPessoaHorarioVO compromissoExistente : lista) {
			if (compromissoExistente.getCompromissoSelecionado()) {
				String horaProximoCompromisso = "";
				nenhumCompromissoSelecionado = false;
				
				if (!((String) mapaResultados.get("ultimaHoraRegistrada")).equals("")) {
					 horaProximoCompromisso = Uteis.obterHoraAvancada((String) mapaResultados.get("ultimaHoraRegistrada"), intervaloAgendaCompromisso);
				}
				
				criarReagendamento(compromissoExistente , usuarioLogado);
				
				CompromissoAgendaPessoaHorarioVO novoCompromisso = compromissoExistente.clone();
				
				novoCompromisso.getCampanha().getWorkflow().setTempoMedioGerarAgenda(intervaloAgendaCompromisso);
				
				if (Uteis.isAtributoPreenchido(horaProximoCompromisso)) {
					novoCompromisso.setAgendaPessoaHorario(getFacadeFactory().getCampanhaFacade().realizarValidacaoAgendaPessoaHorarioExiste(novoCompromisso.getAgendaPessoaHorario().getAgendaPessoa(), dataCompromissoAdiado, considerarSabado, considerarFeriados, usuarioLogado));
					mapaResultados.put("ultimaHoraRegistrada", atualizarCompromissoHoraInicial(novoCompromisso, novoCompromisso.getAgendaPessoaHorario(), novoCompromisso.getCampanha(), ((String) mapaResultados.get("ultimaHoraRegistrada")), ""));
				}
				
				
				 if ((Uteis.isAtributoPreenchido(horaFimCompromissoAdiado) && Uteis.isAtributoPreenchido(horaProximoCompromisso)) &&
					(horaProximoCompromisso.compareTo(horaFimCompromissoAdiado) >= 0)) {
					
					 mapaResultados.put("dataCompromissoAdiado", Uteis.obterDataAvancada((Date) mapaResultados.get("dataCompromissoAdiado"), 1));
					 mapaResultados.put("ultimaHoraRegistrada", "");
					 
					}
				
				 if ((Uteis.isAtributoPreenchido(horaIntevaloFimCompromissoAdiado) && Uteis.isAtributoPreenchido(horaIntevaloInicioCompromissoAdiado) && Uteis.isAtributoPreenchido(horaProximoCompromisso)) &&
				    ((horaProximoCompromisso.compareTo(horaIntevaloFimCompromissoAdiado) < 0) && horaProximoCompromisso.compareTo(horaIntevaloInicioCompromissoAdiado) >= 0)) {
					 mapaResultados.put("ultimaHoraRegistrada", Uteis.obterHoraRegredida(horaIntevaloFimCompromissoAdiado, intervaloAgendaCompromisso));
				 }
				executarRemarcacaoCompromisso(mapaResultados, novoCompromisso, true, true, horaFimCompromissoAdiado,  horaIntevaloInicioCompromissoAdiado, horaIntevaloFimCompromissoAdiado, intervaloAgendaCompromisso, usuarioLogado);
				executarAtualizacaoTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.REALIZADO_COM_REMARCACAO, compromissoExistente.getCodigo(), usuarioLogado);
			}
		}
		if (nenhumCompromissoSelecionado) {
			throw new Exception("Nenhum compromisso foi selecionado.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarExclusaoCompromissos(List<CompromissoAgendaPessoaHorarioVO> lista, UsuarioVO usuarioLogado) throws Exception {
		boolean nenhumCompromissoSelecionado = true;

		for (CompromissoAgendaPessoaHorarioVO compromissoExistente : lista) {
			if (compromissoExistente.getCompromissoSelecionado()) {
				nenhumCompromissoSelecionado = false;
				if (compromissoExistente.getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)) {
					excluir(compromissoExistente, usuarioLogado);
				} else {
					executarAtualizacaoTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.REALIZADO, compromissoExistente.getCodigo(), usuarioLogado);
				}
			}
		}
		if (nenhumCompromissoSelecionado) {
			throw new Exception("Nenhum compromisso foi selecionado.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRemarcacaoCompromisso(CompromissoAgendaPessoaHorarioVO novoCompromisso, Date dataCompromissoAdiado, String horaAdiado, String horaFimAdiado, Boolean considerarSabado, Boolean considerarFeriados,String horaFimCompromissoAdiado, String horaIntevaloInicioCompromissoAdiado,String horaIntevaloFimCompromissoAdiado,Integer intervaloAgendaCompromisso, UsuarioVO usuarioLogado) throws Exception {
		if (dataCompromissoAdiado == null) {
			throw new Exception("O campo (Data compromisso) deve ser informado.");
		}
		if (horaAdiado == null || horaAdiado.isEmpty()) {
			throw new Exception("O campo (Hora) deve ser informado.");
		}
		HashMap<String, Object> mapaResultados = new HashMap<String, Object>();
		mapaResultados.put("dataCompromissoAdiado", dataCompromissoAdiado);
		mapaResultados.put("horaAdiado", horaAdiado);
		mapaResultados.put("horaFimAdiado", horaFimAdiado);
		mapaResultados.put("ultimaHoraRegistrada", "");
		executarRemarcacaoCompromisso(mapaResultados, novoCompromisso, true, true, horaFimCompromissoAdiado,  horaIntevaloInicioCompromissoAdiado, horaIntevaloFimCompromissoAdiado, intervaloAgendaCompromisso,  usuarioLogado);	
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarRemarcacaoCompromisso(HashMap<String, Object> mapaResultados, CompromissoAgendaPessoaHorarioVO novoCompromisso, Boolean considerarSabado, Boolean considerarFeriados,String horaFimCompromissoAdiado, String horaIntevaloInicioCompromissoAdiado,String horaIntevaloFimCompromissoAdiado,Integer intervaloAgendaCompromisso,  UsuarioVO usuarioLogado) throws Exception {		
		Date dataCompromissoAdiar = ((Date) mapaResultados.get("dataCompromissoAdiado"));
		novoCompromisso.setDataCompromisso(dataCompromissoAdiar);
		novoCompromisso.setAgendaPessoaHorario(getFacadeFactory().getCampanhaFacade().realizarValidacaoAgendaPessoaHorarioExiste(novoCompromisso.getAgendaPessoaHorario().getAgendaPessoa(), dataCompromissoAdiar, considerarSabado, considerarFeriados, usuarioLogado));
 		if (((String) mapaResultados.get("ultimaHoraRegistrada")).equals("")) {
			if (!consultarHoraCompromissoAgendaPessoaHora(novoCompromisso.getAgendaPessoaHorario(), ((String) mapaResultados.get("horaAdiado")), Uteis.getDiaMesData(((Date) mapaResultados.get("dataCompromissoAdiado"))))) {
				novoCompromisso.setHora(((String) mapaResultados.get("horaAdiado")));
				novoCompromisso.setHoraFim(((String) mapaResultados.get("horaFimAdiado")));
			} else {
				throw new Exception("Já existe um compromisso agendado para o dia " + Uteis.getData((Date) mapaResultados.get("dataCompromissoAdiado")) + " às: " + mapaResultados.get("horaAdiado") + ".");
			}
		} else {
			if (!novoCompromisso.getCampanha().getWorkflow().getTempoMedioGerarAgenda().equals(0)) {
				novoCompromisso.setHora(Uteis.obterHoraAvancada((String) mapaResultados.get("ultimaHoraRegistrada"), novoCompromisso.getCampanha().getWorkflow().getTempoMedioGerarAgenda()));
			} else {
				novoCompromisso.setHora(Uteis.obterHoraAvancada((String) mapaResultados.get("ultimaHoraRegistrada"), intervaloAgendaCompromisso));
			}
		}				
		novoCompromisso = gerarHoraCompromisso(novoCompromisso, novoCompromisso.getAgendaPessoaHorario(), novoCompromisso.getCampanha(), novoCompromisso.getProspect(), false, usuarioLogado);
		mapaResultados.put("ultimaHoraRegistrada", novoCompromisso.getHora());
		mapaResultados.put("dataCompromissoAdiado", novoCompromisso.getDataCompromisso());	
	}

	public Integer realizarSomaTotalProspects(List<CampanhaPublicoAlvoVO> lista) {
		Iterator<CampanhaPublicoAlvoVO> i = lista.iterator();
		Integer somaTotalProspects = 0;
		while (i.hasNext()) {
			CampanhaPublicoAlvoVO obj = i.next();
			somaTotalProspects = somaTotalProspects + obj.getTotalProspectsSelecionadosCampanha();
		}
		return somaTotalProspects;
	}

	public boolean consultarHoraCompromissoAgendaPessoaHora(AgendaPessoaHorarioVO agendaPessoaHorario, String hora, Date diaEspecifico) throws Exception {
		Integer dia = Uteis.getDiaMesData(diaEspecifico);
		Integer mes = Uteis.getMesData(diaEspecifico);
		Integer ano = Uteis.getAnoData(diaEspecifico);
		String sql = "SELECT * FROM CompromissoAgendaPessoaHorario AS caph INNER JOIN " + "AgendaPessoaHorario AS aph ON aph.codigo = caph.agendapessoahorario WHERE aph.codigo = " + agendaPessoaHorario.getCodigo() + " and aph.ano = " + ano + " and aph.mes = " + mes + " and aph.dia = " + dia + " and caph.hora = '" + hora + "'  ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			return false;
		}
		return true;
	}

	public boolean consultarHoraCompromissoAgendaPessoaHora(AgendaPessoaHorarioVO agendaPessoaHorario, String hora, Integer dia) throws Exception {
		String sql = "SELECT * FROM CompromissoAgendaPessoaHorario AS caph INNER JOIN " + "AgendaPessoaHorario AS aph ON aph.codigo = caph.agendapessoahorario WHERE aph.codigo = " + agendaPessoaHorario.getCodigo() + " and aph.mes = " + agendaPessoaHorario.getMes() + " and aph.ano = " + agendaPessoaHorario.getAno() + " and caph.hora = '" + hora + "' and aph.dia = " + dia;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			return false;
		}
		return true;
	}

	public CompromissoAgendaPessoaHorarioVO consultarCompromissoPorCodigoProspect(Integer valorConsulta, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT cap.codigo AS cap_codigo, ap.pessoa AS ap_pessoa, cap.hora AS hora, aph.dia AS dia, aph.mes AS mes, ");
		sqlStr.append("cap.descricao AS cap_descricao, cap.dataCompromisso AS cap_dataCompromisso, cap.tipoContato AS cap_tipoContato,");
		sqlStr.append("cap.dataInicialCompromisso AS cap_dataInicialCompromisso, cap.historicoReagendamentoCompromisso AS cap_historicoReagendamentoCompromisso,");
		sqlStr.append("cap.tipoCompromisso AS cap_tipoCompromisso, campanha.codigo AS campanha_codigo,");
		sqlStr.append("cap.urgente AS cap_urgente, cap.observacao AS cap_observacao,");
		sqlStr.append("campanha.descricao AS campanha_descricao, cap.tipoSituacaoCompromissoEnum AS cap_tipoSituacaoCompromissoEnum, ");
		sqlStr.append("prospects.codigo AS prospects_codigo, prospects.nome AS prospects_nome,");
		sqlStr.append(" aph.ano AS ano, wf.nome AS wf_nome, pessoa.nome AS nomeColaborador,  ewf.codigo as  ewf_codigo, ewf.nome as  ewf_nome ");
		sqlStr.append("FROM compromissoagendapessoahorario AS cap ");
		sqlStr.append("INNER JOIN agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario ");
		sqlStr.append("INNER JOIN agendapessoa AS ap ON ap.codigo = aph.agendapessoa ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = ap.pessoa ");
		sqlStr.append("INNER JOIN prospects ON prospects.codigo = cap.prospect ");
		sqlStr.append("LEFT JOIN campanha ON campanha.codigo = cap.campanha ");
		sqlStr.append("LEFT JOIN workflow AS wf ON wf.codigo = campanha.workflow ");
		sqlStr.append(" left join etapaworkflow AS ewf ON ewf.codigo = cap.etapaworkflow ");

		// sqlStr.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = ewf.situacaodefinirprospectfinal ");
		// sqlStr.append(" left join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline   ");

		sqlStr.append("WHERE prospects.codigo = ").append(valorConsulta);
		sqlStr.append(" AND  cap.tipoSituacaoCompromissoEnum <> '").append(TipoSituacaoCompromissoEnum.REALIZADO).append("'");
		sqlStr.append(" AND  cap.tipoSituacaoCompromissoEnum <> '").append(TipoSituacaoCompromissoEnum.REALIZADO_COM_REMARCACAO).append("'");

		boolean compromissoVenda = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaVendas", usuario);
			compromissoVenda = true;
		} catch (Exception e) {
			compromissoVenda = false;
		}		 
		
		boolean compromissoFinanceiro = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVisualizarCampanhaCobranca", usuario);
			compromissoFinanceiro = true;
		} catch (Exception e) {
			compromissoFinanceiro = false;
		}
		if (compromissoVenda && !compromissoFinanceiro) {
			sqlStr.append(" and campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA'");
		} else if (!compromissoVenda && compromissoFinanceiro) {
			sqlStr.append(" and campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA'");
		} else if (!compromissoVenda && !compromissoFinanceiro) {
			sqlStr.append(" and campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA'");
		}		
		// sqlStr.append(" AND ((cap.hora > '").append(Uteis.getHoraAtual()).append("'");
		// sqlStr.append(" AND aph.dia = ").append(Uteis.getDiaMesData(new
		// Date()));
		// sqlStr.append(" AND aph.mes = ").append(Uteis.getMesData(new
		// Date()));
		// sqlStr.append(" AND aph.ano = ").append(Uteis.getAnoData(new
		// Date()));
		// sqlStr.append(") OR  (aph.dia > ").append(Uteis.getDiaMesData(new
		// Date()));
		// sqlStr.append(" AND aph.mes = ").append(Uteis.getMesData(new
		// Date()));
		// sqlStr.append(" AND aph.ano = ").append(Uteis.getAnoData(new
		// Date()));
		// sqlStr.append(") OR (aph.mes > ").append(Uteis.getMesData(new
		// Date()));
		// sqlStr.append(" AND aph.ano = ").append(Uteis.getAnoData(new
		// Date()));
		// sqlStr.append(") OR aph.ano > ").append(Uteis.getAnoData(new
		// Date()));
		// sqlStr.append(") ");
		sqlStr.append(" ORDER BY aph.ano, aph.mes, aph.dia, cap.hora ");
		sqlStr.append("limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarCompromisso(tabelaResultado);
		}
		return new CompromissoAgendaPessoaHorarioVO();

	}

	/**
	 * Este método verificar se existe uma agenda (que nao seja de cobranca) não
	 * finalizada, para contactar um determinado prospect. Este método é
	 * utilizado na geração de agenda para garantir que não seja gerada uma
	 * agenda duplicada para um prospect, em um determinado período.
	 * 
	 * @param codigoProspect
	 * @param dataInicio
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean verificarExisteAgendaNaoConcluidaProspectDentroPeriodoParaCaptacao(Integer codigoProspect, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT compromissoagendapessoahorario.codigo ");
		sqlStr.append("FROM compromissoagendapessoahorario ");
		sqlStr.append(" INNER JOIN prospects ON prospects.codigo = compromissoagendapessoahorario.prospect ");
		sqlStr.append(" INNER JOIN campanha ON campanha.codigo = compromissoagendapessoahorario.campanha ");
		sqlStr.append(" WHERE (prospects.codigo = ").append(codigoProspect).append(")");
		sqlStr.append("  AND (compromissoagendapessoahorario.datacompromisso::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and compromissoagendapessoahorario.datacompromisso::date <='").append(Uteis.getDataJDBC(dataFinal)).append("') ");
		sqlStr.append("  AND ((compromissoagendapessoahorario.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO') or (compromissoagendapessoahorario.tiposituacaocompromissoenum = 'PARALIZADO'))");
		sqlStr.append("  AND (campanha.tipocampanha <> 'CONTACTAR_ALUNOS_COBRANCA') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true ;
		}
		return false;
	}

	@Override
	public List<CompromissoAgendaPessoaHorarioVO> consultarTodosCompromissoPorCodigoProspect(Integer valorConsulta) throws Exception {
		List<CompromissoAgendaPessoaHorarioVO> compromissoAgendaPessoaHorarioVOs = new ArrayList<CompromissoAgendaPessoaHorarioVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT cap.codigo AS cap_codigo, ap.pessoa AS ap_pessoa, cap.hora AS hora, aph.dia AS dia, aph.mes AS mes, ");
		sqlStr.append("cap.descricao AS cap_descricao, cap.dataCompromisso AS cap_dataCompromisso, cap.tipoContato AS cap_tipoContato,");
		sqlStr.append("cap.dataInicialCompromisso AS cap_dataInicialCompromisso, cap.historicoReagendamentoCompromisso AS cap_historicoReagendamentoCompromisso,");
		sqlStr.append("cap.tipoCompromisso AS cap_tipoCompromisso, campanha.codigo AS campanha_codigo,");
		sqlStr.append("cap.urgente AS cap_urgente, cap.observacao AS cap_observacao,");
		sqlStr.append("campanha.descricao AS campanha_descricao, cap.tipoSituacaoCompromissoEnum AS cap_tipoSituacaoCompromissoEnum, ");
		sqlStr.append("prospects.codigo AS prospects_codigo, prospects.nome AS prospects_nome,");
		sqlStr.append(" aph.ano AS ano, wf.nome AS wf_nome, pessoa.nome AS nomeColaborador,  ewf.codigo as  ewf_codigo, ewf.nome as  ewf_nome ");
		sqlStr.append("FROM compromissoagendapessoahorario AS cap ");
		sqlStr.append("INNER JOIN agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario ");
		sqlStr.append("INNER JOIN agendapessoa AS ap ON ap.codigo = aph.agendapessoa ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = ap.pessoa ");
		sqlStr.append(" INNER JOIN prospects ON prospects.codigo = cap.prospect ");
		sqlStr.append(" LEFT JOIN campanha ON campanha.codigo = cap.campanha ");
		sqlStr.append(" LEFT JOIN workflow AS wf ON wf.codigo = campanha.workflow ");
		sqlStr.append(" left join etapaworkflow AS ewf ON ewf.codigo = cap.etapaworkflow ");

		// sqlStr.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = ewf.situacaodefinirprospectfinal ");
		// sqlStr.append(" left join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline   ");

		sqlStr.append("WHERE prospects.codigo = ").append(valorConsulta);

		sqlStr.append(" ORDER BY cap.dataCompromisso asc, cap.hora asc");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			compromissoAgendaPessoaHorarioVOs.add(montarCompromisso(tabelaResultado));
		}
		return compromissoAgendaPessoaHorarioVOs;
	}

	public CompromissoAgendaPessoaHorarioVO montarCompromisso(SqlRowSet dadosSQL) throws Exception {
		CompromissoAgendaPessoaHorarioVO obj = new CompromissoAgendaPessoaHorarioVO();
		// if (dadosSQL.next()) {
		obj.setCodigo(dadosSQL.getInt("cap_codigo"));
		obj.setObservacao(dadosSQL.getString("cap_observacao"));
		if (dadosSQL.getString("cap_tipoCompromisso") != null || dadosSQL.getString("cap_tipoCompromisso").equals("")) {
			obj.setTipoCompromisso(TipoCompromissoEnum.valueOf(dadosSQL.getString("cap_tipoCompromisso")));
		}
		obj.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(dadosSQL.getInt("ap_pessoa"));
		obj.setDescricao(dadosSQL.getString("cap_descricao"));
		obj.getCampanha().setCodigo(dadosSQL.getInt("campanha_codigo"));
		obj.getCampanha().setDescricao(dadosSQL.getString("campanha_descricao"));
		obj.getProspect().setCodigo(dadosSQL.getInt("prospects_codigo"));
		obj.getProspect().setNome(dadosSQL.getString("prospects_nome"));
		obj.setDataCompromisso(dadosSQL.getTimestamp("cap_dataCompromisso"));
		obj.setDataCompromissoAnterior(dadosSQL.getTimestamp("cap_dataCompromisso"));
		obj.setDataInicialCompromisso(dadosSQL.getTimestamp("cap_dataInicialCompromisso"));
		obj.setHistoricoReagendamentoCompromisso(dadosSQL.getString("cap_historicoReagendamentoCompromisso"));
		
		if (dadosSQL.getString("cap_tipoCompromisso") != null || dadosSQL.getString("cap_tipoContato").equals("")) {
			obj.setTipoContato(TipoContatoEnum.valueOf(dadosSQL.getString("cap_tipoContato")));
		}
		obj.setUrgente(dadosSQL.getBoolean("cap_urgente"));
		obj.setHora(dadosSQL.getString("hora"));
		if (dadosSQL.getString("cap_tipoSituacaoCompromissoEnum") != null && !dadosSQL.getString("cap_tipoSituacaoCompromissoEnum").isEmpty()) {
			obj.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.valueOf(dadosSQL.getString("cap_tipoSituacaoCompromissoEnum")));
		}
		// obj.setCompromissoRealizado(dadosSQL.getBoolean("cap_compromissorealizado"));
		// obj.setCompromissoRealizado(dadosSQL.getBoolean("cap_compromissoparalizado"));
		obj.getAgendaPessoaHorario().setDia(dadosSQL.getInt("dia"));
		obj.getAgendaPessoaHorario().setMes(dadosSQL.getInt("mes"));
		obj.getAgendaPessoaHorario().setAno(dadosSQL.getInt("ano"));
		obj.getCampanha().getWorkflow().setNome(dadosSQL.getString("wf_nome"));
		obj.getEtapaWorkflowVO().setCodigo(dadosSQL.getInt("ewf_codigo"));
		obj.getEtapaWorkflowVO().setNome(dadosSQL.getString("ewf_nome"));
		obj.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setNome(dadosSQL.getString("nomeColaborador"));
		// }
		return obj;
	}

	/**
	 * Remove outros compromissos de uma campanha de pre-incsricao, quando
	 * altera-se o responavel por atender um determinado prospect. Somente
	 * compromissos nao iniciados e anteriores ao sia atual.
	 * 
	 * @param codPessoaAgendaManter
	 * @param codProspectAgenda
	 * @param codCampanha
	 * @param verificarAcesso
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerCompromissoPreInscricaoPessoaAnteriormenteResponsavelCompromisso(Integer codPessoaAgendaManter, Integer codProspectAgenda, Integer codCampanha, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.excluir(getIdEntidade(), false, usuario);
		StringBuilder str = new StringBuilder();
		str.append(" delete from compromissoagendapessoahorario where codigo in ( ");
		str.append(" select cap.codigo ");
		str.append(" FROM agendapessoahorario as aph ");
		str.append(" inner join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
		str.append(" inner join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
		str.append(" inner join pessoa on ap.pessoa = pessoa.codigo ");
		str.append(" inner join prospects on cap.prospect = prospects.codigo ");
		str.append(" inner join campanha on cap.campanha = campanha.codigo ");
		str.append(" inner join workflow on campanha.workflow = workflow.codigo ");
		str.append(" inner join etapaworkflow on cap.etapaworkflow = etapaworkflow.codigo ");
		str.append(" where pessoa.codigo!= ").append(codPessoaAgendaManter);
		str.append(" and cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
		str.append(" AND aph.dia <= ").append(Uteis.getDiaMesData(new Date()));
		str.append(" AND aph.mes <= ").append(Uteis.getMesData(new Date()));
		str.append(" AND aph.ano <= ").append(Uteis.getAnoData(new Date()));
		str.append(" AND campanha.codigo = ").append(codCampanha);
		str.append(" AND prospects.codigo = ").append(codProspectAgenda);
		str.append(" ) ");
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(str.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerCompromissosNaoIniciacaoCampanha(Integer codCampanha, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.excluir(getIdEntidade(), false, usuario);
		StringBuilder str = new StringBuilder();
		str.append(" delete from compromissoagendapessoahorario where codigo in ( ");
		str.append(" select cap.codigo ");
		str.append(" FROM agendapessoahorario as aph ");
		str.append(" inner join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
		str.append(" inner join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
		str.append(" inner join campanha on cap.campanha = campanha.codigo ");
		str.append(" where campanha.codigo= ").append(codCampanha);
		str.append(" and cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
		str.append(" ) and codigo not in (select compromissoagendapessoahorario from interacaoworkflow where compromissoagendapessoahorario is not null) ");
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(str.toString());
	}

	public List<CompromissoAgendaPessoaHorarioVO> consultarCompromissoPorCodigoPessoaContatosPendentes(Integer codigoPessoa, Integer unidadeEnsino, Boolean contatosNaoIniciados, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		StringBuilder str = new StringBuilder();
		str.append(" SELECT DISTINCT ");
		str.append(" pessoa.nome as pessoa_nome, ");
		str.append(" pessoa.codigo as pessoa_codigo, ");
		str.append(" ap.codigo as ap_codigo, ");
		str.append(" aph.codigo as aph_codigo, ");
		str.append(" aph.dia as aph_dia, ");
		str.append(" aph.mes as aph_mes,  ");
		str.append(" aph.ano as aph_ano , ");
		str.append(" aph.diaSemanaEnum as aph_diaSemanaEnum , ");
		str.append(" aph.agendapessoa as aph_agendapessoa, ");
		str.append(" cap.codigo as cap_codigo, ");
		str.append(" cap.descricao as cap_descricao,  ");
		str.append(" cap.hora as cap_hora ,  ");
		str.append(" cap.tipocompromisso as cap_tipocompromisso,  ");
		str.append(" cap.tipoSituacaoCompromissoEnum as cap_tipoSituacaoCompromissoEnum,  ");
		str.append(" cap.observacao as cap_observacao,  ");
		str.append(" cap.origem as cap_origem,  ");
		str.append(" cap.urgente as cap_urgente,  ");
		str.append(" cap.datacadastro as cap_datacadastro,   ");
		str.append(" cap.datacompromisso as cap_datacompromisso,   ");
		str.append(" cap.dataInicialCompromisso AS cap_dataInicialCompromisso, ");
		str.append(" cap.historicoReagendamentoCompromisso AS cap_historicoReagendamentoCompromisso, ");
		str.append(" cap.tipocontato as cap_tipocontato,   ");
		str.append(" cap.prospect as cap_prospect ,   ");
		str.append(" cap.campanha as cap_campanha,  ");
		str.append(" prospects.nome as prospects_nome,  ");
		str.append(" prospects.emailprincipal as prospects_emailprincipal,  ");
		str.append(" prospects.telefoneresidencial as prospects_telefoneresidencial,  ");
		str.append(" campanha.descricao as campanha_descricao,  ");
		str.append(" workflow.codigo as workflow_codigo,  ");
		str.append(" workflow.tempoMedioGerarAgenda as workflow_tempoMedioGerarAgenda,  ");
		str.append(" etapaworkflow.codigo as etapaworkflow_codigo,  ");
		str.append(" etapaworkflow.nome as etapaworkflow_nome  ");
		str.append(" FROM agendapessoahorario as aph  ");
		str.append(" inner join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
		str.append(" inner join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
		str.append(" inner join pessoa on ap.pessoa = pessoa.codigo ");
		str.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
		str.append(" left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario ");
		str.append(" inner join prospects on cap.prospect = prospects.codigo ");
		str.append(" inner join campanha on cap.campanha = campanha.codigo ");
		str.append(" inner join workflow on campanha.workflow = workflow.codigo ");
		str.append(" inner join etapaworkflow on cap.etapaworkflow = etapaworkflow.codigo ");
		str.append(" where pessoa.codigo= ").append(codigoPessoa);
		if (contatosNaoIniciados) {
			str.append(" and cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
			str.append(" AND aph.dia < ").append(Uteis.getDiaMesData(new Date()));
		} else {
			str.append(" and cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.PARALIZADO).append("'");
			str.append(" AND aph.dia <= ").append(Uteis.getDiaMesData(new Date()));
		}
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			str.append(" and funcionariocargo.unidadeEnsino = ").append(unidadeEnsino);
		}
		str.append(" AND aph.mes <= ").append(Uteis.getMesData(new Date()));
		str.append(" AND aph.ano <= ").append(Uteis.getAnoData(new Date()));
		str.append(" order by  aph.dia, aph.mes, aph.ano, cap.hora  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return montarCompromissoContatosPendentes(tabelaResultado);

	}

	public List<CompromissoAgendaPessoaHorarioVO> montarCompromissoContatosPendentes(SqlRowSet dadosSQL) throws Exception {
		List<CompromissoAgendaPessoaHorarioVO> lista = new ArrayList<CompromissoAgendaPessoaHorarioVO>();
		while (dadosSQL.next()) {
			CompromissoAgendaPessoaHorarioVO compromisso = new CompromissoAgendaPessoaHorarioVO();
			compromisso.setCodigo(new Integer(dadosSQL.getInt("cap_codigo")));
			compromisso.setDescricao(dadosSQL.getString("cap_descricao"));
			compromisso.setHora(dadosSQL.getString("cap_hora"));
			compromisso.setTipoCompromisso(TipoCompromissoEnum.valueOf(dadosSQL.getString("cap_tipocompromisso")));
			compromisso.setObservacao(dadosSQL.getString("cap_observacao"));
			compromisso.setOrigem(dadosSQL.getString("cap_origem"));
			if (dadosSQL.getString("cap_tipoSituacaoCompromissoEnum") != null && !dadosSQL.getString("cap_tipoSituacaoCompromissoEnum").isEmpty()) {
				compromisso.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.valueOf(dadosSQL.getString("cap_tipoSituacaoCompromissoEnum")));
			}
			compromisso.setUrgente(new Boolean(dadosSQL.getBoolean("cap_urgente")));
			compromisso.setDataCadastro(dadosSQL.getDate("cap_datacadastro"));
			compromisso.setDataCompromisso(dadosSQL.getDate("cap_datacompromisso"));
			compromisso.setDataCompromissoAnterior(dadosSQL.getDate("cap_dataCompromisso"));
			compromisso.setDataInicialCompromisso(dadosSQL.getDate("cap_dataInicialCompromisso"));
			compromisso.setHistoricoReagendamentoCompromisso(dadosSQL.getString("cap_historicoReagendamentoCompromisso"));
			
			compromisso.setTipoContato(TipoContatoEnum.valueOf(dadosSQL.getString("cap_tipocontato")));

			/*
			 * Dados Pessoa
			 */
			compromisso.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setNome(dadosSQL.getString("pessoa_nome"));
			compromisso.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(dadosSQL.getInt("pessoa_codigo"));

			/*
			 * Dados AgendaPessoa
			 */
			compromisso.getAgendaPessoaHorario().getAgendaPessoa().setCodigo(new Integer(dadosSQL.getInt("ap_codigo")));

			/*
			 * Dados AgendaPessoaHorario
			 */
			compromisso.getAgendaPessoaHorario().setCodigo(new Integer(dadosSQL.getInt("aph_codigo")));
			compromisso.getAgendaPessoaHorario().getAgendaPessoa().setCodigo(new Integer(dadosSQL.getInt("aph_agendapessoa")));
			compromisso.getAgendaPessoaHorario().setDia(new Integer(dadosSQL.getInt("aph_dia")));
			compromisso.getAgendaPessoaHorario().setMes(new Integer(dadosSQL.getInt("aph_mes")));
			compromisso.getAgendaPessoaHorario().setAno(new Integer(dadosSQL.getInt("aph_ano")));
			compromisso.getAgendaPessoaHorario().setDiaSemanaEnum(DiaSemana.valueOf(dadosSQL.getString("aph_diaSemanaEnum")));
			compromisso.getAgendaPessoaHorario().setNovoObj(new Boolean(false));
			/*
			 * Dados Prospect
			 */
			compromisso.getProspect().setCodigo(new Integer(dadosSQL.getInt("cap_prospect")));
			compromisso.getProspect().setNome((dadosSQL.getString("prospects_nome")));
			compromisso.getProspect().setEmailPrincipal((dadosSQL.getString("prospects_emailprincipal")));
			compromisso.getProspect().setTelefoneResidencial((dadosSQL.getString("prospects_telefoneresidencial")));
			/*
			 * Dados Campanha
			 */
			compromisso.getCampanha().setCodigo(new Integer(dadosSQL.getInt("cap_campanha")));
			compromisso.getCampanha().setDescricao((dadosSQL.getString("campanha_descricao")));
			/*
			 * Dados Workflow
			 */
			compromisso.getCampanha().getWorkflow().setCodigo(new Integer(dadosSQL.getInt("workflow_codigo")));
			compromisso.getCampanha().getWorkflow().setTempoMedioGerarAgenda((dadosSQL.getInt("workflow_tempoMedioGerarAgenda")));
			/*
			 * Dados Etapa
			 */
			compromisso.getEtapaWorkflowVO().setCodigo(new Integer(dadosSQL.getInt("etapaworkflow_codigo")));
			compromisso.getEtapaWorkflowVO().setNome((dadosSQL.getString("etapaworkflow_nome")));
			compromisso.setNovoObj(new Boolean(false));
			lista.add(compromisso);
		}
		return lista;
	}

	public void consultarQuantidadeCompromissoPorColaborador(CampanhaColaboradorVO campanhaColaboradorVO, Date dataIncial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		StringBuilder str = new StringBuilder();
		str.append(" SELECT ");
		str.append(" ( select count (1) from compromissoagendapessoahorario as cap ");
		str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo");
		str.append(" where  cap.campanha = ").append(campanhaColaboradorVO.getCampanha().getCodigo());
		str.append(" ) as qtdCompromissoCampanha, ");
		str.append(" ( select count (1) from compromissoagendapessoahorario as cap  ");
		str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo ");
		str.append(" where cap.datacompromisso BETWEEN '").append(Uteis.getDataJDBC(dataIncial)).append("' AND '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		str.append(" and cap.campanha = ").append(campanhaColaboradorVO.getCampanha().getCodigo());
		str.append(" and cap.tipoSituacaoCompromissoEnum in ('").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("','").append(TipoSituacaoCompromissoEnum.PARALIZADO).append("') ");
		str.append(" ) as qtdContatosPeriodo ");
		str.append(" from agendapessoa  where  agendapessoa.pessoa = ").append(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (tabelaResultado.next()) {
			campanhaColaboradorVO.setQtdCompromissoCampanha(tabelaResultado.getInt("qtdCompromissoCampanha"));
			campanhaColaboradorVO.setQtdCompromissoPeriodo(tabelaResultado.getInt("qtdContatosPeriodo"));
		}
	}

	public void consultarCompromissoConsultor(CampanhaColaboradorVO campanhaColaboradorVO, Date dataIncial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		StringBuilder str = new StringBuilder();
		str.append(" SELECT ");
		str.append(" ( select count (1) from compromissoagendapessoahorario as cap ");
		str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo");
		str.append(" where  cap.campanha = ").append(campanhaColaboradorVO.getCampanha().getCodigo());
		str.append(" ) as qtdCompromissoCampanha, ");
		str.append(" ( select count (1) from compromissoagendapessoahorario as cap  ");
		str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario and aph.agendapessoa = agendapessoa.codigo ");
		str.append(" where cap.datacompromisso BETWEEN '").append(Uteis.getDataJDBC(dataIncial)).append("' AND '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		str.append(" and cap.campanha = ").append(campanhaColaboradorVO.getCampanha().getCodigo());
		str.append(" and cap.tipoSituacaoCompromissoEnum in ('").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("','").append(TipoSituacaoCompromissoEnum.PARALIZADO).append("') ");
		str.append(" ) as qtdContatosPeriodo ");
		str.append(" from agendapessoa  where  agendapessoa.pessoa = ").append(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (tabelaResultado.next()) {
			campanhaColaboradorVO.setQtdCompromissoCampanha(tabelaResultado.getInt("qtdCompromissoCampanha"));
			campanhaColaboradorVO.setQtdCompromissoPeriodo(tabelaResultado.getInt("qtdContatosPeriodo"));
		}
	}

	public List<CompromissoAgendaPessoaHorarioVO> consultarCompromissoPorColaborador(CampanhaColaboradorVO campanhaColaboradorVO, Date dataIncial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		StringBuilder str = new StringBuilder();
		str.append(" SELECT cap.codigo, cap.prospect from compromissoagendapessoahorario as cap  ");
		str.append(" inner join agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario ");
		str.append(" inner join agendapessoa AS ap ON aph.agendapessoa = ap.codigo ");
		str.append(" where cap.datacompromisso BETWEEN '").append(Uteis.getDataJDBC(dataIncial)).append("' AND '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		str.append(" and cap.campanha = ").append(campanhaColaboradorVO.getCampanha().getCodigo());
		str.append(" and cap.tipoSituacaoCompromissoEnum in ('").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("','").append(TipoSituacaoCompromissoEnum.PARALIZADO).append("') ");
		str.append(" and ap.pessoa = ").append(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		List<CompromissoAgendaPessoaHorarioVO> lista = new ArrayList<CompromissoAgendaPessoaHorarioVO>();
		while (rs.next()) {
			CompromissoAgendaPessoaHorarioVO obj = new CompromissoAgendaPessoaHorarioVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.getProspect().setCodigo(rs.getInt("prospect"));
			lista.add(obj);
		}
		return lista;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoCompromissoAoAlterarConsultor(ProspectsVO prospectsVO, UsuarioVO usuarioLogado) throws Exception {
		if (prospectsVO.getConsultorAlterado()) {
			
			if (prospectsVO.getFinalizarCompromissoParalizado()) {
				realizarFinalizacaoTodosCompromissosProspectOutroConsultor(prospectsVO, usuarioLogado);
			}
			if (prospectsVO.getAcaoCompromissoAguardandoExcecucao().equals("EXCLUIR")) {
				realizarExclusaoTodosCompromissosProspectOutroConsultorNaoIniciado(prospectsVO, false, usuarioLogado);
			} else if (prospectsVO.getAcaoCompromissoAguardandoExcecucao().equals("ALTERAR")) {
				realizarAlteracaoConsultorTodosCompromissosProspectOutroConsultorNaoIniciado(prospectsVO, usuarioLogado);
			}
			if (!prospectsVO.getAcaoCompromissoAguardandoExcecucao().equals("EXCLUIR") && prospectsVO.getExcluirCompromissoNaoIniciadoPassado()) {
				realizarExclusaoTodosCompromissosProspectOutroConsultorNaoIniciado(prospectsVO, true, usuarioLogado);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarFinalizacaoTodosCompromissosProspectOutroConsultor(ProspectsVO prospectsVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE compromissoagendapessoahorario  set tiposituacaocompromissoenum  = 'REALIZADO' ");
		sql.append(" FROM agendapessoahorario, agendapessoa ");
		sql.append(" WHERE agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
		sql.append(" and agendapessoa.codigo = agendapessoahorario.agendapessoa  ");
		sql.append(" and agendapessoa.pessoa <>  ").append(prospectsVO.getConsultorPadrao().getPessoa().getCodigo());
		sql.append(" and tiposituacaocompromissoenum = 'PARALIZADO' and prospect = ");
		sql.append(prospectsVO.getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarExclusaoTodosCompromissosProspectOutroConsultorNaoIniciado(ProspectsVO prospectsVO, Boolean excluirApenasPassado, UsuarioVO usuarioLogado) throws Exception {
		
		StringBuilder sql = new StringBuilder("update interacaoworkflow set compromissoagendapessoahorario =  null where compromissoagendapessoahorario in ( SELECT codigo FROM compromissoagendapessoahorario ");
		sql.append(" where tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' and prospect = ").append(prospectsVO.getCodigo());
		if(excluirApenasPassado){
			sql.append(" and agendapessoahorario in ( ");
			sql.append(" select codigo from agendaPessoaHorario where agendaPessoaHorario.codigo =  compromissoagendapessoahorario.agendaPessoaHorario ");
			sql.append(" and (agendapessoahorario.ano||'-'||agendapessoahorario.mes||'-'||agendapessoahorario.dia||' '||compromissoagendapessoahorario.hora)::TIMESTAMP <= current_timestamp ");
			sql.append(" ) ");
			
		}
		sql.append(" and agendapessoahorario not in (select agendapessoahorario.codigo  ");
		sql.append(" from agendapessoahorario inner join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa ");
		sql.append(" and agendapessoa.pessoa = ").append(prospectsVO.getConsultorPadrao().getPessoa().getCodigo()).append(") ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		
		getConexao().getJdbcTemplate().update(sql.toString());
		
		sql = new StringBuilder("DELETE FROM compromissoagendapessoahorario ");
		sql.append(" where tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' and prospect = ").append(prospectsVO.getCodigo());
		if(excluirApenasPassado){
			sql.append(" and agendapessoahorario in ( ");
			sql.append(" select codigo from agendaPessoaHorario where agendaPessoaHorario.codigo =  compromissoagendapessoahorario.agendaPessoaHorario ");
			sql.append(" and (agendapessoahorario.ano||'-'||agendapessoahorario.mes||'-'||agendapessoahorario.dia||' '||compromissoagendapessoahorario.hora)::TIMESTAMP <= current_timestamp ");
			sql.append(" ) ");
			
		}
		sql.append(" and agendapessoahorario not in (select agendapessoahorario.codigo  ");
		sql.append(" from agendapessoahorario inner join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa ");
		sql.append(" and agendapessoa.pessoa = ").append(prospectsVO.getConsultorPadrao().getPessoa().getCodigo()).append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoConsultorTodosCompromissosProspectOutroConsultorNaoIniciado(ProspectsVO prospectsVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder("select compromissoagendapessoahorario.* from compromissoagendapessoahorario ");
		sql.append(" inner join agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
		sql.append(" inner join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa and agendapessoa.pessoa <>  ").append(prospectsVO.getConsultorPadrao().getPessoa().getCodigo());
		sql.append(" where  tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' and prospect = ").append(prospectsVO.getCodigo());
		sql.append(" and (agendapessoahorario.ano||'-'||agendapessoahorario.mes||'-'||agendapessoahorario.dia||' '||compromissoagendapessoahorario.hora)::TIMESTAMP > current_timestamp ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<CompromissoAgendaPessoaHorarioVO> compromissoAgendaPessoaHorarioVOs = montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		AgendaPessoaVO agenda = getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(prospectsVO.getConsultorPadrao().getPessoa(), usuarioLogado);
		if (agenda.getCodigo() == 0) {
			agenda.setPessoa(prospectsVO.getConsultorPadrao().getPessoa());
			getFacadeFactory().getAgendaPessoaFacade().persistir(agenda, usuarioLogado);
		}
		List<Integer> compromissoExcluir = new ArrayList<Integer>(0);
		for (CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO : compromissoAgendaPessoaHorarioVOs) {
			compromissoExcluir.add(compromissoAgendaPessoaHorarioVO.getCodigo());
			compromissoAgendaPessoaHorarioVO.setAgendaPessoaHorario(null);
			compromissoAgendaPessoaHorarioVO.setCodigo(0);
			compromissoAgendaPessoaHorarioVO.setNovoObj(true);
			getFacadeFactory().getAgendaPessoaFacade().realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(compromissoAgendaPessoaHorarioVO, agenda, true, usuarioLogado);
		}
		if (!compromissoExcluir.isEmpty()) {
			sql = new StringBuilder("update interacaoworkflow set compromissoagendapessoahorario =  null where compromissoagendapessoahorario in (0");
			for (Integer codigo : compromissoExcluir) {
				sql.append(", ").append(codigo);
			}
			sql.append(") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
			getConexao().getJdbcTemplate().update(sql.toString());
			
			sql = new StringBuilder("DELETE FROM compromissoagendapessoahorario where codigo in (0");
			for (Integer codigo : compromissoExcluir) {
				sql.append(", ").append(codigo);
			}
			sql.append(") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
			getConexao().getJdbcTemplate().update(sql.toString());
		}
	}
	
	
	@Override
	 public boolean consultarExisteCompromissoCampanhaTipoCobranca(Integer codigoProspect) throws Exception{
	    	try {
	    		Integer quantidade = 0;
				StringBuilder sql = new StringBuilder();
				sql.append(" select count(compromissoagendapessoahorario.codigo) from compromissoagendapessoahorario ");
				sql.append(" inner join campanha on campanha.codigo = compromissoagendapessoahorario.campanha");
				sql.append(" where campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA'");
				sql.append(" AND compromissoagendapessoahorario.prospect = ");
				sql.append(codigoProspect);
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				while (tabelaResultado.next()) {
					quantidade = tabelaResultado.getInt("count");
				}
				if(quantidade >0){
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			} catch (Exception e) {
				throw e;
			}
	    }
		
	@Override
	public boolean verificarExisteAgendaNaoConcluidaProspectDentroPeriodoParaCobranca(Integer codigoPessoa, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT compromissoagendapessoahorario.codigo ");
		sqlStr.append("FROM compromissoagendapessoahorario ");
		sqlStr.append(" INNER JOIN prospects ON prospects.codigo = compromissoagendapessoahorario.prospect ");
		sqlStr.append(" LEFT JOIN campanha ON campanha.codigo = compromissoagendapessoahorario.campanha ");
		sqlStr.append("WHERE (prospects.pessoa = ").append(codigoPessoa).append(")");
		sqlStr.append("  AND (compromissoagendapessoahorario.datacompromisso between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFinal)).append("') ");
		sqlStr.append("  AND ((compromissoagendapessoahorario.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO') or (compromissoagendapessoahorario.tiposituacaocompromissoenum = 'PARALIZADO'))");
		sqlStr.append("  AND (campanha.tipocampanha = 'CONTACTAR_ALUNOS_COBRANCA') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
        public String atualizarCompromissoHoraInicial(CompromissoAgendaPessoaHorarioVO novoCompromisso, AgendaPessoaHorarioVO agendaPessoaHorario, String horaAvancada15min) throws Exception {
            String horarioInicialTentativaInsercao = horaAvancada15min;
            while (consultarHoraCompromissoAgendaPessoaHora(agendaPessoaHorario, horarioInicialTentativaInsercao, agendaPessoaHorario.getDia())) {
                horarioInicialTentativaInsercao = Uteis.obterHoraAvancada(horarioInicialTentativaInsercao, 1);
            }
            return horarioInicialTentativaInsercao;
        }        
        
        public void gerarCompromissoUrgente(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, AgendaPessoaVO agenda) throws Exception {
            AgendaPessoaHorarioVO agendaPessoaHorarioVO = new AgendaPessoaHorarioVO();
            agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(new Date()), Uteis.getMesData(new Date()), Uteis.getAnoData(new Date()), Uteis.getDiaSemanaEnum(new Date()), true);
            compromissoAgendaPessoaHorarioVO.setAgendaPessoaHorario(agendaPessoaHorarioVO);
            String horaAvancada15min = Uteis.getHoraMinutoComMascara(Uteis.getDataFutura(new Date(), Calendar.MINUTE, 15));
            gerarHoraValidaCompromisso(horaAvancada15min, compromissoAgendaPessoaHorarioVO, agendaPessoaHorarioVO, agenda);
        }

        public void gerarHoraValidaCompromisso(String hora, CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, AgendaPessoaHorarioVO agendaPessoaHorarioVO, AgendaPessoaVO agenda) throws Exception {
            if (!consultarHoraCompromissoAgendaPessoaHora(compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario(), hora, Uteis.getDiaMesData(new Date()))) {
                compromissoAgendaPessoaHorarioVO.setHora(hora);
            } else {
                compromissoAgendaPessoaHorarioVO.setHora(atualizarCompromissoHoraInicial(compromissoAgendaPessoaHorarioVO, compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario(), hora));
            }
            if (Integer.parseInt(hora.substring(0, 2)) >= 18) {
                Date data = Uteis.obterDataAvancadaPorDiaPorMesPorAno(agendaPessoaHorarioVO.getDia(), agendaPessoaHorarioVO.getMes(), agendaPessoaHorarioVO.getAno(), 1);
                agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(data), Uteis.getMesData(data), Uteis.getAnoData(data), Uteis.getDiaSemanaEnum(data), true);
                compromissoAgendaPessoaHorarioVO.setHora("08:00");
                compromissoAgendaPessoaHorarioVO.setAgendaPessoaHorario(agendaPessoaHorarioVO);
                gerarHoraValidaCompromisso(compromissoAgendaPessoaHorarioVO.getHora(), compromissoAgendaPessoaHorarioVO, agendaPessoaHorarioVO, agenda);
            }
            if (Integer.parseInt(hora.substring(0, 2)) >= 12 && Integer.parseInt(hora.substring(0, 2)) < 14) {
                compromissoAgendaPessoaHorarioVO.setHora("14:00");
                gerarHoraValidaCompromisso(compromissoAgendaPessoaHorarioVO.getHora(), compromissoAgendaPessoaHorarioVO, agendaPessoaHorarioVO, agenda);
            }
        }        
        
        public AgendaPessoaVO realizarValidacaoAgenda(PessoaVO pessoa, UsuarioVO usuario) throws Exception {
            AgendaPessoaVO agenda = getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(pessoa, usuario);
            if (agenda.getCodigo() == 0) {
                agenda.setPessoa(pessoa);
                getFacadeFactory().getAgendaPessoaFacade().persistir(agenda, usuario);
            }
            return agenda;
        }      
        
        @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
        public ProspectsVO realizarValidacaoProspect(Integer codigoPessoa, Integer codigoConsultor, Integer unidadeEnsino, InscricaoVO inscricaoVO, UsuarioVO usuario) throws Exception {
            ProspectsVO prospectVO = getFacadeFactory().getProspectsFacade().consultarPorCodigoPessoa(codigoPessoa, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            PessoaVO pessoa = new PessoaVO();
            pessoa.setCodigo(codigoPessoa);
            if (!Uteis.isAtributoPreenchido(prospectVO) && prospectVO.getFisico()) {
            	getFacadeFactory().getPessoaFacade().carregarDados(pessoa, NivelMontarDados.BASICO, usuario);
            	prospectVO.setCpf(pessoa.getCPF());
            	prospectVO = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(prospectVO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            }
            if (!Uteis.isAtributoPreenchido(prospectVO) && Uteis.isAtributoPreenchido(pessoa.getEmail())) {
            	prospectVO = getFacadeFactory().getProspectsFacade().consultarPorNomeEmailUnico(pessoa.getEmail(), prospectVO.getNome(), false, usuario);
            }
            if (Uteis.isAtributoPreenchido(prospectVO)) {
                // ATUALIZANDO DADOS DO PROSPECT COM RELAÇÃO À INSCRICAO NO PROC. SELETIVO
                if (prospectVO.getConsultorPadrao().getCodigo().equals(0)) {
                    prospectVO.getConsultorPadrao().setCodigo(codigoConsultor);
                    getFacadeFactory().getProspectsFacade().alterarConsultorProspect(prospectVO, usuario);
                }
                if (prospectVO.getUnidadeEnsino().getCodigo().equals(0)) {
                    // se o prospect já existe mas não está associado a nenhuma unidade de ensino,
                    // entao vamos associá-lo à unidade de ensino no qual o mesmo está se inscrevendo/vinculando
                    // que é passada como parametro para este método.
                    prospectVO.getUnidadeEnsino().setCodigo(unidadeEnsino);
                    getFacadeFactory().getProspectsFacade().alterarUnidadeEnsinoProspect(prospectVO, usuario);
                }
                CursoInteresseVO cursoInteresseVO = getFacadeFactory().getCursoInteresseFacade().consultarPorCodigoProspectCodigoCurso(prospectVO.getCodigo(), inscricaoVO.getCursoOpcao1().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
                if (cursoInteresseVO == null) {
                    cursoInteresseVO = new CursoInteresseVO();
                    cursoInteresseVO.setProspects(prospectVO);
                    cursoInteresseVO.setCurso(inscricaoVO.getCursoOpcao1().getCurso());
                    cursoInteresseVO.setTurno(inscricaoVO.getCursoOpcao1().getTurno());
                    getFacadeFactory().getCursoInteresseFacade().incluir(cursoInteresseVO, usuario);
                }
                return prospectVO;
            }
            PessoaVO pessoaTemp = new PessoaVO();
            pessoaTemp.setCodigo(codigoPessoa);
            getFacadeFactory().getPessoaFacade().carregarDados(pessoaTemp, NivelMontarDados.BASICO, usuario);
            
            ConfiguracaoGeralSistemaVO cfgVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, unidadeEnsino);
            getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(pessoaTemp.getCPF(), prospectVO, usuario);
            if (prospectVO.getUnidadeEnsino().getCodigo().equals(0)) {
                prospectVO.getUnidadeEnsino().setCodigo(unidadeEnsino);
            }
            prospectVO.getConsultorPadrao().setCodigo(codigoConsultor);
            prospectVO.setTipoOrigemCadastro(TipoOrigemCadastroProspectEnum.INSCRICAO_PROCSELETIVO);
            CursoInteresseVO cursoInteresseVO = new CursoInteresseVO();
            cursoInteresseVO.setCurso(inscricaoVO.getCursoOpcao1().getCurso());
            cursoInteresseVO.setTurno(inscricaoVO.getCursoOpcao1().getTurno());
            cursoInteresseVO.setProspects(prospectVO);
            prospectVO.getCursoInteresseVOs().add(cursoInteresseVO);
            getFacadeFactory().getProspectsFacade().incluirSemValidarDados(prospectVO, Boolean.FALSE, usuario, cfgVO);
            return prospectVO;
        }
        
	@Override
	public boolean verificarExisteAgendaNaoConcluidaProspectCampanhaEspecifica(Integer codigoPessoa, 
                    Integer codigoCampanha) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT compromissoagendapessoahorario.codigo ");
		sqlStr.append("FROM compromissoagendapessoahorario ");
		sqlStr.append(" INNER JOIN prospects ON prospects.codigo = compromissoagendapessoahorario.prospect ");
		sqlStr.append(" INNER JOIN pessoa ON prospects.pessoa = pessoa.codigo ");
		sqlStr.append(" INNER JOIN campanha ON campanha.codigo = compromissoagendapessoahorario.campanha ");
		sqlStr.append("WHERE (pessoa.codigo = ").append(codigoPessoa).append(")");
		sqlStr.append("  AND (campanha.codigo = ").append(codigoCampanha).append(") ");
		sqlStr.append("  AND ((compromissoagendapessoahorario.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO') or (compromissoagendapessoahorario.tiposituacaocompromissoenum = 'PARALIZADO'))");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
        
        public void gerarAgendaCampanhaCRMInscricaoProcessoSeletivo(Integer codigoProcessoSeletivo,
            InscricaoVO inscricaoVO, PoliticaGerarAgendaEnum politica, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        	CampanhaColaboradorVO campanhaColaboradorVO = getFacadeFactory().getCampanhaColaboradorFacade().consultarCampanhaAndResponsavelInscritoProcessoSeletivo(codigoProcessoSeletivo, inscricaoVO.getCursoOpcao1().getCurso().getCodigo(), politica, false, usuario);
            if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo().intValue() == 0) {
                return;
            }

            if ((inscricaoVO.getCandidato().getCodigo().equals(0)) ||
                (inscricaoVO.getCursoOpcao1().getCurso().getCodigo().equals(0)) ||
                (inscricaoVO.getUnidadeEnsino().getCodigo().equals(0))) {
                // estes são dados essenciais e portanto nao podem ficar sem serem
                // carregados para a sequencia do processamento. Assim, iremos 
                // carregar os dados da inscricao para esta situacao
                getFacadeFactory().getInscricaoFacade().carregarDados(inscricaoVO, NivelMontarDados.TODOS, usuario);
            }   
            
            if (verificarExisteAgendaNaoConcluidaProspectCampanhaEspecifica(inscricaoVO.getCandidato().getCodigo(), campanhaColaboradorVO.getCampanha().getCodigo())) {
                // Se já existe um compromisso NAO FINALIZADO gerado para falar com este prospect, entao
                // este método nao deve fazer nada. Pois ja este uma agenda que foi gerada e está sendo
                // acompanhada.
                return;
            }
            
            AgendaPessoaVO agendaPessoaVO = realizarValidacaoAgenda(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa(), usuario);
            CompromissoAgendaPessoaHorarioVO compromissoVO = new CompromissoAgendaPessoaHorarioVO();

            ProspectsVO prospectVO = realizarValidacaoProspect(inscricaoVO.getCandidato().getCodigo(), campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getCodigo(), inscricaoVO.getUnidadeEnsino().getCodigo(), inscricaoVO, usuario);
            compromissoVO.setProspect(prospectVO);
            compromissoVO.setCampanha(campanhaColaboradorVO.getCampanha());
            compromissoVO.setDataCadastro(new Date());
            if (politica.equals(PoliticaGerarAgendaEnum.GERAR_AO_LANCAR_RESULTADO_INSCRICAO)) {
                compromissoVO.setDescricao("Atendimento Referente ao Resultado Processo Seletivo - Inscrição: " + inscricaoVO.getCodigo() + "");
            } else {
                compromissoVO.setDescricao("Atendimento à Nova Inscrição: " + inscricaoVO.getCodigo() + " em Processo Seletivo");
            }
            compromissoVO.setOrigem(inscricaoVO.getCodigo().toString());
            compromissoVO.setTipoCompromisso(TipoCompromissoEnum.CONTATO);
            compromissoVO.setTipoContato(TipoContatoEnum.TELEFONE);
            compromissoVO.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
            compromissoVO.setUrgente(Boolean.TRUE);
            gerarCompromissoUrgente(compromissoVO, agendaPessoaVO);
            compromissoVO.setDataCompromisso(compromissoVO.getAgendaPessoaHorario().getDataCompromisso());
            compromissoVO.setDataInicialCompromisso(compromissoVO.getAgendaPessoaHorario().getDataCompromisso());
            getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(compromissoVO, usuario);
            alterarDataUltimoCompromissoAgendaCampanhaColaboradorCurso(campanhaColaboradorVO, compromissoVO.getDataCompromisso(), inscricaoVO.getCursoOpcao1().getCurso().getCodigo(), usuario);
        }
        
        public void alterarDataUltimoCompromissoAgendaCampanhaColaboradorCurso(CampanhaColaboradorVO campanhaColaboradorVO, final Date dataCompromisso, Integer curso, UsuarioVO usuarioVO) throws Exception {
        	campanhaColaboradorVO.setListaCampanhaColaboradorCursoVOs(getFacadeFactory().getCampanhaColaboradorCursoFacade().consultarCampanhaColaboradorCursos(campanhaColaboradorVO.getCodigo(), false, usuarioVO));
        	if (campanhaColaboradorVO.getListaCampanhaColaboradorCursoVOs().isEmpty()) {
        		return;
        	}
        	CampanhaColaboradorCursoVO campanhaColaboradorCursoVO = null;
        	for (CampanhaColaboradorCursoVO campanhaCursoVO : campanhaColaboradorVO.getListaCampanhaColaboradorCursoVOs()) {
        		if (campanhaCursoVO.getCursoVO().getCodigo().equals(curso)) {
        			campanhaColaboradorCursoVO = campanhaCursoVO;
        			break;
        		}
        	}
        	if (campanhaColaboradorCursoVO != null && !campanhaColaboradorCursoVO.getCodigo().equals(0)) {
        		final Integer codigo = campanhaColaboradorCursoVO.getCodigo();
        		final String sql = "UPDATE campanhaColaboradorCurso set dataUltimoCompromissoGerado = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
        			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
        				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
        				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(dataCompromisso));
        				sqlAlterar.setInt(2, codigo);
        				return sqlAlterar;
        			}
        		});
        		
        	}
        }
        
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarPreInscricaoCompromissoAgendaPessoaHorario(final Integer compromissoAgendaPessoaHorario, final Integer preInscricao, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE CompromissoAgendaPessoaHorario set preInscricao = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, preInscricao);
				sqlAlterar.setInt(2, compromissoAgendaPessoaHorario);
				return sqlAlterar;
			}
		});
	}
    
    public CompromissoAgendaPessoaHorarioVO gerarSimulacaoHoraCompromisso(CompromissoAgendaPessoaHorarioVO novoCompromisso, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, ProspectsVO prospect,  UsuarioVO usuario) throws Exception {
		novoCompromisso.setDescricao("Agendamento gerado pela campanha " + campanha.getDescricao());
		novoCompromisso.setTipoContato(TipoContatoEnum.TELEFONE);
		novoCompromisso.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
		novoCompromisso.setProspect(prospect);
		novoCompromisso.setAgendaPessoaHorario(agendaPessoaHorario);
		novoCompromisso.setCampanha(campanha);
//		if (novoCompromisso.getIsTipoCompromissoContato() && novoCompromisso.getEtapaWorkflowVO().getCodigo() == 0) {
//			novoCompromisso.setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorCodigoCampanhaEtapaInicial(campanha.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
//		}
		if(!Uteis.isAtributoPreenchido(novoCompromisso)){
			novoCompromisso.reagendarCompromissoParaDataFutura(novoCompromisso.getDataCompromisso(), usuario);
		}
		agendaPessoaHorario.getListaCompromissoAgendaPessoaHorarioVOs().add(novoCompromisso);
		return novoCompromisso;
	}
    

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CompromissoAgendaPessoaHorarioVO inicializarDadosCompromissoAgendaPorCompromissoCamapnhaPublicoAlvo(CompromissoCampanhaPublicoAlvoProspectVO novoCompromisso, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, ProspectsVO prospect,  UsuarioVO usuario) throws Exception {
		CompromissoAgendaPessoaHorarioVO obj = new CompromissoAgendaPessoaHorarioVO();
		obj.setDataCompromisso(novoCompromisso.getDataCompromisso());
		obj.setDataInicialCompromisso(novoCompromisso.getDataCompromisso());
		obj.setHora(novoCompromisso.getHora());
		obj.setDescricao("Agendamento gerado pela campanha " + campanha.getDescricao());
		obj.setTipoContato(TipoContatoEnum.TELEFONE);
		obj.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
		obj.setProspect(prospect);
		obj.setAgendaPessoaHorario(agendaPessoaHorario);
		obj.setCampanha(campanha);
		if (obj.getIsTipoCompromissoContato() && novoCompromisso.getEtapaWorkflowVO().getCodigo() == 0) {
			obj.setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorCodigoCampanhaEtapaInicial(campanha.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		if(!Uteis.isAtributoPreenchido(novoCompromisso)){
			obj.reagendarCompromissoParaDataFutura(novoCompromisso.getDataCompromisso(), usuario);
		}
		return obj;
	}
    
    @Override
    public TipoSituacaoCompromissoEnum consultarSituacaoAtualCompromissoPorProspectCampanha(Integer campanha, Integer prospect, Date dataCompromisso, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select distinct tiposituacaocompromissoenum AS situacaoAtualCompromisso from compromissoagendapessoahorario ");
    	sb.append(" where campanha = ").append(campanha);
    	sb.append(" and prospect = ").append(prospect) ;
    	sb.append(" and datacompromisso::date = '").append(Uteis.getDataJDBC(dataCompromisso)).append("' ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (tabelaResultado.next()) {
    		if (tabelaResultado.getString("situacaoAtualCompromisso") != null) {
    			return TipoSituacaoCompromissoEnum.valueOf(tabelaResultado.getString("situacaoAtualCompromisso"));
    		}
    	}
    	return null;
    }
    
    @Override
    public HashMap<Integer, List<CompromissoAgendaPessoaHorarioVO>> consultarQuantidadeCompromissoIniciouAgendaPorCampanhaProspect(Integer campanha, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, List<CampanhaColaboradorVO> campanhaColaboradorVOs, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select distinct compromissoagendapessoahorario.codigo, compromissoagendapessoahorario.prospect, agendapessoa.pessoa from compromissoagendapessoahorario  ");
    	sb.append(" inner join agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
    	sb.append(" inner join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa ") ;
    	sb.append(" where campanha = ").append(campanha);
    	boolean primeiraVez = true;
    	if (!campanhaColaboradorVOs.isEmpty()) {
    		sb.append(" and agendapessoa.pessoa in(");
    		for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaColaboradorVOs) {
    			if (primeiraVez) {
    				sb.append(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo());
    				primeiraVez = false;
    			} else {
    				sb.append(", ").append(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo()); 
    			}
    		}
    		sb.append(") ");
    	}
    	
    	boolean primeiraVezProspect = true;
    	if (!listaCampanhaPublicoAlvoProspectVOs.isEmpty()) {
    		sb.append(" and compromissoagendapessoahorario.prospect in(");
    		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectVOs) {
    			if (primeiraVezProspect) {
    				sb.append(campanhaPublicoAlvoProspectVO.getProspect().getCodigo());
    				primeiraVezProspect = false;
    			} else {
    				sb.append(", ").append(campanhaPublicoAlvoProspectVO.getProspect().getCodigo()); 
    			}
    		}
    		sb.append(") ");
    	}
    	
    	sb.append(" and tiposituacaocompromissoenum != 'AGUARDANDO_CONTATO'");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	HashMap<Integer, List<CompromissoAgendaPessoaHorarioVO>> mapQuantidadeProspectIniciouAgendaVOs = new HashMap<Integer, List<CompromissoAgendaPessoaHorarioVO>>(0);
    	while (tabelaResultado.next()) {
    		CompromissoAgendaPessoaHorarioVO obj = new CompromissoAgendaPessoaHorarioVO();
    		obj.setCodigo(tabelaResultado.getInt("codigo"));
    		obj.getProspect().setCodigo(tabelaResultado.getInt("prospect"));
    		obj.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(tabelaResultado.getInt("pessoa"));
    		
    		if (!mapQuantidadeProspectIniciouAgendaVOs.containsKey(tabelaResultado.getInt("pessoa"))) {
    			List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaPessoaHorarioVOs = new ArrayList<CompromissoAgendaPessoaHorarioVO>(0);
    			listaCompromissoAgendaPessoaHorarioVOs.add(obj);
    			mapQuantidadeProspectIniciouAgendaVOs.put(tabelaResultado.getInt("pessoa"), listaCompromissoAgendaPessoaHorarioVOs);
    		} else {
    			List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaPessoaHorarioVOs = mapQuantidadeProspectIniciouAgendaVOs.get(tabelaResultado.getInt("pessoa"));
    			listaCompromissoAgendaPessoaHorarioVOs.add(obj);
    		}
    	}
    	return mapQuantidadeProspectIniciouAgendaVOs;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCompromissoAgendaPessoaHorarioPorCampanhaProspect(Integer campanha, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, UsuarioVO usuarioVO) throws Exception {
    	List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectExcluirVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
    	for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectVOs) {
    		if ((campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)
    				|| campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA))
    				&& campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)) {
    			listaCampanhaPublicoAlvoProspectExcluirVOs.add(campanhaPublicoAlvoProspectVO);
    		}
    	}
    	if (listaCampanhaPublicoAlvoProspectExcluirVOs.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("delete from compromissoagendaPessoaHorario where campanha = ?");
		sb.append(" and prospect in(");
		boolean primeiraVez = true;
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectExcluirVOs) {
			if (primeiraVez) {
				sb.append(campanhaPublicoAlvoProspectVO.getProspect().getCodigo());
				primeiraVez = false;
			} else {
				sb.append(", ").append(campanhaPublicoAlvoProspectVO.getProspect().getCodigo());
			}
		}
		sb.append(") ");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sb.toString(), new Object[] { campanha });
	}

    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CompromissoAgendaPessoaHorarioVO realizarCriacaoCompromissoCampanhaLigacaoReceptivaRS(final ProspectsVO prospectVO, CampanhaVO campanhaVO, TipoCompromissoEnum tipoCompromisso, Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, String duvida, UsuarioVO usuario) throws Exception {
		CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO = new CompromissoAgendaPessoaHorarioVO();
		AgendaPessoaVO agenda = null;
		CampanhaColaboradorVO campanhaColaboradorVO = getFacadeFactory().getCampanhaColaboradorFacade().consultarColaboradorDeveRealizarProximaAgendaOrdenandoPelaDataDoUltimoCompromissoPorCampanha(campanhaVO.getCodigo(), false, usuario);
		if (campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().equals(0)) {
			return null;
		}
		agenda = realizarValidacaoAgenda(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa(), usuario);
		
		compromissoAgendaPessoaHorarioVO.setProspect(prospectVO);
		compromissoAgendaPessoaHorarioVO.setCampanha(campanhaVO);
		compromissoAgendaPessoaHorarioVO.setDataCadastro(new Date());
		if (tipoCompromisso.equals(TipoCompromissoEnum.TIRE_SUAS_DUVIDAS)) {
			compromissoAgendaPessoaHorarioVO.setDescricao("Agendamento Gerado pela Campanha Ligações Receptivas - Tire Suas Dúvidas");
		} else {
			compromissoAgendaPessoaHorarioVO.setDescricao("Agendamento Gerado pela Campanha Ligações Receptivas - Quero Ser Aluno");
		}
		compromissoAgendaPessoaHorarioVO.setObservacao("");
		compromissoAgendaPessoaHorarioVO.setOrigem("");
		compromissoAgendaPessoaHorarioVO.setTipoCompromisso(tipoCompromisso);
		compromissoAgendaPessoaHorarioVO.setTipoContato(TipoContatoEnum.TELEFONE);
		compromissoAgendaPessoaHorarioVO.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
		compromissoAgendaPessoaHorarioVO.setUrgente(Boolean.TRUE);
		gerarCompromissoUrgente(compromissoAgendaPessoaHorarioVO, agenda);
		compromissoAgendaPessoaHorarioVO.setDataCompromisso(compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario().getDataCompromisso());
		compromissoAgendaPessoaHorarioVO.setDataInicialCompromisso(compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario().getDataCompromisso());
		if (tipoCompromisso.equals(TipoCompromissoEnum.TIRE_SUAS_DUVIDAS)) {
			compromissoAgendaPessoaHorarioVO.setDuvida(duvida);
		}
		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(compromissoAgendaPessoaHorarioVO, usuario);
		return compromissoAgendaPessoaHorarioVO;
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCompromissoAgendaPessoaHorarioPorCampanhaProspectNaoInicializado(Integer campanha, Integer prospect, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "";
			StringBuilder sqlUpdate = new StringBuilder();
			sqlUpdate.append("update interacaoworkflow set CompromissoAgendaPessoaHorario = null where CompromissoAgendaPessoaHorario in (");
			sqlUpdate.append(" select codigo from compromissoagendapessoahorario where campanha = ").append(campanha);
			sqlUpdate.append(" and prospect = ").append(prospect).append(")");
			getConexao().getJdbcTemplate().update(sqlUpdate.toString());
			sql = " delete from compromissoagendaPessoaHorario where campanha = ? and prospect = ? AND ((compromissoagendapessoahorario.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO') or (compromissoagendapessoahorario.tiposituacaocompromissoenum = 'PARALIZADO'))   "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { campanha, prospect });
		} catch (Exception e) {
			throw e;
		}
	}
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoInteracaoWorkFlowCompromissoAgendaPessoaHorario(Integer campanha, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, UsuarioVO usuarioVO) throws Exception {
    	List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectExcluirVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
    	for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectVOs) {
    		if ((campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)
    				|| campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA))
    				&& campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)) {
    			listaCampanhaPublicoAlvoProspectExcluirVOs.add(campanhaPublicoAlvoProspectVO);
    		}
    	}
    	if (listaCampanhaPublicoAlvoProspectExcluirVOs.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("update interacaoworkflow set compromissoagendapessoahorario = null where campanha = ?");
		sb.append(" and prospect in(");
		boolean primeiraVez = true;
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectExcluirVOs) {
			if (primeiraVez) {
				sb.append(campanhaPublicoAlvoProspectVO.getProspect().getCodigo());
				primeiraVez = false;
			} else {
				sb.append(", ").append(campanhaPublicoAlvoProspectVO.getProspect().getCodigo());
			}
		}
		sb.append(") ");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sb.toString(), new Object[] { campanha });
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirReagendamento(final ReagendamentoCompromissoVO ReagendamentoCompromissoVO ,  UsuarioVO usuarioVO) throws Exception {
		try {
			
			String sql = "INSERT INTO reagendamentoCompromisso( dataModificacaoReagendamento , dataInicioCompromisso, dataReagendamentoCompromisso, compromissoAgendaPessoaHorario, agendaPessoaHorario, campanha, responsavelReagendamento) VALUES (?,?,?,?,?,?,?) returning codigo";
			ReagendamentoCompromissoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(ReagendamentoCompromissoVO.getDataModificacaoReagendamento()));
					sqlInserir.setDate(2, Uteis.getDataJDBC(ReagendamentoCompromissoVO.getDataInicioCompromisso()));
					sqlInserir.setDate(3, Uteis.getDataJDBC(ReagendamentoCompromissoVO.getDataReagendamentoCompromisso()));
					sqlInserir.setInt(4, ReagendamentoCompromissoVO.getCompromissoAgendaPessoaHorario());
					sqlInserir.setInt(5, ReagendamentoCompromissoVO.getAgendaPessoaHorario());
					sqlInserir.setInt(6, ReagendamentoCompromissoVO.getCampanha());
					sqlInserir.setString(7,ReagendamentoCompromissoVO.getResponsavelReagendamento());
					
					return sqlInserir;
				}
			}, new ResultSetExtractor() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						ReagendamentoCompromissoVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			ReagendamentoCompromissoVO.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			ReagendamentoCompromissoVO.setNovoObj(Boolean.TRUE);
			throw e;
		}
    }
    public void criarReagendamento(CompromissoAgendaPessoaHorarioVO compromissoExistente , UsuarioVO usuarioLogado ){
		 
		 ReagendamentoCompromissoVO reagendamentoCompromissoVO = new ReagendamentoCompromissoVO();
			
			reagendamentoCompromissoVO.setDataInicioCompromisso(compromissoExistente.getDataCompromissoAnterior());
			reagendamentoCompromissoVO.setDataReagendamentoCompromisso(compromissoExistente.getDataCompromisso());
			reagendamentoCompromissoVO.setDataModificacaoReagendamento(new Date());
			reagendamentoCompromissoVO.setCompromissoAgendaPessoaHorario(compromissoExistente.getCodigo());
			reagendamentoCompromissoVO.setAgendaPessoaHorario(compromissoExistente.getAgendaPessoaHorario().getCodigo());
			reagendamentoCompromissoVO.setCampanha(compromissoExistente.getCampanha().getCodigo());
			reagendamentoCompromissoVO.setResponsavelReagendamento(usuarioLogado.getPessoa().getNome());
			try {
				incluirReagendamento(reagendamentoCompromissoVO, usuarioLogado);
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
    
    public void validarDadosRemarcacaoAgenda(Date dataCompromissoAdiado ,String horaAdiado,String horaFimCompromissoAdiado, String horaIntevaloInicioCompromissoAdiado,String horaIntevaloFimCompromissoAdiado,Integer intervaloAgendaCompromisso) throws Exception{
    	
    	if (!Uteis.isAtributoPreenchido(dataCompromissoAdiado)) {
			throw new Exception("O Campo (Data compromisso) Deve Ser Informado.");
		}
		if (!Uteis.isAtributoPreenchido(horaAdiado)) {
			throw new Exception("O Campo (Hora Compromisso) Deve Ser Informado.");
		}
		if (!Uteis.isAtributoPreenchido(horaFimCompromissoAdiado)) {
			throw new Exception("O Campo (Hora Fim Compromisso) Deve Ser Informado.");
		}
		if (Uteis.isAtributoPreenchido(horaIntevaloInicioCompromissoAdiado) && !Uteis.isAtributoPreenchido(horaIntevaloFimCompromissoAdiado)) {
			throw new Exception("A Hora Fim Intervalo Deve Ser Informada.");
		}
		
		if (!Uteis.isAtributoPreenchido(horaIntevaloInicioCompromissoAdiado) && Uteis.isAtributoPreenchido(horaIntevaloFimCompromissoAdiado)) {
			throw new Exception("A Hora Inicio Intervalo Deve Ser Informada.");
		}
		
		if (!Uteis.verificarHoraValida(horaAdiado)) {
			throw new Exception("O Campo Hora Compromisso Invalido.");
		}
		if (!Uteis.verificarHoraValida(horaFimCompromissoAdiado)) {
			throw new Exception("O Campo Hora Fim Compromisso Invalido.");
		}
		
		if (!Uteis.verificarHoraInicialMenorFinal(horaAdiado, horaFimCompromissoAdiado)) {
			throw new Exception("A Hora Fim Não Pode Ser Menor Que Inicio.");
		}
		
		if (Uteis.isAtributoPreenchido(horaIntevaloInicioCompromissoAdiado) && Uteis.isAtributoPreenchido(horaIntevaloFimCompromissoAdiado)) {
			
			if (!Uteis.verificarHoraInicialMenorFinal(horaIntevaloInicioCompromissoAdiado, horaIntevaloFimCompromissoAdiado)) {
				throw new Exception("A Hora Fim Intervalo Não Pode Ser Menor Que Inicio Intervalo.");
			}
			
			if (!Uteis.verificarHoraInicialMenorFinal(horaAdiado, horaIntevaloInicioCompromissoAdiado) ||
					!Uteis.verificarHoraInicialMenorFinal(horaIntevaloFimCompromissoAdiado,horaFimCompromissoAdiado) ||
					!Uteis.verificarHoraInicialMenorFinal(horaIntevaloInicioCompromissoAdiado, horaFimCompromissoAdiado)
					) {
				throw new Exception("Intervalo Tem que estar dentro do Horario Agenda.");
			}
		
		}
    }
    
//    public void validarHorarioIntervaloeFinalCompromisso() {
//    	boolean reiniciouHorario = false;
//	
//    	if (campanhaColaborador.getNumeroAgendasGeradasParaData().compareTo(campanhaColaborador.getQtdContato()) >= 0) {
//			
//			// Se entrarmos aqui é por que atingimos o número máximos de
//			// contatos estabelecidos como meta
//			// para o dia de trabalho do consultor em questão. Então temos que
//			// avançar de dia, e zerar a contagem
//			// de prospects agendados para o dia, para que o controle seja
//			// iniciado novamente.
//			// Quando entramos aqui, a troca de dia é realizada não em
//			// consequencia de atingir o final do expediente
//			// mas sim em função da meta do dia de contatos já ter sido
//			// atingida.
//			Date proximoDiaGerarAgenda = Uteis.obterDataAvancada(campanhaColaborador.getDataGeracaoProximaAgenda(), 1);
//			campanhaColaborador.setDataGeracaoProximaAgenda(proximoDiaGerarAgenda);
//			agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(agenda.getCodigo(), proximoDiaGerarAgenda, Uteis.NIVELMONTARDADOS_COMBOBOX, true, usuario);
//			if (agendaPessoaHorario.getCodigo() == 0) {
//				agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(proximoDiaGerarAgenda), Uteis.getMesData(proximoDiaGerarAgenda), Uteis.getAnoData(proximoDiaGerarAgenda), Uteis.getDiaSemanaEnum(proximoDiaGerarAgenda), true);
//			}
//			campanhaColaborador.setHoraGeracaoProximaAgenda(campanhaColaborador.getHoraInicioGerarAgenda());
//			campanhaColaborador.setNumeroAgendasGeradasParaData(0);
//                        reiniciouHorario = true;
//		}
//
//		Integer tempoMedioCadaAgenda = campanha.getWorkflow().getTempoMedioGerarAgenda();
//		String proximoHorarioAgenda = "";
//                if (!reiniciouHorario) {
//                    if (!tempoMedioCadaAgenda.equals(0)) {
//                            proximoHorarioAgenda = Uteis.obterHoraAvancada(campanhaColaborador.getHoraGeracaoProximaAgenda(), campanha.getWorkflow().getTempoMedioGerarAgenda());
//                    } else {
//                            proximoHorarioAgenda = Uteis.obterHoraAvancada(campanhaColaborador.getHoraGeracaoProximaAgenda(), 1);
//                    }
//                } else {
//                    proximoHorarioAgenda = campanhaColaborador.getHoraInicioGerarAgenda();
//                }
//		if ((!campanhaColaborador.getHoraFimIntervalo().equals("")) && ((proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraFimIntervalo()) < 0))) {
//			// Se existe uma horario final para o intervalo e
//			// proximoHorarioAgenda é menor que o mesmo
//			// é por que ainda estamos no primeiro periodo de trabalho do
//			// consultor, entao temos que verificar
//			// se já atingimos o final do primeiro periodo. Caso sim, já
//			// assumimos o fim do intervalo como
//			// proximoHorarioAgenda
//			if (!campanhaColaborador.getHoraInicioIntervalo().equals("")) {
//				if (proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraInicioIntervalo()) >= 0) {
//					// Atingimos o horário do intervalor da pessoa, logo o
//					// proximo horário a ser gerada agenda
//					// é no retorno do intervalo.
//					proximoHorarioAgenda = campanhaColaborador.getHoraFimIntervalo();
//				}
//			}
//			campanhaColaborador.setHoraGeracaoProximaAgenda(proximoHorarioAgenda);
//		} else {
//			// Se entrarmos aqui é por que não existe intervalo programado ou já
//			// estamos gerando
//			// agenda para o segundo período de trabalho do consultor (parte da
//			// tarde por exemplo).
//			// Logo temos que avaliar se já atingimos o final do expediente.
//			// Caso sim, temos que
//			// ir para o próximo dia, pois não é possível mais gerar agenda no
//			// mesmo dia.
//			if ((proximoHorarioAgenda.compareTo(campanhaColaborador.getHoraFinalGerarAgenda()) >= 0)) {
//				// Avancar para o próximo dia
//				Date proximoDiaGerarAgenda = Uteis.obterDataAvancada(campanhaColaborador.getDataGeracaoProximaAgenda(), 1);
//				campanhaColaborador.setDataGeracaoProximaAgenda(proximoDiaGerarAgenda);
//				agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(agenda.getCodigo(), proximoDiaGerarAgenda, Uteis.NIVELMONTARDADOS_COMBOBOX, true, usuario);
//				if (agendaPessoaHorario.getCodigo() == 0) {
//					agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(proximoDiaGerarAgenda), Uteis.getMesData(proximoDiaGerarAgenda), Uteis.getAnoData(proximoDiaGerarAgenda), Uteis.getDiaSemanaEnum(proximoDiaGerarAgenda), true);
//				}
//				proximoHorarioAgenda = campanhaColaborador.getHoraInicioGerarAgenda();
//				campanhaColaborador.setNumeroAgendasGeradasParaData(0);
//			}
//			campanhaColaborador.setHoraGeracaoProximaAgenda(proximoHorarioAgenda);
//		}
//    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCancelamentoCompromissosNaoIniciacaoCampanha(Integer codCampanha, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.excluir(getIdEntidade(), false, usuario);
		StringBuilder str = new StringBuilder();
		str.append(" update compromissoagendapessoahorario set tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.CANCELADO).append("'");
		str.append(" where codigo in (  ");
		str.append(" select cap.codigo ");
		str.append(" FROM agendapessoahorario as aph ");
		str.append(" inner join compromissoagendapessoahorario as cap on cap.agendapessoahorario = aph.codigo ");
		str.append(" inner join agendapessoa AS ap on aph.agendapessoa = ap.codigo ");
		str.append(" inner join campanha on cap.campanha = campanha.codigo ");
		str.append(" where campanha.codigo= ").append(codCampanha);
		str.append(" and cap.tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
		str.append(" )");
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(str.toString());
	}
    
    public boolean verificarExistenciaCompromissoAgendaPessoaHorarioPorProspectCampanhaDataCompromissoHora(int prospect, int campanha, Date dataCompromisso, String hora) throws Exception {
		StringBuilder str = new StringBuilder("SELECT codigo FROM compromissoagendapessoahorario WHERE prospect = ").append(prospect)
				.append(" AND campanha = ").append(campanha)
				.append(" AND datacompromisso::date = '").append(Uteis.getDataJDBC(dataCompromisso)).append("'")
				.append(" AND hora = '").append(hora).append("' ");
		SqlRowSet sqlRowSet = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return sqlRowSet.next();
	}
}