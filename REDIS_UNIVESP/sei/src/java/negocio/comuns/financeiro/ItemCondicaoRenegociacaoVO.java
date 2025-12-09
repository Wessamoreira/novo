package negocio.comuns.financeiro;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoParcelaNegociarEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.CondicaoRenegociacao;

/**
 * Reponsável por manter os dados da entidade ItemCondicaoRenegociacao. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see CondicaoRenegociacao
*/

public class ItemCondicaoRenegociacaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1255712479691212522L;
	private Integer codigo;
	private String descricao;
	private Double valorInicial;
	private Double valorFinal;
	private Integer parcelaInicial;
	private Integer parcelaFinal;
	private Double juro;
	private Double desconto;
	private String status;
	// Transiente
	private String statusAnterior;
	private CondicaoRenegociacaoVO condicaoRenegociacao;
	private TipoFinanciamentoEnum tipoFinanciamentoEnum;
	private Boolean tipoOrigemMatricula;
	private Boolean tipoOrigemMensalidade;
	private Boolean tipoOrigemBiblioteca;
	private Boolean tipoOrigemDevolucaoCheque;
	private Boolean tipoOrigemNegociacao;
	private Boolean tipoOrigemBolsaCusteadaConvenio;
	private Boolean tipoOrigemContratoReceita;
	private Boolean tipoOrigemOutros;
	private Boolean tipoOrigemInclusaoReposicao;
	private TipoParcelaNegociarEnum tipoParcelaNegociar;
	private BigDecimal valorMinimoPorParcela;
	private Integer qtdeInicialDiasAtraso;
	private Integer qtdeFinalDiasAtraso;
	private Integer qtdeDiasEntrada;
	private Boolean gerarParcelas30DiasAposDataEntrada;
	
	private Boolean utilizarVisaoAdministrativa;
    private Boolean utilizarVisaoAluno;
    
    private Double faixaEntradaInicial;
    private Double faixaEntradaFinal;
    private Boolean definirNumeroDiasVencimentoPrimeiraParcela;
    private Integer numeroDiasAposVencimentoEntrada;
    private Boolean isentarJuroParcela;
    private Boolean isentarMultaParcela;
    private Boolean isentarIndiceReajustePorAtrasoParcela;
    
    //Transiente
    private List<OpcaoAlunoCondicaoRenegociacaoVO> opcaoAlunoCondicaoRenegociacaoVOs;


	/**
	 * Construtor padrão da classe <code>ItemCondicaoRenegociacao</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ItemCondicaoRenegociacaoVO() {
		super();
	}

	public CondicaoRenegociacaoVO getCondicaoRenegociacao() {
		if (condicaoRenegociacao == null) {
			condicaoRenegociacao = new CondicaoRenegociacaoVO();
		}
		return (condicaoRenegociacao);
	}

	public void setCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacao) {
		this.condicaoRenegociacao = condicaoRenegociacao;
	}

	public String getStatus() {
		if (status == null) {
			status = "EM";
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return (desconto);
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return (juro);
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Integer getParcelaFinal() {
		if (parcelaFinal == null) {
			parcelaFinal = 0;
		}
		return (parcelaFinal);
	}

	public void setParcelaFinal(Integer parcelaFinal) {
		this.parcelaFinal = parcelaFinal;
	}

	public Integer getParcelaInicial() {
		if (parcelaInicial == null) {
			parcelaInicial = 0;
		}
		return (parcelaInicial);
	}

	public void setParcelaInicial(Integer parcelaInicial) {
		this.parcelaInicial = parcelaInicial;
	}

	public Double getValorFinal() {
		if (valorFinal == null) {
			valorFinal = 0.0;
		}
		return (valorFinal);
	}

	public void setValorFinal(Double valorFinal) {
		this.valorFinal = valorFinal;
	}

	public Double getValorInicial() {
		if (valorInicial == null) {
			valorInicial = 0.0;
		}
		return (valorInicial);
	}

	public void setValorInicial(Double valorInicial) {
		this.valorInicial = valorInicial;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public boolean getIsAtivo() {
		return getStatus().equals("AT");
	}

	public boolean getIsInativo() {
		return getStatus().equals("IN");
	
	}
	public boolean getIsEmConstrucao() {
		return getStatus().equals("EM");
	}

	public String getStatusAnterior() {
		if (statusAnterior == null) {
			statusAnterior = getStatus();
		}
		return statusAnterior;
	}

	public void setStatusAnterior(String statusAnterior) {
		this.statusAnterior = statusAnterior;
	}

	public TipoFinanciamentoEnum getTipoFinanciamentoEnum() {
		if (tipoFinanciamentoEnum == null) {
			tipoFinanciamentoEnum = TipoFinanciamentoEnum.OPERADORA;
		}
		return tipoFinanciamentoEnum;
	}

	public void setTipoFinanciamentoEnum(TipoFinanciamentoEnum tipoFinanciamentoEnum) {
		this.tipoFinanciamentoEnum = tipoFinanciamentoEnum;
	}

	public Boolean getTipoOrigemMatricula() {
		if (tipoOrigemMatricula == null) {
			tipoOrigemMatricula = true;
		}
		return tipoOrigemMatricula;
	}

	public void setTipoOrigemMatricula(Boolean tipoOrigemMatricula) {
		this.tipoOrigemMatricula = tipoOrigemMatricula;
	}

	public Boolean getTipoOrigemMensalidade() {
		if (tipoOrigemMensalidade == null) {
			tipoOrigemMensalidade = true;
		}
		return tipoOrigemMensalidade;
	}

	public void setTipoOrigemMensalidade(Boolean tipoOrigemMensalidade) {
		this.tipoOrigemMensalidade = tipoOrigemMensalidade;
	}

	public Boolean getTipoOrigemBiblioteca() {
		if (tipoOrigemBiblioteca == null) {
			tipoOrigemBiblioteca = true;
		}
		return tipoOrigemBiblioteca;
	}

	public void setTipoOrigemBiblioteca(Boolean tipoOrigemBiblioteca) {
		this.tipoOrigemBiblioteca = tipoOrigemBiblioteca;
	}

	public Boolean getTipoOrigemDevolucaoCheque() {
		if (tipoOrigemDevolucaoCheque == null) {
			tipoOrigemDevolucaoCheque = true;
		}
		return tipoOrigemDevolucaoCheque;
	}

	public void setTipoOrigemDevolucaoCheque(Boolean tipoOrigemDevolucaoCheque) {
		this.tipoOrigemDevolucaoCheque = tipoOrigemDevolucaoCheque;
	}

	public Boolean getTipoOrigemNegociacao() {
		if (tipoOrigemNegociacao == null) {
			tipoOrigemNegociacao = true;
		}
		return tipoOrigemNegociacao;
	}

	public void setTipoOrigemNegociacao(Boolean tipoOrigemNegociacao) {
		this.tipoOrigemNegociacao = tipoOrigemNegociacao;
	}

	public Boolean getTipoOrigemBolsaCusteadaConvenio() {
		if (tipoOrigemBolsaCusteadaConvenio == null) {
			tipoOrigemBolsaCusteadaConvenio = true;
		}
		return tipoOrigemBolsaCusteadaConvenio;
	}

	public void setTipoOrigemBolsaCusteadaConvenio(Boolean tipoOrigemBolsaCusteadaConvenio) {
		this.tipoOrigemBolsaCusteadaConvenio = tipoOrigemBolsaCusteadaConvenio;
	}

	public Boolean getTipoOrigemContratoReceita() {
		if (tipoOrigemContratoReceita == null) {
			tipoOrigemContratoReceita = true;
		}
		return tipoOrigemContratoReceita;
	}

	public void setTipoOrigemContratoReceita(Boolean tipoOrigemContratoReceita) {
		this.tipoOrigemContratoReceita = tipoOrigemContratoReceita;
	}

	public Boolean getTipoOrigemOutros() {
		if (tipoOrigemOutros == null) {
			tipoOrigemOutros = true;
		}
		return tipoOrigemOutros;
	}

	public void setTipoOrigemOutros(Boolean tipoOrigemOutros) {
		this.tipoOrigemOutros = tipoOrigemOutros;
	}

	public Boolean getTipoOrigemInclusaoReposicao() {
		if (tipoOrigemInclusaoReposicao == null) {
			tipoOrigemInclusaoReposicao = true;
		}
		return tipoOrigemInclusaoReposicao;
	}

	public void setTipoOrigemInclusaoReposicao(Boolean tipoOrigemInclusaoReposicao) {
		this.tipoOrigemInclusaoReposicao = tipoOrigemInclusaoReposicao;
	}

	public TipoParcelaNegociarEnum getTipoParcelaNegociar() {
		if (tipoParcelaNegociar == null) {
			tipoParcelaNegociar = TipoParcelaNegociarEnum.VENCIDAS;
		}
		return tipoParcelaNegociar;
	}

	public void setTipoParcelaNegociar(TipoParcelaNegociarEnum tipoParcelaNegociar) {
		this.tipoParcelaNegociar = tipoParcelaNegociar;
	}

	public BigDecimal getValorMinimoPorParcela() {
		if (valorMinimoPorParcela == null) {
			valorMinimoPorParcela = BigDecimal.ZERO;
		}
		return valorMinimoPorParcela;
	}

	public void setValorMinimoPorParcela(BigDecimal valorMinimoPorParcela) {
		this.valorMinimoPorParcela = valorMinimoPorParcela;
	}

	public Integer getQtdeInicialDiasAtraso() {
		if (qtdeInicialDiasAtraso == null) {
			qtdeInicialDiasAtraso = 0;
		}
		return qtdeInicialDiasAtraso;
	}

	public void setQtdeInicialDiasAtraso(Integer qtdeInicialDiasAtraso) {
		this.qtdeInicialDiasAtraso = qtdeInicialDiasAtraso;
	}

	public Integer getQtdeFinalDiasAtraso() {
		if (qtdeFinalDiasAtraso == null) {
			qtdeFinalDiasAtraso = 0;
		}
		return qtdeFinalDiasAtraso;
	}

	public void setQtdeFinalDiasAtraso(Integer qtdeFinalDiasAtraso) {
		this.qtdeFinalDiasAtraso = qtdeFinalDiasAtraso;
	}
	

	public Boolean getUtilizarVisaoAdministrativa() {
		if (utilizarVisaoAdministrativa == null) {
			utilizarVisaoAdministrativa = true;
		}
		return utilizarVisaoAdministrativa;
	}

	public void setUtilizarVisaoAdministrativa(Boolean utilizarVisaoAdministrativa) {
		this.utilizarVisaoAdministrativa = utilizarVisaoAdministrativa;
	}

	public Boolean getUtilizarVisaoAluno() {
		if (utilizarVisaoAluno == null) {
			utilizarVisaoAluno = true;
		}
		return utilizarVisaoAluno;
	}

	public void setUtilizarVisaoAluno(Boolean utilizarVisaoAluno) {
		this.utilizarVisaoAluno = utilizarVisaoAluno;
	}

	public Integer getQtdeDiasEntrada() {
		if(qtdeDiasEntrada == null){
			qtdeDiasEntrada = 0;
		}
		return qtdeDiasEntrada;
	}

	public void setQtdeDiasEntrada(Integer qtdeDiasEntrada) {
		this.qtdeDiasEntrada = qtdeDiasEntrada;
	}

	public Boolean getGerarParcelas30DiasAposDataEntrada() {
		if(gerarParcelas30DiasAposDataEntrada == null){
			gerarParcelas30DiasAposDataEntrada = true;
		}
		return gerarParcelas30DiasAposDataEntrada;
	}

	public void setGerarParcelas30DiasAposDataEntrada(Boolean gerarParcelas30DiasAposDataEntrada) {
		this.gerarParcelas30DiasAposDataEntrada = gerarParcelas30DiasAposDataEntrada;
	}   
	
	public Double getFaixaEntradaInicial() {
		if (faixaEntradaInicial == null) {
			faixaEntradaInicial = 0.0;
		}
		return faixaEntradaInicial;
	}

	public void setFaixaEntradaInicial(Double faixaEntradaInicial) {
		this.faixaEntradaInicial = faixaEntradaInicial;
	}

	public Double getFaixaEntradaFinal() {
		if (faixaEntradaFinal == null) {
			faixaEntradaFinal = 0.0;
		}
		return faixaEntradaFinal;
	}

	public void setFaixaEntradaFinal(Double faixaEntradaFinal) {
		this.faixaEntradaFinal = faixaEntradaFinal;
	}

	public Boolean getDefinirNumeroDiasVencimentoPrimeiraParcela() {
		if (definirNumeroDiasVencimentoPrimeiraParcela == null) {
			definirNumeroDiasVencimentoPrimeiraParcela = Boolean.FALSE;
		}
		return definirNumeroDiasVencimentoPrimeiraParcela;
	}

	public void setDefinirNumeroDiasVencimentoPrimeiraParcela(Boolean definirNumeroDiasVencimentoPrimeiraParcela) {
		this.definirNumeroDiasVencimentoPrimeiraParcela = definirNumeroDiasVencimentoPrimeiraParcela;
	}

	public Integer getNumeroDiasAposVencimentoEntrada() {
		if (numeroDiasAposVencimentoEntrada == null) {
			numeroDiasAposVencimentoEntrada = 30;
		}
		return numeroDiasAposVencimentoEntrada;
	}

	public void setNumeroDiasAposVencimentoEntrada(Integer numeroDiasAposVencimentoEntrada) {
		this.numeroDiasAposVencimentoEntrada = numeroDiasAposVencimentoEntrada;
	}

	public Boolean getIsentarJuroParcela() {
		if (isentarJuroParcela == null) {
			isentarJuroParcela = Boolean.FALSE;
		}
		return isentarJuroParcela;
	}

	public void setIsentarJuroParcela(Boolean isentarJuroParcela) {
		this.isentarJuroParcela = isentarJuroParcela;
	}

	public Boolean getIsentarMultaParcela() {
		if (isentarMultaParcela == null) {
			isentarMultaParcela = Boolean.FALSE;
		}
		return isentarMultaParcela;
	}

	public void setIsentarMultaParcela(Boolean isentarMultaParcela) {
		this.isentarMultaParcela = isentarMultaParcela;
	}

	public Boolean getIsentarIndiceReajustePorAtrasoParcela() {
		if (isentarIndiceReajustePorAtrasoParcela == null) {
			isentarIndiceReajustePorAtrasoParcela = Boolean.FALSE;
		}
		return isentarIndiceReajustePorAtrasoParcela;
	}

	public void setIsentarIndiceReajustePorAtrasoParcela(Boolean isentarIndiceReajustePorAtrasoParcela) {
		this.isentarIndiceReajustePorAtrasoParcela = isentarIndiceReajustePorAtrasoParcela;
	}

	public List<OpcaoAlunoCondicaoRenegociacaoVO> getOpcaoAlunoCondicaoRenegociacaoVOs() {
		if (opcaoAlunoCondicaoRenegociacaoVOs == null) {
			opcaoAlunoCondicaoRenegociacaoVOs = new ArrayList();
		}
		return opcaoAlunoCondicaoRenegociacaoVOs;
	}

	public void setOpcaoAlunoCondicaoRenegociacaoVOs(List<OpcaoAlunoCondicaoRenegociacaoVO> opcaoAlunoCondicaoRenegociacaoVOs) {
		this.opcaoAlunoCondicaoRenegociacaoVOs = opcaoAlunoCondicaoRenegociacaoVOs;
	}

	public String getDescricao() {
		if(descricao == null || descricao.isEmpty()) {
			descricao = (getFaixaEntradaFinal() > 0.0 ? "Entrada entre "+Uteis.getDoubleFormatado(getFaixaEntradaInicial())+" e "+Uteis.getDoubleFormatado(getFaixaEntradaFinal())+" " : "")+(getParcelaFinal() > 0 ? "Parcelamento entre "+getParcelaInicial()+" e "+getParcelaFinal() : "");
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	
	
	
}