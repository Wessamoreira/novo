package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PerguntaItemRespostaOrigemInterfaceFacade {
	
	/**
	 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
	 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
	 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
	 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
	 */
	
    public PerguntaItemRespostaOrigemVO novo() throws Exception;

    public void incluir(PerguntaItemRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(PerguntaItemRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;
    
    public List<PerguntaItemRespostaOrigemVO> consultarPorPerguntaRespostaOrigem(Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void reorganizarOrdemPerguntaItemRespostaOrigem(List<List<PerguntaItemRespostaOrigemVO>> listaPerguntaItemRespostaOrigemAdicionadas, UsuarioVO usuario) throws Exception;
    
    public List<PerguntaItemRespostaOrigemVO> consultarPerguntaItemRespostaOrigemAexcluir(List<PerguntaItemRespostaOrigemVO> listPerguntaItemRespostaOrigemVO, Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception;


}
