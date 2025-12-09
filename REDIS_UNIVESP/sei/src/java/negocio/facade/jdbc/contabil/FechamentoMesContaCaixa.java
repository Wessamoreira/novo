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
import negocio.comuns.contabil.FechamentoMesContaCaixaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.FechamentoMesContaCaixaInterfaceFacade;

/**
 * @see FechamentoMesContaCaixaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class FechamentoMesContaCaixa extends ControleAcesso implements FechamentoMesContaCaixaInterfaceFacade {

	protected static String idEntidade;

	public FechamentoMesContaCaixa() throws Exception {
		super();
		setIdEntidade("FechamentoMes");
	}

	public FechamentoMesContaCaixaVO novo() throws Exception {
		incluir(getIdEntidade());
		FechamentoMesContaCaixaVO obj = new FechamentoMesContaCaixaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FechamentoMesContaCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesContaCaixa.incluir(getIdEntidade());
			FechamentoMesContaCaixaVO.validarDados(obj);
			final String sql = "INSERT INTO FechamentoMesContaCaixa ( "
					+ "fechamentoMes, contaCaixa "
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
					if (Uteis.isAtributoPreenchido(obj.getContaCaixa().getCodigo())) {
						sqlInserir.setInt(2, obj.getContaCaixa().getCodigo().intValue());
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
    public void incluirListaFechamentoMesContaCaixa(Integer codigoFechamentoMes, List<FechamentoMesContaCaixaVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<FechamentoMesContaCaixaVO> e = objetos.iterator();
        while (e.hasNext()) {
        	FechamentoMesContaCaixaVO obj = e.next();
            obj.getFechamentoMes().setCodigo(codigoFechamentoMes);
            incluir(obj, usuario);
        }
    }	
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaFechamentoMesContaCaixa(Integer codigoFechamentoMes, List<FechamentoMesContaCaixaVO> objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM FechamentoMesContaCaixa WHERE fechamentoMes = " + codigoFechamentoMes;
        Iterator<FechamentoMesContaCaixaVO> i = objetos.iterator();
        while (i.hasNext()) {
        	FechamentoMesContaCaixaVO objeto = i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator<FechamentoMesContaCaixaVO> e = objetos.iterator();
        while (e.hasNext()) {
        	FechamentoMesContaCaixaVO objeto = e.next();
            objeto.getFechamentoMes().setCodigo(codigoFechamentoMes);
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto,usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }    

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FechamentoMesContaCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesContaCaixa.alterar(getIdEntidade());
			FechamentoMesContaCaixaVO.validarDados(obj);
			final String sql = "UPDATE FechamentoMesContaCaixa set "
					+ "fechamentoMes=?, contaCaixa=? "
					+ "WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getFechamentoMes().getCodigo())) {
						sqlAlterar.setInt(1, obj.getFechamentoMes().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getContaCaixa().getCodigo())) {
						sqlAlterar.setInt(2, obj.getContaCaixa().getCodigo().intValue());
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
	public void excluir(FechamentoMesContaCaixaVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesContaCaixa.excluir(getIdEntidade());
			String sql = "DELETE FROM FechamentoMesContaCaixa WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer codigoFechamento, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesContaCaixa.excluir(getIdEntidade());
			String sql = "DELETE FROM FechamentoMesContaCaixa WHERE ((fechamentoMes = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(sql, new Object[] { codigoFechamento });
		} catch (Exception e) {
			throw e;
		}
	}	
	
	public List<FechamentoMesContaCaixaVO> consultarPorFechamentoMes(Integer codigoFechamento, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FechamentoMesContaCaixa.* FROM FechamentoMesContaCaixa "
				+ " INNER JOIN ContaCorrente ON ContaCorrente.codigo = FechamentoMesContaCaixa.contaCaixa "
				+ " WHERE FechamentoMesContaCaixa.fechamentoMes = " + codigoFechamento.intValue() + " ORDER BY ContaCorrente.codigo ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public static FechamentoMesContaCaixaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		FechamentoMesContaCaixaVO obj = new FechamentoMesContaCaixaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getFechamentoMes().setCodigo(dadosSQL.getInt("fechamentoMes"));
		obj.getContaCaixa().setCodigo(dadosSQL.getInt("contaCaixa"));
		montarDadosContaCaixa(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	public static void montarDadosContaCaixa(FechamentoMesContaCaixaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getContaCaixa().getCodigo().intValue() == 0) {
			return;
		}
		obj.setContaCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCaixa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	public FechamentoMesContaCaixaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM FechamentoMesContaCaixa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( FechamentoMesContaCaixa ).");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	public static List<FechamentoMesContaCaixaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<FechamentoMesContaCaixaVO> vetResultado = new ArrayList<FechamentoMesContaCaixaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static String getIdEntidade() {
		return FechamentoMesContaCaixa.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FechamentoMesContaCaixa.idEntidade = idEntidade;
	}

}
