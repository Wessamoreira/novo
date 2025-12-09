package negocio.interfaces.recursoshumanos;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface MarcacaoFeriasColetivasInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String valorConsultaSituacao) throws Exception;

	public void marcarFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void calcularRecibo(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void finalizarFerias(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void cancelarRecibo(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}