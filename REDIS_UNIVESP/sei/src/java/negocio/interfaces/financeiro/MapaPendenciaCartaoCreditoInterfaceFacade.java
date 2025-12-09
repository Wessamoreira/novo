package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoTotalVO;
import negocio.comuns.financeiro.MapaPendenciaCartaoCreditoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MapaPendenciaCartaoCreditoInterfaceFacade {
    

    public void executarBaixaParcelaCartaoCredito(List<MapaPendenciaCartaoCreditoTotalVO> listaMapaPendenciaCartaoCreditoVO, Boolean unidadeMatriz, Date dataVencimento, Date dataRecebimento, FormaPagamentoVO formaPagamentoVO,  UsuarioVO usuario) throws Exception;
    
    public void executarEstornoBaixa(List<MapaPendenciaCartaoCreditoTotalVO> listaMapaPendenciaCartaoCreditoTotalVO, Boolean unidadeMatriz, UsuarioVO usuario) throws Exception;
    
    public void persitirAlteracoesAjusteValorLiquido(MapaPendenciaCartaoCreditoVO obj, UsuarioVO usuarioVO) throws Exception;
    
    public void persitirAlteracoesNegociacaoRecebimento(MapaPendenciaCartaoCreditoVO obj,  ConfiguracaoFinanceiroVO conf, UsuarioVO usuarioVO) throws Exception;

    public void alterarSaldoContaCorrente(MapaPendenciaCartaoCreditoTotalVO obj, String tipoMovimentacao, Double valor, UsuarioVO usuario) throws Exception;

    public List<MapaPendenciaCartaoCreditoVO> consultarPorOrigemFormaPagamentoNegociacaoRecebimentoCartaoCredito(String formapagamentonegociacaorecebimentocartaocredito, UsuarioVO usuario) throws Exception;
    
    public MapaPendenciaCartaoCreditoVO consultarPorCodigoFormaPagamentoNegociacaoRecebimentoCartaoCredito(Integer formapagamentonegociacaorecebimentocartaocredito, UsuarioVO usuario) throws Exception;
    
    public List<MapaPendenciaCartaoCreditoVO> consultarPorParcelasCartaoCredito(String situacao, Date dataEmissaoInicial, Date dataEmissaoFinal, Date dataVencimentoInicial, Date dataVencimentoFinal, Date dataRecebimentoOperadoraInicial, Date dataRecebimentoOperadoraFinal,  List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, Integer contaCorrente, String ordenarPor, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void executarCalculoValorPagar(MapaPendenciaCartaoCreditoVO obj) throws Exception; 
  
	
	List<MapaPendenciaCartaoCreditoVO> consultarOperacoesInstituicaoDCC(boolean previsto, boolean recebidos, boolean recusado, boolean cancelado, boolean cancelamentopendente, boolean estornopendente, Date dataEmissaoInicial, Date dataEmissaoFinal, Date dataVencimentoInicial, Date dataVencimentoFinal, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, Integer contaCorrente, String ordenarPor, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<MapaPendenciaCartaoCreditoTotalVO> realizarCalculoMapaPendenciaCartaCreditoTotal(Date dataRecebimento, List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs) throws Exception;

	void realizarCalculoMapaPendenciaCartaCreditoTotal(MapaPendenciaCartaoCreditoVO mapaPendenciaCartaoCreditoVO, List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs, boolean adicionar, Date dataRecebimento) throws Exception;

	void removerMapaPendenciaCartaoPorMapaPendenciaCartaoCreditoTotal(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO, List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs) throws Exception;

	void selecionarMapaPendenciaCartaoPorMapaPendenciaCartaoCreditoTotal(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO, List<MapaPendenciaCartaoCreditoTotalVO> mapaPendenciaCartaoCreditoTotalVOs, Date dataRecebimento) throws Exception;

	void realizarAtualizacaoDataPrevisao(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, Date dataPrevisao, UsuarioVO usuarioVO) throws Exception;

	void realizarAlteracaoTaxa(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs, Double taxa, boolean taxaAntecipacao, UsuarioVO usuarioVO) throws Exception;
}
