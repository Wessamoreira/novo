package relatorio.negocio.interfaces.avaliacaoInst;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO;
import relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;

public interface AvaliacaoInstitucionalRelInterfaceFacade {

	
	public AvaliacaoInstucionalRelVO emitirRelatorioPerguntasTextuais(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma,
            List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes,  Date datainicio, Date dataFim, boolean agruparResposta) throws Exception;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#emitirRelatorioGrafico(java.lang
	 * .Integer, java.lang.Integer, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.util.List, java.util.List)
	 */
	public AvaliacaoInstucionalRelVO emitirRelatorioGrafico(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno,
			Integer turma, List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#emitirRelatorio(java.lang.Integer,
	 * java.lang.Integer, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.util.List, java.util.List)
	 */
	public AvaliacaoInstucionalRelVO emitirRelatorio(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma,
			List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#executarOrdenarListas(relatorio
	 * .negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO)
	 */
	public void executarOrdenarListas(AvaliacaoInstucionalRelVO obj);

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#
	 * incializarDadosPerguntaRespostaSemResposta(relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO,
	 * java.lang.Integer)
	 */
	public void incializarDadosPerguntaRespostaSemResposta(AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Integer avaliacaoInst) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#consultarDadosPergunta(relatorio
	 * .negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO, java.lang.Integer)
	 */
	public SqlRowSet consultarDadosPergunta(AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Integer avaliacaoInst) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#consultarRelatorio(java.lang.Integer
	 * , java.lang.Integer, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.util.List, relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO, java.lang.Boolean,
	 * java.util.List)
	 */
	public void consultarRelatorio(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma,
			List<DisciplinaVO> disciplinaVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Boolean relatorioTextual, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#montarDadosConsulta(java.sql.ResultSet
	 * , relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer)
	 */
	public void montarDadosConsulta(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, SqlRowSet tabelaResultado, Integer unidadeEnsino, AvaliacaoInstucionalRelVO obj, Integer avaliacaoInst, Integer turno, Integer turma, boolean agruparResposta) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#montarDadosQuestionario(java.sql
	 * .ResultSet, relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer)
	 */
	public void montarDadosQuestionario(SqlRowSet dadosSQL, QuestionarioRelVO obj, Integer avaliacaoInt, Integer turno, Integer turma, boolean agruparResposta) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#montarDadosPergunta(java.sql.ResultSet
	 * , relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO, relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO,
	 * java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public void montarDadosPergunta(SqlRowSet dadosSQL, PerguntaRelVO obj, QuestionarioRelVO questionario, Integer avaliacaoInst, Integer turno, Integer turma, boolean agruparResposta) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#montarDadosResposta(java.sql.ResultSet
	 * , relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO)
	 */
	public void montarDadosResposta(SqlRowSet dadosSql, PerguntaRelVO obj) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#consultarPesoPergunta(java.lang
	 * .Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.Integer)
	 */
	public SqlRowSet consultarPesoPergunta(Integer avaliacaoInst, Integer questionario, Integer perguntaVO, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplinaVO) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#consultarProfessor(java.lang.Integer
	 * , java.lang.Integer, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.util.List)
	 */
	public List<PessoaVO> consultarProfessor(Integer avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma,
			List<DisciplinaVO> disciplinaVOs) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRelInterfaceFacade#consultarRespostaPerguntaVO(java
	 * .lang.Integer)
	 */
	public List consultarRespostaPerguntaVO(Integer pergunta) throws Exception;
	
	public AvaliacaoInstucionalRelVO emitirRelatorioPorRespondente(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, Integer avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim) throws Exception;

	public List<PessoaVO> consultarRespondente(Integer avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, List<DisciplinaVO> disciplinaVOs) throws Exception;

	List<QuestionarioRelVO> consultarQuantidadeRelatorioSerGerado(Boolean relatorioRespondente, AvaliacaoInstitucionalVO avaliacaoInst, NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento,
			Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno,
			Integer turma, List<DisciplinaVO> disciplinaVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO,
			Boolean relatorioTextual, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio,
			Date dataFim) throws Exception;

	List<DisciplinaVO> consultarDisciplinas(Integer avaliacaoInst, Integer questionario, Integer unidadeEnsino,
			Integer curso, Integer turno, Integer turma) throws Exception;

	List<TurmaVO> consultarTurmas(String tipoConsulta, String valorConsulta, Integer avaliacaoInst,
			Integer questionario, Integer unidadeEnsino, Integer curso, Integer turno) throws Exception;

	List<CursoVO> consultarCursos(String tipoConsulta, String valorConsulta, Integer avaliacaoInst,
			Integer questionario, Integer unidadeEnsino) throws Exception;
	
	public List<QuestionarioRelVO> consultarQuantidadeRelatorioSerGeradoVisaoCoordenador(Boolean relatorioRespondente,int unidadeEnsino , AvaliacaoInstitucionalVO avaliacaoInst, NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento,  List<CursoCoordenadorVO> CursoCoordenadorVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO) throws Exception;


	List<UnidadeEnsinoVO> consultarUnidadeEnsinoComboBoxPorAvaliacaoInstitucionalPorQuestionario(Integer avaliacaoInst, Integer questionario, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<CursoVO> consultarCursosComboBoxPorAvaliacaoInstitucionalPorQuestionario(String tipoConsulta, String valorConsulta, Integer avaliacaoInst,
								  Integer questionario, Integer unidadeEnsino) throws Exception;
	public int consultarQuantidadeResposta(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional, AvaliacaoInstitucionalVO avaliacaoInst, Integer questionario, List<PerguntaVO> perguntaVOs, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma,
											List<DisciplinaVO> disciplinaVOs, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO, Boolean relatorioTextual, List<PessoaVO> listaProfessor, List<PessoaVO> listaRespondentes, Date dataInicio, Date dataFim, boolean agruparResposta, Boolean gerarRelatorioPublicacao, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception;

}