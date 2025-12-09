package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import negocio.comuns.contabil.FechamentoMesUnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.FechamentoMesUnidadeEnsinoInterfaceFacade;

/**
 * @see FechamentoMesUnidadeEnsinoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class FechamentoMesUnidadeEnsino extends ControleAcesso implements FechamentoMesUnidadeEnsinoInterfaceFacade {

	protected static String idEntidade;

	public FechamentoMesUnidadeEnsino() throws Exception {
		super();
		setIdEntidade("FechamentoMes");
	}

	public FechamentoMesUnidadeEnsinoVO novo() throws Exception {
		incluir(getIdEntidade());
		FechamentoMesUnidadeEnsinoVO obj = new FechamentoMesUnidadeEnsinoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FechamentoMesUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesUnidadeEnsino.incluir(getIdEntidade());
			FechamentoMesUnidadeEnsinoVO.validarDados(obj);
			final String sql = "INSERT INTO FechamentoMesUnidadeEnsino ( "
					+ "fechamentoMes, unidadeEnsino "
					+ ") VALUES ( "
					+ "?, ? "
					+ ") returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getFechamentoMes().getCodigo())) {
						sqlInserir.setInt(1, obj.getFechamentoMes().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getCodigo())) {
						sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}					
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
    public void incluirListaFechamentoMesUnidadeEnsino(Integer codigoFechamentoMes, List<FechamentoMesUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<FechamentoMesUnidadeEnsinoVO> e = objetos.iterator();
        while (e.hasNext()) {
        	FechamentoMesUnidadeEnsinoVO obj = e.next();
            obj.getFechamentoMes().setCodigo(codigoFechamentoMes);
            incluir(obj, usuario);
        }
    }	
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaFechamentoMesUnidadeEnsino(Integer codigoFechamentoMes, List<FechamentoMesUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM FechamentoMesUnidadeEnsino WHERE fechamentoMes = " + codigoFechamentoMes;
        Iterator<FechamentoMesUnidadeEnsinoVO> i = objetos.iterator();
        while (i.hasNext()) {
        	FechamentoMesUnidadeEnsinoVO objeto = i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator<FechamentoMesUnidadeEnsinoVO> e = objetos.iterator();
        while (e.hasNext()) {
        	FechamentoMesUnidadeEnsinoVO objeto = e.next();
            objeto.getFechamentoMes().setCodigo(codigoFechamentoMes);
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto,usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }    

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FechamentoMesUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesUnidadeEnsino.alterar(getIdEntidade());
			FechamentoMesUnidadeEnsinoVO.validarDados(obj);
			final String sql = "UPDATE FechamentoMesUnidadeEnsino set "
					+ "fechamentoMes=?, unidadeEnsino=? "
					+ "WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getFechamentoMes().getCodigo())) {
						sqlAlterar.setInt(1, obj.getFechamentoMes().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getCodigo())) {
						sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}								
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FechamentoMesUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesUnidadeEnsino.excluir(getIdEntidade());
			String sql = "DELETE FROM FechamentoMesUnidadeEnsino WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer codigoFechamento, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesUnidadeEnsino.excluir(getIdEntidade());
			String sql = "DELETE FROM FechamentoMesUnidadeEnsino WHERE ((fechamentoMes = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(sql, new Object[] { codigoFechamento });
		} catch (Exception e) {
			throw e;
		}
	}	
	
	public List<FechamentoMesUnidadeEnsinoVO> consultarPorFechamentoMes(Integer codigoFechamento, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FechamentoMesUnidadeEnsino.* FROM FechamentoMesUnidadeEnsino "
				+ " INNER JOIN UnidadeEnsino ON UnidadeEnsino.codigo = FechamentoMesUnidadeEnsino.unidadeEnsino "
				+ " WHERE FechamentoMesUnidadeEnsino.fechamentoMes = " + codigoFechamento.intValue() + " ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public static FechamentoMesUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		FechamentoMesUnidadeEnsinoVO obj = new FechamentoMesUnidadeEnsinoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getFechamentoMes().setCodigo(dadosSQL.getInt("fechamentoMes"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		montarDadosUnidadeEnsino(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	public static void montarDadosUnidadeEnsino(FechamentoMesUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	public FechamentoMesUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM FechamentoMesUnidadeEnsino WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( FechamentoMesUnidadeEnsino ).");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	public static List<FechamentoMesUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<FechamentoMesUnidadeEnsinoVO> vetResultado = new ArrayList<FechamentoMesUnidadeEnsinoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static String getIdEntidade() {
		return FechamentoMesUnidadeEnsino.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FechamentoMesUnidadeEnsino.idEntidade = idEntidade;
	}

}
