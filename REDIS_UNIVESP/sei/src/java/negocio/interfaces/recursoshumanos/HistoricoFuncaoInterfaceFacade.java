package negocio.interfaces.recursoshumanos;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoFuncaoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public void persistirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean validarAcesso) throws Exception;
}