package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroCentroResultadoEnum;

public interface FechamentoFinanceiroCentroResultadoInterfaceFacade {

	void incluir(FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO, UsuarioVO usuarioVO) throws Exception;
	List<FechamentoFinanceiroCentroResultadoVO> consultarPorOrigemCodigoOrigem(OrigemFechamentoFinanceiroCentroResultadoEnum origemFechamentoFinanceiroCentroResultado, Integer codigoOrigem) throws Exception;
	void processarDadosParaFechamentoFinanceiroCentroResultadoVO(FechamentoFinanceiroContaVO ffc, UsuarioVO usuario) throws Exception;
	
	
}
