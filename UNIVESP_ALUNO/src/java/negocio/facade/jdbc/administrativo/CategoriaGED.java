package negocio.facade.jdbc.administrativo;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CategoriaGEDInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>CategoriaGEDVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>CategoriaGEDVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see CategoriaGEDVO
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class CategoriaGED extends ControleAcesso implements CategoriaGEDInterfaceFacade {

	protected static String idEntidade;

	private static final long serialVersionUID = -5561256915455466953L;

	public CategoriaGED() throws Exception { 
		super();
		setIdEntidade("CategoriaGED");
	}

	@Override
	public void persistir(CategoriaGEDVO categoriaGEDVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(categoriaGEDVO);
		validarDescricaoOuIdentificadorDuplicado(categoriaGEDVO);

		if (categoriaGEDVO.getCodigo() == null || categoriaGEDVO.getCodigo() == 0) {
			incluir(categoriaGEDVO, validarAcesso, usuarioVO);
		} else {
			alterar(categoriaGEDVO, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(CategoriaGEDVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) {
		try {
			final String sql = "INSERT INTO CategoriaGED( descricao, identificador) VALUES ( ?, ?) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
	
				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setString(2, obj.getIdentificador());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {
	
				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(CategoriaGEDVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE CategoriaGED set descricao=?, identificador=? WHERE codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, obj.getIdentificador());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getAplicacaoControle().removerCategoriaGED(obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CategoriaGEDVO categoriaGEDVO, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "DELETE FROM CategoriaGED WHERE ((codigo = ?)) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { categoriaGEDVO.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<CategoriaGEDVO> consultar(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<CategoriaGEDVO> listaCategoriaGED = new ArrayList<>(0);
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaGED";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		while (tabelaResultado.next()) {
			listaCategoriaGED.add(montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return listaCategoriaGED;
	}

	@Override
	public CategoriaGEDVO consultarPorIdentificador(String identificador) throws Exception {
		String sql = "SELECT * FROM CategoriaGED WHERE identificador = '" + identificador +"'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);

		if (tabelaResultado.next()) {
			return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		return null;
	}

	public List<CategoriaGEDVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM CategoriaGED ");

		switch (campoConsulta) {
		case "descricao":
			sql.append(" WHERE upper( descricao ) like(?) ORDER BY descricao");
			break;
		case "identificador":
			sql.append(" WHERE upper( identificador ) like(?) ORDER BY identificador");
			break;
		default:
			break;
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta.toUpperCase()+"%");
		List<CategoriaGEDVO> listaCategoriaGED = new ArrayList<>();

		while (tabelaResultado.next()) {
			listaCategoriaGED.add(montarDadosConsulta(tabelaResultado, nivelMontarDados));
		}
		return listaCategoriaGED;
	}

	@Override
	public int consultarTotalRegistrosPorDescricaoOuIdentificador(CategoriaGEDVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(CategoriaGED.codigo) as qtde FROM CategoriaGED");
		sql.append(" WHERE (descricao = '").append(obj.getDescricao().trim() + "'");
		sql.append(" OR identificador = '").append(obj.getIdentificador().trim() + "')");

		if (obj.getCodigo() != null) {
			sql.append(" AND codigo !=").append(obj.getCodigo());
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		if (rs.next()) {
			return rs.getInt("qtde");
		}

		return 0;
	}

	private CategoriaGEDVO montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) {
		CategoriaGEDVO obj = new CategoriaGEDVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setIdentificador(tabelaResultado.getString("identificador"));
		return obj;
	}
	
	private void validarDescricaoOuIdentificadorDuplicado(final CategoriaGEDVO obj) throws ConsistirException {
		int quantidadeRegistro = consultarTotalRegistrosPorDescricaoOuIdentificador(obj);

		if (quantidadeRegistro > 0 ) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CategoriaGED_registroDuplicado"));
		}
	}

	private void validarDados(CategoriaGEDVO categoriaGEDVO) throws ConsistirException {
		if (categoriaGEDVO.getDescricao().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CategoriaGED_descricao"));
		}

		if (categoriaGEDVO.getIdentificador().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CategoriaGED_identificador"));
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		CategoriaGED.idEntidade = idEntidade;
	}
	
	@Override
	public CategoriaGEDVO consultarPorChavePrimaria(Integer codigo) throws Exception {
		return getAplicacaoControle().getCategoriaGEDVO(codigo, null);
	}
	
	@Override
	public CategoriaGEDVO consultarPorChavePrimariaUnico(Integer codigo) throws Exception {
		String sql = "SELECT * FROM CategoriaGED WHERE codigo = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);

		if (tabelaResultado.next()) {
			return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		return null;
	}
}