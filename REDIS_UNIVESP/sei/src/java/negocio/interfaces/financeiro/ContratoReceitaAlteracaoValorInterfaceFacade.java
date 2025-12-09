package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContratoReceitaAlteracaoValorVO;

public interface ContratoReceitaAlteracaoValorInterfaceFacade {

	void incluir(final ContratoReceitaAlteracaoValorVO contratoReceitaAlteracaoValorVO, UsuarioVO usuario) throws Exception;
	List<ContratoReceitaAlteracaoValorVO> consultarPorContratoReceita(Integer contratoReceita, UsuarioVO usuario) throws Exception;
	
}
