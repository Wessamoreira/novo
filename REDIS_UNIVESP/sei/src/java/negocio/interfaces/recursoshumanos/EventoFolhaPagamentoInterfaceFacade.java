package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;
import java.util.List;

import javax.script.ScriptEngine;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaEventoFolhaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.recursoshumanos.CalculoContraCheque;

public interface EventoFolhaPagamentoInterfaceFacade {

	public void persistir(EventoFolhaPagamentoVO evento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(EventoFolhaPagamentoVO evento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<EventoFolhaPagamentoVO> consultarPorFiltro(String campoConsultaEvento, String valorConsultaEvento, String valorConsultaSituacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public EventoFolhaPagamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario, int nivelMontarDados) throws Exception;

	public void validarDadosPorFormulaFolhaPagamento(FormulaFolhaPagamentoVO formulaDia, String campoConsulta) throws ConsistirException;

	public EventoFolhaPagamentoVO consultarPorChaveIdentificador(String identificador, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public int consultarProximoCodigoDoEventoFolhaPagamento();

	public List<EventoFolhaPagamentoVO> consultarEventosFolhaCompletoPorContraChequeEvento(List<ContraChequeEventoVO> contraChequeEventos, boolean b, UsuarioVO usuarioVO) throws Exception;
	
	public List<EventoFolhaPagamentoVO> consultarEventosFolhaCompletoPorTemplateEventoFolhaPagamentoVO(List<TemplateEventoFolhaPagamentoVO> templateEventos, boolean b, UsuarioVO usuario) ;

	public void inativar(EventoFolhaPagamentoVO eventoFolhaPagamentoVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<EventoFolhaPagamentoVO> consultarPorFiltroEProvento(String campoConsulta, String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public BigDecimal recuperarValorDoEventoCalculado(EventoFolhaPagamentoVO evento, FuncionarioCargoVO funcionarioCargo, CalculoContraCheque calculoContraCheque, ScriptEngine engine);

	public String recuperarReferenciaDoEventoCalculado(EventoFolhaPagamentoVO evento, FuncionarioCargoVO funcionarioCargo, CalculoContraCheque calculoContraCheque, ScriptEngine engine);

	public void calcularEventoFolhaPagamento(EventoFolhaPagamentoVO evento, FuncionarioCargoVO funcionarioCargo, CalculoContraCheque calculoContraCheque, ScriptEngine engine);

	public EventoFolhaPagamentoVO montarDadosDoEventoParaContraCheque(Integer eventoFolhaPgto, Integer contraChequeEvento);

	public List<String> consultarListaDeIdentificadoresAtivo();

	public void consultarPorFiltro(DataModelo dataModelo, String situacao, String tipoLancamento) throws Exception;

	public List<EventoFolhaPagamentoVO> consultarEventosDeFerias();
	
	public List<EventoFolhaPagamentoVO> consultarEventosDeFolhaNormal();

	public void adicionarEventosDeFolhaNormal(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario);

	public EventoFolhaPagamentoVO consultarEventoPorCategoriaEAgrupamento(CategoriaEventoFolhaEnum categoriaEvento, String agrupamento);
	
	public List<EventoFolhaPagamentoVO> consultarEventosDo13();

	public List<EventoFolhaPagamentoVO> consultarEventosDeRescisao();
}