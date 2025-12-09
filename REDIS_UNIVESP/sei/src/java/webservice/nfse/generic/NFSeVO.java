package webservice.nfse.generic;

import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

public class NFSeVO { 

	private String certificatePath;
	private String certificatePassword;
	private X509Certificate certificado;
	private String token;
	private String assinatura;
	private String versaoSchema;
	private AmbienteEnum ambiente;
	private String envioXML;
	private String consultaXML;
	private String cancelamentoXML;
	private String visualizacaoXML;
	private String retornoXML;
	private List<String> mensagens;
	private List<String> mensagensAlerta;
	private String urlVisualizacao;
	private String caminhoPDF;
	private String caminhoWeb;

	private Date dataCancelamento;
	private JustificativaCancelamentoEnum justificativaCancelamento;
	private String codigoVerificacao;
	private String protocolo;
	private SituacaoLoteRPSEnum situacaoLoteRPS;

	private long numeroLote;
	private long numeroRPS;
	private String logoPrestador;
	private String razSocialPrestador;
	private String cnpjPrestador;
	private String inscMunicipal;
	private String enderecoPrestador;
	private String complementoPrestador;
	private String numEndPrestador;
	private String bairroPrestador;
	private int cepPrestador;
	private String emailPrestador;
	private String dddPrestador;
	private String fonePrestador;
	private UFEnum ufPrestador;
	private int quantidadeRps;
	private int codigoIBGEMunicPrest;
	private int codigoBacenPaisPrest;
	private ExigibilidadeISSEnum exigibilidadeISS;
	private int codigoIBGEMunicIncidencia;
	private String numeroProcesso;
	private Date dataHoraEmissao;
	private Date dataHoraCompetencia;
	private long numeroNFSe;
	private String serie;
	private NaturezaOperacaoEnum naturezaOperacao;
	private RegimeEspecialTributacaoEnum regimeEspecialTributacao;
	private TipoRPSEnum tipoRPS;
	private boolean optanteSimplesNacional;
	private boolean incentivadorCultural;
	private StatusRPSEnum status;
	private long numeroRPSSubstituido;
	private String serieRPSSubstituido;
	private TipoRPSEnum tipoRPSSubstituido;
	private long numeroNFSeSubstituida;
	private Date dataEmissaoNFSeSubstituida;
	private int codigoSIAFIMunicPrest;
	private String descricaoMunicPrest;
	private OperacaoEnum operacao;
	private TributacaoEnum tributacao;
	private boolean transacao;
	
	private BigDecimal valorServicos;
	private BigDecimal valorDeducoes;
	private BigDecimal valorPis;
	private BigDecimal valorCofins;
	private BigDecimal valorInss;
	private BigDecimal valorIr;
	private BigDecimal valorCsll;
	private boolean issRetido;
	private ResponsavelRetencaoEnum responsavelRetencao;
	private BigDecimal valorRetido;
	private BigDecimal valorIss;
	private BigDecimal valorIssRetido;
	private BigDecimal valorOutras;
	private BigDecimal valorBaseCalc;
	private BigDecimal aliquota;
	private BigDecimal aliquotaPis;
	private BigDecimal aliquotaCofins;
	private BigDecimal aliquotaInss;
	private BigDecimal aliquotaIr;
	private BigDecimal aliquotaCsll;
	private String descricaoRPS;
	private String dddTomador;
	private TipoRecolhimentoEnum tipoRecolhimento;
	private BigDecimal valorLiquido;
	private BigDecimal valorDescontoIncondicionado;
	private BigDecimal valorDescontoCondicionado;
	private BigDecimal quantidade;
	private BigDecimal ValorUnitario;
	private BigDecimal ValorTotal;
	private TributavelEnum tributavel;
	private BigDecimal valorCargaTributaria;
	private BigDecimal percentualCargaTributaria;
	private String fonteCargaTributaria;

