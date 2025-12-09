package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioRequerimentoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface QuestionarioInterfaceFacade {

    public QuestionarioVO novo() throws Exception;

    public void incluir(QuestionarioVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

    public void alterar(QuestionarioVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

    public void excluir(QuestionarioVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

    public List<QuestionarioVO> consultarPorEscopo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public QuestionarioVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<QuestionarioVO> consultarPorCodigo(Integer valorConsulta, String escopoBase, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<QuestionarioVO> consultarPorDescricao(String valorConsulta, String escopoBase,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<QuestionarioVO> consultarPorEscopoSituacaoDiferenteEmConstrucao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void executarRestauracaoRespostaQuestionarioPorInscricao(Integer inscricao, QuestionarioVO questionarioVO) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void removerPerguntaListaQuestionario(Integer pergunta, List<PerguntaVO> listaPerguntaQuestionario);

    public QuestionarioVO consultarUltimoQuestionarioPorEscopoSituacao(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void montarQuestionarioRespostasDoQuestionarioAluno(QuestionarioVO obj, QuestionarioAlunoVO questionarioAluno) throws Exception;

    public void alterarSituacaoAvaliacao(final Integer codigo, final String situacao, UsuarioVO usuario) throws Exception;

    public Boolean verificarQuestionarioVinculoAvaliacao(Integer valorConsulta, UsuarioVO usuario) throws Exception;

	void alterarOrdemPergunta(QuestionarioVO questionarioVO, PerguntaQuestionarioVO perguntaQuestionarioVO1, PerguntaQuestionarioVO perguntaQuestionarioVO2) throws Exception;

	void executarRestauracaoRespostaQuestionarioPorRequerimento(Integer requerimento, QuestionarioVO questionarioVO) throws Exception;

	List<QuestionarioVO> consultarQuestionarioRequerimentoPorEscopoRequerimento(TipoEscopoQuestionarioRequerimentoEnum tipoEscopoQuestionarioRequerimentoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Boolean executarRestauracaoRespostaQuestionarioPorRequerimentoHistorico(Integer requerimento, Integer departamentoTramite, Integer ordemTramite, QuestionarioVO questionarioVO) throws Exception;

	List<QuestionarioVO> consultarPorEscopoSituacao(TipoEscopoQuestionarioPerguntaEnum valorConsulta, String situacao, boolean trazerQuestionarioEscopoGeral, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<QuestionarioVO> consultarPorCodigoAvaliacao(Integer avaliacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	 public Boolean verificarQuestionarioVinculoPlanoEnsino(Integer codigoQuestionario, UsuarioVO usuario) throws Exception;

	public QuestionarioVO consultarQuestionarioInscricao(Integer codigoQuestionario, Integer codigoInscricao, boolean b,
			int nivelmontardadosDadosbasicos, UsuarioVO usuarioVO)throws Exception;
}
