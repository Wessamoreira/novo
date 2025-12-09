package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirFuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface LayoutRelatorioSEIDecidirFuncionarioInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public List<LayoutRelatorioSEIDecidirFuncionarioVO> consultarPorLayoutRelatorioSeiDecidir(Integer idLayoutRelatorioSeiDecidir) throws Exception;
	
	public void excluirPorLayoutRelatorioSeiDecidir(LayoutRelatorioSEIDecidirVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}
