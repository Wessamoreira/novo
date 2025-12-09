package negocio.interfaces.academico;
import java.util.List;

import negocio.comuns.academico.ForumPessoaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface ForumPessoaInterfaceFacade {
	
	void persistirForumPessoa(ForumVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<ForumPessoaVO> consultarForumPessoa(Integer forum, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

}
