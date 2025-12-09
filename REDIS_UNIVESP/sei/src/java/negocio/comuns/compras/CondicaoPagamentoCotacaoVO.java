package negocio.comuns.compras;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.utilitarias.Uteis;

public class CondicaoPagamentoCotacaoVO implements Serializable {

	private Integer nrParcela;
	private Date dataVencimento;
	private Double valorParcela;
	public static final long serialVersionUID = 1L;

	public Date getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = new Date();
		}
		return dataVencimento;
	}

	public String getDataVencimento_Apresentar() {
		return Uteis.getData(getDataVencimento());
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Integer getNrParcela() {
		if (nrParcela == null) {
			nrParcela = 0;
		}
		return nrParcela;
	}

	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
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
}
