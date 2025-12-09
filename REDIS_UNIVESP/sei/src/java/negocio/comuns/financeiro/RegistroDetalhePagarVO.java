package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class RegistroDetalhePagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7637241540196683379L;
	private Integer codigo;
	private String codigoBanco;
	private String loteServico;
	private Integer tipoRegistro;
	private String numeroSequencialRegistroLote;
	private String codigoSegmentoRegistroLote;
	/**
	 * Nao foi usado ainda 14/12/2017 Pedro Andrade
	 */
	private Integer motivoOcorrencia;
	private String motivoRegeicao;
	
	private RegistroHeaderLotePagarVO registroHeaderLotePagarVO;
	
	/**
	 * Segmento A
	 */
	private String tipoMovimento;
	private String codigoInstrucaoMovimento;
	private String codigoCamaraCompesacao;
	private Integer codigoBancoFavorecido;
	private Integer numeroAgenciaFavorecido;
	private String digitoAgenciaFavorecido;
	private String numeroContaFavorecido;
	private String digitoContaFavorecido;
	private String digitoAgenciaContaFavorecido;
	private String nomeFavorecido;
	private Long nossoNumero;
	private Date dataPagamento;
	private Double valorPagamento;
	private String numeroDocumento;
	private Date dataRealPagamento;
	private Double valorRealPagamento;
	private String informacao2;
	private String finalidadeDoc;
	private String finalidadeTed;
	private String codigoFinalidadeComplementar;
	private String nossoNumeroContaAgrupada;
	private String codigoTransmissaoRemessaNossonumero;

	/**
	 * Segmento B
	 */
	private Integer tipoInscricaoFavorecido;
	private Long numeroInscricaoFavorecido;
	private String logradouroFavorecido;
	private String numeroEnderecoFavorecido;
	private String complementoEnderecoFavorecido;
	private String bairroFavorecido;
	private String cidadeFavorecido;
	private String cepFavorecido;
	private String estadoFavorecido;
	private Date dataVencimento;
	private Double valorDesconto;
	private Double valorMulta;
	private Double valorJuro;
	private String horarioEnvioTed;
	private String codigoHistoricoParaCredito;
	private String tedInstituicaoFinanceira;
	private String numeroISPB;

	/**
	 * Segmento J, O
	 */
	private String codigoBarra;

	/**
	 * Segmento N
	 */
	private String codigoReceitaTributo;
	private String tipoIdentificacaoContribuinte;
	private String identificacaoContribuinte;
	private String codigoidentificacaoTributo;

	/**
	 * Segmento N1 - GPS
	 */
	private String mesAnoCompetencia;

	/**
	 * Segmento N2 - DARF NORMAL
	 */
	private Date periodoApuracao;
	private String numeroReferencia;

	/**
	 * Segmento N3 - DARF SIMPLES
	 */
	private Double valorReceitaBrutaAcumulada;
	private Double percentualReceitaBrutaAcumulada;

	/**
	 * Segmento N4 - DARF SIMPLES
	 */
	private Long numeroInscricaoEstadualFavorecido;
	private Long numeroInscricaoMunicipalFavorecido;
	
	
	/**
	 * Segmento Z 
	 */
	private String autenticacaoPagamento;
	private String protocoloPagamento;
	
	
	/**
	 * transient
	 * @return
	 */
	private ContaPagarVO contaPagarVO;
	
	
	

	
	
	
	

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	public RegistroHeaderLotePagarVO getRegistroHeaderLotePagarVO() {
		if (registroHeaderLotePagarVO == null) {
			registroHeaderLotePagarVO = new RegistroHeaderLotePagarVO();
		}
		return registroHeaderLotePagarVO;
	}

	public void setRegistroHeaderLotePagarVO(RegistroHeaderLotePagarVO registroHeaderLotePagarVO) {
		this.registroHeaderLotePagarVO = registroHeaderLotePagarVO;
	}	

	
	public String getMotivoRegeicao() {
		if (motivoRegeicao == null) {
			motivoRegeicao = "";
		}
		return (motivoRegeicao);
	}

	public void setMotivoRegeicao(String motivoRegeicao) {
		this.motivoRegeicao = motivoRegeicao;
	}

	public Integer getMotivoOcorrencia() {
		if (motivoOcorrencia == null) {
			motivoOcorrencia = 0;
		}
		return (motivoOcorrencia);
	}

	public void setMotivoOcorrencia(Integer motivoOcorrencia) {
		this.motivoOcorrencia = motivoOcorrencia;
	}

	public String getCodigoBanco() {
		if (codigoBanco == null) {
			codigoBanco = "";
		}
		return (codigoBanco);
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
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

	public String getLoteServico() {
		if (loteServico == null) {
			loteServico = "";
		}
		return loteServico;
	}

	public void setLoteServico(String loteServico) {
		this.loteServico = loteServico;
	}

	public Integer getTipoRegistro() {
		if (tipoRegistro == null) {
			tipoRegistro = 0;
		}
		return tipoRegistro;
	}

	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getNumeroSequencialRegistroLote() {
		if (numeroSequencialRegistroLote == null) {
			numeroSequencialRegistroLote = "";
		}
		return numeroSequencialRegistroLote;
	}

	public void setNumeroSequencialRegistroLote(String numeroSequencialRegistroLote) {
		this.numeroSequencialRegistroLote = numeroSequencialRegistroLote;
	}

	public String getCodigoSegmentoRegistroLote() {
		if (codigoSegmentoRegistroLote == null) {
			codigoSegmentoRegistroLote = "";
		}
		return codigoSegmentoRegistroLote;
	}

	public void setCodigoSegmentoRegistroLote(String codigoSegmentoRegistroLote) {
		this.codigoSegmentoRegistroLote = codigoSegmentoRegistroLote;
	}

	public String getTipoMovimento() {
		if (tipoMovimento == null) {
			tipoMovimento = "";
		}
		return tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public String getCodigoInstrucaoMovimento() {
		if (codigoInstrucaoMovimento == null) {
			codigoInstrucaoMovimento = "";
		}
		return codigoInstrucaoMovimento;
	}

	public void setCodigoInstrucaoMovimento(String codigoInstrucaoMovimento) {
		this.codigoInstrucaoMovimento = codigoInstrucaoMovimento;
	}

	public String getCodigoCamaraCompesacao() {
		if (codigoCamaraCompesacao == null) {
			codigoCamaraCompesacao = "";
		}
		return codigoCamaraCompesacao;
	}

	public void setCodigoCamaraCompesacao(String codigoCamaraCompesacao) {
		this.codigoCamaraCompesacao = codigoCamaraCompesacao;
	}

	public Integer getCodigoBancoFavorecido() {
		if (codigoBancoFavorecido == null) {
			codigoBancoFavorecido = 0;
		}
		return codigoBancoFavorecido;
	}

	public void setCodigoBancoFavorecido(Integer codigoBancoFavorecido) {
		this.codigoBancoFavorecido = codigoBancoFavorecido;
	}

	public Integer getNumeroAgenciaFavorecido() {
		if (numeroAgenciaFavorecido == null) {
			numeroAgenciaFavorecido = 0;
		}
		return numeroAgenciaFavorecido;
	}

	public void setNumeroAgenciaFavorecido(Integer numeroAgenciaFavorecido) {
		this.numeroAgenciaFavorecido = numeroAgenciaFavorecido;
	}

	public String getDigitoAgenciaFavorecido() {
		if (digitoAgenciaFavorecido == null) {
			digitoAgenciaFavorecido = "";
		}
		return digitoAgenciaFavorecido;
	}

	public void setDigitoAgenciaFavorecido(String digitoAgenciaFavorecido) {
		this.digitoAgenciaFavorecido = digitoAgenciaFavorecido;
	}

	public String getNumeroContaFavorecido() {
		if (numeroContaFavorecido == null) {
			numeroContaFavorecido = "";
		}
		return numeroContaFavorecido;
	}

	public void setNumeroContaFavorecido(String numeroContaFavorecido) {
		this.numeroContaFavorecido = numeroContaFavorecido;
	}

	public String getDigitoContaFavorecido() {
		if (digitoContaFavorecido == null) {
			digitoContaFavorecido = "";
		}
		return digitoContaFavorecido;
	}

	public void setDigitoContaFavorecido(String digitoContaFavorecido) {
		this.digitoContaFavorecido = digitoContaFavorecido;
	}

	public String getDigitoAgenciaContaFavorecido() {
		if (digitoAgenciaContaFavorecido == null) {
			digitoAgenciaContaFavorecido = "";
		}
		return digitoAgenciaContaFavorecido;
	}

	public void setDigitoAgenciaContaFavorecido(String digitoAgenciaContaFavorecido) {
		this.digitoAgenciaContaFavorecido = digitoAgenciaContaFavorecido;
	}	
	

	public String getNomeFavorecido() {
		if (nomeFavorecido == null) {
			nomeFavorecido = "";
		}
		return nomeFavorecido;
	}

	public void setNomeFavorecido(String nomeFavorecido) {
		this.nomeFavorecido = nomeFavorecido;
	}

	public Long getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = 0L;
		}
		return nossoNumero;
	}

	public void setNossoNumero(Long nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDataPagamento_Apresentar() {
		return Uteis.getData(getDataPagamento());
	}
	
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getValorPagamento() {
		if (valorPagamento == null) {
			valorPagamento = 0.0;
		}
		return valorPagamento;
	}

	public void setValorPagamento(Double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public String getNumeroDocumento() {
		if (numeroDocumento == null) {
			numeroDocumento = "";
		}
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Date getDataRealPagamento() {
		return dataRealPagamento;
	}

	public void setDataRealPagamento(Date dataRealPagamento) {
		this.dataRealPagamento = dataRealPagamento;
	}

	public Double getValorRealPagamento() {
		if (valorRealPagamento == null) {
			valorRealPagamento = 0.0;
		}
		return valorRealPagamento;
	}

	public void setValorRealPagamento(Double valorRealPagamento) {
		this.valorRealPagamento = valorRealPagamento;
	}

	public String getInformacao2() {
		if (informacao2 == null) {
			informacao2 = "";
		}
		return informacao2;
	}

	public void setInformacao2(String informacao2) {
		this.informacao2 = informacao2;
	}

	public String getFinalidadeDoc() {
		if (finalidadeDoc == null) {
			finalidadeDoc = "";
		}
		return finalidadeDoc;
	}

	public void setFinalidadeDoc(String finalidadeDoc) {
		this.finalidadeDoc = finalidadeDoc;
	}

	public String getFinalidadeTed() {
		if (finalidadeTed == null) {
			finalidadeTed = "";
		}
		return finalidadeTed;
	}

	public void setFinalidadeTed(String finalidadeTed) {
		this.finalidadeTed = finalidadeTed;
	}

	public String getCodigoFinalidadeComplementar() {
		if (codigoFinalidadeComplementar == null) {
			codigoFinalidadeComplementar = "";
		}
		return codigoFinalidadeComplementar;
	}

	public void setCodigoFinalidadeComplementar(String codigoFinalidadeComplementar) {
		this.codigoFinalidadeComplementar = codigoFinalidadeComplementar;
	}

	public Integer getTipoInscricaoFavorecido() {
		if (tipoInscricaoFavorecido == null) {
			tipoInscricaoFavorecido = 0;
		}
		return tipoInscricaoFavorecido;
	}

	public void setTipoInscricaoFavorecido(Integer tipoInscricaoFavorecido) {
		this.tipoInscricaoFavorecido = tipoInscricaoFavorecido;
	}

	public Long getNumeroInscricaoFavorecido() {
		if (numeroInscricaoFavorecido == null) {
			numeroInscricaoFavorecido = 0L;
		}
		return numeroInscricaoFavorecido;
	}

	public void setNumeroInscricaoFavorecido(Long numeroInscricaoFavorecido) {
		this.numeroInscricaoFavorecido = numeroInscricaoFavorecido;
	}

	public String getLogradouroFavorecido() {
		if (logradouroFavorecido == null) {
			logradouroFavorecido = "";
		}
		return logradouroFavorecido;
	}

	public void setLogradouroFavorecido(String logradouroFavorecido) {
		this.logradouroFavorecido = logradouroFavorecido;
	}

	public String getNumeroEnderecoFavorecido() {
		if (numeroEnderecoFavorecido == null) {
			numeroEnderecoFavorecido = "";
		}
		return numeroEnderecoFavorecido;
	}

	public void setNumeroEnderecoFavorecido(String numeroEnderecoFavorecido) {
		this.numeroEnderecoFavorecido = numeroEnderecoFavorecido;
	}

	public String getComplementoEnderecoFavorecido() {
		if (complementoEnderecoFavorecido == null) {
			complementoEnderecoFavorecido = "";
		}
		return complementoEnderecoFavorecido;
	}

	public void setComplementoEnderecoFavorecido(String complementoEnderecoFavorecido) {
		this.complementoEnderecoFavorecido = complementoEnderecoFavorecido;
	}

	public String getBairroFavorecido() {
		if (bairroFavorecido == null) {
			bairroFavorecido = "";
		}
		return bairroFavorecido;
	}

	public void setBairroFavorecido(String bairroFavorecido) {
		this.bairroFavorecido = bairroFavorecido;
	}

	public String getCidadeFavorecido() {
		if (cidadeFavorecido == null) {
			cidadeFavorecido = "";
		}
		return cidadeFavorecido;
	}

	public void setCidadeFavorecido(String cidadeFavorecido) {
		this.cidadeFavorecido = cidadeFavorecido;
	}

	public String getCepFavorecido() {
		if (cepFavorecido == null) {
			cepFavorecido = "";
		}
		return cepFavorecido;
	}

	public void setCepFavorecido(String cepFavorecido) {
		this.cepFavorecido = cepFavorecido;
	}

	public String getEstadoFavorecido() {
		if (estadoFavorecido == null) {
			estadoFavorecido = "";
		}
		return estadoFavorecido;
	}

	public void setEstadoFavorecido(String estadoFavorecido) {
		this.estadoFavorecido = estadoFavorecido;
	}

	public String getDataVencimento_Apresentar() {
		if(Uteis.isAtributoPreenchido(getDataVencimento())){
			return Uteis.getData(getDataVencimento());	
		}else if(Uteis.isAtributoPreenchido(getDataPagamento())){
			return Uteis.getData(getDataPagamento());
		}
		return "";
		
	}
	
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Double getValorJuro() {
		if (valorJuro == null) {
			valorJuro = 0.0;
		}
		return valorJuro;
	}

	public void setValorJuro(Double valorJuro) {
		this.valorJuro = valorJuro;
	}

	public Double getValorMulta() {
		if (valorMulta == null) {
			valorMulta = 0.0;
		}
		return valorMulta;
	}

	public void setValorMulta(Double valorMulta) {
		this.valorMulta = valorMulta;
	}

	public String getHorarioEnvioTed() {
		if (horarioEnvioTed == null) {
			horarioEnvioTed = "";
		}
		return horarioEnvioTed;
	}

	public void setHorarioEnvioTed(String horarioEnvioTed) {
		this.horarioEnvioTed = horarioEnvioTed;
	}

	public String getCodigoHistoricoParaCredito() {
		if (codigoHistoricoParaCredito == null) {
			codigoHistoricoParaCredito = "";
		}
		return codigoHistoricoParaCredito;
	}

	public void setCodigoHistoricoParaCredito(String codigoHistoricoParaCredito) {
		this.codigoHistoricoParaCredito = codigoHistoricoParaCredito;
	}

	public String getTedInstituicaoFinanceira() {
		if (tedInstituicaoFinanceira == null) {
			tedInstituicaoFinanceira = "";
		}
		return tedInstituicaoFinanceira;
	}

	public void setTedInstituicaoFinanceira(String tedInstituicaoFinanceira) {
		this.tedInstituicaoFinanceira = tedInstituicaoFinanceira;
	}

	public String getNumeroISPB() {
		if (numeroISPB == null) {
			numeroISPB = "";
		}
		return numeroISPB;
	}

	public void setNumeroISPB(String numeroISPB) {
		this.numeroISPB = numeroISPB;
	}

	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getCodigoReceitaTributo() {
		if (codigoReceitaTributo == null) {
			codigoReceitaTributo = "";
		}
		return codigoReceitaTributo;
	}

	public void setCodigoReceitaTributo(String codigoReceitaTributo) {
		this.codigoReceitaTributo = codigoReceitaTributo;
	}

	public String getTipoIdentificacaoContribuinte() {
		if (tipoIdentificacaoContribuinte == null) {
			tipoIdentificacaoContribuinte = "";
		}
		return tipoIdentificacaoContribuinte;
	}

	public void setTipoIdentificacaoContribuinte(String tipoIdentificacaoContribuinte) {
		this.tipoIdentificacaoContribuinte = tipoIdentificacaoContribuinte;
	}

	public String getIdentificacaoContribuinte() {
		if (identificacaoContribuinte == null) {
			identificacaoContribuinte = "";
		}
		return identificacaoContribuinte;
	}

	public void setIdentificacaoContribuinte(String identificacaoContribuinte) {
		this.identificacaoContribuinte = identificacaoContribuinte;
	}

	public String getCodigoidentificacaoTributo() {
		if (codigoidentificacaoTributo == null) {
			codigoidentificacaoTributo = "";
		}
		return codigoidentificacaoTributo;
	}

	public void setCodigoidentificacaoTributo(String codigoidentificacaoTributo) {
		this.codigoidentificacaoTributo = codigoidentificacaoTributo;
	}

	public String getMesAnoCompetencia() {
		if (mesAnoCompetencia == null) {
			mesAnoCompetencia = "";
		}
		return mesAnoCompetencia;
	}

	public void setMesAnoCompetencia(String mesAnoCompetencia) {
		this.mesAnoCompetencia = mesAnoCompetencia;
	}

	public Date getPeriodoApuracao() {
		return periodoApuracao;
	}

	public void setPeriodoApuracao(Date periodoApuracao) {
		this.periodoApuracao = periodoApuracao;
	}

	public String getNumeroReferencia() {
		if (numeroReferencia == null) {
			numeroReferencia = "";
		}
		return numeroReferencia;
	}

	public void setNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
	}

	public Double getValorReceitaBrutaAcumulada() {
		if (valorReceitaBrutaAcumulada == null) {
			valorReceitaBrutaAcumulada = 0.0;
		}
		return valorReceitaBrutaAcumulada;
	}

	public void setValorReceitaBrutaAcumulada(Double valorReceitaBrutaAcumulada) {
		this.valorReceitaBrutaAcumulada = valorReceitaBrutaAcumulada;
	}

	public Double getPercentualReceitaBrutaAcumulada() {
		if (percentualReceitaBrutaAcumulada == null) {
			percentualReceitaBrutaAcumulada = 0.0;
		}
		return percentualReceitaBrutaAcumulada;
	}

	public void setPercentualReceitaBrutaAcumulada(Double percentualReceitaBrutaAcumulada) {
		this.percentualReceitaBrutaAcumulada = percentualReceitaBrutaAcumulada;
	}

	public Long getNumeroInscricaoEstadualFavorecido() {
		if (numeroInscricaoEstadualFavorecido == null) {
			numeroInscricaoEstadualFavorecido = 0L;
		}
		return numeroInscricaoEstadualFavorecido;
	}

	public void setNumeroInscricaoEstadualFavorecido(Long numeroInscricaoEstadualFavorecido) {
		this.numeroInscricaoEstadualFavorecido = numeroInscricaoEstadualFavorecido;
	}

	public Long getNumeroInscricaoMunicipalFavorecido() {
		if (numeroInscricaoMunicipalFavorecido == null) {
			numeroInscricaoMunicipalFavorecido = 0L;
		}
		return numeroInscricaoMunicipalFavorecido;
	}

	public void setNumeroInscricaoMunicipalFavorecido(Long numeroInscricaoMunicipalFavorecido) {
		this.numeroInscricaoMunicipalFavorecido = numeroInscricaoMunicipalFavorecido;
	}

	public String getAutenticacaoPagamento() {
		if (autenticacaoPagamento == null) {
			autenticacaoPagamento = "";
		}
		return autenticacaoPagamento;
	}

	public void setAutenticacaoPagamento(String autenticacaoPagamento) {
		this.autenticacaoPagamento = autenticacaoPagamento;
	}

	public String getProtocoloPagamento() {
		if (protocoloPagamento == null) {
			protocoloPagamento = "";
		}
		return protocoloPagamento;
	}

	public void setProtocoloPagamento(String protocoloPagamento) {
		this.protocoloPagamento = protocoloPagamento;
	}

	public String getNossoNumeroContaAgrupada() {
		if(nossoNumeroContaAgrupada == null ) {
			nossoNumeroContaAgrupada ="";
			}
		return nossoNumeroContaAgrupada;
	}

	public void setNossoNumeroContaAgrupada(String nossoNumeroContaAgrupada) {
		this.nossoNumeroContaAgrupada = nossoNumeroContaAgrupada;
	}

	
	

}
