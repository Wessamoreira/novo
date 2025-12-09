package negocio.comuns.faturamento.nfe;

import java.io.Serializable;

public class ItemNfeVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String codigoProduto;
    private String nomeProduto;
    private String ncm;
    private String cfop;
    private String unidadeCom;
    private String quantidadeCom;
    private String valorUnidadeCom;
    private String valorProd;
    private String unidadeTributaria;
    private String quantidadeTributaria;
    private String valorUnidadeTributaria;
    private String valorBaseCalcImpostoIcms;
    private String valorImpostoIcms;
    private String valorImpostoIssqn;
    private String valorImpostoIpi;
    private String valorBaseCalcImpostoIpi;
    private String valorSubstituicaoTributaria;
    private String totalSubstituicaoTributaria;
    private String valorBaseSubstituicaoTributaria;
    private String valorBaseCalcImpostoIssqn;
    private String aliquotaIcms;
    private String aliquotaIpi;
    private String aliquotaIssqn;
    private String aliquotaReduzidaIcms;
    private String cst;
    private String cean;
    private String ceanTrib;
    private String origem;
    private String cstPIS;
    private String cstCOFINS;
    private String aliquotaPIS;
    private String aliquotaCOFINS;
    private String valorTotalPIS;
    private String valotTotalCOFINS;
    private String valorTotalRetencaoIss;

    /**
     * Construtor padrão da classe <code>ItemNotaFiscal</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ItemNfeVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    private void inicializarDados() {
        setCodigoProduto("");
        setNomeProduto("");
        setNcm("");
        setCfop("");
        setUnidadeCom("");
        setQuantidadeCom("");
        setValorUnidadeCom("");
        setValorProd("");
        setUnidadeTributaria("");
        setQuantidadeTributaria("");
        setValorUnidadeTributaria("");
        setValorBaseCalcImpostoIcms("0");
        setValorImpostoIcms("0");
        setAliquotaIcms("0");
        setAliquotaIpi("0");
        setAliquotaReduzidaIcms("0");
        setValorImpostoIpi("0");
        setValorBaseCalcImpostoIpi("0");
        setValorSubstituicaoTributaria("0");
        setTotalSubstituicaoTributaria("0");
        setValorBaseSubstituicaoTributaria("0");
        setCst("");
        setCean("");
        setCeanTrib("");
        setOrigem("");
    }
    
//    public Double getValorTotalPreco() {
//        return Double.parseDouble(getQuantidadeCom()) * Double.parseDouble(getValorProd());
//    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public String getUnidadeCom() {
        return unidadeCom;
    }

    public void setUnidadeCom(String unidadeCom) {
        this.unidadeCom = unidadeCom;
    }

    public String getQuantidadeCom() {
        return quantidadeCom;
    }

    public void setQuantidadeCom(String quantidadeCom) {
        this.quantidadeCom = quantidadeCom;
    }

    public String getValorUnidadeCom() {
        return valorUnidadeCom;
    }

    public void setValorUnidadeCom(String valorUnidadeCom) {
        this.valorUnidadeCom = valorUnidadeCom;
    }

    public String getValorProd() {
        return valorProd;
    }

    public void setValorProd(String valorProd) {
        this.valorProd = valorProd;
    }

    public String getUnidadeTributaria() {
        return unidadeTributaria;
    }

    public void setUnidadeTributaria(String unidadeTributaria) {
        this.unidadeTributaria = unidadeTributaria;
    }

    public String getQuantidadeTributaria() {
        return quantidadeTributaria;
    }

    public void setQuantidadeTributaria(String quantidadeTributaria) {
        this.quantidadeTributaria = quantidadeTributaria;
    }

    public String getValorUnidadeTributaria() {
        return valorUnidadeTributaria;
    }

    public void setValorUnidadeTributaria(String valorUnidadeTributaria) {
        this.valorUnidadeTributaria = valorUnidadeTributaria;
    }

    public String getValorBaseCalcImpostoIcms() {
        return valorBaseCalcImpostoIcms;
    }

    public void setValorBaseCalcImpostoIcms(String valorBaseCalcImpostoIcms) {
        this.valorBaseCalcImpostoIcms = valorBaseCalcImpostoIcms;
    }

    public String getValorImpostoIcms() {
        return valorImpostoIcms;
    }

    public void setValorImpostoIcms(String valorImpostoIcms) {
        this.valorImpostoIcms = valorImpostoIcms;
    }

    public String getAliquotaIcms() {
        return aliquotaIcms;
    }

    public void setAliquotaIcms(String aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public String getValorImpostoIpi() {
        return valorImpostoIpi;
    }

    public void setValorImpostoIpi(String valorImpostoIpi) {
        this.valorImpostoIpi = valorImpostoIpi;
    }

    public String getValorBaseCalcImpostoIpi() {
        return valorBaseCalcImpostoIpi;
    }

    public void setValorBaseCalcImpostoIpi(String valorBaseCalcImpostoIpi) {
        this.valorBaseCalcImpostoIpi = valorBaseCalcImpostoIpi;
    }

    public String getValorBaseSubstituicaoTributaria() {
        return valorBaseSubstituicaoTributaria;
    }

    public void setValorBaseSubstituicaoTributaria(String valorBaseSubstituicaoTributaria) {
        this.valorBaseSubstituicaoTributaria = valorBaseSubstituicaoTributaria;
    }

    public String getValorSubstituicaoTributaria() {
        return valorSubstituicaoTributaria;
    }

    public void setValorSubstituicaoTributaria(String valorSubstituicaoTributaria) {
        this.valorSubstituicaoTributaria = valorSubstituicaoTributaria;
    }

    public String getTotalSubstituicaoTributaria() {
        return totalSubstituicaoTributaria;
    }

    public void setTotalSubstituicaoTributaria(String totalSubstituicaoTributaria) {
        this.totalSubstituicaoTributaria = totalSubstituicaoTributaria;
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }

    public String getCean() {
        return cean;
    }

    public void setCean(String cean) {
        this.cean = cean;
    }

    public String getCeanTrib() {
        return ceanTrib;
    }

    public void setCeanTrib(String ceanTrib) {
        this.ceanTrib = ceanTrib;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getAliquotaReduzidaIcms() {
        return aliquotaReduzidaIcms;
    }

    public void setAliquotaReduzidaIcms(String aliquotaReduzidaIcms) {
        this.aliquotaReduzidaIcms = aliquotaReduzidaIcms;
    }

    public String getAliquotaIpi() {
        return aliquotaIpi;
    }

    public void setAliquotaIpi(String aliquotaIpi) {
        this.aliquotaIpi = aliquotaIpi;
    }

	public String getValorImpostoIssqn() {
		if (valorImpostoIssqn == null) {
			valorImpostoIssqn = "";
		}
		return valorImpostoIssqn;
	}

	public void setValorImpostoIssqn(String valorImpostoIssqn) {
		this.valorImpostoIssqn = valorImpostoIssqn;
	}

	public String getAliquotaIssqn() {
		if (aliquotaIssqn == null) {
			aliquotaIssqn = "";
		}
		return aliquotaIssqn;
	}

	public void setAliquotaIssqn(String aliquotaIssqn) {
		this.aliquotaIssqn = aliquotaIssqn;
	}

	public String getValorBaseCalcImpostoIssqn() {
		if (valorBaseCalcImpostoIssqn == null) {
			valorBaseCalcImpostoIssqn = "";
		}
		return valorBaseCalcImpostoIssqn;
	}

	public void setValorBaseCalcImpostoIssqn(String valorBaseCalcImpostoIssqn) {
		this.valorBaseCalcImpostoIssqn = valorBaseCalcImpostoIssqn;
	}

	public String getCstPIS() {
		if (cstPIS == null) {
			cstPIS = "";
		}
		return cstPIS;
	}

	public void setCstPIS(String cstPIS) {
		this.cstPIS = cstPIS;
	}

	public String getCstCOFINS() {
		if (cstCOFINS == null) {
			cstCOFINS = "";
		}
		return cstCOFINS;
	}

	public void setCstCOFINS(String cstCOFINS) {
		this.cstCOFINS = cstCOFINS;
	}

	public String getAliquotaPIS() {
		if (aliquotaPIS == null) {
			aliquotaPIS = "";
		}
		return aliquotaPIS;
	}

	public void setAliquotaPIS(String aliquotaPIS) {
		this.aliquotaPIS = aliquotaPIS;
	}

	public String getAliquotaCOFINS() {
		if (aliquotaCOFINS == null) {
			aliquotaCOFINS = "";
		}
		return aliquotaCOFINS;
	}

	public void setAliquotaCOFINS(String aliquotaCOFINS) {
		this.aliquotaCOFINS = aliquotaCOFINS;
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

	public String getValotTotalCOFINS() {
		if (valotTotalCOFINS == null) {
			valotTotalCOFINS = "";
		}
		return valotTotalCOFINS;
	}

	public void setValotTotalCOFINS(String valotTotalCOFINS) {
		this.valotTotalCOFINS = valotTotalCOFINS;
	}

	public String getValorTotalRetencaoIss() {
	    if(valorTotalRetencaoIss == null){
		valorTotalRetencaoIss = "";
	    }
	    return valorTotalRetencaoIss;
	}

	public void setValorTotalRetencaoIss(String valorTotalRetencaoIss) {
	    this.valorTotalRetencaoIss = valorTotalRetencaoIss;
	}
	
}