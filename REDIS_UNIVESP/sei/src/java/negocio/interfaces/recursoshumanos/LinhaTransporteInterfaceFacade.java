package negocio.interfaces.recursoshumanos;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.LinhaTransporteVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface LinhaTransporteInterfaceFacade  <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacao) throws Exception;

	public void inativar(LinhaTransporteVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

}
