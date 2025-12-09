package negocio.facade.jdbc.arquitetura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

import negocio.comuns.arquitetura.SimularAcessoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.SimularAcessoAlunoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class SimularAcessoAluno extends ControleAcesso implements SimularAcessoAlunoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -242106456962783487L;
	protected static String idEntidade;

	public SimularAcessoAluno() throws Exception {
		super();
		setIdEntidade("SimulacaoAcessoAluno");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SimularAcessoAlunoVO obj, UsuarioVO usuario) throws Exception {
		incluir(obj, true, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SimularAcessoAlunoVO obj, final boolean verificarPermissao, UsuarioVO usuario)
			throws Exception {
		try {
			SimularAcessoAluno.incluir(getIdEntidade(), verificarPermissao, usuario);
			SimularAcessoAlunoVO.validarDados(obj);
			final String sql = "INSERT INTO SimularAcessoAluno( dataSimulacao, usuarioSimulado, responsavelSimulacaoAluno) VALUES ( ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataSimulacao()));
					sqlInserir.setInt(2, obj.getUsuarioSimulado().getCodigo());
					sqlInserir.setInt(3, obj.getResponsavelSimulacaoAluno().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final SimularAcessoAlunoVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, true, usuario);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>UsuarioVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>UsuarioVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final SimularAcessoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			SimularAcessoAluno.alterar(getIdEntidade(), verificarAcesso, usuario);
			SimularAcessoAlunoVO.validarDados(obj);

			final String sql = "UPDATE SimularAcessoAluno set dataSimulacao=?, usuarioSimulado=?, responsavelSimulacaoAluno=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataSimulacao()));
					sqlAlterar.setInt(2, obj.getUsuarioSimulado().getCodigo());
					sqlAlterar.setInt(3, obj.getResponsavelSimulacaoAluno().getCodigo());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	public SimularAcessoAlunoVO consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getPadraoSqlConsultaSimularAcessoAluno();
        sqlStr.append(" WHERE simularacessoaluno.codigo = ").append(valorConsulta);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        SimularAcessoAlunoVO obj = new SimularAcessoAlunoVO();
        while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("simularacessoaluno.codigo"));
			obj.setDataSimulacao(tabelaResultado.getDate("simularacessoaluno.datasimulacao"));
			obj.setUsuarioSimulado(new UsuarioVO());
			obj.getUsuarioSimulado().setCodigo(tabelaResultado.getInt("usu_simulado.codigo"));
			obj.getUsuarioSimulado().setUsername(tabelaResultado.getString("usu_simulado.username"));
			obj.getUsuarioSimulado().setNome(tabelaResultado.getString("usu_simulado.nome"));
			obj.getUsuarioSimulado().setSenha(tabelaResultado.getString("usu_simulado.senha"));
			obj.getUsuarioSimulado().setTipoUsuario(tabelaResultado.getString("usu_simulado.tipousuario"));
			obj.setResponsavelSimulacaoAluno(new UsuarioVO());
			obj.getResponsavelSimulacaoAluno().setCodigo(tabelaResultado.getInt("usu_responsavel.codigo"));
			obj.getResponsavelSimulacaoAluno().setUsername(tabelaResultado.getString("usu_responsavel.username"));
			obj.getResponsavelSimulacaoAluno().setNome(tabelaResultado.getString("usu_responsavel.nome"));
			obj.getResponsavelSimulacaoAluno().setSenha(tabelaResultado.getString("usu_responsavel.senha"));
			obj.getResponsavelSimulacaoAluno().setTipoUsuario(tabelaResultado.getString("usu_responsavel.tipousuario"));
		}
        return obj;
    }
	
	/**
	 * Padrao Sql para utilizacao nas consultas aos funcionarios
	 * @return
	 */
	public StringBuilder getPadraoSqlConsultaSimularAcessoAluno() {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT simularacessoaluno.codigo \"simularacessoaluno.codigo\" ,");
		sqlStr.append(" simularacessoaluno.datasimulacao \"simularacessoaluno.datasimulacao\" ,");
		sqlStr.append(" usu_simulado.codigo \"usu_simulado.codigo\" ,");
		sqlStr.append(" usu_simulado.username \"usu_simulado.username\" ,");
		sqlStr.append(" usu_simulado.nome \"usu_simulado.nome\" ,");
		sqlStr.append(" usu_simulado.senha\"usu_simulado.senha\" ,");
		sqlStr.append(" usu_simulado.tipousuario \"usu_simulado.tipousuario\" ,");
		sqlStr.append(" usu_responsavel.codigo \"usu_responsavel.codigo\" ,");
		sqlStr.append(" usu_responsavel.username \"usu_responsavel.username\" ,");
		sqlStr.append(" usu_responsavel.nome \"usu_responsavel.nome\" ,");
		sqlStr.append(" usu_responsavel.senha\"usu_responsavel.senha\" ,");
		sqlStr.append(" usu_responsavel.tipousuario \"usu_responsavel.tipousuario\" ");

		sqlStr.append(" FROM simularacessoaluno  simularacessoaluno ");
		sqlStr.append(" inner join usuario usu_simulado on simularacessoaluno.usuariosimulado = usu_simulado.codigo ");
		sqlStr.append(" inner join usuario usu_responsavel on simularacessoaluno.responsavelSimulacaoAluno = usu_responsavel.codigo ");
		
		return sqlStr;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>UsuarioVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	@Override
	public SimularAcessoAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM SimularAcessoAluno WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql,
				new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados. (Simulação Acesso Aluno)");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}
	
	

	public static SimularAcessoAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		SimularAcessoAlunoVO obj = new SimularAcessoAlunoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDataSimulacao(dadosSQL.getDate("dataSimulacao"));
		obj.setUsuarioSimulado(new UsuarioVO());
		obj.getUsuarioSimulado().setCodigo(dadosSQL.getInt("usuarioSimulado"));
		obj.setResponsavelSimulacaoAluno(new UsuarioVO());
		obj.getResponsavelSimulacaoAluno().setCodigo(dadosSQL.getInt("responsavelSimulacaoAluno"));
		return obj;
	}
	
	

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return SimularAcessoAluno.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		SimularAcessoAluno.idEntidade = idEntidade;
	}

}
