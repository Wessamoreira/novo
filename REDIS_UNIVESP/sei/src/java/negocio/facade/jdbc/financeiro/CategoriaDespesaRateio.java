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
import negocio.comuns.financeiro.CategoriaDespesaRateioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CategoriaDespesaRateioInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class CategoriaDespesaRateio extends ControleAcesso implements CategoriaDespesaRateioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1710833958548130197L;

	protected static String idEntidade;

	public CategoriaDespesaRateio() throws Exception {
		super();
		setIdEntidade("CategoriaDespesaRateio");
	}

	public void validarDados(CategoriaDespesaRateioVO obj, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			throw new Exception("O campo Unidade Ensino (Categoria Despesa Rateio) deve ser informado.");
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<CategoriaDespesaRateioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (CategoriaDespesaRateioVO CategoriaDespesaRateioVO : lista) {
			persistir(CategoriaDespesaRateioVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CategoriaDespesaRateioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj, usuarioVO);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CategoriaDespesaRateioVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			CategoriaDespesaRateio.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO CategoriaDespesaRateio ( unidadeEnsino, curso, departamento, categoriaDespesa, porcentagem, tipocategoriadespesarateio ) ");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
						sqlInserir.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
						sqlInserir.setInt(++i, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDepartamentoVO())) {
						sqlInserir.setInt(++i, obj.getDepartamentoVO().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setInt(++i, obj.getCategoriaDespesaVO().getCodigo());
					sqlInserir.setDouble(++i, obj.getPorcentagem());
					sqlInserir.setString(++i, obj.getTipoCategoriaDespesaRateioEnum().name());
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

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CategoriaDespesaRateioVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			CategoriaDespesaRateio.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE CategoriaDespesaRateio ");
			sql.append("   SET unidadeEnsino=?, curso=?, departamento=?, categoriaDespesa=?, porcentagem=?, tipocategoriadespesarateio=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
						sqlAlterar.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
						sqlAlterar.setInt(++i, obj.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDepartamentoVO())) {
						sqlAlterar.setInt(++i, obj.getDepartamentoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setInt(++i, obj.getCategoriaDespesaVO().getCodigo());
					sqlAlterar.setDouble(++i, obj.getPorcentagem());
					sqlAlterar.setString(++i, obj.getTipoCategoriaDespesaRateioEnum().name());
					sqlAlterar.setInt(++i, obj.getCodigo());
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
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CategoriaDespesaRateioVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			CategoriaDespesaRateio.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM CategoriaDespesaRateio WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT cdr.codigo as \"cdr.codigo\",  cdr.porcentagem as \"cdr.porcentagem\", cdr.categoriadespesa as \"cdr.categoriadespesa\",  ");
		sql.append(" cdr.tipocategoriadespesarateio as \"cdr.tipocategoriadespesarateio\",  ");
		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.codigocontabil as \"curso.codigocontabil\", curso.nomecontabil as \"curso.nomecontabil\", ");
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\", departamento.codigocontabil as \"departamento.codigocontabil\", departamento.nomecontabil as \"departamento.nomecontabil\" ");
		sql.append(" FROM CategoriaDespesaRateio cdr ");
		sql.append(" left join  unidadeEnsino on unidadeEnsino.codigo = cdr.unidadeEnsino ");
		sql.append(" left join  curso on curso.codigo = cdr.curso ");
		sql.append(" left join  departamento on departamento.codigo = cdr.departamento ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CategoriaDespesaRateioVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE cdr.codigo = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY cdr.codigo asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultaRapidaPorCategoriaDespesaVO(CategoriaDespesaVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE cdr.categoriadespesa = ").append(obj.getCodigo()).append(" ");
		sqlStr.append(" ORDER BY cdr.codigo asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			CategoriaDespesaRateioVO cdr = new CategoriaDespesaRateioVO();
			montarDadosBasico(cdr, tabelaResultado, nivelMontarDados, usuario);
			if(cdr.getTipoCategoriaDespesaRateioEnum().isAcademico()){
				obj.getListaCategoriaDespesaRateioAcademico().add(cdr);
			}else{
				obj.getListaCategoriaDespesaRateioAdministrativo().add(cdr);
			}
		}
		
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CategoriaDespesaRateioVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CategoriaDespesaRateioVO> vetResultado = new ArrayList<CategoriaDespesaRateioVO>(0);
		while (tabelaResultado.next()) {
			CategoriaDespesaRateioVO obj = new CategoriaDespesaRateioVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(CategoriaDespesaRateioVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("cdr.codigo")));
		obj.setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum.valueOf(dadosSQL.getString("cdr.tipocategoriadespesarateio")));
		obj.setPorcentagem(dadosSQL.getDouble("cdr.porcentagem"));
		obj.getCategoriaDespesaVO().setCodigo(new Integer(dadosSQL.getInt("cdr.categoriadespesa")));
		
		obj.getUnidadeEnsinoVO().setCodigo(new Integer(dadosSQL.getInt("unidadeensino.codigo")));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		
		obj.getCursoVO().setCodigo(new Integer(dadosSQL.getInt("curso.codigo")));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getCursoVO().setCodigoContabil(dadosSQL.getString("curso.codigocontabil"));
		obj.getCursoVO().setNomeContabil(dadosSQL.getString("curso.nomecontabil"));
		
		
		obj.getDepartamentoVO().setCodigo(new Integer(dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamentoVO().setNome(dadosSQL.getString("departamento.nome"));
		obj.getDepartamentoVO().setCodigoContabil(dadosSQL.getString("departamento.codigocontabil"));
		obj.getDepartamentoVO().setNomeContabil(dadosSQL.getString("departamento.nomecontabil"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}

	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CategoriaDespesaRateio.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CategoriaDespesaRateio.idEntidade = idEntidade;
	}

}
