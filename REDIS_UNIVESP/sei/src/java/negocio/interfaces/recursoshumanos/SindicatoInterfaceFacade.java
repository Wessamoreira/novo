package negocio.interfaces.recursoshumanos;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface SindicatoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorFiltro(DataModelo dataModelo) throws Exception;

	public SindicatoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario, int nivelMontarDados) throws Exception;

}