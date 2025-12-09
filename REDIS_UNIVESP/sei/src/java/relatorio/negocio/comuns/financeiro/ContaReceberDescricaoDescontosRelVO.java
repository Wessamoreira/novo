package relatorio.negocio.comuns.financeiro;

public class ContaReceberDescricaoDescontosRelVO {

	private String tipoDesconto;
	private String descricaoDesconto;
	private Double valorDescontoIntitucional;
	private Double valorDescontoConvenio;
	private Double valorOutrosDescontos;

	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "";
		}
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public String getDescricaoDesconto() {
		if (descricaoDesconto == null) {
			descricaoDesconto = "";
		}
		return descricaoDesconto;
	}

	public void setDescricaoDesconto(String descricaoDesconto) {
		this.descricaoDesconto = descricaoDesconto;
	}

	public Double getValorDescontoIntitucional() {
		return valorDescontoIntitucional;
	}

	public void setValorDescontoIntitucional(Double valorDescontoIntitucional) {
		this.valorDescontoIntitucional = valorDescontoIntitucional;
	}

	public Double getValorDescontoConvenio() {
		return valorDescontoConvenio;
	}

	public void setValorDescontoConvenio(Double valorDescontoConvenio) {
		this.valorDescontoConvenio = valorDescontoConvenio;
	}

	public Double getValorOutrosDescontos() {
		return valorOutrosDescontos;
	}

	public void setValorOutrosDescontos(Double valorOutrosDescontos) {
		this.valorOutrosDescontos = valorOutrosDescontos;
	}

}
