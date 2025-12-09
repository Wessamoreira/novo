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
import negocio.comuns.financeiro.CartaCobrancaAlunoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CartaCobrancaAlunoInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class CartaCobrancaAluno extends ControleAcesso implements CartaCobrancaAlunoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public CartaCobrancaAluno() throws Exception {
		super();
		setIdEntidade("CartaCobrancaAluno");
	}

	public void setIdEntidade(String idEntidade) {
		CartaCobrancaAluno.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluir(final CartaCobrancaAlunoVO obj) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle Comentado 23/10/2014
			 */
			// CartaCobranca.incluir(getIdEntidade());
			final String sql = "INSERT INTO cartacobrancaaluno( aluno, matricula, cartacobranca, tipoPessoa, responsavelFinanceiro, contas ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getAluno());
					sqlInserir.setString(2, obj.getMatricula());
					sqlInserir.setInt(3, obj.getCartacobranca());
					sqlInserir.setString(4, obj.getTipoPessoa());
					sqlInserir.setString(5, obj.getResponsavelFinanceiro());
					sqlInserir.setString(6, obj.getContas());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
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

	@Override
	public List<CartaCobrancaAlunoVO> consultarPorCartaCobranca(Integer codigoCarta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cartacobrancaaluno.* from cartacobrancaaluno");
		sql.append(" inner join cartacobranca on cartacobrancaaluno.cartacobranca=cartacobranca.codigo");
		sql.append(" WHERE  cartacobranca.codigo=").append(codigoCarta);
		sql.append(" order by cartacobrancaaluno.aluno");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
	}

	public static List<CartaCobrancaAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CartaCobrancaAlunoVO> vetResultado = new ArrayList<CartaCobrancaAlunoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return vetResultado;
	}

	public static CartaCobrancaAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		CartaCobrancaAlunoVO obj = new CartaCobrancaAlunoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setAluno(dadosSQL.getString("aluno"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setCartacobranca(dadosSQL.getInt("cartaCobranca"));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro"));
		obj.setContas(dadosSQL.getString("contas"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}
}
