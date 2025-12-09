package negocio.interfaces.academico;

import negocio.comuns.academico.HistoricoEquivalenteVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface HistoricoEquivalenteInterfaceFacade {

	public void incluir(final HistoricoEquivalenteVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final HistoricoEquivalenteVO obj, UsuarioVO usuario) throws Exception;
	public void excluir(HistoricoEquivalenteVO obj, UsuarioVO usuario) throws Exception;
	
}
