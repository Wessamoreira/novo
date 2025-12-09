package negocio.comuns.faturamento.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;

public class NfeVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String idNfe;
    private String tpNF;
    private String nNF;
    private String serie;
    private String naturezaOperacao;
    private String dataEmissao;
    private String dataSaidaEntrada;
    private String cnpjEmit;
    private String nomeEmit;
    private String nomeFantasiaEmit;
    private String logradouroEmit;
    private String nrEmit;
    private String bairoEmit;
    private String municipioEmit;
    private String ufEmit;
    private String cepEmit;
    private String paisEmit;
    private String nomePaisEmit;
    private String foneEmit;
    private String inscricaoEstEmit;
    private String cnpjDest;
    private String cpfDest;
    private String nomeDest;
    private String logradouroDest;
    private String nrDest;
    private String bairroDest;
    private String municipioDest;
    private String ufDest;
    private String cepDest;
    private String paisDest;
    private String nomePaisDest;
    private String foneDest;
    private String inscricaoEstDest;
    private String modFreteTransp;
    private String valorFreteTransp;
    private String cnpjTransp;
    private String nomeTransp;
    private String inscricaoEstTransp;
    private String enderecoTransp;
    private String municipioTransp;
    private String ufTransp;
    private String placaVeicTransp;
    private String ufVeicTransp;
    private String rntcVeicTransp;
    private String quantidadeVolTransp;
    private String especieVolTransp;
    private String marcaVolTransp;
    private String numeracaoVolTransp;
    private String pesoLiquidoVolTransp;
    private String pesoBrutoVolTransp;
    private String observacao;
    private String modelo;
    private String codigoIBGEMunicipio;
    private String codigoIBGEMunicipioDest;
    private String tipoPessoaDest;
    private String valorTotal;
    private String valorTotalItem;
    private String totalBaseIcms;
    private String totalIcms;
    private String totalBaseIpi;
    private String totalIpi;
    private String totalBaseSubTrib;
    private String totalSubTrib;
    private String observacaoContribuinte;
    private String formaPagamento;
    private String tipoEmissao;
    private String tipoImpressao;
    private String crt;
    private String csosn;
    private String ambiente;
    private String finalidadeEmissao;
    private String processoEmissao;

    private List<ContaReceberNfeVO> contaReceberNfeVOs;
    private List<ItemNfeVO> itemNfeVOs;
    private String idLocalDestinoOperacao;
    private String indOperacaoConsumidorFinal;
    private String indPresencaComprador;
    
    private Double totalBaseIssqn;
	private Double totalIssqn;
	private Double totalIss;
	
	private String idLote;
	private String indSinc;
	
	private String inscricaoMunicipalEmit;
	
	private String codigoNaturezaOperacao;
	private String valorTotalPIS;
	private String valorTotalCOFINS;
	
	private String valorRetidoPIS;
	private String valorRetidoCOFINS;
	private String valorRetidoCSLL;
	private String baseCalculoIRRF;
	private String valorRetidoIRRF;
	private String baseCalculoRetencaoPrevidenciaSocial;
	private String valorRetidoPrevidenciaSocial;
	private Boolean possuiAliquotasEspecificasParceiro;
	private Boolean issRetido;
	private Double valorRetencaoIss;
	
	// Parametros auxiliares
	private String pastaArquivoXML;
	private String nomeArquivoXML;
	private String xml;
	private String protocolo;
	private String dataRecebimento;
	private String status;
	private String motivo;
	private String chaveAcesso;
	private String recibo;
	
	private String jsonEnvio;
	private String jsonRetornoEnvio;
	
    public NfeVO() {
        super();
        inicializarDados();
    }

    /**
     * Método Responsável por popular os dados do objeto que será enviado
     * à SEFAZ.
     */
