package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.NegociacaoContaReceberPlanoDescontoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;

public interface NegociacaoContaReceberPlanoDescontoInterfaceFacade {
	
	void incluirNegociacaoContaReceberPlanoDesconto(NegociacaoContaReceberVO obj, UsuarioVO usuarioVO) throws Exception;
	void incluir(NegociacaoContaReceberPlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception;
	List<NegociacaoContaReceberPlanoDescontoVO> consultarPorNegociacaoContaReceber(Integer negociacaoContaReceber, UsuarioVO usuarioVO) throws Exception;
}
