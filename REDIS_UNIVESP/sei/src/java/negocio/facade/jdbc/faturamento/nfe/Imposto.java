package negocio.facade.jdbc.faturamento.nfe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.ImpostoInterfaceFacade;

/**
 *
 * @author Pedro
 */
@Repository
@Scope("singleton")
@Lazy
public class Imposto extends ControleAcesso implements ImpostoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7916150146525207178L;
	protected static String idEntidade = "Imposto";

	public Imposto() {
		super();
	}

	public void validarDados(ImpostoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), "O campo Nome (Imposto) não foi informado.");
		Uteis.checkState(validarUnicidade(obj), "Já existe um imposto cadastrado com esse nome: " + obj.getNome());
		obj.setNome(obj.getNome().trim());// retirar espado do campo nome
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ImpostoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			Imposto.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO Imposto (nome, utilizarFolhaPagamento) ");
			sql.append("    VALUES (?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setBoolean(2, obj.getUtilizarFolhaPagamento());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
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
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			Imposto.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE Imposto ");
			sql.append("   SET nome=?, utilizarFolhaPagamento = ?");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUtilizarFolhaPagamento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			Imposto.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM Imposto WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void consultar(DataModelo dataModelo) {
		List<ImpostoVO> objs = new ArrayList<>();
		if (dataModelo.getCampoConsulta().equals("codigo")) {
			if (dataModelo.getValorConsulta().equals("")) {
				dataModelo.setValorConsulta("0");
			}
			int valorInt = Integer.parseInt(dataModelo.getValorConsulta());
			objs = getFacadeFactory().getImpostoFacade().consultaRapidaPorCodigo(valorInt, dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getImpostoFacade().consultaTotalPorCodigo(valorInt,dataModelo));
		}
		if (dataModelo.getCampoConsulta().equals("nome")) {
			objs = getFacadeFactory().getImpostoFacade().consultaRapidaPorNome(dataModelo.getValorConsulta(), dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getImpostoFacade().consultaTotalPorNome(dataModelo.getValorConsulta(), dataModelo));
		}
		dataModelo.setListaConsulta(objs);
	}

	private Boolean validarUnicidade(ImpostoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM imposto ");
		sql.append(" WHERE lower(sem_acentos(nome) ) = (lower(sem_acentos(?)))");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getNome().toLowerCase()).next();
	}

	private StringBuilder getSQLPadraoConsultaTotal() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(Imposto.codigo) as qtde ");
		sql.append(" FROM Imposto ");
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT imposto.codigo as \"imposto.codigo\", imposto.nome as \"imposto.nome\", imposto.utilizarFolhaPagamento as \"imposto.utilizarFolhaPagamento\" ");
		sql.append(" FROM Imposto ");
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.dsfs#consultaRapidaPorCodigo(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ImpostoVO> consultaRapidaPorCodigo(Integer valorConsulta, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE imposto.codigo = ? ");
			dataModelo.getListaFiltros().add(valorConsulta);
			sqlStr.append(" ORDER BY imposto.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.dsfs#consultaRapidaPorCodigo(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultaTotalPorCodigo(Integer valorConsulta, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaTotal();
			sqlStr.append(" WHERE imposto.codigo = ? ");
			dataModelo.getListaFiltros().add(valorConsulta);
			sqlStr.append(" ORDER BY imposto.codigo desc ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.dsfs#consultaRapidaPorNome(java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ImpostoVO> consultaRapidaPorNome(String valorConsulta, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE lower(imposto.nome) like(?) ");
			dataModelo.getListaFiltros().add(PERCENT + valorConsulta.toLowerCase() + PERCENT);
			sqlStr.append(" ORDER BY imposto.nome ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.dsfs#consultaRapidaPorCodigo(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultaTotalPorNome(String valorConsulta, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaTotal();
			sqlStr.append(" WHERE lower(imposto.nome) like(?) ");
			dataModelo.getListaFiltros().add(PERCENT + valorConsulta.toLowerCase() + PERCENT);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.financeiro.dsfs#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ImpostoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE imposto.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( ImpostoVO ).");
			}
			ImpostoVO obj = new ImpostoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ImpostoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<ImpostoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			ImpostoVO obj = new ImpostoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(ImpostoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("imposto.codigo")));
		obj.setNome(dadosSQL.getString("imposto.nome"));
		obj.setUtilizarFolhaPagamento(dadosSQL.getBoolean("imposto.utilizarFolhaPagamento"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}
	
	@Override
	public List<ImpostoVO> consultarImpostoComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT codigo, nome FROM Imposto WHERE utilizarfolhapagamento = true  ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ImpostoVO> vetResultado = new ArrayList<ImpostoVO>(0);
		while (tabelaResultado.next()) {
			ImpostoVO obj = new ImpostoVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setNome(tabelaResultado.getString("nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Imposto.idEntidade;
	}

}
