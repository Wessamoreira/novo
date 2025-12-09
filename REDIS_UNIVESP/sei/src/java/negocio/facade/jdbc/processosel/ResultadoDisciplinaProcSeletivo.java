package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.ResultadoDisciplinaProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ResultadoDisciplinaProcSeletivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ResultadoDisciplinaProcSeletivoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ResultadoDisciplinaProcSeletivoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ResultadoDisciplinaProcSeletivoVO
 * @see ControleAcesso
 * @see ResultadoProcessoSeletivo
 */
@Repository
public class ResultadoDisciplinaProcSeletivo extends ControleAcesso implements ResultadoDisciplinaProcSeletivoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ResultadoDisciplinaProcSeletivo() throws Exception {
		super();
		setIdEntidade("ResultadoProcessoSeletivo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>.
	 */
	public ResultadoDisciplinaProcSeletivoVO novo() throws Exception {
		ResultadoDisciplinaProcSeletivo.incluir(getIdEntidade());
		ResultadoDisciplinaProcSeletivoVO obj = new ResultadoDisciplinaProcSeletivoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ResultadoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ResultadoDisciplinaProcSeletivoVO.validarDados(obj);
			final String sql = "INSERT INTO ResultadoDisciplinaProcSeletivo( resultadoProcessoSeletivo, disciplinaProcSeletivo, nota, observacoes, quantidadeAcertos, variavelNota ) VALUES ( ?, ?, ?, ?, ?, ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getResultadoProcessoSeletivo().intValue());
					sqlInserir.setInt(2, obj.getDisciplinaProcSeletivo().getCodigo().intValue());
					sqlInserir.setDouble(3, obj.getNota().doubleValue());
					sqlInserir.setString(4, obj.getObservacoes());
					sqlInserir.setInt(5, obj.getQuantidadeAcertos());
					sqlInserir.setString(6, obj.getVariavelNota());
					return sqlInserir;
				}
			});
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ResultadoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		ResultadoDisciplinaProcSeletivoVO.validarDados(obj);
		final String sql = "UPDATE ResultadoDisciplinaProcSeletivo set nota=?, observacoes=?, quantidadeAcertos=?, variavelNota=? WHERE ((resultadoProcessoSeletivo = ?) and (disciplinaProcSeletivo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setDouble(1, obj.getNota().doubleValue());
				sqlAlterar.setString(2, obj.getObservacoes());
				sqlAlterar.setInt(3, obj.getResultadoProcessoSeletivo().intValue());
				sqlAlterar.setInt(4, obj.getQuantidadeAcertos());
				sqlAlterar.setString(5, obj.getVariavelNota());
				sqlAlterar.setInt(6, obj.getDisciplinaProcSeletivo().getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar
	 * esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ResultadoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		ResultadoDisciplinaProcSeletivo.excluir(getIdEntidade());
		String sql = "DELETE FROM ResultadoDisciplinaProcSeletivo WHERE ((resultadoProcessoSeletivo = ?) and (disciplinaProcSeletivo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, obj.getResultadoProcessoSeletivo().intValue(), obj.getDisciplinaProcSeletivo().getCodigo().intValue());
	}

	/**
	 * Responsável por realizar uma consulta de <code>ResultadoDisciplinaProcSeletivo</code> através do valor do atributo <code>nome</code> da classe
	 * <code>DisciplinasProcSeletivo</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ResultadoDisciplinaProcSeletivoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ResultadoDisciplinaProcSeletivoVO> consultarPorNomeDisciplinasProcSeletivo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT ResultadoDisciplinaProcSeletivo.* FROM ResultadoDisciplinaProcSeletivo, DisciplinasProcSeletivo WHERE ResultadoDisciplinaProcSeletivo.disciplinaProcSeletivo = DisciplinasProcSeletivo.codigo and DisciplinasProcSeletivo.nome like('" + valorConsulta + "%') ORDER BY DisciplinasProcSeletivo.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuarioLogado);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ResultadoDisciplinaProcSeletivoVO</code> resultantes da consulta.
	 */
	public List<ResultadoDisciplinaProcSeletivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioLogado) throws Exception {
		List<ResultadoDisciplinaProcSeletivoVO> vetResultado = new ArrayList<ResultadoDisciplinaProcSeletivoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuarioLogado));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ResultadoDisciplinaProcSeletivoVO</code>.
	 * 
	 * @return O objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> com os dados devidamente montados.
	 */
	public ResultadoDisciplinaProcSeletivoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioLogado) throws Exception {
		ResultadoDisciplinaProcSeletivoVO obj = new ResultadoDisciplinaProcSeletivoVO();
		obj.setResultadoProcessoSeletivo(new Integer(dadosSQL.getInt("resultadoProcessoSeletivo")));
		obj.getDisciplinaProcSeletivo().setCodigo(new Integer(dadosSQL.getInt("disciplinaProcSeletivo")));
		obj.setNota(new Double(dadosSQL.getDouble("nota")));
		obj.setQuantidadeAcertos(new Integer(dadosSQL.getInt("quantidadeAcertos")));
		obj.setObservacoes(dadosSQL.getString("observacoes"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setVariavelNota(dadosSQL.getString("variavelNota"));
		montarDadosDisciplinaProcSeletivo(obj, usuarioLogado);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>DisciplinasProcSeletivoVO</code> relacionado ao objeto
	 * <code>ResultadoDisciplinaProcSeletivoVO</code>. Faz uso da chave primária da classe <code>DisciplinasProcSeletivoVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosDisciplinaProcSeletivo(ResultadoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioLogado) throws Exception {
		if (obj.getDisciplinaProcSeletivo().getCodigo().intValue() == 0) {
			obj.setDisciplinaProcSeletivo(new DisciplinasProcSeletivoVO());
			return;
		}
		obj.setDisciplinaProcSeletivo(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(obj.getDisciplinaProcSeletivo().getCodigo(), usuarioLogado));
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>ResultadoDisciplinaProcSeletivoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>ResultadoDisciplinaProcSeletivo</code>.
	 * 
	 * @param <code>resultadoProcessoSeletivo</code>
	 *            campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivo, UsuarioVO usuarioVO) throws Exception {
		ResultadoDisciplinaProcSeletivo.excluir(getIdEntidade());
		String sql = "DELETE FROM ResultadoDisciplinaProcSeletivo WHERE (resultadoProcessoSeletivo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, resultadoProcessoSeletivo.intValue());
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>ResultadoDisciplinaProcSeletivoVO</code> contidos em um Hashtable no BD. Faz uso da
	 * operação <code>excluirResultadoDisciplinaProcSeletivos</code> e <code>incluirResultadoDisciplinaProcSeletivos</code> disponíveis na classe
	 * <code>ResultadoDisciplinaProcSeletivo</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivo, List<ResultadoDisciplinaProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception {
		excluirResultadoDisciplinaProcSeletivos(resultadoProcessoSeletivo, usuarioVO);
		incluirResultadoDisciplinaProcSeletivos(resultadoProcessoSeletivo, objetos, usuarioVO);
	}

	/**
	 * Operação responsável por incluir objetos da <code>ResultadoDisciplinaProcSeletivoVO</code> no BD. Garantindo o relacionamento com a entidade
	 * principal <code>processosel.ResultadoProcessoSeletivo</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivoPrm, List<ResultadoDisciplinaProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception {
		Iterator<ResultadoDisciplinaProcSeletivoVO> e = objetos.iterator();
		while (e.hasNext()) {
			ResultadoDisciplinaProcSeletivoVO obj = (ResultadoDisciplinaProcSeletivoVO) e.next();
			obj.setResultadoProcessoSeletivo(resultadoProcessoSeletivoPrm);
			incluir(obj, usuarioVO);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ResultadoDisciplinaProcSeletivoVO</code> relacionados a um objeto da classe
	 * <code>processosel.ResultadoProcessoSeletivo</code>.
	 * 
	 * @param resultadoProcessoSeletivo
	 *            Atributo de <code>processosel.ResultadoProcessoSeletivo</code> a ser utilizado para localizar os objetos da classe
	 *            <code>ResultadoDisciplinaProcSeletivoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ResultadoDisciplinaProcSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Override
	public List<ResultadoDisciplinaProcSeletivoVO> consultarResultadoDisciplinaProcSeletivos(Integer resultadoProcessoSeletivo, UsuarioVO usuarioLogado) throws Exception {
		ResultadoDisciplinaProcSeletivo.consultar(getIdEntidade());
		String sql = "SELECT * FROM ResultadoDisciplinaProcSeletivo WHERE resultadoProcessoSeletivo = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, resultadoProcessoSeletivo.intValue());
		return montarDadosConsulta(resultado, usuarioLogado);
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ResultadoDisciplinaProcSeletivoVO consultarPorChavePrimaria(Integer resultadoProcessoSeletivoPrm, Integer disciplinaProcSeletivoPrm, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		String sql = "SELECT * FROM ResultadoDisciplinaProcSeletivo WHERE resultadoProcessoSeletivo = ? and disciplinaProcSeletivo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, resultadoProcessoSeletivoPrm.intValue(), disciplinaProcSeletivoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuarioLogado));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return ResultadoDisciplinaProcSeletivo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ResultadoDisciplinaProcSeletivo.idEntidade = idEntidade;
	}

}
