package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import negocio.comuns.academico.DisciplinaVO;
//import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
//import relatorio.negocio.comuns.academico.HistoricoTurmaRelVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ProfessorTitularDisciplinaTurmaVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ProfessorTitularDisciplinaTurmaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * 
 * @see ProfessorTitularDisciplinaTurmaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy

public class ProfessorTitularDisciplinaTurma extends ControleAcesso   {
	protected static String idEntidade;

	@SuppressWarnings("OverridableMethodCallInConstructor")
	public ProfessorTitularDisciplinaTurma() throws Exception {
		super();
		setIdEntidade("ProfessorTitularDisciplinaTurma");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ProfessorTitularDisciplinaTurmaVO</code>.
	 */
	public ProfessorTitularDisciplinaTurmaVO novo() throws Exception {
		ProfessorTitularDisciplinaTurma.incluir(getIdEntidade());
		ProfessorTitularDisciplinaTurmaVO obj = new ProfessorTitularDisciplinaTurmaVO();
		return obj;
	}

	public static void verificaPermissaoAlterarCargaHoraria() throws Exception {
//		verificarPermissaoUsuarioFuncionalidade("RegistroAulaAlterarCargaHoraria");
	}

	// public void
	// verificarSeExisteProgramacaoAulaParaEssaTurma_e_Disciplina(ProfessorTitularDisciplinaTurmaVO
	// obj) throws Exception {
	// if (existeRegistroAula(obj)) {
	// throw new
	// Exception("Já existe aula registrada nesta data, turma e horário.");
	// }
	// }
	public void validarConsultaDoUsuario(UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), true, usuario);
	}

	public void incluirProfessorTitularDisciplinaTurmaEspecificaNaoAgrupada(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception {
		ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
		p.setTurma(turma);
		p.getDisciplina().setCodigo(disciplina);
		p.getProfessor().setCodigo(professor);
		p.setAno(ano);
		p.setSemestre(semestre);
		// p.setData(dataAula);
		// p.setTitular(true);
		// p.getResponsavelRegistro().setCodigo(getUsuarioLogado().getCodigo());
		List l = this.consultarPorProfessorTurmaDisciplinaAnoSemestre(professor, turma.getCodigo(), disciplina, ano, semestre, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (l.isEmpty()) {
			this.incluir(p, usuario);
		} else {
			ProfessorTitularDisciplinaTurmaVO prof = (ProfessorTitularDisciplinaTurmaVO) l.get(0);
			p.setCodigo(prof.getCodigo());
			this.alterar(p, usuario);
		}
		// p.setTitular(Boolean.FALSE);
		alterarTodosProfessores(p, usuario);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProfessorTitularDisciplinaTurmaVO</code>. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProfessorTitularDisciplinaTurmaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluirProfessorTitularDisciplinaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception {
		if (turma.getTurmaAgrupada()) {
			getFacadeFactory().getTurmaFacade().carregarDados(turma, NivelMontarDados.TODOS, usuario);
			for (TurmaAgrupadaVO turmaAgrupada : turma.getTurmaAgrupadaVOs()) {
				incluirProfessorTitularDisciplinaTurmaEspecificaNaoAgrupada(turmaAgrupada.getTurma(), disciplina, professor, ano, semestre, dataAula, usuario);
			}
		}
		incluirProfessorTitularDisciplinaTurmaEspecificaNaoAgrupada(turma, disciplina, professor, ano, semestre, dataAula, usuario);
	}

	public void alterarProfessorTitularDisciplinaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception {
		if (turma.getTurmaAgrupada()) {
			getFacadeFactory().getTurmaFacade().carregarDados(turma, NivelMontarDados.TODOS, usuario);
			for (TurmaAgrupadaVO turmaAgrupada : turma.getTurmaAgrupadaVOs()) {
				incluirProfessorTitularDisciplinaTurmaEspecificaNaoAgrupada(turmaAgrupada.getTurma(), disciplina, professor, ano, semestre, dataAula, usuario);
			}
		}
		incluirProfessorTitularDisciplinaTurmaEspecificaNaoAgrupada(turma, disciplina, professor, ano, semestre, dataAula, usuario);
		// ProfessorTitularDisciplinaTurmaVO p = new
		// ProfessorTitularDisciplinaTurmaVO();
		// p.setTurma(turma);
		// p.getDisciplina().setCodigo(disciplina);
		// p.getProfessor().setCodigo(professor);
		// p.setAno(ano);
		// p.setSemestre(semestre);
		// p.setData(dataAula);
		// p.setTitular(true);
		// p.getResponsavelRegistro().setCodigo(getUsuarioLogado().getCodigo());
		// List l =
		// this.consultarPorProfessorTurmaDisciplinaAnoSemestre(professor,
		// turma.getCodigo(), disciplina, ano, semestre, false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		// if (l.isEmpty()) {
		// this.incluir(p);
		// } else {
		// ProfessorTitularDisciplinaTurmaVO prof =
		// (ProfessorTitularDisciplinaTurmaVO)l.get(0);
		// p.setCodigo(prof.getCodigo());
		// this.alterar(p);
		// }
		// p.setTitular(Boolean.FALSE);
		// alterarTodosProfessores(p);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProfessorTitularDisciplinaTurmaVO obj, final UsuarioVO usuario) throws Exception {
		try {
			ProfessorTitularDisciplinaTurmaVO.validarDados(obj);
			// verificarSeExisteProgramacaoAulaParaEssaTurma_e_Disciplina(obj);
			// ProfessorTitularDisciplinaTurma.incluir(getIdEntidade());

			final String sql = "INSERT INTO ProfessorTitularDisciplinaTurma( turma, disciplina, professor, semestre, ano, titular, disciplinaEquivalenteTurmaAgrupada, curso, dataCadastro, dataUltimaAlteracao, responsavel ) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					// sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (!obj.getTurma().getCodigo().equals(0)) {
						sqlInserir.setInt(1, obj.getTurma().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					// sqlInserir.setInt(3,
					// obj.getResponsavelRegistro().getCodigo().intValue());
					sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getProfessor().getPessoa().getCodigo().intValue());
					sqlInserir.setString(4, obj.getSemestre());
					sqlInserir.setString(5, obj.getAno());
					sqlInserir.setBoolean(6, obj.getTitular());
					if (!obj.getDisciplinaEquivalenteTurmaAgrupadaVO().getCodigo().equals(0)) {
						sqlInserir.setInt(7, obj.getDisciplinaEquivalenteTurmaAgrupadaVO().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (!obj.getCursoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(8, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(8, 0);
					}
					sqlInserir.setTimestamp(9, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setTimestamp(10, Uteis.getDataJDBCTimestamp(new Date()));
					if (!obj.getResponsavelVO().getCodigo().equals(0)) {
						sqlInserir.setInt(11, obj.getResponsavelVO().getCodigo());
					} else {
						sqlInserir.setNull(11, 0);
					}
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
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLog(obj, "inclusão", usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProfessorTitularDisciplinaTurmaVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProfessorTitularDisciplinaTurmaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProfessorTitularDisciplinaTurmaVO obj, final UsuarioVO usuario) throws Exception {
		try {
			ProfessorTitularDisciplinaTurmaVO.validarDados(obj);
			// verificarSeExisteProgramacaoAulaParaEssaTurma_e_Disciplina(obj);
			// ProfessorTitularDisciplinaTurma.alterar(getIdEntidade());

			final String sql = "UPDATE ProfessorTitularDisciplinaTurma set turma=?, disciplina=?, professor = ?, semestre=?, ano=?, titular=?, disciplinaEquivalenteTurmaAgrupada=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					// sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setInt(1, obj.getTurma().getCodigo().intValue());
					// sqlAlterar.setInt(3,
					// obj.getResponsavelRegistro().getCodigo().intValue());
					sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getProfessor().getPessoa().getCodigo().intValue());
					sqlAlterar.setString(4, obj.getSemestre());
					sqlAlterar.setString(5, obj.getAno());
					sqlAlterar.setBoolean(6, obj.getTitular());
					if (!obj.getDisciplinaEquivalenteTurmaAgrupadaVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(7, obj.getDisciplinaEquivalenteTurmaAgrupadaVO().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLog(obj, "alteração", usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinaEquivalenteTurmaAgrupada(final Integer disciplinaEquivalenteTurmaAgrupada, final Integer turma, final Integer professor, final Integer disciplina, final String ano, final String semestre, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE professortitulardisciplinaturma set disciplinaEquivalenteTurmaAgrupada=? WHERE turma=? and professor=? and disciplina=? and ano=? and semestre=?  and titular = true "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setInt(++i, disciplinaEquivalenteTurmaAgrupada.intValue());
					sqlAlterar.setInt(++i, turma.intValue());
					sqlAlterar.setInt(++i, professor.intValue());
					sqlAlterar.setInt(++i, disciplina.intValue());
					sqlAlterar.setString(++i, ano);
					sqlAlterar.setString(++i, semestre);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarListaProfessoresTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> professoresTitularDisciplinaTurma, String tipoDefinicaoProfessor,  UsuarioVO usuario) throws Exception {
		validarSeExisteApenasUmProfessorComoTitular(professoresTitularDisciplinaTurma);
		for (ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO : professoresTitularDisciplinaTurma) {
			if (professorTitularDisciplinaTurmaVO.isNovoObj() && professorTitularDisciplinaTurmaVO.getCodigo() == 0) {
//				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().incluir(professorTitularDisciplinaTurmaVO, usuario);
			} else {
				if (tipoDefinicaoProfessor.equals("TURMA")) {
					alterarTodosProfessoresTitularPorTurma(professorTitularDisciplinaTurmaVO, usuario);
				} else {
					alterarTodosProfessoresTitularPorCurso(professorTitularDisciplinaTurmaVO, usuario);
				}
			}
		}
	}

	public void validarSeExisteApenasUmProfessorComoTitular(List professoresTitularDisciplinaTurma) throws Exception {
		Iterator i = professoresTitularDisciplinaTurma.iterator();
		int cont = 0;
		while (i.hasNext()) {
			ProfessorTitularDisciplinaTurmaVO p = (ProfessorTitularDisciplinaTurmaVO) i.next();
			if (p.getTitular().booleanValue()) {
				cont++;
			}
		}
		if (cont >= 2) {
			throw new Exception("Deve Existir Apenas um Professor Selecionado como Titular!");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTodosProfessores(final ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, final UsuarioVO usuario) throws Exception {
		try {
			String sqlSTR = "UPDATE ProfessorTitularDisciplinaTurma set titular=? WHERE professorTitularDisciplinaTurma.turma = ? and professorTitularDisciplinaTurma.disciplina = ? and professorTitularDisciplinaTurma.professor != ?";
			if (!professorTitularDisciplinaTurmaVO.getAno().isEmpty()) {
				sqlSTR += " and professorTitularDisciplinaTurma.ano = ? ";
			}
			if (!professorTitularDisciplinaTurmaVO.getSemestre().isEmpty()) {
				sqlSTR += " and professorTitularDisciplinaTurma.semestre = ? ";
			}
			final String sql = sqlSTR + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, professorTitularDisciplinaTurmaVO.getTitular());
					sqlAlterar.setInt(2, professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue());
					sqlAlterar.setInt(3, professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue());
					sqlAlterar.setInt(4, professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getCodigo().intValue());
					if (!professorTitularDisciplinaTurmaVO.getAno().isEmpty()) {
						sqlAlterar.setString(5, professorTitularDisciplinaTurmaVO.getAno());
					}
					if (!professorTitularDisciplinaTurmaVO.getSemestre().isEmpty()) {
						sqlAlterar.setString(6, professorTitularDisciplinaTurmaVO.getSemestre());
					}
					return sqlAlterar;
				}
			});
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLog(professorTitularDisciplinaTurmaVO, "alteração", usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTodosProfessoresTitularPorTurma(final ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, final UsuarioVO usuario) throws Exception {
		try {
			String sqlSTR = "UPDATE ProfessorTitularDisciplinaTurma set titular=?, dataUltimaAlteracao=? WHERE professorTitularDisciplinaTurma.turma = ? and professorTitularDisciplinaTurma.disciplina = ? and professorTitularDisciplinaTurma.professor = ? ";

			if (!professorTitularDisciplinaTurmaVO.getAno().isEmpty()) {
				sqlSTR += " and professorTitularDisciplinaTurma.ano = ? ";
			}
			if (!professorTitularDisciplinaTurmaVO.getSemestre().isEmpty()) {
				sqlSTR += " and professorTitularDisciplinaTurma.semestre = ? ";
			}

			final String sql = sqlSTR;

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, professorTitularDisciplinaTurmaVO.getTitular());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setInt(3, professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue());
					sqlAlterar.setInt(4, professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue());
					sqlAlterar.setInt(5, professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getCodigo().intValue());
					if (!professorTitularDisciplinaTurmaVO.getAno().isEmpty()) {
						sqlAlterar.setString(6, professorTitularDisciplinaTurmaVO.getAno());
					}
					if (!professorTitularDisciplinaTurmaVO.getSemestre().isEmpty()) {
						sqlAlterar.setString(7, professorTitularDisciplinaTurmaVO.getSemestre());
					}
					return sqlAlterar;
				}
			});
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLog(professorTitularDisciplinaTurmaVO, "alteração", usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTodosProfessoresTitularPorCurso(final ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, final UsuarioVO usuario) throws Exception {
		try {
			String sqlSTR = "UPDATE ProfessorTitularDisciplinaTurma set titular=?, dataUltimaAlteracao=? WHERE professorTitularDisciplinaTurma.curso = ? and professorTitularDisciplinaTurma.disciplina = ? and professorTitularDisciplinaTurma.professor = ? ";

			if (!professorTitularDisciplinaTurmaVO.getAno().isEmpty()) {
				sqlSTR += " and professorTitularDisciplinaTurma.ano = ? ";
			}
			if (!professorTitularDisciplinaTurmaVO.getSemestre().isEmpty()) {
				sqlSTR += " and professorTitularDisciplinaTurma.semestre = ? ";
			}

			final String sql = sqlSTR;

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, professorTitularDisciplinaTurmaVO.getTitular());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setInt(3, professorTitularDisciplinaTurmaVO.getCursoVO().getCodigo().intValue());
					sqlAlterar.setInt(4, professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue());
					sqlAlterar.setInt(5, professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getCodigo().intValue());
					if (!professorTitularDisciplinaTurmaVO.getAno().isEmpty()) {
						sqlAlterar.setString(6, professorTitularDisciplinaTurmaVO.getAno());
					}
					if (!professorTitularDisciplinaTurmaVO.getSemestre().isEmpty()) {
						sqlAlterar.setString(7, professorTitularDisciplinaTurmaVO.getSemestre());
					}
					return sqlAlterar;
				}
			});
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLog(professorTitularDisciplinaTurmaVO, "alteração", usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarProfessorTitularDisciplinaTurmaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer disciplina, String ano, String semestre, Integer novaDisciplina, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("update ProfessorTitularDisciplinaTurma set ");
		sqlStr.append(" disciplina = ").append(novaDisciplina).append(" ");
		sqlStr.append(" where turma =").append(turma).append(" ");
		sqlStr.append(" and disciplina = ").append(disciplina).append(" ");
		if(Uteis.isAtributoPreenchido(ano)){
			sqlStr.append(" and ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)){
			sqlStr.append(" and semestre = '").append(semestre).append("' ");
		}
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProfessorTitularDisciplinaTurmaVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProfessorTitularDisciplinaTurmaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProfessorTitularDisciplinaTurmaVO obj, UsuarioVO usuario) throws Exception {
		try {
			ProfessorTitularDisciplinaTurma.excluir(getIdEntidade());
			String sql = "DELETE FROM ProfessorTitularDisciplinaTurma WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			//getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulas(obj.getCodigo(), usuario);
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLogOperacaoExclusao(obj, "exclusão", usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCodigoDisciplinaTurma(Integer turma, Integer disciplina,String ano, String semestre, UsuarioVO usuario) throws Exception {
		TurmaDisciplina.excluir(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder("DELETE FROM ProfessorTitularDisciplinaTurma  ");
		sqlStr.append(" where turma =").append(turma).append(" ");
		sqlStr.append(" and disciplina = ").append(disciplina).append(" ");
		if(Uteis.isAtributoPreenchido(ano)){
			sqlStr.append(" and ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)){
			sqlStr.append(" and semestre = '").append(semestre).append("' ");
		}
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	public List montarProfessoresTitularDisciplinaTurma(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select * from professorTitularDisciplinaTurma where professorTitularDisciplinaTurma.turma = " + professorTitularDisciplinaTurmaVO.getTurma().getCodigo() + " and professorTitularDisciplinaTurma.disciplina = " + professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo();
		if (!professorTitularDisciplinaTurmaVO.getAno().isEmpty()) {
			sqlStr += " and professorTitularDisciplinaTurma.semestre = '" + professorTitularDisciplinaTurmaVO.getSemestre() + "' ";
		}
		if (!professorTitularDisciplinaTurmaVO.getSemestre().isEmpty()) {
			sqlStr += " and professorTitularDisciplinaTurma.ano = '" + professorTitularDisciplinaTurmaVO.getAno() + "' ";
		}
		sqlStr += " ORDER BY professorTitularDisciplinaTurma.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public ProfessorTitularDisciplinaTurmaVO montarProfessoresTitularDisciplinaTurmaTitular(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select * from professorTitularDisciplinaTurma where professorTitularDisciplinaTurma.turma = ? and professorTitularDisciplinaTurma.disciplina = ? ";
		sqlStr += " and professorTitularDisciplinaTurma.semestre = ? ";
		sqlStr += " and professorTitularDisciplinaTurma.ano = ? ";
		sqlStr += " and professorTitularDisciplinaTurma.titular = true";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue(), professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue(), professorTitularDisciplinaTurmaVO.getSemestre(), professorTitularDisciplinaTurmaVO.getAno() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Não foi possivel localizar o professor titular da turma!");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ProfessorTitularDisciplinaTurmaVO montarProfessoresTitularDisciplinaTurmaAgrupadaTitular(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, Boolean liberarRegistroAulaEntrePeriodo, Boolean retornarExcessao, int nivelMontarDados, String matricula, UsuarioVO usuario) throws Exception {
		// Busca o professor na turma origem
		StringBuilder sqlStr = new StringBuilder("");
		SqlRowSet tabelaResultado = null;
		sqlStr.append("select * from professorTitularDisciplinaTurma ");
		sqlStr.append("where professorTitularDisciplinaTurma.turma = ? ");
		sqlStr.append("and professorTitularDisciplinaTurma.disciplina = ? ");
		if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty() && !professorTitularDisciplinaTurmaVO.getSemestre().trim().isEmpty()){
			sqlStr.append(" and professorTitularDisciplinaTurma.semestre = '").append(professorTitularDisciplinaTurmaVO.getSemestre()).append("' ");
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("' ");
		}else if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty()){			
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("' ");
		}			
		sqlStr.append(" and professorTitularDisciplinaTurma.titular = true");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue(), professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue() });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}

		// Busca o professor na turma agrupada	
		sqlStr = new StringBuilder("");
		sqlStr.append(" select * from professorTitularDisciplinaTurma  ");
		sqlStr.append(" where (professorTitularDisciplinaTurma.turma in ( select turmaorigem from turmaagrupada where turma = "+professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue()+"))");
		sqlStr.append(" and (professorTitularDisciplinaTurma.disciplina = "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue()+" or professorTitularDisciplinaTurma.disciplina in ( ");
		sqlStr.append(" select equivalente from disciplinaequivalente where disciplina = ").append(professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue());
		sqlStr.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue());
		sqlStr.append(" )) ");
		if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty() && !professorTitularDisciplinaTurmaVO.getSemestre().trim().isEmpty()){
			sqlStr.append(" and professorTitularDisciplinaTurma.semestre = '").append(professorTitularDisciplinaTurmaVO.getSemestre()).append("' ");
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("' ");
		}else if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty()){			
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("' ");
		}			
		sqlStr.append(" and professorTitularDisciplinaTurma.titular = true ");			
		sqlStr.append(" order by exists(select horarioturma.codigo from horarioturma ");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append(" and horarioturma.turma = professorTitularDisciplinaTurma.turma ");
		sqlStr.append(" and horarioturma.anovigente = professorTitularDisciplinaTurma.ano ");
		sqlStr.append(" and horarioturma.semestrevigente = professorTitularDisciplinaTurma.semestre ");
		sqlStr.append(" and horarioturmadiaitem.disciplina = professorTitularDisciplinaTurma.disciplina limit 1 ) desc");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}			
		
		// Busca o professor na programacao de aula da turma de origem
		sqlStr = new StringBuilder("select 0 as codigo, horarioturma.turma, ht.professor,  ht.disciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, true as titular ");
		if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty() && !professorTitularDisciplinaTurmaVO.getSemestre().trim().isEmpty()){
			sqlStr.append(" from horarioturmadetalhado( null," + professorTitularDisciplinaTurmaVO.getTurma().getCodigo() + ",'" + professorTitularDisciplinaTurmaVO.getAno() + "','" +  professorTitularDisciplinaTurmaVO.getSemestre() + "', null, "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo()+", null, null) as ht ");
		}else if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty()){
			sqlStr.append(" from horarioturmadetalhado( null," + professorTitularDisciplinaTurmaVO.getTurma().getCodigo() + ",'" + professorTitularDisciplinaTurmaVO.getAno() + "', null, null, "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo()+", null, null) as ht ");
		}else{
			sqlStr.append(" from horarioturmadetalhado( null," + professorTitularDisciplinaTurmaVO.getTurma().getCodigo() + ", null , null, null, "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo()+", null, null) as ht ");
		}		
		sqlStr.append(" inner join horarioturma on horarioturma.codigo = ht.horarioturma ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = ht.professor ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = ht.disciplina ");
		if (!liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" where horarioturma.semestrevigente = '").append(professorTitularDisciplinaTurmaVO.getSemestre()).append("' ");
			sqlStr.append(" and horarioturma.anovigente = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("' ");
		}
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		sqlStr = new StringBuilder("");
		//Busca o professor na programação tutoria on-line
		sqlStr.append(" select 0 as codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.professor,  matriculaperiodoturmadisciplina.disciplina, matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, true as titular");
		sqlStr.append(" from matriculaperiodoturmadisciplina");
		sqlStr.append(" where matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(professorTitularDisciplinaTurmaVO.getTurma().getCodigo());
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo());
		sqlStr.append(" and matriculaperiodoturmadisciplina.ano = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = '").append(professorTitularDisciplinaTurmaVO.getSemestre()).append("'");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		sqlStr = new StringBuilder("");
		StringBuilder sqlStr2 = new StringBuilder("");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select turmaorigem from turmaagrupada where turma = " + professorTitularDisciplinaTurmaVO.getTurma().getCodigo());
		while (rs.next()) {
			Integer turmaOrigem = rs.getInt("turmaorigem");
			if(!sqlStr.toString().trim().isEmpty()){
				sqlStr.append(" union ");
				sqlStr2.append(" union ");
			}
			/**
			 * Traz a disciplina programada para o professor na turma agrupada
			 * onde a disciplina programada é a mesma da turma normal
			 */
			sqlStr = new StringBuilder("select distinct 0 as codigo, horarioturma.turma, ht.professor,  ht.disciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, true as titular ");
			if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty() && !professorTitularDisciplinaTurmaVO.getSemestre().trim().isEmpty()){
				sqlStr.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + professorTitularDisciplinaTurmaVO.getAno() + "','" +  professorTitularDisciplinaTurmaVO.getSemestre() + "', null, "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo()+", null, null) as ht ");
			}else if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty()){
				sqlStr.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + professorTitularDisciplinaTurmaVO.getAno() + "', null, null, "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo()+", null, null) as ht ");
			}else{
				sqlStr.append(" from horarioturmadetalhado( null," + turmaOrigem + ", null , null, null, "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo()+", null, null) as ht ");
			}	
			sqlStr.append(" inner join horarioturma on horarioturma.codigo = ht.horarioturma ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = ht.professor ");
			sqlStr.append(" inner join disciplina on disciplina.codigo = ht.disciplina ");
			
			/**
			 * Traz a disciplina programada para o professor na turma agrupada
			 * onde a disciplina programada é uma equivalente da disciplina da
			 * turma normal
			 */
			
			sqlStr2 = new StringBuilder("select distinct 0 as codigo, horarioturma.turma, ht.professor, disciplina.codigo as disciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, true as titular ");
			if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty() && !professorTitularDisciplinaTurmaVO.getSemestre().trim().isEmpty()){
				sqlStr2.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + professorTitularDisciplinaTurmaVO.getAno() + "','" +  professorTitularDisciplinaTurmaVO.getSemestre() + "', null, null, null, null) as ht ");
			}else if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty()){
				sqlStr2.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + professorTitularDisciplinaTurmaVO.getAno() + "', null, null, null, null, null) as ht ");
			}else{
				sqlStr2.append(" from horarioturmadetalhado( null," + turmaOrigem + ", null , null, null, null, null, null) as ht ");
			}	
			sqlStr2.append(" inner join horarioturma on horarioturma.codigo = ht.horarioturma ");
			sqlStr2.append(" inner join pessoa on pessoa.codigo = ht.professor ");
			sqlStr2.append(" inner join turma on turma.codigo = ht.turma ");
			sqlStr2.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = ht.disciplina ");
			sqlStr2.append(" inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente ");
			sqlStr2.append(" where disciplina.codigo = ").append(professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo()).append(" ");
		}
		if(!sqlStr.toString().trim().isEmpty()){
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return (montarDados(tabelaResultado, nivelMontarDados, usuario));
			}
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr2.toString());
			if (tabelaResultado.next()) {
				return (montarDados(tabelaResultado, nivelMontarDados, usuario));
			}
		}
		
		sqlStr = new StringBuilder("");
		//Busca o professor na programação tutoria on-line
		sqlStr.append(" SELECT DISTINCT 0 AS codigo, turma, professor, disciplina, TRUE AS titular, null as ano, null as semestre");
		sqlStr.append(" FROM programacaotutoriaonline");
		sqlStr.append(" INNER JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonline.codigo = programacaotutoriaonlineprofessor.programacaotutoriaonline");
		sqlStr.append(" WHERE programacaotutoriaonline.disciplina = ").append(professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo());
		sqlStr.append(" AND programacaotutoriaonline.turma = ").append(professorTitularDisciplinaTurmaVO.getTurma().getCodigo());
		sqlStr.append(" and programacaotutoriaonlineprofessor.situacaoprogramacaotutoriaonline = 'ATIVO'");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		
		// Busca em subturma pois nao encontrou nenhum professor na turma principal e nem nas consultas anteriores - chamado 18037
		sqlStr = new StringBuilder("");
		sqlStr.append(" select * from professorTitularDisciplinaTurma  ");
		sqlStr.append(" where (professorTitularDisciplinaTurma.turma in (");
		sqlStr.append("	select turma.codigo from turma  inner join turmadisciplina on turmadisciplina.turma = turma.codigo inner join disciplina on turmadisciplina.disciplina = disciplina.codigo  ");
		sqlStr.append("	where turma.subturma and turma.turmaPrincipal = ").append(professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue()).append(" group by turma.codigo  union all  ");
		sqlStr.append("	select turma.codigo from turma  inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = ").append(professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue()).append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append("	inner join turmadisciplina td2 on td2.turma = ").append(professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue()).append(" and (td2.disciplina = turmadisciplina.disciplina or td2.disciplina = turmadisciplina.disciplinaEquivalenteTurmaAgrupada) ");
		sqlStr.append("	inner join disciplina on turmadisciplina.disciplina = disciplina.codigo  inner join turma as turmaPrincipal on turmaPrincipal.codigo = turma.turmaPrincipal  ");
		sqlStr.append("	where turma.turmaagrupada and turma.subturma  group by turma.codigo");
		sqlStr.append(")) ");
		sqlStr.append(" and (professorTitularDisciplinaTurma.disciplina = "+professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue()+" or professorTitularDisciplinaTurma.disciplina in ( ");
		sqlStr.append(" select equivalente from disciplinaequivalente where disciplina = ").append(professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue());
		sqlStr.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo().intValue());
		sqlStr.append(" )) ");
		if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty() && !professorTitularDisciplinaTurmaVO.getSemestre().trim().isEmpty()){
			sqlStr.append(" and professorTitularDisciplinaTurma.semestre = '").append(professorTitularDisciplinaTurmaVO.getSemestre()).append("' ");
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("' ");
		}else if(!professorTitularDisciplinaTurmaVO.getAno().trim().isEmpty()){			
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(professorTitularDisciplinaTurmaVO.getAno()).append("' ");
		}			
		sqlStr.append(" and professorTitularDisciplinaTurma.titular = true ");			
		sqlStr.append(" order by exists(select horarioturma.codigo from horarioturma ");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append(" and horarioturma.turma = professorTitularDisciplinaTurma.turma ");
		sqlStr.append(" and horarioturma.anovigente = professorTitularDisciplinaTurma.ano ");
		sqlStr.append(" and horarioturma.semestrevigente = professorTitularDisciplinaTurma.semestre ");
		sqlStr.append(" and horarioturmadiaitem.disciplina = professorTitularDisciplinaTurma.disciplina limit 1 ) desc");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		
		if (retornarExcessao) {
			throw new ConsistirException("Não foi possivel localizar o professor titular da turma!");
		} else {
			return null;
		}
	}

	public List consultarPorProfessorTurmaDisciplinaAnoSemestre(Integer professor, Integer turma, Integer disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select * from professorTitularDisciplinaTurma where professorTitularDisciplinaTurma.turma = " + turma + " and professorTitularDisciplinaTurma.disciplina = " + disciplina + " and professorTitularDisciplinaTurma.professor = " + professor;
		if (!ano.isEmpty()) {
			sqlStr += " and professorTitularDisciplinaTurma.semestre = '" + semestre + "' ";
		}
		if (!semestre.isEmpty()) {
			sqlStr += " and professorTitularDisciplinaTurma.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY professorTitularDisciplinaTurma.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<PessoaVO> montarListaProfessorRegistroAula( ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, UsuarioVO usuario) {
		List<PessoaVO> listaProfessor = new ArrayList<PessoaVO>(0);
		try {		
//			List<Integer> professores = getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina( professorTitularDisciplinaTurmaVO.getDisciplina().getCodigo());
//			for (Integer professor : professores) {
//				adicionarProfessor(professor, listaProfessor, usuario);
//			}

		} catch (Exception e) {
			////System.out.println("Erro professorTitularDisciplinaTurma.montarListaProfessorRegistroAula: " + e.getMessage());
		}
		return listaProfessor;
	}

	private void adicionarProfessor(Integer codigoProfessor, List<PessoaVO> listaProfessor, UsuarioVO usuario) throws Exception {
		Iterator i = listaProfessor.iterator();
		while (i.hasNext()) {
			PessoaVO item = (PessoaVO) i.next();
			if (item.getCodigo().equals(codigoProfessor)) {
				return;
			}
		}
		PessoaVO professor = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigoProfessor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		listaProfessor.add(professor);
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>codigo</code> da classe
	 * <code>ProgramacaoAula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ProfessorTitularDisciplinaTurmaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoTurma(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = turma.codigo and turma.codigo >= " + valorConsulta.intValue() + " ";

		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoDisciplina(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina WHERE RegistroAula.disciplina = disciplina.codigo and disciplina.codigo = " + valorConsulta.intValue() + " ";
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY disciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoDisciplinaCodigoTurma(Integer valorConsulta, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Turma WHERE RegistroAula.disciplina = disciplina.codigo " + " and disciplina.codigo = " + valorConsulta.intValue() + " and RegistroAula.turma = turma.codigo and turma.codigo = " + turma.intValue() + " ";
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY disciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeDisciplina(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina WHERE RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Turma WHERE RegistroAula.turma = Turma.codigo and RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY disciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeDisciplinaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Pessoa WHERE RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " and RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Pessoa, Turma WHERE RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " and RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') and RegistroAula.turma = turma.codigo and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY disciplina.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeProfessor(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Pessoa WHERE RegistroAula.professor = pessoa.codigo and lower (pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Pessoa WHERE RegistroAula.professor = pessoa.codigo and lower (pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') and RegistroAula.turma = turma.codigo and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY pessoa.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeCurso(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Curso WHERE RegistroAula.turma = Turma.codigo and turma.curso = curso.codigo and lower (curso.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}

		sqlStr += " ORDER BY RegistroAula.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeCursoProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Curso, Pessoa WHERE RegistroAula.turma = Turma.codigo and turma.curso = curso.codigo and lower (curso.nome) like('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " ";

		sqlStr += " ORDER BY RegistroAula.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorIdentificadorTurma(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase() + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase() + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorIdentificadorTurmaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Pessoa WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase() + "%') " + "  and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Pessoa WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase() + "%') and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorIdentificadorTurmaProfessorDisciplina(String valorConsulta, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) = '" + valorConsulta.toLowerCase() + "' " + " and RegistroAula.disciplina = " + disciplina.intValue();
		// if ((professor != null) && (professor != 0)) {
		// sqlStr += " and RegistroAula.professor = " + professor.intValue();
		// }
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and Turma.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY registroaula.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>String conteudo</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ProfessorTitularDisciplinaTurmaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorConteudo(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE conteudo like('" + valorConsulta + "%') ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and conteudo like('" + valorConsulta + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY conteudo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>Integer cargaHoraria</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ProfessorTitularDisciplinaTurmaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCargaHoraria(Integer valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE cargaHoraria >= " + valorConsulta.intValue() + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and cargaHoraria >= " + valorConsulta.intValue() + " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY cargaHoraria";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ProfessorTitularDisciplinaTurmaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM RegistroAula, Turma  WHERE RegistroAula.turma = Turma.codigo and ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public ProfessorTitularDisciplinaTurmaVO consultarUltimoRegistroAulaPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT registroaula.* ");
		sqlStr.append("FROM matricula ");
		sqlStr.append("INNER JOIN matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
		sqlStr.append("INNER JOIN registroaula ON (matriculaperiodo.turma = registroaula.turma AND matriculaperiodo.ano = registroaula.ano AND matriculaperiodo.semestre = registroaula.semestre) ");
		sqlStr.append("WHERE matricula.matricula like'").append(matricula).append("' ");
		sqlStr.append("Order BY registroaula.data ");
		sqlStr.append("limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		throw new ConsistirException("Não existe nenhum registro de aula para esse aluno no curso escolhido.");
	}

	public List consultarPorDataProfessor(Date prmIni, Date prmFim, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RegistroAula.* FROM RegistroAula , Pessoa WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) " + "  and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Pessoa WHERE RegistroAula.turma = Turma.codigo and ((RegistroAula.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (RegistroAula.data <= '" + Uteis.getDataJDBC(prmFim) + "')) and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY Pessoa.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	// public boolean existeRegistroAula(ProfessorTitularDisciplinaTurmaVO ra)
	// throws Exception {
	// String sql =
	// "SELECT dataregistroaula, responsavelregistroaula, turma, conteudo, " +
	// "cargahoraria, data, codigo, disciplina, diaSemana, tipoaula " +
	// "FROM registroaula " + "WHERE data = ? "
	// + "AND turma = ? " + "AND disciplina = ? and diaSemana = ? " +
	// "AND codigo != ?";
	// SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql,
	// new Object[]{Uteis.getDataJDBC(ra.getData()),
	// ra.getTurma().getCodigo().intValue(),
	// ra.getDisciplina().getCodigo().intValue(), ra.getDiaSemana(),
	// ra.getCodigo().intValue()});
	// if (rs.next()) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	/**
	 * Responsável por realizar uma consulta de <code>RegistroAula</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ProfessorTitularDisciplinaTurmaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RegistroAula WHERE codigo >= " + valorConsulta.intValue() + " ";
		if (!semestre.equals("")) {
			sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
		}

		if (!ano.equals("")) {
			sqlStr += " and RegistroAula.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ProfessorTitularDisciplinaTurmaVO</code> resultantes da
	 *         consulta.
	 */
	public  List<ProfessorTitularDisciplinaTurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ProfessorTitularDisciplinaTurmaVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ProfessorTitularDisciplinaTurmaVO</code>.
	 * 
	 * @return O objeto da classe <code>ProfessorTitularDisciplinaTurmaVO</code>
	 *         com os dados devidamente montados.
	 */
	public  ProfessorTitularDisciplinaTurmaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProfessorTitularDisciplinaTurmaVO obj = new ProfessorTitularDisciplinaTurmaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		// obj.setData(dadosSQL.getDate("data"));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		// obj.getResponsavelRegistro().setCodigo(new
		// Integer(dadosSQL.getInt("responsavelRegistro")));
		obj.getProfessor().getPessoa().setCodigo(new Integer(dadosSQL.getInt("professor")));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setTitular(dadosSQL.getBoolean("titular"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosRapidoProfessor(obj, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosRapidoProfessor(obj, usuario);
			montarDadosTurma(obj, nivelMontarDados, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}
		montarDadosTurma(obj, nivelMontarDados, usuario);
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// montarDadosResponsavelRegistroAula(obj,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ProgramacaoAulaVO</code> relacionado ao objeto
	 * <code>ProfessorTitularDisciplinaTurmaVO</code>. Faz uso da chave primária
	 * da classe <code>ProgramacaoAulaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosTurma(ProfessorTitularDisciplinaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			obj.setTurma(new TurmaVO());
			return;
		}
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosProfessor(ProfessorTitularDisciplinaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProfessor().getPessoa().getCodigo().intValue() == 0) {
			obj.setProfessor(new FuncionarioVO());
			return;
		}
		obj.setProfessor(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getProfessor().getPessoa().getCodigo(), 0, false, nivelMontarDados, usuario));
	}

	public  void montarDadosRapidoProfessor(ProfessorTitularDisciplinaTurmaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getProfessor().getPessoa().getCodigo().intValue() == 0) {
			obj.setProfessor(new FuncionarioVO());
			return;
		}
		obj.setProfessor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(obj.getProfessor().getPessoa().getCodigo(), false, usuario));
	}

	public  void montarDadosDisciplina(ProfessorTitularDisciplinaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ProfessorTitularDisciplinaTurmaVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ProfessorTitularDisciplinaTurmaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ProfessorTitularDisciplinaTurma WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (ProfessorTitularDisciplinaTurma).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ProfessorTitularDisciplinaTurmaVO> montarProfessoresComProgramacaoAulaDisciplinaTurma(Integer codTurma, Integer codCurso, Integer codDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "";
		if (Uteis.isAtributoPreenchido(codTurma)) {
			sqlStr = "select * from ProfessorTitularDisciplinaTurma where ProfessorTitularDisciplinaTurma.turma = " + codTurma + " and ProfessorTitularDisciplinaTurma.disciplina = " + codDisciplina;
		} else {
			sqlStr = "select * from ProfessorTitularDisciplinaTurma where ProfessorTitularDisciplinaTurma.curso = " + codCurso + " and ProfessorTitularDisciplinaTurma.disciplina = " + codDisciplina;
		}
		if (semestre != null) {
			sqlStr += " and ProfessorTitularDisciplinaTurma.semestre = '" + semestre + "' ";
		}
		if (ano != null) {
			sqlStr += " and ProfessorTitularDisciplinaTurma.ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY ProfessorTitularDisciplinaTurma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<ProfessorTitularDisciplinaTurmaVO> professorTitularDisciplinaTurmaVOs = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		montarCursoProfessorTitularDisciplinaTurma(professorTitularDisciplinaTurmaVOs, codCurso);
		return professorTitularDisciplinaTurmaVOs;
	}

	public ProfessorTitularDisciplinaTurmaVO consultarPorProfessorDisciplinaTurmaAnoSemestre(Integer codProfessor, Integer codDisciplina, Integer codTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ProfessorTitularDisciplinaTurma WHERE professor = ? AND disciplina = ? AND turma = ? AND semestre = ? AND ano = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codProfessor, codDisciplina, codTurma, semestre, ano });
		if (!tabelaResultado.next()) {
			return new ProfessorTitularDisciplinaTurmaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ProfessorTitularDisciplinaTurmaVO consultarPorDisciplinaTurmaAnoSemestre(Integer codDisciplina, Integer codTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ProfessorTitularDisciplinaTurma WHERE disciplina = ? AND turma = ? AND semestre = ? AND ano = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codDisciplina, codTurma, semestre, ano });
		if (!tabelaResultado.next()) {
			return new ProfessorTitularDisciplinaTurmaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirProfessorTitularDisciplinaTurma(PessoaVO professorVO, DisciplinaVO disciplinaVO, TurmaVO turmaVO, UsuarioVO usuario) throws Exception {
		incluirProfessorTitularDisciplinaTurma(professorVO, disciplinaVO, turmaVO, "", "", usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirProfessorTitularDisciplinaTurma(PessoaVO professorVO, DisciplinaVO disciplinaVO, TurmaVO turmaVO, String semestre, String ano, UsuarioVO usuario) throws Exception {
		ProfessorTitularDisciplinaTurmaVO obj = consultarPorDisciplinaTurmaAnoSemestre(disciplinaVO.getCodigo(), turmaVO.getCodigo(), ano, semestre, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
		if (obj.getCodigo().equals(0)) {
			obj.setTurma(turmaVO);
			obj.setDisciplina(disciplinaVO);
			obj.getProfessor().setPessoa(professorVO);
			obj.setAno(ano);
			obj.setSemestre(semestre);
			obj.setTitular(true);
			incluir(obj, usuario);
		} else {
			ProfessorTitularDisciplinaTurmaVO profTitular = consultarProfessorTitularTurma(turmaVO, disciplinaVO.getCodigo(), ano, semestre, false, usuario);
			obj = consultarPorProfessorDisciplinaTurmaAnoSemestre(professorVO.getCodigo(), disciplinaVO.getCodigo(), turmaVO.getCodigo(), ano, semestre, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
			if (obj.getCodigo().equals(0)) {
				obj.setTurma(turmaVO);
				obj.setDisciplina(disciplinaVO);
				obj.getProfessor().setPessoa(professorVO);
				obj.setAno(ano);
				obj.setSemestre(semestre);
				if(!Uteis.isAtributoPreenchido(profTitular) || profTitular.getProfessor().getPessoa().getCodigo().equals(professorVO.getCodigo())) {
					obj.setTitular(true);					
				}else {
					obj.setTitular(false);
				}
				incluir(obj, usuario);
				if(!Uteis.isAtributoPreenchido(profTitular) || profTitular.getProfessor().getPessoa().getCodigo().equals(professorVO.getCodigo())) {
					obj.setTitular(false);
					alterarTodosProfessores(obj, usuario);
				}
			} else {
				if(!Uteis.isAtributoPreenchido(profTitular) || profTitular.getProfessor().getPessoa().getCodigo().equals(professorVO.getCodigo())) {
					obj.setTitular(true);					
				}else {
					obj.setTitular(false);
				}
				alterar(obj, usuario);
				if(!Uteis.isAtributoPreenchido(profTitular) || profTitular.getProfessor().getPessoa().getCodigo().equals(professorVO.getCodigo())) {
					obj.setTitular(false);
					alterarTodosProfessores(obj, usuario);
				}
			}
			// else if(!obj.getTitular()){
			// obj.setTitular(true);
			// alterar(obj);
			// obj.setTitular(false);
			// alterarTodosProfessores(obj);
			// }
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAula(Integer turma, Integer professor, Integer disciplina, String semestre, String ano, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM professortitulardisciplinaturma WHERE turma = ").append(turma).append(" AND disciplina = ").append(disciplina);
		sqlStr.append(" AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append("AND professor = ").append(professor).append(" AND not exists ( ");
		sqlStr.append(" SELECT distinct turma FROM horarioturma ht ");
		sqlStr.append(" LEFT JOIN horarioturmadia htd ON ht.codigo = htd.horarioturma ");
		sqlStr.append(" LEFT JOIN horarioturmadiaitem htdi ON htd.codigo = htdi.horarioturmadia ");
		sqlStr.append(" WHERE ht.turma = ").append(turma).append(" AND ht.semestrevigente = '").append(semestre).append("' AND ht.anovigente = '").append(ano).append("' ");
		sqlStr.append(" AND htdi.professor = ").append(professor);
		sqlStr.append(" AND htdi.disciplina = ").append(disciplina);
		sqlStr.append(" ) ");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
			ProfessorTitularDisciplinaTurmaVO obj = new ProfessorTitularDisciplinaTurmaVO();
			obj.getTurma().setCodigo(turma);
			obj.getProfessor().setCodigo(professor);
			obj.getDisciplina().setCodigo(disciplina);
			obj.setSemestre(semestre);
			obj.setAno(ano);
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLogOperacaoExclusao(obj, "exclusão", usuario);
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaProgramacaoAulaSemDisciplina(Integer turma, Integer professor, String semestre, String ano, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM professortitulardisciplinaturma WHERE turma = ").append(turma).append(" ");
		sqlStr.append("AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		sqlStr.append("AND professor = ").append(professor).append(" ");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
			ProfessorTitularDisciplinaTurmaVO obj = new ProfessorTitularDisciplinaTurmaVO();
			obj.getTurma().setCodigo(turma);
			obj.getProfessor().setCodigo(professor);
			obj.setSemestre(semestre);
			obj.setAno(ano);
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLogOperacaoExclusao(obj, "exclusão", usuario);
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTodosRegistrosTurmaComBaseNaProgramacaoAula(Integer turma, String semestre, String ano, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM professortitulardisciplinaturma WHERE turma = ").append(turma).append(" ");
		sqlStr.append("AND semestre = '").append(semestre).append("' AND ano = '").append(ano).append("' ");
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
			ProfessorTitularDisciplinaTurmaVO obj = new ProfessorTitularDisciplinaTurmaVO();
			obj.getTurma().setCodigo(turma);
			obj.setSemestre(semestre);
			obj.setAno(ano);
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaLogFacade().preencherProfessorTitularDisciplinaTurmaLogOperacaoExclusao(obj, "exclusão", usuario);
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}
	
	public ProfessorTitularDisciplinaTurmaVO executarObterDadosProfessorTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores) throws Exception {
		try {
			if (listaProfessores != null && !listaProfessores.isEmpty()) {
				for (ProfessorTitularDisciplinaTurmaVO professor : listaProfessores) {
					if (professor.getTitular()) {
						return professor;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ProfessorTitularDisciplinaTurma.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProfessorTitularDisciplinaTurma.idEntidade = idEntidade;
	}
	
	public ProfessorTitularDisciplinaTurmaVO consultarProfessorTitularTurma(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean retornarExcessao, UsuarioVO usuario) throws Exception {
		String semestrePrm = "";
		String anoPrm = "";
		if (turmaVO.getAnual() || turmaVO.getSemestral()) {
			anoPrm = ano;
			semestrePrm = "";
		}
		if (turmaVO.getSemestral()) {
			semestrePrm = semestre;
			anoPrm = ano;
		}
		ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
		p.setAno(anoPrm);
		p.setSemestre(semestrePrm);
		p.setTurma(turmaVO);
		p.getDisciplina().setCodigo(disciplina);
		// p =
		// getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().montarProfessoresMinistrouAulaTurmaTitular(p,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		// p =
		// getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaTitular(p,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//		p = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaAgrupadaTitular(p, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), retornarExcessao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, "", usuario);
		return null;
	}
	
	
	public List<ProfessorTitularDisciplinaTurmaVO> consultarProfessoresDisciplinaTurma(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean retornarExcessao, Boolean liberarRegistroAulaEntrePeriodo, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String semestrePrm = "";
		String anoPrm = "";
		if (turmaVO.getAnual() || turmaVO.getSemestral()) {
			anoPrm = ano;
			semestrePrm = "";
		}
		if (turmaVO.getSemestral()) {
			semestrePrm = semestre;
			anoPrm = ano;
		}
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessorTitularDisciplinaTurmaVOs = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		HashMap<Integer, ProfessorTitularDisciplinaTurmaVO> mapProfessorDisciplinaTurmaVOs = new HashMap<Integer, ProfessorTitularDisciplinaTurmaVO>(0);
		// Busca o professor na turma origem
		StringBuilder sqlStr = new StringBuilder("");
		SqlRowSet tabelaResultado = null;
		sqlStr.append("select * from professorTitularDisciplinaTurma ");
		sqlStr.append("where professorTitularDisciplinaTurma.turma = ? ");
		sqlStr.append("and professorTitularDisciplinaTurma.disciplina = ? ");
		if(!anoPrm.trim().isEmpty() && !semestrePrm.trim().isEmpty()){
			sqlStr.append(" and professorTitularDisciplinaTurma.semestre = '").append(semestrePrm).append("' ");
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(anoPrm).append("' ");
		}else if(!anoPrm.trim().isEmpty()){			
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(anoPrm).append("' ");
		}			
		sqlStr.append(" and professorTitularDisciplinaTurma.titular = true");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { turmaVO.getCodigo().intValue(), disciplina.intValue() });
		while (tabelaResultado.next()) {
			ProfessorTitularDisciplinaTurmaVO obj = (montarDados(tabelaResultado, nivelMontarDados, usuario));
			if (!mapProfessorDisciplinaTurmaVOs.containsKey(obj.getProfessor().getCodigo())) {
				obj.setOrdemApresentacao(1);
				mapProfessorDisciplinaTurmaVOs.put(obj.getProfessor().getCodigo(), obj);
				listaProfessorTitularDisciplinaTurmaVOs.add(obj);
			}
		}

		// Busca o professor na turma agrupada	
		sqlStr = new StringBuilder("");
		sqlStr.append(" select * from professorTitularDisciplinaTurma  ");
		sqlStr.append(" where (professorTitularDisciplinaTurma.turma in ( select turmaorigem from turmaagrupada where turma = "+turmaVO.getCodigo().intValue()+"))");
		sqlStr.append(" and (professorTitularDisciplinaTurma.disciplina = "+disciplina.intValue()+" or professorTitularDisciplinaTurma.disciplina in ( ");
		sqlStr.append(" select equivalente from disciplinaequivalente where disciplina = ").append(disciplina.intValue());
		sqlStr.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(disciplina.intValue());
		sqlStr.append(" )) ");
		if(!anoPrm.trim().isEmpty() && !semestrePrm.trim().isEmpty()){
			sqlStr.append(" and professorTitularDisciplinaTurma.semestre = '").append(semestrePrm).append("' ");
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(anoPrm).append("' ");
		}else if(!anoPrm.trim().isEmpty()){			
			sqlStr.append(" and professorTitularDisciplinaTurma.ano = '").append(anoPrm).append("' ");
		}			
		sqlStr.append(" and professorTitularDisciplinaTurma.titular = true ");					
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			ProfessorTitularDisciplinaTurmaVO obj = (montarDados(tabelaResultado, nivelMontarDados, usuario));
			if (!mapProfessorDisciplinaTurmaVOs.containsKey(obj.getProfessor().getCodigo())) {
				obj.setOrdemApresentacao(2);
				mapProfessorDisciplinaTurmaVOs.put(obj.getProfessor().getCodigo(), obj);
				listaProfessorTitularDisciplinaTurmaVOs.add(obj);
			}
		}			
		
		// Busca o professor na programacao de aula da turma de origem
		sqlStr = new StringBuilder("select distinct 0 as codigo, horarioturma.turma, ht.professor,  ht.disciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, true as titular ");
		if(!anoPrm.trim().isEmpty() && !semestrePrm.trim().isEmpty()){
			sqlStr.append(" from horarioturmadetalhado( null," + turmaVO.getCodigo() + ",'" + anoPrm + "','" +  semestrePrm + "', null, "+disciplina+", null, null) as ht ");
		}else if(!anoPrm.trim().isEmpty()){
			sqlStr.append(" from horarioturmadetalhado( null," + turmaVO.getCodigo() + ",'" + anoPrm + "', null, null, "+disciplina+", null, null) as ht ");
		}else{
			sqlStr.append(" from horarioturmadetalhado( null," + turmaVO.getCodigo() + ", null , null, null, "+disciplina+", null, null) as ht ");
		}		
		sqlStr.append(" inner join horarioturma on horarioturma.codigo = ht.horarioturma ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = ht.professor ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = ht.disciplina ");
		if (!liberarRegistroAulaEntrePeriodo) {
			sqlStr.append(" where horarioturma.semestrevigente = '").append(semestrePrm).append("' ");
			sqlStr.append(" and horarioturma.anovigente = '").append(anoPrm).append("' ");
		}
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			ProfessorTitularDisciplinaTurmaVO obj = (montarDados(tabelaResultado, nivelMontarDados, usuario));
			if (!mapProfessorDisciplinaTurmaVOs.containsKey(obj.getProfessor().getCodigo())) {
				obj.setOrdemApresentacao(3);
				mapProfessorDisciplinaTurmaVOs.put(obj.getProfessor().getCodigo(), obj);
				listaProfessorTitularDisciplinaTurmaVOs.add(obj);
			}
		}
		sqlStr = new StringBuilder("");
		//Busca o professor na programação tutoria on-line
		sqlStr.append(" select distinct 0 as codigo, matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.professor,  matriculaperiodoturmadisciplina.disciplina, matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, true as titular");
		sqlStr.append(" from matriculaperiodoturmadisciplina");
		sqlStr.append(" where matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turmaVO.getCodigo());
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		sqlStr.append(" and matriculaperiodoturmadisciplina.ano = '").append(anoPrm).append("'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = '").append(semestrePrm).append("'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.professor is not null ");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			ProfessorTitularDisciplinaTurmaVO obj = (montarDados(tabelaResultado, nivelMontarDados, usuario));
			if (!mapProfessorDisciplinaTurmaVOs.containsKey(obj.getProfessor().getCodigo())) {
				obj.setOrdemApresentacao(4);
				mapProfessorDisciplinaTurmaVOs.put(obj.getProfessor().getCodigo(), obj);
				listaProfessorTitularDisciplinaTurmaVOs.add(obj);
			}
		}
		sqlStr = new StringBuilder("");
		StringBuilder sqlStr2 = new StringBuilder("");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select turmaorigem from turmaagrupada where turma = " + turmaVO.getCodigo());
		while (rs.next()) {
			Integer turmaOrigem = rs.getInt("turmaorigem");
			if(!sqlStr.toString().trim().isEmpty()){
				sqlStr.append(" union ");
				sqlStr2.append(" union ");
			}
			/**
			 * Traz a disciplina programada para o professor na turma agrupada
			 * onde a disciplina programada é a mesma da turma normal
			 */
			sqlStr = new StringBuilder("select distinct 0 as codigo, horarioturma.turma, ht.professor,  ht.disciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, true as titular ");
			if(!anoPrm.trim().isEmpty() && !semestrePrm.trim().isEmpty()){
				sqlStr.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + anoPrm + "','" +  semestrePrm + "', null, "+disciplina+", null, null) as ht ");
			}else if(!anoPrm.trim().isEmpty()){
				sqlStr.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + anoPrm + "', null, null, "+disciplina+", null, null) as ht ");
			}else{
				sqlStr.append(" from horarioturmadetalhado( null," + turmaOrigem + ", null , null, null, "+disciplina+", null, null) as ht ");
			}	
			sqlStr.append(" inner join horarioturma on horarioturma.codigo = ht.horarioturma ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = ht.professor ");
			sqlStr.append(" inner join disciplina on disciplina.codigo = ht.disciplina ");
			
			/**
			 * Traz a disciplina programada para o professor na turma agrupada
			 * onde a disciplina programada é uma equivalente da disciplina da
			 * turma normal
			 */
			
			sqlStr2 = new StringBuilder("select distinct 0 as codigo, horarioturma.turma, ht.professor, disciplina.codigo as disciplina, horarioturma.anovigente as ano, horarioturma.semestrevigente as semestre, true as titular ");
			if(!anoPrm.trim().isEmpty() && !semestrePrm.trim().isEmpty()){
				sqlStr2.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + anoPrm + "','" +  semestrePrm + "', null, null, null, null) as ht ");
			}else if(!anoPrm.trim().isEmpty()){
				sqlStr2.append(" from horarioturmadetalhado( null," + turmaOrigem + ",'" + anoPrm + "', null, null, null, null, null) as ht ");
			}else{
				sqlStr2.append(" from horarioturmadetalhado( null," + turmaOrigem + ", null , null, null, null, null, null) as ht ");
			}	
			sqlStr2.append(" inner join horarioturma on horarioturma.codigo = ht.horarioturma ");
			sqlStr2.append(" inner join pessoa on pessoa.codigo = ht.professor ");
			sqlStr2.append(" inner join turma on turma.codigo = ht.turma ");
			sqlStr2.append(" inner join disciplinaequivalente on disciplinaequivalente.disciplina = ht.disciplina ");
			sqlStr2.append(" inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente ");
			sqlStr2.append(" where disciplina.codigo = ").append(disciplina).append(" ");
		}
		if(!sqlStr.toString().trim().isEmpty()){
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				ProfessorTitularDisciplinaTurmaVO obj = (montarDados(tabelaResultado, nivelMontarDados, usuario));
				if (!mapProfessorDisciplinaTurmaVOs.containsKey(obj.getProfessor().getCodigo())) {
					obj.setOrdemApresentacao(5);
					mapProfessorDisciplinaTurmaVOs.put(obj.getProfessor().getCodigo(), obj);
					listaProfessorTitularDisciplinaTurmaVOs.add(obj);
				}
			}
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr2.toString());
			if (tabelaResultado.next()) {
				ProfessorTitularDisciplinaTurmaVO obj = (montarDados(tabelaResultado, nivelMontarDados, usuario));
				if (!mapProfessorDisciplinaTurmaVOs.containsKey(obj.getProfessor().getCodigo())) {
					obj.setOrdemApresentacao(6);
					mapProfessorDisciplinaTurmaVOs.put(obj.getProfessor().getCodigo(), obj);
					listaProfessorTitularDisciplinaTurmaVOs.add(obj);
				}
			}
		}
		
		sqlStr = new StringBuilder("");
		//Busca o professor na programação tutoria on-line
		sqlStr.append(" SELECT DISTINCT 0 AS codigo, turma, professor, disciplina, TRUE AS titular, null as ano, null as semestre");
		sqlStr.append(" FROM programacaotutoriaonline");
		sqlStr.append(" INNER JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonline.codigo = programacaotutoriaonlineprofessor.programacaotutoriaonline");
		sqlStr.append(" WHERE programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" AND programacaotutoriaonline.turma = ").append(turmaVO.getCodigo());
		sqlStr.append(" and programacaotutoriaonlineprofessor.situacaoprogramacaotutoriaonline = 'ATIVO'");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			ProfessorTitularDisciplinaTurmaVO obj = (montarDados(tabelaResultado, nivelMontarDados, usuario));
			if (!mapProfessorDisciplinaTurmaVOs.containsKey(obj.getProfessor().getCodigo())) {
				obj.setOrdemApresentacao(7);
				mapProfessorDisciplinaTurmaVOs.put(obj.getProfessor().getCodigo(), obj);
				listaProfessorTitularDisciplinaTurmaVOs.add(obj);
			}
		}
		
		if (retornarExcessao) {
			throw new ConsistirException("Não foi possivel localizar o professor titular da turma!");
		} else {
			Ordenacao.ordenarLista(listaProfessorTitularDisciplinaTurmaVOs, "ordemApresentacao");
			return listaProfessorTitularDisciplinaTurmaVOs;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaProfessorTitularDisciplinaTurmaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE ProfessorTitularDisciplinaTurma set professor=? WHERE ((professor = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}
	
//	public String consultarProfessorTitularTurma(HistoricoTurmaRelVO historicoTurmaRelVO) throws Exception{
//
//		
//		StringBuilder sqlStr = new StringBuilder("");
//		SqlRowSet tabelaResultado = null;
//		sqlStr.append(" SELECT pessoa.nome FROM ProfessorTitularDisciplinaTurma ");
//		sqlStr.append(" inner join pessoa on ProfessorTitularDisciplinaTurma.professor = pessoa.codigo ");
//		sqlStr.append(" WHERE titular and disciplina = ").append(historicoTurmaRelVO.getDisciplinaVO().getCodigo()).append(" AND turma = ").append(historicoTurmaRelVO.getTurmaVO().getCodigo());
//		if(Uteis.isAtributoPreenchido(historicoTurmaRelVO.getAno())) {
//			sqlStr.append(" AND ano = '").append(historicoTurmaRelVO.getAno()).append("'");
//		}
//		if(Uteis.isAtributoPreenchido(historicoTurmaRelVO.getSemestre())) {
//			sqlStr.append(" AND semestre = '").append(historicoTurmaRelVO.getSemestre()).append("'");
//		}		
//		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		
//		while (tabelaResultado.next()) {
//			return tabelaResultado.getString("nome");
//		}
//		return "";
//		
//}
	
	private void montarCursoProfessorTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> professorTitularDisciplinaTurmaVOs, int curso) {
		if (Uteis.isAtributoPreenchido(professorTitularDisciplinaTurmaVOs) && Uteis.isAtributoPreenchido(curso)) {
			professorTitularDisciplinaTurmaVOs.forEach(p -> p.getCursoVO().setCodigo(curso));
		}
	}
}
