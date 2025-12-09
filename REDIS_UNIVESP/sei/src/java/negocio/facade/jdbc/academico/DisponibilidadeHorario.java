package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisponibilidadeHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.interfaces.academico.DisponibilidadeHorarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DisponibilidadeHorarioVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>DisponibilidadeHorarioVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see DisponibilidadeHorarioVO
 * @see ControleAcesso
 * @see Pessoa
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class DisponibilidadeHorario extends ControleAcesso implements DisponibilidadeHorarioInterfaceFacade {

	protected static String idEntidade;

	public DisponibilidadeHorario() throws Exception {
		super();
		setIdEntidade("DisponibilidadeHorario");
	}

	public DisponibilidadeHorarioVO novo() throws Exception {
		DisponibilidadeHorario.incluir(getIdEntidade());
		DisponibilidadeHorarioVO obj = new DisponibilidadeHorarioVO();
		return obj;
	}

	public void incluir(DisponibilidadeHorarioVO obj, UsuarioVO usuario) throws Exception {
		// DisponibilidadeHorarioVO.validarDados(obj);
		// DisponibilidadeHorario.incluir(getIdEntidade());
		// String sql =
		// "INSERT INTO DisponibilidadeHorario( professor, diaSemana, aulaInicio, aulaFim, turno, nrAula, horaInicio, horaFim ) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";
		// PreparedStatement sqlInserir = con.prepareStatement(sql);
		// sqlInserir.setInt( 1, obj.getProfessor().intValue() );
		// sqlInserir.setString( 2, obj.getDiaSemana() );
		// sqlInserir.setInt( 3, obj.getAulaInicio().intValue() );
		// sqlInserir.setInt( 4, obj.getAulaFim().intValue() );
		// sqlInserir.setInt( 5, obj.getTurno().getCodigo().intValue() );
		// sqlInserir.setInt(6, obj.getNrAula().intValue());
		// sqlInserir.setString(7, obj.getHoraInicio());
		// sqlInserir.setString(8, obj.getHoraFim());
		// sqlInserir.execute();
		// obj.setCodigo(obterValorChavePrimariaCodigo());
		// obj.setNovoObj(Boolean.FALSE);
	}

	public void alterar(DisponibilidadeHorarioVO obj, UsuarioVO usuario) throws Exception {
		// DisponibilidadeHorarioVO.validarDados(obj);
		// DisponibilidadeHorario.alterar(getIdEntidade());
		// String sql =
		// "UPDATE DisponibilidadeHorario set professor=?, diaSemana=?, aulaInicio=?, aulaFim=?, turno=?, nrAula=?, horaInicio = ?, horaFim =?  WHERE ((codigo = ?))";
		// PreparedStatement sqlAlterar = con.prepareStatement(sql);
		// sqlAlterar.setInt( 1, obj.getProfessor().intValue() );
		// sqlAlterar.setString( 2, obj.getDiaSemana() );
		// sqlAlterar.setInt( 3, obj.getAulaInicio().intValue() );
		// sqlAlterar.setInt( 4, obj.getAulaFim().intValue() );
		// sqlAlterar.setInt( 5, obj.getTurno().getCodigo().intValue() );
		// sqlAlterar.setInt(6, obj.getNrAula().intValue());
		// sqlAlterar.setString(7, obj.getHoraInicio());
		// sqlAlterar.setString(8, obj.getHoraFim());
		// sqlAlterar.setInt( 9, obj.getCodigo().intValue() );
		// sqlAlterar.execute();
	}

	public void excluir(DisponibilidadeHorarioVO obj, UsuarioVO usuario) throws Exception {
		// DisponibilidadeHorario.excluir(getIdEntidade());
		// String sql = "DELETE FROM DisponibilidadeHorario WHERE ((codigo = ?))";
		// PreparedStatement sqlExcluir = con.prepareStatement(sql);
		// sqlExcluir.setInt( 1, obj.getCodigo().intValue() );
		// sqlExcluir.execute();
	}

	public List consultarPorNomeTurno(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DisponibilidadeHorario.* FROM DisponibilidadeHorario, Turno WHERE DisponibilidadeHorario.turno = Turno.codigo and Turno.nome like('" + valorConsulta
				+ "%') ORDER BY Turno.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public List consultarDisponibilidadeHorarioProfessorNoHorarioProgramadoAula(Integer professor, String diaSemana, Integer turno, Integer nrAula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from disponibilidadeHorario where professor = ? and diasemana = ? and turno = ? and nrAula = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { professor, diaSemana, turno, nrAula });
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public List consultarDisponibilidadeHorarioProfessorTurno(Integer professor, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from disponibilidadeHorario where professor = ? and turno = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { professor, turno });
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public List consultarPorAulaFim(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisponibilidadeHorario WHERE aulaFim >= '" + valorConsulta.intValue() + "' ORDER BY aulaFim";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List consultarPorAulaInicio(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisponibilidadeHorario WHERE aulaInicio >= '" + valorConsulta.intValue() + "' ORDER BY aulaInicio";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List consultarPorDiaSemana(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisponibilidadeHorario WHERE diaSemana like('" + valorConsulta + "%') ORDER BY diaSemana";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DisponibilidadeHorario.* FROM DisponibilidadeHorario, Pessoa WHERE DisponibilidadeHorario.professor = Pessoa.codigo and Pessoa.nome like('" + valorConsulta
				+ "%') ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisponibilidadeHorario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>DisponibilidadeHorarioVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>DisponibilidadeHorarioVO</code>.
	 * 
	 * @return O objeto da classe <code>DisponibilidadeHorarioVO</code> com os dados devidamente montados.
	 */
	public static DisponibilidadeHorarioVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		DisponibilidadeHorarioVO obj = new DisponibilidadeHorarioVO();
		// obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
		// obj.setProfessor( new Integer( dadosSQL.getInt("professor")));
		// obj.setDiaSemana( dadosSQL.getString("diaSemana"));
		// obj.setAulaInicio(new Integer(dadosSQL.getInt("aulaInicio")));
		// obj.setAulaFim( new Integer(dadosSQL.getString("aulaFim")));
		// obj.setNrAula( dadosSQL.getInt("nrAula"));
		// obj.setHoraFim(dadosSQL.getString("horaFim"));
		// obj.setHoraInicio(dadosSQL.getString("horaInicio"));
		// obj.setIntervaloHora(obj.getHoraInicio() + " até " + obj.getHoraFim());
		// obj.getTurno().setCodigo( new Integer( dadosSQL.getInt("turno")));
		// obj.setAula(String.valueOf(obj.getNrAula().intValue())+"ª Aula");
		obj.setNovoObj(Boolean.FALSE);
		montarDadosTurno(obj, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>TurnoVO</code> relacionado ao objeto
	 * <code>DisponibilidadeHorarioVO</code>. Faz uso da chave primária da classe <code>TurnoVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTurno(DisponibilidadeHorarioVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			obj.setTurno(new TurnoVO());
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDisponibilidadeHorarios(Integer professor, UsuarioVO usuario) throws Exception {
		DisponibilidadeHorario.excluir(getIdEntidade());
		String sql = "DELETE FROM DisponibilidadeHorario WHERE (professor = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { professor });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisponibilidadeHorarios(Integer professor, List objetos, UsuarioVO usuario) throws Exception {
		excluirDisponibilidadeHorarios(professor, usuario);
		incluirDisponibilidadeHorarios(professor, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisponibilidadeHorarios(Integer professorPrm, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			DisponibilidadeHorarioVO obj = (DisponibilidadeHorarioVO) e.next();
			obj.setProfessor(professorPrm);
			incluir(obj, usuario);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>DisponibilidadeHorarioVO</code> relacionados a um objeto da
	 * classe <code>basico.Pessoa</code>.
	 * 
	 * @param professor
	 *            Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe
	 *            <code>DisponibilidadeHorarioVO</code>.
	 * @return List Contendo todos os objetos da classe <code>DisponibilidadeHorarioVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public static List consultarDisponibilidadeHorarios(Integer professor, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM DisponibilidadeHorario WHERE professor = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { professor });
		while (resultado.next()) {
			objetos.add(DisponibilidadeHorario.montarDados(resultado, usuario));
		}
		return objetos;
	}

	public DisponibilidadeHorarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM DisponibilidadeHorario WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return DisponibilidadeHorario.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		DisponibilidadeHorario.idEntidade = idEntidade;
	}
}