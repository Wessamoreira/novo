package negocio.comuns.faturamento.nfe;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.faturamento.nfe.enumeradores.CodigoRegimeTributarioEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoIntegracaoNfeEnum;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import webservice.nfse.generic.NaturezaOperacaoEnum;
import webservice.nfse.generic.RegimeEspecialTributacaoEnum;

public class ConfiguracaoNotaFiscalVO extends SuperVO implements PossuiEndereco {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nome;
	private String senhaCertificado;
	private String senhaUnidadeCertificadora;
	private String caminhoCertificado;
	private ArquivoVO arquivoVO;
	private Double issqn;
	private Double iss;
	private Long numeroNota;
	private Long lote;
	private TipoIntegracaoNfeEnum tipoIntegracaoNfeEnum;
	private AmbienteNfeEnum ambienteNfeEnum;
	private CodigoRegimeTributarioEnum codigoRegimeTributarioEnum;
	private String codigoNaturezaOperacao;
	private String nomeNaturezaOperacao;
	private String nomeNaturezaOperacaoInterestadual;
	private String codigoNaturezaOperacaoInterestadual;
	private Boolean utilizarEnderecoDiferenteUnidade;
	private Boolean notificarAlunoNotaFiscalGerada;
	private String endereco;
	private String setor;
	private String numero;
	private String complemento;
	private CidadeVO cidade;
	private String CEP;
	protected String telComercial1;
	private String codigoNCM;
	private Double pis;
	private Double cofins;
	private Double inss;
	private Double csll;
	private String codigoTributacaoMunicipio;
	private String codigoMunicipio;
	private String codigoItemListaServico;
	private String cstPis;
	private String cstCofins;
	private String descricaoAtividadeTributacaoMunicipio;
	private boolean agruparNotaFicalPorResponsavelFinanceiro;
	private String tokenWebserviceNFe;
	private NaturezaOperacaoEnum naturezaOperacaoEnum;
	private Boolean isIncentivadorCultural;
	private RegimeEspecialTributacaoEnum regimeEspecialTributacaoEnum;
	private String codigoCNAE;
	private String serie;
	private Integer qtdeDiasPermitidoEmitirNotaFiscalRetroativa;
	private Integer numeroNotaHomologacao;
	private Integer loteHomologacao;
	private String serieHomologacao;
	private Integer fusoHorario;
	private Double percentualCargaTributaria;
	private String fonteCargaTributaria;
	private Boolean utilizarServicoWebserviceAuxiliar;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenhaCertificado() {
		if (senhaCertificado == null) {
			senhaCertificado = "";
		}
		return senhaCertificado;
	}

	public void setSenhaCertificado(String senhaCertificado) {
		this.senhaCertificado = senhaCertificado;
	}

	public AmbienteNfeEnum getAmbienteNfeEnum() {
		return ambienteNfeEnum;
	}

	public void setAmbienteNfeEnum(AmbienteNfeEnum ambienteNfeEnum) {
		this.ambienteNfeEnum = ambienteNfeEnum;
	}

	public String getSenhaUnidadeCertificadora() {
		if (senhaUnidadeCertificadora == null) {
			senhaUnidadeCertificadora = "processus10";
		}
		return senhaUnidadeCertificadora;
	}

	public void setSenhaUnidadeCertificadora(String senhaUnidadeCertificadora) {
		this.senhaUnidadeCertificadora = senhaUnidadeCertificadora;
	}

	public CodigoRegimeTributarioEnum getCodigoRegimeTributarioEnum() {
		return codigoRegimeTributarioEnum;
	}

	public void setCodigoRegimeTributarioEnum(CodigoRegimeTributarioEnum codigoRegimeTributarioEnum) {
		this.codigoRegimeTributarioEnum = codigoRegimeTributarioEnum;
	}

	public TipoIntegracaoNfeEnum getTipoIntegracaoNfeEnum() {
		return tipoIntegracaoNfeEnum;
	}

	public void setTipoIntegracaoNfeEnum(TipoIntegracaoNfeEnum tipoIntegracaoNfeEnum) {
		this.tipoIntegracaoNfeEnum = tipoIntegracaoNfeEnum;
	}

	public String getCaminhoCertificado() {
		if (caminhoCertificado == null) {
			caminhoCertificado = "";
		}
		return caminhoCertificado;
	}

	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public String getAmbienteNfeApresentar() {
		return ambienteNfeEnum.getValue();
	}

	public Double getIssqn() {
		if (issqn == null) {
			issqn = 0.0;
		}
		return issqn;
	}

	public void setIssqn(Double issqn) {
		this.issqn = issqn;
	}

