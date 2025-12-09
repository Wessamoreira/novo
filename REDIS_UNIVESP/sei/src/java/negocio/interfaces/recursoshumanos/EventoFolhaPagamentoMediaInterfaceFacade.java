package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoMediaVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface EventoFolhaPagamentoMediaInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<EventoFolhaPagamentoMediaVO> {

	void persistirTodos(List<EventoFolhaPagamentoMediaVO> eventoMediaVOs, EventoFolhaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	List<EventoFolhaPagamentoMediaVO> consultarPorEventoFolha(EventoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

}