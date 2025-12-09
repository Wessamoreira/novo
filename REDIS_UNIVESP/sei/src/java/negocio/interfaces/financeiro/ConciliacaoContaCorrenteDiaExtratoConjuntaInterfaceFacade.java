package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoConjuntaVO;

public interface ConciliacaoContaCorrenteDiaExtratoConjuntaInterfaceFacade {

	void persistir(List<ConciliacaoContaCorrenteDiaExtratoConjuntaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

}
