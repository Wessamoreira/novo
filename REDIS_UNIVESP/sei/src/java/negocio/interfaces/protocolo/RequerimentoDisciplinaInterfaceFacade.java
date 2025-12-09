package negocio.interfaces.protocolo;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoDisciplinaVO;
import negocio.comuns.protocolo.RequerimentoVO;

public interface RequerimentoDisciplinaInterfaceFacade {

	void persistir(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;
	void consultarPorRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO)throws Exception;

}
