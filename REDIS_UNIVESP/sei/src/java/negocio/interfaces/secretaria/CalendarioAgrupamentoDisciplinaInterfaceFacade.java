package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoDisciplinaVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;

public interface CalendarioAgrupamentoDisciplinaInterfaceFacade {

	
	void incluirCalendarioAgrupamentoDisciplinaVOs(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, UsuarioVO usuarioVO) throws Exception;
	void alterarCalendarioAgrupamentoDisciplinaVOs(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, UsuarioVO usuarioVO) throws Exception;
	
	List<CalendarioAgrupamentoDisciplinaVO> consultarPorCalendarioAgrupamento(Integer calendarioAgrupamento, UsuarioVO usuarioVO) throws Exception;
		
	List<CalendarioAgrupamentoDisciplinaVO> consultarPorDisciplinaNaoSelecionadaPorCalendarioAgrupamento(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, UsuarioVO usuarioVO) throws Exception;
	
}
