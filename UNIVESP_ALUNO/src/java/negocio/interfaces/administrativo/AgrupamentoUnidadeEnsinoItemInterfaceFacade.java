package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoItemVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface AgrupamentoUnidadeEnsinoItemInterfaceFacade {

	void persistir(List<AgrupamentoUnidadeEnsinoItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

}
