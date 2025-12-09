package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.GrupoLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;

public interface GrupoLancamentoFolhaPagamentoInterfaceFacade {

	public void persistir(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO, List<TemplateEventoFolhaPagamentoVO> listaAnteriorTemplateEvento) throws Exception;

	public void excluir(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public List<GrupoLancamentoFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void validarEventoFolhaPagamento(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento, TemplateEventoFolhaPagamentoVO templateLancamentoFolhaPagamento, EventoFolhaPagamentoVO eventoFolhaPagamento) throws Exception;

	public GrupoLancamentoFolhaPagamentoVO consultarPorChavePrimaria(Long id) throws Exception;

	public boolean consultarExisteGrupoLancamentoPorCodigoTipo(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception;

}
