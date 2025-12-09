package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.QuestionarioVO;

public interface QuestionarioRespostaOrigemInterfaceFacade {
	
	/**
	 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
	 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
	 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
	 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
	 */
	
    public QuestionarioRespostaOrigemVO novo() throws Exception;
    
    public void persistir(QuestionarioRespostaOrigemVO obj, UsuarioVO usuarioVO) throws Exception;
    
    public void atualizarSituacaoQuestionarioRespostaOrigemVO(QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

    public void incluir(QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

   // public void excluir(QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;
    
    public QuestionarioRespostaOrigemVO preencherQuestionarioRespostaOrigem(QuestionarioVO questionarioVO, UsuarioVO usuario);
    
    public void preencherPerguntaChecklistOrigemVO(QuestionarioRespostaOrigemVO obj, UsuarioVO usuario);

	void adicionarListaPerguntaItemRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO)
			throws Exception;

	void removerListaPerguntaItemRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO, int index)
			throws Exception;
	
	public QuestionarioRespostaOrigemVO consultarPorQuestionarioPlanoEnsino(Integer questionario, Integer planoEnsino, int nivelMontarDados,UsuarioVO usuario) throws Exception;
	
	public void editarListaPerguntaItemRespostaOrigemAdicionadasVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO, List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigemVOs, UsuarioVO usuario) throws Exception;

	QuestionarioRespostaOrigemVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;

	List<QuestionarioRespostaOrigemVO> consultarPorQuestionarioEstagio(Integer estagio, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	QuestionarioRespostaOrigemVO consultarPorQuestionarioUltimaVersaoPorEstagio(Integer estagio,  int nivelMontarDados,UsuarioVO usuario) throws Exception;
	
	public void preencherMotivosPadroesEstagioVO(QuestionarioRespostaOrigemVO obj, TipoEstagioEnum tipoEstagio, Boolean retorno, Boolean indeferido, UsuarioVO usuario);

	void executarClonePerguntaRespostaOrigemArquivo(QuestionarioRespostaOrigemVO obj, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception;

}