//    public abstract void popularDados();

    private void inicializarDados() {
        setIdNfe("");
        setTpNF("");
        setNNF("");
        setSerie("");
        setNaturezaOperacao("");
        setDataEmissao("");
        setDataSaidaEntrada("");
        setCnpjEmit("");
        setNomeEmit("");
        setNomeFantasiaEmit("");
        setLogradouroEmit("");
        setNrEmit("");
        setBairoEmit("");
        setMunicipioEmit("");
        setUfEmit("");
        setCepEmit("");
        setPaisEmit("");
        setNomePaisEmit("");
        setFoneEmit("");
        setInscricaoEstEmit("");
        setCnpjDest("");
        setCpfDest("");
        setFormaPagamento("");
        setNomeDest("");
        setLogradouroDest("");
        setNrDest("");
        setBairroDest("");
        setMunicipioDest("");
        setUfDest("");
        setCepDest("");
        setPaisDest("");
        setNomePaisDest("");
        setFoneDest("");
        setInscricaoEstDest("");
        setModFreteTransp("");
        setValorFreteTransp("");
        setCnpjTransp("");
        setNomeTransp("");
        setInscricaoEstTransp("");
        setEnderecoTransp("");
        setMunicipioTransp("");
        setUfTransp("");
        setPlacaVeicTransp("");
        setUfVeicTransp("");
        setRntcVeicTransp("");
        setQuantidadeVolTransp("");
        setEspecieVolTransp("");
        setMarcaVolTransp("");
        setNumeracaoVolTransp("");
        setPesoLiquidoVolTransp("");
        setPesoBrutoVolTransp("");
        setObservacao("");
        setItemNfeVOs(new ArrayList<ItemNfeVO>(0));
        setCodigoIBGEMunicipio("");
        setCodigoIBGEMunicipioDest("");
        setTipoPessoaDest("");
        setObservacaoContribuinte("");
        setTipoEmissao("");
        setTipoImpressao("");
        setAmbiente("");
        setFinalidadeEmissao("");
        setProcessoEmissao("");
        setModelo("");
        setCsosn("");
        setContaReceberNfeVOs(new ArrayList<ContaReceberNfeVO>(0));
        setItemNfeVOs(new ArrayList<ItemNfeVO>(0));
        setJsonRetornoEnvio("");
        setJsonEnvio("");
    }

    public String getIdNfe() {
        return idNfe;
    }

    public void setIdNfe(String idNfe) {
        this.idNfe = idNfe;
    }

    public String getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(String naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataSaidaEntrada() {
        return dataSaidaEntrada;
    }

    public void setDataSaidaEntrada(String dataSaidaEntrada) {
        this.dataSaidaEntrada = dataSaidaEntrada;
    }

    public String getCnpjEmit() {
        return UteisNfe.mascaraCNPJ(cnpjEmit);
    }

    public void setCnpjEmit(String cnpjEmit) {
        this.cnpjEmit = cnpjEmit;
    }

    public String getNomeEmit() {
        return nomeEmit;
    }

    public void setNomeEmit(String nomeEmit) {
        this.nomeEmit = nomeEmit;
    }

    public String getNomeFantasiaEmit() {
        return nomeFantasiaEmit;
    }

    public void setNomeFantasiaEmit(String nomeFantasiaEmit) {
        this.nomeFantasiaEmit = nomeFantasiaEmit;
    }

    public String getLogradouroEmit() {
        return logradouroEmit;
    }

    public void setLogradouroEmit(String logradouroEmit) {
        this.logradouroEmit = logradouroEmit;
    }

    public String getNrEmit() {
        return nrEmit;
    }

    public void setNrEmit(String nrEmit) {
        this.nrEmit = nrEmit;
    }

    public String getBairoEmit() {
        return bairoEmit;
    }

    public void setBairoEmit(String bairoEmit) {
        this.bairoEmit = bairoEmit;
    }

    public String getMunicipioEmit() {
        return municipioEmit;
    }

    public void setMunicipioEmit(String municipioEmit) {
        this.municipioEmit = municipioEmit;
    }

    public String getUfEmit() {
        return ufEmit;
    }

    public void setUfEmit(String ufEmit) {
        this.ufEmit = ufEmit;
    }

    public String getCepEmit() {
        return cepEmit;
    }

    public void setCepEmit(String cepEmit) {
        this.cepEmit = cepEmit;
    }

    public String getPaisEmit() {
        return paisEmit;
    }

    public void setPaisEmit(String paisEmit) {
        this.paisEmit = paisEmit;
    }

    public String getFoneEmit() {
        return foneEmit;
    }

    public void setFoneEmit(String foneEmit) {
        this.foneEmit = foneEmit;
    }

    public String getInscricaoEstEmit() {
        return inscricaoEstEmit;
    }

    public void setInscricaoEstEmit(String inscricaoEstEmit) {
        this.inscricaoEstEmit = inscricaoEstEmit;
    }

    public String getCnpjDest() {
        return UteisNfe.mascaraCNPJ(cnpjDest);
    }

    public void setCnpjDest(String cnpjDest) {
        this.cnpjDest = cnpjDest;
    }

    public String getNomeDest() {
        return nomeDest;
    }

    public void setNomeDest(String nomeDest) {
        this.nomeDest = nomeDest;
    }

    public String getLogradouroDest() {
        return logradouroDest;
    }

    public void setLogradouroDest(String logradouroDest) {
        this.logradouroDest = logradouroDest;
    }

    public String getNrDest() {
        return nrDest;
    }

    public void setNrDest(String nrDest) {
        this.nrDest = nrDest;
    }

    public String getBairroDest() {
        return bairroDest;
    }

    public void setBairroDest(String bairroDest) {
        this.bairroDest = bairroDest;
    }

    public String getMunicipioDest() {
        return municipioDest;
    }

    public void setMunicipioDest(String municipioDest) {
        this.municipioDest = municipioDest;
    }

    public String getUfDest() {
        return ufDest;
    }

    public void setUfDest(String ufDest) {
        this.ufDest = ufDest;
    }

    public String getCepDest() {
        return cepDest;
    }

    public void setCepDest(String cepDest) {
        this.cepDest = cepDest;
    }

    public String getPaisDest() {
        return paisDest;
    }

    public void setPaisDest(String paisDest) {
        this.paisDest = paisDest;
    }
    public String getNomePaisDest() {
        return nomePaisDest;
    }

    public void setNomePaisDest(String nomePaisDest) {
        this.nomePaisDest = nomePaisDest;
    }

    public String getFoneDest() {
        return foneDest;
    }

    public void setFoneDest(String foneDest) {
        this.foneDest = foneDest;
    }

    public String getInscricaoEstDest() {
    	if (inscricaoEstDest==null) {
			inscricaoEstDest="";
		}
        return inscricaoEstDest;
    }

    public void setInscricaoEstDest(String inscricaoEstDest) {
        this.inscricaoEstDest = inscricaoEstDest;
    }
    public String getValorFreteTransp() {
        return valorFreteTransp;
    }

    public void setValorFreteTransp(String valorFreteTransp) {
        this.valorFreteTransp = valorFreteTransp;
    }

    public String getCnpjTransp() {
        return UteisNfe.mascaraCNPJ(cnpjTransp);
    }

    public void setCnpjTransp(String cnpjTransp) {
        this.cnpjTransp = cnpjTransp;
    }

    public String getNomeTransp() {
        return nomeTransp;
    }

    public void setNomeTransp(String nomeTransp) {
        this.nomeTransp = nomeTransp;
    }

    public String getInscricaoEstTransp() {
        return inscricaoEstTransp;
    }

    public void setInscricaoEstTransp(String inscricaoEstTransp) {
        this.inscricaoEstTransp = inscricaoEstTransp;
    }

    public String getEnderecoTransp() {
        return enderecoTransp;
    }

    public void setEnderecoTransp(String enderecoTransp) {
        this.enderecoTransp = enderecoTransp;
    }

    public String getMunicipioTransp() {
        return municipioTransp;
    }

    public void setMunicipioTransp(String municipioTransp) {
        this.municipioTransp = municipioTransp;
    }

    public String getUfTransp() {
        return ufTransp;
    }

    public void setUfTransp(String ufTransp) {
        this.ufTransp = ufTransp;
    }

    public String getPlacaVeicTransp() {
        return placaVeicTransp;
    }

    public void setPlacaVeicTransp(String placaVeicTransp) {
        this.placaVeicTransp = placaVeicTransp;
    }

    public String getUfVeicTransp() {
        return ufVeicTransp;
    }

    public void setUfVeicTransp(String ufVeicTransp) {
        this.ufVeicTransp = ufVeicTransp;
    }

    public String getQuantidadeVolTransp() {
        return quantidadeVolTransp;
    }

    public void setQuantidadeVolTransp(String quantidadeVolTransp) {
        this.quantidadeVolTransp = quantidadeVolTransp;
    }

    public String getEspecieVolTransp() {
        return especieVolTransp;
    }

    public void setEspecieVolTransp(String especieVolTransp) {
        this.especieVolTransp = especieVolTransp;
    }

    public String getMarcaVolTransp() {
        return marcaVolTransp;
    }

    public void setMarcaVolTransp(String marcaVolTransp) {
        this.marcaVolTransp = marcaVolTransp;
    }

    public String getNumeracaoVolTransp() {
        return numeracaoVolTransp;
    }

    public void setNumeracaoVolTransp(String numeracaoVolTransp) {
        this.numeracaoVolTransp = numeracaoVolTransp;
    }

    public String getPesoLiquidoVolTransp() {
        return pesoLiquidoVolTransp;
    }

    public void setPesoLiquidoVolTransp(String pesoLiquidoVolTransp) {
        this.pesoLiquidoVolTransp = pesoLiquidoVolTransp;
    }

    public String getPesoBrutoVolTransp() {
        return pesoBrutoVolTransp;
    }

    public void setPesoBrutoVolTransp(String pesoBrutoVolTransp) {
        this.pesoBrutoVolTransp = pesoBrutoVolTransp;
    }

    public String getModFreteTransp() {
        return modFreteTransp;
    }

    public void setModFreteTransp(String modFreteTransp) {
        this.modFreteTransp = modFreteTransp;
    }

    public List<ItemNfeVO> getItemNfeVOs() {
        return itemNfeVOs;
    }

    public void setItemNfeVOs(List<ItemNfeVO> itemNfeVOs) {
        this.itemNfeVOs = itemNfeVOs;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNNF2() {
        if (nNF.length() == 1) {
            return "000.000.00" + nNF;
        } else if (nNF.length() == 2) {
            return "000.000.0" + nNF;
        } else if (nNF.length() == 3) {
            return "000.000." + nNF;
        } else if (nNF.length() == 4) {
            return "000.00" + nNF.substring(0, 1) + "." + nNF.substring(1);
        } else if (nNF.length() == 5) {
            return "000.0" + nNF.substring(0, 2) + "." + nNF.substring(2);
        } else if (nNF.length() == 6) {
            return "000." + nNF.substring(0, 3) + "." + nNF.substring(3);
        } else if (nNF.length() == 7) {
            return "00" + nNF.substring(0, 1) + "." + nNF.substring(1, 4) + "." + nNF.substring(4);
        } else if (nNF.length() == 8) {
            return "0" + nNF.substring(0, 2) + "." + nNF.substring(2, 5) + "." + nNF.substring(5);
        } else {
            return nNF.substring(0, 3) + "." + nNF.substring(3, 5) + "." + nNF.substring(5);
        }
    }

    public void setNNF(String nNF) {
        this.nNF = nNF;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTpNF() {
        return tpNF;
    }

    public void setTpNF(String tpNF) {
        this.tpNF = tpNF;
    }

    public String getCodigoIBGEMunicipio() {
        return codigoIBGEMunicipio;
    }

    public void setCodigoIBGEMunicipio(String codigoIBGEMunicipio) {
        this.codigoIBGEMunicipio = codigoIBGEMunicipio;
    }

    public String getTipoPessoaDest() {
        return tipoPessoaDest;
    }

    public void setTipoPessoaDest(String tipoPessoaDest) {
        this.tipoPessoaDest = tipoPessoaDest;
    }

    public String getCodigoIBGEMunicipioDest() {
        return codigoIBGEMunicipioDest;
    }

    public void setCodigoIBGEMunicipioDest(String codigoIBGEMunicipioDest) {
        this.codigoIBGEMunicipioDest = codigoIBGEMunicipioDest;
    }

    public String getTotalBaseIcms() {
        return totalBaseIcms;
    }

    public void setTotalBaseIcms(String totalBaseIcms) {
        this.totalBaseIcms = totalBaseIcms;
    }

    public String getTotalBaseIpi() {
        return totalBaseIpi;
    }

    public void setTotalBaseIpi(String totalBaseIpi) {
        this.totalBaseIpi = totalBaseIpi;
    }

    public String getTotalBaseSubTrib() {
        return totalBaseSubTrib;
    }

    public void setTotalBaseSubTrib(String totalBaseSubTrib) {
        this.totalBaseSubTrib = totalBaseSubTrib;
    }

    public String getTotalIcms() {
        return totalIcms;
    }

    public void setTotalIcms(String totalIcms) {
        this.totalIcms = totalIcms;
    }

    public String getTotalIpi() {
        return totalIpi;
    }

    public void setTotalIpi(String totalIpi) {
        this.totalIpi = totalIpi;
    }

    public String getTotalSubTrib() {
        return totalSubTrib;
    }

    public void setTotalSubTrib(String totalSubTrib) {
        this.totalSubTrib = totalSubTrib;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorTotalItem() {
        return valorTotalItem;
    }

    public void setValorTotalItem(String valorTotalItem) {
        this.valorTotalItem = valorTotalItem;
    }

    public String getObservacaoContribuinte() {
        return observacaoContribuinte;
    }

    public void setObservacaoContribuinte(String observacaoContribuinte) {
        this.observacaoContribuinte = observacaoContribuinte;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public List<ContaReceberNfeVO> getContaReceberNfeVOs() {
        return contaReceberNfeVOs;
    }

    public void setContaReceberNfeVOs(List<ContaReceberNfeVO> contaReceberNfeVOs) {
        this.contaReceberNfeVOs = contaReceberNfeVOs;
    }

    public String getCpfDest() {
        return cpfDest;
    }

    public void setCpfDest(String cpfDest) {
        this.cpfDest = cpfDest;
    }

    public String getnNF() {
        return nNF;
    }

    public void setnNF(String nNF) {
        this.nNF = nNF;
    }

    public String getTipoEmissao() {
        return tipoEmissao;
    }

    public void setTipoEmissao(String tipoEmissao) {
        this.tipoEmissao = tipoEmissao;
    }

    public String getCrt() {
        return crt;
    }

    public void setCrt(String crt) {
        this.crt = crt;
    }

    public String getTipoImpressao() {
        return tipoImpressao;
    }

    public void setTipoImpressao(String tipoImpressao) {
        this.tipoImpressao = tipoImpressao;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getFinalidadeEmissao() {
        return finalidadeEmissao;
    }

    public void setFinalidadeEmissao(String finalidadeEmissao) {
        this.finalidadeEmissao = finalidadeEmissao;
    }

    public String getProcessoEmissao() {
        return processoEmissao;
    }

    public void setProcessoEmissao(String processoEmissao) {
        this.processoEmissao = processoEmissao;
    }

    public String getNomePaisEmit() {
        return nomePaisEmit;
    }

    public void setNomePaisEmit(String nomePaisEmit) {
        this.nomePaisEmit = nomePaisEmit;
    }

    public String getRntcVeicTransp() {
        return rntcVeicTransp;
    }

    public void setRntcVeicTransp(String rntcVeicTransp) {
        this.rntcVeicTransp = rntcVeicTransp;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCsosn() {
        return csosn;
    }

    public void setCsosn(String csosn) {
        this.csosn = csosn;
    }

	public String getIdLocalDestinoOperacao() {
		if (idLocalDestinoOperacao==null) {
			idLocalDestinoOperacao = "";
		}
		return idLocalDestinoOperacao;
	}

	public void setIdLocalDestinoOperacao(String idLocalDestinoOperacao) {
		this.idLocalDestinoOperacao = idLocalDestinoOperacao;
	}

	public String getIndOperacaoConsumidorFinal() {
		if (indOperacaoConsumidorFinal==null) {
			indOperacaoConsumidorFinal = "";
		}
		return indOperacaoConsumidorFinal;
	}

	public void setIdOperacaoConsumidorFinal(String indOperacaoConsumidorFinal) {
		this.indOperacaoConsumidorFinal = indOperacaoConsumidorFinal;
	}

	public String getIndPresencaComprador() {
		if (indPresencaComprador==null) {
			indPresencaComprador = "";
		}
		return indPresencaComprador;
	}

	public void setIndPresencaComprador(String indPresencaComprador) {
		this.indPresencaComprador = indPresencaComprador;
	}

	public Double getTotalIssqn() {
		if (totalIssqn == null) {
			totalIssqn = 0.0;
		}
		return totalIssqn;
	}

	public void setTotalIssqn(Double totalIssqn) {
		this.totalIssqn = totalIssqn;
	}

	public Double getTotalIss() {
		if (totalIss == null) {
			totalIss = 0.0;
		}
		return totalIss;
	}

	public void setTotalIss(Double totalIss) {
		this.totalIss = totalIss;
	}

	public String getIdLote() {
		if (idLote == null) {
			idLote = "";
		}
		return idLote;
	}

	public void setIdLote(String idLote) {
		this.idLote = idLote;
	}

	public String getIndSinc() {
		if (indSinc == null) {
			indSinc = "";
		}
		return indSinc;
	}

	public void setIndSinc(String indSinc) {
		this.indSinc = indSinc;
	}

	public Double getTotalBaseIssqn() {
		if (totalBaseIssqn == null) {
			totalBaseIssqn = 0.0;
		}
		return totalBaseIssqn;
	}

	public void setTotalBaseIssqn(Double totalBaseIssqn) {
		this.totalBaseIssqn = totalBaseIssqn;
	}

	public String getInscricaoMunicipalEmit() {
		if (inscricaoMunicipalEmit == null) {
			inscricaoMunicipalEmit = "";
		}
		return inscricaoMunicipalEmit;
	}

	public void setInscricaoMunicipalEmit(String inscricaoMunicipalEmit) {
		this.inscricaoMunicipalEmit = inscricaoMunicipalEmit;
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

	public String getValorTotalPIS() {
		if (valorTotalPIS == null) {
			valorTotalPIS = "";
		}
		return valorTotalPIS;
	}

	public void setValorTotalPIS(String valorTotalPIS) {
		this.valorTotalPIS = valorTotalPIS;
	}

	public String getValorTotalCOFINS() {
		if (valorTotalCOFINS == null) {
			valorTotalCOFINS = "";
		}
		return valorTotalCOFINS;
	}

	public void setValorTotalCOFINS(String valorTotalCOFINS) {
		this.valorTotalCOFINS = valorTotalCOFINS;
	}
	
	public String getValorRetidoPIS() {
		if (valorRetidoPIS == null) {
			valorRetidoPIS = "";
		}
		return valorRetidoPIS;
	}

	public void setValorRetidoPIS(String valorRetidoPIS) {
		this.valorRetidoPIS = valorRetidoPIS;
	}

	public String getValorRetidoCOFINS() {
		if (valorRetidoCOFINS == null) {
			valorRetidoCOFINS = "";
		}
		return valorRetidoCOFINS;
	}

	public void setValorRetidoCOFINS(String valorRetidoCOFINS) {
		this.valorRetidoCOFINS = valorRetidoCOFINS;
	}

	public String getValorRetidoCSLL() {
		if (valorRetidoCSLL == null) {
			valorRetidoCSLL = "";
		}
		return valorRetidoCSLL;
	}

	public void setValorRetidoCSLL(String valorRetidoCSLL) {
		this.valorRetidoCSLL = valorRetidoCSLL;
	}

	public String getBaseCalculoIRRF() {
		if (baseCalculoIRRF == null) {
			baseCalculoIRRF = "";
		}
		return baseCalculoIRRF;
	}

	public void setBaseCalculoIRRF(String baseCalculoIRRF) {
		this.baseCalculoIRRF = baseCalculoIRRF;
	}

	public String getValorRetidoIRRF() {
		if (valorRetidoIRRF == null) {
			valorRetidoIRRF = "";
		}
		return valorRetidoIRRF;
	}

	public void setValorRetidoIRRF(String valorRetidoIRRF) {
		this.valorRetidoIRRF = valorRetidoIRRF;
	}

	public String getBaseCalculoRetencaoPrevidenciaSocial() {
		if (baseCalculoRetencaoPrevidenciaSocial == null) {
			baseCalculoRetencaoPrevidenciaSocial = "";
		}
		return baseCalculoRetencaoPrevidenciaSocial;
	}

	public void setBaseCalculoRetencaoPrevidenciaSocial(String baseCalculoRetencaoPrevidenciaSocial) {
		this.baseCalculoRetencaoPrevidenciaSocial = baseCalculoRetencaoPrevidenciaSocial;
	}

	public String getValorRetidoPrevidenciaSocial() {
		if (valorRetidoPrevidenciaSocial == null) {
			valorRetidoPrevidenciaSocial = "";
		}
		return valorRetidoPrevidenciaSocial;
	}

	public void setValorRetidoPrevidenciaSocial(String valorRetidoPrevidenciaSocial) {
		this.valorRetidoPrevidenciaSocial = valorRetidoPrevidenciaSocial;
	}

	public Boolean getPossuiAliquotasEspecificasParceiro() {
		if (possuiAliquotasEspecificasParceiro == null) {
			possuiAliquotasEspecificasParceiro = false;
		}
		return possuiAliquotasEspecificasParceiro;
	}

	public void setPossuiAliquotasEspecificasParceiro(Boolean possuiAliquotasEspecificasParceiro) {
		this.possuiAliquotasEspecificasParceiro = possuiAliquotasEspecificasParceiro;
	}

	public Boolean getIssRetido() {
	    if(issRetido == null){
		issRetido = false;
	    }
	    return issRetido;
	}

	public void setIssRetido(Boolean issRetido) {
	    this.issRetido = issRetido;
	}

	public Double getValorRetencaoIss() {
	    if(valorRetencaoIss == null){
		valorRetencaoIss = 0.0;
	    }
	    return valorRetencaoIss;
	}

	public void setValorRetencaoIss(Double valorRetencaoIss) {
	    this.valorRetencaoIss = valorRetencaoIss;
	}
	
	public String getXml() {
		if (xml == null) {
			xml = "";
		}
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getProtocolo() {
		if (protocolo == null) {
			protocolo = "";
		}
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getDataRecebimento() {
		if (dataRecebimento == null) {
			dataRecebimento = "";
		}
		return dataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getStatus() {
		if (status == null) {
			status = "";
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getChaveAcesso() {
		if (chaveAcesso == null) {
			chaveAcesso = "";
		}
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	
	public String getPastaArquivoXML() {
		if (pastaArquivoXML == null) {
			pastaArquivoXML = "";
		}
		return pastaArquivoXML;
	}

	public void setPastaArquivoXML(String pastaArquivoXML) {
		this.pastaArquivoXML = pastaArquivoXML;
	}

	public String getNomeArquivoXML() {
		if (nomeArquivoXML == null) {
			nomeArquivoXML = "";
		}
		return nomeArquivoXML;
	}

	public void setNomeArquivoXML(String nomeArquivoXML) {
		this.nomeArquivoXML = nomeArquivoXML;
	}
	
	public String getRecibo() {
		if (recibo == null) {
			recibo = "";
		}
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}
	
	public String getJsonEnvio() {
		if(jsonEnvio == null) {
			jsonEnvio ="";
		}
		return jsonEnvio;
	}

	public void setJsonEnvio(String jsonEnvio) {
		this.jsonEnvio = jsonEnvio;
	}

	public String getJsonRetornoEnvio() {
		if(jsonRetornoEnvio == null) {
			jsonRetornoEnvio ="";
		}
		return jsonRetornoEnvio;
	}

	public void setJsonRetornoEnvio(String jsonRetornoEnvio) {
		this.jsonRetornoEnvio = jsonRetornoEnvio;
	}
	
}
