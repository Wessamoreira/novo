package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface RespostaPerguntaRespostaOrigemInterfaceFacade {

	/**
	 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
	 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
	 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
	 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
	 */
	
    public RespostaPerguntaRespostaOrigemVO novo() throws Exception;

    public void incluir(RespostaPerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(RespostaPerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;
    
    public List<RespostaPerguntaRespostaOrigemVO> consultarPorPerguntaRespostaOrigem(Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public RespostaPerguntaRespostaOrigemVO consultarPorCodigoRespostaPerguntaCodigoPerguntaRespostaOrigem(Integer codigoRespostaPergunta, Integer codigoPerguntaRespostaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
   // public void excluir(RespostaPerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception;
}
