package negocio.facade.jdbc.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.LayoutBancos;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleCobrancaInterfaceFacade;

import jobs.enumeradores.JobsEnum;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ControleCobrancaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ControleCobrancaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ControleCobrancaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleCobranca extends ControleAcesso implements ControleCobrancaInterfaceFacade {

	protected static String idEntidade;

	public ControleCobranca() throws Exception {
		super();
		setIdEntidade("ControleCobranca");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ControleCobrancaVO</code>.
	 */
	public ControleCobrancaVO novo() throws Exception {
		ControleCobranca.incluir(getIdEntidade());
		ControleCobrancaVO obj = new ControleCobrancaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ControleCobrancaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ControleCobrancaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ControleCobrancaVO obj, List<ContaReceberVO> contaReceberVOs, final RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ContaCorrenteVO contaCorrenteVO, Integer diasVariacaoDataVencimento) throws Exception {
		try {
			ControleCobrancaVO.validarDados(obj);
			ControleCobranca.incluir(getIdEntidade(), true, usuario);
			if (!obj.getMovimentacaoRemessaRetorno()) {
				baixarContas(obj, contaReceberVOs, arquivo, configuracaoFinanceiroVO, usuario, contaCorrenteVO, diasVariacaoDataVencimento);
			}
			getFacadeFactory().getRegistroArquivoFacade().incluir(arquivo, false, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO ControleCobranca( responsavel, tipoControle, banco, arquivo, nomearquivo, registroarquivo, dataProcessamento, unidadeensino, movimentacaoRemessaRetorno, situacaoProcessamento, contaCorrente, tipoCNAB, tipoCarteira ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getResponsavel().getCodigo());
					sqlInserir.setString(2, obj.getTipoControle());
					sqlInserir.setInt(3, obj.getBanco().intValue());
					sqlInserir.setBytes(4, obj.getArquivo());
					sqlInserir.setString(5, obj.getNomeArquivo());
					if (arquivo.getCodigo().intValue() == 0) {
						sqlInserir.setNull(6, 0);
					} else {
						sqlInserir.setInt(6, obj.getRegistroArquivoVO().getCodigo());
					}
					if (obj.getDataProcessamento() != null) {
						sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
						sqlInserir.setNull(8, 0);
					} else {
						sqlInserir.setInt(8, obj.getUnidadeEnsinoVO().getCodigo());
					}
					sqlInserir.setBoolean(9, obj.getMovimentacaoRemessaRetorno());
					sqlInserir.setString(10, obj.getSituacaoProcessamento().name());
					if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
						sqlInserir.setNull(11, 0);
					} else {
						sqlInserir.setInt(11, obj.getContaCorrenteVO().getCodigo());
					}
					sqlInserir.setString(12, obj.getTipoCNAB());
					sqlInserir.setString(13, obj.getTipoCarteira());
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
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemBaixarContas(final ControleCobrancaVO obj, final RegistroArquivoVO arquivo, UsuarioVO usuario) throws Exception {
		try {
			ControleCobrancaVO.validarDados(obj);
			ControleCobranca.incluir(getIdEntidade(), true, usuario);
			getFacadeFactory().getRegistroArquivoFacade().incluir(arquivo, false, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO ControleCobranca( responsavel, tipoControle, banco, arquivo, nomearquivo, registroarquivo, dataProcessamento, unidadeEnsino, movimentacaoRemessaRetorno, situacaoProcessamento, contaCorrente, tipocnab, tipocarteira ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getResponsavel().getCodigo());
					sqlInserir.setString(2, obj.getTipoControle());
					sqlInserir.setInt(3, obj.getBanco().intValue());
					sqlInserir.setBytes(4, obj.getArquivo());
					sqlInserir.setString(5, obj.getNomeArquivo());
					if (arquivo.getCodigo().intValue() == 0) {
						sqlInserir.setNull(6, 0);
					} else {
						sqlInserir.setInt(6, obj.getRegistroArquivoVO().getCodigo());
					}
					if (obj.getDataProcessamento() != null) {
						sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
						sqlInserir.setNull(8, 0);
					} else {
						sqlInserir.setInt(8, obj.getUnidadeEnsinoVO().getCodigo());
					}
					sqlInserir.setBoolean(9, obj.getMovimentacaoRemessaRetorno());
					sqlInserir.setString(10, obj.getSituacaoProcessamento().name());
					if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
						sqlInserir.setNull(11, 0);
					} else {
						sqlInserir.setInt(11, obj.getContaCorrenteVO().getCodigo());
					}
					sqlInserir.setString(12, obj.getTipoCNAB());
					sqlInserir.setString(13, obj.getTipoCarteira());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

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
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSemBaixarContas(final ControleCobrancaVO obj, final RegistroArquivoVO arquivo, UsuarioVO usuario) throws Exception {
		try {
			ControleCobrancaVO.validarDados(obj);
			ControleCobranca.incluir(getIdEntidade(), false, usuario);
			getFacadeFactory().getRegistroArquivoFacade().alterarSemBaixarContas(arquivo, false, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE ControleCobranca set responsavel=?, tipoControle=?, banco=?, arquivo=?, nomearquivo=?, dataProcessamento=?, unidadeEnsino=?, movimentacaoRemessaRetorno=?, registroarquivo = ?, contaCorrente=?, tipocnab=?, tipocarteira=?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getResponsavel().getCodigo());
					sqlAlterar.setString(2, obj.getTipoControle());
					sqlAlterar.setInt(3, obj.getBanco().intValue());
					sqlAlterar.setBytes(4, obj.getArquivo());
					sqlAlterar.setString(5, obj.getNomeArquivo());
					if (obj.getDataProcessamento() != null) {
						sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(7, 0);
					} else {
						sqlAlterar.setInt(7, obj.getUnidadeEnsinoVO().getCodigo());
					}
					sqlAlterar.setBoolean(8, obj.getMovimentacaoRemessaRetorno());
					if (obj.getRegistroArquivoVO().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(9, 0);
					} else {
						sqlAlterar.setInt(9, obj.getRegistroArquivoVO().getCodigo());
					}
					if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(10, 0);
					} else {
						sqlAlterar.setInt(10, obj.getContaCorrenteVO().getCodigo());
					}
					sqlAlterar.setString(11, obj.getTipoCNAB());
					sqlAlterar.setString(12, obj.getTipoCarteira());
					sqlAlterar.setInt(13, obj.getCodigo().intValue());
					return sqlAlterar;					
				}
			});
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ControleCobrancaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ControleCobrancaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ControleCobrancaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ControleCobrancaVO.validarDados(obj);
			ControleCobranca.alterar(getIdEntidade(), true, usuarioVO);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE ControleCobranca set responsavel=?, tipoControle=?, banco=?, arquivo=?, nomearquivo=?, dataProcessamento=?, unidadeEnsino=?, movimentacaoRemessaRetorno=?, registroarquivo = ?, contaCorrente=?, tipocnab=?, tipocarteira=?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getResponsavel().getCodigo());
					sqlAlterar.setString(2, obj.getTipoControle());
					sqlAlterar.setInt(3, obj.getBanco().intValue());
					sqlAlterar.setBytes(4, obj.getArquivo());
					sqlAlterar.setString(5, obj.getNomeArquivo());
					if (obj.getDataProcessamento() != null) {
						sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(7, 0);
					} else {
						sqlAlterar.setInt(7, obj.getUnidadeEnsinoVO().getCodigo());
					}
					sqlAlterar.setBoolean(8, obj.getMovimentacaoRemessaRetorno());
					if (obj.getRegistroArquivoVO().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(9, 0);
					} else {
						sqlAlterar.setInt(9, obj.getRegistroArquivoVO().getCodigo());
					}
					if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(10, 0);
					} else {
						sqlAlterar.setInt(10, obj.getContaCorrenteVO().getCodigo());
					}
					sqlAlterar.setString(11, obj.getTipoCNAB());
					sqlAlterar.setString(12, obj.getTipoCarteira());
					sqlAlterar.setInt(13, obj.getCodigo().intValue());
					return sqlAlterar;					
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ControleCobrancaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ControleCobrancaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ControleCobrancaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ControleCobranca.excluir(getIdEntidade(), true, usuarioVO);			
			String sql = "DELETE FROM ControleCobranca WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });			
			getFacadeFactory().getRegistroArquivoFacade().excluir(obj.getRegistroArquivoVO(), false, usuarioVO);
			getFacadeFactory().getRegistroExecucaoJobFacade().excluirRegistroExecucaoJobPorCodigoOrigem(JobsEnum.JOB_PROCESSAR_ARQUIVO_RETORNO, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void baixarContas(ControleCobrancaVO obj, List<ContaReceberVO> contaReceberVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ContaCorrenteVO contaCorrenteVO, Integer diasVariacaoDataVencimento) throws Exception {
		realizarCriacaorNegociacaoRecebimentoVOs(contaReceberVOs, arquivo, obj, configuracaoFinanceiroVO, contaCorrenteVO, diasVariacaoDataVencimento);
	}

	//@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void baixarContasRegistroArquivo(ControleCobrancaVO obj, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {

		realizarCriacaoNegociacaoRecebimentoVOs(contaReceberRegistroArquivoVOs, arquivo, obj, configuracaoFinanceiroVO, usuario);
	}

	// @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,
	// rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	// public void baixarContas(ControleCobrancaVO obj, List<ContaReceberVO>
	// contaReceberVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO
	// configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
	// obj.setResponsavel(usuario);
	// HashMap<String, String> hashAux = new HashMap<String, String>();
	// List<NegociacaoRecebimentoVO> negociacaoRecebimentoVOs =
	// criarNegociacaoRecebimentoVOs(contaReceberVOs, arquivo,
	// obj,configuracaoFinanceiroVO);
	// for (NegociacaoRecebimentoVO negociacaoRecebimentoVO :
	// negociacaoRecebimentoVOs) {
	// if
	// (!hashAux.containsKey(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber().getMatriculaAluno().getMatricula()))
	// {
	// hashAux.put(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber().getMatriculaAluno().getMatricula(),
	// "");
	// } else {
	// negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber().setControlarConcorrencia(Boolean.TRUE);
	// }
	// getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO,
	// configuracaoFinanceiroVO,usuario);
	// }
	// }
	//
	/**
	 * Responsável por realizar uma consulta de <code>ControleCobranca</code>
	 * através do valor do atributo <code>String tipoControle</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ControleCobrancaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoControle(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ControleCobranca WHERE upper( tipoControle ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoControle";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ControleCobranca</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ControleCobrancaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ControleCobranca WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ControleCobrancaVO> consultaRapidaPorCodigo(Boolean movimentacaoRemessaRetorno, Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE cc.codigo = ").append(valorConsulta).append(" ");
		sql.append(" AND cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		if (unidadeEnsino != 0) {
			sql.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sql.append(" ORDER BY cc.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<ControleCobrancaVO> consultaRapidaPorTipoControle(Boolean movimentacaoRemessaRetorno, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE upper( cc.tipoControle ) like('").append(valorConsulta.toUpperCase()).append("%') ");
		sqlStr.append(" AND cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY cc.tipoControle");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<ControleCobrancaVO> consultaRapidaPorDataProcessamento(Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE cc.dataProcessamento >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND cc.dataProcessamento <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		sqlStr.append(" AND cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY cc.dataProcessamento desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<ControleCobrancaVO> consultaRapidaPorContaCorrenteDataProcessamento(Integer contaCorrente, Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE cc.dataProcessamento >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND cc.dataProcessamento <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		sqlStr.append(" AND cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		sqlStr.append(" AND cc.contaCorrente = ").append(contaCorrente);
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY cc.dataProcessamento desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<ControleCobrancaVO> consultaRapidaPorNomeArquivo(Boolean movimentacaoRemessaRetorno, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE cc.nomearquivo ilike '").append(valorConsulta).append("%' ");
		sqlStr.append(" AND cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		sqlStr.append(" ORDER BY cc.dataProcessamento");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct cc.codigo as \"cc.codigo\", cc.banco as \"cc.banco\", cc.tipocontrole as \"cc.tipoControle\", ");
		sql.append("cc.responsavel as \"cc.responsavel\", cc.arquivo as \"cc.arquivo\", cc.nomearquivo as \"cc.nomeArquivo\", ");
		sql.append("cc.registroarquivo as \"cc.registroArquivo\", cc.dataprocessamento as \"cc.dataProcessamento\", cc.movimentacaoRemessaRetorno as movimentacaoRemessaRetorno, cc.tipocnab, cc.tipocarteira, ra.registrotrailer as \"ra.registroTrailer\", ");
		sql.append("ra.registroheader as \"ra.registroHeader\", ra.arquivoprocessado as \"ra.arquivoProcessado\", ra.contasbaixadas as \"ra.contasBaixadas\", ");
		sql.append("u.codigo as \"u.codigo\", u.nome as \"u.nome\", p.codigo as \"p.codigo\", p.nome as \"p.nome\", p.email as \"p.email\", p.email2 as \"p.email2\", ");
		sql.append("ue.codigo as \"ue.codigo\", ue.nome as \"ue.nome\", cc.situacaoProcessamento, cc.qtdeLote, cc.loteAtual, cc.dataInicioProcessamento, cc.dataTerminoProcessamento, cc.motivoErroProcessamento, ");
		sql.append("banco.codigo AS \"banco.codigo\", banco.nome AS \"banco.nome\", agencia.codigo AS \"agencia.codigo\", agencia.numeroAgencia AS \"agencia.numeroAgencia\", ");
		sql.append("agencia.digito AS \"agencia.digito\", contaCorrente.codigo AS \"contaCorrente.codigo\", contaCorrente.numero AS \"contaCorrente.numero\", ");
		sql.append("contaCorrente.digito AS \"contaCorrente.digito\", contaCorrente.carteira AS \"contaCorrente.carteira\", contaCorrente.nomeApresentacaoSistema AS \"contaCorrente.nomeApresentacaoSistema\" ");
		sql.append("FROM controlecobranca cc ");
		sql.append("LEFT JOIN registroarquivo ra ON ra.codigo = cc.registroarquivo ");
		sql.append("LEFT JOIN usuario u ON u.codigo = cc.responsavel ");
		sql.append("LEFT JOIN pessoa p ON p.codigo = u.pessoa ");
		sql.append("LEFT JOIN unidadeensino ue ON ue.codigo = cc.unidadeEnsino ");
		sql.append("LEFT JOIN contaCorrente on contaCorrente.codigo = cc.contaCorrente ");
		sql.append("LEFT JOIN agencia on agencia.codigo = contaCorrente.agencia ");
		sql.append("LEFT JOIN banco on banco.codigo = agencia.banco ");
		return sql;
	}

	public List<ControleCobrancaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<ControleCobrancaVO> vetResultado = new ArrayList<ControleCobrancaVO>(0);
		while (tabelaResultado.next()) {
			ControleCobrancaVO obj = new ControleCobrancaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(ControleCobrancaVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("cc.codigo"));
		obj.setBanco(dadosSQL.getInt("cc.banco"));
		obj.setTipoControle(dadosSQL.getString("cc.tipoControle"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("cc.responsavel"));
		obj.setArquivo((byte[]) dadosSQL.getObject("cc.arquivo"));
		obj.setNomeArquivo(dadosSQL.getString("cc.nomeArquivo"));
		obj.getRegistroArquivoVO().setCodigo(dadosSQL.getInt("cc.registroArquivo"));
		obj.setDataProcessamento(dadosSQL.getTimestamp("cc.dataProcessamento"));
		obj.setQtdeLote(dadosSQL.getInt("qtdeLote"));
		obj.setLoteAtual(dadosSQL.getInt("loteAtual"));
		obj.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
		obj.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
		obj.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
		if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
		}
		obj.setTipoCNAB(dadosSQL.getString("tipocnab"));
		obj.setTipoCarteira(dadosSQL.getString("tipocarteira"));
		obj.getRegistroArquivoVO().getRegistroTrailer().setCodigo(dadosSQL.getInt("ra.registroTrailer"));
		obj.getRegistroArquivoVO().getRegistroHeader().setCodigo(dadosSQL.getInt("ra.registroHeader"));
		obj.getRegistroArquivoVO().setArquivoProcessado(dadosSQL.getBoolean("ra.arquivoProcessado"));
		obj.getRegistroArquivoVO().setContasBaixadas(dadosSQL.getBoolean("ra.contasBaixadas"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("u.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("u.nome"));
		obj.getResponsavel().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
		obj.getResponsavel().getPessoa().setNome(dadosSQL.getString("p.nome"));
		obj.getResponsavel().getPessoa().setEmail(dadosSQL.getString("p.email"));
		obj.getResponsavel().getPessoa().setEmail2(dadosSQL.getString("p.email2"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("ue.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("ue.nome"));
		
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente.codigo"));
		obj.getContaCorrenteVO().setNumero(dadosSQL.getString("contaCorrente.numero"));
		obj.getContaCorrenteVO().setDigito(dadosSQL.getString("contaCorrente.digito"));
		obj.getContaCorrenteVO().setCarteira(dadosSQL.getString("contaCorrente.carteira"));
		obj.getContaCorrenteVO().setNomeApresentacaoSistema(dadosSQL.getString("contaCorrente.nomeApresentacaoSistema"));
		obj.getContaCorrenteVO().getAgencia().setCodigo(dadosSQL.getInt("agencia.codigo"));
		obj.getContaCorrenteVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agencia.numeroAgencia"));
		obj.getContaCorrenteVO().getAgencia().setDigito(dadosSQL.getString("agencia.digito"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("banco.codigo"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNome(dadosSQL.getString("banco.nome"));

		if (obj.getRegistroArquivoVO().getContasBaixadas()) {
			obj.getRegistroArquivoVO().setSituacao(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor());
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(obj.getNomeArquivo_Apresentar(), obj.getUnidadeEnsinoVO().getCodigo()), obj.getRegistroArquivoVO().getSituacao());
		} else if (obj.getRegistroArquivoVO().getArquivoProcessado()) {
			obj.getRegistroArquivoVO().setSituacao(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(obj.getNomeArquivo_Apresentar(), obj.getUnidadeEnsinoVO().getCodigo()))) {
				if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(obj.getNomeArquivo_Apresentar(), obj.getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor())) {
					obj.getRegistroArquivoVO().setSituacao(Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(obj.getNomeArquivo_Apresentar(), obj.getUnidadeEnsinoVO().getCodigo())));
				}
			} else {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(obj.getNomeArquivo_Apresentar(), obj.getUnidadeEnsinoVO().getCodigo()), obj.getRegistroArquivoVO().getSituacao());
			}
		}
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>ControleCobrancaVO</code> resultantes da consulta.
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
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ControleCobrancaVO</code>.
	 *
	 * @return O objeto da classe <code>ControleCobrancaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ControleCobrancaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleCobrancaVO obj = new ControleCobrancaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.setTipoControle(dadosSQL.getString("tipoControle"));
		obj.setBanco(new Integer(dadosSQL.getInt("banco")));
		obj.setArquivo((byte[]) dadosSQL.getObject("arquivo"));
		obj.setNomeArquivo(dadosSQL.getString("nomearquivo"));
		obj.getRegistroArquivoVO().setCodigo(dadosSQL.getInt("registroarquivo"));
		obj.setDataProcessamento(dadosSQL.getTimestamp("dataProcessamento"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setMovimentacaoRemessaRetorno(dadosSQL.getBoolean("movimentacaoRemessaRetorno"));
		obj.setQtdeLote(dadosSQL.getInt("qtdeLote"));
		obj.setLoteAtual(dadosSQL.getInt("loteAtual"));
		obj.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
		obj.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
		obj.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
		if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
		}
		obj.setTipoCNAB(dadosSQL.getString("tipocnab"));
		obj.setTipoCarteira(dadosSQL.getString("tipocarteira"));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	public static ControleCobrancaVO montarDadosCompleto(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleCobrancaVO obj = new ControleCobrancaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.setTipoControle(dadosSQL.getString("tipoControle"));
		obj.setBanco(new Integer(dadosSQL.getInt("banco")));
		obj.setArquivo((byte[]) dadosSQL.getObject("arquivo"));
		obj.setNomeArquivo(dadosSQL.getString("nomearquivo"));
		obj.getRegistroArquivoVO().setCodigo(dadosSQL.getInt("registroarquivo"));
		obj.setDataProcessamento(dadosSQL.getTimestamp("dataProcessamento"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setMovimentacaoRemessaRetorno(dadosSQL.getBoolean("movimentacaoRemessaRetorno"));
		obj.setQtdeLote(dadosSQL.getInt("qtdeLote"));
		obj.setLoteAtual(dadosSQL.getInt("loteAtual"));
		obj.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
		obj.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
		obj.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente"));
		if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
		}
		obj.setTipoCNAB(dadosSQL.getString("tipocnab"));
		obj.setTipoCarteira(dadosSQL.getString("tipocarteira"));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosRegistroArquivo(obj, nivelMontarDados, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>CensoVO</code>. Faz uso
	 * da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(ControleCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosRegistroArquivo(ControleCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRegistroArquivoVO().getCodigo().intValue() == 0) {
			return;
		}
		obj.setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorChavePrimariaCompleto(obj.getRegistroArquivoVO().getCodigo(), "", nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(ControleCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ControleCobrancaVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ControleCobrancaVO consultarBasicaPorCodigo(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sql =getSQLPadraoConsultaBasica();
		sql.append("where cc.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm);
		if (!tabelaResultado.next()) {
			return new ControleCobrancaVO();
		}
		ControleCobrancaVO obj = new ControleCobrancaVO();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}
	
	public ControleCobrancaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ControleCobranca WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ControleCobranca ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ControleCobrancaVO consultarPorChavePrimariaCompleto(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ControleCobranca WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return new ControleCobrancaVO();
		}
		return (montarDadosCompleto(tabelaResultado, nivelMontarDados, usuario));
	}

	public ControleCobrancaVO consultarPorNomeArquivo(String nomeArquivo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder("select cc.* from controlecobranca cc ");
		sql.append("inner join registroarquivo ra on ra.codigo = cc.registroarquivo ");
		sql.append("where upper(nomearquivo) like (?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { "%" + nomeArquivo.toUpperCase() + "%" });
		if (!tabelaResultado.next()) {
			return new ControleCobrancaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ControleCobrancaVO consultarPorNomeArquivoAnoProcessamento(String nomeArquivo, String anoProcessamento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder("select cc.* from controlecobranca cc ");
		sql.append("inner join registroarquivo ra on ra.codigo = cc.registroarquivo ");
		sql.append("where upper(nomearquivo) like (?) and (extract(year from cc.dataprocessamento)::character varying) = (?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { "%" + nomeArquivo.toUpperCase() + "%", anoProcessamento });
		if (!tabelaResultado.next()) {
			return new ControleCobrancaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ControleCobrancaVO consultarPorNomeArquivoAnoProcessamentoUnidadeEnsino(String nomeArquivo, String anoProcessamento, int unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		SqlRowSet tabelaResultado = null;
		StringBuilder sql = new StringBuilder("select cc.* from controlecobranca cc ");
		sql.append("inner join registroarquivo ra on ra.codigo = cc.registroarquivo ");
		sql.append("where upper(nomearquivo) like (?) and (extract(year from cc.dataprocessamento)::character varying) = (?) ");
		if (unidadeEnsino != 0) {
			sql.append("and cc.unidadeEnsino = ?");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { "%" + nomeArquivo.toUpperCase() + "%", anoProcessamento, unidadeEnsino });
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { "%" + nomeArquivo.toUpperCase() + "%", anoProcessamento });
		}
		if (!tabelaResultado.next()) {
			return new ControleCobrancaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ControleCobranca.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ControleCobranca.idEntidade = idEntidade;
	}

	public LayoutBancos getLayout(ControleCobrancaVO controleCobrancaVO) throws Exception {
		LayoutBancos layout = BancoFactory.getLayoutInstancia(controleCobrancaVO.getBanco(), controleCobrancaVO.getTipoCarteira());
		return layout;
	}

	public RegistroArquivoVO processarArquivo(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		ControleCobrancaVO.validarDados(controleCobrancaVO);
		RegistroArquivoVO registroArquivoVO = getLayout(controleCobrancaVO).processarArquivo(controleCobrancaVO, caminho + File.separator + controleCobrancaVO.getNomeArquivo(), configuracaoFinanceiroVO, usuarioVO);
		registroArquivoVO.setArquivoProcessado(false);
		controleCobrancaVO.setRegistroArquivoVO(registroArquivoVO);
		//		throw new Exception("Não foi encontrado a Conta Corrente no sistema de Número/Dígito: "+registroArquivoVO.getRegistroHeader().getNumeroConta().toString()+" / "+ registroArquivoVO.getRegistroHeader().getDigitoConta() +" Agência: " + registroArquivoVO.getRegistroHeader().getNumeroAgencia().toString()+". ");
		return registroArquivoVO;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarArquivoProgressBarVO(ControleCobrancaVO controleCobrancaVO, ProgressBarVO progressBarVO,  UsuarioVO usuarioVO) throws Exception {		
		getLayout(controleCobrancaVO).processarArquivoProgressBarVO(controleCobrancaVO, progressBarVO, usuarioVO);
		controleCobrancaVO.setDataProcessamento(new Date());
		controleCobrancaVO.getRegistroArquivoVO().setArquivoProcessado(true);
        alterarSemBaixarContas(controleCobrancaVO, controleCobrancaVO.getRegistroArquivoVO(), usuarioVO);
        if(getFacadeFactory().getRegistroExecucaoJobFacade().consultarSeExisterRegistroExecucaoJobPorCodigoOrigem(JobsEnum.JOB_PROCESSAR_ARQUIVO_RETORNO, controleCobrancaVO.getCodigo())) {
        	getFacadeFactory().getRegistroExecucaoJobFacade().atualizarCampoDataTerminoRegistroExecucaoJob(JobsEnum.JOB_PROCESSAR_ARQUIVO_RETORNO, controleCobrancaVO.getCodigo());
        }
   }
	
	

	public void estornarConta(ControleCobrancaVO controleCobrancaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		getLayout(controleCobrancaVO).estornarConta(controleCobrancaVO, caminho + File.separator + controleCobrancaVO.getNomeArquivo(), configuracaoFinanceiroVO, usuarioVO);
	}

	public List<ContaReceberVO> criarContaReceberVOs(RegistroArquivoVO registroArquivo, ControleCobrancaVO controleCobrancaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		return getLayout(controleCobrancaVO).listarContaReceberVOs(registroArquivo, configuracaoFinanceiroVO, controleCobrancaVO.getResponsavel());
	}

	// public List<NegociacaoRecebimentoVO>
	// criarNegociacaoRecebimentoVOs(List<ContaReceberVO> contaReceberVOs,
	// RegistroArquivoVO registroArquivo, ControleCobrancaVO controleCobrancaVO,
	// ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
	// if (!registroArquivo.isArquivoProcessado()) {
	// throw new ConsistirException("O Arquivo ainda não foi processado.");
	// }
	// return
	// getLayout(controleCobrancaVO).criarNegociacaoRecebimentoVOs(contaReceberVOs,
	// registroArquivo,configuracaoFinanceiroVO,
	// controleCobrancaVO.getResponsavel());
	// }
	public void realizarCriacaorNegociacaoRecebimentoVOs(List<ContaReceberVO> contaReceberVOs, RegistroArquivoVO registroArquivo, ControleCobrancaVO controleCobrancaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaCorrenteVO contaCorrenteVO, Integer diasVariacaoDataVencimento) throws Exception {
		if (!registroArquivo.getArquivoProcessado()) {
			throw new ConsistirException("O Arquivo ainda não foi processado.");
		}
		getLayout(controleCobrancaVO).criarNegociacaoRecebimentoVOs(contaReceberVOs, registroArquivo, configuracaoFinanceiroVO, controleCobrancaVO.getResponsavel(), contaCorrenteVO, diasVariacaoDataVencimento);
	}

	public void realizarCriacaoNegociacaoRecebimentoVOs(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, RegistroArquivoVO registroArquivo, ControleCobrancaVO controleCobrancaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if (!registroArquivo.getArquivoProcessado()) {
			throw new ConsistirException("O Arquivo ainda não foi processado.");
		}
		getLayout(controleCobrancaVO).criarNegociacaoRecebimentoVOsBaixandoContas(contaReceberRegistroArquivoVOs, registroArquivo, configuracaoFinanceiroVO, controleCobrancaVO, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirControleRemessaContaReceber(ContaReceberVO contaReceber, UsuarioVO usuario) throws Exception {
		ContaReceber.excluir(getIdEntidade());
//		String sql = "DELETE FROM ControleRemessaContaReceber WHERE ((contaReceber = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
//		getConexao().getJdbcTemplate().update(sql, new Object[] { contaReceber.getCodigo() });
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroLoteEmProcessamento(final Integer controleCobranca, final Integer loteAtual, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ControleCobranca set loteAtual = ? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, loteAtual);
				sqlAlterar.setInt(2, controleCobranca);
				return sqlAlterar;
			}
		});
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroErroProcessamento(final Integer controleCobranca, final SituacaoProcessamentoArquivoRetornoEnum situacao, final String erro, final Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ControleCobranca set situacaoProcessamento=?, dataTerminoProcessamento=?, motivoErroProcessamento = ?  WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, situacao.name());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(dataTerminoProcessamento));
				sqlAlterar.setString(3, erro);
				sqlAlterar.setInt(4, controleCobranca);
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroTerminoProcessamento(final Integer controleCobranca, final Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ControleCobranca set situacaoProcessamento=?, dataTerminoProcessamento=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_CONCLUIDO.name());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(dataTerminoProcessamento)); 
				sqlAlterar.setInt(3, controleCobranca);
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroProcessamentoInterrompido() throws Exception {
		final String sql = "UPDATE ControleCobranca set situacaoProcessamento=?, motivoErroProcessamento = ?  WHERE situacaoProcessamento = ? ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_INTERROMPIDO.name());				
				sqlAlterar.setString(2, "SERVIDOR REINICIADO");
				sqlAlterar.setString(3, SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO.name());
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroInicioProcessamento(final Integer controleCobranca, final Integer qtdeLote, final Date dataInicioProcessamento, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE ControleCobranca set qtdeLote=?, situacaoProcessamento=?, dataInicioProcessamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, qtdeLote);
					sqlAlterar.setString(2, SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO.name());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(dataInicioProcessamento));
					sqlAlterar.setInt(4, controleCobranca);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void realizarAtualizacaoDadosProcessamento(ControleCobrancaVO controleCobrancaVO) throws Exception {
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet("SELECT * FROM ControleCobranca WHERE codigo = ? ", controleCobrancaVO.getCodigo());
		if (dadosSQL.next()) {
			controleCobrancaVO.setQtdeLote(dadosSQL.getInt("qtdeLote"));
			controleCobrancaVO.setLoteAtual(dadosSQL.getInt("loteAtual"));
			controleCobrancaVO.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
			controleCobrancaVO.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
			controleCobrancaVO.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
			if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
				controleCobrancaVO.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
			}
		}
	}
	
	public static void montarDadosContaCorrente(ControleCobrancaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
			obj.setContaCorrenteVO(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	@Override
	public List<ControleCobrancaVO> consultaRapidaPorNossoNumero(String nossonumero, Boolean movimentacaoRemessaRetorno, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		
		sqlStr.append(" where cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		sqlStr.append(" AND exists (select codigo from contareceberregistroarquivo where nossonumero = ? and contareceberregistroarquivo.registroarquivo = ra.codigo) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY cc.dataProcessamento desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nossonumero);
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public List<ControleCobrancaVO> consultaRapidaPorSacado(String nomeSacado, Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal,  Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		
		sqlStr.append(" where cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		sqlStr.append(" and cc.dataProcessamento >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND cc.dataProcessamento <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		sqlStr.append(" AND exists (select contareceberregistroarquivo.codigo from contareceberregistroarquivo ");
		sqlStr.append(" inner join contareceber on contareceber.codigo = contareceberregistroarquivo.contareceber ");		
		sqlStr.append(" left join pessoa on contareceber.pessoa = pessoa.codigo ");
		sqlStr.append(" left join pessoa as responsavelfinanceiro on contareceber.responsavelfinanceiro = responsavelfinanceiro.codigo ");
		sqlStr.append(" left join convenio on contareceber.convenio = convenio.codigo ");		
		sqlStr.append(" left join fornecedor on contareceber.fornecedor = fornecedor.codigo ");
		sqlStr.append(" left join parceiro on contareceber.parceiro = parceiro.codigo ");
				
		sqlStr.append(" where contareceberregistroarquivo.registroarquivo = ra.codigo and case contareceber.tipoPessoa ");
		sqlStr.append(" when 'AL' then (sem_acentos ( upper (pessoa.nome)) LIKE sem_acentos ( upper ( ? ))) ");
		sqlStr.append(" when 'RF' then (sem_acentos ( upper (responsavelfinanceiro.nome)) LIKE sem_acentos ( upper ( ? ))) or (sem_acentos ( upper (pessoa.nome)) LIKE sem_acentos ( upper ( ? ))) ");
		sqlStr.append(" when 'FU' then (sem_acentos ( upper (pessoa.nome)) LIKE sem_acentos ( upper ( ?)))  ");
		sqlStr.append(" when 'CA' then (sem_acentos ( upper (pessoa.nome)) LIKE sem_acentos ( upper ( ?)))  ");
		sqlStr.append(" when 'CO' then (sem_acentos ( upper (convenio.descricao)) LIKE sem_acentos ( upper ( ?)))  ");
		sqlStr.append(" when 'FO' then (sem_acentos ( upper (fornecedor.nome)) LIKE sem_acentos ( upper (?)))  ");
		sqlStr.append(" when 'PA' then (sem_acentos ( upper (parceiro.nome)) LIKE sem_acentos ( upper ( ?)))  or (sem_acentos ( upper (pessoa.nome)) LIKE sem_acentos ( upper (?)))  ");
		sqlStr.append(" else (sem_acentos ( upper (pessoa.nome)) LIKE sem_acentos ( upper ( ?))) end ");

		sqlStr.append(" ) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY cc.dataProcessamento desc ");
		nomeSacado = "%"+nomeSacado+"%";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeSacado, nomeSacado, nomeSacado, nomeSacado, nomeSacado, nomeSacado, nomeSacado, nomeSacado, nomeSacado, nomeSacado);
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public List<ControleCobrancaVO> consultaRapidaPorMatricula(String matricula, Boolean movimentacaoRemessaRetorno, Date dataInicial, Date dataFinal,  Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		
		sqlStr.append(" where cc.movimentacaoRemessaRetorno = ").append(movimentacaoRemessaRetorno);
		sqlStr.append(" and cc.dataProcessamento >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND cc.dataProcessamento <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		sqlStr.append(" AND exists (select contareceberregistroarquivo.codigo from contareceberregistroarquivo ");
		sqlStr.append(" inner join contareceber on contareceber.codigo = contareceberregistroarquivo.contareceber ");		
		sqlStr.append(" left join pessoa on contareceber.pessoa = pessoa.codigo ");
		sqlStr.append(" left join pessoa as responsavelfinanceiro on contareceber.responsavelfinanceiro = responsavelfinanceiro.codigo ");
		sqlStr.append(" left join convenio on contareceber.convenio = convenio.codigo ");		
		sqlStr.append(" left join fornecedor on contareceber.fornecedor = fornecedor.codigo ");
		sqlStr.append(" left join parceiro on contareceber.parceiro = parceiro.codigo ");			
		sqlStr.append(" where sem_acentos(contareceber.matriculaaluno) ilike sem_acentos(?) ");
		sqlStr.append(" and contareceberregistroarquivo.registroarquivo = ra.codigo) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY cc.dataProcessamento desc ");
		matricula = "%"+matricula+"%";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	
	
	
	@Override
	public File realizarGeracaoRelatorioExcel(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoDuplicidadeVOs, String urlLogoPadraoRelatorio) throws Exception {
		File arquivo = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Arquivo de Retorno");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		uteisExcel.realizarGeracaoTopoPadraoRelatorio(workbook, sheet, urlLogoPadraoRelatorio, null , 10, "");
		montarCabecalhoRelatorioExcel(uteisExcel, workbook, sheet);
		List<ContaReceberRegistroArquivoVO> listaTemp = new ArrayList<>();
		listaTemp.addAll(contaReceberRegistroArquivoVOs);
		montarItensRelatorioExcel(uteisExcel, workbook, sheet, listaTemp);
		montarTotalizadoresRelatorioExcel(uteisExcel, sheet, listaTemp);
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + String.valueOf(new Date().getTime())+".xls");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}
	
	public void montarCabecalhoRelatorioExcel(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet) {
		int cellnum = 0;
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000, "Observação");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Nr. Documento");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Nosso Numero");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Pessoa");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Turma");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3500,"Valor");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3500,"Valor Recebido");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3500,"Desconto");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3500,"Acréscimo");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3500,"Multa");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3500,"Juros");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3000,"Data Vcto");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3000,"Data Crédito");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 3000,"Situação");
	}
	
	public void montarItensRelatorioExcel(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet, List<ContaReceberRegistroArquivoVO> listaTemp) {
		Row row = null;
		for (ContaReceberRegistroArquivoVO obj :  listaTemp) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			int cellnum = 0;
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getObservacao());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getNrDocumento());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getNossoNumero());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getPessoa().getNome());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getTurma().getIdentificadorTurma());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getValor());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getValorRecebido());
			uteisExcel.preencherCelula( row, cellnum++, obj.getValorDesconto());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getAcrescimo());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getMulta());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getJuro());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getDataVencimento_Apresentar());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getDataCredito_Apresentar());
			uteisExcel.preencherCelula( row, cellnum++, obj.getContaReceberVO().getSituacao_Apresentar());
		}
	}

	private void montarTotalizadoresRelatorioExcel(UteisExcel uteisExcel, HSSFSheet sheet, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs) {
		if (Uteis.isAtributoPreenchido(contaReceberRegistroArquivoVOs)) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			uteisExcel.preencherCelula(row, 0, "TOTAL");
			uteisExcel.preencherCelula(row, 1, 5, "");
			uteisExcel.preencherCelula(row, 5, "R$" + Uteis.getDoubleFormatado(contaReceberRegistroArquivoVOs.stream().map(ContaReceberRegistroArquivoVO::getContaReceberVO).map(ContaReceberVO::getValor).reduce(0D, Double::sum)));
			uteisExcel.preencherCelula(row, 6, "R$" + Uteis.getDoubleFormatado(contaReceberRegistroArquivoVOs.stream().map(ContaReceberRegistroArquivoVO::getContaReceberVO).map(ContaReceberVO::getValorRecebido).reduce(0D, Double::sum)));
			uteisExcel.preencherCelula(row, 7, "R$" + Uteis.getDoubleFormatado(contaReceberRegistroArquivoVOs.stream().map(ContaReceberRegistroArquivoVO::getValorDesconto).reduce(0D, Double::sum)));
			uteisExcel.preencherCelula(row, 8, "R$" + Uteis.getDoubleFormatado(contaReceberRegistroArquivoVOs.stream().map(ContaReceberRegistroArquivoVO::getContaReceberVO).map(ContaReceberVO::getAcrescimo).reduce(0D, Double::sum)));
			uteisExcel.preencherCelula(row, 9, "R$" + Uteis.getDoubleFormatado(contaReceberRegistroArquivoVOs.stream().map(ContaReceberRegistroArquivoVO::getContaReceberVO).map(ContaReceberVO::getMulta).reduce(0D, Double::sum)));
			uteisExcel.preencherCelula(row, 10, "R$" + Uteis.getDoubleFormatado(contaReceberRegistroArquivoVOs.stream().map(ContaReceberRegistroArquivoVO::getContaReceberVO).map(ContaReceberVO::getJuro).reduce(0D, Double::sum)));
			uteisExcel.preencherCelula(row, 11, 13, "");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarContaCorrenteControleCobranca(final Integer controleCobranca, final Integer codigoContaCorrente, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ControleCobranca set contaCorrente=?  WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, codigoContaCorrente);
				sqlAlterar.setInt(2, controleCobranca);
				return sqlAlterar;
			}
		});
	}
}
