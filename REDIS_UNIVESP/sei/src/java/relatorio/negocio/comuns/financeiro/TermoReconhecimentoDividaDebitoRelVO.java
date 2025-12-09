/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.financeiro;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
public class TermoReconhecimentoDividaDebitoRelVO {
    
    private String dataVencimento;
    private String servico;
    private String parcela;
    private Double juro;
    private Double multa;
    private Double acrescimo;
    private Double valorFinal;
    private Double valor;
    private String nrDocumento;
    private Double valorDesconto;
    private String centroReceita;
    private Double valorDescontoConvenio;
    private String periodo;
    private String tipo;
    private Double valorIndiceReajustePorAtraso;

    public String getDataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = "";
        }
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getServico() {
        if (servico == null) {
            servico = "";
        }
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    /**
     * @return the juro
     */
    public Double getJuro() {
        if (juro == null) {
            juro = 0.0;
        }
        return juro;
    }

    /**
     * @param juro the juro to set
     */
    public void setJuro(Double juro) {
        this.juro = juro;
    }

    /**
     * @return the multa
     */
    public Double getMulta() {
        if (multa == null) {
            multa = 0.0;
        }
        return multa;
    }

    /**
     * @param multa the multa to set
     */
    public void setMulta(Double multa) {
        this.multa = multa;
    }

    /**
     * @return the acrescimo
     */
    public Double getAcrescimo() {
        if (acrescimo == null) {
            acrescimo = 0.0;
        }
        return acrescimo;
    }

    /**
     * @param acrescimo the acrescimo to set
     */
    public void setAcrescimo(Double acrescimo) {
        this.acrescimo = acrescimo;
    }

    /**
     * @return the valorFinal
     */
    public Double getValorFinal() {
        if (valorFinal == null) {
            valorFinal = 0.0;
        }
        return valorFinal;
    }

    /**
     * @param valorFinal the valorFinal to set
     */
    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
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

	public String getCentroReceita() {
		if (centroReceita == null) {
			centroReceita = "";
		}
		return centroReceita;
	}

	public void setCentroReceita(String centroReceita) {
		this.centroReceita = centroReceita;
	}

	public Double getValorDescontoConvenio() {
		if (valorDescontoConvenio == null) {
			valorDescontoConvenio = 0.0;
		}
		return valorDescontoConvenio;
	}

	public void setValorDescontoConvenio(Double valorDescontoConvenio) {
		this.valorDescontoConvenio = valorDescontoConvenio;
	}

	public String getPeriodo() {
		if (periodo == null) {
			periodo = "";
		}
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getValorIndiceReajustePorAtraso() {
		if (valorIndiceReajustePorAtraso == null) {
			valorIndiceReajustePorAtraso = 0.0;
		}
		return valorIndiceReajustePorAtraso;
	}

	public void setValorIndiceReajustePorAtraso(Double valorIndiceReajustePorAtraso) {
		this.valorIndiceReajustePorAtraso = valorIndiceReajustePorAtraso;
	}
}
