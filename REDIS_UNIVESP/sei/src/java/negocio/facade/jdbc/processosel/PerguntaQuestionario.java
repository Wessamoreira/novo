package negocio.facade.jdbc.processosel;

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
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.PerguntaQuestionarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PerguntaQuestionarioVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>PerguntaQuestionarioVO</code>. Encapsula toda a interação com
 * o banco de dados.
 * 
 * @see PerguntaQuestionarioVO
 * @see ControleAcesso
 * @see Questionario
 */
@Repository
@Scope("singleton")
@Lazy
public class PerguntaQuestionario extends ControleAcesso implements PerguntaQuestionarioInterfaceFacade {

	private static final long serialVersionUID = 7469842194855727529L;

	protected static String idEntidade;

	public PerguntaQuestionario() throws Exception {
		super();
		setIdEntidade("Questionario");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>PerguntaQuestionarioVO</code>.
	 */
	public PerguntaQuestionarioVO novo() throws Exception {
		PerguntaQuestionario.incluir(getIdEntidade());
		PerguntaQuestionarioVO obj = new PerguntaQuestionarioVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>PerguntaQuestionarioVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerguntaQuestionarioVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PerguntaQuestionarioVO obj, UsuarioVO usuarioVO) throws Exception {
		PerguntaQuestionarioVO.validarDados(obj);
		final String sql = "INSERT INTO PerguntaQuestionario( questionario, pergunta, respostaObrigatoria, ordem, perguntaRestrita ) VALUES ( ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlInserir = con.prepareStatement(sql);
				if (obj.getQuestionario().intValue() != 0) {
					sqlInserir.setInt(1, obj.getQuestionario().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getPergunta().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getPergunta().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setBoolean(3, obj.getRespostaObrigatoria());
				sqlInserir.setInt(4, obj.getOrdem());
				sqlInserir.setBoolean(5, obj.getPerguntaRestrita());
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
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>PerguntaQuestionarioVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerguntaQuestionarioVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PerguntaQuestionarioVO obj, UsuarioVO usuarioVO) throws Exception {
		PerguntaQuestionarioVO.validarDados(obj);
		final String sql = "UPDATE PerguntaQuestionario set questionario=?, pergunta=?, respostaObrigatoria=?, ordem = ?, perguntaRestrita = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				if (obj.getQuestionario().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getQuestionario().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (obj.getPergunta().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getPergunta().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setBoolean(3, obj.getRespostaObrigatoria());
				sqlAlterar.setInt(4, obj.getOrdem());
				sqlAlterar.setBoolean(5, obj.getPerguntaRestrita());
				sqlAlterar.setInt(6, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>PerguntaQuestionarioVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerguntaQuestionarioVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PerguntaQuestionarioVO obj, UsuarioVO usuarioVO) throws Exception {
		PerguntaQuestionario.excluir(getIdEntidade());
		String sql = "DELETE FROM PerguntaQuestionario WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>PerguntaQuestionario</code> através do valor do atributo
	 * <code>descricao</code> da classe <code>Pergunta</code> Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>PerguntaQuestionarioVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<PerguntaQuestionarioVO> consultarPorDescricaoPergunta(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT PerguntaQuestionario.* FROM PerguntaQuestionario, Pergunta WHERE PerguntaQuestionario.pergunta = Pergunta.codigo and upper( Pergunta.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PerguntaQuestionario.ordem, Pergunta.descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>PerguntaQuestionario</code> através do valor do atributo
	 * <code>descricao</code> da classe <code>Questionario</code> Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>PerguntaQuestionarioVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<PerguntaQuestionarioVO> consultarPorDescricaoQuestionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT PerguntaQuestionario.* FROM PerguntaQuestionario, Questionario WHERE PerguntaQuestionario.questionario = Questionario.codigo and upper( Questionario.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Questionario.descricao, PerguntaQuestionario.ordem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>PerguntaQuestionario</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>PerguntaQuestionarioVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<PerguntaQuestionarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM PerguntaQuestionario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY ordem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}
	
	public List<PerguntaQuestionarioVO> consultarPorCodigoQuestionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM PerguntaQuestionario WHERE questionario = ? order by ordem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.intValue());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>PerguntaQuestionarioVO</code> resultantes da consulta.
	 */
	public static List<PerguntaQuestionarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PerguntaQuestionarioVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>PerguntaQuestionarioVO</code>.
	 * 
	 * @return O objeto da classe <code>PerguntaQuestionarioVO</code> com os
	 *         dados devidamente montados.
	 */
	public static PerguntaQuestionarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PerguntaQuestionarioVO obj = new PerguntaQuestionarioVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setRespostaObrigatoria((dadosSQL.getBoolean("respostaObrigatoria")));
		obj.setPerguntaRestrita((dadosSQL.getBoolean("perguntaRestrita")));
		obj.setQuestionario(new Integer(dadosSQL.getInt("questionario")));
		obj.setOrdem(new Integer(dadosSQL.getInt("ordem")));
		obj.getPergunta().setCodigo(new Integer(dadosSQL.getInt("pergunta")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		montarDadosPergunta(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PerguntaVO</code> relacionado ao objeto
	 * <code>PerguntaQuestionarioVO</code>. Faz uso da chave primária da classe
	 * <code>PerguntaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPergunta(PerguntaQuestionarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPergunta().getCodigo().intValue() == 0) {
			obj.setPergunta(new PerguntaVO());
			return;
		}
		obj.setPergunta(getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getPergunta().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>PerguntaQuestionarioVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe
	 * <code>PerguntaQuestionario</code>.
	 * 
	 * @param <code>questionario</code> campo chave para exclusão dos objetos no
	 *        BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPerguntaQuestionarios(Integer questionario, UsuarioVO usuarioVO) throws Exception {
		PerguntaQuestionario.excluir(getIdEntidade());
		String sql = "DELETE FROM PerguntaQuestionario WHERE (questionario = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, new Object[] { questionario.intValue() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPerguntaQuestionarios(Integer questionario, List objetos, UsuarioVO usuarioVO) throws Exception {
		String sql = "DELETE FROM PerguntaQuestionario WHERE (questionario = ?)";
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) i.next();
			if (obj.getCodigo().intValue() != 0) {
				sql += " and codigo != " + obj.getCodigo().intValue();
			}
		}
		getConexao().getJdbcTemplate().update(sql+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), new Object[] { questionario.intValue() });
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>PerguntaQuestionarioVO</code> contidos em um Hashtable no BD. Faz
	 * uso da operação <code>excluirPerguntaQuestionarios</code> e
	 * <code>incluirPerguntaQuestionarios</code> disponíveis na classe
	 * <code>PerguntaQuestionario</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPerguntaQuestionarios(Integer questionario, List objetos, UsuarioVO usuarioVO) throws Exception {
		excluirPerguntaQuestionarios(questionario, objetos, usuarioVO);
		incluirPerguntaQuestionarios(questionario, objetos, usuarioVO);
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>PerguntaQuestionarioVO</code> no BD. Garantindo o relacionamento
	 * com a entidade principal <code>processosel.Questionario</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPerguntaQuestionarios(Integer questionarioPrm, List objetos, UsuarioVO usuarioVO) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) e.next();
			obj.setQuestionario(questionarioPrm);
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>PerguntaQuestionarioVO</code> relacionados a um objeto da classe
	 * <code>processosel.Questionario</code>.
	 * 
	 * @param questionario
	 *            Atributo de <code>processosel.Questionario</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>PerguntaQuestionarioVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>PerguntaQuestionarioVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static List consultarPerguntaQuestionarios(Integer questionario, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PerguntaQuestionario.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM PerguntaQuestionario WHERE questionario = ? order by ordem";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, questionario.intValue());
		while (resultado.next()) {
			PerguntaQuestionarioVO novoObj = new PerguntaQuestionarioVO();
			novoObj = PerguntaQuestionario.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>PerguntaQuestionarioVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public PerguntaQuestionarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
		String sql = "SELECT * FROM PerguntaQuestionario WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( PerguntaQuestionario ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return PerguntaQuestionario.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		PerguntaQuestionario.idEntidade = idEntidade;
	}
}