	private String listaServico;
	private String descricaoListaServico;
	private int cnae;
	private String codigoTributacaoMunicipio;
	private String descricaoTributacaoMunicipio;
	private String discriminacao;
	private String cpfCnpjTomador;
	private String inscMunicTomador;
	private String razSocialTomador;
	private String tipoLogradouroTomador;
	private String enderecoTomador;
	private String complementoTomador;
	private String numEndTomador;
	private String tipoBairroTomador;
	private String bairroTomador;
	private int cepTomador;
	private String emailTomador;
	private String foneTomador;
	private int codigoIBGEMunicTomador;
	private int codigoBacenPaisTomador;
	private UFEnum ufTomador;
	private int codigoSIAFIMunicTomador;
	private String descricaoMunicTomador;

	private String razaoSocialIntermediario;
	private String inscMunicIntermediario;
	private String cpfCnpjIntermediario;

	private long codigodaObra;
	private long art;
	
	private Date dataInicioConsultarNotas;
	private Date dataFimConsultarNotas;

	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getCertificatePassword() {
		return certificatePassword;
	}

	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}

	public X509Certificate getCertificado() {
		return certificado;
	}

	public void setCertificado(X509Certificate certificado) {
		this.certificado = certificado;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAssinatura() {
		return assinatura;
	}

	public void setAssinatura(String assinatura) {
		this.assinatura = assinatura;
	}

	public String getVersaoSchema() {
		return versaoSchema;
	}

	public void setVersaoSchema(String versaoSchema) {
		this.versaoSchema = versaoSchema;
	}

	public AmbienteEnum getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(AmbienteEnum ambiente) {
		this.ambiente = ambiente;
	}

	public long getNumeroRPS() {
		return numeroRPS;
	}

	public void setNumeroRPS(long numeroRPS) {
		this.numeroRPS = numeroRPS;
	}

	public String getCnpjPrestador() {
		return cnpjPrestador;
	}

	public void setCnpjPrestador(String cnpjPrestador) {
		this.cnpjPrestador = cnpjPrestador;
	}

	public String getInscMunicipal() {
		return inscMunicipal;
	}

	public void setInscMunicipal(String inscMunicipal) {
		this.inscMunicipal = inscMunicipal;
	}

	public String getRazSocialPrestador() {
		return razSocialPrestador;
	}

	public void setRazSocialPrestador(String razSocialPrestador) {
		this.razSocialPrestador = razSocialPrestador;
	}

	public boolean getTransacao() {
		return transacao;
	}

	public void setTransacao(boolean transacao) {
		this.transacao = transacao;
	}

	public int getQuantidadeRps() {
		return quantidadeRps;
	}

	public void setQuantidadeRps(int quantidadeRps) {
		this.quantidadeRps = quantidadeRps;
	}

	public int getCodigoIBGEMunicPrest() {
		return codigoIBGEMunicPrest;
	}

	public void setCodigoIBGEMunicPrest(int codigoIBGEMunicPrest) {
		this.codigoIBGEMunicPrest = codigoIBGEMunicPrest;
	}

	public int getCodigoSIAFIMunicPrest() {
		return codigoSIAFIMunicPrest;
	}

	public void setCodigoSIAFIMunicPrest(int codigoSIAFIMunicPrest) {
		this.codigoSIAFIMunicPrest = codigoSIAFIMunicPrest;
	}

	public String getDescricaoMunicPrest() {
		return descricaoMunicPrest;
	}

	public void setDescricaoMunicPrest(String descricaoMunicPrest) {
		this.descricaoMunicPrest = descricaoMunicPrest;
	}

	public OperacaoEnum getOperacao() {
		return operacao;
	}

	public void setOperacao(OperacaoEnum operacao) {
		this.operacao = operacao;
	}

	public TributacaoEnum getTributacao() {
		return tributacao;
	}

	public void setTributacao(TributacaoEnum tributacao) {
		this.tributacao = tributacao;
	}

	public Date getDataHoraEmissao() {
		return dataHoraEmissao;
	}

	public void setDataHoraEmissao(Date dataHoraEmissao) {
		this.dataHoraEmissao = dataHoraEmissao;
	}

	public long getNumeroNFSe() {
		return numeroNFSe;
	}

	public void setNumeroNFSe(long numeroNFSe) {
		this.numeroNFSe = numeroNFSe;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public NaturezaOperacaoEnum getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(NaturezaOperacaoEnum naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public RegimeEspecialTributacaoEnum getRegimeEspecialTributacao() {
		return regimeEspecialTributacao;
	}

	public void setRegimeEspecialTributacao(RegimeEspecialTributacaoEnum regimeEspecialTributacao) {
		this.regimeEspecialTributacao = regimeEspecialTributacao;
	}

	public TipoRPSEnum getTipoRPS() {
		return tipoRPS;
	}

	public void setTipoRPS(TipoRPSEnum tipoRPS) {
		this.tipoRPS = tipoRPS;
	}

	public boolean isOptanteSimplesNacional() {
		return optanteSimplesNacional;
	}

	public void setOptanteSimplesNacional(boolean optanteSimplesNacional) {
		this.optanteSimplesNacional = optanteSimplesNacional;
	}

	public boolean isIncentivadorCultural() {
		return incentivadorCultural;
	}

	public void setIncentivadorCultural(boolean incentivadorCultural) {
		this.incentivadorCultural = incentivadorCultural;
	}

	public StatusRPSEnum getStatus() {
		return status;
	}

	public void setStatus(StatusRPSEnum status) {
		this.status = status;
	}

	public long getNumeroRPSSubstituido() {
		return numeroRPSSubstituido;
	}

	public void setNumeroRPSSubstituido(long numeroRPSSubstituido) {
		this.numeroRPSSubstituido = numeroRPSSubstituido;
	}

	public String getSerieRPSSubstituido() {
		return serieRPSSubstituido;
	}

	public void setSerieRPSSubstituido(String serieRPSSubstituido) {
		this.serieRPSSubstituido = serieRPSSubstituido;
	}

	public BigDecimal getValorServicos() {
		return valorServicos;
	}

	public void setValorServicos(BigDecimal valorServicos) {
		this.valorServicos = valorServicos;
	}

	public BigDecimal getValorDeducoes() {
		return valorDeducoes;
	}

	public void setValorDeducoes(BigDecimal valorDeducoes) {
		this.valorDeducoes = valorDeducoes;
	}

	public BigDecimal getValorPis() {
		return valorPis;
	}

	public void setValorPis(BigDecimal valorPis) {
		this.valorPis = valorPis;
	}

	public BigDecimal getValorCofins() {
		return valorCofins;
	}

	public void setValorCofins(BigDecimal valorCofins) {
		this.valorCofins = valorCofins;
	}

	public BigDecimal getValorInss() {
		return valorInss;
	}

	public void setValorInss(BigDecimal valorInss) {
		this.valorInss = valorInss;
	}

	public BigDecimal getValorIr() {
		return valorIr;
	}

	public void setValorIr(BigDecimal valorIr) {
		this.valorIr = valorIr;
	}

	public BigDecimal getValorCsll() {
		return valorCsll;
	}

	public void setValorCsll(BigDecimal valorCsll) {
		this.valorCsll = valorCsll;
	}

	public boolean isIssRetido() {
		return issRetido;
	}

	public void setIssRetido(boolean issRetido) {
		this.issRetido = issRetido;
	}

	public BigDecimal getValorRetido() {
		return valorRetido;
	}

	public void setValorRetido(BigDecimal valorRetido) {
		this.valorRetido = valorRetido;
	}

	public BigDecimal getValorIss() {
		return valorIss;
	}

	public void setValorIss(BigDecimal valorIss) {
		this.valorIss = valorIss;
	}

	public BigDecimal getValorOutras() {
		return valorOutras;
	}

	public void setValorOutras(BigDecimal valorOutras) {
		this.valorOutras = valorOutras;
	}

	public BigDecimal getValorBaseCalc() {
		return valorBaseCalc;
	}

	public void setValorBaseCalc(BigDecimal valorBaseCalc) {
		this.valorBaseCalc = valorBaseCalc;
	}

	public BigDecimal getAliquota() {
		return aliquota;
	}

	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
	}

	public BigDecimal getAliquotaPis() {
		return aliquotaPis;
	}

	public void setAliquotaPis(BigDecimal aliquotaPis) {
		this.aliquotaPis = aliquotaPis;
	}

	public BigDecimal getAliquotaCofins() {
		return aliquotaCofins;
	}

	public void setAliquotaCofins(BigDecimal aliquotaCofins) {
		this.aliquotaCofins = aliquotaCofins;
	}

	public BigDecimal getAliquotaInss() {
		return aliquotaInss;
	}

	public void setAliquotaInss(BigDecimal aliquotaInss) {
		this.aliquotaInss = aliquotaInss;
	}

	public BigDecimal getAliquotaIr() {
		return aliquotaIr;
	}

	public void setAliquotaIr(BigDecimal aliquotaIr) {
		this.aliquotaIr = aliquotaIr;
	}

	public BigDecimal getAliquotaCsll() {
		return aliquotaCsll;
	}

	public void setAliquotaCsll(BigDecimal aliquotaCsll) {
		this.aliquotaCsll = aliquotaCsll;
	}

	public String getDescricaoRPS() {
		return descricaoRPS;
	}

	public void setDescricaoRPS(String descricaoRPS) {
		this.descricaoRPS = descricaoRPS;
	}

	public String getDddPrestador() {
		return dddPrestador;
	}

	public void setDddPrestador(String dddPrestador) {
		this.dddPrestador = dddPrestador;
	}

	public String getFonePrestador() {
		return fonePrestador;
	}

	public void setFonePrestador(String fonePrestador) {
		this.fonePrestador = fonePrestador;
	}

	public String getDddTomador() {
		return dddTomador;
	}

	public void setDddTomador(String dddTomador) {
		this.dddTomador = dddTomador;
	}

	public TipoRecolhimentoEnum getTipoRecolhimento() {
		return tipoRecolhimento;
	}

	public void setTipoRecolhimento(TipoRecolhimentoEnum tipoRecolhimento) {
		this.tipoRecolhimento = tipoRecolhimento;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public BigDecimal getValorDescontoIncondicionado() {
		return valorDescontoIncondicionado;
	}

	public void setValorDescontoIncondicionado(BigDecimal valorDescontoIncondicionado) {
		this.valorDescontoIncondicionado = valorDescontoIncondicionado;
	}

	public BigDecimal getValorDescontoCondicionado() {
		return valorDescontoCondicionado;
	}

	public void setValorDescontoCondicionado(BigDecimal valorDescontoCondicionado) {
		this.valorDescontoCondicionado = valorDescontoCondicionado;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return ValorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		ValorUnitario = valorUnitario;
	}

	public BigDecimal getValorTotal() {
		return ValorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		ValorTotal = valorTotal;
	}

	public TributavelEnum getTributavel() {
		return tributavel;
	}

	public void setTributavel(TributavelEnum tributavel) {
		this.tributavel = tributavel;
	}
	
	public BigDecimal getValorCargaTributaria() {
		return valorCargaTributaria;
	}

	public void setValorCargaTributaria(BigDecimal valorCargaTributaria) {
		this.valorCargaTributaria = valorCargaTributaria;
	}

	public BigDecimal getPercentualCargaTributaria() {
		return percentualCargaTributaria;
	}

	public void setPercentualCargaTributaria(BigDecimal percentualCargaTributaria) {
		this.percentualCargaTributaria = percentualCargaTributaria;
	}

	public String getFonteCargaTributaria() {
		return fonteCargaTributaria;
	}

	public void setFonteCargaTributaria(String fonteCargaTributaria) {
		this.fonteCargaTributaria = fonteCargaTributaria;
	}

	public String getListaServico() {
		return listaServico;
	}

	public void setListaServico(String listaServico) {
		this.listaServico = listaServico;
	}

	public int getCnae() {
		return cnae;
	}
	/**
	 * Código da Atividade 
	 */	
	public void setCnae(int cnae) {
		this.cnae = cnae;
	}

	public String getCodigoTributacaoMunicipio() {
		return codigoTributacaoMunicipio;
	}

	public void setCodigoTributacaoMunicipio(String codigoTributacaoMunicipio) {
		this.codigoTributacaoMunicipio = codigoTributacaoMunicipio;
	}

	public String getDiscriminacao() {
		return discriminacao;
	}

	public void setDiscriminacao(String discriminacao) {
		this.discriminacao = discriminacao;
	}

	public String getCpfCnpjTomador() {
		return cpfCnpjTomador;
	}

	public void setCpfCnpjTomador(String cpfCnpjTomador) {
		this.cpfCnpjTomador = cpfCnpjTomador;
	}

	public String getInscMunicTomador() {
		return inscMunicTomador;
	}

	public void setInscMunicTomador(String inscMunicTomador) {
		this.inscMunicTomador = inscMunicTomador;
	}

	public String getRazSocialTomador() {
		return razSocialTomador;
	}

	public void setRazSocialTomador(String razSocialTomador) {
		this.razSocialTomador = razSocialTomador;
	}

	public String getTipoLogradouroTomador() {
		return tipoLogradouroTomador;
	}

	/**
	 * Tipos de Logradouro permitidos para NFSe de São Luis:
	 * Avenida;Rua;Rodovia;Ruela;Rio;Sítio;Sup Quadra;Travessa;Vale
	 * Via;Viaduto;Viela;Vila;Vargem
	 */
	
	public void setTipoLogradouroTomador(String tipoLogradouroTomador) {
		this.tipoLogradouroTomador = tipoLogradouroTomador;
	}

	public String getEnderecoTomador() {
		return enderecoTomador;
	}

	public void setEnderecoTomador(String enderecoTomador) {
		this.enderecoTomador = enderecoTomador;
	}

	public String getComplementoTomador() {
		return complementoTomador;
	}

	public void setComplementoTomador(String complementoTomador) {
		this.complementoTomador = complementoTomador;
	}

	public String getNumEndTomador() {
		return numEndTomador;
	}

	public void setNumEndTomador(String numEndTomador) {
		this.numEndTomador = numEndTomador;
	}

	public String getDescricaoMunicTomador() {
		return descricaoMunicTomador;
	}

	public void setDescricaoMunicTomador(String descricaoMunicTomador) {
		this.descricaoMunicTomador = descricaoMunicTomador;
	}

	public String getTipoBairroTomador() {
		return tipoBairroTomador;
	}

	/**
	 * Tipos de Bairro permitidos para NFSe de São Luis:
	 * Bairro;Bosque;Chácara;Conjunto;Desmembramento;Distrito;Favela;Fazenda;Gleba;Horto;
	 * Jardim;Loteamento;Núcleo;Parque;Residencial;Sítio;Tropical;Vila;Zona
	 */
	public void setTipoBairroTomador(String tipoBairroTomador) {
		this.tipoBairroTomador = tipoBairroTomador;
	}

	public String getBairroTomador() {
		return bairroTomador;
	}

	public void setBairroTomador(String bairroTomador) {
		this.bairroTomador = bairroTomador;
	}

	public int getCepTomador() {
		return cepTomador;
	}

	public void setCepTomador(int cepTomador) {
		this.cepTomador = cepTomador;
	}

	public String getEmailTomador() {
		return emailTomador;
	}

	public void setEmailTomador(String emailTomador) {
		this.emailTomador = emailTomador;
	}

	public String getFoneTomador() {
		return foneTomador;
	}

	public void setFoneTomador(String foneTomador) {
		this.foneTomador = foneTomador;
	}

	public int getCodigoIBGEMunicTomador() {
		return codigoIBGEMunicTomador;
	}

	public void setCodigoIBGEMunicTomador(int codigoIBGEMunicTomador) {
		this.codigoIBGEMunicTomador = codigoIBGEMunicTomador;
	}

	public int getCodigoSIAFIMunicTomador() {
		return codigoSIAFIMunicTomador;
	}

	public void setCodigoSIAFIMunicTomador(int codigoSIAFIMunicTomador) {
		this.codigoSIAFIMunicTomador = codigoSIAFIMunicTomador;
	}

	public UFEnum getUfTomador() {
		return ufTomador;
	}

	public void setUfTomador(UFEnum ufTomador) {
		this.ufTomador = ufTomador;
	}

	public String getRazaoSocialIntermediario() {
		return razaoSocialIntermediario;
	}

	public void setRazaoSocialIntermediario(String razaoSocialIntermediario) {
		this.razaoSocialIntermediario = razaoSocialIntermediario;
	}

	public String getInscMunicIntermediario() {
		return inscMunicIntermediario;
	}

	public void setInscMunicIntermediario(String inscMunicIntermediario) {
		this.inscMunicIntermediario = inscMunicIntermediario;
	}

	public String getCpfCnpjIntermediario() {
		return cpfCnpjIntermediario;
	}

	public void setCpfCnpjIntermediario(String cpfCnpjIntermediario) {
		this.cpfCnpjIntermediario = cpfCnpjIntermediario;
	}

	public long getCodigodaObra() {
		return codigodaObra;
	}

	public void setCodigodaObra(long codigodaObra) {
		this.codigodaObra = codigodaObra;
	}

	public long getArt() {
		return art;
	}

	public void setArt(long art) {
		this.art = art;
	}

	public String getCodigoVerificacao() {
		return codigoVerificacao;
	}

	public void setCodigoVerificacao(String codigoVerificacao) {
		this.codigoVerificacao = codigoVerificacao;
	}

	public JustificativaCancelamentoEnum getJustificativaCancelamento() {
		return justificativaCancelamento;
	}

	public void setJustificativaCancelamento(JustificativaCancelamentoEnum justificativaCancelamento) {
		this.justificativaCancelamento = justificativaCancelamento;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public BigDecimal getValorIssRetido() {
		return valorIssRetido;
	}

	public void setValorIssRetido(BigDecimal valorIssRetido) {
		this.valorIssRetido = valorIssRetido;
	}

	public String getEnvioXML() {
		return envioXML;
	}

	public void setEnvioXML(String envioXML) {
		this.envioXML = envioXML;
	}

	public String getRetornoXML() {
		return retornoXML;
	}

	public void setRetornoXML(String retornoXML) {
		this.retornoXML = retornoXML;
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}

	public String getUrlVisualizacao() {
		return urlVisualizacao;
	}

	public void setUrlVisualizacao(String urlVisualizacao) {
		this.urlVisualizacao = urlVisualizacao;
	}

	public long getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(long numeroLote) {
		this.numeroLote = numeroLote;
	}

	public String getConsultaXML() {
		return consultaXML;
	}

	public void setConsultaXML(String consultaXML) {
		this.consultaXML = consultaXML;
	}

	public String getCancelamentoXML() {
		return cancelamentoXML;
	}

	public void setCancelamentoXML(String cancelamentoXML) {
		this.cancelamentoXML = cancelamentoXML;
	}

	public String getVisualizacaoXML() {
		return visualizacaoXML;
	}

	public void setVisualizacaoXML(String visualizacaoXML) {
		this.visualizacaoXML = visualizacaoXML;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public TipoRPSEnum getTipoRPSSubstituido() {
		return tipoRPSSubstituido;
	}

	public void setTipoRPSSubstituido(TipoRPSEnum tipoRPSSubstituido) {
		this.tipoRPSSubstituido = tipoRPSSubstituido;
	}

	public long getNumeroNFSeSubstituida() {
		return numeroNFSeSubstituida;
	}

	public void setNumeroNFSeSubstituida(long numeroNFSeSubstituida) {
		this.numeroNFSeSubstituida = numeroNFSeSubstituida;
	}

	public Date getDataEmissaoNFSeSubstituida() {
		return dataEmissaoNFSeSubstituida;
	}

	public void setDataEmissaoNFSeSubstituida(Date dataEmissaoNFSeSubstituida) {
		this.dataEmissaoNFSeSubstituida = dataEmissaoNFSeSubstituida;
	}

	public SituacaoLoteRPSEnum getSituacaoLoteRPS() {
		return situacaoLoteRPS;
	}

	public void setSituacaoLoteRPS(SituacaoLoteRPSEnum situacaoLoteRPS) {
		this.situacaoLoteRPS = situacaoLoteRPS;
	}

	public int getCodigoBacenPaisPrest() {
		return codigoBacenPaisPrest;
	}

	public void setCodigoBacenPaisPrest(int codigoBacenPaisPrest) {
		this.codigoBacenPaisPrest = codigoBacenPaisPrest;
	}

	public ExigibilidadeISSEnum getExigibilidadeISS() {
		return exigibilidadeISS;
	}

	public void setExigibilidadeISS(ExigibilidadeISSEnum exigibilidadeISS) {
		this.exigibilidadeISS = exigibilidadeISS;
	}

	public int getCodigoIBGEMunicIncidencia() {
		return codigoIBGEMunicIncidencia;
	}

	public void setCodigoIBGEMunicIncidencia(int codigoIBGEMunicIncidencia) {
		this.codigoIBGEMunicIncidencia = codigoIBGEMunicIncidencia;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Date getDataHoraCompetencia() {
		return dataHoraCompetencia;
	}

	public void setDataHoraCompetencia(Date dataHoraCompetencia) {
		this.dataHoraCompetencia = dataHoraCompetencia;
	}

	public ResponsavelRetencaoEnum getResponsavelRetencao() {
		return responsavelRetencao;
	}

	public void setResponsavelRetencao(ResponsavelRetencaoEnum responsavelRetencao) {
		this.responsavelRetencao = responsavelRetencao;
	}

	public int getCodigoBacenPaisTomador() {
		return codigoBacenPaisTomador;
	}

	public void setCodigoBacenPaisTomador(int codigoBacenPaisTomador) {
		this.codigoBacenPaisTomador = codigoBacenPaisTomador;
	}

	public Date getDataInicioConsultarNotas() {
		return dataInicioConsultarNotas;
	}

	public void setDataInicioConsultarNotas(Date dataInicioConsultarNotas) {
		this.dataInicioConsultarNotas = dataInicioConsultarNotas;
	}

	public Date getDataFimConsultarNotas() {
		return dataFimConsultarNotas;
	}

	public void setDataFimConsultarNotas(Date dataFimConsultarNotas) {
		this.dataFimConsultarNotas = dataFimConsultarNotas;
	}

	public String getEnderecoPrestador() {
		return enderecoPrestador;
	}

	public void setEnderecoPrestador(String enderecoPrestador) {
		this.enderecoPrestador = enderecoPrestador;
	}

	public String getComplementoPrestador() {
		return complementoPrestador;
	}

	public void setComplementoPrestador(String complementoPrestador) {
		this.complementoPrestador = complementoPrestador;
	}

	public String getNumEndPrestador() {
		return numEndPrestador;
	}

	public void setNumEndPrestador(String numEndPrestador) {
		this.numEndPrestador = numEndPrestador;
	}

	public String getBairroPrestador() {
		return bairroPrestador;
	}

	public void setBairroPrestador(String bairroPrestador) {
		this.bairroPrestador = bairroPrestador;
	}

	public int getCepPrestador() {
		return cepPrestador;
	}

	public void setCepPrestador(int cepPrestador) {
		this.cepPrestador = cepPrestador;
	}

	public String getEmailPrestador() {
		return emailPrestador;
	}

	public void setEmailPrestador(String emailPrestador) {
		this.emailPrestador = emailPrestador;
	}

	public UFEnum getUfPrestador() {
		return ufPrestador;
	}

	public void setUfPrestador(UFEnum ufPrestador) {
		this.ufPrestador = ufPrestador;
	}

	public String getLogoPrestador() {
		return logoPrestador;
	}

	public void setLogoPrestador(String logoPrestador) {
		this.logoPrestador = logoPrestador;
	}

	public String getDescricaoListaServico() {
		return descricaoListaServico;
	}

	public void setDescricaoListaServico(String descricaoListaServico) {
		this.descricaoListaServico = descricaoListaServico;
	}

	public String getDescricaoTributacaoMunicipio() {
		return descricaoTributacaoMunicipio;
	}

	public void setDescricaoTributacaoMunicipio(String descricaoTributacaoMunicipio) {
		this.descricaoTributacaoMunicipio = descricaoTributacaoMunicipio;
	}

	public String getCaminhoPDF() {
		return caminhoPDF;
	}

	public void setCaminhoPDF(String caminhoPDF) {
		this.caminhoPDF = caminhoPDF;
	}
	
	public String getCaminhoWeb() {
		return caminhoWeb;
	}

	public void setCaminhoWeb(String caminhoWeb) {
		this.caminhoWeb = caminhoWeb;
	}
	
	public List<String> getMensagensAlerta() {
		return mensagensAlerta;
	}
	
	public void setMensagensAlerta(List<String> mensagensAlerta) {
		this.mensagensAlerta = mensagensAlerta;
	}
	 
}