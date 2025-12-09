package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.financeiro.FechamentoFinanceiroCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroVO;

public interface FechamentoFinanceiroInterfaceFacade {

	
	FechamentoFinanceiroVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;
	void adicionarFechamentoFinanceiroCentroResultadoOrigem(FechamentoFinanceiroVO fechamentoFinanceiroVO, FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO, UsuarioVO usuarioVO) throws Exception;
	void persistir(FechamentoFinanceiroVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	void processarDadosParaFechamentoFinanceiro(FechamentoMesVO obj, UsuarioVO usuario) throws Exception;
	
	
}
