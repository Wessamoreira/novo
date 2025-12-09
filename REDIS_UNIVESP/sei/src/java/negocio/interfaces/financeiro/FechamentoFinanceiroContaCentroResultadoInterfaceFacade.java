package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaCentroResultadoVO;

public interface FechamentoFinanceiroContaCentroResultadoInterfaceFacade {

	void incluir(FechamentoFinanceiroContaCentroResultadoVO fechamentoFinanceiroContaCentroResultadoVO, UsuarioVO usuarioVO) throws Exception;
	List<FechamentoFinanceiroContaCentroResultadoVO> consultarPorFechamentoFinanceiroCentroResultado(Integer fechamentoFinanceiroCentroResultado, UsuarioVO usuarioVO) throws Exception;
}
