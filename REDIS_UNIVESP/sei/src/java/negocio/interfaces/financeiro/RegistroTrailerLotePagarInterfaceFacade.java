package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroTrailerLotePagarVO;

public interface RegistroTrailerLotePagarInterfaceFacade {

	void persistir(RegistroTrailerLotePagarVO obj, UsuarioVO usuarioVO) throws Exception;

	RegistroTrailerLotePagarVO consultarPorRegistroHeaderLotePagar(Integer registroHeaderLotePagar, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
