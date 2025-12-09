package negocio.interfaces.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ReciboFeriasInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public ReciboFeriasVO consultarPorMarcacao(MarcacaoFeriasVO marcacao, int nivelmontardadosTodos, UsuarioVO usuario, boolean controlarAcesso);

	public void calcularRecibo(MarcacaoFeriasVO marcacaoFeriasVO, ReciboFeriasVO recibo) throws Exception;

	public void cancelarRecibo(MarcacaoFeriasVO marcacaoFeriasVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

}