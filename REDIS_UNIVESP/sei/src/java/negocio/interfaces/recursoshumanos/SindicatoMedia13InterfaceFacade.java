package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SindicatoMedia13VO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface SindicatoMedia13InterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<SindicatoMedia13VO> {

	void persistirTodos(List<SindicatoMedia13VO> sindicatoMediaVOs, SindicatoVO obj, UsuarioVO usuarioVO) throws Exception;

	List<SindicatoMedia13VO> consultarPorSindicatoVO(SindicatoVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;
	
	public void excluirPorSindicato(Integer codigoSindicato,boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void consultarEventosDeMediaDe13(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia, List<EventoFolhaPagamentoVO> listaDeEventos);
}