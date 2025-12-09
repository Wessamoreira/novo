package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.ArtefatoEntregaAlunoVO;
import negocio.comuns.academico.NivelControleArtefatoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ArtefatoEntregaAlunoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ArtefatoEntregaAlunoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ArtefatoEntregaAlunoVO</code>. Encapsula toda a interação com
 * o banco de dados.
 * 
 * @see ArtefatoEntregaAlunoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ArtefatoEntregaAluno extends ControleAcesso implements ArtefatoEntregaAlunoInterfaceFacade {

	protected static String idEntidade;
	public static final long serialVersionUID = 1L;

	public ArtefatoEntregaAluno() throws Exception {
		super();

	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>.
	 */
	public ArtefatoEntregaAlunoVO novo() throws Exception {
		ArtefatoEntregaAluno.incluir(getIdEntidade());
		ArtefatoEntregaAlunoVO obj = new ArtefatoEntregaAlunoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception {
		try {

			ArtefatoEntregaAluno.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO artefato(nome, nivelControle, periodicidadeCurso, trazerAlunoPreMatricula, situacao, scriptsRegraRestricaoAlunos ) VALUES (?, ?, ?, ?, ?, ?) returning codigo";

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getNivelControle());
					sqlInserir.setString(3, obj.getPeriodicidadeCurso());
					sqlInserir.setBoolean(4, obj.getTrazerAlunoPreMatricula());
					sqlInserir.setString(5, obj.getSituacao());
					sqlInserir.setString(6, obj.getScriptsRegraRestricaoAluno());
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
			new NivelControleArtefato().incluirNivelControleArtefato(obj);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception {
		try {

			ArtefatoEntregaAluno.alterar(getIdEntidade());
			final String sql = "UPDATE artefato set nome=?, nivelControle=?, periodicidadeCurso=?, trazerAlunoPreMatricula=?, situacao=?, scriptsRegraRestricaoAlunos=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getNivelControle());
					sqlAlterar.setString(3, obj.getPeriodicidadeCurso());
					sqlAlterar.setBoolean(4, obj.getTrazerAlunoPreMatricula());
					sqlAlterar.setString(5, obj.getSituacao());
					sqlAlterar.setString(6, obj.getScriptsRegraRestricaoAluno());
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

			getFacadeFactory().getNivelControleArtefatoFacade().alterarNivelControleArtefato(obj, usuario);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception {
		try {
			ArtefatoEntregaAluno.excluir(getIdEntidade());
			getFacadeFactory().getNivelControleArtefatoFacade().excluirNivelControleArtefato(obj, usuario);
			String sql = "DELETE FROM artefato WHERE ((codigo = ?))"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ArtefatoEntregaAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		ArtefatoEntregaAluno.consultar(getIdEntidade(), false, usuario);		
		String sql = "SELECT * FROM artefato WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Curso).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ArtefatoEntregaAlunoVO</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os objetos
	 * com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		ArtefatoEntregaAluno.consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT * ");
		sqlStr.append("FROM artefato ");
		sqlStr.append("WHERE codigo >=  ").append(valorConsulta.intValue());
		sqlStr.append(" ORDER BY codigo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ArtefatoEntregaAluno</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		ArtefatoEntregaAluno.consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT * ");
		sqlStr.append("FROM artefato ");
		sqlStr.append("WHERE upper( nome ) like('%").append(valorConsulta.toUpperCase()).append("%') ");
		sqlStr.append(" ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ArtefatoEntregaAluno</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarArtefatosAtivosPorUnidadeEnsino(Integer codigoFuncionario, UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		ArtefatoEntregaAluno.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");		
		sqlStr.append("SELECT artefato.* ");
		sqlStr.append("FROM artefato ");		
		sqlStr.append(" where artefato.situacao = 'AT'");
		if(Uteis.isAtributoPreenchido(codigoFuncionario)) {
			sqlStr.append(" AND (NOT EXISTS (SELECT artefato FROM nivelcontroleartefato WHERE artefato.codigo=nivelcontroleartefato.artefato and tipo = 'FUN' ) ");
			sqlStr.append(" OR EXISTS (SELECT artefato FROM nivelcontroleartefato WHERE artefato.codigo=nivelcontroleartefato.artefato AND tipo = 'FUN' AND funcionario= ").append(codigoFuncionario);
			sqlStr.append("))");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {			
			sqlStr.append(" AND (NOT EXISTS (SELECT artefato FROM nivelcontroleartefato WHERE artefato.codigo=nivelcontroleartefato.artefato and tipo = 'UNE' ) ");
			sqlStr.append(" OR EXISTS (SELECT artefato FROM nivelcontroleartefato WHERE artefato.codigo=nivelcontroleartefato.artefato AND tipo = 'UNE' AND unidadeensino= ").append(unidadeEnsino.getCodigo());
			sqlStr.append("))");
		}
		sqlStr.append(" ORDER BY artefato.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	/**
	 * Responsável por realizar uma consulta de <code>ArtefatoEntregaAluno</code>
	 * ativos</code>. Retorna os objetos.
	 * Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarArtefatosAtivos(Integer codigoFuncionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		ArtefatoEntregaAluno.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");		
		sqlStr.append("SELECT * ");
		sqlStr.append("FROM artefato ");
		sqlStr.append(" WHERE artefato.situacao = 'AT'");
		sqlStr.append(" AND NOT EXISTS (SELECT artefato FROM nivelcontroleartefato WHERE artefato.codigo=nivelcontroleartefato.artefato and tipo = 'FUN') ");
		if(codigoFuncionario > 0) {
			sqlStr.append(" OR EXISTS (SELECT artefato FROM nivelcontroleartefato WHERE artefato.codigo=nivelcontroleartefato.artefato AND tipo = 'FUN' AND funcionario= ").append(codigoFuncionario);
			sqlStr.append(")");
		}
		sqlStr.append(" ORDER BY artefato.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>.
	 * 
	 * @return O objeto da classe <code>ArtefatoEntregaAlunoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ArtefatoEntregaAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {

		ArtefatoEntregaAlunoVO obj = new ArtefatoEntregaAlunoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setNivelControle(dadosSQL.getString("nivelControle"));
		obj.setPeriodicidadeCurso(dadosSQL.getString("periodicidadeCurso"));
		obj.setTrazerAlunoPreMatricula(dadosSQL.getBoolean("trazerAlunoPreMatricula"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setScriptsRegraRestricaoAluno(dadosSQL.getString("scriptsRegraRestricaoAlunos"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		List<NivelControleArtefatoVO> nivelControleArtefatoVOs = getFacadeFactory().getNivelControleArtefatoFacade()
				.consultarNivelControleArtefato(obj.getCodigo(), nivelMontarDados, usuario);

		for (NivelControleArtefatoVO nivelControleArtefatoVO : nivelControleArtefatoVOs) {
			if (nivelControleArtefatoVO.getTipo().equals("UNE")) {
				obj.getNivelControleArtefatoUnidadeEnsinoVOs().add(nivelControleArtefatoVO);
			} else if (nivelControleArtefatoVO.getTipo().equals("CUR")) {
				obj.getNivelControleArtefatoCursoVOs().add(nivelControleArtefatoVO);
			} else if (nivelControleArtefatoVO.getTipo().equals("DIS")) {
				obj.getNivelControleArtefatoDisciplinaVOs().add(nivelControleArtefatoVO);
			} else if (nivelControleArtefatoVO.getTipo().equals("FUN")) {
				obj.getNivelControleArtefatoFuncionarioVOs().add(nivelControleArtefatoVO);
			}
		}
		return obj;
	}

	public void validarDados(ArtefatoEntregaAlunoVO obj) throws Exception {
		if (obj.getNome() == null || obj.getNome().equals("")) {
			throw new Exception("O nome deve ser informado!");
		}

		if (obj.getNivelControle().equals("DIS") && obj.getNivelControleArtefatoDisciplinaVOs().isEmpty()) {
			throw new Exception("Deve ser infomada ao menos uma disciplina!");
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		if (ArtefatoEntregaAluno.idEntidade == null) {
			ArtefatoEntregaAluno.idEntidade = "ArtefatoEntregaAluno";
		}
		return ArtefatoEntregaAluno.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ArtefatoEntregaAluno.idEntidade = idEntidade;
	}

}
