package negocio.comuns.compras;

import java.util.Date;
import java.util.Optional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.compras.Requisicao;

/**
 * 
 * @see SuperVO
 * @see Requisicao
 */
public class RequisicaoItemVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private RequisicaoVO requisicaoVO;
	private Double quantidadeSolicitada;
	private Double quantidadeAutorizada;
	private Double quantidadeEntregue;
	private Double quantidadeEstoque;
	private Integer cotacao;
	private ProdutoServicoVO produtoServico;
	private Double valorUnitario;
	private Date dataPrevisaoPagamento;
	private String justificativa;
	private UnidadeEnsinoVO unidadeEnsinoEstoqueRetirada;
	private CompraItemVO compraItemVO;
	private TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum;
	private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	private String tipoAutorizacao_Apresentar;

	public RequisicaoItemVO() {
		super();
		inicializarDados();
	}

	public static void validarDados(RequisicaoItemVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		
		if ((obj.getProdutoServico() == null) || (obj.getProdutoServico().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo PRODUTO (Itens da Requisição) deve ser informado.");
		}
		
		if (obj.getQuantidadeSolicitada().doubleValue() == 0) {
			throw new ConsistirException("O campo QUANTIDADE SOLICITADA (Itens da Requisição) deve ser informado.");
		}
		
//		if (obj.getDataPrevisaoPagamento() == null && (!obj.getProdutoServico().getExigeCompraCotacao())) {
//			throw new ConsistirException("A DATA PREVISÃO PGTO (Itens da Requisição) deve ser informada.");
//		}
//		
		if (((obj.getValorUnitario() == null) || (obj.getValorUnitario().equals(0.0))) && (!obj.getProdutoServico().getExigeCompraCotacao())) {
			throw new ConsistirException("O VALOR UNITÁRIO (Itens da Requisição) do item deve ser informado, pois o mesmo não exige cotação.");
		}
		Uteis.checkState(obj.getProdutoServico().isJustificativaRequisicaoObrigatoria() && !Uteis.isAtributoPreenchido(obj.getJustificativa()), "O campo Justificativa (Itens da Requisição) deve ser informado.");
	}

	public void inicializarDados() {
		setCodigo(0);
		setQuantidadeSolicitada(0.0);
		setQuantidadeAutorizada(0.0);
		setQuantidadeEntregue(0.0);
		setCotacao(0);
		setValorUnitario(0.0);
		setDataPrevisaoPagamento(null);
	}

	/**
	 * Retorna o objeto da classe <code>Produto</code> relacionado com ( <code>RequisicaoItem</code>).
	 */
	public ProdutoServicoVO getProdutoServico() {
		if (produtoServico == null) {
			produtoServico = new ProdutoServicoVO();
		}
		return (produtoServico);
	}

	public void indeferirRequisicaoItem() {
		setQuantidadeAutorizada(0.0);
	}
	
	public void autorizarRequisicaoItem() {
		setQuantidadeAutorizada(getQuantidadeSolicitada());
	}
	
	public boolean isApresentarBotaoIndeferirRequisicaoItem(){
		return Uteis.isAtributoPreenchido(getQuantidadeAutorizada());
	}

	/**
	 * Define o objeto da classe <code>Produto</code> relacionado com ( <code>RequisicaoItem</code>).
	 */
	public void setProdutoServico(ProdutoServicoVO obj) {
		this.produtoServico = obj;
	}

	public Integer getCotacao() {
		return (cotacao);
	}

	public void setCotacao(Integer cotacao) {
		this.cotacao = cotacao;
	}

	public Double getQuantidadeEntregue() {
		return (quantidadeEntregue);
	}

	public void setQuantidadeEntregue(Double quantidadeEntregue) {
		this.quantidadeEntregue = quantidadeEntregue;
	}	

	public Double getQuantidadeAutorizada() {
		return quantidadeAutorizada;
	}

	public void setQuantidadeAutorizada(Double quantidadeAutorizada) {
		this.quantidadeAutorizada = quantidadeAutorizada;
	}

	public Double getQuantidadeSolicitada() {
		return (quantidadeSolicitada);
	}

	public void setQuantidadeSolicitada(Double quantidadeSolicitada) {
		this.quantidadeSolicitada = quantidadeSolicitada;
	}

	public RequisicaoVO getRequisicaoVO() {
		requisicaoVO = Optional.ofNullable(requisicaoVO).orElse(new RequisicaoVO());
		return requisicaoVO;
	}

	public void setRequisicaoVO(RequisicaoVO requisicaoVO) {
		this.requisicaoVO = requisicaoVO;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Double getValorTotal() {
		return getValorUnitario() * getQuantidadeAutorizada();
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}
	
	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	

	public Date getDataPrevisaoPagamento() {
		return dataPrevisaoPagamento;
	}

	public String getDataPrevisaoPagamento_Apresentar() {
		return Uteis.getData(dataPrevisaoPagamento);
	}

	public void setDataPrevisaoPagamento(Date dataPrevisaoPagamento) {
		this.dataPrevisaoPagamento = dataPrevisaoPagamento;
	}

	public String getJustificativaApresentar() {
		return UteisTexto.limitarQuantidadeCaracteresComIndicadorMaisTexto(getJustificativa(), 30, true);
	}
	
	public String getJustificativa() {
		this.justificativa = Optional.ofNullable(this.justificativa).orElse("");
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Double getQuantidadeEstoque() {
		quantidadeEstoque = Optional.ofNullable(quantidadeEstoque).orElse(0.0);
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Double quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoEstoqueRetirada() {
		unidadeEnsinoEstoqueRetirada = Optional.ofNullable(unidadeEnsinoEstoqueRetirada).orElse(new UnidadeEnsinoVO());
		return unidadeEnsinoEstoqueRetirada;
	}

	public void setUnidadeEnsinoEstoqueRetirada(UnidadeEnsinoVO unidadeEnsinoEstoqueRetirada) {
		this.unidadeEnsinoEstoqueRetirada = unidadeEnsinoEstoqueRetirada;
	}

	public CompraItemVO getCompraItemVO() {
		compraItemVO = Optional.ofNullable(compraItemVO).orElse(new CompraItemVO());
		return compraItemVO;
	}

	public void setCompraItemVO(CompraItemVO compraItemVO) {
		this.compraItemVO = compraItemVO;
	}

	public TipoAutorizacaoRequisicaoEnum getTipoAutorizacaoRequisicaoEnum() {
		if(tipoAutorizacaoRequisicaoEnum == null) {
			tipoAutorizacaoRequisicaoEnum = TipoAutorizacaoRequisicaoEnum.NENHUM;
		}
		return tipoAutorizacaoRequisicaoEnum;
	}

	public void setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum) {
		this.tipoAutorizacaoRequisicaoEnum = tipoAutorizacaoRequisicaoEnum;
	}
	
	public boolean isExisteCotacao(){
		return Uteis.isAtributoPreenchido(getCotacao());
	}

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO getItemSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		if (itemSolicitacaoOrcamentoPlanoOrcamentarioVO == null) {
			itemSolicitacaoOrcamentoPlanoOrcamentarioVO = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO) {
		this.itemSolicitacaoOrcamentoPlanoOrcamentarioVO = itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public String getTipoAutorizacao_Apresentar() {
		if(getTipoAutorizacaoRequisicaoEnum().isCompraDireta()) {
			tipoAutorizacao_Apresentar = "Compra Direta";
		}
		else if(getTipoAutorizacaoRequisicaoEnum().isCotacao()) {
			tipoAutorizacao_Apresentar = "Cotação";
		}
		else if (getTipoAutorizacaoRequisicaoEnum().isRetirada()) {
			tipoAutorizacao_Apresentar = "Retirada";
		}
		else {
			tipoAutorizacao_Apresentar = "Nenhum";
		}
			
		return tipoAutorizacao_Apresentar;
	}

	public void setTipoAutorizacao_Apresentar(String tipoAutorizacao_Apresentar) {
		this.tipoAutorizacao_Apresentar = tipoAutorizacao_Apresentar;
	}
	
	
}
