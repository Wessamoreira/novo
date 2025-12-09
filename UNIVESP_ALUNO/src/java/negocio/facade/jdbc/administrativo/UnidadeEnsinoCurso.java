package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;

import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.administrativo.UnidadeEnsinoCursoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>UnidadeEnsinoCursoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>UnidadeEnsinoCursoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see UnidadeEnsinoCursoVO
 * @see ControleAcesso
 * @see UnidadeEnsino
 */
/**
 * @author otimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsinoCurso extends ControleAcesso implements UnidadeEnsinoCursoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public UnidadeEnsinoCurso() throws Exception {
		super();
		setIdEntidade("UnidadeEnsino");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>.
	 */
	public UnidadeEnsinoCursoVO novo() throws Exception {
		UnidadeEnsinoCurso.incluir(getIdEntidade());
		UnidadeEnsinoCursoVO obj = new UnidadeEnsinoCursoVO();
		return obj;
	}
	
	

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoCursoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception {
		UnidadeEnsinoCursoVO.validarDados(obj);
		final String sql = "INSERT INTO UnidadeEnsinoCurso( unidadeEnsino, curso, situacaoCurso, nrVagasPeriodoLetivo, turno, planoFinanceiroCurso, mantenedora, mantida, valorMensalidade, codigoItemListaServico, codigoInep ,codigoCursoUnidadeEnsinoGinfes) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlInserir = con.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getUnidadeEnsino().intValue());
				sqlInserir.setInt(2, obj.getCurso().getCodigo().intValue());
				sqlInserir.setString(3, obj.getSituacaoCurso());
				sqlInserir.setInt(4, obj.getNrVagasPeriodoLetivo().intValue());
				sqlInserir.setInt(5, obj.getTurno().getCodigo().intValue());
//				if (obj.getPlanoFinanceiroCurso().getCodigo().intValue() != 0) {
//					sqlInserir.setInt(6, obj.getPlanoFinanceiroCurso().getCodigo().intValue());
//				} else {
//					sqlInserir.setNull(6, 0);
//				}
				sqlInserir.setString(7, obj.getMantenedora());
				sqlInserir.setString(8, obj.getMantida());
				sqlInserir.setDouble(9, obj.getValorMensalidade());
				sqlInserir.setString(10, obj.getCodigoItemListaServico());
				sqlInserir.setInt(11, obj.getCodigoInep());
                sqlInserir.setInt(12, obj.getCodigoCursoUnidadeEnsinoGinfes());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet rs) throws SQLException {
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
	 * <code>UnidadeEnsinoCursoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoCursoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UnidadeEnsinoCursoVO obj) throws Exception {
		UnidadeEnsinoCursoVO.validarDados(obj);
		final String sql = "UPDATE UnidadeEnsinoCurso set situacaoCurso=?, nrVagasPeriodoLetivo=?, turno=? , unidadeEnsino = ?, curso = ?, planoFinanceiroCurso=?, mantenedora=?, Mantida=?, valorMensalidade=?, codigoItemListaServico=?, codigoInep= ? , codigoCursoUnidadeEnsinoGinfes=?  WHERE codigo=?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getSituacaoCurso());
				sqlAlterar.setInt(2, obj.getNrVagasPeriodoLetivo().intValue());
				sqlAlterar.setInt(3, obj.getTurno().getCodigo().intValue());
				sqlAlterar.setInt(4, obj.getUnidadeEnsino().intValue());
				sqlAlterar.setInt(5, obj.getCurso().getCodigo().intValue());
//				if (obj.getPlanoFinanceiroCurso().getCodigo().intValue() != 0) {
//					sqlAlterar.setInt(6, obj.getPlanoFinanceiroCurso().getCodigo().intValue());
//				} else {
//					sqlAlterar.setNull(6, 0);
//				}
				sqlAlterar.setString(7, obj.getMantenedora());
				sqlAlterar.setString(8, obj.getMantida());
				sqlAlterar.setDouble(9, obj.getValorMensalidade());
				sqlAlterar.setString(10, obj.getCodigoItemListaServico());
				sqlAlterar.setInt(11, obj.getCodigoInep());
                sqlAlterar.setInt(12, obj.getCodigoCursoUnidadeEnsinoGinfes());
				sqlAlterar.setInt(13, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValoresCursoGinfes(final UnidadeEnsinoCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
//			UnidadeEnsinoCursoValoresGinfes.alterar(UnidadeEnsinoCursoValoresGinfes.idEntidade, verificarAcesso, usuario);
			alterar(obj, "UnidadeEnsinoCurso", new AtributoPersistencia()
					.add("codigoCursoUnidadeEnsinoGinfes", obj.getCodigoCursoUnidadeEnsinoGinfes())
					.add("codigoItemListaServico", obj.getCodigoItemListaServico())
					.add("valorMensalidade", obj.getValorMensalidade())
					, new AtributoPersistencia().add("codigo", obj.getCodigo())
					, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UnidadeEnsinoCursoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoCursoVO obj) throws Exception {
		UnidadeEnsinoCurso.excluir(getIdEntidade());
		String sql = "DELETE FROM UnidadeEnsinoCurso WHERE ((unidadeEnsino = ?) and (curso = ?) and (turno = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getUnidadeEnsino().intValue(), obj.getCurso().getCodigo().intValue(), obj.getTurno().getCodigo().intValue() });
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsinoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Turno</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoCursoVO> consultarPorNomeTurno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso, Turno, UnidadeEnsino WHERE UnidadeEnsinoCurso.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsinoCurso.turno = Turno.codigo and Turno.nome like('" + valorConsulta + "%') ORDER BY Turno.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoCursoVO> consultarPorNomeTurnoUnidadeEnsino(String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso, Turno, UnidadeEnsino WHERE UnidadeEnsinoCurso.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsinoCurso.turno = Turno.codigo and Turno.nome like('" + valorConsulta + "%') and UnidadeEnsinoCurso.unidadeensino = " + unidadeEnsinoCodigo.intValue() + " ORDER BY Turno.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsinoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Curso</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoCursoVO> consultarPorNomeCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso, Curso, UnidadeEnsino WHERE UnidadeEnsinoCurso.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsinoCurso.curso = Curso.codigo and Curso.nome ilike('" + valorConsulta + "%') ORDER BY Curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCurso(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorCodigoCurso(valorConsulta, nivelMontarDados, true, usuario);
	}

	public UnidadeEnsinoCursoVO consultarPorMatriculaAluno(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT uec.*, m.*, unidadeensino.nome as \"unidadeensino.nome\" FROM unidadeensinocurso uec ");
		sqlStr.append("INNER JOIN matricula m ON uec.curso = m.curso AND uec.unidadeensino = m.unidadeensino ");
		sqlStr.append("INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = uec.unidadeensino ");
		sqlStr.append("WHERE m.matricula = '").append(valorConsulta).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		if (!tabelaResultado.next()) {
			throw new Exception();
		}
		return montarDados(tabelaResultado, unidadeEnsinoCursoVO, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoCursoVO> consultaUnidadeEnsinoCursoDoProfessor(Integer professor, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("select distinct unidadeEnsinoCurso.unidadeEnsino AS codigoUnidade, unidadeEnsinoCurso.curso AS codigoCurso, unidadeEnsino.nome AS nomeUnidade, curso.nome AS nomeCurso from turma ");
		sql.append("INNER JOIN curso on turma.curso = curso.codigo ");
		sql.append("INNER JOIN unidadeEnsino on turma.unidadeEnsino = unidadeEnsino.codigo ");
		sql.append("INNER JOIN unidadeEnsinoCurso on unidadeEnsino.codigo = unidadeEnsinoCurso.unidadeEnsino AND unidadeEnsinoCurso.curso = curso.codigo ");
		sql.append("INNER JOIN horarioTurmaProfessorDisciplina htpd on turma.codigo = htpd.turma ");
		sql.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sql.append("WHERE htpd.professor = ").append(professor).append(" ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append("AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<UnidadeEnsinoCursoVO> listaUnidadeEnsinoCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
			unidadeEnsinoCursoVO.setNomeUnidadeEnsino(tabelaResultado.getString("nomeUnidade"));
			unidadeEnsinoCursoVO.setUnidadeEnsino(tabelaResultado.getInt("codigoUnidade"));
			unidadeEnsinoCursoVO.getCurso().setNome(tabelaResultado.getString("nomeCurso"));
			unidadeEnsinoCursoVO.getCurso().setCodigo(tabelaResultado.getInt("codigoCurso"));
			listaUnidadeEnsinoCurso.add(unidadeEnsinoCursoVO);
		}
		return listaUnidadeEnsinoCurso;
	}

	public UnidadeEnsinoCursoVO consultarPorCursoTurnoPlanoFinanceiro(Integer curso, Integer turno, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT unidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM unidadeensinocurso ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeEnsino ");
		sqlStr.append("WHERE unidadeEnsinocurso.curso = ").append(curso).append(" AND unidadeEnsinocurso.turno = ").append(turno).append(" AND unidadeEnsinoCurso.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		if (!tabelaResultado.next()) {
			throw new Exception();
		}
		return montarDados(tabelaResultado, unidadeEnsinoCursoVO, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCurso(Integer valorConsulta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), verificarAcesso, usuario);
		String sqlStr = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso, Curso, unidadeensino WHERE unidadeensino.codigo = UnidadeEnsinoCurso.unidadeensino and UnidadeEnsinoCurso.curso = Curso.codigo and Curso.codigo = " + valorConsulta + " ORDER BY Curso.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso ");
		sqlStr.append("INNER JOIN curso ON UnidadeEnsinoCurso.curso = Curso.codigo ");
		sqlStr.append("INNER JOIN unidadeensino ON UnidadeEnsinoCurso.unidadeensino = unidadeensino.codigo ");
		sqlStr.append("WHERE UnidadeEnsinoCurso.unidadeensino = ").append(valorConsulta).append(" ORDER BY Curso.Nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorUnidadeEnsino(Integer unidadeEnsinoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (UnidadeEnsinoCurso.unidadeEnsino = ");
		sqlStr.append(unidadeEnsinoPrm);
		sqlStr.append(")");
		sqlStr.append("ORDER BY curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);

	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsinoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>UnidadeEnsino</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<UnidadeEnsinoCursoVO> consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso, UnidadeEnsino " + "WHERE UnidadeEnsinoCurso.unidadeEnsino = UnidadeEnsino.codigo and sem_acentos(UnidadeEnsino.nome) ilike sem_acentos(('%" + valorConsulta + "%')) ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoUnidadeEnsino(Integer curso, Integer unidadeEnsino, String situacaoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" ");
		sqlStr.append("FROM unidadeensinocurso ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sqlStr.append("WHERE unidadeensinocurso.curso = ").append(curso);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensinocurso.unidadeensino = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(situacaoCurso)) {
			sqlStr.append(" and unidadeensinocurso.situacaocurso = '").append(situacaoCurso).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoUnidadeEnsinoNivelEducacional(Integer valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM unidadeensinocurso ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = unidadeensinocurso.curso");
		sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = unidadeensinocurso.unidadeensino");
		sqlStr.append(" WHERE unidadeensinocurso.curso >= ").append(valorConsulta);
		if (!nivelEducacional.equals("TO")) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("'");
		}
		if (unidadeEnsinoCodigo != null && unidadeEnsinoCodigo != 0) {
			sqlStr.append(" AND unidadeensinocurso.unidadeensino = ").append(unidadeEnsinoCodigo);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoUnidadeEnsinoPeriodicidade(Integer valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM unidadeensinocurso" + " inner join unidadeensino on (unidadeensinocurso.unidadeensino = unidadeensino.codigo) " + " inner join curso on (unidadeensinocurso.curso = curso.codigo) WHERE unidadeensinocurso.curso >= " + valorConsulta.intValue();
		if (!nivelEducacional.equals("")) {
			if (nivelEducacional.equals("SU")) {
				// neste caso temos que filtrar os diferentes tipos de cursos
				// superior existentes,
				// a citar: superior, graduação tecnologica ou sequencial
				sqlStr = sqlStr.concat(" and ((curso.nivelEducacional = 'SU') OR " + "(curso.nivelEducacional = 'GT') OR " + "(curso.nivelEducacional = 'SE')) and unidadeensinocurso.unidadeensino = " + unidadeEnsinoCodigo + " ");
			} else {
				sqlStr = sqlStr.concat(" and (curso.nivelEducacional = '" + nivelEducacional.toUpperCase() + "') and unidadeensinocurso.unidadeensino = " + unidadeEnsinoCodigo + " ");
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsino(String nomeCurso, Integer unidadeEnsino, boolean apresentarHomePreInscricao, String situacaoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" ");
		sqlStr.append("FROM unidadeensinocurso ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sqlStr.append("inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sqlStr.append("WHERE upper(sem_acentos(curso.nome)) ilike(sem_acentos('").append(nomeCurso).append("%'))");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensinocurso.unidadeensino = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(situacaoCurso)) {
			sqlStr.append(" and unidadeensinocurso.situacaocurso = '").append(situacaoCurso).append("'");
		}
		if (apresentarHomePreInscricao) {
			sqlStr.append(" and curso.apresentarHomePreInscricao = ").append(apresentarHomePreInscricao);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsinoNivelEducacional(String valorConsulta, String nivelEducacional, Integer unidadeEnsinoCodigo, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), verificarAcesso, usuario);
		String sqlStr = "SELECT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM unidadeensinocurso, curso, unidadeensino " + "where unidadeensinocurso.unidadeensino = unidadeensino.codigo and unidadeensinocurso.curso = curso.codigo and  curso.nome ilike ('" + valorConsulta + "%') and curso.nivelEducacional in '" + nivelEducacional + "' and unidadeensinocurso.unidadeensino = " + unidadeEnsinoCodigo.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsinoCodigo, boolean verificarAcesso,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		try {
			sqlStr.append("SELECT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM unidadeensinocurso, curso, unidadeEnsino " + "where unidadeensinocurso.unidadeensino = unidadeensino.codigo and unidadeensinocurso.curso = curso.codigo ");
			sqlStr.append("and  curso.nome ilike('");
			sqlStr.append(valorConsulta);
			sqlStr.append("%') ");
			if (unidadeEnsinoCodigo != 0) {
				sqlStr.append("and unidadeensinocurso.unidadeensino = ");
				sqlStr.append(unidadeEnsinoCodigo.intValue());
			}
			sqlStr.append(" ORDER BY curso.nome");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario, String periodicidade) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM unidadeensinocurso");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = unidadeensinocurso.curso");
		sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = unidadeensinocurso.unidadeensino");
		sqlStr.append(" WHERE (curso.nome) ilike (?) ");
		// Nivel TO = "Todos"
		if (Uteis.isAtributoPreenchido(nivelEducacional) && !nivelEducacional.equals("TO")) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("'");
		}
		if (unidadeEnsinoCodigo != null && unidadeEnsinoCodigo != 0) {
			sqlStr.append(" AND unidadeensinocurso.unidadeensino = ").append(unidadeEnsinoCodigo);
		}
		if (periodicidade != null && Uteis.isAtributoPreenchido(periodicidade)) {
			sqlStr.append(" and curso.periodicidade = '").append(periodicidade).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString() , valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoUnidadeEnsinoPeriodicidade(String valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" ");
		sqlStr.append(" FROM unidadeensinocurso ");
		sqlStr.append("INNER JOIN curso         ON unidadeensinocurso.curso         = curso.codigo ");
		sqlStr.append("INNER JOIN unidadeensino ON unidadeensinocurso.unidadeensino = unidadeensino.codigo ");
		sqlStr.append("WHERE SEM_ACENTOS(curso.nome) ILIKE ('").append(Uteis.removerAcentos(valorConsulta)).append("%') ");
		sqlStr.append("AND unidadeensinocurso.unidadeensino = ").append(unidadeEnsinoCodigo.intValue());
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional.toUpperCase()).append("';");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 */
	public  List<UnidadeEnsinoCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoCursoVO> vetResultado = new ArrayList<UnidadeEnsinoCursoVO>(0);
		UnidadeEnsinoCursoVO obj = null;
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code>.
	 * 
	 * @return O objeto da classe <code>UnidadeEnsinoCursoVO</code> com os dados
	 *         devidamente montados.
	 */
	public  UnidadeEnsinoCursoVO montarDados(SqlRowSet dadosSQL, UnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// //System.out.println(">> Montar dados(UnidadeEnsinoCurso) - " + new
		// Date());
		obj = new UnidadeEnsinoCursoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setUnidadeEnsino(dadosSQL.getInt("unidadeEnsino"));
		obj.setNomeUnidadeEnsino(dadosSQL.getString("unidadeEnsino.nome"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.setSituacaoCurso(dadosSQL.getString("situacaoCurso"));
		obj.setNrVagasPeriodoLetivo(dadosSQL.getInt("nrVagasPeriodoLetivo"));
		obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
//		obj.getPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("planoFinanceiroCurso"));
		obj.setMantenedora(dadosSQL.getString("mantenedora"));
		obj.setMantida(dadosSQL.getString("mantida"));
		obj.setValorMensalidade(dadosSQL.getDouble("valorMensalidade"));
		obj.setCodigoItemListaServico(dadosSQL.getString("codigoItemListaServico"));
		obj.setCodigoCursoUnidadeEnsinoGinfes(dadosSQL.getInt("codigoCursoUnidadeEnsinoGinfes"));
		obj.setCodigoInep(dadosSQL.getInt("codigoInep"));
        obj.setCodigoCursoUnidadeEnsinoGinfes(dadosSQL.getInt("codigoCursoUnidadeEnsinoGinfes"));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		montarDadosPlanoFinanceiroCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TurnoVO</code> relacionado ao objeto
	 * <code>UnidadeEnsinoCursoVO</code>. Faz uso da chave primária da classe
	 * <code>TurnoVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosTurno(UnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			obj.setTurno(new TurnoVO());
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CursoVO</code> relacionado ao objeto
	 * <code>UnidadeEnsinoCursoVO</code>. Faz uso da chave primária da classe
	 * <code>CursoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosCurso(UnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		getFacadeFactory().getCursoFacade().carregarDados(obj.getCurso(), NivelMontarDados.BASICO, usuario);
	}

	public static void montarDadosPlanoFinanceiroCurso(UnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		if (obj.getPlanoFinanceiroCurso().getCodigo().intValue() == 0) {
//			obj.setPlanoFinanceiroCurso(new PlanoFinanceiroCursoVO());
//			return;
//		}
//		obj.setPlanoFinanceiroCurso(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(obj.getPlanoFinanceiroCurso().getCodigo(), "", nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>UnidadeEnsinoCursoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>UnidadeEnsinoCurso</code>
	 * .
	 * 
	 * @param <code>unidadeEnsino</code> campo chave para exclusão dos objetos
	 *        no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirUnidadeEnsinoCursos(Integer unidadeEnsino) throws Exception {
		UnidadeEnsinoCurso.excluir(getIdEntidade());
		String sql = "DELETE FROM UnidadeEnsinoCurso WHERE (unidadeEnsino = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { unidadeEnsino.intValue() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirUnidadeEnsinoCursos(Integer unidadeEnsino, List<UnidadeEnsinoCursoVO> objetos) throws Exception {
		String sql = "DELETE FROM UnidadeEnsinoCurso WHERE (unidadeEnsino = ?)";
		Iterator<UnidadeEnsinoCursoVO> i = objetos.iterator();
		while (i.hasNext()) {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) i.next();
			if (obj.getCodigo().intValue() != 0) {
				sql += " and codigo != " + obj.getCodigo().intValue();
			}
		}
		getConexao().getJdbcTemplate().update(sql, new Object[] { unidadeEnsino.intValue() });
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>UnidadeEnsinoCursoVO</code> contidos em um Hashtable no BD. Faz uso
	 * da operação <code>excluirUnidadeEnsinoCursos</code> e
	 * <code>incluirUnidadeEnsinoCursos</code> disponíveis na classe
	 * <code>UnidadeEnsinoCurso</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void alterarUnidadeEnsinoCursos(Integer unidadeEnsinoPrm, List<UnidadeEnsinoCursoVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator<UnidadeEnsinoCursoVO> e = objetos.iterator();
		while (e.hasNext()) {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) e.next();
			obj.setUnidadeEnsino(unidadeEnsinoPrm);
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuario);
			} else {
				alterar(obj);
			}
		}
		excluirUnidadeEnsinoCursos(unidadeEnsinoPrm, objetos);
		// incluirUnidadeEnsinoCursos( unidadeEnsino, objetos );
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCoordenadorTCC(final UnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().alterar("CoordenadorTCC", true, usuario);
			final String sql = "UPDATE UnidadeEnsinoCurso set coordenadorTCC=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					if (obj.getCoordenadorTCC().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(1, 0);
					} else {
						sqlAlterar.setInt(1, obj.getCoordenadorTCC().getCodigo().intValue());
					}
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>UnidadeEnsinoCursoVO</code> no BD. Garantindo o relacionamento com
	 * a entidade principal <code>administrativo.UnidadeEnsino</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void incluirUnidadeEnsinoCursos(Integer unidadeEnsinoPrm, List<UnidadeEnsinoCursoVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator<UnidadeEnsinoCursoVO> e = objetos.iterator();
		while (e.hasNext()) {
			UnidadeEnsinoCursoVO obj =  e.next();
			obj.setUnidadeEnsino(unidadeEnsinoPrm);
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuario);
			} else {
				alterar(obj);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>UnidadeEnsinoCursoVO</code> relacionados a um objeto da classe
	 * <code>administrativo.UnidadeEnsino</code>.
	 * 
	 * @param unidadeEnsino
	 *            Atributo de <code>administrativo.UnidadeEnsino</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>UnidadeEnsinoCursoVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>UnidadeEnsinoCursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List<UnidadeEnsinoCursoVO> consultarUnidadeEnsinoCursos(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UnidadeEnsinoCurso.consultar(getIdEntidade());
		List<UnidadeEnsinoCursoVO> objetos = new ArrayList<UnidadeEnsinoCursoVO>();
		String sql = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso " + "inner join unidadeEnsino on unidadeensino.codigo = UnidadeEnsinoCurso.unidadeensino " + "WHERE UnidadeEnsinoCurso.unidadeEnsino = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { unidadeEnsino.intValue() });
		UnidadeEnsinoCursoVO obj = null;
		while (resultado.next()) {
			objetos.add(montarDados(resultado, obj, nivelMontarDados, usuario));
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>UnidadeEnsinoCursoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UnidadeEnsinoCursoVO consultarPorChavePrimaria(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso " + "inner join unidadeEnsino on unidadeensino.codigo = UnidadeEnsinoCurso.unidadeensino " + "WHERE UnidadeEnsinoCurso.curso = ? and UnidadeEnsinoCurso.unidadeEnsino = ? and UnidadeEnsinoCurso.turno = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { cursoPrm.intValue(), unidadeEnsinoPrm.intValue(), turnoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		UnidadeEnsinoCursoVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	public UnidadeEnsinoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso " + "inner join unidadeEnsino on unidadeensino.codigo = UnidadeEnsinoCurso.unidadeensino " + "WHERE UnidadeEnsinoCurso.codigo = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigo.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		UnidadeEnsinoCursoVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	public UnidadeEnsinoCursoVO consultarPorCursoUnidadeEnsino(Integer curso, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT UnidadeEnsinoCurso.*, unidadeensino.nome as \"unidadeensino.nome\" FROM UnidadeEnsinoCurso " + "inner join unidadeEnsino on unidadeensino.codigo = UnidadeEnsinoCurso.unidadeensino " + "WHERE UnidadeEnsinoCurso.curso = ? and UnidadeEnsinoCurso.unidadeensino = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { curso.intValue(), unidadeEnsino.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		UnidadeEnsinoCursoVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return UnidadeEnsinoCurso.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		UnidadeEnsinoCurso.idEntidade = idEntidade;
	}

	public void carregarDados(UnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception {
		carregarDados(obj, NivelMontarDados.BASICO, usuario);
	}

	private StringBuffer getSQLPadraoConsultaTotalBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT count(unidadeEnsinoCurso.codigo) as qtde ");
		str.append(" FROM UnidadeEnsinoCurso  ");
		str.append("      LEFT JOIN Curso ON (UnidadeEnsinoCurso.curso = Curso.codigo) ");
		str.append("      LEFT JOIN Turno ON (UnidadeEnsinoCurso.turno = Turno.codigo) ");
		str.append("      LEFT JOIN unidadeensino ON (UnidadeEnsinoCurso.unidadeensino = unidadeensino.codigo) ");
		str.append("      LEFT JOIN PlanoFinanceiroCurso ON (UnidadeEnsinoCurso.planoFinanceiroCurso = PlanoFinanceiroCurso.codigo) ");
		return str;
	}
	
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT UnidadeEnsinoCurso.*, ");
		str.append("Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.periodicidade as \"Curso.periodicidade\", ");
		str.append("Turno.nome as \"Turno.nome\", Turno.codigo as \"Turno.codigo\", ");
		str.append("unidadeensino.nome as \"unidadeensino.nome\", ");
		str.append("PlanoFinanceiroCurso.descricao as \"PlanoFinanceiroCurso.descricao\", PlanoFinanceiroCurso.codigo as \"PlanoFinanceiroCurso.codigo\" ");
		str.append(" FROM UnidadeEnsinoCurso  ");
		str.append("      LEFT JOIN Curso ON (UnidadeEnsinoCurso.curso = Curso.codigo) ");
		str.append("      LEFT JOIN Turno ON (UnidadeEnsinoCurso.turno = Turno.codigo) ");
		str.append("      LEFT JOIN unidadeensino ON (UnidadeEnsinoCurso.unidadeensino = unidadeensino.codigo) ");
		str.append("      LEFT JOIN PlanoFinanceiroCurso ON (UnidadeEnsinoCurso.planoFinanceiroCurso = PlanoFinanceiroCurso.codigo) ");
		return str;
	}

	private void montarDadosBasico(UnidadeEnsinoCursoVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da UnidadeEnsinoCursoVO
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setUnidadeEnsino(dadosSQL.getInt("unidadeEnsino"));
		obj.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino.nome"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.setSituacaoCurso(dadosSQL.getString("situacaoCurso"));
		obj.setNrVagasPeriodoLetivo(dadosSQL.getInt("nrVagasPeriodoLetivo"));
		obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
//		obj.getPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("planoFinanceiroCurso"));
		obj.setMantenedora(dadosSQL.getString("mantenedora"));
		obj.setMantida(dadosSQL.getString("mantida"));
		obj.setValorMensalidade(dadosSQL.getDouble("valorMensalidade"));
		obj.setCodigoItemListaServico(dadosSQL.getString("codigoItemListaServico"));
		obj.setCodigoCursoUnidadeEnsinoGinfes(dadosSQL.getInt("codigoCursoUnidadeEnsinoGinfes"));
		obj.setCodigoInep(dadosSQL.getInt("codigoInep"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("Turno.nome"));
		obj.getTurno().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Plano Financeiro
//		obj.getPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("PlanoFinanceiroCurso.codigo"));
//		obj.getPlanoFinanceiroCurso().setDescricao(dadosSQL.getString("PlanoFinanceiroCurso.descricao"));
//		obj.getPlanoFinanceiroCurso().setNivelMontarDados(NivelMontarDados.BASICO);
	}

	public List<UnidadeEnsinoCursoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<UnidadeEnsinoCursoVO> vetResultado = new ArrayList<UnidadeEnsinoCursoVO>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoCursoVO obj = new UnidadeEnsinoCursoVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void carregarDados(UnidadeEnsinoCursoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico(obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico(obj, resultado);
		}
	}

	public UnidadeEnsinoCursoVO consultaRapidaPorCursoUnidadeTurno(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE 1=1 ");
		
		if (cursoPrm != 0) {
			sqlStr.append(" and (Curso.codigo = " + cursoPrm + ")");
		}
		if (turnoPrm != 0) {
			sqlStr.append("   AND (Turno.codigo = " + turnoPrm + ")");
		}
		if (unidadeEnsinoPrm != 0) {
			sqlStr.append("   AND (UnidadeEnsinoCurso.unidadeEnsino = " + unidadeEnsinoPrm + ")");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Unidade Ensino Curso).");
		}
		UnidadeEnsinoCursoVO obj = new UnidadeEnsinoCursoVO();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}
	
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorCursoUnidadeTurnoLista(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Curso.codigo = " + cursoPrm + ")");
		if (turnoPrm != 0) {
			sqlStr.append("   AND (Turno.codigo = " + turnoPrm + ")");
		}
		if (unidadeEnsinoPrm != 0) {
			sqlStr.append("   AND (UnidadeEnsinoCurso.unidadeEnsino = " + unidadeEnsinoPrm + ")");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorCursoUnidade(Integer cursoPrm, Integer unidadeEnsinoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Curso.codigo >= ");
		sqlStr.append(cursoPrm);
		sqlStr.append(")");
		if (unidadeEnsinoPrm != 0) {
			sqlStr.append("   AND (UnidadeEnsinoCurso.unidadeEnsino = ");
			sqlStr.append(unidadeEnsinoPrm);
			sqlStr.append(")");
		}
		sqlStr.append("ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);

	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (UnidadeEnsinoCurso.codigo = " + codigo + ")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where sem_acentos(curso.nome) ilike(sem_acentos(?)) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" and unidadeEnsinoCurso.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY curso.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadesEnsino, boolean controlarAcesso, String nivelEducacional, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where sem_acentos(curso.nome) ilike(sem_acentos('").append(valorConsulta).append("%')) ");
		if (Uteis.isAtributoPreenchido(unidadesEnsino)) {
			sqlStr.append(" AND unidadeEnsinoCurso.unidadeEnsino  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadesEnsino) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional) && !nivelEducacional.equals("TO")) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("'");
		}
		sqlStr.append(" ORDER BY curso.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadesEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where sem_acentos(curso.nome) ilike(sem_acentos('").append(valorConsulta).append("%')) ");
		if (Uteis.isAtributoPreenchido(unidadesEnsino)) {
			sqlStr.append(" AND unidadeEnsinoCurso.unidadeEnsino  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadesEnsino) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}
		sqlStr.append(" ORDER BY curso.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<UnidadeEnsinoCursoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<UnidadeEnsinoCursoVO> vetResultado = new ArrayList<UnidadeEnsinoCursoVO>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoCursoVO obj = new UnidadeEnsinoCursoVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void validarDadosFechamentoPeriodoLetivo(String periodicidade, String ano, String semestre,String nivelEducacionalApresentar) throws Exception {
		if (!Uteis.isAtributoPreenchido(nivelEducacionalApresentar)) {
			throw new Exception("O campo Nível Educacional (Fechamento Período Letivo) deve ser informado.");
		}
		if ((periodicidade.equals("AN") || periodicidade.equals("SE")) && !Uteis.isAtributoPreenchido(ano)) {
			throw new Exception("O campo ANO (Fechamento Período Letivo) deve ser informado.");
		}
		if (periodicidade.equals("SE") && !Uteis.isAtributoPreenchido(semestre)) {
			throw new Exception("O campo SEMESTRE (Fechamento Período Letivo) deve ser informado.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, UnidadeEnsinoCursoVO obj) {
		List<UnidadeEnsinoCursoVO> objs = new ArrayList<>();
		objs = consultaRapidaPorFiltros(obj, dataModelo);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
		dataModelo.setListaConsulta(objs);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<UnidadeEnsinoCursoVO> consultaRapidaPorFiltros(UnidadeEnsinoCursoVO obj, DataModelo dataModelo) {
		try {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = new StringBuilder(getSQLPadraoConsultaBasica().toString());
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY Curso.nome ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaRapida(tabelaResultado);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Integer consultarTotalPorFiltros(UnidadeEnsinoCursoVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = new StringBuilder(getSQLPadraoConsultaTotalBasica());
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(UnidadeEnsinoCursoVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
			sqlStr.append(" and unidadeensino.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getUnidadeEnsino());
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sqlStr.append(" and unidadeEnsinoCurso.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getCurso().getNome())) {
			sqlStr.append(" and lower(curso.nome) like(?) ");
			dataModelo.getListaFiltros().add(PERCENT + obj.getCurso().getNome().toLowerCase() + PERCENT);	
		}
		if (Uteis.isAtributoPreenchido(obj.getTurno().getNome())) {
			sqlStr.append(" and lower(turno.nome) like(?)");
			dataModelo.getListaFiltros().add(PERCENT + obj.getTurno().getNome().toLowerCase() + PERCENT);	
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigoCursoUnidadeEnsinoGinfes())) {
			sqlStr.append(" and unidadeEnsinoCurso.codigoCursoUnidadeEnsinoGinfes = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigoCursoUnidadeEnsinoGinfes());	
		}
		if (Uteis.isAtributoPreenchido(obj.getValorMensalidade())) {
			sqlStr.append(" and unidadeEnsinoCurso.valorMensalidade = ? ");
			dataModelo.getListaFiltros().add(obj.getValorMensalidade());	
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigoItemListaServico())) {
			sqlStr.append(" and unidadeEnsinoCurso.codigoItemListaServico like(?) ");
			dataModelo.getListaFiltros().add(PERCENT + obj.getCodigoItemListaServico() + PERCENT);
		}
		
	}

	@Override
	public List<UnidadeEnsinoCursoVO> consultar(String campoConsultaCurso, String valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelmontardadosDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoCursoVO> objs = new ArrayList<>(0);
		if (campoConsultaCurso.equals("curso")) {
			objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(valorConsultaCurso, unidadeEnsinoCodigo, false, "", controlarAcesso, nivelmontardadosDados, usuario);
		}
		if (campoConsultaCurso.equals("turno")) {
			objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeTurnoUnidadeEnsino(valorConsultaCurso, unidadeEnsinoCodigo, controlarAcesso, nivelmontardadosDados, usuario);
		}
		return objs;
	}

	public List<UnidadeEnsinoCursoVO> consultarPorProfessor(String nome, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct uec.curso, uec.turno from unidadeensinocurso uec ");
		sqlStr.append("inner join curso c on c.codigo = uec.curso ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = uec.unidadeensino ");
		sqlStr.append("inner join turma t on t.curso = c.codigo ");
		sqlStr.append("inner join horarioturmaprofessordisciplina htpd on htpd.turma = t.codigo ");
		sqlStr.append("where sem_acentos(c.nome) ilike sem_acentos(?) and htpd.professor = ").append(professor).append(" ");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append("and uec.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nome + PERCENT);

		List<UnidadeEnsinoCursoVO> vetResultado = new ArrayList<UnidadeEnsinoCursoVO>(0);
		UnidadeEnsinoCursoVO obj = null;
		while (tabelaResultado.next()) {
			obj = new UnidadeEnsinoCursoVO();
			obj.getCurso().setCodigo(tabelaResultado.getInt("curso"));
			obj.getTurno().setCodigo(tabelaResultado.getInt("turno"));
			montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			vetResultado.add(obj);
		}
		return (vetResultado);
	}

	public List<UnidadeEnsinoCursoVO> consultarPorProcessoMatricula(String nomeCurso, Integer processomatricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder();
		try {
			sqlStr.append("select uec.*, unidadeensino.nome as \"unidadeensino.nome\" from unidadeensinocurso uec ");
			sqlStr.append("inner join curso c on uec.curso = c.codigo ");
			sqlStr.append("inner join unidadeensino on uec.unidadeensino = unidadeensino.codigo ");
			sqlStr.append("inner join periodoletivoativounidadeensinocurso plauec on plauec.unidadeensinocurso = uec.codigo ");
			sqlStr.append("inner join processomatriculacalendario pmc on pmc.periodoletivoativounidadeensinocurso = plauec.codigo ");
			sqlStr.append("where pmc.processomatricula = ").append(processomatricula).append(" and ");
			sqlStr.append("c.nome ilike '").append(nomeCurso).append("%' order by c.nome");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	public List<UnidadeEnsinoCursoVO> consultaRapidaPorUnidadeEnsino(Integer unidadeEnsinoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select unidadeEnsinocurso.codigo AS \"unidadeEnsinocurso.codigo\", turno.nome AS \"turno.nome\", turno.codigo AS \"turno.codigo\", ");
		sqlStr.append("curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", unidadeensino.codigo AS \"unidadeensino.codigo\", ");
		sqlStr.append("unidadeensino.nome as \"unidadeensino.nome\" from unidadeEnsinocurso ");
		sqlStr.append(" inner join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sqlStr.append(" inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sqlStr.append(" inner join unidadeensino on unidadeEnsinocurso.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" WHERE (UnidadeEnsinoCurso.unidadeEnsino = ");
		sqlStr.append(unidadeEnsinoPrm);
		sqlStr.append(")");
		sqlStr.append("ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaMinimosRel(tabelaResultado, nivelMontarDados, usuario);

	}

	@Override
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorUnidadeEnsinoENivelEducacional(Integer unidadeEnsinoPrm, String nivelEducacional, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select unidadeEnsinocurso.codigo AS \"unidadeEnsinocurso.codigo\", turno.nome AS \"turno.nome\", turno.codigo AS \"turno.codigo\", ");
		sqlStr.append("curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", unidadeensino.codigo AS \"unidadeensino.codigo\", ");
		sqlStr.append("unidadeensino.nome as \"unidadeensino.nome\" from unidadeEnsinocurso ");
		sqlStr.append(" inner join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sqlStr.append(" inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sqlStr.append(" inner join unidadeensino on unidadeEnsinocurso.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" WHERE (UnidadeEnsinoCurso.unidadeEnsino = ");
		sqlStr.append(unidadeEnsinoPrm);
		sqlStr.append(")");
		if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sqlStr.append(" and nivelEducacional in ('").append(nivelEducacional).append("') ");
		}
		sqlStr.append(" and curso.apresentarHomePreInscricao = true ");
		sqlStr.append("ORDER BY curso.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaMinimosRel(tabelaResultado, nivelMontarDados, usuario);

	}

	@Override
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorProcSeletivo(Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select unidadeEnsinocurso.codigo AS \"unidadeEnsinocurso.codigo\", turno.nome AS \"turno.nome\", turno.codigo AS \"turno.codigo\", curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", ");
		sqlStr.append("unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\" from unidadeEnsinocurso ");
		sqlStr.append(" inner join procseletivocurso on procseletivocurso.unidadeEnsinocurso = unidadeEnsinocurso.codigo ");
		sqlStr.append(" inner join procseletivounidadeensino on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino ");
		sqlStr.append(" inner join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sqlStr.append(" inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sqlStr.append(" where procseletivounidadeensino.procseletivo = ");
		sqlStr.append(procSeletivo);
		sqlStr.append(" order by curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaMinimosRel(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorCursoProcSeletivo(String nomeCurso, Integer procSeletivo, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct unidadeEnsinocurso.codigo AS \"unidadeEnsinocurso.codigo\", turno.nome AS \"turno.nome\", turno.codigo AS \"turno.codigo\", curso.nome AS \"curso.nome\", curso.codigo AS \"curso.codigo\", ");
		sqlStr.append(" unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\" from unidadeEnsinocurso ");
		sqlStr.append(" inner join procseletivocurso on procseletivocurso.unidadeEnsinocurso = unidadeEnsinocurso.codigo ");
		sqlStr.append(" inner join procseletivounidadeensino on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino ");
		sqlStr.append(" inner join procseletivo on procseletivo.codigo = procseletivounidadeensino.procseletivo ");
		sqlStr.append(" inner join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sqlStr.append(" inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sqlStr.append(" where 1=1 ");
		if (procSeletivo != 0) {
			sqlStr.append(" and procseletivounidadeensino.procseletivo = ");
			sqlStr.append(procSeletivo);
		}
		if (!ano.equals("")) {
			sqlStr.append(" and procseletivo.ano = '");
			sqlStr.append(ano);
			sqlStr.append("'");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and procseletivo.semestre = '");
			sqlStr.append(semestre);
			sqlStr.append("'");
		}
		sqlStr.append(" and sem_acentos(upper(curso.nome)) like (sem_acentos(upper('").append(nomeCurso).append("%')))");
		sqlStr.append(" order by curso.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaMinimosRel(tabelaResultado, nivelMontarDados, usuario));
	}


	public static List<UnidadeEnsinoCursoVO> montarDadosConsultaMinimosRel(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoCursoVO> vetResultado = new ArrayList<UnidadeEnsinoCursoVO>(0);
		UnidadeEnsinoCursoVO obj = null;
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosMinimosRel(tabelaResultado, obj, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static UnidadeEnsinoCursoVO montarDadosMinimosRel(SqlRowSet dadosSQL, UnidadeEnsinoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) {
		obj = new UnidadeEnsinoCursoVO();
		obj.setCodigo(dadosSQL.getInt("unidadeEnsinocurso.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("turno.nome"));
		obj.getTurno().setCodigo(dadosSQL.getInt("turno.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.setUnidadeEnsino(dadosSQL.getInt("unidadeensino.codigo"));
		obj.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino.nome"));
		return obj;
	}

	@Override
	public List<UnidadeEnsinoCursoVO> consultarCursoPorCodigoUnidadeEnsino(Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controleAcesso, usuario);
		String sqlStr = "SELECT distinct curso.nome FROM unidadeensinocurso, curso, unidadeensino " + "where unidadeensinocurso.unidadeensino = unidadeensino.codigo and unidadeensinocurso.curso = curso.codigo " + "and curso.codigo=" + codigoCurso;
		if (unidadeEnsinoCodigo != 0) {
			sqlStr += "and unidadeensinocurso.unidadeensino = " + unidadeEnsinoCodigo.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public Boolean validarUnidadeEnsinoCursoExistenteMatriculaPeriodo(UnidadeEnsinoCursoVO obj, boolean isAlterando) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select count(matriculaperiodo.codigo) as qtd from matricula ");  
			sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			sql.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = matriculaperiodo.unidadeensinocurso ");
			sql.append(" where 1=1 ");			
			sql.append(" AND  unidadeensinocurso.codigo = ").append(obj.getCodigo());
			if(isAlterando){
				sql.append(" AND matricula.turno != ").append(obj.getTurno().getCodigo());	
			}else{
				sql.append(" AND matricula.turno = ").append(obj.getTurno().getCodigo());
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private Boolean validarIncluirUnidadeEnsinoCursoCadastroCurso(UnidadeEnsinoCursoVO obj) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT * FROM unidadeensinocurso ");
			sql.append(" where turno = ").append(obj.getTurno().getCodigo());
			sql.append(" AND curso = ").append(obj.getCurso().getCodigo());
			sql.append(" AND  unidadeensino = ").append(obj.getUnidadeEnsino());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return Boolean.FALSE;
			} else {
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarUnidadeEnsinoCursoCadastroCurso(List<UnidadeEnsinoVO> listaUnidadeEnsino, CursoVO curso, UsuarioVO usuarioLogado) throws Exception {
		try {
			List<UnidadeEnsinoCursoVO> listaIncluir = new ArrayList<>();
			if (curso.getCursoTurnoVOs().isEmpty()) {
				throw new Exception("Por favor adicione ao menos um(1) TURNO para adicionar uma unidade.");
			}
			for (CursoTurnoVO turno : curso.getCursoTurnoVOs()) {
				for (UnidadeEnsinoVO unidade : listaUnidadeEnsino) {
					if (unidade.getSelecionarAdicionarCursoInstituicao()) {
						UnidadeEnsinoCursoVO obj = new UnidadeEnsinoCursoVO();
						obj.setUnidadeEnsino(unidade.getCodigo());
						obj.setCurso(curso);
						obj.setTurno(turno.getTurno());
						obj.setSituacaoCurso("AT");
						listaIncluir.add(obj);
					}
				}
			}
			for (UnidadeEnsinoCursoVO obj : listaIncluir) {
				if (validarIncluirUnidadeEnsinoCursoCadastroCurso(obj)) {
					incluir(obj, usuarioLogado);
					validarCentroResultadoExistentePorUnidadeEnsinoCurso(obj, usuarioLogado);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void validarCentroResultadoExistentePorUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, UsuarioVO usuarioVO) {
		try {			
			boolean existeCentroResultado = getFacadeFactory().getUnidadeEnsinoCursoCentroResultadoFacade().consultarSeExisteUnidadeEnsinoCursoCentroResultadoPorCursoPorUnidadeEnsino(unidadeEnsinoCursoVO.getCurso().getCodigo(), unidadeEnsinoCursoVO.getUnidadeEnsino());
			if(!existeCentroResultado){
//				CentroResultadoVO obj = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico(unidadeEnsinoCursoVO.getCurso().getNome(), unidadeEnsinoCursoVO.getUnidadeEnsino(), TipoNivelEducacional.getEnum(unidadeEnsinoCursoVO.getCurso().getNivelEducacional()), null, true, usuarioVO);
				UnidadeEnsinoCursoCentroResultadoVO uecr = new UnidadeEnsinoCursoCentroResultadoVO();
//				uecr.setCentroResultadoVO(obj);
				uecr.setCursoVO(unidadeEnsinoCursoVO.getCurso());
				uecr.getUnidadeEnsinoVO().setCodigo(unidadeEnsinoCursoVO.getUnidadeEnsino());
				getFacadeFactory().getUnidadeEnsinoCursoCentroResultadoFacade().persistir(uecr, false, usuarioVO);	
			}	
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public List<UnidadeEnsinoCursoVO> consultaRapidaPorNomeCursoUnidadeEnsinoPeriodicidade(String valorConsulta, Integer unidadeEnsino, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where sem_acentos(curso.nome) ilike(sem_acentos('").append(valorConsulta).append("%')) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" and unidadeEnsinoCurso.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" and curso.periodicidade = '").append(periodicidade).append("'");
		sqlStr.append(" ORDER BY curso.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	@Override 
	public List<UnidadeEnsinoCursoVO> consultarPorCursoAgrupandoPorUnidadeEnsino(Integer codigoCurso) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select unidadeensino.codigo, unidadeensino.nome, unidadeensinocurso.codigoinep from unidadeensinocurso");
		sql.append(" inner join unidadeensino on unidadeensinocurso.unidadeensino = unidadeensino.codigo");
		sql.append(" where curso = ? group by unidadeensino.codigo, unidadeensino.nome, unidadeensinocurso.codigoinep ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCurso);
		List<UnidadeEnsinoCursoVO> lista = new ArrayList<>();
		while(tabelaResultado.next()) {
			UnidadeEnsinoCursoVO obj = new UnidadeEnsinoCursoVO();
			obj.setUnidadeEnsino(tabelaResultado.getInt("codigo"));
			obj.setNomeUnidadeEnsino(tabelaResultado.getString("nome"));
			obj.setCodigoInep(tabelaResultado.getInt("codigoinep"));

			lista.add(obj);
		} 
		return lista;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarUnidadeEnsinoCurso(List<UnidadeEnsinoCursoVO> listaUnidadeEnsinoCursos, CursoVO curso) throws Exception {
		for (UnidadeEnsinoCursoVO unidadeEnsinoCursoVO : listaUnidadeEnsinoCursos) {

			final String sql = "UPDATE UnidadeEnsinoCurso set codigoInep= ? WHERE unidadeensino = ? and curso=?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, unidadeEnsinoCursoVO.getCodigoInep());
					sqlAlterar.setInt(2, unidadeEnsinoCursoVO.getUnidadeEnsino());
					sqlAlterar.setInt(3, curso.getCodigo());
					return sqlAlterar;
				}
			});
		}
	}
	
	
	@Override
	public Boolean verificarUnidadeEnsinoCursoExistente(Integer unidadeEnsinoCodigo,Integer turnoCodigo,Integer cursoCodigo) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT COUNT(*) AS qtd FROM unidadeensinocurso  ");  		
			sql.append(" WHERE  unidadeensinocurso.curso = ").append(cursoCodigo);
			sql.append(" AND  unidadeensinocurso.unidadeensino = ").append(unidadeEnsinoCodigo);
			sql.append(" AND  unidadeensinocurso.turno = ").append(turnoCodigo);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public UnidadeEnsinoCursoVO consultarPorCursoUnidadeTurno(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Curso.codigo = " + cursoPrm + ")");
		if (turnoPrm != 0) {
			sqlStr.append("   AND (Turno.codigo = " + turnoPrm + ")");
		}
		if (unidadeEnsinoPrm != 0) {
			sqlStr.append("   AND (UnidadeEnsinoCurso.unidadeEnsino = " + unidadeEnsinoPrm + ")");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new UnidadeEnsinoCursoVO();
		}
		UnidadeEnsinoCursoVO obj = new UnidadeEnsinoCursoVO();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}
	
	
	
	
	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursosUnidadeEnsino(List<CursoVO> cursos, Integer unidadeEnsino, String situacaoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT unidadeensinocurso.*, unidadeensino.nome as \"unidadeensino.nome\" ");
		sqlStr.append("FROM unidadeensinocurso ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sqlStr.append("WHERE unidadeensinocurso.curso in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(cursos)).append(" )");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensinocurso.unidadeensino = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(situacaoCurso)) {
			sqlStr.append(" and unidadeensinocurso.situacaocurso = '").append(situacaoCurso).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoCursoVO> consultarPorCodigoCursoListaUnidadeEnsinoNivelEducacional(Integer valorConsulta,List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct unidadeensinocurso.curso, curso.nome as \"curso.nome\",curso.periodicidade as \"curso.periodicidade\" ,unidadeensinocurso.turno, turno.nome as \"turno.nome\" FROM unidadeensinocurso ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = unidadeensinocurso.curso");
		sqlStr.append(" INNER JOIN turno ON turno.codigo = unidadeensinocurso.turno");
		sqlStr.append(" WHERE unidadeensinocurso.curso >= ").append(valorConsulta);
		if (Uteis.isAtributoPreenchido(nivelEducacional) && !nivelEducacional.equals("TO")) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("'");
		}
		sqlStr.append(" order by unidadeensinocurso.curso ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs =  new ArrayList<UnidadeEnsinoCursoVO>(0);
		while(tabelaResultado.next()) {
			UnidadeEnsinoCursoVO unidadeEnsinoCursoVO =  new UnidadeEnsinoCursoVO();
			unidadeEnsinoCursoVO.getCurso().setCodigo(tabelaResultado.getInt("curso"));
			unidadeEnsinoCursoVO.getCurso().setNome(tabelaResultado.getString("curso.nome"));
			unidadeEnsinoCursoVO.getCurso().setPeriodicidade(tabelaResultado.getString("curso.periodicidade"));
			unidadeEnsinoCursoVO.getTurno().setCodigo(tabelaResultado.getInt("turno"));
			unidadeEnsinoCursoVO.getTurno().setNome(tabelaResultado.getString("turno.nome"));
			unidadeEnsinoCursoVOs.add(unidadeEnsinoCursoVO);
		}
		return unidadeEnsinoCursoVOs;
	}
	
	public List<UnidadeEnsinoCursoVO> consultarPorNomeCursoListaUnidadeEnsinoNivelEducacional(String valorConsulta,List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), verificarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct unidadeensinocurso.curso, unidadeensinocurso.unidadeEnsino ,curso.nome as \"curso.nome\",curso.periodicidade as \"curso.periodicidade\" ,unidadeensinocurso.turno, turno.nome as \"turno.nome\" FROM unidadeensinocurso ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = unidadeensinocurso.curso");
		sqlStr.append(" INNER JOIN turno ON turno.codigo = unidadeensinocurso.turno");
		sqlStr.append(" WHERE (curso.nome) ilike (?) ");
		if (Uteis.isAtributoPreenchido(nivelEducacional) && !nivelEducacional.equals("TO")) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("'");
		}
		sqlStr.append(" order by curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString() , valorConsulta + PERCENT);
		List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs =  new ArrayList<UnidadeEnsinoCursoVO>(0);
		while(tabelaResultado.next()) {
			UnidadeEnsinoCursoVO unidadeEnsinoCursoVO =  new UnidadeEnsinoCursoVO();
			unidadeEnsinoCursoVO.getCurso().setCodigo(tabelaResultado.getInt("curso"));
			unidadeEnsinoCursoVO.getCurso().setNome(tabelaResultado.getString("curso.nome"));
			unidadeEnsinoCursoVO.getCurso().setPeriodicidade(tabelaResultado.getString("curso.periodicidade"));
			unidadeEnsinoCursoVO.getTurno().setCodigo(tabelaResultado.getInt("turno"));
			unidadeEnsinoCursoVO.getTurno().setNome(tabelaResultado.getString("turno.nome"));
			unidadeEnsinoCursoVOs.add(unidadeEnsinoCursoVO);
		}
		return unidadeEnsinoCursoVOs;	}
}