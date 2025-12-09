package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoDisciplinasProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.DisciplinasGrupoDisciplinaProcSeletivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ProcSeletivoDisciplinasProcSeletivoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProcSeletivoDisciplinasProcSeletivoVO
 * @see ControleAcesso
 * @see ProcSeletivo
 */
@Repository
@Scope("singleton")
@Lazy
public class DisciplinasGrupoDisciplinaProcSeletivo extends ControleAcesso implements DisciplinasGrupoDisciplinaProcSeletivoInterfaceFacade {

	protected static String idEntidade;

	public DisciplinasGrupoDisciplinaProcSeletivo() throws Exception {
		super();
		setIdEntidade("GrupoDisciplinaProcSeletivo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
	 */
	public DisciplinasGrupoDisciplinaProcSeletivoVO novo() throws Exception {
		DisciplinasGrupoDisciplinaProcSeletivo.incluir(getIdEntidade());
		DisciplinasGrupoDisciplinaProcSeletivoVO obj = new DisciplinasGrupoDisciplinaProcSeletivoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO DisciplinasGrupoDisciplinaProcSeletivo( grupoDisciplinaProcSeletivo, disciplinasProcSeletivo, variavelNota, ordemCriterioDesempate, notaMinimaReprovadoImediato, formaCalculoAprovacao ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getGrupoDisciplinaProcSeletivo().intValue());
					sqlInserir.setInt(2, obj.getDisciplinasProcSeletivo().getCodigo().intValue());
					sqlInserir.setString(3, obj.getVariavelNota());
					sqlInserir.setString(4, obj.getOrdemCriterioDesempate());
					if (obj.getNotaMinimaReprovadoImediato() == null) {
						sqlInserir.setNull(5, 0);
					} else {
						sqlInserir.setDouble(5, obj.getNotaMinimaReprovadoImediato().doubleValue());
					}
					sqlInserir.setString(6, obj.getFormaCalculoAprovacao());
					return sqlInserir;
				}
			});
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>)
	 * do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE DisciplinasGrupoDisciplinaProcSeletivo set grupoDisciplinaProcSeletivo = ?, disciplinasProcSeletivo = ?, variavelNota=?, ordemCriterioDesempate=?, notaMinimaReprovadoImediato=?, formaCalculoAprovacao=?   WHERE ((grupoDisciplinaProcSeletivo = ?) and (disciplinasProcSeletivo = ?))";
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getGrupoDisciplinaProcSeletivo().intValue());
				sqlAlterar.setInt(2, obj.getDisciplinasProcSeletivo().getCodigo().intValue());
				sqlAlterar.setString(3, obj.getVariavelNota());
				sqlAlterar.setString(4, obj.getOrdemCriterioDesempate());
				if (obj.getNotaMinimaReprovadoImediato() == null) {
					sqlAlterar.setNull(5, 0);
				} else {
					sqlAlterar.setDouble(5, obj.getNotaMinimaReprovadoImediato().doubleValue());
				}
				sqlAlterar.setString(6, obj.getFormaCalculoAprovacao());
				sqlAlterar.setInt(7, obj.getGrupoDisciplinaProcSeletivo().intValue());
				sqlAlterar.setInt(8, obj.getDisciplinasProcSeletivo().getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj);
			return;
		}
		;
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar
	 * esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws Exception {
		DisciplinasGrupoDisciplinaProcSeletivo.excluir(getIdEntidade());
		String sql = "DELETE FROM DisciplinasGrupoDisciplinaProcSeletivo WHERE ((grupoDisciplinaProcSeletivo = ?) and (disciplinasProcSeletivo = ?))";
		getConexao().getJdbcTemplate().update(sql, obj.getGrupoDisciplinaProcSeletivo().intValue(), obj.getDisciplinasProcSeletivo().getCodigo().intValue());
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProcSeletivoDisciplinasProcSeletivo</code> através do valor do atributo <code>nome</code> da
	 * classe <code>DisciplinasProcSeletivo</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeDisciplinasGrupoDisciplinaProcSeletivo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DisciplinasGrupoDisciplinaProcSeletivo.* FROM DisciplinasGrupoDisciplinaProcSeletivo, DisciplinasProcSeletivo WHERE DisciplinasGrupoDisciplinaProcSeletivo.disciplinasProcSeletivo = DisciplinasProcSeletivo.codigo and DisciplinasProcSeletivo.nome like('" + valorConsulta + "%') ORDER BY DisciplinasProcSeletivo.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
	 * 
	 * @return O objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> com os dados devidamente montados.
	 */
	public static DisciplinasGrupoDisciplinaProcSeletivoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		DisciplinasGrupoDisciplinaProcSeletivoVO obj = new DisciplinasGrupoDisciplinaProcSeletivoVO();
		obj.setGrupoDisciplinaProcSeletivo(new Integer(dadosSQL.getInt("grupoDisciplinaProcSeletivo")));
		obj.getDisciplinasProcSeletivo().setCodigo(new Integer(dadosSQL.getInt("disciplinasProcSeletivo")));
		obj.setVariavelNota(dadosSQL.getString("variavelNota"));
		obj.setOrdemCriterioDesempate(dadosSQL.getString("ordemCriterioDesempate"));

		if (dadosSQL.getObject("notaMinimaReprovadoImediato") == null) {
			obj.setNotaMinimaReprovadoImediato((Double) dadosSQL.getObject("notaMinimaReprovadoImediato"));
		} else {
			obj.setNotaMinimaReprovadoImediato(dadosSQL.getDouble("notaMinimaReprovadoImediato"));
		}
		obj.setFormaCalculoAprovacao(dadosSQL.getString("formaCalculoAprovacao"));
		obj.setNovoObj(Boolean.FALSE);

		montarDadosDisciplinasProcSeletivo(obj, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>DisciplinasProcSeletivoVO</code> relacionado ao objeto
	 * <code>ProcSeletivoDisciplinasProcSeletivoVO</code>. Faz uso da chave primária da classe <code>DisciplinasProcSeletivoVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosDisciplinasProcSeletivo(DisciplinasGrupoDisciplinaProcSeletivoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplinasProcSeletivo().getCodigo().intValue() == 0) {
			obj.setDisciplinasProcSeletivo(new DisciplinasProcSeletivoVO());
			return;
		}
		obj.setDisciplinasProcSeletivo(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(obj.getDisciplinasProcSeletivo().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
	 * 
	 * @param <code>procSeletivo</code>
	 *            campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDisciplinasGrupoDisciplinaProcSeletivos(Integer procSeletivo) throws Exception {
		String sql = "DELETE FROM DisciplinasGrupoDisciplinaProcSeletivo WHERE (grupoDisciplinaProcSeletivo = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { procSeletivo });
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> contidos em um Hashtable no BD. Faz uso
	 * da operação <code>excluirProcSeletivoDisciplinasProcSeletivos</code> e <code>incluirProcSeletivoDisciplinasProcSeletivos</code> disponíveis na
	 * classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinasGrupoDisciplinaProcSeletivos(Integer grupoDisciplinaProcSeletivo, List<DisciplinasGrupoDisciplinaProcSeletivoVO> objetos) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM DisciplinasGrupoDisciplinaProcSeletivo WHERE grupoDisciplinaProcSeletivo = ").append(grupoDisciplinaProcSeletivo).append(" and disciplinasProcSeletivo not in (0 ");
		for (DisciplinasGrupoDisciplinaProcSeletivoVO obj : objetos) {
			sql.append(", ").append(obj.getDisciplinasProcSeletivo().getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
		for (DisciplinasGrupoDisciplinaProcSeletivoVO obj : objetos) {
			obj.setGrupoDisciplinaProcSeletivo(grupoDisciplinaProcSeletivo);
			alterar(obj);
		}
	}

	/**
	 * Operação responsável por incluir objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no BD. Garantindo o relacionamento com a
	 * entidade principal <code>processosel.ProcSeletivo</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisciplinasGrupoDisciplinaProcSeletivos(Integer grupoDisciplinaProcSeletivoPrm, List<DisciplinasGrupoDisciplinaProcSeletivoVO> objetos) throws Exception {
		try {
			for (DisciplinasGrupoDisciplinaProcSeletivoVO obj : objetos) {
				obj.setGrupoDisciplinaProcSeletivo(grupoDisciplinaProcSeletivoPrm);
				incluir(obj);
			}
		} catch (Exception e) {
			for (DisciplinasGrupoDisciplinaProcSeletivoVO obj : objetos) {
				obj.setNovoObj(true);
			}
			throw e;
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ProcSeletivoDisciplinasProcSeletivoVO</code> relacionados a um objeto da classe
	 * <code>processosel.ProcSeletivo</code>.
	 * 
	 * @param procSeletivo
	 *            Atributo de <code>processosel.ProcSeletivo</code> a ser utilizado para localizar os objetos da classe
	 *            <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Override
	public List<DisciplinasGrupoDisciplinaProcSeletivoVO> consultarDisciplinasGrupoDisciplinaProcSeletivos(Integer grupoDisciplinaProcSeletivo, UsuarioVO usuario) throws Exception {
		DisciplinasGrupoDisciplinaProcSeletivo.consultar(getIdEntidade());
		String sql = "SELECT * FROM DisciplinasGrupoDisciplinaProcSeletivo WHERE grupoDisciplinaProcSeletivo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, grupoDisciplinaProcSeletivo.intValue());
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public static List consultarDisciplinasGrupoDisciplinaProcSeletivosLinguaExtrangeira(Integer grupoDisciplinaProcSeletivo, UsuarioVO usuario) throws Exception {
		DisciplinasGrupoDisciplinaProcSeletivo.consultar(getIdEntidade());
		List objetos = new ArrayList();
		String sql = "select distinct disciplinasprocseletivo.* from procseletivounidadeensino  " + "inner join procseletivocurso on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino " + "inner join procseletivo on procseletivo.codigo = procseletivounidadeensino.procseletivo " + "inner join grupodisciplinaprocseletivo gdps on gdps.codigo = procseletivocurso.grupodisciplinaprocseletivo " + "inner join DisciplinasGrupoDisciplinaProcSeletivo on DisciplinasGrupoDisciplinaProcSeletivo.grupodisciplinaprocseletivo = gdps.codigo " + "inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = DisciplinasGrupoDisciplinaProcSeletivo.disciplinasprocseletivo " + "where procseletivounidadeensino.procseletivo = ? " + "and disciplinasprocseletivo.disciplinaidioma = true ";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, grupoDisciplinaProcSeletivo.intValue());
		while (resultado.next()) {
			objetos.add(DisciplinasProcSeletivo.montarDados(resultado));
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public DisciplinasGrupoDisciplinaProcSeletivoVO consultarPorChavePrimaria(Integer disciplinasProcSeletivoPrm, Integer procSeletivoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM DisciplinasGrupoDisciplinaProcSeletivo WHERE disciplinasProcSeletivo = ? and grupoDisciplinaProcSeletivo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, disciplinasProcSeletivoPrm.intValue(), procSeletivoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return ProcSeletivoDisciplinasProcSeletivo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProcSeletivoDisciplinasProcSeletivo.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>DisciplinasGrupoDisciplinaProcSeletivoVO</code>. Todos os tipos de
	 * consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de
	 * valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	@Override
	public void validarDados(DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws ConsistirException {
		if ((obj.getDisciplinasProcSeletivo() == null) || (obj.getDisciplinasProcSeletivo().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo DISCIPLINAS PROCESSO SELETIVO (Disciplinas Processo Seletivo) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getVariavelNota())) {
			throw new ConsistirException("O campo VARIÁVEL NOTA (Disciplinas Processo Seletivo) da disciplina " + obj.getDisciplinasProcSeletivo().getNome() + " deve ser informado.");
		}
	}
	
	@Override
	public List<DisciplinasGrupoDisciplinaProcSeletivoVO> consultarPorInscricaoCandidato(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select distinct DisciplinasGrupoDisciplinaProcSeletivo.* from DisciplinasGrupoDisciplinaProcSeletivo  ";
		sqlStr += "inner join procseletivocurso on procseletivocurso.grupodisciplinaprocseletivo = DisciplinasGrupoDisciplinaProcSeletivo.grupodisciplinaprocseletivo ";
		sqlStr += "inner join procseletivounidadeensino on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino ";
		sqlStr += "inner join procseletivo on procseletivo.codigo = procseletivounidadeensino.procseletivo  ";
		sqlStr += "inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = DisciplinasGrupoDisciplinaProcSeletivo.disciplinasprocseletivo ";
		sqlStr += "LEFT JOIN inscricao ON inscricao.procseletivo = procseletivo.codigo AND DisciplinasGrupoDisciplinaProcSeletivo.disciplinasprocseletivo = inscricao.opcaolinguaestrangeira ";
		sqlStr += "WHERE inscricao.codigo = " + valorConsulta + " and inscricao.cursoOpcao1 = procseletivocurso.unidadeensinocurso ";
		sqlStr += "UNION ALL ";
		sqlStr += "select distinct DisciplinasGrupoDisciplinaProcSeletivo.* from DisciplinasGrupoDisciplinaProcSeletivo  ";
		sqlStr += "inner join procseletivocurso on procseletivocurso.grupodisciplinaprocseletivo = DisciplinasGrupoDisciplinaProcSeletivo.grupodisciplinaprocseletivo ";
		sqlStr += "inner join procseletivounidadeensino on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino ";
		sqlStr += "inner join procseletivo on procseletivo.codigo = procseletivounidadeensino.procseletivo  ";
		sqlStr += "inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = DisciplinasGrupoDisciplinaProcSeletivo.disciplinasprocseletivo ";
		sqlStr += "LEFT JOIN inscricao ON inscricao.procseletivo = procseletivo.codigo  ";
		sqlStr += "WHERE inscricao.codigo = " + valorConsulta + " AND disciplinasprocseletivo.disciplinaidioma = FALSE and inscricao.cursoOpcao1 = procseletivocurso.unidadeensinocurso ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}
	
}
