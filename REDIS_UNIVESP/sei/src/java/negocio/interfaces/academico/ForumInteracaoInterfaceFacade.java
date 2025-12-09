package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ForumInteracaoVO;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumInteracaoEnum;
import negocio.comuns.arquitetura.UsuarioVO;


public interface ForumInteracaoInterfaceFacade {
	void persistir(ForumInteracaoVO forumInteracaoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
  
	void excluir(ForumInteracaoVO forumInteracaoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
	List<ForumInteracaoVO> consultarPorForum(Integer forum, OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao, Integer limite, Integer pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistroPorForum(Integer forum) throws Exception;

	Integer consultarTotalGostado(Integer codigoForumInteracao) throws Exception;

	List<ForumInteracaoVO> consultarPorForumPorUsuario(Integer forum, Integer usuarioInteracao, Integer limite, Integer pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistroPorForumPorUsuario(Integer forum, Integer usuarioInteracao) throws Exception;
	
   List<ForumInteracaoVO> consultarPorForumPorCodigoUsuarioLogado(Integer forum, OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao, Integer limite, Integer pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}