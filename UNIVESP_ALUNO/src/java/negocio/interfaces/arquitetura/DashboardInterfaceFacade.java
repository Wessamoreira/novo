package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.DashboardVO;

public interface DashboardInterfaceFacade {

	void persistir(DashboardVO dashboardVO, UsuarioVO usuarioVO) throws Exception;
	
	List<DashboardVO> consultarDashboardPorUsuarioAmbiente(UsuarioVO usuarioVO, TipoVisaoEnum ambiente) throws Exception;

	List<DashboardVO> consultarDashboardPorUsuarioAmbiente(UsuarioVO usuarioVO, TipoVisaoEnum ambiente,
			PerfilAcessoModuloEnum modulo) throws Exception;
}
