package negocio.interfaces.academico;

import negocio.comuns.academico.ForumInteracaoVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface ForumInteracaoGostadoInterfaceFacade {
    
    void incluir(ForumInteracaoVO forumInteracaoVO, UsuarioVO usuarioVO) throws Exception;

    void excluir(ForumInteracaoVO forumInteracaoVO, UsuarioVO usuario) throws Exception;
    
    Boolean consultarForumIntegracaoGostadoPorUsuario(UsuarioVO usuarioManterVO, UsuarioVO usuarioLogado);

	void excluirPorUsuario(UsuarioVO usuario) throws Exception;

}
