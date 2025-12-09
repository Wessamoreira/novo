package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroDetalhamentoValorVO;

public interface FechamentoFinanceiroDetalhamentoValorInterfaceFacade {

	void incluir(FechamentoFinanceiroDetalhamentoValorVO fechamentoFinanceiroDetalhamentoValorVO, UsuarioVO usuarioVO) throws Exception;
	List<FechamentoFinanceiroDetalhamentoValorVO> consultarPorFechamentoFinanceiroCentroResultado(Integer fechamentoFinanceiroCentroResultado) throws Exception;
	void processarDadosParaFechamentoFinanceiroDetalhamentoValorVO(FechamentoFinanceiroContaVO ffc, UsuarioVO usuario) throws Exception;
}
