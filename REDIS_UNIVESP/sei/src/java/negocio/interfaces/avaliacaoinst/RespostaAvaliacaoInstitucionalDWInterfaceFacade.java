package negocio.interfaces.avaliacaoinst;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface RespostaAvaliacaoInstitucionalDWInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 */
	public RespostaAvaliacaoInstitucionalDWVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(RespostaAvaliacaoInstitucionalDWVO obj,UsuarioVO usuario) throws Exception;

	public void incluirTodas(List<RespostaAvaliacaoInstitucionalDWVO> listaResposta,UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(RespostaAvaliacaoInstitucionalDWVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(RespostaAvaliacaoInstitucionalDWVO obj) throws Exception;

	public Boolean consultarPorAvaliacaoInstitucionalRespondida(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public RespostaAvaliacaoInstitucionalDWVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception;

        public List<RespostaAvaliacaoInstitucionalDWVO> consultarRespostaQuestionarioPorInscricao(Integer inscricao, Integer questionario) throws Exception;

        public void excluirPorCodigoInscricaoProcSeletivo(Integer inscricao) throws Exception ;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

        public List<RespostaAvaliacaoInstitucionalDWVO> gerarListaRespostaAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Integer pessoa, String tipoPessoa, Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacao, QuestionarioVO questionario, String matriculaFuncionario, Integer inscricaoProcessoSeletivo, Integer processoSeletivo, Integer requerimento, Integer departamentoTramite, Integer ordemTramite, Boolean validarImportanciaPergunta, List<QuestionarioVO> listaQuestionarioVOs) throws ConsistirException;
        
        public void validarDadosCssApresentarHeaderDisciplina(List<QuestionarioVO> questionarioVOs, Boolean apresentarImportanciaPergunta);

        public List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoDisciplinaQuestionario(AvaliacaoInstitucionalVO obj, String matricula, MatriculaPeriodoVO matriculaperiodoVO, Integer unidadeEnsino, Boolean trazerRespondido, UsuarioVO usuario) throws Exception;

		List<RespostaAvaliacaoInstitucionalDWVO> gerarRespostaQuestionarioRequerimento(RequerimentoVO requerimentoVO) throws ConsistirException;

		void excluirPorCodigoRequerimento(Integer requerimento) throws Exception;

		List<RespostaAvaliacaoInstitucionalDWVO> consultarRespostaQuestionarioPorRequerimento(Integer requerimento, Integer questionario) throws Exception;

		List<RespostaAvaliacaoInstitucionalDWVO> consultarRespostaQuestionarioPorRequerimentoHistorico(Integer requerimento, Integer departamentoTramite, Integer ordemTramite, Integer questionario) throws Exception;

		List<RespostaAvaliacaoInstitucionalDWVO> gerarRespostaQuestionarioRequerimentoHistorico(RequerimentoHistoricoVO requerimentoHistoricoVO) throws ConsistirException;

		void excluirPorCodigoRequerimentoHistorico(Integer requerimentoHistorico) throws Exception;

		void alterarPorRequerimentoHistorico(RespostaAvaliacaoInstitucionalDWVO obj, UsuarioVO usuario) throws Exception;

		List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoProfessorAvaliandoCoodenador(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception;

		List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoProfessor(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, boolean trazerRespondido) throws Exception;

		List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoDepartamento(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception;

		List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoCargo(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception;

		List<QuestionarioVO> executarMontagemQuestionarioSerRespondidoPorAvaliacaoInstitucionalDeAcordoComPublicoAlvo(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean trazerRespondido) throws Exception;

		RespostaAvaliacaoInstitucionalDWVO realizarCriacaoRespostaAvaliacao(MatriculaVO matriculaVO,
				MatriculaPeriodoVO matriculaPeriodoVO, Integer pessoa, String tipoPessoa, Integer unidadeEnsinoVO,
				Integer disciplina, Integer turma, AvaliacaoInstitucionalVO avaliacao, QuestionarioVO questionario,
				PerguntaVO pergunta, String matriculaFuncionario, Integer inscricaoProcessoSeletivo,
				Integer processoSeletivo, Integer requerimento, Integer departamentoTramite, Integer ordemTramite,
				Boolean validarImportanciaPergunta, Boolean obrigarResposta, Integer cargo, Integer departamento,
				Integer coordenador, Integer curso) throws ConsistirException;

		String validarDadosRepostaPergunta(QuestionarioVO questionarioVO, PerguntaVO obj,
				Boolean validarImportanciaPergunta, Boolean obrigarResposta, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) throws ConsistirException;

		String validarDadosRepostaPerguntaEscopoDisciplina(PerguntaVO obj, Boolean validarImportanciaPergunta,
				Boolean obrigarResposta, DisciplinaVO disciplinaVO, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, QuestionarioVO questionarioVO) throws ConsistirException;

		List<QuestionarioVO> executarMontagemListaAvaliacaoEscopoCoodenadorAvaliandoCurso(
				AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception;

		AvaliacaoInstitucionalVO realizarCarregamentoQuestionariosPorUsuarioAvaliacaoInstitucional(
				AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, MatriculaVO matriculaVO, UsuarioVO usuarioVO)
				throws Exception;

		void realizarCarregamentoDadosAlunoResponderAvaliacaoInstitucional(
				AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, MatriculaVO matriculaVO, UsuarioVO usuarioVO)
				throws Exception;

		void realizarValidacaoImportanciaPerguntaSelecionado(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
				QuestionarioVO questionario, PerguntaQuestionarioVO obj, Integer pesoSelecionado, UsuarioVO usuarioVO)
				throws Exception;

		List<AvaliacaoInstitucionalVO> persistir(List<AvaliacaoInstitucionalVO> avaliacaoInstitucionalVOs,
				AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception;

		void realizarValidacaoRespostaQuestionario(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
				QuestionarioVO questionarioVO, PerguntaQuestionarioVO perguntaQuestionarioVO,
				RespostaPerguntaVO respostaPerguntaVO, UsuarioVO usuarioVO) throws Exception;

}