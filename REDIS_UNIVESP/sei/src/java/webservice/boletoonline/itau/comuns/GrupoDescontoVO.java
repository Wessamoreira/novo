package webservice.boletoonline.itau.comuns;

public class GrupoDescontoVO {

	private String data_desconto;
	private Integer tipo_desconto;
	private String valor_desconto;
	private String percentual_desconto;

	public String getData_desconto() {
		if(data_desconto == null) {
			data_desconto = "";
		}
		return data_desconto;
	}

	public void setData_desconto(String data_desconto) {
		this.data_desconto = data_desconto;
	}

	public Integer getTipo_desconto() {
		if(tipo_desconto == null) {
			tipo_desconto = 0;
		}
		return tipo_desconto;
	}

	public void setTipo_desconto(Integer tipo_desconto) {
		this.tipo_desconto = tipo_desconto;
	}

	public String getValor_desconto() {
		if(valor_desconto == null) {
			valor_desconto = "";
		}
		return valor_desconto;
	}

	public void setValor_desconto(String valor_desconto) {
		this.valor_desconto = valor_desconto;
	}

	public String getPercentual_desconto() {
		if(percentual_desconto == null) {
			percentual_desconto = "";
		}
		return percentual_desconto;
	}

	public void setPercentual_desconto(String percentual_desconto) {
		this.percentual_desconto = percentual_desconto;
	}

}
