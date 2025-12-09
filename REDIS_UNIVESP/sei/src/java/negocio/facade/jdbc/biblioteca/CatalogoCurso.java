package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoCursoVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.CatalogoCursoInterfaceFacade;

@Service
public class CatalogoCurso extends ControleAcesso implements CatalogoCursoInterfaceFacade {

	protected static String idEntidade;

	public CatalogoCurso() throws Exception {
		super();
		setIdEntidade("CatalogoCurso");
	}

	public CatalogoCursoVO novo() throws Exception {
		CatalogoCurso.incluir(getIdEntidade());
		CatalogoCursoVO obj = new CatalogoCursoVO();
		return obj;
	}

	public static void validarDados(CatalogoCursoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getCatalogoVO().getCodigo() == 0) {
			throw new ConsistirException("O campo Catálogo deve ser informado.");
		}
		if (obj.getCursoVO().getCodigo() == 0) {
			throw new ConsistirException("O campo Curso deve ser informado.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CatalogoCursoVO obj) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO CatalogoCurso( catalogo, curso ) VALUES ( ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getCatalogoVO().getCodigo());
					sqlInserir.setInt(2, obj.getCursoVO().getCodigo());
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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CatalogoCursoVO obj) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE CatalogoCurso set catalogo = ?, curso = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getCatalogoVO().getCodigo());
					sqlAlterar.setInt(2, obj.getCursoVO().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CatalogoCursoVO obj) throws Exception {
		try {
			CatalogoAutor.excluir(getIdEntidade());
			String sql = "DELETE FROM CatalogoCurso WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCatalogoCursoCatalogos(Integer catalogo) throws Exception {
		String sql = "DELETE FROM CatalogoCurso WHERE (catalogo = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { catalogo });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaCatalogoCursoPorCodigoCatalogo(CatalogoVO catalogo, List<CatalogoCursoVO> objetos)
            throws Exception {
        String str = "DELETE FROM CatalogoCurso WHERE catalogo = " + catalogo.getCodigo();
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CatalogoCursoVO objeto = (CatalogoCursoVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str);

        Iterator e = objetos.iterator();
        while (e.hasNext()) {
        	CatalogoCursoVO objeto = (CatalogoCursoVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.setCatalogoVO(catalogo);
                incluir(objeto);
            } else {
                objeto.setCatalogoVO(catalogo);
                alterar(objeto);
            }
        }
    }

	@Override
	public List consultarPorCatalogo(Integer catalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CatalogoCurso WHERE catalogo = " + catalogo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static CatalogoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CatalogoCursoVO obj = new CatalogoCursoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(dadosSQL.getInt("curso"), nivelMontarDados, false, usuario));
		obj.setCatalogoVO(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(dadosSQL.getInt("catalogo"), nivelMontarDados, 0, usuario));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		CatalogoCurso.idEntidade = idEntidade;
	}

}
