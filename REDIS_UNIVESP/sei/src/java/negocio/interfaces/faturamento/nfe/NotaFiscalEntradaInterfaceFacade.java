package negocio.interfaces.faturamento.nfe;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;

public interface NotaFiscalEntradaInterfaceFacade {

	void persistir(NotaFiscalEntradaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) ;

	void excluir(NotaFiscalEntradaVO obj, boolean verificarAcesso, UsuarioVO usuario) ;

	void addNotaFiscalEntradaImposto(NotaFiscalEntradaVO obj, NotaFiscalEntradaImpostoVO notaFiscalImpostoVO, UsuarioVO usuario) ;

	void removerNotaFiscalEntradaImposto(NotaFiscalEntradaVO obj, NotaFiscalEntradaImpostoVO notaFiscalImpostoVO, UsuarioVO usuario) ;

	void removerNotaFiscalEntradaItem(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO,  boolean isManual, UsuarioVO usuario) ;

	List<NotaFiscalEntradaVO> consultaRapidaPorCodigo(Integer valorConsulta, DataModelo dataModelo) ;

	List<NotaFiscalEntradaVO> consultaRapidaPorNumero(Long numero, DataModelo dataModelo) ;

	List<NotaFiscalEntradaVO> consultaRapidaPorUnidadeEnsino(Integer valorConsulta, DataModelo dataModelo) ;

	List<NotaFiscalEntradaVO> consultaRapidaPorFornecedor(String valorConsulta, DataModelo dataModelo) ;

	NotaFiscalEntradaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	Integer consultarTotalPorFornecedor(String valorConsulta, DataModelo dataModelo) ;

	Integer consultarTotalPorUnidadeEnsino(Integer valorConsulta, DataModelo dataModelo) ;

	Integer consultarTotalPorNumero(Long valorConsulta, DataModelo dataModelo) ;

	Integer consultarTotalPorCodigo(Integer valorConsulta, DataModelo dataModelo) ;

	void preencherNotaFiscalEntradaCompraPorFornecedor(NotaFiscalEntradaVO obj, UsuarioVO usuario) ;

	void gerarNotaFiscalEntradaItemPorCompra(NotaFiscalEntradaVO obj, NotaFiscalEntradaRecebimentoCompraVO nferc, UsuarioVO usuario);

	void addLancamentoContabilVO(NotaFiscalEntradaVO nfe, LancamentoContabilVO lancamento, CategoriaProdutoVO categoriaProdutoFiltroVO, ImpostoVO impstoFiltroVO, UsuarioVO usuario) ;

	void removeLancamentoContabilVO(NotaFiscalEntradaVO nfe, LancamentoContabilVO lancamento, UsuarioVO usuario) ;		

	String consultarNumeroDasNotasFiscaisPorCompra(Integer compra, UsuarioVO usuario) ;	

	void consultar(DataModelo dataModelo, NotaFiscalEntradaVO obj, Date dataEntradaInicio, Date dataEntradaFim);

	void removerNotaFiscalEntradaContaPagar(NotaFiscalEntradaVO obj, ContaPagarVO contaPagar);	

	void preencherDadosContaPagarPadrao(NotaFiscalEntradaVO obj, ContaPagarVO contaPagar, UsuarioVO usuario);

	void gerarLancamentoContabilPadrao(NotaFiscalEntradaVO obj, UsuarioVO usuario);

	void adicionarCentroResultadoOrigemNotaFiscalEntradaItem(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO, CentroResultadoOrigemVO centroResultadoOrigemVO, UsuarioVO usuario);

	void adicionarNotaFiscalEntradaItemManual(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO, UsuarioVO usuario);

	void verificarAdiantamentosDisponiveisParaAbatimentoContasPagar(NotaFiscalEntradaVO nfe, UsuarioVO usuario);

	void limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(NotaFiscalEntradaVO nfe, Boolean manterListaAdiantamentosParaNovoProcessamento);
	
	void removerAdiantamentosDisponiveisParaAbaterContasPagar(NotaFiscalEntradaVO nfe, ContaPagarAdiantamentoVO cpa, UsuarioVO usuario);

	void realizarDistribuicaoAutomaticaAdiantamentosDisponiveisNotaFiscalEntrada(NotaFiscalEntradaVO nfe, UsuarioVO usuario);	

	double realizarDistribuicaoAutomaticaAdiantamentosDisponiveisParaAbaterContasPagar(NotaFiscalEntradaVO nfe, ContaPagarVO contaPagar, double valorTotalAdiantamentoAbatidoContasAPagar, UsuarioVO usuario) throws Exception;
	
	Double consultarTotalNotaFiscalEntradaPorFiltros(NotaFiscalEntradaVO obj, DataModelo dataModelo, Date dataEntradaInicio, Date dataEntradaFim);

	void atualizarListasDaNotaFiscalEntrada(NotaFiscalEntradaVO obj, UsuarioVO usuario);
	void adicionarCentroResultadoOrigemNotaFiscalEntradaItemPorRateioCategoriaDespesa(NotaFiscalEntradaVO obj,
			NotaFiscalEntradaItemVO notaFiscalItemVO, CentroResultadoOrigemVO centroResultadoOrigemVO,
			UsuarioVO usuario) throws Exception;

}