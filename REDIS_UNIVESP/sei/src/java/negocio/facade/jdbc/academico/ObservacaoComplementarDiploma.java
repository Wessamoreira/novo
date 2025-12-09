package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.ObservacaoComplementarDiplomaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ObservacaoComplementarDiplomaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ObservacaoComplementarDiploma extends ControleAcesso implements ObservacaoComplementarDiplomaInterfaceFacade {

	protected static String idEntidade;

	public ObservacaoComplementarDiploma() throws Exception {
		super();
		setIdEntidade("ExpedicaoDiploma");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ObservacaoComplementarDiplomaInterfaceFacade#
	 * novo()
	 */
	public ObservacaoComplementarDiplomaVO novo() throws Exception {
		ObservacaoComplementarDiploma.incluir(getIdEntidade());
		ObservacaoComplementarDiplomaVO obj = new ObservacaoComplementarDiplomaVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.ObservacaoComplementarDiplomaInterfaceFacade#
	 * incluir(negocio.comuns.academico. ObservacaoComplementarDiplomaVO)
	 */
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ObservacaoComplementarDiplomaVO obj) throws Exception {
		try {
			ObservacaoComplementarDiplomaVO.validarDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO ObservacaoComplementarDiploma ");
			sql.append(" (expedicaoDiploma, observacaoComplementar");
			sql.append(" ) VALUES (?, ?) returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					if (obj.getExpedicaoDiploma().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getExpedicaoDiploma().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getObservacaoComplementar().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getObservacaoComplementar().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}                                        
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			throw e;
		}
	}

        @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ObservacaoComplementarDiplomaVO obj) throws Exception {
		try {
			ObservacaoComplementarDiplomaVO.validarDados(obj);
			ObservacaoComplementarDiploma.alterar(getIdEntidade());
			final StringBuilder sql = new StringBuilder("UPDATE ObservacaoComplementarDiploma set ");
			sql.append(" expedicaoDiploma=?, observacaoComplementar=? ");
			sql.append(" WHERE ((codigo = ?))");
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					if (obj.getExpedicaoDiploma().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getExpedicaoDiploma().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getObservacaoComplementar().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getObservacaoComplementar().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}    
                                        sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj);
				return;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.ObservacaoComplementarDiplomaInterfaceFacade#
	 * excluir(negocio.comuns.academico. ObservacaoComplementarDiplomaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ObservacaoComplementarDiplomaVO obj) throws Exception {
		ObservacaoComplementarDiploma.excluir(getIdEntidade());
		String sql = "DELETE FROM ObservacaoComplementarDiploma WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public List consultarPorExpedicaoDiploma(Integer codigoExpedicao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ObservacaoComplementarDiploma.* FROM ObservacaoComplementarDiploma WHERE expedicaoDiploma = " + codigoExpedicao;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public static List<ObservacaoComplementarDiplomaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<ObservacaoComplementarDiplomaVO> vetResultado = new ArrayList<ObservacaoComplementarDiplomaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static ObservacaoComplementarDiplomaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		ObservacaoComplementarDiplomaVO obj = new ObservacaoComplementarDiplomaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getExpedicaoDiploma().setCodigo(dadosSQL.getInt("expedicaoDiploma"));
		obj.getObservacaoComplementar().setCodigo(dadosSQL.getInt("observacaoComplementar"));
                try {
                    obj.setObservacaoComplementar(getFacadeFactory().getObservacaoComplementarFacade().consultarPorChavePrimaria(obj.getObservacaoComplementar().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
                } catch (Exception e) {
                }
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirObservacaoComplementarDiplomas(Integer codigoExpedicaoDiploma) throws Exception {
		String sql = "DELETE FROM ObservacaoComplementarDiploma WHERE (expedicaoDiploma = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { codigoExpedicaoDiploma });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarObservacaoComplementarDiplomas(Integer codigoExpedicaoDiploma, List objetos) throws Exception {
		excluirObservacaoComplementarDiplomas(codigoExpedicaoDiploma);
		incluirObservacaoComplementarDiplomas(codigoExpedicaoDiploma, objetos);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirObservacaoComplementarDiplomas(Integer codigoExpedicaoDiploma, List objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ObservacaoComplementarDiplomaVO obj = (ObservacaoComplementarDiplomaVO) e.next();
			obj.getExpedicaoDiploma().setCodigo(codigoExpedicaoDiploma);
			incluir(obj);
		}
	}

	public ObservacaoComplementarDiplomaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ObservacaoComplementarDiploma WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ObservacaoComplementarDiploma.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ObservacaoComplementarDiplomaInterfaceFacade#
	 * setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		ObservacaoComplementarDiploma.idEntidade = idEntidade;
	}
}