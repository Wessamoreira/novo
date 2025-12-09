package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ColacaoGrauInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ColacaoGrauVO</code>
 * . Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ColacaoGrauVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ColacaoGrauVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class ColacaoGrau extends ControleAcesso implements ColacaoGrauInterfaceFacade {

	private static final long serialVersionUID = -2285948600123268921L;

	protected static String idEntidade;

	public ColacaoGrau() throws Exception {
		super();
		setIdEntidade("ColacaoGrau");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ColacaoGrauVO</code>.
	 */
	public ColacaoGrauVO novo() throws Exception {
		ColacaoGrau.incluir(getIdEntidade());
		ColacaoGrauVO obj = new ColacaoGrauVO();
		return obj;
	}
	
	public void inicializarDadosColacaoGrauParaExpedicaoDiploma(ColacaoGrauVO colacaoGrauVO,MatriculaVO matriculaVO) {
		colacaoGrauVO.setTitulo("Colação de Grau Automática - Matrícula:" + matriculaVO.getMatricula() + " - " +  Uteis.getData(colacaoGrauVO.getData(), "ddMMyy"));
		colacaoGrauVO.setAta("Ata Automática - " + Uteis.getData(colacaoGrauVO.getData(), "ddMMyy"));
		colacaoGrauVO.setLocal("Local Automático - " + Uteis.getData(colacaoGrauVO.getData(), "ddMMyy"));
		colacaoGrauVO.setSituacao("FE");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirComProgramacaoFormaturaAluno(MatriculaVO matriculaVO, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, ColacaoGrauVO colacaoGrauVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		incluir(colacaoGrauVO, matriculaVO.getUsuario());
		
		getFacadeFactory().getProgramacaoFormaturaFacade().incluirSeNaoExistirProgramacaoFormatura(programacaoFormaturaAlunoVO, matriculaVO, colacaoGrauVO, usuario);

		getFacadeFactory().getProgramacaoFormaturaAlunoFacade().inicializarDadosProgramacaoFormaturaAlunoParaExpedicaoDiploma(matriculaVO, programacaoFormaturaAlunoVO, colacaoGrauVO);
		getFacadeFactory().getProgramacaoFormaturaAlunoFacade().incluir(programacaoFormaturaAlunoVO,  usuario);
		matriculaVO.setDataColacaoGrau(colacaoGrauVO.getData());
		getFacadeFactory().getMatriculaFacade().alterarDataColacaoGrauPorMatricula(matriculaVO, matriculaVO.getUsuario());
	}
	
	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ColacaoGrauVO</code>. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ColacaoGrauVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ColacaoGrauVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ColacaoGrauVO.validarDados(obj);
			ColacaoGrau.incluir(getIdEntidade(), true, usuarioVO);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO ColacaoGrau( titulo, data, local, horario, ata, presidenteMesa, secretariaAcademica, situacao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getTitulo());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(3, obj.getLocal());
					sqlInserir.setString(4, obj.getHorario());
					sqlInserir.setString(5, obj.getAta());
					if (obj.getPresidenteMesa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getPresidenteMesa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getSecretariaAcademica().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getSecretariaAcademica().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getSituacao());
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
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ColacaoGrauVO</code>. Sempre utiliza
	 * a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ColacaoGrauVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ColacaoGrauVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ColacaoGrauVO.validarDados(obj);
			ColacaoGrau.alterar(getIdEntidade(), true, usuarioVO);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE ColacaoGrau set titulo=?, data=?, local=?, horario=?, ata=?, presidenteMesa=?, secretariaAcademica=?, situacao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getTitulo());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(3, obj.getLocal());
					sqlAlterar.setString(4, obj.getHorario());
					sqlAlterar.setString(5, obj.getAta());
					if (obj.getPresidenteMesa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getPresidenteMesa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getSecretariaAcademica().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getSecretariaAcademica().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getSituacao());
					sqlAlterar.setInt(9, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ColacaoGrauVO</code>. Sempre localiza o registro
	 * a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ColacaoGrauVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ColacaoGrauVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ColacaoGrau.excluir(getIdEntidade(), true, usuarioVO);
			this.executarAlteracaoDataColacaoGrauMatriculaPorColacaoGrau(obj.getCodigo(),usuarioVO);
			getFacadeFactory().getProgramacaoFormaturaFacade().executarExclusaoProgramacaoFormaturaVinculadaColacaoGrau(obj, usuarioVO);
			getFacadeFactory().getProgramacaoFormaturaAlunoFacade().excluirProgramacaoFormaturaAlunoVinculadaColacaoGrau(obj.getCodigo(),usuarioVO);
			String sql = "DELETE FROM ColacaoGrau WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public ColacaoGrauVO consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select colacaograu.* from programacaoformaturaaluno " + "inner join colacaograu on programacaoformaturaaluno.colacaograu = colacaograu.codigo "
				+ "where programacaoformaturaaluno.matricula = '" + matricula + "'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados,usuario);
		} else {
			throw new ConsistirException("A matricula informada não possui colação de grau programada.");
		}
	}
	
	/**
	 * Responsável por realizar uma consulta de <code>ColacaoGrau</code> através do valor do atributo
	 * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ColacaoGrauVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ColacaoGrau WHERE upper( situacao ) like upper(?) ORDER BY data,situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ColacaoGrau</code> através do valor do atributo <code>nome</code>
	 * da classe <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ColacaoGrau.* FROM ColacaoGrau, Pessoa WHERE ColacaoGrau.presidenteMesa = Pessoa.codigo and upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase()
				+ "%') ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ColacaoGrau</code> através do valor do atributo
	 * <code>String ata</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorAta(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ColacaoGrau WHERE upper( ata ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY ata";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ColacaoGrau</code> através do valor do atributo
	 * <code>String local</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorLocal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ColacaoGrau WHERE upper( local ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY local";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ColacaoGrau</code> através do valor do atributo
	 * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ColacaoGrau WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ColacaoGrau</code> através do valor do atributo
	 * <code>String titulo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTitulo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ColacaoGrau WHERE upper( titulo ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY titulo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ColacaoGrau</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ColacaoGrau WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ColacaoGrauVO</code> resultantes da consulta.
	 */
	public static List<ColacaoGrauVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ColacaoGrauVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>ColacaoGrauVO</code>.
	 * 
	 * @return O objeto da classe <code>ColacaoGrauVO</code> com os dados devidamente montados.
	 */
	public static ColacaoGrauVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ColacaoGrauVO obj = new ColacaoGrauVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTitulo(dadosSQL.getString("titulo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setLocal(dadosSQL.getString("local"));
		obj.setHorario(dadosSQL.getString("horario"));
		obj.setAta(dadosSQL.getString("ata"));
		obj.getPresidenteMesa().setCodigo(new Integer(dadosSQL.getInt("presidenteMesa")));
		obj.getSecretariaAcademica().setCodigo(new Integer(dadosSQL.getInt("secretariaAcademica")));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosPresidenteMesa(obj, nivelMontarDados,usuario);
		montarDadosSecretariaAcademica(obj, nivelMontarDados,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>ColacaoGrauVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosSecretariaAcademica(ColacaoGrauVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getSecretariaAcademica().getCodigo().intValue() == 0) {
			obj.setSecretariaAcademica(new PessoaVO());
			return;
		}
		obj.setSecretariaAcademica(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getSecretariaAcademica().getCodigo(), false, nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>ColacaoGrauVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPresidenteMesa(ColacaoGrauVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPresidenteMesa().getCodigo().intValue() == 0) {
			obj.setPresidenteMesa(new PessoaVO());
			return;
		}
		obj.setPresidenteMesa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPresidenteMesa().getCodigo(), false, nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ColacaoGrauVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ColacaoGrauVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ColacaoGrau WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ColacaoGrau ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ColacaoGrau.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ColacaoGrau.idEntidade = idEntidade;
	}
	public void executarAlteracaoDataColacaoGrauMatriculaPorColacaoGrau(Integer colacaoGrau, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getMatriculaFacade().alterarDataColacaoGrauMatriculaPorColacaoGrau(colacaoGrau, null, usuarioVO);		
	}
	
	public ColacaoGrauVO consultarPorMatricula(MatriculaVO matriculaVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT colacaograu.* FROM programacaoformaturaaluno ");
		sqlStr.append("INNER JOIN colacaograu ON programacaoformaturaaluno.colacaograu = colacaograu.codigo ");
		sqlStr.append("WHERE programacaoformaturaaluno.matricula = '").append(matriculaVO.getMatricula()).append("';");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		return null;
		
	}
	
	public boolean verificarColacaoGrauPertenceMaisDeUmAluno(MatriculaVO matriculaVO,ColacaoGrauVO colacaoGrauVO) throws Exception{
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT colacaograu.codigo FROM programacaoformaturaaluno ");
		sqlStr.append("INNER JOIN colacaograu ON programacaoformaturaaluno.colacaograu = colacaograu.codigo ");
		sqlStr.append("WHERE colacaograu.codigo = ").append(colacaoGrauVO.getCodigo().intValue());
		sqlStr.append(" AND programacaoformaturaaluno.matricula <> '").append(matriculaVO.getMatricula()).append("';");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}
}