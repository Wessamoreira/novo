package negocio.facade.jdbc.avaliacaoinst;

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

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.avaliacaoinst.RespostaAvaliacaoInstitucionalParcialInterfaceFacade;

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
public class RespostaAvaliacaoInstitucionalParcial extends ControleAcesso implements RespostaAvaliacaoInstitucionalParcialInterfaceFacade {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1665788607066818289L;

	public RespostaAvaliacaoInstitucionalParcial() throws Exception {
		super();		
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
			final String sql = "INSERT INTO RespostaAvaliacaoInstitucionalParcial( unidadeEnsino, curso, disciplina, avaliacaoInstitucional, questionario, pergunta, tipoPergunta, resposta, areaConhecimento, periodo, matriculaFuncionario, matriculaAluno, matriculaPeriodo, turno, tipoPessoa, pessoa, publicoAlvo, escopo, unidadeEnsinoCurso, turma, pesoPergunta, professor, inscricaoProcessoSeletivo, processoSeletivo, respostaAdicional, dataResposta, requerimento, departamentoTramite, ordemTramite, cargo, departamento, coordenador ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getUnidadeEnsino().intValue());
					sqlInserir.setInt(2, obj.getCurso().intValue());
					sqlInserir.setInt(3, obj.getDisciplina().intValue());
					sqlInserir.setInt(4, obj.getAvaliacaoInstitucional().intValue());
					sqlInserir.setInt(5, obj.getQuestionario().intValue());
					sqlInserir.setInt(6, obj.getPergunta().intValue());
					sqlInserir.setString(7, obj.getTipoPergunta());
					sqlInserir.setString(8, obj.getResposta());
					sqlInserir.setInt(9, obj.getAreaConhecimento().intValue());
					sqlInserir.setInt(10, obj.getPeriodo().intValue());					
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
					sqlInserir.setInt(13, obj.getMatriculaPeriodo().intValue());
					sqlInserir.setInt(14, obj.getTurno().intValue());
					sqlInserir.setString(15, obj.getTipoPessoa());
					sqlInserir.setInt(16, obj.getPessoa().intValue());
					sqlInserir.setString(17, obj.getPublicoAlvo());
					sqlInserir.setString(18, obj.getEscopo());
					sqlInserir.setInt(19, obj.getUnidadeEnsinoCurso().intValue());
					sqlInserir.setInt(20, obj.getTurma().intValue());
					sqlInserir.setInt(21, obj.getPesoPergunta().intValue());
					sqlInserir.setInt(22, obj.getProfessor().intValue());
					sqlInserir.setInt(23, obj.getInscricaoProcessoSeletivo().intValue());
					sqlInserir.setInt(24, obj.getProcessoSeletivo().intValue());
					sqlInserir.setString(25, obj.getRespostaAdicional());
					sqlInserir.setDate(26, Uteis.getDataJDBC(new Date()));
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
	public void alterar(final RespostaAvaliacaoInstitucionalDWVO obj, UsuarioVO usuarioVO) throws Exception {
		try {		
			final String sql = "UPDATE RespostaAvaliacaoInstitucionalParcial set unidadeEnsino=?, curso=?, disciplina=?, avaliacaoInstitucional=?, questionario=?, pergunta=?, tipoPergunta=?, resposta=?, areaConhecimento=?, periodo=?, matriculaFuncionario=?, matriculaAluno=?, matriculaPeriodo=?, turno=?, tipoPessoa=?, pessoa=?, publicoAlvo=?, escopo=?, unidadeEnsinoCurso=?, turma=?, pesoPergunta=?, professor=?,  inscricaoProcessoSeletivo=?, processoSeletivo=?, respostaAdicional = ?, dataResposta=?, requerimento = ?, departamentoTramite = ?, ordemTramite = ?, cargo = ?, departamento = ?, coordenador = ? WHERE ((codigo = ?)) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RespostaAvaliacaoInstitucionalDWVO obj, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("DELETE FROM RespostaAvaliacaoInstitucionalParcial WHERE codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), obj.getCodigo());		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorAvaliacaoRespondente(Integer avaliacao, Integer respondente, String matriculaAluno, UsuarioVO usuarioVO) throws Exception {
		if(Uteis.isAtributoPreenchido(matriculaAluno)){
			getConexao().getJdbcTemplate().update("DELETE FROM RespostaAvaliacaoInstitucionalParcial WHERE avaliacaoinstitucional = ? and pessoa = ? and matriculaaluno = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), avaliacao, respondente, matriculaAluno);
		}else{
			getConexao().getJdbcTemplate().update("DELETE FROM RespostaAvaliacaoInstitucionalParcial WHERE avaliacaoinstitucional = ? and pessoa = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), avaliacao, respondente);
		}
	}

	@Override
	public List<RespostaAvaliacaoInstitucionalDWVO> consultarPorAvaliacaoInstitucionalRespondente(Integer avaliacaoInstitucional, Integer respondente, String matriculaAluno) throws Exception {
		StringBuilder sql = new StringBuilder("select * from RespostaAvaliacaoInstitucionalParcial ");
		sql.append(" where avaliacaoinstitucional = ? and pessoa = ?  ");
		if(Uteis.isAtributoPreenchido(matriculaAluno)){
			sql.append(" and matriculaaluno = ? ");
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), avaliacaoInstitucional, respondente, matriculaAluno));
		}else{
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), avaliacaoInstitucional, respondente));
		}
	}
	
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
		obj.setInscricaoProcessoSeletivo(dadosSQL.getInt("inscricaoProcessoSeletivo"));
		obj.setProcessoSeletivo(dadosSQL.getInt("processoSeletivo"));

		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarRespostaAvaliacaoInstitucionalParcial(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, 
			UnidadeEnsinoVO unidadeEnsinoVO, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, QuestionarioVO questionarioVO, 
			PerguntaQuestionarioVO perguntaQuestionarioVO, String matriculaFuncionario, UsuarioVO usuarioVO) throws Exception{
		if(unidadeEnsinoVO == null) {	
			unidadeEnsinoVO =  new UnidadeEnsinoVO();
			if (matriculaVO != null && Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
				unidadeEnsinoVO.setCodigo(matriculaVO.getUnidadeEnsino().getCodigo());
				unidadeEnsinoVO.setNome(matriculaVO.getUnidadeEnsino().getNome());
			} else if (Uteis.isAtributoPreenchido(usuarioVO.getUnidadeEnsinoLogado())) {
				unidadeEnsinoVO.setCodigo(usuarioVO.getUnidadeEnsinoLogado().getCodigo());
				unidadeEnsinoVO.setNome(usuarioVO.getUnidadeEnsinoLogado().getNome());
			} else {
				unidadeEnsinoVO.setCodigo(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo());
				unidadeEnsinoVO.setNome(avaliacaoInstitucionalVO.getUnidadeEnsino().getNome());				
			}
		}
		realizarPreenchimentoRespostaAvaliacaoInstitucionalParcial(matriculaVO, matriculaPeriodoVO, unidadeEnsinoVO, avaliacaoInstitucionalVO, questionarioVO, perguntaQuestionarioVO, matriculaFuncionario, usuarioVO);		
		
		if(Uteis.isAtributoPreenchido(perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO())
				&& perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().getResposta().trim().isEmpty()){
			excluir(perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO(), usuarioVO);
			perguntaQuestionarioVO.setRespostaAvaliacaoInstitucionalDWVO(new RespostaAvaliacaoInstitucionalDWVO());
		}else if(!Uteis.isAtributoPreenchido(perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO())
				&& !perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().getResposta().trim().isEmpty()){
			incluir(perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO(), usuarioVO);
		}else if(Uteis.isAtributoPreenchido(perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO())
				&& !perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().getResposta().trim().isEmpty()){
			alterar(perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO(), usuarioVO);
		}
	}
	
	public void realizarPreenchimentoRespostaAvaliacaoInstitucionalParcial(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, 
			UnidadeEnsinoVO unidadeEnsinoVO, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, QuestionarioVO questionarioVO, 
			PerguntaQuestionarioVO perguntaQuestionarioVO, String matriculaFuncionario, UsuarioVO usuarioVO) throws Exception{
		if(!Uteis.isAtributoPreenchido(perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO())){
			perguntaQuestionarioVO.setRespostaAvaliacaoInstitucionalDWVO(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarCriacaoRespostaAvaliacao(usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoPais() ? matriculaVO : null, usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoPais() ? matriculaPeriodoVO : null, usuarioVO.getPessoa().getCodigo(), usuarioVO.getTipoPessoa(), unidadeEnsinoVO.getCodigo(), questionarioVO.getDisciplinaVO().getCodigo(), questionarioVO.getTurmaVO(), 
				avaliacaoInstitucionalVO, questionarioVO, perguntaQuestionarioVO.getPergunta(), matriculaFuncionario, 0, 0, 0, 0, 0, false, false, questionarioVO.getCargo().getCodigo(), 
				questionarioVO.getDepartamento().getCodigo(), questionarioVO.getCoordenador().getCodigo(), questionarioVO.getCodigoCurso()));
		}else{
			perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().setResposta("");
			if (questionarioVO.getEscopo().equals("DI")) {
				perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().setResposta(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().validarDadosRepostaPerguntaEscopoDisciplina(perguntaQuestionarioVO.getPergunta(), avaliacaoInstitucionalVO.getInformarImportanciaPergunta(), false, questionarioVO.getDisciplinaVO(), avaliacaoInstitucionalVO, questionarioVO));
				perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().setRespostaAdicional(perguntaQuestionarioVO.getPergunta().getTextoAdicional());
			} else {
				if (avaliacaoInstitucionalVO == null) {
					perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().setResposta(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().validarDadosRepostaPergunta(questionarioVO, perguntaQuestionarioVO.getPergunta(), false, false, avaliacaoInstitucionalVO));
					perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().setRespostaAdicional(perguntaQuestionarioVO.getPergunta().getTextoAdicional());
				} else {
					perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().setResposta(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().validarDadosRepostaPergunta(questionarioVO, perguntaQuestionarioVO.getPergunta(), avaliacaoInstitucionalVO.getInformarImportanciaPergunta(), false, avaliacaoInstitucionalVO));
					perguntaQuestionarioVO.getRespostaAvaliacaoInstitucionalDWVO().setRespostaAdicional(perguntaQuestionarioVO.getPergunta().getTextoAdicional());
				}
			}
		}
	}
	
	@Override
	public void realizarRecuperacaoRespostaParcial(final AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, final List<QuestionarioVO> questionarioVOs, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception{
		final List<RespostaAvaliacaoInstitucionalDWVO> respostaAvaliacaoInstitucionalDWVOs = consultarPorAvaliacaoInstitucionalRespondente(avaliacaoInstitucionalVO.getCodigo(), usuarioVO.getPessoa().getCodigo(), matriculaVO.getMatricula());		
		ProcessarParalelismo.executar(0, respostaAvaliacaoInstitucionalDWVOs.size(), null, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {
				try {
					realizarRecuperacaoRespostaParcialQuestionario(respostaAvaliacaoInstitucionalDWVOs.get(i), questionarioVOs);
				} catch (Exception e) {
					
				}				
			}
		});
		
	}
	
	public void realizarRecuperacaoRespostaParcialQuestionario(final RespostaAvaliacaoInstitucionalDWVO resposta, final List<QuestionarioVO> questionarioVOs) {
		for(QuestionarioVO questionarioVO: questionarioVOs){
			if(questionarioVO.getCodigoCurso().equals(resposta.getCurso())
				&& questionarioVO.getCargo().getCodigo().equals(resposta.getCargo())
				&& questionarioVO.getDepartamento().getCodigo().equals(resposta.getDepartamento())
				&& questionarioVO.getDisciplinaVO().getCodigo().equals(resposta.getDisciplina())
				&& questionarioVO.getProfessor().getCodigo().equals(resposta.getProfessor())
				&& questionarioVO.getCoordenador().getCodigo().equals(resposta.getCoordenador())
				&& questionarioVO.getTurmaVO().equals(resposta.getTurma())){
				realizarRecuperacaoRespostaParcialQuestionarioPergunta(resposta, questionarioVO);
				return;
			}
		}
	}
	
	public void realizarRecuperacaoRespostaParcialQuestionarioPergunta(final RespostaAvaliacaoInstitucionalDWVO resposta, final QuestionarioVO questionarioVO) {
		for(PerguntaQuestionarioVO perguntaQuestionarioVO: questionarioVO.getPerguntaQuestionarioVOs()){
			if(perguntaQuestionarioVO.getPergunta().getCodigo().equals(resposta.getPergunta())){
				perguntaQuestionarioVO.setRespostaAvaliacaoInstitucionalDWVO(resposta);
				if(perguntaQuestionarioVO.getPergunta().getTipoRespostaTextual()){
					perguntaQuestionarioVO.getPergunta().setTexto(resposta.getResposta());					
				}else{
					for(RespostaPerguntaVO respostaPerguntaVO: perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs()){
						String key = "["+respostaPerguntaVO.getCodigo()+"]";
						respostaPerguntaVO.setSelecionado(resposta.getResposta().contains(key));
						key += "{";
						if(resposta.getRespostaAdicional().contains(key)){
							String respAdd = resposta.getRespostaAdicional().substring(resposta.getRespostaAdicional().indexOf(key)+
									key.length(), resposta.getRespostaAdicional().length());
							respostaPerguntaVO.setRespostaAdicional(respAdd.substring(0, respAdd.indexOf("}")));
						}
						if(perguntaQuestionarioVO.getPergunta().getTipoRespostaSimplesEscolha() && respostaPerguntaVO.getSelecionado()){
							break;
						}
					}
				}
				if(resposta.getPesoPergunta() > 0){
					perguntaQuestionarioVO.getPergunta().setPeso(resposta.getPesoPergunta());
				}
				return;
			}
		}
	}
	
	
}
