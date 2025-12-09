package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroVO;

public interface FechamentoFinanceiroContaInterfaceFacade {
	
	void incluir(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO, UsuarioVO usuarioVO) throws Exception;
	FechamentoFinanceiroContaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;
	void processarDadosParaFechamentoFinanceiroConta(FechamentoFinanceiroVO ff, UsuarioVO usuario) throws Exception;	

}
