package negocio.facade.jdbc.arquitetura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.OperacaoFuncionalidadeInterfaceFacade;

/**
 * @author Wellington - 25 de set de 2015
 *
 */
@Repository
public class OperacaoFuncionalidade extends ControleAcesso implements OperacaoFuncionalidadeInterfaceFacade {

	private static final long serialVersionUID = 1L;
	private static String idEntidade;

	public OperacaoFuncionalidade() {
		super();
		setIdEntidade("OperacaoFuncionalidade");
	}

	/**
	 * Responsável por mater o registro de operações realizadas por funcionalidades que solicitam usuário e senha para confirmação.
	 * 
	 * @author Wellington - 25 de set de 2015
	 * @param obj
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final OperacaoFuncionalidadeVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO OperacaoFuncionalidade (origem, codigoOrigem, operacao, responsavel, data, observacao) VALUES (?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setString(1, obj.getOrigem().name());
						sqlInserir.setString(2, obj.getCodigoOrigem());
						sqlInserir.setString(3, obj.getOperacao().name());
						sqlInserir.setInt(4, obj.getResponsavel().getCodigo());
						sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getData()));
						sqlInserir.setString(6, obj.getObservacao());
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(false);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Responsável por executar a geração da operação funcionalidade de acordo com os parâmetros passado.
	 * 
	 * @author Wellington - 25 de set de 2015
	 * @param origem
	 * @param codigoOrigem
	 * @param operacao
	 * @param responsavel
	 * @param observacao
	 * @return
	 * @throws Exception
	 */
	@Override
	public OperacaoFuncionalidadeVO executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum origem, String codigoOrigem, OperacaoFuncionalidadeEnum operacao, UsuarioVO responsavel, String observacao) throws Exception {
		OperacaoFuncionalidadeVO obj = new OperacaoFuncionalidadeVO();
		obj.setOrigem(origem);
		obj.setCodigoOrigem(codigoOrigem);
		obj.setOperacao(operacao);
		obj.setResponsavel(responsavel);
		obj.setData(new Date());
		obj.setObservacao(observacao);
		return obj;
	}

	@Override
	public List<OperacaoFuncionalidadeVO> consultarPorOrigem(OrigemOperacaoFuncionalidadeEnum origem, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		OperacaoFuncionalidade.consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT OperacaoFuncionalidade.* FROM OperacaoFuncionalidade ");
		sqlStr.append("WHERE origem = '").append(origem.name()).append("' ");
		sqlStr.append("ORDER BY data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<OperacaoFuncionalidadeVO> consultarPorOperacao(OperacaoFuncionalidadeEnum operacao, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		OperacaoFuncionalidade.consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT OperacaoFuncionalidade.* FROM OperacaoFuncionalidade ");
		sqlStr.append("WHERE operacao = '").append(operacao.name()).append("' ");
		sqlStr.append("ORDER BY data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<OperacaoFuncionalidadeVO> consultarPorResponsavel(Integer responsavel, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		OperacaoFuncionalidade.consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT OperacaoFuncionalidade.* FROM OperacaoFuncionalidade ");
		sqlStr.append("WHERE responsavel = ").append(responsavel);
		sqlStr.append(" ORDER BY data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<OperacaoFuncionalidadeVO> consultarPorCodigoOrigem(Integer codigoOrigem, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		OperacaoFuncionalidade.consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT OperacaoFuncionalidade.* FROM OperacaoFuncionalidade ");
		sqlStr.append("WHERE codigoOrigem = '").append(codigoOrigem).append("' ");
		sqlStr.append("ORDER BY data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<OperacaoFuncionalidadeVO> consultarOperacaoFuncionalidadeVOPorCodigoOrigemPorOrigemOperacaoFuncionalidadePorOperacaoFuncionalidade(Integer codigoOrigem, OrigemOperacaoFuncionalidadeEnum oofe, OperacaoFuncionalidadeEnum ofe, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		OperacaoFuncionalidade.consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT OperacaoFuncionalidade.* FROM OperacaoFuncionalidade ");
		sqlStr.append("WHERE codigoOrigem = ? ");
		sqlStr.append("AND origem = ? ");
		sqlStr.append("AND operacao = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {codigoOrigem.toString(), oofe.name(), ofe.name()});
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public boolean consultarPorCodigoOrigemPorOrigemOperacaoFuncionalidadePorOperacaoFuncionalidade(Integer codigoOrigem, OrigemOperacaoFuncionalidadeEnum oofe, OperacaoFuncionalidadeEnum ofe, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(OperacaoFuncionalidade.codigo) as qtd FROM OperacaoFuncionalidade ");
		sqlStr.append("WHERE codigoOrigem = '").append(codigoOrigem).append("' ");
		sqlStr.append("AND origem = '").append(oofe.name()).append("' ");
		sqlStr.append("AND operacao = '").append(ofe.name()).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	private List<OperacaoFuncionalidadeVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<OperacaoFuncionalidadeVO> operacaoFuncionalidadeVOs = new ArrayList<OperacaoFuncionalidadeVO>(0);
		while (tabelaResultado.next()) {
			operacaoFuncionalidadeVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return operacaoFuncionalidadeVOs;
	}

	private OperacaoFuncionalidadeVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		OperacaoFuncionalidadeVO obj = new OperacaoFuncionalidadeVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setOrigem(OrigemOperacaoFuncionalidadeEnum.valueOf(tabelaResultado.getString("origem")));
		obj.setCodigoOrigem(tabelaResultado.getString("codigoOrigem"));
		obj.setOperacao(OperacaoFuncionalidadeEnum.valueOf(tabelaResultado.getString("operacao")));
		obj.getResponsavel().setCodigo(tabelaResultado.getInt("responsavel"));
		obj.setData(Uteis.getDataJDBCTimestamp(tabelaResultado.getDate("data")));
		obj.setObservacao(tabelaResultado.getString("observacao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			return obj;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
		return obj;
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		OperacaoFuncionalidade.idEntidade = idEntidade;
	}

}
