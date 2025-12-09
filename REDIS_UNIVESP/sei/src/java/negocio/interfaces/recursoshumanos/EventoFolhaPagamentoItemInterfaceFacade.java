package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoItemVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface EventoFolhaPagamentoItemInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	void persistirTodos(List<EventoFolhaPagamentoItemVO> eventoFolhaPagamentoItemVOs, EventoFolhaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	List<EventoFolhaPagamentoItemVO> consultarPorEventoFolha(EventoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public void adicionarEventosVinculadosDosEventosDoContraChequeDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo);

}
