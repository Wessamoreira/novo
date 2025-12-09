package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaChecklistVO;
import negocio.comuns.processosel.PerguntaItemVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface PerguntaInterfaceFacade {

    public PerguntaVO novo() throws Exception;

    public void incluir(PerguntaVO obj, EscopoPerguntaEnum escopoPerguntaBase, UsuarioVO usuario) throws Exception;

    public void alterar(PerguntaVO obj, EscopoPerguntaEnum escopoPerguntaBase, UsuarioVO usuario) throws Exception;

    public void excluir(PerguntaVO obj, EscopoPerguntaEnum escopoPerguntaBase, UsuarioVO usuario) throws Exception;

    public PerguntaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PerguntaVO>  consultarPorCodigo(Integer valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PerguntaVO>  consultarPorDescricao(String valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PerguntaVO>  consultarPorTipoResposta(String valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<PerguntaVO>  consultarPorCodigoQuestionario(Integer questionario, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;
    
    public List<PerguntaVO>  consultaPorCodigo(Integer valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	void alterarOrdemOpcaoRespostaQuestao(PerguntaVO perguntaVO, RespostaPerguntaVO respostaPerguntaVO1, RespostaPerguntaVO respostaPerguntaVO2) throws Exception;
	
	public void adicionarObjPerguntaItemVOs(PerguntaItemVO obj, List<PerguntaItemVO> perguntaItemVOs) throws Exception;
	
	public void excluirObjPerguntaItemVOs(Integer pergunta, List<PerguntaItemVO> perguntaItemVOs) throws Exception;	
	
	public void removerPerguntaListaPergunta(Integer pergunta, List<PerguntaVO> listaPergunta);
	
    public List<PerguntaVO>  consultarPorCodigoTipoResposta(Integer valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PerguntaVO>  consultarPorDescricaoTipoResposta(String valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void alterarOrdemPergunta(PerguntaVO perguntaVO, PerguntaItemVO perguntaItemVO1, PerguntaItemVO perguntaItemVO2) throws Exception;

	void adicionarPerguntaChecklistVO(PerguntaVO obj, PerguntaChecklistVO perguntaChecklistVO, UsuarioVO usuario);

	void removerPerguntaChecklistVO(PerguntaVO obj, PerguntaChecklistVO perguntaChecklistVO, UsuarioVO usuario);


}
