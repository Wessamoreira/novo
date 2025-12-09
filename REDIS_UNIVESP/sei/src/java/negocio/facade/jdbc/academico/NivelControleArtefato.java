/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArtefatoEntregaAlunoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.NivelControleArtefatoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.NivelControleArtefatoInterfaceFacade;

/**
 *
 * @author Ana Claudia
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class NivelControleArtefato extends ControleAcesso implements NivelControleArtefatoInterfaceFacade {

	protected static String idEntidade;
	public static final long serialVersionUID = 1L;

	public NivelControleArtefato() throws Exception {
		super();
		setIdEntidade("NivelControleArtefato");
	}

	public NivelControleArtefatoVO novo() throws Exception {
		NivelControleArtefato.incluir(getIdEntidade());
		NivelControleArtefatoVO obj = new NivelControleArtefatoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirNivelControleArtefatoUnidadeEnsino(final NivelControleArtefatoVO obj) throws Exception {
		final String sql = "INSERT INTO nivelControleArtefato( artefato, unidadeEnsino, tipo ) VALUES ( ?, ?, ?)";

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getArtefatoEntregaAluno().getCodigo());
				sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo());
				sqlInserir.setString(3, obj.getTipo());

				return sqlInserir;
			}
		});

		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirNivelControleArtefatoCurso(final NivelControleArtefatoVO obj) throws Exception {
		final String sql = "INSERT INTO nivelControleArtefato( artefato, curso, tipo ) VALUES ( ?, ?, ?)";

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getArtefatoEntregaAluno().getCodigo());
				sqlInserir.setInt(2, obj.getCurso().getCodigo());
				sqlInserir.setString(3, obj.getTipo());

				return sqlInserir;
			}
		});

		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirNivelControleArtefatoDisciplina(final NivelControleArtefatoVO obj) throws Exception {
		final String sql = "INSERT INTO nivelControleArtefato( artefato, disciplina, tipo ) VALUES ( ?, ?, ?)";

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getArtefatoEntregaAluno().getCodigo());
				sqlInserir.setInt(2, obj.getDisciplina().getCodigo());
				sqlInserir.setString(3, obj.getTipo());

				return sqlInserir;
			}
		});

		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirNivelControleArtefatoFuncionario(final NivelControleArtefatoVO obj) throws Exception {
		final String sql = "INSERT INTO nivelControleArtefato( artefato, funcionario, tipo ) VALUES ( ?, ?, ?)";

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getArtefatoEntregaAluno().getCodigo());
				sqlInserir.setInt(2, obj.getFuncionario().getCodigo());
				sqlInserir.setString(3, obj.getTipo());

				return sqlInserir;
			}
		});

		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>NivelControleArtefatoVO</code> no BD. Garantindo o relacionamento com a
	 * entidade principal <code>basico.ArtefatoEntregaAluno</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {
			Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirNivelControleArtefato(ArtefatoEntregaAlunoVO artefatoEntregaAluno) throws Exception {
		if (!artefatoEntregaAluno.getNivelControleArtefatoUnidadeEnsinoVOs().isEmpty()) {
			Iterator e = artefatoEntregaAluno.getNivelControleArtefatoUnidadeEnsinoVOs().iterator();
			while (e.hasNext()) {
				NivelControleArtefatoVO obj = (NivelControleArtefatoVO) e.next();
				obj.setArtefatoEntregaAluno(artefatoEntregaAluno);
				if (obj.getCodigo().equals(0)) {
					incluirNivelControleArtefatoUnidadeEnsino(obj);
				}

			}
		}

		if (!artefatoEntregaAluno.getNivelControleArtefatoCursoVOs().isEmpty()) {
			Iterator e = artefatoEntregaAluno.getNivelControleArtefatoCursoVOs().iterator();
			while (e.hasNext()) {
				NivelControleArtefatoVO obj = (NivelControleArtefatoVO) e.next();
				obj.setArtefatoEntregaAluno(artefatoEntregaAluno);
				if (obj.getCodigo().equals(0)) {
					incluirNivelControleArtefatoCurso(obj);
				}

			}
		}

		if (!artefatoEntregaAluno.getNivelControleArtefatoDisciplinaVOs().isEmpty()) {
			Iterator e = artefatoEntregaAluno.getNivelControleArtefatoDisciplinaVOs().iterator();
			while (e.hasNext()) {
				NivelControleArtefatoVO obj = (NivelControleArtefatoVO) e.next();
				obj.setArtefatoEntregaAluno(artefatoEntregaAluno);
				if (obj.getCodigo().equals(0)) {
					incluirNivelControleArtefatoDisciplina(obj);
				}
			}
		}

		if (!artefatoEntregaAluno.getNivelControleArtefatoFuncionarioVOs().isEmpty()) {
			Iterator e = artefatoEntregaAluno.getNivelControleArtefatoFuncionarioVOs().iterator();
			while (e.hasNext()) {
				NivelControleArtefatoVO obj = (NivelControleArtefatoVO) e.next();
				obj.setArtefatoEntregaAluno(artefatoEntregaAluno);
				if (obj.getCodigo().equals(0)) {
					incluirNivelControleArtefatoFuncionario(obj);
				}
			}
		}
	}

	/**
	 * Operação responsável por excluir do banco de dados o objeto da classe
	 * <code>NivelControleArtefatoVO</code>,que não esteja mais na lista de
	 * NivelControeArtefatoVOs. E incluir no banco de dados um objeto da classe
	 * <code>NivelControleArtefatoVO</code>, que seja novo na lista de
	 * NivelControleArtefatoVOs. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade.
	 * 
	 * @param artefatoEntregaAluno
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que tem as
	 *            listas de NivelControleArtefato.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterarNivelControleArtefato(ArtefatoEntregaAlunoVO artefatoEntregaAluno, UsuarioVO usuario)
			throws Exception {
		try {
			NivelControleArtefato.excluir(getIdEntidade());

			/*
			 * o método de consulta abaixo irá retornar todos os nivelControleArtefatoVO do
			 * artefato, permitindo a exclusao de cada um deles logo abaixo.
			 */
			List<NivelControleArtefatoVO> listaNivelControleArtefatoVORemover = consultarNivelControleArtefatoRemovidosArtefatoEntregaAluno(
					artefatoEntregaAluno, usuario);

			for (NivelControleArtefatoVO nivelControleArtefatoVO : listaNivelControleArtefatoVORemover) {

				this.excluir(nivelControleArtefatoVO, usuario);
			}
			/*
			 * o método abaixo, inclui os novos objetos da classe nivelControleArtefatoVO
			 * das listas de nivelControleArtefatoVOs
			 */
			this.incluirNivelControleArtefato(artefatoEntregaAluno);

		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * Método responsável por excluir o nivelControleArtefato ao mesmo.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(NivelControleArtefatoVO obj, UsuarioVO usuario) throws Exception {
		try {
			NivelControleArtefato.excluir(getIdEntidade());
			String sql = "DELETE FROM NivelControleArtefato WHERE ((codigo = ?))"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * Método responsável por excluir todos nivelControleArtefato que tenha o codigo
	 * do artefato ao mesmo.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirNivelControleArtefato(ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception {
		try {
			NivelControleArtefato.excluir(getIdEntidade());
			String sql = "DELETE FROM NivelControleArtefato WHERE ((artefato = ?))"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>NivelControleArtefato</code>
	 * através do valor do atributo <code>Integer artefato</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>NivelControleArtefato</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarNivelControleArtefato(Integer artefatoEntregaAluno, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {

		String sqlStr = "SELECT * FROM nivelControleArtefato WHERE artefato = " + artefatoEntregaAluno;

		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>NivelControleArtefato</code> resultantes da consulta.
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
	 * 
	 * @return O objeto da classe <code>NivelControleArtefato</code> com os dados
	 *         devidamente montados.
	 */
	public static NivelControleArtefatoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {

		NivelControleArtefatoVO obj = new NivelControleArtefatoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getArtefatoEntregaAluno().setCodigo(new Integer(dadosSQL.getInt("artefato")));
		obj.setTipo(dadosSQL.getString("tipo"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		if (obj.getTipo().equals("UNE")) {
			obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
			montarDadosUnidadeEnsino(obj, usuario);
		} else if (obj.getTipo().equals("CUR")) {
			obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
			montarDadosCurso(obj, usuario);
		} else if (obj.getTipo().equals("DIS")) {
			obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
			montarDadosDisciplina(obj, usuario);
		} else if (obj.getTipo().equals("FUN")) {
			obj.getFuncionario().setCodigo(new Integer(dadosSQL.getInt("funcionario")));
			montarDadosFuncionario(obj, usuario);
		}
		return obj;
	}

	public List<NivelControleArtefatoVO> consultarNivelControleArtefatoRemovidosArtefatoEntregaAluno(
			ArtefatoEntregaAlunoVO artefatoEntregaAluno, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		List<NivelControleArtefatoVO> listaNivelControleArtefatoManter = new ArrayList<NivelControleArtefatoVO>();
		if (!artefatoEntregaAluno.getNivelControleArtefatoUnidadeEnsinoVOs().isEmpty()) {
			for (NivelControleArtefatoVO nivelControleArtefatoUnidadeEnsinoVOManter : artefatoEntregaAluno
					.getNivelControleArtefatoUnidadeEnsinoVOs()) {
				listaNivelControleArtefatoManter.add(nivelControleArtefatoUnidadeEnsinoVOManter);
			}
		}
		if (!artefatoEntregaAluno.getNivelControleArtefatoCursoVOs().isEmpty()) {
			for (NivelControleArtefatoVO nivelControleArtefatoCursoVOManter : artefatoEntregaAluno
					.getNivelControleArtefatoCursoVOs()) {
				listaNivelControleArtefatoManter.add(nivelControleArtefatoCursoVOManter);
			}
		}
		if (!artefatoEntregaAluno.getNivelControleArtefatoDisciplinaVOs().isEmpty()) {
			for (NivelControleArtefatoVO nivelControleArtefatoDisciplinaVOManter : artefatoEntregaAluno
					.getNivelControleArtefatoDisciplinaVOs()) {
				listaNivelControleArtefatoManter.add(nivelControleArtefatoDisciplinaVOManter);
			}
		}
		if (!artefatoEntregaAluno.getNivelControleArtefatoFuncionarioVOs().isEmpty()) {
			for (NivelControleArtefatoVO nivelControleArtefatoFuncionarioVOManter : artefatoEntregaAluno
					.getNivelControleArtefatoFuncionarioVOs()) {
				listaNivelControleArtefatoManter.add(nivelControleArtefatoFuncionarioVOManter);
			}
		}
		sql.append(" SELECT NivelControleArtefato.*  ");
		sql.append(" FROM NivelControleArtefato ");
		sql.append(" WHERE (artefato = ").append(artefatoEntregaAluno.getCodigo()).append(") ");
		if (!listaNivelControleArtefatoManter.isEmpty()) {
			/*
			 * se nao está vazia, temos que retornar todos os nivelControleArtefato que não
			 * estão nesta lista, pois estes serão mantidos. Queremos justamente obter os
			 * que não estão contidos nesta lista. Caso esteja vazia, significa que nenhum
			 * nivelControleArtefato será mantido
			 */
			sql.append("   and (NivelControleArtefato.codigo not in (");
			String virgula = "";
			for (NivelControleArtefatoVO nivelControleArtefatoVOManter : listaNivelControleArtefatoManter) {
				sql.append(virgula);
				sql.append(nivelControleArtefatoVOManter.getCodigo());
				virgula = ", ";
			}
			sql.append("))");
		}

		sql.append("   ORDER BY NivelControleArtefato.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}

	public static void montarDadosUnidadeEnsino(NivelControleArtefatoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
				obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public static void montarDadosCurso(NivelControleArtefatoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(),
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
	}

	public static void montarDadosDisciplina(NivelControleArtefatoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(
				obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public static void montarDadosFuncionario(NivelControleArtefatoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new FuncionarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(
				obj.getFuncionario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return NivelControleArtefato.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		NivelControleArtefato.idEntidade = idEntidade;
	}
}
