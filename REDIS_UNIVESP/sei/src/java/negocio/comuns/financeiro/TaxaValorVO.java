package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class TaxaValorVO extends SuperVO implements Serializable {
	private Integer codigo;
	private Integer taxa;
	private Date data;
	private Double valor;
	private TaxaVO taxaVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getTaxa() {
		if (taxa == null) {
			taxa = 0;
		}
		return taxa;
	}

	public void setTaxa(Integer taxa) {
		this.taxa = taxa;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public TaxaVO getTaxaVO() {
		if (taxaVO == null) {
			taxaVO = new TaxaVO();
		}
		return taxaVO;
	}

	public void setTaxaVO(TaxaVO taxaVO) {
		this.taxaVO = taxaVO;
	}

}
