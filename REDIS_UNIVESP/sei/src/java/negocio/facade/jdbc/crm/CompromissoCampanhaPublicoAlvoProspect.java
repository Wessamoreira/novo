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

import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoCampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoContatoEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.administrativo.CampanhaMidia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.CompromissoCampanhaPublicoAlvoProspectInterfaceFacade;

/**
 * 
 * @author edigarjr
 */
@Repository
@Scope("singleton")
@Lazy
public class CompromissoCampanhaPublicoAlvoProspect extends ControleAcesso implements CompromissoCampanhaPublicoAlvoProspectInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public CompromissoCampanhaPublicoAlvoProspect() throws Exception {
		super();
		setIdEntidade("CompromissoCampanhaPublicoAlvoProspect");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Primeiramente valida os
	 * dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoCampanhaPublicoAlvoProspectVO</code>
	 *            que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception {
		CompromissoCampanhaPublicoAlvoProspectVO.validarDados(obj);
		incluirSemValidarDados(obj, usuarioVO);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Primeiramente valida os
	 * dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoCampanhaPublicoAlvoProspectVO</code>
	 *            que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemValidarDados(final CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// CompromissoCampanhaPublicoAlvoProspect.incluir(getIdEntidade());
			realizarUpperCaseDados(obj);
			final String sql = "INSERT INTO CompromissoCampanhaPublicoAlvoProspect( descricao, hora, tipoCompromisso, prospect, campanha, observacao, origem, " + "urgente, dataCadastro, tipoContato, dataCompromisso, etapaworkflow, tipoSituacaoCompromissoEnum, horaFim, preInscricao, dataInicialCompromisso, historicoReagendamentoCompromisso ) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setString(2, Uteis.getFormatoHoraMinuto(obj.getHora()));
					sqlInserir.setString(3, obj.getTipoCompromisso().toString());
					if (obj.getProspect().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getProspect().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getCampanha().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getCampanha().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, obj.getObservacao());
					sqlInserir.setString(7, obj.getOrigem());
					sqlInserir.setBoolean(8, obj.isUrgente().booleanValue());
					sqlInserir.setTimestamp(9, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlInserir.setString(10, obj.getTipoContato().toString());
                    sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataCompromisso()));
					if (obj.getEtapaWorkflowVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getEtapaWorkflowVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setString(13, obj.getTipoSituacaoCompromissoEnum().toString());
					sqlInserir.setString(14, Uteis.getFormatoHoraMinuto(obj.getHoraFim()));
					if (obj.getPreInscricao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(15, obj.getPreInscricao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(15, 0);
					}
					// Registra a data de inicio do compromisso para que a mesma seja utilizada como
					// referencia para saber se um compromisso já foi reagendado ou não.
					sqlInserir.setTimestamp(16, Uteis.getDataJDBCTimestamp(obj.getDataInicialCompromisso()));
					sqlInserir.setString(17, obj.getHistoricoReagendamentoCompromisso());
					
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
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados ( <code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoCampanhaPublicoAlvoProspectVO</code>
	 *            que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception {
		CompromissoCampanhaPublicoAlvoProspectVO.validarDados(obj);
		alterarSemValidarDados(obj, usuarioVO);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados ( <code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoCampanhaPublicoAlvoProspectVO</code>
	 *            que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Override 
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSemValidarDados(final CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 30/10/2014
			  *  Classe Subordinada
			  */ 
			// CompromissoCampanhaPublicoAlvoProspect.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			final String sql = "UPDATE CompromissoCampanhaPublicoAlvoProspect set descricao=?, hora=?, tipoCompromisso=?, prospect=?, campanha=?, " 
					+ "observacao=?, origem=?, urgente=?, dataCompromisso=? , tipoContato=?, etapaworkflow=?, " 
					+ "tipoSituacaoCompromissoEnum=?, horaFim=?, preInscricao=?, historicoReagendamentoCompromisso=? WHERE ((codigo = ?)) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			if (!(Boolean) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, Uteis.getFormatoHoraMinuto(obj.getHora()));
					sqlAlterar.setString(3, obj.getTipoCompromisso().toString());
					if (obj.getProspect().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getProspect().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (obj.getCampanha().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getCampanha().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, obj.getObservacao());
					sqlAlterar.setString(7, obj.getOrigem());
					sqlAlterar.setBoolean(8, obj.isUrgente().booleanValue());
					sqlAlterar.setTimestamp(9, Uteis.getDataJDBCTimestamp(obj.getDataCompromisso()));
					sqlAlterar.setString(10, obj.getTipoContato().toString());
					if (obj.getEtapaWorkflowVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getEtapaWorkflowVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setString(12, obj.getTipoSituacaoCompromissoEnum().toString());
					sqlAlterar.setString(13, Uteis.getFormatoHoraMinuto(obj.getHoraFim()));

					if (obj.getPreInscricao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(14, obj.getPreInscricao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(14, 0);
					}
					sqlAlterar.setString(15, obj.getHistoricoReagendamentoCompromisso());
					
					sqlAlterar.setInt(16, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet arg0) throws SQLException, DataAccessException {
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
			final String sql = "UPDATE CompromissoCampanhaPublicoAlvoProspect set tipoSituacaoCompromissoEnum=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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
			final String sql = "UPDATE CompromissoCampanhaPublicoAlvoProspect set dataCompromisso=?, hora=?, agendapessoahorario = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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
			final String sql = "UPDATE CompromissoCampanhaPublicoAlvoProspect set prospect=? WHERE (prospect = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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
			final String sql = "UPDATE CompromissoCampanhaPublicoAlvoProspect set tipoSituacaoCompromissoEnum=?, etapaworkflow = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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
			final String sql = "UPDATE CompromissoCampanhaPublicoAlvoProspect set etapaworkflow = ?, tipoSituacaoCompromissoEnum = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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
			final String sql = "UPDATE CompromissoCampanhaPublicoAlvoProspect set tipoSituacaoCompromissoEnum = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Sempre localiza o registro
	 * a ser excluído através da chave primária da entidade. Primeiramente
	 * verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CompromissoCampanhaPublicoAlvoProspectVO</code>
	 *            que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "DELETE FROM CompromissoCampanhaPublicoAlvoProspect WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCompromissoCampanhaPublicoAlvoPorCampanha(Integer campanha, UsuarioVO usuarioLogado) throws Exception {
		try {
			CampanhaMidia.excluir(getIdEntidade());
			String sql = "DELETE FROM CompromissoCampanhaPublicoAlvoProspect WHERE ((Campanha= ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(sql, new Object[] { campanha });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCompromissoCampanhaPublicoAlvoPorCampanhaProspect(Integer campanha, Integer prospect, UsuarioVO usuarioVO) throws Exception {
		try {
			CampanhaMidia.excluir(getIdEntidade());
			if(Uteis.isAtributoPreenchido(campanha)) {
				String sql = "DELETE FROM CompromissoCampanhaPublicoAlvoProspect WHERE (Campanha= ? and prospect = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);			
				getConexao().getJdbcTemplate().update(sql, new Object[] { campanha, prospect });
			}else {
				String sql = "DELETE FROM CompromissoCampanhaPublicoAlvoProspect WHERE (prospect = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);			
				getConexao().getJdbcTemplate().update(sql, new Object[] { prospect });
			}
		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param CompromissoCampanhaPublicoAlvoProspectVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
	}	

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(CompromissoCampanhaPublicoAlvoProspectVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		obj.setDescricao(obj.getDescricao().toUpperCase());
		obj.setHora(obj.getHora().toUpperCase());
		obj.setObservacao(obj.getObservacao().toUpperCase());
		obj.setOrigem(obj.getOrigem().toUpperCase());
	}


	
	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>CompromissoCampanhaPublicoAlvoProspectVO</code> resultantes da
	 *         consulta.
	 */
	public static List<CompromissoCampanhaPublicoAlvoProspectVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<CompromissoCampanhaPublicoAlvoProspectVO> vetResultado = new ArrayList<CompromissoCampanhaPublicoAlvoProspectVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados ( <code>ResultSet</code>) em um objeto da classe
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>.
	 * 
	 * @return O objeto da classe <code>CompromissoCampanhaPublicoAlvoProspectVO</code>
	 *         com os dados devidamente montados.
	 *         
	 *    ************ AO ADICIONAR ALGUMA INFORMAÇÃO NESTE MONTAR DADOS DEVE SER ADICIONADO NO SQL DE CONSULTA POR CHAVE PRIMARIA ****************
	 */
	public static CompromissoCampanhaPublicoAlvoProspectVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		CompromissoCampanhaPublicoAlvoProspectVO obj = new CompromissoCampanhaPublicoAlvoProspectVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
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
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Faz uso da chave primária
	 * da classe <code>CampanhaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosEtapaWorkflow(CompromissoCampanhaPublicoAlvoProspectVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getEtapaWorkflowVO().getCodigo().intValue() == 0) {
			obj.setEtapaWorkflowVO(new EtapaWorkflowVO());
			return;
		}
		obj.setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorChavePrimaria(obj.getEtapaWorkflowVO().getCodigo(), nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CampanhaVO</code> relacionado ao objeto
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Faz uso da chave primária
	 * da classe <code>CampanhaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCampanha(CompromissoCampanhaPublicoAlvoProspectVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getCampanha().getCodigo().intValue() == 0) {
			obj.setCampanha(new CampanhaVO());
			return;
		}
		obj.setCampanha(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCampanha().getCodigo(), false, nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ProspectsVO</code> relacionado ao objeto
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code>. Faz uso da chave primária
	 * da classe <code>ProspectsVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosProspect(CompromissoCampanhaPublicoAlvoProspectVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getProspect().getCodigo().intValue() == 0) {
			obj.setProspect(new ProspectsVO());
			return;
		}
		obj.setProspect(getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(obj.getProspect().getCodigo(), nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>CompromissoCampanhaPublicoAlvoProspectVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	@Override 
	public CompromissoCampanhaPublicoAlvoProspectVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		StringBuilder sql = new StringBuilder(" SELECT * FROM CompromissoCampanhaPublicoAlvoProspect where codigo = ? ");		
		sql.append(" and not exists (select codigo from compromissoagendapessoahorario where compromissoagendapessoahorario.prospect = CompromissoCampanhaPublicoAlvoProspect.prospect ");
		sql.append(" and compromissoagendapessoahorario.campanha = CompromissoCampanhaPublicoAlvoProspect.campanha limit 1) ");		
		
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm});
		if (!tabelaResultado.next()) {
			/**
			 * Este tem a função de carregar os dados conforme ao ultimo compromisso deste prospect na campanha
			 */
			sql = new StringBuilder(" SELECT  CompromissoCampanhaPublicoAlvoProspect.codigo, ");
			sql.append(" compromissoagendapessoahorario.descricao, compromissoagendapessoahorario.hora, compromissoagendapessoahorario.tipoCompromisso, compromissoagendapessoahorario.prospect, ");
			sql.append(" compromissoagendapessoahorario.campanha, compromissoagendapessoahorario.observacao, compromissoagendapessoahorario.origem, ");
			sql.append(" compromissoagendapessoahorario.urgente, compromissoagendapessoahorario.dataCadastro, compromissoagendapessoahorario.tipoContato, compromissoagendapessoahorario.dataCompromisso, ");
			sql.append(" compromissoagendapessoahorario.etapaworkflow, compromissoagendapessoahorario.tipoSituacaoCompromissoEnum, ");
			sql.append(" compromissoagendapessoahorario.horaFim, compromissoagendapessoahorario.preInscricao, compromissoagendapessoahorario.dataInicialCompromisso, compromissoagendapessoahorario.historicoReagendamentoCompromisso, ");
			sql.append(" funcionario.codigo as consultorAtual, funcionario.pessoa as pessoaConsultor, pessoa.nome as consultorNome, compromissoagendapessoahorario.codigo as compromissoagendapessoahorario ");
			sql.append(" FROM CompromissoCampanhaPublicoAlvoProspect ");
			sql.append(" inner join  compromissoagendapessoahorario on compromissoagendapessoahorario.prospect = CompromissoCampanhaPublicoAlvoProspect.prospect ");
			sql.append(" and compromissoagendapessoahorario.campanha = CompromissoCampanhaPublicoAlvoProspect.campanha "); 
			sql.append(" and compromissoagendapessoahorario.codigo = (select caph.codigo from compromissoagendapessoahorario caph ");
			sql.append(" where caph.prospect = CompromissoCampanhaPublicoAlvoProspect.prospect and ");
			sql.append(" caph.campanha = CompromissoCampanhaPublicoAlvoProspect.campanha  order by case when caph.tiposituacaocompromissoenum in ('AGUARDANDO_CONTATO', 'PARALIZADO') then 0 else 1 end,  caph.datacompromisso desc, caph.hora desc, caph.codigo desc limit 1) ");
			sql.append(" inner join agendapessoahorario on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario ");
			sql.append(" inner join agendapessoa on agendapessoahorario.agendapessoa = agendapessoa.codigo ");
			sql.append(" inner join funcionario on funcionario.pessoa = agendapessoa.pessoa ");
			sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
			sql.append(" WHERE  CompromissoCampanhaPublicoAlvoProspect.codigo = ? "); 
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm});
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados.");
			}
			CompromissoCampanhaPublicoAlvoProspectVO compromissoCampanhaPublicoAlvoProspectVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			compromissoCampanhaPublicoAlvoProspectVO.getCompromissoAgendaPessoaHorarioVO().setCodigo(tabelaResultado.getInt("compromissoagendapessoahorario"));
			compromissoCampanhaPublicoAlvoProspectVO.getConsultorAtual().setCodigo(tabelaResultado.getInt("consultorAtual"));
			compromissoCampanhaPublicoAlvoProspectVO.getConsultorAtual().getPessoa().setCodigo(tabelaResultado.getInt("pessoaConsultor"));
			compromissoCampanhaPublicoAlvoProspectVO.getConsultorAtual().getPessoa().setNome(tabelaResultado.getString("consultorNome"));
			return compromissoCampanhaPublicoAlvoProspectVO;
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		
	}

	public List<CompromissoCampanhaPublicoAlvoProspectVO> consultarUnicidade(CompromissoCampanhaPublicoAlvoProspectVO obj, boolean alteracao, UsuarioVO usuarioLogado) throws Exception {
		super.verificarPermissaoConsultar(getIdEntidade(), false, usuarioLogado);
		return new ArrayList<CompromissoCampanhaPublicoAlvoProspectVO>(0);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CompromissoCampanhaPublicoAlvoProspect.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CompromissoCampanhaPublicoAlvoProspect.idEntidade = idEntidade;
	}

	 @Override
	    public CompromissoCampanhaPublicoAlvoProspectVO executarGeracaoCompromissoRotacionamentoSimulacao(CampanhaVO campanha, Date dataCompromisso, UsuarioVO usuario, ProspectsVO prospect, String horaGeracaoProximaAgenda) throws Exception {
		 	CompromissoCampanhaPublicoAlvoProspectVO novoCompromisso = new CompromissoCampanhaPublicoAlvoProspectVO();
			novoCompromisso.setDataCompromisso(dataCompromisso);
			novoCompromisso.setHora(horaGeracaoProximaAgenda);
			return gerarSimulacaoHoraCompromisso(novoCompromisso, campanha, prospect,usuario);
		}
	    
	    public CompromissoCampanhaPublicoAlvoProspectVO gerarSimulacaoHoraCompromisso(CompromissoCampanhaPublicoAlvoProspectVO novoCompromisso, CampanhaVO campanha, ProspectsVO prospect,  UsuarioVO usuario) throws Exception {
			novoCompromisso.setDescricao("Agendamento gerado pela campanha " + campanha.getDescricao());
			novoCompromisso.setTipoContato(TipoContatoEnum.TELEFONE);
			novoCompromisso.setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
			novoCompromisso.setProspect(prospect);
			novoCompromisso.setCampanha(campanha);
//			if (novoCompromisso.getIsTipoCompromissoContato() && novoCompromisso.getEtapaWorkflowVO().getCodigo() == 0) {
//				novoCompromisso.setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorCodigoCampanhaEtapaInicial(campanha.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
//			}
			if(!Uteis.isAtributoPreenchido(novoCompromisso)){
				novoCompromisso.reagendarCompromissoParaDataFutura(novoCompromisso.getDataCompromisso(), usuario);
			}

			return novoCompromisso;
		}
	    
	    
	    @Override
	    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void excluirCompromissoCampanhaPublicoAlvoProspectQuandoExcluidoCompromissoConsultor(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, UsuarioVO usuarioVO){
	    	if(Uteis.isAtributoPreenchido(compromissoAgendaPessoaHorarioVO.getCampanha().getCodigo()) && Uteis.isAtributoPreenchido(compromissoAgendaPessoaHorarioVO.getProspect().getCodigo())){
	    		 StringBuilder sql =  new StringBuilder("delete from compromissocampanhapublicoalvoprospect ");
	    		 sql.append(" where prospect = ").append(compromissoAgendaPessoaHorarioVO.getProspect().getCodigo());
	    		 sql.append(" and  campanha = ").append(compromissoAgendaPessoaHorarioVO.getCampanha().getCodigo());
	    		 sql.append(" and not exists ( ");
	    		 sql.append(" select compromissoAgendaPessoaHorario.codigo from compromissoAgendaPessoaHorario where compromissoAgendaPessoaHorario.prospect = ").append(compromissoAgendaPessoaHorarioVO.getProspect().getCodigo());
	    		 sql.append(" and compromissoAgendaPessoaHorario.campanha = ").append(compromissoAgendaPessoaHorarioVO.getCampanha().getCodigo());
	    		 sql.append(" and compromissoAgendaPessoaHorario.codigo != ").append(compromissoAgendaPessoaHorarioVO.getCodigo());
	    		 sql.append(" limit 1) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));	    		 
	    		 getConexao().getJdbcTemplate().update(sql.toString());
	    	} 
	    }
	    
	    @Override
	    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void excluirCompromissoCampanhaPublicoAlvoProspectQuandoExcluidoCompromissoConsultor(Integer prospect, TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum, UsuarioVO usuarioVO){
	    	if(Uteis.isAtributoPreenchido(prospect)){
	    	StringBuilder sql =  new StringBuilder("delete from compromissocampanhapublicoalvoprospect where codigo in ( ");
	    	sql.append(" select compromissocampanhapublicoalvoprospect.codigo from compromissocampanhapublicoalvoprospect ");
	    	sql.append(" inner join  compromissoagendapessoahorario on compromissoagendapessoahorario.prospect = CompromissoCampanhaPublicoAlvoProspect.prospect ");
			sql.append(" and compromissoagendapessoahorario.campanha = CompromissoCampanhaPublicoAlvoProspect.campanha "); 
			sql.append(" and compromissoagendapessoahorario.codigo = (select caph.codigo from compromissoagendapessoahorario caph ");
			sql.append(" where caph.prospect = CompromissoCampanhaPublicoAlvoProspect.prospect and ");
			sql.append(" caph.campanha = CompromissoCampanhaPublicoAlvoProspect.campanha  order by caph.datacompromisso desc, caph.codigo desc limit 1) ");
   		    sql.append(" where CompromissoCampanhaPublicoAlvoProspect.prospect = ").append(prospect);
   		    if(tipoSituacaoCompromissoEnum != null){
   		    	sql.append(" and compromissoagendapessoahorario.tiposituacaocompromissoenum = '").append(tipoSituacaoCompromissoEnum.name()).append("' ");  
   		    }
   		    sql.append(" and not exists ( ");
   		    sql.append(" select compromissoAgendaPessoaHorario.codigo from compromissoAgendaPessoaHorario caph where caph.prospect = ").append(prospect);
   		    sql.append(" and caph.campanha = compromissoagendapessoahorario.campanha ");
   		    sql.append(" and caph.codigo != compromissoagendapessoahorario.codigo ");
   		    sql.append(" limit 1) ");
   		    sql.append(" ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO)); 
   		    getConexao().getJdbcTemplate().update(sql.toString());
	    	}
	    }
	    
	    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void alterarProspectCompromissoCampanhaPublicoAlvoProspect(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception {
	        try {
	            final String sql = "UPDATE compromissocampanhapublicoalvoprospect set prospect=? WHERE ((prospect = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
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
		public void executarCancelamentoCompromissoCampanhaPublicoAlvoProspectNaoIniciacaoCampanha(Integer codCampanha,  UsuarioVO usuario) throws Exception {
			StringBuilder str = new StringBuilder();
			str.append(" update compromissoCampanhaPublicoAlvoProspect set tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.CANCELADO).append("'");
			str.append(" where campanha = ").append(codCampanha);
			str.append(" and tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
			str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(str.toString());
		}
	    
	    
}
