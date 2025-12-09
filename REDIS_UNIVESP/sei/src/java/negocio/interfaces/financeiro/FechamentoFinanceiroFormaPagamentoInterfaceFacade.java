package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroFormaPagamentoVO;

public interface FechamentoFinanceiroFormaPagamentoInterfaceFacade {
    
	void incluir(FechamentoFinanceiroFormaPagamentoVO fechamentoFinanceiroFormaPagamentoVO, UsuarioVO usuarioVO) throws Exception;
    List<FechamentoFinanceiroFormaPagamentoVO> consultarPorFechamentoFinanceiroConta(Integer fechamentoFinanceiroConta) throws Exception;
	void processarDadosParaFechamentoFinanceiroFormaPagamentoVO(FechamentoFinanceiroContaVO ffc, UsuarioVO usuario) throws Exception;
}
