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
import negocio.comuns.financeiro.ParceiroUnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ParceiroUnidadeEnsinoContaCorrenteInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ParceiroUnidadeEnsinoContaCorrente extends ControleAcesso implements ParceiroUnidadeEnsinoContaCorrenteInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ParceiroUnidadeEnsinoContaCorrente() throws Exception {
		super();
		setIdEntidade("ParceiroUnidadeEnsinoContaCorrente");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ParceiroUnidadeEnsinoContaCorrenteVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ParceiroUnidadeEnsinoContaCorrenteVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);	
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistir(ParceiroUnidadeEnsinoContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ParceiroUnidadeEnsinoContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ParceiroUnidadeEnsinoContaCorrente.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO parceiroUnidadeEnsinoContaCorrente (parceiro, unidadeEnsino, contaCorrente) ");
			sql.append("    VALUES ( ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getParceiroVO().getCodigo());
					sqlInserir.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setInt(++i, obj.getContaCorrenteVO().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ParceiroUnidadeEnsinoContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ParceiroUnidadeEnsinoContaCorrente.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE parceiroUnidadeEnsinoContaCorrente ");
			sql.append("   SET parceiro=?, unidadeEnsino=?, contaCorrente=? ");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;					
					sqlAlterar.setInt(++i, obj.getParceiroVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getUnidadeEnsinoVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getContaCorrenteVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ParceiroUnidadeEnsinoContaCorrenteVO> consultaRapidaPorParceiro(ParceiroVO obj, UsuarioVO usuario) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE parceiro.codigo = ").append(obj.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ParceiroUnidadeEnsinoContaCorrenteVO> lista = new ArrayList<ParceiroUnidadeEnsinoContaCorrenteVO>();
		while (tabelaResultado.next()) {
			lista.add(montarDadosBasico(tabelaResultado));
		}
		return lista;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ParceiroUnidadeEnsinoContaCorrenteVO montarDadosBasico(SqlRowSet dadosSQL) throws Exception {		
		ParceiroUnidadeEnsinoContaCorrenteVO obj = new ParceiroUnidadeEnsinoContaCorrenteVO();
		obj.setCodigo(dadosSQL.getInt("puecc.codigo"));
		obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro.codigo"));
		obj.getParceiroVO().setNome(dadosSQL.getString("parceiro.nome"));

		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getUnidadeEnsinoVO().setAbreviatura(dadosSQL.getString("unidadeensino.abreviatura"));
		
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contacorrente.codigo"));
		obj.getContaCorrenteVO().setNumero(dadosSQL.getString("contacorrente.numero"));
		obj.getContaCorrenteVO().setDigito(dadosSQL.getString("contacorrente.digito"));
		obj.getContaCorrenteVO().setCarteira(dadosSQL.getString("contacorrente.carteira"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("banco.codigo"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNome(dadosSQL.getString("banco.nome"));
		obj.getContaCorrenteVO().getAgencia().setCodigo(dadosSQL.getInt("agencia.codigo"));
		obj.getContaCorrenteVO().getAgencia().setNumeroAgencia(dadosSQL.getString("agencia.numeroagencia"));
		obj.getContaCorrenteVO().getAgencia().setDigito(dadosSQL.getString("agencia.digito"));
		
		return obj;
	}
	

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer(" SELECT puecc.codigo as \"puecc.codigo\",  ");
		sql.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.nome as \"parceiro.nome\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", unidadeensino.abreviatura as \"unidadeensino.abreviatura\",  ");
		sql.append(" contacorrente.codigo as \"contacorrente.codigo\", contacorrente.numero as \"contacorrente.numero\", contacorrente.digito as \"contacorrente.digito\", contacorrente.carteira as \"contacorrente.carteira\", ");	
		sql.append(" banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\",  ");	
		sql.append(" agencia.codigo as \"agencia.codigo\", agencia.numeroagencia as \"agencia.numeroagencia\", agencia.digito as \"agencia.digito\" ");	
		sql.append(" FROM parceiroUnidadeEnsinoContaCorrente puecc ");
		sql.append(" INNER JOIN parceiro on parceiro.codigo = puecc.parceiro");
		sql.append(" INNER JOIN unidadeensino on unidadeensino.codigo = puecc.unidadeensino");
		sql.append(" INNER JOIN contacorrente on contacorrente.codigo = puecc.contacorrente ");
		sql.append(" inner join agencia on agencia.codigo = contacorrente.agencia ");
		sql.append(" inner join banco on banco.codigo = agencia.banco ");
		return sql;
	}
	
	
	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return ParceiroUnidadeEnsinoContaCorrente.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ParceiroUnidadeEnsinoContaCorrente.idEntidade = idEntidade;
	}

}
