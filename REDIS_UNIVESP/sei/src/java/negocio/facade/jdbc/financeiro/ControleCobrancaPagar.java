package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleCobrancaPagarInterfaceFacade;
import negocio.interfaces.financeiro.remessa.ControleRemessaContaPagarLayoutInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleCobrancaPagar extends ControleAcesso implements ControleCobrancaPagarInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6693463161640685747L;
	protected static String idEntidade;

	public ControleCobrancaPagar() throws Exception {
		super();
		setIdEntidade("ControleCobrancaPagar");
	}
	
	public ControleRemessaContaPagarLayoutInterfaceFacade getLayout(ControleCobrancaPagarVO controleCobrancaVO) throws Exception {
		ControleRemessaContaPagarLayoutInterfaceFacade layout = BancoFactory.getLayoutInstanciaControleRemessaContaPagar(controleCobrancaVO.getBancoVO().getNrBanco(), controleCobrancaVO.getContaCorrenteVO().getCnab());
		return layout;
	}


	@Override
	public void processarArquivo(ControleCobrancaPagarVO controleCobrancaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		getLayout(controleCobrancaVO).processarArquivoRetornoPagar(controleCobrancaVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO);
	}

	public void validarDados(ControleCobrancaPagarVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getBancoVO().getCodigo().intValue() == 0) {
			throw new ConsistirException("Um banco deve ser selecionado.");
		}
		if (obj.getArquivoRetornoContaPagar() == null) {
			throw new ConsistirException("Arquivo não selecionado, você precisa selecionar um Arquivo.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ControleCobrancaPagarVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		validarDados(obj);
		obj.getArquivoRetornoContaPagar().setValidarDados(false);
		getFacadeFactory().getArquivoFacade().persistir(obj.getArquivoRetornoContaPagar(), false, usuarioVO, configuracaoGeralSistemaVO);
		if (obj.getCodigo() == 0) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.ARQUIVO_PROCESSADO_CONTAS_NAO_BAIXADAS);
			obj.setDataProcessamento(new Date());
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		getFacadeFactory().getRegistroHeaderPagarFacade().persistir(obj.getRegistroHeaderPagarVO(), usuarioVO);
    	getFacadeFactory().getRegistroTrailerPagarFacade().persistir(obj.getRegistroTrailerPagarVO(), usuarioVO);
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaRegistroHeaderLotePagarVO(), "RegistroHeaderLotePagar", "controlecobrancapagar", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getRegistroHeaderLotePagarFacade().persistir(obj.getListaRegistroHeaderLotePagarVO(), usuarioVO);
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getContaPagarRegistroArquivoVOs(), "ContaPagarRegistroArquivo", "controlecobrancapagar", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getContaPagarRegistroArquivoFacade().persistir(obj.getContaPagarRegistroArquivoVOs(), usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ControleCobrancaPagarVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ControleCobranca.incluir(getIdEntidade(), verificarAcesso, usuario);
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ControleCobrancaPagar (responsavel,  banco, contaCorrente, arquivoRetornoContaPagar, dataProcessamento,  unidadeensino, ");
			sql.append(" situacaoProcessamento, qtdelote, loteatual, datainicioprocessamento, dataterminoprocessamento, motivoerroprocessamento ) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getResponsavel().getCodigo());
					sqlInserir.setInt(++i, obj.getBancoVO().getCodigo().intValue());
					Uteis.setValuePreparedStatement(obj.getContaCorrenteVO(), ++i, sqlInserir);
					sqlInserir.setInt(++i, obj.getArquivoRetornoContaPagar().getCodigo().intValue());
					if (obj.getDataProcessamento() != null) {
						sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo().intValue())) {
						sqlInserir.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getSituacaoProcessamento().name());
					sqlInserir.setInt(++i, obj.getQtdeLote());
					sqlInserir.setInt(++i, obj.getLoteAtual());
					if (obj.getDataInicioProcessamento() != null) {
						sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataInicioProcessamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getDataTerminoProcessamento() != null) {
						sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataTerminoProcessamento()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getMotivoErroProcessamento());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
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

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ControleCobrancaVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ControleCobrancaVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ControleCobrancaPagarVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			ControleCobrancaPagar.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ControleCobrancaPagar set responsavel=?,  banco=?, contaCorrente=?,  arquivo=?, nomearquivo=?, registroarquivo=?, dataProcessamento=?,   ");
			sql.append(" unidadeensino=?, situacaoProcessamento=?  ");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getResponsavel().getCodigo());
					sqlAlterar.setInt(++i, obj.getBancoVO().getCodigo().intValue());
					Uteis.setValuePreparedStatement(obj.getContaCorrenteVO(), ++i, sqlAlterar);
					sqlAlterar.setInt(++i, obj.getArquivoRetornoContaPagar().getCodigo().intValue());
					if (obj.getDataProcessamento() != null) {
						sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataProcessamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo().intValue())) {
						sqlAlterar.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getSituacaoProcessamento().name());
					sqlAlterar.setInt(++i, obj.getQtdeLote());
					sqlAlterar.setInt(++i, obj.getLoteAtual());
					if (obj.getDataInicioProcessamento() != null) {
						sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataInicioProcessamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (obj.getDataTerminoProcessamento() != null) {
						sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataTerminoProcessamento()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getMotivoErroProcessamento());
					sqlAlterar.setInt(10, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ControleCobrancaVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>ControleCobrancaVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ControleCobrancaPagarVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ControleCobranca.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM ControleCobrancaPagar WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			// getFacadeFactory().getRegistroArquivoFacade().excluir(obj.getRegistroArquivoVO(), false, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoContaCorrente(final Integer controleCobranca, final Integer contaCorrente, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaCorrente), "O campo conta corrente deve ser informado.");
		final String sql = "UPDATE ControleCobrancaPagar set contacorrente=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, contaCorrente);
				sqlAlterar.setInt(2, controleCobranca);
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroTerminoProcessamento(final Integer controleCobranca, final Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ControleCobrancaPagar set situacaoProcessamento=?, dataTerminoProcessamento=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
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
	public void realizarRegistroInicioProcessamento(final ControleCobrancaPagarVO obj,  UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE ControleCobrancaPagar set qtdeLote=?, situacaoProcessamento=?, dataInicioProcessamento=?, contaCorrente=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getQtdeLote());
					sqlAlterar.setString(2, SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO.name());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataInicioProcessamento()));
					sqlAlterar.setInt(4, obj.getContaCorrenteVO().getCodigo().intValue());
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void realizarAtualizacaoDadosProcessamento(ControleCobrancaPagarVO controleCobrancaVO) throws Exception {
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet("SELECT * FROM ControleCobrancaPagar WHERE codigo = ? ", controleCobrancaVO.getCodigo());
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
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroLoteEmProcessamento(final Integer controleCobranca, final Integer loteAtual, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ControleCobrancaPagar set loteAtual = ? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, loteAtual);
				sqlAlterar.setInt(2, controleCobranca);
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroErroProcessamento(final Integer controleCobranca, final String erro, final Date dataTerminoProcessamento, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE ControleCobrancaPagar set situacaoProcessamento=?, dataTerminoProcessamento=?, motivoErroProcessamento = ?  WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO.name());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(dataTerminoProcessamento));
				sqlAlterar.setString(3, erro);
				sqlAlterar.setInt(4, controleCobranca);
				return sqlAlterar;
			}
		});
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT distinct ccp.codigo as \"ccp.codigo\", ccp.responsavel as \"ccp.responsavel\", ");
		sql.append(" ccp.dataprocessamento as \"ccp.dataProcessamento\", ccp.situacaoProcessamento, ");
		sql.append(" ccp.qtdeLote, ccp.loteAtual, ccp.dataInicioProcessamento, ccp.dataTerminoProcessamento,  ");
		sql.append(" ccp.motivoErroProcessamento, ccp.banco as \"ccp.banco\", ccp.contaCorrente as \"ccp.contaCorrente\",  ");
		sql.append(" arcp.codigo as \"arcp.codigo\", arcp.nome as \"arcp.nome\", arcp.descricao as \"arcp.descricao\", ");
		sql.append(" u.nome as \"u.nome\", p.codigo as \"p.codigo\", p.nome as \"p.nome\", p.email as \"p.email\", p.email2 as \"p.email2\", ");
		sql.append(" ue.codigo as \"ue.codigo\", ue.nome as \"ue.nome\" ");
		sql.append(" FROM controlecobrancapagar ccp ");
		sql.append(" LEFT JOIN arquivo arcp ON arcp.codigo = ccp.arquivoRetornoContaPagar ");
		sql.append(" LEFT JOIN usuario u ON u.codigo = ccp.responsavel ");
		sql.append(" LEFT JOIN pessoa p ON p.codigo = u.pessoa ");
		sql.append(" LEFT JOIN unidadeensino ue ON ue.codigo = ccp.unidadeEnsino ");
		return sql;
	}

	@Override
	public List<ControleCobrancaPagarVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE ccp.codigo = ").append(valorConsulta).append(" ");
		if (unidadeEnsino != 0) {
			sql.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sql.append(" ORDER BY ccp.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<ControleCobrancaPagarVO> consultaRapidaPorDataProcessamento(Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE ccp.dataProcessamento >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND ccp.dataProcessamento <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY ccp.dataProcessamento desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ControleCobrancaVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	@Override
	public ControleCobrancaPagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM controlecobrancapagar WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( controlecobrancapagar ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	

	public List<ControleCobrancaPagarVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<ControleCobrancaPagarVO> vetResultado = new ArrayList<ControleCobrancaPagarVO>(0);
		while (tabelaResultado.next()) {
			ControleCobrancaPagarVO obj = new ControleCobrancaPagarVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(ControleCobrancaPagarVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("ccp.codigo"));
		obj.getBancoVO().setCodigo(dadosSQL.getInt("ccp.banco"));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("ccp.contaCorrente"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("ccp.responsavel"));
		obj.setDataProcessamento(dadosSQL.getTimestamp("ccp.dataProcessamento"));
		obj.setQtdeLote(dadosSQL.getInt("qtdeLote"));
		obj.setLoteAtual(dadosSQL.getInt("loteAtual"));
		obj.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
		obj.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
		obj.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
		if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
		}
		obj.getResponsavel().setNome(dadosSQL.getString("u.nome"));
		obj.getResponsavel().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
		obj.getResponsavel().getPessoa().setNome(dadosSQL.getString("p.nome"));
		obj.getResponsavel().getPessoa().setEmail(dadosSQL.getString("p.email"));
		obj.getResponsavel().getPessoa().setEmail2(dadosSQL.getString("p.email2"));
		
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("ue.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("ue.nome"));
		
		obj.getArquivoRetornoContaPagar().setCodigo(dadosSQL.getInt("arcp.codigo"));
		obj.getArquivoRetornoContaPagar().setNome(dadosSQL.getString("arcp.nome"));		
		obj.getArquivoRetornoContaPagar().setDescricao(dadosSQL.getString("arcp.descricao"));		
		obj.getArquivoRetornoContaPagar().setDescricaoAntesAlteracao(dadosSQL.getString("arcp.descricao"));		
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ControleCobrancaVO</code>.
	 *
	 * @return O objeto da classe <code>ControleCobrancaVO</code> com os dados devidamente montados.
	 */
	public static ControleCobrancaPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleCobrancaPagarVO obj = new ControleCobrancaPagarVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataProcessamento(dadosSQL.getTimestamp("dataProcessamento"));
		obj.setQtdeLote(dadosSQL.getInt("qtdeLote"));
		obj.setLoteAtual(dadosSQL.getInt("loteAtual"));
		obj.setDataInicioProcessamento(dadosSQL.getDate("dataInicioProcessamento"));
		obj.setDataTerminoProcessamento(dadosSQL.getDate("dataTerminoProcessamento"));
		obj.setMotivoErroProcessamento(dadosSQL.getString("motivoErroProcessamento"));
		obj.getArquivoRetornoContaPagar().setCodigo(dadosSQL.getInt("arquivoRetornoContaPagar"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		obj.getBancoVO().setCodigo(new Integer(dadosSQL.getInt("banco")));
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		if (dadosSQL.getString("situacaoProcessamento") != null && !dadosSQL.getString("situacaoProcessamento").trim().isEmpty()) {
			obj.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.valueOf(dadosSQL.getString("situacaoProcessamento")));
		}
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			obj.setContaPagarRegistroArquivoVOs(getFacadeFactory().getContaPagarRegistroArquivoFacade().consultarPorControleCobrancaPagar(obj.getCodigo(), false, nivelMontarDados, usuario));
			obj.setListaContaPagarHistorico(getFacadeFactory().getContaPagarHistoricoFacade().consultarPorCodigoControleCobrancaPagar(obj.getCodigo(), nivelMontarDados, usuario));
			montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosControleCobrancaPagarArquivoVO(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		getFacadeFactory().getRegistroHeaderLotePagarFacade().consultarPorControleCobrancaPagar(obj, nivelMontarDados, usuario);
		obj.setRegistroHeaderPagarVO(getFacadeFactory().getRegistroHeaderPagarFacade().consultarPorControleCobrancaPagar(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setRegistroTrailerPagarVO(getFacadeFactory().getRegistroTrailerPagarFacade().consultarPorControleCobrancaPagar(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosControleCobrancaPagarArquivoVO(obj, nivelMontarDados, usuario);	
		return obj;
	}
	
	public static void montarDadosResponsavel(ControleCobrancaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}
	
	public static void montarDadosBanco(ControleCobrancaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBancoVO().getCodigo().intValue() == 0) {
			obj.setBancoVO(new BancoVO());
			return;
		}
		obj.setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBancoVO().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	public static void montarDadosContaCorrente(ControleCobrancaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
			obj.setContaCorrenteVO(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosControleCobrancaPagarArquivoVO(ControleCobrancaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoRetornoContaPagar().getCodigo().intValue() == 0) {
			obj.setArquivoRetornoContaPagar(new ArquivoVO());
			return;
		}
		obj.setArquivoRetornoContaPagar(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoRetornoContaPagar().getCodigo(), nivelMontarDados, usuario));	
	}

	public static void montarDadosUnidadeEnsino(ControleCobrancaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void baixarContasPagarArquivoRetorno(ControleCobrancaPagarVO obj, List<ContaPagarRegistroArquivoVO> listaContaPagarRegistroDetalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		getLayout(obj).criarNegociacaoPagamentoBaixandoContasPagas(obj, listaContaPagarRegistroDetalhe, configuracaoGeralSistema, configuracaoFinanceiroVO, usuario);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ControleCobrancaPagar.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ControleCobrancaPagar.idEntidade = idEntidade;
	}

}
