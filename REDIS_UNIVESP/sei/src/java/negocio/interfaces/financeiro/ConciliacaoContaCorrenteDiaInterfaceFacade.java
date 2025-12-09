package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;

public interface ConciliacaoContaCorrenteDiaInterfaceFacade {

	void persistir(List<ConciliacaoContaCorrenteDiaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ConciliacaoContaCorrenteDiaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<ConciliacaoContaCorrenteDiaVO> consultaRapidaPorConciliacaoContaCorrente(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception;
	

}
