package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.TipoTransporteVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface TipoTransporteInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public List<TipoTransporteVO> consultarTipoTransporte(DataModelo dataModelo, String campoConsulta) throws Exception;

}
