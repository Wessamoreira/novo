package negocio.interfaces.recursoshumanos;

import java.util.Date;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.RescisaoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface RescisaoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public void rescindirContrato(RescisaoVO rescisaoVO , boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void consultarPorFiltros(DataModelo controleConsultaOtimizado, Date dataInicial, Date dataFinal) throws Exception;

	public void cancelarRescisao(RescisaoVO rescisaoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

}