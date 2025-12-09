package negocio.facade.jdbc.biblioteca;

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
import negocio.comuns.biblioteca.CidadePublicacaoCatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.CidadePublicacaoCatalogoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CidadePublicacaoCatalogoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>CidadePublicacaoCatalogoVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see CidadePublicacaoCatalogoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class CidadePublicacaoCatalogo extends ControleAcesso implements CidadePublicacaoCatalogoInterfaceFacade {

	protected static String idEntidade;

	public CidadePublicacaoCatalogo() throws Exception {
		super();
		setIdEntidade("CidadePublicacaoCatalogo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>CidadePublicacaoCatalogoVO</code>.
	 */
	public CidadePublicacaoCatalogoVO novo() throws Exception {
		CidadePublicacaoCatalogo.incluir(getIdEntidade());
		CidadePublicacaoCatalogoVO obj = new CidadePublicacaoCatalogoVO();
		return obj;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CidadePublicacaoCatalogoVO</code>. Todos os tipos de consistência
	 * de dados são e devem ser implementadas neste método. São validações
	 * típicas: verificação de campos obrigatórios, verificação de valores
	 * válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(CidadePublicacaoCatalogoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Cidade Publicação) deve ser informado.");
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CidadePublicacaoCatalogoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CidadePublicacaoCatalogoVO</code> que
	 *            será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CidadePublicacaoCatalogoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
//			CidadePublicacaoCatalogo.incluir(getIdEntidade());

			final String sql = "INSERT INTO CidadePublicacaoCatalogo( nome, estado, pais ) VALUES ( ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getEstado());
					sqlInserir.setString(3, obj.getPais());
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

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>CidadePublicacaoCatalogoVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CidadePublicacaoCatalogoVO</code> que
	 *            será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CidadePublicacaoCatalogoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			CidadePublicacaoCatalogo.alterar(getIdEntidade());

			final String sql = "UPDATE CidadePublicacaoCatalogo set nome=?, estado=?, pais=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getEstado());
					sqlAlterar.setString(3, obj.getPais());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>CidadePublicacaoCatalogoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CidadePublicacaoCatalogoVO</code> que
	 *            será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CidadePublicacaoCatalogoVO obj, UsuarioVO usuario) throws Exception {
		try {
			CidadePublicacaoCatalogo.excluir(getIdEntidade());
			String sql = "DELETE FROM CidadePublicacaoCatalogo WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CidadePublicacaoCatalogo</code> através do valor do atributo
	 * <code>String nome</code>. Retorna os objetos, com início do valor do
	 * atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CidadePublicacaoCatalogoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CidadePublicacaoCatalogo WHERE sem_acentos(nome)  ilike (sem_acentos(?)) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>CidadePublicacaoCatalogo</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CidadePublicacaoCatalogoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CidadePublicacaoCatalogo WHERE codigo >= " + valorConsulta.intValue()
				+ " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>CidadePublicacaoCatalogoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>CidadePublicacaoCatalogoVO</code>.
	 * 
	 * @return O objeto da classe <code>CidadePublicacaoCatalogoVO</code> com os
	 *         dados devidamente montados.
	 */
	public static CidadePublicacaoCatalogoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		CidadePublicacaoCatalogoVO obj = new CidadePublicacaoCatalogoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setEstado(dadosSQL.getString("estado"));
		obj.setPais(dadosSQL.getString("pais"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>CidadePublicacaoCatalogoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public CidadePublicacaoCatalogoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM CidadePublicacaoCatalogo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}
	
    public CidadePublicacaoCatalogoVO consultarPorNomeRegistroUnico(String nome, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT * FROM cidadepublicacaocatalogo WHERE sem_acentos(nome) ilike sem_acentos('%").append(nome).append("%') LIMIT 1 ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!rs.next()) {
            return new CidadePublicacaoCatalogoVO();
        }
        return montarDados(rs, nivelMontarDados);
    }

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CidadePublicacaoCatalogo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CidadePublicacaoCatalogo.idEntidade = idEntidade;
	}
}