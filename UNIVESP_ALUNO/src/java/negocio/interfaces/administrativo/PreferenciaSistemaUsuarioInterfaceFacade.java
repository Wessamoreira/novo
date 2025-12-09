package negocio.interfaces.administrativo;

import negocio.comuns.administrativo.PreferenciaSistemaUsuarioVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PreferenciaSistemaUsuarioInterfaceFacade {
	
	void persistir(PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO) throws Exception;
	
	void excluir(PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO) throws Exception;
	
	PreferenciaSistemaUsuarioVO consultarPorUsuario(UsuarioVO usuarioVO) throws Exception;
	

}