	public Long getNumeroNota() {
		if (numeroNota == null) {
			numeroNota = 0L;
		}
		return numeroNota;
	}

	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Long getLote() {
		if (lote == null) {
			lote = 0l;
		}
		return lote;
	}

	public void setLote(Long lote) {
		this.lote = lote;
	}

	public Double getIss() {
		if (iss == null) {
			iss = 0.0;
		}
		return iss;
	}

	public void setIss(Double iss) {
		this.iss = iss;
	}

	public String getCodigoNaturezaOperacao() {
		if (codigoNaturezaOperacao == null) {
			codigoNaturezaOperacao = "";
		}
		return codigoNaturezaOperacao;
	}

	public void setCodigoNaturezaOperacao(String codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	public String getNomeNaturezaOperacao() {
		if (nomeNaturezaOperacao == null) {
			nomeNaturezaOperacao = "";
		}
		return nomeNaturezaOperacao;
	}

	public void setNomeNaturezaOperacao(String nomeNaturezaOperacao) {
		this.nomeNaturezaOperacao = nomeNaturezaOperacao;
	}

	public String getNomeNaturezaOperacaoInterestadual() {
		if (nomeNaturezaOperacaoInterestadual == null) {
			nomeNaturezaOperacaoInterestadual = "";
		}
		return nomeNaturezaOperacaoInterestadual;
	}

	public void setNomeNaturezaOperacaoInterestadual(String nomeNaturezaOperacaoInterestadual) {
		this.nomeNaturezaOperacaoInterestadual = nomeNaturezaOperacaoInterestadual;
	}

	public String getCodigoNaturezaOperacaoInterestadual() {
		if (codigoNaturezaOperacaoInterestadual == null) {
			codigoNaturezaOperacaoInterestadual = "";
		}
		return codigoNaturezaOperacaoInterestadual;
	}

	public void setCodigoNaturezaOperacaoInterestadual(String codigoNaturezaOperacaoInterestadual) {
		this.codigoNaturezaOperacaoInterestadual = codigoNaturezaOperacaoInterestadual;
	}

	public Boolean getUtilizarEnderecoDiferenteUnidade() {
		if (utilizarEnderecoDiferenteUnidade == null) {
			utilizarEnderecoDiferenteUnidade = false;
		}
		return utilizarEnderecoDiferenteUnidade;
	}

	public void setUtilizarEnderecoDiferenteUnidade(Boolean utilizarEnderecoDiferenteUnidade) {
		this.utilizarEnderecoDiferenteUnidade = utilizarEnderecoDiferenteUnidade;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public String getCEP() {
		if (CEP == null) {
			CEP = "";
		}
		return CEP;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	public String getTelComercial1() {
		if (telComercial1 == null) {
			telComercial1 = "";
		}
		return telComercial1;
	}

	public void setTelComercial1(String telComercial1) {
		this.telComercial1 = telComercial1;
	}

	public String getCodigoNCM() {
		if (codigoNCM == null) {
			codigoNCM = "";
		}
		return codigoNCM;
	}

	public void setCodigoNCM(String codigoNCM) {
		this.codigoNCM = codigoNCM;
	}

	public Double getPis() {
		if(pis == null) {
			pis = 0.0;
		}
		return pis;
	}

	public void setPis(Double pis) {
		this.pis = pis;
	}

	public Double getCofins() {
		if(cofins == null) {
			cofins = 0.0;
		}
		return cofins;
	}

	public void setCofins(Double cofins) {
		this.cofins = cofins;
	}

	public Double getInss() {
		if(inss == null) {
			inss = 0.0;
		}
		return inss;
	}

	public void setInss(Double inss) {
		this.inss = inss;
	}

	public Double getCsll() {
		if(csll == null) {
			csll = 0.0;
		}
		return csll;
	}

	public void setCsll(Double csll) {
		this.csll = csll;
	}

	public String getCodigoTributacaoMunicipio() {
		if(codigoTributacaoMunicipio == null) {
			codigoTributacaoMunicipio = "";
		}
		return codigoTributacaoMunicipio;
	}

	public void setCodigoTributacaoMunicipio(String codigoTributacaoMunicipio) {
		this.codigoTributacaoMunicipio = codigoTributacaoMunicipio;
	}

	public String getCodigoMunicipio() {
		if(codigoMunicipio == null) {
			codigoMunicipio = "";
		}
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public Boolean getNotificarAlunoNotaFiscalGerada() {
		if (notificarAlunoNotaFiscalGerada == null) {
			notificarAlunoNotaFiscalGerada = Boolean.FALSE;
		}
		return notificarAlunoNotaFiscalGerada;
	}

	public void setNotificarAlunoNotaFiscalGerada(Boolean notificarAlunoNotaFiscalGerada) {
		this.notificarAlunoNotaFiscalGerada = notificarAlunoNotaFiscalGerada;
	}
	
	
	public String getCodigoItemListaServico() {
		if(codigoItemListaServico == null) {
			codigoItemListaServico = "08.01";
		}
		return codigoItemListaServico;
	}

	public void setCodigoItemListaServico(String codigoItemListaServico) {
		this.codigoItemListaServico = codigoItemListaServico;
	}

	public String getCstPis() {
		if(cstPis == null) {
			cstPis = "01";
		}
		return cstPis;
	}

	public void setCstPis(String cstPis) {
		this.cstPis = cstPis;
	}

	public String getCstCofins() {
		if(cstCofins == null) {
			cstCofins = "01";
		}
		return cstCofins;
	}

	public void setCstCofins(String cstCofins) {
		this.cstCofins = cstCofins;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 * 30/11/2015
	 */
	private String usuarioIntegracaoNotaFiscalServico;
	private String senhaUsuarioIntegracaoNotaFiscalServico;
	private String textoPadraoDescriminacaoServicoNotaFiscal;
	private Double aliquotaIR;

	public String getUsuarioIntegracaoNotaFiscalServico() {
		if(usuarioIntegracaoNotaFiscalServico == null) {
			usuarioIntegracaoNotaFiscalServico = "";
		}
		return usuarioIntegracaoNotaFiscalServico;
	}

	public void setUsuarioIntegracaoNotaFiscalServico(String usuarioIntegracaoNotaFiscalServico) {
		this.usuarioIntegracaoNotaFiscalServico = usuarioIntegracaoNotaFiscalServico;
	}

	public String getSenhaUsuarioIntegracaoNotaFiscalServico() {
		if(senhaUsuarioIntegracaoNotaFiscalServico == null) {
			senhaUsuarioIntegracaoNotaFiscalServico = "";
		}
		return senhaUsuarioIntegracaoNotaFiscalServico;
	}

	public void setSenhaUsuarioIntegracaoNotaFiscalServico(String senhaUsuarioIntegracaoNotaFiscalServico) {
		this.senhaUsuarioIntegracaoNotaFiscalServico = senhaUsuarioIntegracaoNotaFiscalServico;
	}

	public String getTextoPadraoDescriminacaoServicoNotaFiscal() {
		if(textoPadraoDescriminacaoServicoNotaFiscal == null) {
			textoPadraoDescriminacaoServicoNotaFiscal = "REFERENTE A SERVICO PRESTADO NO MES DE COMPETENCIA AO TIPO_PESSOA(A) NOME_ALUNO DO CURSO NOME_CURSO";
		}
		return textoPadraoDescriminacaoServicoNotaFiscal;
	}

	public void setTextoPadraoDescriminacaoServicoNotaFiscal(String textoPadraoDescriminacaoServicoNotaFiscal) {
		this.textoPadraoDescriminacaoServicoNotaFiscal = textoPadraoDescriminacaoServicoNotaFiscal;
	}

	public Double getAliquotaIR() {
		if(aliquotaIR == null) {
			aliquotaIR = 0.00;
		}
		return aliquotaIR;
	}

	public void setAliquotaIR(Double aliquotaIR) {
		this.aliquotaIR = aliquotaIR;
	}
	

	public String getDescricaoAtividadeTributacaoMunicipio() {
		if(descricaoAtividadeTributacaoMunicipio == null){
			descricaoAtividadeTributacaoMunicipio = "";
		}
		return descricaoAtividadeTributacaoMunicipio;
	}

	public void setDescricaoAtividadeTributacaoMunicipio(String descricaoAtividadeTributacaoMunicipio) {
		this.descricaoAtividadeTributacaoMunicipio = descricaoAtividadeTributacaoMunicipio;
	}
	
	public boolean isAgruparNotaFicalPorResponsavelFinanceiro() {
	    return agruparNotaFicalPorResponsavelFinanceiro;
	}
	
	public void setAgruparNotaFicalPorResponsavelFinanceiro(boolean agruparNotaFicalPorResponsavelFinanceiro) {
	    this.agruparNotaFicalPorResponsavelFinanceiro = agruparNotaFicalPorResponsavelFinanceiro;
	}

	public String getTokenWebserviceNFe() {
		if (tokenWebserviceNFe == null) {
			tokenWebserviceNFe = "";
		}
		return tokenWebserviceNFe;
	}

	public void setTokenWebserviceNFe(String tokenWebserviceNFe) {
		this.tokenWebserviceNFe = tokenWebserviceNFe;
	}
	
	public NaturezaOperacaoEnum getNaturezaOperacaoEnum() {
		return naturezaOperacaoEnum;
	}

	public void setNaturezaOperacaoEnum(NaturezaOperacaoEnum naturezaOperacaoEnum) {
		this.naturezaOperacaoEnum = naturezaOperacaoEnum;
	}

	public Boolean getIsIncentivadorCultural() {
		if (isIncentivadorCultural == null) {
			isIncentivadorCultural = false;
		}
		return isIncentivadorCultural;
	}

	public void setIsIncentivadorCultural(Boolean isIncentivadorCultural) {
		this.isIncentivadorCultural = isIncentivadorCultural;
	}

	public String getCodigoCNAE() {
		if (codigoCNAE == null) {
			codigoCNAE = "";
		}
		return codigoCNAE;
	}

	public void setCodigoCNAE(String codigoCNAE) {
		this.codigoCNAE = codigoCNAE;
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
	
	public Integer getQtdeDiasPermitidoEmitirNotaFiscalRetroativa() {
		if (qtdeDiasPermitidoEmitirNotaFiscalRetroativa == null) {
			qtdeDiasPermitidoEmitirNotaFiscalRetroativa = 0;
		}
		return qtdeDiasPermitidoEmitirNotaFiscalRetroativa;
	}

	public void setQtdeDiasPermitidoEmitirNotaFiscalRetroativa(Integer qtdeDiasPermitidoEmitirNotaFiscalRetroativa) {
		this.qtdeDiasPermitidoEmitirNotaFiscalRetroativa = qtdeDiasPermitidoEmitirNotaFiscalRetroativa;
	}
	
	public Integer getNumeroNotaHomologacao() {
		if (numeroNotaHomologacao == null) {
			numeroNotaHomologacao = 0;
		}
		return numeroNotaHomologacao;
	}

	public void setNumeroNotaHomologacao(Integer numeroNotaHomologacao) {
		this.numeroNotaHomologacao = numeroNotaHomologacao;
	}

	public Integer getLoteHomologacao() {
		if (loteHomologacao == null) {
			loteHomologacao = 0;
		}
		return loteHomologacao;
	}

	public void setLoteHomologacao(Integer loteHomologacao) {
		this.loteHomologacao = loteHomologacao;
	}

	public String getSerieHomologacao() {
		if (serieHomologacao == null) {
			serieHomologacao = "";
		}
		return serieHomologacao;
	}

	public void setSerieHomologacao(String serieHomologacao) {
		this.serieHomologacao = serieHomologacao;
	}

	public Integer getFusoHorario() {
		if (fusoHorario == null) {
			fusoHorario = 0;
		}
		return fusoHorario;
	}

	public void setFusoHorario(Integer fusoHorario) {
		this.fusoHorario = fusoHorario;
	}
	
	public RegimeEspecialTributacaoEnum getRegimeEspecialTributacaoEnum() {
		return regimeEspecialTributacaoEnum;
	}

	public void setRegimeEspecialTributacaoEnum(RegimeEspecialTributacaoEnum regimeEspecialTributacaoEnum) {
		this.regimeEspecialTributacaoEnum = regimeEspecialTributacaoEnum;
	}

	public Double getPercentualCargaTributaria() {
		if (percentualCargaTributaria == null) {
			percentualCargaTributaria = 0.0;
		}
		return percentualCargaTributaria;
	}

	public void setPercentualCargaTributaria(Double percentualCargaTributaria) {
		this.percentualCargaTributaria = percentualCargaTributaria;
	}

	public String getFonteCargaTributaria() {
		if (fonteCargaTributaria == null) {
			fonteCargaTributaria = "";
		}
		return fonteCargaTributaria;
	}

	public void setFonteCargaTributaria(String fonteCargaTributaria) {
		this.fonteCargaTributaria = fonteCargaTributaria;
	}

	public Boolean getUtilizarServicoWebserviceAuxiliar() {
		if(utilizarServicoWebserviceAuxiliar == null ) {
			utilizarServicoWebserviceAuxiliar = Boolean.FALSE;
		}
		return utilizarServicoWebserviceAuxiliar;
	}

	public void setUtilizarServicoWebserviceAuxiliar(Boolean utilizarServicoWebserviceAuxiliar) {
		this.utilizarServicoWebserviceAuxiliar = utilizarServicoWebserviceAuxiliar;
	}
	
}
