package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteHistoricoVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ParametroValeTransporteHistoricoInterfaceFacade  <T extends SuperVO> extends SuperFacadeInterface<T> {

	public List<ParametroValeTransporteHistoricoVO> consultarPorParametroValeTransporte(Integer codigoParametro) throws Exception;

	public ParametroValeTransporteHistoricoVO montarDados(ParametroValeTransporteVO parametroValeTransporteEdicao, UsuarioVO usuarioVO) throws Exception;

	public void excluirPorParametroValeTransporte(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}
