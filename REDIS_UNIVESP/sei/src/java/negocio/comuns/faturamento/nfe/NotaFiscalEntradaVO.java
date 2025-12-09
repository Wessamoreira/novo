package negocio.comuns.faturamento.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.OrdenarNotaFiscalEntradaItemEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNotaFiscalEntradaEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;

/**
 * 
 * @author Pedro Otimize
 *
 */
public class NotaFiscalEntradaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 377232616804220424L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private FornecedorVO fornecedorVO;
	private TipoNotaFiscalEntradaEnum tipoNotaFiscalEntradaEnum;
	private NaturezaOperacaoVO naturezaOperacaoVO;
	private Date dataEntrada;
	private Date dataEmissao;
	private Long numero;
	private String serie;
	private Double totalNotaEntrada;
	private Double totalContaPagar;
	private Double totalImpostoRetido;
	private Double liquidoPagar;
	private Double totalImpostoPorcentagem;
	private Double totalImpostoValor;
	private Double totalRecebimentoCompra;
	private List<NotaFiscalEntradaItemVO> listaNotaFiscalEntradaItem;
	private List<NotaFiscalEntradaImpostoVO> listaNotaFiscalEntradaImposto;
	private List<NotaFiscalEntradaRecebimentoCompraVO> listaNotaFiscalEntradaRecebimentoCompra;
	private List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs;
	private List<ContaPagarVO> listaContaPagar;
	private List<ContaPagarVO> listaContaPagarOutrasOrigem;
	private List<ContaPagarVO> listaContaPagarExcluidas;
	private List<LancamentoContabilVO> listaLancamentoContabeisDebito;
	private List<LancamentoContabilVO> listaLancamentoContabeisCredito;
	
	private Date dataRegistro;
	private UsuarioVO usuarioCadastro;
	/**
	 * transient
	 */
	private boolean lancamentoContabil = false;
	private tipoLancamentoContabilNotaFiscalEntrada tipoLancamentoContabilNotaFiscalEntradaEnum;
	private List<ContaPagarVO> listaAdiantamentosUtilizadosAbaterContasPagar;
	private Double valorTotalAtiantamentosAbaterContasPagar;
	private ConfiguracaoContabilVO configuracaoContabilVO;
	private Double totalNotaEntradaIncio;
	private Double totalNotaEntradaFim;
	private Double notaEntradaLiquidoAPagarInicio;
	private Double notaEntradaLiquidoAPagarFim;
	private ProdutoServicoVO produtoServicoVO;

	public enum enumCampoConsultaNotaFiscalEntrada {
		NUMERO, FORNECEDOR, UNIDADEENSINO, CODIGO,
	}

	public enum tipoLancamentoContabilNotaFiscalEntrada {
		NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO, NOTA_FISCAL_ENTRADA_IMPOSTO;

		public boolean isCategoriaProduto() {
			return name().equals(tipoLancamentoContabilNotaFiscalEntrada.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO.name());
		}

		public boolean isImposto() {
			return name().equals(tipoLancamentoContabilNotaFiscalEntrada.NOTA_FISCAL_ENTRADA_IMPOSTO.name());
		}
	}

	public void limparCamposLista() {
		getListaNotaFiscalEntradaImposto().clear();
		getListaNotaFiscalEntradaItem().clear();
		getListaNotaFiscalEntradaRecebimentoCompra().clear();
		getListaContaPagar().clear();
		getListaContaPagarOutrasOrigem().clear();
		getListaLancamentoContabeisCredito().clear();
		getListaLancamentoContabeisDebito().clear();
	}

	public void atualizarTotalizadoresLista() {
		setTotalNotaEntrada(getListaNotaFiscalEntradaItem().stream().mapToDouble(NotaFiscalEntradaItemVO::getValorTotal).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
		setTotalRecebimentoCompra(getListaNotaFiscalEntradaRecebimentoCompra().stream().mapToDouble(p -> p.getRecebimentoCompraVO().getValorTotal()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
		setTotalImpostoPorcentagem(getListaNotaFiscalEntradaImposto().stream().mapToDouble(NotaFiscalEntradaImpostoVO::getPorcentagem).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8)));
		setTotalImpostoValor(getListaNotaFiscalEntradaImposto().stream().mapToDouble(NotaFiscalEntradaImpostoVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
		setTotalImpostoRetido(getListaNotaFiscalEntradaImposto().stream().filter(NotaFiscalEntradaImpostoVO::isRetido).mapToDouble(NotaFiscalEntradaImpostoVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
		setTotalContaPagar(getTotalContaPagarNotaFiscal() + getTotalContaPagarOutrasOrigem());
		setLiquidoPagar(Uteis.arrendondarForcando2CadasDecimais(getTotalNotaEntrada() - getTotalImpostoRetido()));		
	}
	
	public void atualizarDatasParaLancamentoContabil() {
		getListaLancamentoContabeisCredito().stream()
		.forEach(p->{
			p.setDataRegistro(getDataEmissao());
			p.setDataCompensacao(getDataEntrada());
		});
		getListaLancamentoContabeisDebito().stream()
		.forEach(p->{
			p.setDataRegistro(getDataEmissao());
			p.setDataCompensacao(getDataEntrada());
		});				
	}
	
	
	public NotaFiscalEntradaVO clonar() {
		try {
			NotaFiscalEntradaVO notaFiscalEntradaVO = (NotaFiscalEntradaVO) super.clone();
			notaFiscalEntradaVO.setNovoObj(true);
			notaFiscalEntradaVO.setCodigo(0);
			notaFiscalEntradaVO.setUnidadeEnsinoVO(unidadeEnsinoVO);
			notaFiscalEntradaVO.setFornecedorVO(fornecedorVO);
			notaFiscalEntradaVO.setNaturezaOperacaoVO(naturezaOperacaoVO);
			notaFiscalEntradaVO.setTipoNotaFiscalEntradaEnum(tipoNotaFiscalEntradaEnum);
			notaFiscalEntradaVO.setSerie(serie);
			notaFiscalEntradaVO.setDataEmissao(new Date());
			notaFiscalEntradaVO.setDataEntrada(new Date());
			notaFiscalEntradaVO.setTotalContaPagar(0.0);
			notaFiscalEntradaVO.getListaNotaFiscalEntradaItem().stream().forEach(notaItem -> zerarNotaItem(notaItem,notaFiscalEntradaVO));
			notaFiscalEntradaVO.getListaNotaFiscalEntradaImposto().stream().forEach(imposto -> zerarImposto(imposto,notaFiscalEntradaVO));
			notaFiscalEntradaVO.setListaNotaFiscalEntradaRecebimentoCompra(new ArrayList<NotaFiscalEntradaRecebimentoCompraVO>());
			notaFiscalEntradaVO.setListaLancamentoContabeisCredito(new ArrayList<LancamentoContabilVO>());
			notaFiscalEntradaVO.setListaLancamentoContabeisDebito(new ArrayList<LancamentoContabilVO>());
			notaFiscalEntradaVO.setListaContaPagar(new ArrayList<ContaPagarVO>());
			notaFiscalEntradaVO.setListaContaPagarOutrasOrigem(new ArrayList<ContaPagarVO>());
//			notaFiscalEntradaVO.setListaCentroResultadoOrigemVOs((List<CentroResultadoOrigemVO>) Uteis.clonar((Serializable) this.listaCentroResultadoOrigemVOs));
			notaFiscalEntradaVO.getListaCentroResultadoOrigemVOs().stream().forEach(centroResultado -> zerarCentroDeResultado(centroResultado));
			return notaFiscalEntradaVO;
		} catch (Exception e) {
				throw new StreamSeiException(e);
		}
	}

	
	private void zerarNotaItem(NotaFiscalEntradaItemVO notaItem,NotaFiscalEntradaVO notaFiscalEntradaVO) {
		notaItem.setCodigo(0);
		notaItem.setNovoObj(true);
		notaItem.setNotaFiscalEntradaVO(notaFiscalEntradaVO);
		notaItem.getListaNotaFiscalEntradaItemRecebimentoVOs().clear();
		notaItem.getListaCentroResultadoOrigemVOs().stream().forEach(centroResultado -> zerarCentroDeResultado(centroResultado));
	}
	
	private void zerarCentroDeResultado(CentroResultadoOrigemVO centroResultado) {
		centroResultado.setCodigo(0);
		centroResultado.setCodOrigem("");
		centroResultado.setNovoObj(true);
	}
	
	private void zerarImposto(NotaFiscalEntradaImpostoVO imposto,NotaFiscalEntradaVO notaFiscalEntradaVO) {
		imposto.setCodigo(0);
		imposto.setNovoObj(true);
		imposto.setNotaFiscalEntradaVO(notaFiscalEntradaVO);
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public TipoNotaFiscalEntradaEnum getTipoNotaFiscalEntradaEnum() {
		return tipoNotaFiscalEntradaEnum;
	}

	public void setTipoNotaFiscalEntradaEnum(TipoNotaFiscalEntradaEnum tipoNotaFiscalEntradaEnum) {
		this.tipoNotaFiscalEntradaEnum = tipoNotaFiscalEntradaEnum;
	}

	public NaturezaOperacaoVO getNaturezaOperacaoVO() {
		if (naturezaOperacaoVO == null) {
			naturezaOperacaoVO = new NaturezaOperacaoVO();
		}
		return naturezaOperacaoVO;
	}

	public void setNaturezaOperacaoVO(NaturezaOperacaoVO naturezaOperacaoVO) {
		this.naturezaOperacaoVO = naturezaOperacaoVO;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Long getNumero() {
		if (numero == null) {
			numero = 0L;
		}
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getSerie() {
		if (serie == null) {
			serie = "";
		}
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	

	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public UsuarioVO getUsuarioCadastro() {
		if (usuarioCadastro == null) {
			usuarioCadastro = new UsuarioVO();
		}
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(UsuarioVO usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public List<NotaFiscalEntradaItemVO> getListaNotaFiscalEntradaItem() {
		if (listaNotaFiscalEntradaItem == null) {
			listaNotaFiscalEntradaItem = new ArrayList<>();
		}
		return listaNotaFiscalEntradaItem;
	}

	public void setListaNotaFiscalEntradaItem(List<NotaFiscalEntradaItemVO> listaNotaFiscalEntradaItem) {
		this.listaNotaFiscalEntradaItem = listaNotaFiscalEntradaItem;
	}

	public List<NotaFiscalEntradaImpostoVO> getListaNotaFiscalEntradaImposto() {
		if (listaNotaFiscalEntradaImposto == null) {
			listaNotaFiscalEntradaImposto = new ArrayList<>();
		}
		return listaNotaFiscalEntradaImposto;
	}

	public void setListaNotaFiscalEntradaImposto(List<NotaFiscalEntradaImpostoVO> listaNotaFiscalEntradaImposto) {
		this.listaNotaFiscalEntradaImposto = listaNotaFiscalEntradaImposto;
	}

	public List<NotaFiscalEntradaRecebimentoCompraVO> getListaNotaFiscalEntradaRecebimentoCompra() {
		if (listaNotaFiscalEntradaRecebimentoCompra == null) {
			listaNotaFiscalEntradaRecebimentoCompra = new ArrayList<>();
		}
		return listaNotaFiscalEntradaRecebimentoCompra;
	}

	public void setListaNotaFiscalEntradaRecebimentoCompra(List<NotaFiscalEntradaRecebimentoCompraVO> listaNotaFiscalEntradaCompra) {
		this.listaNotaFiscalEntradaRecebimentoCompra = listaNotaFiscalEntradaCompra;
	}

	public List<CentroResultadoOrigemVO> getListaCentroResultadoOrigemVOs() {
		listaCentroResultadoOrigemVOs = Optional.ofNullable(listaCentroResultadoOrigemVOs).orElse(new ArrayList<>());
		return listaCentroResultadoOrigemVOs;
	}

	public void setListaCentroResultadoOrigemVOs(List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs) {
		this.listaCentroResultadoOrigemVOs = listaCentroResultadoOrigemVOs;
	}

	public List<ContaPagarVO> getListaContaPagar() {
		listaContaPagar = Optional.ofNullable(listaContaPagar).orElse(new ArrayList<>());
		return listaContaPagar;
	}

	public void setListaContaPagar(List<ContaPagarVO> listaContaPagar) {
		this.listaContaPagar = listaContaPagar;
	}

	public List<ContaPagarVO> getListaContaPagarOutrasOrigem() {
		listaContaPagarOutrasOrigem = Optional.ofNullable(listaContaPagarOutrasOrigem).orElse(new ArrayList<>());
		return listaContaPagarOutrasOrigem;
	}

	public void setListaContaPagarOutrasOrigem(List<ContaPagarVO> listaContaPagarOutrasOrigem) {
		this.listaContaPagarOutrasOrigem = listaContaPagarOutrasOrigem;
	}

	public List<ContaPagarVO> getListaContaPagarExcluidas() {
		listaContaPagarExcluidas = Optional.ofNullable(listaContaPagarExcluidas).orElse(new ArrayList<>());
		return listaContaPagarExcluidas;
	}

	public void setListaContaPagarExcluidas(List<ContaPagarVO> listaContaPagarExcluidas) {
		this.listaContaPagarExcluidas = listaContaPagarExcluidas;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisDebito() {
		listaLancamentoContabeisDebito = Optional.ofNullable(listaLancamentoContabeisDebito).orElse(new ArrayList<>());
		return listaLancamentoContabeisDebito;
	}

	public void setListaLancamentoContabeisDebito(List<LancamentoContabilVO> listaLancamentoContabeisDebito) {
		this.listaLancamentoContabeisDebito = listaLancamentoContabeisDebito;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisCredito() {
		listaLancamentoContabeisCredito = Optional.ofNullable(listaLancamentoContabeisCredito).orElse(new ArrayList<>());
		return listaLancamentoContabeisCredito;
	}

	public void setListaLancamentoContabeisCredito(List<LancamentoContabilVO> listaLancamentoContabeisCredito) {
		this.listaLancamentoContabeisCredito = listaLancamentoContabeisCredito;
	}

	public Double getQuantidadeCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getQuantidade).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getPorcentagemCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getPorcentagem).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
	}
	
	public Double getPrecoCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public boolean isLancamentoContabil() {
		return lancamentoContabil;
	}

	public void setLancamentoContabil(boolean lancamentoContabil) {
		this.lancamentoContabil = lancamentoContabil;
	}

	public tipoLancamentoContabilNotaFiscalEntrada getTipoLancamentoContabilNotaFiscalEntradaEnum() {
		tipoLancamentoContabilNotaFiscalEntradaEnum = Optional.ofNullable(tipoLancamentoContabilNotaFiscalEntradaEnum).orElse(tipoLancamentoContabilNotaFiscalEntrada.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO);
		return tipoLancamentoContabilNotaFiscalEntradaEnum;
	}

	public void setTipoLancamentoContabilNotaFiscalEntradaEnum(tipoLancamentoContabilNotaFiscalEntrada tipoLancamentoContabilNotaFiscalEntradaEnum) {
		this.tipoLancamentoContabilNotaFiscalEntradaEnum = tipoLancamentoContabilNotaFiscalEntradaEnum;
	}

	public Map<TipoCriacaoContaPagarEnum, List<NotaFiscalEntradaRecebimentoCompraVO>> getMapaTipoCriacaoContaPagarNotaFiscal() {
		if (Uteis.isAtributoPreenchido(getListaNotaFiscalEntradaRecebimentoCompra())) {
			return getListaNotaFiscalEntradaRecebimentoCompra().stream().filter(NotaFiscalEntradaRecebimentoCompraVO::isExisteRecebimentoItemSelecionado).collect(Collectors.groupingBy(p -> p.getRecebimentoCompraVO().getTipoCriacaoContaPagarEnum()));
		}
		return new HashMap<>();
	}

	public Map<Integer, List<NotaFiscalEntradaItemVO>> getMapaCategoriaProdutoNotaFiscal() {
		getListaNotaFiscalEntradaItem().sort(OrdenarNotaFiscalEntradaItemEnum.CODIGO);
		if (Uteis.isAtributoPreenchido(getListaNotaFiscalEntradaItem())) {
			return getListaNotaFiscalEntradaItem().stream().collect(Collectors.groupingBy(p -> p.getProdutoServicoVO().getCategoriaProduto().getCodigo()));
		}
		return new HashMap<>();
	}

	public Double getValorTotalImpostoRetidoPorcentagem() {
		return getListaNotaFiscalEntradaImposto().stream().filter(NotaFiscalEntradaImpostoVO::isRetido).mapToDouble(NotaFiscalEntradaImpostoVO::getPorcentagem).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
	}

	public Double getTotalRestanteContaPagar() {
		return getLiquidoPagar() - getTotalContaPagar();
	}

	public Double getTotalContaPagarNotaFiscal() {
		return getListaContaPagar().stream().mapToDouble(ContaPagarVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getTotalContaPagarOutrasOrigem() {
		return getListaContaPagarOutrasOrigem().stream().mapToDouble(ContaPagarVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getTotalLancamentoContabeisCredito() {
		return getListaLancamentoContabeisCredito().stream().mapToDouble(LancamentoContabilVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getTotalLancamentoContabeisDebito() {
		return getListaLancamentoContabeisDebito().stream().mapToDouble(LancamentoContabilVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double calcularValorContabilPorMapaCategoriaProduto(Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapa) {
		Double valorTotalMapa = mapa.getValue().stream().mapToDouble(NotaFiscalEntradaItemVO::getValorTotal).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
		return valorTotalMapa - ((valorTotalMapa * getValorTotalImpostoRetidoPorcentagem()) / 100);
	}

	public void calcularValorNotaFiscalEntradaImposto() {
		if (Uteis.isAtributoPreenchido(getListaNotaFiscalEntradaImposto())) {
			getListaNotaFiscalEntradaImposto().stream()
					.forEach(p -> {
						p.setNotaFiscalEntradaVO(this);
						p.calcularValorNotaFiscalEntradaImposto();
					});
		}
	}

	public String getListaCodigoCompra(List<NotaFiscalEntradaRecebimentoCompraVO> lista) {
		StringBuilder sb = new StringBuilder();
		lista.stream().forEach(p -> UteisTexto.addCampoParaClausaIn(sb, "'" + p.getRecebimentoCompraVO().getCompra().getCodigo() + "'"));
		return sb.toString().isEmpty() ? "'0'" : sb.toString();
	}

	public String getListaCodigoRecebimentoCompra(List<NotaFiscalEntradaRecebimentoCompraVO> lista) {
		StringBuilder sb = new StringBuilder();
		lista.stream().forEach(p -> UteisTexto.addCampoParaClausaIn(sb, "'" + p.getRecebimentoCompraVO().getCodigo() + "'"));
		return sb.toString().isEmpty() ? "'0'" : sb.toString();
	}

	public Double getTotalContaPagar() {
		totalContaPagar = Optional.ofNullable(totalContaPagar).orElse(0.0);
		return totalContaPagar;
	}

	public void setTotalContaPagar(Double totalContaPagar) {
		this.totalContaPagar = totalContaPagar;
	}

	public Double getTotalNotaEntrada() {
		totalNotaEntrada = Optional.ofNullable(totalNotaEntrada).orElse(0.0);
		return totalNotaEntrada;
	}

	public void setTotalNotaEntrada(Double totalNotaEntrada) {
		this.totalNotaEntrada = totalNotaEntrada;
	}

	public Double getTotalImpostoRetido() {
		totalImpostoRetido = Optional.ofNullable(totalImpostoRetido).orElse(0.0);
		return totalImpostoRetido;
	}

	public void setTotalImpostoRetido(Double totalImpostoRetido) {
		this.totalImpostoRetido = totalImpostoRetido;
	}

	public Double getLiquidoPagar() {
		liquidoPagar = Optional.ofNullable(liquidoPagar).orElse(0.0);
		return liquidoPagar;
	}

	public void setLiquidoPagar(Double liquidoPagar) {
		this.liquidoPagar = liquidoPagar;
	}

	public Double getTotalRecebimentoCompra() {
		totalRecebimentoCompra = Optional.ofNullable(totalRecebimentoCompra).orElse(0.0);
		return totalRecebimentoCompra;
	}

	public void setTotalRecebimentoCompra(Double totalRecebimentoCompra) {
		this.totalRecebimentoCompra = totalRecebimentoCompra;
	}

	public Double getTotalImpostoPorcentagem() {
		totalImpostoPorcentagem = Optional.ofNullable(totalImpostoPorcentagem).orElse(0.0);
		return totalImpostoPorcentagem;
	}

	public void setTotalImpostoPorcentagem(Double totalImpostoPorcentagem) {
		this.totalImpostoPorcentagem = totalImpostoPorcentagem;
	}

	public Double getTotalImpostoValor() {
		totalImpostoValor = Optional.ofNullable(totalImpostoValor).orElse(0.0);
		return totalImpostoValor;
	}

	public void setTotalImpostoValor(Double totalImpostoValor) {
		this.totalImpostoValor = totalImpostoValor;
	}

	public List<ContaPagarVO> getListaAdiantamentosUtilizadosAbaterContasPagar() {
		listaAdiantamentosUtilizadosAbaterContasPagar = Optional.ofNullable(listaAdiantamentosUtilizadosAbaterContasPagar).orElse(new ArrayList<>());
		return listaAdiantamentosUtilizadosAbaterContasPagar;
	}

	public void setListaAdiantamentosUtilizadosAbaterContasPagar(List<ContaPagarVO> listaAdiantamentosUtilizadosAbaterContasPagar) {
		this.listaAdiantamentosUtilizadosAbaterContasPagar = listaAdiantamentosUtilizadosAbaterContasPagar;
	}

	public Double getValorTotalAtiantamentosAbaterContasPagar() {
		valorTotalAtiantamentosAbaterContasPagar = Optional.ofNullable(valorTotalAtiantamentosAbaterContasPagar).orElse(0.0);
		return valorTotalAtiantamentosAbaterContasPagar;
	}

	public void setValorTotalAtiantamentosAbaterContasPagar(Double valorTotalAtiantamentosAbaterContasPagar) {
		this.valorTotalAtiantamentosAbaterContasPagar = valorTotalAtiantamentosAbaterContasPagar;
	}
	
	private Date dataEntradaAnterior;
	
	public Date getDataEntradaAnterior() {
		return dataEntradaAnterior;
	}
	
	public void setDataEntradaAnterior(Date dataEntradaAnterior) {
		this.dataEntradaAnterior = dataEntradaAnterior;
	}
		
	public Boolean getPermitiEditarValorCentroResultadoOrigem() {
		return !getPrecoCentroResultadoTotal().equals(getTotalNotaEntrada()) && getPorcentagemCentroResultadoTotal() >= 100.00;
	}
	
	public Boolean getPermitiEditarPorcentagemCentroResultadoOrigem() {
		return getPorcentagemCentroResultadoTotal() > 100.00 || (getPrecoCentroResultadoTotal().equals(getTotalNotaEntrada()) && getPorcentagemCentroResultadoTotal() < 100.00);
	}

	public ConfiguracaoContabilVO getConfiguracaoContabilVO() {
		if(configuracaoContabilVO == null) {
			configuracaoContabilVO =  new ConfiguracaoContabilVO();
		}
		return configuracaoContabilVO;
	}

	public void setConfiguracaoContabilVO(ConfiguracaoContabilVO configuracaoContabilVO) {
		this.configuracaoContabilVO = configuracaoContabilVO;
	}
	
	public Double getTotalNotaEntradaIncio() {
		return totalNotaEntradaIncio;
	}

	public void setTotalNotaEntradaIncio(Double totalNotaEntradaIncio) {
		this.totalNotaEntradaIncio = totalNotaEntradaIncio;
	}

	public Double getTotalNotaEntradaFim() {
		return totalNotaEntradaFim;
	}

	public void setTotalNotaEntradaFim(Double totalNotaEntradaFim) {
		this.totalNotaEntradaFim = totalNotaEntradaFim;
	}

	public Double getNotaEntradaLiquidoAPagarInicio() {
		return notaEntradaLiquidoAPagarInicio;
	}

	public void setNotaEntradaLiquidoAPagarInicio(Double notaEntradaLiquidoAPagarInicio) {
		this.notaEntradaLiquidoAPagarInicio = notaEntradaLiquidoAPagarInicio;
	}

	public Double getNotaEntradaLiquidoAPagarFim() {
		return notaEntradaLiquidoAPagarFim;
	}

	public void setNotaEntradaLiquidoAPagarFim(Double notaEntradaLiquidoAPagarFim) {
		this.notaEntradaLiquidoAPagarFim = notaEntradaLiquidoAPagarFim;
	}

	public ProdutoServicoVO getProdutoServicoVO() {
		if(produtoServicoVO == null) {
			produtoServicoVO = new ProdutoServicoVO();
		}
		return produtoServicoVO;
	}

	public void setProdutoServicoVO(ProdutoServicoVO produtoServicoVO) {
		this.produtoServicoVO = produtoServicoVO;
	}
}
