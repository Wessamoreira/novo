package negocio.facade.jdbc.faturamento.nfe;

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
import negocio.comuns.faturamento.nfe.NaturezaOperacaoVO;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNaturezaOperacaoEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoOrigemDestinoNaturezaOperacaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.NaturezaOperacaoInterfaceFacade;

/**
*
* @author Pedro
*/
@Repository
@Scope("singleton")
@Lazy
public class NaturezaOperacao extends ControleAcesso implements NaturezaOperacaoInterfaceFacade {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6280879744174284168L;
	protected static String idEntidade;

	public NaturezaOperacao() throws Exception {
		super();
		setIdEntidade("NaturezaOperacao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(NaturezaOperacaoVO obj, UsuarioVO usuario) throws Exception {		
		if (!Uteis.isAtributoPreenchido(obj.getCodigoNaturezaOperacao())) {
			throw new Exception("O campo Código Natureza Operação (Natureza Operação) não foi informado.");
		}		
		if (!Uteis.isAtributoPreenchido(obj.getNome())) {
			throw new Exception("O campo Nome (Natureza Operação) não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoNaturezaOperacaoEnum())) {
			throw new Exception("O campo Tipo Natureza Operação (Natureza Operação) não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoOrigemDestinoNaturezaOperacaoEnum())) {
			throw new Exception("O campo Tipo Origem/Destiono Natureza Operação (Natureza Operação) não foi informado.");
		}
		obj.setNome(obj.getNome().trim());// retirar espado do campo nome
		if (validarUnicidade(obj, usuario)) {
			throw new Exception("Já existe uma natureza operação cadastrada com esse código Natureza Operação: " + obj.getCodigoNaturezaOperacao() +" para esse Tipo Natureza Operação:"+ UteisJSF.internacionalizarEnum(obj.getTipoNaturezaOperacaoEnum()));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(NaturezaOperacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj, usuarioVO);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final NaturezaOperacaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			NaturezaOperacao.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO NaturezaOperacao (nome, codigoNaturezaOperacao, tipoNaturezaOperacao, descricao, tipoOrigemDestinoNaturezaOperacao) ");
			sql.append("    VALUES (?,?,?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodigoNaturezaOperacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoNaturezaOperacaoEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoOrigemDestinoNaturezaOperacaoEnum(), ++i, sqlInserir);
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
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final NaturezaOperacaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			NaturezaOperacao.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE NaturezaOperacao ");
			sql.append("   SET nome=?, codigoNaturezaOperacao=?, tipoNaturezaOperacao=?, descricao=?, tipoOrigemDestinoNaturezaOperacao=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigoNaturezaOperacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoNaturezaOperacaoEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoOrigemDestinoNaturezaOperacaoEnum(), ++i, sqlAlterar);
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
	public void excluir(NaturezaOperacaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			NaturezaOperacao.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM NaturezaOperacao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private Boolean validarUnicidade(NaturezaOperacaoVO obj, UsuarioVO usuario) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT codigo FROM NaturezaOperacao ");
		sql.append(" WHERE codigoNaturezaOperacao = ").append(obj.getCodigoNaturezaOperacao());
		sql.append(" and tipoNaturezaOperacao = '").append(obj.getTipoNaturezaOperacaoEnum()).append("' ");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM NaturezaOperacao ");
		return sql;
	}

	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NaturezaOperacaoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE codigo = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NaturezaOperacaoVO> consultaRapidaPorCodigoNaturezaOperacao(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE codigonaturezaoperacao = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY codigonaturezaoperacao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NaturezaOperacaoVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE upper(nome) like('%").append(valorConsulta.toUpperCase()).append("%') ");
		sqlStr.append(" ORDER BY nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NaturezaOperacaoVO> consultaRapidaPorTipoNaturezaOperacaoEnum(TipoNaturezaOperacaoEnum tipoNaturezaOperacaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE tipoNaturezaOperacao ='").append(tipoNaturezaOperacaoEnum.name()).append("' ");
		sqlStr.append(" ORDER BY nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}	

	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NaturezaOperacaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE codigo = ").append(codigoPrm).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NaturezaOperacaoVO ).");
		}
		NaturezaOperacaoVO obj = new NaturezaOperacaoVO();
		montarDadosBasico(obj,tabelaResultado, nivelMontarDados, usuario);
		return obj;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NaturezaOperacaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<NaturezaOperacaoVO> vetResultado = new ArrayList<NaturezaOperacaoVO>(0);
		while (tabelaResultado.next()) {
			NaturezaOperacaoVO obj = new NaturezaOperacaoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(NaturezaOperacaoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setCodigoNaturezaOperacao(dadosSQL.getInt("codigoNaturezaOperacao"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setTipoNaturezaOperacaoEnum(TipoNaturezaOperacaoEnum.valueOf(dadosSQL.getString("tipoNaturezaOperacao")));
		obj.setTipoOrigemDestinoNaturezaOperacaoEnum(TipoOrigemDestinoNaturezaOperacaoEnum.valueOf(dadosSQL.getString("tipoOrigemDestinoNaturezaOperacao")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return NaturezaOperacao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		NaturezaOperacao.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NaturezaOperacaoVO consultarPorCodigoNaturezaOperacao(Integer codigoNaturezaOperacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE codigonaturezaoperacao = ").append(codigoNaturezaOperacao).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( NaturezaOperacaoVO ).");
		}
		NaturezaOperacaoVO obj = new NaturezaOperacaoVO();
		montarDadosBasico(obj,tabelaResultado, nivelMontarDados, usuario);
		return obj;
	}

}
