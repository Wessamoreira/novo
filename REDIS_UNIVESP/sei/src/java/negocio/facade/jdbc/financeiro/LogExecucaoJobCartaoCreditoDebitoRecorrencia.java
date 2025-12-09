package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CategoriaDescontoInterfaceFacade;
import negocio.interfaces.financeiro.LogExecucaoJobCartaoCreditoDebitoRecorrenciaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MotivoCancelamentoTrancamentoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>MotivoCancelamentoTrancamentoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see MotivoCancelamentoTrancamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class LogExecucaoJobCartaoCreditoDebitoRecorrencia extends ControleAcesso implements LogExecucaoJobCartaoCreditoDebitoRecorrenciaInterfaceFacade {

	protected static String idEntidade;

	public LogExecucaoJobCartaoCreditoDebitoRecorrencia() throws Exception {
		super();
		setIdEntidade("CategoriaDesconto");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>MotivoCancelamentoTrancamentoVO</code>.
	 */
	public LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO novo() throws Exception {
		LogExecucaoJobCartaoCreditoDebitoRecorrencia.incluir(getIdEntidade());
		LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO obj = new LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			
			final String sql = "INSERT INTO LogExecucaoJobCartaoCreditoDebitoRecorrencia( cartaoCreditoDebitoRecorrenciaPessoa, contaReceber, nossoNumero, parcela, matriculaPeriodo, tipoOrigem, dataVencimento, data, "
					+ "execucaoManual, responsavel, observacao, erro ) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo())) {
						sqlInserir.setInt(1, obj.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getContaReceber())) {
						sqlInserir.setInt(2, obj.getContaReceber());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getNossoNumero());
					sqlInserir.setString(4, obj.getParcela());
					if (Uteis.isAtributoPreenchido(obj.getMatriculaPeriodo())) {
						sqlInserir.setInt(5, obj.getMatriculaPeriodo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, obj.getTipoOrigem());
					if (Uteis.isAtributoPreenchido(obj.getDataVencimento())) {
						sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataVencimento()));
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setBoolean(9, obj.getExecucaoManual());
					if (Uteis.isAtributoPreenchido(obj.getResponsavelVO().getCodigo())) {
						sqlInserir.setInt(10, obj.getResponsavelVO().getCodigo());
					} else {
						sqlInserir.setNull(10, 0);
					}
					sqlInserir.setString(11, obj.getObservacao());
					sqlInserir.setBoolean(12, obj.getErro());
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
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	public static LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO obj = new LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static String getIdEntidade() {
		return LogExecucaoJobCartaoCreditoDebitoRecorrencia.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		LogExecucaoJobCartaoCreditoDebitoRecorrencia.idEntidade = idEntidade;
	}

	@Override
	public LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO inicializarDadosLogExecucaoJobCartaoCredito(CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO, ContaReceberVO contaReceberVO, Boolean erro, String observacao, Boolean execucaoManual, UsuarioVO usuarioVO) {
		LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO obj = new LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO();
		obj.getCartaoCreditoDebitoRecorrenciaPessoaVO().setCodigo(cartaoCreditoDebitoRecorrenciaPessoaVO.getCodigo());
		obj.getCartaoCreditoDebitoRecorrenciaPessoaVO().getMatriculaVO().setMatricula(cartaoCreditoDebitoRecorrenciaPessoaVO.getMatriculaVO().getMatricula());
		obj.setExecucaoManual(execucaoManual);
		obj.setErro(erro);
		obj.setObservacao(observacao);
		obj.getResponsavelVO().setCodigo(usuarioVO.getCodigo());
		obj.getResponsavelVO().setNome(usuarioVO.getNome());
		if (Uteis.isAtributoPreenchido(contaReceberVO)) {
			obj.setContaReceber(contaReceberVO.getCodigo());
			obj.setNossoNumero(contaReceberVO.getNossoNumero());
			obj.setParcela(contaReceberVO.getParcela());
			obj.setMatriculaPeriodo(contaReceberVO.getMatriculaPeriodo());
			obj.setTipoOrigem(contaReceberVO.getTipoOrigem());
			obj.setDataVencimento(contaReceberVO.getDataVencimento());
		}
		return obj;
	}

}
