package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.NivelSalarialVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface NivelSalarialInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;
	
	public List<NivelSalarialVO> consultarListaDeNivelSalarial() throws Exception;
}