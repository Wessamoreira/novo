package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface TemplateEventoFolhaPagamentoInterfaceFacade  <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void validarEventoFolhaPagamento(List<TemplateEventoFolhaPagamentoVO> listaDeEventos, TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO) throws Exception;

	public List<TemplateEventoFolhaPagamentoVO> consultarPorTemplateEventoFolhaPagamento(Integer codigo, int nivelmontardadosTodos, UsuarioVO usuarioLogado);

	/**
	 * Adiciona os eventos da folha de pagamento que estao nos Eventos do Grupo e que nao estao no contracheque
	 * 
	 */
	public void adicionarEventosDoGrupoLancamentoQueNaoEstaoNoContraCheque(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, TemplateLancamentoFolhaPagamentoVO template, ContraChequeVO contraChequeVO);

}