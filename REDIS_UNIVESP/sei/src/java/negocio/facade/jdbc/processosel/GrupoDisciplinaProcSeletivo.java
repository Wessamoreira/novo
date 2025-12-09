package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoGrupoDisciplinaProcSeletivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.GrupoDisciplinaProcSeletivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>GrupoDisciplinaProcSeletivoVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>GrupoDisciplinaProcSeletivoVO</code>. Encapsula
 * toda a interação com o banco de dados.
 * 
 * @see GrupoDisciplinaProcSeletivoVO
 * @see ControleAcesso
 */
@Repository
public class GrupoDisciplinaProcSeletivo extends ControleAcesso implements GrupoDisciplinaProcSeletivoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public GrupoDisciplinaProcSeletivo() throws Exception {
		super();
		setIdEntidade("GrupoDisciplinaProcSeletivo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code>.
	 */
	public GrupoDisciplinaProcSeletivoVO novo() throws Exception {
		GrupoDisciplinaProcSeletivo.incluir(getIdEntidade());
		GrupoDisciplinaProcSeletivoVO obj = new GrupoDisciplinaProcSeletivoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code>. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GrupoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			GrupoDisciplinaProcSeletivo.incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO GrupoDisciplinaProcSeletivo( descricao,  formulaCalculoAprovacao, situacao ) VALUES ( ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setString(2, obj.getFormulaCalculoAprovacao());
					sqlInserir.setString(3, obj.getSituacao().name());
					// sqlInserir.setInt(3, obj.getResponsavel().getCodigo().intValue());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().incluirDisciplinasGrupoDisciplinaProcSeletivos(obj.getCodigo(), obj.getDisciplinasGrupoDisciplinaProcSeletivoVOs());
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GrupoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			GrupoDisciplinaProcSeletivo.alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE GrupoDisciplinaProcSeletivo set descricao=?, formulaCalculoAprovacao=?, situacao=? WHERE (codigo = ?) ";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, obj.getFormulaCalculoAprovacao());
					sqlAlterar.setString(3, obj.getSituacao().name());
					// sqlAlterar.setInt(3, obj.getResponsavel().getCodigo().intValue());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().alterarDisciplinasGrupoDisciplinaProcSeletivos(obj.getCodigo(), obj.getDisciplinasGrupoDisciplinaProcSeletivoVOs());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar
	 * esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(GrupoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			GrupoDisciplinaProcSeletivo.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM GrupoDisciplinaProcSeletivo WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
			Iterator<DisciplinasGrupoDisciplinaProcSeletivoVO> i = obj.getDisciplinasGrupoDisciplinaProcSeletivoVOs().iterator();
			while (i.hasNext()) {
				DisciplinasGrupoDisciplinaProcSeletivoVO objs = (DisciplinasGrupoDisciplinaProcSeletivoVO) i.next();
				getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().excluir(objs);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public GrupoDisciplinaProcSeletivoVO consultarUltimoProcessoSeletivo(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT codigo, descricao, formulaCalculoAprovacao FROM GrupoDisciplinaProcSeletivo ORDER BY codigo DESC LIMIT 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		GrupoDisciplinaProcSeletivoVO obj = new GrupoDisciplinaProcSeletivoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setSituacao(SituacaoGrupoDisciplinaProcSeletivoEnum.valueOf(tabelaResultado.getString("situacao")));
		obj.setFormulaCalculoAprovacao(tabelaResultado.getString("formulaCalculoAprovacao"));
		return obj;
	}

	/**
	 * Responsável por realizar uma consulta de <code>GrupoDisciplinaProcSeletivo</code> através do valor do atributo <code>String descricao</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>GrupoDisciplinaProcSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<GrupoDisciplinaProcSeletivoVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDisciplinaProcSeletivo WHERE descricao ilike lower (?) ORDER BY descricao ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public GrupoDisciplinaProcSeletivoVO consultarCodigo(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDisciplinaProcSeletivo WHERE codigo = " + codigo.intValue() + " ORDER BY codigo";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!dadosSQL.next()) {
			return new GrupoDisciplinaProcSeletivoVO();
		}
		return montarDados(dadosSQL, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>GrupoDisciplinaProcSeletivo</code> através do valor do atributo <code>Integer codigo</code>.
	 * Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>GrupoDisciplinaProcSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<GrupoDisciplinaProcSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDisciplinaProcSeletivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>GrupoDisciplinaProcSeletivoVO</code> resultantes da consulta.
	 */
	public static List<GrupoDisciplinaProcSeletivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GrupoDisciplinaProcSeletivoVO> vetResultado = new ArrayList<GrupoDisciplinaProcSeletivoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>GrupoDisciplinaProcSeletivoVO</code>.
	 *
	 * @return O objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code> com os dados devidamente montados.
	 */
	public static GrupoDisciplinaProcSeletivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		GrupoDisciplinaProcSeletivoVO obj = new GrupoDisciplinaProcSeletivoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSituacao(SituacaoGrupoDisciplinaProcSeletivoEnum.valueOf(dadosSQL.getString("situacao")));
		obj.setFormulaCalculoAprovacao(dadosSQL.getString("formulaCalculoAprovacao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}

		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setDisciplinasGrupoDisciplinaProcSeletivoVOs(getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().consultarDisciplinasGrupoDisciplinaProcSeletivos(obj.getCodigo(), usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>GrupoDisciplinaProcSeletivoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(GrupoDisciplinaProcSeletivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public GrupoDisciplinaProcSeletivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM GrupoDisciplinaProcSeletivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return GrupoDisciplinaProcSeletivo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		GrupoDisciplinaProcSeletivo.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>GrupoDisciplinaProcSeletivoVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos
	 * para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	private void validarDados(GrupoDisciplinaProcSeletivoVO obj) throws ConsistirException {
		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo DESCRIÇÃO (Dados Básicos) deve ser informado.");
		}
		if (obj.getFormulaCalculoAprovacao().equals("")) {
			throw new ConsistirException("O campo FORMÚLA DE CÁLCULO APROVAÇÃO (Dados Básicos) deve ser informado.");
		} else {
			String formula = obj.getFormulaCalculoAprovacao().trim();
			for (Iterator<DisciplinasGrupoDisciplinaProcSeletivoVO> iterator = obj.getDisciplinasGrupoDisciplinaProcSeletivoVOs().iterator(); iterator.hasNext();) {
				DisciplinasGrupoDisciplinaProcSeletivoVO resultadoDisciplina = iterator.next();
				if (formula.contains(resultadoDisciplina.getVariavelNota())) {
					formula = formula.replaceAll(resultadoDisciplina.getVariavelNota(), "0");
				}
			}
			if (!formula.isEmpty()) {
				ScriptEngineManager factory = new ScriptEngineManager();
				// create a JavaScript engine
				ScriptEngine engine = factory.getEngineByName("JavaScript");

				Object result;
				try {
					// for (VariaveisNotaEnum variavelNota : VariaveisNotaEnum.values()) {
					// formula = formula.replaceAll(variavelNota.getValor(), "0.0");
					// }
					formula = formula.replaceAll("e", "&&");
					formula = formula.replaceAll("E", "&&");
					formula = formula.replaceAll("ou", "||");
					formula = formula.replaceAll("OU", "||");
					formula = formula.replaceAll("=", "==");
					formula = formula.replaceAll("====", "==");
					formula = formula.replaceAll(">==", ">=");
					formula = formula.replaceAll("<==", "<=");
					formula = formula.replaceAll("!==", "!=");
					formula = formula.replaceAll(",", ".");
					result = engine.eval(formula);
					if (result instanceof Number) {
						result = ((Number) result).doubleValue();
					}
					if (!(result instanceof Double)) {
						throw new ConsistirException("A FORMULA DE CÁLCULO APROVAÇÃO informado está incorreta.");
					}
				} catch (ScriptException e) {
					throw new ConsistirException("A FORMULA DE CÁLCULO APROVAÇÃO informado está incorreta.");
				}

			}
		}

		// if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
		// throw new ConsistirException("O campo RESPONSÁVEL (Dados Básicos) deve ser informado.");
		// }

	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>ProcSeletivoDisciplinasGrupoDisciplinaProcSeletivoVO</code> ao List
	 * <code>procSeletivoDisciplinasGrupoDisciplinaProcSeletivoVOs</code>. Utiliza o atributo padrão de consulta da classe
	 * <code>ProcSeletivoDisciplinasProcSeletivo</code> - getDisciplinasProcSeletivo().getCodigo() - como identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcSeletivoDisciplinasGrupoDisciplinaProcSeletivoVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	@Override
	public void adicionarObjDisciplinasGrupoDisciplinaProcSeletivoVOs(DisciplinasGrupoDisciplinaProcSeletivoVO obj, List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs) throws Exception {
		getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().validarDados(obj);
		int index = 0;
		Iterator<DisciplinasGrupoDisciplinaProcSeletivoVO> i = disciplinasGrupoDisciplinaProcSeletivoVOs.iterator();
		while (i.hasNext()) {
			DisciplinasGrupoDisciplinaProcSeletivoVO objExistente = (DisciplinasGrupoDisciplinaProcSeletivoVO) i.next();
			if (objExistente.getDisciplinasProcSeletivo().getCodigo().equals(obj.getDisciplinasProcSeletivo().getCodigo())) {
				disciplinasGrupoDisciplinaProcSeletivoVOs.set(index, obj);
				return;
			}
			index++;
		}
		disciplinasGrupoDisciplinaProcSeletivoVOs.add(obj);
		// adicionarObjSubordinadoOC
	}

	/**
	 * Operação responsável por excluir um objeto da classe <code>ProcSeletivoDisciplinasGrupoDisciplinaProcSeletivoVO</code> no List
	 * <code>procSeletivoDisciplinasGrupoDisciplinaProcSeletivoVOs</code>. Utiliza o atributo padrão de consulta da classe
	 * <code>ProcSeletivoDisciplinasProcSeletivo</code> - getDisciplinasProcSeletivo().getCodigo() - como identificador (key) do objeto no List.
	 * 
	 * @param disciplinasProcSeletivo
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	@Override
	public void excluirObjDisciplinasGrupoDisciplinaProcSeletivoVOs(Integer disciplinasProcSeletivo, List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs) throws Exception {
		int index = 0;
		Iterator<DisciplinasGrupoDisciplinaProcSeletivoVO> i = disciplinasGrupoDisciplinaProcSeletivoVOs.iterator();
		while (i.hasNext()) {
			DisciplinasGrupoDisciplinaProcSeletivoVO objExistente = (DisciplinasGrupoDisciplinaProcSeletivoVO) i.next();
			if (objExistente.getDisciplinasProcSeletivo().getCodigo().equals(disciplinasProcSeletivo)) {
				disciplinasGrupoDisciplinaProcSeletivoVOs.remove(index);
				return;
			}
			index++;
		}
	}
	
	@Override
	public GrupoDisciplinaProcSeletivoVO consultarGrupoDisciplinaPorProcessoSeletivoEUnidadeEnsinoCurso(Integer procSeletivo, Integer unidadeEnsinoCurso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception{
		StringBuilder sql = new StringBuilder("SELECT GrupoDisciplinaProcSeletivo.* FROM procseletivounidadeensino ");
		sql.append(" inner join procseletivocurso on procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo ");
		sql.append(" inner join grupodisciplinaprocseletivo  on grupodisciplinaprocseletivo.codigo = procseletivocurso.grupodisciplinaprocseletivo");
		sql.append(" where procseletivounidadeensino.procseletivo = ? ");
		sql.append(" and procseletivocurso.unidadeensinocurso = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), procSeletivo, unidadeEnsinoCurso);
		if(rs.next()){
			return montarDados(rs, nivelMontarDados, usuarioLogado);
		}
		return new GrupoDisciplinaProcSeletivoVO();
	}
	
	@Override
	public List<GrupoDisciplinaProcSeletivoVO> consultarPorDescricaoSituacaoAtivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDisciplinaProcSeletivo WHERE descricao ilike('" + valorConsulta + "%') and situacao = 'ATIVA' ORDER BY descricao ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

}
