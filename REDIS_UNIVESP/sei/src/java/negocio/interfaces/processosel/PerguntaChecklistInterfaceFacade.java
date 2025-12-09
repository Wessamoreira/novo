package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaChecklistVO;
import negocio.comuns.processosel.PerguntaVO;

public interface PerguntaChecklistInterfaceFacade {

	void persistir(List<PerguntaChecklistVO> lista, UsuarioVO usuarioVO);

	List<PerguntaChecklistVO> consultarPerguntaChecklistPorPerguntaVO(PerguntaVO obj, int nivelMontarDados, UsuarioVO usuario);

}
