package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

/**
 * Reponsável por manter os dados da entidade ConfiguracaoFinanceiro. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO extends SuperVO {

	private Integer codigo;
	private String numeroParcela;
	private Double valorParcela;
	private Date dataEmissao;
	private Date dataVencimento;
	private Date dataRecebimento;
	private String situacao;
	private UsuarioVO responsavelPelaBaixa;
	private Double valorDescontoCalculado;
	private TipoFinanciamentoEnum tipoFinanciamentoEnum;
	private String numeroReciboTransacao;
	private String numeroCartao;
	private Integer mesValidade;
	private Integer anoValidade;
	private String codigoVerificacao;
	private String nomeCartaoCredito;
	private Boolean situacaoTransacao;
	private Date dataCobranca;
	private String chaveDaTransacao;
	private boolean configuracaoContabilExistente = false;
	private boolean lancamentoContabil = false;
	private List<LancamentoContabilVO> listaLancamentoContabeisDebito;
	private List<LancamentoContabilVO> listaLancamentoContabeisCredito;
	private UsuarioVO responsavelAjustarValorLiquido;
	private Double ajustarValorLiquido;
	private Boolean apresentarVersoCartao;
	public static final long serialVersionUID = 1L;
	/**
	 * Chave Split
	 */
	private String financialMovementKey;
	private Boolean splitRealizado;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Double getValorParcela() {
		if (valorParcela == null) {
			valorParcela = 0.0;
		}
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Integer getConverterParcelaEmNumero() {
		String numeroparcela = getNumeroParcela().contains("/") ? getNumeroParcela().substring(0, getNumeroParcela().indexOf("/")) :"";
		if (!numeroparcela.isEmpty()) {
			return Integer.parseInt(numeroparcela);
		}
		throw new StreamSeiException("Não foi popssível converte o numero a parcela " + getNumeroParcela() + " em número");
	}
	
	public String getNumeroParcela() {
		if (numeroParcela == null) {
			numeroParcela = "";
		}
		return numeroParcela;
	}

	public void setNumeroParcela(String numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getDataEmissao_Apresentar() {
		if (dataEmissao == null) {
			return "";
		}
		return (Uteis.getDataAno4Digitos(dataEmissao));
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getDataRecebimento_Apresentar() {
		if (dataRecebimento == null) {
			return "";
		}
		return (Uteis.getDataAno4Digitos(dataRecebimento));
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getDataVencimento_Apresentar() {
		if (dataVencimento == null) {
			return "";
		}
		return (Uteis.getDataAno4Digitos(dataVencimento));
	}
	
	public boolean isSituacaoRecebido(){
		return getSituacao().equals("RE");
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AR";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getSituacao_Apresentar() {
		return SituacaoContaReceber.getDescricao(situacao);
	}

	public UsuarioVO getResponsavelPelaBaixa() {
		if (responsavelPelaBaixa == null) {
			responsavelPelaBaixa = new UsuarioVO();
		}
		return responsavelPelaBaixa;
	}

	public void setResponsavelPelaBaixa(UsuarioVO responsavelPelaBaixa) {
		this.responsavelPelaBaixa = responsavelPelaBaixa;
	}
	

	public UsuarioVO getResponsavelAjustarValorLiquido() {
		if (responsavelAjustarValorLiquido == null) {
			responsavelAjustarValorLiquido = new UsuarioVO();
		}
		return responsavelAjustarValorLiquido;
	}

	public void setResponsavelAjustarValorLiquido(UsuarioVO responsavelAjustarValorLiquido) {
		this.responsavelAjustarValorLiquido = responsavelAjustarValorLiquido;
	}

	public Double getAjustarValorLiquido() {
		if (ajustarValorLiquido == null) {
			ajustarValorLiquido = 0.0;
		}
		return ajustarValorLiquido;
	}

	public void setAjustarValorLiquido(Double ajustarValorLiquido) {
		this.ajustarValorLiquido = ajustarValorLiquido;
	}

	public Double getValorDescontoCalculado() {
		if (valorDescontoCalculado == null) {
			valorDescontoCalculado = 0.0;
		}
		return valorDescontoCalculado;
	}

	public void setValorDescontoCalculado(Double valorDescontoCalculado) {
		this.valorDescontoCalculado = valorDescontoCalculado;
	}

	public TipoFinanciamentoEnum getTipoFinanciamentoEnum() {
		if (tipoFinanciamentoEnum == null) {
			tipoFinanciamentoEnum = TipoFinanciamentoEnum.INSTITUICAO;
		}
		return tipoFinanciamentoEnum;
	}

	public void setTipoFinanciamentoEnum(TipoFinanciamentoEnum tipoFinanciamentoEnum) {
		this.tipoFinanciamentoEnum = tipoFinanciamentoEnum;
	}

	public String getNumeroReciboTransacao() {
		if (numeroReciboTransacao == null) {
			numeroReciboTransacao = "";
		}
		return numeroReciboTransacao;
	}

	public void setNumeroReciboTransacao(String numeroReciboTransacao) {
		this.numeroReciboTransacao = numeroReciboTransacao;
	}

	public String getNumeroCartao() {
		if (numeroCartao == null) {
			numeroCartao = "";
		}
		return numeroCartao.trim();
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public Integer getMesValidade() {
		if (mesValidade == null) {
			mesValidade = 0;
		}
		return mesValidade;
	}

	public void setMesValidade(Integer mesValidade) {
		this.mesValidade = mesValidade;
	}

	public Integer getAnoValidade() {
		if(anoValidade == null) {
			anoValidade = 0;
		}
		return anoValidade;
	}

	public void setAnoValidade(Integer anoValidade) {
		this.anoValidade = anoValidade;
	}

	public String getCodigoVerificacao() {
		if(codigoVerificacao == null) {
			codigoVerificacao = "";
		}
		return codigoVerificacao;
	}

	public void setCodigoVerificacao(String codigoVerificacao) {
		this.codigoVerificacao = codigoVerificacao;
	}

	public String getNomeCartaoCredito() {
		if(nomeCartaoCredito == null) {
			nomeCartaoCredito = "";
		}
		return nomeCartaoCredito.toUpperCase();
	}

	public void setNomeCartaoCredito(String nomeCartaoCredito) {
		this.nomeCartaoCredito = nomeCartaoCredito;
	}

	public Boolean getSituacaoTransacao() {
		if(situacaoTransacao == null) {
			situacaoTransacao = false;
		}
		return situacaoTransacao;
	}

	public void setSituacaoTransacao(Boolean situacaoTransacao) {
		this.situacaoTransacao = situacaoTransacao;
	}

	public Date getDataCobranca() {
		if(dataCobranca == null) {
			dataCobranca = new Date();
		}
		return dataCobranca;
	}

	public void setDataCobranca(Date dataCobranca) {
		this.dataCobranca = dataCobranca;
	}

	public String getChaveDaTransacao() {
		if(chaveDaTransacao == null) {
			chaveDaTransacao = "";
		}
		return chaveDaTransacao;
	}

	public void setChaveDaTransacao(String chaveDaTransacao) {
		this.chaveDaTransacao = chaveDaTransacao;
	}
	
	public boolean isChaveDaTransacaoInformada(){
		return Uteis.isAtributoPreenchido(getChaveDaTransacao());
	}
	
	//Transient
	private Boolean validarCampoNomeCartaoCredito;
	private Boolean validarCampoNumeroCartaoCredito;
	private Boolean validarCampoMesVencimentoCartaoCredito;
	private Boolean validarCampoAnoVencimentoCartaoCredito;
	private Boolean validarCampoCVCartaoCredito;
	private Boolean apresentarDicaCVCartaoCredito;

	public Boolean getValidarCampoNomeCartaoCredito() {
		if(validarCampoNomeCartaoCredito == null) {
			validarCampoNomeCartaoCredito = false;
		}
		return validarCampoNomeCartaoCredito = !getNomeCartaoCredito().equals("");
	}

	public void setValidarCampoNomeCartaoCredito(Boolean validarCampoNomeCartaoCredito) {
		this.validarCampoNomeCartaoCredito = validarCampoNomeCartaoCredito;
	}

	public Boolean getValidarCampoNumeroCartaoCredito() {
		if(validarCampoNumeroCartaoCredito == null) {
			validarCampoNumeroCartaoCredito = false;
		}
		return validarCampoNumeroCartaoCredito;
	}

	public void setValidarCampoNumeroCartaoCredito(Boolean validarCampoNumeroCartaoCredito) {
		this.validarCampoNumeroCartaoCredito = validarCampoNumeroCartaoCredito;
	}

	public Boolean getValidarCampoMesVencimentoCartaoCredito() {
		if(validarCampoMesVencimentoCartaoCredito == null){
			validarCampoMesVencimentoCartaoCredito = false;
		}
		return validarCampoMesVencimentoCartaoCredito = !(getMesValidade().equals(0)) && (getMesValidade() < 13);
	}

	public void setValidarCampoMesVencimentoCartaoCredito(Boolean validarCampoMesVencimentoCartaoCredito) {
		this.validarCampoMesVencimentoCartaoCredito = validarCampoMesVencimentoCartaoCredito;
	}

	public Boolean getValidarCampoAnoVencimentoCartaoCredito() {
		if(validarCampoAnoVencimentoCartaoCredito == null) {
			validarCampoAnoVencimentoCartaoCredito = false;
		}
		return validarCampoAnoVencimentoCartaoCredito = !getAnoValidade().equals(0);
	}

	public void setValidarCampoAnoVencimentoCartaoCredito(Boolean validarCampoAnoVencimentoCartaoCredito) {
		this.validarCampoAnoVencimentoCartaoCredito = validarCampoAnoVencimentoCartaoCredito;
	}

	public Boolean getValidarCampoCVCartaoCredito() {
		if(validarCampoCVCartaoCredito == null) {
			validarCampoCVCartaoCredito = false;
		}
		return validarCampoCVCartaoCredito = getCodigoVerificacao() != "";
	}

	public void setValidarCampoCVCartaoCredito(Boolean validarCampoCVCartaoCredito) {
		this.validarCampoCVCartaoCredito = validarCampoCVCartaoCredito;
	}

	public Boolean getApresentarDicaCVCartaoCredito() {
		if(apresentarDicaCVCartaoCredito == null) {
			apresentarDicaCVCartaoCredito = false;
		}
		return apresentarDicaCVCartaoCredito;
	}

	public void setApresentarDicaCVCartaoCredito(Boolean apresentarDicaCVCartaoCredito) {
		this.apresentarDicaCVCartaoCredito = apresentarDicaCVCartaoCredito;
	}

	public String getFinancialMovementKey() {
		if(financialMovementKey == null) {
			financialMovementKey = "";
		}
		return financialMovementKey;
	}

	public void setFinancialMovementKey(String financialMovementKey) {
		this.financialMovementKey = financialMovementKey;
	}

	@Override
	public String toString() {
		return "FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO [codigo=" + codigo + ", numeroParcela=" + numeroParcela + ", valorParcela=" + valorParcela + ", dataEmissao=" + dataEmissao + ", dataVencimento=" + dataVencimento + ", dataRecebimento=" + dataRecebimento + ", situacao=" + situacao + ", responsavelPelaBaixa=" + responsavelPelaBaixa + ", valorDescontoCalculado=" + valorDescontoCalculado + ", tipoFinanciamentoEnum=" + tipoFinanciamentoEnum + ", numeroReciboTransacao=" + numeroReciboTransacao + ", numeroCartao=" + numeroCartao + ", mesValidade=" + mesValidade + ", anoValidade=" + anoValidade + ", codigoVerificacao=" + codigoVerificacao + ", nomeCartaoCredito=" + nomeCartaoCredito + ", situacaoTransacao=" + situacaoTransacao + ", dataCobranca=" + dataCobranca + ", chaveDaTransacao=" + chaveDaTransacao + ", financialMovementKey=" + financialMovementKey + ", validarCampoNomeCartaoCredito=" + validarCampoNomeCartaoCredito + ", validarCampoNumeroCartaoCredito="
				+ validarCampoNumeroCartaoCredito + ", validarCampoMesVencimentoCartaoCredito=" + validarCampoMesVencimentoCartaoCredito + ", validarCampoAnoVencimentoCartaoCredito=" + validarCampoAnoVencimentoCartaoCredito + ", validarCampoCVCartaoCredito=" + validarCampoCVCartaoCredito + ", apresentarDicaCVCartaoCredito=" + apresentarDicaCVCartaoCredito + "]";
	}
	
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;

	public ConfiguracaoFinanceiroCartaoVO getConfiguracaoFinanceiroCartaoVO() {
		if(configuracaoFinanceiroCartaoVO == null) {
			configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
		}
		return configuracaoFinanceiroCartaoVO;
	}

	public void setConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) {
		this.configuracaoFinanceiroCartaoVO = configuracaoFinanceiroCartaoVO;
	}

	public Boolean getSplitRealizado() {
		if(splitRealizado == null) {
			splitRealizado = false;
		}
		return splitRealizado;
	}

	public void setSplitRealizado(Boolean splitRealizado) {
		this.splitRealizado = splitRealizado;
	}
	
	public List<SelectItem> listaSelectItemMesValidade;
	public List<SelectItem> listaSelectItemAnoValidade;
	
	public List<SelectItem> getListaSelectItemMesValidade() {
		if(listaSelectItemMesValidade == null) {
			listaSelectItemMesValidade = new ArrayList<SelectItem>();
			for (Integer i = 1; i < 13; i++) {
				listaSelectItemMesValidade.add(new SelectItem(i.toString(), i.toString()));
			}
		}
		return listaSelectItemMesValidade;
	}

	public void setListaSelectItemMesValidade(List<SelectItem> listaSelectItemMesValidade) {
		this.listaSelectItemMesValidade = listaSelectItemMesValidade;
	}

	public List<SelectItem> getListaSelectItemAnoValidade() {
		if(listaSelectItemAnoValidade == null) {
			listaSelectItemAnoValidade = new ArrayList<SelectItem>();
			Integer anoAtual = Integer.parseInt(Uteis.getAnoDataAtual());
			for (int i = 0; i < 11; i++) {
				listaSelectItemAnoValidade.add(new SelectItem(anoAtual.toString(), anoAtual.toString()));
				anoAtual++;
			}
		}
		return listaSelectItemAnoValidade;
	}

	public void setListaSelectItemAnoValidade(List<SelectItem> listaSelectItemAnoValidade) {
		this.listaSelectItemAnoValidade = listaSelectItemAnoValidade;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 5.0.4.0 07/04/2016
	 */
	//transient
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;
	public FormaPagamentoNegociacaoRecebimentoVO getFormaPagamentoNegociacaoRecebimentoVO() {
		if(formaPagamentoNegociacaoRecebimentoVO == null)
			formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVO(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) {
		this.formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO;
	}
	
	private String contaReceberRecebimento;
	public String getContaReceberRecebimento() {
		if (contaReceberRecebimento == null) {
			contaReceberRecebimento = "";
		}
		return contaReceberRecebimento;
	}

	public void setContaReceberRecebimento(String contaReceberRecebimento) {
		this.contaReceberRecebimento = contaReceberRecebimento;
	}
	
	
	private ContaReceberVO contaReceberVO;
	private ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO;
	
	public ContaReceberVO getContaReceberVO() {
		if(contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public ContaReceberNegociacaoRecebimentoVO getContaReceberNegociacaoRecebimentoVO() {
		if(contaReceberNegociacaoRecebimentoVO == null) {
			contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
		}
		return contaReceberNegociacaoRecebimentoVO;
	}

	public void setContaReceberNegociacaoRecebimentoVO(
			ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO) {
		this.contaReceberNegociacaoRecebimentoVO = contaReceberNegociacaoRecebimentoVO;
	}
	
	public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO clone() throws CloneNotSupportedException {
		FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO clone = (FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO) super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);
		clone.setFormaPagamentoNegociacaoRecebimentoVO(new FormaPagamentoNegociacaoRecebimentoVO());
		clone.setFormaPagamentoNegociacaoRecebimentoVO(this.getFormaPagamentoNegociacaoRecebimentoVO());
		return clone;
	}
	//Transient
	private String mascaraNumeroCartao;
	
	public String getMascaraNumeroCartao() {
		if (mascaraNumeroCartao == null) {
			mascaraNumeroCartao = "";
			if(!getNumeroCartao().isEmpty()) {
				mascaraNumeroCartao = "XXXX.XXXX.XXXX." + getNumeroCartao().substring(getNumeroCartao().length() - 4);
			}
		}
		return mascaraNumeroCartao;
	}

	public void setMascaraNumeroCartao(String mascaraNumeroCartao) {
		this.mascaraNumeroCartao = mascaraNumeroCartao;
	}
	
	public String getDadosCartaoApresentar(){
		return !getNomeCartaoCredito().isEmpty() && !getNumeroCartao().isEmpty() ? getNumeroCartao() +" - "+ getNomeCartaoCredito() + " - "+ getNumeroReciboTransacao() :
			   !getNomeCartaoCredito().isEmpty() && getNumeroCartao().isEmpty()  ? getNomeCartaoCredito() +" - "+ getNumeroReciboTransacao() :
			    getNomeCartaoCredito().isEmpty() && !getNumeroCartao().isEmpty() ? getNumeroCartao() +" - "+ getNumeroReciboTransacao() :
			   !getNumeroReciboTransacao().isEmpty() ? getNumeroReciboTransacao() : "";
	}
	
	public String getDadosCartaoApresentarTeste(){
		if (!getNomeCartaoCredito().isEmpty() && !getNumeroCartao().isEmpty()) {
			return "Nu Car - No Car - Nu Re Trans";
		} else if (!getNomeCartaoCredito().isEmpty() && getNumeroCartao().isEmpty()) {
			return "No Car - Nu Re Trans";
		} else if (getNomeCartaoCredito().isEmpty() && !getNumeroCartao().isEmpty()) {
			return "Nu Car - Nu Re Trans";
		} else if (!getNumeroReciboTransacao().isEmpty()) {
			return "Nu Re Trans"; 
		} return "";
	}
	
	public String getDadosValorParcelaApresentar(){
		return "Parc. " + getNumeroParcela() + " R$ " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(getValorParcela(), ",");

	}
	
	public List<LancamentoContabilVO> getListaLancamentoContabeisDebito() {
		if (listaLancamentoContabeisDebito == null) {
			listaLancamentoContabeisDebito = new ArrayList<LancamentoContabilVO>();
		}
		return listaLancamentoContabeisDebito;
	}

	public void setListaLancamentoContabeisDebito(List<LancamentoContabilVO> listaLancamentoContabeisDebito) {
		this.listaLancamentoContabeisDebito = listaLancamentoContabeisDebito;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisCredito() {
		if (listaLancamentoContabeisCredito == null) {
			listaLancamentoContabeisCredito = new ArrayList<LancamentoContabilVO>();
		}
		return listaLancamentoContabeisCredito;
	}

	public void setListaLancamentoContabeisCredito(List<LancamentoContabilVO> listaLancamentoContabeisCredito) {
		this.listaLancamentoContabeisCredito = listaLancamentoContabeisCredito;
	}
	
	public Double getTotalLancamentoContabeisCredito() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisCredito()) {
			valor = valor + lc.getValor();
		}
		return valor;
	}
	
	public Double getTotalLancamentoContabeisDebito() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisDebito()) {
			valor = valor + lc.getValor();
		}
		return valor;
	}
	
	

	public boolean isConfiguracaoContabilExistente() {
		return configuracaoContabilExistente;
	}

	public void setConfiguracaoContabilExistente(boolean configuracaoContabilExistente) {
		this.configuracaoContabilExistente = configuracaoContabilExistente;
	}

	public boolean isLancamentoContabil() {
		return lancamentoContabil;
	}

	public void setLancamentoContabil(boolean lancamentoContabil) {
		this.lancamentoContabil = lancamentoContabil;
	}

	public String getNumeroCartao_Apresentar() {
		if (getNumeroCartao().equals("")) {
			numeroCartao = "**** **** **** ****";
		}
		if (getNumeroCartao().length() == 1) {
			return getNumeroCartao() + "*** **** **** ****";
		}
		if (getNumeroCartao().length() == 2) {
			return getNumeroCartao() + "** **** **** ****";
		}
		if (getNumeroCartao().length() == 3) {
			return getNumeroCartao() + "* **** **** ****";
		}
		if (getNumeroCartao().length() == 4) {
			return getNumeroCartao() + " **** **** ****";
		}
		if (getNumeroCartao().length() == 5) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4) + "*** **** ****";
		}
		if (getNumeroCartao().length() == 6) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 6) + "** **** ****";
		}
		if (getNumeroCartao().length() == 7) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 7) + "* **** ****";
		}
		if (getNumeroCartao().length() == 8) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " **** ****";
		}
		if (getNumeroCartao().length() == 9) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8) + "*** ****";
		}
		if (getNumeroCartao().length() == 10) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8, 10) + "** ****";
		}
		if (getNumeroCartao().length() == 11) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8, 11) + "* ****";
		}
		if (getNumeroCartao().length() == 12) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8, 12) + " ****";
		}
		if (getNumeroCartao().length() == 13) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8, 12) + " " + getNumeroCartao().substring(12) + "***";
		}
		if (getNumeroCartao().length() == 14) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8, 12) + " " + getNumeroCartao().substring(12, 14) + "**";
		}
		if (getNumeroCartao().length() == 15) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8, 12) + " " + getNumeroCartao().substring(12, 15) + "*";
		}
		if (getNumeroCartao().length() == 16) {
			return getNumeroCartao().substring(0, 4) + " " + getNumeroCartao().substring(4, 8) + " " + getNumeroCartao().substring(8, 12) + " " + getNumeroCartao().substring(12, 16);
		}
		return getNumeroCartao();
	}
	
	public String getMesValidade_Apresentar() {
		if (getMesValidade().equals(0)) {
			return "MM";
		}
		return getMesValidade().toString().length() > 1 ?  getMesValidade().toString() : "0" + getMesValidade().toString();
	}
	
	public String getAnoValidade_Apresentar() {
		if(getAnoValidade().equals(0)) {
			return "AA";
		}
		return getAnoValidade().toString();
	}
	
	public String getMesAnoValidade_Apresentar() {
		return getMesValidade_Apresentar() + "/" + getAnoValidade_Apresentar();
	}
	
	public String getNomeCartaoCredito_Apresentar() {
		if(getNomeCartaoCredito().equals("")) {
			nomeCartaoCredito = "NOME E SOBRENOME";
		}
		return getNomeCartaoCredito().toUpperCase();
	}
	
	public Boolean getApresentarVersoCartao() {
		if (apresentarVersoCartao == null) {
			apresentarVersoCartao = false;
		}
		return apresentarVersoCartao;
	}

	public void setApresentarVersoCartao(Boolean apresentarVersoCartao) {
		this.apresentarVersoCartao = apresentarVersoCartao;
	}
	
}
