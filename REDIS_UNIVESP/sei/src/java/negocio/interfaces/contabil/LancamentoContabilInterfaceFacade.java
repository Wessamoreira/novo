package negocio.interfaces.contabil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.LancamentoContabilCentroNegocioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.contabil.enumeradores.TipoValorLancamentoContabilEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.dominios.TipoDesconto;
import negocio.comuns.utilitarias.dominios.TipoSacado;

public interface LancamentoContabilInterfaceFacade {

	void persistir(LancamentoContabilVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(LancamentoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<LancamentoContabilVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	

	void gerarLancamentoContabilPorMapaPendenciaCartaoCredito(Map<Integer, List<MapaPendenciaCartaoCreditoVO>> map, Date dataBaixa, boolean abaterTaxaExtratoContaCorrente, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception;
	
	void gerarLancamentoContabilPorContaPagar(List<LancamentoContabilVO> listaLancamentoContabilVOs, Date dataPagamento, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO, Double valorPagamento, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception;
	
	void gerarLancamentoContabilPorContaReceber(List<LancamentoContabilVO> listaLancamentoContabilVOs, ContaReceberVO contaReceber, FormaPagamentoNegociacaoRecebimentoVO fpnrVO, Double valorPagamento, Date dataBaixa, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception;
	
	void gerarLancamentoContabilPorMovimentacaoFinanceira(MovimentacaoFinanceiraVO mov, boolean excluirLancamentoExistente, UsuarioVO usuarioLogado) throws Exception;

	List<LancamentoContabilVO> consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(String codOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, TipoPlanoContaEnum tipoplanoConta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void preencherLancamentoContabilPorContaPagar(LancamentoContabilVO lc, PlanoContaVO planoConta, Date dataPagamento, ContaPagarVO contaPagar, TipoValorLancamentoContabilEnum tipoValor, Double valorPagamento, ContaCorrenteVO contaCorrente, boolean isPreencherRaterio, TipoPlanoContaEnum tipoPlanoContaEnum, boolean isCalculoPorcentagem, UsuarioVO usuarioLogado) ;
	
	void preencherLancamentoContabilPorContaReceber(LancamentoContabilVO lc, PlanoContaVO planoConta, Date dataBaixa, ContaReceberVO contaReceber, TipoValorLancamentoContabilEnum tipoValor, FormaPagamentoNegociacaoRecebimentoVO fpnrVO,  Double valorPagamento, CursoVO curso, ContaCorrenteVO contaCorrente, boolean isPreencherRaterio, TipoPlanoContaEnum tipoPlanoContaEnum, Date dataCompensacao,  UsuarioVO usuarioLogado) throws Exception;

	void removeLancamentoContabilCentroNegociacaoVO(LancamentoContabilVO lc, LancamentoContabilCentroNegocioVO lccn, UsuarioVO usuario) throws Exception;

	void addLancamentoContabilCentroNegociacao(LancamentoContabilVO lc, LancamentoContabilCentroNegocioVO lccn, TipoCentroNegocioEnum tipoCategoriaRateio, UsuarioVO usuarioLogado) throws Exception;

	void excluirPorCodOrigemTipoOrigem(String codOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, boolean verificarAcesso, UsuarioVO usuario);

	void validarSeLancamentoContabilFoiExcluido(List<LancamentoContabilVO> lista, Map<TipoOrigemLancamentoContabilEnum, String> mapaOrigemLancmentoContabil, UsuarioVO usuario) throws Exception;

	void preencherLancamentoContabilPorMovimentacaoFinanceira(UnidadeEnsinoVO unidadEnsino, LancamentoContabilVO lc, PlanoContaVO planoConta, MovimentacaoFinanceiraVO mov, ContaCorrenteVO contaCorrente, Double valorPagamento, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws Exception;	

	void consultaLoteLancamentoContabilParaProcessamento(IntegracaoContabilVO integracaoContabil, LayoutIntegracaoVO layoutIntegracaoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void atualizarLancamentoContabilPorIntegracaoContabil(IntegracaoContabilVO ic,  boolean anular, UsuarioVO usuario) throws Exception;

	void consultaLancamentoContabilPorIntegracaoContabil(IntegracaoContabilVO integracaoContabil, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void validarDados(LancamentoContabilVO obj);

	void realizarAtualizacaoLancamentoContabilPorCheque(List<Integer> cheques, boolean isRecebimento, UsuarioVO usuario) throws Exception;

	void realizarAtualizacaoLancamentoContabilPorMapaPendenciaCartaoCredito(List<Integer> listaMapaPendenciaCartaoCreditoVO, Date dataBaixa, UsuarioVO usuario) throws Exception;
	
	public void gerarLancamentoContabilPorNotaFiscalEntradaImposto(NotaFiscalEntradaVO nfe, UsuarioVO usuarioLogado);
	
	public void gerarLancamentoContabilPorNotaFiscalEntradaItem(NotaFiscalEntradaVO nfe, UsuarioVO usuarioLogado);

	List<LancamentoContabilVO> consultaRapidaPorListaCodOrigemPorTipoOrigemPorTipoPlanoConta(String listaCodOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, TipoPlanoContaEnum tipoplanoConta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public boolean consultaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(String codOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, boolean controlarAcesso, UsuarioVO usuario) throws Exception; 

	void preencherLancamentoContabilPorNotaFiscalEntradaItem(LancamentoContabilVO lc, PlanoContaVO planoConta, NotaFiscalEntradaVO nfe, Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto,  TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws StreamSeiException;

	void preencherLancamentoContabilPorNotaFiscalEntradaImposto(LancamentoContabilVO lc, PlanoContaVO planoConta, NotaFiscalEntradaVO nfe, NotaFiscalEntradaImpostoVO nfei, TipoPlanoContaEnum tipoPlanoContaEnum, UsuarioVO usuarioLogado) throws StreamSeiException;

	void preencherListaLancamentoContabilVO(List<LancamentoContabilVO> lista, LancamentoContabilVO lancamento);

	void removeLancamentoContabilVO(List<LancamentoContabilVO> lista, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception;

	void excluirPorListaCodOrigemTipoOrigem(String listaCodOrigem, TipoOrigemLancamentoContabilEnum tipoOrigem, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	
	ConfiguracaoContabilRegraVO obterRegraContabilParaJuroMultaAcrescimoContaPagar(ConfiguracaoContabilVO conf, TipoDesconto tipoDesconto);

	ConfiguracaoContabilRegraVO obterRegraContabilParaDescontoContaPagar(ConfiguracaoContabilVO conf, TipoSacado tipoSacado, Integer codigoSacado, TipoDesconto tipoDesconto);
	
	ConfiguracaoContabilRegraVO obterRegraContabilParaSacadoContaPagarPorCentroResultado(ConfiguracaoContabilVO conf, TipoSacado tipoSacado, Integer codigoSacado, CentroResultadoOrigemVO cro);
	
	ConfiguracaoContabilRegraVO obterRegraContabilParaSacadoContaPagar(ConfiguracaoContabilVO conf, TipoSacado tipoSacado, Integer codigoSacado);
	
	ConfiguracaoContabilRegraVO obterRegraContabilParaAdiantamentoContaPagar(ConfiguracaoContabilVO conf, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO );

	ConfiguracaoContabilRegraVO obterRegraContabilParaContaPagar(ConfiguracaoContabilVO conf, ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO fpnpVO);

	ConfiguracaoContabilRegraVO obterRegraContabilParaContaPagarComCentroResultadoOrigem(ConfiguracaoContabilVO conf, ContaPagarVO contaPagar, CentroResultadoOrigemVO cro, FormaPagamentoNegociacaoPagamentoVO fpnpVO);
	

	void gerarLancamentoContabilPorNegociacaoContaPagar(NegociacaoContaPagarVO negociacaoContaPagarVO, Boolean forcarRecarregamentoConfiguracaoContabil, UsuarioVO usuarioLogado) throws Exception;	

}
