package negocio.comuns.contabil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.enumeradores.TipoGeracaoIntegracaoContabilEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public class IntegracaoContabilVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3778816293298505544L;
	private Integer codigo;
	private TipoGeracaoIntegracaoContabilEnum tipoGeracaoIntegracaoContabilEnum;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String codigoIntegracaoContabil;
	private Date dataInicio;
	private Date dataTermino;
	private Integer lote;
	private Double valorLote;
	private Date dataGeracao;
	private UsuarioVO responsavel;
	private ArquivoVO arquivo;
	/**
	 * atributos transient
	 */
	private ConfiguracaoContabilVO configuracaoContabilVO;
	private List<LancamentoContabilVO> listaLancamentoContabil;
	private List<LancamentoContabilVO> listaLancamentoContabeisDebito;
	private List<LancamentoContabilVO> listaLancamentoContabeisCredito;
	private String textoArquivo;
	private String numeroNotaFiscalEntradaFiltro;
	private String nossaNumeroFiltro;
	private String sacadoFiltro;
	private String numeroContaCorrenteFiltro;
	private String codigoOrigemFiltro;
	private String planoContaFiltro;
	private String tipoCampoPlanoContaFiltro;

	private boolean origemNotaFiscalEntrada = true;
	private boolean origemContaReceber = true;
	private boolean origemContaPagar = true;
	private boolean origemMovFinanceira = true;
	private boolean origemNegociacaoContaPagar = true;
	private boolean origemMapaPendenciaCartao = true;

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

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabil() {
		if (listaLancamentoContabil == null) {
			listaLancamentoContabil = new ArrayList<LancamentoContabilVO>();
		}
		return listaLancamentoContabil;
	}

	public void setListaLancamentoContabil(List<LancamentoContabilVO> listaIntegracaoContabilLote) {
		this.listaLancamentoContabil = listaIntegracaoContabilLote;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Date getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public ArquivoVO getArquivo() {
		if (arquivo == null) {
			arquivo = new ArquivoVO();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}

	public Integer getLote() {
		return lote;
	}

	public void setLote(Integer lote) {
		this.lote = lote;
	}

	public Double getValorLote() {
		if (valorLote == null) {
			valorLote = 0.0;
		}
		return valorLote;
	}

	public void setValorLote(Double valorLote) {
		this.valorLote = valorLote;
	}

	public TipoGeracaoIntegracaoContabilEnum getTipoGeracaoIntegracaoContabilEnum() {
		return tipoGeracaoIntegracaoContabilEnum;
	}

	public void setTipoGeracaoIntegracaoContabilEnum(TipoGeracaoIntegracaoContabilEnum tipoGeracaoIntegracaoContabilEnum) {
		this.tipoGeracaoIntegracaoContabilEnum = tipoGeracaoIntegracaoContabilEnum;
	}

	public String getCodigoIntegracaoContabil() {
		return codigoIntegracaoContabil;
	}

	public void setCodigoIntegracaoContabil(String codigoIntegracaoContabil) {
		this.codigoIntegracaoContabil = codigoIntegracaoContabil;
	}

	public String getTextoArquivo() {
		return textoArquivo;
	}

	public void setTextoArquivo(String textoXml) {
		this.textoArquivo = textoXml;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisDebito() {
		if (listaLancamentoContabeisDebito == null) {
			listaLancamentoContabeisDebito = new ArrayList<>();
		}
		return listaLancamentoContabeisDebito;
	}

	public void setListaLancamentoContabeisDebito(List<LancamentoContabilVO> listaLancamentoContabeisDebito) {
		this.listaLancamentoContabeisDebito = listaLancamentoContabeisDebito;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisCredito() {
		if (listaLancamentoContabeisCredito == null) {
			listaLancamentoContabeisCredito = new ArrayList<>();
		}
		return listaLancamentoContabeisCredito;
	}

	public void setListaLancamentoContabeisCredito(List<LancamentoContabilVO> listaLancamentoContabeisCredito) {
		this.listaLancamentoContabeisCredito = listaLancamentoContabeisCredito;
	}

	public ConfiguracaoContabilVO getConfiguracaoContabilVO() {
		if (configuracaoContabilVO == null) {
			configuracaoContabilVO = new ConfiguracaoContabilVO();
		}
		return configuracaoContabilVO;
	}

	public void setConfiguracaoContabilVO(ConfiguracaoContabilVO configuracaoContabilVO) {
		this.configuracaoContabilVO = configuracaoContabilVO;
	}

	public String getNumeroNotaFiscalEntradaFiltro() {
		if (numeroNotaFiscalEntradaFiltro == null) {
			numeroNotaFiscalEntradaFiltro = "";
		}
		return numeroNotaFiscalEntradaFiltro;
	}

	public void setNumeroNotaFiscalEntradaFiltro(String numeroNotaFiscalEntradaFiltro) {
		this.numeroNotaFiscalEntradaFiltro = numeroNotaFiscalEntradaFiltro;
	}

	public String getSacadoFiltro() {
		if (sacadoFiltro == null) {
			sacadoFiltro = "";
		}
		return sacadoFiltro;
	}

	public void setSacadoFiltro(String sacadoFiltro) {
		this.sacadoFiltro = sacadoFiltro;
	}

	public String getNumeroContaCorrenteFiltro() {
		if (numeroContaCorrenteFiltro == null) {
			numeroContaCorrenteFiltro = "";
		}
		return numeroContaCorrenteFiltro;
	}

	public void setNumeroContaCorrenteFiltro(String numeroContaCorrenteFiltro) {
		this.numeroContaCorrenteFiltro = numeroContaCorrenteFiltro;
	}

	public String getPlanoContaFiltro() {
		if (planoContaFiltro == null) {
			planoContaFiltro = "";
		}
		return planoContaFiltro;
	}

	public void setPlanoContaFiltro(String planoContaFiltro) {
		this.planoContaFiltro = planoContaFiltro;
	}

	public String getTipoCampoPlanoContaFiltro() {
		if (tipoCampoPlanoContaFiltro == null) {
			tipoCampoPlanoContaFiltro = "";
		}
		return tipoCampoPlanoContaFiltro;
	}

	public void setTipoCampoPlanoContaFiltro(String tipoCampoPlanoContaFiltro) {
		this.tipoCampoPlanoContaFiltro = tipoCampoPlanoContaFiltro;
	}

	public String getCodigoOrigemFiltro() {
		if (codigoOrigemFiltro == null) {
			codigoOrigemFiltro = "";
		}
		return codigoOrigemFiltro;
	}

	public void setCodigoOrigemFiltro(String codigoOrigemFiltro) {
		this.codigoOrigemFiltro = codigoOrigemFiltro;
	}

	public String getNossaNumeroFiltro() {
		if (nossaNumeroFiltro == null) {
			nossaNumeroFiltro = "";
		}
		return nossaNumeroFiltro;
	}

	public void setNossaNumeroFiltro(String nossaNumeroFiltro) {
		this.nossaNumeroFiltro = nossaNumeroFiltro;
	}

	public boolean isOrigemNotaFiscalEntrada() {
		return origemNotaFiscalEntrada;
	}

	public void setOrigemNotaFiscalEntrada(boolean origemNotaFiscalEntrada) {
		this.origemNotaFiscalEntrada = origemNotaFiscalEntrada;
	}

	public boolean isOrigemContaReceber() {
		return origemContaReceber;
	}

	public void setOrigemContaReceber(boolean origemContaReceber) {
		this.origemContaReceber = origemContaReceber;
	}

	public boolean isOrigemContaPagar() {
		return origemContaPagar;
	}

	public void setOrigemContaPagar(boolean origemContaPagar) {
		this.origemContaPagar = origemContaPagar;
	}

	public boolean isOrigemMovFinanceira() {
		return origemMovFinanceira;
	}

	public void setOrigemMovFinanceira(boolean origemMovFinanceira) {
		this.origemMovFinanceira = origemMovFinanceira;
	}

	public boolean isOrigemNegociacaoContaPagar() {
		return origemNegociacaoContaPagar;
	}

	public void setOrigemNegociacaoContaPagar(boolean origemNegociacaoContaPagar) {
		this.origemNegociacaoContaPagar = origemNegociacaoContaPagar;
	}
	
	

	public boolean isOrigemMapaPendenciaCartao() {
		return origemMapaPendenciaCartao;
	}

	public void setOrigemMapaPendenciaCartao(boolean origemMapaPendenciaCartao) {
		this.origemMapaPendenciaCartao = origemMapaPendenciaCartao;
	}

	public boolean isExisteLancamentoContabilContaReceber() {
		return getListaLancamentoContabil().stream().anyMatch(p -> p.getTipoOrigemLancamentoContabilEnum().isReceber());
	}

	public boolean isExisteLancamentoContabilContaPagar() {
		return getListaLancamentoContabil().stream().anyMatch(p -> p.getTipoOrigemLancamentoContabilEnum().isPagar());
	}

	public boolean isExisteLancamentoContabilNotaFiscalEntrada() {
		return getListaLancamentoContabil().stream().anyMatch(p -> p.getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada());
	}

	public boolean isExisteLancamentoContabilMovFinanceira() {
		return getListaLancamentoContabil().stream().anyMatch(p -> p.getTipoOrigemLancamentoContabilEnum().isMovimentacaoFinanceira());
	}
	
	public boolean isExisteLancamentoContabilMapaPendenciaCartao() {
		return getListaLancamentoContabil().stream().anyMatch(p -> p.getTipoOrigemLancamentoContabilEnum().isCartaoCredito());
	}

	public boolean isExisteLancamentoContabilNegociacaoContaPagar() {
		return getListaLancamentoContabil().stream().anyMatch(p -> p.getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar());
	}
	public Integer getTotalLancamentoContabeis() {
		return getListaLancamentoContabil().size();
	}

	public Double getTotalLancamentoContabeisCredito() {
		return getListaLancamentoContabeisCredito().stream().mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getTotalLancamentoContabeisDebito() {
		return getListaLancamentoContabeisDebito().stream().mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisCreditoReceber() {
		return getListaLancamentoContabeisCredito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isReceber()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisDebitoReceber() {
		return getListaLancamentoContabeisDebito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isReceber()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisCreditoPagar() {
		return getListaLancamentoContabeisCredito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isPagar()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisDebitoPagar() {
		return getListaLancamentoContabeisDebito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isPagar()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisCreditoNotaFiscalEntrada() {
		return getListaLancamentoContabeisCredito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisDebitoNotaFiscalEntrada() {
		return getListaLancamentoContabeisDebito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisCreditoMovimentacaoFinanceira() {
		return getListaLancamentoContabeisCredito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isMovimentacaoFinanceira()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisDebitoMovimentacaoFinanceira() {
		return getListaLancamentoContabeisDebito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isMovimentacaoFinanceira()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisCreditoMapaPendenciaCartao() {
		return getListaLancamentoContabeisCredito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isCartaoCredito()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisDebitoMapaPendenciaCartao() {
		return getListaLancamentoContabeisDebito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isCartaoCredito()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisCreditoNegocicaoContaPagar() {
		return getListaLancamentoContabeisCredito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getTotalLancamentoContabeisDebitoNegocicaoContaPagar() {
		return getListaLancamentoContabeisDebito().stream().filter(p-> p.getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar()).mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	

}
