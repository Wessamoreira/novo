package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface LancamentoFolhaPagamentoInterfaceFacade  {

	public void persistir(LancamentoFolhaPagamentoVO lancamentoFolhaPagamentoVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<LancamentoFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

	public LancamentoFolhaPagamentoVO consultarPorChavePrimaria(Long codigo, NivelMontarDados nivelMontarDados) throws Exception;

	public void encerrarVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO);

	public LancamentoFolhaPagamentoVO consultarPorChavePrimaria(Long id) throws Exception;
	
	public LancamentoFolhaPagamentoVO consultarPorContraCheque(ContraChequeVO contraCheque, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;

	public void gerarFolhaPagamento(LancamentoFolhaPagamentoVO lancamento, List<TemplateEventoFolhaPagamentoVO> listaTemplateEventoFolhaPagamento, UsuarioVO usuarioLogado) throws Exception;

	public void cancelarFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado) throws Exception;

	public LancamentoFolhaPagamentoVO consultarPorTemplate(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;
	
	public void validarDadosGerarFolhaPagamento(LancamentoFolhaPagamentoVO lancamento, List<TemplateEventoFolhaPagamentoVO> listaTemplateEventoFolhaPagamento,  UsuarioVO usuarioLogado) throws Exception;
}