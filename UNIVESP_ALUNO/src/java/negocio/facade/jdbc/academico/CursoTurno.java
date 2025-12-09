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

import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.ProcessoMatriculaUnidadeEnsinoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CursoTurnoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CursoTurnoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CursoTurnoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CursoTurnoVO
 * @see ControleAcesso
 * @see Curso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class CursoTurno extends ControleAcesso implements CursoTurnoInterfaceFacade {

	protected static String idEntidade;

	public CursoTurno() throws Exception {
		super();
		setIdEntidade("Curso");
	}

	public CursoTurnoVO novo() throws Exception {
		CursoTurno.incluir(getIdEntidade());
		CursoTurnoVO obj = new CursoTurnoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CursoTurnoVO obj, UsuarioVO usuario) throws Exception {
		CursoTurnoVO.validarDados(obj);
		final String sql = "INSERT INTO CursoTurno( curso, turno, nomeTurnoCenso ) VALUES ( ?, ?, ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getCurso());
                sqlInserir.setInt(2, obj.getTurno().getCodigo());
                if (obj.getNomeTurnoCenso() != null) {
                	sqlInserir.setString(3, obj.getNomeTurnoCenso().name());
                } else {
                	sqlInserir.setNull(3, 0);
                }
                return sqlInserir;
            }
        });
        obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CursoTurnoVO obj, UsuarioVO usuario) throws Exception {
		CursoTurnoVO.validarDados(obj);
		final String sql = "UPDATE CursoTurno set  WHERE ((curso = ?) and (turno = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getCurso().intValue());
				sqlAlterar.setInt(2, obj.getTurno().getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CursoTurnoVO obj, UsuarioVO usuario) throws Exception {
		CursoTurno.excluir(getIdEntidade());
		String sql = "DELETE FROM CursoTurno WHERE ((curso = ?) and (turno = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCurso(), obj.getTurno() });
	}

	public List consultarPorNomeTurno(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controleAcesso, usuario);
		String sqlStr = "SELECT CursoTurno.* FROM CursoTurno, Turno WHERE CursoTurno.turno = Turno.codigo and Turno.nome like('" + valorConsulta + "%') ORDER BY Turno.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public List consultarPorNomeCurso(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controleAcesso, usuario);
		String sqlStr = "SELECT CursoTurno.* FROM CursoTurno, Curso WHERE CursoTurno.curso = Curso.codigo and Curso.nome like('" + valorConsulta + "%') ORDER BY Curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>CursoTurnoVO</code> resultantes da consulta.
	 */
	public  List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>CursoTurnoVO</code>.
	 * 
	 * @return O objeto da classe <code>CursoTurnoVO</code> com os dados devidamente montados.
	 */
	public  CursoTurnoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		CursoTurnoVO obj = new CursoTurnoVO();
		obj.setCurso(new Integer(dadosSQL.getInt("curso")));
		obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
		if (dadosSQL.getString("nomeTurnoCenso") != null) {
			obj.setNomeTurnoCenso(NomeTurnoCensoEnum.valueOf(dadosSQL.getString("nomeTurnoCenso")));
		}
		obj.setNovoObj(Boolean.FALSE);
		montarDadosTurno(obj, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>TurnoVO</code> relacionado ao objeto
	 * <code>CursoTurnoVO</code>. Faz uso da chave primária da classe <code>TurnoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosTurno(CursoTurnoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			obj.setTurno(new TurnoVO());
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCursoTurnos(Integer curso, UsuarioVO usuario) throws Exception {
		CursoTurno.excluir(getIdEntidade());
		String sql = "DELETE FROM CursoTurno WHERE (curso = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { curso });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCursoTurnos(Integer curso, List objetos, UsuarioVO usuario) throws Exception {
		excluirCursoTurnos(curso, usuario);
		incluirCursoTurnos(curso, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCursoTurnos(Integer cursoPrm, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			CursoTurnoVO obj = (CursoTurnoVO) e.next();
			obj.setCurso(cursoPrm);
			incluir(obj, usuario);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>CursoTurnoVO</code> relacionados a um objeto da classe
	 * <code>academico.Curso</code>.
	 * 
	 * @param curso
	 *            Atributo de <code>academico.Curso</code> a ser utilizado para localizar os objetos da classe
	 *            <code>CursoTurnoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>CursoTurnoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List consultarCursoTurnos(Integer curso, boolean controleAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controleAcesso, usuario);
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM CursoTurno WHERE curso = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { curso });
		while (resultado.next()) {
			objetos.add(montarDados(resultado, usuario));
		}
		return objetos;
	}

	public CursoTurnoVO consultarPorChavePrimaria(Integer cursoPrm, Integer turnoPrm, boolean controleAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controleAcesso, usuario);
		String sql = "SELECT * FROM CursoTurno WHERE curso = ? and turno = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { cursoPrm, turnoPrm });
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
		return CursoTurno.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		CursoTurno.idEntidade = idEntidade;
	}
	
	@Override
	public List<CursoTurnoVO> consultarCursoTurnoProcessoMatricula(String campoConsulta, String valorConsulta, String nivelEducacional, List<ProcessoMatriculaUnidadeEnsinoVO> processoMatriculaUnidadeEnsinoVOs, Integer unidadeEnsinoLogado) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\", curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.periodicidade as \"curso.periodicidade\" , curso.nivelEducacional as \"curso.nivelEducacional\" ");
		sql.append(" from cursoturno ");
		sql.append(" inner join curso on curso.codigo = cursoturno.curso ");
		sql.append(" inner join turno on turno.codigo = cursoturno.turno ");
		sql.append(" where 1 = 1 ");
		if(Uteis.isAtributoPreenchido(processoMatriculaUnidadeEnsinoVOs) && processoMatriculaUnidadeEnsinoVOs.stream().anyMatch(t -> t.getSelecionado()) || Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sql.append(" and exists (select unidadeensinocurso.codigo from unidadeensinocurso where unidadeensinocurso.curso =curso.codigo and unidadeensinocurso.turno = turno.codigo ");
			sql.append(" and unidadeensinocurso.unidadeensino in ( ");
			if(Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
				sql.append(unidadeEnsinoLogado);
			}else {
				int x = 0;
			for(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO: processoMatriculaUnidadeEnsinoVOs) {
				if(processoMatriculaUnidadeEnsinoVO.getSelecionado()) {
					if(x > 0) {
						sql.append(", ");
					}
					sql.append(processoMatriculaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
					x++;
				}
			}
			}
			sql.append(") limit  1) ");
			 
		}
		if(campoConsulta.equals("codigo")) {
			if(!Uteis.getIsValorNumerico(valorConsulta)) {
				throw new Exception("Informar apenas valor numérico.");
			}
			if (valorConsulta.equals("") ) {
				valorConsulta = "0";
			}			
			sql.append(" and  curso.codigo::varchar >= ?");
		}
		if(campoConsulta.equals("nome")) {
			if(!valorConsulta.contains("%")) {
				valorConsulta = "%"+valorConsulta+"%";
			}
			sql.append(" and  sem_acentos(curso.nome) ilike sem_acentos(?)");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sql.append(" and  curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
		List<CursoTurnoVO> cursoTurnoVOs =  new ArrayList<CursoTurnoVO>();
		while(rs.next()) {
			CursoTurnoVO cursoTurnoVO = new CursoTurnoVO();
			cursoTurnoVO.getTurno().setCodigo(rs.getInt("turno.codigo"));
			cursoTurnoVO.getTurno().setNome(rs.getString("turno.nome"));
			cursoTurnoVO.setCurso(rs.getInt("curso.codigo"));
			cursoTurnoVO.getCursoVO().setCodigo(rs.getInt("curso.codigo"));
			cursoTurnoVO.getCursoVO().setNome(rs.getString("curso.nome"));
			cursoTurnoVO.getCursoVO().setPeriodicidade(rs.getString("curso.periodicidade"));
			cursoTurnoVO.getCursoVO().setNivelEducacional(rs.getString("curso.nivelEducacional"));
			cursoTurnoVOs.add(cursoTurnoVO);
		}
		return cursoTurnoVOs;
	}
}