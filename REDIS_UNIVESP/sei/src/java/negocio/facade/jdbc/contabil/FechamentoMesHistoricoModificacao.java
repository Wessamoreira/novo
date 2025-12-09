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
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.FechamentoMesHistoricoModificacaoInterfaceFacade;

/**
 * @see FechamentoMesHistoricoModificacaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class FechamentoMesHistoricoModificacao extends ControleAcesso implements FechamentoMesHistoricoModificacaoInterfaceFacade {

	protected static String idEntidade;

	public FechamentoMesHistoricoModificacao() throws Exception {
		super();
		setIdEntidade("FechamentoMes");
	}

	public FechamentoMesHistoricoModificacaoVO novo() throws Exception {
		incluir(getIdEntidade());
		FechamentoMesHistoricoModificacaoVO obj = new FechamentoMesHistoricoModificacaoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FechamentoMesHistoricoModificacaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesHistoricoModificacao.incluir(getIdEntidade());
			FechamentoMesHistoricoModificacaoVO.validarDados(obj);
			final String sql = "INSERT INTO FechamentoMesHistoricoModificacao ( "
					+ "fechamentoMes, descricao, usuarioResponsavel, tipoOrigemBloqueio, "
					+ "codigoOrigem, dataModificacaoBloqueio, detalheModificacao"
					+ ") VALUES ( "
					+ "?, ?, ?, ?, ?, ?, ? "
					+ ") returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 1;
					if (Uteis.isAtributoPreenchido(obj.getFechamentoMes().getCodigo())) {
						sqlInserir.setInt(i++, obj.getFechamentoMes().getCodigo().intValue());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					sqlInserir.setString(i++, obj.getDescricao());
					if (Uteis.isAtributoPreenchido(obj.getUsuarioResponsavel().getCodigo())) {
						sqlInserir.setInt(i++, obj.getUsuarioResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					sqlInserir.setString(i++, obj.getTipoOrigemBloqueio().toString());
					sqlInserir.setInt(i++, obj.getCodigoOrigem());
					if (Uteis.isAtributoPreenchido(obj.getDataModificacaoBloqueio())) {
						sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataModificacaoBloqueio()));
					} else {
						sqlInserir.setNull(i++, 0);
					}
					sqlInserir.setString(i++, obj.getDetalheModificacao());
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
    public void incluirListaFechamentoMesHistoricoModificacao(Integer codigoFechamentoMes, List<FechamentoMesHistoricoModificacaoVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<FechamentoMesHistoricoModificacaoVO> e = objetos.iterator();
        while (e.hasNext()) {
        	FechamentoMesHistoricoModificacaoVO obj = e.next();
            obj.getFechamentoMes().setCodigo(codigoFechamentoMes);
            incluir(obj, usuario);
        }
    }	
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaFechamentoMesHistoricoModificacao(Integer codigoFechamentoMes, List<FechamentoMesHistoricoModificacaoVO> objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM FechamentoMesHistoricoModificacao WHERE fechamentoMes = " + codigoFechamentoMes;
        Iterator<FechamentoMesHistoricoModificacaoVO> i = objetos.iterator();
        while (i.hasNext()) {
        	FechamentoMesHistoricoModificacaoVO objeto = i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        Iterator<FechamentoMesHistoricoModificacaoVO> e = objetos.iterator();
        while (e.hasNext()) {
        	FechamentoMesHistoricoModificacaoVO objeto = e.next();
            objeto.getFechamentoMes().setCodigo(codigoFechamentoMes);
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto,usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }    

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FechamentoMesHistoricoModificacaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesHistoricoModificacao.alterar(getIdEntidade());
			FechamentoMesHistoricoModificacaoVO.validarDados(obj);
			final String sql = "UPDATE FechamentoMesHistoricoModificacao set "
					+ "fechamentoMes=?, descricao=?, usuarioResponsavel=?, tipoOrigemBloqueio=?, "
					+ "codigoOrigem=?, dataModificacaoBloqueio=?, detalheModificacao=? "
					+ "WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 1;
					if (Uteis.isAtributoPreenchido(obj.getFechamentoMes().getCodigo())) {
						sqlAlterar.setInt(i++, obj.getFechamentoMes().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(i++, 0);
					}
					sqlAlterar.setString(i++, obj.getDescricao());
					if (Uteis.isAtributoPreenchido(obj.getUsuarioResponsavel().getCodigo())) {
						sqlAlterar.setInt(i++, obj.getUsuarioResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(i++, 0);
					}
					sqlAlterar.setString(i++, obj.getTipoOrigemBloqueio().toString());
					sqlAlterar.setInt(i++, obj.getCodigoOrigem());
					if (Uteis.isAtributoPreenchido(obj.getDataModificacaoBloqueio())) {
						sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataModificacaoBloqueio()));
					} else {
						sqlAlterar.setNull(i++, 0);
					}
					sqlAlterar.setString(i++, obj.getDetalheModificacao());
					sqlAlterar.setInt(i++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FechamentoMesHistoricoModificacaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesHistoricoModificacao.excluir(getIdEntidade());
			String sql = "DELETE FROM FechamentoMesHistoricoModificacao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer codigoFechamento, UsuarioVO usuario) throws Exception {
		try {
			FechamentoMesHistoricoModificacao.excluir(getIdEntidade());
			String sql = "DELETE FROM FechamentoMesHistoricoModificacao WHERE ((fechamentoMes = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(sql, new Object[] { codigoFechamento });
		} catch (Exception e) {
			throw e;
		}
	}	
	
	public List<FechamentoMesHistoricoModificacaoVO> consultarPorFechamentoMes(Integer codigoFechamento, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FechamentoMesHistoricoModificacao.* FROM FechamentoMesHistoricoModificacao "
				+ " WHERE FechamentoMesHistoricoModificacao.fechamentoMes = " + codigoFechamento.intValue() + "  "
			    + " ORDER BY dataModificacaoBloqueio desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public static FechamentoMesHistoricoModificacaoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		FechamentoMesHistoricoModificacaoVO obj = new FechamentoMesHistoricoModificacaoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getFechamentoMes().setCodigo(dadosSQL.getInt("fechamentoMes"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getUsuarioResponsavel().setCodigo(dadosSQL.getInt("usuarioResponsavel"));
		obj.setTipoOrigemBloqueio(TipoOrigemHistoricoBloqueioEnum.valueOf(dadosSQL.getString("tipoOrigemBloqueio")));
		obj.setCodigoOrigem(dadosSQL.getInt("codigoOrigem"));
		obj.setDataModificacaoBloqueio(dadosSQL.getDate("dataModificacaoBloqueio"));
		obj.setDetalheModificacao(dadosSQL.getString("detalheModificacao"));
		montarDadosUsuario(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	public static void montarDadosUsuario(FechamentoMesHistoricoModificacaoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getUsuarioResponsavel().getCodigo().intValue() == 0) {
			return;
		}
		obj.setUsuarioResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}		
	
	public FechamentoMesHistoricoModificacaoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM FechamentoMesHistoricoModificacao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( FechamentoMesHistoricoModificacao ).");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	public static List<FechamentoMesHistoricoModificacaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<FechamentoMesHistoricoModificacaoVO> vetResultado = new ArrayList<FechamentoMesHistoricoModificacaoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static String getIdEntidade() {
		return FechamentoMesHistoricoModificacao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FechamentoMesHistoricoModificacao.idEntidade = idEntidade;
	}

	public FechamentoMesHistoricoModificacaoVO gerarNovoHistoricoModificacao(FechamentoMesVO fechamentoMes, UsuarioVO usuario, TipoOrigemHistoricoBloqueioEnum tipoOrigem, String descricao, String detalhe) {
		FechamentoMesHistoricoModificacaoVO historico = new FechamentoMesHistoricoModificacaoVO();
		historico.setFechamentoMes(fechamentoMes);
		historico.setCodigoOrigem(fechamentoMes.getCodigo());
		historico.setDescricao(descricao);
		historico.setUsuarioResponsavel(usuario);
		historico.setTipoOrigemBloqueio(tipoOrigem);
		historico.setDetalheModificacao(detalhe);
		return historico;
	}	
}
