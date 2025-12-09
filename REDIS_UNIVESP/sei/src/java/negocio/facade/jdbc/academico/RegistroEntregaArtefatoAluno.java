package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.NivelControleArtefatoVO;
import negocio.comuns.academico.RegistroEntregaArtefatoAlunoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RegistroEntregaArtefatoAlunoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>RegistroEntregaArtefatoAlunoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>RegistroEntregaArtefatoAlunoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see RegistroEntregaArtefatoAlunoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class RegistroEntregaArtefatoAluno extends ControleAcesso
		implements RegistroEntregaArtefatoAlunoInterfaceFacade {

	protected static String idEntidade;
	public static final long serialVersionUID = 1L;

	public RegistroEntregaArtefatoAluno() throws Exception {
		super();

	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>.
	 */
	public RegistroEntregaArtefatoAlunoVO novo() throws Exception {
		RegistroEntregaArtefatoAluno.incluir(getIdEntidade());
		RegistroEntregaArtefatoAlunoVO obj = new RegistroEntregaArtefatoAlunoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>. Verifica a conexão com o banco
	 * de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroEntregaArtefatoAlunoVO</code> que
	 *            será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ArtefatoEntregaAlunoVO artefatoEntregaAluno,
			RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAluno, DisciplinaVO disciplina, UsuarioVO usuario)
			throws Exception {
		try {

			RegistroEntregaArtefatoAluno.incluir(getIdEntidade(), true, usuario);

			final String sql = "INSERT INTO registroEntregaArtefatoAluno(usuario, matriculaperiodo, artefato, situacao, data, disciplina ) VALUES (?, ?, ?, ?, ?, ?) returning codigo";

			registroEntregaArtefatoAluno
					.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

						public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
							PreparedStatement sqlInserir = arg0.prepareStatement(sql);
							sqlInserir.setInt(1, usuario.getCodigo());
							sqlInserir.setInt(2, registroEntregaArtefatoAluno.getMatriculaPeriodo().getCodigo());
							sqlInserir.setInt(3, artefatoEntregaAluno.getCodigo());
							if (registroEntregaArtefatoAluno.getEntregue()) {
								sqlInserir.setString(4, "ENT");
								sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(new Date()));
							} else {
								sqlInserir.setString(4, "NAOENT");
								sqlInserir.setNull(5, 0);
							}
							if (disciplina.getCodigo() != null && disciplina.getCodigo() > 0) {
								sqlInserir.setInt(6, disciplina.getCodigo());
							} else {
								sqlInserir.setNull(6, 0);
							}
							return sqlInserir;
						}
					}, new ResultSetExtractor() {

						public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
							if (arg0.next()) {
								registroEntregaArtefatoAluno.setNovoObj(Boolean.FALSE);
								return arg0.getInt("codigo");
							}
							return null;
						}
					}));

			registroEntregaArtefatoAluno.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			registroEntregaArtefatoAluno.setNovoObj(Boolean.TRUE);
			registroEntregaArtefatoAluno.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado. Verifica
	 * a conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroEntregaArtefatoAlunoVO</code> que
	 *            será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ArtefatoEntregaAlunoVO artefartoEntregaAluno, RegistroEntregaArtefatoAlunoVO obj,
			DisciplinaVO disciplina, UsuarioVO usuario) throws Exception {
		try {

			RegistroEntregaArtefatoAluno.alterar(getIdEntidade());
			final String sql = "UPDATE registroEntregaArtefatoAluno set usuario=?, matriculaPeriodo=?, artefato=?, situacao=?, data=?, disciplina=?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, usuario.getCodigo());
					sqlAlterar.setInt(2, obj.getMatriculaPeriodo().getCodigo());
					sqlAlterar.setInt(3, artefartoEntregaAluno.getCodigo());
					if (obj.getEntregue()) {
						sqlAlterar.setString(4, "ENT");
						sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(new Date()));
					} else {
						sqlAlterar.setString(4, "NAOENT");
						sqlAlterar.setNull(5, 0);
					}
					if (disciplina.getCodigo() != null && disciplina.getCodigo() > 0) {
						sqlAlterar.setInt(6, disciplina.getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>RegistroEntregaArtefatoAluno</code> através do valor do código da
	 * pessoa. Retorna os objetos, com início do valor do atributo idêntico ao
	 * parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorAluno(PessoaVO pessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		RegistroEntregaArtefatoAluno.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(
				"SELECT artefato.codigo as artefato, artefato.nome as nomeArtefato, matricula.matricula, registroentregaartefatoaluno.data ");
		sqlStr.append("FROM artefato ");
		sqlStr.append(
				"INNER JOIN registroentregaartefatoaluno on artefato.codigo=registroentregaartefatoaluno.artefato  ");
		sqlStr.append(
				"INNER JOIN matriculaperiodo on registroentregaartefatoaluno.matriculaperiodo=matriculaperiodo.codigo  ");
		sqlStr.append("INNER JOIN matricula on matriculaperiodo.matricula=matricula.matricula  ");
		sqlStr.append("INNER JOIN pessoa on matricula.aluno=pessoa.codigo  ");
		sqlStr.append("WHERE pessoa.codigo =  ").append(pessoa.getCodigo());
		sqlStr.append("and registroentregaartefatoaluno.situacao = 'ENT'");
		sqlStr.append(" ORDER BY artefato.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaPorAluno(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 */
	public static List<RegistroEntregaArtefatoAlunoVO> montarDadosConsultaAlunos(SqlRowSet tabelaResultado,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosAlunos(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>.
	 * 
	 * @return O objeto da classe <code>RegistroEntregaArtefatoAlunoVO</code> com os
	 *         dados devidamente montados.
	 */
	public static RegistroEntregaArtefatoAlunoVO montarDadosAlunos(SqlRowSet dadosSQL, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {

		RegistroEntregaArtefatoAlunoVO obj = new RegistroEntregaArtefatoAlunoVO();
		obj.setTurma(dadosSQL.getString("turma"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setAluno(dadosSQL.getString("aluno"));
		obj.getMatriculaPeriodo().setCodigo(dadosSQL.getInt("matriculaperiodo"));

		return obj;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 */
	public static List<RegistroEntregaArtefatoAlunoVO> montarDadosConsultaPorAluno(SqlRowSet tabelaResultado,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosPorAluno(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>.
	 * 
	 * @return O objeto da classe <code>RegistroEntregaArtefatoAlunoVO</code> com os
	 *         dados devidamente montados.
	 */
	public static RegistroEntregaArtefatoAlunoVO montarDadosPorAluno(SqlRowSet dadosSQL, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {

		RegistroEntregaArtefatoAlunoVO obj = new RegistroEntregaArtefatoAlunoVO();
		obj.getArtefatoEntregaAluno().setCodigo(dadosSQL.getInt("artefato"));
		obj.getArtefatoEntregaAluno().setNome(dadosSQL.getString("nomeArtefato"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setData(dadosSQL.getTimestamp("data"));

		return obj;
	}

	public List<RegistroEntregaArtefatoAlunoVO> consultarAlunos(ArtefatoEntregaAlunoVO artefatoEntregaAlunoVO,
			Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO curso,
			TurmaVO turma, DisciplinaVO disciplinaVO, MatriculaVO matricula, String situacaoEntrega, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(
				"SELECT distinct matricula.matricula as matricula, pessoa.nome as aluno, turma.identificadorturma as turma, matriculaperiodo.codigo as matriculaperiodo ");
		sqlStr.append("FROM matricula ");
		sqlStr.append("INNER JOIN pessoa on matricula.aluno=pessoa.codigo  ");
		sqlStr.append("INNER JOIN unidadeensino on matricula.unidadeensino=unidadeensino.codigo  ");
		sqlStr.append("INNER JOIN curso on matricula.curso=curso.codigo  ");
		sqlStr.append("INNER JOIN turno on matricula.turno=turno.codigo  ");
		sqlStr.append("INNER JOIN matriculaperiodo on matricula.matricula=matriculaperiodo.matricula  ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodo.codigo=matriculaperiodoturmadisciplina.matriculaperiodo  ");
		sqlStr.append("INNER JOIN disciplina on matriculaperiodoturmadisciplina.disciplina=disciplina.codigo  ");
		if(Uteis.isAtributoPreenchido(turma) && turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sqlStr.append("INNER JOIN turma on matriculaperiodoturmadisciplina.turmapratica = turma.codigo  ");
		}else if(Uteis.isAtributoPreenchido(turma) && turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sqlStr.append("INNER JOIN turma on matriculaperiodoturmadisciplina.turmateorica = turma.codigo  ");
		}else if(Uteis.isAtributoPreenchido(turma) && !turma.getSubturma() && turma.getTurmaAgrupada()) {
			sqlStr.append("INNER JOIN turma on turma.codigo = ").append(turma.getCodigo());
			sqlStr.append(" and  exists ( ");
			sqlStr.append(" select ta.turma from turmaagrupada ta inner join turmadisciplina on turmadisciplina.turma = ta.turmaorigem  where ta.turmaorigem =  ").append(turma.getCodigo());
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ta.turma ");
			sqlStr.append(" and (matriculaperiodoturmadisciplina.disciplina = turmadisciplina.disciplina ");
			sqlStr.append(" or (exists (select disciplina from disciplinaequivalente where disciplinaequivalente.disciplina = matriculaperiodoturmadisciplina.disciplina ");
			sqlStr.append(" and disciplinaequivalente.equivalente = turmadisciplina.disciplina  ))");
			sqlStr.append(" or (exists (select disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = matriculaperiodoturmadisciplina.disciplina ");
			sqlStr.append(" and disciplinaequivalente.disciplina = turmadisciplina.disciplina  ))");
			sqlStr.append(" )) ");
		}else {
			sqlStr.append("INNER JOIN turma on matriculaperiodoturmadisciplina.turma = turma.codigo  ");
		}

		sqlStr.append("LEFT JOIN horarioturma on turma.codigo=horarioturma.turma   ");
		if (!artefatoEntregaAlunoVO.getPeriodicidadeCurso().equals("IN")) {
			sqlStr.append(" AND horarioturma.anovigente =  '").append(ano).append("'");
		}
		if (artefatoEntregaAlunoVO.getPeriodicidadeCurso().equals("SE")) {
			sqlStr.append(" AND horarioturma.semestrevigente =  '").append(semestre).append("'");
		}
		sqlStr.append(" LEFT JOIN horarioturmadia on horarioturma.codigo=horarioturmadia.horarioturma  ");
		sqlStr.append(
				"LEFT JOIN horarioturmadiaitem on horarioturmadiaitem.horarioturmadia=horarioturmadia.codigo and horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina ");

		sqlStr.append(" WHERE (matriculaperiodo.situacaomatriculaperiodo = 'AT' ");

		if (artefatoEntregaAlunoVO.getTrazerAlunoPreMatricula()) {
			sqlStr.append("OR matriculaperiodo.situacaomatriculaperiodo = 'PR' ");
		}
		sqlStr.append(" ) ");

		if (dataInicio != null) {
			sqlStr.append(" AND horarioturmadiaitem.data >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
		}
		if (dataFim != null) {
			sqlStr.append(" AND horarioturmadiaitem.data <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}

		sqlStr.append(" AND curso.periodicidade = '").append(artefatoEntregaAlunoVO.getPeriodicidadeCurso())
				.append("' ");
		if (!artefatoEntregaAlunoVO.getPeriodicidadeCurso().equals("IN")) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano =  '").append(ano).append("'");

		}

		if (artefatoEntregaAlunoVO.getPeriodicidadeCurso().equals("SE")) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre =  '").append(semestre).append("'");

		}

		if (turma.getCodigo() != null && turma.getCodigo() > 0) {
			sqlStr.append(" AND turma.codigo =  ").append(turma.getCodigo()).append(" ");
		}

		if (unidadeEnsinoVO.getCodigo() != null && unidadeEnsinoVO.getCodigo() > 0) {
			sqlStr.append(" AND unidadeensino.codigo =  " + unidadeEnsinoVO.getCodigo()).append(" ");
		}

		else if (!artefatoEntregaAlunoVO.getNivelControleArtefatoUnidadeEnsinoVOs().isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND unidadeensino.codigo IN(");
			for (NivelControleArtefatoVO nivelControleArtefatoVO : artefatoEntregaAlunoVO
					.getNivelControleArtefatoUnidadeEnsinoVOs()) {

				if (!virgula) {
					sqlStr.append(nivelControleArtefatoVO.getUnidadeEnsino().getCodigo());
				} else {
					sqlStr.append(", ").append(nivelControleArtefatoVO.getUnidadeEnsino().getCodigo());
				}
				virgula = true;

			}
			sqlStr.append(") ");
		}

		if (curso.getCodigo() != null && curso.getCodigo() > 0) {
			sqlStr.append(" AND curso.codigo =  " + curso.getCodigo()).append(" ");
		}

		else if (!artefatoEntregaAlunoVO.getNivelControleArtefatoCursoVOs().isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND curso.codigo IN(");
			for (NivelControleArtefatoVO nivelControleArtefatoVO : artefatoEntregaAlunoVO
					.getNivelControleArtefatoCursoVOs()) {

				if (!virgula) {
					sqlStr.append(nivelControleArtefatoVO.getCurso().getCodigo());
				} else {
					sqlStr.append(", ").append(nivelControleArtefatoVO.getCurso().getCodigo());
				}
				virgula = true;

			}
			sqlStr.append(") ");
		}

		if (disciplinaVO.getCodigo() != null && disciplinaVO.getCodigo() > 0) {
			sqlStr.append(" AND disciplina.codigo =  " + disciplinaVO.getCodigo()).append(" ");
		}

		else if (!artefatoEntregaAlunoVO.getNivelControleArtefatoDisciplinaVOs().isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND disciplina.codigo IN(");
			for (NivelControleArtefatoVO nivelControleArtefatoVO : artefatoEntregaAlunoVO
					.getNivelControleArtefatoDisciplinaVOs()) {

				if (!virgula) {
					sqlStr.append(nivelControleArtefatoVO.getDisciplina().getCodigo());
				} else {
					sqlStr.append(", ").append(nivelControleArtefatoVO.getDisciplina().getCodigo());
				}
				virgula = true;

			}
			sqlStr.append(") ");
		}
		
		if (matricula.getMatricula() != null && !matricula.getMatricula().equals("")) {
			sqlStr.append(" AND matricula.matricula =  '").append(matricula.getMatricula()).append("' ");
		}

		if (!situacaoEntrega.equals("todos")) {
			sqlStr.append(" AND");
			if (situacaoEntrega.equals("naoEntregue")) {
				sqlStr.append(" NOT");
			}
			sqlStr.append(
					"  EXISTS (select registroEntregaArtefatoAluno.matriculaperiodo from registroEntregaArtefatoAluno");
			sqlStr.append(" INNER join artefato on registroEntregaArtefatoAluno.artefato=artefato.codigo");
			sqlStr.append(" WHERE registroEntregaArtefatoAluno.matriculaperiodo=matriculaperiodo.codigo");
			sqlStr.append(" AND registroEntregaArtefatoAluno.situacao = 'ENT'");
			sqlStr.append("  AND artefato.codigo = ").append(artefatoEntregaAlunoVO.getCodigo()).append(") ");
		}

		if (!artefatoEntregaAlunoVO.getScriptsRegraRestricaoAluno().equals("")) {
			sqlStr.append(artefatoEntregaAlunoVO.getScriptsRegraRestricaoAluno());
		}
		sqlStr.append(" ORDER BY turma.identificadorturma, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaAlunos(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>RegistroEntregaArtefatoAluno</code> através do valor do artefato.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RegistroEntregaArtefatoAlunoVO> consultarRegistroEntregaArtefatoAluno(
			ArtefatoEntregaAlunoVO artefatoEntregaAluno, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		RegistroEntregaArtefatoAluno.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(
				"SELECT artefato.codigo as codigoArtefato, registroentregaartefatoaluno.codigo as codigoRegistroEntregaArtefatoAluno, registroentregaartefatoaluno.matriculaperiodo, registroEntregaArtefatoAluno.situacao as situacaoRegistroEntregaArtefatoAluno ");
		sqlStr.append("FROM artefato ");
		sqlStr.append(
				"INNER JOIN registroentregaartefatoaluno on artefato.codigo=registroentregaartefatoaluno.artefato  ");
		sqlStr.append("WHERE artefato.codigo =  ").append(artefatoEntregaAluno.getCodigo());
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
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 */
	public static List<RegistroEntregaArtefatoAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>.
	 * 
	 * @return O objeto da classe <code>RegistroEntregaArtefatoAlunoVO</code> com os
	 *         dados devidamente montados.
	 */
	public static RegistroEntregaArtefatoAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {

		RegistroEntregaArtefatoAlunoVO obj = new RegistroEntregaArtefatoAlunoVO();
		obj.getArtefatoEntregaAluno().setCodigo(dadosSQL.getInt("codigoArtefato"));
		obj.setCodigo(dadosSQL.getInt("codigoRegistroEntregaArtefatoAluno"));
		obj.getMatriculaPeriodo().setCodigo(dadosSQL.getInt("matriculaPeriodo"));
		obj.setSituacao(dadosSQL.getString("situacaoRegistroEntregaArtefatoAluno"));

		return obj;
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>RegistroEntregaArtefatoAluno</code> através do artefato e da
	 * matriculaperiodo. Retorna os objetos, com início do valor do atributo
	 * idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public RegistroEntregaArtefatoAlunoVO verificarArtefatoEntregueAluno(ArtefatoEntregaAlunoVO artefatoEntregaAluno,
			RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAluno, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		RegistroEntregaArtefatoAluno.consultar(getIdEntidade(), controlarAcesso, usuario);
		RegistroEntregaArtefatoAlunoVO obj = new RegistroEntregaArtefatoAlunoVO();

		String sql = "SELECT registroentregaartefatoaluno.artefato as codigoArtefato, registroentregaartefatoaluno.codigo as codigoRegistroEntregaArtefatoAluno, registroentregaartefatoaluno.matriculaperiodo, registroEntregaArtefatoAluno.situacao as situacaoRegistroEntregaArtefatoAluno FROM registroentregaartefatoaluno WHERE registroentregaartefatoaluno.artefato = ? AND registroentregaartefatoaluno.matriculaPeriodo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {
				artefatoEntregaAluno.getCodigo(), registroEntregaArtefatoAluno.getMatriculaPeriodo().getCodigo() });
		if (!tabelaResultado.next()) {
			return obj;
		}
		obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
		return obj;

	}

	public void validarDados(ArtefatoEntregaAlunoVO obj, String ano, String semestre, DisciplinaVO disciplina)
			throws Exception {
		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			throw new Exception("O artefato deve ser informado!");
		}

		if ((obj.getPeriodicidadeCurso().equals("AN") || obj.getPeriodicidadeCurso().equals("SE")) && ano.equals("")) {
			throw new Exception("O ano deve ser informado!");
		}

		if (obj.getPeriodicidadeCurso().equals("SE") && semestre.equals("")) {
			throw new Exception("O semestre deve ser informado!");
		}

		if (obj.getNivelControle().equals("DIS") && (disciplina.getCodigo() == null || disciplina.getCodigo() == 0)) {
			throw new Exception("Informe uma disciplina!");
		}

	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico");
	}

	@Override
	public String designIReportRelatorio(String layout) {
		if(layout.equals("relAssinaturaAlunoEResponsavelEntrega")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RegistroEntregaArtefatoAlunoAssinaturaResponsavelEntrega" + ".jrxml");

		}
		else {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator
					+ getIdEntidade() + ".jrxml");
		}

	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		if (RegistroEntregaArtefatoAluno.idEntidade == null) {
			RegistroEntregaArtefatoAluno.idEntidade = "RegistroEntregaArtefatoAluno";
		}
		return RegistroEntregaArtefatoAluno.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		RegistroEntregaArtefatoAluno.idEntidade = idEntidade;
	}

}
