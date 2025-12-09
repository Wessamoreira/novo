package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class CompraVO extends SuperVO {

	private Integer codigo;
	private String situacaoFinanceira;
	private String situacaoRecebimento;
	private Date data;
	private UnidadeEnsinoVO unidadeEnsino;
	private List<CompraItemVO> compraItemVOs;
	private FornecedorVO fornecedor;
	private CotacaoVO cotacao;
	private UsuarioVO responsavel;
	private FormaPagamentoVO formaPagamento;
	private CondicaoPagamentoVO condicaoPagamento;
	private TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum;
	private ContaCorrenteVO contaCorrente;
	private String codigoItensCompra;
	private List<CondicaoPagamentoCotacaoVO> listaCondicaoPagamento;
	private List<RecebimentoCompraVO> recebimentoCompraVOs;	
	private List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs;
	private Date dataPrevisaoEntrega;
	/**
	 * transient
	 */
	private List<RequisicaoVO> listaRequisicaoVOs;
	private CategoriaProdutoVO categoriaProduto;
	public static final long serialVersionUID = 1L;

	public CompraVO() {
		super();
		inicializarDados();
	}

	public static void validarDados(CompraVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Compra) deve ser informado.");
		}
		if ((obj.getFornecedor() == null) || (obj.getFornecedor().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo FORNECEDOR (Compra) deve ser informado.");
		}
		if ((obj.getFormaPagamento() == null) || (obj.getFormaPagamento().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo FORMA DE PAGAMENTO (Compra) deve ser informado.");
		}
		if ((obj.getCondicaoPagamento() == null) || (obj.getCondicaoPagamento().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo CONDIÇÃO DE PAGAMENTO (Compra) deve ser informado.");
		}
		//Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCategoriaProduto()), "O campo Categoria de Produto (Compra) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(!obj.getPrecoTotal().equals(obj.getPrecoCentroResultadoTotal())), "O valor Total dos Itens da Compra está diferente do Total do Centro de Resultado.");
		if(obj.getPrecoTotal().equals(0.0)) {
			throw new ConsistirException("O Preço total da compra não pode ser R$ 0,00.");			
		}
	}

	public void realizarUpperCaseDados() {
	}

	public void inicializarDados() {
		setData(new Date());
	}

	

	public void excluirObjCompraItemVOs(Integer produto) {
		Iterator<CompraItemVO> i = getCompraItemVOs().iterator();
		while (i.hasNext()) {
			CompraItemVO objExistente = i.next();
			if (objExistente.getProduto().getCodigo().equals(produto)) {
				i.remove();
				return;
			}
		}
	}

	public void montarListaCondicaoPagamento(List<ParcelaCondicaoPagamentoVO> obj) throws Exception {
		setListaCondicaoPagamento(new ArrayList<>());
		Iterator<ParcelaCondicaoPagamentoVO> i = obj.iterator();
		while (i.hasNext()) {
			double valorParcela = 0;
			CondicaoPagamentoCotacaoVO condPgto = new CondicaoPagamentoCotacaoVO();
			ParcelaCondicaoPagamentoVO parcela = i.next();
			condPgto.setNrParcela(parcela.getNumeroParcela());
			condPgto.setDataVencimento(Uteis.obterDataFutura(getData(), parcela.getIntervalo()));
			valorParcela = Uteis.arrendondarForcando2CadasDecimais(getPrecoTotal() * (parcela.getPercentualValor() / 100));
			condPgto.setValorParcela((valorParcela));
			getListaCondicaoPagamento().add(condPgto);
		}
	}

	public Boolean getExisteCotacao() {
		return Uteis.isAtributoPreenchido(getCotacao());
	}

	public Boolean getExisteUnidadeEnsino() {
		return Uteis.isAtributoPreenchido(getUnidadeEnsino());
	}

	public boolean isExisteCategoriaProduto() {
		return Uteis.isAtributoPreenchido(getCategoriaProduto());
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	public CotacaoVO getCotacao() {
		if (cotacao == null) {
			cotacao = new CotacaoVO();
		}
		return (cotacao);
	}

	public void setCotacao(CotacaoVO obj) {
		this.cotacao = obj;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return (fornecedor);
	}

	public void setFornecedor(FornecedorVO obj) {
		this.fornecedor = obj;
	}

	public List<CompraItemVO> getCompraItemVOs() {
		if (compraItemVOs == null) {
			compraItemVOs = new ArrayList<>();
		}
		return (compraItemVOs);
	}

	public void setCompraItemVOs(List<CompraItemVO> compraItemVOs) {
		this.compraItemVOs = compraItemVOs;
	}

	public Date getData() {
		return (data);
	}

	public String getData_Apresentar() {
		if (data == null) {
			return "";
		}
		return (Uteis.getDataComHora(getData()));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public boolean isSituacaoRecebimentoFinalizado() {
		return Uteis.isAtributoPreenchido(getSituacaoRecebimento()) && getSituacaoRecebimento().equals("FI");
	}

	public boolean isSituacaoRecebimentoParcial() {
		return Uteis.isAtributoPreenchido(getSituacaoRecebimento()) && getSituacaoRecebimento().equals("PA");
	}

	public String getSituacaoRecebimento_Apresentar() {
		if (getSituacaoRecebimento().equals("FI")) {
			return "Finalizado";
		}
		if (getSituacaoRecebimento().equals("PE")) {
			return "Pendente";
		}
		if (getSituacaoRecebimento().equals("PA")) {
			return "Parcial";
		}
		return (getSituacaoRecebimento());
	}

	public String getSituacaoRecebimento() {
		if (situacaoRecebimento == null) {
			situacaoRecebimento = "PE";
		}
		return (situacaoRecebimento);
	}

	public void setSituacaoRecebimento(String situacaoRecebimento) {
		this.situacaoRecebimento = situacaoRecebimento;
	}

	public CondicaoPagamentoVO getCondicaoPagamento() {
		if (condicaoPagamento == null) {
			condicaoPagamento = new CondicaoPagamentoVO();
		}
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamentoVO condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<RecebimentoCompraVO> getRecebimentoCompraVOs() {
		if (recebimentoCompraVOs == null) {
			recebimentoCompraVOs = new ArrayList<>();
		}
		return recebimentoCompraVOs;
	}

	public void setRecebimentoCompraVOs(List<RecebimentoCompraVO> recebimentoCompraVOs) {
		this.recebimentoCompraVOs = recebimentoCompraVOs;
	}

	public String getSituacaoFinanceira_Apresentar() {
		if (getSituacaoFinanceira().equals("AP")) {
			return "A Pagar";
		}
		if (getSituacaoFinanceira().equals("PP")) {
			return "Pago Parcialmente";
		}
		if (getSituacaoFinanceira().equals("PA")) {
			return "Pago";
		}
		return (getSituacaoFinanceira());
	}

	public String getSituacaoFinanceira() {
		if (situacaoFinanceira == null) {
			situacaoFinanceira = "AP";
		}
		return (situacaoFinanceira);
	}

	public void setSituacaoFinanceira(String situacaoFinanceira) {
		this.situacaoFinanceira = situacaoFinanceira;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = new Integer(0);
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Double getPrecoTotal() {
		return getCompraItemVOs().stream().mapToDouble(p -> p.getPrecoUnitario() * p.getQuantidade()).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	public Double getQuantidadeAutorizadaoTotal() {
		return getListaRequisicaoVOs().stream().mapToDouble(RequisicaoVO::getQuantidadeTotalAutorizada).sum();
	}
	
	public Double getPrecoCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getValor).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getPorcentagemCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getPorcentagem).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
	}

	public Double getQuantidadeRequisicaoTotal() {
		return getCompraItemVOs().stream().mapToDouble(CompraItemVO::getQuantidadeRequisicao).sum();
	}
	
	public Double getQuantidadeAdicionalTotal() {
		return getCompraItemVOs().stream().mapToDouble(CompraItemVO::getQuantidadeAdicional).sum();
	}
	
	public Double getQuantidadeCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getQuantidade).sum();
	}
	
	public Double getQuantidadeTotal() {
		return getCompraItemVOs().stream().mapToDouble(CompraItemVO::getQuantidade).sum();
	}

	public List<CondicaoPagamentoCotacaoVO> getListaCondicaoPagamento() {
		if (listaCondicaoPagamento == null) {
			listaCondicaoPagamento = new ArrayList<>();
		}
		return listaCondicaoPagamento;
	}

	public void setListaCondicaoPagamento(List<CondicaoPagamentoCotacaoVO> listaCondicaoPagamento) {
		this.listaCondicaoPagamento = listaCondicaoPagamento;
	}

	public TipoCriacaoContaPagarEnum getTipoCriacaoContaPagarEnum() {
		if (tipoCriacaoContaPagarEnum == null) {
			tipoCriacaoContaPagarEnum = TipoCriacaoContaPagarEnum.COMPRA;
		}
		return tipoCriacaoContaPagarEnum;
	}

	public void setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum) {
		this.tipoCriacaoContaPagarEnum = tipoCriacaoContaPagarEnum;
	}

	public CategoriaProdutoVO getCategoriaProduto() {
		categoriaProduto = Optional.ofNullable(categoriaProduto).orElse(new CategoriaProdutoVO());
		return categoriaProduto;
	}

	public void setCategoriaProduto(CategoriaProdutoVO categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public ContaCorrenteVO getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = new ContaCorrenteVO();
		}
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteVO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public List<RequisicaoVO> getListaRequisicaoVOs() {
		listaRequisicaoVOs = Optional.ofNullable(listaRequisicaoVOs).orElse(new ArrayList<>());
		return listaRequisicaoVOs;
	}

	public void setListaRequisicaoVOs(List<RequisicaoVO> listaRequisicaoVOs) {
		this.listaRequisicaoVOs = listaRequisicaoVOs;
	}

	public List<CentroResultadoOrigemVO> getListaCentroResultadoOrigemVOs() {
		listaCentroResultadoOrigemVOs = Optional.ofNullable(listaCentroResultadoOrigemVOs).orElse(new ArrayList<>());
		return listaCentroResultadoOrigemVOs;
	}

	public void setListaCentroResultadoOrigemVOs(List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs) {
		this.listaCentroResultadoOrigemVOs = listaCentroResultadoOrigemVOs;
	}

	public void montarCodigoItensCompra() {
		setCodigoItensCompra("");
		getCompraItemVOs().stream().forEach(p->setCodigoItensCompra(getCodigoItensCompra() + p.getProduto().getCodigo()));
	}

	public String getCodigoItensCompra() {
		if (codigoItensCompra == null) {
			codigoItensCompra = "";
		}
		return codigoItensCompra;
	}

	public void setCodigoItensCompra(String codigoItensCompra) {
		this.codigoItensCompra = codigoItensCompra;
	}

	public Integer getQuantidadeDeElementosRecebimentoCompraVO() {
		return getRecebimentoCompraVOs().size();
	}

	public Date getDataPrevisaoEntrega() {
		return dataPrevisaoEntrega;
	}

	public void setDataPrevisaoEntrega(Date dataPrevisaoEntrega) {
		this.dataPrevisaoEntrega = dataPrevisaoEntrega;
	}
	
}
