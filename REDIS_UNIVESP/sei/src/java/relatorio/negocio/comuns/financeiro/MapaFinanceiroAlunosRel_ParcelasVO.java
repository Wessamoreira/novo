package relatorio.negocio.comuns.financeiro;

import java.util.Date;

public class MapaFinanceiroAlunosRel_ParcelasVO {

    private String parcela;
    private Double valor;
    private Double ant1;
    private Double ant2;
    private Double ant3;
    private Double ant4;
    private String situacao;
    private Double valorPago;
    private Date dataVencimento;
    private String tipoOrigemParcela;
    private Double juro;
    private Double multa;
    private Double acrescimo;

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getAnt1() {
        return ant1;
    }

    public void setAnt1(Double ant1) {
        this.ant1 = ant1;
    }

    public Double getAnt2() {
        return ant2;
    }

    public void setAnt2(Double ant2) {
        this.ant2 = ant2;
    }

    public Double getAnt3() {
        return ant3;
    }

    public void setAnt3(Double ant3) {
        this.ant3 = ant3;
    }

    public Double getAnt4() {
        return ant4;
    }

    public void setAnt4(Double ant4) {
        this.ant4 = ant4;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getTipoOrigemParcela() {
		return tipoOrigemParcela;
	}
    
    public void setTipoOrigemParcela(String tipoOrigemParcela) {
		this.tipoOrigemParcela = tipoOrigemParcela;
	}

	public Double getJuro() {
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getMulta() {
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getAcrescimo() {
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}
}
