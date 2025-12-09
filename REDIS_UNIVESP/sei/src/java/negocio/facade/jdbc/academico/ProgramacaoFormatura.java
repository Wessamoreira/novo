package negocio.facade.jdbc.academico;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
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
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProgramacaoFormaturaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProgramacaoFormaturaVO</code>. Responsável por implementar operações como incluir, alterar, excluir
 * e consultar pertinentes a classe <code>ProgramacaoFormaturaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProgramacaoFormaturaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
@SuppressWarnings("unchecked")
public class ProgramacaoFormatura extends ControleAcesso implements ProgramacaoFormaturaInterfaceFacade {

	private static final long serialVersionUID = -7636403689252095519L;

	protected static String idEntidade;

	public ProgramacaoFormatura() throws Exception {
		super();
		setIdEntidade("ProgramacaoFormatura");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ProgramacaoFormaturaVO</code>.
	 */
	public ProgramacaoFormaturaVO novo() throws Exception {
		ProgramacaoFormatura.incluir(getIdEntidade());
		ProgramacaoFormaturaVO obj = new ProgramacaoFormaturaVO();
		return obj;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProgramacaoFormaturaVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,UsuarioVO usuario) throws Exception {
		this.incluir(obj, configuracaoFinanceiroVO, usuario, true);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProgramacaoFormaturaVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProgramacaoFormaturaVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProgramacaoFormaturaVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,UsuarioVO usuario, boolean incluirProgramacaoFormaturaAlunos) throws Exception {
		try {
			ProgramacaoFormaturaVO.validarDados(obj);
			ProgramacaoFormatura.incluir(getIdEntidade(), true, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO ProgramacaoFormatura( dataCadastro, responsavelCadastro, colacaograu, niveleducacional, dataLimiteAssinaturaAta) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataCadastro()));
					if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavelCadastro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getColacaoGrauVO().getCodigo().intValue() !=0) {
						sqlInserir.setInt(3, obj.getColacaoGrauVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getNivelEducacional());
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataLimiteAssinaturaAta()));
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
			getFacadeFactory().getProgramacaoFormaturaUnidadeEnsinoInterfaceFacade().persistir(obj, usuario);
			if(incluirProgramacaoFormaturaAlunos) {
				getFacadeFactory().getProgramacaoFormaturaAlunoFacade().incluirProgramacaoFormaturaAlunos(obj.getCodigo(), obj.getProgramacaoFormaturaAlunoVOs(), usuario);
			}
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProgramacaoFormaturaVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro
	 * a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProgramacaoFormaturaVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProgramacaoFormaturaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ProgramacaoFormaturaVO.validarDados(obj);
			ProgramacaoFormatura.alterar(getIdEntidade(), true, usuarioVO);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE ProgramacaoFormatura set dataCadastro=?, responsavelCadastro=?, colacaograu=?, niveleducacional=?, dataLimiteAssinaturaAta=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataCadastro()));
					if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getResponsavelCadastro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getColacaoGrauVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getColacaoGrauVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getNivelEducacional());
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataLimiteAssinaturaAta()));
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getProgramacaoFormaturaUnidadeEnsinoInterfaceFacade().persistir(obj, usuarioVO);
			getFacadeFactory().getProgramacaoFormaturaAlunoFacade().alterarProgramacaoFormaturaAlunos(obj.getCodigo(), obj.getProgramacaoFormaturaAlunoVOs(),  usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProgramacaoFormaturaVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente
	 * verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProgramacaoFormaturaVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProgramacaoFormaturaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ProgramacaoFormatura.excluir(getIdEntidade(), true, usuarioVO);
			getFacadeFactory().getProgramacaoFormaturaAlunoFacade().excluirProgramacaoFormaturaAlunos(obj.getCodigo());
			getFacadeFactory().getProgramacaoFormaturaUnidadeEnsinoInterfaceFacade().excluir(obj.getCodigo(), usuarioVO);
			String sql = "DELETE FROM ProgramacaoFormatura WHERE ((codigo = ?))"  + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>nome</code> da classe <code>Usuario</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorNomeUsuario(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT DISTINCT ProgramacaoFormatura.* FROM ProgramacaoFormatura, Usuario WHERE ProgramacaoFormatura.responsavelCadastro = Usuario.codigo and upper( Usuario.nome ) like('"
				+ valorConsulta.toUpperCase() + "%') ORDER BY programacaoformatura.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>Date dataCadastro</code>. Retorna os objetos com valores pertecentes ao período
	 * informado por parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorDataCadastro(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProgramacaoFormatura WHERE ((dataCadastro >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCadastro <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		sqlStr += " ORDER BY codigo;";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>titulo</code> da classe <code>ColacaoGrau</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorDataColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN colacaograu ON colacaograu.codigo = programacaoformaturaaluno.colacaograu");
		sqlStr.append(" WHERE (colacaograu.data >= '" + Uteis.getDataJDBC(prmIni) + "') AND (colacaograu.data <= '" + Uteis.getDataJDBC(prmFim) + "')");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ProgramacaoFormaturaVO> consultarPorDataRequerimento(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.codigo = programacaoformaturaaluno.requerimento");
		sqlStr.append(" WHERE (requerimento.data >= '" + Uteis.getDataJDBC(prmIni) + "') AND (requerimento.data <= '" + Uteis.getDataJDBC(prmFim) + "')");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>matricula</code> da classe <code>Matricula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorMatriculaMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" WHERE lower(matricula.matricula) like('" + valorConsulta.toLowerCase() + "%')");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>identificadorTurma</code> da classe <code>Turma</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matriculaperiodo.turma");
		sqlStr.append(" WHERE lower(turma.identificadorturma) like('" + valorConsulta.toLowerCase() + "%')");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>nome</code> da classe <code>Turno</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorNomeTurno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = matricula.turno");
		sqlStr.append(" WHERE lower(turno.nome) like('" + valorConsulta.toLowerCase() + "%')");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" WHERE lower(curso.nome) like('" + valorConsulta.toLowerCase() + "%')");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao
	 * parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProgramacaoFormatura WHERE codigo = " + valorConsulta.intValue() + " ";
//		if (!(usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
//			sqlStr += "AND unidadeEnsino = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
//		}
		sqlStr += "ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
	 * para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 */
	public static List<ProgramacaoFormaturaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ProgramacaoFormaturaVO> vetResultado = new ArrayList<ProgramacaoFormaturaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ProgramacaoFormaturaVO</code>.
	 * 
	 * @return O objeto da classe <code>ProgramacaoFormaturaVO</code> com os dados devidamente montados.
	 */
	public static ProgramacaoFormaturaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaVO obj = new ProgramacaoFormaturaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.getResponsavelCadastro().setCodigo(new Integer(dadosSQL.getInt("responsavelCadastro")));
		obj.setUnidadeConsultaApresentar(getFacadeFactory().getProgramacaoFormaturaFacade().consultarUnidadesVinculadas(dadosSQL.getInt("codigo")));
		obj.setProgramacaoFormaturaUnidadeEnsinoVOs(getFacadeFactory().getProgramacaoFormaturaUnidadeEnsinoInterfaceFacade().consultarPorProgramacaoMatricula(obj.getCodigo()));
		obj.getColacaoGrauVO().setCodigo(dadosSQL.getInt("colacaograu"));
		obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.setDataLimiteAssinaturaAta(dadosSQL.getTimestamp("dataLimiteAssinaturaAta"));
		obj.setNovoObj(Boolean.FALSE);		
		try {
			if(Uteis.isAtributoPreenchido(obj.getColacaoGrauVO())){
				obj.setColacaoGrauVO(getFacadeFactory().getColacaoGrauFacade().consultarPorChavePrimaria(obj.getColacaoGrauVO().getCodigo(), nivelMontarDados, usuario));
			}
		} catch (Exception e) {
			obj.setColacaoGrauVO(new ColacaoGrauVO());
		}
		montarDadosResponsavelCadastro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}		
		obj.setProgramacaoFormaturaAlunoVOs(getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarProgramacaoFormaturaAlunos(obj.getCodigo(), nivelMontarDados, usuario));
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>ProgramacaoFormaturaVO</code>. Faz uso da chave primária da classe
	 * <code>UsuarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelCadastro(ProgramacaoFormaturaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelCadastro().getCodigo().intValue() == 0) {
			return;
		}
		obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>ProgramacaoFormaturaVO</code>. Faz uso da chave primária da classe
	 * <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
//	public static void montarDadosUnidadeEnsino(ProgramacaoFormaturaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
//			return;
//		}
//		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
//	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ProgramacaoFormaturaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ProgramacaoFormaturaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ProgramacaoFormatura WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ProgramacaoFormatura ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ProgramacaoFormatura.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
	 * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProgramacaoFormatura.idEntidade = idEntidade;
	}
	
	public List<ProgramacaoFormaturaVO> consultarCodigoVinculadoColacaoGrau(ColacaoGrauVO colacaoGrauVO,UsuarioVO usuarioVO) throws Exception{
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT programacaoformatura.* FROM programacaoformatura ");		
		sqlStr.append("INNER JOIN colacaograu          ON programacaoformatura.colacaograu = colacaograu.codigo ");
		sqlStr.append("WHERE colacaograu.codigo = ").append(colacaoGrauVO.getCodigo().intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuarioVO));
	}

	public void executarExclusaoProgramacaoFormaturaVinculadaColacaoGrau(ColacaoGrauVO colacaoGrauVO,UsuarioVO usuarioVO) throws Exception{
		List<ProgramacaoFormaturaVO> listaProgramacaoFormaturaExclusao = this.consultarCodigoVinculadoColacaoGrau(colacaoGrauVO,usuarioVO);
		Iterator<ProgramacaoFormaturaVO> i = listaProgramacaoFormaturaExclusao.iterator();
		  while(i.hasNext()){
			  ProgramacaoFormaturaVO obj = i.next();
			  getFacadeFactory().getProgramacaoFormaturaAlunoFacade().removerVinculoProgramacaoFormatura(obj, usuarioVO);
			  this.excluir(obj, usuarioVO);
		  }
	}

	@Override
	public Integer consultarPorUnidadeEnsinoEColacaoGrau(Integer codigoUnidadeEnsino, Integer codigoColacaoGrau) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select programacaoformatura.codigo  from programacaoformatura");
		sql.append(" where exists(select programacaoformaturaunidaensino.codigo from  programacaoformaturaunidaensino where programacaoformaturaunidaensino.programacaoformatura = programacaoformatura.codigo and programacaoformaturaunidaensino.unidadeensino = ? ) ");
		sql.append(" and programacaoformatura.colacaograu = ? limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoUnidadeEnsino, codigoColacaoGrau);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("programacaoformatura.codigo");
		}

		return 0;
	}

	/**
	 * Metodo que inclui a {@link ProgramacaoFormaturaVO} caso não exista nenhuma programacao com
	 * a unidade de ensino e colação de grau.
	 */
	@Override
	public void incluirSeNaoExistirProgramacaoFormatura(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, MatriculaVO matriculaVO, ColacaoGrauVO colacaoGrauVO, UsuarioVO usuario) throws Exception {
		Integer codigoProgramacaoFormatura = this.consultarPorUnidadeEnsinoEColacaoGrau(matriculaVO.getUnidadeEnsino().getCodigo(), colacaoGrauVO.getCodigo());
		if (Uteis.isAtributoPreenchido(codigoProgramacaoFormatura)) {
			programacaoFormaturaAlunoVO.setProgramacaoFormatura(codigoProgramacaoFormatura);
		} else {
			ProgramacaoFormaturaVO obj = montarDadosProgramacaoFormatura(matriculaVO, usuario);
			obj.setValidarDados(false);
			incluir(obj, null, usuario, false);

			programacaoFormaturaAlunoVO.setProgramacaoFormatura(obj.getCodigo());
		}
	}

	/**
	 * Monta os dados recebendo como paramatro a unidade de ensino da matricula do aluno e usuario.
	 * 
	 * @param matriculaVO
	 * @param usuario
	 * @return
	 */
	private ProgramacaoFormaturaVO montarDadosProgramacaoFormatura(MatriculaVO matriculaVO, UsuarioVO usuario) {
		ProgramacaoFormaturaVO obj = new ProgramacaoFormaturaVO();
		obj.setDataCadastro(new Date());
		obj.setResponsavelCadastro(usuario);
//		obj.setUnidadeEnsino(matriculaVO.getUnidadeEnsino());
		return obj;
	}
	
	@Override
	public List<ProgramacaoFormaturaVO> consultarPorRegistroAcademico(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN pessoa on pessoa.codigo =  matricula.aluno ");		
		sqlStr.append(" WHERE pessoa.registroAcademico like ? ");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),valorConsulta+PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public String consultarUnidadesVinculadas(Integer codigoProgramacao) {
		String unidades = "";
		List<String> lista = new ArrayList<>(0);
		StringBuilder sql = new StringBuilder("SELECT unidadeensino.nome as \"unidadeensino.nome\" FROM programacaoformatura ");
		sql.append("INNER JOIN programacaoformaturaunidaensino ON programacaoformatura.codigo = programacaoformaturaunidaensino.programacaoformatura ");
		sql.append("INNER JOIN unidadeensino ON unidadeensino.codigo =  programacaoformaturaunidaensino.unidadeensino ");
		sql.append("WHERE programacaoformatura.codigo = "+codigoProgramacao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			lista.add(tabelaResultado.getString("unidadeensino.nome"));
		}
		for (String unidadeVO : lista) {
			if (!lista.get(lista.size() -1).equals(unidadeVO)) {
				unidades += unidadeVO+"; ";
			} else {
				unidades += unidadeVO;
			}
		}
		if (unidades.length() >= 150) {
			String uniSubstr = unidades.substring(0, 149);
			return uniSubstr += "...";
		}
		return unidades;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaVO programacaoFormaturaVO, List<TurnoVO> listaTurnoVOs, List<CursoVO> listaCursoVO, Integer turma, String matriculaAluno, Integer requerimento, Boolean filtrarEnade, Boolean filtrarTCC, Boolean filtrarCurriculoIntegralizado, UsuarioVO usuarioVO) throws Exception {
		ProgramacaoFormaturaVO.validarDados(programacaoFormaturaVO);
		if(!Uteis.isAtributoPreenchido(programacaoFormaturaVO)) {
//			incluir(programacaoFormaturaVO, null, usuarioVO);
		}
		List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = programacaoFormaturaVO.getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
		List<TurnoVO> listaTurno = listaTurnoVOs.stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
		List<CursoVO> listaCurso = listaCursoVO.stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
		final List<MatriculaVO> matriculaVOs = (getFacadeFactory().getMatriculaFacade().consultaCompletaSemProgramacaoFormatura(programacaoFormaturaVO.getCodigo(), listaProgramacao, listaTurno, listaCurso, turma, matriculaAluno, requerimento, programacaoFormaturaVO.getNivelEducacional(), filtrarEnade, filtrarTCC, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO).getPermiteProgramacaoFormaturaSemRequerimento(), TiposRequerimento.COLACAO_GRAU.getValor(), false, usuarioVO));
		boolean processarEmParalelo = true;
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		if(processarEmParalelo) {
			ConsistirException e = new ConsistirException();
			ProcessarParalelismo.executar(0, matriculaVOs.size(), e, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {
				MatriculaVO matricula = matriculaVOs.get(i);
				try {
					if (validarMatriculaAdicionadaNaProgramacaoFormatura(programacaoFormaturaVO, matricula.getMatricula())) {
						return;
					}        	
		            ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO = (new ProgramacaoFormaturaAlunoVO());
		            programacaoFormaturaAlunoVO.setMatricula(matricula);
		            programacaoFormaturaAlunoVO.getMatricula().getAluno().setCPF(matricula.getAluno().getCPF());
		            programacaoFormaturaAlunoVO.setCurriculoIntegralizado(getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(programacaoFormaturaAlunoVO.getMatricula(), programacaoFormaturaAlunoVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuarioVO, programacaoFormaturaAlunoVO));
		            programacaoFormaturaAlunoVO.setMatriculaEnadeVO(matricula.getMatriculaEnadeVO());
		            if(filtrarCurriculoIntegralizado && !programacaoFormaturaAlunoVO.isCurriculoIntegralizado()){
		            	return;
					}
		            programacaoFormaturaAlunoVO.setDataConclusaoCurso(matricula.getDataConclusaoCurso());
		            programacaoFormaturaAlunoVO.getColacaoGrau().setCodigo(programacaoFormaturaVO.getColacaoGrauVO().getCodigo());
		            programacaoFormaturaAlunoVO.setColouGrau("NI");
						/*
						 * List<RequerimentoVO> listaRequerimento =
						 * getFacadeFactory().getRequerimentoFacade().
						 * consultarPorMatriculaSituacaoDiferenteDe(matricula.getMatricula(), "FI",
						 * "CG",matricula.getUnidadeEnsino().getCodigo(), false,
						 * Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO,
						 * getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matricula.
						 * getUnidadeEnsino().getCodigo(), usuarioVO)); if
						 * (Uteis.isAtributoPreenchido(listaRequerimento)) {
						 * programacaoFormaturaAlunoVO.setRequerimento(listaRequerimento.get(0).
						 * getCodigo()); }
						 */
		            programacaoFormaturaAlunoVO.setProgramacaoFormatura(programacaoFormaturaVO.getCodigo());
		            adicionarProgramacaoFormaturaAluno(programacaoFormaturaVO, programacaoFormaturaAlunoVO);
		            //programacaoFormaturaAlunoVO = null;
				}catch (Exception ex) {
					e.adicionarListaMensagemErro(ex.getMessage());	
				}
			}
		});
		if(!e.getListaMensagemErro().isEmpty()) {
			throw e;
		} 
		}else {
			for (MatriculaVO matricula : matriculaVOs) {
				try {
					if (validarMatriculaAdicionadaNaProgramacaoFormatura(programacaoFormaturaVO, matricula.getMatricula())) {
		        		continue;
		        	}        	
		            ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO = (new ProgramacaoFormaturaAlunoVO());
		            programacaoFormaturaAlunoVO.setMatricula(matricula);
		            programacaoFormaturaAlunoVO.setCurriculoIntegralizado(getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(programacaoFormaturaAlunoVO.getMatricula(), programacaoFormaturaAlunoVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuarioVO, programacaoFormaturaAlunoVO));
		            programacaoFormaturaAlunoVO.setMatriculaEnadeVO(matricula.getMatriculaEnadeVO());
		            if(filtrarCurriculoIntegralizado && !programacaoFormaturaAlunoVO.isCurriculoIntegralizado()){
						continue;
					}
		            programacaoFormaturaAlunoVO.setDataConclusaoCurso(matricula.getDataConclusaoCurso());
		            programacaoFormaturaAlunoVO.getColacaoGrau().setCodigo(programacaoFormaturaVO.getColacaoGrauVO().getCodigo());
		            programacaoFormaturaAlunoVO.setColouGrau("NI");
					/*
					 * List<RequerimentoVO> listaRequerimento =
					 * getFacadeFactory().getRequerimentoFacade().
					 * consultarPorMatriculaSituacaoDiferenteDe(matricula.getMatricula(), "FI",
					 * "CG",matricula.getUnidadeEnsino().getCodigo(), false,
					 * Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO,
					 * getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matricula.
					 * getUnidadeEnsino().getCodigo(), usuarioVO)); if
					 * (Uteis.isAtributoPreenchido(listaRequerimento)) {
					 * programacaoFormaturaAlunoVO.setRequerimento(listaRequerimento.get(0).
					 * getCodigo()); }
					 */
		            programacaoFormaturaAlunoVO.setProgramacaoFormatura(programacaoFormaturaVO.getCodigo());
		            adicionarProgramacaoFormaturaAluno(programacaoFormaturaVO, programacaoFormaturaAlunoVO);              	      
				} catch (Exception e) {
					throw e;
				}
	        }
		}
		stopwatch.stop();
    	System.out.println("TEMPO DE PROCESSAMENTO TOTAL Método adicionarProgramacaoFormaturaAlunoVO : "+stopwatch.getElapsed()); 
	}
	
	private synchronized Boolean validarMatriculaAdicionadaNaProgramacaoFormatura(ProgramacaoFormaturaVO programacaoFormaturaVO, String maticula) {
		if (programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs().stream().map(pfavo -> pfavo.getMatricula().getMatricula()).anyMatch(m -> m.equals(maticula))) {
    		return true;
    	}
		return false;
	}
	
	private synchronized void adicionarProgramacaoFormaturaAluno(ProgramacaoFormaturaVO programacaoFormaturaVO, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) {
		 programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs().add(programacaoFormaturaAlunoVO);  
	}

	@Override
	public List<ProgramacaoFormaturaVO> consultarPorProgramacaoUnidadeEnsino(String valorConsulta, int nivel, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		String sqlStr = "SELECT ProgramacaoFormatura.* FROM ProgramacaoFormatura WHERE ";
		sqlStr += "EXISTS(SELECT pfu.programacaoformatura FROM programacaoformaturaunidaensino pfu ";
		sqlStr += "INNER JOIN unidadeensino un ON un.codigo = pfu.unidadeensino ";
		sqlStr += "INNER JOIN programacaoformatura pff ON ProgramacaoFormatura.codigo = pfu.programacaoformatura ";
		sqlStr += "WHERE sem_acentos(un.nome) ilike(sem_acentos(?))) ";
		sqlStr += "ORDER BY ProgramacaoFormatura.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, "%"+valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivel, usuarioVO);
	}

	@Override
	public List consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN pessoa on pessoa.codigo = matricula.aluno");
		sqlStr.append(" WHERE sem_acentos(pessoa.nome) ilike(sem_acentos(?))");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<ProgramacaoFormaturaVO> consultarPorCodigoEUnidadeEnsinos(Integer valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT programacaoFormatura.* FROM ProgramacaoFormatura ");		
		sql.append("WHERE programacaoFormatura.codigo >= " + valorConsulta.intValue() + " ");
		if(Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
		sql.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformatura.codigo = programacaoformaturaunidaensino.programacaoformatura and  programacaoformaturaunidaensino.unidadeensino in (");
		for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
			if (unidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
				sql.append(unidade.getUnidadeEnsino().getCodigo()).append(") ");
			} else {
				sql.append(unidade.getUnidadeEnsino().getCodigo()).append(", ");
			}
		}
		sql.append(") ");
		}
		sql.append("ORDER BY programacaoFormatura.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<ProgramacaoFormaturaVO> consultarPorColacaoGrau(String valorConsultaProgramacao, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelmontardados, UsuarioVO usuarioLogado) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct programacaoFormatura.* FROM ProgramacaoFormatura ");		
		sql.append("INNER JOIN colacaograu ON colacaograu.codigo = programacaoformatura.colacaograu ");
		sql.append("WHERE (sem_acentos(colacaograu.titulo)) ILIKE (upper(sem_acentos(?))) ");
		if(Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sql.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformatura.codigo = programacaoformaturaunidaensino.programacaoformatura and  programacaoformaturaunidaensino.unidadeensino in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (unidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sql.append(unidade.getUnidadeEnsino().getCodigo()).append(") ");
				} else {
					sql.append(unidade.getUnidadeEnsino().getCodigo()).append(", ");
				}
			}
			sql.append(") ");
			}
		sql.append("ORDER BY programacaoFormatura.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsultaProgramacao + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelmontardados, usuarioLogado));
	}
	
	/**
	 * Responsável por realizar uma consulta de <code>ProgramacaoFormatura</code> através do valor do atributo <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ProgramacaoFormaturaVO> consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT distinct ProgramacaoFormatura.* FROM ProgramacaoFormatura inner join programacaoformaturaunidaensino on programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo inner join unidadeensino on unidadeensino.codigo = programacaoformaturaunidaensino.unidadeensino WHERE 1=1 ";
		if (!(usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr += " AND unidadeEnsino.codigo = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ";
		}
		sqlStr += " AND UnidadeEnsino.nome  ilike(?) ";
		sqlStr += "ORDER BY ProgramacaoFormatura.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorCodigoFiltroAlunosPresentesColacaoGrau(Integer valorConsulta, String colouGrau, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProgramacaoFormatura WHERE codigo = " + valorConsulta.intValue() + " ";
		if (!(usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr += " AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + usuario.getUnidadeEnsinoLogado().getCodigo() + ") ";
		}
		sqlStr += " and exists ( select programacaoformaturaaluno.codigo from programacaoformaturaaluno";
		sqlStr += " where programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo and programacaoformaturaaluno.colougrau = '" +colouGrau+"' )";
		sqlStr += "ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorNomeUnidadeEnsinoFiltroAlunosPresentesColacaoGrau(String valorConsulta, String colouGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT ProgramacaoFormatura.* FROM ProgramacaoFormatura WHERE ";
		sqlStr += "EXISTS(SELECT pfu.programacaoformatura FROM programacaoformaturaunidaensino pfu ";
		sqlStr += "INNER JOIN unidadeensino un ON un.codigo = pfu.unidadeensino ";
		sqlStr += "INNER JOIN programacaoformatura pff ON ProgramacaoFormatura.codigo = pfu.programacaoformatura ";
		sqlStr += "WHERE sem_acentos(un.nome) ilike(sem_acentos(?)))";
		sqlStr += " and exists ( select programacaoformaturaaluno.codigo from programacaoformaturaaluno";
		sqlStr += " where programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo and programacaoformaturaaluno.colougrau = '" +colouGrau+"' )";
		sqlStr += "ORDER BY ProgramacaoFormatura.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, "%"+valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);		
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorNomeCursoFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" WHERE sem_acentos(curso.nome) ilike(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + unidadeEnsino+ ") ");
		}
		sqlStr.append(" and programacaoformaturaaluno.colougrau = '").append(colouGrau).append("' ");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorNomeTurnoFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = matricula.turno");
		sqlStr.append(" WHERE sem_acentos(turno.nome) ilike(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + unidadeEnsino + ") ");
		}
		sqlStr.append("  and programacaoformaturaaluno.colougrau = '").append(colouGrau).append("' ");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorIdentificadorTurmaFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matriculaperiodo.turma");
		sqlStr.append(" WHERE sem_acentos(turma.identificadorturma) ilike(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + unidadeEnsino + ") ");
		}
		sqlStr.append(" and exists ( select programacaoformaturaaluno.codigo from programacaoformaturaaluno");
		sqlStr.append(" where programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo and programacaoformaturaaluno.colougrau = '").append(colouGrau).append("' )");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorMatriculaMatriculaFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = programacaoformaturaaluno.matricula");
		sqlStr.append(" WHERE sem_acentos(matricula.matricula) ilike(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + unidadeEnsino + ") ");
		}
		sqlStr.append(" and exists ( select programacaoformaturaaluno.codigo from programacaoformaturaaluno");
		sqlStr.append(" where programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo and programacaoformaturaaluno.colougrau = '").append(colouGrau).append("' )");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorNomeUsuarioFiltroAlunosPresentesColacaoGrau(String valorConsulta, String colouGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT DISTINCT ProgramacaoFormatura.* FROM ProgramacaoFormatura  inner join Usuario on ProgramacaoFormatura.responsavelCadastro = Usuario.codigo where sem_acentos( Usuario.nome ) ilike(sem_acentos(?)) ";
		sqlStr += " and exists ( select programacaoformaturaaluno.codigo from programacaoformaturaaluno";
		sqlStr += " where programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo and programacaoformaturaaluno.colougrau = '" +colouGrau+"' )";
		sqlStr += " ORDER BY programacaoformatura.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorDataRequerimentoFiltroAlunosPresentesColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.codigo = programacaoformaturaaluno.requerimento");
		sqlStr.append(" WHERE (requerimento.data >= '" + Uteis.getDataJDBC(prmIni) + "') AND (requerimento.data <= '" + Uteis.getDataJDBC(prmFim) + "')");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + unidadeEnsino + ") ");
		}
		sqlStr.append(" and programacaoformaturaaluno.colougrau = '").append(colouGrau).append("' ");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorDataColacaoGrauFiltroAlunosPresentesColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT programacaoformatura.* from programacaoformatura");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo");
		sqlStr.append(" LEFT JOIN colacaograu ON colacaograu.codigo = programacaoformaturaaluno.colacaograu");
		sqlStr.append(" WHERE (colacaograu.data >= '" + Uteis.getDataJDBC(prmIni) + "') AND (colacaograu.data <= '" + Uteis.getDataJDBC(prmFim) + "')");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + unidadeEnsino + ") ");
		}
		sqlStr.append(" and programacaoformaturaaluno.colougrau = '").append(colouGrau).append("' ");
		sqlStr.append(" ORDER BY programacaoformatura.codigo;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<ProgramacaoFormaturaVO> consultarPorDataCadastroFiltroAlunosPresentesColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProgramacaoFormatura WHERE ((dataCadastro >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCadastro <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr += (" AND exists (select programacaoformaturaunidaensino.codigo from programacaoformaturaunidaensino where programacaoformaturaunidaensino.ProgramacaoFormatura = ProgramacaoFormatura.codigo and programacaoformaturaunidaensino.unidadeensino  = " + unidadeEnsino + ") ");
		}
		sqlStr += " and exists ( select programacaoformaturaaluno.codigo from programacaoformaturaaluno";
		sqlStr += " where programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo and programacaoformaturaaluno.colougrau = '" +colouGrau+"' )";
		sqlStr += " ORDER BY codigo;";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}	
	
	@Override
	public Boolean validarDataLimitePodeAssinarAta(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		if (Uteis.isAtributoPreenchido(programacaoFormaturaVO)) {			
			if(Uteis.isAtributoPreenchido(programacaoFormaturaVO.getDataLimiteAssinaturaAta()) && 
			   Uteis.isAtributoPreenchido(programacaoFormaturaVO.getDataLimiteAssinaturaAta()) && programacaoFormaturaVO.getDataLimiteAssinaturaAta().before(new Date())) {
			   return false;
			}
		}
		return true;
	}

	/**
	 * Consulta irá trazer a programação de formatura na qual o aluno está pendente
	 * colar grau ou colou grau
	 * chamado 40903
	 * 
	 * @param programacaoFormaturaAlunoVO
	 * @param nivelMontarDados
	 * @param usuario
	 * @author Felipi Alves
	 */
	@Override
	public ProgramacaoFormaturaVO consultarProgramacaoFormaturaAtivaPorProgramacaoFormaturaAluno(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM programacaoformatura ");
		sql.append("WHERE EXISTS ( ");
		sql.append("SELECT FROM programacaoformaturaaluno ");
		sql.append("WHERE programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo AND programacaoformaturaaluno.matricula = ? AND programacaoformaturaaluno.codigo <> ? AND programacaoformaturaaluno.colougrau <> 'NO') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), programacaoFormaturaAlunoVO.getMatricula().getMatricula(), programacaoFormaturaAlunoVO.getCodigo());
		if (!tabelaResultado.next()) {
			return new ProgramacaoFormaturaVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
		
	public List<ProgramacaoFormaturaAlunoVO> realizarProcessamentoExcelPlanilha(FileUploadEvent uploadEvent) throws Exception {
	    String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
	    Sheet sheet = null;
	    int rowMax = 0;
	    try (InputStream inputStream = uploadEvent.getUploadedFile().getInputStream()) {
	        if ("xlsx".equals(extensao)) {
	            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	            sheet = workbook.getSheetAt(0);
	            rowMax = sheet.getLastRowNum();
	        } else if ("xls".equals(extensao)) {
	            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
	            sheet = workbook.getSheetAt(0);
	            rowMax = sheet.getLastRowNum();
	        } else {
	            throw new IllegalArgumentException("Formato de arquivo não suportado: " + extensao);
	        }
	    }
	    List<ProgramacaoFormaturaAlunoVO> listaAlunos = new ArrayList<>();
	    for (int linha = 1; linha <= rowMax; linha++) {
	        Row row = sheet.getRow(linha);
	        if (row == null) {
	        	continue;
	        }
	        ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO = new ProgramacaoFormaturaAlunoVO();
	        List<String> listaErros = new ArrayList<String>();
	        String programacaoFormatura = processarCelula(row, 0, "Nº Programação de Formatura", "A", listaErros);
	        String polo = processarCelula(row, 1, "Polo", "B", listaErros);
	        String matricula = processarCelula(row, 2, "Matrícula", "C", listaErros);
	        String nome = processarCelula(row, 3, "Nome do Aluno", "D", listaErros);
	        String email = processarCelula(row, 4, "Email do Aluno", "E", listaErros);
	        String presenca = processarCelula(row, 6, "Presença da Colação de Grau", "G", listaErros);
	        if(programacaoFormatura == null) {
	        	programacaoFormatura = "0.0";
	        }
	        programacaoFormaturaAlunoVO.setProgramacaoFormatura((int) Double.parseDouble(programacaoFormatura));
	        programacaoFormaturaAlunoVO.getMatricula().getUnidadeEnsino().setNome(polo);
	        programacaoFormaturaAlunoVO.getMatricula().setMatricula(matricula);
	        programacaoFormaturaAlunoVO.getMatricula().getAluno().setNome(nome);
	        programacaoFormaturaAlunoVO.getMatricula().getAluno().setEmail(email);
	        programacaoFormaturaAlunoVO.setColouGrau("Presente".equalsIgnoreCase(presenca) ? "SI" : "Ausente".equalsIgnoreCase(presenca) ? "NO" : "NI");
	        programacaoFormaturaAlunoVO.setListaMensagemErroProcessamento(listaErros);
	        listaAlunos.add(programacaoFormaturaAlunoVO);
	    }
	    return listaAlunos;
	}

	private String processarCelula(Row row, int colunaIndex, String nomeCampo, String colunaLetra, List<String> listaErros) {
		Cell cell = row.getCell(colunaIndex);
	    if (cell == null || cell.toString().trim().isEmpty()) {
	        String mensagemErro = String.format("%s não informada. (linha %d, coluna %s)", nomeCampo, row.getRowNum() + 1, colunaLetra);
	        listaErros.add(mensagemErro);
	        return null;
	    }
	    return cell.toString().trim();
	}
	
	public void realizarProcessamentoAlunosAusentes(String situacao, ProgramacaoFormaturaVO programacaoFormaturaVO, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosAusentes, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosEncontrados) {
		if(Uteis.isAtributoPreenchido(listaProgramacaoFormaturaAlunosAusentes)) {
			if(situacao.equals("EXCLUSAO")) {
				programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs().removeIf(programacaoFormaturaAlunoVO ->  listaProgramacaoFormaturaAlunosAusentes
			        .stream().anyMatch(p -> p.getMatricula().getMatricula().equals(programacaoFormaturaAlunoVO.getMatricula().getMatricula())));
			}
			if(situacao.equals("AUSENTE")) {
				for (ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO : programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs()) {
					if(listaProgramacaoFormaturaAlunosAusentes.stream().anyMatch(p -> p.getMatricula().getMatricula().equals(programacaoFormaturaAlunoVO.getMatricula().getMatricula()))) {
						programacaoFormaturaAlunoVO.setColouGrau("NO");
					}
				}
			}
		}
		if(Uteis.isAtributoPreenchido(listaProgramacaoFormaturaAlunosEncontrados)) {
			Map<String, ProgramacaoFormaturaAlunoVO> mapaAlunosEncontrados = listaProgramacaoFormaturaAlunosEncontrados.stream()
					.collect(Collectors.toMap(aluno -> aluno.getMatricula().getMatricula(), aluno -> aluno));
			for (ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO : programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs()) {
			    ProgramacaoFormaturaAlunoVO alunoEncontrado = mapaAlunosEncontrados.get(programacaoFormaturaAlunoVO.getMatricula().getMatricula());
			    if (alunoEncontrado != null) {
			        programacaoFormaturaAlunoVO.setColouGrau(alunoEncontrado.getColouGrau());
			    }
			}
		}	
	}
}
