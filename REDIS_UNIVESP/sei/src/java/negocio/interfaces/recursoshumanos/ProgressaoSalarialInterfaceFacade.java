package negocio.interfaces.recursoshumanos;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ProgressaoSalarialInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;
	public List<ProgressaoSalarialVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}