package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.RegistroHeaderLotePagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroHeaderLotePagarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegistroHeaderLotePagar extends ControleAcesso implements RegistroHeaderLotePagarInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3107789516572957384L;
	protected static String idEntidade;

	public RegistroHeaderLotePagar() throws Exception {
		super();
		setIdEntidade("RegistroHeaderLotePagar");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<RegistroHeaderLotePagarVO> lista, UsuarioVO usuarioVO) throws Exception {
		for (RegistroHeaderLotePagarVO obj : lista) {
			if (obj.getCodigo() == 0) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
			obj.getRegistroTrailerLotePagarVO().setRegistroHeaderLotePagarVO(obj);
			getFacadeFactory().getRegistroTrailerLotePagarFacade().persistir(obj.getRegistroTrailerLotePagarVO(), usuarioVO);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaRegistroDetalhePagarVO(), "RegistroDetalhePagar", "RegistroHeaderLotePagar", obj.getCodigo(), usuarioVO);
			getFacadeFactory().getRegistroDetalhePagarFacade().persistir(obj.getListaRegistroDetalhePagarVO(), usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final RegistroHeaderLotePagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			RegistroHeaderPagar.incluir(getIdEntidade());
			// RegistroHeaderPagarVO.validarDados(obj);
			final String sql = "INSERT INTO RegistroHeaderLotePagar(controlecobrancapagar, codigoBanco, loteServico, tipoRegistro, tipoOperacao, tipoServico, formaLancamento, numeroVersaoLote, tipoInscricaoEmpresa, numeroInscricaoEmpresa, codigoConvenioBanco,numeroAgencia,digitoAgencia,numeroConta,digitoConta,digitoAgenciaConta,nomeEmpresa ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					sqlInserir.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo().intValue());
					sqlInserir.setString(++i, obj.getCodigoBanco());
					sqlInserir.setString(++i, obj.getLoteServico());
					sqlInserir.setInt(++i, obj.getTipoRegistro().intValue());
					sqlInserir.setString(++i, obj.getTipoOperacao());
					sqlInserir.setString(++i, obj.getTipoServico());
					sqlInserir.setString(++i, obj.getFormaLancamento());
					sqlInserir.setString(++i, obj.getNumeroVersaoLote());
					sqlInserir.setInt(++i, obj.getTipoInscricaoEmpresa());
					sqlInserir.setLong(++i, obj.getNumeroInscricaoEmpresa());
					sqlInserir.setString(++i, obj.getCodigoConvenioBanco());
					sqlInserir.setInt(++i, obj.getNumeroAgencia());
					sqlInserir.setString(++i, obj.getDigitoAgencia());
					sqlInserir.setString(++i, obj.getNumeroConta());
					sqlInserir.setString(++i, obj.getDigitoConta());
					sqlInserir.setInt(++i, obj.getDigitoAgenciaConta());
					sqlInserir.setString(++i, obj.getNomeEmpresa());
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
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroHeaderPagarVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroHeaderPagarVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final RegistroHeaderLotePagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			RegistroHeaderPagar.alterar(getIdEntidade());
			// RegistroHeaderPagarVO.validarDados(obj);
			final String sql = "UPDATE RegistroHeaderLotePagar set controlecobrancapagar=?, codigoBanco=?, loteServico=?, tipoRegistro=?, tipoOperacao=?, tipoServico=?, formaLancamento=?, numeroVersaoLote=?, tipoInscricaoEmpresa=?, numeroInscricaoEmpresa=?, codigoConvenioBanco=?,numeroAgencia=?,digitoAgencia=?,numeroConta=?,digitoConta=?,digitoAgenciaConta=?,nomeEmpresa=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;

					sqlAlterar.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo().intValue());
					sqlAlterar.setString(++i, obj.getCodigoBanco());
					sqlAlterar.setString(++i, obj.getLoteServico());
					sqlAlterar.setInt(++i, obj.getTipoRegistro().intValue());
					sqlAlterar.setString(++i, obj.getTipoOperacao());
					sqlAlterar.setString(++i, obj.getTipoServico());
					sqlAlterar.setString(++i, obj.getFormaLancamento());
					sqlAlterar.setString(++i, obj.getNumeroVersaoLote());
					sqlAlterar.setInt(++i, obj.getTipoInscricaoEmpresa());
					sqlAlterar.setLong(++i, obj.getNumeroInscricaoEmpresa());
					sqlAlterar.setString(++i, obj.getCodigoConvenioBanco());
					sqlAlterar.setInt(++i, obj.getNumeroAgencia());
					sqlAlterar.setString(++i, obj.getDigitoAgencia());
					sqlAlterar.setString(++i, obj.getNumeroConta());
					sqlAlterar.setString(++i, obj.getDigitoConta());
					sqlAlterar.setInt(++i, obj.getDigitoAgenciaConta());
					sqlAlterar.setString(++i, obj.getNomeEmpresa());
					sqlAlterar.setInt(++i, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>RegistroHeaderPagarVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroHeaderPagarVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RegistroHeaderLotePagarVO obj, UsuarioVO usuario) throws Exception {
		try {
			RegistroHeaderPagar.excluir(getIdEntidade());
			String sql = "DELETE FROM RegistroHeaderLotePagar WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroHeaderPagar</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>RegistroHeaderPagarVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroHeaderLotePagar WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public void consultarPorControleCobrancaPagar(ControleCobrancaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM RegistroHeaderLotePagar WHERE controlecobrancapagar = " + obj.getCodigo() + " ORDER BY codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		while (rs.next()) {
			RegistroHeaderLotePagarVO rhlp = montarDados(rs, nivelMontarDados, usuario);
			obj.getListaRegistroHeaderLotePagarVO().add(rhlp);
		}
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>RegistroHeaderPagarVO</code> resultantes da consulta.
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
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>RegistroHeaderPagarVO</code>.
	 * 
	 * @return O objeto da classe <code>RegistroHeaderPagarVO</code> com os dados devidamente montados.
	 */
	public static RegistroHeaderLotePagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroHeaderLotePagarVO obj = new RegistroHeaderLotePagarVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setCodigoBanco((dadosSQL.getString("codigoBanco")));
		obj.setLoteServico((dadosSQL.getString("loteServico")));
		obj.setTipoRegistro(new Integer(dadosSQL.getInt("tipoRegistro")));
		obj.setTipoOperacao((dadosSQL.getString("tipoOperacao")));
		obj.setTipoServico(dadosSQL.getString("tipoServico"));
		obj.setFormaLancamento(dadosSQL.getString("formaLancamento"));
		obj.setNumeroVersaoLote((dadosSQL.getString("numeroVersaoLote")));
		obj.setTipoInscricaoEmpresa(new Integer(dadosSQL.getInt("tipoInscricaoEmpresa")));
		obj.setNumeroInscricaoEmpresa(dadosSQL.getLong("numeroInscricaoEmpresa"));
		obj.setCodigoConvenioBanco(dadosSQL.getString("codigoConvenioBanco"));
		obj.setNumeroAgencia(new Integer(dadosSQL.getInt("numeroAgencia")));
		obj.setDigitoAgencia(dadosSQL.getString("digitoAgencia"));
		obj.setNumeroConta(dadosSQL.getString("numeroConta"));
		obj.setDigitoConta(dadosSQL.getString("digitoConta"));
		obj.setDigitoAgenciaConta(new Integer(dadosSQL.getInt("digitoAgenciaConta")));
		obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
		obj.getControleCobrancaPagarVO().setCodigo(dadosSQL.getInt("controlecobrancapagar"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setListaRegistroDetalhePagarVO(getFacadeFactory().getRegistroDetalhePagarFacade().consultarPorRegistroHeaderLotePagar(obj.getCodigo(), false, nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			return obj;
		}
		obj.setRegistroTrailerLotePagarVO(getFacadeFactory().getRegistroTrailerLotePagarFacade().consultarPorRegistroHeaderLotePagar(obj.getCodigo(), nivelMontarDados, usuario));
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>RegistroHeaderPagarVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public RegistroHeaderLotePagarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM RegistroHeaderLotePagar WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return RegistroHeaderLotePagar.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		RegistroHeaderLotePagar.idEntidade = idEntidade;
	}

}
