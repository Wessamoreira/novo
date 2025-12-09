package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ForumVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;


public interface ForumAcessoInterfaceFacade {
    
    void incluir(ForumVO forumVO, UsuarioVO usuario) throws Exception;
    
    List<PessoaVO> consultarAlunoQueAcessaramForum(Integer forum, UsuarioVO usuario) throws Exception;
        
    

}
