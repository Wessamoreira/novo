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

import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaItemVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.PerguntaItemInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PerguntaItemVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>PerguntaItemVO</code>. Encapsula toda a interação com
 * o banco de dados.
 * 
 * @see PerguntaItemVO
 * @see ControleAcesso
 * @see Pergunta
 */
@Repository
@Scope("singleton")
@Lazy
public class PerguntaItem extends ControleAcesso implements PerguntaItemInterfaceFacade {

	protected static String idEntidade;

	public PerguntaItem() throws Exception {
		super();
		setIdEntidade("PerguntaItem");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>PerguntaQuestionarioVO</code>.
	 */
	public PerguntaItemVO novo() throws Exception {
		PerguntaItem.incluir(getIdEntidade());
		PerguntaItemVO obj = new PerguntaItemVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>PerguntaItemVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerguntaItemVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PerguntaItemVO obj, UsuarioVO usuarioVO) throws Exception {
		PerguntaItemVO.validarDados(obj);
		final String sql = "INSERT INTO PerguntaItem( perguntaPrincipal, pergunta, ordem ) VALUES ( ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlInserir = con.prepareStatement(sql);
				if (obj.getPerguntaPrincipalVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getPerguntaPrincipalVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getPerguntaVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getPerguntaVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setInt(3, obj.getOrdem());
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
	 * <code>PerguntaItemVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerguntaItemVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PerguntaItemVO obj, UsuarioVO usuarioVO) throws Exception {
		PerguntaItemVO.validarDados(obj);
		final String sql = "UPDATE PerguntaItem set perguntaPrincipal = ?, pergunta=?, ordem = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				if (obj.getPerguntaPrincipalVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getPerguntaPrincipalVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);				
				}
				if (obj.getPerguntaVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getPerguntaVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);				
				}
				sqlAlterar.setInt(3, obj.getOrdem());

				sqlAlterar.setInt(4, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>PerguntaItemVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerguntaItemVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PerguntaItemVO obj, UsuarioVO usuarioVO) throws Exception {
		PerguntaItem.excluir(getIdEntidade());
		String sql = "DELETE FROM PerguntaItem WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}
	
	/**
	 * Operação responsável por incluir objetos da
	 * <code>PerguntaItemVO</code> no BD. Garantindo o relacionamento
	 * com a entidade principal <code>processosel.Pergunta</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPerguntaItens(Integer perguntaPrm, List objetos, UsuarioVO usuarioVO) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			PerguntaItemVO obj = (PerguntaItemVO) e.next();
			obj.getPerguntaPrincipalVO().setCodigo(perguntaPrm);
			obj.getPerguntaVO().setCodigo(obj.getPerguntaVO().getCodigo());
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		}
	}
	
	/**
	 * Operação responsável por consultar todos os
	 * <code>PerguntaItemVO</code> relacionados a um objeto da classe
	 * <code>processosel.Pergunta</code>.
	 * 
	 * @param perguntaPrm
	 *            Atributo de <code>processosel.Pergunta</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>PerguntaItemVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>PerguntaItemVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List consultarPerguntaItens(Integer perguntaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PerguntaItem.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM PerguntaItem WHERE perguntaPrincipal = ? order by ordem";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, perguntaPrm.intValue());
		while (resultado.next()) {
			PerguntaItemVO novoObj = new PerguntaItemVO();
			novoObj = montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}
	
	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>PerguntaItemVO</code>.
	 * 
	 * @return O objeto da classe <code>PerguntaQuestionarioVO</code> com os
	 *         dados devidamente montados.
	 */
	public  PerguntaItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PerguntaItemVO obj = new PerguntaItemVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setOrdem(new Integer(dadosSQL.getInt("ordem")));
		obj.getPerguntaPrincipalVO().setCodigo(new Integer(dadosSQL.getInt("perguntaPrincipal")));;
		obj.getPerguntaVO().setCodigo(new Integer(dadosSQL.getInt("pergunta")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		montarDadosPergunta(obj, nivelMontarDados, usuario);
		montarDadosPerguntaPrincipal(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
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
	public  void montarDadosPergunta(PerguntaItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerguntaVO().getCodigo().intValue() == 0) {
			obj.setPerguntaVO(new PerguntaVO());
			return;
		}
		obj.setPerguntaVO(getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getPerguntaVO().getCodigo(), nivelMontarDados, usuario));
	}
	
	
	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>PerguntaQuestionarioVO</code> contidos em um Hashtable no BD. Faz
	 * uso da operação <code>excluirPerguntaItens</code> e
	 * <code>incluirPerguntaItens</code> disponíveis na classe
	 * <code>PerguntaItem</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPerguntaItens(Integer pergunta, List objetos, UsuarioVO usuarioVO) throws Exception {
		excluirPerguntaItens(pergunta, objetos, usuarioVO);
		incluirPerguntaItens(pergunta, objetos, usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPerguntaItens(Integer pergunta, List objetos, UsuarioVO usuarioVO) throws Exception {
		String sql = "DELETE FROM PerguntaItem WHERE (pergunta = ?)";
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			PerguntaItemVO obj = (PerguntaItemVO) i.next();
			if (obj.getCodigo().intValue() != 0) {
				sql += " and codigo != " + obj.getCodigo().intValue();
			}
		}
		getConexao().getJdbcTemplate().update(sql+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), new Object[] { pergunta.intValue() });
	}
	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return PerguntaItem.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		PerguntaItem.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PerguntaItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuffer str = new StringBuffer();
		str.append(" select * from perguntaitem ");
		str.append(" WHERE perguntaitem.codigo = ").append(codigoPrm);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (PerguntaItemVO).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public  void montarDadosPerguntaPrincipal(PerguntaItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerguntaPrincipalVO().getCodigo().intValue() == 0) {
			obj.setPerguntaPrincipalVO(new PerguntaVO());
			return;
		}
		obj.setPerguntaPrincipalVO((getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getPerguntaPrincipalVO().getCodigo(), nivelMontarDados, usuario)));
	}

}
