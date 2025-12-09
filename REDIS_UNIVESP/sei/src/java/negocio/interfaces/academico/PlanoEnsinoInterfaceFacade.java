package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoEnsinoHorarioAulaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ReferenciaBibliograficaVO;
import negocio.comuns.academico.enumeradores.SituacaoPlanoEnsinoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface PlanoEnsinoInterfaceFacade {

	void persistir(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioLogado) throws Exception;
	
	List<PlanoEnsinoVO> consultar(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, String descricao, String situacao, Integer limit, Integer offset, boolean validarAcesso, Integer periodoLetivo, Integer turma, UsuarioVO usuarioLogado) throws Exception;
	
	Integer consultarTotalRegistro(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, String descricao, String situacao, Integer periodoLetivo, Integer turma) throws Exception;
	
	PlanoEnsinoVO consultarPorChavePrimaria(Integer codigo, int nivelMontardados, UsuarioVO usuarioLogado) throws Exception;
	
	PlanoEnsinoVO consultarPorUnidadeEnsinoCursoDisciplinaAnoSemestre(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, Integer turno,  int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	void adicionarReferenciaBibliografiaVOs(PlanoEnsinoVO planoEnsinoVO, ReferenciaBibliograficaVO referenciaBibliograficaVO) throws Exception;

	void removerReferenciaBibliografiaVOs(PlanoEnsinoVO planoEnsinoVO, ReferenciaBibliograficaVO referenciaBibliograficaVO) throws Exception;

	void adicionarConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO, ConteudoPlanejamentoVO conteudoPlanejamentoVO) throws Exception;

	void removerConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO, ConteudoPlanejamentoVO conteudoPlanejamentoVO) throws Exception;

	void realizarClonagem(PlanoEnsinoVO planoEnsinoVO, boolean clonarVisaoProfessor, boolean clonarVisaoAdmCoordenador, UsuarioVO usuarioVO) throws Exception;

	PlanoEnsinoVO consultarPorDisciplinaMatriculaAluno(Integer disciplina, String matricula, boolean buscarPlanoVinculadoProfessorResponsavel, String nomeProfessor, UsuarioVO usuarioVO) throws Exception;

	void excluir(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioLogado) throws Exception;

	void alterarOrdenacaoConteudoPlanejamentoVO(PlanoEnsinoVO planoEnsinoVO, ConteudoPlanejamentoVO obj1, ConteudoPlanejamentoVO obj2) throws Exception;

	List<Integer> consultarDisciplinaMatriculaAlunoQuePossuiPlanoEnsino(String matricula, UsuarioVO usuarioVO)
			throws Exception;

	void realizarVerificacaoDisciplinaAlunoQuePossuiPlanoEnsino(MatriculaVO matriculaVO, UsuarioVO usuarioVO)
			throws Exception;
	
	PlanoEnsinoVO atualizarTotalCargaHoraria(PlanoEnsinoVO planoEnsinoVO);

	List<PlanoEnsinoVO> consultarPlanoEnsinoProfessor(String campoConsulta, String valorConsultar, String ano, String semestre,	SituacaoPlanoEnsinoEnum situacaoPlanoEnsino, 
			boolean filtrarTodosPlanosEnsino, boolean controlarAcesso, boolean trazerApenasPlanosSemProfessorResponsavelOuVinculadosAoProfessor, 
			Integer professorCodigoPessoa, Integer turma, Integer curso, Integer periodoLetivo, UsuarioVO usuarioVO) throws Exception;

	List<PlanoEnsinoVO> consultarPlanoEnsinoCoordenador(String campoConsulta, String valorConsultar, Integer unidadeEnsino, 
			String ano, String semestre, SituacaoPlanoEnsinoEnum situacaoPlanoEnsino, boolean filtrarTodosPlanosEnsino, 
			boolean controlarAcesso, Integer turma, Integer curso, Integer periodoLetivo, UsuarioVO usuarioVO) throws Exception;

	void removerPlanoEnsinoHorarioAula(PlanoEnsinoVO planoEnsinoVO, PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO)
			throws Exception;

	void adicionarPlanoEnsinoHorarioAula(PlanoEnsinoVO planoEnsinoVO, PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO)
			throws Exception;

	PlanoEnsinoVO consultarPlanoEnsinoValidoPorUnidadeEnsinoCursoDisciplinaAnoSemestre(Integer unidadeEnsino,
			Integer curso, Integer disciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO)
			throws Exception;

	PlanoEnsinoVO consultarPorDisciplinaMatriculaAluno(Integer disciplina, String matricula, boolean buscarPlanoVinculadoProfessorResponsavel, HistoricoVO historicoVO, UsuarioVO usuarioVO) throws Exception;

	public PlanoEnsinoVO consultarPlanoEnsino(
			Integer unidadeEnsino, Integer curso, String ano, String semestre, Integer disciplina,
			Integer turno, Integer professor, String situacao, UsuarioVO usuarioLogado) throws Exception;

	void realizarCriacaoQuestionarioRespostaOrigem(PlanoEnsinoVO planoEnsinoVO, QuestionarioVO questionarioVO,
			UsuarioVO usuarioVO) throws Exception;
	
	void preencherDadosPlanoEnsinoQuestionarioRespostaOrigem(PlanoEnsinoVO planoEnsinoVO, Integer codigoQuestionarioPlanoEnsino, UsuarioVO usuarioVO) throws Exception;

	void validarCalendarioLancamentoPlanoEnsino(PlanoEnsinoVO planoEnsinoVO, boolean visaoProfessor) throws ConsistirException;

	void alterarSituacao(int codigo, String situacao, String motivo, UsuarioVO usuarioVO) throws Exception;
	
	public List<PlanoEnsinoVO> consultarPlanoPorUnidadeEnsinoCursoDisciplinaAnoSemestre(Integer unidadeEnsino, Integer curso, Integer disciplina, String ano, String semestre, Integer turno, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

}
