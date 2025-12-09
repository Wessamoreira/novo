package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.aop.framework.Advised;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaAbonoVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.FrequenciaAulaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>FrequenciaAulaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>FrequenciaAulaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see FrequenciaAulaVO
 * @see ControleAcesso
 * @see RegistroAula
 */
@Repository
@Scope("singleton")
@Lazy
public class FrequenciaAula extends ControleAcesso implements FrequenciaAulaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public FrequenciaAula() throws Exception {
		super();
		setIdEntidade("FrequenciaAula");
	}

	public FrequenciaAulaVO novo() throws Exception {
		FrequenciaAula.incluir(getIdEntidade());
		FrequenciaAulaVO obj = new FrequenciaAulaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FrequenciaAulaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "INSERT INTO FrequenciaAula(matricula, presente, registroAula, abonado, justificado, matriculaperiodoturmadisciplina) VALUES (?, ?, ?, ?, ?, ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		
		
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getMatricula().getMatricula());
					sqlInserir.setBoolean(2, obj.isPresente().booleanValue());
					sqlInserir.setInt(3, obj.getRegistroAula().intValue());
					sqlInserir.setBoolean(4, obj.getAbonado());
					sqlInserir.setBoolean(5, obj.getJustificado());
					if(Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())){
						sqlInserir.setInt(6, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
					}else{
						sqlInserir.setNull(6, 0);
					}
					return sqlInserir;
				}
			});
		
		if(Uteis.isAtributoPreenchido(obj.getDisciplinaAbonoVO().getAbonoFalta()) 
				&& !Uteis.isAtributoPreenchido(obj.getDisciplinaAbonoVO().getCodigo())
				&& (obj.getAbonado() || obj.getJustificado()) ){
			getFacadeFactory().getDisciplinaAbonoFacade().incluir(obj.getDisciplinaAbonoVO(), usuario);
		}
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FrequenciaAulaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE FrequenciaAula set presente=?, abonado=?, justificado=?, matriculaperiodoturmadisciplina = ? WHERE ((matricula = ?) and (registroAula = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		// Matricula observador = (Matricula) ((Advised)
		// getFacadeFactory().getMatriculaFacade()).getTargetSource().getTarget();
		// obj.getMatricula().addObserver(observador);
		// obj.getMatricula().setChanged();
		// obj.getMatricula().notifyObservers();
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.isPresente().booleanValue());
					sqlAlterar.setBoolean(2, obj.getAbonado().booleanValue());
					sqlAlterar.setBoolean(3, obj.getJustificado().booleanValue());
					if(Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())){
						sqlAlterar.setInt(4, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
					}else{
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setString(5, obj.getMatricula().getMatricula());
					sqlAlterar.setInt(6, obj.getRegistroAula().intValue());
					return sqlAlterar;
				}
			});
		} finally {
			// obj.getMatricula().deleteObserver(observador);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FrequenciaAulaVO obj, UsuarioVO usuario) throws Exception {
		FrequenciaAula.excluir(getIdEntidade());
		String sql = "DELETE FROM FrequenciaAula WHERE ((matricula = ?) and (registroAula = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMatricula().getMatricula(), obj.getRegistroAula() });
	}

	public List<FrequenciaAulaVO> consultarPorConteudoRegistroAula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FrequenciaAula.* FROM FrequenciaAula, RegistroAula WHERE FrequenciaAula.registroAula = RegistroAula.codigo and RegistroAula.conteudo like('" + valorConsulta + "%') ORDER BY RegistroAula.conteudo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<FrequenciaAulaVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT FrequenciaAula.* FROM FrequenciaAula, Matricula WHERE FrequenciaAula.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>FrequenciaAulaVO</code> resultantes da consulta.
	 */
	public static List<FrequenciaAulaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<FrequenciaAulaVO> vetResultado = new ArrayList<FrequenciaAulaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FrequenciaAulaVO</code>.
	 *
	 * @return O objeto da classe <code>FrequenciaAulaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static FrequenciaAulaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FrequenciaAulaVO obj = new FrequenciaAulaVO();
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.setPresente((dadosSQL.getBoolean("presente")));
		obj.setAbonado((dadosSQL.getBoolean("abonado")));
		obj.setJustificado((dadosSQL.getBoolean("justificado")));
		obj.setRegistroAula(new Integer(dadosSQL.getInt("registroAula")));
		obj.setMatriculaPeriodoTurmaDisciplina(dadosSQL.getInt("matriculaPeriodoTurmaDisciplina"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosMatriculaPeriodoTurmaDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		return obj;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FrequenciaAulaVO</code>.
	 *
	 * @return O objeto da classe <code>FrequenciaAulaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static FrequenciaAulaVO montarDadosComNomeAluno(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FrequenciaAulaVO obj = new FrequenciaAulaVO();
		obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
		obj.getMatricula().getAluno().setNome(tabelaResultado.getString("nome"));
		montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		obj.setPresente(tabelaResultado.getBoolean("presente"));
		obj.setRegistroAula(new Integer(tabelaResultado.getInt("registroAula")));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FrequenciaAulaVO</code>.
	 *
	 * @return O objeto da classe <code>FrequenciaAulaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static FrequenciaAulaVO montarDadosComNomeAlunoSituacaoMatriculaPeriodo(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FrequenciaAulaVO obj = new FrequenciaAulaVO();
		obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
		obj.getMatricula().getAluno().setNome(tabelaResultado.getString("nome"));
		montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		obj.setPresente(tabelaResultado.getBoolean("presente"));
		obj.setRegistroAula(new Integer(tabelaResultado.getInt("registroAula")));
		obj.getHistoricoVO().getMatriculaPeriodo().setSituacao(tabelaResultado.getString("situacao"));
		obj.getHistoricoVO().setMatricula(obj.getMatricula());
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>FrequenciaAulaVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatricula(FrequenciaAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirFrequenciaAulas(Integer registroAula, UsuarioVO usuario) throws Exception {
		FrequenciaAula.excluir(getIdEntidade());
		String sql = "DELETE FROM FrequenciaAula WHERE (registroAula = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { registroAula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirFrequenciaAulaVOs(RegistroAulaVO registroAulaVO, Integer perfilPadraoProfessor, UsuarioVO usuario) throws Exception {
		int cont = 0;
		for (Iterator<FrequenciaAulaVO> iterator = registroAulaVO.getFrequenciaAulaVOs().iterator(); iterator.hasNext();) {
			FrequenciaAulaVO obj = iterator.next();
			if ((!perfilPadraoProfessor.equals(0) && usuario.getPerfilAcesso().getCodigo().equals(perfilPadraoProfessor) && obj.getHistoricoVO().getIsProfessorNaoPodeAlterarRegistro()) || (obj.getFrequenciaOculta())  || (obj.getBloqueadoDevidoDataMatricula() && !obj.getHistoricoVO().getConfiguracaoAcademico().getRegistrarComoFaltaAulasRealizadasAposDataMatricula())) {
				continue;
			} else {
				obj.setRegistroAula(registroAulaVO.getCodigo());
				if (obj.getNovoObj()) {
					incluir(obj, usuario);
					cont++;
				} else {
						alterar(obj, usuario);
						if (Uteis.isAtributoPreenchido(obj.getRemoverAbono())) {
							DisciplinaAbonoVO disciplinaAbonoVO = getFacadeFactory().getDisciplinaAbonoFacade().consultarDisciplinaAbonoPorRegistroAula(obj.getRegistroAula(), obj.getMatricula().getMatricula(), registroAulaVO.getHorario(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
							disciplinaAbonoVO.setFaltaAbonada(!obj.getRemoverAbono());
							if (Uteis.isAtributoPreenchido(Uteis.isAtributoPreenchido(disciplinaAbonoVO.getCodigo()))) {
								getFacadeFactory().getDisciplinaAbonoFacade().alterar(disciplinaAbonoVO, usuario);
							}		
						}
					cont++;
				}
//				executarAlteracaoFaltaPrimeiroSegundoTerceiroQuartoTotalFaltaBimestreFrequenciaHistorico(obj.getMatriculaPeriodoTurmaDisciplina(), false, usuario);
			}
		}
		if(cont == 0) {
			throw new Exception("Não foi informada nenhuma frequência válida para esse registro de aula.");
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>FrequenciaAulaVO</code>
	 * relacionados a um objeto da classe <code>academico.RegistroAula</code>.
	 *
	 * @param registroAula
	 *            Atributo de <code>academico.RegistroAula</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>FrequenciaAulaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>FrequenciaAulaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static List<FrequenciaAulaVO> consultarFrequenciaAulas(Integer registroAula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<FrequenciaAulaVO> frequenciaAulaVOs = new ArrayList<FrequenciaAulaVO>(0);
		StringBuilder sql = new StringBuilder(" SELECT registroaula, presente, abonado, justificado, frequenciaaula.matricula as matricula ,pessoa.nome FROM FrequenciaAula INNER JOIN matricula on matricula.matricula =  frequenciaaula.matricula INNER JOIN pessoa on matricula.aluno = pessoa.codigo WHERE registroAula = ?");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { registroAula });
		while (resultado.next()) {
			frequenciaAulaVOs.add(FrequenciaAula.montarDados(resultado, nivelMontarDados, usuario));
		}
		return frequenciaAulaVOs;
	}

	/**
	 * Operação responsável por consultar todos os <code>FrequenciaAulaVO</code>
	 * relacionados a um objeto da classe <code>academico.RegistroAula</code>.
	 *
	 * @param registroAula
	 *            Atributo de <code>academico.RegistroAula</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>FrequenciaAulaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>FrequenciaAulaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static List<FrequenciaAulaVO> consultarFrequenciaAulasComNomeAluno(Integer registroAula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<FrequenciaAulaVO> frequenciaAulaVOs = new ArrayList<FrequenciaAulaVO>(0);
		StringBuilder sql = new StringBuilder(" SELECT frequenciaaula.registroaula,frequenciaaula.presente,frequenciaaula.matricula,pessoa.nome FROM FrequenciaAula INNER JOIN matricula on matricula.matricula =  frequenciaaula.matricula INNER JOIN pessoa on matricula.aluno = pessoa.codigo WHERE registroAula = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { registroAula });
		while (tabelaResultado.next()) {
			frequenciaAulaVOs.add(FrequenciaAula.montarDadosComNomeAluno(tabelaResultado, nivelMontarDados, usuario));
		}
		return frequenciaAulaVOs;
	}

	public static List<FrequenciaAulaVO> consultarFrequenciaAulasComNomeAlunoSituacaoMatriculaPeriodo(Integer registroAula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<FrequenciaAulaVO> frequenciaAulaVOs = new ArrayList<FrequenciaAulaVO>(0);
		StringBuilder sql = new StringBuilder(" SELECT frequenciaaula.registroaula, frequenciaaula.presente, frequenciaaula.matricula, pessoa.nome, matriculaperiodo.situacao ");
		sql.append("FROM FrequenciaAula ");
		sql.append("INNER JOIN registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sql.append("INNER JOIN matricula on matricula.matricula =  frequenciaaula.matricula ");
		sql.append("INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula AND ");
		sql.append("matriculaperiodo.ano = registroaula.ano and matriculaperiodo.semestre = registroaula.semestre ");
		sql.append("INNER JOIN pessoa on matricula.aluno = pessoa.codigo ");
		sql.append("WHERE registroAula = ?");
		sql.append("order by pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { registroAula });
		while (tabelaResultado.next()) {
			frequenciaAulaVOs.add(FrequenciaAula.montarDadosComNomeAlunoSituacaoMatriculaPeriodo(tabelaResultado, nivelMontarDados, usuario));
		}
		return frequenciaAulaVOs;
	}

	public FrequenciaAulaVO consultarPorChavePrimaria(String matriculaPrm, Integer registroAulaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM FrequenciaAula WHERE matricula = ? AND registroAula = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matriculaPrm, registroAulaPrm });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return null;
	}

	public Integer consultarSomaFrequenciaAlunoEspecifico(String matricula, String semestre, String ano, Integer turma, Integer disciplina, boolean presente, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("SELECT sum(somaCargaHoraria) as somaCargaHoraria  from ( ");
		sql.append("(select SUM(case when turno.considerarhoraaulasessentaminutosgeracaodiario then 60 else registroaula.cargahoraria end) as somaCargaHoraria ");
		sql.append("FROM registroAula ");
		sql.append(" inner join frequenciaaula on registroAula.codigo = frequenciaaula.registroAula ");
		sql.append(" inner join turma on registroAula.turma = turma.codigo ");
		sql.append(" inner join turno on turma.turno = turno.codigo ");
		sql.append(" WHERE frequenciaaula.matricula = '").append(matricula).append("'  ");
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		sql.append(" and RegistroAula.turma = ").append(turma);
		sql.append(" and (RegistroAula.disciplina = ").append(disciplina);
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(")) ");
		sql.append(" ) ");
		sql.append(" and presente = ").append(presente).append(" and atividadecomplementar = false )");
		sql.append(" union all ");
		// Alterado por Edigar A. Diniz Junior
		// Agora para se calcular as faltas abonadas, devemos olhar para o
		// próprio registro de aula, pois a tabela
		// disciplinaAbono ficou para registro de histórico (quem abonou que
		// falta de qual disciplina). Na prática
		// podem existir abonos futuros - como licenca maternidade, que ficam
		// registradas somente na fequenciaAula
		sql.append("(select SUM(case when turno.considerarhoraaulasessentaminutosgeracaodiario then 60 else registroaula.cargahoraria end) as somaCargaHoraria ");
		sql.append(" FROM registroAula ");
		sql.append(" inner join frequenciaaula on registroAula.codigo = frequenciaaula.registroAula ");
		sql.append(" inner join turma on registroAula.turma = turma.codigo ");
		sql.append(" inner join turno on turma.turno = turno.codigo ");
		sql.append(" WHERE frequenciaaula.matricula = '").append(matricula).append("' ");
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		sql.append(" and RegistroAula.turma = ").append(turma);
		sql.append(" and (RegistroAula.disciplina = ").append(disciplina);
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(")) ");
		sql.append(" ) ");
		sql.append(" and abonado = ").append(presente).append(" and atividadecomplementar = false)");
		sql.append(" union all ");
		// Alterado por Carlos
		// Deve ser calculado a atividade complementar em horas
		sql.append("(select SUM(registroaula.cargahoraria * 60) as somaCargaHoraria ");
		sql.append(" FROM registroAula ");
		sql.append(" inner join frequenciaaula on registroAula.codigo = frequenciaaula.registroAula ");
		sql.append(" inner join turma on registroAula.turma = turma.codigo ");
		sql.append(" WHERE frequenciaaula.matricula = '").append(matricula).append("' ");
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		sql.append(" and RegistroAula.turma = ").append(turma);
		sql.append(" and (RegistroAula.disciplina = ").append(disciplina);
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(")) ");
		sql.append(" or (turma.turmaagrupada and RegistroAula.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(")) ");
		sql.append(" ) ");
		sql.append(" and (presente = ").append(presente).append(presente ? " or " : " and ").append(" abonado  = ").append(presente).append(") and atividadecomplementar = true)");

		sql.append(") as t");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (new Integer(tabelaResultado.getInt("somaCargaHoraria")));
	}

	public Integer consultarSomaFrequenciaAlunoEspecificoDisciplinaComposta(String matricula, String semestre, String ano, Integer turma, Integer disciplina, boolean presente, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("SELECT sum(somaCargaHoraria) as somaCargaHoraria  from ( (select SUM(registroaula.cargahoraria) as somaCargaHoraria ");
		sql.append("FROM frequenciaaula, registroAula, disciplinaComposta WHERE frequenciaaula.matricula = '").append(matricula).append("' and frequenciaAula.registroAula = RegistroAula.codigo ");
		sql.append(" and disciplinaComposta.composta = RegistroAula.disciplina ");
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		sql.append(" and RegistroAula.turma = ").append(turma).append(" and disciplinaComposta.disciplina = ").append(disciplina).append(" and presente = ").append(presente).append(")");
		sql.append(" union all (select SUM(registroaula.cargahoraria) FROM registroaula ");
		sql.append(" inner join  disciplinaabono on disciplinaabono.registroaula = RegistroAula.codigo ");
		sql.append(" WHERE disciplinaabono.matricula = '").append(matricula).append("' and disciplinaabono.disciplina = ").append(disciplina).append(")) as t");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (new Integer(tabelaResultado.getInt("somaCargaHoraria")));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return FrequenciaAula.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FrequenciaAula.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM frequenciaaula WHERE matricula = '").append(matricula).append("' ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAula(Integer turma, Integer disciplina, String semestre, String ano, Date dataAula, String nrAula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM frequenciaaula WHERE registroaula in( ");
		sqlStr.append(" SELECT codigo FROM registroaula WHERE turma = ").append(turma).append(" AND disciplina = ").append(disciplina);
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append(" AND data = '").append(Uteis.getDataJDBC(dataAula)).append("' AND horario LIKE('").append(nrAula).append("%'))");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAulaPorDia(Integer turma, String semestre, String ano, Date dataAula, Integer professor, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM frequenciaaula WHERE registroaula in( ");
		sqlStr.append(" SELECT codigo FROM registroaula WHERE turma = ").append(turma);
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append(" AND data = '").append(Uteis.getDataJDBC(dataAula)).append("' AND professor = ").append(professor).append(")");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAulaPorAnoSemestreProfessor(Integer turma, String semestre, String ano, Integer professor, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM frequenciaaula WHERE registroaula in( ");
		sqlStr.append(" SELECT codigo FROM registroaula WHERE turma = ").append(turma);
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append(" AND professor = ").append(professor).append(")");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAula(Integer turma, String semestre, String ano, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM frequenciaaula WHERE registroaula in( ");
		sqlStr.append(" SELECT codigo FROM registroaula WHERE turma = ").append(turma).append(" ");
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("') ");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAbonoFalta(final DisciplinaAbonoVO disciplinaAbonoVO, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE FrequenciaAula set presente=?, abonado=?, justificado=? WHERE ((matricula = ?) and (registroAula = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, false);
				sqlAlterar.setBoolean(2, disciplinaAbonoVO.getFaltaAbonada());
				sqlAlterar.setBoolean(3, disciplinaAbonoVO.getFaltaJustificada());
				sqlAlterar.setString(4, disciplinaAbonoVO.getMatricula().getMatricula());
				sqlAlterar.setInt(5, disciplinaAbonoVO.getRegistroAula().getCodigo());
				return sqlAlterar;
			}
		}) == 0) {
			executarGeracaoFrequenciaAulaAbonoFalta(disciplinaAbonoVO, usuario);
		}
		alterarAbonoFaltaTurmaOrigem(disciplinaAbonoVO, usuario);		
	}
	
	public void alterarAbonoFaltaTurmaOrigem(DisciplinaAbonoVO disciplinaAbonoVO, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(disciplinaAbonoVO.getRegistroAula().getTurmaOrigem())){
			final StringBuilder sql2 = new StringBuilder("update frequenciaaula set presente = ?, abonado=?, justificado=?  from registroaula ");
			sql2.append(" where registroaula.codigo = frequenciaaula.registroaula ");
			sql2.append(" and  matricula  = ? ");
			sql2.append(" and abonado = false ");
			sql2.append(" and registroaula.turma = ? ");
			sql2.append(" and (disciplina = ? ");
			sql2.append(" or disciplina in (select disciplina from disciplinaequivalente  where equivalente = ?) ");
			sql2.append(" or disciplina in (select equivalente from disciplinaequivalente  where disciplina = ?) ");
			sql2.append(" ) ");
			sql2.append(" and registroaula.data::DATE = ? ");
			sql2.append(" and registroaula.nraula = ? ");
			sql2.append(" and registroaula.ano = ? ");
			sql2.append(" and registroaula.semestre = ? ");
			sql2.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql2.toString());
					sqlAlterar.setBoolean(1, false);
					sqlAlterar.setBoolean(2, disciplinaAbonoVO.getFaltaAbonada());
					sqlAlterar.setBoolean(3, disciplinaAbonoVO.getFaltaJustificada());
					sqlAlterar.setString(4, disciplinaAbonoVO.getMatricula().getMatricula());
					sqlAlterar.setInt(5, disciplinaAbonoVO.getRegistroAula().getTurmaOrigem());
					sqlAlterar.setInt(6, disciplinaAbonoVO.getRegistroAula().getDisciplina().getCodigo());
					sqlAlterar.setInt(7, disciplinaAbonoVO.getRegistroAula().getDisciplina().getCodigo());
					sqlAlterar.setInt(8, disciplinaAbonoVO.getRegistroAula().getDisciplina().getCodigo());
					sqlAlterar.setDate(9, Uteis.getDataJDBC(disciplinaAbonoVO.getRegistroAula().getData()));
					sqlAlterar.setInt(10, disciplinaAbonoVO.getRegistroAula().getNrAula());
					sqlAlterar.setString(11, disciplinaAbonoVO.getRegistroAula().getAno());
					sqlAlterar.setString(12, disciplinaAbonoVO.getRegistroAula().getSemestre());
					return sqlAlterar;
				}
			});
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirFrequenciaAulaPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina, String matricula, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM FrequenciaAula WHERE registroAula IN (");
		sqlStr.append(" SELECT DISTINCT ra.codigo");
		sqlStr.append(" FROM FrequenciaAula fa");
		sqlStr.append(" INNER JOIN RegistroAula ra on ra.codigo = fa.registroAula");
		sqlStr.append(" INNER JOIN MatriculaPeriodo mp on mp.matricula = fa.matricula");
		sqlStr.append(" INNER JOIN MatriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = mp.codigo");
		sqlStr.append(" WHERE mptd.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		sqlStr.append(" AND mptd.ano = ra.ano");
		sqlStr.append(" AND mptd.semestre = ra.semestre");
		sqlStr.append(" AND mptd.turma = ra.turma");
		sqlStr.append(" AND mptd.disciplina = ra.disciplina");
		sqlStr.append(") ");
		sqlStr.append(" and FrequenciaAula.matricula = '").append(matricula).append("' ");
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirFrequenciaAulaPorMatriculaMatriculaPeriodoTurmaDisciplina(String matricula, Integer matriculaPeriodo, Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM FrequenciaAula WHERE registroAula IN (");
		sqlStr.append(" SELECT DISTINCT ra.codigo");
		sqlStr.append(" FROM FrequenciaAula fa");
		sqlStr.append(" INNER JOIN RegistroAula ra on ra.codigo = fa.registroAula");
		sqlStr.append(" INNER JOIN MatriculaPeriodo mp on mp.matricula = fa.matricula");
		sqlStr.append(" WHERE mp.codigo = ").append(matriculaPeriodo);
		sqlStr.append(" AND mp.ano = ra.ano");
		sqlStr.append(" AND mp.semestre = ra.semestre");
		sqlStr.append(" AND ra.turma = ").append(turma);
		sqlStr.append(" AND ra.disciplina = ").append(disciplina);
		sqlStr.append(") ");
		sqlStr.append(" and FrequenciaAula.matricula = '").append(matricula).append("' ");
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	/**
	 * Responsável por executar a geração da frequência aula quando é realizado
	 * a transferência de turma
	 * 
	 * @author Wellington Rodrigues - 05/03/2015
	 * @param disciplinaAbonoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarGeracaoFrequenciaAulaAbonoFalta(DisciplinaAbonoVO disciplinaAbonoVO, UsuarioVO usuarioVO) throws Exception {
		FrequenciaAulaVO obj = new FrequenciaAulaVO();
		obj.setNovoObj(true);
		obj.setPresente(false);
		obj.setAbonado(disciplinaAbonoVO.getFaltaAbonada());
		obj.setJustificado(disciplinaAbonoVO.getFaltaJustificada());
		obj.setRegistroAula(disciplinaAbonoVO.getRegistroAula().getCodigo());
		obj.setMatricula(disciplinaAbonoVO.getMatricula());
		incluir(obj, usuarioVO);
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>FrequenciaAulaVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(FrequenciaAulaVO obj) throws ConsistirException {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			throw new ConsistirException("O campo MATRÍCULA (Freqüência Aula) deve ser informado.");
		}
	}

	/**
	 * Responsável por executar a alteração das faltas do primeiro, segundo,
	 * terceiro, quarto bimestre, total falta e frequencia do histórico que por
	 * sua vez será consultado através da matriculaPeriodoTurmaDisciplina.
	 * 
	 * @author Wellington Rodrigues - 07/04/2015
	 * @param matriculaPeriodoTurmaDisciplina
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	private void executarAlteracaoFaltaPrimeiroSegundoTerceiroQuartoTotalFaltaBimestreFrequenciaHistorico(Integer matriculaPeriodoTurmaDisciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplina, false, controlarAcesso, usuarioVO);
		getFacadeFactory().getHistoricoFacade().executarAlteracaoFaltaPrimeiroSegundoTerceiroQuartoTotalFaltaBimestreFrequenciaConsiderandoHistoricoPorCorrespondenciaEquivalenciaFazParteComposicao(historicoVO, controlarAcesso, usuarioVO);
	}
	
	@Override
	public Integer consultarSomaFrequenciaAlunoEspecificoConsiderantoTurmaTeoricaETurmaPratica(String matricula, String semestre, String ano, Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT sum(somaCargaHoraria) as somaCargaHoraria from ( ");
		sql.append("(select SUM(registroaula.cargahoraria) as somaCargaHoraria ");
		sql.append("FROM frequenciaaula ");
		sql.append("inner join registroAula on registroAula.codigo = frequenciaaula.registroAula ");
		sql.append("inner join matriculaperiodoturmadisciplina mptd ");
		sql.append("	on mptd.matricula = frequenciaaula.matricula ");
		sql.append("	and mptd.turmateorica = registroaula.turma ");
		sql.append("	and mptd.disciplina = registroaula.disciplina ");
		sql.append("	and mptd.ano = registroaula.ano ");
		sql.append("	and mptd.semestre = registroaula.semestre ");
		sql.append("where mptd.matricula = '").append(matricula).append("'");
		sql.append(" and mptd.turma =  ").append(turma);
		sql.append(" and mptd.disciplina = ").append(disciplina);
		sql.append(" and (presente or abonado) ");
		if(Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and mptd.ano = '").append(ano).append("'");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and mptd.semestre = '").append(semestre).append("'");
		}
		sql.append(" and atividadecomplementar = false)");
		sql.append(" union ");
		sql.append("(select SUM(registroaula.cargahoraria) as somaCargaHoraria ");
		sql.append("FROM frequenciaaula ");
		sql.append("inner join registroAula on registroAula.codigo = frequenciaaula.registroAula ");
		sql.append("inner join matriculaperiodoturmadisciplina mptd ");
		sql.append("	on mptd.matricula = frequenciaaula.matricula ");
		sql.append("	and mptd.turmateorica = registroaula.turma ");
		sql.append("	and mptd.disciplina = registroaula.disciplina ");
		sql.append("	and mptd.ano = registroaula.ano ");
		sql.append("	and mptd.semestre = registroaula.semestre ");
		sql.append("where mptd.matricula = '").append(matricula).append("'");
		sql.append(" and mptd.turma =  ").append(turma);
		sql.append(" and mptd.disciplina = ").append(disciplina);
		sql.append(" and (presente or abonado) ");
		if(Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and mptd.ano = '").append(ano).append("'");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and mptd.semestre = '").append(semestre).append("'");
		}
		sql.append(" and atividadecomplementar)");
		sql.append(" union ");
		sql.append("(select SUM(registroaula.cargahoraria) as somaCargaHoraria ");
		sql.append("FROM frequenciaaula ");
		sql.append("inner join registroAula on registroAula.codigo = frequenciaaula.registroAula ");
		sql.append("inner join matriculaperiodoturmadisciplina mptd ");
		sql.append("	on mptd.matricula = frequenciaaula.matricula ");
		sql.append("	and mptd.turmaPratica = registroaula.turma ");
		sql.append("	and mptd.disciplina = registroaula.disciplina ");
		sql.append("	and mptd.ano = registroaula.ano ");
		sql.append("	and mptd.semestre = registroaula.semestre ");
		sql.append("where mptd.matricula = '").append(matricula).append("'");
		sql.append(" and mptd.turma =  ").append(turma);
		sql.append(" and mptd.disciplina = ").append(disciplina);
		sql.append(" and (presente or abonado) ");
		if(Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and mptd.ano = '").append(ano).append("'");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and mptd.semestre = '").append(semestre).append("'");
		}
		sql.append(" and atividadecomplementar = false)");
		sql.append(" union ");
		sql.append("(select SUM(registroaula.cargahoraria) as somaCargaHoraria ");
		sql.append("FROM frequenciaaula ");
		sql.append("inner join registroAula on registroAula.codigo = frequenciaaula.registroAula ");
		sql.append("inner join matriculaperiodoturmadisciplina mptd ");
		sql.append("	on mptd.matricula = frequenciaaula.matricula ");
		sql.append("	and mptd.turmaPratica = registroaula.turma ");
		sql.append("	and mptd.disciplina = registroaula.disciplina ");
		sql.append("	and mptd.ano = registroaula.ano ");
		sql.append("	and mptd.semestre = registroaula.semestre ");
		sql.append("where mptd.matricula = '").append(matricula).append("'");
		sql.append(" and mptd.turma =  ").append(turma);
		sql.append(" and mptd.disciplina = ").append(disciplina);
		sql.append(" and (presente or abonado) ");
		if(Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and mptd.ano = '").append(ano).append("'");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and mptd.semestre = '").append(semestre).append("'");
		}
		sql.append(" and atividadecomplementar)");
		sql.append(") as t");
		return getConexao().getJdbcTemplate().queryForInt(sql.toString());
	}

	
	public static void montarDadosMatriculaPeriodoTurmaDisciplina(FrequenciaAulaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo() == null)) {
			obj.setMatriculaPeriodoTurmaDisciplinaVO(null);
			return;
		}
		obj.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), nivelMontarDados, usuario));
	}
	
	@Override
	public List<FrequenciaAulaVO> consultarMatriculaFrequenciaAulaPorRegistroAula(Integer registroAula, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from frequenciaAula where registroAula = ").append(registroAula);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<FrequenciaAulaVO> listaFrequenciaVOs = new ArrayList<FrequenciaAulaVO>(0);
		while (tabelaResultado.next()) {
			FrequenciaAulaVO obj = new FrequenciaAulaVO();
			obj.setRegistroAula(tabelaResultado.getInt("registroAula"));
			obj.setPresente(tabelaResultado.getBoolean("presente"));
			obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
			obj.setAbonado(tabelaResultado.getBoolean("abonado"));
			obj.setJustificado(tabelaResultado.getBoolean("justificado"));
			listaFrequenciaVOs.add(obj);
		}
		return listaFrequenciaVOs;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirFrequenciaFaltaAulasRealizadasAposDataMatricula(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Date dataMatricula, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into frequenciaaula (registroaula, presente, matricula, abonado, justificado, matriculaperiodoturmadisciplina) ( ");
		sb.append("select distinct t.codigo, false, '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' ");
		sb.append("as matricula, false, false,").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append("as matriculaPeriodoTurmaDisciplina from ( ");		
		//Turma base
		sb.append("select registroaula.codigo  ");
		sb.append("from registroaula where turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		sb.append(" and disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		sb.append(" and ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
		sb.append(" and semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
		sb.append(" and data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
		sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
		sb.append(" union all ");
		//turmabase considerando turmaagrupada com a mesma disciplina
		sb.append(" select ra.codigo from registroaula ");
		sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem and ra.disciplina = registroaula.disciplina and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre and ra.nraula = registroaula.nraula and ra.data = registroaula.data ");
		sb.append("where registroaula.turmaorigem is not null");
		sb.append(" and registroaula.turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
		sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
		sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
		sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
		sb.append("union all ");
		//turmabase considerando turmaagrupada com equivalencia de disciplina cenario 1 
		sb.append("select ra.codigo from registroaula  ");
		sb.append("inner join disciplinaequivalente on disciplinaequivalente.disciplina = registroaula.disciplina  ");
		sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem  ");
		sb.append("and ra.disciplina = disciplinaequivalente.equivalente and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
		sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data  ");
		sb.append("where registroaula.turmaorigem is not null ");
		sb.append(" and registroaula.turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
		sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
		sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
		sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
		sb.append("union all ");
		//turmabase considerando turmaagrupada com equivalencia de disciplina cenario 2
		sb.append("select ra.codigo from registroaula  ");
		sb.append("inner join disciplinaequivalente on disciplinaequivalente.equivalente = registroaula.disciplina  ");
		sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem ");
		sb.append("and ra.disciplina = disciplinaequivalente.disciplina and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
		sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data ");
		sb.append("where registroaula.turmaorigem is not null ");
		sb.append(" and registroaula.turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
		sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
		sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
		sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo())) {
			sb.append("union all ");
			//turmateorica
			sb.append("select registroaula.codigo ");
			sb.append("from registroaula where turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo());
			sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
			sb.append("union all ");
			//turmateorica considerando turmaagrupada com a mesma disciplina
			sb.append("select ra.codigo from registroaula ");
			sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem  ");
			sb.append("and ra.disciplina = registroaula.disciplina and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
			sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data ");
			sb.append("where registroaula.turmaorigem is not null ");
			sb.append(" and registroaula.turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo());
			sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
			sb.append("union all ");
			//turmateorica considerando turmaagrupada com equivalencia de disciplina cenario 1 
			sb.append("select ra.codigo from registroaula ");
			sb.append("inner join disciplinaequivalente on disciplinaequivalente.disciplina = registroaula.disciplina  ");
			sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem  ");
			sb.append("and ra.disciplina = disciplinaequivalente.equivalente and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
			sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data ");
			sb.append("where registroaula.turmaorigem is not null ");
			sb.append(" and registroaula.turma =  ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo());
			sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
			//turmateorica considerando turmaagrupada com equivalencia de disciplina cenario 2
			sb.append("union all ");
			sb.append("select ra.codigo from registroaula ");
			sb.append("inner join disciplinaequivalente on disciplinaequivalente.equivalente = registroaula.disciplina  ");
			sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem  ");
			sb.append("and ra.disciplina = disciplinaequivalente.disciplina and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
			sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data  ");
			sb.append("where registroaula.turmaorigem is not null ");
			sb.append(" and registroaula.turma =  ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo());
			sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
		} 
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo())) {
			sb.append("union all ");
			//turmapratica
			sb.append("select registroaula.codigo ");
			sb.append("from registroaula where turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo());
			sb.append("and disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append("and ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append("and semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append("and data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
			sb.append("union all ");
			//turmapratica considerando turmaagrupada com a mesma disciplina
			sb.append("select ra.codigo from registroaula ");
			sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem  ");
			sb.append("and ra.disciplina = registroaula.disciplina and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
			sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data ");
			sb.append("where registroaula.turmaorigem is not null ");
			sb.append(" and registroaula.turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo());
			sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
			sb.append("union all ");
			//turmapratica considerando turmaagrupada com equivalencia de disciplina cenario 1 
			sb.append("select ra.codigo from registroaula ");
			sb.append("inner join disciplinaequivalente on disciplinaequivalente.disciplina = registroaula.disciplina ");
			sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem ");
			sb.append("and ra.disciplina = disciplinaequivalente.equivalente and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
			sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data  ");
			sb.append("where registroaula.turmaorigem is not null ");
			sb.append(" and registroaula.turma =  ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo());
			sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
			//turmapratica considerando turmaagrupada com equivalencia de disciplina cenario 2
			sb.append("union all ");
			sb.append("select ra.codigo from registroaula ");
			sb.append("inner join disciplinaequivalente on disciplinaequivalente.equivalente = registroaula.disciplina  ");
			sb.append("inner join registroaula ra on ra.turma = registroaula.turmaorigem  ");
			sb.append("and ra.disciplina = disciplinaequivalente.disciplina and ra.ano = registroaula.ano and ra.semestre = registroaula.semestre ");
			sb.append("and ra.nraula = registroaula.nraula and ra.data = registroaula.data  ");			
			sb.append("where registroaula.turmaorigem is not null ");
			sb.append(" and registroaula.turma =  ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo());
			sb.append(" and registroaula.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			sb.append(" and registroaula.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and registroaula.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and registroaula.data < '").append(Uteis.getDataJDBC(dataMatricula)).append("' ");
			sb.append(" and not exists (select frequenciaaula.matricula from frequenciaaula where frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVO.getMatricula()).append("' )");
		}
		sb.append(") as t );");
		getConexao().getJdbcTemplate().update(sb.toString());
	}
}
