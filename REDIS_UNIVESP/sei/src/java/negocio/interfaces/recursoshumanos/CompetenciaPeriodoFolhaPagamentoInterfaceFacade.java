package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface CompetenciaPeriodoFolhaPagamentoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T>{

	void persistir(CompetenciaPeriodoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	public List<CompetenciaPeriodoFolhaPagamentoVO> consultarPorCompetenciaFolhaPagamento(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception ; 
	public void persistirTodos(CompetenciaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
}