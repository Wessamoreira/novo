package negocio.facade.jdbc.biblioteca;

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
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ArquivoMarc21CatalogoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ArquivoMarc21Catalogo extends ControleAcesso implements ArquivoMarc21CatalogoInterfaceFacade {
	
	protected static String idEntidade;

	public ArquivoMarc21Catalogo() throws Exception {
		super();
		setIdEntidade("ArquivoMarc21Catalogo");
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ArquivoMarc21CatalogoVO obj, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getCatalogoFacade().incluir(obj.getCatalogoVO(), usuarioVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO), true);
		final String sql = " INSERT INTO arquivomarc21catalogo (arquivomarc21, catalogo) VALUES (?, ?) returning codigo ";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getArquivoMarc21VO().getCodigo());
                sqlInserir.setInt(2, obj.getCatalogoVO().getCodigo());
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
	}
	
	public List<ArquivoMarc21CatalogoVO> consultarPorCodigoArquivoMarc21(Integer codigoArquivoMarc21, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM arquivomarc21catalogo ");
		sqlStr.append(" WHERE arquivomarc21 = ").append(codigoArquivoMarc21);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}
	
	public static List<ArquivoMarc21CatalogoVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ArquivoMarc21CatalogoVO> objs = new ArrayList<ArquivoMarc21CatalogoVO>(0);
		while (rs.next()) {
			objs.add(montarDados(rs, nivelMontarDados, usuario));
		}
		return objs;
	}

	public static ArquivoMarc21CatalogoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ArquivoMarc21CatalogoVO obj = new ArquivoMarc21CatalogoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getArquivoMarc21VO().setCodigo(dadosSQL.getInt("arquivomarc21"));
		obj.getCatalogoVO().setCodigo(dadosSQL.getInt("catalogo"));
		montarDadosCatalogo(obj, nivelMontarDados, usuario);
		return obj;
	}
	
	public static void montarDadosCatalogo(ArquivoMarc21CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCatalogoVO().getCodigo().intValue() == 0) {
			obj.setCatalogoVO(new CatalogoVO());
			return;
		}
		obj.setCatalogoVO(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCatalogoVO().getCodigo(), nivelMontarDados, 0, usuario));
	}

	public static String getIdEntidade() {
		return ArquivoMarc21Catalogo.idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ArquivoMarc21Catalogo.idEntidade = idEntidade;
	}

}
