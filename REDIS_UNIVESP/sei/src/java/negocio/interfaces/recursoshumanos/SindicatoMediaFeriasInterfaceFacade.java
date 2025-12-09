package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.SindicatoMediaFeriasVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface SindicatoMediaFeriasInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<SindicatoMediaFeriasVO> {

	void persistirTodos(List<SindicatoMediaFeriasVO> sindicatoMediaVOs, SindicatoVO obj, UsuarioVO usuarioVO) throws Exception;

	List<SindicatoMediaFeriasVO> consultarPorSindicatoVO(SindicatoVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;
	
	public void excluirPorSindicato(Integer codigoSindicato,boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void consultarEventosDeMediaDeFerias(MarcacaoFeriasVO marcacao, List<EventoFolhaPagamentoVO> listaDeEventos);
	
}