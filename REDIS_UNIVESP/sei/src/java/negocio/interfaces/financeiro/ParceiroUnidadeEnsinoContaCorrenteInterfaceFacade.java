package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ParceiroUnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.ParceiroVO;

public interface ParceiroUnidadeEnsinoContaCorrenteInterfaceFacade {

	void persistir(List<ParceiroUnidadeEnsinoContaCorrenteVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<ParceiroUnidadeEnsinoContaCorrenteVO> consultaRapidaPorParceiro(ParceiroVO obj, UsuarioVO usuario)
			throws Exception;

}
