package negocio.interfaces.administrativo;

import negocio.comuns.administrativo.CampanhaRSLogVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CampanhaRSLogInterfaceFacade {

	void incluir(CampanhaRSLogVO obj, UsuarioVO usuario) throws Exception;

	void alterar(CampanhaRSLogVO obj, UsuarioVO usuario) throws Exception;

}
