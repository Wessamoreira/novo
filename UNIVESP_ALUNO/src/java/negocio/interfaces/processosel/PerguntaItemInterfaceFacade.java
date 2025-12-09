package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaItemVO;

public interface PerguntaItemInterfaceFacade {

    public PerguntaItemVO novo() throws Exception;

    public void incluir(PerguntaItemVO obj, UsuarioVO usuarioVO) throws Exception;

    public void alterar(PerguntaItemVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(PerguntaItemVO obj, UsuarioVO usuarioVO) throws Exception; 
    
    public void incluirPerguntaItens(Integer perguntaPrm, List objetos, UsuarioVO usuarioVO) throws Exception;
    
    public List consultarPerguntaItens(Integer perguntaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void alterarPerguntaItens(Integer pergunta, List objetos, UsuarioVO usuarioVO) throws Exception;
    
    public PerguntaItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);


}
