package webservice.boletoonline.bancoBrasil.comuns;

public class BancoBrasilBoletoOnlineVO {	
	
	
	
	private String numeroConvenio;
    private String dataVencimento;
    private String valorOriginal;
    private String numeroCarteira;
    private Integer numeroVariacaoCarteira;
    private Integer codigoModalidade;
    private String dataEmissao;
    private String valorAbatimento;
    private Integer quantidadeDiasProtesto;
    private Integer quantidadeDiasNegativacao;
    private String orgaoNegativador;
    private String indicadorAceiteTituloVencido;
    private Integer numeroDiasLimiteRecebimento;
    private String codigoAceite;
    private String codigoTipoTitulo;
    private String descricaoTipoTitulo;
    private String indicadorPermissaoRecebimentoParcial;
    private String numeroTituloBeneficiario;
    private String campoUtilizacaoBeneficiario;
    private String numeroTituloCliente;
    private String mensagemBloquetoOcorrencia;	
    private DescontoVO desconto;
    private DescontoVO segundoDesconto;
    private DescontoVO terceiroDesconto;
    private JurosVO jurosMora;
	private MultaVO multa;
    private PagadorVO pagador;
	private BeneficiarioVO beneficiarioFinal;
	private String indicadorPix;
	
	
	 public String getNumeroConvenio() {
		 if(numeroConvenio == null) {
			 numeroConvenio ="";
		 }
		return numeroConvenio;
	}
	public void setNumeroConvenio(String numeroConvenio) {
		this.numeroConvenio = numeroConvenio;
	}
	public String getDataVencimento() {
		if(dataVencimento == null) {
			dataVencimento ="";
		}
		return dataVencimento;
	}
	
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public String getValorOriginal() {
		if(valorOriginal ==null) {
			valorOriginal ="";
		}
		return valorOriginal;
	}
	public void setValorOriginal(String valorOriginal) {
		this.valorOriginal = valorOriginal;
	}
	public String getNumeroCarteira() {
         if(numeroCarteira ==null) {
        	 numeroCarteira ="";
         }
		return numeroCarteira;
	}
	public void setNumeroCarteira(String numeroCarteira) {
		this.numeroCarteira = numeroCarteira;
	}
	public Integer getNumeroVariacaoCarteira() {
		if(numeroVariacaoCarteira ==null) {
			numeroVariacaoCarteira =0 ;
		}
		return numeroVariacaoCarteira;
	}
	public void setNumeroVariacaoCarteira(Integer numeroVariacaoCarteira) {
		this.numeroVariacaoCarteira = numeroVariacaoCarteira;
	}
	public Integer getCodigoModalidade() {
		if(codigoModalidade ==null) {
			codigoModalidade =0;
		}
		return codigoModalidade;
	}
	public void setCodigoModalidade(Integer codigoModalidade) {
		this.codigoModalidade = codigoModalidade;
	}
	public String getDataEmissao() {
		if(dataEmissao == null) {
			dataEmissao ="";
		}
		return dataEmissao;
	}
	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	public String getValorAbatimento() {
		if(valorAbatimento == null) {
			valorAbatimento ="";
		}
		return valorAbatimento;
	}
	public void setValorAbatimento(String valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}
	public Integer getQuantidadeDiasProtesto() {
		if(quantidadeDiasProtesto == null) {
			quantidadeDiasProtesto =0;
		}
		return quantidadeDiasProtesto;
	}
	public void setQuantidadeDiasProtesto(Integer quantidadeDiasProtesto) {
		this.quantidadeDiasProtesto = quantidadeDiasProtesto;
	}
	public Integer getQuantidadeDiasNegativacao() {
		if(quantidadeDiasNegativacao == null) {
			quantidadeDiasNegativacao =0;
		}
		return quantidadeDiasNegativacao;
	}
	public void setQuantidadeDiasNegativacao(Integer quantidadeDiasNegativacao) {
		this.quantidadeDiasNegativacao = quantidadeDiasNegativacao;
	}
	public String getOrgaoNegativador() {
		if(orgaoNegativador == null) {
			orgaoNegativador ="";
		}
		return orgaoNegativador;
	}
	public void setOrgaoNegativador(String orgaoNegativador) {
		this.orgaoNegativador = orgaoNegativador;
	}
	public String getIndicadorAceiteTituloVencido() {
		if(indicadorAceiteTituloVencido == null) {
			indicadorAceiteTituloVencido ="";
		}
		return indicadorAceiteTituloVencido;
	}
	public void setIndicadorAceiteTituloVencido(String indicadorAceiteTituloVencido) {
		this.indicadorAceiteTituloVencido = indicadorAceiteTituloVencido;
	}
	public Integer getNumeroDiasLimiteRecebimento() {
		if(numeroDiasLimiteRecebimento == null) {
			numeroDiasLimiteRecebimento =0;
		}
		return numeroDiasLimiteRecebimento;
	}
	public void setNumeroDiasLimiteRecebimento(Integer numeroDiasLimiteRecebimento) {
		this.numeroDiasLimiteRecebimento = numeroDiasLimiteRecebimento;
	}
	public String getCodigoAceite() {
		if(codigoAceite == null) {
			codigoAceite ="";
		}
		return codigoAceite;
	}
	public void setCodigoAceite(String codigoAceite) {
		this.codigoAceite = codigoAceite;
	}
	public String getCodigoTipoTitulo() {
		if(codigoTipoTitulo == null) {
			codigoTipoTitulo ="";
		}
		return codigoTipoTitulo;
	}
	public void setCodigoTipoTitulo(String codigoTipoTitulo) {
		this.codigoTipoTitulo = codigoTipoTitulo;
	}
	public String getDescricaoTipoTitulo() {
		if(descricaoTipoTitulo == null) {
			descricaoTipoTitulo ="";
		}
		return descricaoTipoTitulo;
	}
	public void setDescricaoTipoTitulo(String descricaoTipoTitulo) {
		this.descricaoTipoTitulo = descricaoTipoTitulo;
	}
	public String getIndicadorPermissaoRecebimentoParcial() {
		if(indicadorPermissaoRecebimentoParcial == null) {
			indicadorPermissaoRecebimentoParcial ="";
		}
		return indicadorPermissaoRecebimentoParcial;
	}
	public void setIndicadorPermissaoRecebimentoParcial(String indicadorPermissaoRecebimentoParcial) {
		this.indicadorPermissaoRecebimentoParcial = indicadorPermissaoRecebimentoParcial;
	}
	public String getNumeroTituloBeneficiario() {
		if(numeroTituloBeneficiario == null) {
			numeroTituloBeneficiario ="";
		}
		return numeroTituloBeneficiario;
	}
	public void setNumeroTituloBeneficiario(String numeroTituloBeneficiario) {
		this.numeroTituloBeneficiario = numeroTituloBeneficiario;
	}
	public String getCampoUtilizacaoBeneficiario() {
		if(campoUtilizacaoBeneficiario == null) {
			campoUtilizacaoBeneficiario ="";
		}
		return campoUtilizacaoBeneficiario;
	}
	public void setCampoUtilizacaoBeneficiario(String campoUtilizacaoBeneficiario) {
		this.campoUtilizacaoBeneficiario = campoUtilizacaoBeneficiario;
	}
	public String getNumeroTituloCliente() {
		if(numeroTituloCliente == null) {
			numeroTituloCliente ="";
		}
		return numeroTituloCliente;
	}
	public void setNumeroTituloCliente(String numeroTituloCliente) {
		this.numeroTituloCliente = numeroTituloCliente;
	}
	public String getMensagemBloquetoOcorrencia() {
		if(mensagemBloquetoOcorrencia == null) {
			mensagemBloquetoOcorrencia ="";
		}
		return mensagemBloquetoOcorrencia;
	}
	public void setMensagemBloquetoOcorrencia(String mensagemBloquetoOcorrencia) {
		this.mensagemBloquetoOcorrencia = mensagemBloquetoOcorrencia;
	}
	public DescontoVO getDesconto() {
		if(desconto == null) {
			desconto = new DescontoVO();
		}
		return desconto;
	}
	public void setDesconto(DescontoVO desconto) {
		this.desconto = desconto;
	}
	public DescontoVO getSegundoDesconto() {
		if(segundoDesconto == null) {
			segundoDesconto = new DescontoVO();
		}
		return segundoDesconto;
	}
	public void setSegundoDesconto(DescontoVO segundoDesconto) {
		this.segundoDesconto = segundoDesconto;
	}
	public DescontoVO getTerceiroDesconto() {
		if(terceiroDesconto == null) {
			terceiroDesconto = new DescontoVO();
		}
		return terceiroDesconto;
	}
	public void setTerceiroDesconto(DescontoVO terceiroDesconto) {
		this.terceiroDesconto = terceiroDesconto;
	}
	public JurosVO getJurosMora() {
		if(jurosMora == null) {
			jurosMora = new JurosVO();
		}
		return jurosMora;
	}
	public void setJurosMora(JurosVO jurosMora) {
		this.jurosMora = jurosMora;
	}
	public MultaVO getMulta() {
		if(multa == null) {
			multa = new MultaVO();
		}
		return multa;
	}
	public void setMulta(MultaVO multa) {
		this.multa = multa;
	}
	public PagadorVO getPagador() {
		if(pagador ==null) {
			pagador = new PagadorVO();
		}
		return pagador;
	}
	public void setPagador(PagadorVO pagador) {
		this.pagador = pagador;
	}
	public BeneficiarioVO getBeneficiarioFinal() {
		if(beneficiarioFinal == null ) {
			beneficiarioFinal = new BeneficiarioVO();
		}
		return beneficiarioFinal;
	}
	public void setBeneficiarioFinal(BeneficiarioVO beneficiarioFinal) {
		this.beneficiarioFinal = beneficiarioFinal;
	}
	public String getIndicadorPix() {
		if(indicadorPix == null ) {
			indicadorPix ="";
		}
		return indicadorPix;
	}
	public void setIndicadorPix(String indicadorPix) {
		this.indicadorPix = indicadorPix;
	}
	

	
	
	

	
}
