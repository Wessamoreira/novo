package negocio.facade.jdbc.avaliacaoinst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.avaliacaoinst.RespostaAvaliacaoInstitucionalDWInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * 
 * @see RespostaAvaliacaoInstitucionalDWVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class RespostaAvaliacaoInstitucionalDW extends ControleAcesso implements RespostaAvaliacaoInstitucionalDWInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9115314990440263189L;
	protected static String idEntidade;

	public RespostaAvaliacaoInstitucionalDW() throws Exception {
		super();
		setIdEntidade("RespostaAvaliacaoInstitucionalDW");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 */
	public RespostaAvaliacaoInstitucionalDWVO novo() throws Exception {
		RespostaAvaliacaoInstitucionalDW.incluir(getIdEntidade());
		RespostaAvaliacaoInstitucionalDWVO obj = new RespostaAvaliacaoInstitucionalDWVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>RespostaAvaliacaoInstitucionalDWVO</code>. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe
	 *            <code>RespostaAvaliacaoInstitucionalDWVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RespostaAvaliacaoInstitucionalDWVO obj, UsuarioVO usuario) throws Exception {
		try {
			incluir(getIdEntidade(), false, usuario);
			RespostaAvaliacaoInstitucionalDWVO.validarDados(obj);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO RespostaAvaliacaoInstitucionalDW( unidadeEnsino, curso, disciplina, avaliacaoInstitucional, questionario, pergunta, tipoPergunta, resposta, areaConhecimento, periodo, matriculaFuncionario, matriculaAluno, matriculaPeriodo, turno, tipoPessoa, pessoa, publicoAlvo, escopo, unidadeEnsinoCurso, turma, pesoPergunta, professor, inscricaoProcessoSeletivo, processoSeletivo, respostaAdicional, dataResposta, requerimento, departamentoTramite, ordemTramite, cargo, departamento, coordenador ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
						sqlInserir.setInt(1, obj.getUnidadeEnsino().intValue());
					}else {
						sqlInserir.setNull(1, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getCurso())) {
						sqlInserir.setInt(2, obj.getCurso().intValue());
					}else {
						sqlInserir.setNull(2, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDisciplina())) {
						sqlInserir.setInt(3, obj.getDisciplina().intValue());
					}else {
						sqlInserir.setNull(3, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getAvaliacaoInstitucional())) {
						sqlInserir.setInt(4, obj.getAvaliacaoInstitucional().intValue());
					}else {
						sqlInserir.setNull(4, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getQuestionario())) {
						sqlInserir.setInt(5, obj.getQuestionario().intValue());
					} else {
						sqlInserir.setNull(5, 0);					
					}
					if(Uteis.isAtributoPreenchido(obj.getPergunta())) {
						sqlInserir.setInt(6, obj.getPergunta().intValue());
					}else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, obj.getTipoPergunta());
					sqlInserir.setString(8, obj.getResposta());
					if(Uteis.isAtributoPreenchido(obj.getAreaConhecimento())) {
						sqlInserir.setInt(9, obj.getAreaConhecimento().intValue());
					}else {
						sqlInserir.setNull(9, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getPeriodo())) {
						sqlInserir.setInt(10, obj.getPeriodo().intValue());			
					}else {
						sqlInserir.setNull(10, 0);
					}
					if(!obj.getMatriculaFuncionario().trim().isEmpty()){
						sqlInserir.setString(11, obj.getMatriculaFuncionario());
					}else{
						sqlInserir.setNull(11, 0);
					}
					if(!obj.getMatriculaAluno().trim().isEmpty()){
						sqlInserir.setString(12, obj.getMatriculaAluno());
					}else{
						sqlInserir.setNull(12, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getMatriculaPeriodo())) {
						sqlInserir.setInt(13, obj.getMatriculaPeriodo().intValue());
					}else {
						sqlInserir.setNull(13, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getTurno())) {
						sqlInserir.setInt(14, obj.getTurno().intValue());
					}else {
						sqlInserir.setNull(14, 0);
					}
					sqlInserir.setString(15, obj.getTipoPessoa());
					if(Uteis.isAtributoPreenchido(obj.getPessoa())) {
						sqlInserir.setInt(16, obj.getPessoa().intValue());
					}else {
						sqlInserir.setNull(16, 0);
					}
					sqlInserir.setString(17, obj.getPublicoAlvo());
					sqlInserir.setString(18, obj.getEscopo());
					if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoCurso())) {
						sqlInserir.setInt(19, obj.getUnidadeEnsinoCurso().intValue());
					}else {
						sqlInserir.setNull(19, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getTurma())) {
						sqlInserir.setInt(20, obj.getTurma().intValue());
					}else {
						sqlInserir.setNull(20, 0);
					}
					sqlInserir.setInt(21, obj.getPesoPergunta().intValue());
					if(Uteis.isAtributoPreenchido(obj.getProfessor())) {
						sqlInserir.setInt(22, obj.getProfessor().intValue());
					}else {
						sqlInserir.setNull(22, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getInscricaoProcessoSeletivo())) {
						sqlInserir.setInt(23, obj.getInscricaoProcessoSeletivo().intValue());
					}else {
						sqlInserir.setNull(23, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getProcessoSeletivo())) {
						sqlInserir.setInt(24, obj.getProcessoSeletivo().intValue());
					}else {
						sqlInserir.setNull(24, 0);
					}
					sqlInserir.setString(25, obj.getRespostaAdicional());
					sqlInserir.setTimestamp(26, Uteis.getDataJDBCTimestamp(new Date()));
					if (obj.getRequerimento() != null && obj.getRequerimento() > 0) {
						sqlInserir.setInt(27, obj.getRequerimento());
					} else {
						sqlInserir.setNull(27, 0);
					}
					if (obj.getDepartamentoTramite() != null && obj.getDepartamentoTramite() > 0) {
						sqlInserir.setInt(28, obj.getDepartamentoTramite());
					} else {
						sqlInserir.setNull(28, 0);
					}
					sqlInserir.setInt(29, obj.getOrdemTramite());
					if (obj.getCargo() != null && obj.getCargo() > 0) {
						sqlInserir.setInt(30, obj.getCargo());
					} else {
						sqlInserir.setNull(30, 0);
					}
					if (obj.getDepartamento() != null && obj.getDepartamento() > 0) {
						sqlInserir.setInt(31, obj.getDepartamento());
					} else {
						sqlInserir.setNull(31, 0);
					}
					if (obj.getCoordenador() != null && obj.getCoordenador() > 0) {
						sqlInserir.setInt(32, obj.getCoordenador());
					} else {
						sqlInserir.setNull(32, 0);
					}
					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirTodas(List<RespostaAvaliacaoInstitucionalDWVO> listaResposta, UsuarioVO usuario) throws Exception {
		try {
			for (RespostaAvaliacaoInstitucionalDWVO obj : listaResposta) {
				incluir(obj, usuario);
			}
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>RespostaAvaliacaoInstitucionalDWVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe
	 *            <code>RespostaAvaliacaoInstitucionalDWVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RespostaAvaliacaoInstitucionalDWVO obj) throws Exception {
		try {
			RespostaAvaliacaoInstitucionalDWVO.validarDados(obj);

			obj.realizarUpperCaseDados();
			final String sql = "UPDATE RespostaAvaliacaoInstitucionalDW set unidadeEnsino=?, curso=?, disciplina=?, avaliacaoInstitucional=?, questionario=?, pergunta=?, tipoPergunta=?, resposta=?, areaConhecimento=?, periodo=?, matriculaFuncionario=?, matriculaAluno=?, matriculaPeriodo=?, turno=?, tipoPessoa=?, pessoa=?, publicoAlvo=?, escopo=?, unidadeEnsinoCurso=?, turma=?, pesoPergunta=?, professor=?,  inscricaoProcessoSeletivo=?, processoSeletivo=?, respostaAdicional = ?, dataResposta=?, requerimento = ?, departamentoTramite = ?, ordemTramite = ?, cargo = ?, departamento = ?, coordenador = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getUnidadeEnsino().intValue());
					sqlAlterar.setInt(2, obj.getCurso().intValue());
					sqlAlterar.setInt(3, obj.getDisciplina().intValue());
					sqlAlterar.setInt(4, obj.getAvaliacaoInstitucional().intValue());
					sqlAlterar.setInt(5, obj.getQuestionario().intValue());
					sqlAlterar.setInt(6, obj.getPergunta().intValue());
					sqlAlterar.setString(7, obj.getTipoPergunta());
					sqlAlterar.setString(8, obj.getResposta());
					sqlAlterar.setInt(9, obj.getAreaConhecimento().intValue());
					sqlAlterar.setInt(10, obj.getPeriodo().intValue());
					if(!obj.getMatriculaFuncionario().trim().isEmpty()){
						sqlAlterar.setString(11, obj.getMatriculaFuncionario());
					}else{
						sqlAlterar.setNull(11, 0);
					}
					if(!obj.getMatriculaAluno().trim().isEmpty()){
						sqlAlterar.setString(12, obj.getMatriculaAluno());
					}else{
						sqlAlterar.setNull(12, 0);
					}
					sqlAlterar.setInt(13, obj.getMatriculaPeriodo().intValue());
					sqlAlterar.setInt(14, obj.getTurno().intValue());
					sqlAlterar.setString(15, obj.getTipoPessoa());
					sqlAlterar.setInt(16, obj.getPessoa().intValue());
					sqlAlterar.setString(17, obj.getPublicoAlvo());
					sqlAlterar.setString(18, obj.getEscopo());
					sqlAlterar.setInt(19, obj.getUnidadeEnsinoCurso().intValue());
					sqlAlterar.setInt(20, obj.getTurma().intValue());
					sqlAlterar.setInt(21, obj.getPesoPergunta().intValue());
					sqlAlterar.setInt(22, obj.getProfessor().intValue());
					sqlAlterar.setInt(23, obj.getInscricaoProcessoSeletivo().intValue());
					sqlAlterar.setInt(24, obj.getProcessoSeletivo().intValue());
					sqlAlterar.setString(25, obj.getRespostaAdicional());
					sqlAlterar.setDate(26, Uteis.getDataJDBC(new Date()));
					if (obj.getRequerimento() != null && obj.getRequerimento() > 0) {
						sqlAlterar.setInt(27, obj.getRequerimento());
					} else {
						sqlAlterar.setNull(27, 0);
					}
					if (obj.getDepartamentoTramite() != null && obj.getDepartamentoTramite() > 0) {
						sqlAlterar.setInt(28, obj.getDepartamentoTramite());
					} else {
						sqlAlterar.setNull(28, 0);
					}
					sqlAlterar.setInt(29, obj.getOrdemTramite());
					if (obj.getCargo() != null && obj.getCargo() > 0) {
						sqlAlterar.setInt(30, obj.getCargo());
					} else {
						sqlAlterar.setNull(30, 0);
					}
					if (obj.getDepartamento() != null && obj.getDepartamento() > 0) {
						sqlAlterar.setInt(31, obj.getDepartamento());
					} else {
						sqlAlterar.setNull(31, 0);
					}
					if (obj.getCoordenador() != null && obj.getCoordenador() > 0) {
						sqlAlterar.setInt(32, obj.getCoordenador());
					} else {
						sqlAlterar.setNull(32, 0);
					}
					sqlAlterar.setInt(33, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPorRequerimentoHistorico(final RespostaAvaliacaoInstitucionalDWVO obj, UsuarioVO usuario) throws Exception {
		try {
			RespostaAvaliacaoInstitucionalDWVO.validarDados(obj);

			obj.realizarUpperCaseDados();
			final String sql = "UPDATE RespostaAvaliacaoInstitucionalDW set tipoPergunta=?, resposta=?, escopo=?, respostaAdicional = ?, dataResposta=? WHERE departamentoTramite = ? and ordemtramite = ? and requerimento = ? and questionario = ? and pergunta = ? ";
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getTipoPergunta());
					sqlAlterar.setString(2, obj.getResposta());
					sqlAlterar.setString(3, obj.getEscopo());
					sqlAlterar.setString(4, obj.getRespostaAdicional());
					sqlAlterar.setDate(5, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setInt(6, obj.getDepartamentoTramite());
					sqlAlterar.setInt(7, obj.getOrdemTramite());
					sqlAlterar.setInt(8, obj.getRequerimento());
					sqlAlterar.setInt(9, obj.getQuestionario().intValue());
					sqlAlterar.setInt(10, obj.getPergunta().intValue());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, usuario);
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>RespostaAvaliacaoInstitucionalDWVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe
	 *            <code>RespostaAvaliacaoInstitucionalDWVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RespostaAvaliacaoInstitucionalDWVO obj) throws Exception {
		try {
			RespostaAvaliacaoInstitucionalDW.excluir(getIdEntidade());
			String sql = "DELETE FROM RespostaAvaliacaoInstitucionalDW WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirPorCodigoInscricaoProcSeletivo(Integer inscricao) throws Exception {
		try {
			if (inscricao != null && inscricao > 0) {
				String sql = "DELETE FROM RespostaAvaliacaoInstitucionalDW WHERE ((inscricaoProcessoSeletivo = ?))";
				getConexao().getJdbcTemplate().update(sql, new Object[] { inscricao });
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluirPorCodigoRequerimento(Integer requerimento) throws Exception {
		try {
			if (requerimento != null && requerimento > 0) {
				String sql = "DELETE FROM RespostaAvaliacaoInstitucionalDW WHERE ((requerimento = ?))";
				getConexao().getJdbcTemplate().update(sql, new Object[] { requerimento });
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluirPorCodigoRequerimentoHistorico(Integer requerimentoHistorico) throws Exception {
		try {
			if (requerimentoHistorico != null && requerimentoHistorico > 0) {
				String sql = "DELETE FROM RespostaAvaliacaoInstitucionalDW WHERE ((requerimentoHistorico = ?))";
				getConexao().getJdbcTemplate().update(sql, new Object[] { requerimentoHistorico });
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<RespostaAvaliacaoInstitucionalDWVO> consultarRespostaQuestionarioPorInscricao(Integer inscricao, Integer questionario) throws Exception {
		StringBuilder sb = new StringBuilder(" select * from respostaAvaliacaoInstitucionalDW where inscricaoProcessoSeletivo = ").append(inscricao).append(" and questionario = ").append(questionario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado));
	}

	@Override
	public List<RespostaAvaliacaoInstitucionalDWVO> consultarRespostaQuestionarioPorRequerimento(Integer requerimento, Integer questionario) throws Exception {
		StringBuilder sb = new StringBuilder(" select * from respostaAvaliacaoInstitucionalDW where escopo = 'RE' and requerimento = ").append(requerimento).append(" and (departamentoTramite is null or departamentoTramite = 0) and (ordemtramite = 0 or ordemtramite is null) and questionario = ").append(questionario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado));
	}

	@Override
	public List<RespostaAvaliacaoInstitucionalDWVO> consultarRespostaQuestionarioPorRequerimentoHistorico(Integer requerimento, Integer departamentoTramite, Integer ordemTramite, Integer questionario) throws Exception {
		StringBuilder sb = new StringBuilder(" select * from respostaAvaliacaoInstitucionalDW where escopo = 'RE' and requerimento = ").append(requerimento).append(" and departamentoTramite = ").append(departamentoTramite).append(" and ordemtramite = ").append(ordemTramite).append(" and questionario = ").append(questionario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado));
	}
	
	public Boolean consultarPorAvaliacaoInstitucionalRespondida(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RespostaAvaliacaoInstitucionalDW WHERE avaliacaoInstitucional = " + valorConsulta.intValue() + " ORDER BY avaliacaoInstitucional";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>RespostaAvaliacaoInstitucionalDWVO</code> resultantes da
	 *         consulta.
	 */
	public static List<RespostaAvaliacaoInstitucionalDWVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<RespostaAvaliacaoInstitucionalDWVO> vetResultado = new ArrayList<RespostaAvaliacaoInstitucionalDWVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 *
	 * @return O objeto da classe
	 *         <code>RespostaAvaliacaoInstitucionalDWVO</code> com os dados
	 *         devidamente montados.
	 */
	public static RespostaAvaliacaoInstitucionalDWVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		RespostaAvaliacaoInstitucionalDWVO obj = new RespostaAvaliacaoInstitucionalDWVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setUnidadeEnsino(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setCurso(new Integer(dadosSQL.getInt("curso")));
		obj.setDisciplina(new Integer(dadosSQL.getInt("disciplina")));
		obj.setAvaliacaoInstitucional(new Integer(dadosSQL.getInt("avaliacaoInstitucional")));
		obj.setQuestionario(new Integer(dadosSQL.getInt("questionario")));
		obj.setPergunta(new Integer(dadosSQL.getInt("pergunta")));
		obj.setTipoPergunta(dadosSQL.getString("tipoPergunta"));
		obj.setResposta(dadosSQL.getString("resposta"));
		obj.setRespostaAdicional(dadosSQL.getString("respostaAdicional"));
		obj.setAreaConhecimento(new Integer(dadosSQL.getInt("areaConhecimento")));
		obj.setPeriodo(new Integer(dadosSQL.getInt("periodo")));
		obj.setMatriculaFuncionario(dadosSQL.getString("matriculaFuncionario"));
		obj.setMatriculaAluno(dadosSQL.getString("matriculaAluno"));
		obj.setMatriculaPeriodo(new Integer(dadosSQL.getInt("matriculaPeriodo")));
		obj.setTurno(new Integer(dadosSQL.getInt("turno")));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.setPessoa(new Integer(dadosSQL.getInt("pessoa")));
		obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
		obj.setEscopo(dadosSQL.getString("escopo"));
		obj.setUnidadeEnsinoCurso(new Integer(dadosSQL.getInt("unidadeEnsinoCurso")));
		obj.setTurma(new Integer(dadosSQL.getInt("turma")));
		obj.setPesoPergunta(new Integer(dadosSQL.getInt("pesoPergunta")));
		obj.setProfessor(new Integer(dadosSQL.getInt("professor")));
		obj.setRequerimento(new Integer(dadosSQL.getInt("requerimento")));
		obj.setDepartamentoTramite(dadosSQL.getInt("departamentoTramite"));
		obj.setOrdemTramite(dadosSQL.getInt("ordemTramite"));
		obj.setCargo(dadosSQL.getInt("cargo"));
		obj.setDepartamento(dadosSQL.getInt("departamento"));
		obj.setCoordenador(dadosSQL.getInt("coordenador"));

		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>RespostaAvaliacaoInstitucionalDWVO</code> através de sua chave
	 * primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public RespostaAvaliacaoInstitucionalDWVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM RespostaAvaliacaoInstitucionalDW WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( RespostaAvaliacaoInstitucionalDW ).");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return RespostaAvaliacaoInstitucionalDW.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		RespostaAvaliacaoInstitucionalDW.idEntidade = idEntidade;
	}

	@Override
	public List<RespostaAvaliacaoInstitucionalDWVO> gerarRespostaQuestionarioRequerimento(RequerimentoVO requerimentoVO) throws ConsistirException {
		return gerarListaRespostaAluno(requerimentoVO.getMatricula(), null, requerimentoVO.getPessoa().getCodigo(), TipoPessoa.ALUNO.getValor(), requerimentoVO.getUnidadeEnsino().getCodigo(), null, requerimentoVO.getQuestionarioVO(), "", null, null, requerimentoVO.getCodigo(), null, null, false, null);

	}

	@Override
	public List<RespostaAvaliacaoInstitucionalDWVO> gerarRespostaQuestionarioRequerimentoHistorico(RequerimentoHistoricoVO requerimentoHistoricoVO) throws ConsistirException {
		return gerarListaRespostaAluno(null, null, null, TipoPessoa.ALUNO.getValor(), null, null, requerimentoHistoricoVO.getQuestionario(), "", null, null, requerimentoHistoricoVO.getRequerimento(), requerimentoHistoricoVO.getDepartamento().getCodigo(), requerimentoHistoricoVO.getOrdemExecucaoTramite(), false, null);

	}

	public List<RespostaAvaliacaoInstitucionalDWVO> gerarListaRespostaAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Integer pessoa, String tipoPessoa, Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacao, QuestionarioVO questionario, String matriculaFuncionario, Integer inscricaoProcessoSeletivo, Integer processoSeletivo, Integer requerimento, Integer departamentoTramite, Integer ordemTramite, Boolean validarImportanciaPergunta, List<QuestionarioVO> listaQuestionarioVOs) throws ConsistirException {
		List<RespostaAvaliacaoInstitucionalDWVO> respostaAvaliacaoInstitucionalDWVOs = new ArrayList<RespostaAvaliacaoInstitucionalDWVO>(0);

		RespostaAvaliacaoInstitucionalDWVO novoObjeto = new RespostaAvaliacaoInstitucionalDWVO();
		if ((listaQuestionarioVOs == null || listaQuestionarioVOs.isEmpty()) && !questionario.getEscopo().equals("DI") && !questionario.getEscopo().equals("UM")) {

			Iterator<PerguntaQuestionarioVO> i = questionario.getPerguntaQuestionarioVOs().iterator();
			while (i.hasNext()) {
				PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
				novoObjeto = realizarCriacaoRespostaAvaliacao(matriculaVO, matriculaPeriodoVO, pessoa, tipoPessoa, unidadeEnsino, questionario.getDisciplinaVO().getCodigo(), 
						questionario.getTurmaVO(), avaliacao, questionario, objExistente.getPergunta(), matriculaFuncionario, inscricaoProcessoSeletivo, processoSeletivo, 
						requerimento, departamentoTramite, ordemTramite, validarImportanciaPergunta, objExistente.getRespostaObrigatoria(),
						questionario.getCargo().getCodigo(), questionario.getDepartamento().getCodigo(), questionario.getCoordenador().getCodigo(), questionario.getCodigoCurso());
				respostaAvaliacaoInstitucionalDWVOs.add(novoObjeto);
			}
		} else {

			for (QuestionarioVO obj : listaQuestionarioVOs) {
				for (PerguntaQuestionarioVO perguntaQuestionarioVO : obj.getPerguntaQuestionarioVOs()) {
					novoObjeto = new RespostaAvaliacaoInstitucionalDWVO();					
					obj.setCodigo(questionario.getCodigo());
					if (obj.getTurmaVO() == 0) {
						obj.setTurmaVO(questionario.getTurmaVO());
					}
					novoObjeto = realizarCriacaoRespostaAvaliacao(matriculaVO, matriculaPeriodoVO, pessoa, tipoPessoa, unidadeEnsino, obj.getDisciplinaVO().getCodigo(), obj.getTurmaVO(), avaliacao, obj, perguntaQuestionarioVO.getPergunta(), matriculaFuncionario, inscricaoProcessoSeletivo, processoSeletivo, requerimento, departamentoTramite, ordemTramite, validarImportanciaPergunta, perguntaQuestionarioVO.getRespostaObrigatoria(),
							obj.getCargo().getCodigo(), obj.getDepartamento().getCodigo(), obj.getCoordenador().getCodigo(), obj.getCodigoCurso());
					respostaAvaliacaoInstitucionalDWVOs.add(novoObjeto);
				}
			}

		}
		return respostaAvaliacaoInstitucionalDWVOs;
	}

	@Override
	public RespostaAvaliacaoInstitucionalDWVO realizarCriacaoRespostaAvaliacao(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Integer pessoa, String tipoPessoa, 
			Integer unidadeEnsinoVO, Integer disciplina, Integer turma, AvaliacaoInstitucionalVO avaliacao, QuestionarioVO questionario, PerguntaVO pergunta, 
			String matriculaFuncionario, Integer inscricaoProcessoSeletivo, Integer processoSeletivo, Integer requerimento, Integer departamentoTramite, Integer ordemTramite, 
			Boolean validarImportanciaPergunta, Boolean obrigarResposta,
			Integer cargo, Integer departamento, Integer coordenador, Integer curso) throws ConsistirException {
		RespostaAvaliacaoInstitucionalDWVO obj = new RespostaAvaliacaoInstitucionalDWVO();
		
		obj.setEscopo(questionario.getEscopo());
		obj.setTurma(questionario.getTurmaVO());
		obj.setUnidadeEnsino(questionario.getUnidadeEnsino());
		obj.setTurno(questionario.getTurno());
		obj.setCurso(questionario.getCodigoCurso());
		if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
			obj.setUnidadeEnsino(unidadeEnsinoVO);
		}
		if (matriculaVO != null) {
			obj.setCurso(matriculaVO.getCurso().getCodigo());
			obj.setMatriculaAluno(matriculaVO.getMatricula());
			obj.setTurno(matriculaVO.getTurno().getCodigo());
//			obj.setAreaConhecimento(matriculaVO.getCurso().getAreaConhecimento().getCodigo());
		}
		if (matriculaPeriodoVO != null) {
			obj.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
			obj.setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCurso());
		}
		if (avaliacao != null) {
			obj.setAvaliacaoInstitucional(avaliacao.getCodigo());
			obj.setPublicoAlvo(avaliacao.getPublicoAlvo());
		}
		obj.setQuestionario(questionario.getCodigo());
		if (questionario.getEscopo().equals("DI")) {
			obj.setResposta(validarDadosRepostaPerguntaEscopoDisciplina(pergunta, avaliacao.getInformarImportanciaPergunta(), obrigarResposta, questionario.getDisciplinaVO(), avaliacao, questionario));
			obj.setRespostaAdicional(pergunta.getTextoAdicional());
		} else {
			if (avaliacao == null) {
				obj.setResposta(validarDadosRepostaPergunta(questionario, pergunta, false, obrigarResposta, avaliacao));
				obj.setRespostaAdicional(pergunta.getTextoAdicional());
			} else {
				obj.setResposta(validarDadosRepostaPergunta(questionario, pergunta, avaliacao.getInformarImportanciaPergunta(), obrigarResposta, avaliacao));
				obj.setRespostaAdicional(pergunta.getTextoAdicional());
			}
		}
		obj.setCoordenador(coordenador);
		obj.setCargo(cargo);
		obj.setDepartamento(departamento);
		obj.setPergunta(pergunta.getCodigo());
		obj.setTipoPergunta(pergunta.getTipoResposta());
		obj.setDisciplina(questionario.getDisciplinaVO().getCodigo());

		obj.setPeriodo(Uteis.getAnoData(new Date()));
		obj.setMatriculaFuncionario(matriculaFuncionario);
		obj.setPessoa(pessoa);
		obj.setTipoPessoa(tipoPessoa);

		if (questionario.getEscopo().equals("UM")) {
			obj.setTurma(turma);
			TurmaVO t = new TurmaVO();
			t.setCodigo(turma);
			try {
				getFacadeFactory().getTurmaFacade().carregarDados(t, null);
				obj.setUnidadeEnsino(t.getUnidadeEnsino().getCodigo());
				obj.setCurso(t.getCurso().getCodigo());				
				obj.setTurno(t.getTurno().getCodigo());
			} catch (Exception e) {
				throw new ConsistirException(e.getMessage());
			}
		}
		if (tipoPessoa.equals("AL") && matriculaPeriodoVO != null) {
			obj.setTurma(matriculaPeriodoVO.getTurma().getCodigo());
		}
		if (avaliacao != null) {
			if(avaliacao.getPublicoAlvo_ProfessorTurma() && Uteis.isAtributoPreenchido(turma)){
				obj.setTurma(turma);
				TurmaVO t = new TurmaVO();
				t.setCodigo(turma);
				try {
					getFacadeFactory().getTurmaFacade().carregarDados(t, null);
					obj.setUnidadeEnsino(t.getUnidadeEnsino().getCodigo());
					obj.setCurso(t.getCurso().getCodigo());
					obj.setTurno(t.getTurno().getCodigo());
				} catch (Exception e) {
					throw new ConsistirException(e.getMessage());
				}
			}
			if(avaliacao.getPublicoAlvo_ProfessorCurso() || avaliacao.getPublicoAlvo_CoordenadorAvaliacaoCurso() && Uteis.isAtributoPreenchido(curso)){
				obj.setCurso(curso);
			}
		}
		obj.setPesoPergunta(pergunta.getPeso());
		obj.setProfessor(questionario.getProfessor().getCodigo());
		obj.setInscricaoProcessoSeletivo(inscricaoProcessoSeletivo);
		obj.setProcessoSeletivo(processoSeletivo);
		obj.setRequerimento(requerimento);
		obj.setDepartamentoTramite(departamentoTramite);
		obj.setOrdemTramite(ordemTramite);
		
		
		return obj;
	}

	@Override
	public String validarDadosRepostaPergunta(QuestionarioVO questionarioVO, PerguntaVO obj, Boolean validarImportanciaPergunta, Boolean obrigarResposta, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) throws ConsistirException {
		if(avaliacaoInstitucionalVO != null) { 
			avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("");
		}
		if (obj.getPeso().equals(0) && validarImportanciaPergunta != null && validarImportanciaPergunta) {
			if(avaliacaoInstitucionalVO != null) {
			avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("pergunta"+questionarioVO.getKey()+""+obj.getCodigo()+"");
			}
			throw new ConsistirException("A importância da pergunta ''" + obj.getDescricao() + "'' deve ser informada.");
		}
		if (!obj.getTipoRespostaTextual()) {
			obj.setTexto("");
			obj.setTextoAdicional("");
			Iterator<RespostaPerguntaVO> j = obj.getRespostaPerguntaVOs().iterator();
			while (j.hasNext()) {
				RespostaPerguntaVO objExistenteResposta = (RespostaPerguntaVO) j.next();
				if (objExistenteResposta.getSelecionado()) {
					obj.setTexto(obj.getTexto() + "[" + objExistenteResposta.getCodigo() + "]");
					if (objExistenteResposta.getApresentarRespostaAdicional() && !objExistenteResposta.getRespostaAdicional().trim().isEmpty()) {
						obj.setTextoAdicional(obj.getTextoAdicional() + "[" + objExistenteResposta.getCodigo() + "]{" + objExistenteResposta.getRespostaAdicional() + "}");
					}
				}
			}
		}
		if (obj.getTexto().equals("") && obrigarResposta) {
			if(questionarioVO.getTituloToggle_Apresentar().trim().isEmpty()) {
				if(avaliacaoInstitucionalVO != null) {
				avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("pergunta"+questionarioVO.getKey()+""+obj.getCodigo()+"");
				}
				throw new ConsistirException("A pergunta ''" + obj.getDescricao() + "'' deve ser respondida.");
			}else {
				avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("pergunta"+questionarioVO.getKey()+""+obj.getCodigo()+"");
				throw new ConsistirException("A pergunta ''" + obj.getDescricao() + "'' ("+questionarioVO.getTituloToggle_Apresentar()+") deve ser respondida.");
			}
		}
		if (Uteis.isAtributoPreenchido(obj.getTexto()) && obj.getTipoResposta().equals("SE") && obj.getTexto().contains("][")) {
			if(avaliacaoInstitucionalVO != null) {
				avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("pergunta"+questionarioVO.getKey()+""+obj.getCodigo()+"");
			}
			throw new ConsistirException("A pergunta ''" + obj.getDescricao() + "'' é de simples escolha.");
		}
		return obj.getTexto();
	}

	@Override
	public String validarDadosRepostaPerguntaEscopoDisciplina(PerguntaVO obj, Boolean validarImportanciaPergunta, Boolean obrigarResposta, DisciplinaVO disciplinaVO, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, QuestionarioVO questionarioVO) throws ConsistirException {
		if(avaliacaoInstitucionalVO != null) {
		avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("");
		}
		if (obj.getPeso().equals(0) && validarImportanciaPergunta) {
			if(avaliacaoInstitucionalVO != null) {
				avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("pergunta"+questionarioVO.getKey()+""+obj.getCodigo()+"");
			}
			throw new ConsistirException("A importância da pergunta ''" + obj.getDescricao() + "'' deve ser informada para a Disciplina - " + disciplinaVO.getNome().toUpperCase() + "");
		}
		if (!obj.getTipoRespostaTextual()) {
			obj.setTexto("");
			obj.setTextoAdicional("");
			Iterator<RespostaPerguntaVO> j = obj.getRespostaPerguntaVOs().iterator();
			while (j.hasNext()) {
				RespostaPerguntaVO objExistenteResposta = (RespostaPerguntaVO) j.next();
				if (objExistenteResposta.getSelecionado()) {
					obj.setTexto(obj.getTexto() + "[" + objExistenteResposta.getCodigo() + "]");
					if (objExistenteResposta.getApresentarRespostaAdicional() && !objExistenteResposta.getRespostaAdicional().trim().isEmpty()) {
						obj.setTextoAdicional(obj.getTextoAdicional() + "[" + objExistenteResposta.getCodigo() + "]{" + objExistenteResposta.getRespostaAdicional() + "}");
					}
				}
			}
		}
		if (obj.getTexto().equals("") && obrigarResposta) {
			if(avaliacaoInstitucionalVO != null) {
				avaliacaoInstitucionalVO.setKeyPerguntaNaoRespondida("pergunta"+questionarioVO.getKey()+""+obj.getCodigo()+"");
			}
			throw new ConsistirException("A pergunta ''" + obj.getDescricao() + "'' deve ser respondida para a Disciplina - " + disciplinaVO.getNome().toUpperCase() + "");
		}
		return obj.getTexto();
	}

	/**
	 * Este método é responsável por verificar qual é o publico alvo da
	 * avaliação institucional e chamar o método correspondente que verifica
	 * qual ou quais questionários o mesmo deve responder de acordo com os
	 * filtros da avaliação institucional
	 * 
	 * @param avaliacaoInstitucionalVO
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<QuestionarioVO> executarMontagemQuestionarioSerRespondidoPorAvaliacaoInstitucionalDeAcordoComPublicoAlvo(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean trazerRespondido) throws Exception {
		if (avaliacaoInstitucionalVO.getPublicoAlvo_Curso() || avaliacaoInstitucionalVO.getPublicoAlvo_TodosCursos() || avaliacaoInstitucionalVO.getPublicoAlvo_Turma()) {
			matriculaPeriodoVO.setMatriculaVO(matriculaVO);
			if (!avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo() && avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("DI")) {
				return executarMontagemListaAvaliacaoEscopoDisciplinaQuestionario(avaliacaoInstitucionalVO, matriculaVO.getMatricula(), matriculaPeriodoVO, matriculaVO.getUnidadeEnsino().getCodigo(), trazerRespondido, usuario);
			} else if (avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
				return executarMontagemListaAvaliacaoEscopoDisciplinaQuestionario(avaliacaoInstitucionalVO, matriculaVO.getMatricula(), matriculaPeriodoVO, matriculaVO.getUnidadeEnsino().getCodigo(), trazerRespondido, usuario);				
			}
		}

		PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional = PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo());
		switch (publicoAlvoAvaliacaoInstitucional) {
		case CARGO_COORDENADORES:
			return executarMontagemListaAvaliacaoEscopoCargoAvaliandoCoordenador(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case COLABORADORES_INSTITUICAO:
			return executarMontagemListaAvaliacaoEscopoColaboradorAvaliandoInstituicao(avaliacaoInstitucionalVO, usuario);
		case COORDENADORES:
			return executarMontagemListaAvaliacaoEscopoCoordenador(avaliacaoInstitucionalVO, usuario);
		case COORDENADORES_CARGO:
			return executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoCargo(avaliacaoInstitucionalVO, usuario);
		case COORDENADORES_CURSO:
			return executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoCurso(avaliacaoInstitucionalVO, usuario);
		case COORDENADORES_DEPARTAMENTO:
			return executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoDepartamento(avaliacaoInstitucionalVO, usuario);
		case COORDENADORES_PROFESSOR:
			return executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoProfessor(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case CURSO:
			return executarMontagemListaAvaliacaoEscopoCurso(avaliacaoInstitucionalVO, usuario, matriculaVO, matriculaPeriodoVO);
		case DEPARTAMENTO_COORDENADORES:
			return executarMontagemListaAvaliacaoEscopoDepartamentoAvaliandoCoordenador(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case DEPARTAMENTO_DEPARTAMENTO:
			return executarMontagemListaAvaliacaoEscopoDepartamentoAvaliandoDepartamento(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case DEPARTAMENTO_CARGO:
			return executarMontagemListaAvaliacaoEscopoDepartamentoAvaliandoCargo(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case CARGO_DEPARTAMENTO:
			return executarMontagemListaAvaliacaoEscopoCargoAvaliandoDepartamento(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case CARGO_CARGO:
			return executarMontagemListaAvaliacaoEscopoCargoAvaliandoCargo(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case FUNCIONARIO_GESTOR:
			return executarMontagemListaAvaliacaoEscopoFuncionarioGestor(avaliacaoInstitucionalVO, usuario);
		case PROFESSORES:
			return executarMontagemListaAvaliacaoEscopoProfessor(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case PROFESSORES_COORDENADORES:
			return executarMontagemListaAvaliacaoEscopoProfessorAvaliandoCoodenador(avaliacaoInstitucionalVO, usuario, trazerRespondido);
		case TODOS_CURSOS:
			return executarMontagemListaAvaliacaoEscopoTodosCurso(avaliacaoInstitucionalVO, usuario, matriculaVO, matriculaPeriodoVO);
		case TURMA:
			return executarMontagemListaAvaliacaoEscopoTurma(avaliacaoInstitucionalVO, usuario, matriculaVO, matriculaPeriodoVO);
		case ALUNO_COORDENADOR:
			return executarMontagemListaAvaliacaoEscopoAlunoCoordenador(avaliacaoInstitucionalVO, usuario, matriculaVO, matriculaPeriodoVO, trazerRespondido);
		case PROFESSOR_TURMA:
			return executarMontagemListaAvaliacaoEscopoProfessorTurma(avaliacaoInstitucionalVO, usuario, matriculaVO, matriculaPeriodoVO, trazerRespondido);
		case PROFESSOR_CURSO:
			return executarMontagemListaAvaliacaoEscopoProfessorCurso(avaliacaoInstitucionalVO, usuario, matriculaVO, matriculaPeriodoVO, trazerRespondido);
		default:
			return null;
		}

	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoProfessor(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, Boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_Professor()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		if(avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
			List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarTurmaDisciplinaUltimoModuloTurmaVisaoProfessorPorAvaliacaoInstitucional(usuario, avaliacaoInstitucionalVO, trazerRespondido);
			for(TurmaDisciplinaVO turmaDisciplinaVO: turmaDisciplinaVOs) {
				QuestionarioVO questionarioVO = new QuestionarioVO();
				questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
				questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
				questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
				questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
				questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
				questionarioVO.setTurmaVO(turmaDisciplinaVO.getTurmaDescricaoVO().getCodigo());
				questionarioVO.setIdentificadorTurma(turmaDisciplinaVO.getTurmaDescricaoVO().getIdentificadorTurma());
				questionarioVO.setCodigoCurso(turmaDisciplinaVO.getTurmaDescricaoVO().getCurso().getCodigo());
				questionarioVO.setCurso(turmaDisciplinaVO.getTurmaDescricaoVO().getCurso().getNome());
				questionarioVO.setDisciplinaVO(turmaDisciplinaVO.getDisciplina());				
				questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
				questionarioVO.setAbrirToggle(Boolean.TRUE);
				questionarioVO.setUnidadeEnsino(turmaDisciplinaVO.getTurmaDescricaoVO().getUnidadeEnsino().getCodigo());
				questionarioVO.setTurno(turmaDisciplinaVO.getTurmaDescricaoVO().getTurno().getCodigo());
				questionarioVOs.add(questionarioVO);
			}
			
		}else {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVO.setAbrirToggle(Boolean.TRUE);
			questionarioVOs.add(questionarioVO);			
		}
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCargoAvaliandoCargo(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO.getValor())) {
			return null;
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" 	select distinct departamento.codigo, departamento.nome, cargoavaliado.codigo as cargo_codigo, cargoavaliado.nome as cargo_nome from funcionariocargo");
		sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo");
		sqlStr.append(" 	inner join departamento on departamento.codigo = ").append(avaliacaoInstitucionalVO.getDepartamentoAvaliado().getCodigo());
		if (avaliacaoInstitucionalVO.getCargoAvaliado().getCodigo() > 0) {
			sqlStr.append(" 	inner join cargo cargoavaliado on cargoavaliado.codigo = ").append(avaliacaoInstitucionalVO.getCargoAvaliado().getCodigo());
		} else {
			sqlStr.append(" 	inner join cargo cargoavaliado on cargoavaliado.departamento =  departamento.codigo ");
		}
		if(!trazerRespondido){
			sqlStr.append(" 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.cargo = cargoavaliado.codigo");
		}
		sqlStr.append(" 	where funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		sqlStr.append(" 	and cargo.codigo = ").append(avaliacaoInstitucionalVO.getCargo().getCodigo());
		if(!trazerRespondido){
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getDepartamento().setCodigo(rs.getInt("codigo"));
			questionarioVO.getDepartamento().setNome(rs.getString("nome"));
			questionarioVO.getCargo().setCodigo(rs.getInt("cargo_codigo"));
			questionarioVO.getCargo().setNome(rs.getString("cargo_nome"));
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoDepartamentoAvaliandoCargo(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO.getValor())) {
			return null;
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" 	select distinct departamento.codigo, departamento.nome, cargoavaliado.codigo as cargo_codigo, cargoavaliado.nome as cargo_nome from funcionariocargo");
		sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo");
		sqlStr.append(" 	inner join departamento on departamento.codigo = ").append(avaliacaoInstitucionalVO.getDepartamentoAvaliado().getCodigo());
		if (avaliacaoInstitucionalVO.getCargoAvaliado().getCodigo() > 0) {
			sqlStr.append(" 	inner join cargo cargoavaliado on cargoavaliado.codigo = ").append(avaliacaoInstitucionalVO.getCargoAvaliado().getCodigo());
		} else {
			sqlStr.append(" 	inner join cargo cargoavaliado on cargoavaliado.departamento =  departamento.codigo ");
		}
		if(!trazerRespondido){
			sqlStr.append(" 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.cargo = cargoavaliado.codigo");
		}
		sqlStr.append(" 	where funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		sqlStr.append(" 	and cargo.departamento = ").append(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		if(!trazerRespondido){
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x=1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getDepartamento().setCodigo(rs.getInt("codigo"));
			questionarioVO.getDepartamento().setNome(rs.getString("nome"));
			questionarioVO.getCargo().setCodigo(rs.getInt("cargo_codigo"));
			questionarioVO.getCargo().setNome(rs.getString("cargo_nome"));
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCargoAvaliandoDepartamento(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO.getValor())) {
			return null;
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" 	select distinct departamento.codigo, departamento.nome from funcionariocargo");
		sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo ");
		if (avaliacaoInstitucionalVO.getDepartamentoAvaliado().getCodigo() > 0) {
			sqlStr.append(" 	inner join departamento on departamento.codigo = ").append(avaliacaoInstitucionalVO.getDepartamentoAvaliado().getCodigo());
		} else {
			sqlStr.append(" 	inner join departamento on departamento.codigo in (select codigo from departamento where departamento.unidadeensino is null or departamento.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(") ");
		}
		if(!trazerRespondido){
			sqlStr.append(" 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
		}
		sqlStr.append(" 	where funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		sqlStr.append(" 	and cargo.codigo = ").append(avaliacaoInstitucionalVO.getCargo().getCodigo());
		if(!trazerRespondido){
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getDepartamento().setCodigo(rs.getInt("codigo"));
			questionarioVO.getDepartamento().setNome(rs.getString("nome"));
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoDepartamentoAvaliandoDepartamento(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO.getValor())) {
			return null;
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" 	select distinct departamento.codigo, departamento.nome from funcionariocargo");
		sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo");
		if (avaliacaoInstitucionalVO.getDepartamentoAvaliado().getCodigo() > 0) {
			sqlStr.append(" 	inner join departamento on departamento.codigo = ").append(avaliacaoInstitucionalVO.getDepartamentoAvaliado().getCodigo());
		} else {
			sqlStr.append(" 	inner join departamento on departamento.codigo in (select codigo from departamento where departamento.unidadeensino is null or departamento.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(") ");
		}
		if(!trazerRespondido){
			sqlStr.append(" 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
		}
		sqlStr.append(" 	where funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		sqlStr.append(" 	and cargo.departamento = ").append(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		if(!trazerRespondido){
			sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getDepartamento().setCodigo(rs.getInt("codigo"));
			questionarioVO.getDepartamento().setNome(rs.getString("nome"));
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoFuncionarioGestor(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_FuncionarioGestor()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_Curso()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVO.setCodigoCurso(matriculaVO.getCurso().getCodigo());
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVO.setCurso(matriculaVO.getCurso().getNome());
		questionarioVO.setTurmaVO(matriculaPeriodoVO.getTurma().getCodigo());
		questionarioVO.setIdentificadorTurma(matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}
	
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoTodosCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_TodosCursos()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVO.setCurso(matriculaVO.getCurso().getNome());
		questionarioVO.setCodigoCurso(matriculaVO.getCurso().getCodigo());
		questionarioVO.setTurmaVO(matriculaPeriodoVO.getTurma().getCodigo());
		questionarioVO.setIdentificadorTurma(matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoTurma(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_Turma()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVO.setCodigoCurso(matriculaVO.getCurso().getCodigo());
		questionarioVO.setCurso(matriculaVO.getCurso().getNome());
		questionarioVO.setTurmaVO(matriculaPeriodoVO.getTurma().getCodigo());
		questionarioVO.setIdentificadorTurma(matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoDepartamentoAvaliandoCoordenador(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_DepartamentoCoordenador()) {
			return null;
		}
		StringBuilder sql = new StringBuilder("select codigo, nome from pessoa ");
		sql.append(" where codigo in ( ");
		sql.append(" 		select distinct pessoa.codigo from funcionariocargo");
		sql.append(" 		inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" 		inner join cargo on cargo.codigo = funcionariocargo.cargo");
		sql.append(" 		inner join cursocoordenador on cursocoordenador.unidadeensino = funcionariocargo.unidadeensino");
		sql.append(" 		inner join funcionario coordenador on cursocoordenador.funcionario = coordenador.codigo");
		sql.append(" 		inner join pessoa on pessoa.codigo = coordenador.pessoa");
		sql.append(" 		inner join curso on curso.codigo = cursocoordenador.curso");
		if(!trazerRespondido){
			sql.append(" 		left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sql.append(" 		and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
			sql.append(" 		and respostaavaliacaoinstitucionaldw.coordenador = coordenador.pessoa");
		}
		sql.append(" 		where funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sql.append(" 		and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		sql.append(" 		and cargo.departamento =  ").append(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		sql.append(" 		and pessoa.ativo = true and funcionariocargo.ativo ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append("   	and cursocoordenador.tipocoordenadorcurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");			
		sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and avaliacaoinstitucionalpessoaavaliada.pessoa = pessoa.codigo )) ");
		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		sql.append(" 		and pessoa.codigo != ").append(usuario.getPessoa().getCodigo());
		if(!trazerRespondido){
			sql.append(" 		and respostaavaliacaoinstitucionaldw.codigo is null ");
		}
		sql.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getCoordenador().setCodigo(rs.getInt("codigo"));
			questionarioVO.getCoordenador().setNome(rs.getString("nome"));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}	

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCargoAvaliandoCoordenador(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_CargoCoordenador()) {
			return null;
		}
		StringBuilder sql = new StringBuilder("select codigo, nome from pessoa ");
		sql.append(" where codigo in ( ");
		sql.append(" 		select distinct pessoa.codigo from funcionariocargo");
		sql.append(" 		inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" 		inner join cargo on cargo.codigo = funcionariocargo.cargo");
		sql.append(" 		inner join cursocoordenador on cursocoordenador.unidadeensino = funcionariocargo.unidadeensino");
		sql.append(" 		inner join funcionario coordenador on cursocoordenador.funcionario = coordenador.codigo");
		sql.append(" 		inner join pessoa on pessoa.codigo = coordenador.pessoa");
		sql.append(" 		inner join curso on curso.codigo = cursocoordenador.curso ");
		if(!trazerRespondido){
			sql.append(" 		left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sql.append(" 		and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
			sql.append(" 		and respostaavaliacaoinstitucionaldw.coordenador = coordenador.pessoa");
		}
		sql.append(" 		where funcionariocargo.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append("   	and cursocoordenador.tipocoordenadorcurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		sql.append(" 		and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		sql.append(" 		and cargo.codigo =  ").append(avaliacaoInstitucionalVO.getCargo().getCodigo());
		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sql.append(" 		and curso.nivelEducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		sql.append(" 		and pessoa.ativo = true and funcionariocargo.ativo ");
		sql.append(" 		and pessoa.codigo != ").append(usuario.getPessoa().getCodigo());
		sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");			
		sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and avaliacaoinstitucionalpessoaavaliada.pessoa = pessoa.codigo )) ");
		if(!trazerRespondido){
			sql.append(" 		and respostaavaliacaoinstitucionaldw.codigo is null ");
		}
		sql.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getCoordenador().setCodigo(rs.getInt("codigo"));
			questionarioVO.getCoordenador().setNome(rs.getString("nome"));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoordenador(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_TodosCoordenadores()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoColaboradorAvaliandoInstituicao(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_ColaboradorInstituicao()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}

	@Override
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoProfessorAvaliandoCoodenador(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_ProfessorCoordenador()) {
			return null;
		}
		StringBuilder sql = new StringBuilder("select codigo, nome from pessoa ");
		sql.append(" where codigo in ( ");
		sql.append(" 	select distinct funcionario.pessoa as coordenador from horarioturma ");
		sql.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
		sql.append(" 	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
		sql.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
		sql.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
		sql.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
		sql.append(" 	inner join cursocoordenador on curso.codigo = cursocoordenador.curso and cursocoordenador.unidadeensino = turma.unidadeensino ");
		sql.append("	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append("	inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		if(!trazerRespondido){
			sql.append(" 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sql.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" ");
			sql.append(" 	and respostaavaliacaoinstitucionaldw.coordenador = funcionario.pessoa ");
		}
		sql.append(" 	where turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append("   	and cursocoordenador.tipocoordenadorcurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		sql.append(" 	and pessoa.ativo = true ");
		sql.append(" 	and pessoa.codigo != ").append(usuario.getPessoa().getCodigo());
		sql.append(" 	and horarioturmadiaitem.professor  = "+usuario.getPessoa().getCodigo()+" ");
		if (avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" and	horarioturmadia.data::DATE >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' and horarioturmadia.data::DATE <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
		} else if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && !avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		} else {
			sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if(!trazerRespondido){
			sql.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null  ");
		}
		sql.append(" ) ");
		sql.append(" order by nome ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x= 1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getCoordenador().setCodigo(rs.getInt("codigo"));
			questionarioVO.getCoordenador().setNome(rs.getString("nome"));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVOs.add(questionarioVO);
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			x++;
		}
		return questionarioVOs;
	}

	@Override
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoDepartamento(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_CoordenadorDepartamento()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.getDepartamento().setCodigo(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		questionarioVO.getDepartamento().setNome(avaliacaoInstitucionalVO.getDepartamento().getNome());
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}

	@Override
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoCargo(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_CoordenadorCargo()) {
			return null;
		}
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
		questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
		questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
		questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
		questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
		questionarioVO.getDepartamento().setCodigo(avaliacaoInstitucionalVO.getDepartamento().getCodigo());
		questionarioVO.getDepartamento().setNome(avaliacaoInstitucionalVO.getDepartamento().getNome());
		questionarioVO.getCargo().setCodigo(avaliacaoInstitucionalVO.getCargo().getCodigo());
		questionarioVO.getCargo().setNome(avaliacaoInstitucionalVO.getCargo().getNome());
		questionarioVO.setAbrirToggle(Boolean.TRUE);
		questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
		questionarioVOs.add(questionarioVO);
		return questionarioVOs;
	}

	@Override
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_CoordenadorAvaliacaoCurso()) {
			return null;
		}
		int x = 1;		
		StringBuilder sql = new StringBuilder("");
		sql.append("select distinct curso.codigo, curso.nome from curso ");
		sql.append(" inner join cursocoordenador on cursocoordenador.curso = curso.codigo ");
		sql.append(" inner join funcionario on cursocoordenador.funcionario = funcionario.codigo ");
		sql.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		sql.append(" and cursocoordenador.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getNivelEducacional())) {
			sql.append(" and curso.nivelEducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append(" and cursocoordenador.tipocoordenadorcurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		sql.append(" order by curso.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);		
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.setCodigoCurso(rs.getInt("codigo"));
			questionarioVO.setCurso(rs.getString("nome"));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVOs.add(questionarioVO);
		}
		return questionarioVOs;
	}

	
	@Override
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoProfessor(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_CoordenadorProfessor()) {
			return null;
		}
		StringBuilder sql = new StringBuilder("select codigo, nome from pessoa ");
		sql.append(" where codigo in ( ");
		sql.append(" 	select distinct t.professor as professor from horarioturmadetalhado(null,  null, null, null) as t ");
		sql.append(" 	inner join horarioturma on horarioturma.codigo = t.horarioturma ");
		sql.append(" 	inner join turma on turma.codigo = t.turma  ");
		sql.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
		sql.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
		sql.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
		sql.append(" 	inner join cursocoordenador on curso.codigo = cursocoordenador.curso and cursocoordenador.unidadeensino = turma.unidadeensino ");
		sql.append("	 	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append("	 	inner join pessoa on pessoa.codigo = t.professor  ");
		if(!trazerRespondido){
			sql.append(" 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sql.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa  ");
			sql.append(" 	and respostaavaliacaoinstitucionaldw.professor = t.professor ");
		}
		sql.append(" 	where turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append("   	and cursocoordenador.tipocoordenadorcurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sql.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		sql.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		sql.append(" 	and pessoa.codigo != ").append(usuario.getPessoa().getCodigo());
		sql.append(" 	and pessoa.ativo = true ");
		if (avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" and	t.data::DATE >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicio())).append("' and t.data::DATE <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataFinal())).append("' ");
		} else if (!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && !avaliacaoInstitucionalVO.getSemestre().trim().isEmpty()) {
			sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		} else {
			sql.append(" 	and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		sql.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
		if(!trazerRespondido){
			sql.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null  ");
		}
		sql.append(" ) ");
		sql.append(" order by nome ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 1;
		while (rs.next()) {
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.getProfessor().setCodigo(rs.getInt("codigo"));
			questionarioVO.getProfessor().setNome(rs.getString("nome"));
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVO.setAbrirToggle(x==1);
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoDisciplinaQuestionario(AvaliacaoInstitucionalVO obj, String matricula, MatriculaPeriodoVO matriculaperiodoVO, Integer unidadeEnsino, Boolean trazerRespondido, UsuarioVO usuario) throws Exception {
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplina = null;
		if(obj.getAvaliacaoUltimoModulo()) {
			listaDisciplina = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarTurmaDisciplinaUltimoModuloTurmaVisaoAlunoPorAvaliacaoInstitucional(usuario, obj, matricula, trazerRespondido);				
		} else {
			listaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaEMatriculaPeriodoPorAvaliacaoInstitucional(matriculaperiodoVO.getMatricula(), obj.getDisciplina().getCodigo(), matriculaperiodoVO.getCodigo(), obj, trazerRespondido, false, usuario);
			
		}
		
		for (MatriculaPeriodoTurmaDisciplinaVO disciplinaVO : listaDisciplina) {
			Integer professor = 0;			
			QuestionarioVO questionarioVO = null;
			if(!Uteis.isAtributoPreenchido(disciplinaVO.getTurmaPratica()) && !Uteis.isAtributoPreenchido(disciplinaVO.getTurmaTeorica()) && disciplinaVO.getTurma().getConsiderarTurmaAvaliacaoInstitucional()){
				if(!obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().isEmpty() && Uteis.isAtributoPreenchido(disciplinaVO.getTurma()) && !usuario.getIsApresentarVisaoProfessor()) {
					questionarioVOs.addAll(criarQuestionarioDisciplinaVariosProfessores(obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs(), obj.getQuestionarioVO(), disciplinaVO, disciplinaVO.getTurma(), matriculaperiodoVO, usuario));
				}else {
					questionarioVO = criarQuestionarioDisciplina(Uteis.isAtributoPreenchido(disciplinaVO.getProfessor()) ? disciplinaVO.getProfessor() : null , obj.getQuestionarioVO(), disciplinaVO, disciplinaVO.getTurma(), matriculaperiodoVO, usuario);
				}
			} 
			if (Uteis.isAtributoPreenchido(questionarioVO) && (professor.equals(0) || !professor.equals(questionarioVO.getProfessor().getCodigo())) && Uteis.isAtributoPreenchido(questionarioVO.getProfessor().getCodigo())) {
				questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(obj.getPublicoAlvo()));
				questionarioVOs.add(questionarioVO);	
				professor = questionarioVO.getProfessor().getCodigo();
			}			
			questionarioVO = new QuestionarioVO();
			if(Uteis.isAtributoPreenchido(disciplinaVO.getTurmaPratica()) && disciplinaVO.getTurmaPratica().getConsiderarTurmaAvaliacaoInstitucional()){
				if(!obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().isEmpty() && Uteis.isAtributoPreenchido(disciplinaVO.getTurma()) && !usuario.getIsApresentarVisaoProfessor()) {
					questionarioVOs.addAll(criarQuestionarioDisciplinaVariosProfessores(obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs(), obj.getQuestionarioVO(), disciplinaVO, disciplinaVO.getTurmaPratica(), matriculaperiodoVO, usuario));
				}else {
					questionarioVO = criarQuestionarioDisciplina(null, obj.getQuestionarioVO(), disciplinaVO, disciplinaVO.getTurmaPratica(), matriculaperiodoVO, usuario);
				}
			}
			if (Uteis.isAtributoPreenchido(questionarioVO) && (professor.equals(0) || !professor.equals(questionarioVO.getProfessor().getCodigo())) && Uteis.isAtributoPreenchido(questionarioVO.getProfessor().getCodigo())) {
				questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(obj.getPublicoAlvo()));
				questionarioVOs.add(questionarioVO);	
				professor = questionarioVO.getProfessor().getCodigo();
			}
			questionarioVO = new QuestionarioVO();
			if(Uteis.isAtributoPreenchido(disciplinaVO.getTurmaTeorica()) && disciplinaVO.getTurmaTeorica().getConsiderarTurmaAvaliacaoInstitucional()){
				if(!obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().isEmpty() && Uteis.isAtributoPreenchido(disciplinaVO.getTurma()) && !usuario.getIsApresentarVisaoProfessor()) {
					questionarioVOs.addAll(criarQuestionarioDisciplinaVariosProfessores(obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs(), obj.getQuestionarioVO(), disciplinaVO, disciplinaVO.getTurmaTeorica(), matriculaperiodoVO, usuario));
				}else {
					questionarioVO = criarQuestionarioDisciplina(null, obj.getQuestionarioVO(), disciplinaVO, disciplinaVO.getTurmaTeorica(), matriculaperiodoVO, usuario);
				}
			}
			if (Uteis.isAtributoPreenchido(questionarioVO) && (professor.equals(0) || !professor.equals(questionarioVO.getProfessor().getCodigo())) && Uteis.isAtributoPreenchido(questionarioVO.getProfessor().getCodigo())) {
				questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(obj.getPublicoAlvo()));
				questionarioVOs.add(questionarioVO);	
				professor = questionarioVO.getProfessor().getCodigo();
			}
		}
		if(!questionarioVOs.isEmpty()){
			questionarioVOs.get(0).setAbrirToggle(true);
		}
		return questionarioVOs;	
	}

	public List<QuestionarioVO> criarQuestionarioDisciplinaVariosProfessores(List<AvaliacaoInstitucionalPessoaAvaliadaVO> avaliacaoInstitucionalPessoaAvaliadaVOs, QuestionarioVO objPrincipal, MatriculaPeriodoTurmaDisciplinaVO mptdVO, TurmaVO turmaVO, MatriculaPeriodoVO matriculaperiodoVO, UsuarioVO usuarioVO) throws Exception {
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		QuestionarioVO questionarioVO =null;
		if(!avaliacaoInstitucionalPessoaAvaliadaVOs.isEmpty() && Uteis.isAtributoPreenchido(turmaVO) && !usuarioVO.getIsApresentarVisaoProfessor()) {
			List<PessoaVO> listaProfessoresVOs = new ArrayList<PessoaVO>(0);
			if(Uteis.isAtributoPreenchido(mptdVO.getProfessor())) {
				listaProfessoresVOs.add(mptdVO.getProfessor());
			}else {
				listaProfessoresVOs.addAll(getFacadeFactory().getPessoaFacade().consultarPorTurma(turmaVO.getCodigo().intValue(), mptdVO.getDisciplina().getCodigo(), matriculaperiodoVO.getAno(), matriculaperiodoVO.getSemestre(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, usuarioVO));
			}
			for(PessoaVO professorVO :  listaProfessoresVOs) {
				for(AvaliacaoInstitucionalPessoaAvaliadaVO avaliacaoInstitucionalPessoaAvaliadaVO: avaliacaoInstitucionalPessoaAvaliadaVOs) {
					if(avaliacaoInstitucionalPessoaAvaliadaVO.getPessoaVO().getCodigo().equals(professorVO.getCodigo())) {
						questionarioVO = criarQuestionarioDisciplina(professorVO, objPrincipal, mptdVO, turmaVO, matriculaperiodoVO, usuarioVO);
						questionarioVOs.add(questionarioVO);
					}
				}
			}
		}
		return questionarioVOs;
	}
	public QuestionarioVO criarQuestionarioDisciplina(PessoaVO professorEspecifico, QuestionarioVO objPrincipal, MatriculaPeriodoTurmaDisciplinaVO mptdVO, TurmaVO turmaVO, MatriculaPeriodoVO matriculaperiodoVO, UsuarioVO usuarioVO) throws Exception {
		QuestionarioVO questionarioVO = new QuestionarioVO();
		questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(objPrincipal.getPerguntaQuestionarioVOs(), mptdVO.getDisciplina().getCodigo()));		
//		if (objPrincipal.getEscopo().equals("UM")) {	
//			if (!Uteis.isAtributoPreenchido(turmaVO)) {	
//				if (usuarioVO.getTurmaAvaliacaoInstitucionalProfessor() == null) {
//					turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaMatriculaPeriodo(mptdVO.getDisciplina().getCodigo(), matriculaperiodoVO.getCodigo(), false, usuarioVO);
//				} else {
//					turmaVO = usuarioVO.getTurmaAvaliacaoInstitucionalProfessor();
//				}
//			}
//		} else {
			if (!Uteis.isAtributoPreenchido(turmaVO)) {				
				if (usuarioVO.getTurmaAvaliacaoInstitucionalProfessor() == null) {
					turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaMatriculaPeriodo(mptdVO.getDisciplina().getCodigo(), matriculaperiodoVO.getCodigo(), false, usuarioVO);
				} else {
					turmaVO = usuarioVO.getTurmaAvaliacaoInstitucionalProfessor();
				}
			}			
//		}
		if (turmaVO != null && !turmaVO.getCodigo().equals(0)) {
			if (usuarioVO.getIsApresentarVisaoProfessor()) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), false, usuarioVO);
				if (funcionarioVO != null && !funcionarioVO.getCodigo().equals(0)) {
					questionarioVO.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(funcionarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
					questionarioVO.setTurmaVO(turmaVO.getCodigo());
				}
			} else {
				if(Uteis.isAtributoPreenchido(professorEspecifico)) {
					questionarioVO.setProfessor(professorEspecifico);
					questionarioVO.setTurmaVO(turmaVO.getCodigo());
					
				}else {
					ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO = new ProfessorTitularDisciplinaTurmaVO();
					professorTitularDisciplinaTurmaVO.setAno(matriculaperiodoVO.getAno());
					professorTitularDisciplinaTurmaVO.setSemestre(matriculaperiodoVO.getSemestre());
					professorTitularDisciplinaTurmaVO.setTurma(turmaVO);
					professorTitularDisciplinaTurmaVO.getDisciplina().setCodigo(mptdVO.getDisciplina().getCodigo());
//					professorTitularDisciplinaTurmaVO = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaAgrupadaTitular(professorTitularDisciplinaTurmaVO, turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, mptdVO.getMatricula(), usuarioVO);
					if (professorTitularDisciplinaTurmaVO != null) {
						Integer codigoProfessor = professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getCodigo();
						questionarioVO.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigoProfessor, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
						questionarioVO.setTurmaVO(professorTitularDisciplinaTurmaVO.getTurma().getCodigo());
					}
				}
			}
		}			
		questionarioVO.setCodigo(objPrincipal.getCodigo());
		questionarioVO.setDescricao(objPrincipal.getDescricao());
		questionarioVO.setSituacao(objPrincipal.getSituacao());
		questionarioVO.setEscopo(objPrincipal.getEscopo());
		questionarioVO.setDisciplinaVO(mptdVO.getDisciplina());
		questionarioVO.setCodigoCurso(matriculaperiodoVO.getMatriculaVO().getCurso().getCodigo());
		questionarioVO.setCurso(matriculaperiodoVO.getMatriculaVO().getCurso().getNome());
		questionarioVO.setTurmaVO(turmaVO.getCodigo());
		questionarioVO.setIdentificadorTurma(turmaVO.getIdentificadorTurma());
		if (questionarioVO.getEscopo().equals("UM")) {
			questionarioVO.setAbrirToggle(Boolean.TRUE);
		} else {
			questionarioVO.setAbrirToggle(Boolean.FALSE);
			
		}
		return questionarioVO;
	}

	public List<PerguntaQuestionarioVO> realizarCriacaoPerguntaQuestionarioVOs(List<PerguntaQuestionarioVO> perguntaQuestionarioVOs, Integer disciplina) {
		List<PerguntaQuestionarioVO> lista = new ArrayList<PerguntaQuestionarioVO>(0);
		PerguntaQuestionarioVO obj = null;
		for (PerguntaQuestionarioVO perguntaQuestionarioVO : perguntaQuestionarioVOs) {
			obj = new PerguntaQuestionarioVO();
			obj.setCodigo(perguntaQuestionarioVO.getCodigo());
			obj.getPergunta().setCodigo(perguntaQuestionarioVO.getPergunta().getCodigo());
			obj.getPergunta().setDescricao(perguntaQuestionarioVO.getPergunta().getDescricao());
			obj.getPergunta().setSelecionado(perguntaQuestionarioVO.getPergunta().getSelecionado());
			obj.getPergunta().setTipoResposta(perguntaQuestionarioVO.getPergunta().getTipoResposta());
			obj.getPergunta().setRespostaPerguntaVOs(realizarCriacaoRespostaPerguntaVOs(perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs(), disciplina));
			obj.getPergunta().setLayoutPergunta(perguntaQuestionarioVO.getPergunta().getLayoutPergunta());
			obj.setQuestionario(perguntaQuestionarioVO.getQuestionario());
			obj.setRespostaObrigatoria(perguntaQuestionarioVO.getRespostaObrigatoria());
			obj.setOrdem(perguntaQuestionarioVO.getOrdem());
			lista.add(obj);
		}
		Ordenacao.ordenarLista(lista, "ordem");
		return lista;
	}

	public List<RespostaPerguntaVO> realizarCriacaoRespostaPerguntaVOs(List<RespostaPerguntaVO> respostaPerguntaVOs, Integer disciplina) {
		List<RespostaPerguntaVO> lista = new ArrayList<RespostaPerguntaVO>(0);
		RespostaPerguntaVO obj = null;
		for (RespostaPerguntaVO respostaPerguntaVO : respostaPerguntaVOs) {
			obj = new RespostaPerguntaVO();
			obj.setCodigo(respostaPerguntaVO.getCodigo());
			obj.setPergunta(respostaPerguntaVO.getPergunta());
			obj.setTipoPergunta(respostaPerguntaVO.getTipoPergunta());
			obj.setDescricao(respostaPerguntaVO.getDescricao());
			obj.setApresentarRespostaAdicional(respostaPerguntaVO.getApresentarRespostaAdicional());
			obj.setDisciplina(disciplina);
			obj.setOrdem(respostaPerguntaVO.getOrdem());
			lista.add(obj);
		}
		Ordenacao.ordenarLista(lista, "ordem");
		return lista;
	}

	/*public void validarDadosListaRespostaEscopoDisciplina(List<QuestionarioVO> questionarioVOs, RespostaPerguntaVO respostaPerguntaVO) throws Exception {
		for (QuestionarioVO questionarioVO : questionarioVOs) {
			if (questionarioVO.getDisciplinaVO().getCodigo().equals(respostaPerguntaVO.getDisciplina())) {
				questionarioVO.varrerListaQuestionarioRetornarPerguntaRespondida(respostaPerguntaVO);
			}
		}
	}*/

	public void validarDadosCssApresentarHeaderDisciplina(List<QuestionarioVO> questionarioVOs, Boolean apresentarImportanciaPergunta) {
		Integer disciplinaTemp = 0;
		Boolean selecionado = Boolean.FALSE;
		Boolean selecionadoSimplesEscolha = Boolean.FALSE;
		Boolean selecionadoMultiplaEscolha = Boolean.FALSE;
		try {
			for (QuestionarioVO questionarioVO : questionarioVOs) {
				
				if (disciplinaTemp.equals(questionarioVO.getDisciplinaVO().getCodigo()) || questionarioVO.getDisciplinaVO().getCodigo().equals(0)) {
					disciplinaTemp = questionarioVO.getDisciplinaVO().getCodigo();

					for (PerguntaQuestionarioVO perguntaQuestionarioVO : questionarioVO.getPerguntaQuestionarioVOs()) {					
						if (perguntaQuestionarioVO.getPergunta().getTipoResposta().equals("TE")) {
							selecionado = Boolean.FALSE;
							if (!perguntaQuestionarioVO.getPergunta().getTexto().trim().isEmpty()) {
								selecionado = Boolean.TRUE;
								questionarioVO.setTodasPerguntasRespondidas(Boolean.TRUE);
							}
							if (!selecionado) {
								questionarioVO.setTodasPerguntasRespondidas(Boolean.FALSE);
								break;
							}
							if (apresentarImportanciaPergunta) {
								if (perguntaQuestionarioVO.getPergunta().getPeso() == 0) {
									questionarioVO.setTodasPerguntasRespondidas(Boolean.FALSE);
									break;
								}
							}
						} else if (perguntaQuestionarioVO.getPergunta().getTipoResposta().equals("SE")) {
							selecionadoSimplesEscolha = Boolean.FALSE;
							for (RespostaPerguntaVO respostaPerguntaVO : perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs()) {
								if (respostaPerguntaVO.getSelecionado()) {
									selecionadoSimplesEscolha = Boolean.TRUE;
									questionarioVO.setTodasPerguntasRespondidas(Boolean.TRUE);
								}
							}
							if (!selecionadoSimplesEscolha) {
								questionarioVO.setTodasPerguntasRespondidas(Boolean.FALSE);
								break;
							}
							if (apresentarImportanciaPergunta) {
								if (perguntaQuestionarioVO.getPergunta().getPeso() == 0) {
									questionarioVO.setTodasPerguntasRespondidas(Boolean.FALSE);
									break;
								}
							}
						} else if (perguntaQuestionarioVO.getPergunta().getTipoResposta().equals("ME")) {
							selecionadoMultiplaEscolha = Boolean.FALSE;
							for (RespostaPerguntaVO respostaPerguntaVO : perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs()) {
								if (respostaPerguntaVO.getSelecionado()) {
									selecionadoMultiplaEscolha = Boolean.TRUE;
									questionarioVO.setTodasPerguntasRespondidas(Boolean.TRUE);
								}
							}
							if (!selecionadoMultiplaEscolha) {
								questionarioVO.setTodasPerguntasRespondidas(Boolean.FALSE);
								break;
							}
							if (apresentarImportanciaPergunta) {
								if (perguntaQuestionarioVO.getPergunta().getPeso() == 0) {
									questionarioVO.setTodasPerguntasRespondidas(Boolean.FALSE);
									break;
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoAlunoCoordenador(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_AlunoCoordenador()) {
			return null;
		}
		StringBuilder sql = new StringBuilder("select distinct pessoa.codigo, pessoa.nome from cursocoordenador ");
		sql.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sql.append("   	inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sql.append("   	where pessoa.ativo = true ");
		sql.append("   	and cursocoordenador.unidadeensino = ").append(matriculaVO.getUnidadeEnsino().getCodigo());
		sql.append("   	and cursocoordenador.curso = ").append(matriculaVO.getCurso().getCodigo());
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTipoCoordenadorCurso()) && !avaliacaoInstitucionalVO.getTipoCoordenadorCurso().equals(TipoCoordenadorCursoEnum.AMBOS)) {
			sql.append("   	and cursocoordenador.tipocoordenadorcurso = '").append(avaliacaoInstitucionalVO.getTipoCoordenadorCurso().name()).append("' ");
		}
		sql.append("   	and case when cursocoordenador.turma is not null then  cursocoordenador.turma = ").append(matriculaPeriodoVO.getTurma().getCodigo()).append(" else true end ");
		if(!trazerRespondido){
			sql.append("   	and not exists (select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw ");
			sql.append("   	where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo());
			sql.append("   	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo());
			sql.append("   	and respostaavaliacaoinstitucionaldw.coordenador = pessoa.codigo limit 1)");
		}
		sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" limit 1) ");
		sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");			
		sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
		sql.append("   	and avaliacaoinstitucionalpessoaavaliada.pessoa = pessoa.codigo ");
		sql.append("   	)) order by pessoa.nome ");
			
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		while(rs.next()){
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.setAbrirToggle(Boolean.TRUE);
			questionarioVO.setCodigoCurso(matriculaVO.getCurso().getCodigo());
			questionarioVO.getCoordenador().setCodigo(rs.getInt("codigo"));
			questionarioVO.getCoordenador().setNome(rs.getString("nome"));
			questionarioVO.setCurso(matriculaVO.getCurso().getNome());
			questionarioVO.setTurmaVO(matriculaPeriodoVO.getTurma().getCodigo());
			questionarioVO.setIdentificadorTurma(matriculaPeriodoVO.getTurma().getIdentificadorTurma());
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVOs.add(questionarioVO);
		}
		return questionarioVOs;
	}
	
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoProfessorTurma(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_ProfessorTurma()) {
			return null;
		}
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" 	select distinct turma.codigo, turma.identificadorturma, turma.curso as codigo_curso, array_to_string(array_agg(distinct curso.nome order by curso.nome), ', ') as curso from horarioturma ");
		sqlStr.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
		sqlStr.append(" 	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
		sqlStr.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
		sqlStr.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
		sqlStr.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");				
		sqlStr.append(" 	where turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sqlStr.append(" 	and horarioturmadiaitem.professor = ").append(usuario.getPessoa().getCodigo()).append(" ");
		sqlStr.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina())){
			sqlStr.append(" 	and horarioturmadiaitem.disciplina = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTurma())){
			sqlStr.append(" 	and (turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
			sqlStr.append(" 	or (turma.turmaagrupada and ").append(avaliacaoInstitucionalVO.getTurma().getCodigo()).append(" in (select turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo))) ");
		}
		
		if (avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("PROFESSOR_TURMA") || avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo().equals("DI")) {
			sqlStr.append(" AND turma.considerarTurmaAvaliacaoInstitucional = true ");			
		}

		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sqlStr.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getAno().length() == 4){
			sqlStr.append("     and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getSemestre().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().length() == 1){
			sqlStr.append("     and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		if(avaliacaoInstitucionalVO.getIsApresentarPeriodoAula()){
			sqlStr.append("     and horarioturmadia.data >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' ");
			sqlStr.append("     and horarioturmadia.data <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
		}		
		if(!trazerRespondido){
			sqlStr.append("     and not exists(select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			sqlStr.append("     and respostaavaliacaoinstitucionaldw.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			sqlStr.append("     and respostaavaliacaoinstitucionaldw.turma = turma.codigo ");
			sqlStr.append("     and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" limit 1) ");
		}
		sqlStr.append(" group by turma.codigo, turma.identificadorturma, turma.curso	order by identificadorturma ");
		
			
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 0;
		while(rs.next()){
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.setAbrirToggle(x==0);								
			questionarioVO.setTurmaVO(rs.getInt("codigo"));
			questionarioVO.setIdentificadorTurma(rs.getString("identificadorturma"));
			questionarioVO.setCodigoCurso(rs.getInt("codigo_curso"));
			questionarioVO.setCurso(rs.getString("curso"));
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}
	
	public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoProfessorCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean trazerRespondido) throws Exception {
		if (!avaliacaoInstitucionalVO.getPublicoAlvo_ProfessorCurso()) {
			return null;
		}
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" 	select distinct curso.codigo, curso.nome from horarioturma ");
		sqlStr.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
		sqlStr.append(" 	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
		sqlStr.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
		sqlStr.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
		sqlStr.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");				
		sqlStr.append(" 	where turma.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
		sqlStr.append(" 	and horarioturmadiaitem.professor = ").append(usuario.getPessoa().getCodigo()).append(" ");
		sqlStr.append(getSqlCondicaoWhereCurso(avaliacaoInstitucionalVO, "curso.codigo", "and"));
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getDisciplina())){
			sqlStr.append(" 	and horarioturmadiaitem.disciplina = ").append(avaliacaoInstitucionalVO.getDisciplina().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getTurma())){
			sqlStr.append(" 	and (turma.codigo = ").append(avaliacaoInstitucionalVO.getTurma().getCodigo());
			sqlStr.append(" 	or (turma.turmaagrupada and ").append(avaliacaoInstitucionalVO.getTurma().getCodigo()).append(" in (select turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo))) ");
		}
		if(!avaliacaoInstitucionalVO.getNivelEducacional().trim().isEmpty()){
			sqlStr.append(" 	and curso.niveleducacional = '").append(avaliacaoInstitucionalVO.getNivelEducacional()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getAno().trim().isEmpty() && avaliacaoInstitucionalVO.getAno().length() == 4){
			sqlStr.append("     and horarioturma.anovigente = '").append(avaliacaoInstitucionalVO.getAno()).append("' ");
		}
		if(!avaliacaoInstitucionalVO.getSemestre().trim().isEmpty() && avaliacaoInstitucionalVO.getSemestre().length() == 1){
			sqlStr.append("     and horarioturma.semestrevigente = '").append(avaliacaoInstitucionalVO.getSemestre()).append("' ");
		}
		if(avaliacaoInstitucionalVO.getIsApresentarPeriodoAula()){
			sqlStr.append("     and horarioturmadia.data >= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioAula())).append("' ");
			sqlStr.append("     and horarioturmadia.data <= '").append(Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoAula())).append("' ");
		}	
		if(!trazerRespondido){
			sqlStr.append("     and not exists(select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" ");
			sqlStr.append("     and respostaavaliacaoinstitucionaldw.unidadeensino = ").append(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo()).append(" ");
			sqlStr.append("     and respostaavaliacaoinstitucionaldw.curso = curso.codigo ");
			sqlStr.append("     and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" limit 1) ");
		}
		sqlStr.append(" 	order by curso.nome ");
		
		
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<QuestionarioVO> questionarioVOs = new ArrayList<QuestionarioVO>(0);
		int x = 0;
		while(rs.next()){
			QuestionarioVO questionarioVO = new QuestionarioVO();
			questionarioVO.getPerguntaQuestionarioVOs().addAll(realizarCriacaoPerguntaQuestionarioVOs(avaliacaoInstitucionalVO.getQuestionarioVO().getPerguntaQuestionarioVOs(), 0));
			questionarioVO.setCodigo(avaliacaoInstitucionalVO.getQuestionarioVO().getCodigo());
			questionarioVO.setDescricao(avaliacaoInstitucionalVO.getQuestionarioVO().getDescricao());
			questionarioVO.setSituacao(avaliacaoInstitucionalVO.getQuestionarioVO().getSituacao());
			questionarioVO.setEscopo(avaliacaoInstitucionalVO.getQuestionarioVO().getEscopo());
			questionarioVO.setAbrirToggle(x==0);			
			questionarioVO.setCodigoCurso(rs.getInt("codigo"));
			questionarioVO.setCurso(rs.getString("nome"));
			questionarioVO.setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional.getEnum(avaliacaoInstitucionalVO.getPublicoAlvo()));
			questionarioVOs.add(questionarioVO);
			x++;
		}
		return questionarioVOs;
	}

	
	public StringBuilder getSqlCondicaoWhereCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String campoComparacao, String andOr) {
		StringBuilder sql = new StringBuilder();
		if(avaliacaoInstitucionalVO.getCurso().getCodigo() > 0 && avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().isEmpty()){
			sql.append(" ").append(andOr).append(" ").append(campoComparacao).append(" = ").append(avaliacaoInstitucionalVO.getCurso().getCodigo()).append(" ");
		}else if(!avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
			sql.append(" ").append(andOr).append(" ").append(" exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = ").append(avaliacaoInstitucionalVO.getCodigo()).append(" and avaliacaoinstitucionalcurso.curso = ").append(campoComparacao).append(" ) ");
		}
		return sql;
	}
	
	@Override
	public AvaliacaoInstitucionalVO realizarCarregamentoQuestionariosPorUsuarioAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception{
		avaliacaoInstitucionalVO = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorChavePrimaria(avaliacaoInstitucionalVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);         
		realizarCarregamentoDadosAlunoResponderAvaliacaoInstitucional(avaliacaoInstitucionalVO, matriculaVO, usuarioVO);
		if(matriculaVO == null || !Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
			matriculaVO = avaliacaoInstitucionalVO.getMatriculaVO();
		}
         if (avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
             if (usuarioVO.getIsApresentarVisaoAluno() && !matriculaVO.getMatricula().equals("")) {
	                avaliacaoInstitucionalVO.getQuestionarioVO().setCurso(matriculaVO.getCurso().getNome());
	                avaliacaoInstitucionalVO.getQuestionarioVO().setCodigoCurso(matriculaVO.getCurso().getCodigo());
             }                
             avaliacaoInstitucionalVO.setDataInicioAula(Uteis.obterDataAntiga(new Date(), avaliacaoInstitucionalVO.getDiasDisponivel()));
             avaliacaoInstitucionalVO.setDataTerminoAula(new Date());     		
         }
         avaliacaoInstitucionalVO.setQuestionarioVOs(new ArrayList<QuestionarioVO>());
         avaliacaoInstitucionalVO.setQuestionarioVOs(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().executarMontagemQuestionarioSerRespondidoPorAvaliacaoInstitucionalDeAcordoComPublicoAlvo(avaliacaoInstitucionalVO, usuarioVO, matriculaVO, avaliacaoInstitucionalVO.getMatriculaPeriodoVO(), false));
         getFacadeFactory().getRespostaAvaliacaoInstitucionalParcialFacade().realizarRecuperacaoRespostaParcial(avaliacaoInstitucionalVO, avaliacaoInstitucionalVO.getQuestionarioVOs(), matriculaVO, usuarioVO);               
         getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().validarDadosCssApresentarHeaderDisciplina(avaliacaoInstitucionalVO.getQuestionarioVOs(), avaliacaoInstitucionalVO.getInformarImportanciaPergunta());
         return avaliacaoInstitucionalVO;
	}
	
	@Override
	 public void realizarCarregamentoDadosAlunoResponderAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
	    	if(usuarioVO.getIsApresentarVisaoAluno()) {
	    		if(matriculaVO == null || !Uteis.isAtributoPreenchido(matriculaVO.getMatricula()) || !matriculaVO.getSituacao().equals("AT")) {
	    			List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade()
							.consultaRapidaBasicaPorCodigoPessoaNaoCancelada(usuarioVO.getPessoa().getCodigo(), false, false,
									true, true, usuarioVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO));
	    			if(Uteis.isAtributoPreenchido(matriculaVOs)) {
	    				matriculaVO = matriculaVOs.get(0);	    				
	    			}else {
	    				throw new Exception("Não foi encontado uma matrícula apta para responder esta avaliação institucional");
	    			}
	    		}	    		
	    		avaliacaoInstitucionalVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(matriculaVO.getMatricula(), avaliacaoInstitucionalVO.getSemestre(), avaliacaoInstitucionalVO.getAno(), true, false, Optional.ofNullable(null), Optional.ofNullable(null), usuarioVO));	    			    	
	    		avaliacaoInstitucionalVO.setMatriculaVO(matriculaVO);
	    	}
	  }
	
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 @Override
	 public void realizarValidacaoImportanciaPerguntaSelecionado(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, QuestionarioVO questionario, PerguntaQuestionarioVO obj, Integer pesoSelecionado, UsuarioVO usuarioVO) throws Exception {
		 q:
		 for(QuestionarioVO questionarioVO: avaliacaoInstitucionalVO.getQuestionarioVOs()) {			 
			 for(PerguntaQuestionarioVO perguntaQuestionarioVO: questionarioVO.getPerguntaQuestionarioVOs()){
	        		if(obj.getPergunta().getCodigo().equals(perguntaQuestionarioVO.getPergunta().getCodigo())){
	        			perguntaQuestionarioVO.getPergunta().setPeso(pesoSelecionado);
	        			continue q;
	        		}
	        	}
		 }
	 	 getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().validarDadosCssApresentarHeaderDisciplina(avaliacaoInstitucionalVO.getQuestionarioVOs(), avaliacaoInstitucionalVO.getInformarImportanciaPergunta());
	 	 getFacadeFactory().getRespostaAvaliacaoInstitucionalParcialFacade().gravarRespostaAvaliacaoInstitucionalParcial(avaliacaoInstitucionalVO.getMatriculaVO(), avaliacaoInstitucionalVO.getMatriculaPeriodoVO(), null, avaliacaoInstitucionalVO, questionario, obj, "", usuarioVO);
     
	 }
	 
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 @Override
	 public List<AvaliacaoInstitucionalVO> persistir(List<AvaliacaoInstitucionalVO> avaliacaoInstitucionalVOs, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception{
		UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
		if (avaliacaoInstitucionalVO.getMatriculaVO() != null
				&& Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO.getMatriculaVO().getMatricula())) {
			unidadeEnsinoVO.setCodigo(avaliacaoInstitucionalVO.getMatriculaVO().getUnidadeEnsino().getCodigo());
			unidadeEnsinoVO.setNome(avaliacaoInstitucionalVO.getMatriculaVO().getUnidadeEnsino().getNome());
		} else if (Uteis.isAtributoPreenchido(usuarioVO.getUnidadeEnsinoLogado())) {
			unidadeEnsinoVO.setCodigo(usuarioVO.getUnidadeEnsinoLogado().getCodigo());
			unidadeEnsinoVO.setNome(usuarioVO.getUnidadeEnsinoLogado().getNome());
		} else {
			unidadeEnsinoVO.setCodigo(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
			unidadeEnsinoVO.setNome(avaliacaoInstitucionalVO.getUnidadeEnsino().getNome());
		}
			
		  List<RespostaAvaliacaoInstitucionalDWVO> listaRespostaQuestionario = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().gerarListaRespostaAluno(avaliacaoInstitucionalVO.getMatriculaVO(), avaliacaoInstitucionalVO.getMatriculaPeriodoVO(), usuarioVO.getPessoa().getCodigo(), usuarioVO.getTipoPessoa(), unidadeEnsinoVO.getCodigo(),
				  avaliacaoInstitucionalVO, avaliacaoInstitucionalVO.getQuestionarioVO(), "", 0, 0, 0, 0, 0, true, avaliacaoInstitucionalVO.getQuestionarioVOs());
		  avaliacaoInstitucionalVO.setRespostaAvaliacaoInstitucionalDWVOs(new ArrayList<>());
		  avaliacaoInstitucionalVO.adicionarListaRespostaQuestionario(listaRespostaQuestionario);
          getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().incluirTodas(avaliacaoInstitucionalVO.getRespostaAvaliacaoInstitucionalDWVOs(), usuarioVO);
          if(getFacadeFactory().getAvaliacaoInstitucionalRespondenteInterfaceFacade().consultarAvaliacaoInstitucionalRespondenteExistentePorAvaliacaoInstitucional(avaliacaoInstitucionalVO, usuarioVO.getPessoa().getCodigo(), usuarioVO)) {
        	  getFacadeFactory().getAvaliacaoInstitucionalRespondenteInterfaceFacade().alterarSituacaoAvaliacaoRespondente(avaliacaoInstitucionalVO.getRespostaAvaliacaoInstitucionalDWVOs().iterator().next(), Boolean.TRUE, usuarioVO);
          }
          getFacadeFactory().getRespostaAvaliacaoInstitucionalParcialFacade().excluirPorAvaliacaoRespondente(avaliacaoInstitucionalVO.getCodigo(), usuarioVO.getPessoa().getCodigo(), avaliacaoInstitucionalVO.getMatriculaVO().getMatricula(), usuarioVO);
          int index = 0;
          for (AvaliacaoInstitucionalVO obj : avaliacaoInstitucionalVOs) {
              if (obj.getCodigo().equals(avaliacaoInstitucionalVO.getCodigo())) {              		
            	  avaliacaoInstitucionalVOs.remove(index);
                  break;
              }
              index++;
          }
          return avaliacaoInstitucionalVOs;
	 }
	 
	 @Override
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 public void realizarValidacaoRespostaQuestionario(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, QuestionarioVO questionarioVO, PerguntaQuestionarioVO perguntaQuestionarioVO, RespostaPerguntaVO respostaPerguntaVO, UsuarioVO usuarioVO) throws Exception {
		 if(respostaPerguntaVO != null && perguntaQuestionarioVO != null && !perguntaQuestionarioVO.getPergunta().getTipoRespostaTextual()) {
			 questionarioVO.varrerListaQuestionarioRetornarPerguntaRespondida(respostaPerguntaVO);
		 }
		 if (!usuarioVO.getPermiteSimularNavegacaoAluno()) {
			 getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().validarDadosCssApresentarHeaderDisciplina(avaliacaoInstitucionalVO.getQuestionarioVOs(), avaliacaoInstitucionalVO.getInformarImportanciaPergunta());
			 getFacadeFactory().getRespostaAvaliacaoInstitucionalParcialFacade().gravarRespostaAvaliacaoInstitucionalParcial(avaliacaoInstitucionalVO.getMatriculaVO(), avaliacaoInstitucionalVO.getMatriculaPeriodoVO(), null, avaliacaoInstitucionalVO, questionarioVO, perguntaQuestionarioVO, "", usuarioVO);
		 }
	 }
	 
	 
}
