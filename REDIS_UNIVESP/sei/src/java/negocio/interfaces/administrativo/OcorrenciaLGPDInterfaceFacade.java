package negocio.interfaces.administrativo;

import negocio.comuns.administrativo.OcorrenciaLGPDVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface OcorrenciaLGPDInterfaceFacade {

	void incluir(OcorrenciaLGPDVO obj, UsuarioVO usuarioVO) throws Exception;

}
